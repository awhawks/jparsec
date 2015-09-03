package jparsec.ephem;

import static jparsec.ephem.Target.TARGET.*;

import jparsec.util.JPARSECException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TargetTest {
    @DataProvider(name = "earthCentered")
    private final static Object[][] data_earthCentered() {
        return new Object[][] {
            { Moon },
            { Nutation },
        };
    }

    @Test(dataProvider = "earthCentered")
    public void testEarthCentralBody(final Target.TARGET target) {
        assertEquals(EARTH, target.getCentralBody());
    }

    @DataProvider(name = "marsCentered")
    private final static Object[][] data_marsCentered() {
        return new Object[][] {
            { Phobos },
            { Deimos },
        };
    }

    @Test(dataProvider = "marsCentered")
    public void testMarsCentralBody(final Target.TARGET target) {
        assertEquals(MARS, target.getCentralBody());
    }

    @DataProvider(name = "jupiterCentered")
    private final static Object[][] data_jupiterCentered() {
        return new Object[][] {
            { Io },
            { Europa },
            { Ganymede },
            { Callisto },
            { Amalthea },
            { Thebe },
            { Adrastea },
            { Metis },
            { Himalia },
            { Elara },
            { Pasiphae },
            { Sinope },
            { Lysithea },
            { Carme },
            { Ananke },
            { Leda },
        };
    }

    @Test(dataProvider = "jupiterCentered")
    public void testJupiterCentralBody(final Target.TARGET target) {
        assertEquals(JUPITER, target.getCentralBody());
    }

    @DataProvider(name = "saturnCentered")
    private static final Object[][] data_saturnCentered() {
        return new Object[][] {
            { Mimas },
            { Enceladus },
            { Tethys },
            { Dione },
            { Rhea },
            { Titan },
            { Hyperion },
            { Iapetus },
            { Atlas },
            { Prometheus },
            { Pandora },
            { Pan },
            { Epimetheus },
            { Janus },
            { Telesto },
            { Calypso },
            { Helene },
            { Phoebe },
        };
    }

    @Test(dataProvider = "saturnCentered")
    public void testSaturnCentralBody(final Target.TARGET target) {
        assertEquals(SATURN, target.getCentralBody());
    }

    @DataProvider(name = "uranusCentered")
    private static final Object[][] data_uranusCentered() {
        return new Object[][] {
            { Miranda },
            { Ariel },
            { Umbriel },
            { Titania },
            { Oberon },
            { Cordelia },
            { Ophelia },
            { Cressida },
            { Bianca },
            { Desdemona },
            { Juliet },
            { Portia },
            { Rosalind },
            { Puck },
            { Belinda },
        };
    }

    @Test(dataProvider = "uranusCentered")
    public void testUranusCentralBody(final Target.TARGET target) {
        assertEquals(URANUS, target.getCentralBody());
    }

    @DataProvider(name = "neptuneCentered")
    private static final Object[][] data_neptuneCentered() {
        return new Object[][] {
            { Triton },
            { Nereid },
            { Naiad },
            { Thalassa },
            { Despina },
            { Galatea },
            { Larissa },
            { Proteus },
        };
    }

    @Test(dataProvider = "neptuneCentered")
    public void testNeptuneCentralBody(final Target.TARGET target) {
        assertEquals(NEPTUNE, target.getCentralBody());
    }

    @Test
    public void testPlutoCentralBody() {
        assertEquals(Pluto, Charon.getCentralBody());
    }

    @DataProvider(name = "sunCentered")
    private final static Object[][] data_sunCentered() {
        return new Object[][] {
            { SUN },
            { MERCURY },
            { VENUS },
            { EARTH },
            { MARS },
            { JUPITER },
            { SATURN },
            { URANUS },
            { NEPTUNE },
            { Pluto },
            { Earth_Moon_Barycenter },
            { Comet },
            { Asteroid },
            { NEO },
            { Libration },
            { Solar_System_Barycenter },
            { Ceres },
            { Pallas },
            { Vesta },
            { Lutetia },
            { Ida },
            { Eros },
            { Davida },
            { Gaspra },
            { Steins },
            { Itokawa },
            { P9_Tempel_1 },
            { P19_Borrelly },
            { NOT_A_PLANET },
        };
    }

    @Test(dataProvider = "sunCentered")
    public void testSunCentralBody(final Target.TARGET target) {
        assertEquals(SUN, target.getCentralBody());
    }
}
