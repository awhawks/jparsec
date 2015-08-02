package jparsec.vo;

import jparsec.graph.DataSet;
import jparsec.io.FileIO;
import jparsec.util.JPARSECException;

public class SimbadQueryTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        try {
            String name = "HD259431";

            if (args != null && args.length > 0) {
                name = DataSet.toString(args, " ");
                System.out.println("<html><body><pre>");
            } else {
                System.out.println("SimbadQuery test");
            }

            SimbadElement oe = SimbadQuery.query(name);
            System.out.println(oe.name);
            System.out.println(oe.rightAscension);
            System.out.println(oe.declination);
            System.out.println(oe.type);
            System.out.println(oe.spectralType);
            System.out.println(oe.bMinusV);
            System.out.println(oe.properMotionRA);
            System.out.println(oe.properMotionDEC);
            System.out.println(oe.properMotionRadialV);
            System.out.println(oe.parallax);

            if (oe.otherNames != null && oe.otherNames.length > 0) {
                for (int i = 0; i < oe.otherNames.length; i++) {
                    System.out.println(oe.otherNames[i]);
                }
            }

            if (args != null && args.length > 0)
                System.out.println("</pre></body></html>");
        } catch (Exception e) {
            System.out.println(DataSet.toString(JPARSECException.toStringArray(e.getStackTrace()), FileIO.getLineSeparator()));
        }
    }
}
