package jparsec.ephem.planets.imcce;

import com.thoughtworks.xstream.XStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import jparsec.xml.JParsecDoubleConverter;

//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;

public class Serialise {
    private final static XStream xs;

    static {
        xs = new XStream();
        xs.registerConverter(new JParsecDoubleConverter(), XStream.PRIORITY_VERY_HIGH);
        xs.alias("s1", Elp2000Set1.class);
        xs.alias("s2", Elp2000Set2.class);
        xs.alias("s3", Elp2000Set3.class);
        xs.aliasField("I", Elp2000Set1.class, "ILU");
        xs.aliasField("C", Elp2000Set1.class, "COEF");
        xs.aliasField("I", Elp2000Set2.class, "ILU");
        xs.aliasField("C", Elp2000Set2.class, "COEF");
        xs.aliasField("I", Elp2000Set3.class, "ILU");
        xs.aliasField("C", Elp2000Set3.class, "COEF");
        xs.alias("i", int.class);
        xs.alias("d", double.class);
    }

    private final static Elp2000_data1 data1 = new Elp2000_data1();
    private final static Elp2000_data2 data2 = new Elp2000_data2();

    private Serialise () {
    }

    public static void main(final String[] args) throws Exception {
        long t0, t1, t2;

        //t0 = System.currentTimeMillis();
        //saveAll();
        //t1 = System.currentTimeMillis();
        //System.out.println("saveAll " + (t1 - t0));

        //serialiseXML(data1.elp_tidal_Lat_t, "elp_tidal.Lat_t.xml");
        //serialiseXML(data1.elp_tidal_Lon_t, "elp_tidal.Lon_t.xml");
        //serialiseXML(data1.elp_tidal_Rad_t, "elp_tidal.Rad_t.xml");

        t0 = System.currentTimeMillis();
        populateData1();
        t1 = System.currentTimeMillis();
        populateData2();
        t2 = System.currentTimeMillis();
        System.out.println("data1 " + (t1 - t0) + ", data2 " + (t2 - t1));

        t0 = System.currentTimeMillis();
        serialiseObject(data1, "elp2000_data1.ser.gz");
        //Elp2000_data1 newData1 = (Elp2000_data1) deserialiseObject("elp2000_data1.ser.gz");
        t1 = System.currentTimeMillis();
        serialiseObject(data2, "elp2000_data2.ser.gz");
        //Elp2000_data2 newData2 = (Elp2000_data2) deserialiseObject("elp2000_data2.ser.gz");
        t2 = System.currentTimeMillis();
        System.out.println("data1 " + (t1 - t0) + ", data2 " + (t2 - t1));
    }

    private static void populateData1() throws Exception {
        data1.elp_earth_perturb_t_Lat = (Elp2000Set2[]) deserialiseXML("elp_earth_perturb_t.Lat.xml");
        data1.elp_earth_perturb_t_Lon = (Elp2000Set2[]) deserialiseXML("elp_earth_perturb_t.Lon.xml");
        data1.elp_earth_perturb_t_Rad = (Elp2000Set2[]) deserialiseXML("elp_earth_perturb_t.Rad.xml");
        data1.elp_lat_earth_perturb_Lat = (Elp2000Set2[]) deserialiseXML("elp_lat_earth_perturb.Lat.xml");
        data1.elp_lat_sine_0_LatSine0 = (Elp2000Set1[]) deserialiseXML("elp_lat_sine_0.LatSine0.xml");
        data1.elp_lat_sine_1_LatSine1 = (Elp2000Set1[]) deserialiseXML("elp_lat_sine_1.LatSine1.xml");
        data1.elp_lat_sine_2_LatSine2 = (Elp2000Set1[]) deserialiseXML("elp_lat_sine_2.LatSine2.xml");
        data1.elp_lon_earth_perturb_Lon = (Elp2000Set2[]) deserialiseXML("elp_lon_earth_perturb.Lon.xml");
        data1.elp_lon_sine_0_LonSine0 = (Elp2000Set1[]) deserialiseXML("elp_lon_sine_0.LonSine0.xml");
        data1.elp_lon_sine_1_LonSine1 = (Elp2000Set1[]) deserialiseXML("elp_lon_sine_1.LonSine1.xml");
        data1.elp_lon_sine_2_LonSine2 = (Elp2000Set1[]) deserialiseXML("elp_lon_sine_2.LonSine2.xml");
        data1.elp_moon_Lat = (Elp2000Set2[]) deserialiseXML("elp_moon.Lat.xml");
        data1.elp_moon_Lon = (Elp2000Set2[]) deserialiseXML("elp_moon.Lon.xml");
        data1.elp_moon_Rad = (Elp2000Set2[]) deserialiseXML("elp_moon.Rad.xml");
        data1.elp_plan_Lat = (Elp2000Set2[]) deserialiseXML("elp_plan.Lat.xml");
        data1.elp_plan_Lon = (Elp2000Set2[]) deserialiseXML("elp_plan.Lon.xml");
        data1.elp_plan_Rad = (Elp2000Set2[]) deserialiseXML("elp_plan.Rad.xml");
        data1.elp_plan_perturb10_0_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_0.Lon.xml");
        data1.elp_plan_perturb10_1_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_1.Lon.xml");
        data1.elp_plan_perturb10_2_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_2.Lon.xml");
        data1.elp_plan_perturb10_3_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_3.Lon.xml");
        data1.elp_plan_perturb10_4_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_4.Lon.xml");
        data1.elp_plan_perturb10_5_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_5.Lon.xml");
        data1.elp_plan_perturb10_6_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_6.Lon.xml");
        data1.elp_plan_perturb10_7_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_7.Lon.xml");
        data1.elp_plan_perturb10_8_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_8.Lon.xml");
        data1.elp_plan_perturb10_9_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_9.Lon.xml");
        data1.elp_plan_perturb10_10_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_10.Lon.xml");
        data1.elp_plan_perturb10_11_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_11.Lon.xml");
        data1.elp_plan_perturb10_12_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_12.Lon.xml");
        data1.elp_plan_perturb10_13_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_13.Lon.xml");
        data1.elp_plan_perturb10_14_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_14.Lon.xml");
        data1.elp_plan_perturb10_15_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_15.Lon.xml");
        data1.elp_plan_perturb10_16_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_16.Lon.xml");
        data1.elp_plan_perturb10_17_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_17.Lon.xml");
        data1.elp_plan_perturb10_18_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_18.Lon.xml");
        data1.elp_plan_perturb10_19_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_19.Lon.xml");
        data1.elp_plan_perturb10_20_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_20.Lon.xml");
        data1.elp_plan_perturb10_21_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_21.Lon.xml");
        data1.elp_plan_perturb10_22_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_22.Lon.xml");
        data1.elp_plan_perturb10_23_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_23.Lon.xml");
        data1.elp_plan_perturb10_24_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_24.Lon.xml");
        data1.elp_plan_perturb10_25_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_25.Lon.xml");
        data1.elp_plan_perturb10_26_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_26.Lon.xml");
        data1.elp_plan_perturb10_27_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_27.Lon.xml");
        data1.elp_plan_perturb10_28_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_28.Lon.xml");
        data1.elp_plan_perturb10_29_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_29.Lon.xml");
        data1.elp_plan_perturb10_30_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_30.Lon.xml");
        data1.elp_plan_perturb10_31_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_31.Lon.xml");
        data1.elp_plan_perturb10_32_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_32.Lon.xml");
        data1.elp_plan_perturb10_33_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_33.Lon.xml");
        data1.elp_plan_perturb10_34_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_34.Lon.xml");
        data1.elp_plan_perturb10_35_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb10_35.Lon.xml");

        data1.elp_plan_perturb2_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb2.Lat.xml");
        data1.elp_plan_perturb2_Lat_t = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb2.Lat_t.xml");
        data1.elp_plan_perturb2_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb2.Lon.xml");
        data1.elp_plan_perturb2_Lon_t = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb2.Lon_t.xml");
        data1.elp_plan_perturb2_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb2.Rad.xml");
        data1.elp_plan_perturb2_Rad_t = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb2.Rad_t.xml");
        data1.elp_rad_cose_0_RadCose0 = (Elp2000Set1[]) deserialiseXML("elp_rad_cose_0.RadCose0.xml");
        data1.elp_rad_cose_1_RadCose1 = (Elp2000Set1[]) deserialiseXML("elp_rad_cose_1.RadCose1.xml");
        data1.elp_rad_earth_perturb_Rad = (Elp2000Set2[]) deserialiseXML("elp_rad_earth_perturb.Rad.xml");
        data1.elp_rel_Lat = (Elp2000Set2[]) deserialiseXML("elp_rel.Lat.xml");
        data1.elp_rel_Lon = (Elp2000Set2[]) deserialiseXML("elp_rel.Lon.xml");
        data1.elp_rel_Rad = (Elp2000Set2[]) deserialiseXML("elp_rel.Rad.xml");
        data1.elp_tidal_Lat = (Elp2000Set2[]) deserialiseXML("elp_tidal.Lat.xml");
        data1.elp_tidal_Lon = (Elp2000Set2[]) deserialiseXML("elp_tidal.Lon.xml");
        data1.elp_tidal_Rad = (Elp2000Set2[]) deserialiseXML("elp_tidal.Rad.xml");
        data1.elp_tidal_Lat_t = (Elp2000Set2[]) deserialiseXML("elp_tidal.Lat_t.xml");
        data1.elp_tidal_Lon_t = (Elp2000Set2[]) deserialiseXML("elp_tidal.Lon_t.xml");
        data1.elp_tidal_Rad_t = (Elp2000Set2[]) deserialiseXML("elp_tidal.Rad_t.xml");
    }

    private static void populateData2() throws Exception {
        data2.elp_plan_perturb11_0_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_0.Lat.xml");
        data2.elp_plan_perturb11_1_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_1.Lat.xml");
        data2.elp_plan_perturb11_2_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_2.Lat.xml");
        data2.elp_plan_perturb11_3_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_3.Lat.xml");
        data2.elp_plan_perturb11_4_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_4.Lat.xml");
        data2.elp_plan_perturb11_5_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_5.Lat.xml");
        data2.elp_plan_perturb11_6_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_6.Lat.xml");
        data2.elp_plan_perturb11_7_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_7.Lat.xml");
        data2.elp_plan_perturb11_8_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_8.Lat.xml");
        data2.elp_plan_perturb11_9_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_9.Lat.xml");
        data2.elp_plan_perturb11_10_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_10.Lat.xml");
        data2.elp_plan_perturb11_11_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_11.Lat.xml");
        data2.elp_plan_perturb11_12_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_12.Lat.xml");
        data2.elp_plan_perturb11_13_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb11_14.Lat.xml");
        data2.elp_plan_perturb12_0_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_0.Rad.xml");
        data2.elp_plan_perturb12_1_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_1.Rad.xml");
        data2.elp_plan_perturb12_2_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_2.Rad.xml");
        data2.elp_plan_perturb12_3_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_3.Rad.xml");
        data2.elp_plan_perturb12_4_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_4.Rad.xml");
        data2.elp_plan_perturb12_5_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_5.Rad.xml");
        data2.elp_plan_perturb12_6_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_6.Rad.xml");
        data2.elp_plan_perturb12_7_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_7.Rad.xml");
        data2.elp_plan_perturb12_8_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_8.Rad.xml");
        data2.elp_plan_perturb12_9_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_9.Rad.xml");
        data2.elp_plan_perturb12_10_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_10.Rad.xml");
        data2.elp_plan_perturb12_11_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_11.Rad.xml");
        data2.elp_plan_perturb12_12_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_12.Rad.xml");
        data2.elp_plan_perturb12_13_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_13.Rad.xml");
        data2.elp_plan_perturb12_14_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_14.Rad.xml");
        data2.elp_plan_perturb12_15_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_15.Rad.xml");
        data2.elp_plan_perturb12_16_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb12_16.Rad.xml");
        data2.elp_plan_perturb13_0_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb13_0.Lon.xml");
        data2.elp_plan_perturb13_1_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb13_1.Lon.xml");
        data2.elp_plan_perturb13_2_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb13_2.Lon.xml");
        data2.elp_plan_perturb13_3_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb13_3.Lon.xml");
        data2.elp_plan_perturb13_4_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb13_4.Lon.xml");
        data2.elp_plan_perturb13_5_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb13_5.Lon.xml");
        data2.elp_plan_perturb13_6_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb13_6.Lon.xml");
        data2.elp_plan_perturb13_7_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb13_7.Lon.xml");
        data2.elp_plan_perturb13_8_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb13_8.Lon.xml");
        data2.elp_plan_perturb13_9_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb13_9.Lon.xml");
        data2.elp_plan_perturb13_10_Lon = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb13_10.Lon.xml");
        data2.elp_plan_perturb14_0_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb14_0.Lat.xml");
        data2.elp_plan_perturb14_1_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb14_1.Lat.xml");
        data2.elp_plan_perturb14_2_Lat = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb14_2.Lat.xml");
        data2.elp_plan_perturb15_0_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb15_0.Rad.xml");
        data2.elp_plan_perturb15_1_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb15_1.Rad.xml");
        data2.elp_plan_perturb15_2_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb15_2.Rad.xml");
        data2.elp_plan_perturb15_3_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb15_3.Rad.xml");
        data2.elp_plan_perturb15_4_Rad = (Elp2000Set3[]) deserialiseXML("elp_plan_perturb15_4.Rad.xml");
    }

    private static void saveAll() throws Exception {
        serialiseXML(data1.elp_earth_perturb_t_Lat, "elp_earth_perturb_t.Lat.xml");
        serialiseXML(data1.elp_earth_perturb_t_Lon, "elp_earth_perturb_t.Lon.xml");
        serialiseXML(data1.elp_earth_perturb_t_Rad, "elp_earth_perturb_t.Rad.xml");
        serialiseXML(data1.elp_lat_earth_perturb_Lat, "elp_lat_earth_perturb.Lat.xml");
        serialiseXML(data1.elp_lat_sine_0_LatSine0, "elp_lat_sine_0.LatSine0.xml");
        serialiseXML(data1.elp_lat_sine_1_LatSine1, "elp_lat_sine_1.LatSine1.xml");
        serialiseXML(data1.elp_lat_sine_2_LatSine2, "elp_lat_sine_2.LatSine2.xml");
        serialiseXML(data1.elp_lon_earth_perturb_Lon, "elp_lon_earth_perturb.Lon.xml");
        serialiseXML(data1.elp_lon_sine_0_LonSine0, "elp_lon_sine_0.LonSine0.xml");
        serialiseXML(data1.elp_lon_sine_1_LonSine1, "elp_lon_sine_1.LonSine1.xml");
        serialiseXML(data1.elp_lon_sine_2_LonSine2, "elp_lon_sine_2.LonSine2.xml");
        serialiseXML(data1.elp_moon_Lat, "elp_moon.Lat.xml");
        serialiseXML(data1.elp_moon_Lon, "elp_moon.Lon.xml");
        serialiseXML(data1.elp_moon_Rad, "elp_moon.Rad.xml");
        serialiseXML(data1.elp_plan_Lat, "elp_plan.Lat.xml");
        serialiseXML(data1.elp_plan_Lon, "elp_plan.Lon.xml");
        serialiseXML(data1.elp_plan_Rad, "elp_plan.Rad.xml");
        serialiseXML(data1.elp_plan_perturb10_0_Lon, "elp_plan_perturb10_0.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_1_Lon, "elp_plan_perturb10_1.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_2_Lon, "elp_plan_perturb10_2.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_3_Lon, "elp_plan_perturb10_3.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_4_Lon, "elp_plan_perturb10_4.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_5_Lon, "elp_plan_perturb10_5.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_6_Lon, "elp_plan_perturb10_6.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_7_Lon, "elp_plan_perturb10_7.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_8_Lon, "elp_plan_perturb10_8.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_9_Lon, "elp_plan_perturb10_9.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_10_Lon, "elp_plan_perturb10_10.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_11_Lon, "elp_plan_perturb10_11.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_12_Lon, "elp_plan_perturb10_12.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_13_Lon, "elp_plan_perturb10_13.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_14_Lon, "elp_plan_perturb10_14.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_15_Lon, "elp_plan_perturb10_15.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_16_Lon, "elp_plan_perturb10_16.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_17_Lon, "elp_plan_perturb10_17.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_18_Lon, "elp_plan_perturb10_18.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_19_Lon, "elp_plan_perturb10_19.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_20_Lon, "elp_plan_perturb10_20.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_21_Lon, "elp_plan_perturb10_21.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_22_Lon, "elp_plan_perturb10_22.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_23_Lon, "elp_plan_perturb10_23.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_24_Lon, "elp_plan_perturb10_24.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_25_Lon, "elp_plan_perturb10_25.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_26_Lon, "elp_plan_perturb10_26.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_27_Lon, "elp_plan_perturb10_27.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_28_Lon, "elp_plan_perturb10_28.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_29_Lon, "elp_plan_perturb10_29.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_30_Lon, "elp_plan_perturb10_30.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_31_Lon, "elp_plan_perturb10_31.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_32_Lon, "elp_plan_perturb10_32.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_33_Lon, "elp_plan_perturb10_33.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_34_Lon, "elp_plan_perturb10_34.Lon.xml");
        serialiseXML(data1.elp_plan_perturb10_35_Lon, "elp_plan_perturb10_35.Lon.xml");
        serialiseXML(data1.elp_plan_perturb2_Lat, "elp_plan_perturb2.Lat.xml");
        serialiseXML(data1.elp_plan_perturb2_Lat_t, "elp_plan_perturb2.Lat_t.xml");
        serialiseXML(data1.elp_plan_perturb2_Lon, "elp_plan_perturb2.Lon.xml");
        serialiseXML(data1.elp_plan_perturb2_Lon_t, "elp_plan_perturb2.Lon_t.xml");
        serialiseXML(data1.elp_plan_perturb2_Rad, "elp_plan_perturb2.Rad.xml");
        serialiseXML(data1.elp_plan_perturb2_Rad_t, "elp_plan_perturb2.Rad_t.xml");
        serialiseXML(data1.elp_rad_cose_0_RadCose0, "elp_rad_cose_0.RadCose0.xml");
        serialiseXML(data1.elp_rad_cose_1_RadCose1, "elp_rad_cose_1.RadCose1.xml");
        serialiseXML(data1.elp_rad_earth_perturb_Rad, "elp_rad_earth_perturb.Rad.xml");
        serialiseXML(data1.elp_rel_Lat, "elp_rel.Lat.xml");
        serialiseXML(data1.elp_rel_Lon, "elp_rel.Lon.xml");
        serialiseXML(data1.elp_rel_Rad, "elp_rel.Rad.xml");
        serialiseXML(data1.elp_tidal_Lat, "elp_tidal.Lat.xml");
        serialiseXML(data1.elp_tidal_Lon, "elp_tidal.Lon.xml");
        serialiseXML(data1.elp_tidal_Rad, "elp_tidal.Rad.xml");
        serialiseXML(data1.elp_tidal_Lat_t, "elp_tidal.Lat_t.xml");
        serialiseXML(data1.elp_tidal_Lon_t, "elp_tidal.Lon_t.xml");
        serialiseXML(data1.elp_tidal_Rad_t, "elp_tidal.Rad_t.xml");

        serialiseXML(data2.elp_plan_perturb11_0_Lat, "elp_plan_perturb11_0.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_1_Lat, "elp_plan_perturb11_1.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_2_Lat, "elp_plan_perturb11_2.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_3_Lat, "elp_plan_perturb11_3.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_4_Lat, "elp_plan_perturb11_4.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_5_Lat, "elp_plan_perturb11_5.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_6_Lat, "elp_plan_perturb11_6.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_7_Lat, "elp_plan_perturb11_7.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_8_Lat, "elp_plan_perturb11_8.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_9_Lat, "elp_plan_perturb11_9.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_10_Lat, "elp_plan_perturb11_10.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_11_Lat, "elp_plan_perturb11_11.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_12_Lat, "elp_plan_perturb11_12.Lat.xml");
        serialiseXML(data2.elp_plan_perturb11_13_Lat, "elp_plan_perturb11_14.Lat.xml");
        serialiseXML(data2.elp_plan_perturb12_0_Rad, "elp_plan_perturb12_0.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_1_Rad, "elp_plan_perturb12_1.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_2_Rad, "elp_plan_perturb12_2.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_3_Rad, "elp_plan_perturb12_3.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_4_Rad, "elp_plan_perturb12_4.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_5_Rad, "elp_plan_perturb12_5.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_6_Rad, "elp_plan_perturb12_6.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_7_Rad, "elp_plan_perturb12_7.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_8_Rad, "elp_plan_perturb12_8.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_9_Rad, "elp_plan_perturb12_9.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_10_Rad, "elp_plan_perturb12_10.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_11_Rad, "elp_plan_perturb12_11.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_12_Rad, "elp_plan_perturb12_12.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_13_Rad, "elp_plan_perturb12_13.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_14_Rad, "elp_plan_perturb12_14.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_15_Rad, "elp_plan_perturb12_15.Rad.xml");
        serialiseXML(data2.elp_plan_perturb12_16_Rad, "elp_plan_perturb12_16.Rad.xml");
        serialiseXML(data2.elp_plan_perturb13_0_Lon, "elp_plan_perturb13_0.Lon.xml");
        serialiseXML(data2.elp_plan_perturb13_1_Lon, "elp_plan_perturb13_1.Lon.xml");
        serialiseXML(data2.elp_plan_perturb13_2_Lon, "elp_plan_perturb13_2.Lon.xml");
        serialiseXML(data2.elp_plan_perturb13_3_Lon, "elp_plan_perturb13_3.Lon.xml");
        serialiseXML(data2.elp_plan_perturb13_4_Lon, "elp_plan_perturb13_4.Lon.xml");
        serialiseXML(data2.elp_plan_perturb13_5_Lon, "elp_plan_perturb13_5.Lon.xml");
        serialiseXML(data2.elp_plan_perturb13_6_Lon, "elp_plan_perturb13_6.Lon.xml");
        serialiseXML(data2.elp_plan_perturb13_7_Lon, "elp_plan_perturb13_7.Lon.xml");
        serialiseXML(data2.elp_plan_perturb13_8_Lon, "elp_plan_perturb13_8.Lon.xml");
        serialiseXML(data2.elp_plan_perturb13_9_Lon, "elp_plan_perturb13_9.Lon.xml");
        serialiseXML(data2.elp_plan_perturb13_10_Lon, "elp_plan_perturb13_10.Lon.xml");
        serialiseXML(data2.elp_plan_perturb14_0_Lat, "elp_plan_perturb14_0.Lat.xml");
        serialiseXML(data2.elp_plan_perturb14_1_Lat, "elp_plan_perturb14_1.Lat.xml");
        serialiseXML(data2.elp_plan_perturb14_2_Lat, "elp_plan_perturb14_2.Lat.xml");
        serialiseXML(data2.elp_plan_perturb15_0_Rad, "elp_plan_perturb15_0.Rad.xml");
        serialiseXML(data2.elp_plan_perturb15_1_Rad, "elp_plan_perturb15_1.Rad.xml");
        serialiseXML(data2.elp_plan_perturb15_2_Rad, "elp_plan_perturb15_2.Rad.xml");
        serialiseXML(data2.elp_plan_perturb15_3_Rad, "elp_plan_perturb15_3.Rad.xml");
        serialiseXML(data2.elp_plan_perturb15_4_Rad, "elp_plan_perturb15_4.Rad.xml");
    }

    private static void serialiseXML (final Object obj, final String fileName) throws Exception {
        //Path dir = Paths.get("target/resources/jparsec/ephem/planets/imcce");
        File dir = new File("src/main/resources/jparsec/ephem/planets/imcce");

        //Files.createDirectories(dir);
        dir.mkdirs();

        //Path file = dir.resolve(fileName);
        File file = new File(dir, fileName);
        //Files.createFile(file);
        file.createNewFile();

        //FileOutputStream fos = new FileOutputStream(file.toFile());
        FileOutputStream fos = new FileOutputStream(file);
        //BufferedOutputStream bos = new BufferedOutputStream(fos);
        fos.write("<?xml version='1.0' encoding='utf-8'?>\n".getBytes());
        xs.toXML(obj, fos);
        //bos.close();
        fos.close();
    }

    private static void serialiseObject (final Object obj, final String fileName) throws Exception {
        //Path dir = Paths.get("target/resources/jparsec/ephem/planets/imcce");
        File dir = new File("src/main/resources/jparsec/ephem/planets/imcce");

        //Files.createDirectories(dir);
        dir.mkdirs();

        //Path file = dir.resolve(fileName);
        File file = new File(dir, fileName);
        //Files.createFile(file);
        file.createNewFile();

        //FileOutputStream fos = new FileOutputStream(file.toFile());
        FileOutputStream fos = new FileOutputStream(file);
        GZIPOutputStream gos = new GZIPOutputStream(fos, 4096);
        ObjectOutputStream oos = new ObjectOutputStream(gos);
        oos.writeObject(obj);
        oos.close();
        gos.close();
        fos.close();
    }

    public static Object deserialiseXML(final String fileName) throws Exception {
        //Path dir = Paths.get("jparsec/ephem/planets/imcce");
        File dir = new File("src/main/resources/jparsec/ephem/planets/imcce");
        //Path file = dir.resolve(fileName);
        File file = new File(dir, fileName);

        //Serialise.class.getClassLoader().getResourceAsStream("/jparsec/ephem/planets/imcce/" + fileName);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        Object result = xs.fromXML(bis);
        //Object result = xs.fromXML(file.toFile());
        fis.close();

        try {
            bis.close();
            fis.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not close input stream for " + fileName);
        }

        return result;
    }

    private static Object deserialiseObject (final String fileName) throws Exception {
        //Path dir = Paths.get("jparsec/ephem/planets/imcce");
        File dir = new File("src/main/resources/jparsec/ephem/planets/imcce");
        //Path file = dir.resolve(fileName);
        File file = new File(dir, fileName);

        ////return xs.fromXML(file.toFile());
        //return xs.fromXML(file);

        //Serialise.class.getClassLoader().getResourceAsStream("/jparsec/ephem/planets/imcce/" + fileName);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream (fis);
        GZIPInputStream gis = new GZIPInputStream(bis);
        ObjectInputStream ois = new ObjectInputStream(gis);
        Object result = ois.readObject();

        try {
            ois.close();
            gis.close();
            bis.close();
            fis.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not close input stream for " + fileName);
        }

        return result;
    }
}
