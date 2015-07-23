package jparsec.graph.chartRendering;

import jparsec.ephem.Target;
import jparsec.io.image.Picture;

public class SVGPlanetsTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws Exception {
        Picture p = new Picture(SVGPlanets.drawIcon(Target.TARGET.VENUS, 4));
        p.show("My icon");
    }
}
