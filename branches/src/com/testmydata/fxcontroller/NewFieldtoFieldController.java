package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.FieldtoFieldBinaryTrade;
import com.testmydata.binarybeans.ModulesBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxutil.UndecoratorController;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.CustomComparator;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.StaticImages;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class NewFieldtoFieldController implements Initializable {

	private static NewFieldtoFieldController userHome = null;
	@FXML
	private ImageView homeicon, searchicon, closeicon;
	@FXML
	private JFXComboBox<String> modulecombo;
	@FXML
	private JFXTextField tsnametext, tcnametext, searchtext;
	@FXML
	private JFXTextArea tctextarea, sqlscripttextarea;
	@FXML
	private AnchorPane searchanchor;
	@FXML
	private JFXDatePicker startdate, enddate;
	@FXML
	private JFXButton test, save, show, update, searchbuttonlabel;
	@FXML
	private TableView<FieldtoFieldBinaryTrade> tctable;
	@FXML
	private TableColumn<FieldtoFieldBinaryTrade, String> id, module, testscenario, testcase, testcondition, sqlscript,
			createdby, createddate;
	@FXML
	private TableColumn<FieldtoFieldBinaryTrade, Boolean> modifybutton = new TableColumn<FieldtoFieldBinaryTrade, Boolean>(
			"Modify");
	@FXML
	private TableColumn<FieldtoFieldBinaryTrade, Boolean> deletebutton = new TableColumn<FieldtoFieldBinaryTrade, Boolean>(
			"Delete");
	Stage myStage;
	ArrayList<ModulesBinaryTrade> moduleslist = new ArrayList<ModulesBinaryTrade>();
	ArrayList<FieldtoFieldBinaryTrade> testcaselist = new ArrayList<FieldtoFieldBinaryTrade>();
	static String testcaseid = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setexistingmodules();
		setdatesinitially();
		populatetestcases();
		homeicon.setImage(StaticImages.homeicon.getImage());
		searchicon.setImage(StaticImages.searchicon.getImage());
		closeicon.setImage(StaticImages.wrong_tick.getImage());

		startdate.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		enddate.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");

		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		module.setCellValueFactory(new PropertyValueFactory<>("modulename"));
		testscenario.setCellValueFactory(new PropertyValueFactory<>("tsname"));
		testcase.setCellValueFactory(new PropertyValueFactory<>("tcname"));
		testcondition.setCellValueFactory(new PropertyValueFactory<>("testcondition"));
		sqlscript.setCellValueFactory(new PropertyValueFactory<>("sqlscript"));
		createdby.setCellValueFactory(new PropertyValueFactory<>("createdby"));
		createddate.setCellValueFactory(new PropertyValueFactory<>("createddate"));

		id.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		module.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
		testscenario.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		testcase.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		testcondition.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
		sqlscript.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
		createdby.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		createddate.setStyle("-fx-text-fill: blue; -fx-font-weight:bold;");

		modifybutton.setSortable(false);
		modifybutton.setCellValueFactory(new PropertyValueFactory<>("buttons"));
		modifybutton.setPrefWidth(85);
		modifybutton.setResizable(false);
		modifybutton.setCellFactory(
				new Callback<TableColumn<FieldtoFieldBinaryTrade, Boolean>, TableCell<FieldtoFieldBinaryTrade, Boolean>>() {
					@Override
					public TableCell<FieldtoFieldBinaryTrade, Boolean> call(
							TableColumn<FieldtoFieldBinaryTrade, Boolean> p) {
						return new ModifyButtonCell();
					}
				});
		tctable.getColumns().add(modifybutton);

		deletebutton.setSortable(false);
		deletebutton.setCellValueFactory(new PropertyValueFactory<>("buttons1"));
		deletebutton.setPrefWidth(85);
		deletebutton.setResizable(false);
		deletebutton.setCellFactory(
				new Callback<TableColumn<FieldtoFieldBinaryTrade, Boolean>, TableCell<FieldtoFieldBinaryTrade, Boolean>>() {
					@Override
					public TableCell<FieldtoFieldBinaryTrade, Boolean> call(
							TableColumn<FieldtoFieldBinaryTrade, Boolean> p) {
						return new DeleteButtonCell();
					}
				});
		tctable.getColumns().add(deletebutton);

		tcnametext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (tcnametext.getText().length() >= 500) {
						tcnametext.setText(tcnametext.getText().substring(0, 500));
					}
				}
			}
		});
		tsnametext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (tsnametext.getText().length() >= 500) {
						tsnametext.setText(tsnametext.getText().substring(0, 500));
					}
				}
			}
		});
		tctextarea.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (tctextarea.getText().length() >= 8000) {
						tctextarea.setText(tctextarea.getText().substring(0, 8000));
					}
				}
			}
		});
		sqlscripttextarea.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (sqlscripttextarea.getText().length() >= 8000) {
						sqlscripttextarea.setText(sqlscripttextarea.getText().substring(0, 8000));
					}
				}
			}
		});

		searchtext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String enteredString = searchtext.getText().toString();
				if (enteredString.length() >= 1) {
					@SuppressWarnings("unchecked")
					ArrayList<FieldtoFieldBinaryTrade> filteredData = filterByDescription(testcaselist, enteredString);
					populateTable(filteredData);
				} else if (enteredString != null && enteredString.length() == 0) {
					populateTable(testcaselist);
				}
			}
		});

		searchicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				searchanchor.setVisible(true);
				searchbuttonlabel.setVisible(true);
			}
		});

		closeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				searchtext.clear();
				searchanchor.setVisible(false);
				searchbuttonlabel.setVisible(false);
			}
		});

		// closing screen when clicks on home icon
		homeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(MouseEvent event) {
				Cleanup scu = new Cleanup();
				NewFieldtoFieldController nc = new NewFieldtoFieldController();
				scu.nullifyStrings(nc);

				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();
				UndecoratorController.getInstance(null);
			}
		});
	}

	// Method used to get data from previous class
	public static NewFieldtoFieldController getInstance() {
		return userHome;
	}

	// Method to display the Messages
	public void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	// Method to display the Results
	public void showresult(String result, String status, String message) {
		CommonFunctions.queryresult = result;
		CommonFunctions.teststatus = status;
		CommonFunctions.message = message;
		CommonFunctions.invokeTestResultsDialog(getClass());
	}

	// Code to control Modules Combo Box
	private void setexistingmodules() {
		modulecombo.getItems().clear();
		modulecombo.getItems().add("QA Modules");
		modulecombo.getSelectionModel().select(0);
		moduleslist = new DAO().getModuleDetails("modules", "all");
		if (moduleslist != null && moduleslist.size() > 0) {
			for (int i = 0; i < moduleslist.size(); i++) {
				modulecombo.getItems().add(moduleslist.get(i).getModulename());
			}
		}
		modulecombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
	}

	@FXML
	private void test() {
		if (sqlscripttextarea != null && !sqlscripttextarea.equals("")) {
			String result = new DAO().getTestResult(sqlscripttextarea.getText());
			if (result.equals("conerror")) {
				runmessage("Unable to Connect to the QA Server. Please check QA Server settings...");
			} else if (result.equals("noserver")) {
				runmessage("Unable to find default QA Server. Please check QA Server settings...");
			} else if (result.equals("runerror")) {
				runmessage("Runtime Error. Please Try Again...");
			} else if (result.matches("\\d+")) {
				if (Integer.parseInt(result) == 0) {
					showresult(result, "Pass", "Test Success");
				} else {
					showresult(result, "Fail", "Test Failure");
				}
			} else {
				showresult("", "FAILED", result);
			}
		} else {

		}
	}

	@FXML
	private void save() {
		if (validatefields()) {
			String result = new DAO().createtestcasestable("testcases", Loggedinuserdetails.id,
					modulecombo.getSelectionModel().getSelectedItem(), tsnametext.getText(), tcnametext.getText(),
					tctextarea.getText(), sqlscripttextarea.getText());

			if (result.equals("success")) {
				setdefault();
				populatetestcases();
				runmessage("Successfully Test Case Added...");
			} else if (result.equals("duplicate")) {
				runmessage("Given Test Case Already Exists. Please Try New Test Case Name...");
			} else {
				runmessage("Failed to Add Test Case, Please Try Again...");
			}
		}
	}

	private boolean validatefields() {
		boolean result = true;
		if (modulecombo.getSelectionModel().getSelectedItem().equals("QA Modules")) {
			result = false;
			runmessage("Please Select QA Module for the Test Case...");
		} else if (tsnametext.getText() == null || tsnametext.getText().isEmpty()) {
			result = false;
			runmessage("Please Enter Test Scenario Name...");
		} else if (tcnametext.getText() == null || tcnametext.getText().isEmpty()) {
			result = false;
			runmessage("Please Enter Test Case Name...");
		} else if (tctextarea.getText() == null || tctextarea.getText().isEmpty()) {
			result = false;
			runmessage("Please Specify Test Condition or Mapping Rule...");
		} else if (sqlscripttextarea.getText() == null || sqlscripttextarea.getText().isEmpty()) {
			result = false;
			runmessage("Please Enter SQL Script.\n\nNote: For Best Result Please Test Sql Script before saving...");
		}
		return result;
	}

	private void setdatesinitially() {
		startdate.setValue(CommonFunctions.getdateforpicker(366));
		enddate.setValue(CommonFunctions.getdateforpicker(0));
	}

	private void setdefault() {
		tsnametext.clear();
		tcnametext.clear();
		tctextarea.clear();
		sqlscripttextarea.clear();
		save.setVisible(true);
		update.setVisible(false);
	}

	@FXML
	private void show() {
		if (searchtext.getText() != null && !searchtext.getText().isEmpty()) {
			String enteredString = searchtext.getText().toString();
			if (enteredString.length() >= 1) {
				@SuppressWarnings("unchecked")
				ArrayList<FieldtoFieldBinaryTrade> filteredData = filterByDescription(testcaselist, enteredString);
				populateTable(filteredData);
			} else if (enteredString != null && enteredString.length() == 0) {
				populateTable(testcaselist);
			}
		} else {
			removePrevioustestcasesfromtable();
			populatetestcases();
		}
	}

	private void populatetestcases() {
		try {

			testcaselist = new DAO().getTestCasesDetails("testcases", startdate.getValue().toString(),
					enddate.getValue().toString(), null);
			if (testcaselist == null || testcaselist.size() == 0) {
				searchtext.setDisable(true);
				removePrevioustestcasesfromtable();
			} else {
				searchtext.setDisable(false);
				if (testcaselist.size() >= 2) {
					Collections.sort(testcaselist, new CustomComparator());
				}
				populateTable(testcaselist);
			}

		} catch (Exception e) {

		}
	}

	private void populateTable(ArrayList<FieldtoFieldBinaryTrade> arrayListData) {
		if (arrayListData != null && arrayListData.size() > 0) {
			ObservableList<FieldtoFieldBinaryTrade> data = FXCollections.observableArrayList();

			for (int i = 0; i < arrayListData.size(); i++) {
				FieldtoFieldBinaryTrade ffb = arrayListData.get(i);
				ffb.setId(arrayListData.get(i).getId());
				ffb.setModulename(arrayListData.get(i).getModulename());
				ffb.setTsname(arrayListData.get(i).getTsname());
				ffb.setTcname(arrayListData.get(i).getTcname());
				ffb.setTestcondition(arrayListData.get(i).getTestcondition());
				ffb.setSqlscript(arrayListData.get(i).getSqlscript());
				ffb.setCreatedby(arrayListData.get(i).getCreatedby());
				ffb.setCreateddate(arrayListData.get(i).getCreateddate());

				data.add(ffb);
				tctable.setItems(data);
				tctable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			}
			tctable.refresh();

		} else {
			removePrevioustestcasesfromtable();
			CommonFunctions.message = "No Test Cases Found...!";
			CommonFunctions.invokeAlertBox(getClass());
		}
	}

	@SuppressWarnings("rawtypes")
	private ArrayList filterByDescription(ArrayList<FieldtoFieldBinaryTrade> unFiltered, String str) {

		ArrayList<FieldtoFieldBinaryTrade> expens = new ArrayList<FieldtoFieldBinaryTrade>();
		for (FieldtoFieldBinaryTrade bean : unFiltered) {
			if (bean.getModulename() != null && bean.getModulename().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getTsname() != null && bean.getTsname().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getTcname() != null && bean.getTcname().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getCreatedby() != null && bean.getCreatedby().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getTestcondition() != null
					&& bean.getTestcondition().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getSqlscript() != null && bean.getSqlscript().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getCreateddate() != null
					&& bean.getCreateddate().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			}
		}
		return expens;
	}

	private void removePrevioustestcasesfromtable() {
		if (tctable != null) {
			for (int i = tctable.getItems().size() - 1; i >= 0; i--) {
				tctable.getItems().remove(i);
			}
		}
	}

	@FXML
	private void update() {
		if (validatefields()) {
			boolean one = CommonFunctions.updatetables("testcases", "moduleid",
					modulecombo.getSelectionModel().getSelectedItem(), "id", testcaseid, "Module Updated Successfully",
					"Module Failed to Update", 1, 7);
			boolean two = CommonFunctions.updatetables("testcases", "tsname", tsnametext.getText(), "id", testcaseid,
					"Test Scenario Updated Successfully", "Test Scenario Failed", 0, 7);
			boolean three = CommonFunctions.updatetables("testcases", "tcname", tcnametext.getText(), "id", testcaseid,
					"Test Case Name Updated Successfully", "Test Case Name Failed to Update", 0, 7);
			boolean four = CommonFunctions.updatetables("testcases", "testcondition", tctextarea.getText(), "id",
					testcaseid, "Test Condition Updated Successfully", "Test Condition Failed to Update", 0, 7);
			boolean five = CommonFunctions.updatetables("testcases", "sqlscript", sqlscripttextarea.getText(), "id",
					testcaseid, "Test Script Updated Successfully", "Test Script Failed to Update", 0, 7);
			CommonFunctions.updatetables("testcases", "updateduserid", Integer.toString(Loggedinuserdetails.id), "id",
					testcaseid, "Test Case Updated Successfully", "Failed to Update Test Case", 0, 7);
			CommonFunctions.updatetables("testcases", "updateddate", "CURRENT_TIMESTAMP", "id", testcaseid,
					"Test Case Updated Successfully", "Failed to Update Test Case", 0, 7);
			if (one == true && two == true && three == true && four == true && five == true) {
				setdefault();
				populatetestcases();
			}

		}
	}

	public class ModifyButtonCell extends TableCell<FieldtoFieldBinaryTrade, Boolean> {
		final Button cellButton = new Button("Modify");

		ModifyButtonCell() {
			cellButton.setStyle(
					"-fx-background-color: linear-gradient(#277CD2, #0C23EA); -fx-background-radius: 25; -fx-background-insets: 0; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-clor: white;");
			cellButton.setPrefWidth(85);
			cellButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent t) {
					FieldtoFieldBinaryTrade person = tctable.getItems().get(getIndex());
					modulecombo.getSelectionModel().select(person.getModulename());
					tsnametext.setText(person.getTsname());
					tcnametext.setText(person.getTcname());
					tctextarea.setText(person.getTestcondition());
					sqlscripttextarea.setText(person.getSqlscript());
					testcaseid = person.getId();
					save.setVisible(false);
					update.setVisible(true);

				}
			});
		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(cellButton);
			}
		}
	}

	public class DeleteButtonCell extends TableCell<FieldtoFieldBinaryTrade, Boolean> {
		final Button cellButton = new Button("Delete");

		DeleteButtonCell() {
			cellButton.setStyle(
					"-fx-background-color: linear-gradient(#FA3F3F, #F8340D); -fx-background-radius: 25; -fx-background-insets: 0; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-clor: white;");
			cellButton.setPrefWidth(85);
			cellButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent t) {
					FieldtoFieldBinaryTrade person = tctable.getItems().get(getIndex());
					CommonFunctions.message = "Please confirm to Delete Test Case :" + person.getTcname() + ".\n\n"
							+ "Note: Please contact your Manager, to get back the deleted testcases...";
					CommonFunctions.invokeConfirmDialog(getClass());

					if (CommonFunctions.selectionstatus == "yes") {
						String result = new DAO().updatetabledata("testcases", "status", "0", "id", person.getId());
						new DAO().updatetabledata("testcases", "updateduserid",
								Integer.toString(Loggedinuserdetails.id), "id", person.getId());
						new DAO().updatetabledata("testcases", "updateddate", "CURRENT_TIMESTAMP", "id",
								person.getId());
						if (result.equals("success")) {
							populatetestcases();
							runmessage("Deleted Successfully...");
						} else {
							runmessage("Failed to Perform Delete Operation. Please Try Again...");
						}
					}
				}
			});
		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(cellButton);
			}
		}
	}
}
