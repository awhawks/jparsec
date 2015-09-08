package jparsec.ephem;

import jparsec.util.JPARSECException;
import jparsec.util.Translate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static jparsec.ephem.Target.TARGET.*;
import static jparsec.ephem.Target.TARGET.Nutation;
import static org.testng.Assert.*;

public class TargetEnglishTest {
    @DataProvider(name = "names")
    private final static Object[][] data_names() {
        return new Object[][] {
            { SUN, "Sun" },
            { MERCURY, "Mercury" },
            { VENUS, "Venus" },
            { EARTH, "Earth" },
            { MARS, "Mars" },
            { JUPITER, "Jupiter" },
            { SATURN, "Saturn" },
            { URANUS, "Uranus" },
            { NEPTUNE, "Neptune" },
            { Pluto, "Pluto" },
            { Moon, "Moon" },
            { Earth_Moon_Barycenter, "Earth-Moon barycenter" },
            { Comet, "Comet" },
            { Asteroid, "Asteroid" },
            { NEO, "Near Earth Object" },
            { Nutation, "Nutation" },
            { Libration, "Libration" },
            { Solar_System_Barycenter, "Solar system barycenter" },
            { Phobos, "Phobos" },
            { Deimos, "Deimos" },
            { Io, "Io" },
            { Europa, "Europa" },
            { Ganymede, "Ganymede" },
            { Callisto, "Callisto" },
            { Mimas, "Mimas" },
            { Enceladus, "Enceladus" },
            { Tethys, "Tethys" },
            { Dione, "Dione" },
            { Rhea, "Rhea" },
            { Titan, "Titan" },
            { Hyperion, "Hyperion" },
            { Iapetus, "Iapetus" },
            { Miranda, "Miranda" },
            { Ariel, "Ariel" },
            { Umbriel, "Umbriel" },
            { Titania, "Titania" },
            { Oberon, "Oberon" },
            { Triton, "Triton" },
            { Nereid, "Nereid" },
            { Charon, "Charon" },
            { Amalthea, "Amalthea" },
            { Thebe, "Thebe" },
            { Adrastea, "Adrastea" },
            { Metis, "Metis" },
            { Himalia, "Himalia" },
            { Elara, "Elara" },
            { Pasiphae, "Pasiphae" },
            { Sinope, "Sinope" },
            { Lysithea, "Lysithea" },
            { Carme, "Carme" },
            { Ananke, "Ananke" },
            { Leda, "Leda" },
            { Atlas, "Atlas" },
            { Prometheus, "Prometheus" },
            { Pandora, "Pandora" },
            { Pan, "Pan" },
            { Epimetheus, "Epimetheus" },
            { Janus, "Janus" },
            { Telesto, "Telesto" },
            { Calypso, "Calypso" },
            { Helene, "Helene" },
            { Phoebe, "Phoebe" },
            { Cordelia, "Cordelia" },
            { Ophelia, "Ophelia" },
            { Cressida, "Cressida" },
            { Bianca, "Bianca" },
            { Desdemona, "Desdemona" },
            { Juliet, "Juliet" },
            { Portia, "Portia" },
            { Rosalind, "Rosalind" },
            { Puck, "Puck" },
            { Belinda, "Belinda" },
            { Naiad, "Naiad" },
            { Thalassa, "Thalassa" },
            { Despina, "Despina" },
            { Galatea, "Galatea" },
            { Larissa, "Larissa" },
            { Proteus, "Proteus" },
            { Ceres, "Ceres" },
            { Pallas, "Pallas" },
            { Vesta, "Vesta" },
            { Lutetia, "Lutetia" },
            { Ida, "Ida" },
            { Eros, "Eros" },
            { Davida, "Davida" },
            { Gaspra, "Gaspra" },
            { Steins, "Steins" },
            { Itokawa, "Itokawa" },
            { P9_Tempel_1, "9P/Tempel 1" },
            { P19_Borrelly, "19P/Borrelly" },
            { NOT_A_PLANET, "Not a planet" },
        };
    }

    @BeforeClass
    public void setLanguage() {
        Translate.setDefaultLanguage(Translate.LANGUAGE.ENGLISH);
    }

    @Test(dataProvider = "names")
    public void testName(final Target.TARGET target, final String name) {
        assertEquals(name, target.getName());
        assertEquals(name, target.getEnglishName());
    }

    @Test(dataProvider = "names")
    public void testGetID(final Target.TARGET target, final String name) {
        Target.TARGET t = null;
        try {
            t = Target.getID(name);
            assertEquals(target, t);
            t = Target.getIDFromEnglishName(name);
            assertEquals(target, t);
        } catch (JPARSECException ignored) {
            fail();
        }
    }

    @Test(expectedExceptions = JPARSECException.class)
    public void testGetIDFromEnglishNameExceptionNull () throws Exception {
        Target.getIDFromEnglishName(null);
    }

    @Test(expectedExceptions = JPARSECException.class)
    public void testGtIDFromEnglishNameExceptionEmptyString () throws Exception {
        Target.getIDFromEnglishName("");
    }

    @Test
    public void testGetIDFromEnglishNameNull() throws Exception {
        assertEquals(Target.TARGET.NOT_A_PLANET, Target.getIDFromEnglishName("nonsense"));
    }
}
