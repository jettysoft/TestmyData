package com.testmydata.fxcontroller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.auditlog.StoreAuditLogger;
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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class EmailSettingsController implements Initializable {
	@FXML
	private static EmailSettingsController userHome = null;
	@FXML
	private ImageView closeicon, saveicon, wrongtick, greentick;
	@FXML
	private JFXTextField emailTxt, hosttext, porttext;
	@FXML
	private JFXPasswordField passTxt;
	@FXML
	private Label messagelabel;
	@FXML
	private AnchorPane actionanchor1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		StoreAuditLogger.logStoreTransaction(Loggedinuserdetails.userId, "Settings", "", "", true,
				"Opens Email Settings", "");
		populateEmailDetailsInitially();

		closeicon.setImage(StaticImages.homeicon.getImage());
		saveicon.setImage(StaticImages.save.getImage());
		wrongtick.setImage(StaticImages.wrong_tick.getImage());
		greentick.setImage(StaticImages.green_tick.getImage());

		Label savelbl = new Label("  Save ");
		savelbl.setStyle(StaticImages.lblStyle);
		savelbl.setMinWidth(40);
		savelbl.setLayoutX(65);
		savelbl.setLayoutY(15);
		savelbl.setVisible(false);
		actionanchor1.getChildren().add(savelbl);

		saveicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				savelbl.setVisible(true);
			}
		});
		saveicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				savelbl.setVisible(false);
			}
		});
		saveicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				save();
			}
		});

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

		closeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				AnchorPane pane = (AnchorPane) ((ImageView) t.getSource()).getParent().getParent().getParent();
				pane.getChildren().remove(pane.getChildren().size() - 1);

				QAServerSettingsController nc = new QAServerSettingsController();
				Cleanup.nullifyStrings(nc);
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

			if (passTxt.getText().length() != 0 && hosttext.getText().length() != 0
					&& porttext.getText().length() != 0) {
				HashMap<String, String> fileDetails = new HashMap<String, String>();
				fileDetails.put("email", EncryptAndDecrypt.encryptData(emailTxt.getText()));
				fileDetails.put("password", EncryptAndDecrypt.encryptData(passTxt.getText()));
				fileDetails.put("host", EncryptAndDecrypt.encryptData(hosttext.getText()));
				fileDetails.put("port", EncryptAndDecrypt.encryptData(porttext.getText()));
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
				hosttext.setText(EncryptAndDecrypt.decryptData(dafaultValuesPF.getProperty("host")));
				passTxt.setText(EncryptAndDecrypt.decryptData(dafaultValuesPF.getProperty("password")));
				porttext.setText(EncryptAndDecrypt.decryptData(dafaultValuesPF.getProperty("port")));
			}

			fis.close();

		} catch (Exception e) {

		}
	}

}
