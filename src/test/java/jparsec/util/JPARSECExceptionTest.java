package jparsec.util;

public class JPARSECExceptionTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("JPARSECException test");

        try {
            JPARSECException.addWarning("warning from main");
            System.out.println(JPARSECException.getWarnings());

            throw new JPARSECException("exception from main");
        } catch (JPARSECException jpe) {
            jpe.showException();
        }
    }
}
