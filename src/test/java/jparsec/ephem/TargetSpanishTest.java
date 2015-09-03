package jparsec.ephem;

import static jparsec.ephem.Target.TARGET.*;

import jparsec.util.JPARSECException;
import jparsec.util.Translate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

public class TargetSpanishTest {
    @DataProvider(name = "names")
    private final static Object[][] data_names() {
        return new Object[][] {
            { SUN, "Sol" },
            { MERCURY, "Mercurio" },
            { VENUS, "Venus" },
            { EARTH, "Tierra" },
            { MARS, "Marte" },
            { JUPITER, "J\u00fapiter" },
            { SATURN, "Saturno" },
            { URANUS, "Urano" },
            { NEPTUNE, "Neptuno" },
            { Pluto, "Plut\u00f3n" },
            { Moon, "Luna" },
            { Earth_Moon_Barycenter, "Baricentro Tierra-Luna" },
            { Comet, "Cometa" },
            { Asteroid, "Asteroide" },
            { NEO, "Objeto Cercano a la Tierra" },
            { Nutation, "Nutaci\u00f3n" },
            { Libration, "Libraci\u00f3n" },
            { Solar_System_Barycenter, "Baricentro del Sistema Solar" },
            { Phobos, "Fobos" },
            { Deimos, "Deimos" },
            { Io, "\u00cdo" },
            { Europa, "Europa" },
            { Ganymede, "Ganimedes" },
            { Callisto, "Calisto" },
            { Mimas, "Mimas" },
            { Enceladus, "Enc\u00e9lado" },
            { Tethys, "Tetis" },
            { Dione, "Dione" },
            { Rhea, "Rea" },
            { Titan, "Tit\u00e1n" },
            { Hyperion, "Hiperi\u00f3n" },
            { Iapetus, "Japeto" },
            { Miranda, "Miranda" },
            { Ariel, "Ariel" },
            { Umbriel, "Umbriel" },
            { Titania, "Titania" },
            { Oberon, "Ober\u00f3n" },
            { Triton, "Trit\u00f3n" },
            { Nereid, "Nereida" },
            { Charon, "Caronte" },
            { Amalthea, "Amaltea" },
            { Thebe, "Tebe" },
            { Adrastea, "Adrastea" },
            { Metis, "Metis" },
            { Himalia, "Himalia" },
            { Elara, "Elara" },
            { Pasiphae, "Pas\u00edfae" },
            { Sinope, "S\u00ednope" },
            { Lysithea, "Lisitea" },
            { Carme, "Carmen" },
            { Ananke, "Ananque" },
            { Leda, "Leda" },
            { Atlas, "Atlas" },
            { Prometheus, "Prometeo" },
            { Pandora, "Pandora" },
            { Pan, "Pan" },
            { Epimetheus, "Epimeteo" },
            { Janus, "Jano" },
            { Telesto, "Telesto" },
            { Calypso, "Calipso" },
            { Helene, "Elena" },
            { Phoebe, "Febe" },
            { Cordelia, "Cordelia" },
            { Ophelia, "Ofelia" },
            { Cressida, "Cr\u00e9sida" },
            { Bianca, "Bianca" },
            { Desdemona, "Desd\u00e9mona" },
            { Juliet, "Julieta" },
            { Portia, "Porcia" },
            { Rosalind, "Rosalinda" },
            { Puck, "Puck" },
            { Belinda, "Belinda" },
            { Naiad, "N\u00e1yade" },
            { Thalassa, "Talasa" },
            { Despina, "Despina" },
            { Galatea, "Galatea" },
            { Larissa, "Larisa" },
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
            { NOT_A_PLANET, "No es planeta" },
        };
    }

    @BeforeClass
    public void setLanguage() {
        Translate.setDefaultLanguage(Translate.LANGUAGE.SPANISH);
    }

    @Test(dataProvider = "names")
    public void testName(final Target.TARGET target, final String name) {
        assertEquals(name, target.getName());
    }

    @Test(dataProvider = "names")
    public void testGetID(final Target.TARGET target, final String name) {
        Target.TARGET t = null;
        try {
            t = Target.getID(name);
            assertEquals(target, t);
        } catch (JPARSECException ignored) {
            fail();
        }
    }

    @Test(expectedExceptions = JPARSECException.class)
    public void testGetIDExceptionNull () throws Exception {
        Target.getID(null);
    }

    @Test(expectedExceptions = JPARSECException.class)
    public void testGtIDExceptionEmptyString () throws Exception {
        Target.getID("");
    }

    @Test
    public void testGetIDNull() throws Exception {
        assertNull(Target.getID("sinsentido"));
    }
}
