package jparsec.util;

import java.util.HashMap;
import java.util.Map;

public class Satellite2Planet {
    private final static Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("Moon", "Earth");

        map.put("Phobos", "Mars");
        map.put("Deimos", "Mars");

        map.put("Io", "Jupiter");
        map.put("Europa", "Jupiter");
        map.put("Ganymede", "Jupiter");
        map.put("Callisto", "Jupiter");
        map.put("Amalthea", "Jupiter");
        map.put("Thebe", "Jupiter");
        map.put("Adrastea", "Jupiter");
        map.put("Metis", "Jupiter");

        map.put("Himalia", "Jupiter");
        map.put("Elara", "Jupiter");
        map.put("Pasiphae", "Jupiter");
        map.put("Sinope", "Jupiter");
        map.put("Lysithea", "Jupiter");
        map.put("Carme", "Jupiter");
        map.put("Ananke", "Jupiter");
        map.put("Leda", "Jupiter");
        map.put("Callirrhoe", "Jupiter");
        map.put("Themisto", "Jupiter");
        map.put("Megaclite", "Jupiter");
        map.put("Taygete", "Jupiter");
        map.put("Chaldene", "Jupiter");
        map.put("Harpalyke", "Jupiter");
        map.put("Kalyke", "Jupiter");
        map.put("Iocaste", "Jupiter");
        map.put("Erinome", "Jupiter");
        map.put("Isonoe", "Jupiter");
        map.put("Praxidike", "Jupiter");
        map.put("Autonoe", "Jupiter");
        map.put("Thyone", "Jupiter");
        map.put("Hermippe", "Jupiter");
        map.put("Aitne", "Jupiter");
        map.put("Eurydome", "Jupiter");
        map.put("Euanthe", "Jupiter");
        map.put("Euporie", "Jupiter");
        map.put("Orthosie", "Jupiter");
        map.put("Sponde", "Jupiter");
        map.put("Kale", "Jupiter");
        map.put("Pasithee", "Jupiter");
        map.put("Hegemone", "Jupiter");
        map.put("Mneme", "Jupiter");
        map.put("Aoede", "Jupiter");
        map.put("Thelxinoe", "Jupiter");
        map.put("Arche", "Jupiter");
        map.put("Kallichore", "Jupiter");
        map.put("Helike", "Jupiter");
        map.put("Carpo", "Jupiter");
        map.put("Eukelade", "Jupiter");
        map.put("Cyllene", "Jupiter");
        map.put("Kore", "Jupiter");
        map.put("Herse", "Jupiter");
        map.put("S/2000", "Jupiter");
        map.put("S/2003", "Jupiter");
        map.put("S/2010", "Jupiter");
        map.put("S/2011", "Jupiter");

        map.put("Mimas", "Saturn");
        map.put("Enceladus", "Saturn");
        map.put("Tethys", "Saturn");
        map.put("Dione", "Saturn");
        map.put("Rhea", "Saturn");
        map.put("Titan", "Saturn");
        map.put("Hyperion", "Saturn");
        map.put("Iapetus", "Saturn");
        map.put("Phoebe", "Saturn");
        map.put("Janus", "Saturn");
        map.put("Epimetheus", "Saturn");
        map.put("Helene", "Saturn");
        map.put("Telesto", "Saturn");
        map.put("Calypso", "Saturn");
        map.put("Atlas", "Saturn");
        map.put("Prometheus", "Saturn");
        map.put("Pandora", "Saturn");
        map.put("Pan", "Saturn");
        map.put("Methone", "Saturn");
        map.put("Pallene", "Saturn");
        map.put("Polydeuces", "Saturn");
        map.put("Daphnis", "Saturn");
        map.put("Anthe", "Saturn");
        map.put("Aegaeon", "Saturn");

        map.put("Saturn", "Saturn");
        map.put("Ymir", "Saturn");
        map.put("Paaliaq", "Saturn");
        map.put("Tarvos", "Saturn");
        map.put("Ijiraq", "Saturn");
        map.put("Suttungr", "Saturn");
        map.put("Kiviuq", "Saturn");
        map.put("Mundilfari", "Saturn");
        map.put("Albiorix", "Saturn");
        map.put("Skathi", "Saturn");
        map.put("Erriapus", "Saturn");
        map.put("Siarnaq", "Saturn");
        map.put("Thrymr", "Saturn");
        map.put("Narvi", "Saturn");
        map.put("Aegir", "Saturn");
        map.put("Bebhionn", "Saturn");
        map.put("Bergelmir", "Saturn");
        map.put("Bestla", "Saturn");
        map.put("Farbauti", "Saturn");
        map.put("Fenrir", "Saturn");
        map.put("Fornjot", "Saturn");
        map.put("Hati", "Saturn");
        map.put("Hyrrokkin", "Saturn");
        map.put("Kari", "Saturn");
        map.put("Loge", "Saturn");
        map.put("Skoll", "Saturn");
        map.put("Surtur", "Saturn");
        map.put("Jarnsaxa", "Saturn");
        map.put("Greip", "Saturn");
        map.put("Tarqeq", "Saturn");
        map.put("S/2004", "Saturn");
        map.put("S/2006", "Saturn");
        map.put("S/2007", "Saturn");

        map.put("Ariel", "Uranus");
        map.put("Umbriel", "Uranus");
        map.put("Titania", "Uranus");
        map.put("Oberon", "Uranus");
        map.put("Miranda", "Uranus");

        map.put("Uranus", "Uranus");
        map.put("Cordelia", "Uranus");
        map.put("Ophelia", "Uranus");
        map.put("Bianca", "Uranus");
        map.put("Cressida", "Uranus");
        map.put("Desdemona", "Uranus");
        map.put("Juliet", "Uranus");
        map.put("Portia", "Uranus");
        map.put("Rosalind", "Uranus");
        map.put("Belinda", "Uranus");
        map.put("Puck", "Uranus");

        map.put("Perdita", "Uranus");
        map.put("Mab", "Uranus");
        map.put("Cupid", "Uranus");

        map.put("Caliban", "Uranus");
        map.put("Sycorax", "Uranus");
        map.put("Prospero", "Uranus");
        map.put("Setebos", "Uranus");
        map.put("Stephano", "Uranus");
        map.put("Trinculo", "Uranus");
        map.put("Francisco", "Uranus");
        map.put("Margaret", "Uranus");
        map.put("Ferdinand", "Uranus");

        map.put("Triton", "Neptune");
        map.put("Nereid", "Neptune");

        map.put("Naiad", "Neptune");
        map.put("Thalassa", "Neptune");
        map.put("Despina", "Neptune");
        map.put("Galatea", "Neptune");
        map.put("Larissa", "Neptune");
        map.put("Proteus", "Neptune");
        map.put("S/2004", "Neptune");

        map.put("Halimede", "Neptune");
        map.put("Psamathe", "Neptune");
        map.put("Sao", "Neptune");
        map.put("Laomedeia", "Neptune");
        map.put("Neso", "Neptune");

        map.put("Charon", "Pluto");
        map.put("Nix", "Pluto");
        map.put("Hydra", "Pluto");
        map.put("Kerberos", "Pluto");
        map.put("Styx", "Pluto");
    }

    public static String planetOf (final String satelite) {
        return map.get(satelite);
    }
}
