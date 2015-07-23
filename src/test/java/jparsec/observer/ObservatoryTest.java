package jparsec.observer;

import jparsec.math.Constant;
import jparsec.util.JPARSECException;

public class ObservatoryTest {
    /**
     * For unit testing only
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Observatory test");

        try {
            ObservatoryElement loc_elements[] = Observatory.getObservatoriesByCountry(Country.COUNTRY.Spain);

            System.out.println(loc_elements[1]);
            System.out.println(loc_elements[1].location);
            System.out.println(loc_elements[1].name);
            System.out.println(loc_elements[1].longitude);
            System.out.println(loc_elements[1].latitude);
            System.out.println(loc_elements[1].height);

            ObservatoryElement obs = Observatory.findObservatorybyName("Teide");
            System.out.println(obs.name);
            System.out.println(obs.longitude);
            System.out.println(obs.latitude);
            System.out.println(obs.height);
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }

        ObservatoryElement loc_element = null;
        try {
            loc_element = Observatory.findObservatorybyName("Madrid");
            System.out.println(loc_element.name);
            System.out.println(loc_element.longitude);
            System.out.println(loc_element.latitude);
            System.out.println(loc_element.height);
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }

        System.out.println("Observatory Marsden Test");

        try {
            System.out.println(Observatory.getAllObservatories().length);
            System.out.println("Marsden list contains " + Observatory.getNumberOfObservatoriesInMarsdenList() + " observatories");
            int my_obs = Observatory.searchByNameInMarsdenList("Teide");
            ObservatoryElement obs2 = Observatory.getObservatoryFromMarsdenList(my_obs);
            System.out.println(obs2.location);
            System.out.println(obs2.name);
            System.out.println(obs2.longitude - 360.0);
            System.out.println(obs2.latitude);
            System.out.println(obs2.height);

            LocationElement loc = new LocationElement(-3 * Constant.DEG_TO_RAD, 38.0 * Constant.DEG_TO_RAD, 1.0);
            ObservatoryElement observ = Observatory.getObservatoryFromMarsdenList(Observatory.searchByPositionInMarsdenList(loc));
            System.out.println(" Marsden observatory nearest loc(-3, 38): " + observ.name + " (" + ObservatoryElement.getObservatoryCodeAsString(observ.code) + ")");
            System.out.println(" " + observ.name + " lon: " + observ.longitude);
            System.out.println(" " + observ.name + " lat: " + observ.latitude);
            System.out.println(" " + observ.name + " alt: " + observ.height);
            observ = Observatory.findObservatoryByPosition(loc);
            System.out.println(" Sveshnikov observatory nearest loc(-3, 38): " + observ.name);
            System.out.println(" " + observ.name + " lon: " + observ.longitude);
            System.out.println(" " + observ.name + " lat: " + observ.latitude);
            System.out.println(" " + observ.name + " alt: " + observ.height);
/*
            COUNTRY countries[] = COUNTRY.values();
            String out = "";
            for (int i=0; i<countries.length; i++) {
                ObservatoryElement observatories[] = Observatory.getObservatoriesByCountry(countries[i]);
                if (observatories == null) {
                    System.out.println("0 observatories for "+countries[i]);
                } else {
                    for (int j = 0; j < observatories.length; j ++) {
                        String line = FileIO.addSpacesAfterAString(observatories[j].name, 70) +
                                FileIO.addSpacesAfterAString(countries[i].toString(), 50)+ //cities[j].country, 50)+
                                FileIO.addSpacesAfterAString(Functions.formatValue(observatories[j].longitude, 4), 10) +
                                FileIO.addSpacesAfterAString(Functions.formatValue(observatories[j].latitude, 3), 9) +
                                FileIO.addSpacesAfterAString(Functions.formatValue(observatories[j].code, 0), 6) +
                                FileIO.addSpacesAfterAString(Functions.formatValue(observatories[j].height, 0), 5) +
                                FileIO.addSpacesAfterAString(observatories[j].location, 30) +
                                FileIO.addSpacesAfterAString(observatories[j].reference, 3)
                                ;
                        out += line + FileIO.getLineSeparator();
                    }
                }
            }

            WriteFile.writeAnyExternalFile("/home/alonso/observatories.txt", out);
*/
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
