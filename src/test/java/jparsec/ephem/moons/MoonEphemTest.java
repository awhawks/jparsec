package jparsec.ephem.moons;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Precession;
import jparsec.ephem.Target;
import jparsec.graph.DataSet;
import jparsec.io.FileIO;
import jparsec.math.Constant;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;

public class MoonEphemTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("MoonEphem test");

        CityElement city = City.findCity("Madrid");
        ObserverElement observer = ObserverElement.parseCity(city);
        EphemerisElement eph = new EphemerisElement(Target.TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_1976,
                EphemerisElement.FRAME.ICRF);
        eph.algorithm = EphemerisElement.ALGORITHM.JPL_DE405;
        eph.correctForEOP = false;
        //Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH = "/home/alonso/eclipse/libreria_jparsec/ephem/test";

/*      String id[] = DataSet.extractColumnFromTable(ORBITS, " ", 0);
        String did[] = DataSet.getDifferentElements(id);
        if (did.length != id.length) System.out.println("There are repeated elements in ORBITS!");
        String[] compare = OLD_ORBITS.clone(); // Change to NEW_ORBITS when updating

        String nid[] = DataSet.extractColumnFromTable(compare, " ", 0);
        String obj[] = id.clone();
        String cb[] = DataSet.extractColumnFromTable(ORBITS, " ", 1);
        for (int i=0; i<nid.length; i++) {
            int index = DataSet.getIndex(id, nid[i]);
            if (index < 0) System.out.println("New satellite "+nid[i]);
        }
        for (int i=0; i<id.length; i++) {
            int index = DataSet.getIndex(nid, id[i]);
            if (index < 0) {
                System.out.println("No new data for "+id[i]);
                index = DataSet.getIndex(obj, id[i]);
                obj = DataSet.eliminateRowFromTable(obj, 1+index);
                cb = DataSet.eliminateRowFromTable(cb, 1+index);
            }
        }
        String cloneORBITS[] = ORBITS.clone();
        for (int i=0; i<obj.length; i++) {
            double maxD = -1;
            int ncalc = 50;
            double tstep = 50;
            for (double jd = 2451545; jd < 2451545 + ncalc*tstep; jd = jd + tstep) {
                AstroDate astro = new AstroDate(jd); //2454887.00625); // Testing date: 2011-dec-1
                TimeElement time = new TimeElement(astro, SCALE.UNIVERSAL_TIME_UTC);
                TARGET target = Target.getID(obj[i]);
                eph.targetBody = TARGET.values()[Integer.parseInt(cb[i])]; //target.getCentralBody();
                String sid = target.getEnglishName();
                if (target == TARGET.NOT_A_PLANET) sid = obj[i];

                ORBITS = cloneORBITS;
                MoonEphemElement ephem1 = MoonEphem.calcJPLSatellite(time, observer, eph, sid);

                ORBITS = compare;
                MoonEphemElement ephem2 = MoonEphem.calcJPLSatellite(time, observer, eph, sid);

                //System.out.println(sid);
                //System.out.println(ephem1.getEquatorialLocation().toStringAsEquatorialLocation());
                //System.out.println(ephem2.getEquatorialLocation().toStringAsEquatorialLocation());
                double dist = LocationElement.getAngularDistance(ephem1.getEquatorialLocation(), ephem2.getEquatorialLocation());
                if (dist > maxD || maxD == -1) maxD = dist;
            }

            System.out.println("Maximum difference found for "+obj[i]+": "+(maxD * Constant.RAD_TO_ARCSEC)+"\"");
        }

        JPARSECException.showWarnings();
        */

        /*
        Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH = "/home/alonso/eclipse/libreria_jparsec/ephem/test/";
        eph.useVondrak2011PrecessionFormulaInsteadOfIAU2006 = false;
        TimeElement time = new TimeElement(new AstroDate(1600, 1, 1), TimeElement.SCALE.TERRESTRIAL_TIME);
        MoonEphemElement ephem[] = MoonEphem.saturnianSatellitesEphemerides_TASS17(time, observer, eph, false);
        System.out.println(ephem[5].name+": "+Functions.formatRA(ephem[5].rightAscension, 6)+", "+Functions.formatDEC(ephem[5].declination, 5)+", "+ephem[5].distance);
        //IMCCE          13h 45m 15.35365s,   -08Âº 17' 57.0768",  9.99873438
        //JPARSEC         13h 45m 15.3536950s, -08Âº 17' 57.07677", 9.99873438032
        */

        double jd = 2451545.0 + 15 * 365.25;
        double testPos[] = GUST86.GUST86_theory(jd, 5, 3);
        testPos = Precession.precess(Constant.J1950, Constant.J2000, testPos, eph);
        //testPos = Ephem.eclipticToEquatorial(testPos, Constant.J2000, eph);

        // Elements for Oberon from Horizons
        String oberonElements = "2456123.500000000 = A.D. 2012-Jul-15 00:00:00.0000 (CT)\n" +
                "EC= 1.813799361026376E-03 QR= 3.893220361896172E-03 IN= 1.798019718252132E+02\n" +
                "OM= 3.288631399182058E+02 W = 3.004505321279436E+02 Tp=  2456128.448374000378\n" +
                "N = 2.673603454600160E+01 MA= 2.277001017849925E+02 TA= 2.275466065146315E+02\n" +
                "A = 3.900294713956159E-03 AD= 3.907369066016147E-03 PR= 1.346497362503739E+01";
        String elem[] = DataSet.toStringArray(oberonElements, FileIO.getLineSeparator(), true);
        MoonOrbitalElement orbit = new MoonOrbitalElement(elem, "Oberon", Target.TARGET.URANUS);

        double p[] = MoonEphem.rocks(jd, orbit, eph.ephemMethod);
        System.out.println(p[0] + "/" + p[1] + "/" + p[2]);
        System.out.println(testPos[0] + "/" + testPos[1] + "/" + testPos[2]);
        // The conclusion is that elements from Horizons can be used 2 years around the reference time

        // Test on Charon
        jd = (new AstroDate(2010, 1, 1, 0, 0, 0)).jd();
        orbit = MoonEphem.getMoonElements("Charon", jd)[0];
        System.out.println(jd);
        p = MoonEphem.rocks(jd, orbit, eph.ephemMethod);
        System.out.println(p[0] * Constant.AU + "/" + p[1] * Constant.AU + "/" + p[2] * Constant.AU);

        // Get (very good) approximate barycentric position of Pluto
        p = Functions.scalarProduct(p, -1.0 / 8.0);
        System.out.println(p[0] * Constant.AU + "/" + p[1] * Constant.AU + "/" + p[2] * Constant.AU);
    }
}
