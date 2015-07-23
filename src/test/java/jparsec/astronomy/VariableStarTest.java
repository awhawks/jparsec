package jparsec.astronomy;

import jparsec.util.JPARSECException;

public class VariableStarTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        try {
            System.out.println("Variable Star test");
            int var_n = 11;
            String desig = VariableStar.getDesignation(var_n);
            int n = VariableStar.getVariableNumber(desig);
            System.out.println("Var " + var_n + " is " + desig);
            System.out.println("Var " + desig + " is " + n);
        } catch (JPARSECException e) {
            e.showException();
        }
    }
}
