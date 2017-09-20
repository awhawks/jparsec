/*
 * This file is part of JPARSEC library.
 *
 * (C) Copyright 2006-2017 by T. Alonso Albi - OAN (Spain).
 *
 * Project Info:  http://conga.oan.es/~alonso/jparsec/jparsec.html
 *
 * JPARSEC library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JPARSEC library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package jparsec.math;

import java.awt.Color;

import flanagan.analysis.RegressionFunction;
import jparsec.graph.ChartSeriesElement;
import jparsec.graph.DataSet;
import jparsec.util.JPARSECException;
import jparsec.util.Logger;
import jparsec.util.Logger.LEVEL;

/**
 * Extension of the class by Michael Thomas Flanagan to better integrate the fitting 
 * methods in JPARSEC.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Regression extends flanagan.analysis.Regression {
	
	/**
	 * Constructor with data with x as 1D array and no weights.
	 * @param xxData The x values.
	 * @param yData The y values.
	 * @throws JPARSECException If an error occurs.
	 */
	public Regression(double[] xxData, double[] yData) throws JPARSECException {
		int n = xxData.length;

		double weight[] = DataSet.getSetOfValues(1.0, 1.0, n, false);
		double[][] xData = new double[1][n];
		this.weightOpt = false;
		for (int i = 0; i < n; i++) {
			xData[0][i] = xxData[i];
		}

		this.setDefaultValues(xData, yData, weight);
	}

	/**
	 * Constructor with data with x as 1D array and weights provided.
	 * @param xxData The x values.
	 * @param yData The y values.
	 * @param weight The weights.
	 * @throws JPARSECException If an error occurs.
	 */
	public Regression(double[] xxData, double[] yData, double[] weight)
			throws JPARSECException {
		int n = xxData.length;
		double[][] xData = new double[1][n];
		for (int i = 0; i < n; i++) {
			xData[0][i] = xxData[i];
		}

		weight = this.checkForZeroWeights(weight);
		this.setDefaultValues(xData, yData, weight);
	}

	/**
	 * Constructor with data with x as 2D array and weights provided.
	 * @param xData the array of x arrays.
	 * @param yData The array of y values.
	 * @param weight The weights.
	 * @throws JPARSECException If an error occurs.
	 */
	public Regression(double[][] xData, double[] yData, double[] weight)
			throws JPARSECException {
		weight = this.checkForZeroWeights(weight);
		this.setDefaultValues(xData, yData, weight);
	}

	/**
	 * Constructor with data with x as 2D array and no weights provided.
	 * @param xData the array of x arrays.
	 * @param yData The array of y values.
	 * @throws JPARSECException If an error occurs.
	 */
	public Regression(double[][] xData, double[] yData) throws JPARSECException {
		int n = yData.length;
		double[] weight = new double[n];

		this.weightOpt = false;
		for (int i = 0; i < n; i++)
			weight[i] = 1.0D;

		setDefaultValues(xData, yData, weight);
	}
	
	/**
	 * Returns if convergency was reached.
	 * @return True or false.
	 */
	public boolean convergence() {
		return this.nlrStatus;
	}
	
	// Fit data to a Gaussian (normal) probability function
	private void fitCustom(String function, double start[], int plotFlag) throws JPARSECException {
		if (multipleY)
			throw new JPARSECException(
					"This method cannot handle multiply dimensioned y arrays");
		nParam = start.length;
		degreesOfFreedom = nData - nParam;
		if (degreesOfFreedom < 1 && !ignoreDofFcheck)
			throw new JPARSECException(
					"Degrees of freedom must be greater than 0");

		// order data into ascending order of the abscissae
		Regression.sort(xData[0], yData, weight);

		// Fill arrays needed by the Simplex
		double[] step = new double[nParam];
		for (int i=0; i<step.length; i++) {
			step[i] = 0.1D * Math.abs(start[i]);
		}

		// Nelder and Mead Simplex Regression
		GenericFunction f = new GenericFunction();
		f.setFunction(function);
		//addConstraint(1, -1, 0.0D);

		Object regFun2 = (Object) f;
		nelderMead(regFun2, null, start, step, fTol, this.nMax);
	}
	
	/**
	 * Fits the input data to a custom function f=f(x). The unknown parameters
	 * must be named p1, p2, p3, ... Convergency is not guarranteed, even in case
	 * {@linkplain Regression#convergence()} return true.
	 * @param function The function in Java notation.
	 * @param initialValues initial estimates of the unknown parameters. The values
	 * should be as close as possible to the correct fitting, specially in the relative
	 * signs of the parameters.
	 * @return A series object representing the data and the fitting, for charting
	 * purposes.
	 * @throws JPARSECException If an error occurs.
	 */
	public ChartSeriesElement customFunction(String function, double initialValues[]) throws JPARSECException {
		if (initialValues == null) throw new JPARSECException("Initial estimates cannot be null");
		this.fitCustom(function, initialValues, 0);
		ChartSeriesElement series = new ChartSeriesElement(
				xData[0], yData, null, null,
				"", true, Color.BLACK, ChartSeriesElement.SHAPE_CIRCLE,
				ChartSeriesElement.REGRESSION.REGRESSION_CUSTOM);
		series.regressionType.setEquation(function);
		series.regressionType.setCustomRegressionFitFunctions(function, initialValues);
		series.regressionType.setEquationValues(this.getBestEstimates(), this.getBestEstimatesErrors());
		return series;
	}

	/**
	 * Polynomial fitting
	 * y = a + b.x + c.x^2 + d.x^3 + . . .
	 *
	 * @param deg The degree of the polynomial.
	 * @return A series object representing the data and the fitting, for charting
	 * purposes.
	 * @throws JPARSECException If an error occurs.
	 */
	public ChartSeriesElement polynomialFitSeries(int deg) throws JPARSECException {
		if (this.multipleY)
			throw new JPARSECException(
					"This method cannot handle multiply dimensioned y arrays");
		if (this.nXarrays > 1)
			throw new JPARSECException(
					"This class will only perform a polynomial regression on a single x array");
		if (deg < 1)
			throw new JPARSECException(
					"Polynomial degree must be greater than zero");

		this.nParam = deg + 1;
		this.degreesOfFreedom = this.nData - this.nParam;
		if (this.degreesOfFreedom < 1 && !this.ignoreDofFcheck)
			throw new JPARSECException(
					"Degrees of freedom must be greater than 0");
		double[][] aa = new double[this.nParam][this.nData];

		for (int j = 0; j < nData; j++)
			aa[0][j] = 1.0D;
		for (int j = 0; j < nData; j++)
			aa[1][j] = this.xData[0][j];

		for (int i = 2; i < nParam; i++) {
			for (int j = 0; j < nData; j++) {
				aa[i][j] = Math.pow(this.xData[0][j], i);
			}
		}
		this.best = new double[this.nParam];
		this.bestSd = new double[this.nParam];
		this.tValues = new double[this.nParam];
		this.pValues = new double[this.nParam];
		this.generalLinear(aa);
		if (!this.ignoreDofFcheck)
			this.generalLinearStats(aa);

		ChartSeriesElement series = new ChartSeriesElement(
				xData[0], yData, null, null,
				"", true, Color.BLACK, ChartSeriesElement.SHAPE_CIRCLE,
				ChartSeriesElement.REGRESSION.POLYNOMIAL);
		series.regressionType.setPolynomialDegree(deg);
		series.regressionType.setEquationValues(this.getBestEstimates(), this.getBestEstimatesErrors());
		return series;
	}

	/**
	 * Polynomial fitting
	 * y = a + b.x + c.x^2 + d.x^3 + . . .
	 *
	 * @param deg The degree of the polynomial.
	 * @return Coefficients of the best estimates for the polynomial.
	 * @throws JPARSECException If an error occurs.
	 */
	public double[] polynomialFit(int deg) throws JPARSECException {
		if (this.multipleY)
			throw new JPARSECException(
					"This method cannot handle multiply dimensioned y arrays");
		if (this.nXarrays > 1)
			throw new JPARSECException(
					"This class will only perform a polynomial regression on a single x array");
		if (deg < 1)
			throw new JPARSECException(
					"Polynomial degree must be greater than zero");

		this.nParam = deg + 1;
		this.degreesOfFreedom = this.nData - this.nParam;
		if (this.degreesOfFreedom < 1 && !this.ignoreDofFcheck)
			throw new JPARSECException(
					"Degrees of freedom must be greater than 0");
		double[][] aa = new double[this.nParam][this.nData];

		for (int j = 0; j < nData; j++)
			aa[0][j] = 1.0D;
		for (int j = 0; j < nData; j++)
			aa[1][j] = this.xData[0][j];

		for (int i = 2; i < nParam; i++) {
			for (int j = 0; j < nData; j++) {
				aa[i][j] = Math.pow(this.xData[0][j], i);
			}
		}
		this.best = new double[this.nParam];
		this.bestSd = new double[this.nParam];
		this.tValues = new double[this.nParam];
		this.pValues = new double[this.nParam];
		this.generalLinear(aa);
		if (!this.ignoreDofFcheck)
			this.generalLinearStats(aa);

		return this.getBestEstimates();
	}

	/**
	 * Polynomial fitting
	 * y = a + b.x + c.x^2 + d.x^3 + . . .
	 * where a is fixed.
	 *
	 * @param deg Degree of the polynomial.
	 * @param fixed_a The value of a.
	 * @return A series object representing the data and the fitting, for charting
	 * purposes.
	 * @throws JPARSECException If an error occurs.
	 */
	public ChartSeriesElement polynomialFitSeries(int deg, double fixed_a) throws JPARSECException {
		if (this.multipleY)
			throw new JPARSECException(
					"This method cannot handle multiply dimensioned y arrays");
		if (this.nXarrays > 1)
			throw new JPARSECException(
					"This class will only perform a polynomial regression on a single x array");
		if (deg < 1)
			throw new JPARSECException(
					"Polynomial degree must be greater than zero");

		this.nParam = deg;
		this.degreesOfFreedom = this.nData - this.nParam;
		if (this.degreesOfFreedom < 1 && !this.ignoreDofFcheck)
			throw new JPARSECException(
					"Degrees of freedom must be greater than 0");
		double[][] aa = new double[this.nParam][this.nData];

		for (int j = 0; j < nData; j++)
			this.yData[j] -= fixed_a;
		for (int j = 0; j < nData; j++)
			aa[0][j] = this.xData[0][j];

		for (int i = 1; i < nParam; i++) {
			for (int j = 0; j < nData; j++) {
				aa[i][j] = Math.pow(this.xData[0][j], i + 1);
			}
		}
		this.best = new double[this.nParam];
		this.bestSd = new double[this.nParam];
		this.tValues = new double[this.nParam];
		this.pValues = new double[this.nParam];
		this.generalLinear(aa);
		if (!this.ignoreDofFcheck)
			this.generalLinearStats(aa);
		for (int j = 0; j < nData; j++) {
			this.yData[j] += fixed_a;
			this.yCalc[j] += fixed_a;
		}
		ChartSeriesElement series = new ChartSeriesElement(
				xData[0], yData, null, null,
				"", true, Color.BLACK, ChartSeriesElement.SHAPE_CIRCLE,
				ChartSeriesElement.REGRESSION.POLYNOMIAL);
		series.regressionType.setPolynomialDegree(deg);
		series.regressionType.setEquationValues(this.getBestEstimates(), this.getBestEstimatesErrors());
		return series;
	}
	
	class GenericFunction implements RegressionFunction{
		private String function = "x";
		private Evaluation eval;
		public void setFunction(String f) {
			function = f;
			eval = new Evaluation(function, null);
		}

	    public double function(double[] p, double[] x){
	    	try {
	    		String param[] = new String[p.length];
	    		for (int i=0; i<p.length; i++) {
	    			param[i] = "p"+(i+1)+" "+p[i];
	    		}
		    	eval.resetVariables(DataSet.addStringArray(param, new String[] {"x "+x[0]}));
		        return eval.evaluate();
	    	} catch (Exception exc) {
	    		Logger.log(LEVEL.ERROR, "Could not evaluation function "+function+" for x = "+x[0]+". Returning 0.");
	    		return 0;
	    	}
	    }
	}
}
