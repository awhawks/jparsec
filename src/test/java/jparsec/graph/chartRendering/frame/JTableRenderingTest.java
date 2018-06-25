package jparsec.graph.chartRendering.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JList;
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
        final String columns[] = new String[] { "c1", "c2", "c3", "c4", "c5" };
        boolean editable[] = null; // All false except boolean
        Class<?> classes[] = new Class<?>[] { Boolean.class, Integer.class, String.class, 
        	String.class, JList.class };
        String table[][] = new String[11][columns.length];

        String sep = "@";
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                table[i][j] = "r" + (i + 1) + "c" + (j + 1);
            }
            table[i][0] = "true";
            table[i][1] = "" + (i + 1);
            table[i][4] = "" + (i+1) + sep + (i+10) + sep + (i+20) + sep + (i+1) + sep + 
            		(i+10) + sep + (i+20);
        }

        final JTableRendering jt = new JTableRendering(columns, classes, editable, table);
        jt.setRowColor(4, new String[] { "r1c5", "r3c5", "r5c5" }, 
        		new Color[] { Color.RED, Color.GREEN, Color.BLUE });
        jt.setColumnWidth(new int[] { 30, 30, 50, 100, 30 });
        jt.setSeparatorForLists(sep);
        jt.getComponent().setRowHeight((int) ((jt.getComponent().getRowHeight() * 1.1) * 6));

        JFrame frame = new JFrame("My Table");
        frame.setPreferredSize(new Dimension(400, 300));
        frame.add(new JScrollPane(jt.getComponent()));
        frame.pack();
        frame.setVisible(true);
        
        jt.getComponent().getTableHeader().addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					System.out.println("THIS WILL REMOVE ALL ELEMENTS IN THE TABLE");
			        jt.updateTable(null, true);
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					System.out.println("THIS WILL RESTORE ALL ELEMENTS IN THE TABLE");
			        jt.updateTable(jt.getOriginalTableData(), true);
				}
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
        });
    }
}
