package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.exceptions.TFSFederatedAuthException;
import com.microsoft.tfs.core.exceptions.TFSUnauthorizedException;
import com.microsoft.tfs.core.ws.runtime.exceptions.FederatedAuthException;
import com.microsoft.tfs.core.ws.runtime.exceptions.UnauthorizedException;
import com.testmydata.binarybeans.BugServerBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.dao.TFSAccess;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.Loggedinuserdetails;
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

public class BugServerController implements Initializable {
	@FXML
	private ImageView saveicon, updateicon, refreshicon, closeicon, greentick1, wrongtick1;
	@FXML
	private JFXComboBox<String> tfsurlcombo, tfsprojectcombo, tfsusernamecombo;
	@FXML
	private JFXTextField tfsurltext;
	@FXML
	private JFXPasswordField tfspasswordtext;
	@FXML
	private AnchorPane actionanchor1;
	@FXML
	private Label refreshlbl, userlabel, tfsserverlabel, statuslabel;
	@FXML
	private JFXCheckBox deactivatecheck, activatecheck, defaultcheckattfs;

	static int selectedbugserverid = 0, selectedprojectid = 0, selecteduserid = 0;
	static ArrayList<BugServerBinaryTrade> tfsserverlist = new ArrayList<BugServerBinaryTrade>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		tfsurlcombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold; -fx-font-size:11;");
		tfsusernamecombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold; -fx-font-size:11;");
		tfsprojectcombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold; -fx-font-size:11;");

		setdefaultcombo();

		saveicon.setImage(StaticImages.save);
		updateicon.setImage(StaticImages.save);
		refreshicon.setImage(StaticImages.refresh);
		closeicon.setImage(StaticImages.closeicon);
		greentick1.setImage(StaticImages.green_tick);
		wrongtick1.setImage(StaticImages.wrong_tick);

		updateicon.setVisible(false);

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
				tfssave();
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
				tfsupdate();
			}
		});

		refreshlbl.setStyle(StaticImages.lblStyle);

		refreshicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				refreshlbl.setVisible(true);
			}
		});
		refreshicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				refreshlbl.setVisible(false);
			}
		});
		refreshicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				tfsrefresh();
			}
		});

		settfscombo();

		Label sucesslbl = new Label(" Connection Successful ");
		sucesslbl.setStyle(StaticImages.lblStyle);
		sucesslbl.setMinWidth(80);
		sucesslbl.setLayoutX(385);
		sucesslbl.setLayoutY(285);
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
		faillbl.setLayoutX(385);
		faillbl.setLayoutY(285);
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

		tfspasswordtext.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (!newValue) { // when focus lost
				if (validatetfsfields()) {
					if (validateTFSConnection(tfsurltext.getText(),
							tfsprojectcombo.getSelectionModel().getSelectedItem(),
							tfsusernamecombo.getSelectionModel().getSelectedItem(), tfspasswordtext.getText())) {
						wrongtick1.setVisible(false);
						greentick1.setVisible(true);
					} else {
						greentick1.setVisible(false);
						wrongtick1.setVisible(true);
					}
				}
			}
		});

		tfsurltext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (tfsurltext.getText().length() >= 1000) {
						tfsurltext.setText(tfsurltext.getText().substring(0, 1000));
					}
				}
			}
		});

		tfspasswordtext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (tfspasswordtext.getText().length() >= 256) {
						tfspasswordtext.setText(tfspasswordtext.getText().substring(0, 256));
					}
				}
			}
		});

		tfsurlcombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!tfsurlcombo.getSelectionModel().getSelectedItem().equals("Select TFS Server")) {

						for (int i = 0; i < tfsserverlist.size(); i++) {
							if (tfsserverlist.get(i).getCollectionurl()
									.equals(tfsurlcombo.getSelectionModel().getSelectedItem())) {
								saveicon.setVisible(false);
								updateicon.setVisible(true);
								userlabel.setVisible(true);
								tfsprojectcombo.setVisible(true);
								tfsusernamecombo.setVisible(true);
								tfspasswordtext.setVisible(true);
								tfsserverlabel.setText("Update TFS / VSTS Server");
								setdefaultcombo();

								tfsurltext.setText(tfsserverlist.get(i).getCollectionurl());
								selectedbugserverid = Integer.parseInt(tfsserverlist.get(i).getId());

								if (tfsserverlist.get(i).getIsdefault().equals("1")) {
									defaultcheckattfs.setSelected(true);
								} else {
									defaultcheckattfs.setSelected(false);
								}

								if (tfsserverlist.get(i).getIsactive().equals("1")) {
									deactivatecheck.setVisible(true);
								} else {
									activatecheck.setVisible(true);
								}

								for (int j = 0; j < tfsserverlist.get(i).getBugProjects().length; j++) {
									if (tfsserverlist.get(i).getBugProjects()[j].getProjectname() != null) {
										tfsprojectcombo.getItems()
												.add(tfsserverlist.get(i).getBugProjects()[j].getProjectname());
									}
								}

								for (int j = 0; j < tfsserverlist.get(i).getBugUsers().length; j++) {
									if (tfsserverlist.get(i).getBugUsers()[j].getUsername() != null) {
										tfsusernamecombo.getItems()
												.add(tfsserverlist.get(i).getBugUsers()[j].getUsername());
									}
								}
							}
						}

					} else {
						tfsresetonsave();
						tfsresetonupdate();
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		tfsprojectcombo.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (!newValue) { // when focus lost
				try {
					if (!tfsprojectcombo.getSelectionModel().getSelectedItem().isEmpty()
							&& !tfsprojectcombo.getSelectionModel().getSelectedItem().equals("") && !tfsprojectcombo
									.getSelectionModel().getSelectedItem().equals("Enter / Select User Name")) {

						for (int i = 0; i < tfsserverlist.size(); i++) {
							if (tfsserverlist.get(i).getCollectionurl()
									.equals(tfsurlcombo.getSelectionModel().getSelectedItem())) {

								for (int j = 0; j < tfsserverlist.get(i).getBugProjects().length; j++) {
									if (tfsserverlist.get(i).getBugProjects()[j].getProjectname() != null
											&& tfsserverlist.get(i).getBugProjects()[j].getProjectname()
													.equals(tfsprojectcombo.getSelectionModel().getSelectedItem())) {
										selectedprojectid = Integer
												.parseInt(tfsserverlist.get(i).getBugProjects()[j].getProjectid());

									} else {
										selectedprojectid = 0;
									}
								}
							}
						}
					} else {
						selectedprojectid = 0;
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		tfsusernamecombo.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (!newValue) { // when focus lost
				try {
					if (!tfsusernamecombo.getSelectionModel().getSelectedItem().isEmpty()
							&& !tfsusernamecombo.getSelectionModel().getSelectedItem().equals("") && !tfsusernamecombo
									.getSelectionModel().getSelectedItem().equals("Enter / Select User Name")) {
						for (int i = 0; i < tfsserverlist.size(); i++) {
							if (tfsserverlist.get(i).getCollectionurl()
									.equals(tfsurlcombo.getSelectionModel().getSelectedItem())) {

								for (int j = 0; j < tfsserverlist.get(i).getBugUsers().length; j++) {
									if (tfsserverlist.get(i).getBugUsers()[j].getUsername() != null
											&& tfsserverlist.get(i).getBugUsers()[j].getUsername()
													.equals(tfsusernamecombo.getSelectionModel().getSelectedItem())) {
										selecteduserid = Integer
												.parseInt(tfsserverlist.get(i).getBugUsers()[j].getBuguserid());
										tfspasswordtext.setText(tfsserverlist.get(i).getBugUsers()[j].getPassword());
									} else {
										selecteduserid = 0;
									}
								}
							}
						}
					} else {
						selecteduserid = 0;
						tfspasswordtext.clear();
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		// Bug tables
		checkandcreatetables();

		closeicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				AnchorPane pane = (AnchorPane) ((ImageView) t.getSource()).getParent().getParent().getParent();
				pane.getChildren().remove(pane.getChildren().size() - 1);

				BugServerController nc = new BugServerController();
				Cleanup.nullifyStrings(nc);
			}
		});
	}

	// Method to display the Messages
	public void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	public boolean validateTFSConnection(String url, String projectname, String username, String password) {
		boolean result = false;
		TFSTeamProjectCollection tpc = null;
		try {
			TFSAccess.COLLECTION_URL = url;
			TFSAccess.PROJECT_NAME = projectname;
			TFSAccess.USERNAME = username;
			TFSAccess.PASSWORD = password;

			tpc = TFSAccess.connectToTFS();
			tpc.authenticate();
			result = true;
		} catch (TFSUnauthorizedException ts) {
			result = false;
		} catch (UnauthorizedException ue) {
			result = false;
		} catch (TFSFederatedAuthException tae) {
			result = false;
		} catch (FederatedAuthException fe) {
			result = false;
		}
		if (!tpc.isClosed()) {
			tpc.close();
		}
		return result;
	}

	private void tfsresetonsave() {
		tfsurltext.clear();
		defaultcheckattfs.setSelected(false);
	}

	private void tfsresetonupdate() {
		userlabel.setVisible(false);
		tfsurlcombo.getSelectionModel().select(0);
		tfsprojectcombo.getSelectionModel().select(0);
		tfsprojectcombo.setVisible(false);
		tfsusernamecombo.getSelectionModel().select(0);
		tfsusernamecombo.setVisible(false);
		tfspasswordtext.clear();
		tfspasswordtext.setVisible(false);
		activatecheck.setSelected(false);
		activatecheck.setVisible(false);
		deactivatecheck.setSelected(false);
		deactivatecheck.setVisible(false);
		updateicon.setVisible(false);
		saveicon.setVisible(true);
		selectedbugserverid = 0;
		selectedprojectid = 0;
		selecteduserid = 0;
		wrongtick1.setVisible(false);
		greentick1.setVisible(false);
		tfsserverlabel.setText("Add New TFS / VSTS Server");
	}

	public void checkandcreatetables() {
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
	}

	// tfs logic

	private boolean validatetfsfields() {
		boolean result = true;
		StringBuffer message = new StringBuffer();

		if (tfsurltext.getText() == null || tfsurltext.getText().isEmpty()) {
			result = false;
			tfsurltext.setUnFocusColor(Color.RED);
			message.append("Please provide TFS / VSTS URL...\n\n");
		} else {
			tfsurltext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (updateicon.isVisible()) {
			if (tfsprojectcombo.getSelectionModel().getSelectedItem() == null
					|| tfsprojectcombo.getSelectionModel().getSelectedItem().equals("Enter / Select Project Name")) {
				result = false;
				tfsprojectcombo.setUnFocusColor(Color.RED);
				message.append("Please provide Project Name...\n\n");
			} else {
				tfsprojectcombo.setUnFocusColor(Color.rgb(190, 190, 196));
			}

			if (tfsusernamecombo.getSelectionModel().getSelectedItem() == null
					|| tfsusernamecombo.getSelectionModel().getSelectedItem().equals("Enter / Select User Name")) {
				result = false;
				tfsusernamecombo.setUnFocusColor(Color.RED);
				message.append("Please provide User Name...\n\n");
			} else {
				tfsusernamecombo.setUnFocusColor(Color.rgb(190, 190, 196));
			}

			if (tfspasswordtext.getText() == null || tfspasswordtext.getText().isEmpty()) {
				result = false;
				tfspasswordtext.setUnFocusColor(Color.RED);
				message.append("Please provide valid Password...\n\n");
			} else {
				tfspasswordtext.setUnFocusColor(Color.rgb(247, 246, 242));
			}
		}

		if (result == false) {
			runmessage(message.toString());
		}

		return result;
	}

	private void tfssave() {
		if (validatetfsfields()) {
			int defaultcheck = 0, isactive = 1;
			if (defaultcheckattfs.isSelected()) {
				defaultcheck = 1;
			}
			if (deactivatecheck.isSelected()) {
				isactive = 0;
			}

			String status = new DAO().insertBugserverdetails("bugserver", "TFS", defaultcheck, tfsurltext.getText(),
					isactive, Long.parseLong(Integer.toString(Loggedinuserdetails.id)));

			if (status.equals("failure")) {
				runmessage("Failed to Save the TFS / VSTS Server. Please Try Again...");
			} else if (status.equals("duplicate")) {
				runmessage("Duplicate TFS URL. URL provided is already exists...");
			} else if (status.equals("success")) {
				runmessage("TFS / VSTS Server added Successfully...");
				tfsurltext.clear();
				tfsrefresh();
			}

		}
	}

	private void tfsupdate() {
		if (validatetfsfields()) {
			if (validateTFSConnection(tfsurltext.getText(), tfsprojectcombo.getSelectionModel().getSelectedItem(),
					tfsusernamecombo.getSelectionModel().getSelectedItem(), tfspasswordtext.getText())) {
				// insert
				if (selectedbugserverid != 0 && selectedprojectid == 0 && selecteduserid == 0) {
					String result = new DAO().insertBugUsersProjects(
							tfsprojectcombo.getSelectionModel().getSelectedItem(), Loggedinuserdetails.defaultproject,
							tfsusernamecombo.getSelectionModel().getSelectedItem(), tfspasswordtext.getText(),
							Loggedinuserdetails.id, selectedbugserverid);

					if (result.equals("success")) {
						runmessage("Successfully Project and User is Added to System...");
						tfsresetonsave();
						tfsresetonupdate();
					} else if (result.equals("failure")) {
						runmessage("Failed to Add. Please Try Again...");
					}
				}

				// update
				if (selectedbugserverid != 0 && selectedprojectid != 0 && selecteduserid != 0) {
					int isactive = 0, isdefault = 0;
					if (deactivatecheck.isSelected()) {
						isactive = 0;
					} else {
						isactive = 1;
					}

					if (defaultcheckattfs.isSelected()) {
						isdefault = 1;
					} else {
						isdefault = 0;
					}
					String result = new DAO().updateBugUsersProjects(
							tfsprojectcombo.getSelectionModel().getSelectedItem(), Loggedinuserdetails.defaultproject,
							tfsusernamecombo.getSelectionModel().getSelectedItem(), tfspasswordtext.getText(),
							Loggedinuserdetails.id, selectedbugserverid, selectedprojectid, selecteduserid, isdefault,
							tfsurltext.getText(), isactive, Loggedinuserdetails.id);

					if (result.equals("success")) {
						runmessage("Successfully Project and User is Updated...");
						tfsrefresh();
						tfsresetonsave();
						tfsresetonupdate();
					} else if (result.equals("failure")) {
						runmessage("Failed to Update. Please Try Again...");
					}
				}
			} else {
				runmessage("Invalid TFS / VSTS Authentication. Please provide only valid Credentials...");
			}
		}
	}

	private void tfsrefresh() {
		settfscombo();
		setdefaultcombo();
	}

	private void settfscombo() {
		statuslabel.setVisible(true);
		if (tfsurlcombo.getItems() != null) {
			tfsurlcombo.getItems().clear();
		}

		tfsurlcombo.getItems().add("Select TFS Server");
		tfsurlcombo.getSelectionModel().select(0);

		if (tfsserverlist != null && tfsserverlist.size() > 0) {
			tfsserverlist.clear();
		}

		tfsserverlist = new DAO().getTFSBugserversDetails();

		if (tfsserverlist != null && tfsserverlist.size() > 0) {
			for (int i = 0; i < tfsserverlist.size(); i++) {
				tfsurlcombo.getItems().add(tfsserverlist.get(i).getCollectionurl());
			}

			for (int i = 0; i < tfsserverlist.size(); i++) {
				if (tfsserverlist.get(i).getIsdefault().equals("1")) {
					statuslabel.setText("Default Bug Tracker : " + tfsserverlist.get(i).getCollectionurl());
					break;
				} else {
					statuslabel.setText("Default Bug Tracker : Test My Data");
				}
			}
		}
	}

	private void setdefaultcombo() {
		if (tfsusernamecombo.getItems() != null) {
			tfsusernamecombo.getItems().clear();
		}
		tfsusernamecombo.getItems().add("Enter / Select User Name");
		tfsusernamecombo.getSelectionModel().select(0);

		if (tfsprojectcombo.getItems() != null) {
			tfsprojectcombo.getItems().clear();
		}
		tfsprojectcombo.getItems().add("Enter / Select Project Name");
		tfsprojectcombo.getSelectionModel().select(0);
	}
}
