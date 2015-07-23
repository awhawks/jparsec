package jparsec.io;

import java.net.URL;
import jparsec.graph.DataSet;
import jparsec.util.JPARSECException;

public class ReadFileTest {
    /**
     * Example program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        try {
            if (args != null && args.length > 0) {
                String data[];
                if (args[0].startsWith("http")) {
                    data = DataSet.arrayListToStringArray(ReadFile.readAnyExternalFile(new URL(args[0]), "UTF-8"));
                } else {
                    data = DataSet.arrayListToStringArray(ReadFile.readAnyExternalFile(args[0]));
                }
                ConsoleReport.stringArrayReport(data);
                /*
                SortedMap sm = Charset.availableCharsets();
                Iterator it = sm.keySet().iterator();
                for (int i=0; i<sm.size(); i++) {
                    System.out.println(it.next().toString());
                }
                */
            }
        } catch (Exception exc) {
            System.out.println(DataSet.toString(JPARSECException.toStringArray(exc.getStackTrace()), FileIO.getLineSeparator()));
        }
    }
}
