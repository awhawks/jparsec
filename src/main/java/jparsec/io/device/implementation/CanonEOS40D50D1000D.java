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

import javax.swing.JOptionPane;

import jparsec.graph.DataSet;
import jparsec.io.FileIO;
import jparsec.io.device.GenericCamera;
import jparsec.io.device.implementation.GPhotoCamera.CAMERA_ID;
import jparsec.io.device.implementation.GPhotoCamera.CAMERA_PARAMETER;
import jparsec.io.image.ImageHeaderElement;
import jparsec.math.Constant;
import jparsec.math.FastMath;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;
import jparsec.util.Translate;

/**
 * An implementation of the GenericCamera interface for a wide range of Canon EOS
 * DSLR cameras, including the 1000D, 1100D, 40D, 400D, 450D, 50D, 500D, 550D,
 * 5D Mark II, and maybe others, but not the most recent ones (60D/600D for instance).
 * This implementation is based on the gphoto2 project and should work on Unix OS
 * (Linux, Mac). Port of gphoto to Windows is available, but untested in this project.
 * @author T. Alonso Albi - OAN (Spain)
 */
public class CanonEOS40D50D1000D implements GenericCamera {

	@Override
	public boolean setISO(String iso) {
		try {
			gphoto.setParameter(CAMERA_PARAMETER.ISO, iso);
			return true;
		} catch (JPARSECException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean setExpositionTime(String time) {
		try {
			gphoto.setParameter(CAMERA_PARAMETER.SHUTTER_SPEED, time);
			return true;
		} catch (JPARSECException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean setResolutionMode(String mode) {
		try {
			gphoto.setParameter(CAMERA_PARAMETER.RESOLUTION, mode);
			return true;
		} catch (JPARSECException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean shotAndDownload(boolean keepInCamera) {
		shooting = true;
		try {
			// Control minimum time between shots
			if (lastShot != null) {
				TimeElement now = new TimeElement();
				try {
					int dt = (int) (minInterval - (now.astroDate.jd() - lastShot.astroDate.jd()) * Constant.SECONDS_PER_DAY);
					if (dt > 0.0) {
						System.out.println("Waiting "+dt+" seconds to allow the camera to cool down ...");
						Thread.sleep(dt * 1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			lastShot = new TimeElement();
			String t = getExpositionTime();
			if (t.equals(Translate.translate(1180))) t = ""+getCCDorBulbModeTime();
			double timeS = DataSet.getDoubleValueWithoutLimit(t);
			try { lastShot.add(timeS / Constant.SECONDS_PER_DAY); } catch (JPARSECException e) { e.printStackTrace(); }

			lastImage = gphoto.shotAndRetrieveImage();
			shooting = false;
			return true;
		} catch (JPARSECException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isShooting() {
		return gphoto.isShooting() || shooting;
	}

	@Override
	public boolean setCameraOrientation(double ang) {
		orientation = ang;
		return true;
	}

	@Override
	public double getCameraOrientation() {
		return orientation;
	}

	@Override
	public boolean setImageID(IMAGE_ID img) {
		id = img;
		return true;
	}

	@Override
	public IMAGE_ID getImageID() {
		return id;
	}

	@Override
	public String getPathOfLastDownloadedImage() {
		return lastImage;
	}

	@Override
	public boolean setDownloadDirectory(String path) {
		gphoto.setWorkingDirectory(path);
		return true;
	}

	@Override
	public boolean setFilter(FILTER filter) {
		// TODO: Specific support for filter wheels should be added here
		this.filter = filter;
		return true;
	}

	@Override
	public FILTER getFilter() {
		return filter;
	}

	@Override
	public String[] getPossibleISOs() {
		try {
			return gphoto.getConfig(CAMERA_PARAMETER.ISO);
		} catch (JPARSECException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String[] getPossibleResolutionModes() {
		try {
			return gphoto.getConfig(CAMERA_PARAMETER.RESOLUTION);
		} catch (JPARSECException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String[] getPossibleExpositionTimes() {
		try {
			return gphoto.getConfig(CAMERA_PARAMETER.SHUTTER_SPEED);
		} catch (JPARSECException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public CAMERA_MODEL getCameraModel() {
		return model;
	}

	@Override
	public void setCCDorBulbModeTime(int seconds) {
		gphoto.setBulbTime(seconds);
	}

	@Override
	public String getISO() {
		return gphoto.getParameter(CAMERA_PARAMETER.ISO);
	}

	@Override
	public String getExpositionTime() {
		return gphoto.getParameter(CAMERA_PARAMETER.SHUTTER_SPEED);
	}

	@Override
	public boolean isBulb() {
		return gphoto.isBulbMode();
	}

	@Override
	public String getResolutionMode() {
		return gphoto.getParameter(CAMERA_PARAMETER.RESOLUTION);
	}

	@Override
	public int getCCDorBulbModeTime() {
		return gphoto.getBulbTime();
	}

	@Override
	public boolean setAperture(String aperture) {
		try {
			gphoto.setParameter(CAMERA_PARAMETER.APERTURE, aperture);
			return true;
		} catch (JPARSECException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String getAperture() {
		return gphoto.getParameter(CAMERA_PARAMETER.APERTURE);
	}

	@Override
	public String[] getPossibleApertures() {
		try {
			return gphoto.getConfig(CAMERA_PARAMETER.APERTURE);
		} catch (JPARSECException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getCameraName() {
		return cameraName+" ("+gphoto.getPort().trim()+")";
	}

	@Override
	public double getWidthHeightRatio() {
		return 1.5;
	}

	@Override
	public double getInverseElectronicGain() {
		// TODO: more camera models !
		String iso = this.getISO();
		if (!DataSet.isDoubleFastCheck(iso)) return 0;
		if (cameraName.indexOf("40D") >= 0) return 0.84 * 400.0 / Double.parseDouble(iso);
		if (cameraName.indexOf("50D") >= 0) return 0.57 * 400.0 / Double.parseDouble(iso);
		if (cameraName.indexOf("5D") >= 0) return 3.99 * 400.0 / Double.parseDouble(iso);
		if (cameraName.indexOf("5D Mark II") >= 0) return 1.01 * 400.0 / Double.parseDouble(iso);
		if (cameraName.indexOf("20D") >= 0) return 3.1 * 400.0 / Double.parseDouble(iso);
		if (cameraName.indexOf("10D") >= 0) return 2.7 * 400.0 / Double.parseDouble(iso);
		if (cameraName.indexOf("350D") >= 0) return 2.56 * 400.0 / Double.parseDouble(iso);
		if (cameraName.indexOf("300D") >= 0) return 2.78 * 400.0 / Double.parseDouble(iso);
		if (cameraName.indexOf("400D") >= 0) return 2.74 * 400.0 / Double.parseDouble(iso);
		if (cameraName.indexOf("1000D") >= 0) return 2.83 * 400.0 / Double.parseDouble(iso);
		return 0;
	}

	@Override
	public double getSaturationLevelADUs() {
		if (cameraName.indexOf("40D") >= 0) return 12900;
		if (cameraName.indexOf("50D") >= 0) return 8700;
		if (cameraName.indexOf("5D") >= 0) return 15800;
		if (cameraName.indexOf("5D Mark II") >= 0) return 15500;
		return 0;
	}

	@Override
	public int getDepth() {
		if (cameraName.indexOf("20D") >= 0) return 12;
		if (cameraName.indexOf("10D") >= 0) return 12;
		if (cameraName.indexOf("350D") >= 0) return 12;
		if (cameraName.indexOf("300D") >= 0) return 12;
		if (cameraName.indexOf("400D") >= 0) return 12;
		return 14;
	}

	@Override
	public ImageHeaderElement[] getFitsHeaderOfLastImage() {
		int max = FastMath.multiplyBy2ToTheX(1, getDepth()) - 1;
		boolean raw = true;
		String li = getPathOfLastDownloadedImage();
		if (li != null && (li.endsWith(".jpg") || li.endsWith(".png"))) raw = false;
		ImageHeaderElement header[] = new ImageHeaderElement[] {
				new ImageHeaderElement("BITPIX", "32", "Bits per data value"),
				new ImageHeaderElement("NAXIS", "2", "Dimensionality"),
				new ImageHeaderElement("NAXIS1", "0", "Width"),
				new ImageHeaderElement("NAXIS2", "0", "Height"),
				new ImageHeaderElement("EXTEND", "T", "Extension permitted"),
				new ImageHeaderElement("MAXCOUNT", ""+max, "Max counts per pixel"),
				new ImageHeaderElement("ISO", getISO(), "ISO"),
				new ImageHeaderElement("TIME", getExpositionTime(), "Exposure time in s"),
				new ImageHeaderElement("MODE", getResolutionMode(), "Resolution mode"),
				new ImageHeaderElement("RAW", (""+raw), "True for raw mode"),
				new ImageHeaderElement("ANGLE", ""+getCameraOrientation(), "Camera orientation angle (radians)"),
				new ImageHeaderElement("INSTRUME", getCameraName(), "Camera name"),
				new ImageHeaderElement("CAM_MODEL", this.model.name(), "Camera model (driver)"),
				new ImageHeaderElement("APERTURE", getAperture(), "Aperture f/"),
				new ImageHeaderElement("DEPTH", ""+getDepth(), "Pixel depth in bits"),
				new ImageHeaderElement("GAIN", ""+getInverseElectronicGain(), "Gain e-/ADU"),
				new ImageHeaderElement("BULBTIME", ""+getCCDorBulbModeTime(), "Exposure time in bulb mode (s)"),
				new ImageHeaderElement("MAXADU", ""+getSaturationLevelADUs(), "Saturation level in ADUs"),
				new ImageHeaderElement("FILTER", getFilter().getFilterName(), "Filter name"),
				new ImageHeaderElement("BAYER", gphoto.getBayerMatrix(), "Bayer matrix, top-left and clockwise"),
				new ImageHeaderElement("IMGID", GenericCamera.IMAGE_IDS[getImageID().ordinal()], "Image id: Dark, Flat, On, Test, or Reduced")
		};
		return header;
	}

	@Override
	public void setMinimumIntervalBetweenShots(int seconds) {
		minInterval = seconds;
	}

	@Override
	public int getMinimumIntervalBetweenShots() {
		return minInterval;
	}

	private GPhotoCamera gphoto;
	private String cameraName;
	private CAMERA_MODEL model;
	private double orientation = 0;
	private IMAGE_ID id = IMAGE_ID.TEST;
	private String lastImage = null;
	private FILTER filter = FILTER.FILTER_IR_DSLR;
	private int minInterval = 0;
	private TimeElement lastShot = null;
	private boolean shooting = false;

	/**
	 * Constructor for a Canon EOS DSLR.
	 * @param cameraModel The camera model.
	 * @param port The port to use, or null to check for them.
	 * @throws JPARSECException If an error occurs.
	 */
	public CanonEOS40D50D1000D(CAMERA_MODEL cameraModel, String port) throws JPARSECException {
		this.model = cameraModel;

		try {
			String dir = FileIO.getTemporalDirectory();
			String out[] = GPhotoCamera.autoDetect();
			if (out == null || out.length == 0) throw new JPARSECException("No cameras detected.");

			gphoto = null;
			if (out.length == 1 || port != null) {
				if (port != null) {
					gphoto = new GPhotoCamera(CAMERA_ID.EOS40D, null, port, dir, false, null);
					cameraName = gphoto.getModel();
				} else {
					cameraName = out[0].substring(0, out[0].indexOf("usb:")).trim();
					gphoto = new GPhotoCamera(CAMERA_ID.EOS40D, null, dir, false);
				}
			} else {
				int s = JOptionPane.showOptionDialog(null,
						Translate.translate(1172),
						Translate.translate(1171), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
						out, out[0]);
				if (s >= 0) {
					gphoto = new GPhotoCamera(CAMERA_ID.EOS40D, out[s], dir, false);
					cameraName = out[s].substring(0, out[s].indexOf("usb:")).trim();
				}
			}
			if (gphoto == null) throw new JPARSECException("No camera initialized.");

		} catch (Exception exc) {
			if (exc instanceof JPARSECException) throw (JPARSECException) exc;
			throw new JPARSECException("An error occurred.", exc);
		}
	}
}
