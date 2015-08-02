package jparsec.astrophysics.gildas;

public class ConverterFactoryTest {
    /**
     * Test program.
     *
     * @param args Unused.
     */
    public static void main(String args[]) {
        System.out.println("ConverterFactory test");

        double jd = 2453163.5;
        System.out.println("" + jd + " -> " + ConverterFactory.getGILDASdate(jd));
        jd = 2453163.7125212005;
        System.out.println("" + jd + " -> " + ConverterFactory.getGILDASdate(jd));
    }
}
