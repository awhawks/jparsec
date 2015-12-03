package jparsec.time.calendar;

import jparsec.util.JPARSECException;

public class ChineseTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws JPARSECException {
        System.out.println("Chinese test");

        // Calendario Chino: ciclo 78, año 32 (4713, Yi-Wei), mes 10 (Ji-Hai), mes bisiesto no, día 22. 
        // Próximo año nuevo el lunes, 8 de febrero de 2016.
        double jd = 2457359.5; //2451545.5;
        long fixed = (long) jd - Gregorian.EPOCH ; //730120;

        Chinese h = new Chinese(jd);
        System.out.println("JD " + h.julianDate + ' ' + fixed + " = " + h);

        h = new Chinese(h.getFixed());
        System.out.println("JD " + h.julianDate + ' ' + h.fixed + " = " + h);

        Chinese h2 = new Chinese(h.cycle, h.year, h.month, h.leapMonth, h.day);
        System.out.println("JD " + h2.julianDate + ' ' + h2.fixed + " = " + h2);
        //System.out.println(h2.fixed - h.fixed);

        ChineseName name = Chinese.nameOfYear(h2.year);
        String year = Calendar.nameFromMonth(name.stem, Chinese.YEAR_STEM_NAMES) + '-' + Calendar.nameFromMonth(name.branch, Chinese.YEAR_BRANCH_NAMES);
        System.out.println(h2.getYearNumber());
        System.out.println(year);

        name = Chinese.nameOfMonth(h2.year, h2.month);
        String month = Calendar.nameFromMonth(name.stem, Chinese.YEAR_STEM_NAMES);
        month += '-' + Calendar.nameFromMonth(name.branch, Chinese.YEAR_BRANCH_NAMES);
        System.out.println(month);

        name = Chinese.nameOfDay(h2.cycle);
        String day = Calendar.nameFromMonth(name.stem, Chinese.YEAR_STEM_NAMES);
        day += '-' + Calendar.nameFromMonth(name.branch, Chinese.YEAR_BRANCH_NAMES);
        System.out.println(day);

        // New year
        int yearN = 2016;
        Gregorian g = new Gregorian(Chinese.newYear(yearN));
        System.out.println("New Chinese year for Gregorian year " + yearN + ": " + g);
    }
}
