package jparsec.util;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ProcessJPL {
    /**
     * The url used to update JPL catalogue of molecular spectroscopy.
     */
    public static final String URL_JPL_CATALOGUE = "http://spec.jpl.nasa.gov/ftp/pub/catalog/";

    public static void main(String[] args) throws Exception {
        Path targetDir = Paths.get(args[0]);
        Files.createDirectories(targetDir);
        String query = URL_JPL_CATALOGUE + "catdir.cat";

        CloseableHttpClient hc = HttpClients.createDefault();
        HttpGet get = new HttpGet(query);
        CloseableHttpResponse response = hc.execute(get);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
        EntityUtils.consume(entity);
        String lines[] = content.split("\n");
        int i = 0;

        saveFileFromURL(hc, targetDir, "catdir.cat");
        String catalogName;

        while (i < lines.length) {
            catalogName = StringUtils.leftPad(lines[i].substring(0, 6).trim(), 6, '0');
            catalogName = "c" + catalogName + ".cat";
            saveFileFromURL(hc, targetDir, catalogName);
            i++;
        }
    }

    private static void saveFileFromURL(CloseableHttpClient hc, Path path, String catalogName) throws Exception {
        String query = URL_JPL_CATALOGUE + catalogName;
        HttpGet get = new HttpGet(query);
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
