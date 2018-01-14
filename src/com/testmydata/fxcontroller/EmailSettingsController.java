package com.testmydata.fxcontroller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.auditlog.StoreAuditLogger;
import com.testmydata.fxutil.UndecoratorController;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.EncryptAndDecrypt;
import com.testmydata.util.FileIOOperations;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.StaticImages;
import com.testmydata.util.ValidateRealEmail;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EmailSettingsController implements Initializable {
	@FXML
	private static EmailSettingsController userHome = null;
	@FXML
	private ImageView homeicon;
	@FXML
	private JFXTextField emailTxt, subTxt, bodyTxt;
	@FXML
	private JFXPasswordField passTxt;
	@FXML
	private Label messagelabel, emaillabel;
	@FXML
	private JFXButton save;
	Stage myStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		StoreAuditLogger.logStoreTransaction(Loggedinuserdetails.userId, "Settings", "", "", true,
				"Opens Email Settings", "");
		populateEmailDetailsInitially();
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
		emaillabel.setEffect(ds);
		homeicon.setImage(StaticImages.homeicon.getImage());

		passTxt.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (passTxt.getText() != null && !passTxt.getText().trim().isEmpty()
						&& passTxt.getText().trim() != "") {
					if (newPropertyValue) {
						messagelabel.setVisible(true);
						messagelabel
								.setText("Passwords are Highly Secured, BusinessStore will never Store Passwords...!");
					} else if (oldPropertyValue) {
						messagelabel.setVisible(false);
					}
				}

			}
		});

		homeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(MouseEvent event) {
				Cleanup scu = new Cleanup();
				EmailSettingsController nc = new EmailSettingsController();
				scu.nullifyStrings(nc);

				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();
				UndecoratorController.getInstance(null);
			}
		});
	}

	public static EmailSettingsController getInstance() {
		return userHome;
	}

	@FXML
	private void save() {
		boolean state = ValidateRealEmail.validateemail(emailTxt.getText());
		if (!(state)) {
			CommonFunctions.message = "Please Enter Valid Email Id...!";
			CommonFunctions.invokeAlertBox(getClass());
			return;
		} else {

			if (passTxt.getText().length() != 0 && subTxt.getText().length() != 0 && bodyTxt.getText().length() != 0) {
				HashMap<String, String> fileDetails = new HashMap<String, String>();
				fileDetails.put("email", EncryptAndDecrypt.encryptData(emailTxt.getText()));
				fileDetails.put("password", EncryptAndDecrypt.encryptData(passTxt.getText()));
				fileDetails.put("subject", EncryptAndDecrypt.encryptData(subTxt.getText()));
				fileDetails.put("body", EncryptAndDecrypt.encryptData(bodyTxt.getText()));
				FileIOOperations fileIO = new FileIOOperations();
				boolean status = fileIO.createPropertyFile(fileDetails, "emailSettings.properties");
				if (status) {
					CommonFunctions.message = "Sucessfully EMail Settings saved...!";
					CommonFunctions.invokeAlertBox(getClass());
					StoreAuditLogger.logStoreTransaction(Loggedinuserdetails.userId, "Settings", "", "", true,
							"Email Settings Saved", "");
				} else {
					CommonFunctions.message = "Internal Error occurred while updating EMail Settings. Please Try Again..!";
					CommonFunctions.invokeAlertBox(getClass());
					return;
				}
			} else {
				CommonFunctions.message = "Please complete the details properly...!";
				CommonFunctions.invokeAlertBox(getClass());
				return;
			}
		}
	}

	private void populateEmailDetailsInitially() {
		try {
			Properties dafaultValuesPF = new Properties();
			FileInputStream fis = new FileInputStream(
					new File(".", "/conf/emailSettings.properties").getAbsolutePath());
			dafaultValuesPF.load(fis);

			if (dafaultValuesPF != null && !dafaultValuesPF.isEmpty()) {
				emailTxt.setText(EncryptAndDecrypt.decryptData(dafaultValuesPF.getProperty("email")));
				bodyTxt.setText(EncryptAndDecrypt.decryptData(dafaultValuesPF.getProperty("body")));
				passTxt.setText(EncryptAndDecrypt.decryptData(dafaultValuesPF.getProperty("password")));
				subTxt.setText(EncryptAndDecrypt.decryptData(dafaultValuesPF.getProperty("subject")));
			}

			fis.close();

		} catch (Exception e) {

		}
	}

}
