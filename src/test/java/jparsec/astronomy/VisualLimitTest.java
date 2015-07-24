package jparsec.astronomy;

import jparsec.util.JPARSECException;

import java.text.DecimalFormat;

public class VisualLimitTest {

    /**
     * Testing program.
     * @param args Unused.
     */
    public static void main(String args[])
    {

        double zenithAngle = 0.0;

        VisualLimitFixedBrightnessData f = new VisualLimitFixedBrightnessData(Math.toRadians(180), // zenithAngleMoon
                Math.toRadians(180), // zenithAngSun
                Math.toRadians(180), // moonElongation // 180 = full moon
                1000, // htAboveSeaInMeters
                Math.toRadians(40), // latitude
                15, // temperatureInC
                40, // relativeHumidity
                1998, // year
                2 // month
        );

        // Values varying across the sky:
        VisualLimitAngularBrightnessData a = new VisualLimitAngularBrightnessData(Math.toRadians(zenithAngle), // zenithAngle
                Math.toRadians(50), // distMoon
                Math.toRadians(40)); // distSun

        int bandMask = 0x1F; // all five bands: 1 + 2 + 4 + 8 + 16 = 31

        VisualLimit v = new VisualLimit(bandMask, f, a);

        DecimalFormat nf = new DecimalFormat("0.#####E0");
        try
        {
            for (int i = 0; i < VisualLimit.BANDS; i++)
            {
                System.out.println("k: " + nf.format(v.getK(i)) + ", br: " + nf.format(v.getBrightness(i) / 1.11E-15) + ", ex: " + nf.format(v.getExtinction(i)));
            }
        } catch (JPARSECException ve)
        {
            JPARSECException.showException(ve);
        }

        System.out.println("Limiting magnitude: " + nf.format(v.limitingMagnitude()));
    }
}
