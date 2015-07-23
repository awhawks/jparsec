package jparsec.io;

import jparsec.graph.chartRendering.frame.HTMLRendering;
import jparsec.io.image.Picture;

public class HTMLReportTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        int w = 800, h = 800;
        String s = "http://conga.oan.es/~alonso/lib/exe/fetch.php?media=jupitertripleeclipse.png";
        Picture pic = new Picture(s);
        pic.getScaledInstance(w, h, true);

        HTMLReport html = new HTMLReport();
        html.setTextColor(HTMLReport.COLOR_WHITE);
        html.setTextSize(HTMLReport.SIZE.LARGE);
        html.writeHeader("Title");
        html.beginBody(HTMLReport.COLOR_BLACK);
        html.writeAVeryBigMainTitle("Jupiter data");
        html.writeHorizontalLine();
        html.writeParagraph("Jupiter is bla bla bla ...");
        String width = "" + pic.getWidth(), height = "" + pic.getHeight(), align = "center", border = "0", alt = "", src = s;
        html.writeImageToHTML(width, height, align, border, alt, src);
        html.endBody();
        html.endDocument();

        HTMLRendering dlg = new HTMLRendering("Window title", html.getCode(), pic.getImage(), true);
        dlg.setSize(w, h);
        dlg.setModal(true);
        dlg.setVisible(true);
    }
}
