package com.testmydata.fxcontroller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXComboBox;
import com.testmydata.binarybeans.TestSuiteBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxutil.UndecoratorController;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.QADefaultServerDetails;
import com.testmydata.util.ReportsDownloader;
import com.testmydata.util.StaticImages;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TestReportsController implements Initializable {

	private static TestReportsController userHome = null;
	Stage myStage;
	@FXML
	private ImageView homeicon, excelicon, pdficon, runicon, excelicon1, pdficon1, runicon1, excelicon11, pdficon11,
			runicon11;
	@FXML
	private AnchorPane selectionanchor, selectionanchor1, selectionanchor11;
	@FXML
	private JFXComboBox<String> testtype, batchid, testormoduleid, testtype1, batchid1, releasecombo, cyclecombo,
			tscombo;

	private static String lblStyle = null;
	private static String[] testtypeslist = { "Select Test", "Test Suite", "Control Reports" };
	private static int downloadno = 0;

	ArrayList<String> batchidlist = new ArrayList<String>();
	ArrayList<String> testotmodulelist = new ArrayList<String>();
	ArrayList<String> reportfieldcolumnlist = new ArrayList<String>();
	ArrayList<String> reportCRcolumnlist = new ArrayList<String>();
	ArrayList<String> testsuitelist = new ArrayList<String>();
	ArrayList<String> batchidlist1 = new ArrayList<String>();
	ArrayList<TestSuiteBinaryTrade> releaselist = new ArrayList<TestSuiteBinaryTrade>();
	ArrayList<TestSuiteBinaryTrade> cyclelist = new ArrayList<TestSuiteBinaryTrade>();
	ArrayList<TestSuiteBinaryTrade> tslist = new ArrayList<TestSuiteBinaryTrade>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		homeicon.setImage(StaticImages.homeicon.getImage());
		excelicon.setImage(StaticImages.excelicon.getImage());
		pdficon.setImage(StaticImages.pdficon.getImage());
		runicon.setImage(StaticImages.source_run.getImage());
		excelicon1.setImage(StaticImages.excelicon.getImage());
		pdficon1.setImage(StaticImages.pdficon.getImage());
		runicon1.setImage(StaticImages.source_run.getImage());
		excelicon11.setImage(StaticImages.excelicon.getImage());
		pdficon11.setImage(StaticImages.pdficon.getImage());
		runicon11.setImage(StaticImages.source_run.getImage());

		lblStyle = "-fx-background-color: linear-gradient(#277CD2, #0C23EA);  -fx-text-alignment :center; -fx-background-radius: 25; -fx-background-insets: 0; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-clor: red;";

		testtype.getItems().clear();
		testtype.getItems().addAll(testtypeslist);
		testtype.getSelectionModel().select(0);
		testtype.setStyle("-fx-text-fill: #000000; -fx-font-weight:bold;");

		testtype1.getItems().clear();
		testtype1.getItems().add("Select Test Suite");
		testtype1.getSelectionModel().select(0);
		testtype1.setStyle("-fx-text-fill: #000000; -fx-font-weight:bold;");

		setdownloadsinvisible();
		setdownloadsinvisible1();
		setdownloadsinvisible11();
		settestsuites();
		setreleasecombo();

		Label excellbl = new Label("  Excel Report");
		excellbl.setStyle(lblStyle);
		excellbl.setMinWidth(85);
		excellbl.setLayoutY(70);
		excellbl.setLayoutX(660);
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
		pdflbl.setLayoutX(700);
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

		Label excellbl1 = new Label("  Excel Report");
		excellbl1.setStyle(lblStyle);
		excellbl1.setMinWidth(85);
		excellbl1.setLayoutY(70);
		excellbl1.setLayoutX(430);
		excellbl1.setVisible(false);
		selectionanchor1.getChildren().add(excellbl1);

		excelicon1.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				excellbl1.setVisible(true);
			}
		});
		excelicon1.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				excellbl1.setVisible(false);
			}
		});

		Label pdflbl1 = new Label("  PDF Report");
		pdflbl1.setStyle(lblStyle);
		pdflbl1.setMinWidth(85);
		pdflbl1.setLayoutY(70);
		pdflbl1.setLayoutX(470);
		pdflbl1.setVisible(false);
		selectionanchor1.getChildren().add(pdflbl1);

		pdficon1.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				pdflbl1.setVisible(true);
			}
		});
		pdficon1.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				pdflbl1.setVisible(false);
			}
		});

		Label excellbl11 = new Label("  Excel Report");
		excellbl11.setStyle(lblStyle);
		excellbl11.setMinWidth(85);
		excellbl11.setLayoutY(70);
		excellbl11.setLayoutX(660);
		excellbl11.setVisible(false);
		selectionanchor11.getChildren().add(excellbl11);

		excelicon11.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				excellbl11.setVisible(true);
			}
		});
		excelicon11.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				excellbl11.setVisible(false);
			}
		});

		Label pdflbl11 = new Label("  PDF Report");
		pdflbl11.setStyle(lblStyle);
		pdflbl11.setMinWidth(85);
		pdflbl11.setLayoutY(70);
		pdflbl11.setLayoutX(700);
		pdflbl11.setVisible(false);
		selectionanchor11.getChildren().add(pdflbl11);

		pdficon11.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				pdflbl11.setVisible(true);
			}
		});
		pdficon11.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				pdflbl11.setVisible(false);
			}
		});

		testtype.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select Test")) {
						setbatchids(newFruit);
					} else {
						setdefaultbatchcombo();
					}
				} catch (NullPointerException ne) {
					ne.printStackTrace();
				}
			}
		});

		batchid.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select Batch ID")) {
						settestormoduleid(newFruit);
						setdownloadsvisible();
					} else {
						setdefaulttestormoduleid();
						setdownloadsinvisible();
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		pdficon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				runicon.setVisible(true);
				if (testtype.getSelectionModel().getSelectedItem().equals("Test Suite")) {
					File ff = new File(new File(".", "/Reports/TestSuites/PDF").getAbsolutePath());
					if (!(ff.exists() && ff.isDirectory())) {
						ff.mkdirs();
					}
					addcolumnsforreport();

					if (testormoduleid.getSelectionModel().getSelectedIndex() == 0) {
						downloadno = 1;
						downloadservice.reset();
						downloadservice.start();
					} else if (testormoduleid.getSelectionModel().getSelectedIndex() > 0) {
						downloadno = 2;
						downloadservice.reset();
						downloadservice.start();
					}
				} else if (testtype.getSelectionModel().getSelectedItem().equals("Control Reports")) {
					File ff = new File(new File(".", "/Reports/ControlReport/PDF").getAbsolutePath());
					if (!(ff.exists() && ff.isDirectory())) {
						ff.mkdirs();
					}
					addcolumnsforpdfreport();

					if (testormoduleid.getSelectionModel().getSelectedIndex() == 0) {
						downloadno = 3;
						downloadservice.reset();
						downloadservice.start();
					} else if (testormoduleid.getSelectionModel().getSelectedIndex() > 0) {
						downloadno = 4;
						downloadservice.reset();
						downloadservice.start();
					}

				}
			}
		});

		excelicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				runicon.setVisible(true);
				if (testtype.getSelectionModel().getSelectedItem().equals("Test Suite")) {
					File ff = new File(new File(".", "/Reports/TestSuites/Excel").getAbsolutePath());
					if (!(ff.exists() && ff.isDirectory())) {
						ff.mkdirs();
					}
					addcolumnsforreport();

					if (testormoduleid.getSelectionModel().getSelectedIndex() == 0) {
						downloadno = 5;
						downloadservice.reset();
						downloadservice.start();
					} else if (testormoduleid.getSelectionModel().getSelectedIndex() > 0) {
						downloadno = 6;
						downloadservice.reset();
						downloadservice.start();
					}

				} else if (testtype.getSelectionModel().getSelectedItem().equals("Control Reports")) {
					File ff = new File(new File(".", "/Reports/ControlReport/Excel").getAbsolutePath());
					if (!(ff.exists() && ff.isDirectory())) {
						ff.mkdirs();
					}
					addcolumnsforexcelreport();

					if (testormoduleid.getSelectionModel().getSelectedIndex() == 0) {
						downloadno = 7;
						downloadservice.reset();
						downloadservice.start();
					} else if (testormoduleid.getSelectionModel().getSelectedIndex() > 0) {
						downloadno = 8;
						downloadservice.reset();
						downloadservice.start();
					}
				}
			}
		});

		testtype1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select Test Suite")) {
						setbatchids1(newFruit);
						setdownloadsvisible1();
					} else {
						setdefaultbatchcombo1();
						setdownloadsinvisible1();
					}
				} catch (NullPointerException ne) {
					ne.printStackTrace();
				}
			}
		});

		pdficon1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				runicon1.setVisible(true);

				File ff = new File(new File(".", "/Reports/TestSuites/PDF").getAbsolutePath());
				if (!(ff.exists() && ff.isDirectory())) {
					ff.mkdirs();
				}
				addcolumnsforreport();

				if (batchid1.getSelectionModel().getSelectedIndex() == 0) {
					downloadno = 9;
					downloadservice.reset();
					downloadservice.start();
				} else if (batchid1.getSelectionModel().getSelectedIndex() > 0) {
					downloadno = 10;
					downloadservice.reset();
					downloadservice.start();
				}

			}
		});

		excelicon1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				runicon1.setVisible(true);

				File ff = new File(new File(".", "/Reports/TestSuites/Excel").getAbsolutePath());
				if (!(ff.exists() && ff.isDirectory())) {
					ff.mkdirs();
				}
				addcolumnsforreport();

				if (batchid1.getSelectionModel().getSelectedIndex() == 0) {
					downloadno = 11;
					downloadservice.reset();
					downloadservice.start();
				} else if (batchid1.getSelectionModel().getSelectedIndex() > 0) {
					downloadno = 12;
					downloadservice.reset();
					downloadservice.start();
				}
			}
		});

		releasecombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select Release")) {
						setcyclecombo(newFruit);
						setdownloadsvisible11();
					} else {
						setdefaultcycle();
						settestsuite();
						setdownloadsinvisible11();
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		cyclecombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				try {
					if (!newFruit.equals("Select Cycle")) {
						settscombo(releasecombo.getSelectionModel().getSelectedItem(), newFruit);
					} else {
						settestsuite();
					}
				} catch (NullPointerException ne) {
				}
			}
		});

		pdficon11.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				runicon11.setVisible(true);

				File ff = new File(new File(".", "/Reports/TestSuites/PDF").getAbsolutePath());
				if (!(ff.exists() && ff.isDirectory())) {
					ff.mkdirs();
				}
				addcolumnsforreport();

				if (cyclecombo.getSelectionModel().getSelectedIndex() == 0
						&& tscombo.getSelectionModel().getSelectedIndex() == 0) {
					downloadno = 13;
					downloadservice.reset();
					downloadservice.start();
				} else if (cyclecombo.getSelectionModel().getSelectedIndex() > 0
						&& tscombo.getSelectionModel().getSelectedIndex() == 0) {
					downloadno = 14;
					downloadservice.reset();
					downloadservice.start();
				} else if (cyclecombo.getSelectionModel().getSelectedIndex() > 0
						&& tscombo.getSelectionModel().getSelectedIndex() > 0) {
					downloadno = 15;
					downloadservice.reset();
					downloadservice.start();
				}

			}
		});

		excelicon11.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				runicon11.setVisible(true);

				File ff = new File(new File(".", "/Reports/TestSuites/Excel").getAbsolutePath());
				if (!(ff.exists() && ff.isDirectory())) {
					ff.mkdirs();
				}
				addcolumnsforreport();

				if (cyclecombo.getSelectionModel().getSelectedIndex() == 0
						&& tscombo.getSelectionModel().getSelectedIndex() == 0) {
					downloadno = 16;
					downloadservice.reset();
					downloadservice.start();
				} else if (cyclecombo.getSelectionModel().getSelectedIndex() > 0
						&& tscombo.getSelectionModel().getSelectedIndex() == 0) {
					downloadno = 17;
					downloadservice.reset();
					downloadservice.start();
				} else if (cyclecombo.getSelectionModel().getSelectedIndex() > 0
						&& tscombo.getSelectionModel().getSelectedIndex() > 0) {
					downloadno = 18;
					downloadservice.reset();
					downloadservice.start();
				}
			}
		});

		// closing screen when clicks on home icon
		homeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(MouseEvent event) {
				Cleanup scu = new Cleanup();
				TestReportsController nc = new TestReportsController();
				scu.nullifyStrings(nc);

				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();
				UndecoratorController.getInstance(null);
			}
		});
	}

	// Method used to get data from previous class
	public static TestReportsController getInstance() {

		return userHome;
	}

	private void setdefaultbatchcombo() {
		batchid.getItems().clear();
		batchid.getItems().add("Select Batch ID");
		batchid.getSelectionModel().select(0);
	}

	private void setbatchids(String type) {
		setdefaultbatchcombo();
		if (batchidlist != null && batchidlist.size() > 0) {
			batchidlist.clear();
		}

		if (type.equals("Test Suite")) {
			batchidlist = new DAO().getbatchids("fieldresults", QADefaultServerDetails.id);
		} else if (type.equals("Control Reports")) {
			batchidlist = new DAO().getbatchids("crresults", QADefaultServerDetails.id);
		}

		if (batchidlist != null && batchidlist.size() > 0) {
			for (int i = 0; i < batchidlist.size(); i++) {
				batchid.getItems().add(batchidlist.get(i).toString());
			}
			batchid.setStyle("-fx-text-fill: #000000; -fx-font-weight:bold;");
		}

	}

	private void setdefaulttestormoduleid() {
		testormoduleid.getItems().clear();
		if (testtype.getSelectionModel().getSelectedItem().equals("Test Suite")) {
			testormoduleid.getItems().add("Select Test Case");
			testormoduleid.getSelectionModel().select(0);
		} else if (testtype.getSelectionModel().getSelectedItem().equals("Control Reports")) {
			testormoduleid.getItems().add("Select Module");
			testormoduleid.getSelectionModel().select(0);
		}
	}

	private void settestormoduleid(String batchid) {
		setdefaulttestormoduleid();
		if (testotmodulelist != null && testotmodulelist.size() > 0) {
			testotmodulelist.clear();
		}

		if (testtype.getSelectionModel().getSelectedItem().equals("Test Suite")) {
			testotmodulelist = new DAO().gettestormodule("fieldresults", batchid);
		} else if (testtype.getSelectionModel().getSelectedItem().equals("Control Reports")) {
			testotmodulelist = new DAO().gettestormodule("crresults", batchid);
		}

		if (testotmodulelist != null && testotmodulelist.size() > 0) {
			for (int i = 0; i < testotmodulelist.size(); i++) {
				testormoduleid.getItems().add(testotmodulelist.get(i).toString());
			}
			testormoduleid.setStyle("-fx-text-fill: #000000; -fx-font-weight:bold;");
		}
	}

	private void settestsuites() {
		testsuitelist = new DAO().gettestsuites();
		if (testsuitelist != null && testsuitelist.size() > 0) {
			for (int i = 0; i < testsuitelist.size(); i++) {
				testtype1.getItems().add(testsuitelist.get(i).toString());
			}
		}
		testtype1.setStyle("-fx-text-fill: #000000; -fx-font-weight:bold;");
	}

	private void setdefaultbatchcombo1() {
		batchid1.getItems().clear();
		batchid1.getItems().add("Select Batch ID");
		batchid1.getSelectionModel().select(0);
	}

	private void setbatchids1(String type) {
		setdefaultbatchcombo1();
		if (batchidlist1 != null && batchidlist1.size() > 0) {
			batchidlist1.clear();
		}
		batchidlist1 = new DAO().getbatchids1(type, QADefaultServerDetails.id);

		if (batchidlist1 != null && batchidlist1.size() > 0) {
			for (int i = 0; i < batchidlist1.size(); i++) {
				batchid1.getItems().add(batchidlist1.get(i).toString());
			}
			batchid1.setStyle("-fx-text-fill: #000000; -fx-font-weight:bold;");
		}

	}

	private void setreleasecombo() {
		releasecombo.getItems().clear();
		releasecombo.getItems().add("Select Release");
		releasecombo.getSelectionModel().select(0);

		releaselist.clear();
		releaselist = new DAO().getreleases("testsuites");
		if (releaselist != null && releaselist.size() > 0) {
			for (int i = 0; i < releaselist.size(); i++) {
				releasecombo.getItems().add(releaselist.get(i).getRelease());
			}
		}
		releasecombo.setStyle("-fx-text-fill: #40aa03; -fx-font-weight:bold;");
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
		tslist = new DAO().gettestsuitesonly(cycle, release);
		if (tslist != null && tslist.size() > 0) {
			for (int i = 0; i < tslist.size(); i++) {
				tscombo.getItems().add(tslist.get(i).getTestsuitename());
			}
		}
		tscombo.setStyle("-fx-text-fill: #0083ff; -fx-font-weight:bold;");
	}

	private void addcolumnsforreport() {
		reportfieldcolumnlist.clear();
		reportfieldcolumnlist.add("T.C ID");
		reportfieldcolumnlist.add("Release");
		reportfieldcolumnlist.add("Cycle");
		reportfieldcolumnlist.add("Test Suite");
		reportfieldcolumnlist.add("Test Case");
		// reportfieldcolumnlist.add("Test Condition");
		reportfieldcolumnlist.add("Query Result");
		reportfieldcolumnlist.add("Status");
	}

	private void addcolumnsforexcelreport() {
		reportCRcolumnlist.clear();
		reportCRcolumnlist.add("Result ID");
		reportCRcolumnlist.add("Date");
		reportCRcolumnlist.add("Executed By");
		reportCRcolumnlist.add("Module");
		reportCRcolumnlist.add("Rule");
		reportCRcolumnlist.add("Message");
		reportCRcolumnlist.add("Source Count");
		reportCRcolumnlist.add("So-Stg Diff");
		reportCRcolumnlist.add("Staging Count");
		reportCRcolumnlist.add("Stg-Trans Diff");
		reportCRcolumnlist.add("Transformation count");
		reportCRcolumnlist.add("Trans-Ldg Diff");
		reportCRcolumnlist.add("Loading Count");
		reportCRcolumnlist.add("Ldg-Trg Diff");
		reportCRcolumnlist.add("Target Count");
		reportCRcolumnlist.add("Source Sum");
		reportCRcolumnlist.add("So-Stg Sum Diff");
		reportCRcolumnlist.add("Staging Sum");
		reportCRcolumnlist.add("Stg-Trans Sum Diff");
		reportCRcolumnlist.add("Transformation Sum");
		reportCRcolumnlist.add("Trans-Ldg Sum Diff");
		reportCRcolumnlist.add("Loading Sum Count");
		reportCRcolumnlist.add("Ldg-Trg Sum Diff");
		reportCRcolumnlist.add("Target Sum Count");
		reportCRcolumnlist.add("Result");
	}

	private void addcolumnsforpdfreport() {
		reportCRcolumnlist.clear();
		reportCRcolumnlist.add("Result ID");
		reportCRcolumnlist.add("Module");
		reportCRcolumnlist.add("Rule");
		// reportCRcolumnlist.add("Source Count");
		// reportCRcolumnlist.add("Staging Count");
		// reportCRcolumnlist.add("Transformation count");
		// reportCRcolumnlist.add("Loading Count");
		// reportCRcolumnlist.add("Target Count");
		// reportCRcolumnlist.add("Source Sum");
		// reportCRcolumnlist.add("Staging Sum");
		// reportCRcolumnlist.add("Transformation Sum");
		// reportCRcolumnlist.add("Loading Sum Count");
		// reportCRcolumnlist.add("Target Sum Count");
		reportCRcolumnlist.add("So-Stg Status");
		reportCRcolumnlist.add("Stg-Trans Status");
		reportCRcolumnlist.add("Trans-Ldg Status");
		reportCRcolumnlist.add("Ldg-Trg Status");
		reportCRcolumnlist.add("Result");
	}

	private String CRreplacer() {
		String reportitems = null;
		StringBuffer reports = new StringBuffer();
		for (int i = 0; i < reportCRcolumnlist.size(); i++) {
			if (reportCRcolumnlist.get(i).equals("Result ID")) {
				reports.append(reportCRcolumnlist.get(i).replace("Result ID", "id,"));
			} else if (reportCRcolumnlist.get(i).equals("Module")) {
				reports.append(reportCRcolumnlist.get(i).replace("Module",
						"(select module from modules where id = moduleid)as module,"));
			} else if (reportCRcolumnlist.get(i).equals("Rule")) {
				reports.append(reportCRcolumnlist.get(i).replace("Rule",
						"(select rulename from controlreportrules where id = ruleid)as rule,"));
			} else if (reportCRcolumnlist.get(i).equals("Message")) {
				reports.append(reportCRcolumnlist.get(i).replace("Message", "message,"));
			} else if (reportCRcolumnlist.get(i).equals("Source Count")) {
				reports.append(reportCRcolumnlist.get(i).replace("Source Count", "sourcecount,"));
			} else if (reportCRcolumnlist.get(i).equals("So-Stg Diff")) {
				reports.append(reportCRcolumnlist.get(i).replace("So-Stg Diff", "stostdiff,"));
			} else if (reportCRcolumnlist.get(i).equals("Staging Count")) {
				reports.append(reportCRcolumnlist.get(i).replace("Staging Count", "stagingcount,"));
			} else if (reportCRcolumnlist.get(i).equals("Stg-Trans Diff")) {
				reports.append(reportCRcolumnlist.get(i).replace("Stg-Trans Diff", "sttotrdiff,"));
			} else if (reportCRcolumnlist.get(i).equals("Transformation count")) {
				reports.append(reportCRcolumnlist.get(i).replace("Transformation count", "transcount,"));
			} else if (reportCRcolumnlist.get(i).equals("Trans-Ldg Diff")) {
				reports.append(reportCRcolumnlist.get(i).replace("Trans-Ldg Diff", "trtoldiff,"));
			} else if (reportCRcolumnlist.get(i).equals("Loading Count")) {
				reports.append(reportCRcolumnlist.get(i).replace("Loading Count", "loadingcount,"));
			} else if (reportCRcolumnlist.get(i).equals("Ldg-Trg Diff")) {
				reports.append(reportCRcolumnlist.get(i).replace("Ldg-Trg Diff", "ltotdiff,"));
			} else if (reportCRcolumnlist.get(i).equals("Target Count")) {
				reports.append(reportCRcolumnlist.get(i).replace("Target Count", "targetcount,"));
			} else if (reportCRcolumnlist.get(i).equals("Source Sum")) {
				reports.append(reportCRcolumnlist.get(i).replace("Source Sum", "sourcecolvalue,"));
			} else if (reportCRcolumnlist.get(i).equals("So-Stg Sum Diff")) {
				reports.append(reportCRcolumnlist.get(i).replace("So-Stg Sum Diff", "stostcoldiff,"));
			} else if (reportCRcolumnlist.get(i).equals("Staging Sum")) {
				reports.append(reportCRcolumnlist.get(i).replace("Staging Sum", "stagingcolvalue,"));
			} else if (reportCRcolumnlist.get(i).equals("Stg-Trans Sum Diff")) {
				reports.append(reportCRcolumnlist.get(i).replace("Stg-Trans Sum Diff", "sttotrcoldiff,"));
			} else if (reportCRcolumnlist.get(i).equals("Transformation Sum")) {
				reports.append(reportCRcolumnlist.get(i).replace("Transformation Sum", "transcolvalue,"));
			} else if (reportCRcolumnlist.get(i).equals("Trans-Ldg Sum Diff")) {
				reports.append(reportCRcolumnlist.get(i).replace("Trans-Ldg Sum Diff", "trtolcoldiff,"));
			} else if (reportCRcolumnlist.get(i).equals("Loading Sum Count")) {
				reports.append(reportCRcolumnlist.get(i).replace("Loading Sum Count", "loadingcolvalue,"));
			} else if (reportCRcolumnlist.get(i).equals("Ldg-Trg Sum Diff")) {
				reports.append(reportCRcolumnlist.get(i).replace("Ldg-Trg Sum Diff", "ltotcoldiff,"));
			} else if (reportCRcolumnlist.get(i).equals("Target Sum Count")) {
				reports.append(reportCRcolumnlist.get(i).replace("Target Sum Count", "targetcolvalue,"));
			} else if (reportCRcolumnlist.get(i).equals("Result")) {
				reports.append(reportCRcolumnlist.get(i).replace("Result", "result,"));
			} else if (reportCRcolumnlist.get(i).equals("Date")) {
				reports.append(reportCRcolumnlist.get(i).replace("Date", "DATE_FORMAT(executeddate, '%Y-%m-%d'),"));
			} else if (reportCRcolumnlist.get(i).equals("Executed By")) {
				reports.append(reportCRcolumnlist.get(i).replace("Executed By",
						"(select userId from users where id = executeduserid)as user,"));
			} else if (reportCRcolumnlist.get(i).equals("So-Stg Status")) {
				reports.append(reportCRcolumnlist.get(i).replace("So-Stg Status", "stost,"));
			} else if (reportCRcolumnlist.get(i).equals("Stg-Trans Status")) {
				reports.append(reportCRcolumnlist.get(i).replace("Stg-Trans Status", "sttotr,"));
			} else if (reportCRcolumnlist.get(i).equals("Trans-Ldg Status")) {
				reports.append(reportCRcolumnlist.get(i).replace("Trans-Ldg Status", "trtol,"));
			} else if (reportCRcolumnlist.get(i).equals("Ldg-Trg Status")) {
				reports.append(reportCRcolumnlist.get(i).replace("Ldg-Trg Status", "ltot,"));
			}

		}
		reportitems = reports.toString();
		reportitems = reportitems.substring(0, reportitems.length() - 1);
		return reportitems;
	}

	private String fieldreplacer() {
		String reportitems = null;
		StringBuffer reports = new StringBuffer();
		for (int i = 0; i < reportfieldcolumnlist.size(); i++) {
			if (reportfieldcolumnlist.get(i).equals("T.C ID")) {
				reports.append(reportfieldcolumnlist.get(i).replace("T.C ID", "tc.id,"));
			} else if (reportfieldcolumnlist.get(i).equals("Release")) {
				reports.append(reportfieldcolumnlist.get(i).replace("Release", "fr.release,"));
			} else if (reportfieldcolumnlist.get(i).equals("Cycle")) {
				reports.append(reportfieldcolumnlist.get(i).replace("Cycle", "fr.cycle,"));
			} else if (reportfieldcolumnlist.get(i).equals("Test Suite")) {
				reports.append(reportfieldcolumnlist.get(i).replace("Test Suite", "ts.suitename,"));
			} else if (reportfieldcolumnlist.get(i).equals("Test Case")) {
				reports.append(reportfieldcolumnlist.get(i).replace("Test Case", "tc.tcname,"));
			} else if (reportfieldcolumnlist.get(i).equals("Test Condition")) {
				reports.append(reportfieldcolumnlist.get(i).replace("Test Condition", "tc.testcondition,"));
			} else if (reportfieldcolumnlist.get(i).equals("Query Result")) {
				reports.append(reportfieldcolumnlist.get(i).replace("Query Result", "fr.queryresult,"));
			} else if (reportfieldcolumnlist.get(i).equals("Status")) {
				reports.append(reportfieldcolumnlist.get(i).replace("Status", "fr.teststatus,"));
			}

		}
		reportitems = reports.toString();
		reportitems = reportitems.substring(0, reportitems.length() - 1);
		return reportitems;
	}

	private void setdownloadsvisible() {
		excelicon.setVisible(true);
		pdficon.setVisible(true);
	}

	private void setdownloadsinvisible() {
		excelicon.setVisible(false);
		pdficon.setVisible(false);
	}

	private void setdownloadsvisible1() {
		excelicon1.setVisible(true);
		pdficon1.setVisible(true);
	}

	private void setdownloadsinvisible1() {
		excelicon1.setVisible(false);
		pdficon1.setVisible(false);
	}

	private void setdownloadsvisible11() {
		excelicon11.setVisible(true);
		pdficon11.setVisible(true);
	}

	private void setdownloadsinvisible11() {
		excelicon11.setVisible(false);
		pdficon11.setVisible(false);
	}

	// Runs in Background and updates UI Responsively
	Service<Void> downloadservice = new Service<Void>() {
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
								ReportsDownloader rd = new ReportsDownloader();
								if (downloadno == 1) {
									rd.download("Test Suite",
											Integer.parseInt(batchid.getSelectionModel().getSelectedItem()),
											"Reports/TestSuites/PDF", "pdf", reportfieldcolumnlist,
											new DAO().getfieldresults(
													Integer.parseInt(batchid.getSelectionModel().getSelectedItem()), 0,
													0, fieldreplacer(), reportfieldcolumnlist.size(), null,
													QADefaultServerDetails.id));
								} else if (downloadno == 2) {
									String[] select = testormoduleid.getSelectionModel().getSelectedItem().split("-");
									rd.download("Test Suite",
											Integer.parseInt(batchid.getSelectionModel().getSelectedItem()),
											"Reports/TestSuites/PDF", "pdf", reportfieldcolumnlist,
											new DAO().getfieldresults(
													Integer.parseInt(batchid.getSelectionModel().getSelectedItem()), 0,
													Integer.parseInt(select[0]), fieldreplacer(),
													reportfieldcolumnlist.size(), null, QADefaultServerDetails.id));
								} else if (downloadno == 3) {
									rd.download("Control Report",
											Integer.parseInt(batchid.getSelectionModel().getSelectedItem()),
											"Reports/ControlReport/PDF", "pdf", reportCRcolumnlist,
											new DAO().getcrresults(
													Integer.parseInt(batchid.getSelectionModel().getSelectedItem()), 0,
													CRreplacer(), reportCRcolumnlist.size(),
													QADefaultServerDetails.id));
								} else if (downloadno == 4) {
									String[] select = testormoduleid.getSelectionModel().getSelectedItem().split("-");
									rd.download("Control Report",
											Integer.parseInt(batchid.getSelectionModel().getSelectedItem()),
											"Reports/ControlReport/PDF", "pdf", reportCRcolumnlist,
											new DAO().getcrresults(
													Integer.parseInt(batchid.getSelectionModel().getSelectedItem()),
													Integer.parseInt(select[0]), CRreplacer(),
													reportCRcolumnlist.size(), QADefaultServerDetails.id));
								} else if (downloadno == 5) {
									rd.download("Test Suite",
											Integer.parseInt(batchid.getSelectionModel().getSelectedItem()),
											"Reports/TestSuites/Excel", "excel", reportfieldcolumnlist,
											new DAO().getfieldresults(
													Integer.parseInt(batchid.getSelectionModel().getSelectedItem()), 0,
													0, fieldreplacer(), reportfieldcolumnlist.size(), null,
													QADefaultServerDetails.id));
								} else if (downloadno == 6) {
									String[] select = testormoduleid.getSelectionModel().getSelectedItem().split("-");
									rd.download("Test Suite",
											Integer.parseInt(batchid.getSelectionModel().getSelectedItem()),
											"Reports/TestSuites/Excel", "excel", reportfieldcolumnlist,
											new DAO().getfieldresults(
													Integer.parseInt(batchid.getSelectionModel().getSelectedItem()), 0,
													Integer.parseInt(select[0]), fieldreplacer(),
													reportfieldcolumnlist.size(), null, QADefaultServerDetails.id));
								} else if (downloadno == 7) {
									rd.download("Control Report",
											Integer.parseInt(batchid.getSelectionModel().getSelectedItem()),
											"Reports/ControlReport/Excel", "excel", reportCRcolumnlist,
											new DAO().getcrresults(
													Integer.parseInt(batchid.getSelectionModel().getSelectedItem()), 0,
													CRreplacer(), reportCRcolumnlist.size(),
													QADefaultServerDetails.id));
								} else if (downloadno == 8) {
									String[] select = testormoduleid.getSelectionModel().getSelectedItem().split("-");
									rd.download("Control Report",
											Integer.parseInt(batchid.getSelectionModel().getSelectedItem()),
											"Reports/ControlReport/Excel", "excel", reportCRcolumnlist,
											new DAO().getcrresults(
													Integer.parseInt(batchid.getSelectionModel().getSelectedItem()),
													Integer.parseInt(select[0]), CRreplacer(),
													reportCRcolumnlist.size(), QADefaultServerDetails.id));
								} else if (downloadno == 9) {
									rd.download("Test Suite",
											Integer.parseInt(batchid1.getSelectionModel().getSelectedItem()),
											"Reports/TestSuites/PDF", "pdf", reportfieldcolumnlist,
											new DAO().getfieldresults(0, 0, 0, fieldreplacer(),
													reportfieldcolumnlist.size(),
													testtype1.getSelectionModel().getSelectedItem(),
													QADefaultServerDetails.id));
								} else if (downloadno == 10) {
									rd.download("Test Suite",
											Integer.parseInt(batchid1.getSelectionModel().getSelectedItem()),
											"Reports/TestSuites/PDF", "pdf", reportfieldcolumnlist,
											new DAO().getfieldresults(
													Integer.parseInt(batchid1.getSelectionModel().getSelectedItem()), 0,
													0, fieldreplacer(), reportfieldcolumnlist.size(),
													testtype1.getSelectionModel().getSelectedItem(),
													QADefaultServerDetails.id));
								} else if (downloadno == 11) {
									rd.download("Test Suite",
											Integer.parseInt(batchid1.getSelectionModel().getSelectedItem()),
											"Reports/TestSuites/Excel", "excel", reportfieldcolumnlist,
											new DAO().getfieldresults(0, 0, 0, fieldreplacer(),
													reportfieldcolumnlist.size(),
													testtype1.getSelectionModel().getSelectedItem(),
													QADefaultServerDetails.id));
								} else if (downloadno == 12) {
									rd.download("Test Suite",
											Integer.parseInt(batchid1.getSelectionModel().getSelectedItem()),
											"Reports/TestSuites/Excel", "excel", reportfieldcolumnlist,
											new DAO().getfieldresults(
													Integer.parseInt(batchid1.getSelectionModel().getSelectedItem()), 0,
													0, fieldreplacer(), reportfieldcolumnlist.size(),
													testtype1.getSelectionModel().getSelectedItem(),
													QADefaultServerDetails.id));
								} else if (downloadno == 13) {
									rd.download("Test Suite", 0, "Reports/TestSuites/PDF", "pdf", reportfieldcolumnlist,
											new DAO().getReleasefieldresults(fieldreplacer(),
													reportfieldcolumnlist.size(),
													releasecombo.getSelectionModel().getSelectedItem(), null, null));
								} else if (downloadno == 14) {
									rd.download("Test Suite", 0, "Reports/TestSuites/PDF", "pdf", reportfieldcolumnlist,
											new DAO().getReleasefieldresults(fieldreplacer(),
													reportfieldcolumnlist.size(),
													releasecombo.getSelectionModel().getSelectedItem(),
													cyclecombo.getSelectionModel().getSelectedItem(), null));
								} else if (downloadno == 15) {
									rd.download("Test Suite", 0, "Reports/TestSuites/PDF", "pdf", reportfieldcolumnlist,
											new DAO().getReleasefieldresults(fieldreplacer(),
													reportfieldcolumnlist.size(),
													releasecombo.getSelectionModel().getSelectedItem(),
													cyclecombo.getSelectionModel().getSelectedItem(),
													tscombo.getSelectionModel().getSelectedItem()));
								} else if (downloadno == 16) {
									rd.download("Test Suite", 0, "Reports/TestSuites/Excel", "excel",
											reportfieldcolumnlist,
											new DAO().getReleasefieldresults(fieldreplacer(),
													reportfieldcolumnlist.size(),
													releasecombo.getSelectionModel().getSelectedItem(), null, null));
								} else if (downloadno == 17) {
									rd.download("Test Suite", 0, "Reports/TestSuites/Excel", "excel",
											reportfieldcolumnlist,
											new DAO().getReleasefieldresults(fieldreplacer(),
													reportfieldcolumnlist.size(),
													releasecombo.getSelectionModel().getSelectedItem(),
													cyclecombo.getSelectionModel().getSelectedItem(), null));
								} else if (downloadno == 18) {
									rd.download("Test Suite", 0, "Reports/TestSuites/Excel", "excel",
											reportfieldcolumnlist,
											new DAO().getReleasefieldresults(fieldreplacer(),
													reportfieldcolumnlist.size(),
													releasecombo.getSelectionModel().getSelectedItem(),
													cyclecombo.getSelectionModel().getSelectedItem(),
													tscombo.getSelectionModel().getSelectedItem()));
								}

							} finally {
								latch.countDown();
							}
						}
					});
					latch.await();

					runicon.setVisible(false);
					runicon1.setVisible(false);
					runicon11.setVisible(false);
					downloadno = 0;
					// Keep with the background work
					return null;
				}
			};
		}
	};
}
