package jparsec.ephem.stars;

import java.awt.Color;
import java.awt.Graphics2D;
import jparsec.ephem.Functions;
import jparsec.graph.TextLabel;
import jparsec.graph.chartRendering.AWTGraphics;
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
        System.out.println(dstar.name+" RHO "+dstar.getDistance());
        System.out.println(dstar.name+" PA  "+Functions.formatAngleAsDegrees(dstar.getPositionAngle(), 3));

        if (!dstar.orbitPNG.equals("")) {
            try {
                Picture p = new Picture(GeneralQuery.queryImage(dstar.orbitPNG));
                p.show(dstar.name);
            } catch (Exception exc) {}
        }

        // Orbit sketch from JPARSEC, note image is inverted respect to previous one
        Picture pp = new Picture(dstar.orbit.getOrbitImage(dstar.orbit.name, 600, 600, 1.0, astro.jd(), false, true));
        Graphics2D g = pp.getImage().createGraphics();
        AWTGraphics.enableAntialiasing(g);
        g.setColor(Color.BLACK);
        String label1 = "@rho = "+Functions.formatValue(dstar.getDistance(), 3)+"\"";
        String label2 = "PA = "+Functions.formatAngleAsDegrees(dstar.getPositionAngle(), 3)+"?";
        TextLabel tl1 = new TextLabel(label1);
        tl1.draw(g, 10, 560);
        TextLabel tl2 = new TextLabel(label2);
        tl2.draw(g, 10, 580);
        pp.show("");
    }
}
