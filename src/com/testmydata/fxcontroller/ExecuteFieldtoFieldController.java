package com.testmydata.fxcontroller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.FieldtoFieldBinaryTrade;
import com.testmydata.binarybeans.TestSuiteBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxutil.UndecoratorController;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.CustomComparator;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.QADefaultServerDetails;
import com.testmydata.util.ReportsDownloader;

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
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ExecuteFieldtoFieldController implements Initializable {

	private static ExecuteFieldtoFieldController userHome = null;
	Stage myStage;
	@FXML
	private ImageView homeicon, closeicon, searchicon, excelicon, pdficon, runicon, processicon;
	@FXML
	private JFXComboBox<String> releasecombo, cyclecombo, tscombo;
	@FXML
	private JFXTextField totaltext, passedtext, failedtext, searchtext, statustext;
	@FXML
	private JFXButton searchbuttonlabel;
	@FXML
	private JFXDatePicker startdate, enddate;
	@FXML
	private TableView<FieldtoFieldBinaryTrade> tctable;
	@FXML
	private TableColumn<FieldtoFieldBinaryTrade, String> release, cycle, testsuite, id, module, testscenario, testcase,
			sqlscript, messages, queryresult, teststatus;
	@FXML
	private AnchorPane searchanchor, selectionanchor;
	private static int passcount = 0, failcount = 0, batchid = 0, it = 0, counttestcases = 0;

	ArrayList<TestSuiteBinaryTrade> releaselist = new ArrayList<TestSuiteBinaryTrade>();
	ArrayList<TestSuiteBinaryTrade> cyclelist = new ArrayList<TestSuiteBinaryTrade>();
	ArrayList<TestSuiteBinaryTrade> tslist = new ArrayList<TestSuiteBinaryTrade>();
	ArrayList<FieldtoFieldBinaryTrade> testcaselist = new ArrayList<FieldtoFieldBinaryTrade>();
	ArrayList<String> reportcolumnlist = new ArrayList<String>();

	private static String lblStyle = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setreleasecombo();

		setdatesinitially();

		lblStyle = "-fx-background-color: linear-gradient(#277CD2, #0C23EA);  -fx-text-alignment :center; -fx-background-radius: 25; -fx-background-insets: 0; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-clor: red;";
		Label lbl = new Label("    Execute");
		lbl.setStyle(lblStyle);
		lbl.setMinWidth(70);
		lbl.setLayoutY(75);
		lbl.setLayoutX(650);
		lbl.setVisible(false);
		selectionanchor.getChildren().add(lbl);

		runicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				lbl.setVisible(true);
			}
		});
		runicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				lbl.setVisible(false);
			}
		});
		runicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				run();
			}
		});

		Label searchlbl = new Label("     Search");
		searchlbl.setStyle(lblStyle);
		searchlbl.setMinWidth(70);
		searchlbl.setLayoutY(25);
		searchlbl.setLayoutX(35);
		searchlbl.setVisible(false);
		selectionanchor.getChildren().add(searchlbl);

		searchicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				searchlbl.setVisible(true);
			}
		});
		searchicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				searchlbl.setVisible(false);
			}
		});

		Label excellbl = new Label("  Excel Report");
		excellbl.setStyle(lblStyle);
		excellbl.setMinWidth(85);
		excellbl.setLayoutY(70);
		excellbl.setLayoutX(1080);
		excellbl.setVisible(false);
		selectionanchor.getChildren().add(excellbl);

		excelicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				excellbl.setVisible(true);
			}
		});
		excelicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				excellbl.setVisible(false);
			}
		});

		Label pdflbl = new Label("  PDF Report");
		pdflbl.setStyle(lblStyle);
		pdflbl.setMinWidth(85);
		pdflbl.setLayoutY(70);
		pdflbl.setLayoutX(1105);
		pdflbl.setVisible(false);
		selectionanchor.getChildren().add(pdflbl);

		pdficon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				pdflbl.setVisible(true);
			}
		});
		pdficon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				pdflbl.setVisible(false);
			}
		});

		totaltext.setStyle("-fx-text-fill: #0c23ea; -fx-font-weight:bold;");
		passedtext.setStyle("-fx-text-fill: #40aa03; -fx-font-weight:bold;");
		failedtext.setStyle("-fx-text-fill: #f8340d; -fx-font-weight:bold;");

		release.setCellValueFactory(new PropertyValueFactory<>("release"));
		cycle.setCellValueFactory(new PropertyValueFactory<>("cycle"));
		testsuite.setCellValueFactory(new PropertyValueFactory<>("testsuitename"));
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		module.setCellValueFactory(new PropertyValueFactory<>("modulename"));
		testscenario.setCellValueFactory(new PropertyValueFactory<>("tsname"));
		testcase.setCellValueFactory(new PropertyValueFactory<>("tcname"));
		sqlscript.setCellValueFactory(new PropertyValueFactory<>("sqlscript"));
		messages.setCellValueFactory(new PropertyValueFactory<>("message"));
		queryresult.setCellValueFactory(new PropertyValueFactory<>("queryresult"));
		teststatus.setCellValueFactory(new PropertyValueFactory<>("teststatus"));

		release.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		cycle.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		testsuite.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		id.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		module.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
		testscenario.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		testcase.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		sqlscript.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");

		hidecolumn();

		try {
			tctable.getSelectionModel().setCellSelectionEnabled(true);
			tctable.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent click) {
					if (click.getClickCount() > 0) {
						@SuppressWarnings("rawtypes")
						TablePosition pos = tctable.getSelectionModel().getSelectedCells().get(0);
						int row = pos.getRow();
						int col = pos.getColumn();

						if (col == 7) {
							for (int i = 0; i < tctable.getItems().size(); i++) {
								if (i == row) {
									ShowSqlScriptController.getInstance(tctable.getItems().get(row).getSqlscript());
									CommonFunctions.invokeSQLScriptController(getClass());
								}
							}

						}
					}
				}
			});
		} catch (Exception e) {
		}

		releasecombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select Release")) {
						totaltext.setText(Integer.toString(0));
						populatetestcases(newFruit);
						setcyclecombo(newFruit);
					} else {
						setdefaultcycle();
						settestsuite();
						removePrevioustestcasesfromtable();
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		cyclecombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select Cycle")) {
						settscombo(releasecombo.getSelectionModel().getSelectedItem(), newFruit);

						ArrayList<FieldtoFieldBinaryTrade> filteredData1 = filterByDescription(testcaselist,
								releasecombo.getSelectionModel().getSelectedItem());
						ArrayList<FieldtoFieldBinaryTrade> filteredData = filterByDescription(filteredData1, newFruit);
						populateTable(filteredData);
					} else {
						settestsuite();
						if (releasecombo.getSelectionModel().getSelectedIndex() > 0) {
							ArrayList<FieldtoFieldBinaryTrade> filteredData = filterByDescription(testcaselist,
									releasecombo.getSelectionModel().getSelectedItem());
							populateTable(filteredData);
						}
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		tscombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select Test Suite")) {
						ArrayList<FieldtoFieldBinaryTrade> filteredData1 = filterByDescription(testcaselist,
								releasecombo.getSelectionModel().getSelectedItem());
						ArrayList<FieldtoFieldBinaryTrade> filteredData2 = filterByDescription(filteredData1,
								cyclecombo.getSelectionModel().getSelectedItem());
						ArrayList<FieldtoFieldBinaryTrade> filteredData = filterByDescription(filteredData2, newFruit);
						populateTable(filteredData);
					} else {
						if (releasecombo.getSelectionModel().getSelectedIndex() > 0
								&& cyclecombo.getSelectionModel().getSelectedIndex() > 0) {
							ArrayList<FieldtoFieldBinaryTrade> filteredData1 = filterByDescription(testcaselist,
									releasecombo.getSelectionModel().getSelectedItem());
							ArrayList<FieldtoFieldBinaryTrade> filteredData = filterByDescription(filteredData1,
									cyclecombo.getSelectionModel().getSelectedItem());
							populateTable(filteredData);
						} else if (releasecombo.getSelectionModel().getSelectedIndex() > 0
								&& cyclecombo.getSelectionModel().getSelectedIndex() == 0) {
							ArrayList<FieldtoFieldBinaryTrade> filteredData = filterByDescription(testcaselist,
									releasecombo.getSelectionModel().getSelectedItem());
							populateTable(filteredData);
						}
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		messages.setCellFactory(
				new Callback<TableColumn<FieldtoFieldBinaryTrade, String>, TableCell<FieldtoFieldBinaryTrade, String>>() {

					@Override
					public TableCell<FieldtoFieldBinaryTrade, String> call(
							TableColumn<FieldtoFieldBinaryTrade, String> param) {
						final TableCell<FieldtoFieldBinaryTrade, String> cell = new TableCell<FieldtoFieldBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									FieldtoFieldBinaryTrade result = getTableView().getItems().get(getIndex());
									if (item != null) {
										setText(item.toString());

										if (result.getMessage().equals("Run Successful")) {
											setStyle("-fx-text-fill: green; -fx-font-weight:bold;");

										} else {
											setStyle("-fx-text-fill: #F8340D; -fx-font-weight:bold;");
										}
									}
								}
							}
						};
						return cell;
					}
				});
		queryresult.setCellFactory(
				new Callback<TableColumn<FieldtoFieldBinaryTrade, String>, TableCell<FieldtoFieldBinaryTrade, String>>() {

					@Override
					public TableCell<FieldtoFieldBinaryTrade, String> call(
							TableColumn<FieldtoFieldBinaryTrade, String> param) {
						final TableCell<FieldtoFieldBinaryTrade, String> cell = new TableCell<FieldtoFieldBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									FieldtoFieldBinaryTrade result = getTableView().getItems().get(getIndex());
									if (item != null) {
										setText(item.toString());

										if (result.getQueryresult().matches("\\d+")) {
											if (Integer.parseInt(result.getQueryresult()) == 0) {
												setStyle("-fx-text-fill: green; -fx-font-weight:bold; ");
											} else if (Integer.parseInt(result.getQueryresult()) > 0) {
												setStyle("-fx-text-fill: #F8340D; -fx-font-weight:bold;");
											}

										}
										if (!result.getQueryresult().matches("\\d+")) {
											setStyle("-fx-text-fill: #F8340D; -fx-font-weight:bold;");
										}
									}
								}
							}
						};
						return cell;
					}
				});

		teststatus.setCellFactory(
				new Callback<TableColumn<FieldtoFieldBinaryTrade, String>, TableCell<FieldtoFieldBinaryTrade, String>>() {

					@Override
					public TableCell<FieldtoFieldBinaryTrade, String> call(
							TableColumn<FieldtoFieldBinaryTrade, String> param) {
						final TableCell<FieldtoFieldBinaryTrade, String> cell = new TableCell<FieldtoFieldBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									FieldtoFieldBinaryTrade result = getTableView().getItems().get(getIndex());
									if (item != null) {
										setText(item.toString());

										if (result.getTeststatus().equals("Pass")) {
											setStyle("-fx-text-fill: green; -fx-font-weight:bold;");

										}
										if (result.getTeststatus().equals("Fail")) {
											setStyle("-fx-text-fill: #F8340D; -fx-font-weight:bold;");
										}
									}
								}
							}
						};
						return cell;
					}
				});

		searchtext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String enteredString = searchtext.getText().toString();
				if (enteredString != null && !enteredString.isEmpty()) {
					if (enteredString.length() >= 1) {
						@SuppressWarnings("unchecked")
						ArrayList<FieldtoFieldBinaryTrade> filteredData = filterByDescription(testcaselist,
								enteredString);
						populateTable(filteredData);
					} else if (enteredString != null && enteredString.length() == 0) {
						populateTable(testcaselist);
					}
				}
			}
		});

		pdficon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				File ff = new File(new File(".", "/Reports/TestSuites/PDF").getAbsolutePath());
				if (!(ff.exists() && ff.isDirectory())) {
					ff.mkdirs();
				}
				if (reportcolumnlist == null || reportcolumnlist.size() == 0) {
					reportcolumnlist.addAll(addcolumnsforreport());
				}

				ReportsDownloader rd = new ReportsDownloader();
				rd.download("Test Suite", batchid, "Reports/TestSuites/PDF", "pdf", reportcolumnlist,
						new DAO().getfieldresults(batchid, 0, 0, replacer(reportcolumnlist), reportcolumnlist.size(),
								null, QADefaultServerDetails.id));
			}
		});

		excelicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				File ff = new File(new File(".", "/Reports/TestSuites/Excel").getAbsolutePath());
				if (!(ff.exists() && ff.isDirectory())) {
					ff.mkdirs();
				}
				if (reportcolumnlist == null || reportcolumnlist.size() == 0) {
					reportcolumnlist.addAll(addcolumnsforreport());
				}

				ReportsDownloader rd = new ReportsDownloader();
				rd.download("Test Suite", batchid, "Reports/TestSuites/Excel", "excel", reportcolumnlist,
						new DAO().getfieldresults(batchid, 0, 0, replacer(reportcolumnlist), reportcolumnlist.size(),
								null, QADefaultServerDetails.id));
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
				ExecuteFieldtoFieldController nc = new ExecuteFieldtoFieldController();
				scu.nullifyStrings(nc);

				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();
				UndecoratorController.getInstance(null);
			}
		});
	}

	// Method used to get data from previous class
	public static ExecuteFieldtoFieldController getInstance() {
		return userHome;
	}

	private void setdatesinitially() {
		startdate.setValue(CommonFunctions.getdateforpicker(3660));
		enddate.setValue(CommonFunctions.getdateforpicker(0));
	}

	// Method to display the Messages
	public void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	private void setreleasecombo() {
		releasecombo.getItems().clear();
		releasecombo.getItems().add("Select Release");
		releasecombo.getSelectionModel().select(0);

		settestsuite();
		setdefaultcycle();

		releaselist.clear();
		releaselist = new DAO().getreleases("testsuites");
		if (releaselist != null && releaselist.size() > 0) {
			for (int i = 0; i < releaselist.size(); i++) {
				releasecombo.getItems().add(releaselist.get(i).getRelease());
			}
		}
		releasecombo.setStyle("-fx-text-fill: #40aa03; -fx-font-weight:bold;");
	}

	private void setcyclecombo(String release) {
		setdefaultcycle();
		cyclelist.clear();
		cyclelist = new DAO().getcycles(release);
		if (cyclelist != null && cyclelist.size() > 0) {
			for (int i = 0; i < cyclelist.size(); i++) {
				cyclecombo.getItems().add(cyclelist.get(i).getCycle());
			}
		}
		cyclecombo.setStyle("-fx-text-fill: #7c04c6; -fx-font-weight:bold;");
	}

	private void settscombo(String release, String cycle) {
		settestsuite();
		tslist.clear();
		tslist = new DAO().gettestsuitesonly(cycle, release);
		if (tslist != null && tslist.size() > 0) {
			for (int i = 0; i < tslist.size(); i++) {
				tscombo.getItems().add(tslist.get(i).getTestsuitename());
			}
		}
		tscombo.setStyle("-fx-text-fill: #0083ff; -fx-font-weight:bold;");
	}

	private void setdefaultcycle() {
		cyclecombo.getItems().clear();
		cyclecombo.getItems().add("Select Cycle");
		cyclecombo.getSelectionModel().select(0);
	}

	private void settestsuite() {
		tscombo.getItems().clear();
		tscombo.getItems().add("Select Test Suite");
		tscombo.getSelectionModel().select(0);
	}

	// Method to display the Results
	public void showresult(String result, String status, String message) {
		CommonFunctions.queryresult = result;
		CommonFunctions.teststatus = status;
		CommonFunctions.message = message;
		CommonFunctions.invokeTestResultsDialog(getClass());
	}

	private void resetcounts(String totalcount) {
		excelicon.setVisible(false);
		pdficon.setVisible(false);
		statustext.clear();
		totaltext.setText(totalcount);
		passedtext.setText("0");
		failedtext.setText("0");
		hidecolumn();
	}

	private void hidecolumn() {
		tctable.getColumns().remove(messages);
		tctable.getColumns().remove(queryresult);
		tctable.getColumns().remove(teststatus);

		tctable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	private void showcolumn() {
		tctable.getColumns().add(messages);
		tctable.getColumns().add(queryresult);
		tctable.getColumns().add(teststatus);

		tctable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	private void populatetestcases(String release) {
		try {

			if (release != null) {
				removePrevioustestcasesfromtable();

				testcaselist = new DAO().getTestCasesDetails("testcases", startdate.getValue().toString(),
						enddate.getValue().toString(), release);
			}

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
			e.printStackTrace();
		}
	}

	private void populateTable(ArrayList<FieldtoFieldBinaryTrade> arrayListData) {
		if (arrayListData != null && arrayListData.size() > 0) {
			counttestcases = 0;
			ObservableList<FieldtoFieldBinaryTrade> data = FXCollections.observableArrayList();

			for (int i = 0; i < arrayListData.size(); i++) {
				FieldtoFieldBinaryTrade ffb = arrayListData.get(i);
				ffb.setRelease(arrayListData.get(i).getRelease());
				ffb.setCycle(arrayListData.get(i).getCycle());
				ffb.setTestsuitename(arrayListData.get(i).getTestsuitename());
				ffb.setId(arrayListData.get(i).getId());
				ffb.setModulename(arrayListData.get(i).getModulename());
				ffb.setTsname(arrayListData.get(i).getTsname());
				ffb.setTcname(arrayListData.get(i).getTcname());
				ffb.setSqlscript(arrayListData.get(i).getSqlscript());
				ffb.setMessage(arrayListData.get(i).getMessage());
				data.add(ffb);
				tctable.setItems(data);
				tctable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

				counttestcases++;
			}
			tctable.refresh();
			totaltext.setText(Integer.toString(counttestcases));
		} else {
			removePrevioustestcasesfromtable();
			// runmessage("No Test Cases Found...!");
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
			} else if (bean.getSqlscript() != null && bean.getSqlscript().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getQueryresult() != null
					&& bean.getQueryresult().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getTeststatus() != null && bean.getTeststatus().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getRelease() != null && bean.getRelease().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getCycle() != null && bean.getCycle().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getTestsuitename() != null
					&& bean.getTestsuitename().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getId() != null && bean.getId().toLowerCase().contains(str.toLowerCase())) {
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
		totaltext.setText("0");
		passedtext.setText("0");
		failedtext.setText("0");
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
			populatetestcases(null);
		}
	}

	private void run() {
		if (tctable.getItems().size() > 0) {
			resetcounts(Integer.toString(tctable.getItems().size()));

			service.reset();
			service.start();

			showcolumn();
		} else {
			resetcounts(Integer.toString(tctable.getItems().size()));
			runmessage(
					"Please Select Test Cases for Execution.\n\nNote : Please Choose Release or Cycles or Test Suites or By Date Range from Search...");
		}
	}

	// Runs in Background and updates UI Responsively
	Service<Void> service = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					// Background work
					it = 0;
					passcount = 0;
					failcount = 0;
					batchid = new DAO().getmaxbatchid() + 1;
					for (it = 0; it < tctable.getItems().size(); it++) {
						final String result = new DAO().getTestResult(tctable.getItems().get(it).getSqlscript());
						final CountDownLatch latch = new CountDownLatch(1);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								try {
									// FX Stuff done here

									statustext
											.setText("Executing Test Case Id : " + tctable.getItems().get(it).getId());
									if (result.equals("conerror")) {
										runmessage(
												"Unable to Connect to the QA Server. Please check QA Server settings...");
										new DAO().updatetabledata("testcases", "message",
												"Unable to Connect to the QA Server", "id",
												tctable.getItems().get(it).getId());
									} else if (result.equals("noserver")) {
										runmessage(
												"Unable to find default QA Server. Please check QA Server settings...");
										new DAO().updatetabledata("testcases", "message",
												"Unable to find default QA Server", "id",
												tctable.getItems().get(it).getId());
									} else if (result.equals("runerror")) {
										runmessage("Runtime Error. Please Try Again...");
										new DAO().updatetabledata("testcases", "message", "Runtime Error", "id",
												tctable.getItems().get(it).getId());
									} else if (result.matches("\\d+")) {
										if (Integer.parseInt(result) == 0) {
											passcount++;
											passedtext.setText(Integer.toString(passcount));
											postresultactions(tctable.getItems().get(it).getId(), result, "Pass",
													Integer.parseInt(tctable.getItems().get(it).getTestsuiteid()),
													tctable.getItems().get(it).getRelease(),
													tctable.getItems().get(it).getCycle(), "Run Successul", batchid);
										} else {
											failcount++;
											failedtext.setText(Integer.toString(failcount));
											postresultactions(tctable.getItems().get(it).getId(), result, "Fail",
													Integer.parseInt(tctable.getItems().get(it).getTestsuiteid()),
													tctable.getItems().get(it).getRelease(),
													tctable.getItems().get(it).getCycle(), "Run Successful", batchid);
										}
									} else {
										failcount++;
										failedtext.setText(Integer.toString(failcount));
										postresultactions(tctable.getItems().get(it).getId(), result, "Fail",
												Integer.parseInt(tctable.getItems().get(it).getTestsuiteid()),
												tctable.getItems().get(it).getRelease(),
												tctable.getItems().get(it).getCycle(), "Run Successful", batchid);
									}
									showtablenow();
								} finally {
									latch.countDown();
								}
							}
						});
						latch.await();
					}
					statustext.setText("Execution Completed.");

					excelicon.setVisible(true);
					pdficon.setVisible(true);

					// Keep with the background work
					return null;
				}
			};
		}
	};

	@SuppressWarnings("unchecked")
	private void showtablenow() {
		testcaselist = new DAO().getTestCasesDetails("testcases", startdate.getValue().toString(),
				enddate.getValue().toString(), releasecombo.getSelectionModel().getSelectedItem());

		if (releasecombo.getSelectionModel().getSelectedIndex() > 0
				&& cyclecombo.getSelectionModel().getSelectedIndex() == 0
				&& tscombo.getSelectionModel().getSelectedIndex() == 0) {
			ArrayList<FieldtoFieldBinaryTrade> filteredData = filterByDescription(testcaselist,
					releasecombo.getSelectionModel().getSelectedItem());
			populateTable(filteredData);
		} else if (releasecombo.getSelectionModel().getSelectedIndex() > 0
				&& cyclecombo.getSelectionModel().getSelectedIndex() > 0
				&& tscombo.getSelectionModel().getSelectedIndex() == 0) {

			ArrayList<FieldtoFieldBinaryTrade> filteredData1 = filterByDescription(testcaselist,
					releasecombo.getSelectionModel().getSelectedItem());
			ArrayList<FieldtoFieldBinaryTrade> filteredData = filterByDescription(filteredData1,
					cyclecombo.getSelectionModel().getSelectedItem());
			populateTable(filteredData);
		} else if (releasecombo.getSelectionModel().getSelectedIndex() > 0
				&& cyclecombo.getSelectionModel().getSelectedIndex() > 0
				&& tscombo.getSelectionModel().getSelectedIndex() > 0) {

			ArrayList<FieldtoFieldBinaryTrade> filteredData1 = filterByDescription(testcaselist,
					releasecombo.getSelectionModel().getSelectedItem());
			ArrayList<FieldtoFieldBinaryTrade> filteredData2 = filterByDescription(filteredData1,
					cyclecombo.getSelectionModel().getSelectedItem());
			ArrayList<FieldtoFieldBinaryTrade> filteredData = filterByDescription(filteredData2,
					tscombo.getSelectionModel().getSelectedItem());
			populateTable(filteredData);
		}
	}

	public void postresultactions(String id, String result, String status, int suiteid, String releasename,
			String cyclename, String message, int batchid1) {
		new DAO().updatetabledata("testcases", "message", message, "id", id);
		new DAO().updatetabledata("testcases", "queryresult", result, "id", id);
		new DAO().updatetabledata("testcases", "teststatus", status, "id", id);
		new DAO().updatetabledata("testcases", "executeduserid", Integer.toString(Loggedinuserdetails.id), "id", id);
		new DAO().updatetabledata("testcases", "executeddate", "CURRENT_TIMESTAMP", "id", id);
		new DAO().updatetabledata("testcases", "executioncount", "executioncount+1", "id", id);
		if (status.equals("Pass")) {
			new DAO().updatetabledata("testcases", "passcount", "passcount+1", "id", id);
		} else if (status.equals("Fail")) {
			new DAO().updatetabledata("testcases", "failcount", "failcount+1", "id", id);
		}
		new DAO().createresultstable("fieldresults", Long.parseLong(Integer.toString(Loggedinuserdetails.id)), id,
				message, result, status, batchid1, suiteid, releasename, cyclename, QADefaultServerDetails.id);
	}

	public ArrayList<String> addcolumnsforreport() {
		ArrayList<String> result = new ArrayList<String>();

		result.add("T.C ID");
		result.add("Release");
		result.add("Cycle");
		result.add("Test Suite");
		result.add("Test Case");
		// reportcolumnlist.add("Test Condition");
		result.add("Query Result");
		result.add("Status");

		return result;
	}

	public String replacer(ArrayList<String> reportcolumnlist) {
		String reportitems = null;
		StringBuffer reports = new StringBuffer();
		for (int i = 0; i < reportcolumnlist.size(); i++) {
			if (reportcolumnlist.get(i).equals("T.C ID")) {
				reports.append(reportcolumnlist.get(i).replace("T.C ID", "tc.id,"));
			} else if (reportcolumnlist.get(i).equals("Release")) {
				reports.append(reportcolumnlist.get(i).replace("Release", "fr.release,"));
			} else if (reportcolumnlist.get(i).equals("Cycle")) {
				reports.append(reportcolumnlist.get(i).replace("Cycle", "fr.cycle,"));
			} else if (reportcolumnlist.get(i).equals("Test Suite")) {
				reports.append(reportcolumnlist.get(i).replace("Test Suite", "ts.suitename,"));
			} else if (reportcolumnlist.get(i).equals("Test Case")) {
				reports.append(reportcolumnlist.get(i).replace("Test Case", "tc.tcname,"));
			} else if (reportcolumnlist.get(i).equals("Test Condition")) {
				reports.append(reportcolumnlist.get(i).replace("Test Condition", "tc.testcondition,"));
			} else if (reportcolumnlist.get(i).equals("Query Result")) {
				reports.append(reportcolumnlist.get(i).replace("Query Result", "fr.queryresult,"));
			} else if (reportcolumnlist.get(i).equals("Status")) {
				reports.append(reportcolumnlist.get(i).replace("Status", "fr.teststatus,"));
			}

		}
		reportitems = reports.toString();
		reportitems = reportitems.substring(0, reportitems.length() - 1);
		return reportitems;
	}
}
