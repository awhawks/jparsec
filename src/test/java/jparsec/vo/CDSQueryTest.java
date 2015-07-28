package jparsec.vo;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Functions;
import jparsec.ephem.Precession;
import jparsec.math.Constant;
import jparsec.observer.LocationElement;
import jparsec.util.JPARSECException;

public class CDSQueryTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("CDSQuery test");

        try {
            LocationElement loc = new LocationElement(
                    Functions.parseRightAscension("06h 05m 22.0s"),
                    Functions.parseDeclination("-06d 22' 25.0''"),
                    1.0);

            // Input/output epochs for coordinates set as years
            double inputEpoch = 1950.0; // Bessel
            double outputEpoch = 2000.0; // Julian

            // Using CDS
            System.out.println("CDS");

            // ->
            LocationElement out = CDSQuery.query(CDSQuery.FRAME.FK4, CDSQuery.FRAME.FK5, loc, CDSQuery.PRECISION.MAS, inputEpoch, outputEpoch);

            System.out.println(Functions.formatRA(out.getLongitude()));
            System.out.println(Functions.formatDEC(out.getLatitude()));

            // <-
            out = CDSQuery.query(CDSQuery.FRAME.FK5, CDSQuery.FRAME.FK4, out, CDSQuery.PRECISION.MAS, outputEpoch, inputEpoch);

            System.out.println(Functions.formatRA(out.getLongitude()));
            System.out.println(Functions.formatDEC(out.getLatitude()));

            // Now with JPARSEC
            System.out.println("JPARSEC");

            // Obtain input/output epochs as JDs
            double inEpoch = Constant.B1950 + (inputEpoch - 1950.0) * Constant.TROPICAL_YEAR;
            double ouEpoch = Constant.J2000 + (outputEpoch - 2000.0) * 0.01 * Constant.JULIAN_DAYS_PER_CENTURY;
            EphemerisElement.REDUCTION_METHOD method = EphemerisElement.REDUCTION_METHOD.IAU_1976; // Should be IAU 1976 to match CDS output
            EphemerisElement eph = new EphemerisElement();
            eph.ephemMethod = method;

            // ->
            double c[] = Precession.FK4_BxxxxToFK5_Jxxxx(loc.getRectangularCoordinates(), ouEpoch, inEpoch, eph);
            out = LocationElement.parseRectangularCoordinates(c);

            System.out.println(Functions.formatRA(out.getLongitude(), 5));
            System.out.println(Functions.formatDEC(out.getLatitude(), 4));

            // <-
            out = LocationElement.parseRectangularCoordinates(Precession.FK5_JxxxxToFK4_Bxxxx(c, ouEpoch, inEpoch, eph));
            System.out.println(Functions.formatRA(out.getLongitude(), 5));
            System.out.println(Functions.formatDEC(out.getLatitude(), 4));

            // (July 2011) It is clear CDS uses IAU 1976 algorithms
            // See http://ned.ipac.caltech.edu/help/calc_doc.txt
            // For 6h 5m 22s / -6 &ordm; 22' 25"
            // NASA/IPAC gives 06h 07m 48.27712s   -06d 22' 53.7507" (http://ned.ipac.caltech.edu/forms/calculator.html)
            // CDS       gives 06h 07m 48.2727s    -06d 22' 53.763"
            // JPARSEC   gives 06h 07m 48.27269s   -06d 22' 53.7628"

            // JPARSEC = CDS == AA Supplement, up to the milli-arcsecond
        } catch (JPARSECException e) {
            e.showException();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
