package jparsec.math;

public class FastMathTest {
    /**
     * Test program.
     *
     * @param args Unused
     */
    public static void main(String args[]) {
        System.out.println("FastMath test");
        double a = 0.123;
        System.out.println(Math.cos(a) + "/" + FastMath.cos(a));
        double totalJava = 0, totalJPARSEC = 0;

        //ACCURATE_MODE = false;
        long t0 = System.currentTimeMillis();
        FastMath.initialize();
        long t1 = System.currentTimeMillis();
        double dt = (t1 - t0) / 1000.0;
        System.out.println("Initialize needs " + dt + " seconds");
        int n = 100000000;
        t0 = System.currentTimeMillis();
        double y = 0;

        for (int i = 1; i <= n; i++) {
            double x = Constant.TWO_PI / i;
            y += Math.sin(x);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("JAVA calculates " + n + " sines in " + dt + " seconds.");
        totalJava += dt;
        y = 0;
        t0 = System.currentTimeMillis();

        for (int i = 1; i <= n; i++) {
            double x = Constant.TWO_PI / i;
            y += FastMath.sin(x);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates " + n + " sines in " + dt + " seconds.");
        totalJPARSEC += dt;
        double maxError = -1.0;

        for (int i = 1; i <= n; i++) {
            double x = Constant.TWO_PI / i + FastMath.getResolution() * 0.5; // to get max errors
            double x1 = Math.sin(x);
            double x2 = FastMath.sin(x);
            double dif = Math.abs(x1 - x2);
            if (dif > maxError || maxError < 0)
                maxError = dif;
        }

        System.out.println("Maximum error found " + maxError);
        t0 = System.currentTimeMillis();
        y = 0;

        for (int i = 1; i <= n; i++) {
            double x = Constant.TWO_PI / i;
            y += Math.cos(x);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("JAVA calculates " + n + " cosines in " + dt + " seconds.");
        totalJava += dt;
        y = 0;
        t0 = System.currentTimeMillis();

        for (int i = 1; i <= n; i++) {
            double x = Constant.TWO_PI / i;
            y += FastMath.cos(x);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates " + n + " cosines in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1.0;

        for (int i = 1; i <= n; i++) {
            double x = Constant.TWO_PI / i + FastMath.getResolution() * 0.5; // to get max errors
            double x1 = Math.cos(x);
            double x2 = FastMath.cos(x);
            double dif = Math.abs(x1 - x2);
            if (dif > maxError || maxError < 0)
                maxError = dif;
        }

        System.out.println("Maximum error found " + maxError);
        t0 = System.currentTimeMillis();
        y = 0;
        n /= 10;
        double step = 2.0 / (double) n;

        for (double i = -1; i <= 1; i = i + step) {
            y += Math.asin(i);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("JAVA calculates " + n + " arc-sines in " + dt + " seconds.");
        totalJava += dt;
        y = 0;
        t0 = System.currentTimeMillis();

        for (double i = -1; i <= 1; i = i + step) {
            y += FastMath.asin(i);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates " + n + " arc-sines in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1.0;

        for (double i = -1; i <= 1; i = i + step) {
            double x = i + FastMath.A_STEP * 0.5; // to get max errors
            double x1 = Math.asin(x);
            double x2 = FastMath.asin(x);
            double dif = Math.abs(x1 - x2);
            if (dif > maxError || maxError < 0)
                maxError = dif;
        }

        System.out.println("Maximum error found " + maxError);
        t0 = System.currentTimeMillis();
        y = 0;

        for (double i = -1; i <= 1; i = i + step) {
            y += Math.acos(i);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("JAVA calculates " + n + " arc-cosines in " + dt + " seconds.");
        totalJava += dt;
        y = 0;
        t0 = System.currentTimeMillis();

        for (double i = -1; i <= 1; i = i + step) {
            y += FastMath.acos(i);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates " + n + " arc-cosines in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1.0;

        for (double i = -1; i <= 1; i = i + step) {
            double x = i + FastMath.A_STEP * 0.5; // to get max errors
            double x1 = Math.acos(x);
            double x2 = FastMath.acos(x);
            double dif = Math.abs(x1 - x2);
            if (dif > maxError || maxError < 0)
                maxError = dif;
        }

        System.out.println("Maximum error found " + maxError);
        double z = 0;
        t0 = System.currentTimeMillis();

        for (y = -1000; y <= 1000; y = y + 0.0001) {
            z += Math.atan2(y, 1);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("Java calculates 20000000 atan2s in " + dt + " seconds.");
        totalJava += dt;
        t0 = System.currentTimeMillis();

        for (y = -1000; y <= 1000; y = y + 0.0001) {
            z += FastMath.atan2(y, 1);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates 20000000 atan2s in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1;

        for (y = -1000; y <= 1000; y = y + 0.0001) {
            double atan = Math.atan2(y, 1);
            double atan1 = FastMath.atan2(y, 1);
            if (maxError == -1 || Math.abs(atan - atan1) > maxError) maxError = Math.abs(atan - atan1);
        }

        System.out.println("Maximum error found " + maxError);
        z = 0;
        t0 = System.currentTimeMillis();

        for (y = -1000; y <= 1000; y = y + 0.0001) {
            z += Math.atan2(y, 1);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("Java calculates 20000000 atan2s in " + dt + " seconds.");
        totalJava += dt;
        t0 = System.currentTimeMillis();

        for (y = -1000; y <= 1000; y = y + 0.0001) {
            z += FastMath.atan2_accurate(y, 1);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates 20000000 atan2s in " + dt + " seconds using the 'accurate' version of the function.");
        totalJPARSEC += dt;
        maxError = -1;

        for (y = -1000; y <= 1000; y = y + 0.0001) {
            double atan = Math.atan2(y, 1);
            double atan1 = FastMath.atan2_accurate(y, 1);
            if (maxError == -1 || Math.abs(atan - atan1) > maxError) maxError = Math.abs(atan - atan1);
        }

        System.out.println("Maximum error found " + maxError);
        z = 0;
        t0 = System.currentTimeMillis();

        for (y = -1; y <= 1; y = y + 0.0000001) {
            z += Math.atan(y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("Java calculates 20000000 atans in " + dt + " seconds.");
        totalJava += dt;
        t0 = System.currentTimeMillis();

        for (y = -1; y <= 1; y = y + 0.0000001) {
            z += FastMath.atan(y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates 20000000 atans in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1;

        for (y = -1; y <= 1; y = y + 0.0000001) {
            double atan = Math.atan(y);
            double atan1 = FastMath.atan(y);
            if (maxError == -1 || Math.abs(atan - atan1) > maxError) maxError = Math.abs(atan - atan1);
        }

        System.out.println("Maximum error found " + maxError);
        z = 0;
        t0 = System.currentTimeMillis();

        for (y = 2; y <= 100; y = y + 0.0001) {
            z += Math.log(y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("Java calculates 1000000 log in " + dt + " seconds.");
        totalJava += dt;
        t0 = System.currentTimeMillis();

        for (y = 2; y <= 100; y = y + 0.0001) {
            z += FastMath.log(y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates 1000000 log in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1;

        for (y = 2; y <= 100; y = y + 0.0001) {
            double atan = Math.log(y);
            double atan1 = FastMath.log(y);
            if (maxError == -1 || Math.abs(atan - atan1) / atan > maxError) maxError = Math.abs(atan - atan1) / atan;
        }

        System.out.println("Maximum (relative) error found " + maxError);
        z = 0;
        t0 = System.currentTimeMillis();

        for (y = -100; y <= 100; y = y + 0.0001) {
            z += Math.exp(y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("Java calculates 2000000 exp in " + dt + " seconds.");
        totalJava += dt;
        t0 = System.currentTimeMillis();

        for (y = -100; y <= 100; y = y + 0.0001) {
            z += FastMath.exp(y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates 2000000 exp in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1;

        for (y = -100; y <= 100; y = y + 0.0001) {
            double atan = Math.exp(y);
            double atan1 = FastMath.exp(y);
            if (maxError == -1 || Math.abs(atan - atan1) / atan > maxError) maxError = Math.abs(atan - atan1) / atan;
        }

        System.out.println("Maximum (relative) error found " + maxError);
        z = 0;
        t0 = System.currentTimeMillis();

        for (y = 0.1; y <= 100; y = y + 0.00001) {
            z += Math.pow(y, 100 - y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("Java calculates 10000000 pows in " + dt + " seconds.");
        totalJava += dt;
        t0 = System.currentTimeMillis();

        for (y = 0.1; y <= 100; y = y + 0.00001) {
            z += FastMath.pow(y, 100 - y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates 10000000 pows in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1;

        for (y = 0.1; y <= 100; y = y + 0.00001) {
            double atan = Math.pow(y, 100 - y);
            double atan1 = FastMath.pow(y, 100 - y);
            if (maxError == -1 || Math.abs(atan - atan1) / atan > maxError) maxError = Math.abs(atan - atan1) / atan;
        }

        System.out.println("Maximum (relative) error found " + maxError);
        double val = 2.0;
        double max = 1000.0;
        z = 0;
        t0 = System.currentTimeMillis();

        for (y = 0.1; y <= max; y = y + 0.0001) {
            z += Math.pow(y, val);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("Java calculates 10000000 pows(x, " + val + ") in " + dt + " seconds.");
        totalJava += dt;
        t0 = System.currentTimeMillis();

        for (y = 0.1; y <= max; y = y + 0.0001) {
            z += FastMath.pow(y, val);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates 10000000 pows(x, " + val + ") in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1;

        for (y = 0.1; y <= max; y = y + 0.0001) {
            double atan = Math.pow(y, val);
            double atan1 = FastMath.pow(y, val);
            if (maxError == -1 || Math.abs(atan - atan1) / atan > maxError) maxError = Math.abs(atan - atan1) / atan;
        }

        System.out.println("Maximum (relative) error found " + maxError);
        max = 6000;
        z = 0;
        t0 = System.currentTimeMillis();

        for (y = 0.1; y <= max; y = y + 0.0001) {
            z += Math.sqrt(y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("Java calculates 600000000 sqrts in " + dt + " seconds.");
        totalJava += dt;
        t0 = System.currentTimeMillis();

        for (y = 0.1; y <= max; y = y + 0.0001) {
            z += FastMath.sqrt(y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates 600000000 sqrts in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1;

        for (y = 0.1; y <= max; y = y + 0.0001) {
            double atan = Math.sqrt(y);
            double atan1 = FastMath.sqrt(y);
            if (maxError == -1 || Math.abs(atan - atan1) / atan > maxError) maxError = Math.abs(atan - atan1) / atan;
        }

        System.out.println("Maximum (relative) error found " + maxError);
        max = 6;
        z = 0;
        t0 = System.currentTimeMillis();

        for (y = 0.1; y <= max; y = y + 0.000001) {
            z += Math.tan(y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("Java calculates 6000000 tans in " + dt + " seconds.");
        totalJava += dt;
        t0 = System.currentTimeMillis();

        for (y = 0.1; y <= max; y = y + 0.000001) {
            z += FastMath.tan(y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates 6000000 tans in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1;

        for (y = 0.1; y <= max; y = y + 0.000001) {
            double atan = Math.tan(y);
            double atan1 = FastMath.tan(y);
            if (maxError == -1 || Math.abs(atan - atan1) / atan > maxError) maxError = Math.abs(atan - atan1) / atan;
        }

        System.out.println("Maximum (relative) error found " + maxError);
        max = 6;
        z = 0;
        t0 = System.currentTimeMillis();

        for (y = 0.1; y <= max; y = y + 0.000001) {
            z += Math.hypot(y, max - y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("Java calculates 6000000 hypot in " + dt + " seconds.");
        totalJava += dt;
        t0 = System.currentTimeMillis();

        for (y = 0.1; y <= max; y = y + 0.000001) {
            z += FastMath.hypot(y, max - y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates 6000000 hypot in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1;

        for (y = 0.1; y <= max; y = y + 0.000001) {
            double atan = Math.hypot(y, max - y);
            double atan1 = FastMath.hypot(y, max - y);
            if (maxError == -1 || Math.abs(atan - atan1) / atan > maxError) maxError = Math.abs(atan - atan1) / atan;
        }

        System.out.println("Maximum (relative) error found " + maxError);
        max = 6E5;
        z = 0;
        t0 = System.currentTimeMillis();

        for (y = 0.1; y <= max; y = y + 0.1) {
            z += Double.parseDouble("" + y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("Java calculates 6000000 parseDoubles in " + dt + " seconds.");
        totalJava += dt;
        t0 = System.currentTimeMillis();

        for (y = 0.1; y <= max; y = y + 0.1) {
            z += FastMath.parseDouble("" + y);
        }

        t1 = System.currentTimeMillis();
        dt = (t1 - t0) / 1000.0;
        System.out.println("FastMath calculates 6000000 parseDoubles in " + dt + " seconds.");
        totalJPARSEC += dt;
        maxError = -1;

        for (y = 0.1; y <= max; y = y + 0.1) {
            double atan = Double.parseDouble("" + y);
            double atan1 = FastMath.parseDouble("" + y);
            if (maxError == -1 || Math.abs(atan - atan1) / atan > maxError) maxError = Math.abs(atan - atan1) / atan;
        }

        System.out.println("Maximum (relative) error found " + maxError);
        System.out.println();
        System.out.println("Total time (Java):    " + totalJava);
        System.out.println("Total time (JPARSEC): " + totalJPARSEC);
    }
}
