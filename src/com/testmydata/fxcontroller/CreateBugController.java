package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.dao.DAO;
import com.testmydata.dao.TFSAccess;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.tfs.CreateBugTFS;
import com.testmydata.tfs.IterationTFS;
import com.testmydata.tfs.UsersTFS;
import com.testmydata.tfs.jira.binarybeans.ProjectIterationBean;
import com.testmydata.tfs.jira.binarybeans.ProjectUsersBean;
import com.testmydata.util.ComboBoxFilter;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.DefaultBugServerDetails;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.StaticImages;

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

public class CreateBugController implements Initializable {
	@FXML
	private ImageView saveicon, updateicon, refreshicon, closeicon;
	@FXML
	private JFXTextField titletext;
	@FXML
	private JFXTextArea reprostepstextarea;
	@FXML
	private JFXComboBox<String> assignedtocombo, statecombo, reasoncombo, areacombo, iterationcombo;
	@FXML
	private Label refreshlbl, statuslabel;
	@FXML
	private AnchorPane actionanchor1;
	@FXML
	private JFXTabPane tfstab;

	static String[] stateinfo = { "New", "Active", "Resolved", "Closed" };
	static String[] newchildren = { "New", "Build Failure", "Not Fixed", "Test Failed" };
	static String[] activechildren = { "Approved", "Investigate", "Reactivated", "Regression" };
	static String[] resolvedchildren = { "As Designed", "Cannot Reproduce", "Copied to Backlog", "Deferred",
			"Duplicate", "Fixed", "Obsolete" };
	static String[] closedchildren = { "As Designed", "Cannot Reproduce", "Copied to Backlog", "Deferred", "Duplicate",
			"Fixed and Verified", "Obsolete" };

	static ArrayList<ProjectUsersBean> userslist = new ArrayList<ProjectUsersBean>();
	static ArrayList<ProjectIterationBean> iterationlist = new ArrayList<ProjectIterationBean>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ComboBoxFilter.autoCompleteComboBoxPlus(assignedtocombo,
				(typedText, iba) -> iba.toString().toLowerCase().contains(typedText.toLowerCase()));
		ComboBoxFilter.autoCompleteComboBoxPlus(iterationcombo,
				(typedText, iba) -> iba.toString().toLowerCase().contains(typedText.toLowerCase()));

		if (DefaultBugServerDetails.servertype.equals("TFS")) {
			tfstab.setDisable(false);
		} else {
			tfstab.setDisable(true);
		}

		saveicon.setImage(StaticImages.save);
		closeicon.setImage(StaticImages.closeicon);
		updateicon.setImage(StaticImages.save);
		refreshicon.setImage(StaticImages.refresh);

		updateicon.setVisible(false);

		statecombo.getItems().addAll(stateinfo);
		statecombo.getSelectionModel().select("New");

		reasoncombo.getItems().clear();
		reasoncombo.getItems().addAll(newchildren);
		reasoncombo.getSelectionModel().select("New");

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

	private void getusers(String source) {
		if (source != null && source.equals("refresh")) {
			if (userslist != null && userslist.size() > 0) {
				userslist.clear();
				userslist = UsersTFS.gettfsprojectsusers(TFSAccess.PROJECT_NAME);
				loadusers();
			}
		} else {
			userslist = UsersTFS.gettfsprojectsusers(TFSAccess.PROJECT_NAME);
			loadusers();
		}
	}

	private void loadusers() {
		if (userslist != null && userslist.size() > 0) {
			assignedtocombo.getItems().clear();
			for (int i = 0; i < userslist.size(); i++) {
				assignedtocombo.getItems().add(userslist.get(i).getUsername().toUpperCase());
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

	private void getiterations(String source) {
		if (source != null && source.equals("refresh")) {
			if (iterationlist != null && iterationlist.size() > 0) {
				iterationlist.clear();
				iterationlist = IterationTFS.getiterations(TFSAccess.PROJECT_NAME);
				setIteration();
			}
		} else {
			iterationlist = IterationTFS.getiterations(TFSAccess.PROJECT_NAME);
			setIteration();
		}
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

	public void tfssave() {
		if (validatefields()) {

			int newbugid = CreateBugTFS.createTFSBug(titletext.getText(),
					assignedtocombo.getSelectionModel().getSelectedItem(),
					statecombo.getSelectionModel().getSelectedItem(), reasoncombo.getSelectionModel().getSelectedItem(),
					areacombo.getSelectionModel().getSelectedItem(), reprostepstextarea.getText(),
					Loggedinuserdetails.firstName + " " + Loggedinuserdetails.lastName,
					iterationcombo.getSelectionModel().getSelectedItem());

			if (newbugid > 0) {
				String result = new DAO().createbugcopy(DefaultBugServerDetails.serverid, "0", "0", titletext.getText(),
						assignedtocombo.getSelectionModel().getSelectedItem(),
						statecombo.getSelectionModel().getSelectedItem(),
						reasoncombo.getSelectionModel().getSelectedItem(),
						areacombo.getSelectionModel().getSelectedItem(), reprostepstextarea.getText(),
						Loggedinuserdetails.id, Loggedinuserdetails.defaultproject, Integer.toString(newbugid));

				if (result.equals("success")) {
					runmessage("Bug ( " + newbugid + " ) Created Successfully in VSTS...");
					tfsfieldsreset();
				} else {
					runmessage("Error creating Bug. Please try again...");
				}
			}

		}
	}

	public void tfsrefresh() {
		getusers(null);
		setarea();
		getiterations(null);
	}

	public void tfsupdate() {

	}

	public void tfsfieldsreset() {
		titletext.clear();
		reprostepstextarea.clear();
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
							getusers(null);
							setarea();
							getiterations(null);
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
