package jparsec.ephem.stars;

import jparsec.ephem.Functions;
import jparsec.io.ReadFile;
import jparsec.io.image.Picture;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.vo.GeneralQuery;

public class DoubleStarElementTest {
    /**
     * Testing program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("DoubleStarElement test");

        //String name = "alpha centauri";
        String name = "Eps Aur";

        ReadFile re = new ReadFile();
        re.setPath(DoubleStarElement.PATH_VISUAL_DOUBLE_STAR_CATALOG); //.PATH_OLD_VISUAL_DOUBLE_STAR_CATALOG);
        re.readFileOfDoubleStars();
        System.out.println(re.getNumberOfObjects());
        int index = re.searchByName(name);
        DoubleStarElement dstar = re.getDoubleStarElement(index);

        AstroDate astro = new AstroDate(2010, 1, 1);
        TimeElement time = new TimeElement(astro.jd(), TimeElement.SCALE.UNIVERSAL_TIME_UTC);
        CityElement city = City.findCity("Madrid");
        ObserverElement observer = ObserverElement.parseCity(city);
        dstar.calcEphemeris(time, observer);
        System.out.println(dstar.name + " RHO " + dstar.getDistance());
        System.out.println(dstar.name + " PA  " + Functions.formatAngleAsDegrees(dstar.getPositionAngle(), 3));

        if (!"".equals(dstar.orbitPNG)) {
            Picture p = new Picture(GeneralQuery.queryImage(dstar.orbitPNG));
            p.show(dstar.name);
        }

        // Orbit sketch from JPARSEC, note image is inverted respect to previous one
        Picture pp = new Picture(dstar.orbit.getOrbitImage("title", 600, 600, 1.0, astro.jd(), false, false));
        pp.show("");
    }
}
