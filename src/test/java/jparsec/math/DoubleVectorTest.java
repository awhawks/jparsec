package jparsec.math;

public class DoubleVectorTest {
    /**
     * Test program.
     *
     * @param args Unused.
     */
    public static void main(String args[]) {
        DoubleVector u = DoubleVector.random(10);
        DoubleVector v = DoubleVector.random(10);
        DoubleVector a = DoubleVector.random(10);
        DoubleVector w = a;

        System.out.println(DoubleVector.random(10).plus(v).plus(w));
        System.out.println(u.equals(v));
        System.out.println(u.equals(u));
        System.out.println(u.equals(u.copy()));
    }
}
