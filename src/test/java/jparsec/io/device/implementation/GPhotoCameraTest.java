package jparsec.io.device.implementation;

import jparsec.graph.DataSet;
import jparsec.io.ConsoleReport;
import jparsec.io.image.Picture;
import jparsec.util.JPARSECException;

public class GPhotoCameraTest {
    /**
     * Test program.
     *
     * @param args Unused.
     */
    public static void main(String args[]) {
        System.out.println("GPhotoCamera test");

        try {
            String dir = "/home/alonso/";
            String out[] = GPhotoCamera.autoDetect();
            System.out.println("Using gphoto " + GPhotoCamera.gphotoVersion);
            System.out.println("Detected cameras:");
            ConsoleReport.stringArrayReport(out);
            GPhotoCamera c = new GPhotoCamera(null, dir, false);

            GPhotoCamera.CAMERA_PARAMETER cv[] = GPhotoCamera.CAMERA_PARAMETER.values();
            for (int i = 0; i < cv.length; i++) {
                System.out.println("Possible values of " + cv[i]);
                String values[] = c.getConfig(cv[i]);
                ConsoleReport.stringArrayReport(values);
            }
            c.setParameter(GPhotoCamera.CAMERA_PARAMETER.ISO, "640");
            c.setParameter(GPhotoCamera.CAMERA_PARAMETER.SHUTTER_SPEED, "1/4"); // 'bombilla' or 'bulb'
            c.setParameter(GPhotoCamera.CAMERA_PARAMETER.APERTURE, "3.5");
            c.setParameter(GPhotoCamera.CAMERA_PARAMETER.RESOLUTION, "JPEG grande fino");
            c.setBulbTime(5);
            String path = c.shotAndRetrieveImage();
            System.out.println(path);

            if (path != null) {
                String p[] = DataSet.toStringArray(path, ",");
                for (int i = 0; i < p.length; i++) {
                    if (p[i].endsWith(".jpg")) {
                        Picture pic = new Picture(p[i]);
                        pic.show(800, 600, p[i], true, true, true);
                    }
                }
            }
        } catch (JPARSECException exc) {
            exc.showException();
        }
    }
}
