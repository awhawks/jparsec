package jparsec.astrophysics.gildas;

import jparsec.ephem.Functions;
import jparsec.graph.CreateGridChart;
import jparsec.graph.DataSet;
import jparsec.io.FileIO;
import jparsec.math.Constant;
import jparsec.observer.LocationElement;
import jparsec.util.JPARSECException;

import java.awt.geom.Point2D;

public class LMVCubeTest {
    /**
     * Test program.
     *
     * @param args Unused.
     */
    public static void main(String args[]) {
        try {
            /*
            // Strip test
            LMVCube test = new LMVCube("/home/alonso/eclipse/libreria_jparsec/stripTest/modelTest.lmv");
            CreateGridChart gctest = test.getStrip(new LocationElement(-5*15*Constant.ARCSEC_TO_RAD, -60.0 * Constant.ARCSEC_TO_RAD, 1.0),
                    new LocationElement(5*15*Constant.ARCSEC_TO_RAD, 60.0 * Constant.ARCSEC_TO_RAD, 1.0), null);
            gctest.showChartInSGTpanel(false);
            */

            String LMVFiles[] = new String[] {
                    "/home/alonso/documentos/propuesta/2008/lkha233/ees-1-co10.lmv-clean",
                    "/home/alonso/eclipse/workspace/mis_programas/Core/cylinder_models/RMON_obs/cubo/rmon_K.lmv",
                    "/home/alonso/eclipse/workspace/mis_programas/Core/cylinder_models/RMON_obs/cubo/n079-co21-ex.lmv-clean",
                    "/home/alonso/documentos/propuesta/2008/lkha233/ees--1-co10.lmv-clean",
                    "/home/alonso/documentos/propuesta/2008/lkha233/slice-disco.lmv-clean",
                    "/home/alonso/documentos/propuesta/2008/lkha233/cos--1-sio.lmv-clean",
                    "/home/alonso/documentos/propuesta/2008/lkha233/ees--1-co10-un.lmv-clean",
                    "/home/alonso/documentos/propuesta/2008/lkha233/sio-compress2.lmv-clean",
                    "/home/alonso/documentos/propuesta/2008/lkha233/slice-strip.lmv-clean",
                    "/home/alonso/documentos/propuesta/2008/lkha233/slice-jet.lmv-clean",
                    "/home/alonso/documentos/propuesta/2008/lkha233/s--1-c3mm.lmv-clean",
                    "/home/alonso/documentos/propuesta/2008/lkha233/s--1-c2mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pb10_new/total2.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pdbi/zcma-cont3mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pdbi/zcma-cont1mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pa10/pa10-co10-compress4.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pb10/pb10-co10-total-compress20.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pb10/pb10-co10-total-compress4.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pb10/pb10-cont1mm-total.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pb10/pb10-cont3mm-total.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/nuevo/total2/Asuncion/Tomas/pc10-co21-total-2.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/nuevo/total2/Asuncion/Tomas/pc10-co10-total-2.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pb10/pb10-co10-total.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/nuevo/total2/Asuncion/pc10-cont3mm-total-2.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/nuevo/total2/Asuncion/pc10-cont1mm-total-2.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/nuevo/total2/pc10-co10-total-2.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/nuevo/total2/pc10-co21-total-2.lmv-clean",
                    "/home/alonso/colaboraciones/Asuncion/2007/ALMA-special_issue/figuras/ic1396/all-1mm.lmv-clean",
                    "/home/alonso/colaboraciones/Asuncion/2007/ALMA-special_issue/figuras/serpens/pa42-cont3mm.lmv-clean",
                    "/home/alonso/colaboraciones/Asuncion/2007/ALMA-special_issue/figuras/ic1396/all-3mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/total/pc10-co21-total.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/pc10-co10.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/total/pc10-co10-total.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/total/pc10-cont1mm-total.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/total/pc10-cont3mm-total.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/pc10-co21-strip7.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/pc10-co21-strip6.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/pc10-co21-strip5.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/pc10-co21-strip4.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/pc10-co21-strip3.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/pc10-co21-strip2.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/pc10-co21-strip1.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pc10/pc10-co21.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pa10/tardir/pa10-cont3mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pa10/tardir/pa10-cont1mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pdbi/mwc297-cont3mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pa10/pa10-cont3mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pdbi/mwc297-cont1mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pa10/pa10-cont1mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pdbi/lkha215-cont3mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pdbi/lkha215-cont1mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pdbi/mwc137-cont3mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pdbi/mwc137-cont1mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pdbi/mwc1080-cont3mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/pdbi/mwc1080-cont1mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/la3a/cont1mm-total.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/p055/total-cont3mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/p055/total-cont3mm-beam08.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/p055/total-cont1mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/p055/total-cont1mm-beam03.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/p055/n079-co21-ex-sr.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/p055/n079-co10-ex.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/la3a/cont3mm-total.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/la3a/13co21-total-ex1.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/la3a/13co10-total-ex.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/lc3a/lc3a-cont3mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/lc3a/lc3a-cont1mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/lc3a/lc3a-13co21-extract.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/lc3a/lc3a-13co10-extract.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/lb3a/lb3a-cont3mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/lb3a/lb3a-cont1mm.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/lb3a/lb3a-13co21-extract.lmv-clean",
                    "/home/alonso/reduccion/2007/discos/lb3a/lb3a-13co10-extract.lmv-clean",
                    "/home/alonso/eclipse/workspace/mis_programas/Core/cylinder_models/RMON_obs/cubo/n079-co21-ex.lmv-clean",
                    "/home/alonso/eclipse/workspace/mis_programas/Core/cylinder_models/RMON_obs/poster/total-cont3mm-beam08.lmv-clean",
                    "/home/alonso/eclipse/workspace/mis_programas/Core/cylinder_models/RMON_obs/poster/total-cont1mm-beam03.lmv-clean",
                    "/home/alonso/eclipse/workspace/mis_programas/Core/cylinder_models/RMON_obs/jets/n079-co21-ex.lmv-clean"
            };

            String LMVNames[] = new String[LMVFiles.length];
            for (int i = 0; i < LMVFiles.length; i++) {
                LMVNames[i] = FileIO.getFileNameFromPath(LMVFiles[i]);
            }
/*            String s = (String) JOptionPane.showInputDialog(
                    null,
                    "Select the LMV file to read:",
                    "Select LMV file",
                    JOptionPane.PLAIN_MESSAGE, null,
                    LMVNames, LMVNames[0]
            );
            if (s == null) System.exit(0);
*/
            String s = LMVNames[0];
            int index = DataSet.getIndexContaining(LMVFiles, s);
            LMVCube lmv = new LMVCube(LMVFiles[index]);
/*            LMVCube lmv = new LMVCube("/home/alonso/documentos/latex/2010/Tesis/material/convolutionTest/out2cmio.lmv");
            LMVCube lmvv = new LMVCube("/home/alonso/documentos/latex/2010/Tesis/material/convolutionTest/out2c.lmv");
            System.out.println(lmv.axis12PA+"/"+lmvv.axis12PA);
            System.out.println(lmv.axis1Dim+"/"+lmvv.axis1Dim);
            System.out.println(lmv.axis1Label+"/"+lmvv.axis1Label);
            System.out.println(lmv.axis1Pos+"/"+lmvv.axis1Pos);
            System.out.println(lmv.axis2Dim+"/"+lmvv.axis2Dim);
            System.out.println(lmv.axis2Label+"/"+lmvv.axis2Label);
            System.out.println(lmv.axis2Pos+"/"+lmvv.axis2Pos);
            System.out.println(lmv.axis3Dim+"/"+lmvv.axis3Dim);
            System.out.println(lmv.axis3Label+"/"+lmvv.axis3Label);
            System.out.println(lmv.axis4Dim+"/"+lmvv.axis4Dim);
            System.out.println(lmv.axis4Label+"/"+lmvv.axis4Label);
            System.out.println(lmv.beamMajor+"/"+lmvv.beamMajor);
            System.out.println(lmv.beamMinor+"/"+lmvv.beamMinor);
            System.out.println(lmv.beamPA+"/"+lmvv.beamPA);
            System.out.println(lmv.blanking+"/"+lmvv.blanking);
            System.out.println(lmv.blankingTolerance+"/"+lmvv.blankingTolerance);
            System.out.println(lmv.coordinateSystem+"/"+lmvv.coordinateSystem);
            System.out.println(lmv.epoch+"/"+lmvv.epoch);
            System.out.println(lmv.fluxUnit+"/"+lmvv.fluxUnit);
            System.out.println(lmv.freqResolution+"/"+lmvv.freqResolution);
            System.out.println(lmv.imageFormat+"/"+lmvv.imageFormat);
            System.out.println(lmv.imageFrequency+"/"+lmvv.imageFrequency);
            System.out.println(lmv.isBig+"/"+lmvv.isBig);
            System.out.println(lmv.line+"/"+lmvv.line);
            System.out.println(lmv.maximumFlux+"/"+lmvv.maximumFlux);
            System.out.println(lmv.minimumFlux+"/"+lmvv.minimumFlux);
            System.out.println(lmv.minimumAndMaximumFluxPositions[0]+"/"+lmvv.minimumAndMaximumFluxPositions[0]);
            System.out.println(lmv.minimumAndMaximumFluxPositions[1]+"/"+lmvv.minimumAndMaximumFluxPositions[1]);
            System.out.println(lmv.minimumAndMaximumFluxPositions[2]+"/"+lmvv.minimumAndMaximumFluxPositions[2]);
            System.out.println(lmv.minimumAndMaximumFluxPositions[3]+"/"+lmvv.minimumAndMaximumFluxPositions[3]);
            System.out.println(lmv.minimumAndMaximumFluxPositions[4]+"/"+lmvv.minimumAndMaximumFluxPositions[4]);
            System.out.println(lmv.minimumAndMaximumFluxPositions[5]+"/"+lmvv.minimumAndMaximumFluxPositions[5]);
            System.out.println(lmv.minimumAndMaximumFluxPositions[6]+"/"+lmvv.minimumAndMaximumFluxPositions[6]);
            System.out.println(lmv.minimumAndMaximumFluxPositions[7]+"/"+lmvv.minimumAndMaximumFluxPositions[7]);
            System.out.println(lmv.noise+"/"+lmvv.noise);
            System.out.println(lmv.numberOfAxes+"/"+lmvv.numberOfAxes);
            System.out.println(lmv.projectionType+"/"+lmvv.projectionType);
            System.out.println(lmv.restFreq+"/"+lmvv.restFreq);
            System.out.println(lmv.rms+"/"+lmvv.rms);
            System.out.println(lmv.sourceDEC+"/"+lmvv.sourceDEC);
            System.out.println(lmv.sourceGLat+"/"+lmvv.sourceGLat);
            System.out.println(lmv.sourceGLon+"/"+lmvv.sourceGLon);
            System.out.println(lmv.sourceName+"/"+lmvv.sourceName);
            System.out.println(lmv.sourceParallax+"/"+lmvv.sourceParallax);
            System.out.println(lmv.sourceProperMotionDEC+"/"+lmvv.sourceProperMotionDEC);
            System.out.println(lmv.sourceProperMotionRA+"/"+lmvv.sourceProperMotionRA);
            System.out.println(lmv.sourceRA+"/"+lmvv.sourceRA);
            System.out.println(lmv.velOffset+"/"+lmvv.velOffset);
            System.out.println(lmv.velResolution+"/"+lmvv.velResolution);
            System.out.println(lmv.xAxisID+"/"+lmvv.xAxisID);
            System.out.println(lmv.yAxisID+"/"+lmvv.yAxisID);
            System.out.println(lmv.zAxisID+"/"+lmvv.zAxisID);
*/
            lmv.setCubeData(lmv.getCubeData(100, 100, 200));

//            Serialization.writeObject(lmv, "/home/alonso/lmvTest");

/*            int inix = 16, endx = 48, iniy = 16, endy = 48, iniz = 0, endz = 59;
            lmv.clip(inix, endx, iniy, endy, iniz, endz);
            System.out.println("*"+lmv.cube.length+"/"+lmv.cube[0].length+"/"+lmv.cube[0][0].length+"///"+lmv.blanking);
            lmv.cube = LMVCube.scaleIntensity(lmv.cube, lmv.blanking, 24f);
            lmv.fluxUnit = "K";
*/
/*            System.out.println(lmv.minimumAndMaximumFluxPositions[0]+"*"+lmv.minimumAndMaximumFluxPositions[1]+"*"+lmv.axis1Dim);
            System.out.println(lmv.minimumAndMaximumFluxPositions[2]+"*"+lmv.minimumAndMaximumFluxPositions[3]+"*"+lmv.axis2Dim);
            System.out.println(lmv.minimumAndMaximumFluxPositions[4]+"*"+lmv.minimumAndMaximumFluxPositions[5]+"*"+lmv.axis3Dim);
            System.out.println(lmv.minimumAndMaximumFluxPositions[6]+"*"+lmv.minimumAndMaximumFluxPositions[7]+"*"+lmv.axis4Dim);
*/
            System.out.println(lmv.axis1Dim + "/" + lmv.axis2Dim + "/" + lmv.axis3Dim);

            //lmv.setCubeData(lmv.getCubeData(32, 32, 128));
//            lmv.setCubeData(lmv.resample(128, 128));

            System.out.println("width " + lmv.axis1Dim);
            System.out.println("height " + lmv.axis2Dim);
            Point2D.Double p = new Point2D.Double(lmv.wcs.getCrpix1(), lmv.wcs.getCrpix2());
            LocationElement loc = lmv.wcs.getSkyCoordinates(p);
            System.out.println(p.getX() + "/" + p.getY() + " = " + loc.getLongitude() * Constant.RAD_TO_DEG + "/" + loc.getLatitude() * Constant.RAD_TO_DEG);
            Point2D pp = lmv.wcs.getPixelCoordinates(loc);
            System.out.println(pp.getX() + "/" + pp.getY() + " = " + lmv.axis1Pos * Constant.RAD_TO_DEG + "/" + lmv.axis2Pos * Constant.RAD_TO_DEG);
            System.out.println(Functions.formatRA(loc.getLongitude()));
            System.out.println(Functions.formatDEC(loc.getLatitude()));

            String output = "/home/alonso/myLMV.lmv";
            lmv.write(output);

//            lmv = new LMVCube(output);
            //FileIO.deleteFile(output);

/*            System.out.println("bl "+lmv.blanking+"/"+lmv.axis1Dim+"/"+lmv.axis2Dim+"/"+lmv.axis3Dim);
            lmv.setCubeData(lmv.getCubeData());
            lmv.cube[0][0][0] = lmv.blanking;
            lmv.cube[0][1][0] = lmv.blanking;
            lmv.cube[0][0][1] = lmv.blanking;
            lmv.cube[0][1][1] = lmv.blanking;
            lmv.cube[1][0][0] = lmv.blanking;
            lmv.cube[1][1][0] = lmv.blanking;
            lmv.cube[1][0][1] = lmv.blanking;
            lmv.cube[1][1][1] = lmv.blanking;
            lmv.setCubeData(lmv.getCubeData(32,32,32));
*/
/*            float v0 =lmv.getv0();
            float vf = lmv.getvf();
            float x0 = (float) (lmv.getx0() * Constant.RAD_TO_ARCSEC);
            float xf = (float) (lmv.getxf() * Constant.RAD_TO_ARCSEC);
            float y0 = (float) (lmv.gety0() * Constant.RAD_TO_ARCSEC);
            float yf = (float) (lmv.getyf() * Constant.RAD_TO_ARCSEC);
            VISADCubeElement cube = new VISADCubeElement(lmv.getCubeData(),
                      new float[] {x0, xf, y0, yf, v0, vf},
                      "OFFSET_RA", VISADCubeElement.UNIT.ARCSEC,
                      "OFFSET_DEC", VISADCubeElement.UNIT.ARCSEC,
                      "VELOCITY", VISADCubeElement.UNIT.KILOMETER_PER_SECOND,
                      "FLUX", VISADCubeElement.UNIT.KELVIN);
            CreateVISADChart vc = new CreateVISADChart(cube, v0 + (vf - v0) * 0.5, true);
            vc.show(500, 500);
*/
            CreateGridChart ch = lmv.getChart(-1);
            ch.getChartElement().levels = new double[] { 1, 3, 5 };
            ch.update();
            ch.showChartInSGTpanel(true);
        } catch (JPARSECException exc) {
            exc.showException();
        }
    }
}
