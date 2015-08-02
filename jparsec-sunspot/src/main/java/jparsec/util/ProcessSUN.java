package jparsec.util;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ProcessSUN {
    /**
     * The url used to update Sun spots database.
     */
    public static final String URL_SUN_SPOTS = "http://solarscience.msfc.nasa.gov/greenwch/";

    public static void main(String[] args) throws Exception {
        Path targetDir = Paths.get(args[0]);
        Files.createDirectories(targetDir);

        CloseableHttpClient hc = HttpClients.createDefault();

        Calendar cal = new GregorianCalendar();
        int year = cal.get(Calendar.YEAR);

        for (int y = 1874; y <= year; y++) {
            String fileName = "g" + y + ".txt";

            try {
                saveFileFromURL(hc, targetDir, URL_SUN_SPOTS + fileName, fileName);
            } catch (Exception exc) {
                saveFileFromURL(hc, targetDir, URL_SUN_SPOTS + "g" + y + ".TXT", fileName);
            }
        }
    }

    private static void saveFileFromURL(CloseableHttpClient hc, Path path, String url, String catalogName) throws Exception {
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

        Header header = response.getFirstHeader("Last-Modified");
        String lastModified = header.getValue();
        Date date = DateUtils.parseDate(lastModified);
        FileTime time = FileTime.from(date.toInstant());
        Files.setLastModifiedTime(outFile, time);
    }
}
