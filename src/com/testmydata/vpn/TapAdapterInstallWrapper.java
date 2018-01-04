package com.testmydata.vpn;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class TapAdapterInstallWrapper {

	File installerLocation = new File(".", "vpn\\driver");

	@SuppressWarnings("unused")
	public void install() {
		String sysArch = System.getProperty("os.arch");
		String absolutePath = installerLocation.getAbsolutePath() + "\\" + sysArch;
		try {
			Process process = Runtime.getRuntime()
					.exec(absolutePath + "\\tapinstall.exe install " + absolutePath + "\\OemVista.inf" + " tap0901");

			BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line = new String();
			while ((line = buffer.readLine()) != null) {
				// System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// new TapAdapterInstallWrapper().install();
		new TapAdapterInstallWrapper().remove();
	}

	@SuppressWarnings("unused")
	public void remove() {
		String sysArch = System.getProperty("os.arch");
		String absolutePath = installerLocation.getAbsolutePath() + "\\" + sysArch;
		try {
			Process process = Runtime.getRuntime().exec(absolutePath + "\\tapinstall.exe remove tap0901");

			BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line = new String();
			while ((line = buffer.readLine()) != null) {
				// System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
