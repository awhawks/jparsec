package jparsec.io;

import jparsec.astrophysics.Spectrum;
import jparsec.astrophysics.gildas.Spectrum30m;
import jparsec.graph.CreateChart;

public class LATEXReportTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("LatexReport test");

        // Obtain the synthetic output spectrum
        Spectrum s = LATEXReport.calculate(1.0, 2.0, 3.0);

        // Operate with the output spectrum
        String output = "/home/alonso/myModel";
        Spectrum30m s30m = new Spectrum30m(s);
        //s30m.writeAs30m(output+".30m"); // To write the spectrum as a .30m file for GILDAS software
        CreateChart chart = s30m.getChart(800, 600, Spectrum30m.XUNIT.VELOCITY_KMS);
        chart.showChartWithMenu();
        chart.chartAsPNGFile(output + ".png");

        // Export results to LATEX
        LATEXReport latex = new LATEXReport();

        String title = "Results from my model";
        latex.writeHeader(title);
        latex.beginBody();

        latex.writeBigSkip();
        latex.writeParagraph("This paragraph is to describe my model and input parameters...");

        // Simple way, without alignment of decimal places in the numbers
        latex.writeLongTableHeader("This is my table", "lll");
        latex.writeRowInTable(new String[] { "Channel", "Velocity", "Flux" }, null, "LEFT", null);
        latex.writeRowInTable(new String[] { "#", "(km/s)", "(K)" }, null, "LEFT", null);
        latex.writeHorizontalLine();
        for (int i = 0; i < s30m.getSpectrumData().length; i++) {
            String columns[] = new String[] { "" + (i + 1), s.spectrum[i].x.toString(true, false), s.spectrum[i].y.toString(true, false) };
            latex.writeRowInTable(columns, null, "LEFT", null);
        }

        // Better way, with alignment. Note this is a terrible hack since the correct way to do this
        // in Latex is using the new siunitx package and the S type column
/*
        latex.writeLongTableHeader("This is my table", "lr@{}lr@{}lr@{}lr@{}l");
        latex.writeRowInTable(new String[] {"Channel #", "", "Velocity", " (km/s)", "", "", "Flux", " (K)", ""}, null, "LEFT", null);
        latex.writeHorizontalLine();
        for (int i=0; i<s30m.getSpectrumData().length; i++) {
            String c2 = s.spectrum[i].x.toString(true, false), c3 = s.spectrum[i].y.toString(true, false);
            String val2 = FileIO.getField(1, c2, " ", false).trim(), err2 = FileIO.getField(2, c2, " ", false).trim();
            String val3 = FileIO.getField(1, c3, " ", false).trim(), err3 = FileIO.getField(2, c3, " ", false).trim();
            String columns[] = new String[] {""+(i+1),
                    val2.substring(0, val2.indexOf(".")+1), val2.substring(val2.indexOf(".")+1),
                    err2.substring(0, err2.indexOf(".")+1), err2.substring(err2.indexOf(".")+1),
                    val3.substring(0, val3.indexOf(".")+1), val3.substring(val3.indexOf(".")+1),
                    err3.substring(0, err3.indexOf(".")+1), err3.substring(err3.indexOf(".")+1)};
            latex.writeRowInTable(columns, null, "LEFT", null);
        }
*/
        latex.endLongTable("table1");
        latex.writeImageWithCaption("100%", "80%", "CENTER", output + ".png", "Model chart", "chart");
        latex.endBody();
        latex.endDocument();
        WriteFile.writeAnyExternalFile(output + ".tex", latex.getCode());

        // Create pdf file.
        LATEXReport.compileLatexToPDF(output + ".tex");

        // Show file
        ApplicationLauncher.launchDefaultViewer(output + ".pdf");
    }
}
