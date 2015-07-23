package jparsec.io;

import java.util.ArrayList;
import jparsec.graph.DataSet;
import jparsec.util.JPARSECException;

public class CatalogReadTest {
    /**
     * For unit testing only.
     *
     * @param args Name of the molecule in index 0 and catalog name in index 1, optional.
     */
    public static void main(String args[]) {
        String mol = "";
        boolean jpl = true;
        if (args == null || args.length == 0) {
            System.out.println("CatalogRead Test");
        } else {
            mol = args[0];
            if (args.length > 1) {
                String cat = args[1].trim().toLowerCase();
                if (!cat.equals("jpl")) jpl = false;
            }
        }

        try {
            ArrayList<String> v;
            if (mol.equals("")) {
                int hcn = 43; // index of HCN in JPL
                if (jpl) {
                    v = CatalogRead.readJPLcatalog();
                } else {
                    v = CatalogRead.readCOLOGNEcatalog();
                    hcn = 45; // index of HCN in CDMS
                }
                System.out.println(DataSet.arrayListToString(v));

                System.out.println(v.get(hcn) + ":");
                String m = CatalogRead.getMoleculeFileName(CatalogRead.getMolecule(v.get(hcn), jpl));
                if (jpl) {
                    v = CatalogRead.readJPLtransitions(m, 0);
                } else {
                    v = CatalogRead.readCOLOGNEtransitions(m, 0);
                }
                System.out.println(DataSet.arrayListToString(v));

            } else {
                if (mol.toLowerCase().equals("all")) {
                    if (jpl) {
                        v = CatalogRead.readJPLcatalog();
                    } else {
                        v = CatalogRead.readCOLOGNEcatalog();
                    }
                } else {
                    String m = CatalogRead.getMoleculeFileName(CatalogRead.getMolecule(mol, jpl));
                    if (jpl) {
                        v = CatalogRead.readJPLtransitions(m, 0);
                    } else {
                        v = CatalogRead.readCOLOGNEtransitions(m, 0);
                    }
                }
                System.out.println("<HTML><pre>");
                for (int i = 0; i < v.size(); i++) {
                    System.out.println(v.get(i));
                }
                System.out.println("</pre></HTML>");
            }
        } catch (JPARSECException ve) {
            JPARSECException.showException(ve);
        }
    }
}
