package jparsec.graph;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VISADChartTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("VISADChart test");

        int w = 640;
        //final VISADChart v = (VISADChart) Serialization.readObject("/home/alonso/eclipse/libreria_jparsec/presentation/testPanel/visadChartTest");
        final VISADChart v = new VISADChart("/home/alonso/reduccion/2010/reajusteRMonConDataCube/rmon_K.lmv", 0, 0, w);

        final JFrame frame = new JFrame("Example");
        final JPanel panel = v.getComponent();
        frame.add(panel);
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setSize(new Dimension(w, w));

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                v.update();
            }
        });

        //Serialization.writeObject(v, "/home/alonso/visadChartTest");
    }
}
