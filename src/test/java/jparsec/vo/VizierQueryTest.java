package jparsec.vo;

import cds.savot.model.SavotVOTable;
import jparsec.graph.DataSet;
import jparsec.io.FileIO;

public class VizierQueryTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("VizierQuery test");
        String name = "Z CMa"; //"00 42 42.0 41 16 0.0"; //"MWC1080";

        try {
            //Frame frame = new Frame();
            //String votable = DataSet.vectorToString(ReadFile.readAnyExternalFile("./vvser_votable.txt"));
            //SavotVOTable sv = VizierQuery.toVOTable(votable);
            //String html = VizierQuery.createHTMLFromVOTable(sv, null, true);
            //WriteFile.writeAnyExternalFile("test.html", html);
            //ApplicationLauncher.launchDefaultViewer("test.html");
            //HTMLDialog dlg = new HTMLDialog(frame, "Vizier query results", html);
            //dlg.setModal(true);
            //dlg.setSize(400, 400);
            //dlg.setVisible(true);

            //VizierElement catalogToQuery = VizierElement.getVizierElement("I/284"); // USNO
            String doc = VizierQuery.query(name, null, 10, false);
            System.out.println(doc);

            SavotVOTable table = VizierQuery.toVOTable(doc);

            VizierElement[] vizier = VizierQuery.readVOTable(table, null);
            System.out.println("Obtained data from " + vizier.length + " catalogs");
            String sep = ", ", sep2 = FileIO.getLineSeparator() + "      ";

            for (int index = 0; index < vizier.length; index++) {
                System.out.println(vizier[index].catalogName + " (" + vizier[index].catalogAuthor + "): " + vizier[index].catalogDescription);
                System.out.println("   " + DataSet.toString(vizier[index].dataFields, sep));

                for (int i = 0; i < vizier[index].data.size(); i++) {
                    System.out.println("   " + DataSet.toString(vizier[index].data.get(i), sep));
                }

                String flux[] = vizier[index].getFluxes(null, true, true);
                System.out.println("      ALL FLUXES: " + sep2 + DataSet.toString(flux, sep2));
            }

            String html = VizierQuery.createHTMLFromVOTable(table, null, true);
            jparsec.io.WriteFile.writeAnyExternalFile("/home/alonso/test.html", html);
            jparsec.io.ApplicationLauncher.launchDefaultViewer("/home/alonso/test.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
