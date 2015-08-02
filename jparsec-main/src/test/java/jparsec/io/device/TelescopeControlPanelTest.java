package jparsec.io.device;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import jparsec.io.FileIO;
import jparsec.io.ReadFile;
import jparsec.math.Constant;
import jparsec.observer.LocationElement;
import jparsec.util.Translate;

public class TelescopeControlPanelTest {
    /**
     * Test program.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        // Translate.setDefaultLanguage(LANGUAGE.SPANISH);

        JFrame app = new JFrame(Translate.translate(1127));

        app.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });

        app.setIconImage(ReadFile.readImageResource(FileIO.DATA_IMAGES_DIRECTORY + "telescope_transparentOK.png"));

        // Set the hardware
        GenericTelescope.TELESCOPE_MODEL telescopeModel = GenericTelescope.TELESCOPE_MODEL.VIRTUAL_TELESCOPE_EQUATORIAL_MOUNT; //.MEADE_AUTOSTAR;
        GenericDome.DOME_MODEL domeModel = GenericDome.DOME_MODEL.VIRTUAL_DOME;
        GenericCamera.CAMERA_MODEL cameraModel[] = new GenericCamera.CAMERA_MODEL[] { GenericCamera.CAMERA_MODEL.VIRTUAL_CAMERA }; //CAMERA_MODEL.CANON_EOS_40D_400D_50D_500D_1000D;
        GenericWeatherStation.WEATHER_STATION_MODEL weatherStation = GenericWeatherStation.WEATHER_STATION_MODEL.VIRTUAL_WEATHER_STATION;
        ObservationManager obsManager = new ObservationManager("/home/alonso/", "today", telescopeModel, cameraModel, domeModel, weatherStation);
        obsManager.setTelescopeType(GenericTelescope.TELESCOPE_TYPE.SCHMIDT_CASSEGRAIN);
        obsManager.setCameraMinimumIntervalBetweenShots(0, 20);
        obsManager.setCombineMethod(ObservationManager.COMBINATION_METHOD.MEDIAN);
        obsManager.setInterpolationMethod(ObservationManager.INTERPOLATION.BICUBIC);
        obsManager.setDrizzleMethod(ObservationManager.DRIZZLE.NO_DRIZZLE);
        obsManager.setAverageMethod(ObservationManager.AVERAGE_METHOD.PONDERATION);
        obsManager.setTelescopeParkPosition(new LocationElement(0, Constant.PI_OVER_TWO, 1)); // Park to the zenith
        // Ports for telescope and camera are set to null to automatically scan and select the first one available
        boolean addSky = true;

        TelescopeControlPanel tcp = new TelescopeControlPanel(obsManager, addSky);
        Dimension d = tcp.getPreferredSize();

        // Border + window title
        d.height += 80;
        d.width += 10;

        app.add(tcp);
        app.setSize(d);
        app.setVisible(true);

        if (obsManager.reductionPossible()) {
            JFrame app2 = new JFrame(Translate.translate(1188));

            app2.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent evt) {
                    System.exit(0);
                }
            });

            app2.setIconImage(ReadFile.readImageResource(FileIO.DATA_IMAGES_DIRECTORY + "planetaryNeb_transparentOK.png"));
            Dimension d2 = obsManager.getPreferredSize();
            d2.height += 80;
            d2.width += 10;
            app2.add(obsManager);
            app2.setSize(d2);
            app2.setVisible(true);
        }
    }
}
