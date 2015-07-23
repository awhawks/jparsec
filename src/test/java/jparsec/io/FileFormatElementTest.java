package jparsec.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import jparsec.graph.DataSet;

public class FileFormatElementTest {
    /**
     * A program to transform the raw Sky Master 2000 catalog format to
     * JPARSEC format.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        //FileFormatElement.main1();
        //FileFormatElement.main2();
        String path = "/home/alonso/eclipse/libreria_jparsec/sky2000/ATT_sky2kv5.cat";
        String outputPath = "/home/alonso/out.txt", output = "", aux = "";
        double maglim1 = -10, maglim2 = 6.5;
        //double maglim1 = 6.5, maglim2 = 7.5;
        //double maglim1 = 7.5, maglim2 = 8.5;
        //double maglim1 = 8.5, maglim2 = 9.0;
        //double maglim1 = 9.0, maglim2 = 9.5;
        //double maglim1 = 9.5, maglim2 = 10.0;
        // Connect to the file
        int nline = 0;

        try {
            URLConnection Connection = new URL("file:" + path).openConnection();
            InputStream is = Connection.getInputStream();
            BufferedReader dis = new BufferedReader(new InputStreamReader(is, ReadFile.ENCODING_UTF_8));
            ReadFormat rf = new ReadFormat(FileFormatElement.SKY2000_STARS_FORMAT);
            String line;
            String format = "I8, 1x, F2.8, 1x, F3.7, 1x, F2.5, 1x, F3.4, 1x, F3.2, 1x, F1.3, 1x, A1, 1x, A2, 1x, A6, 1x, A2";
            String greek = "AlpBetGamDelEpsZetEtaTheIotKapLamMy Ny Xi OmiPi RhoSigTauIpsPhiChiPsiOme";
            int n = greek.length() / 3;
            while ((line = dis.readLine()) != null) {
                nline++;
                String mag = rf.readString(line, "MAG").trim();
                if (mag.equals("")) mag = rf.readString(line, "MAG_DERIVED").trim();
                if (!mag.equals("")) {
                    double m = Double.parseDouble(mag);
                    if (m > maglim1 && m <= maglim2) {
                        double rah = rf.readDouble(line, "RA_HOUR_J2000");
                        double ram = rf.readDouble(line, "RA_MIN_J2000");
                        double ras = rf.readDouble(line, "RA_SEC_J2000");
                        double ra = rah + (ram + ras / 60.0) / 60.0;

                        String decDeg = rf.readString(line, "DEC_DEG_J2000").trim();
                        double decg = Double.parseDouble(decDeg.substring(1));
                        double decm = rf.readDouble(line, "DEC_MIN_J2000");
                        double decs = rf.readDouble(line, "DEC_SEG_J2000");
                        double dec = Math.abs(decg) + (decm + decs / 60.0) / 60.0;
                        if (rf.readString(line, "DEC_DEG_J2000").startsWith("-")) {
                            dec = -dec;
                        }

                        String wds = rf.readString(line, "WDS").trim();
                        String maxMag = rf.readString(line, "MAX_MAG").trim();
                        String minMag = rf.readString(line, "MIN_MAG").trim();

                        String tipo = "N";
                        if (!wds.equals("")) tipo = "D";
                        if (!maxMag.equals("") && !minMag.equals("")) {
                            if (tipo.equals("D")) {
                                tipo = "B";
                            } else {
                                tipo = "V";
                            }
                        }

                        String name = rf.readString(line, "NAME").trim();
                        name = FileIO.getField(1, name, " ", true);
                        String flams = "";
                        if (name != null) {
                            if (!name.equals("")) {
                                for (int i = 0; i < n; i++) {
                                    int v = name.toLowerCase().indexOf(greek.substring(i * 3, i * 3 + 3).toLowerCase());
                                    if (v >= 0) {
                                        v = i + 1;
                                        flams = "" + v;
                                        break;
                                    }
                                }
                            }
                        }

                        String decPM = rf.readString(line, "DEC_PM");
                        decPM = DataSet.replaceAll(decPM, " ", "", false);
                        if (decPM.equals("")) decPM = "0.0";

                        String raPM = rf.readString(line, "RA_PM");
                        if (raPM.equals("")) raPM = "0.0";

                        String par = rf.readString(line, "PARALLAX").trim();
                        double para = 0.0;
                        if (!par.equals("")) para = Math.abs(Double.parseDouble(par)) * 1000.0;

                        String radial = rf.readString(line, "RADIAL_VELOCITY");
                        radial = DataSet.replaceAll(radial, " ", "0", false);

                        String addon = "";
                        String sep1 = ",", sep2 = ";";
                        if (tipo.equals("D") || tipo.equals("B")) {
                            String sep = rf.readString(line, "SEP_OF_MAIN_COMPONENTS");
                            String magDif = rf.readString(line, "MAG_DIF_A-B");
                            String orbPer = rf.readString(line, "ORBIT_PERIOD");
                            String posA = rf.readString(line, "POSITION_ANGLE");
                            addon = sep + sep1 + magDif + sep1 + orbPer + sep1 + posA;
/*
                            try {
                                double s = Double.parseDouble(sep);
                                double p = Double.parseDouble(posA);
                                double mm = Double.parseDouble(mag);
                                double mmd = Double.parseDouble(magDif);
                                String ssep = " & ";
                                if (s > 2.0 && mm < 5 && mmd < 7.1 && mm+mmd < 8 && mm+mmd+mm < 11) {
                                    String l = rf.readString(line, "SKYMAP") + ssep + Functions.formatRAOnlyMinutes(ra/Constant.RAD_TO_HOUR, 1) + ssep + Functions.formatDECOnlyMinutes(dec*Constant.DEG_TO_RAD, 0) + ssep + "AB" + ssep + Functions.formatValue(mm, 2) + ssep + Functions.formatValue(mmd+mm, 2) + ssep + s + ssep + p;
                                    l = DataSet.replaceAll(l, ",", ".");
                                    System.out.println(l+" \\\\");
                                }
                            } catch (Exception exc) {}
*/
                        }
                        if (tipo.equals("V") || tipo.equals("B")) {
                            String maxM = rf.readString(line, "MAX_MAG");
                            String minM = rf.readString(line, "MIN_MAG");
                            String varP = rf.readString(line, "VAR_PERIOD");
                            String varT = rf.readString(line, "VAR_TYPE");
                            String dmagBand = rf.readString(line, "DMAG_BAND").trim();
                            addon += sep2 + maxM + sep1 + minM + sep1 + varP + sep1 + varT;
                            if (!dmagBand.equals("V")) addon += sep1 + dmagBand;
                        }

                        String val[] = new String[] {
                                rf.readString(line, "SKYMAP"),
                                "" + ra,
                                "" + dec,
                                raPM,
                                decPM,
                                "" + para,
                                mag,
                                tipo,
                                rf.readString(line, "SPECTRUM_SIMPLE"),
                                radial,
                                flams,
                                addon
                        };

                        //ConsoleReport.stringArrayReport(val);
                        String out = ConsoleReport.formatAsFortran(val, format, true);
                        if (!addon.equals("")) out += " " + addon;
                        output += out + FileIO.getLineSeparator();
                        aux += mag + FileIO.getLineSeparator();
                    }
                }
            }

            // Close file
            dis.close();

            String lines[] = DataSet.toStringArray(output, FileIO.getLineSeparator());
            String mags[] = DataSet.toStringArray(aux, FileIO.getLineSeparator());
            ArrayList<Object> v = DataSet.sortInCrescent(mags, lines, null, null);
            WriteFile.writeAnyExternalFile(outputPath, (String[]) v.get(1));
        } catch (Exception e1) {
            e1.printStackTrace();
            System.out.println("Error at line " + nline);
        }
    }
}
