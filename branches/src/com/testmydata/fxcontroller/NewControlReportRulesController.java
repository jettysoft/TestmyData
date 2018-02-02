package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.testmydata.binarybeans.ControlReportHelperBinaryTrade;
import com.testmydata.binarybeans.ControlReportRulesBinaryTrade;
import com.testmydata.binarybeans.ModulesBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.GetDBTables;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.QADefaultServerDetails;
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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;

public class NewControlReportRulesController implements Initializable {

	private static NewControlReportRulesController userHome = null;
	Stage myStage;
	@FXML
	private ImageView closeicon, sourcevalidicon, stagingvalidicon, transvalidicon, targetvalidicon, sourcecloseicon,
			stagingcloseicon, transcloseicon, targetcloseicon, srunicon, strunicon, transrunicon, trunicon,
			sourcecolicon, stagingcolicon, transcolicon, targetcolicon, sourcecolcloseicon, scolrunicon,
			stagingcolcloseicon, stcolrunicon, transcolcloseicon, transcolrunicon, targetcolcloseicon, tcolrunicon,
			saveicon, updateicon, refreshicon;
	@FXML
	private Label refreshlbl;
	@FXML
	private JFXTabPane rulestab;
	@FXML
	private JFXComboBox<String> sdbbox, stablebox, scolumnbox, stdbbox, sttablebox, stcolumnbox, trdbbox, trtablebox,
			trcolumnbox, ldbbox, ltablebox, lcolumnbox, tdbbox, ttablebox, tcolumnbox, modulebox;
	@FXML
	private JFXTextField rulenametext, searchtext;
	@FXML
	private JFXTextArea sourcesqltextarea, stagingsqltextarea, transsqltextarea, targetsqltextarea,
			sourcecolsqltextarea, stagingcolsqltextarea, transcolsqltextarea, targetcolsqltextarea;

	@FXML
	private AnchorPane controlpane, actionanchor1, actionanchor2, sourceanchor, staginganchor, transanchor,
			targetanchor, etlanchor, sourcecolanchor, stagingcolanchor, transcolanchor, targetcolanchor;
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

	private static String ruleid = null;
	static Rectangle s1, st1, tl1, lt1, c1, cst1, ctl1, clt1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setdb();
		settables();
		setcolumns();
		setexistingmodules();
		rulelistservice.reset();
		rulelistservice.start();
		updateicon.setVisible(false);

		closeicon.setImage(StaticImages.closeicon);
		saveicon.setImage(StaticImages.save);
		updateicon.setImage(StaticImages.save);
		refreshicon.setImage(StaticImages.refresh);

		sourcevalidicon.setImage(StaticImages.sqleditor);
		stagingvalidicon.setImage(StaticImages.sqleditor);
		transvalidicon.setImage(StaticImages.sqleditor);
		targetvalidicon.setImage(StaticImages.sqleditor);

		sourcecolicon.setImage(StaticImages.sqlcolumneditor);
		stagingcolicon.setImage(StaticImages.sqlcolumneditor);
		transcolicon.setImage(StaticImages.sqlcolumneditor);
		targetcolicon.setImage(StaticImages.sqlcolumneditor);

		sourcecloseicon.setImage(StaticImages.closeicon);
		stagingcloseicon.setImage(StaticImages.closeicon);
		transcloseicon.setImage(StaticImages.closeicon);
		targetcloseicon.setImage(StaticImages.closeicon);
		sourcecolcloseicon.setImage(StaticImages.closeicon);
		stagingcolcloseicon.setImage(StaticImages.closeicon);
		transcolcloseicon.setImage(StaticImages.closeicon);
		targetcolcloseicon.setImage(StaticImages.closeicon);

		srunicon.setImage(StaticImages.source_execute);
		strunicon.setImage(StaticImages.source_execute);
		transrunicon.setImage(StaticImages.source_execute);
		trunicon.setImage(StaticImages.source_execute);
		scolrunicon.setImage(StaticImages.source_execute);
		stcolrunicon.setImage(StaticImages.source_execute);
		transcolrunicon.setImage(StaticImages.source_execute);
		tcolrunicon.setImage(StaticImages.source_execute);

		s1 = getdesign();
		st1 = getdesign();
		tl1 = getdesign();
		lt1 = getdesign();
		c1 = getdesign();
		cst1 = getdesign();
		ctl1 = getdesign();
		clt1 = getdesign();

		s1.setLayoutX(208);
		s1.setLayoutY(113);
		etlanchor.getChildren().add(s1);
		s1.setVisible(false);

		st1.setLayoutX(448);
		st1.setLayoutY(113);
		etlanchor.getChildren().add(st1);
		st1.setVisible(false);

		tl1.setLayoutX(688);
		tl1.setLayoutY(113);
		etlanchor.getChildren().add(tl1);
		tl1.setVisible(false);

		lt1.setLayoutX(928);
		lt1.setLayoutY(113);
		etlanchor.getChildren().add(lt1);
		lt1.setVisible(false);

		c1.setLayoutX(208);
		c1.setLayoutY(163);
		etlanchor.getChildren().add(c1);
		c1.setVisible(false);

		cst1.setLayoutX(448);
		cst1.setLayoutY(163);
		etlanchor.getChildren().add(cst1);
		cst1.setVisible(false);

		ctl1.setLayoutX(688);
		ctl1.setLayoutY(163);
		etlanchor.getChildren().add(ctl1);
		ctl1.setVisible(false);

		clt1.setLayoutX(928);
		clt1.setLayoutY(163);
		etlanchor.getChildren().add(clt1);
		clt1.setVisible(false);

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
				populateControlReportRules();
			}
		});

		Label lbl = new Label(" Source-Staging Row Count Validation ");
		lbl.setStyle(StaticImages.lblStyle);
		lbl.setMinWidth(170);
		lbl.setLayoutX(240);
		lbl.setLayoutY(115);
		lbl.setVisible(false);
		etlanchor.getChildren().add(lbl);

		sourcevalidicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				lbl.setVisible(true);
			}
		});
		sourcevalidicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				lbl.setVisible(false);
			}
		});

		Label scollbl = new Label(" Source-Staging Column Sum Validation ");
		scollbl.setStyle(StaticImages.lblStyle);
		scollbl.setMinWidth(150);
		scollbl.setLayoutX(240);
		scollbl.setLayoutY(165);
		scollbl.setVisible(false);
		etlanchor.getChildren().add(scollbl);

		sourcecolicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				scollbl.setVisible(true);
			}
		});
		sourcecolicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				scollbl.setVisible(false);
			}
		});

		Label stlbl = new Label(" Staging-Transformation Row Count Validation ");
		stlbl.setStyle(StaticImages.lblStyle);
		stlbl.setMinWidth(160);
		stlbl.setLayoutX(480);
		stlbl.setLayoutY(115);
		stlbl.setVisible(false);
		etlanchor.getChildren().add(stlbl);

		stagingvalidicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stlbl.setVisible(true);
			}
		});
		stagingvalidicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stlbl.setVisible(false);
			}
		});

		Label stcollbl = new Label(" Staging-Transformation Column Sum Validation ");
		stcollbl.setStyle(StaticImages.lblStyle);
		stcollbl.setMinWidth(160);
		stcollbl.setLayoutX(480);
		stcollbl.setLayoutY(165);
		stcollbl.setVisible(false);
		etlanchor.getChildren().add(stcollbl);

		stagingcolicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stcollbl.setVisible(true);
			}
		});
		stagingcolicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stcollbl.setVisible(false);
			}
		});

		Label trtlbl = new Label(" Transformation-Loading Row Count Validation ");
		trtlbl.setStyle(StaticImages.lblStyle);
		trtlbl.setMinWidth(160);
		trtlbl.setLayoutX(720);
		trtlbl.setLayoutY(115);
		trtlbl.setVisible(false);
		etlanchor.getChildren().add(trtlbl);

		transvalidicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				trtlbl.setVisible(true);
			}
		});
		transvalidicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				trtlbl.setVisible(false);
			}
		});

		Label trcoltlbl = new Label(" Transformation-Loading Column Sum Validation ");
		trcoltlbl.setStyle(StaticImages.lblStyle);
		trcoltlbl.setMinWidth(160);
		trcoltlbl.setLayoutX(720);
		trcoltlbl.setLayoutY(165);
		trcoltlbl.setVisible(false);
		etlanchor.getChildren().add(trcoltlbl);

		transcolicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				trcoltlbl.setVisible(true);
			}
		});
		transcolicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				trcoltlbl.setVisible(false);
			}
		});

		Label ttlbl = new Label(" Loading-Target Row Count Validation ");
		ttlbl.setStyle(StaticImages.lblStyle);
		ttlbl.setMinWidth(150);
		ttlbl.setLayoutX(900);
		ttlbl.setLayoutY(150);
		ttlbl.setVisible(false);
		etlanchor.getChildren().add(ttlbl);

		targetvalidicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				ttlbl.setVisible(true);
			}
		});
		targetvalidicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				ttlbl.setVisible(false);
			}
		});

		Label ttcollbl1 = new Label(" Loading-Target Column Sum Validation ");
		ttcollbl1.setStyle(StaticImages.lblStyle);
		ttcollbl1.setMinWidth(150);
		ttcollbl1.setLayoutX(900);
		ttcollbl1.setLayoutY(200);
		ttcollbl1.setVisible(false);
		etlanchor.getChildren().add(ttcollbl1);

		targetcolicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				ttcollbl1.setVisible(true);
			}
		});
		targetcolicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				ttcollbl1.setVisible(false);
			}
		});

		Label savelbl = new Label("   Save ");
		savelbl.setStyle(StaticImages.lblStyle);
		savelbl.setMinWidth(45);
		savelbl.setLayoutX(105);
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
				createrule();
			}
		});

		Label updatelbl = new Label("   Update ");
		updatelbl.setStyle(StaticImages.lblStyle);
		updatelbl.setMinWidth(50);
		updatelbl.setLayoutX(105);
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
				updaterule();
			}
		});

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

		modifybutton.setGraphic(StaticImages.getmodifybutton());
		modifybutton.setText("");
		modifybutton.setSortable(false);
		modifybutton.setCellValueFactory(new PropertyValueFactory<>("buttons"));
		modifybutton.setPrefWidth(30);
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

		deletebutton.setGraphic(StaticImages.getdeletebutton());
		deletebutton.setText("");
		deletebutton.setSortable(false);
		deletebutton.setCellValueFactory(new PropertyValueFactory<>("buttons1"));
		deletebutton.setPrefWidth(30);
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
						if (tablelist != null) {
							tablelist.clear();
						}
						tablelist = GetDBTables.gettablelist(newFruit);
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
						if (tablelist != null) {
							tablelist.clear();
						}
						tablelist = GetDBTables.gettablelist(newFruit);
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
						if (tablelist != null) {
							tablelist.clear();
						}
						tablelist = GetDBTables.gettablelist(newFruit);
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
						if (tablelist != null) {
							tablelist.clear();
						}
						tablelist = GetDBTables.gettablelist(newFruit);
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
						if (tablelist != null) {
							tablelist.clear();
						}
						tablelist = GetDBTables.gettablelist(newFruit);
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
						if (columnlist != null) {
							columnlist.clear();
						}
						columnlist = GetDBTables.getcolumnlist(newFruit, sdbbox.getSelectionModel().getSelectedItem());
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
						if (columnlist != null) {
							columnlist.clear();
						}
						columnlist = GetDBTables.getcolumnlist(newFruit, stdbbox.getSelectionModel().getSelectedItem());
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
						if (columnlist != null) {
							columnlist.clear();
						}
						columnlist = GetDBTables.getcolumnlist(newFruit, trdbbox.getSelectionModel().getSelectedItem());

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
						if (columnlist != null) {
							columnlist.clear();
						}
						columnlist = GetDBTables.getcolumnlist(newFruit, ldbbox.getSelectionModel().getSelectedItem());
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
						if (columnlist != null) {
							columnlist.clear();
						}
						columnlist = GetDBTables.getcolumnlist(newFruit, tdbbox.getSelectionModel().getSelectedItem());

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
				if (enteredString != null) {
					if (enteredString.length() >= 1) {
						@SuppressWarnings("unchecked")
						ArrayList<ControlReportRulesBinaryTrade> filteredData = filterByRules(ruleslist, enteredString);
						populateTable(filteredData);
					} else if (!(enteredString.length() >= 1)) {
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
		closeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {

				AnchorPane pane = (AnchorPane) ((ImageView) t.getSource()).getParent().getParent().getParent();
				pane.getChildren().remove(pane.getChildren().size() - 1);

				NewControlReportRulesController nc = new NewControlReportRulesController();
				Cleanup.nullifyStrings(nc);
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

		if (dblist != null) {
			dblist.clear();
		}
		dblist = GetDBTables.dblist;

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
		moduleslist = new DAO().getModuleDetails("modules", "all", Loggedinuserdetails.defaultproject);
		if (moduleslist != null && moduleslist.size() > 0) {
			for (int i = 0; i < moduleslist.size(); i++) {
				modulebox.getItems().add(moduleslist.get(i).getModulename());
			}
		}
		modulebox.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
	}

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
					stagingcolsqltextarea.getText(), transcolsqltextarea.getText(), targetcolsqltextarea.getText(),
					Loggedinuserdetails.defaultproject);

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

	private Rectangle getdesign() {
		Rectangle r = new Rectangle();
		r.setLayoutX(25);
		r.setLayoutY(25);
		r.setStyle("-fx-fill: transparent; -fx-stroke : red; -fx-stroke-width : 3;");

		return r;
	}

	private boolean validateselections() {

		boolean result = true;
		StringBuffer message = new StringBuffer();
		if (rulenametext.getText() == null || rulenametext.getText().isEmpty()) {
			result = false;
			rulenametext.setUnFocusColor(Color.RED);
			message.append("Please Specify Rule Name...\n");
		} else {
			rulenametext.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (sdbbox.getSelectionModel().getSelectedIndex() == 0) {
			result = false;
			sdbbox.setUnFocusColor(Color.RED);
			message.append("Please Select Source Database...\n");
		} else {
			sdbbox.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (stablebox.getSelectionModel().getSelectedIndex() == 0) {
			result = false;
			stablebox.setUnFocusColor(Color.RED);
			message.append("Please Select Source Table...\n");
		} else {
			stablebox.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (sourcesqltextarea.getText() == null || sourcesqltextarea.getText().isEmpty()) {
			result = false;
			s1.setVisible(true);
			sourcesqltextarea.setUnFocusColor(Color.RED);
			message.append("Provide Script to cross check valid records migrated from Source to Staging...\n");
		} else {
			s1.setVisible(false);
			sourcesqltextarea.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (scolumnbox.getSelectionModel().getSelectedIndex() > 0
				&& (sourcecolsqltextarea.getText() == null || sourcecolsqltextarea.getText().isEmpty())) {
			result = false;
			c1.setVisible(true);
			sourcecolsqltextarea.setUnFocusColor(Color.RED);
			message.append("Provide Script to cross check Column Value migrated from Source to Staging...\n");
		} else {
			c1.setVisible(false);
			sourcecolsqltextarea.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (stdbbox.getSelectionModel().getSelectedIndex() == 0) {
			result = false;
			stdbbox.setUnFocusColor(Color.RED);
			message.append("Please Select Staging Database...\n");
		} else {
			stdbbox.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (sttablebox.getSelectionModel().getSelectedIndex() == 0) {
			result = false;
			sttablebox.setUnFocusColor(Color.RED);
			message.append("Please Select Staging Table...\n");
		} else {
			sttablebox.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (stagingsqltextarea.getText() == null || stagingsqltextarea.getText().isEmpty()) {
			result = false;
			st1.setVisible(true);
			stagingsqltextarea.setUnFocusColor(Color.RED);
			message.append("Provide Script to cross check valid records migrated from Staging to Transformation...\n");
		} else {
			st1.setVisible(false);
			stagingsqltextarea.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (stcolumnbox.getSelectionModel().getSelectedIndex() > 0
				&& (stagingcolsqltextarea.getText() == null || stagingcolsqltextarea.getText().isEmpty())) {
			result = false;
			cst1.setVisible(true);
			stagingcolsqltextarea.setUnFocusColor(Color.RED);
			message.append("Provide Script to cross check Column Value migrated from Staging to Transformation...\n");
		} else {
			cst1.setVisible(false);
			stagingcolsqltextarea.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (trdbbox.getSelectionModel().getSelectedIndex() == 0) {
			result = false;
			trdbbox.setUnFocusColor(Color.RED);
			message.append("Please Select Transformation Database...\n");
		} else {
			trdbbox.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (trtablebox.getSelectionModel().getSelectedIndex() == 0) {
			result = false;
			trtablebox.setUnFocusColor(Color.RED);
			message.append("Please Select Transformation Table...\n");
		} else {
			trtablebox.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (transsqltextarea.getText() == null || transsqltextarea.getText().isEmpty()) {
			result = false;
			transsqltextarea.setUnFocusColor(Color.RED);
			tl1.setVisible(true);
			message.append("Provide Script to cross check valid records migrated from Transformation to Loading...\n");
		} else {
			tl1.setVisible(false);
			transsqltextarea.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (trcolumnbox.getSelectionModel().getSelectedIndex() > 0
				&& (transcolsqltextarea.getText() == null || transcolsqltextarea.getText().isEmpty())) {
			result = false;
			ctl1.setVisible(true);
			transcolsqltextarea.setUnFocusColor(Color.RED);
			message.append("Provide Script to cross check Column Value migrated from Transformation to Loading...\n");
		} else {
			ctl1.setVisible(false);
			transcolsqltextarea.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (ldbbox.getSelectionModel().getSelectedIndex() == 0) {
			result = false;
			ldbbox.setUnFocusColor(Color.RED);
			message.append("Please Select Loading Database...\n");
		} else {
			ldbbox.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (ltablebox.getSelectionModel().getSelectedIndex() == 0) {
			result = false;
			ltablebox.setUnFocusColor(Color.RED);
			message.append("Please Select Loading Table...\n");
		} else {
			ltablebox.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (targetsqltextarea.getText() == null || targetsqltextarea.getText().isEmpty()) {
			result = false;
			targetsqltextarea.setUnFocusColor(Color.RED);
			lt1.setVisible(true);
			message.append("Provide Script to cross check valid records migrated from Loading to Target...\n");
		} else {
			lt1.setVisible(false);
			targetsqltextarea.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (lcolumnbox.getSelectionModel().getSelectedIndex() > 0
				&& (targetcolsqltextarea.getText() == null || targetcolsqltextarea.getText().isEmpty())) {
			result = false;
			clt1.setVisible(true);
			targetcolsqltextarea.setUnFocusColor(Color.RED);
			message.append("Provide Script to cross check Column Value migrated from Loading to Target...\n");
		} else {
			clt1.setVisible(false);
			targetcolsqltextarea.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (tdbbox.getSelectionModel().getSelectedIndex() == 0) {
			result = false;
			tdbbox.setUnFocusColor(Color.RED);
			message.append("Please Select Target Database...\n");
		} else {
			tdbbox.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (ttablebox.getSelectionModel().getSelectedIndex() == 0) {
			result = false;
			ttablebox.setUnFocusColor(Color.RED);
			message.append("Please Select Target Table...\n");
		} else {
			ttablebox.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (modulebox.getSelectionModel().getSelectedIndex() == 0) {
			result = false;
			modulebox.setUnFocusColor(Color.RED);
			message.append("Please Select QA Module...\n");
		} else {
			modulebox.setUnFocusColor(Color.rgb(190, 190, 196));
		}

		if (scolumnbox.getSelectionModel().getSelectedIndex() != 0) {
			if (stcolumnbox.getSelectionModel().getSelectedIndex() == 0) {
				result = false;
				stcolumnbox.setUnFocusColor(Color.RED);
				message.append("Please Select Staging Column...\n");
			} else {
				stcolumnbox.setUnFocusColor(Color.rgb(190, 190, 196));
			}

			if (trcolumnbox.getSelectionModel().getSelectedIndex() == 0) {
				result = false;
				trcolumnbox.setUnFocusColor(Color.RED);
				message.append("Please Select Transformation Column...\n");
			} else {
				trcolumnbox.setUnFocusColor(Color.rgb(190, 190, 196));
			}

			if (lcolumnbox.getSelectionModel().getSelectedIndex() == 0) {
				result = false;
				lcolumnbox.setUnFocusColor(Color.RED);
				message.append("Please Select Loading Column...\n");
			} else {
				lcolumnbox.setUnFocusColor(Color.rgb(190, 190, 196));
			}

			if (tcolumnbox.getSelectionModel().getSelectedIndex() == 0) {
				result = false;
				tcolumnbox.setUnFocusColor(Color.RED);
				message.append("Please Select Target Column...\n");
			} else {
				tcolumnbox.setUnFocusColor(Color.rgb(190, 190, 196));
			}
		}

		if (result == false) {
			runmessage(message.toString());
		}

		return result;
	}

	private void populateControlReportRules() {
		try {
			ruleslist = new DAO().getControlReportRules(null, Loggedinuserdetails.defaultproject);
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

		updateicon.setVisible(false);
		saveicon.setVisible(true);
	}

	public class ModifyButtonCell extends TableCell<ControlReportRulesBinaryTrade, Boolean> {
		final Button cellButton = new Button();
		final ImageView modif_icon = new ImageView();
		StackPane pane = new StackPane();
		Tooltip tp = new Tooltip("Modify");

		ModifyButtonCell() {
			pane.setAlignment(Pos.CENTER);
			tp.setStyle(StaticImages.lblStyle);
			modif_icon.setImage(StaticImages.modify);
			modif_icon.setFitHeight(20.0);
			modif_icon.setFitWidth(20.0);
			cellButton.setMinWidth(20.0);
			cellButton.setMinHeight(20.0);
			cellButton.setStyle("-fx-background-color: transparent");
			Tooltip.install(cellButton, tp);
			pane.getChildren().addAll(modif_icon, cellButton);
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
					saveicon.setVisible(false);
					updateicon.setVisible(true);
					rulestab.getSelectionModel().select(0);
				}
			});
		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(pane);
			}
		}
	}

	public class DeleteButtonCell extends TableCell<ControlReportRulesBinaryTrade, Boolean> {
		final Button cellButton = new Button();
		StackPane pane = new StackPane();
		final ImageView delet_icon = new ImageView();
		Tooltip tp = new Tooltip("Delete");

		DeleteButtonCell() {
			pane.setAlignment(Pos.CENTER);
			tp.setStyle(StaticImages.lblStyle);
			delet_icon.setImage(StaticImages.delete);
			delet_icon.setFitHeight(20);
			delet_icon.setFitWidth(20);
			cellButton.setMinWidth(20.0);
			cellButton.setMinHeight(20.0);
			cellButton.setStyle("-fx-background-color: transparent");
			Tooltip.install(cellButton, tp);
			pane.getChildren().addAll(delet_icon, cellButton);
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
				setGraphic(pane);
			}
		}
	}

}
