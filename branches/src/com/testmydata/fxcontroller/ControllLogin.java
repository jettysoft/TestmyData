package com.testmydata.fxcontroller;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.animation.FadeInLeftTransition;
import com.testmydata.animation.FadeInLeftTransition1;
import com.testmydata.animation.FadeInRightTransition;
import com.testmydata.binarybeans.LocalUserLevelBeanBinaryTrade;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.dao.DBAccess;
import com.testmydata.fxhelpers.LoginFXHelper;
import com.testmydata.fxhelpers.WelcomePageFXHelper;
import com.testmydata.fxutil.DBnameUpdateJAXB;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.EncryptAndDecrypt;
import com.testmydata.util.FileIOOperations;
import com.testmydata.util.StaticImages;
import com.testmydata.vpn.VpnConnectionThread;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ControllLogin implements Initializable {
	@FXML
	private JFXTextField txtUsername, txtCompanyName;
	@FXML
	private JFXPasswordField txtPassword;
	@FXML
	private ImageView imageView;
	@FXML
	private Text lblWelcome, lblUsername, lblPassword, lblcompanyName;
	@FXML
	private Label producttype, lblClose, applabel;
	@FXML
	private Button btnLogin;
	@FXML
	private JFXCheckBox remembermebox;
	@FXML
	private Hyperlink newUser, forgotPassword;
	Stage stage, myStage;

	public String localUserLevel = null;
	public Date activatedDate = null;
	public int userLevel;

	static UsersDetailsBeanBinaryTrade loggedInUsersDetailsBeanBinaryTreeBinaryTree;

	FileIOOperations fileop = new FileIOOperations();
	Properties props, keyprop;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
		remembermebox.setStyle("-fx-text-fill: white; -fx-font-weight:bold;");
		imageView.setImage(StaticImages.appicon);

		txtCompanyName.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					// Check if the new character is greater than LIMIT
					if (txtCompanyName.getText().length() >= 40) {
						txtCompanyName.setText(txtCompanyName.getText().substring(0, 40));
					}
				}
			}
		});

		lblWelcome.setEffect(ds);
		lblUsername.setEffect(ds);
		lblPassword.setEffect(ds);
		lblcompanyName.setEffect(ds);
		producttype.setEffect(ds);
		newUser.setEffect(ds);
		forgotPassword.setEffect(ds);

		Platform.runLater(() -> {
			
			new FadeInLeftTransition(lblWelcome).play();
			new FadeInLeftTransition1(lblPassword).play();
			new FadeInLeftTransition1(lblUsername).play();
			new FadeInLeftTransition1(lblcompanyName).play();
			new FadeInLeftTransition1(txtUsername).play();
			new FadeInLeftTransition1(txtPassword).play();
			new FadeInLeftTransition1(txtCompanyName).play();
			new FadeInRightTransition(producttype).play();
			new FadeInRightTransition(btnLogin).play();
			new FadeInRightTransition(imageView).play();
			new FadeInLeftTransition(applabel).play();

			try {
				props = fileop.readPropertyFile("dafaultValues.properties");
				if (props.getProperty("CompanyName") != null || !props.getProperty("CompanyName").isEmpty()) {
					txtCompanyName.setText(props.getProperty("CompanyName").toLowerCase());
					if (props.getProperty("Remember") != null && !props.getProperty("Remember").isEmpty()
							&& props.getProperty("Remember").equals("true")) {
						remembermebox.setSelected(true);
						txtUsername.setText(EncryptAndDecrypt.decryptData(props.getProperty("Username")));
						txtPassword.setText(EncryptAndDecrypt.decryptData(props.getProperty("Password")));

					} else {
						remembermebox.setSelected(false);
					}
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
			}

			lblClose.setOnMouseClicked((MouseEvent event) -> {
				VpnConnectionThread.shutdown();
				Platform.exit();
				System.exit(0);
			});
		});

		if (txtCompanyName.getText() != null || !txtCompanyName.getText().trim().isEmpty()) {
			getlocaluserLevel("yes");
		} else {
			producttype.setText("Not Registered");
			CommonFunctions.message = "Registration Details not found. Please enter Company Name and try to login...!";
			CommonFunctions.invokeAlertBox(getClass());
		}
		txtCompanyName.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			@SuppressWarnings("static-access")
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (txtCompanyName.getText() != null && !txtCompanyName.getText().trim().isEmpty()
						&& txtCompanyName.getText().trim() != "") {
					if (newPropertyValue) {
						CommonFunctions omm = new CommonFunctions();
						String companyName = txtCompanyName.getText().trim().toLowerCase().replaceAll(" ", "");
						String upToNCharacters = companyName.substring(0, Math.min(companyName.length(), 40));
						omm.updateCompanyName(upToNCharacters.toLowerCase());

						DBAccess dba = new DBAccess();
						dba.con = null;
						dba.getConnection();
					} else if (oldPropertyValue) {

					}
				}
			}
		});
	}

	public void setUsername(String username) {
		if (username != null && (!username.equalsIgnoreCase("error"))) {
			txtUsername.setText(username);
			txtUsername.setEditable(true);
		} else {
			txtUsername.clear();
			txtUsername.setEditable(true);
		}
	}

	@FXML
	private boolean aksiLogin(ActionEvent event) {

		if (txtUsername.getText().trim().isEmpty()) {
			CommonFunctions.message = "Please Enter User Id";
			CommonFunctions.invokeAlertBox(getClass());

			return false;

		} else if (txtPassword.getText().trim().isEmpty()) {
			CommonFunctions.message = "Please Enter Password";
			CommonFunctions.invokeAlertBox(getClass());
			return false;

		} else if (txtCompanyName.getText().trim().isEmpty()) {
			CommonFunctions.message = "Please enter valid Company Name";
			CommonFunctions.invokeAlertBox(getClass());
			return false;

		} else if (!txtCompanyName.getText().trim().isEmpty()) {
			// DBnameUpdateJAXB db = new DBnameUpdateJAXB();
			// db.encrypt(EncryptAndDecrypt.encryptData(txtCompanyName.getText().trim().toString().toLowerCase().substring(0,
			// Math.min(txtCompanyName.getText().trim().toString().toLowerCase().length(),
			// 40))));
			CommonFunctions
					.updateCompanyName(txtCompanyName.getText().trim().toLowerCase().toString().replaceAll(" ", ""));
			String companyname = txtCompanyName.getText().toLowerCase().replaceAll(" ", "");
			String status = new DAO().checkDataBaseExist(companyname.substring(0, Math.min(companyname.length(), 40)));

			if (status.contains("failure")) {
				CommonFunctions.message = "Internal Error Occured. Please Try Again...!";
				CommonFunctions.invokeAlertBox(getClass());
			} else if (status.contains("notExisted")) {
				CommonFunctions.message = "Entered Company Name is not Registered with 'Test My Data'...!";
				CommonFunctions.invokeAlertBox(getClass());
			} else if (status.contains("existed")) {
				loggedInUsersDetailsBeanBinaryTreeBinaryTree = new DAO().authenticateUser(txtUsername.getText().trim(),
						txtPassword.getText().trim());
				if (loggedInUsersDetailsBeanBinaryTreeBinaryTree.getLoginStatus().trim().equals("failure")) {
					// loadingIcon.setVisible(false);
					CommonFunctions.message = "               Invaild Username or Password\n\n                                  (or)\n\n               Your Account might Blocked...!";
					CommonFunctions.invokeAlertBox(getClass());
					return false;
				} else if (loggedInUsersDetailsBeanBinaryTreeBinaryTree.getLoginStatus().trim().equals("notExisted")) {
					CommonFunctions.message = "UserName is not registered. Please Register...!";
					CommonFunctions.invokeAlertBox(getClass());
					return false;
				} else if (loggedInUsersDetailsBeanBinaryTreeBinaryTree.getLoginStatus().trim()
						.equals("Please Register")) {
					CommonFunctions.message = "Check the Internet Connection or Contact Adminstrator...!";
					CommonFunctions.invokeAlertBox(getClass());
					return false;
				} else if (!txtCompanyName.getText().trim().isEmpty()) {

					/*
					 * FileIOOperations fileop = new FileIOOperations();
					 * Properties props =
					 * fileop.readPropertyFile("dafaultValues.properties");
					 * 
					 * try { if(!txtCompanyName.getText().trim().equals(props.
					 * getProperty( "CompanyName"))) { message =
					 * "Please enter valid Company Name."; invokeAlertBox();
					 * return false; }
					 * 
					 * } catch (Exception ex) { ex.printStackTrace(); }
					 */
					/*
					 * //update companyname to dbconfig.xml String companyName =
					 * txtCompanyName.getText().trim(); String upToNCharacters1
					 * = companyName.substring(0, Math.min(companyName.length(),
					 * 40)); try { DBConfigJAXB dbC = new DBConfigJAXB();
					 * DBConfig dbConfig = dbC.readDBConfig();
					 * dbConfig.setDbName(EncryptAndDecrypt.encryptData(
					 * upToNCharacters1.toLowerCase()));
					 * dbC.generateDBConfig(dbConfig); } catch (Exception e1) {
					 * 
					 * }
					 */

					// get the companyname from dbconfig.xml
					try {
						DBnameUpdateJAXB dcrdb = new DBnameUpdateJAXB();
						dcrdb.decrypt();

						String enteredcompanyname = txtCompanyName.getText().trim().toString().substring(0,
								Math.min(txtCompanyName.getText().trim().toString().length(), 40));

						if (!enteredcompanyname.trim().equals(dcrdb.decrypteddbanme)) {
							CommonFunctions.message = "Please enter valid Company Name...!";
							CommonFunctions.invokeAlertBox(getClass());
							return false;
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					/*
					 * java.util.Date dueDate =
					 * CommonFunctions.getStartDateOfWeek(); PendingListTable
					 * mailMode = new PendingListTable(userDashboard,dueDate);
					 */
					CommonFunctions cm = new CommonFunctions();
					cm.updateKeyProperties();

					new DAO().updateUsers(txtUsername.getText().trim());

					rememberornot();// remember method

					Node source = (Node) event.getSource();
					myStage = (Stage) source.getScene().getWindow();
					myStage.close();
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									openDashBoard(localUserLevel, loggedInUsersDetailsBeanBinaryTreeBinaryTree);
								}
							});
						}
					});
				}
			}
		}
		return true;
	}

	public void openDashBoard(String level, UsersDetailsBeanBinaryTrade userbean) {
		openDashBoardnow(level, userbean);
	}

	public void openDashBoardnow(String level, UsersDetailsBeanBinaryTrade userbean) {
		loggedInUsersDetailsBeanBinaryTreeBinaryTree = userbean;
		userLevel = Integer.parseInt(level);
		// FeaturesAllocationController.getInstance(loggedInUsersDetailsBeanBinaryTreeBinaryTree,
		// userLevel);
		try {
			DAO.getInstance(loggedInUsersDetailsBeanBinaryTreeBinaryTree);

			DashBoardController.getInstance(loggedInUsersDetailsBeanBinaryTreeBinaryTree);
			new WelcomePageFXHelper().initAndShowGUI("homepage");
		} catch (NullPointerException e) {
			// Just Ignore
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void forgotPassword(ActionEvent event) {
		Node source = (Node) event.getSource();
		myStage = (Stage) source.getScene().getWindow();
		myStage.close();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "forgotpassword";
						// new WelcomePageFXHelper().initAndShowGUI(screenName);
						new LoginFXHelper().initAndShowGUI(screenName);
					}
				});
			}
		});

	}

	@FXML
	private void newuser(ActionEvent event) {
		Node source = (Node) event.getSource();
		myStage = (Stage) source.getScene().getWindow();
		myStage.close();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "welcomepage";
						// new WelcomePageFXHelper().initAndShowGUI(screenName);
						new LoginFXHelper().initAndShowGUI(screenName);
					}
				});
			}
		});

	}

	@SuppressWarnings("null")
	public int getlocaluserLevel(String display) {
		int levelforotherclasees = 0;
		@SuppressWarnings("rawtypes")
		ArrayList localUserLevelArrayList = new DAO().getlocalUseLevelDetails(1);

		if (localUserLevelArrayList != null || !localUserLevelArrayList.isEmpty()) {
			for (int i = 0; i < localUserLevelArrayList.size(); i++) {
				LocalUserLevelBeanBinaryTrade LocalUserLevelBeanBinaryTrade1 = (LocalUserLevelBeanBinaryTrade) localUserLevelArrayList
						.get(i);
				localUserLevel = LocalUserLevelBeanBinaryTrade1.getLocalUserLevel();
				// activatedDate =
				// LocalUserLevelBeanBinaryTrade1.getCreatedDate();
				// System.out.println(usertypeBean.getUserLevel());
			}
		}

		if (localUserLevel != null && !localUserLevel.isEmpty() && display.equals("yes")) {
			displayProductType();
		} else if (localUserLevel == null || localUserLevel.isEmpty()) {
			CommonFunctions.message = "Please Enter Registered Company Name and ReStart the Application";
			CommonFunctions.invokeAlertBox(getClass());
		}

		levelforotherclasees = Integer.parseInt(localUserLevel);
		return levelforotherclasees;
	}

	public void displayProductType() {
		userLevel = Integer.parseInt(localUserLevel);

		if (userLevel == 1) {
			producttype.setText("Silver");
		} else if (userLevel == 2) {
			producttype.setText("Gold");
		} else if (userLevel == 3) {
			producttype.setText("Platinum");
		} else if (userLevel == 4) {
			producttype.setText("Builder's Platinum");
		} else if (userLevel == 5) {
			producttype.setText("Contractor's Platinum");
		} else if (userLevel == 6) {
			producttype.setText("Services Platinum");
		}
	}

	public void rememberornot() {
		try {
			Properties dafaultValuesPropertiesFile = new Properties();
			FileInputStream fis = new FileInputStream(
					new File(".", "/conf/dafaultValues.properties").getAbsolutePath());
			dafaultValuesPropertiesFile.load(fis);
			fis.close();
			FileOutputStream fos = new FileOutputStream(
					new File(".", "/conf/dafaultValues.properties").getAbsolutePath());

			if (remembermebox.isSelected()) {
				dafaultValuesPropertiesFile.setProperty("Remember", "true");
				dafaultValuesPropertiesFile.setProperty("Username",
						EncryptAndDecrypt.encryptData(txtUsername.getText()));
				dafaultValuesPropertiesFile.setProperty("Password",
						EncryptAndDecrypt.encryptData(txtPassword.getText()));
				dafaultValuesPropertiesFile.store(fos, new Date().toString());
			} else {
				dafaultValuesPropertiesFile.setProperty("Remember", "false");
				dafaultValuesPropertiesFile.store(fos, new Date().toString());
			}

			fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
