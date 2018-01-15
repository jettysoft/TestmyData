package com.testmydata.fxcontroller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.DBConfigBinaryTade;
import com.testmydata.binarybeans.UserTypeBeanBinaryTrade;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxhelpers.LoginFXHelper;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.DBConfigJAXB;
import com.testmydata.util.EncryptAndDecrypt;
import com.testmydata.util.StaticImages;
import com.testmydata.util.ValidateRealEmail;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RegistrationController implements Initializable {

	@FXML
	private JFXTextField companynames, firstname, lastname, streetno, city, province, postalcode, mainservice, username,
			emailid, securityquestion;
	@FXML
	private JFXPasswordField password, confirmpassword, answer;
	@FXML
	private JFXButton register;
	@FXML
	private ImageView greentick, wrongtick, greentick2, wrongtick2, greentick3, wrongtick3;
	@FXML
	private Label registerlabel;
	@FXML
	private AnchorPane passwordinformationanchor;
	@FXML
	private JFXComboBox<String> industrycombo;
	@FXML
	private JFXCheckBox receiveemailcheck;

	Boolean passwordvalid = false;
	String registrationKey, userLevel, usernames;
	@FXML
	Stage myStage;
	ArrayList<UsersDetailsBeanBinaryTrade> webRegistrationInfolist = null;
	String[] industrylist = { "OTHER" };

	String selectedindustry = null, country = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		wrongtick.setImage(StaticImages.wrong_tick.getImage());
		wrongtick2.setImage(StaticImages.wrong_tick.getImage());
		wrongtick3.setImage(StaticImages.wrong_tick.getImage());
		greentick.setImage(StaticImages.green_tick.getImage());
		greentick2.setImage(StaticImages.green_tick.getImage());
		greentick3.setImage(StaticImages.green_tick.getImage());

		industrycombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");

		industrycombo.getItems().clear();
		for (int i = 0; i < industrylist.length; i++) {
			industrycombo.getItems().addAll(industrylist[i]);
		}
		industrycombo.getSelectionModel().select(0);

		industrycombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				selectedindustry = industrycombo.getSelectionModel().getSelectedItem();
			}
		});

		password.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (password.getText() != null && !password.getText().trim().isEmpty() && password.getText().trim() != "") {
				if (!newValue) { // when focus lost
					if (!password.getText()
							.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")) {
						greentick2.setVisible(false);
						wrongtick2.setVisible(true);
						passwordinformationanchor.setVisible(true);
						passwordvalid = false;
					} else {
						greentick2.setVisible(true);
						wrongtick2.setVisible(false);
						passwordinformationanchor.setVisible(false);
						passwordvalid = true;
					}
				}
				if (newValue) { // when focus gain
					if (!password.getText()
							.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")) {
						greentick2.setVisible(false);
						wrongtick2.setVisible(true);
						passwordinformationanchor.setVisible(true);
						passwordvalid = false;
					} else {
						greentick2.setVisible(true);
						wrongtick2.setVisible(false);
						passwordinformationanchor.setVisible(false);
						passwordvalid = true;
					}
				}
			}
		});
		password.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					// Check if the new character is greater than LIMIT
					if (password.getText().length() >= 20) {
						password.setText(password.getText().substring(0, 20));
					}
				}

			}
		});
		confirmpassword.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (confirmpassword.getText() != null && !confirmpassword.getText().trim().isEmpty()
					&& confirmpassword.getText().trim() != "") {
				if (!newValue) { // when focus lost
					if (passwordvalid == true) {
						if (!confirmpassword.getText().equals(password.getText().trim())) {
							greentick.setVisible(false);
							wrongtick.setVisible(true);
							passwordinformationanchor.setVisible(true);
						} else {
							greentick.setVisible(true);
							wrongtick.setVisible(false);
							passwordinformationanchor.setVisible(false);
						}
					} else {
						greentick.setVisible(false);
						wrongtick.setVisible(true);
						passwordinformationanchor.setVisible(true);
					}
				}
				if (newValue) { // when focus gain
					if (passwordvalid == true) {
						if (!confirmpassword.getText().equals(password.getText().trim())) {
							greentick.setVisible(false);
							wrongtick.setVisible(true);
							passwordinformationanchor.setVisible(true);
						} else {
							greentick.setVisible(true);
							wrongtick.setVisible(false);
							passwordinformationanchor.setVisible(false);
						}
					} else {
						greentick.setVisible(false);
						wrongtick.setVisible(true);
						passwordinformationanchor.setVisible(true);
					}
				}
			}
		});
		postalcode.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (postalcode.getText().length() >= 6) {
						postalcode.setText(postalcode.getText().substring(0, 6));
					}
				}
			}
		});
		emailid.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (!newValue) { // when focus lost
				if (emailid.getText() != null && !emailid.getText().trim().isEmpty()
						&& emailid.getText().trim() != "") {
					if (!emailid.getText().matches("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})")) {
						emailid.clear();
					}
					if (emailid.getText().matches("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})")) {
						Boolean result = false;

						result = ValidateRealEmail.validateemail(emailid.getText().trim());

						if (result == true) {
							greentick3.setVisible(true);
							wrongtick3.setVisible(false);
							CommonFunctions.invokePleaseWait(getClass(), result);
						} else {
							greentick3.setVisible(false);
							wrongtick3.setVisible(true);
							CommonFunctions.invokePleaseWait(getClass(), result);

							CommonFunctions.message = "Email ID Doesn't Exist. Please enter Valid E-Mail Only...!";
							CommonFunctions.invokeAlertBox(getClass());
							emailid.clear();
						}
					}
				}
			}

		});
		companynames.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					// Check if the new character is greater than LIMIT
					if (companynames.getText().length() >= 255) {
						companynames.setText(companynames.getText().substring(0, 255));
					}
				}

			}
		});

		firstname.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (firstname.getText().length() >= 255) {
						firstname.setText(firstname.getText().substring(0, 255));
					}
				}

			}
		});
		lastname.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (lastname.getText().length() >= 255) {
						lastname.setText(lastname.getText().substring(0, 255));
					}
				}

			}
		});
		streetno.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (streetno.getText().length() >= 255) {
						streetno.setText(streetno.getText().substring(0, 255));
					}
				}

			}
		});
		city.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (city.getText().length() >= 255) {
						city.setText(city.getText().substring(0, 255));
					}
				}

			}
		});
		province.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (province.getText().length() >= 255) {
						province.setText(province.getText().substring(0, 255));
					}
				}

			}
		});
		postalcode.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (postalcode.getText().length() >= 6) {
						postalcode.setText(postalcode.getText().substring(0, 6));
					}
				}

			}
		});
		mainservice.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (mainservice.getText().length() >= 255) {
						mainservice.setText(mainservice.getText().substring(0, 255));
					}
				}

			}
		});
		username.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (username.getText().length() >= 200) {
						username.setText(username.getText().substring(0, 200));
					}
				}

			}
		});
		emailid.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (emailid.getText().length() >= 300) {
						emailid.setText(emailid.getText().substring(0, 300));
					}
				}

			}
		});
		securityquestion.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (securityquestion.getText().length() >= 255) {
						securityquestion.setText(securityquestion.getText().substring(0, 255));
					}
				}

			}
		});
		answer.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (answer.getText().length() >= 255) {
						answer.setText(answer.getText().substring(0, 255));
					}
				}

			}
		});

		Properties keysPropertiesFile = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(".", "/conf/keys.properties").getAbsolutePath());
			keysPropertiesFile.load(fis);
			registrationKey = EncryptAndDecrypt.decryptData(keysPropertiesFile.getProperty("registedKey"));
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		setInformation();
	}

	private void setInformation() {
		webRegistrationInfolist = new DAO().getWebRegistrationInformation(registrationKey);

		if (webRegistrationInfolist != null && !webRegistrationInfolist.isEmpty()
				&& webRegistrationInfolist.size() != 0) {
			companynames.setText(webRegistrationInfolist.get(0).getCompanyName());
			streetno.setText(webRegistrationInfolist.get(0).getStreetname());
			city.setText(webRegistrationInfolist.get(0).getCityprovince());
			province.setText(webRegistrationInfolist.get(0).getProvince());
			postalcode.setText(webRegistrationInfolist.get(0).getPostalCode());
			username.setText(webRegistrationInfolist.get(0).getUserId());
			firstname.setText(webRegistrationInfolist.get(0).getFirstName());
			lastname.setText(webRegistrationInfolist.get(0).getLastName());
			password.setText(webRegistrationInfolist.get(0).getPassword());
			confirmpassword.setText(webRegistrationInfolist.get(0).getPassword());
			emailid.setText(webRegistrationInfolist.get(0).getEmailId());
			securityquestion.setText(webRegistrationInfolist.get(0).getSecurityQuestion());
			answer.setText(webRegistrationInfolist.get(0).getAnswer());
			country = webRegistrationInfolist.get(0).getCountry();
		} else {
			companynames.clear();
			streetno.clear();
			city.clear();
			province.clear();
			postalcode.clear();
			username.clear();
			firstname.clear();
			lastname.clear();
			password.clear();
			confirmpassword.clear();
			emailid.clear();
			securityquestion.clear();
			answer.clear();
			country = null;
		}
	}

	@SuppressWarnings({ "static-access" })
	@FXML
	public boolean register(ActionEvent event) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("The Business Store - Registration Information");
		alert.setHeaderText(null);

		try {
			if (companynames.getText() == null || companynames.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter Company Name");
				alert.showAndWait();
				return false;
			} else if (streetno.getText() == null || streetno.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter Street Address");
				alert.showAndWait();
				return false;
			} else if (city.getText() == null || city.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter City");
				alert.showAndWait();
				return false;
			} else if (province.getText() == null || province.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter Province");
				alert.showAndWait();
				return false;
			} else if (postalcode.getText() == null || postalcode.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter Postal Code");
				alert.showAndWait();
				return false;
			} else if (mainservice.getText() == null || mainservice.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter Your Company Main Services");
				alert.showAndWait();
				return false;
			} else if (firstname.getText() == null || firstname.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter First Name");
				alert.showAndWait();
				return false;
			} else if (lastname.getText() == null || lastname.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter Last Name");
				alert.showAndWait();
				return false;
			} else if (username.getText() == null || username.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter User Id");
				alert.showAndWait();
				return false;
			} else if (password.getText() == null || password.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter Password");
				alert.showAndWait();
				return false;
			} else if (password.getText().length() < 12) {
				alert.setContentText("Password should have minimum 12 Letters");
				alert.showAndWait();
				return false;
			} else if (password.getText().length() > 20) {
				alert.setContentText("Password should have maximum 20 Letters");
				alert.showAndWait();
				return false;
			} else if (confirmpassword.getText() == null || confirmpassword.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter Password to Confirm");
				alert.showAndWait();
				return false;
			} else if (!password.getText().trim().equals(confirmpassword.getText().trim())) {
				alert.setContentText("Password & Confirm-Password doesn't match");
				alert.showAndWait();
				return false;
			} else if (passwordvalid == false) {
				alert.setContentText("Please Enter Valid Password according to Instructions shown");
				alert.showAndWait();
				return false;
			} else if (emailid.getText() == null || emailid.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter Email Id");
				alert.showAndWait();
				return false;
			} else if (!emailid.getText().trim().isEmpty()
					&& !new CommonFunctions().validateEmail(emailid.getText().trim())) {
				alert.setContentText("Please Enter Valid Email Id");
				alert.showAndWait();
				return false;
			} else if (securityquestion.getText() == null || securityquestion.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter Security Suestion");
				alert.showAndWait();
				return false;
			} else if (answer.getText() == null || answer.getText().trim().isEmpty()) {
				alert.setContentText("Please Enter Answer For Security Suestion");
				alert.showAndWait();
				return false;
			} else {

				CommonFunctions omm = new CommonFunctions();
				String companyName = companynames.getText().toLowerCase().trim().replaceAll(" ", "");
				String upToNCharacters = companyName.substring(0, Math.min(companyName.length(), 40));
				omm.updateCompanyName(upToNCharacters.toLowerCase());

				// updating the Company name as DB name in the dbConfig.xml
				try {
					DBConfigJAXB dbC = new DBConfigJAXB();
					DBConfigBinaryTade dbConfig = dbC.readDBConfig();

					dbConfig.setDbName(EncryptAndDecrypt.encryptData(upToNCharacters.toLowerCase()));

					dbC.generateDBConfig(dbConfig);
				} catch (Exception e1) {
					alert.setContentText("DBConfiguration File is Missing, Please contact JettySoft support");
					alert.showAndWait();
					return false;
				}

				boolean dbstatus = new DAO().createDB();

				// Registration rg = new Registration(registrationKey);
				if (dbstatus) {
					getWebuserLevel(registrationKey);
					int email = 0;

					if (receiveemailcheck.isSelected()) {
						email = 1;
					} else {
						email = 0;
					}

					String status = new DAO().registerUser("users", registrationKey, companynames.getText().trim(),
							firstname.getText().trim(), lastname.getText().trim(), username.getText().trim(),
							password.getText().trim(), emailid.getText().trim(), securityquestion.getText().trim(),
							answer.getText().trim(), streetno.getText().trim(), city.getText().trim(),
							province.getText().trim(), postalcode.getText().trim(), mainservice.getText().trim(),
							userLevel, username.getText().trim(), selectedindustry, country, null, email, 1, 1, 1, 1, 1,
							1, 1, 1, 1, 1, 1, 1, 1);
					usernames = username.getText().trim();

					if (status == "failure") {
						alert.setContentText("Registration Failed...!");
						alert.showAndWait();
						return false;
					} else if (status == "error") {
						alert.setContentText("Internal Error Occured...!");
						alert.showAndWait();
						return false;
					} else if (status.equals("alreadyExisted")) {
						alert.setContentText("Sorry! An User Is Already Existed With Given User Id...!");
						alert.showAndWait();
						return false;
					} else if (status == "success") {
						CommonFunctions cm = new CommonFunctions();
						cm.updateKeyProperties();

						DAO dao = new DAO("testmydata");
						dao.establishRemoteDBConnection();
						dao.updateRegistrationKeys(registrationKey);

						new DAO().updateUserBISupportDetails(registrationKey);

						new DAO().createTrigerstatustable(companynames.getText().trim());
						new DAO().createcontrolreportresultstable();

						Node source = (Node) event.getSource();
						myStage = (Stage) source.getScene().getWindow();
						myStage.close();

						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								String screenName = "loginscreen";
								new LoginFXHelper().initAndShowGUI(screenName);
							}
						});

						if (new DAO().gettrigerstatus("bugstable") == 0) {
							new DAO().createBugstable();
						}
						if (new DAO().gettrigerstatus("bugusers") == 0) {
							new DAO().createBugServerUsersTable();
						}
						if (new DAO().gettrigerstatus("bugprojects") == 0) {
							new DAO().createBugServerProjectsTable();
						}
						if (new DAO().gettrigerstatus("bugserver") == 0) {
							new DAO().createBugServerTable();
						}

						// FXMLLoader fxmlLoader = new FXMLLoader();
						// ControllLogin rgc = (ControllLogin)
						// fxmlLoader.getController();
						// rgc.setUsername(usernames);

						alert.setContentText("Registration Successful. Please Login and Set Initial Settings");
						alert.showAndWait();
						return true;
					}
				} else {
					alert.setContentText("Database creation failed. Please try again...!");
					alert.showAndWait();
					return false;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	@SuppressWarnings({ "rawtypes", "null" })
	public void getWebuserLevel(String key) {

		ArrayList webUserLevelArrayList = new DAO().getwebUserLevelDetails(key);

		if (webUserLevelArrayList != null || !webUserLevelArrayList.isEmpty()) {
			for (int i = 0; i < webUserLevelArrayList.size(); i++) {
				UserTypeBeanBinaryTrade UserTypeBeanBinaryTrade = (UserTypeBeanBinaryTrade) webUserLevelArrayList
						.get(i);
				userLevel = UserTypeBeanBinaryTrade.getUserLevel();
				// System.out.println(UserTypeBeanBinaryTrade.getUserLevel());
			}
		}
	}
}
