package jparsec.astronomy;

import java.text.DecimalFormat;

import jparsec.ephem.Functions;
import jparsec.ephem.planets.EphemElement;
import jparsec.io.ConsoleReport;
import jparsec.math.Constant;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeElement.SCALE;
import jparsec.util.JPARSECException;

public class VisualLimitTest {
    /**
     * Testing program.
     *
     * @param args Unused.
     */
    public static void main(String args[]) {
        VisualLimitFixedBrightnessData f = new VisualLimitFixedBrightnessData(
        		Math.toRadians(180), // zenithAngleMoon
                Math.toRadians(180), // zenithAngSun
                Math.toRadians(0), // moonElongation // 180 = full moon
                1000, // htAboveSeaInMeters
                Math.toRadians(30), // latitude
                15, // temperatureInC
                40, // relativeHumidity
                1998, // year
                2 // month
        );

        double zenithAngle = 45.0;
        // Values varying across the sky:
        VisualLimitAngularBrightnessData a = new VisualLimitAngularBrightnessData(
        		Math.toRadians(zenithAngle), // zenithAngle
                Math.toRadians(180), // distMoon
                Math.toRadians(180)); // distSun

        int bandMask = 0x1F; // all five bands: 1 + 2 + 4 + 8 + 16 = 31
        VisualLimit v = new VisualLimit(bandMask, f, a);
        DecimalFormat nf = new DecimalFormat("0.#####E0");

        try {
            for (int i = 0; i < VisualLimit.BANDS; i++) {
                System.out.println("k: " + nf.format(v.getK(i)) + ", br: " + nf.format(v.getBrightness(i) / 1.11E-15) + ", ex: " + nf.format(v.getExtinction(i)));
            }
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }

        System.out.println("Limiting magnitude: " + nf.format(v.limitingMagnitude()));
        
        // Similar test directly from B. Schaefer's Basic program, but from complete code
        try {
        	AstroDate astro = new AstroDate(1998, 2, 1, 0, 0, 0);
	        TimeElement time = new TimeElement(astro, SCALE.UNIVERSAL_TIME_UTC);
	        ObserverElement obs = new ObserverElement("", 0, 30*Constant.DEG_TO_RAD, 1000, 0);
	        obs.setTemperature(15);
	        obs.setHumidity(40);
	        EphemElement esun = new EphemElement();
	        esun.elevation = -90 * Constant.DEG_TO_RAD;
	        EphemElement emoon = new EphemElement();
	        emoon.elevation = -90 * Constant.DEG_TO_RAD;
	        emoon.elongation = (float) (0 * Constant.DEG_TO_RAD);
	        double az = 0, el = 45 * Constant.DEG_TO_RAD;
	        esun.azimuth = az;
	        esun.elevation = -90 * Constant.DEG_TO_RAD;
	        emoon.azimuth = az;
	        emoon.elevation = -90 * Constant.DEG_TO_RAD;
	        int bm = 31;
			VisualLimit vl = new VisualLimit(time, obs, esun, emoon, az, el, bm);
			
	        try {
	            System.out.println("Limiting magnitude: " + nf.format(vl.limitingMagnitude()));
	            for (int i = 0; i < VisualLimit.BANDS; i++) {
	                System.out.println("k: " + nf.format(vl.getK(i)) + ", br: " + nf.format(vl.getBrightness(i) / 1.11E-15) + ", ex: " + nf.format(vl.getExtinction(i)));
	            }
	            double lm = VisualLimit.getLimitingMagnitude(time, obs, esun, emoon, az, el);
	            double sb[] = VisualLimit.getSkyBrightness(time, obs, esun, emoon, az, el);
	            double ex[] = VisualLimit.getExtinctionCoefficient(time, obs, esun, emoon, az, el);
	            System.out.println("Lim Mag: "+lm);
	            System.out.println("Sky brightness UBVRI");
	            //sb = Functions.scalarProduct(sb, 1.02E-15);
	            ConsoleReport.doubleArrayReport(sb, "f3.3");
	            System.out.println("Extinction");
	            ConsoleReport.doubleArrayReport(ex, "f3.3");
	        } catch (JPARSECException ve) {
	            JPARSECException.showException(ve);
	        }

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
