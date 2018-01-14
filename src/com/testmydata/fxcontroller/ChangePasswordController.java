package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.testmydata.auditlog.StoreAuditLogger;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxutil.UndecoratorController;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.StaticImages;
import com.testmydata.vpn.VpnConnectionThread;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChangePasswordController implements Initializable {
	@FXML
	private static ChangePasswordController userHome = null;
	@FXML
	private ImageView homeicon, wrongtick, greentick, greentick1, wrongtick1, wrongtick2, greentick2;
	@FXML
	private JFXButton update;
	@FXML
	private JFXPasswordField newPasswordField, reTypeNewPasswordField, oldPasswordField;
	@FXML
	private AnchorPane passwordinformationanchor;
	Stage myStage;
	@FXML
	private Label changepasswordlabel;
	Boolean passwordvalid = false;
	// static ActionEvent event1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		StoreAuditLogger.logStoreTransaction(Loggedinuserdetails.userId, "Business Store Settings", "", "", true,
				"Opens Change Password", "");

		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
		changepasswordlabel.setEffect(ds);
		update.setEffect(ds);

		homeicon.setImage(StaticImages.homeicon.getImage());
		wrongtick.setImage(StaticImages.wrong_tick.getImage());
		wrongtick1.setImage(StaticImages.wrong_tick.getImage());
		wrongtick2.setImage(StaticImages.wrong_tick.getImage());
		greentick.setImage(StaticImages.green_tick.getImage());
		greentick1.setImage(StaticImages.green_tick.getImage());
		greentick2.setImage(StaticImages.green_tick.getImage());

		oldPasswordField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (oldPasswordField.getText() != null && !oldPasswordField.getText().trim().isEmpty()
					&& oldPasswordField.getText().trim() != "") {
				if (!newValue) { // when focus lost
					UsersDetailsBeanBinaryTrade loggedInUsersDetailsBeanBinaryTree = new DAO()
							.authenticateUser(Loggedinuserdetails.userId, oldPasswordField.getText().trim());
					if (loggedInUsersDetailsBeanBinaryTree.getLoginStatus().equals("success")) {
						greentick.setVisible(true);
						wrongtick.setVisible(false);
					} else {
						greentick.setVisible(false);
						wrongtick.setVisible(true);
					}
				}
				if (newValue) { // when focus gain
					UsersDetailsBeanBinaryTrade loggedInUsersDetailsBeanBinaryTree = new DAO()
							.authenticateUser(Loggedinuserdetails.userId, oldPasswordField.getText().trim());
					if (loggedInUsersDetailsBeanBinaryTree.getLoginStatus().equals("success")) {
						greentick.setVisible(true);
						wrongtick.setVisible(false);
					} else {
						greentick.setVisible(false);
						wrongtick.setVisible(true);
					}
				}
			}
		});
		newPasswordField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (newPasswordField.getText() != null && !newPasswordField.getText().trim().isEmpty()
					&& newPasswordField.getText().trim() != "") {
				if (!newValue) { // when focus lost
					if (!newPasswordField.getText()
							.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")) {
						greentick1.setVisible(false);
						wrongtick1.setVisible(true);
						passwordinformationanchor.setVisible(true);
						passwordvalid = false;
					} else {
						greentick1.setVisible(true);
						wrongtick1.setVisible(false);
						passwordinformationanchor.setVisible(false);
						passwordvalid = true;
					}
				}
				if (newValue) { // when focus gain
					if (!newPasswordField.getText()
							.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")) {
						greentick1.setVisible(false);
						wrongtick1.setVisible(true);
						passwordinformationanchor.setVisible(true);
						passwordvalid = false;
					} else {
						greentick1.setVisible(true);
						wrongtick1.setVisible(false);
						passwordinformationanchor.setVisible(false);
						passwordvalid = true;
					}
				}
			}
		});
		newPasswordField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					// Check if the new character is greater than LIMIT
					if (newPasswordField.getText().length() >= 20) {
						newPasswordField.setText(newPasswordField.getText().substring(0, 20));
					}
				}

			}
		});
		reTypeNewPasswordField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (reTypeNewPasswordField.getText() != null && !reTypeNewPasswordField.getText().trim().isEmpty()
					&& reTypeNewPasswordField.getText().trim() != "") {
				if (!newValue) { // when focus lost
					if (passwordvalid == true) {
						if (!reTypeNewPasswordField.getText().equals(newPasswordField.getText().trim())) {
							greentick2.setVisible(false);
							wrongtick2.setVisible(true);
							passwordinformationanchor.setVisible(true);
						} else {
							greentick2.setVisible(true);
							wrongtick2.setVisible(false);
							passwordinformationanchor.setVisible(false);
						}
					} else {
						greentick2.setVisible(false);
						wrongtick2.setVisible(true);
						passwordinformationanchor.setVisible(true);
					}
				}
				if (newValue) { // when focus gain
					if (passwordvalid == true) {
						if (!reTypeNewPasswordField.getText().equals(newPasswordField.getText().trim())) {
							greentick2.setVisible(false);
							wrongtick2.setVisible(true);
							passwordinformationanchor.setVisible(true);
						} else {
							greentick2.setVisible(true);
							wrongtick2.setVisible(false);
							passwordinformationanchor.setVisible(false);
						}
					} else {
						greentick2.setVisible(false);
						wrongtick2.setVisible(true);
						passwordinformationanchor.setVisible(true);
					}
				}
			}
		});

		homeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(MouseEvent event) {
				Cleanup scu = new Cleanup();
				ChangePasswordController nc = new ChangePasswordController();
				scu.nullifyStrings(nc);

				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();
				UndecoratorController.getInstance(null);
			}
		});
	}

	public static ChangePasswordController getInstance() {
		return userHome;
	}

	@FXML
	private void update(ActionEvent event) {
		if (oldPasswordField.getText().trim().isEmpty()) {
			CommonFunctions.message = "Please Enter Old Password.";
			CommonFunctions.invokeAlertBox(getClass());
		} else if (newPasswordField.getText().trim().isEmpty()) {
			CommonFunctions.message = "Please Enter New Password.";
			CommonFunctions.invokeAlertBox(getClass());
		} else if (newPasswordField.getText().length() < 12) {
			CommonFunctions.message = "Please Enter Minimum 12 Letters";
			CommonFunctions.invokeAlertBox(getClass());
		} else if (newPasswordField.getText().length() > 20) {
			CommonFunctions.message = "Please Enter Maximum 20 Letters";
			CommonFunctions.invokeAlertBox(getClass());
		} else if (reTypeNewPasswordField.getText().trim().isEmpty()) {
			CommonFunctions.message = "Please Re-Enter New Password.";
			CommonFunctions.invokeAlertBox(getClass());
		} else if (!newPasswordField.getText().trim().equals(reTypeNewPasswordField.getText().trim())) {
			CommonFunctions.message = "Passwords didn't match, Please Try Again.";
			CommonFunctions.invokeAlertBox(getClass());
		} else if (passwordvalid == false) {
			CommonFunctions.message = "Please Enter Valid Password according to Instructions shown";
			CommonFunctions.invokeAlertBox(getClass());
		} else {

			String status = new DAO().updateUserPassword("Users",
					Long.parseLong(Integer.toString(Loggedinuserdetails.id)), oldPasswordField.getText().trim(),
					newPasswordField.getText().trim(), Loggedinuserdetails.userId);

			if (status.equals("failure")) {
				CommonFunctions.message = "Password Updation Failed...!";
				CommonFunctions.invokeAlertBox(getClass());
			} else if (status.equals("error")) {
				CommonFunctions.message = "Internal Error Occured...!";
				CommonFunctions.invokeAlertBox(getClass());
			} else if (status.equals("passwordNotMatched")) {
				CommonFunctions.message = "Sorry! Old Password Not Matched. Please Try Again...!";
				CommonFunctions.invokeAlertBox(getClass());
			} else if (status.equals("success")) {
				CommonFunctions.message = "Password Changed Successfully " + "\n\n" + "System Will Shut Down." + "\n\n"
						+ "Please Restart the System...!";
				CommonFunctions.invokeAlertBox(getClass());

				StoreAuditLogger.logStoreTransaction(Loggedinuserdetails.userId, "Business Store Settings", "", "",
						true, "Sucessfully Changed Password", "");

				VpnConnectionThread.shutdown();
				Platform.exit();
				System.exit(0);
				// LogOut lock = new LogOut();
				// lock.relogin(event);
			}
		}
	}
}
