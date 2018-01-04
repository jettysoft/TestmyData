package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.ControlReportHelperBinaryTrade;
import com.testmydata.binarybeans.ControlReportRulesBinaryTrade;
import com.testmydata.binarybeans.ModulesBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxutil.UndecoratorController;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.QADefaultServerDetails;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class NewControlReportRulesController implements Initializable {

	private static NewControlReportRulesController userHome = null;
	Stage myStage;
	@FXML
	private ImageView homeicon, sourcevalidicon, stagingvalidicon, transvalidicon, targetvalidicon, sourcecloseicon,
			stagingcloseicon, transcloseicon, targetcloseicon, srunicon, strunicon, transrunicon, trunicon,
			sourcecolicon, stagingcolicon, transcolicon, targetcolicon, sourcecolcloseicon, scolrunicon,
			stagingcolcloseicon, stcolrunicon, transcolcloseicon, transcolrunicon, targetcolcloseicon, tcolrunicon;
	@FXML
	private JFXComboBox<String> sdbbox, stablebox, scolumnbox, stdbbox, sttablebox, stcolumnbox, trdbbox, trtablebox,
			trcolumnbox, ldbbox, ltablebox, lcolumnbox, tdbbox, ttablebox, tcolumnbox, modulebox;
	@FXML
	private JFXTextField rulenametext, searchtext;
	@FXML
	private JFXTextArea sourcesqltextarea, stagingsqltextarea, transsqltextarea, targetsqltextarea,
			sourcecolsqltextarea, stagingcolsqltextarea, transcolsqltextarea, targetcolsqltextarea;
	@FXML
	private JFXButton createrule, updaterule;
	@FXML
	private AnchorPane sourceanchor, staginganchor, transanchor, targetanchor, msourceanchor, etlanchor, mtargetanchor,
			sourcecolanchor, stagingcolanchor, transcolanchor, targetcolanchor;
	@FXML
	private TableView<ControlReportRulesBinaryTrade> rulestable;
	@FXML
	private TableColumn<ControlReportRulesBinaryTrade, String> id, name, module, sdb, stable, scolumn, stdb, sttable,
			stcolumn, trdb, trtable, trcolumn, ldb, ltable, lcolumn, tdb, ttable, tcolumn;
	@FXML
	private TableColumn<ControlReportRulesBinaryTrade, Boolean> modifybutton = new TableColumn<ControlReportRulesBinaryTrade, Boolean>(
			"Modify");
	@FXML
	private TableColumn<ControlReportRulesBinaryTrade, Boolean> deletebutton = new TableColumn<ControlReportRulesBinaryTrade, Boolean>(
			"Delete");

	ArrayList<ControlReportHelperBinaryTrade> dblist = new ArrayList<ControlReportHelperBinaryTrade>();
	ArrayList<ControlReportHelperBinaryTrade> tablelist = new ArrayList<ControlReportHelperBinaryTrade>();
	ArrayList<ControlReportHelperBinaryTrade> columnlist = new ArrayList<ControlReportHelperBinaryTrade>();
	ArrayList<ModulesBinaryTrade> moduleslist = new ArrayList<ModulesBinaryTrade>();
	ArrayList<ControlReportRulesBinaryTrade> ruleslist = new ArrayList<ControlReportRulesBinaryTrade>();

	private static String lblStyle = null, ruleid = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setdb();
		settables();
		setcolumns();
		setexistingmodules();
		rulelistservice.reset();
		rulelistservice.start();

		lblStyle = "-fx-background-color: linear-gradient(#277CD2, #0C23EA);  -fx-text-alignment :center; -fx-background-radius: 25; -fx-background-insets: 0; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-clor: red;";
		Label lbl = new Label(" Validation Query ");
		lbl.setStyle(lblStyle);
		lbl.setMinWidth(80);
		lbl.setLayoutX(135);
		lbl.setLayoutY(190);
		lbl.setVisible(false);
		msourceanchor.getChildren().add(lbl);

		Label slbl = new Label(" Source-Staging ");
		slbl.setStyle(lblStyle);
		slbl.setMinWidth(80);
		slbl.setLayoutX(5);
		slbl.setLayoutY(190);
		slbl.setVisible(false);
		msourceanchor.getChildren().add(slbl);

		sourcevalidicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				lbl.setVisible(true);
				slbl.setVisible(true);
				sourcecolicon.setVisible(false);
			}
		});
		sourcevalidicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				lbl.setVisible(false);
				slbl.setVisible(false);
				sourcecolicon.setVisible(true);
			}
		});

		Label scollbl = new Label(" Column Validation ");
		scollbl.setStyle(lblStyle);
		scollbl.setMinWidth(100);
		scollbl.setLayoutX(30);
		scollbl.setLayoutY(190);
		scollbl.setVisible(false);
		msourceanchor.getChildren().add(scollbl);

		sourcecolicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				scollbl.setVisible(true);
				sourcevalidicon.setVisible(false);
			}
		});
		sourcecolicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				scollbl.setVisible(false);
				sourcevalidicon.setVisible(true);
			}
		});

		Label stlbl = new Label(" Staging-Transformation ");
		stlbl.setStyle(lblStyle);
		stlbl.setMinWidth(100);
		stlbl.setLayoutX(55);
		stlbl.setLayoutY(203);
		stlbl.setVisible(false);
		etlanchor.getChildren().add(stlbl);

		Label stlbl1 = new Label(" Validation Query ");
		stlbl1.setStyle(lblStyle);
		stlbl1.setMinWidth(80);
		stlbl1.setLayoutX(240);
		stlbl1.setLayoutY(203);
		stlbl1.setVisible(false);
		etlanchor.getChildren().add(stlbl1);

		stagingvalidicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stlbl1.setVisible(true);
				stlbl.setVisible(true);
				stagingcolicon.setVisible(false);
			}
		});
		stagingvalidicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stlbl1.setVisible(false);
				stlbl.setVisible(false);
				stagingcolicon.setVisible(true);
			}
		});

		Label stcollbl = new Label(" Column Validation ");
		stcollbl.setStyle(lblStyle);
		stcollbl.setMinWidth(100);
		stcollbl.setLayoutX(135);
		stcollbl.setLayoutY(203);
		stcollbl.setVisible(false);
		etlanchor.getChildren().add(stcollbl);

		stagingcolicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stcollbl.setVisible(true);
				stagingvalidicon.setVisible(false);
			}
		});
		stagingcolicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stcollbl.setVisible(false);
				stagingvalidicon.setVisible(true);
			}
		});

		Label trtlbl = new Label(" Transformation-Loading ");
		trtlbl.setStyle(lblStyle);
		trtlbl.setMinWidth(100);
		trtlbl.setLayoutX(275);
		trtlbl.setLayoutY(203);
		trtlbl.setVisible(false);
		etlanchor.getChildren().add(trtlbl);

		Label trtlbl1 = new Label(" Validation Query ");
		trtlbl1.setStyle(lblStyle);
		trtlbl1.setMinWidth(80);
		trtlbl1.setLayoutX(460);
		trtlbl1.setLayoutY(203);
		trtlbl1.setVisible(false);
		etlanchor.getChildren().add(trtlbl1);

		transvalidicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				trtlbl.setVisible(true);
				trtlbl1.setVisible(true);
				transcolicon.setVisible(false);
			}
		});
		transvalidicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				trtlbl.setVisible(false);
				trtlbl1.setVisible(false);
				transcolicon.setVisible(true);
			}
		});

		Label trcoltlbl = new Label(" Column Validation ");
		trcoltlbl.setStyle(lblStyle);
		trcoltlbl.setMinWidth(100);
		trcoltlbl.setLayoutX(360);
		trcoltlbl.setLayoutY(203);
		trcoltlbl.setVisible(false);
		etlanchor.getChildren().add(trcoltlbl);

		transcolicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				trcoltlbl.setVisible(true);
				transvalidicon.setVisible(false);
			}
		});
		transcolicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				trcoltlbl.setVisible(false);
				transvalidicon.setVisible(true);
			}
		});

		Label ttlbl = new Label(" Loading-Target ");
		ttlbl.setStyle(lblStyle);
		ttlbl.setMinWidth(100);
		ttlbl.setLayoutX(5);
		ttlbl.setLayoutY(190);
		ttlbl.setVisible(false);
		mtargetanchor.getChildren().add(ttlbl);

		Label ttlbl1 = new Label(" Validation Query ");
		ttlbl1.setStyle(lblStyle);
		ttlbl1.setMinWidth(80);
		ttlbl1.setLayoutX(147);
		ttlbl1.setLayoutY(190);
		ttlbl1.setVisible(false);
		mtargetanchor.getChildren().add(ttlbl1);

		targetvalidicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				ttlbl.setVisible(true);
				ttlbl1.setVisible(true);
				targetcolicon.setVisible(false);
			}
		});
		targetvalidicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				ttlbl.setVisible(false);
				ttlbl1.setVisible(false);
				targetcolicon.setVisible(true);
			}
		});

		Label ttcollbl1 = new Label(" Column Validation ");
		ttcollbl1.setStyle(lblStyle);
		ttcollbl1.setMinWidth(100);
		ttcollbl1.setLayoutX(40);
		ttcollbl1.setLayoutY(190);
		ttcollbl1.setVisible(false);
		mtargetanchor.getChildren().add(ttcollbl1);

		targetcolicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				ttcollbl1.setVisible(true);
				targetvalidicon.setVisible(false);
			}
		});
		targetcolicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				ttcollbl1.setVisible(false);
				targetvalidicon.setVisible(true);
			}
		});

		sdbbox.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
		stdbbox.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
		trdbbox.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
		ldbbox.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
		tdbbox.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");

		stablebox.setStyle("-fx-text-fill: #0C23EA; -fx-font-weight:bold;");
		sttablebox.setStyle("-fx-text-fill: #0C23EA; -fx-font-weight:bold;");
		trtablebox.setStyle("-fx-text-fill: #0C23EA; -fx-font-weight:bold;");
		ltablebox.setStyle("-fx-text-fill: #0C23EA; -fx-font-weight:bold;");
		ttablebox.setStyle("-fx-text-fill: #0C23EA; -fx-font-weight:bold;");

		scolumnbox.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		stcolumnbox.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		trcolumnbox.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		lcolumnbox.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		tcolumnbox.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");

		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		module.setCellValueFactory(new PropertyValueFactory<>("module"));
		sdb.setCellValueFactory(new PropertyValueFactory<>("sdb"));
		stable.setCellValueFactory(new PropertyValueFactory<>("stable"));
		scolumn.setCellValueFactory(new PropertyValueFactory<>("scolumn"));
		stdb.setCellValueFactory(new PropertyValueFactory<>("stdb"));
		sttable.setCellValueFactory(new PropertyValueFactory<>("sttable"));
		stcolumn.setCellValueFactory(new PropertyValueFactory<>("stcolumn"));
		trdb.setCellValueFactory(new PropertyValueFactory<>("trdb"));
		trtable.setCellValueFactory(new PropertyValueFactory<>("trtable"));
		trcolumn.setCellValueFactory(new PropertyValueFactory<>("trcolumn"));
		ldb.setCellValueFactory(new PropertyValueFactory<>("ldb"));
		ltable.setCellValueFactory(new PropertyValueFactory<>("ltable"));
		lcolumn.setCellValueFactory(new PropertyValueFactory<>("lcolumn"));
		tdb.setCellValueFactory(new PropertyValueFactory<>("tdb"));
		ttable.setCellValueFactory(new PropertyValueFactory<>("ttable"));
		tcolumn.setCellValueFactory(new PropertyValueFactory<>("tcolumn"));

		id.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
		name.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		module.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		sdb.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
		stable.setStyle("-fx-text-fill: #0C23EA; -fx-font-weight:bold;");
		scolumn.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		stdb.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
		sttable.setStyle("-fx-text-fill: #0C23EA; -fx-font-weight:bold;");
		stcolumn.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		trdb.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
		trtable.setStyle("-fx-text-fill: #0C23EA; -fx-font-weight:bold;");
		trcolumn.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		ldb.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
		ltable.setStyle("-fx-text-fill: #0C23EA; -fx-font-weight:bold;");
		lcolumn.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		tdb.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
		ttable.setStyle("-fx-text-fill: #0C23EA; -fx-font-weight:bold;");
		tcolumn.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");

		modifybutton.setSortable(false);
		modifybutton.setCellValueFactory(new PropertyValueFactory<>("buttons"));
		modifybutton.setPrefWidth(85);
		modifybutton.setResizable(false);
		modifybutton.setCellFactory(
				new Callback<TableColumn<ControlReportRulesBinaryTrade, Boolean>, TableCell<ControlReportRulesBinaryTrade, Boolean>>() {
					@Override
					public TableCell<ControlReportRulesBinaryTrade, Boolean> call(
							TableColumn<ControlReportRulesBinaryTrade, Boolean> p) {
						return new ModifyButtonCell();
					}
				});
		rulestable.getColumns().add(modifybutton);

		deletebutton.setSortable(false);
		deletebutton.setCellValueFactory(new PropertyValueFactory<>("buttons1"));
		deletebutton.setPrefWidth(85);
		deletebutton.setResizable(false);
		deletebutton.setCellFactory(
				new Callback<TableColumn<ControlReportRulesBinaryTrade, Boolean>, TableCell<ControlReportRulesBinaryTrade, Boolean>>() {
					@Override
					public TableCell<ControlReportRulesBinaryTrade, Boolean> call(
							TableColumn<ControlReportRulesBinaryTrade, Boolean> p) {
						return new DeleteButtonCell();
					}
				});
		rulestable.getColumns().add(deletebutton);

		sdbbox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT DB")) {
						tablelist = new DAO().getQAServerTables(newFruit);
						if (tablelist != null && tablelist.size() > 0) {
							stablebox.getItems().clear();
							stablebox.getItems().add("SELECT TABLE");
							stablebox.getSelectionModel().select(0);
							for (int i = 0; i < tablelist.size(); i++) {
								stablebox.getItems().add(tablelist.get(i).getTablenames());
							}
						}
					}
				}
			}
		});

		stdbbox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT DB")) {
						tablelist = new DAO().getQAServerTables(newFruit);
						if (tablelist != null && tablelist.size() > 0) {
							sttablebox.getItems().clear();
							sttablebox.getItems().add("SELECT TABLE");
							sttablebox.getSelectionModel().select(0);
							for (int i = 0; i < tablelist.size(); i++) {
								sttablebox.getItems().add(tablelist.get(i).getTablenames());
							}
						}
					}
				}
			}
		});

		trdbbox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT DB")) {
						tablelist = new DAO().getQAServerTables(newFruit);
						if (tablelist != null && tablelist.size() > 0) {
							trtablebox.getItems().clear();
							trtablebox.getItems().add("SELECT TABLE");
							trtablebox.getSelectionModel().select(0);
							for (int i = 0; i < tablelist.size(); i++) {
								trtablebox.getItems().add(tablelist.get(i).getTablenames());
							}
						}
					}
				}
			}
		});

		ldbbox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT DB")) {
						tablelist = new DAO().getQAServerTables(newFruit);
						if (tablelist != null && tablelist.size() > 0) {
							ltablebox.getItems().clear();
							ltablebox.getItems().add("SELECT TABLE");
							ltablebox.getSelectionModel().select(0);
							for (int i = 0; i < tablelist.size(); i++) {
								ltablebox.getItems().add(tablelist.get(i).getTablenames());
							}
						}
					}
				}
			}
		});

		tdbbox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT DB")) {
						tablelist = new DAO().getQAServerTables(newFruit);
						if (tablelist != null && tablelist.size() > 0) {
							ttablebox.getItems().clear();
							ttablebox.getItems().add("SELECT TABLE");
							ttablebox.getSelectionModel().select(0);
							for (int i = 0; i < tablelist.size(); i++) {
								ttablebox.getItems().add(tablelist.get(i).getTablenames());
							}
						}
					}
				}
			}
		});

		stablebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT TABLE")) {
						columnlist = new DAO().getQAServerColumns(newFruit,
								sdbbox.getSelectionModel().getSelectedItem());
						if (columnlist != null && columnlist.size() > 0) {
							scolumnbox.getItems().clear();
							scolumnbox.getItems().add("SELECT COLUMN");
							scolumnbox.getSelectionModel().select(0);
							for (int i = 0; i < columnlist.size(); i++) {
								scolumnbox.getItems().add(columnlist.get(i).getColumnnames());
							}
						}
					}
				}
			}
		});

		sttablebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT TABLE")) {
						columnlist = new DAO().getQAServerColumns(newFruit,
								stdbbox.getSelectionModel().getSelectedItem());
						if (columnlist != null && columnlist.size() > 0) {
							stcolumnbox.getItems().clear();
							stcolumnbox.getItems().add("SELECT COLUMN");
							stcolumnbox.getSelectionModel().select(0);
							for (int i = 0; i < columnlist.size(); i++) {
								stcolumnbox.getItems().add(columnlist.get(i).getColumnnames());
							}
						}
					}
				}
			}
		});

		trtablebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT TABLE")) {
						columnlist = new DAO().getQAServerColumns(newFruit,
								trdbbox.getSelectionModel().getSelectedItem());
						if (columnlist != null && columnlist.size() > 0) {
							trcolumnbox.getItems().clear();
							trcolumnbox.getItems().add("SELECT COLUMN");
							trcolumnbox.getSelectionModel().select(0);
							for (int i = 0; i < columnlist.size(); i++) {
								trcolumnbox.getItems().add(columnlist.get(i).getColumnnames());
							}
						}
					}
				}
			}
		});

		ltablebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT TABLE")) {
						columnlist = new DAO().getQAServerColumns(newFruit,
								ldbbox.getSelectionModel().getSelectedItem());
						if (columnlist != null && columnlist.size() > 0) {
							lcolumnbox.getItems().clear();
							lcolumnbox.getItems().add("SELECT COLUMN");
							lcolumnbox.getSelectionModel().select(0);
							for (int i = 0; i < columnlist.size(); i++) {
								lcolumnbox.getItems().add(columnlist.get(i).getColumnnames());
							}
						}
					}
				}
			}
		});

		ttablebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT TABLE")) {
						columnlist = new DAO().getQAServerColumns(newFruit,
								tdbbox.getSelectionModel().getSelectedItem());
						if (columnlist != null && columnlist.size() > 0) {
							tcolumnbox.getItems().clear();
							tcolumnbox.getItems().add("SELECT COLUMN");
							tcolumnbox.getSelectionModel().select(0);
							for (int i = 0; i < columnlist.size(); i++) {
								tcolumnbox.getItems().add(columnlist.get(i).getColumnnames());
							}
						}
					}
				}
			}
		});

		scolumnbox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT COLUMN")) {
						if (QADefaultServerDetails.servertype.equals("MY SQL")
								|| QADefaultServerDetails.servertype.equals("MSSQL")) {
							sourcecolsqltextarea
									.setText("select sum(" + scolumnbox.getSelectionModel().getSelectedItem()
											+ ") from " + sdbbox.getSelectionModel().getSelectedItem() + "."
											+ stablebox.getSelectionModel().getSelectedItem());
						}
					} else {
						sourcecolsqltextarea.clear();
					}
				}
			}
		});

		stcolumnbox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT COLUMN")) {
						if (QADefaultServerDetails.servertype.equals("MY SQL")
								|| QADefaultServerDetails.servertype.equals("MSSQL")) {
							stagingcolsqltextarea
									.setText("select sum(" + stcolumnbox.getSelectionModel().getSelectedItem()
											+ ") from " + stdbbox.getSelectionModel().getSelectedItem() + "."
											+ sttablebox.getSelectionModel().getSelectedItem());
						}
					} else {
						stagingcolsqltextarea.clear();
					}
				}
			}
		});
		trcolumnbox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT COLUMN")) {
						if (QADefaultServerDetails.servertype.equals("MY SQL")
								|| QADefaultServerDetails.servertype.equals("MSSQL")) {
							transcolsqltextarea
									.setText("select sum(" + trcolumnbox.getSelectionModel().getSelectedItem()
											+ ") from " + trdbbox.getSelectionModel().getSelectedItem() + "."
											+ trtablebox.getSelectionModel().getSelectedItem());
						}
					} else {
						transcolsqltextarea.clear();
					}
				}
			}
		});
		tcolumnbox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("SELECT COLUMN")) {
						if (QADefaultServerDetails.servertype.equals("MY SQL")
								|| QADefaultServerDetails.servertype.equals("MSSQL")) {
							targetcolsqltextarea
									.setText("select sum(" + tcolumnbox.getSelectionModel().getSelectedItem()
											+ ") from " + tdbbox.getSelectionModel().getSelectedItem() + "."
											+ ttablebox.getSelectionModel().getSelectedItem());
						}
					} else {
						targetcolsqltextarea.clear();
					}
				}
			}
		});

		rulenametext.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (rulenametext.getText().length() >= 300) {
						rulenametext.setText(rulenametext.getText().substring(0, 300));
					}
				}
			}
		});

		searchtext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String enteredString = searchtext.getText().toString();
				if (enteredString != null && !enteredString.isEmpty()) {
					if (enteredString.length() >= 1) {
						@SuppressWarnings("unchecked")
						ArrayList<ControlReportRulesBinaryTrade> filteredData = filterByRules(ruleslist, enteredString);
						populateTable(filteredData);
					} else if (enteredString != null && enteredString.length() == 0) {
						populateTable(ruleslist);
					}
				}
			}
		});

		sourcevalidicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				sourceanchor.setVisible(true);
			}
		});
		sourcecloseicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				sourceanchor.setVisible(false);
			}
		});
		sourcecolicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				sourcecolanchor.setVisible(true);
			}
		});
		sourcecolcloseicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				sourcecolanchor.setVisible(false);
			}
		});
		stagingvalidicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				staginganchor.setVisible(true);
			}
		});
		stagingcloseicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				staginganchor.setVisible(false);
			}
		});
		stagingcolicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stagingcolanchor.setVisible(true);
			}
		});
		stagingcolcloseicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stagingcolanchor.setVisible(false);
			}
		});
		transvalidicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				transanchor.setVisible(true);
			}
		});
		transcloseicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				transanchor.setVisible(false);
			}
		});
		transcolicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				transcolanchor.setVisible(true);
			}
		});
		transcolcloseicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				transcolanchor.setVisible(false);
			}
		});
		targetvalidicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				targetanchor.setVisible(true);
			}
		});
		targetcloseicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				targetanchor.setVisible(false);
			}
		});
		targetcolicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				targetcolanchor.setVisible(true);
			}
		});
		targetcolcloseicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				targetcolanchor.setVisible(false);
			}
		});

		srunicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (sourcesqltextarea.getText() != null && !sourcesqltextarea.getText().isEmpty()) {
					test(sourcesqltextarea.getText());
				} else {
					runmessage("Please Enter Script...");
				}
			}
		});
		scolrunicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (sourcecolsqltextarea.getText() != null && !sourcecolsqltextarea.getText().isEmpty()) {
					coltest(sourcecolsqltextarea.getText());
				} else {
					runmessage("Please Enter Script...");
				}
			}
		});
		strunicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (stagingsqltextarea.getText() != null && !stagingsqltextarea.getText().isEmpty()) {
					test(stagingsqltextarea.getText());
				} else {
					runmessage("Please Enter Script...");
				}
			}
		});
		stcolrunicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (stagingcolsqltextarea.getText() != null && !stagingcolsqltextarea.getText().isEmpty()) {
					coltest(stagingcolsqltextarea.getText());
				} else {
					runmessage("Please Enter Script...");
				}
			}
		});
		transrunicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (transsqltextarea.getText() != null && !transsqltextarea.getText().isEmpty()) {
					test(transsqltextarea.getText());
				} else {
					runmessage("Please Enter Script...");
				}
			}
		});
		transcolrunicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (transcolsqltextarea.getText() != null && !transcolsqltextarea.getText().isEmpty()) {
					coltest(transcolsqltextarea.getText());
				} else {
					runmessage("Please Enter Script...");
				}
			}
		});
		trunicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (targetsqltextarea.getText() != null && !targetsqltextarea.getText().isEmpty()) {
					test(targetsqltextarea.getText());
				} else {
					runmessage("Please Enter Script...");
				}
			}
		});
		tcolrunicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (targetcolsqltextarea.getText() != null && !targetcolsqltextarea.getText().isEmpty()) {
					coltest(targetcolsqltextarea.getText());
				} else {
					runmessage("Please Enter Script...");
				}
			}
		});
		// closing screen when clicks on home icon
		homeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(MouseEvent event) {
				Cleanup scu = new Cleanup();
				NewControlReportRulesController nc = new NewControlReportRulesController();
				scu.nullifyStrings(nc);

				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();
				UndecoratorController.getInstance(null);
			}
		});
	}

	// Method used to get data from previous class
	public static NewControlReportRulesController getInstance() {
		return userHome;
	}

	// Runs in Background and updates UI Responsively
	Service<Void> rulelistservice = new Service<Void>() {
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
							try {
								// FX Stuff done here
								populateControlReportRules();
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

	private void setdb() {
		sdbbox.getItems().clear();
		sdbbox.getItems().add("SELECT DB");

		stdbbox.getItems().clear();
		stdbbox.getItems().add("SELECT DB");

		trdbbox.getItems().clear();
		trdbbox.getItems().add("SELECT DB");

		ldbbox.getItems().clear();
		ldbbox.getItems().add("SELECT DB");

		tdbbox.getItems().clear();
		tdbbox.getItems().add("SELECT DB");

		dblist = new DAO().getQAServerDB();
		if (dblist != null && dblist.size() > 0) {
			for (int i = 0; i < dblist.size(); i++) {
				sdbbox.getItems().add(dblist.get(i).getDbnames());
			}

			for (int i = 0; i < dblist.size(); i++) {
				stdbbox.getItems().add(dblist.get(i).getDbnames());
			}

			for (int i = 0; i < dblist.size(); i++) {
				trdbbox.getItems().add(dblist.get(i).getDbnames());
			}

			for (int i = 0; i < dblist.size(); i++) {
				ldbbox.getItems().add(dblist.get(i).getDbnames());
			}

			for (int i = 0; i < dblist.size(); i++) {
				tdbbox.getItems().add(dblist.get(i).getDbnames());
			}
		}

		sdbbox.getSelectionModel().select(0);
		stdbbox.getSelectionModel().select(0);
		trdbbox.getSelectionModel().select(0);
		ldbbox.getSelectionModel().select(0);
		tdbbox.getSelectionModel().select(0);
	}

	private void settables() {
		stablebox.getItems().clear();
		stablebox.getItems().add("SELECT TABLE");

		sttablebox.getItems().clear();
		sttablebox.getItems().add("SELECT TABLE");

		trtablebox.getItems().clear();
		trtablebox.getItems().add("SELECT TABLE");

		ltablebox.getItems().clear();
		ltablebox.getItems().add("SELECT TABLE");

		ttablebox.getItems().clear();
		ttablebox.getItems().add("SELECT TABLE");

		stablebox.getSelectionModel().select(0);
		sttablebox.getSelectionModel().select(0);
		trtablebox.getSelectionModel().select(0);
		ltablebox.getSelectionModel().select(0);
		ttablebox.getSelectionModel().select(0);
	}

	private void setcolumns() {
		scolumnbox.getItems().clear();
		scolumnbox.getItems().add("SELECT COLUMN");

		stcolumnbox.getItems().clear();
		stcolumnbox.getItems().add("SELECT COLUMN");

		trcolumnbox.getItems().clear();
		trcolumnbox.getItems().add("SELECT COLUMN");

		lcolumnbox.getItems().clear();
		lcolumnbox.getItems().add("SELECT COLUMN");

		tcolumnbox.getItems().clear();
		tcolumnbox.getItems().add("SELECT COLUMN");

		scolumnbox.getSelectionModel().select(0);
		stcolumnbox.getSelectionModel().select(0);
		trcolumnbox.getSelectionModel().select(0);
		lcolumnbox.getSelectionModel().select(0);
		tcolumnbox.getSelectionModel().select(0);
	}

	// Code to control Modules Combo Box
	private void setexistingmodules() {
		modulebox.getItems().clear();
		modulebox.getItems().add("QA Modules");
		modulebox.getSelectionModel().select(0);
		moduleslist = new DAO().getModuleDetails("modules");
		if (moduleslist != null && moduleslist.size() > 0) {
			for (int i = 0; i < moduleslist.size(); i++) {
				modulebox.getItems().add(moduleslist.get(i).getModulename());
			}
		}
		modulebox.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
	}

	@FXML
	private void createrule() {
		if (validateselections()) {

			String result = new DAO().createControlReportrules("controlreportrules",
					modulebox.getSelectionModel().getSelectedItem(), rulenametext.getText(),
					sdbbox.getSelectionModel().getSelectedItem(), stablebox.getSelectionModel().getSelectedItem(),
					scolumnbox.getSelectionModel().getSelectedItem(), stdbbox.getSelectionModel().getSelectedItem(),
					sttablebox.getSelectionModel().getSelectedItem(), stcolumnbox.getSelectionModel().getSelectedItem(),
					trdbbox.getSelectionModel().getSelectedItem(), trtablebox.getSelectionModel().getSelectedItem(),
					trcolumnbox.getSelectionModel().getSelectedItem(), ldbbox.getSelectionModel().getSelectedItem(),
					ltablebox.getSelectionModel().getSelectedItem(), lcolumnbox.getSelectionModel().getSelectedItem(),
					tdbbox.getSelectionModel().getSelectedItem(), ttablebox.getSelectionModel().getSelectedItem(),
					tcolumnbox.getSelectionModel().getSelectedItem(), sourcesqltextarea.getText(),
					stagingsqltextarea.getText(), transsqltextarea.getText(), targetsqltextarea.getText(),
					Long.parseLong(Integer.toString(Loggedinuserdetails.id)), sourcecolsqltextarea.getText(),
					stagingcolsqltextarea.getText(), transcolsqltextarea.getText(), targetcolsqltextarea.getText());

			if (result.equals("success")) {
				setdefaults();
				rulelistservice.reset();
				rulelistservice.start();

				runmessage("Successfully Rule Created...");
			} else if (result.equals("duplicate")) {
				runmessage("Given Rule Name Already Exists. Please Try New Rule Name...");
			} else {
				runmessage("Failed to Create Control Report Rule, Please Try Again...");
			}
		}
	}

	@FXML
	private void updaterule() {
		if (validateselections() && ruleid != null) {
			String result = new DAO().updateControlReportRule(modulebox.getSelectionModel().getSelectedItem(),
					rulenametext.getText(), sdbbox.getSelectionModel().getSelectedItem(),
					stablebox.getSelectionModel().getSelectedItem(), scolumnbox.getSelectionModel().getSelectedItem(),
					stdbbox.getSelectionModel().getSelectedItem(), sttablebox.getSelectionModel().getSelectedItem(),
					stcolumnbox.getSelectionModel().getSelectedItem(), trdbbox.getSelectionModel().getSelectedItem(),
					trtablebox.getSelectionModel().getSelectedItem(), trcolumnbox.getSelectionModel().getSelectedItem(),
					ldbbox.getSelectionModel().getSelectedItem(), ltablebox.getSelectionModel().getSelectedItem(),
					lcolumnbox.getSelectionModel().getSelectedItem(), tdbbox.getSelectionModel().getSelectedItem(),
					ttablebox.getSelectionModel().getSelectedItem(), tcolumnbox.getSelectionModel().getSelectedItem(),
					sourcesqltextarea.getText(), stagingsqltextarea.getText(), transsqltextarea.getText(),
					targetsqltextarea.getText(), Long.parseLong(Integer.toString(Loggedinuserdetails.id)), ruleid,
					sourcecolsqltextarea.getText(), stagingcolsqltextarea.getText(), transcolsqltextarea.getText(),
					targetcolsqltextarea.getText());

			if (result.equals("success")) {
				setdefaults();
				rulelistservice.reset();
				rulelistservice.start();

				ruleid = null;

				runmessage("Successfully Rule Updated...");
			} else {
				runmessage("Failed to Update Control Report Rule, Please Try Again...");
			}
		}
	}

	// Method to display the Messages
	public void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	private boolean validateselections() {
		boolean result = true;
		if (sdbbox.getSelectionModel().getSelectedIndex() == 0) {
			runmessage("Please Select Source Database...");
			result = false;
		} else if (stablebox.getSelectionModel().getSelectedIndex() == 0) {
			runmessage("Please Select Source Table...");
			result = false;
		} else if (sourcesqltextarea.getText() == null || sourcesqltextarea.getText().isEmpty()) {
			runmessage("Provide Script to cross check valid records migrated from Source to Staging...");
			result = false;
		} else if (scolumnbox.getSelectionModel().getSelectedIndex() > 0
				&& (sourcecolsqltextarea.getText() == null || sourcecolsqltextarea.getText().isEmpty())) {
			runmessage("Provide Script to cross check Column Value migrated from Source to Staging...");
			result = false;
		} else if (stdbbox.getSelectionModel().getSelectedIndex() == 0) {
			runmessage("Please Select Staging Database...");
			result = false;
		} else if (sttablebox.getSelectionModel().getSelectedIndex() == 0) {
			runmessage("Please Select Staging Table...");
			result = false;
		} else if (stagingsqltextarea.getText() == null || stagingsqltextarea.getText().isEmpty()) {
			runmessage("Provide Script to cross check valid records migrated from Staging to Transformation...");
			result = false;
		} else if (stcolumnbox.getSelectionModel().getSelectedIndex() > 0
				&& (stagingcolsqltextarea.getText() == null || stagingcolsqltextarea.getText().isEmpty())) {
			runmessage("Provide Script to cross check Column Value migrated from Staging to Transformation...");
			result = false;
		} else if (trdbbox.getSelectionModel().getSelectedIndex() == 0) {
			runmessage("Please Select Transformation Database...");
			result = false;
		} else if (trtablebox.getSelectionModel().getSelectedIndex() == 0) {
			runmessage("Please Select Transformation Table...");
			result = false;
		} else if (transsqltextarea.getText() == null || transsqltextarea.getText().isEmpty()) {
			runmessage("Provide Script to cross check valid records migrated from Transformation to Loading...");
			result = false;
		} else if (trcolumnbox.getSelectionModel().getSelectedIndex() > 0
				&& (transcolsqltextarea.getText() == null || transcolsqltextarea.getText().isEmpty())) {
			runmessage("Provide Script to cross check Column Value migrated from Transformation to Loading...");
			result = false;
		} else if (ldbbox.getSelectionModel().getSelectedIndex() == 0) {
			runmessage("Please Select Loading Database...");
			result = false;
		} else if (ltablebox.getSelectionModel().getSelectedIndex() == 0) {
			runmessage("Please Select Loading Table...");
			result = false;
		} else if (targetsqltextarea.getText() == null || targetsqltextarea.getText().isEmpty()) {
			runmessage("Provide Script to cross check valid records migrated from Loading to Target...");
			result = false;
		} else if (lcolumnbox.getSelectionModel().getSelectedIndex() > 0
				&& (targetcolsqltextarea.getText() == null || targetcolsqltextarea.getText().isEmpty())) {
			runmessage("Provide Script to cross check Column Value migrated from Loading to Target...");
			result = false;
		} else if (tdbbox.getSelectionModel().getSelectedIndex() == 0) {
			runmessage("Please Select Target Database...");
			result = false;
		} else if (ttablebox.getSelectionModel().getSelectedIndex() == 0) {
			runmessage("Please Select Target Table...");
			result = false;
		} else if (modulebox.getSelectionModel().getSelectedIndex() == 0) {
			runmessage("Please Select QA Module...");
			result = false;
		} else if (rulenametext.getText() == null || rulenametext.getText().isEmpty()) {
			runmessage("Please Specify Rule Name...");
			result = false;
		} else if (scolumnbox.getSelectionModel().getSelectedIndex() != 0) {
			if (stcolumnbox.getSelectionModel().getSelectedIndex() == 0) {
				runmessage("Please Select Staging Column...");
				result = false;
			} else if (trcolumnbox.getSelectionModel().getSelectedIndex() == 0) {
				runmessage("Please Select Transformation Column...");
				result = false;
			} else if (lcolumnbox.getSelectionModel().getSelectedIndex() == 0) {
				runmessage("Please Select Loading Column...");
				result = false;
			} else if (tcolumnbox.getSelectionModel().getSelectedIndex() == 0) {
				runmessage("Please Select Target Column...");
				result = false;
			}
		}

		return result;
	}

	private void populateControlReportRules() {
		try {
			ruleslist = new DAO().getControlReportRules(null);
			if (ruleslist == null || ruleslist.size() == 0) {
				searchtext.setDisable(true);
				removerulesfromtable();
			} else {
				searchtext.setDisable(false);
				populateTable(ruleslist);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateTable(ArrayList<ControlReportRulesBinaryTrade> arrayListData) {
		if (arrayListData != null && arrayListData.size() > 0) {
			ObservableList<ControlReportRulesBinaryTrade> data = FXCollections.observableArrayList();

			for (int i = 0; i < arrayListData.size(); i++) {
				ControlReportRulesBinaryTrade crbt = arrayListData.get(i);
				crbt.setId(arrayListData.get(i).getId());
				crbt.setName(arrayListData.get(i).getName());
				crbt.setModule(arrayListData.get(i).getModule());
				crbt.setSdb(arrayListData.get(i).getSdb());
				crbt.setStable(arrayListData.get(i).getStable());
				crbt.setScolumn(arrayListData.get(i).getScolumn());
				crbt.setStdb(arrayListData.get(i).getStdb());
				crbt.setSttable(arrayListData.get(i).getSttable());
				crbt.setStcolumn(arrayListData.get(i).getStcolumn());
				crbt.setTrdb(arrayListData.get(i).getTrdb());
				crbt.setTrtable(arrayListData.get(i).getTrtable());
				crbt.setTrcolumn(arrayListData.get(i).getTrcolumn());
				crbt.setLdb(arrayListData.get(i).getLdb());
				crbt.setLtable(arrayListData.get(i).getLtable());
				crbt.setLcolumn(arrayListData.get(i).getLcolumn());
				crbt.setTdb(arrayListData.get(i).getTdb());
				crbt.setTtable(arrayListData.get(i).getTtable());
				crbt.setTcolumn(arrayListData.get(i).getTcolumn());

				data.add(crbt);
				rulestable.setItems(data);
				rulestable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			}
			rulestable.refresh();

		} else {
			removerulesfromtable();
			CommonFunctions.message = "No Control Report Rules Found...";
			CommonFunctions.invokeAlertBox(getClass());
		}
	}

	@SuppressWarnings("rawtypes")
	private ArrayList filterByRules(ArrayList<ControlReportRulesBinaryTrade> unFiltered, String str) {

		ArrayList<ControlReportRulesBinaryTrade> expens = new ArrayList<ControlReportRulesBinaryTrade>();
		for (ControlReportRulesBinaryTrade bean : unFiltered) {
			if (bean.getName() != null && bean.getName().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			} else if (bean.getModule() != null && bean.getModule().toLowerCase().contains(str.toLowerCase())) {
				expens.add(bean);
			}
		}
		return expens;
	}

	private void removerulesfromtable() {
		if (rulestable != null) {
			for (int i = rulestable.getItems().size() - 1; i >= 0; i--) {
				rulestable.getItems().remove(i);
			}
		}
	}

	private void test(String script) {
		String result = new DAO().getTestResult(script);
		if (result.equals("conerror")) {
			runmessage("Unable to Connect to the QA Server. Please check QA Server settings...");
		} else if (result.equals("noserver")) {
			runmessage("Unable to find default QA Server. Please check QA Server settings...");
		} else if (result.equals("runerror")) {
			runmessage("Runtime Error. Please Try Again...");
		} else if (result.matches("\\d+")) {
			showresult(result, "Success", "Valid Row Count");
		} else {
			showresult("", "FAILED", result);
		}
	}

	private void coltest(String script) {
		String result = new DAO().getTestResult(script);
		if (result.equals("conerror")) {
			runmessage("Unable to Connect to the QA Server. Please check QA Server settings...");
		} else if (result.equals("noserver")) {
			runmessage("Unable to find default QA Server. Please check QA Server settings...");
		} else if (result.equals("runerror")) {
			runmessage("Runtime Error. Please Try Again...");
		} else if (result.matches("\\d+(\\.\\d+)?")) {
			showresult(result, "Success", "Column Value");
		}
	}

	// Method to display the Results
	public void showresult(String result, String status, String message) {
		CommonFunctions.queryresult = result;
		CommonFunctions.teststatus = status;
		CommonFunctions.message = message;
		CommonFunctions.invokeTestResultsDialog(getClass());
	}

	private void setdefaults() {
		sdbbox.getSelectionModel().select(0);
		stablebox.getSelectionModel().select(0);
		scolumnbox.getSelectionModel().select(0);
		stdbbox.getSelectionModel().select(0);
		sttablebox.getSelectionModel().select(0);
		stcolumnbox.getSelectionModel().select(0);
		trdbbox.getSelectionModel().select(0);
		trtablebox.getSelectionModel().select(0);
		trcolumnbox.getSelectionModel().select(0);
		ldbbox.getSelectionModel().select(0);
		ltablebox.getSelectionModel().select(0);
		lcolumnbox.getSelectionModel().select(0);
		tdbbox.getSelectionModel().select(0);
		ttablebox.getSelectionModel().select(0);
		tcolumnbox.getSelectionModel().select(0);
		modulebox.getSelectionModel().select(0);
		sourcesqltextarea.clear();
		stagingsqltextarea.clear();
		transsqltextarea.clear();
		targetsqltextarea.clear();
		sourcecolsqltextarea.clear();
		stagingcolsqltextarea.clear();
		transcolsqltextarea.clear();
		targetcolsqltextarea.clear();
		rulenametext.clear();

		updaterule.setVisible(false);
		createrule.setVisible(true);
	}

	public class ModifyButtonCell extends TableCell<ControlReportRulesBinaryTrade, Boolean> {
		final Button cellButton = new Button("Modify");

		ModifyButtonCell() {
			cellButton.setStyle(
					"-fx-background-color: linear-gradient(#277CD2, #0C23EA); -fx-background-radius: 25; -fx-background-insets: 0; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-clor: white;");
			cellButton.setPrefWidth(85);
			cellButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent t) {
					ControlReportRulesBinaryTrade crrbt = rulestable.getItems().get(getIndex());
					ruleid = crrbt.getId();
					sdbbox.getSelectionModel().select(crrbt.getSdb());
					stablebox.getSelectionModel().select(crrbt.getStable());
					scolumnbox.getSelectionModel().select(crrbt.getScolumn());
					stdbbox.getSelectionModel().select(crrbt.getStdb());
					sttablebox.getSelectionModel().select(crrbt.getSttable());
					stcolumnbox.getSelectionModel().select(crrbt.getStcolumn());
					trdbbox.getSelectionModel().select(crrbt.getTrdb());
					trtablebox.getSelectionModel().select(crrbt.getTrtable());
					trcolumnbox.getSelectionModel().select(crrbt.getTrcolumn());
					ldbbox.getSelectionModel().select(crrbt.getLdb());
					ltablebox.getSelectionModel().select(crrbt.getLtable());
					lcolumnbox.getSelectionModel().select(crrbt.getLcolumn());
					tdbbox.getSelectionModel().select(crrbt.getTdb());
					ttablebox.getSelectionModel().select(crrbt.getTtable());
					tcolumnbox.getSelectionModel().select(crrbt.getTcolumn());
					modulebox.getSelectionModel().select(crrbt.getModule());
					rulenametext.setText(crrbt.getName());
					sourcesqltextarea.setText(crrbt.getStost());
					stagingsqltextarea.setText(crrbt.getSttotr());
					transsqltextarea.setText(crrbt.getTrtol());
					targetsqltextarea.setText(crrbt.getLtot());
					sourcecolsqltextarea.setText(crrbt.getStostcol());
					stagingcolsqltextarea.setText(crrbt.getSttotrcol());
					transcolsqltextarea.setText(crrbt.getTrtolcol());
					targetcolsqltextarea.setText(crrbt.getLtotcol());
					createrule.setVisible(false);
					updaterule.setVisible(true);
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

	public class DeleteButtonCell extends TableCell<ControlReportRulesBinaryTrade, Boolean> {
		final Button cellButton = new Button("Delete");

		DeleteButtonCell() {
			cellButton.setStyle(
					"-fx-background-color: linear-gradient(#FA3F3F, #F8340D); -fx-background-radius: 25; -fx-background-insets: 0; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-clor: white;");
			cellButton.setPrefWidth(85);
			cellButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent t) {
					ControlReportRulesBinaryTrade person = rulestable.getItems().get(getIndex());
					runmessage("Please confirm to Delete Rule :" + person.getName() + ".\n\n"
							+ "Attention: Rule will be permanently deleted...");

					if (CommonFunctions.selectionstatus == "yes") {
						String status = new DAO().delete("controlreportrules", "id", person.getId());
						if (status.equals("success")) {
							runmessage("Successfully Rule was deleted...");
							rulelistservice.reset();
							rulelistservice.start();
						} else {
							runmessage("Failed to delete rule. Please Try Again...");
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
