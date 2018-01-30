package com.testmydata.fxcontroller;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.ModulesBinaryTrade;
import com.testmydata.binarybeans.QAServerDetailsBinaryTrade;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.dao.OnlineDBAccess;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.EncryptAndDecrypt;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.QADefaultServerDetails;
import com.testmydata.util.StaticImages;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

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
	private ImageView closeicon, greentick1, wrongtick1, saveicon, updateicon, testicon, modulesaveicon,
			moduleupdateicon;
	@FXML
	private JFXCheckBox deafultcheckbox;
	@FXML
	private AnchorPane actionanchor1, actionanchor11;

	// private String[] servers = { "SELECT SERVER", "MY SQL", "MSSQL",
	// "POSTGRESQL" };
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
		fservercombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");

		setexistingservers();
		setexistingmodules();
		closeicon.setImage(StaticImages.closeicon.getImage());
		greentick1.setImage(StaticImages.green_tick.getImage());
		wrongtick1.setImage(StaticImages.wrong_tick.getImage());
		saveicon.setImage(StaticImages.save.getImage());
		updateicon.setImage(StaticImages.save.getImage());
		testicon.setImage(StaticImages.source_execute.getImage());
		modulesaveicon.setImage(StaticImages.save.getImage());
		moduleupdateicon.setImage(StaticImages.save.getImage());

		Label testlbl = new Label("  Test ");
		testlbl.setStyle(StaticImages.lblStyle);
		testlbl.setMinWidth(35);
		testlbl.setLayoutX(65);
		testlbl.setLayoutY(15);
		testlbl.setVisible(false);
		actionanchor1.getChildren().add(testlbl);

		testicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				testlbl.setVisible(true);
			}
		});
		testicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				testlbl.setVisible(false);
			}
		});
		testicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				ftest();
			}
		});

		Label sucesslbl = new Label(" Connection Successful ");
		sucesslbl.setStyle(StaticImages.lblStyle);
		sucesslbl.setMinWidth(80);
		sucesslbl.setLayoutX(105);
		sucesslbl.setLayoutY(15);
		sucesslbl.setVisible(false);
		actionanchor1.getChildren().add(sucesslbl);

		greentick1.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				sucesslbl.setVisible(true);
			}
		});
		greentick1.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				sucesslbl.setVisible(false);
			}
		});

		Label faillbl = new Label(" Connection Failure ");
		faillbl.setStyle(StaticImages.lblStyle);
		faillbl.setMinWidth(70);
		faillbl.setLayoutX(105);
		faillbl.setLayoutY(15);
		faillbl.setVisible(false);
		actionanchor1.getChildren().add(faillbl);

		wrongtick1.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				faillbl.setVisible(true);
			}
		});
		wrongtick1.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				faillbl.setVisible(false);
			}
		});

		Label savelbl = new Label("  Save ");
		savelbl.setStyle(StaticImages.lblStyle);
		savelbl.setMinWidth(40);
		savelbl.setLayoutX(145);
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
				fsave();
			}
		});

		Label updatelbl = new Label(" Update ");
		updatelbl.setStyle(StaticImages.lblStyle);
		updatelbl.setMinWidth(40);
		updatelbl.setLayoutX(145);
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
				fupdate();
			}
		});

		Label msavelbl = new Label("  Save ");
		msavelbl.setStyle(StaticImages.lblStyle);
		msavelbl.setMinWidth(40);
		msavelbl.setLayoutX(145);
		msavelbl.setLayoutY(15);
		msavelbl.setVisible(false);
		actionanchor11.getChildren().add(msavelbl);

		modulesaveicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				msavelbl.setVisible(true);
			}
		});
		modulesaveicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				msavelbl.setVisible(false);
			}
		});
		modulesaveicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				msave();
			}
		});

		Label mupdatelbl = new Label(" Update ");
		mupdatelbl.setStyle(StaticImages.lblStyle);
		mupdatelbl.setMinWidth(35);
		mupdatelbl.setLayoutX(145);
		mupdatelbl.setLayoutY(15);
		mupdatelbl.setVisible(false);
		actionanchor11.getChildren().add(mupdatelbl);

		moduleupdateicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mupdatelbl.setVisible(true);
			}
		});
		moduleupdateicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mupdatelbl.setVisible(false);
			}
		});
		moduleupdateicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mupdate();
			}
		});

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
								saveicon.setVisible(false);
								updateicon.setVisible(true);
								break;
							}
						}
					} else if (fexistingservercombo.getSelectionModel().getSelectedItem().equals("QA Servers")) {
						fhosttext.clear();
						fusernametext.clear();
						fpasswordtext.clear();
						fservercombo.getSelectionModel().select(0);
						deafultcheckbox.setSelected(false);
						updateicon.setVisible(false);
						saveicon.setVisible(true);
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
						modulesaveicon.setVisible(false);
						moduleupdateicon.setVisible(true);
					} else {
						modulenametext.clear();
						moduleupdateicon.setVisible(false);
						modulesaveicon.setVisible(true);
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
		StringBuffer message = new StringBuffer();
		if (fhosttext.getText() == null || fhosttext.getText().isEmpty()) {
			result = false;
			fhosttext.setUnFocusColor(Color.RED);
			message.append("Please Enter Host IP...\n\n");
		} else {
			fhosttext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (fusernametext.getText() == null || fusernametext.getText().isEmpty()) {
			result = false;
			fusernametext.setUnFocusColor(Color.RED);
			message.append("Please Enter User Name...\n\n");
		} else {
			fusernametext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (fpasswordtext.getText() == null || fpasswordtext.getText().isEmpty()) {
			result = false;
			fpasswordtext.setUnFocusColor(Color.RED);
			message.append("Please Enter Password...\n\n");
		} else {
			fpasswordtext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (fservercombo.getSelectionModel().getSelectedItem().equals("SELECT SERVER")) {
			result = false;
			fservercombo.setUnFocusColor(Color.RED);
			message.append("Please Select Server...\n\n");
		} else {
			fservercombo.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (!result) {
			runmessage(message.toString());
		}
		return result;
	}

	// Action Method to Test Icon. Checking connection
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
		} else if (servertype.equals("POSTGRESQL")) {
			classname = "org.postgresql.Driver";
			dburl = "jdbc:postgresql://" + hostname + "/";
		}
	}

	// Help method to format url
	private String formaturl(String servertype, String url) {
		if (servertype.equals("MY SQL")) {
			formatedurl = url.replaceAll("jdbc.mysql://", "");
			formatedurl = formatedurl.replaceAll("/", "");
		} else if (servertype.equals("MSSQL")) {
			formatedurl = url.replaceAll("jdbc:sqlserver://", "");
		} else if (servertype.equals("POSTGRESQL")) {
			formatedurl = url.replaceAll("jdbc:postgresql://", "");
			formatedurl = formatedurl.replaceAll("/", "");
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
		updateicon.setVisible(false);
		saveicon.setVisible(true);
		deafultcheckbox.setSelected(false);
		fticksnonvisible();
	}

	// Code to control Modules
	private void setexistingmodules() {
		existingmodulescombo.getItems().clear();
		existingmodulescombo.getItems().add("QA Modules");
		existingmodulescombo.getSelectionModel().select(0);
		moduleslist.clear();
		moduleslist = new DAO().getModuleDetails("modules", "all", Loggedinuserdetails.defaultproject);
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
		StringBuffer message = new StringBuffer();
		if (modulenametext.getText() == null || modulenametext.getText().isEmpty()) {
			result = false;
			modulenametext.setUnFocusColor(Color.RED);
			message.append("Please Enter Module Name...\n\n");
		} else {
			modulenametext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (!result) {
			runmessage(message.toString());
		}

		return result;
	}

	private void msave() {
		if (validatemfields()) {
			String result = new DAO().createmodulestable("modules", currentUsersDetailsBeanBinaryTree.getId(),
					modulenametext.getText(), Loggedinuserdetails.defaultproject);
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

	private void mupdate() {
		if (validatemfields()) {
			String result = new DAO().updatemoduledetails("modules", currentUsersDetailsBeanBinaryTree.getId(),
					modulenametext.getText(), 1, Loggedinuserdetails.defaultproject);
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
		moduleupdateicon.setVisible(false);
		modulesaveicon.setVisible(true);
	}
}
