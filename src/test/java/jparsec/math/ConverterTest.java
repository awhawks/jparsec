package jparsec.math;

import jparsec.util.JPARSECException;

public class ConverterTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Converter test");

        try {
            Converter c = new Converter("pc", "AU");
            double val = c.convert(10);
            System.out.println(val);
            System.out.println("Correct value: " + (10 * Constant.PARSEC / (1000.0 * Constant.AU)));
            System.out.println(c.getTargetUnit().explainUnit());

            String all[] = Converter.getAllSymbolsWithExplanations();
            for (int i = 0; i < all.length; i++) {
                System.out.println(all[i]);
            }
        } catch (JPARSECException e) {
            e.showException();
        }
    }
}
