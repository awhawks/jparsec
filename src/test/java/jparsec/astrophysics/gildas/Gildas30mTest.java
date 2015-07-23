package jparsec.astrophysics.gildas;

import jparsec.graph.CreateChart;

public class Gildas30mTest {
    /**
     * A test program.
     *
     * @param args Unused.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Gildas30m test");

        String path = "/home/alonso/reduccion/2006/hot_cores/n7129/n7129.30m";
        //path = "/home/alonso/colaboraciones/Asuncion/2011/figurasMuchasLineas_MonR2/broad-2-new.30m";
        //path = "/home/alonso/colaboraciones/Asuncion/2011/Herschel_OMC2_30mFiles/omc2.30m";
        path = "/home/alonso/documentos/latex/2014/m82/Cubos/spectraCN-Ha_reprojected.30m";
        // path = "/home/alonso/reduccion/2011/observacionesSep2010/PVCEP_reduced.30m";

        Gildas30m g30m = new Gildas30m(path);
        int list[] = g30m.getListOfSpectrums(true);

        System.out.println("List of scans (index position, scan number)");
        for (int i = 0; i < list.length; i++) {
            System.out.println(i + " = " + list[i]);
        }

        int index = 0; //list.length-1;
        Spectrum30m s = g30m.getSpectrum(list[index]);
        String keys[] = s.getKeys();
        Parameter p[] = s.getValues();

        System.out.println();
        System.out.println("List of keys (key name, value, description)");
        for (int i = 0; i < keys.length; i++) {
            System.out.println(keys[i] + " = " + p[i].value + " (" + p[i].description + ")");
        }

        // Header
        SpectrumHeader30m sh = s.getHeader();
        Parameter header[] = (Parameter[]) sh.getHeaderParameters();

        System.out.println();
        System.out.println("List of header parameters (description, value)");
        for (int i = 0; i < header.length; i++) {
            System.out.println(header[i].description + " = " + header[i].value);
        }

        // Create and show a chart
        CreateChart ch = s.getChart(400, 400, Spectrum30m.XUNIT.VELOCITY_KMS);
        ch.showChartInJFreeChartPanel();

        // Export
        // s.writeAsFITS("/home/alonso/myUselessFits.fits", true);
        s.writeAs30m("/home/alonso/myUseless.30m");
        Spectrum30m.writeAs30m(
                new Spectrum30m[] {
                        s.clone(),
                        s.clone(),
                        s.clone(),
                        s.clone(),
                        s.clone() },
                "/home/alonso/myUseless2.30m");

        // Now test the read process on the already created .fits file
        Spectrum30m s2 = new Spectrum30m();
        //s2.readFromFITS("/home/alonso/myUselessFits.fits");
        Gildas30m g30m2 = new Gildas30m("/home/alonso/myUseless.30m");
        s2 = g30m2.getSpectrum(g30m2.getListOfSpectrums(true)[0]);

        CreateChart ch2 = s2.getChart(400, 400, Spectrum30m.XUNIT.VELOCITY_KMS);
        ch2.showChartInJFreeChartPanel();

        keys = s2.getKeys();
        p = s2.getValues();

        System.out.println();
        System.out.println("List of keys (key name, value, description)");
        for (int i = 0; i < keys.length; i++) {
            System.out.println(keys[i] + " = " + p[i].value + " (" + p[i].description + ")");
        }

        // Header
        sh = s2.getHeader();
        header = (Parameter[]) sh.getHeaderParameters();

        System.out.println();
        System.out.println("List of header parameters (description, value)");
        for (int i = 0; i < header.length; i++) {
            System.out.println(header[i].description + " = " + header[i].value);
        }
    }
}
