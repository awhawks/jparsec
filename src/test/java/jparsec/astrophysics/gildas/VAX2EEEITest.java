package jparsec.astrophysics.gildas;

import jparsec.time.AstroDate;

public class VAX2EEEITest {
    /**
     * Testing program
     *
     * @param args Unused.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("VAX2EEEI test");
        VAX2EEEI v = new VAX2EEEI();
        byte[] abyte0 = new byte[4];
        byte[] abyte2 = new byte[2];
        byte[] abyteD = new byte[8];
        int i = 0;
        int valueI = 1234;
        float valueF = 1f;
        double valueD = 1234.1234;
        short valueS = 123;

        v.writeInt(abyte0, i, valueI);
        int valueI2 = v.readInt(abyte0, i);
        System.out.println(valueI + " = " + valueI2 + " / " + abyte0[0] + "/" + abyte0[1] + "/" + abyte0[2] + "/" + abyte0[3]);

        v.writeFloat(abyte0, i, valueF);
        float valueF2 = v.readFloat(abyte0, i);
        System.out.println(valueF + " = " + valueF2 + " / " + abyte0[0] + "/" + abyte0[1] + "/" + abyte0[2] + "/" + abyte0[3]);

        v.writeDouble(abyteD, i, valueD);
        double valueD2 = v.readDouble(abyteD, i);
        System.out.println(valueD + " = (approx) " + valueD2 + " / " + abyteD[0] + "/" + abyteD[1] + "/" + abyteD[2] + "/" + abyteD[3] + "/" + abyteD[4] + "/" + abyteD[5] + "/" + abyteD[6] + "/" + abyteD[7]);

        v.writeShort(abyte2, i, valueS);
        Short valueS2 = v.readShort(abyte2, i);
        System.out.println(valueS + " = " + valueS2 + " / " + abyte2[0] + "/" + abyte2[1]);

        valueD = 2451545.45;
        v.writeDate(abyte0, i, valueD);
        AstroDate ad = new AstroDate(v.readDate(abyte0, i));
        double valueD3 = ad.jd();
        System.out.println(valueD + " = " + valueD3 + " / " + ad.toString() + "/" + abyte0[0] + "/" + abyte0[1] + "/" + abyte0[2] + "/" + abyte0[3]);
    }
}
