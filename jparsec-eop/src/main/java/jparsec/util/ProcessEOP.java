package jparsec.util;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ProcessEOP {
    /**
     * The url used to update IAU1980 Earth Orientation Parameters.
     */
    public static final String URL_EOP_IAU1980 = "http://hpiers.obspm.fr/iers/eop/eopc04/eopc04.62-now";
    /**
     * The url used to update IAU2000 Earth Orientation Parameters.
     */
    public static final String URL_EOP_IAU2000 = "http://hpiers.obspm.fr/iers/eop/eopc04/eopc04_IAU2000.62-now";

    /**
     * File of Earth Rotation Parameters, iau1980 version.
     */
    public static final String FILE_IAU1980 = "IERS_EOP_iau1980.txt";
    /**
     * Path to the file of Earth Rotation Parameters, iau2000 version.
     */
    public static final String FILE_IAU2000 = "IERS_EOP_iau2000.txt";

    public static void main(String[] args) throws Exception {
        Path targetDir = Paths.get(args[0]);
        Files.createDirectories(targetDir);

        CloseableHttpClient hc = HttpClients.createDefault();

        saveFileFromURL(hc, targetDir, URL_EOP_IAU1980, FILE_IAU1980);
        saveFileFromURL(hc, targetDir, URL_EOP_IAU2000, FILE_IAU2000);
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
