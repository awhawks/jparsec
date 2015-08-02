package jparsec.time.calendar;

public class BalineseTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("Balinese test");
        int jd = 2451545;
        Balinese h = new Balinese(jd);
        String date = "luang=" + h.luang + ",dwiwara=" + h.dwiwara + ",triwara=" + h.triwara + ",caturwara=" + h.caturwara + ",pancawara=" + h.pancawara + ",sadwara=" + h.sadwara + ",saptawara=" + h.saptawara + ",asatawara=" + h.asatawara + ",sangawara=" + h.sangawara + ",dasawara=" + h.dasawara;
        System.out.println("JD " + jd + " = " + date);

        Balinese h2 = new Balinese(h.luang, h.dwiwara, h.triwara, h.caturwara, h.pancawara, h.sadwara, h.saptawara, h.asatawara, h.sangawara, h.dasawara);
        date = "luang=" + h2.luang + ",dwiwara=" + h2.dwiwara + ",triwara=" + h2.triwara + ",caturwara=" + h2.caturwara + ",pancawara=" + h2.pancawara + ",sadwara=" + h2.sadwara + ",saptawara=" + h2.saptawara + ",asatawara=" + h2.asatawara + ",sangawara=" + h2.sangawara + ",dasawara=" + h2.dasawara;

        System.out.println(date);
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
