package com.testmydata.fxcontroller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.ControlReportExecutionBinaryTrade;
import com.testmydata.binarybeans.ModulesBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.QADefaultServerDetails;
import com.testmydata.util.ReportsDownloader;
import com.testmydata.util.StaticImages;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class ExecuteControlReportController implements Initializable {

	private static ExecuteControlReportController userHome = null;
	@FXML
	private ImageView closeicon, runicon, processicon, pdficon, excelicon;
	@FXML
	private JFXComboBox<String> modulecombo, rulecombo;
	@FXML
	private JFXTextField statustext, totaltext, passedtext, failedtext, searchtext;
	@FXML
	private TableView<ControlReportExecutionBinaryTrade> crtable;
	@FXML
	private TableColumn<ControlReportExecutionBinaryTrade, String> module, rulename, stost, sttotr, trtol, ltot,
			rulestatus, sourcecount, stagingcount, transcount, loadingcount, targetcount;
	@FXML
	private AnchorPane actionanchor1;

	ArrayList<ModulesBinaryTrade> moduleslist = new ArrayList<ModulesBinaryTrade>();
	ArrayList<ControlReportExecutionBinaryTrade> ruleslist = new ArrayList<ControlReportExecutionBinaryTrade>();
	ArrayList<ControlReportExecutionBinaryTrade> rulenamelist = new ArrayList<ControlReportExecutionBinaryTrade>();
	ArrayList<String> reportcolumnlist = new ArrayList<String>();

	static boolean executed = false;
	static int batchid = 0, counttestcases = 0, it = 0, passcount = 0, failcount = 0, noofthreads = 0, batchsize = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setexistingmodules();
		closeicon.setImage(StaticImages.closeicon);
		runicon.setImage(StaticImages.source_execute);
		processicon.setImage(StaticImages.source_run);
		pdficon.setImage(StaticImages.pdficon);
		excelicon.setImage(StaticImages.excelicon);

		module.setCellValueFactory(new PropertyValueFactory<>("module"));
		rulename.setCellValueFactory(new PropertyValueFactory<>("name"));
		stost.setCellValueFactory(new PropertyValueFactory<>("stoststatus"));
		sttotr.setCellValueFactory(new PropertyValueFactory<>("sttotrstatus"));
		trtol.setCellValueFactory(new PropertyValueFactory<>("trtolstatus"));
		ltot.setCellValueFactory(new PropertyValueFactory<>("ltotstatus"));
		sourcecount.setCellValueFactory(new PropertyValueFactory<>("sourcecount"));
		stagingcount.setCellValueFactory(new PropertyValueFactory<>("stagingcount"));
		transcount.setCellValueFactory(new PropertyValueFactory<>("transcount"));
		loadingcount.setCellValueFactory(new PropertyValueFactory<>("loadingcount"));
		targetcount.setCellValueFactory(new PropertyValueFactory<>("targetcount"));
		rulestatus.setCellValueFactory(new PropertyValueFactory<>("rulestatus"));

		Label lbl = new Label(" Execute ");
		lbl.setStyle(StaticImages.lblStyle);
		lbl.setMinWidth(50);
		lbl.setLayoutY(15);
		lbl.setLayoutX(65);
		lbl.setVisible(false);
		actionanchor1.getChildren().add(lbl);

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

		Label excellbl = new Label(" Excel Report ");
		excellbl.setStyle(StaticImages.lblStyle);
		excellbl.setMinWidth(65);
		excellbl.setLayoutY(15);
		excellbl.setLayoutX(105);
		excellbl.setVisible(false);
		actionanchor1.getChildren().add(excellbl);

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

		Label pdflbl = new Label("  PDF Report ");
		pdflbl.setStyle(StaticImages.lblStyle);
		pdflbl.setMinWidth(65);
		pdflbl.setLayoutY(15);
		pdflbl.setLayoutX(145);
		pdflbl.setVisible(false);
		actionanchor1.getChildren().add(pdflbl);

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

		modulecombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("QA Modules")) {
						populaterules(newFruit);
						setrulecombo(newFruit);

					} else {
						setdefaultrules();
						removePreviousRulesfromtable();
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		rulecombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select Rule")) {
						ArrayList<ControlReportExecutionBinaryTrade> filteredData1 = filterByDescription(ruleslist,
								modulecombo.getSelectionModel().getSelectedItem());
						ArrayList<ControlReportExecutionBinaryTrade> filteredData = filterByDescription(filteredData1,
								newFruit);
						populateTable(filteredData);
					} else {
						if (modulecombo.getSelectionModel().getSelectedIndex() > 0) {
							ArrayList<ControlReportExecutionBinaryTrade> filteredData = filterByDescription(ruleslist,
									modulecombo.getSelectionModel().getSelectedItem());
							populateTable(filteredData);
						}
					}

				} catch (NullPointerException ne) {
				}
			}
		});

		stost.setCellFactory(
				new Callback<TableColumn<ControlReportExecutionBinaryTrade, String>, TableCell<ControlReportExecutionBinaryTrade, String>>() {

					@Override
					public TableCell<ControlReportExecutionBinaryTrade, String> call(
							TableColumn<ControlReportExecutionBinaryTrade, String> param) {
						final TableCell<ControlReportExecutionBinaryTrade, String> cell = new TableCell<ControlReportExecutionBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									ControlReportExecutionBinaryTrade result = getTableView().getItems()
											.get(getIndex());
									if (item != null) {
										setText(item.toString());

										if (result.getStoststatus().equals("Pass")) {
											setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
										}
										if (result.getStoststatus().equals("Fail")) {
											setStyle("-fx-text-fill: #F8340D; -fx-font-weight:bold;");
										}
									}
								}
							}
						};
						return cell;
					}
				});
		sttotr.setCellFactory(
				new Callback<TableColumn<ControlReportExecutionBinaryTrade, String>, TableCell<ControlReportExecutionBinaryTrade, String>>() {

					@Override
					public TableCell<ControlReportExecutionBinaryTrade, String> call(
							TableColumn<ControlReportExecutionBinaryTrade, String> param) {
						final TableCell<ControlReportExecutionBinaryTrade, String> cell = new TableCell<ControlReportExecutionBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									ControlReportExecutionBinaryTrade result = getTableView().getItems()
											.get(getIndex());
									if (item != null) {
										setText(item.toString());

										if (result.getSttotrstatus().equals("Pass")) {
											setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
										}
										if (result.getSttotrstatus().equals("Fail")) {
											setStyle("-fx-text-fill: #F8340D; -fx-font-weight:bold;");
										}
									}
								}
							}
						};
						return cell;
					}
				});
		trtol.setCellFactory(
				new Callback<TableColumn<ControlReportExecutionBinaryTrade, String>, TableCell<ControlReportExecutionBinaryTrade, String>>() {

					@Override
					public TableCell<ControlReportExecutionBinaryTrade, String> call(
							TableColumn<ControlReportExecutionBinaryTrade, String> param) {
						final TableCell<ControlReportExecutionBinaryTrade, String> cell = new TableCell<ControlReportExecutionBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									ControlReportExecutionBinaryTrade result = getTableView().getItems()
											.get(getIndex());
									if (item != null) {
										setText(item.toString());

										if (result.getTrtolstatus().equals("Pass")) {
											setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
										}
										if (result.getTrtolstatus().equals("Fail")) {
											setStyle("-fx-text-fill: #F8340D; -fx-font-weight:bold;");
										}
									}
								}
							}
						};
						return cell;
					}
				});
		ltot.setCellFactory(
				new Callback<TableColumn<ControlReportExecutionBinaryTrade, String>, TableCell<ControlReportExecutionBinaryTrade, String>>() {

					@Override
					public TableCell<ControlReportExecutionBinaryTrade, String> call(
							TableColumn<ControlReportExecutionBinaryTrade, String> param) {
						final TableCell<ControlReportExecutionBinaryTrade, String> cell = new TableCell<ControlReportExecutionBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									ControlReportExecutionBinaryTrade result = getTableView().getItems()
											.get(getIndex());
									if (item != null) {
										setText(item.toString());

										if (result.getLtotstatus().equals("Pass")) {
											setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
										}
										if (result.getLtotstatus().equals("Fail")) {
											setStyle("-fx-text-fill: #F8340D; -fx-font-weight:bold;");
										}
									}
								}
							}
						};
						return cell;
					}
				});
		rulestatus.setCellFactory(
				new Callback<TableColumn<ControlReportExecutionBinaryTrade, String>, TableCell<ControlReportExecutionBinaryTrade, String>>() {

					@Override
					public TableCell<ControlReportExecutionBinaryTrade, String> call(
							TableColumn<ControlReportExecutionBinaryTrade, String> param) {
						final TableCell<ControlReportExecutionBinaryTrade, String> cell = new TableCell<ControlReportExecutionBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									ControlReportExecutionBinaryTrade result = getTableView().getItems()
											.get(getIndex());
									if (item != null) {
										setText(item.toString());

										if (result.getRulestatus().equals("Passed")) {
											setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
										}
										if (result.getRulestatus().equals("Failed")) {
											setStyle("-fx-text-fill: #F8340D; -fx-font-weight:bold;");
										}
									}
								}
							}
						};
						return cell;
					}
				});

		sourcecount.setCellFactory(
				new Callback<TableColumn<ControlReportExecutionBinaryTrade, String>, TableCell<ControlReportExecutionBinaryTrade, String>>() {

					@Override
					public TableCell<ControlReportExecutionBinaryTrade, String> call(
							TableColumn<ControlReportExecutionBinaryTrade, String> param) {
						final TableCell<ControlReportExecutionBinaryTrade, String> cell = new TableCell<ControlReportExecutionBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									if (item != null) {
										setText(item.toString());
										setStyle("-fx-font-weight:bold;");
									}
								}
							}
						};
						return cell;
					}
				});
		stagingcount.setCellFactory(
				new Callback<TableColumn<ControlReportExecutionBinaryTrade, String>, TableCell<ControlReportExecutionBinaryTrade, String>>() {

					@Override
					public TableCell<ControlReportExecutionBinaryTrade, String> call(
							TableColumn<ControlReportExecutionBinaryTrade, String> param) {
						final TableCell<ControlReportExecutionBinaryTrade, String> cell = new TableCell<ControlReportExecutionBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									if (item != null) {
										setText(item.toString());
										setStyle("-fx-font-weight:bold;");
									}
								}
							}
						};
						return cell;
					}
				});

		transcount.setCellFactory(
				new Callback<TableColumn<ControlReportExecutionBinaryTrade, String>, TableCell<ControlReportExecutionBinaryTrade, String>>() {

					@Override
					public TableCell<ControlReportExecutionBinaryTrade, String> call(
							TableColumn<ControlReportExecutionBinaryTrade, String> param) {
						final TableCell<ControlReportExecutionBinaryTrade, String> cell = new TableCell<ControlReportExecutionBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									if (item != null) {
										setText(item.toString());
										setStyle("-fx-font-weight:bold;");
									}
								}
							}
						};
						return cell;
					}
				});
		loadingcount.setCellFactory(
				new Callback<TableColumn<ControlReportExecutionBinaryTrade, String>, TableCell<ControlReportExecutionBinaryTrade, String>>() {

					@Override
					public TableCell<ControlReportExecutionBinaryTrade, String> call(
							TableColumn<ControlReportExecutionBinaryTrade, String> param) {
						final TableCell<ControlReportExecutionBinaryTrade, String> cell = new TableCell<ControlReportExecutionBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									if (item != null) {
										setText(item.toString());
										setStyle("-fx-font-weight:bold;");
									}
								}
							}
						};
						return cell;
					}
				});
		targetcount.setCellFactory(
				new Callback<TableColumn<ControlReportExecutionBinaryTrade, String>, TableCell<ControlReportExecutionBinaryTrade, String>>() {

					@Override
					public TableCell<ControlReportExecutionBinaryTrade, String> call(
							TableColumn<ControlReportExecutionBinaryTrade, String> param) {
						final TableCell<ControlReportExecutionBinaryTrade, String> cell = new TableCell<ControlReportExecutionBinaryTrade, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
									if (item != null) {
										setText(item.toString());
										setStyle("-fx-font-weight:bold;");
									}
								}
							}
						};
						return cell;
					}
				});

		runicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				run();
			}
		});

		pdficon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				File ff = new File(new File(".", "/Reports/ControlReport/PDF").getAbsolutePath());
				if (!(ff.exists() && ff.isDirectory())) {
					ff.mkdirs();
				}
				if (reportcolumnlist != null || reportcolumnlist.size() > 0) {
					reportcolumnlist.clear();
				}
				reportcolumnlist.addAll(addcolumnsforpdfreport());
				ReportsDownloader rd = new ReportsDownloader();
				rd.download("Control Report", batchid, "Reports/ControlReport/PDF", "pdf", reportcolumnlist,
						new DAO().getcrresults(batchid, 0, replacer(reportcolumnlist), reportcolumnlist.size(),
								QADefaultServerDetails.id));
			}
		});

		excelicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				File ff = new File(new File(".", "/Reports/ControlReport/Excel").getAbsolutePath());
				if (!(ff.exists() && ff.isDirectory())) {
					ff.mkdirs();
				}
				if (reportcolumnlist != null || reportcolumnlist.size() > 0) {
					reportcolumnlist.clear();
				}
				reportcolumnlist.addAll(addcolumnsforpdfreport());
				ReportsDownloader rd = new ReportsDownloader();
				rd.download("Control Report", batchid, "Reports/ControlReport/Excel", "excel", reportcolumnlist,
						new DAO().getcrresults(batchid, 0, replacer(reportcolumnlist), reportcolumnlist.size(),
								QADefaultServerDetails.id));
			}
		});

		searchtext.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent event) {
				String enteredString = searchtext.getText().toString();
				if (enteredString != null) {
					if (enteredString.length() >= 1) {

						if (modulecombo.getSelectionModel().getSelectedIndex() > 0
								&& rulecombo.getSelectionModel().getSelectedIndex() == 0) {
							ArrayList<ControlReportExecutionBinaryTrade> filteredData1 = filterByDescription(ruleslist,
									modulecombo.getSelectionModel().getSelectedItem());
							ArrayList<ControlReportExecutionBinaryTrade> filteredData = filterByDescription(
									filteredData1, enteredString);
							populateTable(filteredData);
						} else if (modulecombo.getSelectionModel().getSelectedIndex() > 0
								&& rulecombo.getSelectionModel().getSelectedIndex() > 0) {
							ArrayList<ControlReportExecutionBinaryTrade> filteredData2 = filterByDescription(ruleslist,
									modulecombo.getSelectionModel().getSelectedItem());
							ArrayList<ControlReportExecutionBinaryTrade> filteredData1 = filterByDescription(
									filteredData2, rulecombo.getSelectionModel().getSelectedItem());
							ArrayList<ControlReportExecutionBinaryTrade> filteredData = filterByDescription(
									filteredData1, enteredString);
							populateTable(filteredData);
						}

					} else if (!(enteredString.length() >= 1)) {
						if (modulecombo.getSelectionModel().getSelectedIndex() > 0
								&& rulecombo.getSelectionModel().getSelectedIndex() == 0) {
							ArrayList<ControlReportExecutionBinaryTrade> filteredData1 = filterByDescription(ruleslist,
									modulecombo.getSelectionModel().getSelectedItem());

							populateTable(filteredData1);
						} else if (modulecombo.getSelectionModel().getSelectedIndex() > 0
								&& rulecombo.getSelectionModel().getSelectedIndex() > 0) {
							ArrayList<ControlReportExecutionBinaryTrade> filteredData2 = filterByDescription(ruleslist,
									modulecombo.getSelectionModel().getSelectedItem());
							ArrayList<ControlReportExecutionBinaryTrade> filteredData1 = filterByDescription(
									filteredData2, rulecombo.getSelectionModel().getSelectedItem());
							populateTable(filteredData1);
						}
					}
				}
			}
		});

		// closing screen when clicks on home icon
		closeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				AnchorPane pane = (AnchorPane) ((ImageView) t.getSource()).getParent().getParent().getParent();
				pane.getChildren().remove(pane.getChildren().size() - 1);

				ExecuteControlReportController nc = new ExecuteControlReportController();
				Cleanup.nullifyStrings(nc);
			}
		});
	}

	// Method used to get data from previous class
	public static ExecuteControlReportController getInstance() {
		return userHome;
	}

	// Code to control Modules Combo Box
	private void setexistingmodules() {
		modulecombo.getItems().clear();
		modulecombo.getItems().add("QA Modules");
		modulecombo.getSelectionModel().select(0);
		moduleslist = new DAO().getModuleDetails("modules", "all", Loggedinuserdetails.defaultproject);
		if (moduleslist != null && moduleslist.size() > 0) {
			for (int i = 0; i < moduleslist.size(); i++) {
				modulecombo.getItems().add(moduleslist.get(i).getModulename());
			}
		}
		modulecombo.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
	}

	private void setrulecombo(String module) {
		setdefaultrules();

		rulenamelist.clear();
		rulenamelist = new DAO().getrulenames(module, Loggedinuserdetails.defaultproject);
		if (rulenamelist != null && rulenamelist.size() > 0) {
			for (int i = 0; i < rulenamelist.size(); i++) {
				rulecombo.getItems().add(rulenamelist.get(i).getName());
			}
		}
		rulecombo.setStyle("-fx-text-fill: #7c04c6; -fx-font-weight:bold;");
	}

	private void setdefaultrules() {
		rulecombo.getItems().clear();
		rulecombo.getItems().add("Select Rule");
		rulecombo.getSelectionModel().select(0);
	}

	private void populaterules(String module) {
		try {
			if (module != null) {
				removePreviousRulesfromtable();

				if (executed) {
					ruleslist = new DAO().getrulesAfterexecution(batchid, Loggedinuserdetails.defaultproject);
					executed = false;
					showtablenow(ruleslist);
				} else {
					ruleslist = new DAO().getrulesBeforeexecution(Loggedinuserdetails.defaultproject);
					passcount = 0;
					failcount = 0;
				}
			}

			if (ruleslist == null || ruleslist.size() == 0) {
				removePreviousRulesfromtable();
			} else {

				populateTable(ruleslist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateTable(ArrayList<ControlReportExecutionBinaryTrade> arrayListData) {
		if (arrayListData != null && arrayListData.size() > 0) {
			counttestcases = 0;
			ObservableList<ControlReportExecutionBinaryTrade> data = FXCollections.observableArrayList();

			for (int i = 0; i < arrayListData.size(); i++) {
				ControlReportExecutionBinaryTrade crbt = arrayListData.get(i);
				crbt.setId(arrayListData.get(i).getId());
				crbt.setModule(arrayListData.get(i).getModule());
				crbt.setName(arrayListData.get(i).getName());
				crbt.setStoststatus(arrayListData.get(i).getStoststatus());
				crbt.setSttotrstatus(arrayListData.get(i).getSttotrstatus());
				crbt.setTrtolstatus(arrayListData.get(i).getTrtolstatus());
				crbt.setLtotstatus(arrayListData.get(i).getLtotstatus());
				crbt.setSourcecount(arrayListData.get(i).getSourcecount());
				crbt.setStagingcount(arrayListData.get(i).getStagingcount());
				crbt.setTranscount(arrayListData.get(i).getTranscount());
				crbt.setLoadingcount(arrayListData.get(i).getLoadingcount());
				crbt.setTargetcount(arrayListData.get(i).getTargetcount());
				crbt.setRulestatus(arrayListData.get(i).getRulestatus());

				if (arrayListData.get(i).getRulestatus().equals("Passed")) {
					passcount++;
				} else if (arrayListData.get(i).getRulestatus().equals("Failed")) {
					failcount++;
				}

				data.add(crbt);
				crtable.setItems(data);
				crtable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

				counttestcases++;
			}
			crtable.refresh();
			totaltext.setText(Integer.toString(counttestcases));
			passedtext.setText(Integer.toString(passcount / 2));
			failedtext.setText(Integer.toString(failcount / 2));
		} else {
			removePreviousRulesfromtable();
		}
	}

	private void removePreviousRulesfromtable() {
		if (crtable != null) {
			for (int i = crtable.getItems().size() - 1; i >= 0; i--) {
				crtable.getItems().remove(i);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private ArrayList filterByDescription(ArrayList<ControlReportExecutionBinaryTrade> unFiltered, String str) {

		ArrayList<ControlReportExecutionBinaryTrade> rules = new ArrayList<ControlReportExecutionBinaryTrade>();
		for (ControlReportExecutionBinaryTrade bean : unFiltered) {
			if (bean.getModule() != null && bean.getModule().toLowerCase().contains(str.toLowerCase())) {
				rules.add(bean);
			} else if (bean.getName() != null && bean.getName().toLowerCase().contains(str.toLowerCase())) {
				rules.add(bean);
			}

		}
		return rules;
	}

	private void resetcounts(String totalcount) {
		excelicon.setVisible(false);
		pdficon.setVisible(false);
		statustext.clear();
		totaltext.setText(totalcount);
		passedtext.setText("0");
		failedtext.setText("0");
	}

	// Method to display the Messages
	public void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	private void run() {
		if (crtable.getItems().size() > 0) {
			resetcounts(Integer.toString(crtable.getItems().size()));
			processicon.setVisible(true);

			executionservice.reset();
			executionservice.start();

		} else {
			modulecombo.getSelectionModel().select(0);
			resetcounts(Integer.toString(crtable.getItems().size()));
			runmessage("Please Select Control Report for Execution.\n\nNote : Please Choose QA Module or Rule Name...");
		}
	}

	// Runs in Background and updates UI Responsively
	Service<Void> executionservice = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					// Background work
					it = 0;
					passcount = 0;
					failcount = 0;
					batchid = new DAO().getcrmaxbatchid() + 1;
					batchsize = 20;

					noofthreads = crtable.getItems().size() / batchsize;

					final CountDownLatch latch = new CountDownLatch(1);

					Thread[] threads = new Thread[noofthreads + 1];
					for (int i = 0; i < threads.length; i++) {

						final int startNumber = (i * batchsize) + 1;

						int number = 0;
						if (crtable.getItems().size() >= 20) {
							number = (batchsize + startNumber) - 1;
						} else {
							number = crtable.getItems().size();
						}

						final int endNumber = number;

						threads[i] = new Thread(new Runnable() {
							@Override
							public void run() {
								controlreportsexecution(startNumber, endNumber, batchid);
							}
						});
						threads[i].start();
					}

					try {

					} finally {
						latch.countDown();
					}

					latch.await();

					executed = true;

					// Keep with the background work
					return null;
				}
			};
		}
	};

	private void controlreportsexecution(int start, int end, int batchid) {
		for (int i = start; i <= end; i++) {
			new DAO().executecrrules(Integer.parseInt(crtable.getItems().get(i - 1).getId()), batchid,
					Long.parseLong(Integer.toString(Loggedinuserdetails.id)), QADefaultServerDetails.id);
		}
		populaterules(modulecombo.getSelectionModel().getSelectedItem());
		statustext.setText("Execution Completed.");

		processicon.setVisible(false);
		excelicon.setVisible(true);
		pdficon.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	private void showtablenow(ArrayList<ControlReportExecutionBinaryTrade> ruleslist) {

		if (modulecombo.getSelectionModel().getSelectedIndex() > 0
				&& rulecombo.getSelectionModel().getSelectedIndex() == 0) {
			ArrayList<ControlReportExecutionBinaryTrade> filteredData = filterByDescription(ruleslist,
					modulecombo.getSelectionModel().getSelectedItem());
			populateTable(filteredData);
		} else if (modulecombo.getSelectionModel().getSelectedIndex() > 0
				&& rulecombo.getSelectionModel().getSelectedIndex() > 0) {

			ArrayList<ControlReportExecutionBinaryTrade> filteredData1 = filterByDescription(ruleslist,
					modulecombo.getSelectionModel().getSelectedItem());
			ArrayList<ControlReportExecutionBinaryTrade> filteredData = filterByDescription(filteredData1,
					rulecombo.getSelectionModel().getSelectedItem());
			populateTable(filteredData);
		}
	}

	public ArrayList<String> addcolumnsforexcelreport() {
		ArrayList<String> result = new ArrayList<String>();

		result.add("Result ID");
		result.add("Date");
		result.add("Executed By");
		result.add("Module");
		result.add("Rule");
		result.add("Message");
		result.add("Source Count");
		result.add("So-Stg Diff");
		result.add("Staging Count");
		result.add("Stg-Trans Diff");
		result.add("Transformation count");
		result.add("Trans-Ldg Diff");
		result.add("Loading Count");
		result.add("Ldg-Trg Diff");
		result.add("Target Count");
		result.add("Source Sum");
		result.add("So-Stg Sum Diff");
		result.add("Staging Sum");
		result.add("Stg-Trans Sum Diff");
		result.add("Transformation Sum");
		result.add("Trans-Ldg Sum Diff");
		result.add("Loading Sum Count");
		result.add("Ldg-Trg Sum Diff");
		result.add("Target Sum Count");
		result.add("Result");

		return result;
	}

	public ArrayList<String> addcolumnsforpdfreport() {
		ArrayList<String> result = new ArrayList<String>();

		result.add("Result ID");
		result.add("Module");
		result.add("Rule");
		// result.add("Source Count");
		// result.add("Staging Count");
		// result.add("Transformation count");
		// result.add("Loading Count");
		// result.add("Target Count");
		// result.add("Source Sum");
		// result.add("Staging Sum");
		// result.add("Transformation Sum");
		// result.add("Loading Sum Count");
		// result.add("Target Sum Count");
		result.add("So-Stg Status");
		result.add("Stg-Trans Status");
		result.add("Trans-Ldg Status");
		result.add("Ldg-Trg Status");
		result.add("Result");

		return result;
	}

	public String replacer(ArrayList<String> reportcolumnlist) {
		String reportitems = null;
		StringBuffer reports = new StringBuffer();
		for (int i = 0; i < reportcolumnlist.size(); i++) {
			if (reportcolumnlist.get(i).equals("Result ID")) {
				reports.append(reportcolumnlist.get(i).replace("Result ID", "id,"));
			} else if (reportcolumnlist.get(i).equals("Module")) {
				reports.append(reportcolumnlist.get(i).replace("Module",
						"(select module from modules where id = moduleid)as module,"));
			} else if (reportcolumnlist.get(i).equals("Rule")) {
				reports.append(reportcolumnlist.get(i).replace("Rule",
						"(select rulename from controlreportrules where id = ruleid)as rule,"));
			} else if (reportcolumnlist.get(i).equals("Message")) {
				reports.append(reportcolumnlist.get(i).replace("Message", "message,"));
			} else if (reportcolumnlist.get(i).equals("Source Count")) {
				reports.append(reportcolumnlist.get(i).replace("Source Count", "sourcecount,"));
			} else if (reportcolumnlist.get(i).equals("So-Stg Diff")) {
				reports.append(reportcolumnlist.get(i).replace("So-Stg Diff", "stostdiff,"));
			} else if (reportcolumnlist.get(i).equals("Staging Count")) {
				reports.append(reportcolumnlist.get(i).replace("Staging Count", "stagingcount,"));
			} else if (reportcolumnlist.get(i).equals("Stg-Trans Diff")) {
				reports.append(reportcolumnlist.get(i).replace("Stg-Trans Diff", "sttotrdiff,"));
			} else if (reportcolumnlist.get(i).equals("Transformation count")) {
				reports.append(reportcolumnlist.get(i).replace("Transformation count", "transcount,"));
			} else if (reportcolumnlist.get(i).equals("Trans-Ldg Diff")) {
				reports.append(reportcolumnlist.get(i).replace("Trans-Ldg Diff", "trtoldiff,"));
			} else if (reportcolumnlist.get(i).equals("Loading Count")) {
				reports.append(reportcolumnlist.get(i).replace("Loading Count", "loadingcount,"));
			} else if (reportcolumnlist.get(i).equals("Ldg-Trg Diff")) {
				reports.append(reportcolumnlist.get(i).replace("Ldg-Trg Diff", "ltotdiff,"));
			} else if (reportcolumnlist.get(i).equals("Target Count")) {
				reports.append(reportcolumnlist.get(i).replace("Target Count", "targetcount,"));
			} else if (reportcolumnlist.get(i).equals("Source Sum")) {
				reports.append(reportcolumnlist.get(i).replace("Source Sum", "sourcecolvalue,"));
			} else if (reportcolumnlist.get(i).equals("So-Stg Sum Diff")) {
				reports.append(reportcolumnlist.get(i).replace("So-Stg Sum Diff", "stostcoldiff,"));
			} else if (reportcolumnlist.get(i).equals("Staging Sum")) {
				reports.append(reportcolumnlist.get(i).replace("Staging Sum", "stagingcolvalue,"));
			} else if (reportcolumnlist.get(i).equals("Stg-Trans Sum Diff")) {
				reports.append(reportcolumnlist.get(i).replace("Stg-Trans Sum Diff", "sttotrcoldiff,"));
			} else if (reportcolumnlist.get(i).equals("Transformation Sum")) {
				reports.append(reportcolumnlist.get(i).replace("Transformation Sum", "transcolvalue,"));
			} else if (reportcolumnlist.get(i).equals("Trans-Ldg Sum Diff")) {
				reports.append(reportcolumnlist.get(i).replace("Trans-Ldg Sum Diff", "trtolcoldiff,"));
			} else if (reportcolumnlist.get(i).equals("Loading Sum Count")) {
				reports.append(reportcolumnlist.get(i).replace("Loading Sum Count", "loadingcolvalue,"));
			} else if (reportcolumnlist.get(i).equals("Ldg-Trg Sum Diff")) {
				reports.append(reportcolumnlist.get(i).replace("Ldg-Trg Sum Diff", "ltotcoldiff,"));
			} else if (reportcolumnlist.get(i).equals("Target Sum Count")) {
				reports.append(reportcolumnlist.get(i).replace("Target Sum Count", "targetcolvalue,"));
			} else if (reportcolumnlist.get(i).equals("Result")) {
				reports.append(reportcolumnlist.get(i).replace("Result", "result,"));
			} else if (reportcolumnlist.get(i).equals("Date")) {
				reports.append(reportcolumnlist.get(i).replace("Date", "DATE_FORMAT(executeddate, '%Y-%m-%d'),"));
			} else if (reportcolumnlist.get(i).equals("Executed By")) {
				reports.append(reportcolumnlist.get(i).replace("Executed By",
						"(select userId from users where id = executeduserid)as user,"));
			} else if (reportcolumnlist.get(i).equals("So-Stg Status")) {
				reports.append(reportcolumnlist.get(i).replace("So-Stg Status", "stost,"));
			} else if (reportcolumnlist.get(i).equals("Stg-Trans Status")) {
				reports.append(reportcolumnlist.get(i).replace("Stg-Trans Status", "sttotr,"));
			} else if (reportcolumnlist.get(i).equals("Trans-Ldg Status")) {
				reports.append(reportcolumnlist.get(i).replace("Trans-Ldg Status", "trtol,"));
			} else if (reportcolumnlist.get(i).equals("Ldg-Trg Status")) {
				reports.append(reportcolumnlist.get(i).replace("Ldg-Trg Status", "ltot,"));
			}

		}
		reportitems = reports.toString();
		reportitems = reportitems.substring(0, reportitems.length() - 1);
		return reportitems;
	}
}
