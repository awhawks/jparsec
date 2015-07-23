package jparsec.graph;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GridSpectrumVISADChartTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("GridSpectrumVISADChart test");

        int w = 840;
        final GridSpectrumVISADChart v = new GridSpectrumVISADChart(
                "/home/alonso/reduccion/2010/reajusteRMonConDataCube/rmon_K.lmv",
                w, true, GridSpectrumVISADChart.COORDINATES.EQUATORIAL_OFFSET, new double[] { 0, 20, 40, 60, 80, 100 });

        final JFrame frame = new JFrame("");
        final JPanel panel = v.getComponent();
        frame.add(panel);
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setSize(new Dimension(w, w * 3 / 4 + 100));

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

        //Serialization.writeObject(v, "/home/alonso/gridSpectrum_RMon");
    }
}
