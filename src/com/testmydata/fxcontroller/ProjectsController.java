package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.ProjectsBeanBinaryTrade;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
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
import javafx.stage.Stage;

public class ProjectsController implements Initializable {
	Stage myStage;
	@FXML
	private JFXComboBox<String> ownercombo, exisitingprojectscombo;
	@FXML
	private JFXTextField projectnametext, searchtext;
	@FXML
	private ImageView  closeicon,saveicon, updateicon, refreshicon1, refreshicon;
	@FXML
	private AnchorPane projectspane,actionanchor1, actionanchor2;
	@FXML
	private Label refreshlbl, refreshlbl1;
	static String[] selectedowner = null, selectedproject = null;
	ArrayList<UsersDetailsBeanBinaryTrade> userslist = new ArrayList<UsersDetailsBeanBinaryTrade>();
	ArrayList<ProjectsBeanBinaryTrade> projectslist = new ArrayList<ProjectsBeanBinaryTrade>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		saveicon.setImage(StaticImages.save.getImage());
		updateicon.setImage(StaticImages.save.getImage());
		refreshicon1.setImage(StaticImages.refresh.getImage());
		refreshicon.setImage(StaticImages.refresh.getImage());
		closeicon.setImage(StaticImages.closeicon.getImage());
		updateicon.setVisible(false);

		setexistingusers();
		setexistingprojects();

		projectnametext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (projectnametext.getText().length() >= 256) {
						projectnametext.setText(projectnametext.getText().substring(0, 256));
					}
				}
			}
		});

		ownercombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("Select Project Owner")) {
						selectedowner = ownercombo.getSelectionModel().getSelectedItem().split("-");
					}
				}
			}
		});

		exisitingprojectscombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("Select Project")) {
						selectedproject = exisitingprojectscombo.getSelectionModel().getSelectedItem().split("-");
						loadprojectdetails(Integer.parseInt(selectedproject[0]));
					}
				}
			}
		});

		Label savelbl = new Label("   Save ");
		savelbl.setStyle(StaticImages.lblStyle);
		savelbl.setMinWidth(45);
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

		Label updatelbl = new Label("   Update ");
		updatelbl.setStyle(StaticImages.lblStyle);
		updatelbl.setMinWidth(55);
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

		refreshlbl.setStyle(StaticImages.lblStyle);
		refreshlbl1.setStyle(StaticImages.lblStyle);

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

		refreshicon1.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				refreshlbl1.setVisible(true);
			}
		});
		refreshicon1.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				refreshlbl1.setVisible(false);
			}
		});

		saveicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				save();
			}
		});
		updateicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				update();
			}
		});
		refreshicon1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				refresh1();
			}
		});
		refreshicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				refresh();
			}
		});
		closeicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				AnchorPane pane = (AnchorPane) ((ImageView) t.getSource()).getParent().getParent().getParent();
				pane.getChildren().remove(pane.getChildren().size() - 1);

				ProjectsController nc = new ProjectsController();
				Cleanup.nullifyStrings(nc);
			}
		});
/*
		homeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(MouseEvent event) {
				Cleanup scu = new Cleanup();
				ProjectsController nc = new ProjectsController();
				scu.nullifyStrings(nc);

				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();
				UndecoratorController.getInstance(null);
			}
		});
		*/
	}
	

	private void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	private boolean validfields() {
		boolean result = true;

		if (ownercombo.getSelectionModel().getSelectedIndex() == 0) {
			result = false;
			runmessage("Please Select Project Owner...");
		} else if (projectnametext == null || projectnametext.getText().isEmpty()) {
			result = false;
			runmessage("Please Enter Project Name...");
		}

		return result;
	}

	private void save() {
		if (validfields()) {
			String result = new DAO().createprojectstable("projects", projectnametext.getText(), selectedowner[0],
					Long.parseLong(Integer.toString(Loggedinuserdetails.id)));

			if (result.equals("success")) {
				resetdefault();
				runmessage("Project Added Successfully...");
			} else if (result.equals("error")) {
				runmessage("Error while adding New Project. Please try restarting the System...");
			} else if (result.equals("duplicate")) {
				runmessage("Project Already Exists. Please Try with New Project Name...");
			} else {
				runmessage("Failed to Create Project. Please try restarting the System...");
			}
		}
	}

	private void update() {
		if (validfields()) {
			String result = new DAO().updateprojects("projects", Integer.parseInt(selectedproject[0]),
					projectnametext.getText(), selectedowner[0],
					Long.parseLong(Integer.toString(Loggedinuserdetails.id)), 1);

			if (result.equals("success")) {
				resetdefault();
				runmessage("Project Updated Successfully...");
			} else if (result.equals("error")) {
				runmessage("Error while Updating Project. Please try restarting the System...");
			} else {
				runmessage("Failed to Update Project. Please try restarting the System...");
			}
		}
	}

	private void refresh1() {
		setexistingprojects();
		setexistingusers();
	}

	private void refresh() {
	}

	private void setexistingusers() {
		ownercombo.getItems().clear();
		ownercombo.getItems().add("Select Project Owner");
		userslist = new DAO().getuserlist();
		if (userslist != null && userslist.size() > 0) {
			for (int i = 0; i < userslist.size(); i++) {
				ownercombo.getItems().add(userslist.get(i).getCombinedname());
			}
		}
		ownercombo.getSelectionModel().select(0);
	}

	private void setexistingprojects() {
		if (exisitingprojectscombo.getItems().size() > 0) {
			exisitingprojectscombo.getItems().clear();
		}
		exisitingprojectscombo.getItems().add("Select Project");

		projectslist = new DAO().getprojectnames();
		if (projectslist != null && projectslist.size() > 0) {
			for (int i = 0; i < projectslist.size(); i++) {
				exisitingprojectscombo.getItems().add(projectslist.get(i).getName());
			}
		}
		exisitingprojectscombo.getSelectionModel().select(0);
	}

	private void loadprojectdetails(int projectid) {
		ArrayList<ProjectsBeanBinaryTrade> plist = new ArrayList<ProjectsBeanBinaryTrade>();
		plist = new DAO().getprojectDetails(1, projectid);
		if (plist != null) {
			ownercombo.getSelectionModel().select(Integer.parseInt(plist.get(0).getOwner()));
			projectnametext.setText(plist.get(0).getName());
			saveicon.setVisible(false);
			updateicon.setVisible(true);
		}

	}

	private void resetdefault() {
		updateicon.setVisible(false);
		saveicon.setVisible(true);
		exisitingprojectscombo.getSelectionModel().select(0);
		projectnametext.setText("");
		ownercombo.getSelectionModel().select(0);
		selectedproject = null;
		selectedowner = null;
	}
}
