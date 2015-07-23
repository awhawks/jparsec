package jparsec.astrophysics;

import jparsec.astronomy.Difraction;
import jparsec.astronomy.TelescopeElement;
import jparsec.graph.DataSet;
import jparsec.io.FileFormatElement;
import jparsec.math.matrix.Matrix;

public class TableTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Table test");
        Table table1 = new Table(new double[] { 1, 2, 3, 4 }, "s");
        Table table2 = new Table(new double[] { -1, -2, -3, -4 }, "hr");

        table1.addMask(new boolean[] { false, false, false, true });
        System.out.println(table1.toString());
        table1.add(table2);

        System.out.println(table1.toString());
        System.out.println(table1.equals(table2));
        // 2d test of formatting
        double[][] p = Difraction.pattern(TelescopeElement.SCHMIDT_CASSEGRAIN_20cm, 3);
        table1 = new Table(p, "Jy");
        String table1s = table1.toString(" ", "f1.2");
        table1s = DataSet.replaceAll(table1s, ".00", "   ", false);
        System.out.println(table1s);
        System.out.println("Same using Matrix class ...");
        Matrix m = new Matrix(p);
        System.out.println(m.toString());
        System.out.println("Same using FileFormatElement ...");
        FileFormatElement format[] = new FileFormatElement[p[0].length];
        for (int i = 0; i < format.length; i++) {
            int a = i * 5 + 1, b = a + 3;
            format[i] = new FileFormatElement(a, b, "field" + i);
        }
        table1s = table1.toString(format);
        table1s = DataSet.replaceAll(table1s, ".00", "   ", false);
        System.out.println(table1s);

        //table1.set(15, 15, 0, new MeasureElement(1.0, 0.1, "Jy"));
        int max[] = table1.getMaximumIndex(true);
        System.out.println("Max is " + table1.getMaximum() + " at " + max[0] + ", " + max[1] + ", " + max[2]);
        int min[] = table1.getMinimumIndex(true);
        System.out.println("Min is " + table1.getMinimum() + " at " + min[0] + ", " + min[1] + ", " + min[2]);

        for (double r = 1; r < 15; r++) {
            Table ring = table1.getFluxAround(15, 15, r - 1, r, false);
            System.out.println("Total flux in ring from " + (r - 1) + " to " + r + ": " + ring.get(0, 0, 0));
        }

        double x = 15, y = 15, z = 0;
        MeasureElement val = table1.interpolate(x, y, z);
        System.out.println("val(" + x + "," + y + "," + z + ") = " + val.toString());

        table1.resample(7, 7, 1);
        table1s = table1.toString(" ", "f1.2");
        table1s = DataSet.replaceAll(table1s, ".00", "   ", false);
        System.out.println(table1s);

/*      // Test with 2d image
        String s = "/home/alonso/java/librerias/masymas/tres-3/TRES-3-025-070725-.fit";
        FitsIO fio = new FitsIO(s);
        int imageNumber = 0;
        short data[][] = (short[][]) fio.getData(imageNumber);
        Picture p1 = fio.getPicture(imageNumber, PICTURE_LEVEL.LINEAR_INTERPOLATION, true);
        p1.show("my fits");
        table1 = new Table(new int[][][] {DataSet.toIntArray(data, (int)fio.getBZero(imageNumber))}, "Jy");
        table2 = table1.clone();
        System.out.println("Maximum table1: "+table1.getMaximum().toString());
        System.out.println("Maximum table2: "+table2.getMaximum().toString());
        System.out.println("table1 = table2 ? "+table1.equals(table2));
        table1.subtract(table2);
        fio.setData(DataSet.toShortArray(table1.getValues()[0]), imageNumber);
        Picture p2 = fio.getPicture(imageNumber, PICTURE_LEVEL.LINEAR_INTERPOLATION, true);
        p2.show("my modified fits");
        System.out.println("Maximum table1: "+table1.getMaximum().toString());
        System.out.println("Maximum table2: "+table2.getMaximum().toString());
        System.out.println("table1 = table2 ? "+table1.equals(table2));
*/
    }

}
