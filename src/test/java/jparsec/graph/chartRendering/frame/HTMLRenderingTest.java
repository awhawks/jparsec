package jparsec.graph.chartRendering.frame;

import jparsec.io.image.Picture;
import jparsec.util.JPARSECException;

public class HTMLRenderingTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        try {
            String s = "http://conga.oan.es/~alonso/lib/exe/fetch.php?media=jupitertripleeclipse.png";
            Picture pic = new Picture(s);
            // s = pic.imageToString(true); // base64 images not supported
            HTMLRendering dlg = new HTMLRendering("Hellow!",
                    "<html><body bgcolor='#000000'><img src='" + s + "'><BR>This is a <n>JPARSEC</n> test for an HTML dialog.</body></html>",
                    pic.getImage(), true);
            dlg.setSize(800, 800);
            dlg.setVisible(true);
        } catch (JPARSECException e) {
            e.showException();
        }
    }
}
