package jparsec.graph.chartRendering.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import jparsec.io.image.Draw;
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
                    "<HTML><img src=\"" + s + "\">This is a <B>JPARSEC</B> test for an HTML dialog.</HTML>",
                    pic.getImage(), true);
            dlg.setSize(800, 800);
            dlg.setModal(true);
            dlg.setVisible(true);
            int w = 600, h = 600;
            int th = 30;
            Draw draw = new Draw(600, 1200);
            draw.clear(Color.BLACK);
            draw.setPenColor(Color.WHITE);
            draw.setFont(new Font(Font.DIALOG, Font.PLAIN, th));
            draw.setPenRadius(0.005);
            double x = 0.025, y = 0.975;
            y -= th / (double) draw.getHeight();
            draw.text(x, y, "Jupiter", false);
            draw.setFont(new Font(Font.DIALOG, Font.PLAIN, th / 2));
            y -= th / (double) draw.getHeight();
            y -= pic.getHeight() * 0.5 / draw.getHeight();
            draw.picture(0.5, y, pic.getImage());
            y -= pic.getHeight() * 0.5 / draw.getHeight();
            draw.line(0, y, 1.0, y);
            y -= th / (double) draw.getHeight();
            draw.text(x, y, "TEST", false);
            final JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setUndecorated(true);
            f.setPreferredSize(new Dimension(w, h));
            final JLabel label = new JLabel(new ImageIcon(draw.getOffScreenImage()));
            f.add(new JScrollPane(label));
            f.pack();
            f.setVisible(true);
        } catch (JPARSECException e) {
            e.showException();
        }
    }
}
