package jparsec.io;

import java.util.Properties;

public class ApplicationLauncherTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("My linux desktop is " + ApplicationLauncher.getLinuxDesktop());

        Process p = ApplicationLauncher.executeCommand("env");
        String output = ApplicationLauncher.getConsoleOutputFromProcess(p);
        System.out.println(output);
        String var = "jparsecManagerPath";
        System.out.println("Environtment variable " + var + " = " + ApplicationLauncher.getEnvironmentVariable(var));
        System.out.println(ApplicationLauncher.getUserName());

        Properties prop = new Properties(System.getProperties());
        prop.list(System.out);
    }
}
