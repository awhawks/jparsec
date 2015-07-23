package jparsec.ephem.event;

import jparsec.ephem.EphemerisElement;
import jparsec.ephem.Target;
import jparsec.observer.City;
import jparsec.observer.CityElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.util.Translate;
import jparsec.vo.Feed;
import jparsec.vo.FeedMessageElement;

import java.util.ArrayList;

public class EventReportTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("EventReport test");

        //Translate.setDefaultLanguage(LANGUAGE.SPANISH);
        long t0 = System.currentTimeMillis();
        //AstroDate astroi = new AstroDate(2012, 7, 15, 0, 0, 0);
        //AstroDate astrof = new AstroDate(2012, 7, 15, 23, 59, 59);
        AstroDate astroi = new AstroDate(2014, 1, 19, 0, 0, 0);
        AstroDate astrof = new AstroDate(2014, 5, 19, 11, 59, 59);
        TimeElement init = new TimeElement(astroi, TimeElement.SCALE.TERRESTRIAL_TIME);
        TimeElement end = new TimeElement(astrof, TimeElement.SCALE.TERRESTRIAL_TIME);
        CityElement city = City.findCity("Madrid");
        ObserverElement obs = ObserverElement.parseCity(city);
        EphemerisElement eph = new EphemerisElement(Target.TARGET.NOT_A_PLANET, EphemerisElement.COORDINATES_TYPE.APPARENT,
                EphemerisElement.EQUINOX_OF_DATE, EphemerisElement.TOPOCENTRIC, EphemerisElement.REDUCTION_METHOD.IAU_2006,
                EphemerisElement.FRAME.DYNAMICAL_EQUINOX_J2000);

        EventReport.setEverythingTo(false);
        EventReport.calendar = true;
/*            EventReport.moonEvents = true;
        EventReport.moonEventsAlsoMutualEvents = true;
        EventReport.moonEventsOnlySeveralNonMutualAtSameTime = true;
        EventReport.lunarMaxMinDeclination = true;
        EventReport.calendar = true;
        EventReport.calendarDST = true;
        //EventReport.planetaryEvents = true;
        EventReport.equinoxesAndSolstices = true;
        EventReport.NEOs = true;
        EventReport.eclipses = true;
        EventReport.planetaryEvents = true;
        EventReport.MercuryVenusTransits = true;
        EventReport.lunarPhases = true;
*/
        MoonEvent.SEVERAL_SIMULTANEOUS_EVENTS_MINIMUM_NUMBER = 3;
        MoonEvent.setEventDefinition(MoonEvent.EVENT_DEFINITION.SATELLITE_LIMB);
        ArrayList<SimpleEventElement> list = EventReport.getEvents(init, end, obs, eph, city);
        MoonEvent.SEVERAL_SIMULTANEOUS_EVENTS_MINIMUM_NUMBER = 2;
        Feed feedEng = EventReport.getFeed(list, obs);

        Translate.setDefaultLanguage(Translate.LANGUAGE.SPANISH);
        Feed feedSpa = EventReport.getFeed(list, obs);
        Translate.setDefaultLanguage(Translate.LANGUAGE.ENGLISH);

        ArrayList<FeedMessageElement> mesEng = feedEng.getMessages();
        System.out.println("Found " + mesEng.size() + " events");
        for (int i = 0; i < mesEng.size(); i++) {
            FeedMessageElement message = mesEng.get(i);
            message.link = "Link test";
            message.imageURL = new String[] { "http://hellow" + i };
        }
        ArrayList<FeedMessageElement> mesSpa = feedSpa.getMessages();
        for (int i = 0; i < mesSpa.size(); i++) {
            FeedMessageElement message = mesSpa.get(i);
            message.link = "Test enlace";
            message.imageURL = new String[] { "http://hola" + i };
        }

        //System.out.println(feedSpa.writeFeed("/home/alonso/testSpa.rss"));
        System.out.println(feedEng.writeFeed(null)); //"/home/alonso/testEng.rss"));
        System.out.println(feedSpa.writeFeed(null)); //"/home/alonso/testEng.rss"));
        long t1 = System.currentTimeMillis();
        float s = (t1 - t0) * 0.001f;
        System.out.println("Completed in " + s + "s");
    }
}
