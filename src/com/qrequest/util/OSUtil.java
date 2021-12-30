package com.qrequest.util;

public class OSUtil {
	// Source of this OS checker: https://stackoverflow.com/a/31547504/17764136

	public enum OS {
		WINDOWS("windows"), 
		LINUX("linux"), 
		MACOS("macos");
		
		private final String name;

		private OS(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static OS os = null;

	public static OS getOS() {
		if (os == null) {
			String operSys = System.getProperty("os.name").toLowerCase();
			if (operSys.contains("win")) {
				os = OS.WINDOWS;
			} else if (operSys.contains("mac")) {
				os = OS.MACOS;
			} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix") || operSys.contains("sunos")) {
				os = OS.LINUX;
			}
		}
		return os;
	}
}
