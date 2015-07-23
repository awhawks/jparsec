package jparsec.ephem.event;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.io.FileIO;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;
import jparsec.util.JPARSECException;

import java.util.ArrayList;

public class SarosTest {

    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Saros test");

        try {
            AstroDate astro = new AstroDate(2007, AstroDate.JANUARY, 1, 0, 0, 0);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.UNIVERSAL_TIME_UTC);
            EphemerisElement eph = new EphemerisElement(Target.TARGET.Moon, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.MOSHIER);
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);

/*            LunarEclipse le =  new LunarEclipse(time, observer, eph);

            System.out.println(le.getEclipseType() + " lunar eclipse on " + TimeFormat.formatJulianDayAsDate(le.getEclipseMaximum()));
            Saros saros = new Saros(le.getEclipseMaximum());
            if (Math.abs(saros.eclipseDate - le.getEclipseMaximum()) < 10.0) {
                System.out.println("Saros data for this eclipse");
                System.out.println("Series:             " + saros.sarosSeries);
                System.out.println("Inex:               " + saros.inexCycle);
                System.out.println("Eclipse number:     " + saros.sarosEclipseNumber);
                System.out.println("Eclipse max number: " + saros.sarosEclipseMaxNumber);
            }
*/
            System.out.println("SAROS TABLE OF ECLIPSES");
            double jd0 = astro.jd();
            double jd1 = jd0 + 18 * 365.25;
            ArrayList<double[]> v = Saros.getAllEclipses(jd0, jd1);

            // Create formatted output
            System.out.println("Eclipse       Type         Date       JD max  Saros    Inex   Eclipse #/total");
            for (int i = 0; i < v.size(); i++) {
                double element[] = v.get(i);
                double jd = element[0];
                int ecl_type = (int) element[1];
                int type = (int) element[2];
                int sarosN = (int) element[3];
                int inex = (int) element[4];
                int ecl_n = (int) element[5];
                int max_n = (int) element[6];

                String separator = "   ";
                String line = FileIO.addSpacesBeforeAString(Saros.getEclipsedTarget(SimpleEventElement.EVENT.values()[ecl_type]).getName(), 5) + separator;
                line += FileIO.addSpacesBeforeAString(Saros.getEclipseTypeAsString(SolarEclipse.ECLIPSE_TYPE.values()[type]), 10) + separator;
                line += TimeFormat.formatJulianDayAsDate(jd) + separator;
                line += FileIO.addSpacesAfterAString(Functions.formatValue(jd, 1), 10) + separator;
                line += FileIO.addSpacesAfterAString("" + sarosN, 3) + separator + separator;
                line += FileIO.addSpacesAfterAString("" + inex, 2) + separator + ecl_n;
                if (max_n != -1)
                    line += "/" + max_n;

                System.out.println(line);
            }

            JPARSECException.showWarnings();

            // Code to read the table from F. Spenak with saros data for all series and create the
            // array sarosData
/*          String file[] = DataSet.arrayListToStringArray(ReadFile.readAnyExternalFile("/home/alonso/sarosSun.txt"));
            String months[] = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            for (int i=0; i<file.length; i++) {
                int s = Integer.parseInt(FileIO.getField(1, file[i].trim(), " ", true));
                int n = Integer.parseInt(FileIO.getField(2, file[i].trim(), " ", true));
                int y1 = Integer.parseInt(FileIO.getField(4, file[i].trim(), " ", true));
                int y2 = Integer.parseInt(FileIO.getField(7, file[i].trim(), " ", true));
                int d1 = Integer.parseInt(FileIO.getField(6, file[i].trim(), " ", true));
                int d2 = Integer.parseInt(FileIO.getField(9, file[i].trim(), " ", true));
                String mo1 = FileIO.getField(5, file[i].trim(), " ", true);
                String mo2 = FileIO.getField(8, file[i].trim(), " ", true);
                int m1 = 1 + DataSet.getIndex(months, mo1);
                int m2 = 1 + DataSet.getIndex(months, mo2);
                if (m1 < 1 || m2 < 1) throw new JPARSECException("invalid month");

                if (y1 < 0) y1--;
                if (y2 < 0) y2--;
                AstroDate a1 = new AstroDate(y1, m1, d1);
                AstroDate a2 = new AstroDate(y2, m2, d2);
                // MOON: saros series, initial jd, final jd, status (0 => bad begining, 1 => OK, 2 => bad ending)
                // SUN: saros series, initial jd, final jd, status (0 => bad begining, 1 => OK, 2 => bad ending)

                System.out.println(""+s+","+a1.jd()+","+a2.jd()+",1,");
            }
*/
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
