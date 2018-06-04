package jparsec.astrophysics.gildas;

import jparsec.graph.DataSet;
import jparsec.io.ConsoleReport;
import jparsec.io.ReadFile;

public class pyGildasTest {
    /**
     * Test program.
     *
     * @param args Unused.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("pyGildas test");

        // GREG test
/*
        pyGildas gildas = new pyGildas(
            "/home/alonso/java/librerias/python/gildas-exe-dec08b", "pc-debian5.0.3-g95",
            //"/home/alonso/aplicaciones/alias.txt", // The same, previous 2 variables defined here
            "/home/alonso/reduccion/2010/reajusteRMonConDataCube", "pyclass");
*/
        pyGildas gildas = new pyGildas(
            //"/home/alonso/aplicaciones/gildas/gildas-exe-mar13a", "x86_64-debianwheezy-gfortran", // home desktop
            "/home/gildas/gildas-exe-jan13a", "pc-debian6-gfortran", // OAN desktop
            //"/home/alonso/.bashrc", // The same, previous 2 variables defined here
            //"/home/alonso/aplicaciones/alias.txt", // The same, previous 2 variables defined here
            "/home/alonso/reduccion/2010/reajusteRMonConDataCube", "pyclass");
        ConsoleReport.stringArrayReport(gildas.script);

        String script[] = DataSet.arrayListToStringArray(
                ReadFile.readAnyExternalFile("/home/alonso/reduccion/2010/reajusteRMonConDataCube/fitDisk.class"));
        //script = new String[] {"@850.mapping"};
        String parameters[] = null; //new String[] {"1", "2"};
        String variables[] = new String[] { "rms" };
        String out[] = gildas.executeScript(script, parameters, variables);

        if (out == null) {
            System.out.println("AN ERROR OCCURED");
        } else {
            System.out.println("OUTPUT");
            System.out.println(gildas.getConsoleOutput());
            System.out.println("ERROR");
            System.out.println(gildas.getErrorOutput());
            System.out.println("EXECUTED SCRIPT");
            ConsoleReport.stringArrayReport(gildas.getLastScriptExecuted());
            if (variables != null) {
                System.out.println("OUTPUT VARIABLES");
                for (int i = 0; i < variables.length; i++) {
                    System.out.println(variables[i] + " = " + gildas.getVariableValue(variables[i]));
                }
            }
        }

        // My laptop
        // MAPPING test
/*
        gildas = new pyGildas("/home/alonso/java/librerias/python/gildas-exe-dec08b",
                "pc-debian5.0.3-g95", "/home/alonso/reduccion/2007/discos/scuba/mwc297", "pymapping");
        script = DataSet.arrayListToStringArray(ReadFile.readAnyExternalFile("/home/alonso/reduccion/2007/discos/scuba/mwc297/850.mapping"));
        //script = new String[] {"@850.mapping"};
        parameters = new String[] {"1", "2"};
        variables = new String[] {"field"};
        out = gildas.executeScript(script, parameters, variables);

        if (out == null) {
            System.out.println("AN ERROR OCCURED");
        } else {
            System.out.println("OUTPUT");
            System.out.println(gildas.getConsoleOutput());
            System.out.println("ERROR");
            System.out.println(gildas.getErrorOutput());
            System.out.println("EXECUTED SCRIPT");
            ConsoleReport.stringArrayReport(gildas.getLastScriptExecuted());
            if (variables != null) {
                System.out.println("OUTPUT VARIABLES");
                for (int i=0; i<variables.length; i++) {
                    System.out.println(variables[i] + " = " + gildas.getVariableValue(variables[i]));
                }
            }
        }
*/
    }
}
