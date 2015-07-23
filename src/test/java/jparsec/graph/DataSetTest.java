package jparsec.graph;

import jparsec.io.ConsoleReport;
import jparsec.math.FastMath;
import jparsec.util.JPARSECException;

public class DataSetTest {

    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws JPARSECException {
        System.out.println("DataSet test");

        String table[] = new String[] {
                "f1c1 f1c2 f1c3 f1c4",
                "f2c1 f2c2 f2c3 f2c4",
                "f3c1 f3c2 f3c3 f3c4",
                "f4c1 f4c2 f4c3 f4c4",
                "f5c1 f5c2 f5c3 f5c4",
                "f6c1 f6c2 f6c3 f6c4",
        };

        String separator = " ";
        int columnIndex = 1;
        String newColumn[] = DataSet.extractColumnFromTable(table, separator, columnIndex);
        table = DataSet.eliminateRowFromTable(table, 6);

        ConsoleReport.stringArrayReport(table);
        ConsoleReport.stringArrayReport(newColumn);
        ConsoleReport.stringArrayReport(table);
        table = DataSet.addColumnInTable(table, separator, newColumn, columnIndex);
        ConsoleReport.stringArrayReport(table);
        // Performance check for tryToConvertToDouble
        FastMath.initialize();
        int n = 100000;
        String value = "2.225073858507201E-318";
        //value = "+12345.6E+65";
        //value = "1.0";
        double sum = 0;
        long t1;
        long t0 = System.currentTimeMillis();

        for (int i = 0; i < n; i++) {
            try {
                sum += DataSet.parseDouble(value);
            } catch (Exception exc) {
            }
        }

        t1 = System.currentTimeMillis();
        System.out.println("Sum = " + sum + ", time = " + (float) ((t1 - t0) * 0.001) + "s");

        sum = 0.0;
        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            try {
                //if (DataSet.isDoubleFastCheck(value))
                sum += Double.parseDouble(value);
            } catch (Exception exc) {
            }
        }
        t1 = System.currentTimeMillis();
        System.out.println("Sum = " + sum + ", time = " + (float) ((t1 - t0) * 0.001) + "s");

        sum = 0.0;
        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            try {
                //if (DataSet.isDoubleFastCheck(value))
                sum += DataSet.parseDouble(value);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        t1 = System.currentTimeMillis();
        System.out.println("Sum = " + sum + ", time = " + (float) ((t1 - t0) * 0.001) + "s");

        sum = 0.0;
        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            try {
                //if (DataSet.isDoubleFastCheck(value))
                sum += Double.parseDouble(value);
            } catch (Exception exc) {
            }
        }
        t1 = System.currentTimeMillis();
        System.out.println("Sum = " + sum + ", time = " + (float) ((t1 - t0) * 0.001) + "s");

        sum = 0.0;
        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            try {
                //if (DataSet.isDoubleFastCheck(value))
                sum += DataSet.parseDouble(value);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        t1 = System.currentTimeMillis();
        System.out.println("Sum = " + sum + ", time = " + (float) ((t1 - t0) * 0.001) + "s");

        sum = 0.0;
        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            try {
                //if (DataSet.isDoubleFastCheck(value))
                sum += Double.parseDouble(value);
            } catch (Exception exc) {
            }
        }
        t1 = System.currentTimeMillis();
        System.out.println("Sum = " + sum + ", time = " + (float) ((t1 - t0) * 0.001) + "s");

        sum = 0.0;
        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            try {
                //if (DataSet.isDoubleFastCheck(value))
                sum += DataSet.parseDouble(value);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        t1 = System.currentTimeMillis();
        System.out.println("Sum = " + sum + ", time = " + (float) ((t1 - t0) * 0.001) + "s");

        sum = 0.0;
        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            try {
                //if (DataSet.isDoubleFastCheck(value))
                sum += Double.parseDouble(value);
            } catch (Exception exc) {
            }
        }
        t1 = System.currentTimeMillis();
        System.out.println("Sum = " + sum + ", time = " + (float) ((t1 - t0) * 0.001) + "s");
    }
}
