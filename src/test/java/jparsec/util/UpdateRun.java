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
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Update test");

     // Jupiter GRS - Sky & Telescope, or directly at http://jupos.privat.t-online.de/rGrs.htm
     // TT-UT1: http://maia.usno.navy.mil/ser7/deltat.data
     // bulletins with predictions for variable stars:
     //         Mira: http://www.aavso.org/aavso-bulletin, fichero csv
     //         E: http://www.as.up.krakow.pl/ephem/allstars-cat.txt
     // IRAM catalog: http://www.iram.es/IRAMES/documents/ncs30mPako/Current/Demo/iram-J2000.sou
     // Query for extrasolar planets file:
     //         http://exoplanetarchive.ipac.caltech.edu/cgi-bin/nstedAPI/nph-nstedAPI?table=exoplanets&select=pl_hostname,ra,dec,st_dist,st_mass,pl_masse,pl_rade,pl_trandur,pl_trandep,pl_orbeccen,pl_orbincl,pl_orblper,pl_orbsmax,pl_orbtper,pl_pelink,pl_name,st_vj&order=pl_masse&format=ascii
     // Space probes
     // Other: Planetary features, SkyViewData
     // Sunspot: http://fenyi.solarobs.csfk.mta.hu/en/databases/DPD/
        
        // 1. Backup Files eop.jar, sunspot.jar, orbital_elements.jar, sky.jar, jpl.jar, cologne.jar
        // 2. Execute everything by parts

        Configuration.FORCE_JPARSEC_LIB_DIRECTORY = "/home/alonso/eclipse/workspace/mis_programas/jparsec/manager/lib/";
        AstroDate astro = new AstroDate();

        // orbital_elements.jar
        Update.updateOrbitalElementsOfNaturalSatellites();
        Update.updateOrbitalElementsFromMPC();
        Update.updateSizeAndMagnitudeOfArtificialSatellites();
        Update.updateOrbitsOfVisualBinaryStars();

        // eop.jar
        Update.updateEOPparameters();

        // sky.jar
        Update.updatePadovaAsiagoSNcat();
        Update.updateNovae();

        // sunspot.jar (>= 2017 not available anymore)
        //Update.updateSunSpotsDatabase(astro.getYear());
        //Update.updateSunSpotsDatabase(astro.getYear() - 1);

        // jpl.jar. Check, some files with no standard format: c041001.cat (CH3CN)
        // JPL should not be updated anymore
        //Update.updateJPLdatabase();

        // cologne.jar
        //Update.updateCOLOGNEdatabase();

        String jarpath = "jparsec/data/orbital_elements/JupiterGRS.txt";
        String grs[] = DataSet.arrayListToStringArray(ReadFile.readResource(jarpath));
        int year_grs = Integer.parseInt(FileIO.getField(1, grs[grs.length - 1].trim(), "-", true));

        if (astro.getYear() > year_grs) {
            System.out.println("FILE " + jarpath + " SHOULD BE UPDATED BY HAND");
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
            System.out.println("FILE jparsec/time/TTminusUT1.txt MUST BE UPDATED BY HAND");
        }

        if (laste > 0) {
            yeart = Integer.parseInt(FileIO.getField(1, t[laste - 1].trim(), " ", true));
            if (astro.getYear() > yeart)
                System.out.println("FILE jparsec/time/TTminusUT1.txt SHOULD BE UPDATED BY HAND");
        }

        String url = "ftp://maia.usno.navy.mil/ser7/tai-utc.dat";
        String text = GeneralQuery.query(url);
        String tai[] = DataSet.toStringArray(text, FileIO.getLineSeparator());
        int year_tai = Integer.parseInt(FileIO.getField(1, tai[tai.length - 1].trim(), " ", true));

        if (year_tai > yearl) {
            System.out.println("FILE jparsec/time/leapSeconds.txt MUST BE UPDATED BY HAND");
        }

        JPARSECException.showWarnings();
    }
}
