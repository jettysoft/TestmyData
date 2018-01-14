package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.ModulesBinaryTrade;
import com.testmydata.binarybeans.TestScenariosBinaryTrade;
import com.testmydata.binarybeans.TestSuiteBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxutil.UndecoratorController;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class NewTestSuiteController implements Initializable {

	private static NewTestSuiteController userHome = null;
	Stage myStage;
	@FXML
	private ImageView homeicon, processicon;
	@FXML
	private JFXComboBox<String> testsuitecombo, modulecombo, tscombo, tccombo;
	@FXML
	private JFXButton addtestsuite, clearselection, save, update;
	@FXML
	private JFXTextField relasetext, cycletext, tsnametext;
	@FXML
	private TableView<TestSuiteBinaryTrade> tstable;
	@FXML
	private TableColumn<TestSuiteBinaryTrade, String> items;
	@FXML
	private TableColumn<TestSuiteBinaryTrade, Boolean> checkbox = new TableColumn<TestSuiteBinaryTrade, Boolean>(
			"Select");

	ArrayList<TestSuiteBinaryTrade> testsuitelist = new ArrayList<TestSuiteBinaryTrade>();
	ArrayList<TestSuiteBinaryTrade> testsuitelistbyid = new ArrayList<TestSuiteBinaryTrade>();
	ArrayList<ModulesBinaryTrade> moduleslist = new ArrayList<ModulesBinaryTrade>();
	ArrayList<TestScenariosBinaryTrade> tcnamelist = new ArrayList<TestScenariosBinaryTrade>();
	ArrayList<TestScenariosBinaryTrade> tsnamelist = new ArrayList<TestScenariosBinaryTrade>();
	ArrayList<TestSuiteBinaryTrade> selectedlist = new ArrayList<>();
	boolean wipemodulestext = true, wipetstext = true, wipetctext = true;
	static String selectedtype = "modules", slectedtestsuiteid = null;

	static int tslist = 0, tclist = 0;
	StringBuffer sb = new StringBuffer();

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setinitialdetails();
		homeicon.setImage(StaticImages.homeicon.getImage());
		processicon.setImage(StaticImages.source_run.getImage());

		tstable.getColumns().addAll(checkbox);
		checkbox.setCellValueFactory(new PropertyValueFactory<>("checkboxs"));
		checkbox.setCellFactory(CheckBoxTableCell.forTableColumn(checkbox));
		checkbox.setEditable(true);
		tstable.setEditable(true);

		items.setCellValueFactory(new PropertyValueFactory<>("selecteditems"));

		items.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");

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

		testsuitecombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select Test Suite")) {
						setdefaultcombo();
						showslectedsuitedetails();
						save.setVisible(false);
						update.setVisible(true);
					} else {
						setdefaultcombo();
						tsnametext.clear();
						relasetext.clear();
						cycletext.clear();

						save.setVisible(true);
						update.setVisible(false);
					}
				} catch (NullPointerException ne) {
				}
			}
		});
		modulecombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select QA Modules")) {
						tscombo.getSelectionModel().select(0);
						tccombo.getSelectionModel().select(0);
						tscombo.setVisible(true);
						loadtestscenarios(newFruit);
						selectedtype = "modules";
						if (wipemodulestext) {
							removetestsuitefromtable();
						}
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
						selectedtype = "testscenario";
						if (wipetstext) {
							removetestsuitefromtable();
						}
					} else {
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
						selectedtype = "testcase";
						if (wipetctext) {
							removetestsuitefromtable();
						}
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		// closing screen when clicks on home icon
		homeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(MouseEvent event) {
				Cleanup scu = new Cleanup();
				NewTestSuiteController nc = new NewTestSuiteController();
				scu.nullifyStrings(nc);

				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();
				UndecoratorController.getInstance(null);
			}
		});
	}

	// Method used to get data from previous class
	public static NewTestSuiteController getInstance() {

		return userHome;
	}

	private void addexistingsuites() {
		testsuitecombo.getItems().clear();
		testsuitecombo.getItems().add("Select Test Suite");
		testsuitecombo.getSelectionModel().select(0);
		testsuitelist = new DAO().gettestsuites("testsuites", null);
		if (testsuitelist != null && testsuitelist.size() > 0) {
			for (int i = 0; i < testsuitelist.size(); i++) {
				testsuitecombo.getItems()
						.add(testsuitelist.get(i).getId() + "-" + testsuitelist.get(i).getTestsuitename());
			}
		}
		testsuitecombo.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
	}

	private void setinitialdetails() {
		addexistingsuites();

		modulecombo.getItems().clear();
		modulecombo.getItems().add("Select QA Modules");
		modulecombo.getSelectionModel().select(0);
		moduleslist = new DAO().getModuleDetails("modules", "all");
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
		tsnamelist = new DAO().getTSNameDetails("testcases", selectedmodule);
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

	@FXML
	private void addtestsuite() {
		if (modulecombo.getSelectionModel().getSelectedIndex() != 0
				|| tscombo.getSelectionModel().getSelectedIndex() != 0
				|| tccombo.getSelectionModel().getSelectedIndex() != 0) {
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

					if (selectedtype == "modules") {
						sb = new StringBuffer(modulecombo.getSelectionModel().getSelectedItem().toString());
						wipemodulestext = false;
						wipetstext = true;
						wipetctext = true;
					} else if (selectedtype == "testscenario") {
						sb = new StringBuffer(tscombo.getSelectionModel().getSelectedItem().toString());
						wipemodulestext = true;
						wipetstext = false;
						wipetctext = true;
					} else if (selectedtype == "testcase") {
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
								if (!selectedlist.contains(sb.toString())) {
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
			removetestsuitefromtable();
		}
	}

	private void removetestsuitefromtable() {
		if (tstable != null) {
			for (int i = tstable.getItems().size() - 1; i >= 0; i--) {
				tstable.getItems().remove(i);
			}
		}
		selectedlist.clear();
	}

	private void setdefaultcombo() {
		modulecombo.getSelectionModel().select(0);
		tscombo.getSelectionModel().select(0);
		tccombo.getSelectionModel().select(0);
	}

	@FXML
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
		removetestsuitefromtable();
		update.setVisible(false);
		save.setVisible(true);
		testsuitecombo.getSelectionModel().select(0);
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

	@FXML
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
					relasetext.getText(), cycletext.getText(), selectedlist);
			if (result.equals("success")) {
				runmessage("Test Suite Created Successfully...");
				clearselection();
				addexistingsuites();
			} else if (result.equals("duplicate")) {
				runmessage("Given Suite Name Already Exists. Please Try New Suite Name..");
			} else {
				runmessage("Failed to Create Test Suite. Please Try Again...");
			}

		}
	}

	@FXML
	private void update() {
		if (validatefields()) {
			processicon.setVisible(true);
			updateservice.reset();
			updateservice.start();
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
					String[] selection = testsuitecombo.getSelectionModel().getSelectedItem().split("-");
					String result1 = new DAO().updatetabledata("testsuites", "suitename", tsnametext.getText(), "id",
							selection[0]);
					String result2 = new DAO().updatetabledata("testsuites", "type", selectedtype, "id", selection[0]);
					String result3 = new DAO().updatetabledata("testsuites", "updatedby",
							Integer.toString(Loggedinuserdetails.id), "id", selection[0]);
					String result4 = new DAO().updatetabledata("testsuites", "updateddate", "", "id", selection[0]);
					String result5 = new DAO().updatetabledata("testsuites", "release", relasetext.getText(), "id",
							selection[0]);
					String result6 = new DAO().updatetabledata("testsuites", "cycle", cycletext.getText(), "id",
							selection[0]);
					final CountDownLatch latch = new CountDownLatch(1);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								// FX Stuff done here
								if (result1.equals("success") && result2.equals("success") && result3.equals("success")
										&& result4.equals("success") && result5.equals("success")
										&& result6.equals("success")) {
									String result = new DAO().delete("testsuitedetails", "suiteid", selection[0]);
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
												Integer.parseInt(selection[0]), selectedlist);
										if (result11.equals("success")) {
											runmessage("Test Suite Updated Successfully...");
											update.setVisible(false);
											save.setVisible(true);
											clearselection();
											addexistingsuites();
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

	private void showslectedsuitedetails() {
		String[] selection = testsuitecombo.getSelectionModel().getSelectedItem().split("-");
		testsuitelistbyid = new DAO().gettestsuites("testsuites", selection[0]);
		selectedlist = new DAO().gettestsuitedetails(selection[0]);

		if (testsuitelistbyid != null && testsuitelistbyid.size() > 0) {
			relasetext.setText(testsuitelistbyid.get(0).getRelease());
			cycletext.setText(testsuitelistbyid.get(0).getCycle());
			tsnametext.setText(testsuitelistbyid.get(0).getTestsuitename());
			selectedtype = testsuitelistbyid.get(0).getSelectiontype();
		}
		if (selectedlist != null && selectedlist.size() > 0) {
			populateTable(selectedlist);
		}
	}
}
