/*
 * This file is part of JPARSEC library.
 * 
 * (C) Copyright 2006-2015 by T. Alonso Albi - OAN (Spain).
 *  
 * Project Info:  http://conga.oan.es/~alonso/jparsec/jparsec.html
 * 
 * JPARSEC library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JPARSEC library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */		
package jparsec.io.device.implementation;

import java.io.File;

import javax.swing.JOptionPane;

import jparsec.io.*;
import jparsec.util.*;
import jparsec.graph.*;

/**
 * A class to take control of digital cameras using gphoto2 library. This class has been tested to
 * work for gphoto2 version &gt;= 2.4.0, and Canon EOS 300D / 40D. Other cameras (400D, 450D, 500D, but
 * not 60D/600D) should work, as well as with any version of gphoto. Camera should be operated in
 * the 'M' (manual) mode.<P>
 * Possible issues found:<P>
 * 40D (all): Disable the automatic sleep/standby mode of the camera in the menu. After this mode is active
 * the camera will only wake up manually.<P>
 * Use manual focusing in case of shooting with lens. Automatic will work, but could freeze gphoto in 
 * case the objective is not opened.<P>
 * Don't use simultaneously the bulb mode and the option to maintain photos stored in the camera. In fact,
 * to store images in the camera seems not working, so they cannot be transferred to the PC. In less words,
 * don't maintain photos in the camera.<P>
 * 300D: The resolution parameter may not work, but can be left in the desired position manually.
 *  
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class GPhotoCamera {

	/**
	 * Holds the gphoto version running. Current supported version is 2.4.0 - 2.4.13.
	 * Checks for later version are necessary since the gphoto command line could change.
	 */
	public static String gphotoVersion = ""; // 2.4.0 or later

	/** This is the set of configuration parameters to be checked with gphoto2, like
	 * iso, shutter speed, and so on. Currently there are 4 elements: iso, shutter speed,
	 * aperture, and resolution/image format. To identify them the list of possible 
	 * configuration values for a given camera reported by gphoto should end by /...,
	 * where ... are the values set here. To check several values for a given property
	 * set those values separated by ,. An example is index 3, which is set to "resolution,imageformat",
	 * since currently gphoto uses imageformat, but in previous versions (or maybe old cameras
	 * like the 300D) it was resolution. */
	public static String DEFAULT_CONFIGS[] = new String[] {
		"iso", 
		"shutterspeed", 
		"aperture", 
		"resolution,imageformat" // resolution used in old versions of gphoto2/old EOS cameras
	};
	
	/** The set of possible configuration parameters. */
	public static enum CAMERA_PARAMETER {
		/** The ISO. */
		ISO,
		/** The shutter speed. */
		SHUTTER_SPEED,
		/** The aperture. */
		APERTURE,
		/** The resolution or image mode. */
		RESOLUTION
	}
	
	
	/**
	 * Holds if a copy of the images taken should be maintained in the camera. False (No)
	 * is the default recommended value.
	 */
	private boolean maintainCopyInCamera = false;
	/**
	 * Holds the working directory to download the photos to.
	 */
	private String workingDir = "";
	/**
	 * Holds the bayer matrix of this camera for raw images, from top-left corner 
	 * clockwise.
	 */
	private String BayerMatrix = "RGBG";
	/** Camera model. */
	private String model;
	/** Camera port. */
	private String port;
	/** Holds when the last shot was taken. */
	private long lastShotTime = -1;
	/** Holds the path to the latest image. */
	private String lastShotPath;
	/** Selects exposition time in bulb mode in seconds. */
	private int bulbTime = 60;
	/** If a shot is being taken or not. */
	private boolean isShooting = false;
	/** Detected configuration parameters. */
	private String cameraConfigs[];
	/** Values of the parameters. */
	private String cameraConfigsValues[];
	/** List of all possible values for the parameters. */
	private String cameraPossibleConfigsValues[][];
	
	/**
	 * Constructor for a camera with a Bayer matrix RGBG.
	 * @param name The name of the camera, or null to take the first available one.
	 * @param workingDir The working directory to download images to.
	 * @param copyInCamera True to maintain a copy of the image in the camera. False
	 * is strongly recommended.
	 * @throws JPARSECException If an error occurs.
	 */
	public GPhotoCamera(String name, String workingDir, boolean copyInCamera)
	throws JPARSECException {
		cameraConfigs = new String[DEFAULT_CONFIGS.length];
		cameraConfigsValues = new String[DEFAULT_CONFIGS.length];
		cameraPossibleConfigsValues = new String[DEFAULT_CONFIGS.length][];
		for (int i=0; i<DEFAULT_CONFIGS.length; i++) {
			cameraConfigs[i] = "";
			cameraConfigsValues[i] = "0";
		}
		if (!workingDir.endsWith(FileIO.getFileSeparator())) workingDir += FileIO.getFileSeparator();
		
		String cameras[] = GPhotoCamera.autoDetect();
		boolean ok = false;
		for (int i=0; i<cameras.length; i++)
		{
			int usb = cameras[i].indexOf("usb:");
			String model = cameras[i].substring(0, usb).trim();
			String port = cameras[i].substring(usb);
			if (name == null || name.equals("") || cameras[i].indexOf(name)>=0) {
				this.model = model;
				this.port = port;
				ok = this.checkConfig();
				break;
			}
		}
		if (!ok) throw new JPARSECException("The camera cannot be found or initialized properly. Please turn it off/on and check again.");
		maintainCopyInCamera = copyInCamera;
		this.workingDir = workingDir;
	}

	/**
	 * Constructor for a camera in a given port.
	 * @param name The name of the camera, or null to take the first available one.
	 * @param port The port of the camera, to instantiate two identical cameras in
	 * different ports.
	 * @param workingDir The working directory to download images to.
	 * @param copyInCamera True to maintain a copy of the image in the camera. False
	 * is strongly recommended.
	 * @param bayer Bayer matrix for this camera. Default is "RGBG", set to null to use
	 * this default value.
	 * @throws JPARSECException If an error occurs.
	 */
	public GPhotoCamera(String name, String port, String workingDir, boolean copyInCamera, String bayer)
	throws JPARSECException {
		cameraConfigs = new String[DEFAULT_CONFIGS.length];
		cameraConfigsValues = new String[DEFAULT_CONFIGS.length];
		cameraPossibleConfigsValues = new String[DEFAULT_CONFIGS.length][];
		for (int i=0; i<DEFAULT_CONFIGS.length; i++) {
			cameraConfigs[i] = "";
			cameraConfigsValues[i] = "0";
		}
		if (!workingDir.endsWith(FileIO.getFileSeparator())) workingDir += FileIO.getFileSeparator();
		
		String cameras[] = GPhotoCamera.autoDetect();
		boolean ok = false;
		for (int i=0; i<cameras.length; i++)
		{
			int usb = cameras[i].indexOf("usb:");
			String model = cameras[i].substring(0, usb).trim();
			String cport = cameras[i].substring(usb);
			boolean portOK = false, cameraOK = false;
			if (port == null || port.equals("")) {
				portOK = true;
			} else {
				if (port.indexOf(cport) >= 0) portOK = true;
			}
			if (name == null || name.equals("")) {
				cameraOK = true;
			} else {
				if (name.indexOf(model) >= 0) cameraOK = true;
			}
			if (portOK && cameraOK) {
				this.model = model;
				this.port = cport;
				ok = checkConfig();
				break;
			}
		}
		if (!ok) throw new JPARSECException("The camera cannot be found or initialized properly. Please turn it off/on and check again.");
		maintainCopyInCamera = copyInCamera;
		this.workingDir = workingDir;
		if (bayer != null) this.BayerMatrix = bayer;
	}

	private static void getVersion()
	throws JPARSECException {
		Process p = ApplicationLauncher.executeCommand("gphoto2 --version");
		String out = ApplicationLauncher.getConsoleOutputFromProcess(p);
		String array[] = DataSet.toStringArray(out, FileIO.getLineSeparator());
		gphotoVersion = FileIO.getField(2, array[0], " ", true);
		int index = DataSet.getIndexStartingWith(array, "libgphoto2");
		if (index > 0) gphotoVersion += " (libgphoto2 "+FileIO.getField(2, array[index], " ", true)+")";
	}

	/**
	 * Take a shot and download it.
	 * @throws JPARSECException If an error occurs.
	 * @return Name of downloaded image. In case there are 2 (jpg + raw),
	 * the path to both files are returned, separated by ,. For bulb mode,
	 * null is returned, but typical image name will be capt0000.jpg and/or
	 * capt0000.cr2.
	 */
	public String shotAndRetrieveImage()
	throws JPARSECException {
		ShotThread st = new ShotThread();
		st.start();
		
		do {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while(isShooting);
		
		return lastShotPath;
	}

	private String getFileName(String newFile) {
		int begin = newFile.indexOf("/");
		newFile = newFile.substring(begin);
		int end = newFile.indexOf(".");
		newFile = newFile.substring(0, end+4);
		int lastBar = newFile.lastIndexOf("/");
		newFile = newFile.substring(lastBar+1);
		return newFile;
	}
	private String getFolderName(String newFile) {
		int begin = newFile.indexOf("/");
		newFile = newFile.substring(begin);
		int end = newFile.indexOf(".");
		newFile = newFile.substring(0, end+4);
		int lastBar = newFile.lastIndexOf("/");
		return newFile.substring(0, lastBar+1);
	}
	private String getNewFileName(String newFile) {
		int begin = newFile.indexOf("/");
		return newFile.substring(0, begin).trim();
	}
	
	/**
	 * Auto detect current connected cameras.
	 * @return The list of cameras and ports available.
	 * @throws JPARSECException If an error occurs.
	 */
	public static String[] autoDetect()
	throws JPARSECException {
		if (gphotoVersion.equals("")) getVersion();
		Process p = ApplicationLauncher.executeCommand("gphoto2 --auto-detect");
		String out = ApplicationLauncher.getConsoleOutputFromProcess(p);
		String array[] = DataSet.toStringArray(out, FileIO.getLineSeparator());
		String cameras = "";
		for (int i=2; i<array.length; i++)
		{
			int usb = array[i].indexOf("usb:");
			String after = array[i].substring(usb+4).trim();
			if (!after.equals("")) {
				if (!cameras.equals("")) cameras += FileIO.getLineSeparator();
				cameras += array[i];
			}
		}
		return DataSet.toStringArray(cameras, FileIO.getLineSeparator());
	}
	
	/**
	 * Checks the camera by retrieving the list of possible configuration parameters
	 * and checking that list against the configuration parameters that should be available
	 * for configuration to do astronomical imaging.
	 * @return True is everything is fine.
	 * @throws JPARSECException If an error occurs.
	 */
	public boolean checkConfig()
	throws JPARSECException {
		String command = "gphoto2 --camera \""+this.model+"\" --port "+this.port+" --list-config";
		Process p = ApplicationLauncher.executeCommand(command);
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String out = ApplicationLauncher.getConsoleOutputFromProcess(p);
		String array[] = DataSet.toStringArray(out, FileIO.getLineSeparator());

		int n = 0;
		CAMERA_PARAMETER configParams[] = CAMERA_PARAMETER.values();
		boolean configParamFound[] = new boolean[configParams.length];
		for (int i=0; i<array.length; i++)
		{
			boolean anyFound = false;
			for (int j=0; j<configParamFound.length; j++) {
				configParamFound[j] = endsWith(array[i], configParams[j]);
				if (configParamFound[j]) {
					anyFound = true;
					break;
				}
			}
			if (anyFound) n ++;
		}
		boolean autoDetectionOfParametersSuccesful = false;
		if (n == configParams.length) {
			autoDetectionOfParametersSuccesful = true;
			for (int i=0; i<configParams.length; i++)
			{
				String values[] = getConfig(configParams[i]);
				cameraPossibleConfigsValues[i] = values;
			}
		}
		return autoDetectionOfParametersSuccesful;
	}

	private boolean endsWith(String line, CAMERA_PARAMETER c) {
		String values = DEFAULT_CONFIGS[c.ordinal()];
		String val[] = DataSet.toStringArray(values, ",");
		for (int i=0; i<val.length; i++) {
			if (line.endsWith("/"+val[i])) {
				cameraConfigs[c.ordinal()] = line;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Lists all configuration parameters. These parameters are automatically
	 * identified to detect which of them correspond to iso, shutter speed, image
	 * quality, and so on. However, this identification could fail since the name of
	 * the parameters are different from camera to camera. This method allows
	 * to obtain the full list as a previous step to modify {@link #DEFAULT_CONFIGS}
	 * by hand for a given camera.
	 * @return The list of configuration parameters.
	 * @throws JPARSECException If an error occurs.
	 */
	public String[] listAllConfig()
	throws JPARSECException {
		String command = "gphoto2 --camera \""+this.model+"\" --port "+this.port+" --list-config";
		Process p = ApplicationLauncher.executeCommand(command);
		String out = ApplicationLauncher.getConsoleOutputFromProcess(p);
		String array[] = DataSet.toStringArray(out, FileIO.getLineSeparator());

		StringBuffer config = new StringBuffer("");
		for (int i=1; i<array.length; i++)
		{
			config.append(array[i]);
		}
		
		String prop[] = DataSet.toStringArray(config.toString(), FileIO.getLineSeparator());
		return prop;
	}

	/**
	 * Returns the possible values of a given configuration parameter.
	 * @param config The configuration parameter.
	 * @return List of possible values. The index of the value in the output
	 * array is the value to be passed to the gphoto command, although this is
	 * done automatically.
	 * @throws JPARSECException If an error occurs.
	 */
	public String[] getConfig(CAMERA_PARAMETER config)
	throws JPARSECException {
		String[] values = cameraPossibleConfigsValues[config.ordinal()];
		if (values != null) return values;
		
		String command = "gphoto2 --camera \""+this.model+"\" --port "+this.port+" --get-config "+cameraConfigs[config.ordinal()];
		Process p = ApplicationLauncher.executeCommand(command);
		String out = ApplicationLauncher.getConsoleOutputFromProcess(p);
		String array[] = DataSet.toStringArray(out, FileIO.getLineSeparator());
		
		StringBuffer choices = new StringBuffer("");
		for (int i=0; i<array.length; i++)
		{
			int c= array[i].indexOf("Choice:");
			if (c >= 0) {
				if (!choices.equals("")) choices.append(FileIO.getLineSeparator());
				String newChoice = array[i].substring(c+7).trim();
				newChoice = newChoice.substring(newChoice.indexOf(" ")).trim();
				choices.append(newChoice);
			}
		}
		return DataSet.toStringArray(choices.toString(), FileIO.getLineSeparator());		
	}

	/**
	 * Sets the value of a given configuration parameter.
	 * @param c The configuration parameter.
	 * @param value The value as an integer.
	 * @throws JPARSECException If an error occurs.
	 */
	public void setParameter(CAMERA_PARAMETER c, String value)
	throws JPARSECException {	
		int cv = DataSet.getIndex(this.cameraPossibleConfigsValues[c.ordinal()], value);
		if (cv == -1) throw new JPARSECException("Value "+value+" for parameter "+c.name()+" is not available.");
		cameraConfigsValues[c.ordinal()] = ""+cv;
	}

	/**
	 * Returns the current selected value for a given parameter.
	 * @param c The parameter.
	 * @return The current selected value.
	 */
	public String getParameter(CAMERA_PARAMETER c) {
		return cameraPossibleConfigsValues[c.ordinal()][Integer.parseInt(cameraConfigsValues[c.ordinal()])];
	}
	
	/**
	 * Set the exposition time for bulb mode.
	 * @param seconds Time in seconds.
	 */
	public void setBulbTime(int seconds) {
		this.bulbTime = seconds;
	}

	/**
	 * Returns the exposition time for bulb mode.
	 * @return Time in seconds.
	 */
	public int getBulbTime() {
		return this.bulbTime;
	}

	/**
	 * Returns the Bayer matrix of this camera for raw images, from 
	 * top-left corner clockwise.
	 * @return Bayer matrix.
	 */
	public String getBayerMatrix() {
		return BayerMatrix;
	}
	/**
	 * Returns the camera model.
	 * @return Camera model.
	 */
	public String getModel()
	{
		return this.model;
	}
	/**
	 * Returns the camera port.
	 * @return Camera port.
	 */
	public String getPort()
	{
		return this.port;
	}
	/**
	 * Return time since last shot.
	 * @return Time lapse in seconds, or -1 if no first shot exists.
	 */
	public int lapseSinceLastShot()
	{
		if (this.lastShotTime == -1) return -1;
		int lapse = (int) (0.5 + (double) (System.currentTimeMillis() - this.lastShotTime) / 1000.0);
		return lapse;
	}

	/**
	 * Returns the current working directory where
	 * images are downloaded from the camera.
	 * @return Working directory.
	 */
	public String getWorkingDirectory() {
		return this.workingDir;
	}
	/**
	 * Sets the working directory to download images.
	 * @param dir New working directory.
	 */
	public void setWorkingDirectory(String dir) {
		this.workingDir = dir;
	}
	/**
	 * Returns if a shot is being taken.
	 * @return True or false.
	 */
	public boolean isShooting() {
		return isShooting;
	}
	/**
	 * Returns if the camera is in bulb mode or not.
	 * @return True or false.
	 */
	public boolean isBulbMode() {
		return cameraPossibleConfigsValues[CAMERA_PARAMETER.SHUTTER_SPEED.ordinal()][Integer.parseInt(cameraConfigsValues[CAMERA_PARAMETER.SHUTTER_SPEED.ordinal()])].startsWith("b");
	}
	
	/**
	 * Clones this object.
	 */
	public Object clone()
	{
		GPhotoCamera cam = null;
		try {
			cam = new GPhotoCamera(this.model, this.port, this.workingDir, this.maintainCopyInCamera, this.BayerMatrix);
			cam.cameraConfigsValues = this.cameraConfigsValues.clone();
			cam.cameraConfigs = this.cameraConfigs.clone();
		} catch (Exception exc) {}
		return cam;
	}
	
	class ShotThread extends Thread {
		@Override
		public void run() {
			isShooting = true;
			try {
				if (!isStillConnected(true)) throw new JPARSECException("Camera "+model+" is no longer detected on port "+port.trim()+"!");
				
				if (lastShotPath != null) {
					String val[] = DataSet.toStringArray(lastShotPath, ",");
					for (int i=0; i<val.length; i++) {
						File file = new File(val[i]);
						if (file.exists()) file.delete();
					}
				}
				File file = new File(workingDir + "capt0000.jpg");
				if (file.exists()) file.delete();			
				File file2 = new File(workingDir + "capt0000.cr2");
				if (file2.exists()) file2.delete();			
				File file3 = new File(workingDir + "capt0000.nef");
				if (file3.exists()) file3.delete();			
				
				String configCommand = "";
		
				for (int i=0; i<cameraConfigs.length; i++)
				{
					configCommand += "--set-config "+cameraConfigs[i]+"="+cameraConfigsValues[i]+" ";
				}
		
				String filesBefore[] = FileIO.getFiles(workingDir);
				boolean bulbMode = isBulbMode();
				String command = "";
				if (bulbMode) {
					command = "gphoto2 --camera \""+model+"\" --port "+port+" "+configCommand;
					Process p = ApplicationLauncher.executeCommand(command, null, new File(workingDir));
					String out = ApplicationLauncher.getConsoleOutputFromProcess(p);
					System.out.println(out);
					command = "gphoto2 --camera \""+model+"\" --port "+port+" --set-config bulb=1 --wait-event="+bulbTime+"s --set-config bulb=0 --wait-event";
					if (!maintainCopyInCamera) command +="-and-download";
					command += "=5s";
				} else {
					command = "gphoto2 --camera \""+model+"\" --port "+port+" -F1 -I 1 "+configCommand+"--capture-image";
					if (!maintainCopyInCamera) command +="-and-download";
				}
				Process p = ApplicationLauncher.executeCommand(command, null, new File(workingDir));
				String out = ApplicationLauncher.getConsoleOutputFromProcess(p);
				String array[] = DataSet.toStringArray(out, FileIO.getLineSeparator());
		
				if (!bulbMode && maintainCopyInCamera) {
					// get downloaded file name
					String newFile = getFileName(array[2]), newFile2 = null;
					String line = getNewFileName(array[2]);
					String sa[] = DataSet.getSubArray(array, 3, array.length-1);
					int index = DataSet.getIndexStartingWith(sa, line);
					if (index >= 0) newFile2 = getFileName(sa[index]);
					lastShotPath = workingDir + newFile;
					if (newFile2 != null) lastShotPath += "," + workingDir + newFile2; 

					String folder = getFolderName(array[2]);
					p = ApplicationLauncher.executeCommand("gphoto2 --camera \""+model+"\" --port "+port+" --folder "+folder+" --list-files");
					out = ApplicationLauncher.getConsoleOutputFromProcess(p);
					array = DataSet.toStringArray(out, FileIO.getLineSeparator());
					index = -1;
					int index2 = -1;
					for (int i=0; i<array.length; i++)
					{
						if (array[i].indexOf(newFile) >= 0) {
							String fieldNum = FileIO.getField(1, array[i], " ", true);
							index = Integer.parseInt(fieldNum.substring(1));
						}
						if (array[i].indexOf(newFile2) >= 0) {
							String fieldNum = FileIO.getField(1, array[i], " ", true);
							index2 = Integer.parseInt(fieldNum.substring(1));
						}
					}
					
					if (index > 0) {
						p = ApplicationLauncher.executeCommand(
								"gphoto2 --camera \""+model+"\" --port "+port+" --folder "+folder+" --get-file "+index+"-"+index,
								null, new File( workingDir));
						out = ApplicationLauncher.getConsoleOutputFromProcess(p);
						array = DataSet.toStringArray(out, FileIO.getLineSeparator());
					}
					if (index2 > 0) {
						p = ApplicationLauncher.executeCommand(
								"gphoto2 --camera \""+model+"\" --port "+port+" --folder "+folder+" --get-file "+index2+"-"+index2,
								null, new File( workingDir));
						out = ApplicationLauncher.getConsoleOutputFromProcess(p);
						array = DataSet.toStringArray(out, FileIO.getLineSeparator());
					}
					if (index < 0 && index2 < 0) {
						String img = newFile;
						if (newFile2 != null) img +=", "+newFile2;
						throw new JPARSECException("Could not find image "+img+" in folder "+folder+". This is a known problem, please retry without maintaining images on the camera.");
					}
				}
				
				String filesAfter[] = FileIO.getFiles(workingDir);
				lastShotPath = "";
				for (int i=0; i<filesAfter.length; i++) {
					if (DataSet.getIndex(filesBefore, filesAfter[i]) < 0)
						lastShotPath += filesAfter[i]+",";
				}
				if (lastShotPath.equals("")) {
					lastShotPath = null;
				} else {
					lastShotPath = lastShotPath.substring(0, lastShotPath.length()-1);
				}
				lastShotTime = System.currentTimeMillis();
			} catch (Exception exc) {
				exc.printStackTrace();
				JOptionPane.showMessageDialog(null, Translate.translate(1177) + DataSet.toString(JPARSECException.toStringArray(exc.getStackTrace()), FileIO.getLineSeparator()), 
						Translate.translate(230), JOptionPane.WARNING_MESSAGE);
				lastShotPath = null;
			}
			isShooting = false;
		}
	}

	/**
	 * Returns if the camera is still connected to the same port.
	 * @param fix True to fix the port value in case the camera is attached to the
	 * same usb, but suffered a switch off/on, so that the usb identifier has changed
	 * despite of being connected to the same port.
	 * @return True or false.
	 * @throws JPARSECException If an error occurs.
	 */
	public boolean isStillConnected(boolean fix) throws JPARSECException {
		String cameras[] = GPhotoCamera.autoDetect();
		boolean ok = false;
		String newPort = null;
		for (int i=0; i<cameras.length; i++)
		{
			int usb = cameras[i].indexOf("usb:");
			String model = cameras[i].substring(0, usb).trim();
			String port = cameras[i].substring(usb);
			
			if (model.equals(this.model) && port.equals(this.port)) {
				ok = true;
				break;
			} else {
				if (fix && model.equals(this.model)) {
					int c = port.indexOf(",");
					if (c > 0 && this.port.startsWith(port.substring(0, c+1))) {
						newPort = port;
					}
				}
			}
		}
		if (newPort != null) {
			this.port = newPort;
			ok = true;
		}
		return ok;
	}
}
