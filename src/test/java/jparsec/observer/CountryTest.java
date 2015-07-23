package jparsec.observer;

import jparsec.graph.DataSet;
import jparsec.io.FileIO;
import jparsec.io.ReadFile;
import jparsec.util.JPARSECException;

public class CountryTest {
    /**
     * For unit testing only
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Country Test");

        try {
            System.out.println("Spain is " + Country.getID("Spain"));
            System.out.println("Spain is " + Country.getID("Spain").toString());

            String line[] = DataSet.arrayListToStringArray(ReadFile.readAnyExternalFile("/home/alonso/eclipse/workspace/jparsec/jparsec/observer/Country.java"));
            for (int i = 0; i < line.length; i++) {
                int n = line[i].indexOf("public static final int");
                if (n >= 0) System.out.println(FileIO.getField(5, line[i], " ", true) + ",");
                n = line[i].indexOf("Constant ID");
                if (n >= 0) System.out.println("/** " + line[i].substring(n).trim() + " */");
            }
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
