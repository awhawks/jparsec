package jparsec.math;

import java.awt.image.BufferedImage;
import jparsec.io.image.Picture;

public class LATEXFormulaTest {
    /**
     * Test program.
     *
     * @param args Unused.
     * @throws Exception If an error occurs.
     */
    public static void main(String[] args) throws Exception {
        String expression = LATEXFormula.getIntegral("t=0", "2pi") +
            LATEXFormula.getFraction("sqrt(t)",
            LATEXFormula.getBetweenParentesis("1+cos^2(t)")) + " dt";
        LATEXFormula formula = new LATEXFormula(expression, LATEXFormula.STYLE_DISPLAY, LATEXFormula.SIZE_HUGE);
        // Or, alternatively
        //formula.setCode("\\int_{t=0}^{2\\pi}\\frac{\\sqrt{t}}{(1+\\mathrm{cos}^2{t})}\\nbspdt");

        System.out.println(formula.getCode());
        BufferedImage image = formula.getAsImage();
        Picture p = new Picture(image);
        p.show("LATEX formula");
        p.write("/home/alonso/formula.png");
    }
}
