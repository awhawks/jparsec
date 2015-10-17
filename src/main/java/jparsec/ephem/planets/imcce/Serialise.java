package jparsec.ephem.planets.imcce;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileOutputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import jparsec.xml.JParsecDoubleConverter;

/**
 * Created by carlo on 14.10.15.
 */
public class Serialise {

    private Serialise () {
    }

    public static void main(final String[] args) throws Exception {
        saveAll();
    }

    private static void saveAll() throws Exception {
        serialise(elp_earth_perturb_t.Lat, "elp_earth_perturb_t.Lat.xml");
        serialise(elp_earth_perturb_t.Lon, "elp_earth_perturb_t.Lon.xml");
        serialise(elp_earth_perturb_t.Rad, "elp_earth_perturb_t.Rad.xml");

        serialise(elp_lat_earth_perturb.Lat, "elp_lat_earth_perturb.Lat.xml");

        serialise(elp_lat_sine_0.LatSine0, "elp_lat_sine_0.LatSine0.xml");
        serialise(elp_lat_sine_1.LatSine1, "elp_lat_sine_1.LatSine1.xml");
        serialise(elp_lat_sine_2.LatSine2, "elp_lat_sine_2.LatSine2.xml");

        serialise(elp_lon_earth_perturb.Lon, "elp_lon_earth_perturb.Lon.xml");

        serialise(elp_lon_sine_0.LonSine0, "elp_lon_sine_0.LonSine0.xml");
        serialise(elp_lon_sine_1.LonSine1, "elp_lon_sine_1.LonSine1.xml");
        serialise(elp_lon_sine_2.LonSine2, "elp_lon_sine_2.LonSine2.xml");

        serialise(elp_moon.Lat, "elp_moon.Lat.xml");
        serialise(elp_moon.Lon, "elp_moon.Lon.xml");
        serialise(elp_moon.Rad, "elp_moon.Rad.xml");

        serialise(elp_plan.Lat, "elp_plan.Lat.xml");
        serialise(elp_plan.Lon, "elp_plan.Lon.xml");
        serialise(elp_plan.Rad, "elp_plan.Rad.xml");

        serialise(elp_plan_perturb10_0.Lon, "elp_plan_perturb10_0.Lon.xml");
        serialise(elp_plan_perturb10_1.Lon, "elp_plan_perturb10_1.Lon.xml");
        serialise(elp_plan_perturb10_2.Lon, "elp_plan_perturb10_2.Lon.xml");
        serialise(elp_plan_perturb10_3.Lon, "elp_plan_perturb10_3.Lon.xml");
        serialise(elp_plan_perturb10_4.Lon, "elp_plan_perturb10_4.Lon.xml");
        serialise(elp_plan_perturb10_5.Lon, "elp_plan_perturb10_5.Lon.xml");
        serialise(elp_plan_perturb10_6.Lon, "elp_plan_perturb10_6.Lon.xml");
        serialise(elp_plan_perturb10_7.Lon, "elp_plan_perturb10_7.Lon.xml");
        serialise(elp_plan_perturb10_8.Lon, "elp_plan_perturb10_8.Lon.xml");
        serialise(elp_plan_perturb10_9.Lon, "elp_plan_perturb10_9.Lon.xml");
        serialise(elp_plan_perturb10_10.Lon, "elp_plan_perturb10_10.Lon.xml");
        serialise(elp_plan_perturb10_11.Lon, "elp_plan_perturb10_11.Lon.xml");
        serialise(elp_plan_perturb10_12.Lon, "elp_plan_perturb10_12.Lon.xml");
        serialise(elp_plan_perturb10_13.Lon, "elp_plan_perturb10_13.Lon.xml");
        serialise(elp_plan_perturb10_14.Lon, "elp_plan_perturb10_14.Lon.xml");
        serialise(elp_plan_perturb10_15.Lon, "elp_plan_perturb10_15.Lon.xml");
        serialise(elp_plan_perturb10_16.Lon, "elp_plan_perturb10_16.Lon.xml");
        serialise(elp_plan_perturb10_17.Lon, "elp_plan_perturb10_17.Lon.xml");
        serialise(elp_plan_perturb10_18.Lon, "elp_plan_perturb10_18.Lon.xml");
        serialise(elp_plan_perturb10_19.Lon, "elp_plan_perturb10_19.Lon.xml");
        serialise(elp_plan_perturb10_20.Lon, "elp_plan_perturb10_20.Lon.xml");
        serialise(elp_plan_perturb10_21.Lon, "elp_plan_perturb10_21.Lon.xml");
        serialise(elp_plan_perturb10_22.Lon, "elp_plan_perturb10_22.Lon.xml");
        serialise(elp_plan_perturb10_23.Lon, "elp_plan_perturb10_23.Lon.xml");
        serialise(elp_plan_perturb10_24.Lon, "elp_plan_perturb10_24.Lon.xml");
        serialise(elp_plan_perturb10_25.Lon, "elp_plan_perturb10_25.Lon.xml");
        serialise(elp_plan_perturb10_26.Lon, "elp_plan_perturb10_26.Lon.xml");
        serialise(elp_plan_perturb10_27.Lon, "elp_plan_perturb10_27.Lon.xml");
        serialise(elp_plan_perturb10_28.Lon, "elp_plan_perturb10_28.Lon.xml");
        serialise(elp_plan_perturb10_29.Lon, "elp_plan_perturb10_29.Lon.xml");
        serialise(elp_plan_perturb10_30.Lon, "elp_plan_perturb10_30.Lon.xml");
        serialise(elp_plan_perturb10_31.Lon, "elp_plan_perturb10_31.Lon.xml");
        serialise(elp_plan_perturb10_32.Lon, "elp_plan_perturb10_32.Lon.xml");
        serialise(elp_plan_perturb10_33.Lon, "elp_plan_perturb10_33.Lon.xml");
        serialise(elp_plan_perturb10_34.Lon, "elp_plan_perturb10_34.Lon.xml");
        serialise(elp_plan_perturb10_35.Lon, "elp_plan_perturb10_35.Lon.xml");

        serialise(elp_plan_perturb11_0.Lat, "elp_plan_perturb11_0.Lat.xml");
        serialise(elp_plan_perturb11_1.Lat, "elp_plan_perturb11_1.Lat.xml");
        serialise(elp_plan_perturb11_2.Lat, "elp_plan_perturb11_2.Lat.xml");
        serialise(elp_plan_perturb11_3.Lat, "elp_plan_perturb11_3.Lat.xml");
        serialise(elp_plan_perturb11_4.Lat, "elp_plan_perturb11_4.Lat.xml");
        serialise(elp_plan_perturb11_5.Lat, "elp_plan_perturb11_5.Lat.xml");
        serialise(elp_plan_perturb11_6.Lat, "elp_plan_perturb11_6.Lat.xml");
        serialise(elp_plan_perturb11_7.Lat, "elp_plan_perturb11_7.Lat.xml");
        serialise(elp_plan_perturb11_8.Lat, "elp_plan_perturb11_8.Lat.xml");
        serialise(elp_plan_perturb11_9.Lat, "elp_plan_perturb11_9.Lat.xml");
        serialise(elp_plan_perturb11_10.Lat, "elp_plan_perturb11_10.Lat.xml");
        serialise(elp_plan_perturb11_11.Lat, "elp_plan_perturb11_11.Lat.xml");
        serialise(elp_plan_perturb11_12.Lat, "elp_plan_perturb11_12.Lat.xml");
        serialise(elp_plan_perturb11_13.Lat, "elp_plan_perturb11_14.Lat.xml");

        serialise(elp_plan_perturb12_0.Rad, "elp_plan_perturb12_0.Rad.xml");
        serialise(elp_plan_perturb12_1.Rad, "elp_plan_perturb12_1.Rad.xml");
        serialise(elp_plan_perturb12_2.Rad, "elp_plan_perturb12_2.Rad.xml");
        serialise(elp_plan_perturb12_3.Rad, "elp_plan_perturb12_3.Rad.xml");
        serialise(elp_plan_perturb12_4.Rad, "elp_plan_perturb12_4.Rad.xml");
        serialise(elp_plan_perturb12_5.Rad, "elp_plan_perturb12_5.Rad.xml");
        serialise(elp_plan_perturb12_6.Rad, "elp_plan_perturb12_6.Rad.xml");
        serialise(elp_plan_perturb12_7.Rad, "elp_plan_perturb12_7.Rad.xml");
        serialise(elp_plan_perturb12_8.Rad, "elp_plan_perturb12_8.Rad.xml");
        serialise(elp_plan_perturb12_9.Rad, "elp_plan_perturb12_9.Rad.xml");
        serialise(elp_plan_perturb12_10.Rad, "elp_plan_perturb12_10.Rad.xml");
        serialise(elp_plan_perturb12_11.Rad, "elp_plan_perturb12_11.Rad.xml");
        serialise(elp_plan_perturb12_12.Rad, "elp_plan_perturb12_12.Rad.xml");
        serialise(elp_plan_perturb12_13.Rad, "elp_plan_perturb12_13.Rad.xml");
        serialise(elp_plan_perturb12_14.Rad, "elp_plan_perturb12_14.Rad.xml");
        serialise(elp_plan_perturb12_15.Rad, "elp_plan_perturb12_15.Rad.xml");
        serialise(elp_plan_perturb12_16.Rad, "elp_plan_perturb12_16.Rad.xml");

        serialise(elp_plan_perturb13_0.Lon, "elp_plan_perturb13_0.Lon.xml");
        serialise(elp_plan_perturb13_1.Lon, "elp_plan_perturb13_1.Lon.xml");
        serialise(elp_plan_perturb13_2.Lon, "elp_plan_perturb13_2.Lon.xml");
        serialise(elp_plan_perturb13_3.Lon, "elp_plan_perturb13_3.Lon.xml");
        serialise(elp_plan_perturb13_4.Lon, "elp_plan_perturb13_4.Lon.xml");
        serialise(elp_plan_perturb13_5.Lon, "elp_plan_perturb13_5.Lon.xml");
        serialise(elp_plan_perturb13_6.Lon, "elp_plan_perturb13_6.Lon.xml");
        serialise(elp_plan_perturb13_7.Lon, "elp_plan_perturb13_7.Lon.xml");
        serialise(elp_plan_perturb13_8.Lon, "elp_plan_perturb13_8.Lon.xml");
        serialise(elp_plan_perturb13_9.Lon, "elp_plan_perturb13_9.Lon.xml");
        serialise(elp_plan_perturb13_10.Lon, "elp_plan_perturb13_10.Lon.xml");

        serialise(elp_plan_perturb14_0.Lat, "elp_plan_perturb14_0.Lat.xml");
        serialise(elp_plan_perturb14_1.Lat, "elp_plan_perturb14_1.Lat.xml");
        serialise(elp_plan_perturb14_2.Lat, "elp_plan_perturb14_2.Lat.xml");

        serialise(elp_plan_perturb15_0.Rad, "elp_plan_perturb15_0.Rad.xml");
        serialise(elp_plan_perturb15_1.Rad, "elp_plan_perturb15_1.Rad.xml");
        serialise(elp_plan_perturb15_2.Rad, "elp_plan_perturb15_2.Rad.xml");
        serialise(elp_plan_perturb15_3.Rad, "elp_plan_perturb15_3.Rad.xml");
        serialise(elp_plan_perturb15_4.Rad, "elp_plan_perturb15_4.Rad.xml");

        serialise(elp_plan_perturb2.Lat, "elp_plan_perturb2.Lat.xml");
        serialise(elp_plan_perturb2.Lat_t, "elp_plan_perturb2.Lat_t.xml");

        serialise(elp_plan_perturb2.Lon, "elp_plan_perturb2.Lon.xml");
        serialise(elp_plan_perturb2.Lon_t, "elp_plan_perturb2.Lon_t.xml");

        serialise(elp_plan_perturb2.Rad, "elp_plan_perturb2.Rad.xml");
        serialise(elp_plan_perturb2.Rad_t, "elp_plan_perturb2.Rad_t.xml");

        serialise(elp_rad_cose_0.RadCose0, "elp_rad_cose_0.RadCose0.xml");
        serialise(elp_rad_cose_1.RadCose1, "elp_rad_cose_1.RadCose1.xml");

        serialise(elp_rad_earth_perturb.Rad, "elp_rad_earth_perturb.Rad.xml");

        serialise(elp_rel.Lat, "elp_rel.Lat.xml");
        serialise(elp_rel.Lon, "elp_rel.Lon.xml");
        serialise(elp_rel.Rad, "elp_rel.Rad.xml");

        serialise(elp_tidal.Lat, "elp_tidal.Lat.xml");
        serialise(elp_tidal.Lon, "elp_tidal.Lon.xml");
        serialise(elp_tidal.Rad, "elp_tidal.Rad.xml");
    }

    private static void serialise (final Object o, final String fileName) throws Exception {
        //Path dir = Paths.get("target/resources/jparsec/ephem/planets/imcce");
        File dir = new File("target/resources/jparsec/ephem/planets/imcce");

        //Files.createDirectories(dir);
        dir.mkdirs();

        //Path file = dir.resolve(fileName);
        File file = new File(dir, fileName);
        //Files.createFile(file);
        file.createNewFile();

        //FileOutputStream fos = new FileOutputStream(file.toFile());
        FileOutputStream fos = new FileOutputStream(file);

        XStream xs = new XStream();
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
        xs.toXML(o, fos);

        fos.close();
    }

    public static Object deserialise (final String fileName) {
        XStream xs = new XStream();
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

        /*
        //Path dir = Paths.get("jparsec/ephem/planets/imcce");
        File dir = new File("jparsec/ephem/planets/imcce");
        //Path file = dir.resolve(fileName);
        File file = new File(dir, fileName);

        //return xs.fromXML(file.toFile());
        return xs.fromXML(file);
        */
        Serialise ser = new Serialise();
        InputStream stream = ser.getClass().getResourceAsStream("jparsec/ephem/planets/imcce/" + fileName);
        Object result = xs.fromXML(stream);
        try {
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close input stream for " + fileName);
        }

        return result;
    }
}
