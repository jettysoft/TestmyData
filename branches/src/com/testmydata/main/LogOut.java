/**
 * 
 */
package com.testmydata.main;

import java.util.Properties;

import com.testmydata.util.EncryptAndDecrypt;
import com.testmydata.util.FileIOOperations;
import com.testmydata.vpn.VpnConnectionThread;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class LogOut {

	@FXML
	Stage myStage;

	public LogOut() {

	}

	@SuppressWarnings({ "unused", "static-access" })
	public void exit(ActionEvent event) {
		String state = null;
		UserDashboard user = UserDashboard.setInstance();
		user.resetUser(null);

		FileIOOperations fileOperation = new FileIOOperations();
		Properties details = fileOperation.readPropertyFile("usersession.properties");
		if (!(details.isEmpty())) {
			if (!(details.getProperty("userType").equalsIgnoreCase(EncryptAndDecrypt.encryptData("Single User")))) {
				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();

				WelcomePage login = new WelcomePage();
				login.loginApp();

			} else {
				VpnConnectionThread.shutdown();
				Platform.exit();
				System.exit(0);
			}

		}
	}

	@SuppressWarnings("static-access")
	public void relogin(ActionEvent event) {
		Node source = (Node) event.getSource();
		myStage = (Stage) source.getScene().getWindow();
		myStage.close();

		WelcomePage login = new WelcomePage();
		login.loginApp();
	}

	@SuppressWarnings("static-access")
	public void lockSession(ActionEvent event) {
		Node source = (Node) event.getSource();
		myStage = (Stage) source.getScene().getWindow();
		myStage.close();

		WelcomePage login = new WelcomePage();
		login.loginApp();
	}

	// private static Collection<Stage> closeallstages() {
	// try {
	// return FXRobotHelper.getStages();
	// } catch (NullPointerException npe) {
	// // nasty NPE if no stages exist
	// return Collections.emptyList();
	// }
	// }
}
