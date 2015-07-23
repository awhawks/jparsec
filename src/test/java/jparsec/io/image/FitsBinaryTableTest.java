package jparsec.io.image;

import jparsec.astrophysics.Table;
import jparsec.graph.DataSet;
import jparsec.io.ConsoleReport;

public class FitsBinaryTableTest {
    /**
     * Test program: write/read binary tables from string tables.
     *
     * @param args Unused.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("FitsBinaryTable Test");
        ImageHeaderElement primaryHeader[] = FitsBinaryTable.parseHeader(new String[] {
                "SIMPLE  L  T",
                "NAXIS  I  0",
                "BITPIX  I  32",
                "EXTEND  L  T",
                "TELESCOP 13A OAN", // format is entry name, entry format (fits convention), value, / comment (optional)
                "ORIGIN A OAN",
                "CREATOR A T. Alonso Albi - OAN, JPARSEC-package",
                "COMMENT A Testing phase",
                "OBSERVER  A  TomÃ¡s"
        });

        ImageHeaderElement header[] = FitsBinaryTable.parseHeader(new String[] {
                "EXTNAME  A  MY TABLE",
                "TABLEREV  I  1"
        });
        String table[][] = new String[][] {
                //new String[] {"col1", "col2", "col3", "col4", "col5"},
                //new String[] {"col1row1", "col2row1", "col3row1", "col4row1", "col5row1"},
                //new String[] {"col1row2", "col2row2", "col3row2", "col4row2", "col5row2"},
                //new String[] {"col1row3", "col2row3", "col3row3", "col4row3", "col5row3"}
                new String[] { "1", "2", "3", "4", "5" },
                new String[] { "11", "21", "31", "41", "51" },
                new String[] { "12", "22", "32", "42", "52" },
                new String[] { "13", "23", "33", "43", "53" }
        };

        FitsIO f = new FitsIO(FitsIO.createHDU(null, primaryHeader)); // First HDU must be an image, never a table
        f.addHDU(FitsBinaryTable.createBinaryTable(header, table));

        int n = f.getNumberOfPlains();
        System.out.println();
        System.out.println("Found " + n + " HDUs:");
        System.out.println(f.toString());

        for (int i = 0; i < n; i++) {
            System.out.println("HDU # " + (i + 1));
            header = f.getHeader(i);
            System.out.println(ImageHeaderElement.toString(header));

            if (f.isBinaryTable(i)) {
                table = FitsBinaryTable.getBinaryTable(f.getHDU(i));
                ConsoleReport.stringArrayReport(table, table[0].length + "a15");

                // Same using Table
                Table t = new Table(DataSet.toDoubleValues(table), null);
                System.out.println(t.toString());
            }
            if (f.isAsciiTable(i)) {
                table = FitsBinaryTable.getAsciiTable(f.getHDU(i));
                ConsoleReport.stringArrayReport(table, table[0].length + "a15");
            }
        }
    }
}
