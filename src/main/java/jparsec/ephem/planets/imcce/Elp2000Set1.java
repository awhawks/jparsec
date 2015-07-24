package jparsec.ephem.planets.imcce;

class Elp2000Set1 {
    int[] ILU = new int[4];
    double[] COEF = new double[7];

    Elp2000Set1(int ilu[], double[] coef) {
        ILU = ilu;
        COEF = coef;
    }
}
