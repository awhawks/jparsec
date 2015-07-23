package jparsec.graph;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import jparsec.io.Serialization;
import jparsec.util.Translate;

public class SpectraChartTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("SpectraChart test");
        Translate.setDefaultLanguage(Translate.LANGUAGE.SPANISH);

        String files[] = new String[] {
                "/home/alonso/reduccion/2006/hot_cores/cb3/fich_cb3_map.30m",
                "/home/alonso/reduccion/2006/hot_cores/cepe/fich_cepe.30m",
                "/home/alonso/reduccion/2006/hot_cores/ic1396/fich_ic1396.30m",
                //"/home/alonso/reduccion/2006/hot_cores/omc2/fich_omc2.30m",
                //"/home/alonso/reduccion/2006/hot_cores/n7129/n7129.30m",
                "/home/alonso/reduccion/2006/hot_cores/serp-firs1/fich_serp-firs1.30m",
                //"/home/alonso/reduccion/2006/hot_cores/s140/fich_s140.30m",
                "/home/alonso/reduccion/2011/observacionesSep2010/PVCEP_reduced.30m",
                "/home/alonso/reduccion/2011/observacionesSep2010/HKORI_reduced.30m"
                //"/home/alonso/reduccion/2010/reajusteRMonConDataCube/rmon_K.lmv"
        };

        int w = 630, h = 430;
        final SpectraChart s = new SpectraChart(files, w, h, 12, 0, null, null, false, false);
        Serialization.writeObject(s, "/home/alonso/spectrumChart");
        //final SpectraChart s = (SpectraChart) Serialization.readObject("/home/alonso/spectrumChart");

        final JFrame f = new JFrame();
        //f.setUndecorated(true);
        f.add(s.getComponent());
        f.setPreferredSize(new Dimension(w, h));
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        f.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                s.panel.setSize(f.getSize());
            }
        });
    }
}
