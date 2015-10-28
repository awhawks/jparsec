package jparsec.test;

/**
 * Created by carlo on 20.10.15.
 */
public class Splitter {
    private final static String[] calendar = {
            "Gregorian", "Julian", "Hebrew", "Coptic", "Ethiopic", "Persian", "Islamic", "HinduSolar"
    };

    private final static String lines[] = {
            "2000, 1, 2, Gregorian; 2000 1 2, 1999 12 20, 5760 10 24, 1716 4 23, 1992 4 23, 1378 10 12, 1420 9 25, 1921 9 18",
            "1901, 12, 29, Gregorian; 1901 12 29, 1901 12 16, 5662 10 19, 1618 4 20, 1894 4 20, 1280 10 8, 1319 9 18, 1823 9 15",
            "2101, 1, 1, Gregorian; 2101 1 1, 2100 12 18, 5861 9 30, 1817 4 22, 2093 4 22, 1479 10 11, 1524 11 1, 2022 9 16",
            "1999, 12, 20, Julian; 2000 1 2, 1999 12 20, 5760 10 24, 1716 4 23, 1992 4 23, 1378 10 12, 1420 9 25, 1921 9 18",
            "1901, 12, 16, Julian; 1901 12 29, 1901 12 16, 5662 10 19, 1618 4 20, 1894 4 20, 1280 10 8, 1319 9 18, 1823 9 15",
            "2100, 12, 18, Julian; 2101 1 1, 2100 12 18, 5861 9 30, 1817 4 22, 2093 4 22, 1479 10 11, 1524 11 1, 2022 9 16",
            "5760, 10, 24, Hebrew; 2000 1 2, 1999 12 20, 5760 10 24, 1716 4 23, 1992 4 23, 1378 10 12, 1420 9 25, 1921 9 18",
            "5662, 10, 19, Hebrew; 1901 12 29, 1901 12 16, 5662 10 19, 1618 4 20, 1894 4 20, 1280 10 8, 1319 9 18, 1823 9 15",
            "5861, 9, 30, Hebrew; 2101 1 1, 2100 12 18, 5861 9 30, 1817 4 22, 2093 4 22, 1479 10 11, 1524 11 1, 2022 9 16",
            "1716, 4, 23, Coptic; 2000 1 2, 1999 12 20, 5760 10 24, 1716 4 23, 1992 4 23, 1378 10 12, 1420 9 25, 1921 9 18",
            "1618, 4, 20, Coptic; 1901 12 29, 1901 12 16, 5662 10 19, 1618 4 20, 1894 4 20, 1280 10 8, 1319 9 18, 1823 9 15",
            "1817, 4, 22, Coptic; 2101 1 1, 2100 12 18, 5861 9 30, 1817 4 22, 2093 4 22, 1479 10 11, 1524 11 1, 2022 9 16",
            "1992, 4, 23, Ethiopic; 2000 1 2, 1999 12 20, 5760 10 24, 1716 4 23, 1992 4 23, 1378 10 12, 1420 9 25, 1921 9 18",
            "1894, 4, 20, Ethiopic; 1901 12 29, 1901 12 16, 5662 10 19, 1618 4 20, 1894 4 20, 1280 10 8, 1319 9 18, 1823 9 15",
            "2093, 4, 22, Ethiopic; 2101 1 1, 2100 12 18, 5861 9 30, 1817 4 22, 2093 4 22, 1479 10 11, 1524 11 1, 2022 9 16",
            "1378, 10, 12, Persian; 2000 1 2, 1999 12 20, 5760 10 24, 1716 4 23, 1992 4 23, 1378 10 12, 1420 9 25, 1921 9 18",
            "1280, 10, 8, Persian; 1901 12 29, 1901 12 16, 5662 10 19, 1618 4 20, 1894 4 20, 1280 10 8, 1319 9 18, 1823 9 15",
            "1479, 10, 11, Persian; 2101 1 1, 2100 12 18, 5861 9 30, 1817 4 22, 2093 4 22, 1479 10 11, 1524 11 1, 2022 9 16",
            "1420, 9, 25, Islamic; 2000 1 2, 1999 12 20, 5760 10 24, 1716 4 23, 1992 4 23, 1378 10 12, 1420 9 25, 1921 9 18",
            "1319, 9, 18, Islamic; 1901 12 29, 1901 12 16, 5662 10 19, 1618 4 20, 1894 4 20, 1280 10 8, 1319 9 18, 1823 9 15",
            "1524, 11, 1, Islamic; 2101 1 1, 2100 12 18, 5861 9 30, 1817 4 22, 2093 4 22, 1479 10 11, 1524 11 1, 2022 9 16",
            "1921, 9, 18, HinduSolar; 2000 1 2, 1999 12 20, 5760 10 24, 1716 4 23, 1992 4 23, 1378 10 12, 1420 9 25, 1921 9 18",
            "1823, 9, 15, HinduSolar; 1901 12 29, 1901 12 16, 5662 10 19, 1618 4 20, 1894 4 20, 1280 10 8, 1319 9 18, 1823 9 15",
            "2022, 9, 16, HinduSolar; 2101 1 1, 2100 12 18, 5861 9 30, 1817 4 22, 2093 4 22, 1479 10 11, 1524 11 1, 2022 9 16",
    };

    private final static String[] lines2 = {
        "2009,8,16,5,1 ECL 3 A,20 31 8,20 45 57,1662",
        "2009,8,18,5,1 OCC 2 P,14 26 11,14 37 59,1358",
        "2009,8,19,5,3 ECL 2 P,6 4 39,6 13 48,1110",
        "2009,8,22,5,1 OCC 2 P,4 0 30,4 9 32,1058",
        "2009,8,26,5,3 ECL 2 P,10 59 37,11 12 29,1577",
        "2009,8,26,5,3 OCC 2 P,9 16 21,9 26 55,1283",
        "2009,8,28,5,1 ECL 2 P,13 50 43,13 59 32,1095",
        "2009,8,28,5,1 OCC 2 P,12 42 60,12 51 55,1096",
        "2009,8,30,5,1 ECL 3 P,7 47 19,7 57 59,1348",
        "2009,9,1,5,1 ECL 2 P,3 55 39,4 10 39,2069",
        "2009,9,1,5,1 OCC 2 P,2 4 46,2 15 23,1320",
        "2009,9,2,5,3 OCC 2 P,13 37 17,13 53 38,2020",
        "2009,9,4,5,1 OCC 2 P,15 37 58,15 52 8,1815",
        "2010,3,17,5,2 OCC 1 P,22 40 41,23 1 43,2727",
        "2010,4,4,5,2 ECL 1 P,9 13 28,9 32 9,2147",
        "! Saturn (TASS)",
        "2008,12,19,6,4 OCC 5 P,2 4 57,2 12 15,875",
        "2008,12,31,6,4 OCC 1 T,17 53 53,18 5 17,1365",
        "2009,1,22,6,1 OCC 2 P,5 32 45,5 39 9,769",
        "2009,1,22,6,2 ECL 3 P,7 44 34,7 48 50,596",
        "2009,4,17,6,2 ECL 3 P,7 1 23,7 5 47,596",
        "2009,6,24,6,5 ECL 4 P,13 57 6,14 4 46,1116",
        "2009,7,1,6,4 ECL 5 P,7 31 19,7 35 9,601",
        "2009,7,10,6,2 OCC 3 A,17 48 23,17 53 31,617",
        "2009,8,2,6,6 ECL 5 T,18 54 10,18 57 2,597",
        "2009,9,1,6,5 ECL 3 P,1 44 12,1 51 43,1226",
        "2009,12,23,6,3 ECL 4 P,18 50 13,18 54 42,647",
        "2010,4,24,6,4 OCC 3 P,15 14 55,15 29 59,1805",
        "2010,6,18,6,4 OCC 3 P,8 10 18,8 19 10,1061",
        "! Uranus (GUST86)",
        "2007,5,3,7,1 OCC 3 P,2 12 58,2 22 3,1108",
        "2007,5,3,7,1 OCC 3 P,12 57 18,13 9 45,1493",
        "2007,8,5,7,4 OCC 2 P,13 42 56,13 52 20,1159",
        "2007,8,6,7,4 OCC 2 P,0 56 35,1 11 39,1807",
        "2007,10,21,7,1 ECL 2 P,22 53 5,23 11 32,2087",
        "2007,11,11,7,1 ECL 3 P,3 1 53,3 13 30,1237",
        "2007,11,30,7,3 ECL 4 P,18 32 16,18 46 34,1482",
        "2007,12,24,7,2 ECL 1 P,18 44 52,18 53 54,1010",
        "2007,12,25,7,2 ECL 1 P,17 0 3,17 24 14,2764",
        "2008,2,21,7,2 OCC 1 P,19 33 41,19 42 33,1064",
        "2008,8,12,7,1 ECL 5 T,22 12 35,22 24 9,1317",
        "2008,8,19,7,5 ECL 2 P,9 16 57,9 26 44,1024",
        "2008,8,23,7,5 OCC 2 A,21 16 58,21 27 22,1304",
        "2009,6,11,7,1 ECL 5 T,18 47 57,19 10 52,2636",
        "2009,12,2,7,1 ECL 5 T,8 44 39,9 6 29,2504",
    };

    public static void main (final String[] args) {
        //splitLines();
        splitLines2();
    }

    /*
                { new Gregorian(2000, 1, 2).toJulianDay(),
                        new Gregorian(2000, 1, 2).toJulianDay(),
                        new Julian(1999, 12, 20).toJulianDay(),
                        new Hebrew(5760, 10, 24).toJulianDay(),
                        new Coptic(1716, 4, 23).toJulianDay(),
                        new Ethiopic(1992, 4, 23).toJulianDay(),
                        new Persian(1378, 10, 12).toJulianDay(),
                        new Islamic(1420, 9, 25).toJulianDay(),
                        new HinduSolar(1921, 9, 18).toJulianDay()
                },
     */

    private static void splitLines () {
        int cal = 0;

        for (String line : lines) {
            String[] tokens = line.split("[;,]");
            StringBuffer buf = new StringBuffer("\n                { new " + calendar[cal / 3] + " (" + tokens[0] + ", " + tokens[1] + ", " + tokens[2] + ").toJulianDay (),");

            for (int i = 4; i < 12; i++) {
                buf.append("\n                          new ").append(calendar[i - 4]).append(" (")
                        .append(tokens[i].substring(1).replaceAll(" ", ", "))
                        .append(").toJulianDay (),");
            }

            buf.append("\n                },");
            String code = buf.toString().replaceAll("  ", " ");
            System.out.print(code);
            cal++;
        }
    }

    /*
            "2009, 8, 16, 5, 1 ECL 3 A; 20 31  8, 20 45 57, 1662",
            { new AstroDate(2009,  8, 16), TARGET.JUPITER, "1 ECL 3 A", "20 31  8", "20 45 57", 1662 },
     */
    private static void splitLines2 () {
        for (String line : lines2) {
            if (line.startsWith("!")) {
                System.out.println("                //" + line.substring(1));
                continue;
            }

            //System.out.println("                //" + line);
            String[] tokens = line.split(",");
            String[] time = tokens[5].trim().split(" ");
            int hour = Integer.valueOf(time[0]);
            int min = Integer.valueOf(time[1]);
            int secs = Integer.valueOf(time[2]); // - 480.0;
            StringBuffer buf = new StringBuffer("                { new AstroDate (" + tokens[0] + ", " + tokens[1] + ", " + tokens[2] + ", " + hour + ", " + min + ", " + secs + "), TARGET.");

            switch (Integer.valueOf(tokens[3].trim())) {
                case 5 : buf.append("JUPITER"); break;
                case 6 : buf.append("SATURN"); break;
                case 7 : buf.append("URANUS"); break;
                default : buf.append("NOT_A_PLANET"); break;
            }

            time = tokens[6].trim().split(" ");
            hour = Integer.valueOf(time[0]);
            min = Integer.valueOf(time[1]);
            secs = Integer.valueOf(time[2]); // - 480.0;

            buf.append(", \"").append(tokens[4])
                    .append("\", new AstroDate (" + tokens[0] + ", " + tokens[1] + ", " + tokens[2] + ", " + hour + ", " + min + ", " + secs + "), ")
                    .append(tokens[7]).append(" },");

            System.out.println(buf.toString().replaceAll(" ([0-9]),", "  $1,"));
        }
    }
}
