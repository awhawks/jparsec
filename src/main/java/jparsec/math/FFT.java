package jparsec.math;

import jparsec.graph.DataSet;
import jparsec.util.JPARSECException;

/**
 * FFT (Fast Fourier Transform) utilities.
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class FFT {

    /**
     * Compute the FFT of x.
     * @param x Set of complex.
     * @return The FFT.
     * @throws JPARSECException If the length of x is not a power of 2.
     */
    public static Complex[] fft(Complex[] x) throws JPARSECException {
        int N = x.length;

        // base case
        if (N == 1) return new Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) throw new JPARSECException("N is not a power of 2");

        // fft of even terms
        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        double c = Constant.TWO_PI / N;
        for (int k = 0; k < N/2; k++) {
            double kth = -c * k;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].add(wk.multiply(r[k]));
            y[k + N/2] = q[k].substract(wk.multiply(r[k]));
        }
        return y;
    }

    /**
     * Compute the inverse FFT of x.
     * @param x Set of complex.
     * @return The inverse FFT.
     * @throws JPARSECException If the length of x is not a power of 2.
     */
    public static Complex[] ifft(Complex[] x) throws JPARSECException {
        int N = x.length;
        
        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) throw new JPARSECException("N is not a power of 2");

        Complex[] y = new Complex[N];

        // take conjugate
        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }

        // compute forward FFT
        y = fft(y);

        // take conjugate again
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        // divide by N
        double c = 1.0 / N;
        for (int i = 0; i < N; i++) {
            y[i] = y[i].multiply(c);
        }

        return y;

    }

    /**
     * Compute the circular convolution of x and y.
     * @param x X set of complex.
     * @param y Y set of complex.
     * @return The convolution.
     * @throws JPARSECException If the length of x is not a power of 2.
     */
    public static Complex[] circularConvolution(Complex[] x, Complex[] y) throws JPARSECException {
        if (x.length != y.length) throw new JPARSECException("Dimensions don't agree");

        int N = x.length;

        // compute FFT of each sequence
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // point-wise multiply
        Complex[] c = new Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = a[i].multiply(b[i]);
        }

        // compute inverse FFT
        return ifft(c);
    }


    /**
     * Compute the linear convolution of x and y.
     * @param x X set of complex.
     * @param y Y set of complex.
     * @return The convolution.
     * @throws JPARSECException If the length of x is not a power of 2.
     */
    public static Complex[] linearConvolution(Complex[] x, Complex[] y) throws JPARSECException {
        Complex ZERO = new Complex(0, 0);

        Complex[] a = new Complex[2*x.length];
        for (int i = 0;        i <   x.length; i++) a[i] = x[i];
        for (int i = x.length; i < 2*x.length; i++) a[i] = ZERO;

        Complex[] b = new Complex[2*y.length];
        for (int i = 0;        i <   y.length; i++) b[i] = y[i];
        for (int i = y.length; i < 2*y.length; i++) b[i] = ZERO;

        return circularConvolution(a, b);
    }

    // display an array of Complex numbers to standard output
    /*
    private static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }
        System.out.println();
    }*/

    /**
     * Returns the closest power of 2 lower or equal to the input value.
     * @param n Input value.
     * @return Closest power of 2.
     */
    public static int getClosestPowerOf2(int n) {
    	return (int) Math.pow(2.0, (int)(Math.log(n) / Math.log(2.0)));
    }

    /**
     * Filters a set of data (likely a spectrum) and returns it after FFT filtering.
     * @param data The data to filter.
     * @param cutFactorRespectMax The cut factor respect the highest amplitude in the
     * FFT transform. All frequencies below an amplitude equals to max/cutFactorRespectMax, 
     * likely noise, will be removed.
     * @return The filtered spectrum.
     * @throws JPARSECException If an error occurs.
     */
    public static double[] filter(double data[], double cutFactorRespectMax) throws JPARSECException {
		// Compute FFT and a chart of frequencies vs amplitudes
		Complex cc[] = Complex.createSetOfComplex(data);
        Complex[] ccy = fft(cc);

		// Process the spectrum by eliminating high and low frequencies,
		// and show another chart
		double r[] = Complex.getSetOfReals(ccy);
		double max = DataSet.getMaximumValue(r);
		for (int i=0; i<ccy.length; i++) {
			if (Math.abs(ccy[i].real) < max/cutFactorRespectMax) ccy[i] = new Complex(0, 0);
		}

		// Compute inverse FFT to recover the new spectrum without the high amplitude
		// frequencies (likely noise)
		Complex cccy[] = ifft(ccy);
		return Complex.getSetOfReals(cccy);
    }
    
    /**
     * Filters a set of data (likely a spectrum) and returns it after FFT filtering.
     * @param data The data to filter.
     * @param cutFactorRespectMax The cut factor respect the highest amplitude in the
     * FFT transform. All frequencies below an amplitude equals to max/cutFactorRespectMax, 
     * likely noise, will be removed.
     * @return The filtered spectrum.
     * @throws JPARSECException If an error occurs.
     */
    public static float[] filter(float data[], double cutFactorRespectMax) throws JPARSECException {
		// Compute FFT and a chart of frequencies vs amplitudes
		Complex cc[] = Complex.createSetOfComplex(DataSet.toDoubleArray(data));
        Complex[] ccy = fft(cc);

		// Process the spectrum by eliminating high and low frequencies,
		// and show another chart
		double r[] = Complex.getSetOfReals(ccy);
		double max = DataSet.getMaximumValue(r);
		for (int i=0; i<ccy.length; i++) {
			if (Math.abs(ccy[i].real) < max/cutFactorRespectMax) ccy[i] = new Complex(0, 0);
		}

		// Compute inverse FFT to recover the new spectrum without the high amplitude
		// frequencies (likely noise)
		Complex cccy[] = ifft(ccy);
		return DataSet.toFloatArray(Complex.getSetOfReals(cccy));
    }
}