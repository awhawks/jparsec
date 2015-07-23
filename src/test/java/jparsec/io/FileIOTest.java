package jparsec.io;

import jparsec.time.AstroDate;
import jparsec.time.TimeFormat;

public class FileIOTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("FileIO test");

        String fileName = "ArtificialSatellites.txt";
        AstroDate astro = new AstroDate(FileIO.getLastModifiedTimeOfResource(FileIO.DATA_ORBITAL_ELEMENTS_JARFILE, FileIO.DATA_ORBITAL_ELEMENTS_DIRECTORY, fileName));
        System.out.println(TimeFormat.formatJulianDayAsDateAndTime(astro.jd(), null));

        System.out.println(FileIO.getTemporalDirectory());
    }
}
