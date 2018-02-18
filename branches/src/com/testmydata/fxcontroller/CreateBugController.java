package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.dao.TFSAccess;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.tfs.CreateBugTFS;
import com.testmydata.tfs.jira.binarybeans.ProjectIterationBean;
import com.testmydata.tfs.jira.binarybeans.ProjectUsersBean;
import com.testmydata.util.ComboBoxFilter;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.DefaultBugServerDetails;
import com.testmydata.util.LoadTFSorJiraUsers;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.StaticImages;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class CreateBugController implements Initializable {
	@FXML
	private ImageView saveicon, updateicon, refreshicon, closeicon, tmdsaveicon, tmdupdateicon, tmdrefreshicon;
	@FXML
	private JFXTextField titletext, idtext, tmdtitletext, tmdidtext;
	@FXML
	private JFXTextArea reprostepstextarea, tmdreprostepstextarea;
	@FXML
	private JFXComboBox<String> assignedtocombo, statecombo, reasoncombo, areacombo, iterationcombo,
			linktypeselectcombo, tmdassignedtocombo, tmdstatecombo, tmdreasoncombo, tmdlinktypeselectcombo;
	@FXML
	private Label refreshlbl, statuslabel, idtitlelabel, headerlabel, tmdrefreshlbl, tmdstatuslabel, tmdidtitlelabel;
	@FXML
	private AnchorPane actionanchor1, tmdactionanchor;
	@FXML
	private JFXTabPane tfstab;

	static String[] stateinfo = { "New", "Active", "Resolved", "Closed" };
	static String[] newchildren = { "New", "Build Failure", "Not Fixed", "Test Failed" };
	static String[] activechildren = { "Approved", "Investigate", "Reactivated", "Regression" };
	static String[] resolvedchildren = { "As Designed", "Cannot Reproduce", "Copied to Backlog", "Deferred",
			"Duplicate", "Fixed", "Obsolete" };
	static String[] closedchildren = { "As Designed", "Cannot Reproduce", "Copied to Backlog", "Deferred", "Duplicate",
			"Fixed and Verified", "Obsolete" };
	static String[] tcorrule = { "Test Case", "Control Report" };

	static ArrayList<ProjectUsersBean> userslist = new ArrayList<ProjectUsersBean>();
	static ArrayList<ProjectIterationBean> iterationlist = new ArrayList<ProjectIterationBean>();
	static ArrayList<UsersDetailsBeanBinaryTrade> tmduserslist = new ArrayList<UsersDetailsBeanBinaryTrade>();

	static String bugid = null, serverbugid = null, serverid = null, tcid = null, ruleid = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ComboBoxFilter.autoCompleteComboBoxPlus(assignedtocombo,
				(typedText, iba) -> iba.toString().toLowerCase().contains(typedText.toLowerCase()));
		ComboBoxFilter.autoCompleteComboBoxPlus(iterationcombo,
				(typedText, iba) -> iba.toString().toLowerCase().contains(typedText.toLowerCase()));

		closeicon.setImage(StaticImages.closeicon);

		InvoiceStaticHelper.setcbc(this);

		if (DefaultBugServerDetails.servertype.equals("TFS")) {
			tfstab.getSelectionModel().select(0);
			loadtfsinfo();
		} else if (DefaultBugServerDetails.servertype.equals("JIRA")) {

		} else if (DefaultBugServerDetails.servertype.equals("TMD")) { // TestMyDATA
			tfstab.getSelectionModel().select(1);
			loadtestmydatainfo();
		}

		loadingservice.reset();
		loadingservice.start();

		statecombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (newFruit.equals("New")) {
						reasoncombo.getItems().clear();
						reasoncombo.getItems().addAll(newchildren);
						reasoncombo.getSelectionModel().select("New");
						reasoncombo.setLabelFloat(true);
					} else if (newFruit.equals("Active")) {
						reasoncombo.getItems().clear();
						reasoncombo.getItems().addAll(activechildren);
						reasoncombo.getSelectionModel().select("Approved");
						reasoncombo.setLabelFloat(true);
					} else if (newFruit.equals("Resolved")) {
						reasoncombo.getItems().clear();
						reasoncombo.getItems().addAll(resolvedchildren);
						reasoncombo.getSelectionModel().select("Fixed");
						reasoncombo.setLabelFloat(true);
					} else if (newFruit.equals("Closed")) {
						reasoncombo.getItems().clear();
						reasoncombo.getItems().addAll(closedchildren);
						reasoncombo.getSelectionModel().select("Fixed and Verified");
						reasoncombo.setLabelFloat(true);
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		tmdstatecombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (newFruit.equals("New")) {
						tmdreasoncombo.getItems().clear();
						tmdreasoncombo.getItems().addAll(newchildren);
						tmdreasoncombo.getSelectionModel().select("New");
						tmdreasoncombo.setLabelFloat(true);
					} else if (newFruit.equals("Active")) {
						tmdreasoncombo.getItems().clear();
						tmdreasoncombo.getItems().addAll(activechildren);
						tmdreasoncombo.getSelectionModel().select("Approved");
						tmdreasoncombo.setLabelFloat(true);
					} else if (newFruit.equals("Resolved")) {
						tmdreasoncombo.getItems().clear();
						tmdreasoncombo.getItems().addAll(resolvedchildren);
						tmdreasoncombo.getSelectionModel().select("Fixed");
						tmdreasoncombo.setLabelFloat(true);
					} else if (newFruit.equals("Closed")) {
						tmdreasoncombo.getItems().clear();
						tmdreasoncombo.getItems().addAll(closedchildren);
						tmdreasoncombo.getSelectionModel().select("Fixed and Verified");
						tmdreasoncombo.setLabelFloat(true);
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		Label savelbl = new Label("  Save ");
		savelbl.setStyle(StaticImages.lblStyle);
		savelbl.setMinWidth(40);
		savelbl.setLayoutX(65);
		savelbl.setLayoutY(15);
		savelbl.setVisible(false);
		actionanchor1.getChildren().add(savelbl);
		tmdactionanchor.getChildren().add(savelbl);

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

		tmdsaveicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				savelbl.setVisible(true);
			}
		});
		tmdsaveicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				savelbl.setVisible(false);
			}
		});
		tmdsaveicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				if (tmdvalidatefields()) {
					testmydatasave("0", tmdidtext.getText(), tmdtitletext.getText(),
							tmdassignedtocombo.getSelectionModel().getSelectedItem(),
							tmdstatecombo.getSelectionModel().getSelectedItem(),
							tmdreasoncombo.getSelectionModel().getSelectedItem(), null, tmdreprostepstextarea.getText(),
							tmdidtitlelabel.getText(), tmdlinktypeselectcombo.getSelectionModel().getSelectedItem(),
							null);
				}
			}
		});

		Label updatelbl = new Label(" Update ");
		updatelbl.setStyle(StaticImages.lblStyle);
		updatelbl.setMinWidth(40);
		updatelbl.setLayoutX(65);
		updatelbl.setLayoutY(15);
		updatelbl.setVisible(false);
		actionanchor1.getChildren().add(updatelbl);
		tmdactionanchor.getChildren().add(updatelbl);

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

		tmdupdateicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				updatelbl.setVisible(true);
			}
		});
		tmdupdateicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				updatelbl.setVisible(false);
			}
		});
		tmdupdateicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				if (tmdvalidatefields()) {
					testmydataupdate("0", tmdidtext.getText(), tmdtitletext.getText(),
							tmdassignedtocombo.getSelectionModel().getSelectedItem(),
							tmdstatecombo.getSelectionModel().getSelectedItem(),
							tmdreasoncombo.getSelectionModel().getSelectedItem(), null, tmdreprostepstextarea.getText(),
							tmdidtitlelabel.getText(), tmdlinktypeselectcombo.getSelectionModel().getSelectedItem(),
							null);
				}
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

		tmdrefreshlbl.setStyle(StaticImages.lblStyle);

		tmdrefreshicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				tmdrefreshlbl.setVisible(true);
			}
		});
		tmdrefreshicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				tmdrefreshlbl.setVisible(false);
			}
		});
		tmdrefreshicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				tmdrefresh();
			}
		});

		linktypeselectcombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (idtext.getText() != null && !idtext.getText().isEmpty() && idtext.getText() != ""
							&& idtext.getText().length() > 0) {
						String title = new DAO().searchtitles(newFruit, idtext.getText());
						if (title != null) {
							idtitlelabel.setText(title);
						} else {
							idtitlelabel.setText("");
						}
					}
				} catch (NullPointerException ne) {
				}
			}
		});
		idtext.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (!newValue || newValue) { // when focus lost
				if (idtext.getText() != null && !idtext.getText().isEmpty() && idtext.getText() != ""
						&& idtext.getText().length() > 0) {
					gainfocus();
				}
			}
		});
		titletext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (titletext.getText().length() >= 256) {
						titletext.setText(titletext.getText().substring(0, 256));
					}
				}
			}
		});

		tmdlinktypeselectcombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (tmdidtext.getText() != null && !tmdidtext.getText().isEmpty() && tmdidtext.getText() != ""
							&& tmdidtext.getText().length() > 0) {
						String title = new DAO().searchtitles(newFruit, tmdidtext.getText());
						if (title != null) {
							tmdidtitlelabel.setText(title);
						} else {
							tmdidtitlelabel.setText("");
						}
					}
				} catch (NullPointerException ne) {
				}
			}
		});
		tmdidtext.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (!newValue || newValue) { // when focus lost
				if (tmdidtext.getText() != null && !tmdidtext.getText().isEmpty() && tmdidtext.getText() != ""
						&& tmdidtext.getText().length() > 0) {
					tmdgainfocus();
				}
			}
		});
		tmdtitletext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (tmdtitletext.getText().length() >= 256) {
						tmdtitletext.setText(tmdtitletext.getText().substring(0, 256));
					}
				}
			}
		});
		closeicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				AnchorPane pane = (AnchorPane) ((ImageView) t.getSource()).getParent().getParent().getParent();
				pane.getChildren().remove(pane.getChildren().size() - 1);

				CreateBugController nc = new CreateBugController();
				Cleanup.nullifyStrings(nc);
			}
		});
	}

	// Method to display the Messages
	public void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	private void loadtfsinfo() {
		saveicon.setImage(StaticImages.save);
		updateicon.setImage(StaticImages.save);
		refreshicon.setImage(StaticImages.refresh);

		statecombo.getItems().addAll(stateinfo);
		statecombo.getSelectionModel().select("New");

		reasoncombo.getItems().clear();
		reasoncombo.getItems().addAll(newchildren);
		reasoncombo.getSelectionModel().select("New");

		linktypeselectcombo.getItems().clear();
		linktypeselectcombo.getItems().addAll(tcorrule);
		linktypeselectcombo.getSelectionModel().select("Test Case");

	}

	private void loadtestmydatainfo() {
		tmdsaveicon.setImage(StaticImages.save);
		tmdupdateicon.setImage(StaticImages.save);
		tmdrefreshicon.setImage(StaticImages.refresh);

		tmdstatecombo.getItems().addAll(stateinfo);
		tmdstatecombo.getSelectionModel().select("New");

		tmdreasoncombo.getItems().clear();
		tmdreasoncombo.getItems().addAll(newchildren);
		tmdreasoncombo.getSelectionModel().select("New");

		tmdlinktypeselectcombo.getItems().clear();
		tmdlinktypeselectcombo.getItems().addAll(tcorrule);
		tmdlinktypeselectcombo.getSelectionModel().select("Test Case");
	}

	private void tmdgetusers() {
		tmduserslist = LoadTFSorJiraUsers.tmduserslist;
		tmdloadusers();
	}

	private void tmdloadusers() {
		if (tmduserslist != null && tmduserslist.size() > 0) {
			tmdassignedtocombo.getItems().clear();
			for (int i = 0; i < tmduserslist.size(); i++) {
				tmdassignedtocombo.getItems().add(tmduserslist.get(i).getCombinedname());

				if (tmduserslist.get(i).getCombinedname().toUpperCase()
						.contains(Loggedinuserdetails.id + "-" + Loggedinuserdetails.firstName.toUpperCase() + "-"
								+ Loggedinuserdetails.lastName.toUpperCase())) {
					tmdassignedtocombo.getSelectionModel().select(tmduserslist.get(i).getCombinedname());
				} else {
					tmdassignedtocombo.getSelectionModel().select(0);
				}
				tmdassignedtocombo.setLabelFloat(true);
			}
		} else {
			tmdassignedtocombo.getItems().clear();
			tmdassignedtocombo.getItems().add("No Users Found");
			tmdassignedtocombo.getSelectionModel().select(0);
			tmdassignedtocombo.setLabelFloat(true);
		}
	}

	private void getusers() {
		userslist = LoadTFSorJiraUsers.tfsuserslist;
		loadusers();
	}

	private void loadusers() {
		if (userslist != null && userslist.size() > 0) {
			assignedtocombo.getItems().clear();
			for (int i = 0; i < userslist.size(); i++) {
				assignedtocombo.getItems().add(userslist.get(i).getUsername());
				if (userslist.get(i).getUsername().toUpperCase().contains(
						Loggedinuserdetails.firstName.toUpperCase() + " " + Loggedinuserdetails.lastName.toUpperCase())
						|| userslist.get(i).getUsername().toUpperCase()
								.contains(Loggedinuserdetails.lastName.toUpperCase() + " "
										+ Loggedinuserdetails.firstName.toUpperCase())) {
					assignedtocombo.getSelectionModel().select(userslist.get(i).getUsername());
				} else {
					assignedtocombo.getSelectionModel().select(0);
				}
				assignedtocombo.setLabelFloat(true);
			}
		} else {
			assignedtocombo.getItems().clear();
			assignedtocombo.getItems().add("No Users Found");
			assignedtocombo.getSelectionModel().select(0);
			assignedtocombo.setLabelFloat(true);
		}
	}

	private void setarea() {
		areacombo.getItems().clear();
		areacombo.getItems().add(TFSAccess.PROJECT_NAME);
		areacombo.getSelectionModel().select(0);
		areacombo.setLabelFloat(true);
	}

	private void getiterations() {
		iterationlist = LoadTFSorJiraUsers.tfsiterationlist;
		setIteration();
	}

	private void setIteration() {
		if (iterationlist != null && iterationlist.size() > 0) {
			iterationcombo.getItems().clear();
			for (int i = 0; i < iterationlist.size(); i++) {
				iterationcombo.getItems().add(iterationlist.get(i).getIterationpath());
				iterationcombo.getSelectionModel().select(0);
				iterationcombo.setLabelFloat(true);
			}
		} else {
			iterationcombo.getItems().clear();
			iterationcombo.getItems().add("No Iteration Specified");
			iterationcombo.getSelectionModel().select(0);
			iterationcombo.setLabelFloat(true);
		}
	}

	private boolean validatefields() {
		boolean result = true;
		StringBuffer message = new StringBuffer();

		if (titletext.getText() == null || titletext.getText().isEmpty()) {
			result = false;
			titletext.setUnFocusColor(Color.RED);
			message.append("Please provide Bug Title...\n\n");
		} else {
			titletext.setUnFocusColor(Color.rgb(190, 190, 196));
		}
		if (reprostepstextarea.getText() == null || reprostepstextarea.getText().isEmpty()) {
			result = false;
			reprostepstextarea.setUnFocusColor(Color.RED);
			message.append("Please provide Steps to Reproduce...\n\n");
		} else {
			reprostepstextarea.setUnFocusColor(Color.WHITE);
		}

		if (result == false) {
			runmessage(message.toString());
		}
		return result;
	}

	private boolean tmdvalidatefields() {
		boolean result = true;
		StringBuffer message = new StringBuffer();

		if (tmdtitletext.getText() == null || tmdtitletext.getText().isEmpty()) {
			result = false;
			tmdtitletext.setUnFocusColor(Color.RED);
			message.append("Please provide Bug Title...\n\n");
		} else {
			tmdtitletext.setUnFocusColor(Color.rgb(190, 190, 196));
		}
		if (tmdreprostepstextarea.getText() == null || tmdreprostepstextarea.getText().isEmpty()) {
			result = false;
			tmdreprostepstextarea.setUnFocusColor(Color.RED);
			message.append("Please provide Steps to Reproduce...\n\n");
		} else {
			tmdreprostepstextarea.setUnFocusColor(Color.WHITE);
		}

		if (result == false) {
			runmessage(message.toString());
		}
		return result;
	}

	private void tmdrefresh() {
		tmdgetusers();
	}

	private void testmydatasave(String newbugid, String id, String title, String assignedto, String state,
			String reason, String area, String reprosteps, String idtitle, String linktype, String iteration) {
		String result = null, serverid = null, iterationpath = null;
		if (!newbugid.equals("0")) {
			serverid = DefaultBugServerDetails.serverid;
			iterationpath = iteration;
		} else {
			serverid = "0";
			newbugid = "0";
		}

		if (id == null || id.length() == 0 || id == "") {
			result = new DAO().createbugcopy(serverid, "0", "0", title, assignedto, state, reason, area, reprosteps,
					Loggedinuserdetails.id, Loggedinuserdetails.defaultproject, newbugid, iterationpath);
		} else {
			if (idtitle != null && idtitle.length() > 1) {
				if (linktype.equals("Test Case")) {
					result = new DAO().createbugcopy(serverid, id, "0", title, assignedto, state, reason, area,
							reprosteps, Loggedinuserdetails.id, Loggedinuserdetails.defaultproject, newbugid,
							iterationpath);
				} else if (linktype.equals("Control Report")) {
					result = new DAO().createbugcopy(serverid, "0", id, title, assignedto, state, reason, area,
							reprosteps, Loggedinuserdetails.id, Loggedinuserdetails.defaultproject, newbugid,
							iterationpath);
				}
			}

		}
		if (result.equals("success") && !newbugid.equals("0")) {
			runmessage("Bug ( " + newbugid + " ) Created Successfully...");
			postactions();
		} else if (result.equals("success") && newbugid.equals("0")) {
			runmessage("Bug Created Successfully...");
			postactions();
		} else if (result.equals("failure") && (newbugid.equals("0") || !newbugid.equals("0"))) {
			runmessage("Error creating Bug. Please try again...");
		}
	}

	private void testmydataupdate(String newbugid, String id, String title, String assignedto, String state,
			String reason, String area, String reprosteps, String idtitle, String linktype, String iteration) {
		String result = null, serveridup = null, iterationpath = null;
		if (!newbugid.equals("0")) {
			serveridup = serverid;
			iterationpath = iteration;
		} else {
			serveridup = "0";
			newbugid = "0";
		}

		if (id == null || id.length() == 0 || id == "") {
			result = new DAO().updatebugcopy(serveridup, "0", "0", title, assignedto, state, reason, area, reprosteps,
					Loggedinuserdetails.id, Loggedinuserdetails.defaultproject, newbugid, iterationpath, bugid,
					"update");
		} else {
			if (idtitle != null && idtitle.length() > 1) {
				if (linktype.equals("Test Case")) {
					result = new DAO().updatebugcopy(serveridup, id, "0", title, assignedto, state, reason, area,
							reprosteps, Loggedinuserdetails.id, Loggedinuserdetails.defaultproject, newbugid,
							iterationpath, bugid, "update");
				} else if (linktype.equals("Control Report")) {
					result = new DAO().updatebugcopy(serveridup, "0", id, title, assignedto, state, reason, area,
							reprosteps, Loggedinuserdetails.id, Loggedinuserdetails.defaultproject, newbugid,
							iterationpath, bugid, "update");
				}
			}

		}

		if (result.equals("success") && !newbugid.equals("0")) {
			runmessage("Bug ( " + newbugid + " ) Updated Successfully...");
			postactions();
		} else if (result.equals("success") && newbugid.equals("0")) {
			runmessage("Bug Updated Successfully...");
			postactions();
		} else if (result.equals("failure") && (newbugid.equals("0") || !newbugid.equals("0"))) {
			runmessage("Error updating Bug. Please try again...");
		}
	}

	public void tmdfieldsreset() {
		tmdtitletext.clear();
		tmdreprostepstextarea.clear();
	}

	public void tfssave() {
		if (validatefields()) {

			int newbugid = CreateBugTFS.createTFSBug(titletext.getText(),
					assignedtocombo.getSelectionModel().getSelectedItem(),
					statecombo.getSelectionModel().getSelectedItem(), reasoncombo.getSelectionModel().getSelectedItem(),
					areacombo.getSelectionModel().getSelectedItem(),
					reprostepstextarea.getText().replaceAll("\n", "<br>"),
					Loggedinuserdetails.firstName + " " + Loggedinuserdetails.lastName,
					iterationcombo.getSelectionModel().getSelectedItem());

			if (newbugid > 0) {
				testmydatasave(Integer.toString(newbugid), idtext.getText(), titletext.getText(),
						assignedtocombo.getSelectionModel().getSelectedItem(),
						statecombo.getSelectionModel().getSelectedItem(),
						reasoncombo.getSelectionModel().getSelectedItem(),
						areacombo.getSelectionModel().getSelectedItem(), reprostepstextarea.getText(),
						idtitlelabel.getText(), linktypeselectcombo.getSelectionModel().getSelectedItem(),
						iterationcombo.getSelectionModel().getSelectedItem());
			}

		}
	}

	public void tfsrefresh() {
		LoadTFSorJiraUsers ltj = new LoadTFSorJiraUsers();
		ltj.initialize(null, null);

		getusers();
		setarea();
		getiterations();
	}

	public void tfsupdate() {
		if (validatefields()) {

			int newbugid = CreateBugTFS.updateTFSBug(Integer.parseInt(serverbugid), titletext.getText(),
					assignedtocombo.getSelectionModel().getSelectedItem(),
					statecombo.getSelectionModel().getSelectedItem(), reasoncombo.getSelectionModel().getSelectedItem(),
					areacombo.getSelectionModel().getSelectedItem(),
					reprostepstextarea.getText().replaceAll("\n", "<br>"),
					Loggedinuserdetails.firstName + " " + Loggedinuserdetails.lastName,
					iterationcombo.getSelectionModel().getSelectedItem());

			if (newbugid > 0) {
				testmydataupdate(Integer.toString(newbugid), idtext.getText(), titletext.getText(),
						assignedtocombo.getSelectionModel().getSelectedItem(),
						statecombo.getSelectionModel().getSelectedItem(),
						reasoncombo.getSelectionModel().getSelectedItem(),
						areacombo.getSelectionModel().getSelectedItem(), reprostepstextarea.getText(),
						idtitlelabel.getText(), linktypeselectcombo.getSelectionModel().getSelectedItem(),
						iterationcombo.getSelectionModel().getSelectedItem());
			}
		}
	}

	public void tfsfieldsreset() {
		titletext.clear();
		reprostepstextarea.clear();
	}

	public void newfieldsetter(String tcid1, String ruleid1, String reprosteps, String servertype1, String title) {
		if (servertype1.contains("TFS")) { // set in TFS Pane
			if (!tcid1.equals("0") && ruleid1.equals("0")) {
				tfstab.getSelectionModel().select(0);
				linktypeselectcombo.getSelectionModel().select(0);
				idtext.setText(tcid1);
				gainfocus();
			} else if (tcid1.equals("0") && !ruleid1.equals("0")) {
				linktypeselectcombo.getSelectionModel().select(1);
				idtext.setText(ruleid1);
				gainfocus();
			} else {
				linktypeselectcombo.getSelectionModel().select(0);
			}
			titletext.setText(title);
			reprostepstextarea.setText(reprosteps);
		} else if (servertype1.contains("JIRA")) {

		} else if (servertype1.contains("TMD")) { // set in TMD Pane
			tfstab.getSelectionModel().select(1);
			if (!tcid1.equals("0") && ruleid1.equals("0")) {
				tmdlinktypeselectcombo.getSelectionModel().select(0);
				tmdidtext.setText(tcid1);
				tmdgainfocus();
			} else if (tcid1.equals("0") && !ruleid1.equals("0")) {
				tmdlinktypeselectcombo.getSelectionModel().select(1);
				tmdidtext.setText(ruleid1);
				tmdgainfocus();
			} else {
				tmdlinktypeselectcombo.getSelectionModel().select(0);
			}
			tmdtitletext.setText(title);
			tmdreprostepstextarea.setText(reprosteps);
		}
	}

	public void updatefieldsetter(String id1, String serverbugid1, String title, String assignedto, String state,
			String reason, String area, String iterationpath, String tcid1, String ruleid1, String servertype1,
			String serverid1, String reprosteps) {

		headerlabel.setText("Update Bug");

		if (servertype1.contains("TFS")) { // set in TFS Pane
			tfstab.getSelectionModel().select(0);
			titletext.setText(title);
			saveicon.setVisible(false);
			updateicon.setVisible(true);

			if (!assignedtocombo.getItems().contains(assignedto)) {
				assignedtocombo.getItems().add(assignedto);
			}

			assignedtocombo.getSelectionModel().select(assignedto);
			statecombo.getSelectionModel().select(state);
			reasoncombo.getSelectionModel().select(reason);

			if (!tcid1.equals("0") && ruleid1.equals("0")) {
				linktypeselectcombo.getSelectionModel().select(0);
				idtext.setText(tcid1);
				gainfocus();
			} else if (tcid1.equals("0") && !ruleid1.equals("0")) {
				linktypeselectcombo.getSelectionModel().select(1);
				idtext.setText(ruleid1);
				gainfocus();
			} else {
				linktypeselectcombo.getSelectionModel().select(0);
			}

			reprostepstextarea.setText(reprosteps);

			bugid = id1;
			serverbugid = serverbugid1;
			serverid = serverid1;
		} else if (servertype1.contains("JIRA")) {

		} else if (servertype1.contains("TMD") || servertype1.contains("TestmyData")) { // TMDPane
			tfstab.getSelectionModel().select(1);
			tmdtitletext.setText(title);
			tmdsaveicon.setVisible(false);
			tmdupdateicon.setVisible(true);

			if (!tmdassignedtocombo.getItems().contains(assignedto)) {
				tmdassignedtocombo.getItems().add(assignedto);
			}

			tmdassignedtocombo.getSelectionModel().select(assignedto);
			tmdstatecombo.getSelectionModel().select(state);
			tmdreasoncombo.getSelectionModel().select(reason);

			if (!tcid1.equals("0") && ruleid1.equals("0")) {
				tmdlinktypeselectcombo.getSelectionModel().select(0);
				tmdidtext.setText(tcid1);
				tmdgainfocus();
			} else if (tcid1.equals("0") && !ruleid1.equals("0")) {
				tmdlinktypeselectcombo.getSelectionModel().select(1);
				tmdidtext.setText(ruleid1);
				tmdgainfocus();
			} else {
				tmdlinktypeselectcombo.getSelectionModel().select(0);
			}

			tmdreprostepstextarea.setText(reprosteps);

			bugid = id1;
			serverbugid = serverbugid1;
			serverid = serverid1;
		}
	}

	private void gainfocus() {
		String title = new DAO().searchtitles(linktypeselectcombo.getSelectionModel().getSelectedItem(),
				idtext.getText());
		if (title != null) {
			idtitlelabel.setText(title);
		} else {
			idtitlelabel.setText("");
		}
	}

	private void tmdgainfocus() {
		String title = new DAO().searchtitles(tmdlinktypeselectcombo.getSelectionModel().getSelectedItem(),
				tmdidtext.getText());
		if (title != null) {
			tmdidtitlelabel.setText(title);
		} else {
			tmdidtitlelabel.setText("");
		}
	}

	private void postactions() {
		if (bugid != null && bugid.length() > 0) { // UpdateActions
			if (DefaultBugServerDetails.servertype.equals("TFS")) {
				tfsfieldsreset();
			} else if (DefaultBugServerDetails.servertype.equals("JIRA")) {

			} else if (DefaultBugServerDetails.servertype.equals("TMD")) { // TestMyDATA
				tmdfieldsreset();
			}
			updatesucessactions();
		} else { // NewSaveActions
			if (DefaultBugServerDetails.servertype.equals("TFS")) {
				tfsfieldsreset();
			} else if (DefaultBugServerDetails.servertype.equals("JIRA")) {

			} else if (DefaultBugServerDetails.servertype.equals("TMD")) { // TestMyDATA
				tmdfieldsreset();
			}
		}
	}

	private void updatesucessactions() {
		headerlabel.setText("New Bug");

		if (DefaultBugServerDetails.servertype.equals("TFS")) {
			updateicon.setVisible(false);
			saveicon.setVisible(true);

		} else if (DefaultBugServerDetails.servertype.equals("JIRA")) {

		} else if (DefaultBugServerDetails.servertype.equals("TMD")) { // TestMyDATA
			tmdupdateicon.setVisible(false);
			tmdsaveicon.setVisible(true);
		}

		InvoiceStaticHelper.vblc.refresh();

		ActionEvent t = new ActionEvent();
		closeicon.fireEvent(t);

	}

	Service<Void> loadingservice = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					// Background work
					final CountDownLatch latch = new CountDownLatch(1);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (DefaultBugServerDetails.servertype != null) {
								if (DefaultBugServerDetails.servertype.equals("TFS")) { // TfsData
									getusers();
									setarea();
									getiterations();
								} else if (DefaultBugServerDetails.servertype.equals("JIRA")) {

								} else if (DefaultBugServerDetails.servertype.contains("TMD")) {
									tmdgetusers();
								}
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
