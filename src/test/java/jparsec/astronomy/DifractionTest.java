package jparsec.astronomy;

public class DifractionTest {

    /**
     * For unit testing only.
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception
    {
        System.out.println("Difraction test");
        TelescopeElement telescope = TelescopeElement.SCHMIDT_CASSEGRAIN_20cm;
        double pattern[][] = Difraction.pattern(telescope, 4);
        pattern = Difraction.resample(pattern, 0.25);

        for (int j = 0; j < pattern.length; j++) {
            String line = "";

            for (int i = 0; i < pattern.length; i++) {
                double intensity = pattern[i][j];
                char a = (char) ('A' + (int) (intensity * 16));
                line += a;
            }

            System.out.println(line);
        }
    }
}
