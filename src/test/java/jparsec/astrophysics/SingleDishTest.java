package jparsec.astrophysics;

public class SingleDishTest {
    /**
     * A test program.
     *
     * @param args Unused.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("SingleDish test");

        SingleDish sd = new SingleDish(SingleDish.TELESCOPE.IRAM30m);
        double freq = 372.67251;
        System.out.println("Frequency (GHz): " + freq);
        System.out.println("Beam (\"): " + sd.getBeam(freq));
        //System.out.println("Jy/K in Ta ("+sd.name+"):  "+sd.getJyKInTa(freq));
        //System.out.println("Jy/K in Tmb ("+sd.name+"): "+sd.getJyKInTmb(freq));
        sd = new SingleDish(SingleDish.TELESCOPE.JCMT);
        System.out.println("Jy/K in Ta (" + sd.name + "):  " + sd.getJyKInTa(freq));
        System.out.println("Jy/K in Tmb (" + sd.name + "): " + sd.getJyKInTmb(freq));
        System.out.println("main beam eff (" + sd.name + "): " + sd.getBeamEfficiency(freq));
    }
}
