package com.testmydata.fxcontroller;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import com.testmydata.auditlog.StoreAuditLogger;
import com.testmydata.binarybeans.ControlReportExecutionBinaryTrade;
import com.testmydata.binarybeans.LocalUserLevelBeanBinaryTrade;
import com.testmydata.binarybeans.ModulesBinaryTrade;
import com.testmydata.binarybeans.ProjectsBeanBinaryTrade;
import com.testmydata.binarybeans.QAServerDetailsBinaryTrade;
import com.testmydata.binarybeans.TestSuiteBinaryTrade;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.dashboardfunction.ControlReportsExecutionServices;
import com.testmydata.dashboardfunction.Cycle;
import com.testmydata.dashboardfunction.FieldtoFieldExecutionServices;
import com.testmydata.dashboardfunction.Module;
import com.testmydata.dashboardfunction.Release;
import com.testmydata.dashboardfunction.Rule;
import com.testmydata.dashboardfunction.TestSuite;
import com.testmydata.fxhelpers.MenuItemsFXHelper;
import com.testmydata.main.InactivityEventManager;
import com.testmydata.main.InactivityListener;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.DockerClass;
import com.testmydata.util.EncryptAndDecrypt;
import com.testmydata.util.FileIOOperations;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.QADefaultServerDetails;
import com.testmydata.util.ReportsDownloader;
import com.testmydata.util.SchedulerTime;
import com.testmydata.util.StaticImages;
import com.testmydata.vpn.VpnConnectionThread;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.SubScene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashBoardController implements Initializable {
	@FXML
	private FontAwesomeIconView securedfont;
	@FXML
	private JFXButton designbutton, testsuitebutton, testbutton, bugsbutton, reportsbutton, settingsbutton;
	@FXML
	public AnchorPane dashboardanchor, dashpane, selectionpane, resultspane, chartspane, designanchor, testsuiteanchor,
			testanchor, bugsanchor, reportsanchor, settingsanchor, adduseranchor, subscreenanchor;
	@FXML
	private Hyperlink newfieldtofield, newcontrolreport, projectsetup, testsuiteff, exeff, execr, newbugs, viewbugs,
			downloadreports, viewresults, bugserver, changepasswordlink, emailsettingslink, qaserverlink;
	@FXML
	private JFXComboBox<String> exisitingprojectscombo;
	@FXML
	private MenuBar mymenubar;
	@FXML
	private MenuItem controlreport, fieldtofield, runcontrolreport, runfieldtofield, newtestsuite, executetestsuites,
			testreports, adduser, changepassword, emailsettings, featuresallocation, qaserver;
	@FXML
	private Label clientname, companylabel, qaserverlabel, fieldlabel, crlabel, lbl_data_title;
	@FXML
	private JFXListView<Release> list_field;
	@FXML
	private JFXListView<Module> list_control;
	@FXML
	private JFXListView<Cycle> list_data;
	@FXML
	private JFXListView<Rule> list_control_data;
	@FXML
	private ImageView homeicon, logouticon, appicon, testcasesicon, newcricon, newfieldicon, testsuites,
			newtestsuiteicon, testexecution, execricon, exefieldicon, reporticon, testreport, settings, addusericon,
			changepasswordicon, emailsettingsicon, qaservericon;

	public int localUserLevel = 0;
	public Date activatedDate = null;
	Stage myStage;
	SubScene ss;
	private static UsersDetailsBeanBinaryTrade currentUsersDetailsBeanBinaryTree;
	private static DashBoardController userHome = null;
	static String[] selectedproject = null;

	private static int statuspanecount = 0, countforonehour = 0, userLevel = 0;

	private ArrayList<LocalUserLevelBeanBinaryTrade> localUserLevelArrayList = new ArrayList<LocalUserLevelBeanBinaryTrade>();
	ArrayList<QAServerDetailsBinaryTrade> serverlist = new ArrayList<QAServerDetailsBinaryTrade>();
	ArrayList<TestSuiteBinaryTrade> releaselist = new ArrayList<TestSuiteBinaryTrade>();
	ArrayList<TestSuiteBinaryTrade> cyclelist = new ArrayList<TestSuiteBinaryTrade>();
	ArrayList<TestSuiteBinaryTrade> tslist = new ArrayList<TestSuiteBinaryTrade>();
	ArrayList<ModulesBinaryTrade> moduleslist = new ArrayList<ModulesBinaryTrade>();
	ArrayList<ControlReportExecutionBinaryTrade> rulenamelist = new ArrayList<ControlReportExecutionBinaryTrade>();
	static ArrayList<String> reportcolumnlist = new ArrayList<String>();
	static ArrayList<String> crcolumnlist = new ArrayList<String>();
	static ArrayList<ProjectsBeanBinaryTrade> projectslist = new ArrayList<ProjectsBeanBinaryTrade>();

	DecimalFormat df2 = new DecimalFormat("#.##");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// start(); //starts dock icons
		InvoiceStaticHelper.setDash(this);
		// Order is most important
		invokeInactivityListener();
		getlocaluserLevel();
		trailvalidation(localUserLevel);
		QADefaultServerDetails qasd = new QADefaultServerDetails();
		qasd.setqadefaultserver();
		setqaserver();

		setexistingprojects();
		setdashboardpanels();

		homeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				subscreenanchor.setVisible(false);
				subscreenanchor.getChildren().clear();
			}
		});

		exisitingprojectscombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
				if (newFruit != null) {
					if (!newFruit.equals("Select Project")) {
						selectedproject = exisitingprojectscombo.getSelectionModel().getSelectedItem().split("-");
						Loggedinuserdetails.defaultproject = Integer.parseInt(selectedproject[0]);
						new DAO().updatetabledata("users", "defaultproject", selectedproject[0], "id",
								Integer.toString(Loggedinuserdetails.id));
						setdashboardpanels();
					} else {
						selectedproject = "0-0".split("-");
						Loggedinuserdetails.defaultproject = Integer.parseInt(selectedproject[0]);
						new DAO().updatetabledata("users", "defaultproject", selectedproject[0], "id",
								Integer.toString(Loggedinuserdetails.id));
						setdashboardpanels();
					}
				}
			}
		});

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				if (VpnConnectionThread.isVpnConnected()) {
					importantmethods();
					countforonehour++;
					if (countforonehour == 120) {
						countforonehour = 0;
					}
				} else {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								CommonFunctions.message = "Secured Connection Lost. Please Restart Test My Data...!";
								CommonFunctions.invokeAlertBox(getClass());
								timer.cancel();

								StoreAuditLogger.logStoreTransaction(Loggedinuserdetails.userId, "Security", "", "",
										true, "VPN connection lost", "");

								System.exit(0);
							} finally {

							}
						}
					});

				}
			}
		}, 5000, 30000); // First 1000 is to start after 1 sec, Second 60000
		// is to loop for every 1 min (1000 * 60 sec = 60000)

		lbl_data_title.setStyle(
				"-fx-background-color:  #162a4c;-fx-alignment : center;-fx-background-radius: 1em;-fx-font-size: 14pt;-fx-font-family: \"Calibri\";-fx-text-fill: white;");
		lbl_data_title.setVisible(false);
		list_field.setCellFactory(param -> new Cell());
		list_data.setCellFactory(param -> new DataCell());
		list_control.setCellFactory(param -> new ControlCell());
		list_control_data.setCellFactory(param -> new DataControlCell());
		list_field.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Release>() {
			@Override
			public void changed(ObservableValue<? extends Release> observable, Release oldValue, Release newValue) {

				if (Loggedinuserdetails.newff == 1) {
					resultspane.setVisible(true);
					chartspane.setVisible(true);
					list_data.setVisible(true);
					lbl_data_title.setText(newValue.getName());
					lbl_data_title.setVisible(true);
				}
				list_control_data.setVisible(false);
				list_data.setVisible(true);
				list_control_data.getItems().clear();
				list_data.getItems().clear();
				list_data.refresh();
				list_data.setItems(FXCollections.observableArrayList(newValue.getCycle()));

			}
		});
		list_control.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Module>() {
			@Override
			public void changed(ObservableValue<? extends Module> observable, Module oldValue, Module newValue) {
				if (Loggedinuserdetails.newcr == 1) {
					resultspane.setVisible(true);
					chartspane.setVisible(true);
					list_control_data.setVisible(true);
					lbl_data_title.setText(newValue.getModulename());
					lbl_data_title.setVisible(true);
				}
				list_control_data.setVisible(true);
				list_data.setVisible(false);
				list_control_data.getItems().clear();
				list_data.getItems().clear();
				list_control_data.refresh();
				list_control_data.setItems(FXCollections.observableArrayList(newValue.getRules()));

			}
		});
	}

	public static DashBoardController getInstance(UsersDetailsBeanBinaryTrade loggedInUsersDetailsBeanBinaryTree) {

		if (loggedInUsersDetailsBeanBinaryTree != null) {
			currentUsersDetailsBeanBinaryTree = loggedInUsersDetailsBeanBinaryTree;
		}
		return userHome;
	}

	public void runmethodBeforeScreenopens() {

	}

	private void setdashboardpanels() {
		if (Loggedinuserdetails.dashboard == 1) {
			if (Loggedinuserdetails.newff == 1) {
				loadfieldtofield();
			}
			if (Loggedinuserdetails.newcr == 1) {
				loadcontrolreports();
			}
		}

		if ((releaselist != null && releaselist.size() > 0) || (moduleslist != null && moduleslist.size() > 0)) {
			selectionpane.setVisible(true);
			resultspane.setVisible(true);
			chartspane.setVisible(true);
		} else {
			selectionpane.setVisible(false);
			resultspane.setVisible(false);
			chartspane.setVisible(false);
		}
	}

	private void importantmethods() {
		securestatus();
		SchedulerTime it = new SchedulerTime();
		it.run();
	}

	private void securestatus() {
		securedfont.setVisible(true);
	}

	private void invokeInactivityListener() {
		FileIOOperations fileOperation = new FileIOOperations();
		Properties props = fileOperation.readPropertyFile("sessionLock.properties");
		boolean session = Boolean.parseBoolean(EncryptAndDecrypt.decryptData(props.getProperty("isSessionEnabled")));
		if (session == true) {
			javax.swing.Action s = new InactivityEventManager();
			InactivityListener ssd = new InactivityListener(s,
					Integer.parseInt(EncryptAndDecrypt.decryptData(props.getProperty("timeDuration"))));
			ssd.start();
		}
	}

	public void getlocaluserLevel() {
		localUserLevelArrayList = new DAO().getlocalUseLevelDetails(Loggedinuserdetails.id);

		if (localUserLevelArrayList != null || !localUserLevelArrayList.isEmpty()) {
			for (int i = 0; i < localUserLevelArrayList.size(); i++) {
				LocalUserLevelBeanBinaryTrade LocalUserLevelBeanBinaryTrade1 = localUserLevelArrayList.get(i);
				localUserLevel = Integer.parseInt(LocalUserLevelBeanBinaryTrade1.getLocalUserLevel());
				activatedDate = LocalUserLevelBeanBinaryTrade1.getCreatedDate();
			}
		}
	}

	public void trailvalidation(int userlevel) {
		userLevel = userlevel; // localUserLevel
		if (userLevel == 11 || userLevel == 12 || userLevel == 13 || userLevel == 14 || userLevel == 15
				|| userLevel == 16) {

			Date trailEndDate = new Date();

			try {
				Date date1 = activatedDate;
				Date date2 = trailEndDate;
				long diff = date2.getTime() - date1.getTime();
				long daysCount = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

				if (daysCount > 30) {
					if (userLevel == 11 || userLevel == 12 || userLevel == 13 || userLevel == 14 || userLevel == 15) {
						new DAO().blockTrailPeriod();
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	// private void logout(ActionEvent event) {
	// CommonFunctions.message = "Please confirm to Logout from the System...!";
	// CommonFunctions.invokeConfirmDialog(getClass());
	//
	// if (CommonFunctions.selectionstatus == "yes") {
	// LogOut lock = new LogOut();
	//
	// lock.exit(event);
	// }
	// }

	public void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	public boolean validateRoles(String id) {
		boolean valid = true;

		// if ((featureslist == null || featureslist.isEmpty()) &&
		// roleid.contains("special")) {
		// valid = true;
		// } else if (featureslist != null && !featureslist.isEmpty()) {
		// for (int i = 0; i < featureslist.size(); i++) {
		// featureid = featureslist.get(i);
		// if (featureid.getFeatureid() == Integer.parseInt(id)) {
		// valid = true;
		// break;
		// }
		// }
		// }
		return valid;
	}

	public void displayallstatuspanes() {
		// for (int i = 0; i < featureslist.size(); i++) {
		// featureid = featureslist.get(i);
		// if (featureid.getFeatureid() == 63) {// income
		// incomepane();
		// } else if (featureid.getFeatureid() == 64) {// expenses
		// expensespane();
		// } else if (featureid.getFeatureid() == 65) {// projects
		// projectpane();
		// } else if (featureid.getFeatureid() == 66) {// businessstatus
		// businessstatuspane();
		// } else if ((featureid.getFeatureid() == 67) &&
		// Integer.parseInt(localUserLevel) >= 3) {// salegoal
		// salegoalpane();
		// } else if (featureid.getFeatureid() == 68) {// notifications
		// notificationpane();
		// }
		// }
		//
		// if (roleid.equals("special")) {
		// incomepane();
		// expensespane();
		// projectpane();
		// businessstatuspane();
		// salegoalpane();
		// notificationpane();
		// }
	}

	public double paneXpostion() {
		double value = 0;
		if (statuspanecount == 1) {
			value = 19;
		}
		if (statuspanecount == 2) {
			value = 270;
		}
		if (statuspanecount == 3) {
			value = 521;
		}
		if (statuspanecount == 4) {
			value = 772;
		}
		if (statuspanecount == 5) {
			value = 19;
		}
		return value;
	}

	public double paneYpostion() {
		double value = 0;
		if (statuspanecount == 1) {
			value = 111;
		}
		if (statuspanecount == 2) {
			value = 111;
		}
		if (statuspanecount == 3) {
			value = 111;
		}
		if (statuspanecount == 4) {
			value = 111;
		}
		if (statuspanecount == 5) {
			value = 350;
		}
		return value;
	}

	@SuppressWarnings("static-access")
	public void start() {
		try {
			AnchorPane aPane = new AnchorPane();
			dashboardanchor.getChildren().add(aPane);
			Properties dafaultValuesPF = new Properties();
			FileInputStream fis = new FileInputStream(new File(".", "/conf/dockicons.properties").getAbsolutePath());
			dafaultValuesPF.load(fis);
			List<String> list = null;
			if (dafaultValuesPF != null && !dafaultValuesPF.isEmpty()) {
				list = Arrays.asList(
						EncryptAndDecrypt.decryptData(dafaultValuesPF.getProperty("list").toString()).split("\\|"));
			}
			fis.close();
			DockerClass dock = new DockerClass();

			for (int i = 0; i < list.size(); i++) {
				ImageView img = new ImageView(
						getClass().getResource("/com/testmydata/fximages/" + list.get(i).toString()).toExternalForm());
				int index = list.get(i).toString().lastIndexOf('.');
				img.setId(list.get(i).toString().substring(0, index));
				dock.add(img);
			}

			// get subscene of dock
			ss = dock.redrawDock();

			// add subscene to root pane
			aPane.getChildren().add(ss);

			aPane.setBottomAnchor(ss, 1.0);
			aPane.setRightAnchor(ss, dashboardanchor.getWidth() / 2 - 10 * 36);
			dashboardanchor.setBottomAnchor(aPane, 1.0);
		} catch (Exception e) {

		}
	}

	@FXML
	private void controlreport() {
		// myStage = (Stage) mymenubar.getScene().getWindow();
		if (Loggedinuserdetails.newcr == 1) {
			nowcontrolreport();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}
	}

	public void nowcontrolreport() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String Screenpath = "/com/testmydata/fxmlnew/NewControlReportRule.fxml";
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Screenpath));
				// fxmlLoader.setController(this);
				try {
					subscreenanchor.setVisible(true);
					subscreenanchor.getChildren().clear();
					Region root = (Region) fxmlLoader.load();

					subscreenanchor.getChildren().setAll(root);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@FXML
	private void fieldtofield() {
		// myStage = (Stage) mymenubar.getScene().getWindow();
		if (Loggedinuserdetails.newff == 1) {
			nowfieldtofield();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}
	}

	public void nowfieldtofield() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String Screenpath = "/com/testmydata/fxmlnew/newfieldtofield.fxml";
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Screenpath));
				// fxmlLoader.setController(this);
				try {
					subscreenanchor.setVisible(true);
					subscreenanchor.getChildren().clear();
					Region root = (Region) fxmlLoader.load();

					subscreenanchor.getChildren().setAll(root);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@FXML
	private void projectsetup() {
		// myStage = (Stage) mymenubar.getScene().getWindow();
		if (Loggedinuserdetails.projectaccess == 1) {
			nowprojectaccess();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}
	}

	public void nowprojectaccess() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "projectsetup";
						new MenuItemsFXHelper().initAndShowGUI(screenName);
					}
				});
			}
		});
	}

	@FXML
	private void newtestsuite() {
		// myStage = (Stage) mymenubar.getScene().getWindow();
		if (Loggedinuserdetails.newts == 1) {
			runnewtestsuite();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}
	}

	public void runnewtestsuite() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "newtestsuite";
						new MenuItemsFXHelper().initAndShowGUI(screenName);
						NewTestSuiteController.getInstance();
					}
				});
			}
		});
	}

	@FXML
	private void runcontrolreport() {
		if (Loggedinuserdetails.crexe == 1) {
			executecontrolreport();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}
	}

	public void executecontrolreport() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "executecontrolreport";
						new MenuItemsFXHelper().initAndShowGUI(screenName);
						ExecuteControlReportController.getInstance();
					}
				});
			}
		});
	}

	@FXML
	private void runfieldtofield() {
		if (Loggedinuserdetails.tsexe == 1) {
			executefieldtofield();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}
	}

	public void executefieldtofield() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "executefieldtofield";
						new MenuItemsFXHelper().initAndShowGUI(screenName);
						ExecuteFieldtoFieldController.getInstance();
					}
				});
			}
		});
	}

	@FXML
	private void newbug() {
		if (Loggedinuserdetails.newbug == 1) {
			runnewbug();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}
	}

	public void runnewbug() {
		runmessage("Under Implementation...");
	}

	@FXML
	private void viewbug() {
		if (Loggedinuserdetails.viewbug == 1) {
			runtestresults();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}
	}

	public void runviewbug() {
		runmessage("Under Implementation...");
	}

	@FXML
	private void testreports() {
		if (Loggedinuserdetails.reports == 1) {
			runtestreports();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}
	}

	public void runtestreports() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "testreports";
						new MenuItemsFXHelper().initAndShowGUI(screenName);
						TestReportsController.getInstance();
					}
				});
			}
		});
	}

	@FXML
	private void testresults() {
		if (Loggedinuserdetails.testresults == 1) {
			runtestresults();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}
	}

	public void runtestresults() {
		runmessage("Under Implementation...");
	}

	@FXML
	private void adduser() {
		if (Loggedinuserdetails.adduser == 1) {
			runadduser();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}
	}

	public void runadduser() {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "adduser";
						new MenuItemsFXHelper().initAndShowGUI(screenName);
					}
				});
			}
		});
	}

	@FXML
	private void bugserver() {
		if (Loggedinuserdetails.addbugserver == 1) {
			runaddbugserver();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}
	}

	public void runaddbugserver() {
		runmessage("Under Implementation...");
	}

	@FXML
	private void changepassword() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					// FX Stuff done here
					ChangePasswordController.getInstance();
					CommonFunctions.invokeChangepassword(getClass());
				} finally {

				}
			}
		});
	}

	@FXML
	private void emailsettings() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					// FX Stuff done here
					EmailSettingsController.getInstance();
					CommonFunctions.invokeEmailSettings(getClass());
				} finally {

				}
			}
		});
	}

	@FXML
	private void qaserver() {
		if (Loggedinuserdetails.addqa == 1) {
			runqaserversettings();
		} else {
			runmessage("Access Denied. Contact your Manager...");
		}

	}

	public void runqaserversettings() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "qaserversettings";
						new MenuItemsFXHelper().initAndShowGUI(screenName);
						QAServerSettingsController.getInstance(currentUsersDetailsBeanBinaryTree);
					}
				});
			}
		});

	}

	public void setqaserver() {
		try {
			if (QADefaultServerDetails.url != null && !QADefaultServerDetails.url.isEmpty()) {
				QAServerSettingsController qas = new QAServerSettingsController();

				if (QADefaultServerDetails.url.contains("jdbc:mysql://")) {
					if (qas.validateconnection(QADefaultServerDetails.servertype,
							QADefaultServerDetails.url.replaceAll("jdbc:mysql://", "").replaceAll("/", ""),
							QADefaultServerDetails.username, QADefaultServerDetails.password)) {

						qaserverlabel.setText("Connected to QA Server At : "
								+ QADefaultServerDetails.url.replaceAll("jdbc:mysql://", "").replaceAll("/", "") + " ( "
								+ QADefaultServerDetails.servertype.toUpperCase() + " )");
					} else {
						qaserverlabel.setText("Failed to Connected QA Server At : "
								+ QADefaultServerDetails.url.replaceAll("jdbc:mysql://", "").replaceAll("/", ""));
					}
				} else if (QADefaultServerDetails.url.contains("jdbc:sqlserver://")) {
					if (qas.validateconnection(QADefaultServerDetails.servertype,
							QADefaultServerDetails.url.replaceAll("jdbc:sqlserver://", ""),
							QADefaultServerDetails.username, QADefaultServerDetails.password)) {

						qaserverlabel.setText("Connected to QA Server At : "
								+ QADefaultServerDetails.url.replaceAll("jdbc:sqlserver://", "") + " ( "
								+ QADefaultServerDetails.servertype.toUpperCase() + " )");
					} else {
						qaserverlabel.setText("Failed to Connected QA Server At : "
								+ QADefaultServerDetails.url.replaceAll("jdbc:sqlserver://", ""));
					}
				} else if (QADefaultServerDetails.url.contains("jdbc:postgresql://")) {
					if (qas.validateconnection(QADefaultServerDetails.servertype,
							QADefaultServerDetails.url.replaceAll("jdbc:postgresql://", "").replaceAll("/", ""),
							QADefaultServerDetails.username, QADefaultServerDetails.password)) {

						qaserverlabel.setText("Connected to QA Server At : "
								+ QADefaultServerDetails.url.replaceAll("jdbc:postgresql://", "").replaceAll("/", "")
								+ " ( " + QADefaultServerDetails.servertype.toUpperCase() + " )");
					} else {
						qaserverlabel.setText("Failed to Connected QA Server At : "
								+ QADefaultServerDetails.url.replaceAll("jdbc:postgresql://", "").replaceAll("/", ""));
					}
				}

			} else {
				qaserverlabel.setText("No Default QA Server Selected");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadfieldtofield() {
		if (releaselist != null && releaselist.size() > 0) {
			releaselist.clear();
		}
		releaselist = new DAO().getreleases("testsuites", Loggedinuserdetails.defaultproject);
		if (releaselist != null && releaselist.size() > 0) {
			for (int i = 0; i < releaselist.size(); i++) {
				// load the releases to list pane
				Release release = new Release();
				release.setName(releaselist.get(i).getRelease());
				list_field.getItems().add(release);

				if (cyclelist != null && cyclelist.size() > 0) {
					cyclelist.clear();
				}
				cyclelist = new DAO().getcycles(releaselist.get(i).getRelease(), Loggedinuserdetails.defaultproject);

				if (cyclelist != null && cyclelist.size() > 0) {
					Cycle[] cycles = new Cycle[cyclelist.size()];
					for (int j = 0; j < cyclelist.size(); j++) {
						Cycle cycle = new Cycle();
						cycle.setCyname(cyclelist.get(j).getCycle());
						cycles[j] = cycle;

						if (tslist != null && tslist.size() > 0) {
							tslist.clear();
						}
						tslist = new DAO().gettestsuitesonly(cyclelist.get(j).getCycle(),
								releaselist.get(i).getRelease(), Loggedinuserdetails.defaultproject);
						if (tslist != null && tslist.size() > 0) {
							TestSuite[] suites = new TestSuite[tslist.size()];
							for (int k = 0; k < tslist.size(); k++) {
								TestSuite testSuite = new TestSuite();
								testSuite.setTsname(tslist.get(k).getTestsuitename());
								suites[k] = testSuite;
							}
							cycle.setTestsuites(suites);
						}
					}
					release.setCycle(cycles);
				}
			}
			if (Loggedinuserdetails.newff == 1) {
				selectionpane.setVisible(true);
				list_field.setVisible(true);
				fieldlabel.setVisible(true);
			}
		}
	}

	public void loadcontrolreports() {
		if (moduleslist != null && moduleslist.size() > 0) {
			moduleslist.clear();
		}
		moduleslist = new DAO().getModuleDetails("modules", "moduleonly", Loggedinuserdetails.defaultproject);
		if (moduleslist != null && moduleslist.size() > 0) {
			for (int i = 0; i < moduleslist.size(); i++) {
				Module module = new Module();
				module.setModulename(moduleslist.get(i).getModulename());

				list_control.getItems().add(module);

				if (rulenamelist != null && rulenamelist.size() > 0) {
					rulenamelist.clear();
				}
				rulenamelist = new DAO().getrulenames(moduleslist.get(i).getModulename(),
						Loggedinuserdetails.defaultproject);

				if (rulenamelist != null && rulenamelist.size() > 0) {
					Rule[] rules = new Rule[rulenamelist.size()];
					for (int j = 0; j < rulenamelist.size(); j++) {
						Rule rule = new Rule();
						rule.setRulename(rulenamelist.get(j).getName());
						rules[j] = rule;
					}
					module.setRules(rules);
				}
			}
			if (Loggedinuserdetails.newcr == 1) {
				selectionpane.setVisible(true);
				list_control.setVisible(true);
				crlabel.setVisible(true);
			}
		}
	}

	static class Cell extends ListCell<Release> {

		HBox hbox = new HBox();
		HBox hbox_title = new HBox();
		Label lbl_release = new Label("");
		Label lbl_pass = new Label("");
		Label lbl_fail = new Label("");
		Label rec = new Label("");
		ImageView icon_execute = new ImageView(StaticImages.source_execute.getImage());
		ImageView icon_run = new ImageView(StaticImages.source_run.getImage());
		ImageView pdficon = new ImageView(StaticImages.pdficon.getImage());
		ImageView excelicon = new ImageView(StaticImages.excelicon.getImage());

		public Cell() {
			hbox_title.setPadding(new Insets(0, 0, 0, 0));
			lbl_release.setPadding(new Insets(0, 5, 0, 5));
			lbl_release.setMaxWidth(170);
			lbl_release.setMinWidth(170);
			lbl_pass.setPadding(new Insets(2, 0, 2, 0));
			lbl_fail.setPadding(new Insets(2, 0, 2, 0));
			lbl_pass.setMaxWidth(40);
			lbl_pass.setMinWidth(40);
			lbl_fail.setMaxWidth(40);
			lbl_fail.setMinWidth(40);
			rec.setPadding(new Insets(1, 2, 1, 2));
			hbox_title.setSpacing(2);
			hbox.setSpacing(5);
			hbox.setPadding(new Insets(0, 0, 0, 5));
			hbox_title.setStyle(" -fx-alignment : center_left;");
			hbox.setStyle(" -fx-background-radius: 1em;-fx-alignment : center_left;");
			icon_execute.setFitHeight(25.0);
			icon_execute.setFitWidth(25.0);
			icon_run.setFitHeight(25.0);
			icon_run.setFitWidth(25.0);
			pdficon.setFitHeight(25.0);
			pdficon.setFitWidth(20.0);
			excelicon.setFitHeight(25.0);
			excelicon.setFitWidth(23.0);
			icon_execute.setCursor(Cursor.HAND);
			pdficon.setCursor(Cursor.HAND);
			excelicon.setCursor(Cursor.HAND);
			rec.setStyle("-fx-background-radius: 0em;-fx-background-color: #40AA03");
			lbl_pass.setStyle(
					"-fx-border-color: #66b366;-fx-border-radius: 1em;-fx-font-size: 10pt;-fx-font-family: \"Calibri\";-fx-alignment : center;-fx-text-fill: #66b366;-fx-opacity: 0.8;");
			lbl_fail.setStyle(
					"-fx-border-color: #ff6666;-fx-border-radius: 1em;-fx-font-size: 10pt;-fx-font-family: \"Calibri\";-fx-alignment : center;-fx-text-fill: #ff6666;-fx-opacity: 0.8;");
			lbl_release.setStyle(
					"-fx-background-radius: 0em;-fx-font-size: 12pt;-fx-font-family: \"Calibri\";-fx-text-fill: #162a4c;-fx-opacity: 1;");
			hbox_title.getChildren().addAll(rec, lbl_release, lbl_pass, lbl_fail);
		}

		@Override
		protected void updateItem(Release release, boolean empty) {
			super.updateItem(release, empty);
			setText(null);
			setGraphic(null);
			hbox.getChildren().clear();
			if (release != null && !empty) {
				lbl_release.setText(release.getName());
				lbl_pass.setText(release.getPass() + "%");
				lbl_fail.setText(release.getFail() + "%");

				if (release.IsExecute()) {
					hbox.getChildren().addAll(hbox_title, icon_run); // true
				} else {
					if (release.getBatchId() == new DAO().getmaxbatchid()) {
						hbox.getChildren().addAll(hbox_title, icon_execute, pdficon, excelicon); // false
					} else {
						hbox.getChildren().addAll(hbox_title, icon_execute); // 1min

					}

				}

				for (int i = 0; i < release.getCycle().length; i++) {
					Cycle cycle = release.getCycle()[i];
					cycle.setRelease(release);
					for (int j = 0; j < cycle.getTestsuites().length; j++) {
						TestSuite testSuite = cycle.getTestsuites()[j];
						testSuite.setCycle(cycle);
					}
				}
				icon_execute.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@SuppressWarnings("unchecked")
					@Override
					public void handle(MouseEvent event) {

						int currentbatchid = new DAO().getmaxbatchid() + 1;
						release.setBatchId(currentbatchid);

						for (int i = 0; i < release.getCycle().length; i++) {
							Cycle cycle = release.getCycle()[i];
							cycle.setIsExecute(true); // means processing
							cycle.setRelease(release);
							cycle.setBatchId(currentbatchid);
							for (int j = 0; j < cycle.getTestsuites().length; j++) {
								TestSuite testSuite = cycle.getTestsuites()[j];
								testSuite.setIsExecute(true);
								testSuite.setBatchId(currentbatchid);
								testSuite.setCycle(cycle);
							}
						}
						JFXListView<Release> listField = (JFXListView<Release>) ((ImageView) event.getSource())
								.getScene().lookup("#list_field");
						JFXListView<Cycle> listFieldData = (JFXListView<Cycle>) ((ImageView) event.getSource())
								.getScene().lookup("#list_data");
						JFXListView<Rule> listControl = (JFXListView<Rule>) ((ImageView) event.getSource()).getScene()
								.lookup("#list_control_data");
						AnchorPane pane = (AnchorPane) ((ImageView) event.getSource()).getScene()
								.lookup("#resultspane");
						Label label = (Label) ((ImageView) event.getSource()).getScene().lookup("#lbl_data_title");

						pane.setVisible(true);
						listControl.setVisible(false);
						listFieldData.setVisible(true);
						listControl.getItems().clear();
						listFieldData.getItems().clear();
						listFieldData.refresh();
						listFieldData.setItems(FXCollections.observableArrayList(release.getCycle()));
						label.setText(release.getName());
						label.setStyle(
								"-fx-background-color: #162a4c;-fx-alignment : center;-fx-background-radius: 1em;-fx-font-size: 18pt;-fx-font-family: \"Calibri\";-fx-text-fill: white;");

						FieldtoFieldExecutionServices fs = new FieldtoFieldExecutionServices();
						fs.releaserun(release.getName(), currentbatchid, listField, listFieldData, release);

						if (hbox.getChildren().size() == 4) {
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().add(icon_run);

						} else if (hbox.getChildren().size() == 2) {
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().add(icon_run);
						}

					}
				});

				pdficon.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						File ff = new File(new File(".", "/Reports/TestSuites/PDF").getAbsolutePath());
						if (!(ff.exists() && ff.isDirectory())) {
							ff.mkdirs();
						}
						ExecuteFieldtoFieldController effc = new ExecuteFieldtoFieldController();
						if (reportcolumnlist == null || reportcolumnlist.size() == 0) {
							reportcolumnlist.addAll(effc.addcolumnsforreport());
						}

						ReportsDownloader rd = new ReportsDownloader();
						rd.download("Test Suite", release.getBatchId(), "Reports/TestSuites/PDF", "pdf",
								reportcolumnlist,
								new DAO().getfieldresults(release.getBatchId(), 0, 0, effc.replacer(reportcolumnlist),
										reportcolumnlist.size(), null, QADefaultServerDetails.id));

					}
				});

				excelicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						File ff = new File(new File(".", "/Reports/TestSuites/Excel").getAbsolutePath());
						if (!(ff.exists() && ff.isDirectory())) {
							ff.mkdirs();
						}
						ExecuteFieldtoFieldController effc = new ExecuteFieldtoFieldController();
						if (reportcolumnlist == null || reportcolumnlist.size() == 0) {
							reportcolumnlist.addAll(effc.addcolumnsforreport());
						}

						ReportsDownloader rd = new ReportsDownloader();
						rd.download("Test Suite", release.getBatchId(), "Reports/TestSuites/Excel", "excel",
								reportcolumnlist,
								new DAO().getfieldresults(release.getBatchId(), 0, 0, effc.replacer(reportcolumnlist),
										reportcolumnlist.size(), null, QADefaultServerDetails.id));
					}
				});
				setGraphic(hbox);
			}
		}

	}

	static class ControlCell extends ListCell<Module> {

		HBox hbox = new HBox();
		HBox hbox_title = new HBox();
		Label lbl_module = new Label("");
		Label lbl_pass = new Label("");
		Label lbl_fail = new Label("");
		Label rec = new Label("");
		ImageView icon_execute = new ImageView(StaticImages.source_execute.getImage());
		ImageView icon_run = new ImageView(StaticImages.source_run.getImage());
		ImageView pdficon = new ImageView(StaticImages.pdficon.getImage());
		ImageView excelicon = new ImageView(StaticImages.excelicon.getImage());

		public ControlCell() {
			hbox_title.setPadding(new Insets(0, 0, 0, 0));
			lbl_module.setPadding(new Insets(0, 5, 0, 5));
			lbl_module.setMaxWidth(170);
			lbl_module.setMinWidth(170);
			lbl_pass.setPadding(new Insets(2, 0, 2, 0));
			lbl_fail.setPadding(new Insets(2, 0, 2, 0));
			lbl_pass.setMaxWidth(40);
			lbl_pass.setMinWidth(40);
			lbl_fail.setMaxWidth(40);
			lbl_fail.setMinWidth(40);
			rec.setPadding(new Insets(1, 2, 1, 2));
			hbox_title.setSpacing(2);
			hbox.setSpacing(5);
			hbox.setPadding(new Insets(0, 0, 0, 5));
			hbox_title.setStyle(" -fx-alignment : center_left;");
			hbox.setStyle(" -fx-background-radius: 1em;-fx-alignment : center_left;");
			icon_execute.setFitHeight(25.0);
			icon_execute.setFitWidth(25.0);
			icon_run.setFitHeight(25.0);
			icon_run.setFitWidth(25.0);
			pdficon.setFitHeight(25.0);
			pdficon.setFitWidth(20.0);
			excelicon.setFitHeight(25.0);
			excelicon.setFitWidth(23.0);
			icon_execute.setCursor(Cursor.HAND);
			pdficon.setCursor(Cursor.HAND);
			excelicon.setCursor(Cursor.HAND);
			rec.setStyle("-fx-background-radius: 0em;-fx-background-color:  #FFC107");
			lbl_pass.setStyle(
					"-fx-border-color: #66b366;-fx-border-radius: 1em;-fx-font-size: 10pt;-fx-font-family: \"Calibri\"; -fx-alignment : center; -fx-text-fill: #66b366;-fx-opacity: 0.8;");
			lbl_fail.setStyle(
					"-fx-border-color: #ff6666;-fx-border-radius: 1em;-fx-font-size: 10pt;-fx-font-family: \"Calibri\"; -fx-alignment : center;-fx-text-fill: #ff6666;-fx-opacity: 0.8;");
			lbl_module.setStyle(
					"-fx-background-radius: 0em;-fx-font-size: 13pt;-fx-font-family: \"Calibri\";-fx-text-fill: black;-fx-opacity: 1;");
			hbox_title.getChildren().addAll(rec, lbl_module, lbl_pass, lbl_fail);

		}

		@Override
		protected void updateItem(Module module, boolean empty) {
			super.updateItem(module, empty); // To change body of generated
												// methods, choose Tools |
												// Templates.
			setText(null);
			setGraphic(null);
			hbox.getChildren().clear();
			if (module != null && !empty) {
				lbl_module.setText(module.getModulename());
				lbl_pass.setText(module.getPass() + "%");
				lbl_fail.setText(module.getFail() + "%");

				if (module.IsExecute()) {
					hbox.getChildren().addAll(hbox_title, icon_run);
				} else {
					if (module.getBatchId() == new DAO().getcrmaxbatchid()) {
						hbox.getChildren().addAll(hbox_title, icon_execute, pdficon, excelicon);
					} else {
						hbox.getChildren().addAll(hbox_title, icon_execute);
					}
				}

				for (int j = 0; j < module.getRules().length; j++) {
					Rule rules = module.getRules()[j];
					rules.setModule(module);
				}

				icon_execute.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@SuppressWarnings("unchecked")
					@Override
					public void handle(MouseEvent event) {

						int currentbatchid = new DAO().getcrmaxbatchid() + 1;
						module.setBatchId(currentbatchid);

						for (int i = 0; i < module.getRules().length; i++) {
							Rule rule = module.getRules()[i];
							rule.setIsExecute(true);
							rule.setBatchId(currentbatchid);
						}

						JFXListView<Module> listField = (JFXListView<Module>) ((ImageView) event.getSource()).getScene()
								.lookup("#list_control");
						JFXListView<Cycle> listFieldData = (JFXListView<Cycle>) ((ImageView) event.getSource())
								.getScene().lookup("#list_data");
						JFXListView<Rule> listControlData = (JFXListView<Rule>) ((ImageView) event.getSource())
								.getScene().lookup("#list_control_data");
						AnchorPane pane = (AnchorPane) ((ImageView) event.getSource()).getScene()
								.lookup("#resultspane");
						Label label = (Label) ((ImageView) event.getSource()).getScene().lookup("#lbl_data_title");

						pane.setVisible(true);
						listFieldData.setVisible(false);
						listControlData.setVisible(true);
						listControlData.getItems().clear();
						listFieldData.getItems().clear();
						listControlData.refresh();
						listControlData.setItems(FXCollections.observableArrayList(module.getRules()));
						label.setText(module.getModulename().toUpperCase());
						label.setStyle(
								"-fx-background-color: #162a4c;-fx-alignment : center;-fx-background-radius: 1em;-fx-font-size: 18pt;-fx-font-family: \"Calibri\";-fx-text-fill: white;");

						ControlReportsExecutionServices cre = new ControlReportsExecutionServices();
						cre.modulerun(module.getModulename(), currentbatchid, listField, listControlData, module);

						if (hbox.getChildren().size() == 4) {
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().add(icon_run);

						} else if (hbox.getChildren().size() == 2) {
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().add(icon_run);
						}
					}
				});
				pdficon.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						File ff = new File(new File(".", "/Reports/ControlReport/PDF").getAbsolutePath());
						if (!(ff.exists() && ff.isDirectory())) {
							ff.mkdirs();
						}
						ExecuteControlReportController ecr = new ExecuteControlReportController();
						if (crcolumnlist != null || crcolumnlist.size() > 0) {
							crcolumnlist.clear();
						}
						crcolumnlist.addAll(ecr.addcolumnsforpdfreport());
						ReportsDownloader rd = new ReportsDownloader();
						rd.download("Control Report", module.getBatchId(), "Reports/ControlReport/PDF", "pdf",
								crcolumnlist, new DAO().getcrresults(module.getBatchId(), 0, ecr.replacer(crcolumnlist),
										crcolumnlist.size(), QADefaultServerDetails.id));
					}
				});
				excelicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						File ff = new File(new File(".", "/Reports/ControlReport/Excel").getAbsolutePath());
						if (!(ff.exists() && ff.isDirectory())) {
							ff.mkdirs();
						}
						ExecuteControlReportController ecr = new ExecuteControlReportController();
						if (crcolumnlist != null || crcolumnlist.size() > 0) {
							crcolumnlist.clear();
						}
						crcolumnlist.addAll(ecr.addcolumnsforexcelreport());
						ReportsDownloader rd = new ReportsDownloader();
						rd.download("Control Report", module.getBatchId(), "Reports/ControlReport/Excel", "excel",
								crcolumnlist, new DAO().getcrresults(module.getBatchId(), 0, ecr.replacer(crcolumnlist),
										crcolumnlist.size(), QADefaultServerDetails.id));
					}
				});

				setGraphic(hbox);
			}
		}

	}

	static class DataCell extends ListCell<Cycle> {
		VBox vbox = new VBox();
		HBox hbox_title = new HBox();
		HBox hbox = new HBox();
		Label lbl_cycle = new Label("");
		Label lbl_pass = new Label("");
		Label lbl_fail = new Label("");
		Label rec = new Label();
		Label rec1 = new Label();
		ImageView icon_execute = new ImageView(StaticImages.source_execute.getImage());
		ImageView icon_run = new ImageView(StaticImages.source_run.getImage());
		ImageView pdficon = new ImageView(StaticImages.pdficon.getImage());
		ImageView excelicon = new ImageView(StaticImages.excelicon.getImage());

		public DataCell() {
			hbox_title.setPadding(new Insets(0, 0, 0, 0));
			lbl_cycle.setPadding(new Insets(0, 5, 0, 5));
			lbl_cycle.setMaxWidth(170);
			lbl_cycle.setMinWidth(170);
			lbl_pass.setPadding(new Insets(2, 0, 2, 0));
			lbl_fail.setPadding(new Insets(2, 0, 2, 0));
			lbl_pass.setMaxWidth(40);
			lbl_pass.setMinWidth(40);
			lbl_fail.setMaxWidth(40);
			lbl_fail.setMinWidth(40);
			hbox_title.setSpacing(2);
			hbox.setSpacing(5);
			hbox.setPadding(new Insets(0, 0, 0, 5));
			rec.setPadding(new Insets(1, 2, 1, 2));
			rec1.setPrefSize(330, 0.1);
			rec1.setMinSize(330, 0.1);
			hbox_title.setStyle(" -fx-alignment : center_left;-fx-opacity: 1;");
			hbox.setStyle(" -fx-background-radius: 1em;-fx-alignment : center_left;");

			icon_execute.setFitHeight(25.0);
			icon_execute.setFitWidth(25.0);
			icon_run.setFitHeight(25.0);
			icon_run.setFitWidth(25.0);
			pdficon.setFitHeight(25.0);
			pdficon.setFitWidth(20.0);
			excelicon.setFitHeight(25.0);
			excelicon.setFitWidth(23.0);
			icon_execute.setCursor(Cursor.HAND);
			pdficon.setCursor(Cursor.HAND);
			excelicon.setCursor(Cursor.HAND);
			lbl_pass.setStyle(
					"-fx-border-color: #66b366;-fx-border-radius: 1em;-fx-font-size: 10pt;-fx-font-family: \"Calibri\";-fx-alignment : center;-fx-text-fill: #66b366;-fx-opacity: 0.8;");
			lbl_fail.setStyle(
					"-fx-border-color: #ff6666;-fx-border-radius: 1em;-fx-font-size: 10pt;-fx-font-family: \"Calibri\";-fx-alignment : center;-fx-text-fill: #ff6666;-fx-opacity: 0.8;");
			lbl_cycle.setStyle(
					"-fx-background-radius: 0em;-fx-font-size: 13pt;-fx-font-family: \"Calibri\";-fx-text-fill: #162a4c;-fx-opacity: 1;");
			rec.setStyle("-fx-background-radius: 0em;-fx-background-color: #162a4c;");
			rec1.setStyle(
					"-fx-background-radius: 0em;-fx-background-color: grey;-fx-alignment : center;-fx-opacity: 0.2;");

		}

		@Override
		protected void updateItem(Cycle cycle, boolean empty) {
			super.updateItem(cycle, empty); // To change body of generated
											// methods, choose Tools |
											// Templates.
			setText(null);
			setGraphic(null);
			hbox.getChildren().clear();
			hbox_title.getChildren().clear();
			if (cycle != null && !empty) {
				hbox_title.getChildren().addAll(rec, lbl_cycle, lbl_pass, lbl_fail);

				if (cycle.IsExecute()) {
					hbox.getChildren().addAll(hbox_title, icon_run);
				} else {
					if (cycle.getBatchId() == new DAO().getmaxbatchid()) {
						hbox.getChildren().addAll(hbox_title, icon_execute, pdficon, excelicon);
					} else {
						hbox.getChildren().addAll(hbox_title, icon_execute);
					}
				}

				vbox.getChildren().clear();
				vbox.getChildren().addAll(hbox);
				vbox.setSpacing(5);

				icon_execute.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@SuppressWarnings("unchecked")
					@Override
					public void handle(MouseEvent event) {

						int currentbatchid = new DAO().getmaxbatchid() + 1;
						cycle.setBatchId(currentbatchid);
						cycle.setIsExecute(true);
						for (int j = 0; j < cycle.getTestsuites().length; j++) {
							TestSuite testSuite = cycle.getTestsuites()[j];
							testSuite.setIsExecute(true);
							testSuite.setBatchId(currentbatchid);
						}

						JFXListView<Release> listField = (JFXListView<Release>) ((ImageView) event.getSource())
								.getScene().lookup("#list_field");
						JFXListView<Cycle> listData = (JFXListView<Cycle>) ((ImageView) event.getSource()).getScene()
								.lookup("#list_data");

						listField.refresh();
						listData.refresh();

						FieldtoFieldExecutionServices fs = new FieldtoFieldExecutionServices();

						fs.cyclerun(cycle.getRelease().getName(), cycle.getCyname(), currentbatchid, listField,
								listData, cycle.getRelease(), cycle);

						if (hbox.getChildren().size() == 4) {
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().add(icon_run);

						} else if (hbox.getChildren().size() == 2) {
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().add(icon_run);
						}
					}
				});

				pdficon.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						File ff = new File(new File(".", "/Reports/TestSuites/PDF").getAbsolutePath());
						if (!(ff.exists() && ff.isDirectory())) {
							ff.mkdirs();
						}
						ExecuteFieldtoFieldController effc = new ExecuteFieldtoFieldController();
						if (reportcolumnlist == null || reportcolumnlist.size() == 0) {
							reportcolumnlist.addAll(effc.addcolumnsforreport());
						}

						ReportsDownloader rd = new ReportsDownloader();
						rd.download("Test Suite", cycle.getBatchId(), "Reports/TestSuites/PDF", "pdf", reportcolumnlist,
								new DAO().getfieldresults(cycle.getBatchId(), 0, 0, effc.replacer(reportcolumnlist),
										reportcolumnlist.size(), null, QADefaultServerDetails.id));

					}
				});
				excelicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						File ff = new File(new File(".", "/Reports/TestSuites/Excel").getAbsolutePath());
						if (!(ff.exists() && ff.isDirectory())) {
							ff.mkdirs();
						}
						ExecuteFieldtoFieldController effc = new ExecuteFieldtoFieldController();
						if (reportcolumnlist == null || reportcolumnlist.size() == 0) {
							reportcolumnlist.addAll(effc.addcolumnsforreport());
						}

						ReportsDownloader rd = new ReportsDownloader();
						rd.download("Test Suite", cycle.getBatchId(), "Reports/TestSuites/Excel", "excel",
								reportcolumnlist,
								new DAO().getfieldresults(cycle.getBatchId(), 0, 0, effc.replacer(reportcolumnlist),
										reportcolumnlist.size(), null, QADefaultServerDetails.id));
					}
				});

				lbl_cycle.setStyle(
						"-fx-background-radius: 0.5em;-fx-font-size: 12pt;-fx-font-family: \"Calibri\";-fx-text-fill: #162a4c;-fx-opacity: 1;");
				rec.setStyle("-fx-background-radius: 0em;-fx-background-color: #162a4c");

				lbl_cycle.setText(cycle.getCyname());
				lbl_pass.setText(cycle.getPass() + "%");
				lbl_fail.setText(cycle.getFail() + "%");

				for (int i = 0; i < cycle.getTestsuites().length; i++) {

					TestSuite testSuite = cycle.getTestsuites()[i];

					HBox hbox_title_testSuite = new HBox();
					HBox hbox_percent_testSuite = new HBox();
					hbox_percent_testSuite.setPadding(new Insets(0, 0, 0, 0));
					hbox_title_testSuite.setPadding(new Insets(0, 0, 0, 25));
					hbox_percent_testSuite.setSpacing(2);
					hbox_title_testSuite.setSpacing(5);
					Label lbl_testSuite = new Label("");
					Label lbl_pass_testSuite = new Label("");
					Label lbl_fail_testSuite = new Label("");
					Label rec = new Label("");
					lbl_testSuite.setPadding(new Insets(0, 5, 0, 5));
					lbl_testSuite.setMaxWidth(150);
					lbl_testSuite.setMinWidth(150);
					lbl_pass_testSuite.setPadding(new Insets(2, 0, 2, 0));
					lbl_fail_testSuite.setPadding(new Insets(2, 0, 2, 0));
					lbl_pass_testSuite.setMaxWidth(40);
					lbl_pass_testSuite.setMinWidth(40);
					lbl_fail_testSuite.setMaxWidth(40);
					lbl_fail_testSuite.setMinWidth(40);
					rec.setPadding(new Insets(1, 2, 1, 2));
					lbl_pass_testSuite.setStyle(
							"-fx-border-color: #66b366;-fx-border-radius: 1em;-fx-font-size: 9pt;-fx-font-family: \"Calibri\";-fx-alignment : center;-fx-text-fill: #66b366;-fx-opacity: 0.8;");
					lbl_fail_testSuite.setStyle(
							"-fx-border-color: #ff6666;-fx-border-radius: 1em;-fx-font-size: 9pt;-fx-font-family: \"Calibri\";-fx-alignment : center;-fx-text-fill: #ff6666;-fx-opacity: 0.8;");
					lbl_testSuite.setStyle(
							"-fx-background-radius: 0em;-fx-font-size: 12pt;-fx-font-family: \"Calibri\";-fx-text-fill: #162a4c;-fx-opacity: 1;");

					lbl_testSuite.setText(testSuite.getTsname());
					lbl_pass_testSuite.setText(testSuite.getPass() + "%");
					lbl_fail_testSuite.setText(testSuite.getFail() + "%");

					ImageView icon_execute1 = new ImageView(StaticImages.source_execute.getImage());
					ImageView icon_run1 = new ImageView(StaticImages.source_run.getImage());
					ImageView pdficon1 = new ImageView(StaticImages.pdficon.getImage());
					ImageView excelicon1 = new ImageView(StaticImages.excelicon.getImage());

					icon_execute1.setFitHeight(25.0);
					icon_execute1.setFitWidth(25.0);
					icon_run1.setFitHeight(25.0);
					icon_run1.setFitWidth(25.0);
					pdficon1.setFitHeight(25.0);
					pdficon1.setFitWidth(20.0);
					excelicon1.setFitHeight(25.0);
					excelicon1.setFitWidth(23.0);
					icon_execute1.setCursor(Cursor.HAND);
					pdficon1.setCursor(Cursor.HAND);
					excelicon1.setCursor(Cursor.HAND);
					rec.setStyle("-fx-background-radius: 0em;-fx-background-color: grey");
					hbox_percent_testSuite.getChildren().addAll(rec, lbl_testSuite, lbl_pass_testSuite,
							lbl_fail_testSuite);

					if (testSuite.IsExecute()) {
						hbox_title_testSuite.getChildren().addAll(hbox_percent_testSuite, icon_run1);
					} else {
						if (testSuite.getBatchId() == new DAO().getmaxbatchid()) {
							hbox_title_testSuite.getChildren().addAll(hbox_percent_testSuite, icon_execute1, pdficon1,
									excelicon1);
						} else {
							hbox_title_testSuite.getChildren().addAll(hbox_percent_testSuite, icon_execute1);
						}
					}
					vbox.getChildren().add(hbox_title_testSuite);

					icon_execute1.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@SuppressWarnings("unchecked")
						@Override
						public void handle(MouseEvent event) {
							icon_execute1.setVisible(false);
							pdficon1.setVisible(false);
							excelicon1.setVisible(false);
							icon_run1.setVisible(true);

							int currentbatchid1 = new DAO().getmaxbatchid() + 1;
							testSuite.setIsExecute(true);
							testSuite.setBatchId(currentbatchid1);

							JFXListView<Release> listField = (JFXListView<Release>) ((ImageView) event.getSource())
									.getScene().lookup("#list_field");
							JFXListView<Cycle> listData = (JFXListView<Cycle>) ((ImageView) event.getSource())
									.getScene().lookup("#list_data");

							listField.refresh();
							listData.refresh();

							FieldtoFieldExecutionServices fs = new FieldtoFieldExecutionServices();

							fs.testsuiterun(cycle.getRelease().getName(), cycle.getCyname(), testSuite.getTsname(),
									currentbatchid1, listField, listData, cycle.getRelease(), cycle, testSuite);

							if (hbox_title_testSuite.getChildren().size() == 4) {
								hbox_title_testSuite.getChildren()
										.remove(hbox_title_testSuite.getChildren().size() - 1);
								hbox_title_testSuite.getChildren()
										.remove(hbox_title_testSuite.getChildren().size() - 1);
								hbox_title_testSuite.getChildren()
										.remove(hbox_title_testSuite.getChildren().size() - 1);
								hbox_title_testSuite.getChildren().add(icon_run1);

							} else if (hbox_title_testSuite.getChildren().size() == 2) {
								hbox_title_testSuite.getChildren()
										.remove(hbox_title_testSuite.getChildren().size() - 1);
								hbox_title_testSuite.getChildren().add(icon_run1);
							}
						}
					});
					pdficon1.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							File ff = new File(new File(".", "/Reports/TestSuites/PDF").getAbsolutePath());
							if (!(ff.exists() && ff.isDirectory())) {
								ff.mkdirs();
							}
							ExecuteFieldtoFieldController effc = new ExecuteFieldtoFieldController();
							if (reportcolumnlist == null || reportcolumnlist.size() == 0) {
								reportcolumnlist.addAll(effc.addcolumnsforreport());
							}

							ReportsDownloader rd = new ReportsDownloader();
							rd.download("Test Suite", testSuite.getBatchId(), "Reports/TestSuites/PDF", "pdf",
									reportcolumnlist,
									new DAO().getfieldresults(testSuite.getBatchId(), 0, 0,
											effc.replacer(reportcolumnlist), reportcolumnlist.size(), null,
											QADefaultServerDetails.id));

						}
					});
					excelicon1.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							File ff = new File(new File(".", "/Reports/TestSuites/Excel").getAbsolutePath());
							if (!(ff.exists() && ff.isDirectory())) {
								ff.mkdirs();
							}
							ExecuteFieldtoFieldController effc = new ExecuteFieldtoFieldController();
							if (reportcolumnlist == null || reportcolumnlist.size() == 0) {
								reportcolumnlist.addAll(effc.addcolumnsforreport());
							}

							ReportsDownloader rd = new ReportsDownloader();
							rd.download("Test Suite", testSuite.getBatchId(), "Reports/TestSuites/Excel", "excel",
									reportcolumnlist,
									new DAO().getfieldresults(testSuite.getBatchId(), 0, 0,
											effc.replacer(reportcolumnlist), reportcolumnlist.size(), null,
											QADefaultServerDetails.id));
						}
					});

				}
				vbox.getChildren().add(rec1);
				setGraphic(vbox);
			}
		}

	}

	static class DataControlCell extends ListCell<Rule> {

		HBox hbox = new HBox();
		HBox hbox_title = new HBox();
		Label rec = new Label();
		Label lbl_rule = new Label("");
		Label lbl_pass = new Label("");
		Label lbl_fail = new Label("");
		ImageView icon_execute = new ImageView(StaticImages.source_execute.getImage());
		ImageView icon_run = new ImageView(StaticImages.source_run.getImage());
		ImageView pdficon = new ImageView(StaticImages.pdficon.getImage());
		ImageView excelicon = new ImageView(StaticImages.excelicon.getImage());

		public DataControlCell() {
			hbox_title.setPadding(new Insets(0, 0, 0, 0));
			lbl_rule.setPadding(new Insets(0, 5, 0, 5));
			lbl_rule.setMaxWidth(170);
			lbl_rule.setMinWidth(170);
			lbl_pass.setPadding(new Insets(2, 0, 2, 0));
			lbl_fail.setPadding(new Insets(2, 0, 2, 0));
			lbl_pass.setMaxWidth(40);
			lbl_pass.setMinWidth(40);
			lbl_fail.setMaxWidth(40);
			lbl_fail.setMinWidth(40);
			hbox_title.setSpacing(2);
			rec.setStyle("-fx-background-radius: 0em;-fx-background-color: #162a4c");
			rec.setPadding(new Insets(1, 2, 1, 2));
			hbox.setSpacing(5);
			hbox.setPadding(new Insets(0, 0, 0, 5));
			hbox_title.setStyle(" -fx-alignment : center_left;");
			hbox.setStyle(" -fx-background-radius: 0em;-fx-alignment : center_left;");
			icon_execute.setFitHeight(25.0);
			icon_execute.setFitWidth(25.0);
			icon_run.setFitHeight(25.0);
			icon_run.setFitWidth(25.0);
			pdficon.setFitHeight(25.0);
			pdficon.setFitWidth(20.0);
			excelicon.setFitHeight(25.0);
			excelicon.setFitWidth(23.0);
			icon_execute.setCursor(Cursor.HAND);
			pdficon.setCursor(Cursor.HAND);
			excelicon.setCursor(Cursor.HAND);
			lbl_pass.setStyle(
					"-fx-border-color: #66b366;-fx-border-radius: 1em;-fx-font-size: 10pt;-fx-font-family: \"Calibri\";-fx-alignment : center;-fx-text-fill: #66b366;-fx-opacity: 0.8;");
			lbl_fail.setStyle(
					"-fx-border-color: #ff6666;-fx-border-radius: 1em;-fx-font-size: 10pt;-fx-font-family: \"Calibri\";-fx-alignment : center;-fx-text-fill: #ff6666;-fx-opacity: 0.8;");
			lbl_rule.setStyle(
					"-fx-background-radius: 0em;-fx-font-size: 13pt;-fx-font-family: \"Calibri\";-fx-text-fill: #162a4c;-fx-opacity: 1;");

		}

		@Override
		protected void updateItem(Rule rule, boolean empty) {
			super.updateItem(rule, empty); // To change body of generated
											// methods, choose Tools |
											// Templates.
			setText(null);
			setGraphic(null);
			hbox.getChildren().clear();
			hbox_title.getChildren().clear();
			if (rule != null && !empty) {

				hbox_title.getChildren().addAll(rec, lbl_rule, lbl_pass, lbl_fail);

				if (rule.IsExecute()) {
					hbox.getChildren().addAll(hbox_title, icon_run);
				} else {
					if (rule.getBatchId() == new DAO().getcrmaxbatchid()) {
						hbox.getChildren().addAll(hbox_title, icon_execute, pdficon, excelicon);
					} else {
						hbox.getChildren().addAll(hbox_title, icon_execute);
					}
				}

				lbl_rule.setText(rule.getRulename());
				lbl_pass.setText(rule.getPass() + "%");
				lbl_fail.setText(rule.getFail() + "%");
				icon_execute.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@SuppressWarnings("unchecked")
					@Override
					public void handle(MouseEvent event) {

						int currentbatchid = new DAO().getcrmaxbatchid() + 1;
						rule.setBatchId(currentbatchid);
						rule.setIsExecute(true); // means executing

						JFXListView<Module> listField = (JFXListView<Module>) ((ImageView) event.getSource()).getScene()
								.lookup("#list_control");
						JFXListView<Rule> listControlData = (JFXListView<Rule>) ((ImageView) event.getSource())
								.getScene().lookup("#list_control_data");

						listField.refresh();
						listControlData.refresh();

						ControlReportsExecutionServices cre = new ControlReportsExecutionServices();
						cre.rulerun(rule.getRulename(), currentbatchid, listField, listControlData, rule.getModule(),
								rule);

						if (hbox.getChildren().size() == 4) {
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().add(icon_run);

						} else if (hbox.getChildren().size() == 2) {
							hbox.getChildren().remove(hbox.getChildren().size() - 1);
							hbox.getChildren().add(icon_run);
						}
					}
				});

				pdficon.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						File ff = new File(new File(".", "/Reports/ControlReport/PDF").getAbsolutePath());
						if (!(ff.exists() && ff.isDirectory())) {
							ff.mkdirs();
						}
						ExecuteControlReportController ecr = new ExecuteControlReportController();
						if (crcolumnlist != null || crcolumnlist.size() > 0) {
							crcolumnlist.clear();
						}
						crcolumnlist.addAll(ecr.addcolumnsforpdfreport());
						ReportsDownloader rd = new ReportsDownloader();
						rd.download("Control Report", rule.getBatchId(), "Reports/ControlReport/PDF", "pdf",
								crcolumnlist, new DAO().getcrresults(rule.getBatchId(), 0, ecr.replacer(crcolumnlist),
										crcolumnlist.size(), QADefaultServerDetails.id));
					}
				});
				excelicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						File ff = new File(new File(".", "/Reports/ControlReport/Excel").getAbsolutePath());
						if (!(ff.exists() && ff.isDirectory())) {
							ff.mkdirs();
						}
						ExecuteControlReportController ecr = new ExecuteControlReportController();
						if (crcolumnlist != null || crcolumnlist.size() > 0) {
							crcolumnlist.clear();
						}
						crcolumnlist.addAll(ecr.addcolumnsforexcelreport());
						ReportsDownloader rd = new ReportsDownloader();
						rd.download("Control Report", rule.getBatchId(), "Reports/ControlReport/Excel", "excel",
								crcolumnlist, new DAO().getcrresults(rule.getBatchId(), 0, ecr.replacer(crcolumnlist),
										crcolumnlist.size(), QADefaultServerDetails.id));
					}
				});
				setGraphic(hbox);
			}
		}

	}

	public void setGreen() {
		lbl_data_title.setStyle(
				"-fx-background-color:  #40AA03;-fx-alignment : center;-fx-background-radius: 1em;-fx-font-size: 14pt;-fx-font-family: \"Calibri\";-fx-text-fill: white;");
	}

	public void setRed() {
		lbl_data_title.setStyle(
				"-fx-background-color:  #F8340D;-fx-alignment : center;-fx-background-radius: 1em;-fx-font-size: 14pt;-fx-font-family: \"Calibri\";-fx-text-fill: white;");
	}

	public void setBlue() {
		lbl_data_title.setStyle(
				"-fx-background-color:  #162a4c;-fx-alignment : center;-fx-background-radius: 1em;-fx-font-size: 14pt;-fx-font-family: \"Calibri\";-fx-text-fill: white;");
	}

	@FXML
	private void designbutton() {
		designanchor.setVisible(true);
		testsuiteanchor.setVisible(false);
		testanchor.setVisible(false);
		bugsanchor.setVisible(false);
		reportsanchor.setVisible(false);
		settingsanchor.setVisible(false);
	}

	@FXML
	private void testsuitebutton() {
		designanchor.setVisible(false);
		testsuiteanchor.setVisible(true);
		testanchor.setVisible(false);
		bugsanchor.setVisible(false);
		reportsanchor.setVisible(false);
		settingsanchor.setVisible(false);
	}

	@FXML
	private void testbutton() {
		designanchor.setVisible(false);
		testsuiteanchor.setVisible(false);
		testanchor.setVisible(true);
		bugsanchor.setVisible(false);
		reportsanchor.setVisible(false);
		settingsanchor.setVisible(false);
	}

	@FXML
	private void bugsbutton() {
		designanchor.setVisible(false);
		testsuiteanchor.setVisible(false);
		testanchor.setVisible(false);
		bugsanchor.setVisible(true);
		reportsanchor.setVisible(false);
		settingsanchor.setVisible(false);
	}

	@FXML
	private void reportsbutton() {
		designanchor.setVisible(false);
		testsuiteanchor.setVisible(false);
		testanchor.setVisible(false);
		bugsanchor.setVisible(false);
		reportsanchor.setVisible(true);
		settingsanchor.setVisible(false);
	}

	@FXML
	private void settingsbutton() {
		designanchor.setVisible(false);
		testsuiteanchor.setVisible(false);
		testanchor.setVisible(false);
		bugsanchor.setVisible(false);
		reportsanchor.setVisible(false);
		settingsanchor.setVisible(true);
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
		exisitingprojectscombo.getSelectionModel().select(Loggedinuserdetails.defaultproject);
		exisitingprojectscombo.setStyle("-fx-text-fill: white");
	}
}
