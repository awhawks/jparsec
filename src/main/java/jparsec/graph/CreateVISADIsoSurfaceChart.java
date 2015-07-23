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
package jparsec.graph;

import visad.*;
import visad.java3d.*;
import visad.util.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jparsec.astrophysics.gildas.LMVCube;
import jparsec.graph.VISADCubeElement;
import jparsec.io.Serialization;
import jparsec.math.Constant;
import jparsec.util.JPARSECException;
import jparsec.util.Logger;
import jparsec.util.Translate;
import jparsec.util.Logger.LEVEL;

/**
 * A class to show cubes of data in iso-surface mode using VISAD.
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class CreateVISADIsoSurfaceChart implements DisplayListener, Serializable {

	static final long serialVersionUID = 1;
	
	  /**
	   * Complete panel.
	   */
	public JPanel panel;
	private boolean displaysAreLinked = true;
	
	private RealType rightAscension, declination, velocity;

	/**
	 * Holds the displays.
	 */
	private DisplayImpl[] displays;
	
	/**
	 * The input cubes.
	 */
	public VISADCubeElement cube, cube2;
	private String path[];
	
	/**
	 * Display panel.
	 */
	public JPanel dispPanel;
	
	/**
	 * Constructor for a standard VISAD datacube using right ascension,
	 * declination, and velocity.
	 * @param cube The cube to show.
	 * @param title The title of the window.
	 * @throws JPARSECException If an exception occurs.
	 */
	public CreateVISADIsoSurfaceChart (VISADCubeElement cube, String title)
    throws JPARSECException {
		panel = new JPanel();
		panel.setName(title);
	    start(cube);
	}

	/**
	 * Constructor for one lmv file using right ascension,
	 * declination, and velocity.
	 * @param cube The cube to show.
	 * @param title The title of the window.
	 * @throws JPARSECException If an exception occurs.
	 */
	public CreateVISADIsoSurfaceChart (String cube, String title)
    throws JPARSECException {
		panel = new JPanel();
		panel.setName(title);
		this.cube = new VISADCubeElement(cube);
		path = new String[] {cube};
	    start(this.cube);
	}

	/**
	 * Constructor for two VISAD datacubes using right ascension,
	 * declination, and velocity.
	 * @param cube The first cube to show (observations).
	 * @param cube2 The second cube to show (model).
	 * @param title The title of the window.
	 * @throws JPARSECException If an exception occurs.
	 */
	public CreateVISADIsoSurfaceChart (VISADCubeElement cube, VISADCubeElement cube2, String title)
    throws JPARSECException {
		panel = new JPanel();
		panel.setName(title);
	    start2(cube, cube2);
	}

	/**
	 * Constructor for two lmv files using right ascension,
	 * declination, and velocity.
	 * @param cube The first cube to show (observations).
	 * @param cube2 The second cube to show (model).
	 * @param title The title of the window.
	 * @throws JPARSECException If an exception occurs.
	 */
	public CreateVISADIsoSurfaceChart (String cube, String cube2, String title)
    throws JPARSECException {
		panel = new JPanel();
		panel.setName(title);
		this.cube = new VISADCubeElement(cube);
		this.cube2 = new VISADCubeElement(cube2);
		path = new String[] {cube, cube2};
	    start2(this.cube, this.cube2);
	}

	/**
	 * Returns the title.
	 * @return The title.
	 */
	public String getTitle()  {
		return panel.getName();
	}

	/**
	 * Returns the path to the input lmv file/s.
	 * @return The path/s, or null if they were created from scratch.
	 */
	public String[] getFiles() {
		return path;
	}
	
	private void start(VISADCubeElement cube)
	throws JPARSECException{
		try {
			// Create the quantities
		    rightAscension = cube.rightAscension;
		    declination = cube.declination;
		    velocity = cube.velocity;
		    this.cube = cube;
		    init();
		} catch (RemoteException exc)
		{
			throw new JPARSECException("remote exception.", exc);
		}
		catch (VisADException ex)
		{
			throw new JPARSECException("VisAD exception.", ex);		  
		}
	}

	private void start2(VISADCubeElement cube, VISADCubeElement cube2)
	throws JPARSECException{
		try {
			// Create the quantities
		    rightAscension = cube.rightAscension;
		    declination = cube.declination;
		    velocity = cube.velocity;
		    this.cube = cube;
		    this.cube2 = cube2;
		    if (cube2 == null) {
		    	init();
		    } else {
		    	init2();
		    }
		} catch (RemoteException exc)
		{
			throw new JPARSECException("remote exception.", exc);
		}
		catch (VisADException ex)
		{
			throw new JPARSECException("VisAD exception.", ex);		  
		}
	}

    private void init() throws RemoteException, VisADException {
	    // Create the Displays and their maps
	    displays = new DisplayImpl[] { new DisplayImplJ3D("display")};
	
	    // Get display's graphics mode control draw scales
	    for( int i = 0; i<1;i++){
	      GraphicsModeControl dispGMC = (GraphicsModeControl) displays[i].getGraphicsModeControl();
	      dispGMC.setScaleEnable(true);
	    }
	
	    displays[0].getGraphicsModeControl().setTextureEnable(false);
	
	    RealType ir_radiance = RealType.getRealType(Translate.translate(942)); // "Iso_surface_level");
	    RealType index = RealType.getRealType("index");
	    RealType[] types = {declination, rightAscension, velocity, ir_radiance};
	    RealTupleType radiance = new RealTupleType(types);
	    FunctionType image_tuple = new FunctionType(index, radiance);
	    
	    Set domain_set = new Integer1DSet(cube.getNColumns()*cube.getNLevels()*cube.getNRows());
	    FlatField imaget1 = new FlatField(image_tuple, domain_set);
	    try {
			imaget1.setSamples(getSamples2(cube.getCube()));
		} catch (Exception exc) {
			Logger.log(LEVEL.ERROR, "Error setting the samples. Message was: "+exc.getLocalizedMessage()+". Trace: "+JPARSECException.getTrace(exc.getStackTrace()));
		}
		displays[0].addMap(new ScalarMap(declination, Display.YAxis));
	    displays[0].addMap(new ScalarMap(rightAscension, Display.XAxis));
	    displays[0].addMap(new ScalarMap(velocity, Display.ZAxis));
	    displays[0].addMap(new ScalarMap(ir_radiance, Display.Green));
	    ScalarMap map1contour = new ScalarMap(ir_radiance, Display.IsoContour);
	    displays[0].addMap(map1contour);
	    DataReferenceImpl ref_imaget1 = new DataReferenceImpl("ref_imaget1");
	    ref_imaget1.setData(imaget1);
	    displays[0].addReference(ref_imaget1, null);
	    
	    // Create application window, put display into it
	    panel.setLayout(new BorderLayout());
	
	    dispPanel = new JPanel( new GridLayout(1,2) );
	    dispPanel.add(displays[0].getComponent());
	    panel.add(dispPanel, BorderLayout.CENTER);
	
	    ContourWidget cw = new ContourWidget(map1contour);
	    JPanel panels = new JPanel();
	    panels.setLayout(new GridLayout(2, 1));
	
	    for (int i=0; i<cw.getComponentCount(); i++) {
	    	if (cw.getComponent(i).getClass().getName().equals("javax.swing.JSlider")) {
	    	    final JSlider js1 = (JSlider) cw.getComponent(i);
	    	    
	    	    JPanel jp = ((JPanel) cw.getComponent(i-1));
	    		panels.add(jp.getComponent(1), BorderLayout.EAST);
	    		panels.add(cw.getComponent(i), BorderLayout.CENTER);
	    		
	    	    js1.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						panel.requestFocusInWindow();
					}
	    	    });
	    	    break;
	    	}
	    }
	    
	    this.panel.add(panels, BorderLayout.SOUTH);
    }

    private void init2() throws RemoteException, VisADException {
	    // Create the Displays and their maps
	    displays = new DisplayImpl[] { new DisplayImplJ3D("display1"), new DisplayImplJ3D("display2")};
	
	    // Get display's graphics mode control draw scales
	    for( int i = 0; i< displays.length;i++){
	      GraphicsModeControl dispGMC = (GraphicsModeControl) displays[i].getGraphicsModeControl();
	      dispGMC.setScaleEnable(true);
	      displays[i].getGraphicsModeControl().setTextureEnable(false);
	    }
		
	    RealType ir_radiance = RealType.getRealType(Translate.translate(942)); //"Iso_surface_level");
	    RealType index = RealType.getRealType("index");
	    RealType[] types = {declination, rightAscension, velocity, ir_radiance};
	    RealTupleType radiance = new RealTupleType(types);
	    FunctionType image_tuple = new FunctionType(index, radiance);
	    
	    Set domain_set1 = new Integer1DSet(cube.getNColumns()*cube.getNLevels()*cube.getNRows());
	    FlatField imaget1 = new FlatField(image_tuple, domain_set1);
	    try {
			imaget1.setSamples(getSamples2(cube.getCube()));
		} catch (Exception exc) {
			Logger.log(LEVEL.ERROR, "Error setting the samples. Message was: "+exc.getLocalizedMessage()+". Trace: "+JPARSECException.getTrace(exc.getStackTrace()));
		}
		displays[0].addMap(new ScalarMap(declination, Display.YAxis));
	    displays[0].addMap(new ScalarMap(rightAscension, Display.XAxis));
	    displays[0].addMap(new ScalarMap(velocity, Display.ZAxis));
	    displays[0].addMap(new ScalarMap(ir_radiance, Display.Green));
	    ScalarMap map1contour = new ScalarMap(ir_radiance, Display.IsoContour);
	    displays[0].addMap(map1contour);
	    DataReferenceImpl ref_imaget1 = new DataReferenceImpl("ref_imaget1");
	    ref_imaget1.setData(imaget1);
	    displays[0].addReference(ref_imaget1, null);
	    displays[0].addDisplayListener(this);

	    Set domain_set2 = new Integer1DSet(cube2.getNColumns()*cube2.getNLevels()*cube2.getNRows());
	    FlatField imaget2 = new FlatField(image_tuple, domain_set2);
	    try {
			imaget2.setSamples(getSamples2(cube2.getCube()));
		} catch (Exception exc) {
			Logger.log(LEVEL.ERROR, "Error setting the samples. Message was: "+exc.getLocalizedMessage()+". Trace: "+JPARSECException.getTrace(exc.getStackTrace()));
		}
		displays[1].addMap(new ScalarMap(declination, Display.YAxis));
	    displays[1].addMap(new ScalarMap(rightAscension, Display.XAxis));
	    displays[1].addMap(new ScalarMap(velocity, Display.ZAxis));
	    displays[1].addMap(new ScalarMap(ir_radiance, Display.Green));
	    ScalarMap map2contour = new ScalarMap(ir_radiance, Display.IsoContour);
	    displays[1].addMap(map2contour);
	    DataReferenceImpl ref_imaget2 = new DataReferenceImpl("ref_imaget2");
	    ref_imaget2.setData(imaget2);
	    displays[1].addReference(ref_imaget2, null);

	    // Create application window, put display into it
	    panel.setLayout(new BorderLayout());
	
	    dispPanel = new JPanel( new GridLayout(1, 2) );
	    dispPanel.add(displays[0].getComponent());
	    dispPanel.add(displays[1].getComponent());
	    
	    JPanel titles = new JPanel();

	    titles.setLayout(new GridLayout(1, 2));
	    titles.add(new JLabel(Translate.translate(943)), BorderLayout.EAST);
	    titles.add(new JLabel(Translate.translate(944)), BorderLayout.WEST);
	    panel.add(titles, BorderLayout.NORTH);
	    
	    panel.add(dispPanel, BorderLayout.CENTER);
	
	    ContourWidget cw = new ContourWidget(map1contour);
	    ContourWidget cw2 = new ContourWidget(map2contour);
	    JPanel panels = new JPanel();
	    panels.setLayout(new GridLayout(2, 1));
	
	    for (int i=0; i<cw.getComponentCount(); i++) {
	    	if (cw.getComponent(i).getClass().getName().equals("javax.swing.JSlider")) {
	    	    final JSlider js2 = (JSlider) cw2.getComponent(i);
	    	    final JSlider js1 = (JSlider) cw.getComponent(i);

	    	    JPanel jp = ((JPanel) cw.getComponent(i-1));
	    		panels.add(jp.getComponent(1), BorderLayout.EAST);
	    		panels.add(cw.getComponent(i), BorderLayout.CENTER);
	    		
	    	    js1.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						int v = js1.getValue();
						js2.setValue(v);
						panel.requestFocusInWindow();
					}
	    	    });

	    	    break;
	    	}
	    }
	    
	    this.panel.add(panels, BorderLayout.SOUTH);
    }

    /**
     * Synchronizes the displays if necessary.
     */
    public void displayChanged(DisplayEvent e)
    throws VisADException, RemoteException {
  	  if (e.getId() == DisplayEvent.FRAME_DONE) {
  	    doSynchronize();
  	  }
    }
    private void doSynchronize(){
  	    try{
  	      if(displaysAreLinked){
  	        displays[1].getProjectionControl().setMatrix(displays[0].getProjectionControl().getMatrix());
  	      }
  	    } catch(Exception ex){  }
  	  }

    /**
     * Returns the 'samples' object to be used as a FlatField in the
     * VISAD library. 
     * @param cube The 3d cube.
     * @return The 2d samples.
     */
    private float[][] getSamples2(float cube[][][]){
    	int nlevels = cube.length;
    	int ncolumns = cube[0].length;
    	int nrows = cube[0][0].length;
    	float[][] flat_samples = new float[4][ncolumns * nrows * nlevels];

    	int index = 0;
    	for(int i = 0; i < nlevels; i++) {
    		for(int c = 0; c < ncolumns; c++) {
    			for(int r = 0; r < nrows; r++) {
    				flat_samples[3][index] =   cube[i][c][r];
    				flat_samples[0][index] =   (r-nrows/2.0f);
    				flat_samples[1][index] =   (c-ncolumns/2.0f);
    				flat_samples[2][index] =   (i-nlevels/2.0f);
    				index ++;
    			}
    		}
    	}
    	
/*    	
		// Force sphere isosurface for testing: when resolution is too high
		// VISAD produces wrong results
//		System.out.println(ncolumns * nrows * nlevels+"/"+ncolumns+"/"+nrows+"/"+nlevels);
        Random random = new Random();
        for (int i=0; i<ncolumns * nrows * nlevels; i++) {
        	flat_samples[0][i] = 2f * random.nextFloat() - 1f;
        	flat_samples[1][i] = 2f * random.nextFloat() - 1f;
        	flat_samples[2][i] = 2f * random.nextFloat() - 1f;
        	flat_samples[3][i] = (float) Math.sqrt(flat_samples[0][i] * flat_samples[0][i] +
        			flat_samples[1][i] * flat_samples[1][i] +
        			flat_samples[2][i] * flat_samples[2][i]);
        }
*/
    	return flat_samples;
    }
    
    /**
     * Shows the chart.
     * @param width Width in pixels.
     * @param height Height in pixels.
     */
    public void show(int width, int height)
    {
		JFrame aframe = new JFrame(panel.getName());
		aframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aframe.getContentPane().add(panel);
		aframe.setPreferredSize(new Dimension(width, height));
		aframe.pack();
		aframe.setVisible(true);
    }

	private void writeObject(ObjectOutputStream out)
	throws IOException {
		out.writeObject(panel.getName());
		out.writeObject(this.cube);
		out.writeObject(this.cube2);
	}
	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException {
		String title = (String) in.readObject();
		panel = new JPanel();
		panel.setName(title);

		this.cube = (VISADCubeElement) in.readObject();
		this.cube2 = (VISADCubeElement) in.readObject();
		try {
			if (cube2 == null) {
				start(cube);
			} else {
				start2(cube, cube2);				
			}
		} catch (Exception exc) {
			throw new IOException("file cannot be read");
		}
 	}

	/**
	 * Returns if the window is visible.
	 * @return True or false.
	 */
	public boolean isVisible() {
		if (panel != null && panel.isVisible()) return true;
		return false;
	}

	/**
	 * Returns the JPanel with this component.
	 * @return The VISAD panel.
	 */
	public JPanel getComponent() {
		return panel;
	}

	/**
	 * Test program.
	 * @param args Unused.
	 */
	public static void main(String[] args)
	{
		try {
			String file = "/home/alonso/reduccion/2010/reajusteRMonConDataCube/rmon_K.lmv";
			String file2 = "/home/alonso/reduccion/2010/reajusteRMonConDataCube/rmon_modelo_K.lmv";
//			file = "/home/alonso/reduccion/2007/discos/qa4c_ZCMa/pc10-co21.lmv";
	  		LMVCube lmv = new LMVCube(file);
	  		LMVCube lmv2 = new LMVCube(file2);

	  		// Limit the size of both cubes to the same values
	  		int maxS = 12;
			lmv.setCubeData(lmv.getCubeData(maxS, maxS, maxS));
			lmv2.setCubeData(lmv2.getCubeData(maxS, maxS, maxS));
			maxS = lmv.axis3Dim;
			lmv.resample(maxS, maxS, false);
			lmv2.resample(maxS, maxS, false);
			System.out.println(lmv.axis3Dim);

			// Just for testing: replace the second cube with a sphere
 			int s = lmv2.getCubeData().length;
	  		float synthetic[][][] = new float[s][s][s];
	  		for (int i=0; i<synthetic.length; i++) { // vel
		  		for (int j=0; j<synthetic[0].length; j++) { // ra
			  		for (int k=0; k<synthetic[0][0].length; k++) { // dec
			  			double dx = synthetic.length / 2.0 - i;
			  			double dy = synthetic[0].length / 2.0 - j;
			  			double dz = synthetic[0][0].length / 2.0 - k;
			  			synthetic[i][j][k] = maxS / 2f - (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
			  		}
		  		}
	  		}
			lmv2.setCubeData(synthetic);
	  		
			// Create the 2 cube objects
	  		float v0 = lmv.getv0();
			float vf = lmv.getvf();
			float x0 = (float) (lmv.getx0() * Constant.RAD_TO_ARCSEC);
			float xf = (float) (lmv.getxf() * Constant.RAD_TO_ARCSEC);
			float y0 = (float) (lmv.gety0() * Constant.RAD_TO_ARCSEC);
			float yf = (float) (lmv.getyf() * Constant.RAD_TO_ARCSEC);
			VISADCubeElement cube = new VISADCubeElement(lmv.getCubeData(),
					  new float[] {x0, xf, y0, yf, v0, vf},
					  "OFFSET_RA", VISADCubeElement.UNIT.ARCSEC,
					  "OFFSET_DEC", VISADCubeElement.UNIT.ARCSEC,
					  "Velocity", VISADCubeElement.UNIT.KILOMETER_PER_SECOND,
					  "FLUX", VISADCubeElement.UNIT.KELVIN);
			VISADCubeElement cube2 = new VISADCubeElement(lmv2.getCubeData(),
					  new float[] {x0, xf, y0, yf, v0, vf},
					  "OFFSET_RA", VISADCubeElement.UNIT.ARCSEC,
					  "OFFSET_DEC", VISADCubeElement.UNIT.ARCSEC,
					  "Velocity", VISADCubeElement.UNIT.KILOMETER_PER_SECOND,
					  "FLUX", VISADCubeElement.UNIT.KELVIN);
			
			// Create the chart and show it
			CreateVISADIsoSurfaceChart vc = new CreateVISADIsoSurfaceChart(cube, cube2, lmv.line+" datacube of "+lmv.sourceName);
			vc.show(800, 600);
			
//			CreateVISADIsoSurfaceChart vc = (CreateVISADIsoSurfaceChart) Serialization.readObject("/home/alonso/eclipse/libreria_jparsec/presentation/testPanel/visadIsoSurfaceTest");
//			Serialization.writeObject(vc, "/home/alonso/visadIsoSurfaceTest");
		} catch (JPARSECException e)
		{
			e.showException();
		} catch (Exception ee)
		{
			ee.printStackTrace();
		}
	}
}
