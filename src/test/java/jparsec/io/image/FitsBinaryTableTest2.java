package jparsec.io.image;

import nom.tam.fits.BinaryTableHDU;

public class FitsBinaryTableTest2 {
    /**
     * Test program 2: file reading.
     *
     * @param args Unused.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("FitsBinaryTable test 2");
        // Scan TEST0700.FITS with 433 HDUs = primary header + 2 (datapar + holodata) * 216 observations
        String path = "/home/alonso/colaboraciones/Pablo/2008/fitsALMA/testFitNew.fits"; // 7 HDUs
        FitsIO f = new FitsIO(path);
        int n = f.getNumberOfPlains();
        System.out.println("\n\n\nFound " + n + " HDUs");
        System.out.println(f.toString());

        for (int i = 0; i < n; i++) {
            System.out.println("HDU # " + (i + 1));

            if (f.isBinaryTable(i)) {
                BinaryTableHDU bintable = (BinaryTableHDU) f.getHDU(i);
                for (int j = 0; j < bintable.getNCols(); j++) {
                    System.out.println("* " + bintable.getColumnName(j) + " / " + bintable.getColumn(j).getClass().getSimpleName());
                }
            }

            ImageHeaderElement header[] = f.getHeader(i);
            System.out.println(ImageHeaderElement.toString(header));
        }
    }
}
