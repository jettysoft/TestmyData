package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.StaticImages;
import com.testmydata.util.ValidateRealEmail;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class AddUserController implements Initializable {

	@FXML
	private ImageView closeicon, wrongtick, greentick, wrongtick1, greentick1, pleasewait, saveicon, updateicon;
	@FXML
	private JFXComboBox<String> usercombo;
	@FXML
	private JFXTextField firstnametext, lastnametext, emailtext, securityquestiontext, usernametext;
	@FXML
	private JFXPasswordField securityanstext, passwordtext;
	@FXML
	private JFXCheckBox receiveemailcheck, newcrcheck, projectcheck, newffcheck, newtscheck, execrcheck, exetscheck,
			addusercheck, addqacheck, dasboardcheck, activatecheck, deactivatecheck, newbugcheck, viewbugcheck,
			downloadcheck, viewresultscheck, bugservercheck;
	@FXML
	private AnchorPane passwordinformationanchor, transaprentanchor, actionanchor1;
	boolean passwordvalid = false, emailvalid = false;
	static int email = 0, newcr = 0, newff = 0, newproject = 0, newts = 0, crexe = 0, tsexe, adduser = 0, addqa = 0,
			dashboard = 0, activestatus = 0, newbug = 0, viewbug = 0, download = 0, viewresults = 0, bugserver = 0;
	static String[] combinedname = null;

	ArrayList<UsersDetailsBeanBinaryTrade> userslist = new ArrayList<UsersDetailsBeanBinaryTrade>();
	ArrayList<UsersDetailsBeanBinaryTrade> selecteduserlist = new ArrayList<UsersDetailsBeanBinaryTrade>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setexistingusers();
		closeicon.setImage(StaticImages.closeicon);
		wrongtick.setImage(StaticImages.wrong_tick);
		wrongtick1.setImage(StaticImages.wrong_tick);
		greentick.setImage(StaticImages.green_tick);
		greentick1.setImage(StaticImages.green_tick);
		pleasewait.setImage(StaticImages.source_run);
		saveicon.setImage(StaticImages.save);
		updateicon.setImage(StaticImages.save);

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

		Label updatelbl = new Label(" Update ");
		updatelbl.setStyle(StaticImages.lblStyle);
		updatelbl.setMinWidth(40);
		updatelbl.setLayoutX(65);
		updatelbl.setLayoutY(15);
		updatelbl.setVisible(false);
		actionanchor1.getChildren().add(updatelbl);

		updateicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				updatelbl.setVisible(true);
			}
		});
		updateicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				updatelbl.setVisible(false);
			}
		});
		updateicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				update();
			}
		});

		emailtext.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (!newValue) { // when focus lost
				if (emailtext.getText() != null && !emailtext.getText().trim().isEmpty()
						&& emailtext.getText().trim() != "") {
					if (!emailtext.getText().matches("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})")) {
						emailtext.clear();
					}
					if (emailtext.getText().matches("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})")) {
						transaprentanchor.setVisible(true);
						emailvalidationservice.reset();
						emailvalidationservice.start();
					}
				}
			}

		});

		usercombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select User")) {
						// load the selected user details
						combinedname = usercombo.getSelectionModel().getSelectedItem().split("-");
						loaduserdetails(combinedname[0]);
					} else {
						clearfields();
					}
				} catch (NullPointerException ne) {
					ne.printStackTrace();
				}
			}
		});

		activatecheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (activatecheck.isSelected()) {
				activestatus = 1;
			} else {
				activestatus = 0;
			}
		});
		deactivatecheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (deactivatecheck.isSelected()) {
				activestatus = 0;
			} else {
				activestatus = 1;
			}
		});
		receiveemailcheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (receiveemailcheck.isSelected()) {
				email = 1;
			} else {
				email = 0;
			}
		});
		newcrcheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (newcrcheck.isSelected()) {
				newcr = 1;
			} else {
				newcr = 0;
			}
		});
		projectcheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (projectcheck.isSelected()) {
				newproject = 1;
			} else {
				newproject = 0;
			}
		});
		newffcheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (newffcheck.isSelected()) {
				newff = 1;
			} else {
				newff = 0;
			}
		});
		newtscheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (newtscheck.isSelected()) {
				newts = 1;
			} else {
				newts = 0;
			}
		});
		execrcheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (execrcheck.isSelected()) {
				crexe = 1;
			} else {
				crexe = 0;
			}
		});
		exetscheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (exetscheck.isSelected()) {
				tsexe = 1;
			} else {
				tsexe = 0;
			}
		});
		addusercheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (addusercheck.isSelected()) {
				adduser = 1;
			} else {
				adduser = 0;
			}
		});
		addqacheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (addqacheck.isSelected()) {
				addqa = 1;
			} else {
				addqa = 0;
			}
		});
		dasboardcheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (dasboardcheck.isSelected()) {
				dashboard = 1;
			} else {
				dashboard = 0;
			}
		});
		newbugcheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (newbugcheck.isSelected()) {
				newbug = 1;
			} else {
				newbug = 0;
			}
		});
		viewbugcheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (viewbugcheck.isSelected()) {
				viewbug = 1;
			} else {
				viewbug = 0;
			}
		});
		downloadcheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (downloadcheck.isSelected()) {
				download = 1;
			} else {
				download = 0;
			}
		});
		viewresultscheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (viewresultscheck.isSelected()) {
				viewresults = 1;
			} else {
				viewresults = 0;
			}
		});
		bugservercheck.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (bugservercheck.isSelected()) {
				bugserver = 1;
			} else {
				bugserver = 0;
			}
		});

		passwordtext.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (passwordtext.getText() != null && !passwordtext.getText().trim().isEmpty()
					&& passwordtext.getText().trim() != "") {
				if (!newValue) { // when focus lost
					if (!passwordtext.getText()
							.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")) {
						greentick.setVisible(false);
						wrongtick.setVisible(true);
						passwordinformationanchor.setVisible(true);
						passwordvalid = false;
					} else {
						greentick.setVisible(true);
						wrongtick.setVisible(false);
						passwordinformationanchor.setVisible(false);
						passwordvalid = true;
					}
				}
				if (newValue) { // when focus gain
					if (!passwordtext.getText()
							.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")) {
						greentick.setVisible(false);
						wrongtick.setVisible(true);
						passwordinformationanchor.setVisible(true);
						passwordvalid = false;
					} else {
						greentick.setVisible(true);
						wrongtick.setVisible(false);
						passwordinformationanchor.setVisible(false);
						passwordvalid = true;
					}
				}
			}
		});

		// closing screen when clicks on home icon
		closeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				AnchorPane pane = (AnchorPane) ((ImageView) t.getSource()).getParent().getParent().getParent();
				pane.getChildren().remove(pane.getChildren().size() - 1);

				AddUserController nc = new AddUserController();
				Cleanup.nullifyStrings(nc);
			}
		});
	}

	private void save() {
		if (validatefields()) {
			String status = new DAO().registerUser("users", "", Loggedinuserdetails.companyName,
					firstnametext.getText().trim(), lastnametext.getText().trim(), usernametext.getText().trim(),
					passwordtext.getText().trim(), emailtext.getText().trim(), securityquestiontext.getText().trim(),
					securityanstext.getText().trim(), Loggedinuserdetails.businessAddress,
					Loggedinuserdetails.cityprovince, Loggedinuserdetails.province, Loggedinuserdetails.postalCode,
					Loggedinuserdetails.mainService, Loggedinuserdetails.userLevel, Loggedinuserdetails.userId, null,
					Loggedinuserdetails.country, null, email, newcr, newff, newts, crexe, tsexe, adduser, addqa,
					dashboard, download, viewresults, newbug, viewbug, bugserver, newproject);

			if (status == "failure") {
				runmessage("Failed to Add New User. Please Try Again...");
			} else if (status == "error") {
				runmessage("Internal error occired. Please Try Restarting the 'Test my Data'...");
			} else if (status.equals("alreadyExisted")) {
				runmessage("Sorry! An User Is Already Existed With Given User ID\n\nPlease Try with new User ID...");
			} else if (status == "success") {
				clearfields();
				runmessage("Successfully user was added to System. User can login now to use 'Test my Data'");
			}
		}
	}

	private void update() {
		if (validatefields()) {
			String status = new DAO().updateUser(firstnametext.getText(), lastnametext.getText(),
					usernametext.getText(), passwordtext.getText(), emailtext.getText(), securityquestiontext.getText(),
					securityanstext.getText(), Loggedinuserdetails.userId, email, newcr, newff, newts, crexe, tsexe,
					adduser, addqa, dashboard, activestatus, combinedname[0], download, viewresults, newbug, viewbug,
					bugserver, newproject);
			if (status == "failure") {
				runmessage("Failed to Update New User. Please Try Again...");
			} else if (status == "error") {
				runmessage("Internal error occired. Please Try Restarting the 'Test my Data'...");
			} else if (status == "success") {
				clearfields();
				runmessage("Successfully user info was Updated. User can login now to use 'Test my Data'");
			}
		}
	}

	private void setexistingusers() {
		usercombo.getItems().clear();
		usercombo.getItems().add("Select User");
		userslist = new DAO().getuserlist();
		if (userslist != null && userslist.size() > 0) {
			for (int i = 0; i < userslist.size(); i++) {
				usercombo.getItems().add(userslist.get(i).getCombinedname());
			}
		}
		usercombo.getSelectionModel().select(0);
	}

	private void loaduserdetails(String id) {
		selecteduserlist = new DAO().getselecteduserdetails(id);
		if (selecteduserlist != null && selecteduserlist.size() > 0) {
			if (selecteduserlist.get(0).getIsactive().equals("1")) {
				deactivatecheck.setVisible(true);
				activestatus = 1;
			} else {
				activatecheck.setVisible(true);
				activestatus = 0;
			}

			firstnametext.setText(selecteduserlist.get(0).getFirstName());
			lastnametext.setText(selecteduserlist.get(0).getLastName());
			emailtext.setText(selecteduserlist.get(0).getEmailId());
			if (selecteduserlist.get(0).getEmail().equals("1")) {
				receiveemailcheck.setSelected(true);
			} else {
				receiveemailcheck.setSelected(false);
			}
			securityquestiontext.setText(selecteduserlist.get(0).getSecurityQuestion());
			securityanstext.setText(selecteduserlist.get(0).getAnswer());
			usernametext.setText(selecteduserlist.get(0).getUserId());
			if (selecteduserlist.get(0).getNewcr().equals("1")) {
				newcrcheck.setSelected(true);
				newcr = 1;
			} else {
				newcrcheck.setSelected(false);
				newcr = 0;
			}
			if (selecteduserlist.get(0).getNewff().equals("1")) {
				newffcheck.setSelected(true);
				newff = 1;
			} else {
				newffcheck.setSelected(false);
				newff = 0;
			}
			if (selecteduserlist.get(0).getProjectaccess().equals("1")) {
				projectcheck.setSelected(true);
				newproject = 1;
			} else {
				projectcheck.setSelected(false);
				newproject = 0;
			}
			if (selecteduserlist.get(0).getNewts().equals("1")) {
				newtscheck.setSelected(true);
				newts = 1;
			} else {
				newtscheck.setSelected(false);
				newts = 0;
			}
			if (selecteduserlist.get(0).getCrexe().equals("1")) {
				execrcheck.setSelected(true);
				crexe = 1;
			} else {
				execrcheck.setSelected(false);
				crexe = 0;
			}
			if (selecteduserlist.get(0).getTsexe().equals("1")) {
				exetscheck.setSelected(true);
				tsexe = 1;
			} else {
				exetscheck.setSelected(false);
				tsexe = 0;
			}
			if (selecteduserlist.get(0).getAdduser().equals("1")) {
				addusercheck.setSelected(true);
				adduser = 1;
			} else {
				addusercheck.setSelected(false);
				adduser = 0;
			}
			if (selecteduserlist.get(0).getAddqa().equals("1")) {
				addqacheck.setSelected(true);
				addqa = 1;
			} else {
				addqacheck.setSelected(false);
				addqa = 0;
			}
			if (selecteduserlist.get(0).getDashboard().equals("1")) {
				dasboardcheck.setSelected(true);
				dashboard = 1;
			} else {
				dasboardcheck.setSelected(false);
				dashboard = 0;
			}
			if (selecteduserlist.get(0).getReports().equals("1")) {
				downloadcheck.setSelected(true);
				download = 1;
			} else {
				downloadcheck.setSelected(false);
				download = 0;
			}
			if (selecteduserlist.get(0).getTestresults().equals("1")) {
				viewresultscheck.setSelected(true);
				viewresults = 1;
			} else {
				viewresultscheck.setSelected(false);
				viewresults = 0;
			}
			if (selecteduserlist.get(0).getNewbug().equals("1")) {
				newbugcheck.setSelected(true);
				newbug = 1;
			} else {
				newbugcheck.setSelected(false);
				newbug = 0;
			}
			if (selecteduserlist.get(0).getViewbug().equals("1")) {
				viewbugcheck.setSelected(true);
				viewbug = 1;
			} else {
				viewbugcheck.setSelected(false);
				viewbug = 0;
			}
			if (selecteduserlist.get(0).getAddbugserver().equals("1")) {
				bugservercheck.setSelected(true);
				bugserver = 1;
			} else {
				bugservercheck.setSelected(false);
				bugserver = 0;
			}
			saveicon.setVisible(false);
			updateicon.setVisible(true);
		}
	}

	private boolean validatefields() {
		boolean result = true;
		StringBuffer message = new StringBuffer();
		if (firstnametext.getText() == null || firstnametext.getText().isEmpty()) {
			result = false;
			firstnametext.setUnFocusColor(Color.RED);
			message.append("Please Enter First Name...\n\n");
		} else {
			firstnametext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (lastnametext.getText() == null || lastnametext.getText().isEmpty()) {
			result = false;
			lastnametext.setUnFocusColor(Color.RED);
			message.append("Please Enter Last Name...\n\n");
		} else {
			lastnametext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (emailtext.getText() == null || emailtext.getText().isEmpty()) {
			result = false;
			emailtext.setUnFocusColor(Color.RED);
			message.append("Please Enter E-Mail Address...\n\n");
		} else {
			emailtext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (securityquestiontext.getText() == null || securityquestiontext.getText().isEmpty()) {
			result = false;
			securityquestiontext.setUnFocusColor(Color.RED);
			message.append("Please Enter Security Question...\n\n");
		} else {
			securityquestiontext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (securityanstext.getText() == null || securityanstext.getText().isEmpty()) {
			result = false;
			securityanstext.setUnFocusColor(Color.RED);
			message.append("Please Enter Security Answer to recover the Password...\n\n");
		} else {
			securityanstext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (usernametext.getText() == null || usernametext.getText().isEmpty()) {
			result = false;
			usernametext.setUnFocusColor(Color.RED);
			message.append("Please Enter User ID...\n\n");
		} else {
			usernametext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (passwordtext.getText() == null || passwordtext.getText().isEmpty()) {
			result = false;
			passwordtext.setUnFocusColor(Color.RED);
			message.append("Please Enter Password...\n\n");
		} else {
			passwordtext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (!passwordvalid) {
			result = false;
			passwordtext.setUnFocusColor(Color.RED);
			message.append("Password doesn't meet the Password Stength Policy...\n\n");
		} else {
			passwordtext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (!result) {
			runmessage(message.toString());
		}

		return result;
	}

	private void clearfields() {
		firstnametext.clear();
		lastnametext.clear();
		emailtext.clear();
		securityquestiontext.clear();
		securityanstext.clear();
		usernametext.clear();
		passwordtext.clear();
		receiveemailcheck.setSelected(false);
		newcrcheck.setSelected(false);
		newffcheck.setSelected(false);
		projectcheck.setSelected(false);
		newtscheck.setSelected(false);
		execrcheck.setSelected(false);
		exetscheck.setSelected(false);
		addusercheck.setSelected(false);
		addqacheck.setSelected(false);
		dasboardcheck.setSelected(false);
		downloadcheck.setSelected(false);
		viewresultscheck.setSelected(false);
		newbugcheck.setSelected(false);
		viewbugcheck.setSelected(false);
		bugservercheck.setSelected(false);
		saveicon.setVisible(true);
		updateicon.setVisible(false);
		usercombo.getSelectionModel().select(0);
		greentick.setVisible(false);
		greentick1.setVisible(false);
		wrongtick.setVisible(false);
		wrongtick1.setVisible(false);
		deactivatecheck.setVisible(false);
		activatecheck.setVisible(false);
	}

	// Method to display the Messages
	public void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	// Runs in Background and updates UI Responsively
	Service<Void> emailvalidationservice = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					// Background work

					emailvalid = ValidateRealEmail.validateemail(emailtext.getText().trim());

					final CountDownLatch latch = new CountDownLatch(1);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								if (emailvalid) {
									wrongtick1.setVisible(false);
									greentick1.setVisible(true);
									transaprentanchor.setVisible(false);
								} else {
									emailtext.clear();
									greentick1.setVisible(false);
									wrongtick1.setVisible(true);
									transaprentanchor.setVisible(false);

									runmessage("Email ID Doesn't Exist. Please enter Valid E-Mail Only...");
								}
							} finally {
								latch.countDown();
							}
						}
					});
					latch.await();

					// Keep with the background work
					return null;
				}
			};
		}
	};
}
