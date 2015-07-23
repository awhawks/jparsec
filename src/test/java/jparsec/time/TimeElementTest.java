package jparsec.time;

import jparsec.util.Translate;

public class TimeElementTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("TimeElement test");
        Translate.setDefaultLanguage(Translate.LANGUAGE.SPANISH);

        try {
            TimeElement time = new TimeElement(new AstroDate(-50, 1, 2, 3, 4, 5.678), TimeElement.SCALE.TERRESTRIAL_TIME);
            time.decimalsInSeconds = 3;
            System.out.println("Time is " + time.toString() + '/' + time.astroDate.getSeconds());

            TimeElement time2 = new TimeElement(time.toString());
            System.out.println("Time is " + time2.toString() + '/' + time.astroDate.getSeconds());

            boolean dmy = true, monthAsString = true;
            String s = TimeFormat.formatJulianDayAsDateAndTime(time, dmy, monthAsString);
            System.out.println(s);
            TimeElement time3 = new TimeElement(s);
            System.out.println("Time is " + time3.toString());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
