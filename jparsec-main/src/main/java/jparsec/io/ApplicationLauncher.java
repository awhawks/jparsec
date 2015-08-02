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
package jparsec.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import jparsec.graph.DataSet;
import jparsec.util.JPARSECException;
import jparsec.util.Logger;
import jparsec.util.Logger.LEVEL;

/**
 * A class to launch external applications using static methods.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class ApplicationLauncher {
	private static String linuxDesktop = null;

	// private constructor so that this class cannot be instantiated.
	private ApplicationLauncher() {
	}

	/**
	 * Returns the value of an environment (System-dependent) variable.
	 *
	 * @param envvar The name of the variable.
	 * @return The value, or null string if not found.
	 */
	public static String getEnvironmentVariable(String envvar) {
		Map<String, String> envVar = System.getenv();
		String value = envVar.get(envvar);
		if (value == null) return null;
		else return value.trim();
	}

	/**
	 * Obtains the desktop environment in execution in Linux: KDE or GNOME.
	 *
	 * @return KDE (kde) or GNOME (gnome) as a lowercase string.
	 */
	public static String getLinuxDesktop() {
		if (linuxDesktop != null) return linuxDesktop;
		if (!"".equals(getEnvironmentVariable("KDE_FULL_SESSION")) ||
			!"".equals(getEnvironmentVariable("KDE_MULTIHEAD"))) {
			linuxDesktop = "kde";
		} else if (!"".equals(getEnvironmentVariable("GNOME_DESKTOP_SESSION_ID")) ||
				   !"".equals(getEnvironmentVariable("GNOME_KEYRING_SOCKET"))) {
			linuxDesktop = "gnome";
		} else linuxDesktop = "kde";

		return linuxDesktop;
	}

	/**
	 * Returns the ID constant of the current operating system.
	 *
	 * @return The detected operating system.
	 */
	public static OS getOperatingSystem() {
		String os = System.getProperty("os.name").toUpperCase();

		if (os.contains("WINDOWS")) return ApplicationLauncher.OS.WINDOWS;
		if (os.contains("MAC")) return ApplicationLauncher.OS.MAC;
		if (os.contains("LINUX")) return ApplicationLauncher.OS.LINUX;

		if ("Dalvik".equalsIgnoreCase(System.getProperty("java.vm.name")))
			return ApplicationLauncher.OS.ANDROID;

		return ApplicationLauncher.OS.UNKNOWN;
	}

	/**
	 * Returns the identifier of the current process.
	 *
	 * @return The name of the thread.
	 */
	public static String getProcessID() {
		return Thread.currentThread().getName();
	}

	/**
	 * Returns the current operating system version.
	 *
	 * @return The detected operating system version.
	 */
	public static String getOperatingSystemVersion() {
		String os = System.getProperty("os.version").toUpperCase();
		return os;
	}

	/**
	 * Returns the current operating system version.
	 *
	 * @return The detected operating system version.
	 */
	public static String getUserName() {
		String os = System.getProperty("user.name").toUpperCase();
		return os;
	}

	/**
	 * Returns the current user home.
	 *
	 * @return The user home directory.
	 */
	public static String getUserHome() {
		String os = System.getProperty("user.home").toUpperCase();
		return os;
	}

	/**
	 * Returns the current user language.
	 *
	 * @return The user language.
	 */
	public static String getUserLanguage() {
		String os = System.getProperty("user.language").toUpperCase();
		return os;
	}

	/**
	 * Launch a command/application. For Linux the KDE desktop is used by default, unless
	 * GNOME is properly detected.
	 *
	 * @param url Link to launch.
	 * @return The process.
	 */
	public static Process launchURL(String url) {
		try {
			String OsName = System.getProperty("os.name").toUpperCase();
			String[] cmd = null;

			if (OsName.contains("95")) {
				cmd = new String[] { "command.com", "/C", "start", url };
			}
			if (OsName.contains("WINDOWS")) {
				cmd = new String[] { "cmd.exe", "/C", "start", url };
			}
			if (OsName.contains("MAC")) {
				cmd = new String[] { "open", url };
			}

			if (OsName.contains("LINUX")) {
				if (getLinuxDesktop().equals("kde")) {
					cmd = new String[] { "kfmclient", "exec", url };
				} else {
					cmd = new String[] { "gnome-open", url };
				}
			}

			if (null != cmd) {
				return Runtime.getRuntime().exec(cmd);
			}
		} catch (IOException ioex) {
			Logger.log(LEVEL.ERROR, "Could not launch url " + url + ". Message was: " + ioex.getLocalizedMessage());
		}

		return null;
	}

	/**
	 * Launches an URL using the default viewer.
	 *
	 * @param filepath Path to the file.
	 * @return The process
	 */
	public static Process launchDefaultViewer(String filepath) {
		return launchURL(new File(filepath).getAbsolutePath());
	}

	/**
	 * Returns the console output of a process. The method waits until
	 * the process finish.
	 *
	 * @param pr The process.
	 * @return The output, or null if an error occurs.
	 */
	public static String getConsoleOutputFromProcess(Process pr) {
		StringBuffer output = new StringBuffer("");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				output.append(line + FileIO.getLineSeparator());
			}
			pr.waitFor();
		} catch (Exception e) {
			output = null;
		}

		return output.toString();
	}

	/**
	 * Returns the console error output of a process. The method waits until
	 * the process finish. Sometimes it is necessary to call this method (ej SExtractor)
	 * instead of {@linkplain #getConsoleOutputFromProcess(Process)} to get the
	 * output of a process.
	 *
	 * @param pr The process.
	 * @return The output, or null if an error occurs.
	 */
	public static String getConsoleErrorOutputFromProcess(Process pr) {
		StringBuffer output = new StringBuffer("");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				output.append(line + FileIO.getLineSeparator());
			}
			pr.waitFor();
		} catch (Exception e) {
			output = null;
		}

		return output.toString();
	}

	// Parses the command to set possible environment variables
	private static String parse(String in) {
		int a = in.indexOf("$");
		if (a < 0) return in;
		do {
			String next = in.substring(a + 1);
			int s1 = next.indexOf(" ");
			int s2 = next.indexOf(":");
			int s3 = next.indexOf("/");
			int s4 = next.indexOf(";");
			int s5 = next.indexOf(",");
			int s = 10000;
			if (s1 >= 0 && s1 < s) s = s1;
			if (s2 >= 0 && s2 < s) s = s2;
			if (s3 >= 0 && s3 < s) s = s3;
			if (s4 >= 0 && s4 < s) s = s4;
			if (s5 >= 0 && s5 < s) s = s5;
			if (s < 10000) next = next.substring(0, s);

			in = DataSet.replaceAll(in, "$" + next, ApplicationLauncher.getEnvironmentVariable(next), true);
			a = in.indexOf("$");
		} while (a >= 0);
		return in;
	}

	/**
	 * Executes a command.
	 *
	 * @param command Command to execute.
	 * @return The process.
	 * @throws JPARSECException If an error occurs.
	 */
	public static Process executeCommand(String command)
			throws JPARSECException {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process pr = runtime.exec(parse(command));
			return pr;
		} catch (Throwable t) {
			throw new JPARSECException("error while executing system command.", t);
		}
	}

	/**
	 * Executes a command.
	 *
	 * @param command Command to execute.
	 * @param env	 Environment variables, format name=value. Can be null.
	 * @param dir	 Working directory. Can be null.
	 * @return The process.
	 * @throws JPARSECException If an error occurs.
	 */
	public static Process executeCommand(String command, String env[], File dir)
			throws JPARSECException {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process pr = runtime.exec(parse(command), env, dir);
			return pr;
		} catch (Throwable t) {
			throw new JPARSECException("error while executing system command.", t);
		}
	}

	/**
	 * Executes a command.
	 *
	 * @param command Command to execute.
	 * @param env	 Environment variables, format name=value. Can be null.
	 * @param dir	 Working directory. Can be null.
	 * @return The process.
	 * @throws JPARSECException If an error occurs.
	 */
	public static Process executeCommand(String[] command, String env[], File dir)
			throws JPARSECException {
		try {
			Runtime runtime = Runtime.getRuntime();
			String c[] = command.clone();
			for (int i = 0; i < c.length; i++) {
				c[i] = parse(c[i]);
			}
			Process pr = runtime.exec(c, env, dir);
			return pr;
		} catch (Throwable t) {
			throw new JPARSECException("error while executing system command.", t);
		}
	}

	/**
	 * Executes a set of commands by creating a script.
	 *
	 * @param command Command to execute.
	 * @param dir	 Working directory. Can be null.
	 * @return The console output.
	 * @throws JPARSECException If an error occurs.
	 */
	public static String executeAsScript(String[] command, File dir)
			throws JPARSECException {
		try {
			String path = dir.getAbsolutePath();
			if (!path.endsWith(FileIO.getFileSeparator())) path += FileIO.getFileSeparator();
			path += "script.jparsec";
			WriteFile.writeAnyExternalFile(path, DataSet.toString(command, FileIO.getLineSeparator()));
			Process p = ApplicationLauncher.executeSystemCommand("chmod +x " + path);
			p.waitFor();
			p = ApplicationLauncher.executeSystemCommand(path);
			p.waitFor();
			String out = getConsoleOutputFromProcess(p);
			FileIO.deleteFile(path);
			return out;
		} catch (Throwable t) {
			throw new JPARSECException("error while executing system command.", t);
		}
	}

	/**
	 * Executes a command.
	 *
	 * @param command Command to execute.
	 * @return The process.
	 * @throws JPARSECException If an error occurs.
	 */
	public static Process executeCommand(String[] command)
			throws JPARSECException {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process pr = runtime.exec(command);
			return pr;
		} catch (Throwable t) {
			throw new JPARSECException("error while executing system command.", t);
		}
	}

	/**
	 * Executes a system command.
	 *
	 * @param command Command to execute.
	 * @return The process.
	 * @throws JPARSECException If an error occurs.
	 */
	public static Process executeSystemCommand(String command)
			throws JPARSECException {
		try {
			String osName = System.getProperty("os.name");
			String[] cmd = null;
			String cmdline = command;
			if ("Windows 95".equals(osName) || "Windows 98".equals(osName)) {
				cmd = new String[] { "command.com", "/C", cmdline };
			} else if (osName.startsWith("Windows")) {
				cmd = new String[] { "cmd.exe", "/C", cmdline };
			} else if ("Linux".equals(osName)) {
				cmd = new String[] { "/usr/bin/konsole", "-e", cmdline };
			} else { // for MAC
				cmd = new String[] { "open", "-a", cmdline };
			}

			Runtime runtime = Runtime.getRuntime();
			Process pr = runtime.exec(parse(command));
			return pr;
		} catch (Throwable t) {
			throw new JPARSECException("error while executing system command " + command, t);
		}
	}

	/**
	 * The different constants for the operating systems.
	 */
	public enum OS {
		/**
		 * ID constant for an unknown operating system.
		 */
		UNKNOWN,
		/**
		 * ID constant for a Windows operating system.
		 */
		WINDOWS,
		/**
		 * ID constant for a Linux operating system.
		 */
		LINUX,
		/**
		 * ID constant for a Mac operating system.
		 */
		MAC,
		/**
		 * ID constant for Android plantform.
		 */
		ANDROID
	}
}
