package jparsec.graph;

import jparsec.astronomy.Difraction;
import jparsec.astronomy.TelescopeElement;
import jparsec.io.Serialization;

public class CreateVISADChartTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws Exception {
        TelescopeElement telescope = TelescopeElement.NEWTON_20cm;
        int field = 10;
        int nplane = 16;

        float data[][][] = new float[nplane][field * 10 + 1][field * 10 + 1];
        int initDiam = 200, finalDiam = 20;
        for (int i = 0; i < nplane; i++) {
            telescope.diameter = initDiam + (finalDiam - initDiam) * i / 16;
            telescope.centralObstruction = telescope.diameter / 10;
            double dat[][] = Difraction.pattern(telescope, field);
            for (int ix = 0; ix < dat.length; ix++) {
                for (int iy = 0; iy < dat[0].length; iy++) {
                    data[i][iy][ix] = (float) dat[iy][ix];
                }
            }
        }

        VISADCubeElement cube = new VISADCubeElement(data,
                new float[] { field, -field, -field, field, initDiam, finalDiam },
                "dx", VISADCubeElement.UNIT.RADIAN,
                "dy", VISADCubeElement.UNIT.RADIAN,
                "Diameter", VISADCubeElement.UNIT.KILOMETER_PER_SECOND,
                "Flux", VISADCubeElement.UNIT.KELVIN);
        CreateVISADChart p = new CreateVISADChart(cube, 50.0, false);
        p.show(800, 600);
        System.out.println("diam " + p.getVelSliderValue());

        //Serialization.writeObject(cube, "/home/alonso/visadTest2");
        //Serialization.writeObject(p, "/home/alonso/createVisadTest2");

/*          field = 50;
          float data2[][][] = new float[16][field*10+1][field*10+1];
          for (int i=0; i<16; i++)
          {
              telescope.diameter = initDiam + (finalDiam - initDiam) * i / 16;
              telescope.centralObstruction = telescope.diameter / 10;
              double dat[][] = Difraction.pattern(telescope, field);
              for (int ix=0; ix<dat.length; ix++)
              {
                  for (int iy=0; iy<dat[0].length; iy++)
                  {
                      data2[i][iy][ix] = (float) dat[iy][ix];
                  }
              }
          }

          VISADCubeElement cube2 = new VISADCubeElement(data,
                  new float[] {field, -field, -field, field, initDiam, finalDiam},
                  "dx", VISADCubeElement.UNIT_RADIAN,
                  "dy", VISADCubeElement.UNIT_RADIAN,
                  "Diameter", VISADCubeElement.UNIT_KILOMETER_PER_SECOND,
                  "Flux", VISADCubeElement.UNIT_KELVIN);
          p.update(cube2, 0);
*/
        // Second example
        // Show levels and chart types
        // Retrieve levels at cursor position
        // Show multiple surfaces (and points?)
        field = 10;
        double dat[][] = Difraction.pattern(telescope, field);
        GridChartElement chart = new GridChartElement("Difraction pattern",
                "offsetX", "offsetY", "RelativeIntensity", GridChartElement.COLOR_MODEL.RED_TO_BLUE,
                new double[] { field, -field, -field, field }, dat,
                new double[] { 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0 }, 400);

/*            GridChartElement chart2 = GridChartElement.getSurfaceFromPoints(new DoubleVector[] {
                    new DoubleVector(1, 1, 0),
                    new DoubleVector(2, 1, 1),
                    new DoubleVector(3, 1, 0),
                    new DoubleVector(1, 2, 1),
                    new DoubleVector(2, 2, 4),
                    new DoubleVector(3, 2, 1),
                    new DoubleVector(1, 3, 0),
                    new DoubleVector(2, 3, 1),
                    new DoubleVector(3, 3, 0)
                }, 20);
            chart.data = chart2.data;
            chart.limits = chart2.limits;
*/

        chart.type = GridChartElement.TYPE.RASTER_CONTOUR;
        chart.opacity = GridChartElement.OPACITY.OPAQUE;

        CreateVISADChart vt = new CreateVISADChart(chart);
        vt.show(600, 600);
        Serialization.writeObject(p, "/home/alonso/visadTest");
        Serialization.writeObject(vt, "/home/alonso/visadSurfaceTest");
    }
}
