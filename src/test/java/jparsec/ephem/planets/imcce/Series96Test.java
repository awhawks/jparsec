package jparsec.ephem.planets.imcce;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Target;
import jparsec.ephem.planets.EphemElement;
import jparsec.ephem.planets.JPLEphemeris;
import jparsec.io.FileIO;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;
import jparsec.util.Translate;

public class Series96Test {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Series96 test");

        try {
            AstroDate astro = new AstroDate(2049, AstroDate.JANUARY, 1, 0, 0, 0);
            TimeElement time = new TimeElement(astro, TimeElement.SCALE.TERRESTRIAL_TIME);
            CityElement city = City.findCity("Madrid");
            EphemerisElement eph = new EphemerisElement(Target.TARGET.NEPTUNE, EphemerisElement.COORDINATES_TYPE.APPARENT,
                    EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.GEOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                    EphemerisElement.FRAME.ICRF);
            ObserverElement observer = ObserverElement.parseCity(city);

            // EarthRotationParameters.set_EOP_Correction_To_Be_Applied(false);
            EphemElement ephem = Series96.series96Ephemeris(time, observer, eph);

            String name = ephem.name;
            String out = "", sep = FileIO.getLineSeparator();
            out += sep + "Series96" + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_RIGHT_ASCENSION) + ": " + Functions.formatRA(ephem.rightAscension, 5) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_DECLINATION) + ": " + Functions.formatDEC(ephem.declination, 4) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_DISTANCE) + ": " + Functions.formatValue(ephem.distance, 12) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_ELONGATION) + ": " + Functions.formatAngleAsDegrees(ephem.elongation, 8) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_PHASE_ANGLE) + ": " + Functions.formatAngleAsDegrees(ephem.phaseAngle, 8) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_PHASE) + ": " + Functions.formatValue(ephem.phase, 8) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_HELIOCENTRIC_ECLIPTIC_LONGITUDE) + ": " + Functions.formatAngleAsDegrees(ephem.heliocentricEclipticLongitude, 8) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_HELIOCENTRIC_ECLIPTIC_LATITUDE) + ": " + Functions.formatAngleAsDegrees(ephem.heliocentricEclipticLatitude, 8) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_HELIOCENTRIC_DISTANCE) + ": " + Functions.formatValue(ephem.distanceFromSun, 8) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_SUBSOLAR_LONGITUDE) + ": " + Functions.formatAngleAsDegrees(ephem.subsolarLongitude, 6) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_SUBSOLAR_LATITUDE) + ": " + Functions.formatAngleAsDegrees(ephem.subsolarLatitude, 6) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_POSITION_ANGLE_OF_AXIS) + ": " + Functions.formatAngleAsDegrees(ephem.positionAngleOfAxis, 6) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_POSITION_ANGLE_OF_POLE) + ": " + Functions.formatAngleAsDegrees(ephem.positionAngleOfPole, 6) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_LONGITUDE_OF_CENTRAL_MERIDIAN) + ": " + Functions.formatAngleAsDegrees(ephem.longitudeOfCentralMeridian, 6) + sep;

            eph.algorithm = EphemerisElement.ALGORITHM.JPL_DE403;
            JPLEphemeris jpl = new JPLEphemeris(EphemerisElement.ALGORITHM.JPL_DE403); //, "/home/alonso/eclipse/libreria_jparsec/ephem/test");
            ephem = jpl.getJPLEphemeris(time, observer, eph);

            out += sep + "DE403" + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_RIGHT_ASCENSION) + ": " + Functions.formatRA(ephem.rightAscension, 5) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_DECLINATION) + ": " + Functions.formatDEC(ephem.declination, 4) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_DISTANCE) + ": " + Functions.formatValue(ephem.distance, 12) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_ELONGATION) + ": " + Functions.formatAngleAsDegrees(ephem.elongation, 8) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_PHASE_ANGLE) + ": " + Functions.formatAngleAsDegrees(ephem.phaseAngle, 8) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_PHASE) + ": " + Functions.formatValue(ephem.phase, 8) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_HELIOCENTRIC_ECLIPTIC_LONGITUDE) + ": " + Functions.formatAngleAsDegrees(ephem.heliocentricEclipticLongitude, 8) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_HELIOCENTRIC_ECLIPTIC_LATITUDE) + ": " + Functions.formatAngleAsDegrees(ephem.heliocentricEclipticLatitude, 8) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_HELIOCENTRIC_DISTANCE) + ": " + Functions.formatValue(ephem.distanceFromSun, 8) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_SUBSOLAR_LONGITUDE) + ": " + Functions.formatAngleAsDegrees(ephem.subsolarLongitude, 6) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_SUBSOLAR_LATITUDE) + ": " + Functions.formatAngleAsDegrees(ephem.subsolarLatitude, 6) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_POSITION_ANGLE_OF_AXIS) + ": " + Functions.formatAngleAsDegrees(ephem.positionAngleOfAxis, 6) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_POSITION_ANGLE_OF_POLE) + ": " + Functions.formatAngleAsDegrees(ephem.positionAngleOfPole, 6) + sep;
            out += name + " " + Translate.translate(Translate.JPARSEC_LONGITUDE_OF_CENTRAL_MERIDIAN) + ": " + Functions.formatAngleAsDegrees(ephem.longitudeOfCentralMeridian, 6) + sep;
            System.out.println(out + "*********");

            JPARSECException.showWarnings();
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
