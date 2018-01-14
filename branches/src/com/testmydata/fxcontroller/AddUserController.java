package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxutil.UndecoratorController;
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
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddUserController implements Initializable {
	Stage myStage;
	@FXML
	private ImageView homeicon, wrongtick, greentick, wrongtick1, greentick1, pleasewait;
	@FXML
	private JFXComboBox<String> usercombo;
	@FXML
	private JFXTextField firstnametext, lastnametext, emailtext, securityquestiontext, usernametext;
	@FXML
	private JFXPasswordField securityanstext, passwordtext;
	@FXML
	private JFXCheckBox receiveemailcheck, newcrcheck, newffcheck, newtscheck, execrcheck, exetscheck, addusercheck,
			addqacheck, dasboardcheck, activatecheck, deactivatecheck;
	@FXML
	private AnchorPane passwordinformationanchor, transaprentanchor;
	@FXML
	private JFXButton save, update;
	boolean passwordvalid = false, emailvalid = false;
	static int email = 0, newcr = 0, newff = 0, newts = 0, crexe = 0, tsexe, adduser = 0, addqa = 0, dashboard = 0,
			activestatus = 0;
	static String[] combinedname = null;

	ArrayList<UsersDetailsBeanBinaryTrade> userslist = new ArrayList<UsersDetailsBeanBinaryTrade>();
	ArrayList<UsersDetailsBeanBinaryTrade> selecteduserlist = new ArrayList<UsersDetailsBeanBinaryTrade>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setexistingusers();
		homeicon.setImage(StaticImages.homeicon.getImage());
		wrongtick.setImage(StaticImages.wrong_tick.getImage());
		wrongtick1.setImage(StaticImages.wrong_tick.getImage());
		greentick.setImage(StaticImages.green_tick.getImage());
		greentick1.setImage(StaticImages.green_tick.getImage());
		pleasewait.setImage(StaticImages.source_run.getImage());

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
		homeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(MouseEvent event) {
				Cleanup scu = new Cleanup();
				AddUserController nc = new AddUserController();
				scu.nullifyStrings(nc);

				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();
				UndecoratorController.getInstance(null);
			}
		});
	}

	@FXML
	private void save() {
		if (validatefields()) {
			String status = new DAO().registerUser("users", "", Loggedinuserdetails.companyName,
					firstnametext.getText().trim(), lastnametext.getText().trim(), usernametext.getText().trim(),
					passwordtext.getText().trim(), emailtext.getText().trim(), securityquestiontext.getText().trim(),
					securityanstext.getText().trim(), Loggedinuserdetails.businessAddress,
					Loggedinuserdetails.cityprovince, Loggedinuserdetails.province, Loggedinuserdetails.postalCode,
					Loggedinuserdetails.mainService, Loggedinuserdetails.userLevel, Loggedinuserdetails.userId, null,
					Loggedinuserdetails.country, null, email, newcr, newff, newts, crexe, tsexe, adduser, addqa,
					dashboard);

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

	@FXML
	private void update() {
		if (validatefields()) {
			String status = new DAO().updateUser(firstnametext.getText(), lastnametext.getText(),
					usernametext.getText(), passwordtext.getText(), emailtext.getText(), securityquestiontext.getText(),
					securityanstext.getText(), Loggedinuserdetails.userId, email, newcr, newff, newts, crexe, tsexe,
					adduser, addqa, dashboard, activestatus, combinedname[0]);
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
			save.setVisible(false);
			update.setVisible(true);
		}
	}

	private boolean validatefields() {
		boolean result = true;
		if (firstnametext.getText() == null || firstnametext.getText().isEmpty()) {
			runmessage("Please Enter First Name...");
			result = false;
		} else if (lastnametext.getText() == null || lastnametext.getText().isEmpty()) {
			runmessage("Please Enter Last Name...");
			result = false;
		} else if (emailtext.getText() == null || emailtext.getText().isEmpty()) {
			runmessage("Please Enter E-Mail Address...");
			result = false;
		} else if (securityquestiontext.getText() == null || securityquestiontext.getText().isEmpty()) {
			runmessage("Please Enter Security Question...");
			result = false;
		} else if (securityanstext.getText() == null || securityanstext.getText().isEmpty()) {
			runmessage("Please Enter Security Answer to recover the Password...");
			result = false;
		} else if (usernametext.getText() == null || usernametext.getText().isEmpty()) {
			runmessage("Please Enter User ID...");
			result = false;
		} else if (passwordtext.getText() == null || passwordtext.getText().isEmpty()) {
			runmessage("Please Enter Password...");
			result = false;
		} else if (!passwordvalid) {
			runmessage("Password doesn't meet the Password Stength Policy...");
			result = false;
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
		newtscheck.setSelected(false);
		execrcheck.setSelected(false);
		exetscheck.setSelected(false);
		addusercheck.setSelected(false);
		addqacheck.setSelected(false);
		dasboardcheck.setSelected(false);
		save.setVisible(true);
		update.setVisible(false);
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
									greentick1.setVisible(true);
									transaprentanchor.setVisible(false);
								} else {
									emailtext.clear();
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
