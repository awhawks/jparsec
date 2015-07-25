package jparsec.vo;

public class ADSElementTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        int year = 2006;
        String journal = "ApJ";
        int volume = 649;
        String publicationType = "Letter";
        int page = 119;
        String author = "Fuente";

        ADSElement bib = new ADSElement(year, journal, volume, publicationType, page, author);
        String bibTex = bib.getBibCode();
        System.out.println(bibTex);
        String bibTexEntry = bib.getBibTexEntry();
        System.out.println(bibTexEntry);
        bib.getArticle("/home/alonso/myarticle.pdf", 30000);
        String abs = bib.getAbstract();
        System.out.println(abs);
    }
}
