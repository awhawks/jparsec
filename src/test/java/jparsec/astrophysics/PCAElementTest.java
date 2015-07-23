package jparsec.astrophysics;

import jparsec.io.ConsoleReport;
import jparsec.io.FileIO;
import jparsec.math.matrix.Matrix;

import java.util.ArrayList;

public class PCAElementTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws Exception {
        // PCA test
        String dataset[] = new String[] {
                "x y",
                "2.5 2.4",
                "0.5 0.7",
                "2.2 2.9",
                "1.9 2.2",
                "3.1 3.0",
                "2.3 2.7",
                "2 1.6",
                "1 1.1",
                "1.5 1.6",
                "1.1 0.9"
        };

        boolean withZ = false;
        int f1 = 1;

    /*  dataset = DataSet.arrayListToStringArray(ReadFile.readAnyExternalFile("/home/alonso/eclipse/libreria_jparsec/PCA/SDSS_quasar.dat"));
        withZ = true;
        f1 ++;
        if (dataset.length > 100) dataset = DataSet.getSubArray(dataset, 0, 100);
    */
        double x[] = new double[dataset.length - 1];
        double y[] = new double[dataset.length - 1];
        double z[] = new double[dataset.length - 1];
        for (int i = 1; i < dataset.length; i++) {
            x[i - 1] = Double.parseDouble(FileIO.getField(f1, dataset[i], " ", true));
            y[i - 1] = Double.parseDouble(FileIO.getField(f1 + 1, dataset[i], " ", true));
            if (withZ) z[i - 1] = Double.parseDouble(FileIO.getField(f1 + 2, dataset[i], " ", true));
        }
        ArrayList<double[]> data = new ArrayList<double[]>();
        data.add(x);
        data.add(y);
        if (withZ) data.add(z);

        PCAElement pca = new PCAElement(data);
        System.out.println("Input data");
        System.out.println(new Matrix(pca.getOriginalData()).toString());
        System.out.println("U");
        System.out.println(new Matrix(pca.getSingularVectors()).toString());
        System.out.println("Singular values");
        System.out.println(new Matrix(pca.getSingularValues()).toString());
        System.out.println("V");
        System.out.println(pca.getSingularValueDecomposition().getV().toString());

        int n = 1;
        System.out.println("Data reproduced to " + n + " component/s");
        System.out.println(new Matrix(pca.reproduceOriginalData(n)).toString());
        pca.getChart("title", "x", "y", "z", "leyend", 1, 0, 2).showChart(500, 500);
        pca.reproduceOriginalDataAsChart(n, "Reproduced dataset n = " + n, "x", "y", "z", "leyend", 1, 0, 2).showChart(500, 500);

        System.out.println("NEW Xs");
        ConsoleReport.doubleArrayReport(pca.getNewValues(0), "f2.9");
        System.out.println("NEW Ys");
        ConsoleReport.doubleArrayReport(pca.getNewValues(1), "f2.9");
    }
}
