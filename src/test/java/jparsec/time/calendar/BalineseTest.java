package jparsec.time.calendar;

public class BalineseTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String args[]) {
        System.out.println("Balinese test");

        double jd = 2457359.5;
        Balinese h = new Balinese(jd);
        System.out.println("JD " + jd + " = " + h);

        Balinese h1 = new Balinese(735935);
        System.out.println("JD " + jd + " = " + h1);

        Balinese h2 = new Balinese(h.luang, h.dwiwara, h.triwara, h.caturwara, h.pancawara, h.sadwara, h.saptawara, h.asatawara, h.sangawara, h.dasawara);
        System.out.println("JD " + jd + " = " + h2);

        System.out.println(Calendar.nameFromNumber(h2.dwiwara, Balinese.DWIWARA_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.triwara, Balinese.TRIWARA_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.caturwara, Balinese.CATURWARA_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.pancawara, Balinese.PANCAWARA_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.sadwara, Balinese.SADWARA_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.saptawara, Balinese.SAPTAWARA_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.asatawara, Balinese.ASATAWARA_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.sangawara, Balinese.SANGAWARA_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.dasawara, Balinese.DASAWARA_NAMES));
        System.out.println(Calendar.nameFromNumber(h2.week(), Balinese.WEEK_NAMES));
    }
}
