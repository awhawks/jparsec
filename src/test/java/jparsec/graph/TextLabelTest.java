package jparsec.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class TextLabelTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.pack();
        frame.setVisible(true);
        Graphics g = frame.getGraphics();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        do {
            //String latex = "@latex{\\int_{t=0}^{2\\pi}\\frac{\\sqrt{t}}{(1+\\mathrm{cos}^2{t})}\\nbspdt}";
            //String s = "@ROTATE015HOLA@MARS@JUPITER@SUN@EARTHln(@REDy{^@ORANGEx_{i}}@SIZE40@BOLD@GREENH" + latex +
            // "I@BLUE@ALPHA@BETA@ITALIC@GAMMA@SIZE10@BLACK@ALPHA)";
            //s = "X_{C^{1_{8}@SPACEhola} O} hola";
            String s = "@CLOCK{12h 50m 37.6\"}";
            TextLabel t = new TextLabel(s,
                    //TextLabel.readFont(TextLabel.FONTS_FILE, TextLabel.FONTS_PATH_DEJAVU_SANS),
                    new Font("Dialog", Font.PLAIN, 30),
                    Color.BLACK, TextLabel.ALIGN.CENTER);
            TextLabel.setRenderUnicodeAsImages(true);
            int x = frame.getWidth() / 2, y = frame.getHeight() / 2;
            t.draw(g, x, y);
            //System.out.println(t.getSimplifiedString());
        } while (frame.isVisible());
    }
}
