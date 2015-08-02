package jparsec.astrophysics.gildas;

import jparsec.time.AstroDate;

public class IEEE2EEEITest {
    /**
     * Testing program
     *
     * @param args Unused.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("IEEE2EEEI test");
        int i = 0;

        IEEE2EEEI v = new IEEE2EEEI();
        byte[] abyte0 = new byte[4];
        int valueI = 1234;
        v.writeInt(abyte0, i, valueI);
        int valueI2 = v.readInt(abyte0, i);
        System.out.println(valueI + " = " + valueI2 + " / " + abyte0[0] + '/'+ abyte0[1] + '/'+ abyte0[2] + '/'+ abyte0[3]);

        float valueF = 1234.1f;
        v.writeFloat(abyte0, i, valueF);
        float valueF2 = v.readFloat(abyte0, i);
        System.out.println(valueF + " = " + valueF2 + " / " + abyte0[0] + '/'+ abyte0[1] + '/'+ abyte0[2] + '/'+ abyte0[3]);

        byte[] abyte1 = new byte[8];
        double valueD = 1234.1234;
        v.writeDouble(abyte1, i, valueD);
        double valueD2 = v.readDouble(abyte1, i);
        System.out.println(valueD + " = " + valueD2 + " / " + abyte1[0] + '/'+ abyte1[1] + '/'+ abyte1[2] + '/'+ abyte1[3]);

        byte[] abyte2 = new byte[2];
        short valueS = 123;
        v.writeShort(abyte2, i, valueS);
        Short valueS2 = v.readShort(abyte2, i);
        System.out.println(valueS + " = " + valueS2 + " / " + abyte2[0] + '/'+ abyte2[1]);

        valueD = 2451545.45;
        v.writeDate(abyte0, i, valueD);
        double valueD3 = new AstroDate(v.readDate(abyte0, i)).jd();
        System.out.println(valueD + " = " + valueD3 + " / " + abyte0[0] + '/'+ abyte0[1] + '/'+ abyte0[2] + '/'+ abyte0[3]);
    }
}
