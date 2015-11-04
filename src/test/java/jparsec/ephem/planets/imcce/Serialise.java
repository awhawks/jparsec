package jparsec.ephem.planets.imcce;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;

public class Serialise {
    private static Elp2000_data data = new Elp2000_data();

    private Serialise () {
    }

    public static void main(final String[] args) throws Exception {
        long t0, t1;

        t0 = System.currentTimeMillis();
        data = (Elp2000_data) deserialiseObject("elp2000_data.ser.gz");
        //serialiseObject(data, "elp2000_data.ser.gz");
        t1 = System.currentTimeMillis();
        System.out.println("elp2000_data " + (t1 - t0));
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

    private static Object deserialiseObject (final String fileName) throws Exception {
        InputStream fis = Serialise.class.getClassLoader().getResourceAsStream("jparsec/ephem/planets/imcce/" + fileName);
        //FileInputStream fis = new FileInputStream(file);
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