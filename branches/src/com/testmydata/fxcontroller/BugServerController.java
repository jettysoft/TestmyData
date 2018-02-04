package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.dao.DAO;
import com.testmydata.dao.TFSAccess;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.ComboBoxFilter;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.StaticImages;

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
	private JFXComboBox<String> tfsurlcombo, tfsusernamecombo;
	@FXML
	private JFXTextField tfsurltext, tfsprojectnametext;
	@FXML
	private JFXPasswordField tfspasswordtext;
	@FXML
	private AnchorPane actionanchor1;
	@FXML
	private Label refreshlbl, userlabel;
	@FXML
	private JFXCheckBox deactivatecheck, activatecheck, defaultcheckattfs;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ComboBoxFilter.autoCompleteComboBoxPlus(tfsusernamecombo,
				(typedText, iba) -> iba.toString().toLowerCase().contains(typedText.toLowerCase()));

		tfsusernamecombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold; -fx-font-size:11;");
		tfsusernamecombo.getItems().add("Enter / Select User Name");
		tfsusernamecombo.getSelectionModel().select(0);

		saveicon.setImage(StaticImages.save);
		updateicon.setImage(StaticImages.save);
		refreshicon.setImage(StaticImages.refresh);
		closeicon.setImage(StaticImages.closeicon);

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

		Label sucesslbl = new Label(" Connection Successful ");
		sucesslbl.setStyle(StaticImages.lblStyle);
		sucesslbl.setMinWidth(80);
		sucesslbl.setLayoutX(385);
		sucesslbl.setLayoutY(255);
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
		faillbl.setLayoutY(255);
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
					TFSAccess.COLLECTION_URL = tfsurltext.getText();
					TFSAccess.PROJECT_NAME = tfsprojectnametext.getText();
					TFSAccess.USERNAME = tfsurlcombo.getSelectionModel().getSelectedItem();
					TFSAccess.PASSWORD = tfspasswordtext.getText();

					if (TFSAccess.connectToTFS() == null) {
						greentick1.setVisible(false);
						wrongtick1.setVisible(true);
					} else {
						wrongtick1.setVisible(false);
						greentick1.setVisible(true);
					}
				}
			}
		});

		// Bugs tables
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
			if (tfsprojectnametext.getText() == null || tfsprojectnametext.getText().isEmpty()) {
				result = false;
				tfsprojectnametext.setUnFocusColor(Color.RED);
				message.append("Please provide TFS Project Name...\n\n");
			} else {
				tfsprojectnametext.setUnFocusColor(Color.rgb(190, 190, 196));
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
			}

		}
	}

	private void tfsupdate() {

	}

	private void tfsrefresh() {

	}

}
