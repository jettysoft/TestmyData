package com.testmydata.fxcontroller;

import java.awt.EventQueue;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.dao.DAO;
import com.testmydata.dao.DBAccess;
import com.testmydata.fxhelpers.LoginFXHelper;
import com.testmydata.main.WelcomePage;
import com.testmydata.util.CommonFunctions;
import com.testmydata.vpn.VpnConnectionThread;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CompanyNameAddingController implements Initializable {

	@FXML
	private JFXTextField companytext;
	@FXML
	private JFXButton ok, cancel;
	@FXML
	private Hyperlink new_user;
	Stage myStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		CommonFunctions.companyname = null;
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
		new_user.setEffect(ds);

		if (companytext.getText() != null && !companytext.getText().isEmpty()) {
			companytext.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override

				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (newPropertyValue) {
						String status = new DAO()
								.checkDataBaseExist(companytext.getText().trim().toLowerCase().substring(0,
										Math.min(CommonFunctions.companyname.toString().toLowerCase().length(), 40)));

						if (status.contains("notExisted") || status.contains("failure")) {
							CommonFunctions.message = "Entered Company is not Registered. Please Register your Company with Given Key!";
							CommonFunctions.invokeAlertBox(getClass());

							WelcomePage.welcomeApp();
							ok.setDisable(true);
						} else if (status.contains("existed")) {
							ok.setDisable(false);
						}
					} else if (oldPropertyValue) {

					}
				}
			});
		}

	}

	@FXML
	private void new_user(ActionEvent event) {
		Node source = (Node) event.getSource();
		myStage = (Stage) source.getScene().getWindow();
		myStage.close();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						CommonFunctions.updateCompanyName("trademen");

						String screenName = "welcomepage";
						// new WelcomePageFXHelper().initAndShowGUI(screenName);
						new LoginFXHelper().initAndShowGUI(screenName);
					}
				});
			}
		});

	}

	@FXML
	private void ok(ActionEvent event) {
		CommonFunctions.companyname = companytext.getText().trim();

		if (CommonFunctions.companyname == null || CommonFunctions.companyname.isEmpty()) {
			CommonFunctions.message = "Please Enter Company Name...!";
			CommonFunctions.invokeAlertBox(getClass());
		} else {

			String status = new DAO()
					.checkDataBaseExist(companytext.getText().replace(" ", "").trim().toLowerCase().substring(0, Math
							.min(CommonFunctions.companyname.toString().replace(" ", "").toLowerCase().length(), 40)));

			if (status.contains("notExisted") || status.contains("failure")) {
				CommonFunctions.message = "Entered Company is not Registered. Please Register your Company with Given Key!";
				CommonFunctions.invokeAlertBox(getClass());
				WelcomePage.welcomeApp();
			} else if (status.contains("existed")) {
				CommonFunctions.updateCompanyName(CommonFunctions.companyname);

				// updating the Company name as DB name in the dbConfig.xml
				// DBnameUpdateJAXB db = new DBnameUpdateJAXB();
				// db.encrypt(EncryptAndDecrypt.encryptData(CommonFunctions.companyname.toString().toLowerCase().substring(0,
				// Math.min(CommonFunctions.companyname.toString().toLowerCase().length(),
				// 40))));

				Node source = (Node) event.getSource();
				Stage myStage = (Stage) source.getScene().getWindow();
				myStage.close();

				CommonFunctions.message = "Successfully CompanyName Updated...!";
				CommonFunctions.invokeAlertBox(getClass());

				DBAccess dba = new DBAccess();
				DBAccess.con = null;
				dba.getConnection();

				WelcomePage.loginApp();
			}

		}
	}

	@FXML
	private boolean cancel(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage myStage = (Stage) source.getScene().getWindow();
		myStage.close();
		VpnConnectionThread.shutdown();
		System.exit(0);
		return false;
	}

}
