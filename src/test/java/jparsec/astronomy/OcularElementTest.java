package jparsec.astronomy;

import jparsec.util.JPARSECException;

public class OcularElementTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) {
        System.out.println("OcularElement test");

        try {
            OcularElement ocul[] = OcularElement.getAllAvailableOculars();

            System.out.println("List of all oculars");
            for (int i = 0; i < ocul.length; i++) {
                System.out.println(ocul[i].name + '/' + ocul[i].focalLength + '/' + ocul[i].fieldOfView + '/' + ocul[i].reticleSize);
            }

            /*for (int i = 0; i < ocul.length; i++) {
                String name = ocul[i].name;
                name = DataSet.replaceAll(name, "Ã¡", "&aacute;", true);
                name = DataSet.replaceAll(name, "Ã©", "&eacute;", true);
                name = DataSet.replaceAll(name, "Ã­", "&iacute;", true);
                name = DataSet.replaceAll(name, "Ã³", "&oacute;", true);
                name = DataSet.replaceAll(name, "Ãº", "&uacute;", true);
                name = DataSet.replaceAll(name, "Ã±", "&ntilde;", true);
                name = DataSet.replaceAll(name, "Âº", "&deg;", true);
                name = DataSet.replaceAll(name, "Ã¶", "&ouml;", true);
                String val = ""+ocul[i].focalLength+" "+(float) (ocul[i].fieldOfView * Constant.RAD_TO_DEG);
                System.out.println(" <OPTION value=\""+val+"\">"+name+"</OPTION>");
            } */
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
