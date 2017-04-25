/** This file is part of JPARSEC library.
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
import java.util.ArrayList;

import jparsec.graph.ChartSeriesElement;
import jparsec.graph.DataSet;
import jparsec.math.matrix.Matrix;
import jparsec.util.JPARSECException;
import jparsec.util.Logger;
import jparsec.util.Logger.LEVEL;

/*
 *   Class Regression
 *
 *   Contains methods for simple linear regression
 *   (straight line), for multiple linear regression,
 *   for fitting data to a polynomial and for non-linear
 *   regression (Nelder and Mead Simplex method) for both user
 *   supplied functions and for a wide range of standard functions
 *
 *   The sum of squares function needed by the non-linear regression methods
 *   non-linear regression methods is supplied by means of the interfaces,
 *   RegressionFunction or RegressionFunction2
 *
 *   WRITTEN BY: Dr Michael Thomas Flanagan
 *
 *   DATE:	    February 2002
 *   MODIFIED:   7 January 2006,  28 July 2006, 9 August 2006, 4 November 200621 November 2006, 21 December 2006,
 *               14 April 2007, 9 June 2007, 25 July 2007, 23/24 August 2007, 14 September 2007, 28 December 2007,
 *               18-26 March 2008, 7 April 2008, 27 April 2008, 10/12/19 May 2008, 5-6 July 2004, 28 July 2008,
 *               29 August 2008, 5 September 2008, 6 October 2008, 13-15 October 2009, 13 November 2009, 10 December 2009,
 *               20 December 2009, 12 January 2010, 18-25 May 2010, 9 July 2010, 10-16 August 2010
 *
 *   DOCUMENTATION:
 *   See Michael Thomas Flanagan's Java library on-line web page:
 *   http://www.ee.ucl.ac.uk/~mflanaga/java/Regression.html
 *   http://www.ee.ucl.ac.uk/~mflanaga/java/
 *
 * Copyright (c) 2002 - 2010 Michael Thomas Flanagan
 *
 * PERMISSION TO COPY:
 *
 * Permission to use, copy and modify this software and its documentation for NON-COMMERCIAL purposes is granted, without fee,
 * provided that an acknowledgement to the author, Dr Michael Thomas Flanagan at www.ee.ucl.ac.uk/~mflanaga, appears in all copies
 * and associated documentation or publications.
 *
 * Redistributions of the source code of this source code, or parts of the source codes, must retain the above copyright notice, this list of conditions
 * and the following disclaimer and requires written permission from the Michael Thomas Flanagan:
 *
 * Redistribution in binary form of all or parts of this class must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided with the distribution and requires written permission from the Michael Thomas Flanagan:
 *
 * Dr Michael Thomas Flanagan makes no representations about the suitability or fitness of the software for any or for a particular purpose.
 * Dr Michael Thomas Flanagan shall not be liable for any damages suffered as a result of using, modifying or distributing this software
 * or its derivatives.
 *
 ***************************************************************************************/

/**
 * A class for fitting data to a Gaussian function, a polynomial (Nelder and
 * Mead Simplex method), or simple/multiple linear regression.
 * <P>
 * See Michael Thomas Flanagan's Java library on-line web page:<BR>
 * http://www.ee.ucl.ac.uk/~mflanaga/java/Regression.html<BR>
 * http://www.ee.ucl.ac.uk/~mflanaga/java/
 * <P>
 *
 * @author M. Thomas Flanagan
 * @author T. Alonso Albi - OAN (Spain), just code cleanup
 * @version 1.0
 */
public class Regression {
	private int nData = 0; // number of y data points (nData0 times the number
							// of y arrays)
	private int nXarrays = 1; // number of x arrays
	private int nTerms = 0; // number of unknown parameters to be estimated
	// multiple linear (a + b.x1 +c.x2 + . . ., = nXarrays + 1
	// polynomial fitting; = polynomial degree + 1
	// generalised linear; = nXarrays
	// simplex = no. of parameters to be estimated
	private int degreesOfFreedom = 0; // degrees of freedom = nData - nTerms
	private double[][] xData = null; // x data values
	private double[] yData = null; // y data values
	private double[] yCalc = null; // calculated y values using the regrssion
									// coefficients
	private double[] weight = null; // weighting factors
	private double[] residual = null; // residuals
	private double[] residualW = null; // weighted residuals
	private boolean weightOpt = false; // weighting factor option
	// = true; weights supplied
	// = false; weigths set to unity in regression
	// average error used in statistacal methods
	// if any weight[i] = zero,
	// weighOpt is set to false and
	// all weights set to unity

	private double[] best = null; // best estimates vector of the unknown
									// parameters
	private double[] bestSd = null; // standard deviation estimates of the best
									// estimates of the unknown parameters
	private double[] pseudoSd = null; // Pseudo-nonlinear sd
	private double[] tValues = null; // t-values of the best estimates
	private double[] pValues = null; // p-values of the best estimates

	private double yWeightedMean = Double.NaN; // weighted mean of y data
	private double chiSquare = Double.NaN; // chi square
											// (observed-calculated)^2/variance;
											// weighted error sum of squares
	private double sumOfSquaresError = Double.NaN; // Sum of the squares of the
													// residuals; unweighted
													// error sum of squares
	private double sumOfSquaresTotal = Double.NaN; // Total sum of the squares
	private double sumOfSquaresRegrn = Double.NaN; // Regression sum of the
													// squares

	private double lastSSnoConstraint = 0.0D; // Last sum of the squares of the
												// residuals with no constraint
												// penalty
	private double lastSS = 0.0D;
	private double[][] covar = null; // Covariance matrix
	private double[][] corrCoeff = null; // Correlation coefficient matrix

	private boolean trueFreq = false; // true if xData values are true
										// frequencies, e.g. in a fit to
										// Gaussian
	// false if not
	// if true chiSquarePoisson (see above) is also calculated

	// Non-linear members
	private boolean nlrStatus = true; // Status of non-linear regression on
										// exiting regression method
	// = true - convergence criterion was met
	// = false - convergence criterion not met - current estimates returned
	private int scaleOpt = 0; // if = 0; no scaling of initial estimates
	// if = 1; initial simplex estimates scaled to unity
	// if = 2; initial estimates scaled by user provided values in scale[]
	// (default = 0)
	private double[] scale = null; // values to scale initial estimate (see
									// scaleOpt above)
	private boolean penalty = false; // true if single parameter penalty
										// function is included
	private boolean sumPenalty = false; // true if multiple parameter penalty
										// function is included
	private int nConstraints = 0; // number of single parameter constraints
	private int nSumConstraints = 0; // number of multiple parameter constraints
	private int maxConstraintIndex = -1; // maximum index of constrained
											// parameter/s
	private double constraintTolerance = 1e-4; // tolerance in constraining
												// parameter/s to a fixed value
	private ArrayList<Object> penalties = new ArrayList<Object>(); // constrant
																	// method
																	// index,
	// number of single parameter constraints,
	// then repeated for each constraint:
	// penalty parameter index,
	// below or above constraint flag,
	// constraint boundary value
	private ArrayList<Object> sumPenalties = new ArrayList<Object>(); // constraint
																		// method
																		// index,
	// number of multiple parameter constraints,
	// then repeated for each constraint:
	// number of parameters in summation
	// penalty parameter indices,
	// summation signs
	// below or above constraint flag,
	// constraint boundary value
	private int[] penaltyCheck = null; // = -1 values below the single
										// constraint boundary not allowed
	// = +1 values above the single constraint boundary not allowed
	private int[] sumPenaltyCheck = null; // = -1 values below the multiple
											// constraint boundary not allowed
	// = +1 values above the multiple constraint boundary not allowed
	private double penaltyWeight = 1.0e30; // weight for the penalty functions
	private int[] penaltyParam = null; // indices of paramaters subject to
										// single parameter constraint
	private int[][] sumPenaltyParam = null; // indices of paramaters subject to
											// multiple parameter constraint
	private double[][] sumPlusOrMinus = null; // valueall before each parameter
												// in multiple parameter
												// summation
	private int[] sumPenaltyNumber = null; // number of paramaters in each
											// multiple parameter constraint

	private double[] constraints = null; // single parameter constraint values
	private double[] sumConstraints = null; // multiple parameter constraint
											// values
	private int constraintMethod = 0; // constraint method number
	// =0: cliff to the power two (only method at present)

	private boolean scaleFlag = true; // if true ordinate scale factor, Ao,
										// included as unknown in fitting to
										// special functions
	// if false Ao set to unity (default value) or user provided value (in
	// yScaleFactor)
	private double yScaleFactor = 1.0D; // y axis factor - set if scaleFlag
										// (above) = false
	private int nMax = 3000; // Nelder and Mead simplex maximum number of
								// iterations
	private int nIter = 0; // Nelder and Mead simplex number of iterations
							// performed
	private int konvge = 3; // Nelder and Mead simplex number of restarts
							// allowed
	private double fMin = -1.0D; // Nelder and Mead simplex minimum value
	private double fTol = 1e-9; // Nelder and Mead simplex convergence tolerance
	private double rCoeff = 1.0D; // Nelder and Mead simplex reflection
									// coefficient
	private double eCoeff = 2.0D; // Nelder and Mead simplex extension
									// coefficient
	private double cCoeff = 0.5D; // Nelder and Mead simplex contraction
									// coefficient
	private double[] startH = null; // Nelder and Mead simplex unscaled initial
									// estimates
	private double[] stepH = null; // Nelder and Mead simplex unscaled initial
									// step values
	private double[] startSH = null; // Nelder and Mead simplex scaled initial
										// estimates
	private double[] stepSH = null; // Nelder and Mead simplex scaled initial
									// step values
	private double[][] grad = null; // Non-linear regression gradients
	private double delta = 1e-4; // Fractional step in numerical differentiation
	private boolean invertFlag = true; // Hessian Matrix ('linear' non-linear
										// statistics) check
	// true matrix successfully inverted, false inversion failed
	private boolean posVarFlag = true; // Hessian Matrix ('linear' non-linear
										// statistics) check
	// true - all variances are positive; false - at least one is negative
	private int minTest = 0; // Nelder and Mead minimum test
	// = 0; tests simplex sd < fTol
	// = 1; tests reduced chi suare or sum of squares < mean of abs(y
	// values)*fTol
	private boolean statFlag = true; // if true - statistical method called
	// if false - no statistical analysis
	private boolean multipleY = false; // = true if y variable consists of more
										// than set of data each needing a
										// different calculation in
										// RegressionFunction
	// when set to true - the index of the y value is passed to the function in
	// Regression function

	private boolean ignoreDofFcheck = false; // when set to true, the check on
												// whether degrees of freedom
												// are greater than zero is
												// ignored

	/**
	 * Constructor with data with x as 1D array and no weights (they are set to 1).
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
	 * Constructor with data with x as 2D array and no weights provided (set to 1).
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

	// Fit data to a Gaussian (normal) probability function
	// with option to fix some of the parameters
	// parameter order - mean, sd, scale factor
	private void fitGaussianFixed(double[] initialEstimates, boolean[] fixed,
			int plotFlag) throws JPARSECException {
		if (this.multipleY)
			throw new JPARSECException(
					"This method cannot handle multiply dimensioned y arrays");
		this.scaleFlag = true;
		this.nTerms = 3;
		this.degreesOfFreedom = this.nData - this.nTerms;
		if (this.degreesOfFreedom < 1 && !this.ignoreDofFcheck)
			throw new JPARSECException(
					"Degrees of freedom must be greater than 0");

		// order data into ascending order of the abscissae
		Regression.sort(this.xData[0], this.yData, this.weight);

		// check sign of y data
		Double tempd = null;
		ArrayList<Object> retY = Regression.dataSign(yData);
		tempd = (Double) retY.get(4);
		double yPeak = tempd.doubleValue();
		boolean yFlag = false;
		if (yPeak < 0.0D) {
			for (int i = 0; i < this.nData; i++) {
				yData[i] = -yData[i];
			}
			retY = Regression.dataSign(yData);
			yFlag = true;
		}

		// Create instance of GaussianFunctionFixed
		GaussianFunctionFixed f = new GaussianFunctionFixed();
		f.fixed = fixed;
		f.param = initialEstimates;

		// Determine unknowns
		int nT = this.nTerms;
		for (int i = 0; i < this.nTerms; i++)
			if (fixed[i])
				nT--;
		if (nT == 0) {
			if (plotFlag == 0) {
				throw new JPARSECException(
						"At least one parameter must be available for variation by the Regression procedure or GauasianPlot should have been called and not Gaussian");
			} else {
				plotFlag = 3;
			}
		}

		double[] start = new double[nT];
		double[] step = new double[nT];
		boolean[] constraint = new boolean[nT];

		// Fill arrays needed by the Simplex
		double xMin = DataSet.getMinimumValue(xData[0]);
		double xMax = DataSet.getMaximumValue(xData[0]);
		double yMax = DataSet.getMaximumValue(yData);
		if (initialEstimates[2] == 0.0D) {
			if (fixed[2]) {
				throw new JPARSECException(
						"Scale factor has been fixed at zero");
			} else {
				initialEstimates[2] = yMax;
			}
		}
		int ii = 0;
		for (int i = 0; i < this.nTerms; i++) {
			if (!fixed[i]) {
				start[ii] = initialEstimates[i];
				step[ii] = start[ii] * 0.1D;
				if (step[ii] == 0.0D)
					step[ii] = (xMax - xMin) * 0.1D;
				constraint[ii] = false;
				if (i == 1)
					constraint[ii] = true;
				ii++;
			}
		}
		this.nTerms = nT;

		// Nelder and Mead Simplex Regression
		for (int i = 0; i < this.nTerms; i++) {
			if (constraint[i])
				this.addConstraint(i, -1, 0.0D);
		}
		Object regFun2 = (Object) f;
		if (plotFlag != 3)
			this.nelderMead(regFun2, start, step, this.fTol, this.nMax);

		if (yFlag) {
			// restore data
			for (int i = 0; i < this.nData - 1; i++) {
				this.yData[i] = -this.yData[i];
			}
		}

	}

	// Check entered weights for zeros.
	// If more than 40% are zero or less than zero, all weights replaced by
	// unity
	// If less than 40% are zero or less than zero, the zero or negative weights
	// are replaced by the average of their nearest neighbours
	private double[] checkForZeroWeights(double[] weight) {
		this.weightOpt = true;
		int nZeros = 0;
		int n = weight.length;

		for (int i = 0; i < n; i++)
			if (weight[i] <= 0.0)
				nZeros++;
		double perCentZeros = 100.0 * (double) nZeros / (double) n;
		if (perCentZeros > 40.0) {
			for (int i = 0; i < n; i++)
				weight[i] = 1.0D;
			this.weightOpt = false;
		} else {
			if (perCentZeros > 0.0D) {
				for (int i = 0; i < n; i++) {
					if (weight[i] <= 0.0) {
						if (i == 0) {
							int ii = 1;
							boolean test = true;
							while (test) {
								if (weight[ii] > 0.0D) {
									weight[0] = weight[ii];
									test = false;
								} else {
									ii++;
								}
							}
						}
						if (i == (n - 1)) {
							int ii = n - 2;
							boolean test = true;
							while (test) {
								if (weight[ii] > 0.0D) {
									weight[i] = weight[ii];
									test = false;
								} else {
									ii--;
								}
							}
						}
						if (i > 0 && i < (n - 2)) {
							double lower = 0.0;
							double upper = 0.0;
							int ii = i - 1;
							boolean test = true;
							while (test) {
								if (weight[ii] > 0.0D) {
									lower = weight[ii];
									test = false;
								} else {
									ii--;
									if (ii == 0)
										test = false;
								}
							}
							ii = i + 1;
							test = true;
							while (test) {
								if (weight[ii] > 0.0D) {
									upper = weight[ii];
									test = false;
								} else {
									ii++;
									if (ii == (n - 1))
										test = false;
								}
							}
							if (lower == 0.0) {
								weight[i] = upper;
							} else {
								if (upper == 0.0) {
									weight[i] = lower;
								} else {
									weight[i] = (lower + upper) / 2.0;
								}
							}
						}
					}
				}
			}
		}
		return weight;
	}

	// Set data and default values
	private void setDefaultValues(double[][] xData, double[] yData,
			double[] weight) throws JPARSECException {
		this.nData = yData.length;
		this.nXarrays = xData.length;
		this.nTerms = this.nXarrays;
		this.yData = new double[nData];
		this.yCalc = new double[nData];
		this.weight = new double[nData];
		this.residual = new double[nData];
		this.residualW = new double[nData];
		this.xData = new double[nXarrays][nData];
		int n = weight.length;
		if (n != this.nData)
			throw new JPARSECException(
					"The weight and the y data lengths do not agree");
		for (int i = 0; i < this.nData; i++) {
			this.yData[i] = yData[i];
			this.weight[i] = weight[i];
		}
		for (int j = 0; j < this.nXarrays; j++) {
			n = xData[j].length;
			if (n != this.nData)
				throw new JPARSECException("An x [" + j + "] length " + n
						+ " and the y data length, " + this.nData
						+ ", do not agree");
			for (int i = 0; i < this.nData; i++) {
				this.xData[j][i] = xData[j][i];
			}
		}
	}

	// check data arrays for sign, maximum, minimum and peak
	private static ArrayList<Object> dataSign(double[] data) {

		ArrayList<Object> ret = new ArrayList<Object>();
		int n = data.length;

		double max = data[0]; // maximum
		int maxi = 0; // index of above
		double min = data[0]; // minimum
		int mini = 0; // index of above
		double peak = 0.0D; // peak: larger of maximum and any abs(negative
							// minimum)
		int peaki = -1; // index of above
		int signFlag = -1; // 0 all positive; 1 all negative; 2 positive and
							// negative
		double shift = 0.0D; // shift to make all positive if a mixture of
								// positive and negative
		double mean = 0.0D; // mean value
		int signCheckZero = 0; // number of zero values
		int signCheckNeg = 0; // number of positive values
		int signCheckPos = 0; // number of negative values

		for (int i = 0; i < n; i++) {
			mean = +data[i];
			if (data[i] > max) {
				max = data[i];
				maxi = i;
			}
			if (data[i] < min) {
				min = data[i];
				mini = i;
			}
			if (data[i] == 0.0D)
				signCheckZero++;
			if (data[i] > 0.0D)
				signCheckPos++;
			if (data[i] < 0.0D)
				signCheckNeg++;
		}
		mean /= (double) n;

		if ((signCheckZero + signCheckPos) == n) {
			peak = max;
			peaki = maxi;
			signFlag = 0;
		} else {
			if ((signCheckZero + signCheckNeg) == n) {
				peak = min;
				peaki = mini;
				signFlag = 1;
			} else {
				peak = max;
				peaki = maxi;
				if (-min > max) {
					peak = min;
					peak = mini;
				}
				signFlag = 2;
				shift = -min;
			}
		}

		// transfer results to the ArrayList
		ret.add(new Double(min));
		ret.add(new Integer(mini));
		ret.add(new Double(max));
		ret.add(new Integer(maxi));
		ret.add(new Double(peak));
		ret.add(new Integer(peaki));
		ret.add(new Integer(signFlag));
		ret.add(new Double(shift));
		ret.add(new Double(mean));
		ret.add(new Integer(signCheckZero));
		ret.add(new Integer(signCheckPos));
		ret.add(new Integer(signCheckNeg));

		return ret;
	}

	// sort elements x, y and w arrays of doubles into ascending order of the x
	// array
	// using selection sort method
	private static void sort(double[] x, double[] y, double[] w) {
		int index = 0;
		int lastIndex = -1;
		int n = x.length;
		double holdx = 0.0D;
		double holdy = 0.0D;
		double holdw = 0.0D;

		while (lastIndex < n - 1) {
			index = lastIndex + 1;
			for (int i = lastIndex + 2; i < n; i++) {
				if (x[i] < x[index]) {
					index = i;
				}
			}
			lastIndex++;
			holdx = x[index];
			x[index] = x[lastIndex];
			x[lastIndex] = holdx;
			holdy = y[index];
			y[index] = y[lastIndex];
			y[lastIndex] = holdy;
			holdw = w[index];
			w[index] = w[lastIndex];
			w[lastIndex] = holdw;
		}
	}

	// add a single parameter constraint boundary for the non-linear regression
	private void addConstraint(int paramIndex, int conDir, double constraint) {
		this.penalty = true;

		// First element reserved for method number if other methods than
		// 'cliff' are added later
		if (this.penalties.isEmpty())
			this.penalties.add(new Integer(this.constraintMethod));

		// add constraint
		if (penalties.size() == 1) {
			this.penalties.add(new Integer(1));
		} else {
			int nPC = ((Integer) this.penalties.get(1)).intValue();
			nPC++;
			this.penalties.set(1, new Integer(nPC));
		}
		this.penalties.add(new Integer(paramIndex));
		this.penalties.add(new Integer(conDir));
		this.penalties.add(new Double(constraint));
		if (paramIndex > this.maxConstraintIndex)
			this.maxConstraintIndex = paramIndex;
	}

	// Nelder and Mead Simplex Simplex Non-linear Regression
	private void nelderMead(Object regFun, double[] start, double[] step,
			double fTol, int nMax) throws JPARSECException {
		int np = start.length; // number of unknown parameters;
		if (this.maxConstraintIndex >= np)
			throw new JPARSECException(
					"You have entered more constrained parameters ("
							+ this.maxConstraintIndex
							+ ") than minimisation parameters (" + np + ")");
		this.nlrStatus = true; // -> false if convergence criterion not met
		this.nTerms = np; // number of parameters whose best estimates are to be
							// determined
		int nnp = np + 1; // number of simplex apices
		this.lastSSnoConstraint = 0.0D; // last sum of squares without a penalty
										// constraint being applied

		if (this.scaleOpt < 2)
			this.scale = new double[np]; // scaling factors
		if (scaleOpt == 2 && scale.length != start.length)
			throw new JPARSECException(
					"scale array and initial estimate array are of different lengths");
		if (step.length != start.length)
			throw new JPARSECException("step array length " + step.length
					+ " and initial estimate array length " + start.length
					+ " are of different");

		// check for zero step sizes
		for (int i = 0; i < np; i++)
			if (step[i] == 0.0D)
				throw new JPARSECException("step " + i + " size is zero");

		// set statistic arrays to NaN if degrees of freedom check ignored
		if (this.ignoreDofFcheck) {
			this.bestSd = new double[this.nTerms];
			this.pseudoSd = new double[this.nTerms];
			this.tValues = new double[this.nTerms];
			this.pValues = new double[this.nTerms];

			this.covar = new double[this.nTerms][this.nTerms];
			this.corrCoeff = new double[this.nTerms][this.nTerms];

			for (int i = 0; i < this.nTerms; i++) {
				this.bestSd[i] = Double.NaN;
				this.pseudoSd[i] = Double.NaN;
				for (int j = 0; j < this.nTerms; j++) {
					this.covar[i][j] = Double.NaN;
					this.corrCoeff[i][j] = Double.NaN;
				}
			}
		}

		// set up arrays
		this.startH = new double[np]; // holding array of unscaled initial start
										// values
		this.stepH = new double[np]; // unscaled initial step values
		this.startSH = new double[np]; // holding array of scaled initial start
										// values
		this.stepSH = new double[np]; // scaled initial step values
		double[] pmin = new double[np]; // Nelder and Mead Pmin
		this.best = new double[np]; // best estimates array
		this.bestSd = new double[np]; // sd of best estimates array
		this.tValues = new double[np]; // t-value of best estimates array
		this.pValues = new double[np]; // p-value of best estimates array

		double[][] pp = new double[nnp][nnp]; // Nelder and Mead P
		double[] yy = new double[nnp]; // Nelder and Mead y
		double[] pbar = new double[nnp]; // Nelder and Mead P with bar
											// superscript
		double[] pstar = new double[nnp]; // Nelder and Mead P*
		double[] p2star = new double[nnp]; // Nelder and Mead P**

		// mean of absolute values of yData (for testing for minimum)
		double yabsmean = 0.0D;
		for (int i = 0; i < this.nData; i++)
			yabsmean += Math.abs(yData[i]);
		yabsmean /= this.nData;

		// Set any single parameter constraint parameters
		if (this.penalty) {
			Integer itemp = (Integer) this.penalties.get(1);
			this.nConstraints = itemp.intValue();
			this.penaltyParam = new int[this.nConstraints];
			this.penaltyCheck = new int[this.nConstraints];
			this.constraints = new double[this.nConstraints];
			Double dtemp = null;
			int j = 2;
			for (int i = 0; i < this.nConstraints; i++) {
				itemp = (Integer) this.penalties.get(j);
				this.penaltyParam[i] = itemp.intValue();
				j++;
				itemp = (Integer) this.penalties.get(j);
				this.penaltyCheck[i] = itemp.intValue();
				j++;
				dtemp = (Double) this.penalties.get(j);
				this.constraints[i] = dtemp.doubleValue();
				j++;
			}
		}

		// Set any multiple parameters constraint parameters
		if (this.sumPenalty) {
			Integer itemp = (Integer) this.sumPenalties.get(1);
			this.nSumConstraints = itemp.intValue();
			this.sumPenaltyParam = new int[this.nSumConstraints][];
			this.sumPlusOrMinus = new double[this.nSumConstraints][];
			this.sumPenaltyCheck = new int[this.nSumConstraints];
			this.sumPenaltyNumber = new int[this.nSumConstraints];
			this.sumConstraints = new double[this.nSumConstraints];
			int[] itempArray = null;
			double[] dtempArray = null;
			Double dtemp = null;
			int j = 2;
			for (int i = 0; i < this.nSumConstraints; i++) {
				itemp = (Integer) this.sumPenalties.get(j);
				this.sumPenaltyNumber[i] = itemp.intValue();
				j++;
				itempArray = (int[]) this.sumPenalties.get(j);
				this.sumPenaltyParam[i] = itempArray;
				j++;
				dtempArray = (double[]) this.sumPenalties.get(j);
				this.sumPlusOrMinus[i] = dtempArray;
				j++;
				itemp = (Integer) this.sumPenalties.get(j);
				this.sumPenaltyCheck[i] = itemp.intValue();
				j++;
				dtemp = (Double) this.sumPenalties.get(j);
				this.sumConstraints[i] = dtemp.doubleValue();
				j++;
			}
		}

		// Store unscaled start and step values
		for (int i = 0; i < np; i++) {
			this.startH[i] = start[i];
			this.stepH[i] = step[i];
		}

		// scale initial estimates and step sizes
		if (this.scaleOpt > 0) {
			boolean testzero = false;
			for (int i = 0; i < np; i++)
				if (start[i] == 0.0D)
					testzero = true;
			if (testzero) {
				this.scaleOpt = 0;
			}
		}
		switch (this.scaleOpt) {
		case 0: // No scaling carried out
			for (int i = 0; i < np; i++)
				scale[i] = 1.0D;
			break;
		case 1: // All parameters scaled to unity
			for (int i = 0; i < np; i++) {
				scale[i] = 1.0 / start[i];
				step[i] = step[i] / start[i];
				start[i] = 1.0D;
			}
			break;
		case 2: // Each parameter scaled by a user provided factor
			for (int i = 0; i < np; i++) {
				step[i] *= scale[i];
				start[i] *= scale[i];
			}
			break;
		default:
			throw new JPARSECException("Scaling factor option " + this.scaleOpt
					+ " not recognised");
		}

		// set class member values
		this.fTol = fTol;
		this.nMax = nMax;
		this.nIter = 0;
		for (int i = 0; i < np; i++) {
			this.startSH[i] = start[i];
			this.stepSH[i] = step[i];
			this.scale[i] = scale[i];
		}

		// initial simplex
		double sho = 0.0D;
		for (int i = 0; i < np; ++i) {
			sho = start[i];
			pstar[i] = sho;
			p2star[i] = sho;
			pmin[i] = sho;
		}

		int jcount = this.konvge; // count of number of restarts still available

		for (int i = 0; i < np; ++i) {
			pp[i][nnp - 1] = start[i];
		}
		yy[nnp - 1] = this.sumSquares(regFun, start);
		for (int j = 0; j < np; ++j) {
			start[j] = start[j] + step[j];

			for (int i = 0; i < np; ++i)
				pp[i][j] = start[i];
			yy[j] = this.sumSquares(regFun, start);
			start[j] = start[j] - step[j];
		}

		// loop over allowed number of iterations

		double ynewlo = 0.0D; // current value lowest y
		double ystar = 0.0D; // Nelder and Mead y*
		double y2star = 0.0D; // Nelder and Mead y**
		double ylo = 0.0D; // Nelder and Mead y(low)

		int ilo = 0; // index of lowest apex
		int ihi = 0; // index of highest apex
		int ln = 0; // counter for a check on low and high apices
		boolean test = true; // test becomes false on reaching minimum

		// variables used in calculating the variance of the simplex at a
		// putative minimum
		double curMin = 00D; // sd of the values at the simplex apices
		double sumnm = 0.0D; // for calculating the mean of the apical values
		double zn = 0.0D; // for calculating the summation of their differences
							// from the mean
		double summnm = 0.0D; // for calculating the variance

		while (test) {
			// Determine h
			ylo = yy[0];
			ynewlo = ylo;
			ilo = 0;
			ihi = 0;
			for (int i = 1; i < nnp; ++i) {
				if (yy[i] < ylo) {
					ylo = yy[i];
					ilo = i;
				}
				if (yy[i] > ynewlo) {
					ynewlo = yy[i];
					ihi = i;
				}
			}
			// Calculate pbar
			for (int i = 0; i < np; ++i) {
				zn = 0.0D;
				for (int j = 0; j < nnp; ++j) {
					zn += pp[i][j];
				}
				zn -= pp[i][ihi];
				pbar[i] = zn / np;
			}

			// Calculate p=(1+alpha).pbar-alpha.ph {Reflection}
			for (int i = 0; i < np; ++i)
				pstar[i] = (1.0 + this.rCoeff) * pbar[i] - this.rCoeff
						* pp[i][ihi];

			// Calculate y*
			ystar = this.sumSquares(regFun, pstar);

			++this.nIter;

			// check for y*<yi
			if (ystar < ylo) {
				// Calculate p**=(1+gamma).p*-gamma.pbar {Extension}
				for (int i = 0; i < np; ++i)
					p2star[i] = pstar[i] * (1.0D + this.eCoeff) - this.eCoeff
							* pbar[i];
				// Calculate y**
				y2star = this.sumSquares(regFun, p2star);
				++this.nIter;
				if (y2star < ylo) {
					// Replace ph by p**
					for (int i = 0; i < np; ++i)
						pp[i][ihi] = p2star[i];
					yy[ihi] = y2star;
				} else {
					// Replace ph by p*
					for (int i = 0; i < np; ++i)
						pp[i][ihi] = pstar[i];
					yy[ihi] = ystar;
				}
			} else {
				// Check y*>yi, i!=h
				ln = 0;
				for (int i = 0; i < nnp; ++i)
					if (i != ihi && ystar > yy[i])
						++ln;
				if (ln == np) {
					// y*>= all yi; Check if y*>yh
					if (ystar <= yy[ihi]) {
						// Replace ph by p*
						for (int i = 0; i < np; ++i)
							pp[i][ihi] = pstar[i];
						yy[ihi] = ystar;
					}
					// Calculate p** =beta.ph+(1-beta)pbar {Contraction}
					for (int i = 0; i < np; ++i)
						p2star[i] = this.cCoeff * pp[i][ihi]
								+ (1.0 - this.cCoeff) * pbar[i];
					// Calculate y**
					y2star = this.sumSquares(regFun, p2star);
					++this.nIter;
					// Check if y**>yh
					if (y2star > yy[ihi]) {
						// Replace all pi by (pi+pl)/2

						for (int j = 0; j < nnp; ++j) {
							for (int i = 0; i < np; ++i) {
								pp[i][j] = 0.5 * (pp[i][j] + pp[i][ilo]);
								pmin[i] = pp[i][j];
							}
							yy[j] = this.sumSquares(regFun, pmin);
						}
						this.nIter += nnp;
					} else {
						// Replace ph by p**
						for (int i = 0; i < np; ++i)
							pp[i][ihi] = p2star[i];
						yy[ihi] = y2star;
					}
				} else {
					// replace ph by p*
					for (int i = 0; i < np; ++i)
						pp[i][ihi] = pstar[i];
					yy[ihi] = ystar;
				}
			}

			// test for convergence
			// calculte sd of simplex and determine the minimum point
			sumnm = 0.0;
			ynewlo = yy[0];
			ilo = 0;
			for (int i = 0; i < nnp; ++i) {
				sumnm += yy[i];
				if (ynewlo > yy[i]) {
					ynewlo = yy[i];
					ilo = i;
				}
			}
			sumnm /= (double) (nnp);
			summnm = 0.0;
			for (int i = 0; i < nnp; ++i) {
				zn = yy[i] - sumnm;
				summnm += zn * zn;
			}
			curMin = Math.sqrt(summnm / np);

			// test simplex sd
			switch (this.minTest) {
			case 0: // terminate if the standard deviation of the sum of squares
					// [unweighted data] or of the chi square values [weighted
					// data]
				// at the apices of the simplex is less than the tolerance, fTol
				if (curMin < fTol)
					test = false;
				break;
			case 1: // terminate if the reduced chi square [weighted data] or
					// the reduced sum of squares [unweighted data] at the
					// lowest apex
				// of the simplex is less than the mean of the absolute values
				// of the dependent variable (y values) multiplied by the
				// tolerance, fTol.
				if (Math.sqrt(ynewlo / this.degreesOfFreedom) < yabsmean * fTol)
					test = false;
				break;
			default:
				throw new JPARSECException(
						"Simplex standard deviation test option "
								+ this.minTest + " not recognised");
			}
			this.sumOfSquaresError = ynewlo;
			if (!test) {
				// temporary store of best estimates
				for (int i = 0; i < np; ++i)
					pmin[i] = pp[i][ilo];
				yy[nnp - 1] = ynewlo;
				// store simplex sd
				// test for restart
				--jcount;
				if (jcount > 0) {
					test = true;
					for (int j = 0; j < np; ++j) {
						pmin[j] = pmin[j] + step[j];
						for (int i = 0; i < np; ++i)
							pp[i][j] = pmin[i];
						yy[j] = this.sumSquares(regFun, pmin);
						pmin[j] = pmin[j] - step[j];
					}
				}
			}

			// test for reaching allowed number of iterations
			if (test && this.nIter > this.nMax) {
				this.nlrStatus = false;
				// store current estimates
				for (int i = 0; i < np; ++i)
					pmin[i] = pp[i][ilo];
				yy[nnp - 1] = ynewlo;
				test = false;
			}
		}

		// final store of the best estimates, function value at the minimum and
		// number of restarts
		for (int i = 0; i < np; ++i) {
			pmin[i] = pp[i][ilo];
			this.best[i] = pmin[i] / this.scale[i];
			this.scale[i] = 1.0D; // unscale for statistical methods
		}
		this.fMin = ynewlo;

		// perform statistical analysis if possible and requested
		if (statFlag) {
			if (!this.ignoreDofFcheck)
				pseudoLinearStats(regFun);
		} else {
			for (int i = 0; i < np; ++i) {
				this.bestSd[i] = Double.NaN;
			}
		}
	}

	/**
	 * Sets the maximum number of iterations in the estimation of Nelder and Mead simplex.
	 * Default value is 3000, which could slowdown massive calculations.
	 * @param n The new value.
	 */
	public void setMaximumNumberOfInterationsForNelderAndMeadSimplex(int n) {
		this.nMax = n;
	}

	// Square of a double number
	private double square(double a) {
		return a * a;
	}

	// Calculate the sum of squares of the residuals for non-linear regression
	private double sumSquares(Object regFun, double[] x)
			throws JPARSECException {
	    RegressionFunction g1 = null;
	    RegressionFunction2 g2 = null;
	    if(this.multipleY){
            g2 = (RegressionFunction2)regFun;
        }
        else{
            g1 = (RegressionFunction)regFun;
        }

		double ss = -3.0D;
		double[] param = new double[this.nTerms];
		double[] xd = new double[this.nXarrays];
		// rescale for calcultion of the function
		for (int i = 0; i < this.nTerms; i++)
			param[i] = x[i] / this.scale[i];

		// single parameter penalty functions
		double tempFunctVal = this.lastSSnoConstraint;
		double oneMinusCT = (1.0 - this.constraintTolerance);
		double onePlusCT = (1.0 + this.constraintTolerance);
		boolean test = true;
		if (this.penalty) {
			int k = 0;
			for (int i = 0; i < this.nConstraints; i++) {
				k = this.penaltyParam[i];
				switch (penaltyCheck[i]) {
				case -1: // parameter constrained to lie above a given
							// constraint value
					if (param[k] < constraints[i]) {
						ss = tempFunctVal + this.penaltyWeight * square(constraints[i] - param[k]);
						test = false;
					}
					break;
				case 0: // parameter constrained to lie within a given tolerance
						// about a constraint value
					if (param[k] < constraints[i] * oneMinusCT) {
						ss = tempFunctVal + this.penaltyWeight * square(constraints[i] * oneMinusCT - param[k]);
						test = false;
					}
					if (param[k] > constraints[i] * onePlusCT) {
						ss = tempFunctVal + this.penaltyWeight * square(param[k] - constraints[i] * onePlusCT);
						test = false;
					}
					break;
				case 1: // parameter constrained to lie below a given constraint
						// value
					if (param[k] > constraints[i]) {
						ss = tempFunctVal + this.penaltyWeight * square(param[k] - constraints[i]);
						test = false;
					}
					break;
				default:
					throw new JPARSECException("The " + i + "th penalty check "
							+ penaltyCheck[i] + " not recognised");

				}
			}
		}

		// multiple parameter penalty functions
		if (this.sumPenalty) {
			int kk = 0;
			double pSign = 0;
			for (int i = 0; i < this.nSumConstraints; i++) {
				double sumPenaltySum = 0.0D;
				for (int j = 0; j < this.sumPenaltyNumber[i]; j++) {
					kk = this.sumPenaltyParam[i][j];
					pSign = this.sumPlusOrMinus[i][j];
					sumPenaltySum += param[kk] * pSign;
				}
				switch (this.sumPenaltyCheck[i]) {
				case -1: // designated 'parameter sum' constrained to lie above
							// a given constraint value
					if (sumPenaltySum < sumConstraints[i]) {
						ss = tempFunctVal + this.penaltyWeight * square(sumConstraints[i] - sumPenaltySum);
						test = false;
					}
					break;
				case 0: // designated 'parameter sum' constrained to lie within
						// a given tolerance about a given constraint value
					if (sumPenaltySum < sumConstraints[i] * oneMinusCT) {
						ss = tempFunctVal + this.penaltyWeight * square(sumConstraints[i] * oneMinusCT - sumPenaltySum);
						test = false;
					}
					if (sumPenaltySum > sumConstraints[i] * onePlusCT) {
						ss = tempFunctVal + this.penaltyWeight * square(sumPenaltySum - sumConstraints[i] * onePlusCT);
						test = false;
					}
					break;
				case 1: // designated 'parameter sum' constrained to lie below a
						// given constraint value
					if (sumPenaltySum > sumConstraints[i]) {
						ss = tempFunctVal + this.penaltyWeight * square(sumPenaltySum - sumConstraints[i]);
						test = false;
					}
					break;
				default:
					throw new JPARSECException("The " + i + "th summation penalty check " + sumPenaltyCheck[i] + " not recognised");
				}
			}
		}

		// call function calculation and calculate the sum of squares if
		// constraints have not intervened
		if (test) {
			ss = 0.0D;
			for (int i = 0; i < this.nData; i++) {
				for (int j = 0; j < nXarrays; j++)
					xd[j] = this.xData[j][i];
				if (!this.multipleY) {
					ss += square((this.yData[i] - g1.function(param, xd)) / this.weight[i]);
				} else {
					ss += square((this.yData[i] - g2.function(param, xd, i)) / this.weight[i]);
				}

			}
			this.lastSSnoConstraint = ss;

		}

		lastSS = ss;

		// return sum of squares
		return ss;
	}

	/**
	 * Get the best estimates of the unknown parameters.
	 * @return The best estimates.
	 */
	public double[] getBestEstimates() {
		return best.clone();
	}

	/**
	 * Get the best estimates of the errors in the unknown parameters.
	 * @return The best estimates of the errors.
	 */
	public double[] getBestEstimatesErrors() {
		return bestSd.clone();
	}

	// linear statistics applied to a non-linear regression
	private int pseudoLinearStats(Object regFun) throws JPARSECException {
		double f1 = 0.0D, f2 = 0.0D, f3 = 0.0D, f4 = 0.0D; // intermdiate values
															// in numerical
															// differentiation
		int flag = 0; // returned as 0 if method fully successful;
		// negative if partially successful or unsuccessful: check posVarFlag
		// and invertFlag
		// -1 posVarFlag or invertFlag is false;
		// -2 posVarFlag and invertFlag are false
		int np = this.nTerms;

		double[] f = new double[np];
		double[] pmin = new double[np];
		double[] coeffSd = new double[np];
		double[] xd = new double[this.nXarrays];
		double[][] stat = new double[np][np];
		pseudoSd = new double[np];

		this.grad = new double[np][2];
		this.covar = new double[np][np];
		this.corrCoeff = new double[np][np];

		// get best estimates
		pmin = best.clone();

		// gradient both sides of the minimum
		double hold0 = 1.0D;
		double hold1 = 1.0D;
		for (int i = 0; i < np; ++i) {
			for (int k = 0; k < np; ++k) {
				f[k] = pmin[k];
			}
			hold0 = pmin[i];
			if (hold0 == 0.0D) {
				hold0 = this.stepH[i];
			}
			f[i] = hold0 * (1.0D - this.delta);
			this.lastSSnoConstraint = this.sumOfSquaresError;
			f1 = sumSquares(regFun, f);
			f[i] = hold0 * (1.0 + this.delta);
			this.lastSSnoConstraint = this.sumOfSquaresError;
			f2 = sumSquares(regFun, f);
			this.grad[i][0] = (this.fMin - f1) / Math.abs(this.delta * hold0);
			this.grad[i][1] = (f2 - this.fMin) / Math.abs(this.delta * hold0);
		}

		// second patial derivatives at the minimum
		this.lastSSnoConstraint = this.sumOfSquaresError;
		for (int i = 0; i < np; ++i) {
			for (int j = 0; j < np; ++j) {
				for (int k = 0; k < np; ++k) {
					f[k] = pmin[k];
				}
				hold0 = f[i];
				if (hold0 == 0.0D) {
					hold0 = this.stepH[i];
				}
				f[i] = hold0 * (1.0 + this.delta / 2.0D);
				hold0 = f[j];
				if (hold0 == 0.0D) {
					hold0 = this.stepH[j];
				}
				f[j] = hold0 * (1.0 + this.delta / 2.0D);
				this.lastSSnoConstraint = this.sumOfSquaresError;
				f1 = sumSquares(regFun, f);
				f[i] = pmin[i];
				f[j] = pmin[j];
				hold0 = f[i];
				if (hold0 == 0.0D) {
					hold0 = this.stepH[i];
				}
				f[i] = hold0 * (1.0 - this.delta / 2.0D);
				hold0 = f[j];
				if (hold0 == 0.0D) {
					hold0 = this.stepH[j];
				}
				f[j] = hold0 * (1.0 + this.delta / 2.0D);
				this.lastSSnoConstraint = this.sumOfSquaresError;
				f2 = sumSquares(regFun, f);
				f[i] = pmin[i];
				f[j] = pmin[j];
				hold0 = f[i];
				if (hold0 == 0.0D) {
					hold0 = this.stepH[i];
				}
				f[i] = hold0 * (1.0 + this.delta / 2.0D);
				hold0 = f[j];
				if (hold0 == 0.0D) {
					hold0 = this.stepH[j];
				}
				f[j] = hold0 * (1.0 - this.delta / 2.0D);
				this.lastSSnoConstraint = this.sumOfSquaresError;
				f3 = sumSquares(regFun, f);
				f[i] = pmin[i];
				f[j] = pmin[j];
				hold0 = f[i];
				if (hold0 == 0.0D) {
					hold0 = this.stepH[i];
				}
				f[i] = hold0 * (1.0 - this.delta / 2.0D);
				hold0 = f[j];
				if (hold0 == 0.0D) {
					hold0 = this.stepH[j];
				}
				f[j] = hold0 * (1.0 - this.delta / 2.0D);
				this.lastSSnoConstraint = this.sumOfSquaresError;
				f4 = sumSquares(regFun, f);
				stat[i][j] = (f1 - f2 - f3 + f4) / (this.delta * this.delta);
			}
		}

		double ss = 0.0D;
		double sc = 0.0D;
		for (int i = 0; i < this.nData; i++) {
			for (int j = 0; j < nXarrays; j++)
				xd[j] = this.xData[j][i];
			if (this.multipleY) {
				this.yCalc[i] = ((RegressionFunction2) regFun).function(pmin, xd, i);
			} else {
				this.yCalc[i] = ((RegressionFunction) regFun).function(pmin, xd);
			}
			this.residual[i] = this.yCalc[i] - this.yData[i];
			ss += square(this.residual[i]);
			this.residualW[i] = this.residual[i] / this.weight[i];
			sc += square(this.residualW[i]);
		}
		this.sumOfSquaresError = ss;
		this.chiSquare = sc;

		// calculate reduced sum of squares
		double red = 1.0D;
		if (!this.weightOpt && !this.trueFreq)
		// calculate pseudo errors - reduced sum of squares over second partial
		// derivative
		for (int i = 0; i < np; i++) {
			pseudoSd[i] = (2.0D * delta * red * Math.abs(pmin[i]))
					/ (grad[i][1] - grad[i][0]);
			if (pseudoSd[i] >= 0.0D) {
				pseudoSd[i] = Math.sqrt(pseudoSd[i]);
			} else {
				pseudoSd[i] = Double.NaN;
			}
		}

		// calculate covariance matrix
		if (np == 1) {
			hold0 = pmin[0];
			if (hold0 == 0.0D)
				hold0 = stepH[0];
			stat[0][0] = 1.0D / stat[0][0];
			covar[0][0] = stat[0][0] * red * hold0 * hold0;
			if (covar[0][0] >= 0.0D) {
				coeffSd[0] = Math.sqrt(covar[0][0]);
				corrCoeff[0][0] = 1.0D;
			} else {
				coeffSd[0] = Double.NaN;
				corrCoeff[0][0] = Double.NaN;
				posVarFlag = false;
			}
		} else {
			Matrix cov = new Matrix(stat);
			double determinant = cov.getDeterminant();
			if (determinant == 0) {
				invertFlag = false;
			} else {
				cov = cov.inverse();
				invertFlag = true; // cov.getMatrixCheck();
			}
			if (invertFlag == false) flag--;
			stat = cov.getArrayCopy();

			posVarFlag = true;
			if (invertFlag) {
				for (int i = 0; i < np; ++i) {
					hold0 = pmin[i];
					if (hold0 == 0.0D)
						hold0 = stepH[i];
					for (int j = i; j < np; ++j) {
						hold1 = pmin[j];
						if (hold1 == 0.0D)
							hold1 = stepH[j];
						covar[i][j] = 2.0D * stat[i][j] * red * hold0
								* hold1;
						covar[j][i] = covar[i][j];
					}
					if (covar[i][i] >= 0.0D) {
						coeffSd[i] = Math.sqrt(covar[i][i]);
					} else {
						coeffSd[i] = Double.NaN;
						posVarFlag = false;
					}
				}

				for (int i = 0; i < np; ++i) {
					for (int j = 0; j < np; ++j) {
						if ((coeffSd[i] != Double.NaN)
								&& (coeffSd[j] != Double.NaN)) {
							corrCoeff[i][j] = covar[i][j]
									/ (coeffSd[i] * coeffSd[j]);
						} else {
							corrCoeff[i][j] = Double.NaN;
						}
					}
				}
			} else {
				for (int i = 0; i < np; ++i) {
					for (int j = 0; j < np; ++j) {
						covar[i][j] = Double.NaN;
						corrCoeff[i][j] = Double.NaN;
					}
					coeffSd[i] = Double.NaN;
				}
			}
		}
		if (posVarFlag == false) flag--;

		for (int i = 0; i < nTerms; i++) {
			bestSd[i] = coeffSd[i];
			tValues[i] = best[i] / bestSd[i];
			double atv = Math.abs(tValues[i]);
			if (isNaN(atv)) {
				pValues[i] = Double.NaN;
			} else {
				pValues[i] = 1.0 - studentTcdf(-atv, atv, degreesOfFreedom);
			}
		}

		// Coefficient of determination
		yWeightedMean = mean(yData, weight);

		sumOfSquaresTotal = 0.0;
		for (int i = 0; i < nData; i++) {
			sumOfSquaresTotal += square((yData[i] - yWeightedMean) / weight[i]);
		}

		sumOfSquaresRegrn = sumOfSquaresTotal - chiSquare;

		return flag;
	}

	// Regularised Incomplete Beta function
	// Continued Fraction approximation (see Numerical recipies for details of
	// method)
	private static double regularisedBetaFunction(double z, double w, double x)
			throws JPARSECException {
		if (x < 0.0D || x > 1.0D)
			throw new JPARSECException("Argument x, " + x
					+ ", must be lie between 0 and 1 (inclusive)");
		double ibeta = 0.0D;
		if (x == 0.0D) {
			ibeta = 0.0D;
		} else {
			if (x == 1.0D) {
				ibeta = 1.0D;
			} else {
				// Term before continued fraction
				ibeta = Math.exp(logGamma(z + w) - logGamma(z) - logGamma(w)
						+ z * Math.log(x) + w * Math.log(1.0D - x));
				// Continued fraction
				if (x < (z + 1.0D) / (z + w + 2.0D)) {
					ibeta = ibeta * contFract(z, w, x) / z;
				} else {
					// Use symmetry relationship
					ibeta = 1.0D - ibeta * contFract(w, z, 1.0D - x) / w;
				}
			}
		}
		return ibeta;
	}

	// GAMMA FUNCTIONS
	// Lanczos Gamma Function approximation - N (number of coefficients -1)
	private static int lgfN = 6;
	// Lanczos Gamma Function approximation - Coefficients
	private static double[] lgfCoeff = { 1.000000000190015, 76.18009172947146,
			-86.50532032941677, 24.01409824083091, -1.231739572450155,
			0.1208650973866179E-2, -0.5395239384953E-5 };
	// Lanczos Gamma Function approximation - small gamma
	private static double lgfGamma = 5.0;
	// maximum number of iterations allowed in the contFract method
	private static int cfMaxIter = 500;
	// A small number close to the smallest representable floating point number
	private static final double FPMIN = 1e-300;
	// tolerance used in the contFract method
	private static double cfTol = 1.0e-8;

	// Incomplete fraction summation used in the method regularisedBetaFunction
	// modified Lentz's method
	private static double contFract(double a, double b, double x) {

		double aplusb = a + b;
		double aplus1 = a + 1.0D;
		double aminus1 = a - 1.0D;
		double c = 1.0D;
		double d = 1.0D - aplusb * x / aplus1;
		if (Math.abs(d) < FPMIN)
			d = FPMIN;
		d = 1.0D / d;
		double h = d;
		double aa = 0.0D;
		double del = 0.0D;
		int i = 1, i2 = 0;
		boolean test = true;
		while (test) {
			i2 = 2 * i;
			aa = i * (b - i) * x / ((aminus1 + i2) * (a + i2));
			d = 1.0D + aa * d;
			if (Math.abs(d) < FPMIN)
				d = FPMIN;
			c = 1.0D + aa / c;
			if (Math.abs(c) < FPMIN)
				c = FPMIN;
			d = 1.0D / d;
			h *= d * c;
			aa = -(a + i) * (aplusb + i) * x / ((a + i2) * (aplus1 + i2));
			d = 1.0D + aa * d;
			if (Math.abs(d) < FPMIN)
				d = FPMIN;
			c = 1.0D + aa / c;
			if (Math.abs(c) < FPMIN)
				c = FPMIN;
			d = 1.0D / d;
			del = d * c;
			h *= del;
			i++;
			if (Math.abs(del - 1.0D) < cfTol)
				test = false;
			if (i > cfMaxIter) {
				test = false;
			}
		}
		return h;

	}

	// log to base e of the Gamma function
	// Lanczos approximation (6 terms)
	// Retained for backward compatibility
	private static double logGamma(double x) throws JPARSECException {
		double xcopy = x;
		double fg = 0.0D;
		double first = x + lgfGamma + 0.5;
		double second = lgfCoeff[0];

		if (x >= 0.0) {
			if (x >= 1.0 && x - (int) x == 0.0) {
				fg = logFactorial(x) - Math.log(x);
			} else {
				first -= (x + 0.5) * Math.log(first);
				for (int i = 1; i <= lgfN; i++)
					second += lgfCoeff[i] / ++xcopy;
				fg = Math.log(Math.sqrt(2.0 * Math.PI) * second / x) - first;
			}
		} else {
			fg = Math.PI / (gamma(1.0D - x) * Math.sin(Math.PI * x));

			if (fg != 1.0 / 0.0 && fg != -1.0 / 0.0) {
				if (fg < 0) {
					throw new JPARSECException("The gamma function is negative");
				} else {
					fg = Math.log(fg);
				}
			}
		}
		return fg;
	}

	// Gamma function
	// Lanczos approximation (6 terms)
	// retained for backward compatibity
	private static double gamma(double x) throws JPARSECException {

		double xcopy = x;
		double first = x + lgfGamma + 0.5;
		double second = lgfCoeff[0];
		double fg = 0.0D;

		if (x >= 0.0) {
			if (x >= 1.0D && x - (int) x == 0.0D) {
				fg = factorial(x) / x;
			} else {
				first = Math.pow(first, x + 0.5) * Math.exp(-first);
				for (int i = 1; i <= lgfN; i++)
					second += lgfCoeff[i] / ++xcopy;
				fg = first * Math.sqrt(2.0 * Math.PI) * second / x;
			}
		} else {
			fg = -Math.PI / (x * gamma(-x) * Math.sin(Math.PI * x));
		}
		return fg;
	}

	// factorial of n
	// Argument is of type double but must be, numerically, an integer
	// factorial returned as double but is, numerically, should be an integer
	// numerical rounding may makes this an approximation after n = 21
	private static double factorial(double n) throws JPARSECException {
		if (n < 0 || (n - Math.floor(n)) != 0)
			throw new JPARSECException(
					"n must be a positive integer\nIs a Gamma funtion [Fmath.gamma(x)] more appropriate?");
		double f = 1.0D;
		double iCount = 2.0D;
		while (iCount <= n) {
			f *= iCount;
			iCount += 1.0D;
		}
		return f;
	}

	// log to base e of the factorial of n
	// Argument is of type double but must be, numerically, an integer
	// log[e](factorial) returned as double
	// numerical rounding may makes this an approximation
	private static double logFactorial(double n) throws JPARSECException {
		if (n < 0 || (n - Math.floor(n)) != 0)
			throw new JPARSECException(
					"\nn must be a positive integer\nIs a Gamma funtion [Fmath.gamma(x)] more appropriate?");
		double f = 0.0D;
		double iCount = 2.0D;
		while (iCount <= n) {
			f += Math.log(iCount);
			iCount += 1.0D;
		}
		return f;
	}

	// Fit data to a Gaussian (normal) probability function
	private void fitGaussian(int plotFlag) throws JPARSECException {
		if (multipleY)
			throw new JPARSECException(
					"This method cannot handle multiply dimensioned y arrays");
		nTerms = 3;
		if (!scaleFlag) nTerms = 2;
		degreesOfFreedom = nData - nTerms;
		if (degreesOfFreedom < 1 && !ignoreDofFcheck)
			throw new JPARSECException(
					"Degrees of freedom must be greater than 0");

		// order data into ascending order of the abscissae
		Regression.sort(xData[0], yData, weight);

		// check sign of y data
		Double tempd = null;
		ArrayList<Object> retY = Regression.dataSign(yData);
		tempd = (Double) retY.get(4);
		double yPeak = tempd.doubleValue();
		boolean yFlag = false;
		if (yPeak < 0.0D) {
			for (int i = 0; i < nData; i++) {
				yData[i] = -yData[i];
			}
			retY = Regression.dataSign(yData);
			yFlag = true;
		}

		// Calculate x value at peak y (estimate of the Gaussian mean)
		ArrayList<Object> ret1 = Regression.dataSign(yData);
		Integer tempi = null;
		tempi = (Integer) ret1.get(5);
		int peaki = tempi.intValue();
		double mean = xData[0][peaki];

		// Calculate an estimate of the sd
		double sd = Math.sqrt(2.0D) * halfWidth(xData[0], yData);

		// Calculate estimate of y scale
		tempd = (Double) ret1.get(4);
		double ym = tempd.doubleValue();
		ym = ym * sd * Math.sqrt(2.0D * Math.PI);

		// Fill arrays needed by the Simplex
		double[] start = new double[nTerms];
		double[] step = new double[nTerms];
		start[0] = mean;
		start[1] = sd;
		if (scaleFlag) {
			start[2] = ym;
		}
		step[0] = 0.1D * sd;
		step[1] = 0.1D * start[1];
		if (step[1] == 0.0D) {
			ArrayList<Object> ret0 = Regression.dataSign(xData[0]);
			Double tempdd = null;
			tempdd = (Double) ret0.get(2);
			double xmax = tempdd.doubleValue();
			if (xmax == 0.0D) {
				tempdd = (Double) ret0.get(0);
				xmax = tempdd.doubleValue();
			}
			step[0] = xmax * 0.1D;
		}
		if (scaleFlag)
			step[2] = 0.1D * start[1];

		// Nelder and Mead Simplex Regression
		GaussianFunction f = new GaussianFunction();
		addConstraint(1, -1, 0.0D);
		f.scaleOption = scaleFlag;
		f.scaleFactor = yScaleFactor;

		Object regFun2 = (Object) f;
		nelderMead(regFun2, start, step, fTol, this.nMax);

		if (yFlag) {
			// restore data
			for (int i = 0; i < this.nData - 1; i++) {
				this.yData[i] = -this.yData[i];
			}
		}

	}

	// returns rough estimate of half-height width
	private static double halfWidth(double[] xData, double[] yData) {
		// Find index of maximum value and calculate half maximum height
		double ymax = yData[0];
		int imax = 0;
		int n = xData.length;

		for (int i = 1; i < n; i++) {
			if (yData[i] > ymax) {
				ymax = yData[i];
				imax = i;
			}
		}
		ymax /= 2.0D;

		// Find index of point at half maximum value on the low side of the
		// maximum
		double halfXlow = -1.0D;
		double halfYlow = -1.0D;
		double temp = -1.0D;
		int ihl = -1;
		if (imax > 0) {
			ihl = imax - 1;
			halfYlow = Math.abs(ymax - yData[ihl]);
			for (int i = imax - 2; i >= 0; i--) {
				temp = Math.abs(ymax - yData[i]);
				if (temp < halfYlow) {
					halfYlow = temp;
					ihl = i;
				}
			}
			halfXlow = Math.abs(xData[ihl] - xData[imax]);
		}

		// Find index of point at half maximum value on the high side of the
		// maximum
		double halfXhigh = -1.0D;
		double halfYhigh = -1.0D;
		temp = -1.0D;
		int ihh = -1;
		if (imax < n - 1) {
			ihh = imax + 1;
			halfYhigh = Math.abs(ymax - yData[ihh]);
			for (int i = imax + 2; i < n; i++) {
				temp = Math.abs(ymax - yData[i]);
				if (temp < halfYhigh) {
					halfYhigh = temp;
					ihh = i;
				}
			}
			halfXhigh = Math.abs(xData[ihh] - xData[imax]);
		}

		// Calculate width at half height
		double halfw = 0.0D;
		if (ihl != -1)
			halfw += halfXlow;
		if (ihh != -1)
			halfw += halfXhigh;

		return halfw;
	}

	// WEIGHTED ARITHMETIC MEANS (STATIC)
	// Weighted arithmetic mean of a 1D array of doubles, aa
	private static double mean(double[] aa, double[] ww)
			throws JPARSECException {
		int n = aa.length;
		if (n != ww.length)
			throw new JPARSECException("length of variable array, " + n
					+ " and length of weight array, " + ww.length
					+ " are different");
		double[] weight = ww.clone();
		if (weightingOptionS) {
			DoubleVector am = new DoubleVector(ww);
			am = am.square();
			am = am.invert();
			weight = am.getArray();
		}
		double sumx = 0.0D;
		double sumw = 0.0D;
		for (int i = 0; i < n; i++) {
			sumx += aa[i] * weight[i];
			sumw += weight[i];
		}
		return sumx / sumw;
	}

	private static boolean weightingOptionS = true; // = true 'little w' weights
													// (uncertainties) used

	// Returns the Student's t cumulative distribution function probability
	private static double studentTcdf(double tValue, int df)
			throws JPARSECException {
		if (isNaN(tValue))
			throw new JPARSECException("argument tValue is not a number (NaN)");

		if (tValue == Double.POSITIVE_INFINITY) {
			return 1.0;
		} else {
			if (tValue == Double.NEGATIVE_INFINITY) {
				return 0.0;
			} else {
				double ddf = (double) df;
				double x = ddf / (ddf + tValue * tValue);
				return 0.5D * (1.0D + (regularisedBetaFunction(ddf / 2.0D,
						0.5D, 1) - regularisedBetaFunction(ddf / 2.0D, 0.5D, x))
						* sign(tValue));
			}
		}
	}

	// Returns the Student's t cumulative distribution function probability
	private static double studentTcdf(double tValueLower, double tValueUpper,
			int df) throws JPARSECException {
		if (isNaN(tValueLower))
			throw new JPARSECException(
					"argument tLowerValue is not a number (NaN)");
		if (isNaN(tValueUpper))
			throw new JPARSECException(
					"argument tUpperValue is not a number (NaN)");
		if (tValueUpper == Double.POSITIVE_INFINITY) {
			if (tValueLower == Double.NEGATIVE_INFINITY) {
				return 1.0;
			} else {
				if (tValueLower == Double.POSITIVE_INFINITY) {
					return 0.0;
				} else {
					return (1.0 - studentTcdf(tValueLower, df));
				}
			}
		} else {
			if (tValueLower == Double.NEGATIVE_INFINITY) {
				if (tValueUpper == Double.NEGATIVE_INFINITY) {
					return 0.0;
				} else {
					return studentTcdf(tValueUpper, df);
				}
			} else {
				return studentTcdf(tValueUpper, df)
						- studentTcdf(tValueLower, df);
			}
		}
	}

	// SIGN: returns -1 if x < 0 else returns 1
	private static double sign(double x) {
		if (x < 0.0) {
			return -1.0;
		} else {
			return 1.0;
		}
	}


	// Fit data to a Gaussian (normal) probability function
	private void fitCustom(String function, double start[], int plotFlag) throws JPARSECException {
		if (multipleY)
			throw new JPARSECException(
					"This method cannot handle multiply dimensioned y arrays");
		nTerms = start.length;
		degreesOfFreedom = nData - nTerms;
		if (degreesOfFreedom < 1 && !ignoreDofFcheck)
			throw new JPARSECException(
					"Degrees of freedom must be greater than 0");

		// order data into ascending order of the abscissae
		Regression.sort(xData[0], yData, weight);

		// Fill arrays needed by the Simplex
		double[] step = new double[nTerms];
		for (int i=0; i<step.length; i++) {
			step[i] = 0.1D * Math.abs(start[i]);
		}

		// Nelder and Mead Simplex Regression
		GenericFunction f = new GenericFunction();
		f.setFunction(function);
		//addConstraint(1, -1, 0.0D);

		Object regFun2 = (Object) f;
		nelderMead(regFun2, start, step, fTol, this.nMax);
	}

	/**
	 * Returns the rms of the fit.
	 * @return The sum of the squares of the differences between the fitting value and the input value
	 * divided by the weight applied to each point.
	 */
	public double getRMS() {
		return this.lastSS;
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
	 * Fits the input data to a Gaussian.
	 * @throws JPARSECException If an error occurs.
	 */
	public void gaussian() throws JPARSECException {
		this.fitGaussian(0);
	}

	/**
	 * Fits data to a Gaussian (normal) probability function
	 * with option to fix some of the parameters. Pparameter
	 * order is mean, sd, and yscale in the function:
	 * (yscale/sd.sqrt(2.pi)).exp(-0.5[(x - xmean)/sd]^2).
	 *
	 * @param initialEstimates Initial estimates of the three mentioned values.
	 * @param fixed True or false to fix each of them.
	 * @throws JPARSECException If an error occurs.
	 */
	public void gaussian(double[] initialEstimates, boolean[] fixed)
			throws JPARSECException {
		this.fitGaussianFixed(initialEstimates, fixed, 0);
	}

	/**
	 * Generalised linear regression
	 * y = a.f1(x) + b.f2(x) + c.f3(x) + . . .
	 * for a set of x input arrays.
	 *
	 * @throws JPARSECException If an error occurs.
	 */
	public void linearGeneral() throws JPARSECException {
		if (this.multipleY)
			throw new JPARSECException(
					"This method cannot handle multiply dimensioned y arrays");

		this.nTerms = this.nXarrays;
		this.degreesOfFreedom = this.nData - this.nTerms;
		if (this.degreesOfFreedom < 1 && !this.ignoreDofFcheck)
			throw new IllegalArgumentException(
					"Degrees of freedom must be greater than 0");
		this.best = new double[this.nTerms];
		this.bestSd = new double[this.nTerms];
		this.tValues = new double[this.nTerms];
		this.pValues = new double[this.nTerms];
		this.generalLinear(this.xData);
		if (!this.ignoreDofFcheck)
			this.generalLinearStats(this.xData);
	}

	/**
	 * Multiple linear regression with intercept (including y = ax + b)
	 * y = a + b.x1 + c.x2 + d.x3 + . . .
	 * @throws JPARSECException If an error occurs.
	 */
	public void linear() throws JPARSECException {
		if (this.multipleY)
			throw new JPARSECException(
					"This method cannot handle multiply dimensioned y arrays");
		this.nTerms = this.nXarrays + 1;
		this.degreesOfFreedom = this.nData - this.nTerms;
		if (this.degreesOfFreedom < 1 && !this.ignoreDofFcheck)
			throw new JPARSECException(
					"Degrees of freedom must be greater than 0");
		double[][] aa = new double[this.nTerms][this.nData];

		for (int j = 0; j < nData; j++)
			aa[0][j] = 1.0D;
		for (int i = 1; i < nTerms; i++) {
			for (int j = 0; j < nData; j++) {
				aa[i][j] = this.xData[i - 1][j];
			}
		}
		this.best = new double[this.nTerms];
		this.bestSd = new double[this.nTerms];
		this.tValues = new double[this.nTerms];
		this.pValues = new double[this.nTerms];
		this.generalLinear(aa);
		if (!this.ignoreDofFcheck)
			this.generalLinearStats(aa);
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
	public ChartSeriesElement polynomial(int deg) throws JPARSECException {
		if (this.multipleY)
			throw new JPARSECException(
					"This method cannot handle multiply dimensioned y arrays");
		if (this.nXarrays > 1)
			throw new JPARSECException(
					"This class will only perform a polynomial regression on a single x array");
		if (deg < 1)
			throw new JPARSECException(
					"Polynomial degree must be greater than zero");

		this.nTerms = deg + 1;
		this.degreesOfFreedom = this.nData - this.nTerms;
		if (this.degreesOfFreedom < 1 && !this.ignoreDofFcheck)
			throw new JPARSECException(
					"Degrees of freedom must be greater than 0");
		double[][] aa = new double[this.nTerms][this.nData];

		for (int j = 0; j < nData; j++)
			aa[0][j] = 1.0D;
		for (int j = 0; j < nData; j++)
			aa[1][j] = this.xData[0][j];

		for (int i = 2; i < nTerms; i++) {
			for (int j = 0; j < nData; j++) {
				aa[i][j] = Math.pow(this.xData[0][j], i);
			}
		}
		this.best = new double[this.nTerms];
		this.bestSd = new double[this.nTerms];
		this.tValues = new double[this.nTerms];
		this.pValues = new double[this.nTerms];
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

		this.nTerms = deg + 1;
		this.degreesOfFreedom = this.nData - this.nTerms;
		if (this.degreesOfFreedom < 1 && !this.ignoreDofFcheck)
			throw new JPARSECException(
					"Degrees of freedom must be greater than 0");
		double[][] aa = new double[this.nTerms][this.nData];

		for (int j = 0; j < nData; j++)
			aa[0][j] = 1.0D;
		for (int j = 0; j < nData; j++)
			aa[1][j] = this.xData[0][j];

		for (int i = 2; i < nTerms; i++) {
			for (int j = 0; j < nData; j++) {
				aa[i][j] = Math.pow(this.xData[0][j], i);
			}
		}
		this.best = new double[this.nTerms];
		this.bestSd = new double[this.nTerms];
		this.tValues = new double[this.nTerms];
		this.pValues = new double[this.nTerms];
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
	public ChartSeriesElement polynomial(int deg, double fixed_a) throws JPARSECException {
		if (this.multipleY)
			throw new JPARSECException(
					"This method cannot handle multiply dimensioned y arrays");
		if (this.nXarrays > 1)
			throw new JPARSECException(
					"This class will only perform a polynomial regression on a single x array");
		if (deg < 1)
			throw new JPARSECException(
					"Polynomial degree must be greater than zero");

		this.nTerms = deg;
		this.degreesOfFreedom = this.nData - this.nTerms;
		if (this.degreesOfFreedom < 1 && !this.ignoreDofFcheck)
			throw new JPARSECException(
					"Degrees of freedom must be greater than 0");
		double[][] aa = new double[this.nTerms][this.nData];

		for (int j = 0; j < nData; j++)
			this.yData[j] -= fixed_a;
		for (int j = 0; j < nData; j++)
			aa[0][j] = this.xData[0][j];

		for (int i = 1; i < nTerms; i++) {
			for (int j = 0; j < nData; j++) {
				aa[i][j] = Math.pow(this.xData[0][j], i + 1);
			}
		}
		this.best = new double[this.nTerms];
		this.bestSd = new double[this.nTerms];
		this.tValues = new double[this.nTerms];
		this.pValues = new double[this.nTerms];
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

	// Generalised linear regression (protected method called by linear(),
	// linearGeneral() and polynomial())
	private void generalLinear(double[][] xd) throws JPARSECException {
		if (this.nData <= this.nTerms && !this.ignoreDofFcheck)
			throw new JPARSECException(
					"Number of unknown parameters is greater than or equal to the number of data points");
		double sum = 0.0D;
		double[][] a = new double[this.nTerms][this.nTerms];
		double[] b = new double[this.nTerms];
		double[] coeff = new double[this.nTerms];

		// set statistic arrays to NaN if df check ignored
		if (this.ignoreDofFcheck) {
			this.bestSd = new double[this.nTerms];
			this.pseudoSd = new double[this.nTerms];
			this.tValues = new double[this.nTerms];
			this.pValues = new double[this.nTerms];

			this.covar = new double[this.nTerms][this.nTerms];
			this.corrCoeff = new double[this.nTerms][this.nTerms];

			for (int i = 0; i < this.nTerms; i++) {
				this.bestSd[i] = Double.NaN;
				this.pseudoSd[i] = Double.NaN;
				for (int j = 0; j < this.nTerms; j++) {
					this.covar[i][j] = Double.NaN;
					this.corrCoeff[i][j] = Double.NaN;
				}
			}
		}

		for (int i = 0; i < nTerms; ++i) {
			sum = 0.0D;
			for (int j = 0; j < nData; ++j) {
				sum += this.yData[j] * xd[i][j] / square(this.weight[j]);
			}
			b[i] = sum;
		}
		for (int i = 0; i < nTerms; ++i) {
			for (int j = 0; j < nTerms; ++j) {
				sum = 0.0;
				for (int k = 0; k < nData; ++k) {
					sum += xd[i][k] * xd[j][k] / square(this.weight[k]);
				}
				a[j][i] = sum;
			}
		}
		Matrix aa = new Matrix(a);
		coeff = aa.solve(new Matrix(b)).getColumn(0);

		for (int i = 0; i < this.nTerms; i++) {
			this.best[i] = coeff[i];
		}
	}

	// Generalised linear regression statistics (protected method called by
	// linear(), linearGeneral() and polynomial())
	private void generalLinearStats(double[][] xd) throws JPARSECException {

		double sde = 0.0D, sum = 0.0D, yCalctemp = 0.0D;
		double[][] h = new double[this.nTerms][this.nTerms];
		double[][] stat = new double[this.nTerms][this.nTerms];
		this.covar = new double[this.nTerms][this.nTerms];
		this.corrCoeff = new double[this.nTerms][this.nTerms];
		double[] coeffSd = new double[this.nTerms];
		double[] coeff = new double[this.nTerms];

		for (int i = 0; i < this.nTerms; i++) {
			coeff[i] = this.best[i];
		}

		this.chiSquare = 0.0D;
		this.sumOfSquaresError = 0.0D;
		for (int i = 0; i < nData; ++i) {
			yCalctemp = 0.0;
			for (int j = 0; j < nTerms; ++j) {
				yCalctemp += coeff[j] * xd[j][i];
			}
			this.yCalc[i] = yCalctemp;
			yCalctemp -= this.yData[i];
			this.residual[i] = yCalctemp;
			this.residualW[i] = yCalctemp / weight[i];
			this.chiSquare += square(yCalctemp / this.weight[i]);
			this.sumOfSquaresError += square(yCalctemp);
		}
		double varY = this.sumOfSquaresError / (this.degreesOfFreedom);
		double sdY = Math.sqrt(varY);

		if (this.sumOfSquaresError == 0.0D) {
			for (int i = 0; i < this.nTerms; i++) {
				coeffSd[i] = 0.0D;
				for (int j = 0; j < this.nTerms; j++) {
					this.covar[i][j] = 0.0D;
					if (i == j) {
						this.corrCoeff[i][j] = 1.0D;
					} else {
						this.corrCoeff[i][j] = 0.0D;
					}
				}
			}
		} else {
			for (int i = 0; i < this.nTerms; ++i) {
				for (int j = 0; j < this.nTerms; ++j) {
					sum = 0.0;
					for (int k = 0; k < this.nData; ++k) {
						if (weightOpt) {
							sde = weight[k];
						} else {
							sde = sdY;
						}
						sum += xd[i][k] * xd[j][k] / square(sde);
					}
					h[j][i] = sum;
				}
			}
			Matrix hh = new Matrix(h);
			hh = hh.inverse();
			stat = hh.getArrayCopy();
			for (int j = 0; j < nTerms; ++j) {
				coeffSd[j] = Math.sqrt(stat[j][j]);
			}

			for (int i = 0; i < this.nTerms; i++) {
				for (int j = 0; j < this.nTerms; j++) {
					this.covar[i][j] = stat[i][j];
				}
			}

			for (int i = 0; i < this.nTerms; i++) {
				for (int j = 0; j < this.nTerms; j++) {
					if (i == j) {
						this.corrCoeff[i][j] = 1.0D;
					} else {
						this.corrCoeff[i][j] = covar[i][j]
								/ (coeffSd[i] * coeffSd[j]);
					}
				}
			}
		}

		for (int i = 0; i < this.nTerms; i++) {
			this.bestSd[i] = coeffSd[i];
			this.tValues[i] = this.best[i] / this.bestSd[i];
			double atv = Math.abs(this.tValues[i]);
			if (isNaN(atv)) {
				this.pValues[i] = Double.NaN;
			} else {
				this.pValues[i] = 1.0 - studentTcdf(-atv, atv,
						this.degreesOfFreedom);
			}
		}

		// Coefficient of determination
		this.yWeightedMean = mean(this.yData, this.weight);

		this.sumOfSquaresTotal = 0.0;
		for (int i = 0; i < this.nData; i++) {
			this.sumOfSquaresTotal += square((this.yData[i] - this.yWeightedMean)
					/ weight[i]);
		}

		this.sumOfSquaresRegrn = this.sumOfSquaresTotal - this.chiSquare;
		if (this.sumOfSquaresRegrn < 0.0)
			this.sumOfSquaresRegrn = 0.0;
	}

	/**
	 * Returns true if the convergence was reached, false otherwise.
	 * @return True or false.
	 */
	public boolean convergence() {
		return this.nlrStatus;
	}

	private static boolean isNaN(double x) {
		boolean itis = false;
		if (new Double(x).equals(Double.NaN))
			itis = true;
		return itis;
	}

	private static final double SQRT_TWO_PI = Math.sqrt(Constant.TWO_PI);

	// Class to evaluate the Gausian (normal) function y = (yscale/sd.sqrt(2.pi)).exp(-0.5[(x - xmean)/sd]^2).
	class GaussianFunction implements RegressionFunction{
	    public boolean scaleOption = true;
	    public double scaleFactor = 1.0D;
	    public double function(double[] p, double[] x){
	        double yScale = scaleFactor;
	        if(scaleOption)yScale = p[2];
	        double y = (yScale/(p[1]*SQRT_TWO_PI))*FastMath.exp(-0.5D*square((x[0]-p[0])/p[1]));
	        return y;
	    }
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

	// Class to evaluate the Gausian (normal) function y = (yscale/sd.sqrt(2.pi)).exp(-0.5[(x - xmean)/sd]^2).
	// Some parameters may be fixed
	class GaussianFunctionFixed implements RegressionFunction{

	    public double[] param = new double[3];
	    public boolean[] fixed = new boolean[3];

	    public double function(double[] p, double[] x){

	        int ii = 0;
	        for(int i=0; i<3; i++){
	            if(!fixed[i]){
	                param[i] = p[ii];
	                ii++;
	            }
	        }

	        double y = (param[2]/(param[1]*SQRT_TWO_PI))*Math.exp(-0.5D*square((x[0]-param[0])/param[1]));
	        return y;
	    }
	}
}

interface RegressionFunction {
	public double function(double[] param, double[] x);
}

interface RegressionFunction2 {
	public double function(double[] param, double[] x, int i);
}
