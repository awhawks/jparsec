package jparsec.graph;

import jparsec.astrophysics.gildas.LMVCube;
import jparsec.math.Constant;
import jparsec.util.JPARSECException;

public class CreateVISADIsoSurfaceChartTest {
    /**
     * Test program.
     *
     * @param args Unused.
     */
    public static void main(String[] args) {
        try {
            String file = "/home/alonso/reduccion/2010/reajusteRMonConDataCube/rmon_K.lmv";
            String file2 = "/home/alonso/reduccion/2010/reajusteRMonConDataCube/rmon_modelo_K.lmv";
            //file = "/home/alonso/reduccion/2007/discos/qa4c_ZCMa/pc10-co21.lmv";
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
            for (int i = 0; i < synthetic.length; i++) { // vel
                for (int j = 0; j < synthetic[0].length; j++) { // ra
                    for (int k = 0; k < synthetic[0][0].length; k++) { // dec
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
                    new float[] { x0, xf, y0, yf, v0, vf },
                    "OFFSET_RA", VISADCubeElement.UNIT.ARCSEC,
                    "OFFSET_DEC", VISADCubeElement.UNIT.ARCSEC,
                    "Velocity", VISADCubeElement.UNIT.KILOMETER_PER_SECOND,
                    "FLUX", VISADCubeElement.UNIT.KELVIN);
            VISADCubeElement cube2 = new VISADCubeElement(lmv2.getCubeData(),
                    new float[] { x0, xf, y0, yf, v0, vf },
                    "OFFSET_RA", VISADCubeElement.UNIT.ARCSEC,
                    "OFFSET_DEC", VISADCubeElement.UNIT.ARCSEC,
                    "Velocity", VISADCubeElement.UNIT.KILOMETER_PER_SECOND,
                    "FLUX", VISADCubeElement.UNIT.KELVIN);

            // Create the chart and show it
            CreateVISADIsoSurfaceChart vc = new CreateVISADIsoSurfaceChart(cube, cube2, lmv.line + " datacube of " + lmv.sourceName);
            vc.show(800, 600);

            //CreateVISADIsoSurfaceChart vc = (CreateVISADIsoSurfaceChart) Serialization.readObject("/home/alonso/eclipse/libreria_jparsec/presentation/testPanel/visadIsoSurfaceTest");
            //Serialization.writeObject(vc, "/home/alonso/visadIsoSurfaceTest");
        } catch (JPARSECException e) {
            e.showException();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}
