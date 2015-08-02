package jparsec.astrophysics.gildas;

import jparsec.graph.CreateChart;
import jparsec.math.Constant;

public class Spectrum30mTest {

    /**
     * Test program.
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        //String path = "/home/alonso/colaboraciones/Asuncion/2011/figurasMuchasLineas_MonR2/broad-2-new.30m";
        String path  = "/home/alonso/observaciones/veleta_may_2015/060-15/1/WILMAOdp20150601_old30mVersion.30m";
        Gildas30m g = new Gildas30m(path);
        int list[] = g.getListOfSpectrums(true);
        Spectrum30m s = g.getSpectrum(list[0]);

        Parameter p[] = s.getHeader().getVisibleHeader();
        System.out.println(s.getHeader().toString());
        System.out.println("offset "+Double.parseDouble(p[SpectrumHeader30m.VISIBLE_HEADER.VISIBLE_OFFSET1.ordinal()].value)*Constant.RAD_TO_ARCSEC+" "+Double.parseDouble(p[SpectrumHeader30m.VISIBLE_HEADER.VISIBLE_OFFSET2.ordinal()].value)*Constant.RAD_TO_ARCSEC);
        CreateChart c = s.getChart(800, 600, Spectrum30m.XUNIT.FREQUENCY_MHZ);
        c.showChartInJFreeChartPanel();
        s.modifyRestFrequency(100000);

/*
        double vel = -6938.8, frequency = 87316.94, imgFreq = 95860.29, channel = 1787.0;
        System.out.println(s.getFrequencyForAGivenVelocity(vel) + " / should be " + frequency);
        System.out.println(s.getVelocityForAGivenFrequency(frequency) + " / should be " + vel);
        System.out.println(s.getImageFrequencyForAGivenVelocity(vel) + " / should be " + imgFreq);
        System.out.println(s.getVelocityForAGivenImageFrequency(imgFreq) + " / should be " + vel);
        System.out.println(s.getChannel(vel) + " / should be " + channel);

        double cvel = s.getCorrectedVelocityForAGivenFrequency(frequency);
        double cfreq = s.getFrequencyForAGivenCorrectedVelocity(cvel);
        double cvel2 = s.getCorrectedVelocityForAGivenGildasVelocity(vel);
        double cvel3 = s.getCorrectedVelocity(channel);
        double cvel4 = s.getCorrectedVelocityForAGivenImageFrequency(imgFreq);
        double imgFreq2 = s.getImageFrequencyForAGivenCorrectedVelocity(cvel);
        System.out.println("Corrected velocities");
        System.out.println(cvel + " / should be "+cvel2+", "+cvel3+", "+cvel4+" and different from Gildas vel = " + vel);
        System.out.println(cfreq + " / should be " + frequency);
        System.out.println(imgFreq2 + " / should be " + imgFreq);
        System.out.println("Channel width");
        double vres = s.getVelocityResolution();
        System.out.println("vres = "+vres+", from JPARSEC is "+s.getChannelWidth(s.getReferenceFrequency()));
        System.out.println("at line frequency is "+s.getChannelWidth(frequency));
*/
    }
}
