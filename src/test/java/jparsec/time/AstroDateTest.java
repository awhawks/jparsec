package jparsec.time;

import jparsec.util.JPARSECException;

public class AstroDateTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("AstroDate test");
        // Note that year -1001 = 1001 B.C., and -4713 = 4713 B.C.
        // Year 0 does not exist, will produce error

        try {
            AstroDate astro = new AstroDate(-12000, 1, 1);
            System.out.println(astro.jd() + '/' + astro.toStandarizedString(null) + '/' + astro.toString(0));
            AstroDate astro2 = new AstroDate(astro.toStandarizedString(null));
            System.out.println(astro2.jd() + '/' + astro2.toStandarizedString(null) + '/' + astro2.toString(0));
            System.out.println(astro.getYear() + '/' + astro.getAstronomicalYear());
        } catch (JPARSECException ve) {
            //JPARSECException.showException(ve);
            System.out.println(ve.getStackTraceDetails());
        }
    }
}
