package jparsec.astronomy;

import jparsec.ephem.Functions;
import jparsec.math.Constant;
import jparsec.util.Translate;

public class TelescopeElementTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("TelescopeElement Test");

        TelescopeElement telescope = TelescopeElement.SCHMIDT_CASSEGRAIN_20cm;

        Translate.setDefaultLanguage(Translate.LANGUAGE.SPANISH);
        System.out.println("Field of view: " + Functions.formatAngle(telescope.getField(), 1));
        System.out.println("Limiting magnitude: " + Functions.formatValue(telescope.getLimitingMagnitude(), 1));
        System.out.println("Resolution: " + Functions.formatAngle(telescope.getResolution(), 1));
        System.out.println("mm for 1 degree object: " + Functions.formatValue(telescope.getObjectSizeAtFilmPlane(0.5 * Constant.DEG_TO_RAD), 1));

        TelescopeElement tel[] = TelescopeElement.getAllAvailableTelescopes();
        System.out.println("List of all telescopes");

        for (int i = 0; i < tel.length; i++) {
            System.out.println(tel[i].name + "/" + tel[i].diameter + "/" + tel[i].focalLength + "/" + tel[i].centralObstruction + "/" + tel[i].spidersSize + "/" + tel[i].cromatismLevel);
            System.out.println("  Primary focus field: " + tel[i].getPrimaryFocusField() * Constant.RAD_TO_DEG);
            System.out.println("  Field: " + tel[i].getField() * Constant.RAD_TO_DEG);
            System.out.println("  Limiting magnitude: " + tel[i].getLimitingMagnitude());
            System.out.println("  Resolution: " + tel[i].getResolution() * Constant.RAD_TO_ARCSEC);
            tel[i].attachCCDCamera(CCDElement.getCCD("ST-9E"));
            System.out.println("  Limiting magnitude for CCD ST9E and 100s exposure: " + tel[i].getCCDLimitingMagnitude(100, 19, 7));
            System.out.println("  Field: " + tel[i].getField() * Constant.RAD_TO_DEG);
            tel[i].attachCCDCamera(null);
            System.out.println("  Field: " + tel[i].getField() * Constant.RAD_TO_DEG);
            System.out.println("  Invert H/V: " + tel[i].invertHorizontal + "/" + tel[i].invertVertical);
        }

        /* for (int i = 0; i < tel.length; i++) {
            int type = 0;
            String name = tel[i].name;
            name = DataSet.replaceAll(name, "á", "&aacute;", true);
            name = DataSet.replaceAll(name, "é", "&eacute;", true);
            name = DataSet.replaceAll(name, "í", "&iacute;", true);
            name = DataSet.replaceAll(name, "ó", "&oacute;", true);
            name = DataSet.replaceAll(name, "ú", "&uacute;", true);
            name = DataSet.replaceAll(name, "ñ", "&ntilde;", true);
            name = DataSet.replaceAll(name, "°", "&deg;", true);
            name = DataSet.replaceAll(name, "ö", "&ouml;", true);
            name = DataSet.replaceAll(name, "¼", ".25", true);
            boolean sct = false, newton = false, refractor = false;
            if (name.toLowerCase().indexOf("binocular") >= 0 || name.toLowerCase().indexOf("human") >= 0 || name.toLowerCase().indexOf("obje") >= 0 ) {
                type = 0;
            } else {
                if (name.equals("SCT") || name.indexOf("Schmidt-Cassegrain") >= 0 || name.indexOf(" SCT") >= 0 || name.indexOf(" Mak") >= 0 || name.indexOf(" Cass") >= 0) sct = true;
                if (name.indexOf("Newton") >= 0 || name.indexOf(" Newt") >= 0 || name.indexOf(" Dob") >= 0 ) newton = true;
                if (name.equals("Refractor") || name.indexOf(" Refr") >= 0) refractor = true;
                if (!sct && !newton && !refractor) { // Account for scopes in the scope.txt file that doesn't have clear IDs
                    if (tel[i].diameter < 150) {
                        refractor = true;
                    } else {
                        newton = true;
                    }
                }
                if (refractor) type = 1;
                if (newton) type = 2;
                if (sct) type = 3;
            }
            String val = ""+tel[i].focalLength+" "+(float)tel[i].getFocalRatio()+" "+type;
            System.out.println(" <OPTION value=\""+val+"\">"+name+"</OPTION>");
        }
*/
    }
}
