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
	
	/**
	 * Executes the tests for JPARSEC.
	 * @param args Not used.
	 */
	public static void main(String args[]) {
		System.out.println("EXECUTING TESTS");
		System.out.println("");

		/**
		 * Location of external files for JPL ephemerides DE405. Should be configured by hand.
		 * Set to null to use Series96 or Moshier instead (lower precision).
		 */
		Configuration.JPL_EPHEMERIDES_FILES_EXTERNAL_PATH = "/home/alonso/eclipse/libreria_jparsec/ephem/test";

		TEST testsToExecute = TEST.ALL;
		boolean writeAllToSystemOutAndNotSystemErr = true; // So that errors are always written after the test # identifier, but not in red

		try {
			String file[] = DataSet.arrayListToStringArray(ReadFile.readResource("jparsec/test/tests.txt"));

			long time0 = System.currentTimeMillis();
			int ok = 0, failure = 0, n = 0;
			TEST lastID = TEST.ALL;
			for (int i = 0; i< file.length; i++) {
				if (!file[i].startsWith("!")) {
					TestElement test = parseTestLine(file[i]);
					if (test.testID == testsToExecute || testsToExecute == TEST.ALL) {
						if (test.testID != lastID) {
							System.out.println("Testing ... "+TestElement.TEST_DESCRIPTION[test.testID.ordinal()]);
							lastID = test.testID;
						}
						test.executeTest();
						n ++;
						String out = test.getOutput(), err = test.getErrorOutput();
						System.out.println("   Test case #"+n+": "+file[i]);
						if (out != null && !out.equals("")) System.out.println(out);
						ok ++;
	
						if (ASSIST_MODE)
							System.out.println(test.testID+FIELD_SEPARATOR+" "+DataSet.toString(test.testValues, VALUES_SEPARATOR+" ")+FIELD_SEPARATOR+" "+DataSet.toString(test.getValuesFound(), VALUES_SEPARATOR+" "));
	
						if (err != null && !err.equals("")) {
							if (writeAllToSystemOutAndNotSystemErr) {
								System.out.println(err);
							} else {
								System.err.println(err);
							}
							if (STOP_AT_FIRST_FAILURE) throw new JPARSECException("Failure found");
							failure ++;
							ok --;
						}
					} else {
						n ++;
					}
				}
			}
			
			long time1 = System.currentTimeMillis();
			System.out.println("");
//			System.out.println("JPARSEC WARNINGS");
//			System.out.println("");
//			JPARSECException.showWarnings();
//			System.out.println("");
			System.out.println("SUMMARY");
			System.out.println("");
			System.out.println("Passed tests: "+ok);
			System.out.println("Failures: "+failure);
			System.out.println("Launch time: "+(float) ((time1-time0)*0.001)+"s");

			System.out.println("");
			System.out.println("EXECUTING BENCHMARKS");
			System.out.println("");
			String input = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. "+
			"Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, "+
			"pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate "+
			"eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer "+
			"tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, "+
			"consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut "+
			"metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. "+
			"Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet "+
			"adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et "+
			"ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros "+
			"faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget "+
			"bibendum sodales, augue velit cursus nunc, ";
			String text = input;
			int nrepeat = 10;
			for (int i=0; i< nrepeat; i++) {
				text += text;
			}
			int nfield = 200*(nrepeat+1);
			String separator = "a";
			
			long ta0 = System.nanoTime();
			int nfields1 = FileIO.getNumberOfFields(text, separator, false);
			long ta1 = System.nanoTime();
			int nfields2 = FileIO.getNumberOfFields(text, separator, true);
			long ta2 = System.nanoTime();
			String fieldn_1 = FileIO.getField(nfield, text, separator, false);
			long ta3 = System.nanoTime();
			String fieldn_2 = FileIO.getField(nfield, text, separator, true);
			long ta4 = System.nanoTime();

			// Now using Java intrinsic functions
			long tb0 = System.nanoTime();
			String v[] = text.split(Pattern.quote(separator));
			int nf = v.length;
			long tb1 = System.nanoTime();
			String fieldn = v[nfield-1];
			long tb2 = System.nanoTime();
			
			long tc0 = System.nanoTime();
			StringTokenizer tok = new StringTokenizer(text, separator);
	    	int nf2 = tok.countTokens();
			long tc1 = System.nanoTime();
	    	String f = "";
	    	for (int i=0; i<nfield; i++) {
	    		f = tok.nextToken();
	    	}
			long tc2 = System.nanoTime();

			System.out.println("Counting number of fields");
			System.out.println("JPARSEC needs "+getTime(ta1, ta0)+" to count ("+nfields1+"), and "+getTime(ta2, ta1)+" to eliminate redundant separators ("+nfields2+")");
			System.out.println("String.split needs "+getTime(tb1, tb0)+" to count ("+nf+"), and StringTokenizer "+getTime(tc0, tc1)+" ("+nf2+")");
			System.out.println("Getting a given field (#"+nfield+")");
			System.out.println("JPARSEC needs "+getTime(ta3, ta2)+" to obtain it ("+fieldn_1+"), and "+getTime(ta4, ta3)+" to do that and eliminating redundant separators ("+fieldn_2+")");
			System.out.println("String.split needs "+getTime(tb2, tb1)+" to obtain it ("+fieldn+"), and StringTokenizer "+getTime(tc2, tc1)+" ("+f+")");
			
			// Split is very slow. StringTokenizer is fine when separator is " ". The method implemented in JPARSEC (based on
			// indexOf is between 1.5 and 4 times faster than the others.
			System.out.println("");
			System.out.println("Java test, see http://stackoverflow.com/questions/5965767/performance-of-stringtokenizer-class-vs-split-method-in-java");
			int runs = 10;
	        long start = System.nanoTime();
	        for (int r = 0; r < runs; r++) {
	            StringTokenizer st = new StringTokenizer(text);
	            List<String> list = new ArrayList<String>();
	            while (st.hasMoreTokens())
	                list.add(st.nextToken());
	        }
	        long time = System.nanoTime() - start;
	        System.out.printf("StringTokenizer took an average of %.1f ms%n", time / runs / 1000000.0);

	        start = System.nanoTime();
	        Pattern spacePattern = Pattern.compile(separator);
	        for (int r = 0; r < runs; r++) {
	            List<String> list = Arrays.asList(spacePattern.split(text, 0));
	        }
	        time = System.nanoTime() - start;
	        System.out.printf("Pattern.split took an average of %.1f ms%n", time / runs / 1000000.0);

	        start = System.nanoTime();
	        for (int r = 0; r < runs; r++) {
	            List<String> list = new ArrayList<String>();
	            int pos = 0, end;
	            while ((end = text.indexOf(separator, pos)) >= 0) {
	                list.add(text.substring(pos, end));
	                pos = end + 1;
	            }
	        }
	        time = System.nanoTime() - start;
	        System.out.printf("indexOf loop took an average of %.1f ms%n", time / runs / 1000000.0);

			System.out.println("");
			System.out.println("DataSet - replaceAll function");
			String replace = " ", replacement = "*";
			long td0 = System.nanoTime();
			String out = DataSet.replaceAll(input, replace, replacement, true);
			long td1 = System.nanoTime();
			String out2 = input.replaceAll(replace, replacement);
			long td2 = System.nanoTime();
			System.out.println("JPARSEC (direct string mode) needs "+getTime(td1, td0)+" to replace a character with another, and String.replaceAll needs "+getTime(td2, td1));
			if (!out.equals(out2)) System.out.println("The replace process is not correct !"); 

			System.out.println("");
			System.out.println("FastMath test");
			
			// atan2, 
			n = 36000; // / 5;
			long t0 = System.currentTimeMillis();
			FastMath.setMaximumNumberOfAngles(n);
			long t1 = System.currentTimeMillis();
			double dt = (t1-t0)/1000.0;
			System.out.println("Initialization needs "+dt+" seconds");
			n = 100000000;
			t0 = System.currentTimeMillis();
			double y = 0;
			for (int i=0; i<n; i++)
			{
				double x = Constant.TWO_PI / n;
				y += Math.sin(x);
			}
			t1 = System.currentTimeMillis();
			dt = (t1-t0)/1000.0;
			System.out.println("JAVA calculates "+n+" sines in "+dt+" seconds.");
			t0 = System.currentTimeMillis();
			for (int i=0; i<n; i++)
			{
				double x = Constant.TWO_PI / n;
				y += FastMath.sin(x);
			}
			t1 = System.currentTimeMillis();
			dt = (t1-t0)/1000.0;
			System.out.println("FastMath calculates "+n+" sines in "+dt+" seconds.");
			double maxError = -1.0;
			for (int i=0; i<=n; i++)
			{
				double x = Constant.TWO_PI / n + FastMath.getResolution() * 0.5; // to get max errors
				double x1 = Math.sin(x);
				double x2 = FastMath.sin(x);
				double dif = Math.abs(x1-x2);
				if (dif > maxError || maxError < 0)
					maxError = dif;
			}
			System.out.println("Maximum error found "+maxError);
			
			double z = 0;
			t0 = System.currentTimeMillis();
			for (y=-1000; y<=1000; y=y+0.001) {
				z += Math.atan2(y, 1);
			}
			t1 = System.currentTimeMillis();
			dt = (t1-t0)/1000.0;
			System.out.println("Java calculates 2000000 atans in "+dt+" seconds.");
			t0 = System.currentTimeMillis();
			for (y=-1000; y<=1000; y=y+0.001) {
				z += FastMath.atan2(y, 1);
			}
			t1 = System.currentTimeMillis();
			dt = (t1-t0)/1000.0;
			System.out.println("FastMath calculates 2000000 atans in "+dt+" seconds.");
			maxError = -1;
			for (y=-1000; y<=1000; y=y+0.0001) {
				double atan = Math.atan2(y, 1);
				double atan1 = FastMath.atan2(y, 1);
				if (maxError == -1 || Math.abs(atan-atan1) > maxError) maxError = Math.abs(atan-atan1);
			}
			System.out.println("Maximum error found "+maxError);		

			System.out.println("");
			System.out.println("Other tests");
			
			System.out.println("");
			System.out.println("Reading Marsden list of observatories: Teide");

			int my_obs = Observatory.searchByNameInMarsdenList("Teide");
			ObservatoryElement obs2 = Observatory.getObservatoryFromMarsdenList(my_obs);
			System.out.println(obs2.location);
			System.out.println(obs2.name);
			System.out.println(obs2.longitude - 360.0);
			System.out.println(obs2.latitude);
			System.out.println(obs2.height);
			LocationElement loc = new LocationElement(-3.5 * Constant.DEG_TO_RAD, 40.2 * Constant.DEG_TO_RAD, 1.0);
			ObservatoryElement observ = Observatory.getObservatoryFromMarsdenList(Observatory.searchByPositionInMarsdenList(loc));
			System.out.println(" Observatory nearest loc(-3.5, 40.2): " + observ.name);

			System.out.println("");
			System.out.println("Observer data for this machine");			

			ObserverElement observer = new ObserverElement("");
			System.out.println("Tests launched from "+observer.getName()+", at "+Functions.formatAngleAsDegrees(observer.getLongitudeRad(), 3)+", "+Functions.formatAngleAsDegrees(observer.getLatitudeRad(), 3));
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
