package com.testmydata.fxcontroller;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.ModulesBinaryTrade;
import com.testmydata.binarybeans.QAServerDetailsBinaryTrade;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.dao.OnlineDBAccess;
import com.testmydata.fxutil.UndecoratorController;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.EncryptAndDecrypt;
import com.testmydata.util.QADefaultServerDetails;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class QAServerSettingsController implements Initializable {
	private static UsersDetailsBeanBinaryTrade currentUsersDetailsBeanBinaryTree;
	private static QAServerSettingsController userHome = null;

	@FXML
	private JFXTextField fhosttext, fusernametext, modulenametext;
	@FXML
	private JFXPasswordField fpasswordtext;
	@FXML
	private JFXComboBox<String> fexistingservercombo, fservercombo, existingmodulescombo;
	@FXML
	private JFXButton ftest, fsave, fupdate, msave, mupdate;
	@FXML
	private ImageView homeicon, greentick1, wrongtick1;
	@FXML
	private JFXCheckBox deafultcheckbox;
	Stage myStage;
	private String[] servers = { "SELECT SERVER", "MY SQL", "MSSQL" };

	private static String classname = null, dburl = null, formatedurl = null;
	ArrayList<QAServerDetailsBinaryTrade> serverlist = new ArrayList<QAServerDetailsBinaryTrade>();
	ArrayList<ModulesBinaryTrade> moduleslist = new ArrayList<ModulesBinaryTrade>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// adding serves to combo
		fservercombo.getItems().addAll(servers);
		// selecting first value in combo
		fservercombo.getSelectionModel().select(0);
		// adding set style to combo text
		fservercombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");

		setexistingservers();
		setexistingmodules();

		fexistingservercombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!fexistingservercombo.getSelectionModel().getSelectedItem().equals("QA Servers")) {
						String[] selecteditems = fexistingservercombo.getSelectionModel().getSelectedItem().split("-");
						for (int i = 0; i < serverlist.size(); i++) {
							if (selecteditems[0].equals(serverlist.get(i).getId())) {
								fhosttext.setText(
										formaturl(serverlist.get(i).getServertype(), serverlist.get(i).getDburl()));
								fusernametext.setText(EncryptAndDecrypt.decryptData(serverlist.get(i).getUsername()));
								fpasswordtext.setText(EncryptAndDecrypt.decryptData(serverlist.get(i).getPassword()));
								fservercombo.getSelectionModel().select(serverlist.get(i).getServertype());
								if (serverlist.get(i).getDefaulttype().equals("1")) {
									deafultcheckbox.setSelected(true);
								} else {
									deafultcheckbox.setSelected(false);
								}
								fsave.setVisible(false);
								fupdate.setVisible(true);
								break;
							}
						}
					} else if (fexistingservercombo.getSelectionModel().getSelectedItem().equals("QA Servers")) {
						fhosttext.clear();
						fusernametext.clear();
						fpasswordtext.clear();
						fservercombo.getSelectionModel().select(0);
						deafultcheckbox.setSelected(false);
						fupdate.setVisible(false);
						fsave.setVisible(true);
						greentick1.setVisible(false);
						wrongtick1.setVisible(false);
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		existingmodulescombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!existingmodulescombo.getSelectionModel().getSelectedItem().equals("QA Modules")) {
						String[] selecteditems = existingmodulescombo.getSelectionModel().getSelectedItem().split("-");
						modulenametext.setText(selecteditems[1]);
						msave.setVisible(false);
						mupdate.setVisible(true);
					} else {
						modulenametext.clear();
						mupdate.setVisible(false);
						msave.setVisible(true);
					}
				} catch (NullPointerException ne) {
				}
			}
		});
		fusernametext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (fusernametext.getText().length() >= 300) {
						fusernametext.setText(fusernametext.getText().substring(0, 300));
					}
				}
			}
		});

		modulenametext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (modulenametext.getText().length() >= 200) {
						modulenametext.setText(modulenametext.getText().substring(0, 200));
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
				QAServerSettingsController nc = new QAServerSettingsController();
				scu.nullifyStrings(nc);

				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();
				UndecoratorController.getInstance(null);
			}
		});
	}

	// Method used to get data from previous class
	public static QAServerSettingsController getInstance(
			UsersDetailsBeanBinaryTrade loggedInUsersDetailsBeanBinaryTree) {
		if (loggedInUsersDetailsBeanBinaryTree != null) {
			currentUsersDetailsBeanBinaryTree = loggedInUsersDetailsBeanBinaryTree;
		}
		return userHome;
	}

	private void setexistingservers() {
		fexistingservercombo.getItems().clear();
		fexistingservercombo.getItems().add("QA Servers");
		fexistingservercombo.getSelectionModel().select(0);
		serverlist.clear();
		serverlist = new DAO().getQAServerDetails("qaservers");
		if (serverlist != null && serverlist.size() > 0) {
			for (int i = 0; i < serverlist.size(); i++) {
				fexistingservercombo.getItems().add(serverlist.get(i).getId() + "-" + serverlist.get(i).getDburl());
			}
		}
		fexistingservercombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
	}

	// Method to display the Messages
	public void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	// Validating the Fields in screen
	private boolean validateffields() {
		boolean result = true;
		if (fhosttext.getText() == null || fhosttext.getText().isEmpty()) {
			result = false;
			runmessage("Please Enter Field to Field Host IP...");
		} else if (fusernametext.getText() == null || fusernametext.getText().isEmpty()) {
			result = false;
			runmessage("Please Enter Field to Field User Name...");
		} else if (fpasswordtext.getText() == null || fpasswordtext.getText().isEmpty()) {
			result = false;
			runmessage("Please Enter Field to Field Password...");
		} else if (fservercombo.getSelectionModel().getSelectedItem().equals("SELECT SERVER")) {
			result = false;
			runmessage("Please Select Field to Field Server...");
		}
		return result;
	}

	// Action Method to Test Button. Checking connection
	@FXML
	private void ftest() {
		if (validateffields()) {
			if (validateconnection(fservercombo.getSelectionModel().getSelectedItem(), fhosttext.getText(),
					fusernametext.getText(), fpasswordtext.getText())) {
				greentick1.setVisible(true);
				wrongtick1.setVisible(false);
			} else {
				greentick1.setVisible(false);
				wrongtick1.setVisible(true);
			}
		} else {
			fticksnonvisible();
		}
	}

	// validate connection
	public boolean validateconnection(String servertype, String host, String username, String password) {
		boolean result = false;
		setclassandurl(servertype, host);
		OnlineDBAccess oab = new OnlineDBAccess();
		Connection con = oab.getfConnection(dburl, EncryptAndDecrypt.encryptData(username),
				EncryptAndDecrypt.encryptData(password), classname);
		if (con != null) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	// Saving to DB
	@FXML
	private void fsave() {
		if (validateffields()) {
			if (validateconnection(fservercombo.getSelectionModel().getSelectedItem(), fhosttext.getText(),
					fusernametext.getText(), fpasswordtext.getText())) {
				String result = new DAO().createqaservertable("qaservers", currentUsersDetailsBeanBinaryTree.getId(),
						classname, dburl, EncryptAndDecrypt.encryptData(fusernametext.getText()),
						EncryptAndDecrypt.encryptData(fpasswordtext.getText()),
						fservercombo.getSelectionModel().getSelectedItem(), deafultcheckbox.isSelected());
				if (result.equals("success")) {
					runmessage("Successfully New QA Server Added...");
					new DAO().updatetabledata("qaservers", "defaulttype", "0", "url !", dburl);
					setfdefaults();

					QADefaultServerDetails qasd = new QADefaultServerDetails();
					qasd.setqadefaultserver();
					InvoiceStaticHelper.dash.setqaserver();
				} else {
					runmessage("Failed to Add QA Server, Please Try Again...");
				}
			} else {
				fticksnonvisible();
				runmessage("Please Check Server Details...");
			}
		} else {
			fticksnonvisible();
		}
	}

	// Updating existing server connection details
	@FXML
	private void fupdate() {
		if (validateffields()) {
			if (validateconnection(fservercombo.getSelectionModel().getSelectedItem(), fhosttext.getText(),
					fusernametext.getText(), fpasswordtext.getText())) {
				String[] selecteditems = fexistingservercombo.getSelectionModel().getSelectedItem().split("-");
				String result = new DAO().updateqaserverdetails("qaservers", currentUsersDetailsBeanBinaryTree.getId(),
						classname, dburl, EncryptAndDecrypt.encryptData(fusernametext.getText()),
						EncryptAndDecrypt.encryptData(fpasswordtext.getText()),
						fservercombo.getSelectionModel().getSelectedItem(), Integer.parseInt(selecteditems[0]),
						deafultcheckbox.isSelected());
				if (result.equals("success")) {
					runmessage("Successfully QA Server Updated...");
					new DAO().updatetabledata("qaservers", "defaulttype", "0", "url !", dburl);
					setfdefaults();

					QADefaultServerDetails qasd = new QADefaultServerDetails();
					qasd.setqadefaultserver();
					InvoiceStaticHelper.dash.setqaserver();
				} else {
					runmessage("Failed to Update QA Server, Please Try Again...");
				}
			} else {
				fticksnonvisible();
				runmessage("Please Check Server Details...");
			}
		} else {
			fticksnonvisible();
		}
	}

	// Help method to set the URL and Class names for different server types
	private void setclassandurl(String servertype, String hostname) {
		if (servertype.equals("MY SQL")) {
			classname = "com.mysql.jdbc.Driver";
			dburl = "jdbc:mysql://" + hostname + "/";
		} else if (servertype.equals("MSSQL")) {
			classname = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			dburl = "jdbc:sqlserver://" + hostname;
		}
	}

	// Help method to format url
	private String formaturl(String servertype, String url) {
		if (servertype.equals("MY SQL")) {
			formatedurl = url.replaceAll("jdbc.mysql://", "");
			formatedurl = formatedurl.replaceAll("/", "");
		} else if (servertype.equals("MSSQL")) {
			formatedurl = url.replaceAll("jdbc:sqlserver://", "");
		}
		return formatedurl;
	}

	// Control Visibility of Images Views
	private void fticksnonvisible() {
		greentick1.setVisible(false);
		wrongtick1.setVisible(false);
	}

	private void setfdefaults() {
		setexistingservers();
		fservercombo.getSelectionModel().select(0);
		fhosttext.clear();
		fusernametext.clear();
		fpasswordtext.clear();
		fupdate.setVisible(false);
		fsave.setVisible(true);
		deafultcheckbox.setSelected(false);
		fticksnonvisible();
	}

	// Code to control Modules
	private void setexistingmodules() {
		existingmodulescombo.getItems().clear();
		existingmodulescombo.getItems().add("QA Modules");
		existingmodulescombo.getSelectionModel().select(0);
		moduleslist.clear();
		moduleslist = new DAO().getModuleDetails("modules");
		if (moduleslist != null && moduleslist.size() > 0) {
			for (int i = 0; i < moduleslist.size(); i++) {
				existingmodulescombo.getItems()
						.add(moduleslist.get(i).getId() + "-" + moduleslist.get(i).getModulename());
			}
		}
		existingmodulescombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
	}

	// Validating the Fields in Modules
	private boolean validatemfields() {
		boolean result = true;
		if (modulenametext.getText() == null || modulenametext.getText().isEmpty()) {
			result = false;
			runmessage("Please Enter Module Name...");
		}
		return result;
	}

	@FXML
	private void msave() {
		if (validatemfields()) {
			String result = new DAO().createmodulestable("modules", currentUsersDetailsBeanBinaryTree.getId(),
					modulenametext.getText());
			if (result.equals("success")) {
				setmdefaults();
				runmessage("Successfully Module Added...");
			} else if (result.equals("duplicate")) {
				runmessage("Module Already Exists. Please enter different module name...");
			} else {
				runmessage("Failed to Add Module, Please Try Again...");
			}
		}
	}

	@FXML
	private void mupdate() {
		if (validatemfields()) {
			String result = new DAO().updatemoduledetails("modules", currentUsersDetailsBeanBinaryTree.getId(),
					modulenametext.getText(), 1);
			if (result.equals("success")) {
				setmdefaults();
				runmessage("Successfully Module Updated...");
			} else {
				runmessage("Failed to Update Module, Please Try Again...");
			}
		}
	}

	private void setmdefaults() {
		setexistingmodules();
		modulenametext.clear();
		mupdate.setVisible(false);
		msave.setVisible(true);
	}
}
