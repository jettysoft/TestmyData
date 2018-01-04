package com.testmydata.fxcontroller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxhelpers.LoginFXHelper;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.SendMailUsingAuthentication;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ForgotPasswordController implements Initializable {

	private UsersDetailsBeanBinaryTrade UsersDetailsBeanBinaryTree;

	@FXML
	private JFXTextField securityquestion, answer;
	@FXML
	private Hyperlink backtologin;

	@FXML
	private JFXButton retrive;

	ArrayList<UsersDetailsBeanBinaryTrade> registeredUserDetailsArrayList;

	@FXML
	private JFXComboBox<String> usersCombobox;

	@FXML
	private Label lblClose, retrivepasswordlabel;

	@FXML
	Stage myStage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
		retrivepasswordlabel.setEffect(ds);

		usersCombobox.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");

		registeredUserDetailsArrayList = new DAO().getRegisteredUserDetails();
		for (UsersDetailsBeanBinaryTrade user : registeredUserDetailsArrayList) {
			usersCombobox.getItems().add(user.getUserId());
		}

		usersCombobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				int index = usersCombobox.getSelectionModel().getSelectedIndex();
				UsersDetailsBeanBinaryTree = registeredUserDetailsArrayList.get(index);

				securityquestion.setText(UsersDetailsBeanBinaryTree.getSecurityQuestion());
				answer.clear();
			}
		});

		/*
		 * lblClose.setOnMouseClicked((MouseEvent event) -> { Platform.exit();
		 * System.exit(0); });
		 */
	}

	@FXML
	private void validate(ActionEvent event) throws IOException {
		emailPasswordByValidateGivenAnswer(event);
	}

	private void emailPasswordByValidateGivenAnswer(ActionEvent event) {

		if (answer.getText().trim().toString().equals("")) {
			CommonFunctions.message = "Please answer for security question...!";
			// JOptionPane.showMessageDialog(null, "Please answer for security
			// question...!", "Forgot Password Status",
			// JOptionPane.ERROR_MESSAGE);
			CommonFunctions.invokeAlertBox(getClass());
		} else {
			if (answer.getText().trim().toString().equalsIgnoreCase(UsersDetailsBeanBinaryTree.getAnswer())) {

				String newPassword = new CommonFunctions().genRandomString(12);
				String updatePasswordStatus = new DAO().updatePassword(UsersDetailsBeanBinaryTree.getUserId(),
						newPassword);

				if (updatePasswordStatus.equals("success")) {

					String message = "Hi,<br/><br/>Your <b>Test My Data</b> Credentails Are : <br/><br/> User Name: "
							+ UsersDetailsBeanBinaryTree.getUserId() + "<br/>Password: " + newPassword
							+ "<br/><br/><br/><b>Note:</b> This is an auto generated email and please do not reply to this email.<br><br><a href='#'><b>Test My Data</b></a><br/>JettySoft LTD<br/>Vancouver, Canada <br> Email Disclaimer";
					String[] recipentsList = { UsersDetailsBeanBinaryTree.getEmailId() };

					SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();

					try {
						String returnValue = smtpMailSender.postMailWithOutAttachment(recipentsList,
								"Test My Data - Password Recovery Email", message,
								SendMailUsingAuthentication.SMTP_AUTH_USER);

						if (returnValue.equals("success")) {

							CommonFunctions.message = "Please check your registered email for new password...!";
							CommonFunctions.invokeAlertBox(getClass());

							Node source = (Node) event.getSource();
							myStage = (Stage) source.getScene().getWindow();
							myStage.close();

							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									new LoginFXHelper().initAndShowGUI("loginscreen");
								}
							});
						} else {
							CommonFunctions.message = "Sorry! Internal error occured. Plesae try again...!";
							CommonFunctions.invokeAlertBox(getClass());
						}

					} catch (Exception e) {
						CommonFunctions.message = "Sorry! Internal error occured. Plesae try again...!";
						CommonFunctions.invokeAlertBox(getClass());
					}

				} else {
					CommonFunctions.message = "Sorry! Internal error occured. Plesae try again...!";
					CommonFunctions.invokeAlertBox(getClass());
				}

			} else {
				CommonFunctions.message = "Sorry! Your entered wrong answer...!";
				CommonFunctions.invokeAlertBox(getClass());
			}
		}
	}

	@FXML
	private void backtoLogin(ActionEvent event) {
		Node source = (Node) event.getSource();
		myStage = (Stage) source.getScene().getWindow();
		myStage.close();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new LoginFXHelper().initAndShowGUI("loginscreen");
			}
		});
	}
}
