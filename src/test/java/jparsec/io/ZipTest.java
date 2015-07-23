package jparsec.io;

import jparsec.util.JPARSECException;

public class ZipTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        System.out.println("Zip testing");

        try {
            String filesToAdd[] = FileIO.getFiles("." + FileIO.getFileSeparator());
            Zip.zipFile("test.zip", filesToAdd);
            Zip.unZipFile("test.zip", "./test/", false);
        } catch (JPARSECException e) {
            e.showException();
        }
    }
}
