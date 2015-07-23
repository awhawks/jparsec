package jparsec.math;

import java.awt.geom.Point2D;
import jparsec.ephem.Ephem;
import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.ephem.planets.EphemElement;
import jparsec.graph.CreateChart;
import jparsec.graph.DataSet;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.LocationElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;

public class InterpolationTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Interpolation test");

        double x[] = new double[] { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
        double y[] = new double[] { 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3 };
        double z[] = new double[] { 1, 2, 3, 2, 3, 4, 3, 4, 5, 2, 3, 4 };

        try {
            Interpolation interp = new Interpolation(x, y, z, false);

            double px = 1.5, pz = 2.5;
            double py = interp.linearInterpolation3d(px, pz);

            System.out.println(px + " / " + pz + " / " + py);

            x = new double[] { 7.0, 8.0, 9.0 };
            y = new double[] { 0.884226, 0.877366, 0.870531 };
            interp = new Interpolation(x, y, false);
            System.out.println("Meeus  interpolation: y = " + interp.MeeusInterpolation(8.18125));
            System.out.println("Spline interpolation: y = " + interp.splineInterpolation(8.18125));
            System.out.println("Linear interpolation: y = " + interp.linearInterpolation(8.18125));


            x = new double[] { 12.0, 16.0, 20.0 };
            y = new double[] { 1.3814294, 1.3812213, 1.3812453 };
            interp = new Interpolation(x, y, false);
            Point2D p = interp.MeeusExtremum();
            System.out.println("Extremum: x = " + p.getX() + ", y = " + p.getY());

            CreateChart ch = interp.getChart(false, 2);
            ch.showChartInJFreeChartPanel();


            x = new double[] { -1.0, 0.0, 1.0 };
            y = new double[] { -2.0, 3.0, 2.0 };
            interp = new Interpolation(x, y, false);
            System.out.println("Zero at x = " + interp.MeeusZero());

            x = new double[] { 27.0, 27.5, 28.0, 28.5, 29.0 };
            y = new double[] { 54 * 60 + 36.125, 54 * 60 + 24.606, 54 * 60 + 15.486,
                    54 * 60.0 + 8.694, 54 * 60 + 4.133 };
            double y0 = 28.0 + (3.0 + 20.0 / 60.0) / 24.0;
            interp = new Interpolation(x, y, false);
            System.out.println("Meeus interpolation: y = " + Functions.formatAngle(Constant.ARCSEC_TO_RAD * interp.MeeusInterpolation(y0), 3));

            x = new double[] { 25.0, 26.0, 27.0, 28.0, 29.0 };
            y = new double[] {
                    Functions.parseDeclination("-01Âº 11' 21.23\""),
                    Functions.parseDeclination("-00Âº 28' 12.31\""),
                    Functions.parseDeclination("00Âº 16' 07.02\""),
                    Functions.parseDeclination("01Âº 01' 00.13\""),
                    Functions.parseDeclination("01Âº 45' 46.33\"")
            };
            interp = new Interpolation(x, y, false);
            System.out.println("Zero at x = " + interp.MeeusZero());

            // I must add 3 degrees here to 'help' convergency, since
            // otherwise it does not give results close to those from
            // Meeus, chapter 3, page 31.
            double da = 3;
            x = new double[] {
                    29.0 + da,
                    30.0 + da,
                    31.0 + da,
                    32.0 + da,
                    33.0 + da
            };
            y = new double[] {
                    Math.sin((29.0 + da) * Constant.DEG_TO_RAD),
                    Math.sin((30.0 + da) * Constant.DEG_TO_RAD),
                    Math.sin((31.0 + da) * Constant.DEG_TO_RAD),
                    Math.sin((32.0 + da) * Constant.DEG_TO_RAD),
                    Math.sin((33.0 + da) * Constant.DEG_TO_RAD)
            };
            interp = new Interpolation(x, y, false);
            System.out.println("Zero at x = " + interp.MeeusZero());
            p = interp.MeeusExtremum();
            if (p != null) {
                System.out.println("Extremum: x = " + p.getX() + ", y = " + p.getY());
            } else {
                System.out.println("Could not find extremum (ok!)");
            }

            x = new double[] {
                    29.43, 30.97, 27.69, 28.11, 33.05
            };
            y = new double[] {
                    Math.sin(29.43 * Constant.DEG_TO_RAD),
                    Math.sin(30.97 * Constant.DEG_TO_RAD),
                    Math.sin(27.69 * Constant.DEG_TO_RAD),
                    Math.sin(28.11 * Constant.DEG_TO_RAD),
                    Math.sin(33.05 * Constant.DEG_TO_RAD)
            };
            interp = new Interpolation(x, y, false);
            System.out.println("Lagrange interpolation: y = " + interp.LagrangeInterpolation(30.0));


            // Real case, conjunction Venus - Jupiter on March 15, 2012
            // Let's supose we know it is on March, 16, between 9 and 13 LT
            // We use a time step of 1 hr
            AstroDate astro = new AstroDate(2012, AstroDate.MARCH, 15, 9, 0, 0);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.LOCAL_TIME);
            CityElement city = City.findCity("Madrid");
            ObserverElement observer = ObserverElement.parseCity(city);
            boolean topocentric = false;
            EphemerisElement ephJup = new EphemerisElement(Target.TARGET.JUPITER, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, topocentric, EphemerisElement.REDUCTION_METHOD.IAU_2009,
                    EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.MOSHIER);
            EphemerisElement ephVen = new EphemerisElement(Target.TARGET.VENUS, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, topocentric, EphemerisElement.REDUCTION_METHOD.IAU_2009,
                    EphemerisElement.FRAME.ICRF, EphemerisElement.ALGORITHM.MOSHIER);
            double timeStep = 1.0 / 24.0;
            double jd0 = astro.jd();
            double timeValues[] = new double[0];
            double sepValues[] = new double[0];
            boolean minDistance = false; // False for real conjunction (same right ascension)
            for (int i = 0; i < 5; i++) {
                double jd = jd0 + i * timeStep;
                astro = new AstroDate(jd);
                time = new TimeElement(astro, TimeElement.SCALE.LOCAL_TIME);

                EphemElement ephemJup = Ephem.getEphemeris(time, observer, ephJup, true);
                EphemElement ephemVen = Ephem.getEphemeris(time, observer, ephVen, true);

                double dist = LocationElement.getAngularDistance(ephemJup.getEquatorialLocation(), ephemVen.getEquatorialLocation());
                if (!minDistance) {
                    dist = ephemJup.getEquatorialLocation().getLongitude() - ephemVen.getEquatorialLocation().getLongitude();

                    // This would be for conjunction in ecliptic longitude
                    //LocationElement eclJup = CoordinateSystem.equatorialToEcliptic(ephemJup.getEquatorialLocation(), time, observer, ephJup);
                    //LocationElement eclVen = CoordinateSystem.equatorialToEcliptic(ephemVen.getEquatorialLocation(), time, observer, ephVen);
                    //dist = eclJup.getLongitude() - eclVen.getLongitude();
                }

                timeValues = DataSet.addDoubleArray(timeValues, new double[] { jd });
                sepValues = DataSet.addDoubleArray(sepValues, new double[] { dist });
            }
            interp = new Interpolation(timeValues, sepValues, false);
            double jd = 0.0;
            String phen = "";
            if (!minDistance) {
                phen = "Conjunction";
                jd = interp.MeeusZero();
            } else {
                phen = "Minimum distance";
                jd = interp.MeeusExtremum().getX();
            }
            System.out.println(phen + " Jupiter-Venus on jd = " + jd + " (" + new AstroDate(jd).toString() + ") LT");

            /*
            Accurate results with manual calculations, to the nearest minute

            Minimum distance
            Topo. min dist: 13 Mar 2012, 23:59 TL, 2.998 deg // Note here the 5 min error using Meeus interp. with step of 1 hr
            Geoc. min dist: 13 Mar 2012, 23:26 TL, 2.999 deg

            Conjunction in ecliptic longitude
            Topo. conjunct: 14 Mar 2012, 06:53 TL, 3.011 deg
            Geoc. conjunct: 14 Mar 2012, 06:54 TL, 3.011 deg

            Conjunction in RA
            Topo. conjunct: 15 Mar 2012, 11:34 TL, 3.270 deg
            Geoc. conjunct: 15 Mar 2012, 11:38 TL, 3.270 deg
            */

        } catch (JPARSECException e) {
            JPARSECException.showException(e);
        }
    }
}
