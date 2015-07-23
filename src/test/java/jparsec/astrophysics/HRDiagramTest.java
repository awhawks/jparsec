package jparsec.astrophysics;

import jparsec.graph.CreateChart;
import jparsec.io.image.Picture;

import java.awt.Color;
import java.awt.Graphics2D;

public class HRDiagramTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws Exception {
        HRDiagram hr = new HRDiagram(true, true, 50, 3000, 30000);
        final CreateChart ch = hr.getChart(true);
        ch.getChartElement().imageHeight = 900;
        ch.updateChart();
        Picture pic = new Picture(ch.chartAsBufferedImage());
        Graphics2D g = pic.getImage().createGraphics();
        ch.prepareGraphics2D(g, true);
        Color mainSequence = new Color(255, 0, 0, 128);
        Color giant = new Color(0, 255, 0, 128);
        Color sgiant = new Color(0, 0, 255, 128);
        Color whiteDwarf = new Color(128, 128, 128, 128);
        hr.renderHRbranches(g, mainSequence, giant, sgiant, whiteDwarf);
        pic.show("HR diagram");
        /*
        pic.getCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                double pos[] = ch.getPhysicalUnits(e.getX(), e.getY());
                System.out.println("path.lineTo("+(float)pos[0]+", "+(float)pos[1]+");");
            }
        });
        */
        /*
        String data[] = DataSet.arrayListToStringArray(ReadFile.readAnyExternalFile("/home/alonso/kk.txt"));
        for (int i=0; i<data.length; i = i + 3) {
            int n1 = data[i].indexOf("(");
            int n2 = data[i].indexOf(")");
            String s1 = data[i].substring(n1+1, n2);
            double px1 = Double.parseDouble(FileIO.getField(1, s1, ",", false).trim());
            double py1 = Double.parseDouble(FileIO.getField(2, s1, ",", false).trim());

            n1 = data[i+1].indexOf("(");
            n2 = data[i+1].indexOf(")");
            s1 = data[i+1].substring(n1+1, n2);
            double px2 = Double.parseDouble(FileIO.getField(1, s1, ",", false).trim());
            double py2 = Double.parseDouble(FileIO.getField(2, s1, ",", false).trim());

            n1 = data[i+2].indexOf("(");
            n2 = data[i+2].indexOf(")");
            s1 = data[i+2].substring(n1+1, n2);
            double px3 = Double.parseDouble(FileIO.getField(1, s1, ",", false).trim());
            double py3 = Double.parseDouble(FileIO.getField(2, s1, ",", false).trim());
            System.out.println("path.curveTo("+(float)px1+", "+(float)py1+", "+(float)px2+", "+(float)py2+", "+(float)px3+", "+(float)py3+");");
        }
        */
    }
}
