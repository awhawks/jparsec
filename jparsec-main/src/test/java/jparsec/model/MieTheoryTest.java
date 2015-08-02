package jparsec.model;

import jparsec.util.JPARSECException;

public class MieTheoryTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("MieTheory test");

        try {
            int grain = DustOpacity.GRAIN_ASTRONOMICAL_SILICATE;
            double p = 3.5;
            double max = 0.1;

            for (int pr = 2; pr <= 2; pr++) {
                switch (pr) {
                    case 2:
                        p = 3.0;
                        break;
                    case 1:
                        p = 3.5;
                        break;
                    case 0:
                        p = 2.5;
                        break;
                }

                for (int gr = 0; gr <= 4; gr++) {
                    switch (gr) {
                        case 0:
                            grain = DustOpacity.GRAIN_SMOOTHED_UV_ASTRONOMICAL_SILICATE;
                            break;
                        case 1:
                            grain = DustOpacity.GRAIN_SILICON_CARBIDE;
                            break;
                        case 2:
                            grain = DustOpacity.GRAIN_WATER_ICE;
                            break;
                        case 3:
                            grain = DustOpacity.GRAIN_ASTRONOMICAL_SILICATE;
                            break;
                        case 4:
                            grain = DustOpacity.GRAIN_GRAPHITE;
                            break;
                    }

                    DustOpacity dust = new DustOpacity(grain, p, max, 1.0);

                    /*
                    // To create a file of opacities
                    System.out.println("! Opacity of " + dust.getDustName() + " as function of wavelength");
                    System.out.println("! Column 1: wavelength (micron)");
                    System.out.println("! Columns 2-7: opacity for p = " + dust.sizeDistributionCoefficient + " and amax = 1, 10, 100, 1000, 10000");
                    double minWave = 0.1, maxWave = 1000;
                    int npLambda = 100, npA = 1000;
                    double wavelengths[] = SEDFit.getSetOfWavelengths(minWave, maxWave, npLambda, true);
                    String file_Qabs = "";
                    String file_Qext = "";
                    String file_Qsca = "";
                    String file_Qbsca = "";
                    String file_Qg = "";

                    int iMin = 0;
                    int iMax = wavelengths.length;

                    for (int i = iMin; i < iMax; i++) {
                        String line_Qabs = "" + wavelengths[i];
                        String line_Qext = "" + wavelengths[i];
                        String line_Qsca = "" + wavelengths[i];
                        String line_Qbsca = "" + wavelengths[i];
                        String line_Qg = "" + wavelengths[i];

                        for (int j = -1; j <= -1; j++) {
                            DustOpacity dusty = (DustOpacity) dust.clone();
                            dusty.sizeMax = Math.pow(10.0, (double) j);
                            double k[] = dusty.getMieCoefficients(wavelengths[i], npA);
                            line_Qabs += "   " + k[MieTheory.INDEX_OF_ABSORPTION_COEFFICIENT];
                            line_Qext += "   " + k[MieTheory.INDEX_OF_EXTINCTION_COEFFICIENT];
                            line_Qsca += "   " + k[MieTheory.INDEX_OF_SCATTERING_COEFFICIENT];
                            line_Qbsca += "   " + k[MieTheory.INDEX_OF_BACKSCATTERING_COEFFICIENT];
                            line_Qg += "   " + k[MieTheory.INDEX_OF_COSINE_AVERAGE_COEFFICIENT];
                            JPARSECException.clearWarnings();
                        }

                        System.out.println("Qabs");
                        System.out.println(line_Qabs);
                        System.out.println("Qext");
                        System.out.println(line_Qext);
                        System.out.println("Qsca");
                        System.out.println(line_Qsca);
                        System.out.println("Qbsca");
                        System.out.println(line_Qbsca);
                        System.out.println("Qg");
                        System.out.println(line_Qg);
                        file_Qabs += line_Qabs + FileIO.getLineSeparator();
                        file_Qext += line_Qext + FileIO.getLineSeparator();
                        file_Qsca += line_Qsca + FileIO.getLineSeparator();
                        file_Qbsca += line_Qbsca + FileIO.getLineSeparator();
                        file_Qg += line_Qg + FileIO.getLineSeparator();
                    }

                    WriteFile.writeAnyExternalFile("file_Qabs" + gr + "-" + pr + ".txt", file_Qabs);
                    WriteFile.writeAnyExternalFile("file_Qext" + gr + "-" + pr + ".txt", file_Qext);
                    WriteFile.writeAnyExternalFile("file_Qsca" + gr + "-" + pr + ".txt", file_Qsca);
                    WriteFile.writeAnyExternalFile("file_Qbsca" + gr + "-" + pr + ".txt", file_Qbsca);
                    WriteFile.writeAnyExternalFile("file_Qg" + gr + "-" + pr + ".txt", file_Qg);
                    */

                    MieTheory.reProcess("file_Qabs" + gr + "-" + pr + ".txt", "abs", dust);
                    MieTheory.reProcess("file_Qext" + gr + "-" + pr + ".txt", "ext", dust);
                    MieTheory.reProcess("file_Qsca" + gr + "-" + pr + ".txt", "sca", dust);
                    MieTheory.reProcess("file_Qbsca" + gr + "-" + pr + ".txt", "bsca", dust);
                    MieTheory.reProcess("file_Qg" + gr + "-" + pr + ".txt", "g", dust);
                }
            }

            /*
            double a = 10.0;
            int nang = 10;
            double max = a;
            double lambda = 500;
            double x = 2.0 * Math.PI * a / lambda;
            DustOpacity dust = new DustOpacity(DustOpacity.GRAIN_GRAPHITE, 3.5, max, 1.0);
            String fileName = "callindex.out_CpeD03_0.10.txt";
            double refractiveIndexPe[] = dust.getRefractiveIndex(lambda, fileName);
            double m = Math.sqrt(refractiveIndexPe[0] * refractiveIndexPe[0] + refractiveIndexPe[1] * refractiveIndexPe[1]);
            System.out.println(refractiveIndexPe[0] + "/" + refractiveIndexPe[1] + "/ mx = " + m * x);
            Complex cxref = new Complex(refractiveIndexPe[0], refractiveIndexPe[1]);
            MieTheory mt = new MieTheory(a, lambda, cxref, nang);
            double kpe = mt.qabs;
            System.out.println(" PERPENDICULAR, a = " + a);
            System.out.println(" lambda = " + lambda);
            //System.out.println(" Refractive index = "+ refractiveIndex[0]+ " + i "+refractiveIndex[1]);
            System.out.println(" qabs = " + mt.qabs);
            System.out.println(" qext = " + mt.qext);
            System.out.println(" qsca = " + mt.qsca);
            System.out.println(" gsca = " + mt.gsca);

            fileName = "callindex.out_CpaD03_0.10.txt";
            double refractiveIndexPa[] = dust.getRefractiveIndex(lambda, fileName);
            m = Math.sqrt(refractiveIndexPa[0] * refractiveIndexPa[0] + refractiveIndexPa[1] * refractiveIndexPa[1]);
            System.out.println(refractiveIndexPa[0] + "/" + refractiveIndexPa[1] + "/ mx = " + m * x);

            // Use 1/3 - 2/3 approximation. See Draine and Malhotra 1993.
            cxref = new Complex(refractiveIndexPa[0], refractiveIndexPa[1]);
            mt = new MieTheory(a, lambda, cxref, nang);
            mt.bhmie();
            double kpa = mt.qabs;

            System.out.println(" PARALLEL, a = " + a);
            System.out.println(" lambda = " + lambda);
            //System.out.println(" Refractive index = "+ refractiveIndex[0]+ " + i "+refractiveIndex[1]);
            System.out.println(" qabs = " + mt.qabs);
            System.out.println(" qext = " + mt.qext);
            System.out.println(" qsca = " + mt.qsca);
            System.out.println(" gsca = " + mt.gsca);

            System.out.println(" APROX 1/3-2/3");
            mt.qabs = (mt.qabs + 2.0 * kpe) / 3.0;
            System.out.println(" qabs = " + mt.qabs);
            */

            //double k = MieTheory.getAbsoptionCoefficient(dust, lambda);
            //System.out.println(" k = "+ k);
            JPARSECException.showWarnings();
        } catch (JPARSECException e) {
            JPARSECException.showException(e);
        }
    }
}
