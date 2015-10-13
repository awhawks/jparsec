/*
 * This file is part of JPARSEC library.
 *
 * (C) Copyright 2006-2015 by T. Alonso Albi - OAN (Spain).
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
package jparsec.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import jparsec.ephem.Functions;
import jparsec.graph.DataSet;
import jparsec.io.FileIO;
import jparsec.io.ReadFile;
import jparsec.math.Constant;
import jparsec.math.FastMath;
import jparsec.observer.LocationElement;
import jparsec.observer.Observatory;
import jparsec.observer.ObservatoryElement;
import jparsec.observer.ObserverElement;
import jparsec.test.TestElement.TEST;
import jparsec.util.Configuration;
import jparsec.util.JPARSECException;

/**
 * Manages the execution of tests.
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Test
{
	// private constructor so that this class cannot be instantiated.
	private Test() {}

	private static final String VALUES_SEPARATOR = ",";
	private static final String FIELD_SEPARATOR = ";";

	private static TestElement parseTestLine(String line) throws JPARSECException {
		int id = Integer.parseInt(FileIO.getField(1, line, FIELD_SEPARATOR, false));
		String in = FileIO.getField(2, line, FIELD_SEPARATOR, false);
		String ou = FileIO.getField(3, line, FIELD_SEPARATOR, false);

		TestElement test = new TestElement(TEST.values()[id],
				DataSet.toStringArray(in, VALUES_SEPARATOR),
				DataSet.toStringArray(ou, VALUES_SEPARATOR)
				);
		return test;
	}

	// Stops when first failure is found
	private static final boolean STOP_AT_FIRST_FAILURE = false;
	// True to assist in the elaboration of tests, should be false to execute the tests
	private static final boolean ASSIST_MODE = false;

	private static String getTime(double t0, double t1) {
		double off = Math.abs(t0 - t1) / 1.0E9;
		return Functions.formatValue(off, 5)+"s";
	}
}
