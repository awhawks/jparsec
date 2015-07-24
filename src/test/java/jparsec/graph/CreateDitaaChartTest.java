package jparsec.graph;

import jparsec.io.image.Picture;

public class CreateDitaaChartTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws Exception {
        // Global layout for the diagram, with the
        // position of the boxes and the arrows to show.
        // Each number is the index for a box defined later.
        // This allows to reorganize the chart very quickly
        String diagram[] = new String[] {
            "  0-------|",
            "|-----v   1-",
            "2-3-4-5-6-||7",
            "  |-=-<--=--",
            "          89"
        };

        boolean dashed = false; // Don't use dashed lines for the boxes

        // Definition for each box
        DitaaBoxElement db[] = new DitaaBoxElement[] {
            new DitaaBoxElement(new String[] { "Observed", "Herschel", "spectra" }, "c897", DitaaBoxElement.TYPE.INPUT_OUTPUT, "W", dashed),
            new DitaaBoxElement(new String[] { "Fitted?" }, "cDEF", DitaaBoxElement.TYPE.QUESTION, "nsw", dashed),
            new DitaaBoxElement(new String[] { "  Profiles", "by N. Crimier", "T(r), @rho(r)" }, "c897", DitaaBoxElement.TYPE.DOCUMENT, "NW", dashed),
            new DitaaBoxElement(new String[] { "  Chemical", "   model", "(P. Caselli)" }, "cAAF", DitaaBoxElement.TYPE.ELLIPSE, "Ws", dashed),
            new DitaaBoxElement(new String[] { "Modeled", "abundance", "profiles" }, "c789", DitaaBoxElement.TYPE.DOCUMENT, "W", dashed),
            new DitaaBoxElement(new String[] { " Radiative", " transfer", " model" }, "cAAF", DitaaBoxElement.TYPE.ELLIPSE, "WN", dashed),
            new DitaaBoxElement(new String[] { "Spectra " }, "cFFF", DitaaBoxElement.TYPE.SIMPLE, "W", dashed),
            new DitaaBoxElement(new String[] { "N" }, "cFFF", DitaaBoxElement.TYPE.NO_BOX, "E", false),
            new DitaaBoxElement(new String[] { "Chemical" }, "cFFF", DitaaBoxElement.TYPE.NO_BOX, "NW", false),
            new DitaaBoxElement(new String[] { " effects" }, "cFFF", DitaaBoxElement.TYPE.NO_BOX, "NE", false)
        };

        // Width and height for the boxes
        int w = 15, h = 7;

        // Construct and show the chart
        CreateDitaaChart qd = new CreateDitaaChart(diagram, db, w, h);
        qd.setTransparent(false);
        System.out.println(qd.diagram);
        Picture pic = new Picture(qd.ditaaRenderImage());
        pic.show("My diagram");

        /*
        // Optional export to vector graphics
        Picture.createSVGPicture("/home/alonso/test.svg", qd, "draw", pic.getWidth(), pic.getHeight());
        Picture.createEPSPicture("/home/alonso/test.eps", qd, "draw", pic.getWidth(), pic.getHeight());
        Picture.createPDFPicture("/home/alonso/test.pdf", qd, "draw", pic.getWidth(), pic.getHeight());
        */
    }
}
