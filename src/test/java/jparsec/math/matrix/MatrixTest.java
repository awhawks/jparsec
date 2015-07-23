package jparsec.math.matrix;

import java.io.StringReader;
import java.io.StringWriter;
import jparsec.graph.DataSet;

public class MatrixTest {

    /**
     * Main method for testing this class.
     *
     * @param args Not used.
     */
    public static void main(String[] args) throws Exception {
        Matrix I;
        Matrix A;
        Matrix B;

        // Identity
        System.out.println("\nIdentity\n");
        I = Matrix.identity(3, 5);
        System.out.println("I(3,5)\n" + I);

        // basic operations - square
        System.out.println("\nbasic operations - square\n");
        A = Matrix.random(3, 3);
        B = Matrix.random(3, 3);
        System.out.println("A\n" + A);
        System.out.println("B\n" + B);
        System.out.println("A'\n" + A.inverse());
        System.out.println("A^T\n" + A.transpose());
        System.out.println("A+B\n" + A.plus(B));
        System.out.println("A*B\n" + A.times(B));
        System.out.println("X from A*X=B\n" + A.solve(B));

        // basic operations - non square
        System.out.println("\nbasic operations - non square\n");
        A = Matrix.random(2, 3);
        B = Matrix.random(3, 4);
        System.out.println("A\n" + A);
        System.out.println("B\n" + B);
        System.out.println("A*B\n" + A.times(B));

        // sqrt
        System.out.println("\nsqrt (1)\n");
        A = new Matrix(new double[][] { { 5, -4, 1, 0, 0 }, { -4, 6, -4, 1, 0 }, { 1, -4, 6, -4, 1 }, { 0, 1, -4, 6, -4 }, { 0, 0, 1, -4, 5 } });
        System.out.println("A\n" + A);
        System.out.println("sqrt(A)\n" + A.sqrt());

        // sqrt
        System.out.println("\nsqrt (2)\n");
        A = new Matrix(new double[][] { { 7, 10 }, { 15, 22 } });
        System.out.println("A\n" + A);
        System.out.println("sqrt(A)\n" + A.sqrt());
        System.out.println("det(A)\n" + A.det() + "\n");

        // eigenvalue decomp.
        System.out.println("\nEigenvalue Decomposition\n");
        EigenvalueDecomposition evd = A.eig();
        System.out.println("[V,D] = eig(A)");
        System.out.println("- V\n" + evd.getV());
        System.out.println("- D\n" + evd.getD());

        // LU decomp.
        A = Matrix.random(3, 3);
        System.out.println("\nLU Decomposition\n");
        LUDecomposition lud = A.lu();
        System.out.println("[L,U,P] = lu(A)");
        System.out.println("- L\n" + lud.getL());
        System.out.println("- U\n" + lud.getU());
        System.out.println("- P\n" + DataSet.arrayToString(lud.getPivot()) + "\n");

        // writer/reader
        System.out.println("\nWriter/Reader\n");
        StringWriter writer = new StringWriter();
        A.write(writer);
        System.out.println("A.write(Writer)\n" + writer);
        A = new Matrix(new StringReader(writer.toString()));
        System.out.println("A = new Matrix.read(Reader)\n" + A);

        // Matlab
        System.out.println("\nMatlab-Format\n");
        String matlab = "[ 1   2;3 4 ]";
        System.out.println("Matlab: " + matlab);
        System.out.println("from Matlab:\n" + Matrix.parseMatlab(matlab));
        System.out.println("to Matlab:\n" + Matrix.parseMatlab(matlab).toMatlab());
        matlab = "[1 2 3 4;3 4 5 6;7 8 9 10]";
        System.out.println("Matlab: " + matlab);
        System.out.println("from Matlab:\n" + Matrix.parseMatlab(matlab));
        System.out.println("to Matlab:\n" + Matrix.parseMatlab(matlab).toMatlab() + "\n");
    }
}
