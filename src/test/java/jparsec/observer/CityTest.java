package jparsec.observer;

import java.net.URLDecoder;
import jparsec.math.Constant;

public class CityTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("City Test");

        System.out.println(URLDecoder.decode("Armi%C3%B1%C3%B3n", "UTF-8"));
        CityElement loc_elements[] = City.getCities(Country.COUNTRY.Spain);

        System.out.println(loc_elements[1]);
        System.out.println(loc_elements[1].name);
        System.out.println(loc_elements[1].height);

        CityElement loc_element = jparsec.observer.City.findCity("Tenerife");
        System.out.println(loc_element.name + "/" + loc_element.country);

        LocationElement loc = new LocationElement(-4 * Constant.DEG_TO_RAD, 40 * Constant.DEG_TO_RAD, 1.0);
        CityElement city = City.findNearestCity(loc);
        System.out.println(" City nearest loc(-4, 40): " + city.name);
        city = City.findNearestCity(loc, null, 2 * Constant.DEG_TO_RAD);
        System.out.println(" City nearest loc(-4, 40): " + city.name);

/*
        COUNTRY countries[] = COUNTRY.values();
        String out = "";
        for (int i=0; i<countries.length; i++) {
            CityElement cities[] = City.getCities(countries[i]);
            if (cities == null) {
                System.out.println("0 cities for "+countries[i]);
            } else {
                for (int j = 0; j < cities.length; j ++) {
                    String line = FileIO.addSpacesAfterAString(cities[j].name, 70) +
                            FileIO.addSpacesAfterAString(countries[i].toString(), 50)+ //cities[j].country, 50)+
                            FileIO.addSpacesAfterAString(Functions.formatValue(cities[j].longitude, 4), 10) +
                            FileIO.addSpacesAfterAString(Functions.formatValue(cities[j].latitude, 3), 9) +
                            FileIO.addSpacesAfterAString(Functions.formatValue(cities[j].timeZone, 1), 6) +
                            FileIO.addSpacesAfterAString(Functions.formatValue(cities[j].height, 0), 4)
                            ;
                    out += line + FileIO.getLineSeparator();
                }
            }
        }
        WriteFile.writeAnyExternalFile("/home/alonso/cities.txt", out);
*/
        long t0 = System.currentTimeMillis();
        CityElement citi = City.findCity("New York");
        long t1 = System.currentTimeMillis();
        System.out.println(citi.name + " " + (t1 - t0));
    }
}
