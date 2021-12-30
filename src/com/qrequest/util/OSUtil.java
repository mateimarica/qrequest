package com.qrequest.util;

public class OSUtil {
	// Source of this OS checker: https://stackoverflow.com/a/31547504/17764136

	public enum OS {
		WINDOWS, LINUX, MACOS, SOLARIS
	}

	private static OS os = null;

	public static OS getOS() {
		if (os == null) {
			String operSys = System.getProperty("os.name").toLowerCase();
			if (operSys.contains("win")) {
				os = OS.WINDOWS;
			} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
				os = OS.LINUX;
			} else if (operSys.contains("mac")) {
				os = OS.MACOS;
			} else if (operSys.contains("sunos")) {
				os = OS.SOLARIS;
			}
		}
		return os;
	}
}
