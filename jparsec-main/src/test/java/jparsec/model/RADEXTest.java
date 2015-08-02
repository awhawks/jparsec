package jparsec.model;

import jparsec.ephem.Functions;
import jparsec.graph.DataSet;
import jparsec.math.CGSConstant;
import jparsec.util.JPARSECException;

public class RADEXTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("RADEX test");

        try {
            int hco = DataSet.getIndex(RADEX.MOLECULE_ATOM_NAMES, "HCO+");
            // mol, col den, line width. tkin, Tbg, nu min, nu max, nro partn=1;
            // partner id, col den partn, geometry

            RADEX radex = new RADEX(hco, 1.0E13, 1, 20, 2.73, 88, 500.0,
                    RADEX.PARTNER.H2, 1.0e4, RADEX.METHOD.UNIFORM_SPHERE, true);

            // N2D+ 2-1: LTE-NLTE en funcion de densidad de colisionantes => (rho baja) *15 con 1e3, *7 1e4, *4 1e5. (rho alta) *7, *5, *2.
            // N2H+ 1-0: LTE-NLTE en funcion de densidad de colisionantes => (rho baja) *20 con 1e3, *15 1e4, *3 1e5. (rho alta) *10, *4, *1.5.
            // C18O 1-0: LTE-NLTE en funcion de densidad de colisionantes => (rho baja) *3 con 1e3, *1 1e4, *1 1e5. (rho alta) *3, *1, *1.

            /*
            double nu = 154.219E9;
            System.out.println(radex.tkin+"/"+radex.getExcitationTemperature(1));
            System.out.println(GasFunctions.planck(radex.tkin, nu)+"/"+GasFunctions.planck(radex.getExcitationTemperature(1), nu));
            */
            String sep = "   \t";
            int ndecimals = 3;
            System.out.println("Line" + sep + "Eup   " + sep + "Frequency" + sep + "Wavelength" + sep + "Tex   " + sep + "Opacity" + sep + "Tantenna" + sep + "Flux   " + sep + "Flux in CGS");

            for (int i = 0; i < radex.getNumberOfTransitions(); i++) {
                double wavel = CGSConstant.SPEED_OF_LIGHT / radex.getFrequency(i) / 1.0E5; // unit =  micron
                double ergs = radex.getFluxInCGS(i);

                String element = radex.getName(i) + sep;
                element += Functions.formatValue(radex.getUpperLevelEnergy(i), ndecimals) + sep;
                element += Functions.formatValue(radex.getFrequency(i), ndecimals) + sep;
                element += Functions.formatValue(wavel, ndecimals) + sep;
                element += Functions.formatValue(radex.getExcitationTemperature(i), ndecimals) + sep;
                element += Functions.formatValue(radex.getOpacity(i), ndecimals) + sep;
                element += Functions.formatValue(radex.getAntennaTemperature(i), ndecimals) + sep;
                element += Functions.formatValue(radex.getFlux(i), ndecimals) + sep;
                element += ergs; //Functions.formatVALUE(ergs, ndecimals);
                System.out.println(element);
            }

            JPARSECException.showWarnings();
            radex.update();
            System.out.println("Line" + sep + "Eup   " + sep + "Frequency" + sep + "Wavelength" + sep + "Tex   " + sep + "Opacity" + sep + "Tantenna" + sep + "Flux   " + sep + "Flux in CGS");

            for (int i = 0; i < radex.getNumberOfTransitions(); i++) {
                double wavel = CGSConstant.SPEED_OF_LIGHT / radex.getFrequency(i) / 1.0E5; // unit =  micron
                double ergs = radex.getFluxInCGS(i);

                String element = radex.getName(i) + sep;
                element += Functions.formatValue(radex.getUpperLevelEnergy(i), ndecimals) + sep;
                element += Functions.formatValue(radex.getFrequency(i), ndecimals) + sep;
                element += Functions.formatValue(wavel, ndecimals) + sep;
                element += Functions.formatValue(radex.getExcitationTemperature(i), ndecimals) + sep;
                element += Functions.formatValue(radex.getOpacity(i), ndecimals) + sep;
                element += Functions.formatValue(radex.getAntennaTemperature(i), ndecimals) + sep;
                element += Functions.formatValue(radex.getFlux(i), ndecimals) + sep;
                element += ergs; //Functions.formatVALUE(ergs, ndecimals);
                System.out.println(element);
            }

            JPARSECException.showWarnings();
        } catch (JPARSECException e) {
            e.showException();
        }
    }
}
