package jparsec.ephem.stars;

import jparsec.io.ReadFile;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeFormat;

public class VariableStarElementTest {
    /**
     * Testing program.
     *
     * @param args Not used.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("VariableStarElement test");

        String name = "W CET";
        int year = 2014;
        ReadFile re = new ReadFile();
        re.setPath(VariableStarElement.getPathBulletinAAVSO(year));
        re.readFileOfVariableStars();
        System.out.println(re.getNumberOfObjects());
        int index = re.searchByName(name);
        VariableStarElement vstar = re.getVariableStarElement(index);

        AstroDate astro = new AstroDate(year, 1, 1);
        TimeElement time = new TimeElement(astro.jd(), TimeElement.SCALE.UNIVERSAL_TIME_UTC);
        CityElement city = City.findCity("Madrid");
        ObserverElement observer = ObserverElement.parseCity(city);

        if (vstar.isEclipsing) {
            vstar.calcEphemeris(time, observer, false);
            System.out.println(vstar.name + " PHASE    " + vstar.getPhase());
            System.out.println(vstar.name + " MIN " + TimeFormat.formatJulianDayAsDate(vstar.getNextMinima(time, observer)));
        } else {
            System.out.println(vstar.name + " MAX " + TimeFormat.formatJulianDayAsDate(vstar.getNextMaxima(time, observer)));
            System.out.println(vstar.name + " MIN " + TimeFormat.formatJulianDayAsDate(vstar.getNextMinima(time, observer)));
        }
    }
}
