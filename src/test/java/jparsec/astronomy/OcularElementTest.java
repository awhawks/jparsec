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
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
