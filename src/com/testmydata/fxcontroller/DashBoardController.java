package com.testmydata.fxcontroller;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
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
import com.jfoenix.controls.JFXTextField;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import com.testmydata.auditlog.StoreAuditLogger;
import com.testmydata.binarybeans.LocalUserLevelBeanBinaryTrade;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxhelpers.MenuItemsFXHelper;
import com.testmydata.main.InactivityEventManager;
import com.testmydata.main.InactivityListener;
import com.testmydata.main.LogOut;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.DockerClass;
import com.testmydata.util.EncryptAndDecrypt;
import com.testmydata.util.FileIOOperations;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.QADefaultServerDetails;
import com.testmydata.util.SchedulerTime;
import com.testmydata.vpn.VpnConnectionThread;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DashBoardController implements Initializable {
	@FXML
	private AnchorPane dashboardanchor;
	@FXML
	private MenuBar mymenubar;
	@FXML
	private MenuItem controlreport, fieldtofield, runcontrolreport, runfieldtofield, newtestsuite, executetestsuites,
			testreports, adduser, changepassword, emailsettings, featuresallocation, qaserver;
	@FXML
	private Label clientname, companylabel, securestatuslabel, qaserverlabel;
	@FXML
	private JFXButton logout;
	@FXML
	private JFXTextField qaservertext;

	public int localUserLevel = 0;
	public Date activatedDate = null;
	Stage myStage;
	SubScene ss;
	private static UsersDetailsBeanBinaryTrade currentUsersDetailsBeanBinaryTree;
	private static DashBoardController userHome = null;

	private int statuspanecount = 0, countforonehour = 0, userLevel = 0;

	private ArrayList<LocalUserLevelBeanBinaryTrade> localUserLevelArrayList = new ArrayList<LocalUserLevelBeanBinaryTrade>();

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
		// displayallstatuspanes();

		clientname.setText(
				Loggedinuserdetails.firstName.toUpperCase() + " " + Loggedinuserdetails.lastName.toUpperCase());
		companylabel.setText(Loggedinuserdetails.companyName.toUpperCase());

		Reflection r = new Reflection();
		r.setFraction(0.7f);

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
					CommonFunctions.message = "Secured Connection Lost. Please Restart Test My Data...!";
					CommonFunctions.invokeAlertBox(getClass());
					timer.cancel();

					StoreAuditLogger.logStoreTransaction(Loggedinuserdetails.userId, "Security", "", "", true,
							"VPN connection lost", "");

					System.exit(0);
				}
			}
		}, 5000, 30000); // First 1000 is to start after 1 sec, Second 60000
		// is to loop for every 1 min (1000 * 60 sec = 60000)
	}

	public static DashBoardController getInstance(UsersDetailsBeanBinaryTrade loggedInUsersDetailsBeanBinaryTree) {

		if (loggedInUsersDetailsBeanBinaryTree != null) {
			currentUsersDetailsBeanBinaryTree = loggedInUsersDetailsBeanBinaryTree;
		}
		return userHome;
	}

	public void runmethodBeforeScreenopens() {

	}

	private void importantmethods() {
		securestatus();
		SchedulerTime it = new SchedulerTime();
		it.run();
	}

	private void securestatus() {
		this.securestatuslabel.setVisible(true);
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

	@FXML
	private void logout(ActionEvent event) {
		CommonFunctions.message = "Please confirm to Logout from the System...!";
		CommonFunctions.invokeConfirmDialog(getClass());

		if (CommonFunctions.selectionstatus == "yes") {
			LogOut lock = new LogOut();
			lock.exit(event);
		}
	}

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
		myStage = (Stage) mymenubar.getScene().getWindow();
		nowcontrolreport();
	}

	public void nowcontrolreport() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "newcontrolreport";
						new MenuItemsFXHelper().initAndShowGUI(screenName);
						NewControlReportRulesController.getInstance();
					}
				});
			}
		});
	}

	@FXML
	private void fieldtofield() {
		myStage = (Stage) mymenubar.getScene().getWindow();
		nowfieldtofield();
	}

	public void nowfieldtofield() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "newfieldtofield";
						new MenuItemsFXHelper().initAndShowGUI(screenName);
						NewFieldtoFieldController.getInstance();
					}
				});
			}
		});
	}

	@FXML
	private void runcontrolreport() {
		myStage = (Stage) mymenubar.getScene().getWindow();
		executecontrolreport();
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
		myStage = (Stage) mymenubar.getScene().getWindow();
		executefieldtofield();
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
	private void newtestsuite(ActionEvent event) {
		myStage = (Stage) mymenubar.getScene().getWindow();
		runnewtestsuite(event);
	}

	public void runnewtestsuite(ActionEvent event) {
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
	private void executetestsuites(ActionEvent event) {
		myStage = (Stage) mymenubar.getScene().getWindow();
		runexecutetestsuite(event);
	}

	public void runexecutetestsuite(ActionEvent event) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "executetestsuite";
						new MenuItemsFXHelper().initAndShowGUI(screenName);
						ExecuteTestSuiteController.getInstance();
					}
				});
			}
		});
	}

	@FXML
	private void testreports(ActionEvent event) {
		myStage = (Stage) mymenubar.getScene().getWindow();
		runtestreports(event);
	}

	public void runtestreports(ActionEvent event) {
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
	private void adduser(ActionEvent event) {
		myStage = (Stage) mymenubar.getScene().getWindow();
		runadduser(event);
	}

	public void runadduser(ActionEvent event) {
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
	private void changepassword(ActionEvent event) {
		myStage = (Stage) mymenubar.getScene().getWindow();
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

		// runchangepassword(event);
	}

	public void runchangepassword(ActionEvent event) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "changepassword";
						new MenuItemsFXHelper().initAndShowGUI(screenName);
						ChangePasswordController.getInstance();
					}
				});
			}
		});
	}

	@FXML
	private void emailsettings() {
		myStage = (Stage) mymenubar.getScene().getWindow();
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
		// runemailsettings();
	}

	public void runemailsettings() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String screenName = "emailsettings";
						new MenuItemsFXHelper().initAndShowGUI(screenName);
						EmailSettingsController.getInstance();
					}
				});
			}
		});
	}

	@FXML
	private void featuresallocation() {
	}

	@FXML
	private void qaserver() {
		myStage = (Stage) mymenubar.getScene().getWindow();
		runqaserversettings();
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
				qaservertext.clear();

				if (QADefaultServerDetails.url.contains("jdbc:mysql://")) {
					if (qas.validateconnection(QADefaultServerDetails.servertype,
							QADefaultServerDetails.url.replaceAll("jdbc:mysql://", "").replaceAll("/", ""),
							QADefaultServerDetails.username, QADefaultServerDetails.password)) {

						qaservertext.setText("Connected QA Server : "
								+ QADefaultServerDetails.url.replaceAll("jdbc:mysql://", "").replaceAll("/", "") + " ( "
								+ QADefaultServerDetails.servertype.toUpperCase() + " )");
					} else {
						qaservertext.setText("Failed to Connected QA Server : "
								+ QADefaultServerDetails.url.replaceAll("jdbc:mysql://", "").replaceAll("/", ""));
					}
				} else if (QADefaultServerDetails.url.contains("jdbc:sqlserver://")) {
					if (qas.validateconnection(QADefaultServerDetails.servertype,
							QADefaultServerDetails.url.replaceAll("jdbc:sqlserver://", ""),
							QADefaultServerDetails.username, QADefaultServerDetails.password)) {

						qaservertext.setText("Connected QA Server : "
								+ QADefaultServerDetails.url.replaceAll("jdbc:sqlserver://", "") + " ( "
								+ QADefaultServerDetails.servertype.toUpperCase() + " )");
					} else {
						qaservertext.setText("Failed to Connected QA Server : "
								+ QADefaultServerDetails.url.replaceAll("jdbc:sqlserver://", ""));
					}
				}

			} else {
				qaservertext.setText("No Default QA Server Selected");
			}
			qaservertext.setStyle("-fx-text-fill: #edff00; -fx-font-weight:bold;");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
