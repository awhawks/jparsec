package jparsec.vo;

import jparsec.graph.CreateChart;
import jparsec.io.ConsoleReport;
import jparsec.io.FileIO;
import jparsec.util.Translate;

public class SiessTest {
    /**
     * A test program.
     *
     * @param args Unused.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Siess test");
        Translate.setDefaultLanguage(Translate.LANGUAGE.SPANISH);
        CreateChart ch = Siess.getTrack(3.0, 7.0, Siess.PHASE_PRE_MS, Siess.FIELD_EFFECTIVE_TEMPERATURE, Siess.FIELD_LUMINOSITY, 500, 500);

        String x[] = new String[] { "11220", "15000" };
        String y[] = new String[] { "5300", "22000" };
        Siess.addSeriesToTrack(ch, "Our sample", x, y, null, null);

        ch.showChartInJFreeChartPanel();
        Siess siess = new Siess("3.5");
        System.out.println("age 1: " + FileIO.getField(Siess.FIELD_AGE, siess.getModels()[0], " ", true));

        double luminosity = 300, effectiveTemperature = 11220, lumTolerance = 50, tempTolerance = 200;
        String tracks[] = Siess.getClosestEvolutionaryTracks(luminosity, lumTolerance, effectiveTemperature, tempTolerance);
        if (tracks != null) ConsoleReport.stringArrayReport(tracks);
    }
}
