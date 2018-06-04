package jparsec.graph.chartRendering.frame;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class JTableRenderingTest {
    /**
     * Test program.
     *
     * @param args Not used.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("JTableRendering test");
        String columns[] = new String[] { "c1", "c2", "c3", "c4", "c5" };
        boolean editable[] = null; // All false except boolean
        Class<?> classes[] = new Class<?>[] { Boolean.class, Integer.class, String.class, String.class, String.class };
        String table[][] = new String[11][columns.length];

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                table[i][j] = "r" + (i + 1) + "c" + (j + 1);
            }
            table[i][0] = "true";
            table[i][1] = "" + (i + 1);
        }

        JTableRendering jt = new JTableRendering(columns, classes, editable, table);
        jt.setRowColor(4, new String[] { "r1c5", "r3c5", "r5c5" }, new Color[] { Color.RED, Color.GREEN, Color.BLUE });
        jt.setColumnWidth(new int[] { 30, 30, 50, 100, 30 });

        JFrame frame = new JFrame("My Table");
        frame.setPreferredSize(new Dimension(400, 300));
        frame.add(new JScrollPane(jt.getComponent()));
        frame.pack();
        frame.setVisible(true);
    }
}
