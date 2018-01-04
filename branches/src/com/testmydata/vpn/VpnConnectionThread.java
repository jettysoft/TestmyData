package com.testmydata.vpn;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.testmydata.util.EncryptAndDecrypt;

public class VpnConnectionThread extends Thread {

	File appHome = new File(".", "vpn\\bin");
	private static boolean isVpnConnected = false;
	private static boolean isVpnError = false;
	private static Thread current = new Thread();
	private static Process process = null;

	@Override
	public void run() {
		try {
			// process = Runtime.getRuntime()
			// .exec("cmd /c cd " + appHome.getAbsolutePath() + " && " + "
			// jettysoftvpn.exe --remote "
			// +
			// EncryptAndDecrypt.decryptData("/RoYO9CQ/EJyc/SsodN+u6YvEXwBtoHoEp50l50EtFg=")
			// + " --config " + appHome.getAbsolutePath() + "\\client.ovpn"); //
			// direct

			process = Runtime.getRuntime()
					.exec("cmd /c cd " + appHome.getAbsolutePath() + " && " + "jettysoftvpn.exe --remote "
							+ EncryptAndDecrypt.decryptData("BGJlaGW2XT5jMWggGhjoMnf0rVIxL4jDBzxbcuqiXgQ=")
							+ " --config " + appHome.getAbsolutePath() + "\\client.ovpn"); // indirect

			BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line = new String();
			while ((line = buffer.readLine()) != null) {
				// System.out.println(line);
				if (line.contains("There are no TAP-Windows adapters on this system")) {
					TapAdapterInstallWrapper installTaps = new TapAdapterInstallWrapper();
					installTaps.install();
					run();
				}
				if (line.contains("Initialization Sequence Completed")) {
					isVpnConnected = true;
				}
				if (line.contains("All TAP-Windows adapters on this system are currently in use.")) {
					isVpnError = true;
				}
			}

		} catch (Exception e) {
			// There will be no exception. If this method is called and
			// connection is not established for some reason, it will
			// have to fixed manually
			// System.out.println("Unable to establish VPN Connection: " +
			// e.getMessage());
		}
	}

	public static void launch() {
		try {
			current = new VpnConnectionThread();
			current.setName(VpnConnectionThread.class.getSimpleName());
			current.setDaemon(true);
			current.start();
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// Ignore becoz it will never be interrupted
		} finally {
			if (!isVpnConnected) {
				shutdown();
			}
		}
	}

	public static boolean isVpnConnected() {
		return isVpnConnected && current.isAlive();
	}

	public static boolean isVpnError() {
		return isVpnError;
	}

	public static void shutdown() {
		try {
			process.destroy();
			Runtime.getRuntime().exec("cmd /c taskkill.exe /F /IM jettysoftvpn.exe");
			new TapAdapterInstallWrapper().remove();
		} catch (IOException e) {
			// Ignore this one.
		}

	}
}
