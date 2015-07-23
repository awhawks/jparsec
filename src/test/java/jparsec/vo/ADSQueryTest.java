package jparsec.vo;

import jparsec.graph.DataSet;
import jparsec.io.FileIO;

public class ADSQueryTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
/*
        // Select a given specific article
        String query = ADSQuery.ADS_HARVARD_URL;
        query += ADSQuery.addParameter(ADSQuery.PARAMETER.BIBCODE, "2007A&A...470..625D");
        query += "&" +ADSQuery.addParameter(ADSQuery.PARAMETER.DATATYPE, ADSQuery.DATATYPE.BIBTEX.getType());

        String out = ADSQuery.query(query);
        System.out.println(out);
*/
        // Get all my abstracts as bibtex entries between a given year interval
        String author = "Alonso-Albi, T.";
        int year0 = 2012, yearf = 2012;
        String query = ADSQuery.getAuthorQuery(author, year0, yearf);

        String out = ADSQuery.query(query);
        System.out.println(out);

        // Now show the abstracts and download the PDFs
        String o[] = DataSet.toStringArray(out, FileIO.getLineSeparator(), true);
        ADSElement ads = null;

        for (int i = 0; i < o.length; i++) {
            if (o[i].trim().startsWith("@")) {
                int n = o[i].indexOf("{");
                String abs = o[i].substring(n + 1);
                if (abs.endsWith(",")) abs = abs.substring(0, abs.length() - 1);

                ads = new ADSElement(abs);
            }

            if (o[i].trim().startsWith("title") && ads != null) {
                String title = o[i].trim();
                title = title.substring(title.indexOf("{") + 1);
                title = title.substring(0, title.lastIndexOf("}"));
                System.out.println();
                System.out.println("*** " + title + " ***");
                System.out.println(ads.getAbstract());
                //ads.getArticle("/home/alonso/"+title+".pdf", 30000);
            }
        }
    }
}
