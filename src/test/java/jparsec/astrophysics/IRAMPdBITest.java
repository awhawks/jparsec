package jparsec.astrophysics;

import jparsec.math.Constant;

public class IRAMPdBITest {
    /**
     * A test program.
     *
     * @param args Unused.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("IRAMPdBI test");
        System.out.println("Factor " + (1.0 / IRAMPdBI.getFactorKToJy(Constant.SPEED_OF_LIGHT * 1000.0 / (89189 * 1E6), 5.20, 4.81)));
        IRAMPdBI.setConstants(2008);
        // IRAMPdBI.Na = 6;
        IRAMPdBI ir = new IRAMPdBI(IRAMPdBI.WAVE_3MM, "B", 8, 2, 80000);
        // IRAMPdBI ir = new IRAMPdBI(IRAMPdBI.WAVE_2MM, "C", 2, 1, 2.0E9); // Discos 0.7 mJy, pero mejor 0.86
        // IRAMPdBI ir = new IRAMPdBI(IRAMPdBI.WAVE_3MM, "CD", 6, 2, 40*1.0E3); // R Mon 6 mJy, a mi 7.6, pero mejor 9.3
        double rms = ir.getRMS_PdBI();
        // System.out.println(ir.Jpk);
        // System.out.println(ir.eta);
        // System.out.println(ir.Tsys);
        System.out.println("RMS (mJy/beam): " + (rms * 1000.0));

        // CN 2-1 y este distintos
        double lambda = Constant.SPEED_OF_LIGHT * 1.0E3 / (86.7 * 1.0E9);
        double bmajor = 5.91, bminor = 5.58;
        double mJyToK = (1.0 / (1000.0 * IRAMPdBI.getFactorKToJy(lambda, bmajor, bminor)));
        System.out.println("K/(mJy/beam): " + mJyToK);
    }
}
