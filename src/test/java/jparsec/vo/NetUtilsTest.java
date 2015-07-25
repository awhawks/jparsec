package jparsec.vo;

import jparsec.util.JPARSECException;

public class NetUtilsTest {
    /**
     * A test program.
     *
     * @param args Unused.
     */
    public static void main(String args[]) {
        System.out.println("NetUtils test");

        try {
            System.out.println(NetUtils.getLocalHostName());
            System.out.println(NetUtils.getLocalHostAddress());
        } catch (JPARSECException exc) {
            exc.showException();
        }
    }
}
