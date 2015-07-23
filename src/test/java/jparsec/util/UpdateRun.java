package jparsec.util;

import jparsec.graph.DataSet;
import jparsec.io.FileIO;
import jparsec.io.ReadFile;
import jparsec.time.AstroDate;
import jparsec.vo.GeneralQuery;

public class UpdateRun {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Update Test");

        try {
            // 1. Backup Files eop.jar, sunspot.jar, orbital_elements.jar, sky.jar, jpl.jar, cologne.jar
            // 2. Execute everything by parts

            Configuration.FORCE_JPARSEC_LIB_DIRECTORY = "/home/alonso/eclipse/workspace/mis_programas/jparsec/manager/lib/";
            AstroDate astro = new AstroDate();
            // orbital_elements.jar
            Update.updateOrbitalElementsFromMPC();
            Update.updateOrbitalElementsOfNaturalSatellites();
            Update.updateSizeAndMagnitudeOfArtificialSatellites();
            Update.updateOrbitsOfVisualBinaryStars();

            // eop.jar
            Update.updateEOPparameters();

            // sky.jar
            Update.updatePadovaAsiagoSNcat();

            // sunspot.jar
            Update.updateSunSpotsDatabase(astro.getYear());
            Update.updateSunSpotsDatabase(astro.getYear() - 1);

            // jpl.jar. Check, some files with no standard format: c041001.cat (CH3CN)
            // JPL should not be updated anymore
            // Update.updateJPLdatabase();

            // cologne.jar
            // Update.updateCOLOGNEdatabase();

            String jarpath = "jparsec/data/orbital_elements/JupiterGRS.txt";
            String grs[] = DataSet.arrayListToStringArray(ReadFile.readResource(jarpath));
            int year_grs = Integer.parseInt(FileIO.getField(1, grs[grs.length - 1].trim(), "-", true));
            if (astro.getYear() > year_grs) {
                System.out.println("FILE " + jarpath + " SHOULD BE UPDATED MANUALLY");
            }

            jarpath = "jparsec/time/leapSeconds.txt";
            String leap[] = DataSet.arrayListToStringArray(ReadFile.readResource(jarpath));
            jarpath = "jparsec/time/TTminusUT1.txt";
            String t[] = DataSet.arrayListToStringArray(ReadFile.readResource(jarpath));
            int laste = DataSet.getIndexContaining(t, "extrapolated");
            String lastl = leap[leap.length - 1].trim();
            String lastt = t[t.length - 8].trim();
            int yearl = Integer.parseInt(FileIO.getField(1, lastl, " ", true));
            int yeart = Integer.parseInt(FileIO.getField(1, lastt, " ", true));

            if (astro.getYear() > yeart) {
                System.out.println("FILE jparsec/time/TTminusUT1.txt MUST BE UPDATED MANUALLY");
            }

            if (laste > 0) {
                yeart = Integer.parseInt(FileIO.getField(1, t[laste - 1].trim(), " ", true));
                if (astro.getYear() > yeart)
                    System.out.println("FILE jparsec/time/TTminusUT1.txt SHOULD BE UPDATED MANUALLY");
            }

            String url = "ftp://maia.usno.navy.mil/ser7/tai-utc.dat";
            String text = GeneralQuery.query(url);
            String tai[] = DataSet.toStringArray(text, FileIO.getLineSeparator());
            int year_tai = Integer.parseInt(FileIO.getField(1, tai[tai.length - 1].trim(), " ", true));

            if (year_tai > yearl) {
                System.out.println("FILE jparsec/time/leapSeconds.txt MUST BE UPDATED MANUALLY");
            }

            JPARSECException.showWarnings();
        } catch (Exception ve) {
            ve.printStackTrace();
        }
    }
}
