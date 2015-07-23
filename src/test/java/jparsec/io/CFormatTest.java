package jparsec.io;

import jparsec.util.JPARSECException;

public class CFormatTest {
    /**
     * A test stub for the format class
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        try {
            double x = 1.23456789012;
            double y = 123;
            double z = 1.2345e30;
            double w = 1.02;
            double u = 1.234e-5;
            int d = 0xCAFE;
            CFormat.print(System.out, "x = |%f|\n", x);
            CFormat.print(System.out, "u = |%20f|\n", u);
            CFormat.print(System.out, "x = |% .5f|\n", x);
            CFormat.print(System.out, "w = |%20.5f|\n", w);
            CFormat.print(System.out, "x = |%020.5f|\n", x);
            CFormat.print(System.out, "x = |%+20.5f|\n", x);
            CFormat.print(System.out, "x = |%+020.5f|\n", x);
            CFormat.print(System.out, "x = |% 020.5f|\n", x);
            CFormat.print(System.out, "y = |%#+20.5f|\n", y);
            CFormat.print(System.out, "y = |%-+20.5f|\n", y);
            CFormat.print(System.out, "z = |%20.5f|\n", z);

            CFormat.print(System.out, "x = |%e|\n", x);
            CFormat.print(System.out, "u = |%20e|\n", u);
            CFormat.print(System.out, "x = |% .5e|\n", x);
            CFormat.print(System.out, "w = |%20.5e|\n", w);
            CFormat.print(System.out, "x = |%020.5e|\n", x);
            CFormat.print(System.out, "x = |%+20.5e|\n", x);
            CFormat.print(System.out, "x = |%+020.5e|\n", x);
            CFormat.print(System.out, "x = |% 020.5e|\n", x);
            CFormat.print(System.out, "y = |%#+20.5e|\n", y);
            CFormat.print(System.out, "y = |%-+20.5e|\n", y);

            CFormat.print(System.out, "x = |%g|\n", x);
            CFormat.print(System.out, "z = |%g|\n", z);
            CFormat.print(System.out, "w = |%g|\n", w);
            CFormat.print(System.out, "u = |%g|\n", u);
            CFormat.print(System.out, "y = |%.2g|\n", y);
            CFormat.print(System.out, "y = |%#.2g|\n", y);

            CFormat.print(System.out, "d = |%d|\n", d);
            CFormat.print(System.out, "d = |%20d|\n", d);
            CFormat.print(System.out, "d = |%020d|\n", d);
            CFormat.print(System.out, "d = |%+20d|\n", d);
            CFormat.print(System.out, "d = |% 020d|\n", d);
            CFormat.print(System.out, "d = |%-20d|\n", d);
            CFormat.print(System.out, "d = |%20.8d|\n", d);
            CFormat.print(System.out, "d = |%x|\n", d);
            CFormat.print(System.out, "d = |%20X|\n", d);
            CFormat.print(System.out, "d = |%#20x|\n", d);
            CFormat.print(System.out, "d = |%020X|\n", d);
            CFormat.print(System.out, "d = |%20.8x|\n", d);
            CFormat.print(System.out, "d = |%o|\n", d);
            CFormat.print(System.out, "d = |%020o|\n", d);
            CFormat.print(System.out, "d = |%#20o|\n", d);
            CFormat.print(System.out, "d = |%#020o|\n", d);
            CFormat.print(System.out, "d = |%20.12o|\n", d);

            CFormat.print(System.out, "s = |%-20s|\n", "Hello");
            CFormat.print(System.out, "s = |%-20c|\n", '!');
        } catch (JPARSECException exc) {
            exc.showException();
        }
    }
}
