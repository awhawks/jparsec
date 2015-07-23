package jparsec.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import jparsec.io.image.Draw;

public class ChartSeriesElementTest {
    /**
     * A test program.
     *
     * @param args Unused.
     */
    public static void main(String args[]) {
        System.out.println("ChartSeriesElement test");

        int shape = 0, boxX = 3 * 5, boxY = 3 * 5; //0, 2,3,4
        Shape s = ChartSeriesElement.shapeFor(shape, boxX, boxY);

        Draw draw = new Draw("Mi dibujo", 500, 500);
        Graphics2D g = (Graphics2D) draw.getOffScreenImage().getGraphics();
        g.setColor(Color.BLACK);
        g.translate(100, 100);
        g.fill(s);
        draw.show();
    }
}
