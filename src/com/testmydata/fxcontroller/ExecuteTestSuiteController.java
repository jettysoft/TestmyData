package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.FieldtoFieldBinaryTrade;
import com.testmydata.binarybeans.TestSuiteBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxutil.UndecoratorController;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.QADefaultServerDetails;
import com.testmydata.util.ReportsDownloader;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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

public class ExecuteTestSuiteController implements Initializable {

	private static ExecuteTestSuiteController userHome = null;
	Stage myStage;
	@FXML
	private ImageView homeicon, processicon, excelicon, pdficon;
	@FXML
	private JFXTextField totaltext, executedtext, passedtext, failedtext, searchtext, statustext;
	@FXML
	private JFXButton run;
	@FXML
	private TableView<TestSuiteBinaryTrade> tstable;
	@FXML
	private TableColumn<TestSuiteBinaryTrade, String> id, testsuite, nooftestcases, passcount, failcount, passper,
			failper;
	@FXML
	private TableColumn<TestSuiteBinaryTrade, Boolean> checkbox = new TableColumn<TestSuiteBinaryTrade, Boolean>(
			"Select");
	ArrayList<TestSuiteBinaryTrade> testsuitelist = new ArrayList<TestSuiteBinaryTrade>();
	ArrayList<FieldtoFieldBinaryTrade> sqlscriptslist = new ArrayList<FieldtoFieldBinaryTrade>();
	ArrayList<String> reportcolumnlist = new ArrayList<String>();
	static String[] selectedids = null, selectedtypes = null;
	static int counttestcases = 0, executedtestcases = 0, countpassed = 0, countfailed = 0, batchid = 0, it = 0, in = 0;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		tstable.getColumns().addAll(checkbox);
		checkbox.setCellValueFactory(new PropertyValueFactory<>("checkboxs"));
		checkbox.setCellFactory(CheckBoxTableCell.forTableColumn(checkbox));
		checkbox.setEditable(true);
		tstable.setEditable(true);

		showexistingsuites();
		addcolumnsforreport();

		totaltext.setStyle("-fx-text-fill: #0c23ea; -fx-font-weight:bold;");
		passedtext.setStyle("-fx-text-fill: #40aa03; -fx-font-weight:bold;");
		failedtext.setStyle("-fx-text-fill: #f8340d; -fx-font-weight:bold;");

		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		testsuite.setCellValueFactory(new PropertyValueFactory<>("testsuitename"));
		nooftestcases.setCellValueFactory(new PropertyValueFactory<>("testcasescount"));
		passcount.setCellValueFactory(new PropertyValueFactory<>("passcount"));
		failcount.setCellValueFactory(new PropertyValueFactory<>("failcount"));
		passper.setCellValueFactory(new PropertyValueFactory<>("passper"));
		failper.setCellValueFactory(new PropertyValueFactory<>("failper"));

		id.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		testsuite.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		nooftestcases.setStyle("-fx-text-fill: #0c23ea; -fx-font-weight:bold;");
		passcount.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
		failcount.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
		passper.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
		failper.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");

		pdficon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ReportsDownloader rd = new ReportsDownloader();
				rd.download("TestSuite", batchid, "Reports/TestSuites/PDF", "pdf", reportcolumnlist,
						new DAO().getfieldresults(batchid, 0, 0, replacer(), reportcolumnlist.size(), null,
								QADefaultServerDetails.id));
			}
		});

		excelicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ReportsDownloader rd = new ReportsDownloader();
				rd.download("TestSuite", batchid, "Reports/TestSuites/Excel", "excel", reportcolumnlist,
						new DAO().getfieldresults(batchid, 0, 0, replacer(), reportcolumnlist.size(), null,
								QADefaultServerDetails.id));
			}
		});

		searchtext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String enteredString = searchtext.getText().toString();
				if (enteredString != null && !enteredString.isEmpty()) {
					if (enteredString.length() >= 1) {
						ArrayList<TestSuiteBinaryTrade> filteredData = filterByTestSuites(testsuitelist, enteredString);
						populateTable(filteredData);
					} else if (enteredString != null && enteredString.length() == 0) {
						populateTable(testsuitelist);
					}
				}
			}
		});

		// closing screen when clicks on home icon
		homeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(MouseEvent event) {
				Cleanup scu = new Cleanup();
				ExecuteTestSuiteController nc = new ExecuteTestSuiteController();
				scu.nullifyStrings(nc);

				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();
				UndecoratorController.getInstance(null);
			}
		});
	}

	// Method used to get data from previous class
	public static ExecuteTestSuiteController getInstance() {
		return userHome;
	}

	private void showexistingsuites() {
		testsuitelist = new DAO().gettestsuites("testsuites", null);

		if (testsuitelist != null && testsuitelist.size() > 0) {
			removePrevioustestsuitesfromtable();
			searchtext.setDisable(false);
			populateTable(testsuitelist);
		} else {
			searchtext.setDisable(true);
		}
	}

	@FXML
	private void run() {
		excelicon.setVisible(false);
		pdficon.setVisible(false);
		processicon.setVisible(true);
		runservice.reset();
		runservice.start();
	}

	// Runs in Background and updates UI Responsively
	Service<Void> runservice = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					// Background work
					selectedids = null;
					selectedtypes = null;
					counttestcases = 0;
					executedtestcases = 0;
					countpassed = 0;
					countfailed = 0;
					batchid = new DAO().getmaxbatchid() + 1;

					Vector<String> selectedsuiteids = new Vector<String>();
					Vector<String> selectedtypess = new Vector<String>();
					for (TestSuiteBinaryTrade p : tstable.getItems()) {
						if (p.isCheckboxs() == true) {
							selectedsuiteids.add(p.getId());
							selectedtypess.add(p.getSelectiontype());
							counttestcases += Integer.parseInt(p.getTestcasescount());
						}
					}
					selectedids = selectedsuiteids.toArray(new String[selectedsuiteids.size()]);
					selectedtypes = selectedtypess.toArray(new String[selectedtypess.size()]);

					in = 0;
					for (in = 0; in < selectedids.length; in++) {
						sqlscriptslist.clear();
						sqlscriptslist = new DAO().getSqlScriptforTestSuites(selectedids[in], selectedtypes[in]);
						try {

							final CountDownLatch latch = new CountDownLatch(1);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									try {
										// FX Stuff done here
										totaltext.setText(Integer.toString(counttestcases));
										it = 0;

										for (it = 0; it < sqlscriptslist.size(); it++) {
											final String result = new DAO()
													.getTestResult(sqlscriptslist.get(it).getSqlscript());
											statustext.clear();
											statustext.setText(
													"Executing Test Case Id : " + sqlscriptslist.get(it).getId());
											if (result.equals("conerror")) {
												runmessage(
														"Unable to Connect to the QA Server. Please check QA Server settings...");

											} else if (result.equals("noserver")) {
												runmessage(
														"Unable to find default QA Server. Please check QA Server settings...");

											} else if (result.equals("runerror")) {
												runmessage("Runtime Error. Please Try Again...");

											} else if (result.matches("\\d+")) {
												if (Integer.parseInt(result) == 0) {
													countpassed++;
													executedtestcases++;
													passedtext.setText(Integer.toString(countpassed));
													postresultactions(sqlscriptslist.get(it).getId(), result, "Pass",
															Integer.parseInt(selectedids[in]));
												} else {
													countfailed++;
													executedtestcases++;
													failedtext.setText(Integer.toString(countfailed));
													postresultactions(sqlscriptslist.get(it).getId(), result, "Fail",
															Integer.parseInt(selectedids[in]));
												}
											} else {
												countfailed++;
												executedtestcases++;
												failedtext.setText(Integer.toString(countfailed));
												postresultactions(sqlscriptslist.get(it).getId(), result, "Fail",
														Integer.parseInt(selectedids[in]));
											}
											executedtext.setText(Integer.toString(executedtestcases));
											passedtext.setText(Integer.toString(countpassed));
											showexistingsuites();
										}
									} finally {
										latch.countDown();
									}
								}
							});
							latch.await();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					statustext.setText("Execution Completed.");

					processicon.setVisible(false);
					excelicon.setVisible(true);
					pdficon.setVisible(true);
					// Keep with the background work
					return null;
				}
			};
		}
	};

	private void postresultactions(String id, String result, String status, int suiteid) {
		new DAO().updatetabledata("testcases", "message", "Executed", "id", id);
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
				"Executed", result, status, batchid, suiteid, null, null, QADefaultServerDetails.id);
	}

	// Method to display the Messages
	public void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	private void populateTable(ArrayList<TestSuiteBinaryTrade> arrayListData) {
		if (arrayListData != null && arrayListData.size() > 0) {
			ObservableList<TestSuiteBinaryTrade> data = FXCollections.observableArrayList();
			// int counttestcases = 0;

			for (int i = 0; i < arrayListData.size(); i++) {
				TestSuiteBinaryTrade ffb = arrayListData.get(i);
				ffb.setId(arrayListData.get(i).getId());
				ffb.setTestsuitename(arrayListData.get(i).getTestsuitename());
				ffb.setTestcasescount(arrayListData.get(i).getTestcasescount());
				ffb.setPasscount(arrayListData.get(i).getPasscount());
				ffb.setFailcount(arrayListData.get(i).getFailcount());
				ffb.setPassper(arrayListData.get(i).getPassper());
				ffb.setFailper(arrayListData.get(i).getFailper());

				// counttestcases +=
				// Integer.parseInt(arrayListData.get(i).getTestcasescount());

				data.add(ffb);
				tstable.setItems(data);
				tstable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			}
			tstable.refresh();
			// totaltext.setText(Integer.toString(counttestcases));

		} else {
			removePrevioustestsuitesfromtable();
			CommonFunctions.message = "No Test Suites Found...!";
			CommonFunctions.invokeAlertBox(getClass());
		}
	}

	@SuppressWarnings("rawtypes")
	private ArrayList filterByTestSuites(ArrayList<TestSuiteBinaryTrade> unFiltered, String str) {

		ArrayList<TestSuiteBinaryTrade> expens = new ArrayList<TestSuiteBinaryTrade>();
		for (TestSuiteBinaryTrade bean : unFiltered) {
			if (bean.getTestsuitename() != null && bean.getTestsuitename().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getId() != null && bean.getId().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			}
		}
		return expens;
	}

	private void removePrevioustestsuitesfromtable() {
		if (tstable != null) {
			for (int i = tstable.getItems().size() - 1; i >= 0; i--) {
				tstable.getItems().remove(i);
			}
		}
	}

	private void addcolumnsforreport() {
		reportcolumnlist.clear();
		reportcolumnlist.add("ID");
		reportcolumnlist.add("Test Case");
		reportcolumnlist.add("Test Condition");
		reportcolumnlist.add("SQL Script");
		reportcolumnlist.add("Query Result");
		reportcolumnlist.add("Status");
	}

	private String replacer() {
		String reportitems = null;
		StringBuffer reports = new StringBuffer();
		for (int i = 0; i < reportcolumnlist.size(); i++) {
			if (reportcolumnlist.get(i).equals("ID")) {
				reports.append(reportcolumnlist.get(i).replace("ID", "tc.id,"));
			} else if (reportcolumnlist.get(i).equals("Test Case")) {
				reports.append(reportcolumnlist.get(i).replace("Test Case", "tc.tcname,"));
			} else if (reportcolumnlist.get(i).equals("Test Condition")) {
				reports.append(reportcolumnlist.get(i).replace("Test Condition", "tc.testcondition,"));
			} else if (reportcolumnlist.get(i).equals("SQL Script")) {
				reports.append(reportcolumnlist.get(i).replace("SQL Script", "tc.sqlscript,"));
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
