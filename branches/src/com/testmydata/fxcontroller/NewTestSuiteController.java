package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.ModulesBinaryTrade;
import com.testmydata.binarybeans.TestScenariosBinaryTrade;
import com.testmydata.binarybeans.TestSuiteBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.StaticImages;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class NewTestSuiteController implements Initializable {

	private static NewTestSuiteController userHome = null;
	Stage myStage;
	@FXML
	private ImageView closeicon, processicon, saveicon, updateicon, refreshicon, addicon, clearicon;
	@FXML
	private JFXComboBox<String> modulecombo, tscombo, tccombo;
	@FXML
	private Label refreshlbl;
	@FXML
	private JFXTextField relasetext, cycletext, tsnametext, searchtext;
	@FXML
	private TableView<TestSuiteBinaryTrade> tstable, testsuites;
	@FXML
	private TableColumn<TestSuiteBinaryTrade, String> items, id, testsuitename, testitems, testtype;
	@FXML
	private TableColumn<TestSuiteBinaryTrade, Boolean> checkbox = new TableColumn<TestSuiteBinaryTrade, Boolean>(
			"Select");
	@FXML
	private TableColumn<TestSuiteBinaryTrade, Boolean> modifybutton = new TableColumn<TestSuiteBinaryTrade, Boolean>(
			"Modify");
	@FXML
	private TableColumn<TestSuiteBinaryTrade, Boolean> deletebutton = new TableColumn<TestSuiteBinaryTrade, Boolean>(
			"Delete");
	@FXML
	private AnchorPane actionanchor1, actionanchor11, suiteanchor;
	@FXML
	private JFXTabPane suitespane;

	static ArrayList<TestSuiteBinaryTrade> testsuitelist = new ArrayList<TestSuiteBinaryTrade>();
	static ArrayList<TestSuiteBinaryTrade> testsuitelistbyid = new ArrayList<TestSuiteBinaryTrade>();
	static ArrayList<ModulesBinaryTrade> moduleslist = new ArrayList<ModulesBinaryTrade>();
	static ArrayList<TestScenariosBinaryTrade> tcnamelist = new ArrayList<TestScenariosBinaryTrade>();
	static ArrayList<TestScenariosBinaryTrade> tsnamelist = new ArrayList<TestScenariosBinaryTrade>();
	ArrayList<TestSuiteBinaryTrade> selectedlist = new ArrayList<TestSuiteBinaryTrade>();
	ArrayList<TestSuiteBinaryTrade> selectedlist1 = new ArrayList<TestSuiteBinaryTrade>();
	static boolean wipemodulestext = true, wipetstext = true, wipetctext = true;
	static String selectedtype = "modules", slectedtestsuiteid = null, tempselectiontype = null;

	static int tslist = 0, tclist = 0;
	StringBuffer sb = new StringBuffer();

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setinitialdetails();
		closeicon.setImage(StaticImages.closeicon);
		processicon.setImage(StaticImages.source_run);
		saveicon.setImage(StaticImages.save);
		updateicon.setImage(StaticImages.save);
		refreshicon.setImage(StaticImages.refresh);
		addicon.setImage(StaticImages.add);
		clearicon.setImage(StaticImages.clear);

		tstable.getColumns().addAll(checkbox);
		checkbox.setCellValueFactory(new PropertyValueFactory<>("checkboxs"));
		checkbox.setCellFactory(CheckBoxTableCell.forTableColumn(checkbox));
		checkbox.setEditable(true);
		tstable.setEditable(true);

		items.setCellValueFactory(new PropertyValueFactory<>("selecteditems"));

		items.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");

		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		testsuitename.setCellValueFactory(new PropertyValueFactory<>("testsuitename"));
		testtype.setCellValueFactory(new PropertyValueFactory<>("selectiontype"));
		testitems.setCellValueFactory(new PropertyValueFactory<>("selecteditems"));

		id.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
		testtype.setStyle("-fx-font-weight:bold;");
		testsuitename.setStyle("-fx-text-fill: #162a4c; -fx-font-weight:bold;");

		modifybutton.setGraphic(StaticImages.getmodifybutton());
		modifybutton.setText("");
		modifybutton.setSortable(false);
		modifybutton.setCellValueFactory(new PropertyValueFactory<>("buttons"));
		modifybutton.setPrefWidth(30);
		modifybutton.setResizable(false);
		modifybutton.setCellFactory(
				new Callback<TableColumn<TestSuiteBinaryTrade, Boolean>, TableCell<TestSuiteBinaryTrade, Boolean>>() {
					@Override
					public TableCell<TestSuiteBinaryTrade, Boolean> call(TableColumn<TestSuiteBinaryTrade, Boolean> p) {
						return new ModifyButtonCell();
					}
				});
		testsuites.getColumns().add(modifybutton);

		deletebutton.setGraphic(StaticImages.getdeletebutton());
		deletebutton.setText("");
		deletebutton.setSortable(false);
		deletebutton.setCellValueFactory(new PropertyValueFactory<>("buttons1"));
		deletebutton.setPrefWidth(30);
		deletebutton.setResizable(false);
		deletebutton.setCellFactory(
				new Callback<TableColumn<TestSuiteBinaryTrade, Boolean>, TableCell<TestSuiteBinaryTrade, Boolean>>() {
					@Override
					public TableCell<TestSuiteBinaryTrade, Boolean> call(TableColumn<TestSuiteBinaryTrade, Boolean> p) {
						return new DeleteButtonCell();
					}
				});
		testsuites.getColumns().add(deletebutton);

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
		saveicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				save();
			}
		});

		Label updatelbl = new Label("  Update ");
		updatelbl.setStyle(StaticImages.lblStyle);
		updatelbl.setMinWidth(50);
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
				update();
			}
		});

		Label addlbl = new Label(" Add Selection Criteria to Test Suite ");
		addlbl.setStyle(StaticImages.lblStyle);
		addlbl.setMinWidth(120);
		addlbl.setLayoutX(755);
		addlbl.setLayoutY(100);
		addlbl.setVisible(false);
		suiteanchor.getChildren().add(addlbl);

		addicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				addlbl.setVisible(true);
			}
		});
		addicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				addlbl.setVisible(false);
			}
		});
		addicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				addtestsuite();
			}
		});

		Label clearlbl = new Label(" Clear Selection Criteria from Test Suite ");
		clearlbl.setStyle(StaticImages.lblStyle);
		clearlbl.setMinWidth(120);
		clearlbl.setLayoutX(780);
		clearlbl.setLayoutY(100);
		clearlbl.setVisible(false);
		suiteanchor.getChildren().add(clearlbl);

		clearicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				clearlbl.setVisible(true);
			}
		});
		clearicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				clearlbl.setVisible(false);
			}
		});
		clearicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				removeselectionfromtable();
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
				searchtext.clear();
				populatetestsuites();
			}
		});

		relasetext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (relasetext.getText().length() >= 200) {
						relasetext.setText(relasetext.getText().substring(0, 200));
					}
				}
			}
		});

		cycletext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (cycletext.getText().length() >= 200) {
						cycletext.setText(cycletext.getText().substring(0, 200));
					}
				}
			}
		});

		tsnametext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (tsnametext.getText().length() >= 300) {
						tsnametext.setText(tsnametext.getText().substring(0, 300));
					}
				}
			}
		});

		searchtext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String enteredString = searchtext.getText().toString();
				if (enteredString != null) {
					if (enteredString.length() >= 1) {
						ArrayList<TestSuiteBinaryTrade> filteredData = filterByTestSuites(testsuitelistbyid,
								enteredString);
						populateTestsuitesTable(filteredData);
					} else if (!(enteredString.length() >= 1)) {
						populateTestsuitesTable(testsuitelistbyid);
					}
				}
			}
		});

		// testsuitecombo.getSelectionModel().selectedItemProperty().addListener(new
		// ChangeListener<String>() {
		// @Override
		// public void changed(ObservableValue<? extends String> selected,
		// String oldFruit, String newFruit) {
		// try {
		// if (!newFruit.equals("Select Test Suite")) {
		// setdefaultcombo();
		// showslectedsuitedetails();
		// save.setVisible(false);
		// update.setVisible(true);
		// } else {
		// setdefaultcombo();
		// tsnametext.clear();
		// relasetext.clear();
		// cycletext.clear();
		//
		// save.setVisible(true);
		// update.setVisible(false);
		// }
		// } catch (NullPointerException ne) {
		// }
		// }
		// });

		modulecombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select QA Modules")) {
						tscombo.getSelectionModel().select(0);
						tccombo.getSelectionModel().select(0);
						tscombo.setVisible(true);
						loadtestscenarios(newFruit);

						if (wipemodulestext && !selectedtype.equals("modules")) {
							removeselectionfromtable();
						}
						selectedtype = "modules";
					} else {
						tscombo.getSelectionModel().select(0);
						tccombo.getSelectionModel().select(0);
						tscombo.setVisible(false);
						tccombo.setVisible(false);
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		tscombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select Test Scenario")) {
						tccombo.getSelectionModel().select(0);
						tccombo.setVisible(true);
						loadtestcases(newFruit);

						if (wipetstext && !selectedtype.equals("testscenario")) {
							removeselectionfromtable();
						}
						selectedtype = "testscenario";
					} else {
						selectedtype = "modules";
						tccombo.getSelectionModel().select(0);
						tccombo.setVisible(false);
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		tccombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select Test Case")) {

						if (wipetctext && !selectedtype.equals("testcase")) {
							removeselectionfromtable();
						}
						selectedtype = "testcase";
					} else {
						selectedtype = "testscenario";
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		// closing screen when clicks on home icon
		closeicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				AnchorPane pane = (AnchorPane) ((ImageView) t.getSource()).getParent().getParent().getParent();
				pane.getChildren().remove(pane.getChildren().size() - 1);

				NewTestSuiteController nc = new NewTestSuiteController();
				Cleanup.nullifyStrings(nc);
			}
		});
	}

	// Method used to get data from previous class
	public static NewTestSuiteController getInstance() {

		return userHome;
	}

	// private void addexistingsuites() {
	// testsuitecombo.getItems().clear();
	// testsuitecombo.getItems().add("Select Test Suite");
	// testsuitecombo.getSelectionModel().select(0);
	// testsuitelist = new DAO().gettestsuites("testsuites", null,
	// Loggedinuserdetails.defaultproject);
	// if (testsuitelist != null && testsuitelist.size() > 0) {
	// for (int i = 0; i < testsuitelist.size(); i++) {
	// testsuitecombo.getItems()
	// .add(testsuitelist.get(i).getId() + "-" +
	// testsuitelist.get(i).getTestsuitename());
	// }
	// }
	// testsuitecombo.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
	// }

	private void setinitialdetails() {
		populatetestsuites();

		modulecombo.getItems().clear();
		modulecombo.getItems().add("Select QA Modules");
		modulecombo.getSelectionModel().select(0);
		moduleslist = new DAO().getModuleDetails("modules", "all", Loggedinuserdetails.defaultproject);
		if (moduleslist != null && moduleslist.size() > 0) {
			for (int i = 0; i < moduleslist.size(); i++) {
				modulecombo.getItems().add(moduleslist.get(i).getModulename());
			}
		}
		modulecombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");

	}

	private void loadtestscenarios(String selectedmodule) {
		tscombo.getItems().clear();
		tscombo.getItems().add("Select Test Scenario");
		tscombo.getSelectionModel().select(0);
		tsnamelist = new DAO().getTSNameDetails("testcases", selectedmodule, Loggedinuserdetails.defaultproject);
		loadtestscenariosservice.reset();
		loadtestscenariosservice.start();

		tscombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
	}

	// Runs in Background and updates UI Responsively
	Service<Void> loadtestscenariosservice = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					// Background work
					if (tsnamelist != null && tsnamelist.size() > 0) {
						for (tslist = 0; tslist < tsnamelist.size(); tslist++) {
							final CountDownLatch latch = new CountDownLatch(1);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									try {
										// FX Stuff done here
										tscombo.getItems().add(tsnamelist.get(tslist).getTsname());
									} finally {
										latch.countDown();
									}
								}
							});
							latch.await();
						}
					}
					// Keep with the background work
					return null;
				}
			};
		}
	};

	private void loadtestcases(String selectedtestscenario) {
		tccombo.getItems().clear();
		tccombo.getItems().add("Select Test Case");
		tccombo.getSelectionModel().select(0);
		tcnamelist = new DAO().getTCNameDetails("testcases", selectedtestscenario);
		testcaselistservice.reset();
		testcaselistservice.start();
		tccombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
	}

	Service<Void> testcaselistservice = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					// Background work
					if (tcnamelist != null && tcnamelist.size() > 0) {
						for (tclist = 0; tclist < tcnamelist.size(); tclist++) {
							final CountDownLatch latch = new CountDownLatch(1);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									try {
										// FX Stuff done here
										tccombo.getItems().add(tcnamelist.get(tclist).getTsname());
									} finally {
										latch.countDown();
									}
								}
							});
							latch.await();
						}
					}
					// Keep with the background work
					return null;
				}
			};
		}
	};

	// Method to display the Messages
	public void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	private void addtestsuite() {
		if (modulecombo.getSelectionModel().getSelectedIndex() > 0 || tscombo.getSelectionModel().getSelectedIndex() > 0
				|| tccombo.getSelectionModel().getSelectedIndex() > 0) {
			addtestcase.reset();
			addtestcase.start();
		} else {
			runmessage("Please Select Test Suite Requirements...");
		}
	}

	// Runs in Background and updates UI Responsively
	Service<Void> addtestcase = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					// Background work

					if (tempselectiontype != null && tempselectiontype != selectedtype) {
						removeselectionfromtable();
					}
					tempselectiontype = selectedtype;

					if (selectedtype.equals("modules")) {
						sb = new StringBuffer(modulecombo.getSelectionModel().getSelectedItem().toString());
						wipemodulestext = false;
						wipetstext = true;
						wipetctext = true;
					} else if (selectedtype.equals("testscenario")) {
						sb = new StringBuffer(tscombo.getSelectionModel().getSelectedItem().toString());
						wipemodulestext = true;
						wipetstext = false;
						wipetctext = true;
					} else if (selectedtype.equals("testcase")) {
						sb = new StringBuffer(tccombo.getSelectionModel().getSelectedItem().toString());
						wipemodulestext = true;
						wipetstext = true;
						wipetctext = false;
					}

					final CountDownLatch latch = new CountDownLatch(1);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								// FX Stuff done here
								boolean duplicate = false;
								if (selectedlist != null) {
									for (int i = 0; i < selectedlist.size(); i++) {
										if (selectedlist.get(i).getSelecteditems().contains(sb.toString())) {
											duplicate = true;
											break;
										} else {
											duplicate = false;
										}
									}
								}
								if (!duplicate) {
									TestSuiteBinaryTrade tsbt = new TestSuiteBinaryTrade();
									tsbt.setSelecteditems(sb.toString());
									selectedlist.add(tsbt);

									populateTable(selectedlist);
								} else {
									runmessage("Duplicate Selection..!");
								}
							} finally {
								latch.countDown();
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

	private void populateTable(ArrayList<TestSuiteBinaryTrade> arrayListData) {
		if (arrayListData != null && arrayListData.size() > 0) {
			ObservableList<TestSuiteBinaryTrade> data = FXCollections.observableArrayList();

			for (int i = 0; i < arrayListData.size(); i++) {
				TestSuiteBinaryTrade tsbt = arrayListData.get(i);
				tsbt.setSelecteditems(arrayListData.get(i).getSelecteditems());

				data.add(tsbt);
				tstable.setItems(data);
				tstable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			}
			tstable.refresh();

		} else {
			removeselectionfromtable();
		}
	}

	private void removeselectionfromtable() {
		if (tstable != null) {
			for (int i = tstable.getItems().size() - 1; i >= 0; i--) {
				tstable.getItems().remove(i);
			}
		}
		if (selectedlist != null) {
			selectedlist.clear();
		}

	}

	private void setdefaultcombo() {
		modulecombo.getSelectionModel().select(0);
		tscombo.getSelectionModel().select(0);
		tccombo.getSelectionModel().select(0);
	}

	private void clearselection() {
		wipemodulestext = true;
		wipetstext = true;
		wipetctext = true;
		selectedtype = "modules";
		selectedlist.clear();
		relasetext.clear();
		cycletext.clear();
		tsnametext.clear();
		setdefaultcombo();
		tscombo.setVisible(false);
		tccombo.setVisible(false);
		removeselectionfromtable();
		updateicon.setVisible(false);
		saveicon.setVisible(true);
		slectedtestsuiteid = null;
	}

	private boolean validatefields() {
		boolean result = true;
		if (relasetext.getText() == null || relasetext.getText().isEmpty()) {
			runmessage("Please Specify Release...");
			result = false;
		} else if (cycletext.getText() == null || cycletext.getText().isEmpty()) {
			runmessage("Please Specify Cycle...");
			result = false;
		} else if (tsnametext.getText() == null || tsnametext.getText().isEmpty()) {
			runmessage("Please Specify Test Suite...");
			result = false;
		} else if (tstable.getItems().size() == 0) {
			runmessage("Please Select Required Module or Scenarios or Test Cases...");
			result = false;
		}
		return result;
	}

	private void save() {
		if (validatefields()) {
			selectedlist.clear();

			for (TestSuiteBinaryTrade p : tstable.getItems()) {
				if (p.isCheckboxs() == true) {
					TestSuiteBinaryTrade tsb = new TestSuiteBinaryTrade();
					tsb.setSelecteditems(p.getSelecteditems());
					selectedlist.add(tsb);
				}
			}

			String result = new DAO().createtestsuite("testsuites",
					Long.parseLong(Integer.toString(Loggedinuserdetails.id)), tsnametext.getText(), selectedtype,
					relasetext.getText(), cycletext.getText(), selectedlist, Loggedinuserdetails.defaultproject);
			if (result.equals("success")) {
				runmessage("Test Suite Created Successfully...");
				clearselection();
				populatetestsuites();
			} else if (result.equals("duplicate")) {
				runmessage("Given Suite Name Already Exists. Please Try New Suite Name..");
			} else {
				runmessage("Failed to Create Test Suite. Please Try Again...");
			}

		}
	}

	private void update() {
		if (validatefields()) {
			if (slectedtestsuiteid != null) {
				processicon.setVisible(true);
				updateservice.reset();
				updateservice.start();
			}
		}
	}

	// Runs in Background and updates UI Responsively
	Service<Void> updateservice = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					// Background work

					String result1 = new DAO().updatetabledata("testsuites", "suitename", tsnametext.getText(), "id",
							slectedtestsuiteid);
					String result2 = new DAO().updatetabledata("testsuites", "type", selectedtype, "id",
							slectedtestsuiteid);
					String result3 = new DAO().updatetabledata("testsuites", "updatedby",
							Integer.toString(Loggedinuserdetails.id), "id", slectedtestsuiteid);
					String result4 = new DAO().updatetabledata("testsuites", "updateddate", "", "id",
							slectedtestsuiteid);
					String result5 = new DAO().updatetabledata("testsuites", "release", relasetext.getText(), "id",
							slectedtestsuiteid);
					String result6 = new DAO().updatetabledata("testsuites", "cycle", cycletext.getText(), "id",
							slectedtestsuiteid);
					String result7 = new DAO().updatetabledata("testsuites", "projectId",
							Integer.toString(Loggedinuserdetails.defaultproject), "id", slectedtestsuiteid);
					final CountDownLatch latch = new CountDownLatch(1);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								// FX Stuff done here
								if (result1.equals("success") && result2.equals("success") && result3.equals("success")
										&& result4.equals("success") && result5.equals("success")
										&& result6.equals("success") && result7.equals("success")) {
									String result = new DAO().delete("testsuitedetails", "suiteid", slectedtestsuiteid);
									if (result.equals("success")) {
										selectedlist.clear();

										for (TestSuiteBinaryTrade p : tstable.getItems()) {
											if (p.isCheckboxs() == true) {
												TestSuiteBinaryTrade tsb = new TestSuiteBinaryTrade();
												tsb.setSelecteditems(p.getSelecteditems());
												selectedlist.add(tsb);
											}
										}

										String result11 = new DAO().inserttestsuitedetails("testsuitedetails",
												Integer.parseInt(slectedtestsuiteid), selectedlist);
										if (result11.equals("success")) {
											runmessage("Test Suite Updated Successfully...");
											updateicon.setVisible(false);
											saveicon.setVisible(true);
											clearselection();
											populatetestsuites();
										} else {
											runmessage("Failed to Update. Please Try Again...");
										}
									}
								} else {
									runmessage("Failed to Update. Please Try Again...");
								}
							} finally {
								latch.countDown();
							}
						}
					});
					latch.await();
					processicon.setVisible(false);
					// Keep with the background work
					return null;
				}
			};
		}
	};

	private void populatetestsuites() {
		testsuitelistbyid = new DAO().gettestsuites("testsuites", null, Loggedinuserdetails.defaultproject);

		if (testsuitelistbyid != null && testsuitelistbyid.size() > 0) {
			populateTestsuitesTable(testsuitelistbyid);
		}
	}

	private void populateTestsuitesTable(ArrayList<TestSuiteBinaryTrade> arrayListData) {
		if (arrayListData != null && arrayListData.size() > 0) {
			ObservableList<TestSuiteBinaryTrade> data = FXCollections.observableArrayList();

			for (int i = 0; i < arrayListData.size(); i++) {
				TestSuiteBinaryTrade tsbt = arrayListData.get(i);
				tsbt.setId(arrayListData.get(i).getId());
				tsbt.setTestsuitename(arrayListData.get(i).getTestsuitename());
				tsbt.setSelectiontype(arrayListData.get(i).getSelectiontype());
				tsbt.setSelecteditems(arrayListData.get(i).getSelecteditems());

				tsbt.setRelease(arrayListData.get(i).getRelease());
				tsbt.setCycle(arrayListData.get(i).getCycle());

				data.add(tsbt);
				testsuites.setItems(data);
				testsuites.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			}
			testsuites.refresh();

		} else {
			removetestsuitefromtable();
		}
	}

	private void removetestsuitefromtable() {
		if (testsuites != null) {
			for (int i = testsuites.getItems().size() - 1; i >= 0; i--) {
				testsuites.getItems().remove(i);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private ArrayList filterByTestSuites(ArrayList<TestSuiteBinaryTrade> unFiltered, String str) {

		ArrayList<TestSuiteBinaryTrade> expens = new ArrayList<TestSuiteBinaryTrade>();
		for (TestSuiteBinaryTrade bean : unFiltered) {
			if (bean.getId() != null && bean.getId().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getTestsuitename() != null
					&& bean.getTestsuitename().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getSelectiontype() != null
					&& bean.getSelectiontype().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getSelecteditems() != null
					&& bean.getSelecteditems().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			}
		}
		return expens;
	}

	public class ModifyButtonCell extends TableCell<TestSuiteBinaryTrade, Boolean> {

		Button cellButton = new Button();

		ModifyButtonCell() {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					try {
						removeselectionfromtable();
						if (selectedlist1 != null) {
							selectedlist1.clear();
						}

						suitespane.getSelectionModel().select(0);

						TestSuiteBinaryTrade person = testsuites.getItems().get(getIndex());

						relasetext.setText(person.getRelease());
						cycletext.setText(person.getCycle());
						tsnametext.setText(person.getTestsuitename());

						selectedtype = person.getSelectiontype();
						String tempselected = null;

						slectedtestsuiteid = person.getId();
						saveicon.setVisible(false);
						updateicon.setVisible(true);

						if (person.getSelecteditems().contains(",")) {
							String[] itemslist = person.getSelecteditems().split(",");

							for (int i = 0; i < itemslist.length; i++) {
								TestSuiteBinaryTrade tsb1 = new TestSuiteBinaryTrade();
								tsb1.setSelecteditems(itemslist[i]);

								tempselected = itemslist[i];
								selectedlist1.add(tsb1);
							}
						} else {
							TestSuiteBinaryTrade tsb2 = new TestSuiteBinaryTrade();
							tsb2.setSelecteditems(person.getSelecteditems());

							tempselected = person.getSelecteditems();
							selectedlist1.add(tsb2);
						}

						if (selectedtype.equals("modules")) {
							modulecombo.getSelectionModel().select(tempselected);
							modulecombo.setVisible(true);
							tscombo.setVisible(false);
							tccombo.setVisible(false);
						} else if (selectedtype.equals("testscenario")) {
							modulecombo.getSelectionModel()
									.select(new DAO().getModuleorTS(
											"(select module from modules where id = ts.moduleid)as module", "tsname",
											tempselected));
							tscombo.getSelectionModel().select(tempselected);
							modulecombo.setVisible(true);
							tscombo.setVisible(true);
							tccombo.setVisible(false);
						} else if (selectedtype.equals("testcase")) {
							modulecombo.getSelectionModel()
									.select(new DAO().getModuleorTS(
											"(select module from modules where id = ts.moduleid)as module", "tcname",
											tempselected));
							tscombo.getSelectionModel()
									.select(new DAO().getModuleorTS("tsname", "tcname", tempselected));
							tccombo.getSelectionModel().select(tempselected);
							modulecombo.setVisible(true);
							tscombo.setVisible(true);
							tccombo.setVisible(true);
						}
						selectedlist = selectedlist1;

						populateTable(selectedlist);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});

		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(StaticImages.getmodifybuttonintable(cellButton));
			}
		}

	}

	public class DeleteButtonCell extends TableCell<TestSuiteBinaryTrade, Boolean> {
		Button cellButton = new Button();

		DeleteButtonCell() {
			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					TestSuiteBinaryTrade person = testsuites.getItems().get(getIndex());

					CommonFunctions.message = "Are you Sure to Delete '" + person.getTestsuitename().toUpperCase()
							+ "' Permanently ?";
					CommonFunctions.invokeConfirmDialog(getClass());

					if (CommonFunctions.selectionstatus.equals("yes")) {
						String result1 = new DAO().delete("testsuites", "id", person.getId());
						String result2 = new DAO().delete("testsuitedetails", "suiteid", person.getId());

						if (result1.equals("success") && result2.equals("success")) {
							populatetestsuites();
							runmessage(person.getTestsuitename().toUpperCase() + " Deleted Successfully...");
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
				setGraphic(StaticImages.getdeletebuttonintable(cellButton));
			}
		}
	}
}
