package com.testmydata.fxcontroller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.dao.DAO;
import com.testmydata.fxhelpers.WelcomePageFXHelper;
import com.testmydata.main.WelcomePage;
import com.testmydata.util.EncryptAndDecrypt;
import com.testmydata.util.StaticImages;
import com.testmydata.util.TrademenException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WelcomeController implements Initializable {

	@FXML
	private JFXTextField reg_key_text;

	@FXML
	private Hyperlink already_registered;

	@FXML
	private ImageView imageView;

	@FXML
	private ImageView wrongimageView;

	@FXML
	private JFXButton validate;

	@FXML
	private Label lblClose;

	@FXML
	Stage stage, myStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

		validate.setEffect(ds);
		already_registered.setEffect(ds);

		imageView.setImage(StaticImages.green_tick.getImage());
		wrongimageView.setImage(StaticImages.wrong_tick.getImage());

		reg_key_text.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");

		reg_key_text.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					// Check if the new character is greater than LIMIT
					if (reg_key_text.getText().length() >= 24) {
						reg_key_text.setText(reg_key_text.getText().substring(0, 24));
					}
					if (reg_key_text.getText().length() == 24) {

						try {
							DAO dao = new DAO("testmydata");
							dao.establishRemoteDBConnection();
							String status = dao.validateRegistrationKey(reg_key_text.getText().toString());
							if (status == "success") {
								imageView.setVisible(true);
								wrongimageView.setVisible(false);
							} else {
								imageView.setVisible(false);
								wrongimageView.setVisible(true);
							}
						} catch (TrademenException e) {

						}

					}
				}
			}
		});

	}

	@FXML
	private void already_registered(ActionEvent event) {
		Node source = (Node) event.getSource();
		myStage = (Stage) source.getScene().getWindow();
		myStage.close();

		WelcomePage.loginApp();
	}

	@FXML
	private boolean validate(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("The Business Store - Registration key");
		alert.setHeaderText(null);

		if (reg_key_text.getText().length() == 24) {

			String enteredKey = reg_key_text.getText().toString().trim();
			try {
				DAO dao = new DAO("testmydata");
				dao.establishRemoteDBConnection();

				String status = dao.validateRegistrationKey(enteredKey);

				if (status.equals("success")) {

					imageView.setVisible(true);

					Properties keysPropertiesFile = new Properties();
					FileOutputStream fos = new FileOutputStream(
							new File(".", "/conf/keys.properties").getAbsolutePath());
					keysPropertiesFile.setProperty("registedKey", EncryptAndDecrypt.encryptData(enteredKey));
					keysPropertiesFile.setProperty("isUserAlreadyRegisted", EncryptAndDecrypt.encryptData("false"));
					keysPropertiesFile.store(fos, new Date().toString());
					fos.close();

					Node source = (Node) event.getSource();
					myStage = (Stage) source.getScene().getWindow();
					myStage.close();

					// Passing String value from one controller to another
					// controller
					// run(enteredKey);

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							String screenName = "registration";
							new WelcomePageFXHelper().initAndShowGUI(screenName);
						}
					});

				} else if (status.equals("alreadyRegistered")) {
					alert.setContentText("Registration key already registered...!");
					alert.showAndWait();
					return false;
				} else if (status.equals("invalidKey")) {
					wrongimageView.setVisible(true);
					alert.setContentText("Invalid Registration Key. Please Contact the Administrator...!");
					alert.showAndWait();
					return false;
				} else if (status.equals("failure")) {
					alert.setContentText("Unable to validate registration key...!");
					alert.showAndWait();
					return false;
				} else {
					alert.setContentText("Internal Error Occured...!");
					alert.showAndWait();
					return false;
				}
			} catch (TrademenException e) {
				alert.setContentText("Internal Error Occured...!");
				alert.showAndWait();
				return false;
			}
		} else {
			imageView.setVisible(false);
			wrongimageView.setVisible(true);
		}
		return false;
	}

	@SuppressWarnings("unused")
	public void run(String key) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		RegistrationController rgc = (RegistrationController) fxmlLoader.getController();
		// rgc.register(null, key);
	}
}
