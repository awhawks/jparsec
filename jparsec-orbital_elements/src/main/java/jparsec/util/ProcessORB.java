package jparsec.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ProcessORB {
    /**
     * The url used to update orbital elements of comets.
     */
    public static final String URL_COMETS = "http://www.minorplanetcenter.net/iau/Ephemerides/Comets/Soft00Cmt.txt";
    /**
     * The url used to update orbital elements of trans-Neptunian objects.
     */
    public static final String URL_DISTANT_BODIES = "http://www.minorplanetcenter.net/iau/Ephemerides/Distant/Soft00Distant.txt";
    /**
     * The url used to update orbital elements of bright asteroids.
     */
    public static final String URL_BRIGHT_ASTEROIDS = "http://www.minorplanetcenter.net/iau/Ephemerides/Bright/2007/Soft00Bright.txt";
    /**
     * The url used to update orbital elements of artificial satellites.
     */
    public static final String URL_VISUAL_ARTIFICIAL_SATELLITES = "http://www.celestrak.com/NORAD/elements/visual.txt";
    /**
     * The url used to update orbital elements of iridium artificial satellites.
     */
    public static final String URL_VISUAL_ARTIFICIAL_SATELLITES_IRIDIUM = "http://www.celestrak.com/NORAD/elements/iridium.txt";
    /**
     * The url used to update orbital elements of visual binary stars.
     */
    //public static final String UPDATE_URL_ORBITS_VISUAL_BINARY_STARS = "http://ad.usno.navy.mil/wds/orb6/orb6orbits.txt";
    public static final String URL_ORBITS_VISUAL_BINARY_STARS = "http://www.stsci.edu/~mperrin/software/gpidata/downloads/config/orb6orbits.txt";
    /**
     * The url used to update the official list of observatories.
     */
    public static final String URL_OBSERVATORIES = "http://www.minorplanetcenter.net/iau/lists/ObsCodes.html";
    /**
     * The url used to update sizes and magnitudes or artificial satellites.
     */
    public static final String URL_ARTIFICIAL_SATELLITES_SIZE_AND_MAGNITUDE = "https://www.prismnet.com/~mmccants/programs/qsmag.zip";
    /**
     * The url used to update orbital elements of natural satellites.
     */
    public static final String URL_NATURAL_SATELLITES = "http://ssd.jpl.nasa.gov/?sat_elem";


    /**
     * Updates the orbital elements .jar file by querying Minor Planet
     * Center for updated files for comets, and bright and distant
     * asteroids. Only MPC-style formatted files are updated. This method
     * also updates the list of observatories and the orbital elements
     * of artificial satellites.
     *
     * @throws Exception If an exception occurs.
     */
    public static void main(String[] args) throws Exception {
        Path targetDir = Paths.get(args[0]);
        Files.createDirectories(targetDir);

        CloseableHttpClient hc = HttpClients.createDefault();

        retrieveFileFromURL(hc, targetDir, URL_COMETS, "MPC_comets.txt");
        retrieveFileFromURL(hc, targetDir, URL_DISTANT_BODIES, "MPC_distant_bodies.txt");
        retrieveFileFromURL(hc, targetDir, URL_VISUAL_ARTIFICIAL_SATELLITES, "ArtificialSatellites.txt");
        retrieveFileFromURL(hc, targetDir, URL_VISUAL_ARTIFICIAL_SATELLITES_IRIDIUM, "iridium.txt");
        retrieveFileFromURL(hc, targetDir, URL_ORBITS_VISUAL_BINARY_STARS, "orb6orbits.txt");
        //retrieveFileFromURL(hc, targetDir, "http://maia.usno.navy.mil/ser7/tai-utc.dat", "tai-utc.dat");
        //retrieveFileFromURL(hc, targetDir, "http://maia.usno.navy.mil/ser7/leapsec.dat", "leapSeconds.txt");

        retrieveBrightAsteroids(hc, targetDir, "MPC_asteroids_bright.txt");
        retrieveObservatories(hc, targetDir, URL_OBSERVATORIES, "MPC_observatories.txt");
        retrieveSatMag(hc, targetDir, URL_ARTIFICIAL_SATELLITES_SIZE_AND_MAGNITUDE, "sat_mag.txt");
    }

    private static void retrieveBrightAsteroids(CloseableHttpClient hc, Path targetDir, String fileName) throws Exception {
        String sep = System.getProperty("line.separator");
        int year = Calendar.getInstance().get(Calendar.YEAR);

        HttpGet get = new HttpGet(URL_BRIGHT_ASTEROIDS.replace("2007", Integer.toString(year - 1)));
        CloseableHttpResponse response = hc.execute(get);
        HttpEntity entity = response.getEntity();
        String contentPreviousYear = EntityUtils.toString(entity);
        EntityUtils.consume(entity);

        get = new HttpGet(URL_BRIGHT_ASTEROIDS.replace("2007", Integer.toString(year)));
        response = hc.execute(get);
        entity = response.getEntity();
        String contentThisYear = EntityUtils.toString(entity);
        EntityUtils.consume(entity);

        String d1[] = contentThisYear.split(sep);
        String d2[] = contentPreviousYear.split(sep);
        Set<String> set = new HashSet<String>();
        //String n1[] = new String[d1.length], n2[] = new String[d2.length];
        StringBuffer buf = new StringBuffer(contentThisYear);

        for (String d : d1) {
            set.add(d.substring(177));
        }

        for (String d : d2) {
            String asteroid = d.substring(177);

            if (!set.contains(asteroid)) {
                buf.append(d);
                buf.append(sep);
            }
        }

        Path outFile = targetDir.resolve(fileName);
        BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(buf.toString().getBytes()));
        Files.copy(bis, outFile, StandardCopyOption.REPLACE_EXISTING);
        setLastModificationTime(response, outFile);
    }

    private static void retrieveObservatories(CloseableHttpClient hc, Path dir, String url, String fileName) throws Exception {
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = hc.execute(get);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
        EntityUtils.consume(entity);

        Path outFile = dir.resolve(fileName);
        int preStart = content.indexOf("<pre>") + 6;
        int preEnd = content.indexOf("</pre>") - 1;
        String cont = content.substring(preStart, preEnd);

        BufferedInputStream bis2 = new BufferedInputStream(new ByteArrayInputStream(cont.getBytes()));
        Files.copy(bis2, outFile, StandardCopyOption.REPLACE_EXISTING);
    }

    private static void retrieveSatMag(CloseableHttpClient hc, Path dir, String url, String fileName) throws Exception {
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = hc.execute(get);
        HttpEntity entity = response.getEntity();

        BufferedInputStream bis = new BufferedInputStream(entity.getContent());
        ZipInputStream zis = new ZipInputStream(bis);
        ZipEntry entry = zis.getNextEntry();
        long length = entity.getContentLength();
        Path outFile = dir.resolve(fileName);

        if (null == entry) {
            EntityUtils.consume(entity);
            return;
        }

        FileTime time = entry.getLastAccessTime();

        if (Files.notExists(outFile) || length != Files.size(outFile)) {
            BufferedInputStream bis2 = new BufferedInputStream(zis);
            Files.copy(bis2, outFile, StandardCopyOption.REPLACE_EXISTING);
        }

        zis.close();
        EntityUtils.consume(entity);
        Files.setLastModifiedTime(outFile, time);
    }

    private static void retrieveFileFromURL(CloseableHttpClient hc, Path path, String url, String catalogName) throws Exception {
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = hc.execute(get);
        HttpEntity entity = response.getEntity();
        long length = entity.getContentLength();
        Path outFile = path.resolve(catalogName);

        if (Files.notExists(outFile) || length != Files.size(outFile)) {
            BufferedInputStream bis = new BufferedInputStream(entity.getContent());
            Files.copy(bis, outFile, StandardCopyOption.REPLACE_EXISTING);
        }

        EntityUtils.consume(entity);

        setLastModificationTime(response, outFile);
    }

    private static void setLastModificationTime (CloseableHttpResponse response, Path outFile) throws Exception {
        Header header = response.getFirstHeader("Last-Modified");
        String lastModified = header.getValue();
        Date date = DateUtils.parseDate(lastModified);
        FileTime time = FileTime.from(date.toInstant());
        Files.setLastModifiedTime(outFile, time);
    }
}
