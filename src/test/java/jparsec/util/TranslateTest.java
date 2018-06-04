package jparsec.util;

import jparsec.graph.DataSet;
import jparsec.io.ReadFile;

public class TranslateTest {
    /**
     * Test program.
     *
     * @param args Unused.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Translate test");
        String translate[] = DataSet.arrayListToStringArray(ReadFile.readResource("jparsec/util/english.txt"));
        int index = DataSet.getIndex(translate, "Position angle");
        System.out.println(index);
    }
}
