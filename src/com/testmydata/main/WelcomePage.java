package com.testmydata.main;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import com.sun.javafx.scene.control.skin.FXVK;
import com.testmydata.binarybeans.LocalUserLevelBeanBinaryTrade;
import com.testmydata.binarybeans.UserTypeBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxcontroller.controllSplash;
import com.testmydata.fxhelpers.LoginFXHelper;
import com.testmydata.fxutil.DBnameUpdateJAXB;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.EncryptAndDecrypt;
import com.testmydata.util.FileIOOperations;
import com.testmydata.vpn.VpnConnectionThread;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

public class WelcomePage {

	public static String localUserLevel = null;
	public static Date activatedDate = null;
	public static String dbname = null;
	public static Date paymentEndDate = null;
	long daysCount;
	public static int userLevel;

	String isValidKey = "";

	public WelcomePage() {
	}

	public static void main(String[] args) throws Exception {
		launchApp();
		VpnConnectionThread.launch();
		// setVirtualKeyboardCSS();
	}

	public static void launchApp() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// code to launch splash screen
				String screenName = "splash";
				controllSplash.getInstance("welcome", null, null, null);
				new LoginFXHelper().initAndShowGUI(screenName);
			}
		});
	}

	public void run() {
		try {
			Properties keysPropertiesFile = new Properties();
			FileInputStream fis = new FileInputStream(new File(".", "/conf/keys.properties").getAbsolutePath());
			keysPropertiesFile.load(fis);

			if (VpnConnectionThread.isVpnConnected()) {

				if (keysPropertiesFile.getProperty("isUserAlreadyRegisted") != null && EncryptAndDecrypt
						.decryptData(keysPropertiesFile.getProperty("isUserAlreadyRegisted")).equals("false")) {
					welcomeApp();
				} else {
					loginApp();
				}
			} else {
				vpnmessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void welcomeApp() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				String screenName = "welcomepage";
				new LoginFXHelper().initAndShowGUI(screenName);
			}
		});
	}

	@SuppressWarnings({ "static-access" })
	public static void loginApp() {
		try {
			FileIOOperations fileOperation = new FileIOOperations();

			CommonFunctions comm = new CommonFunctions();
			Properties defIP = fileOperation.readPropertyFile("dafaultValues.properties");

			if (defIP.getProperty("CompanyName") == null || defIP.getProperty("CompanyName") == ""
					|| defIP.getProperty("CompanyName").isEmpty()
					|| defIP.getProperty("CompanyName").trim().equals("trademen")) {

				new WelcomePage().companynamedialog();

			} else {
				comm.updateCompanyName(defIP.getProperty("CompanyName"));

				DBnameUpdateJAXB dcrdb = new DBnameUpdateJAXB();
				dcrdb.decrypt();

				String status = new DAO().checkDataBaseExist(dcrdb.decrypteddbanme);

				if (status.contains("notExisted") || status.contains("failure")) {
					new WelcomePage().companynamedialog();
				} else if (status.contains("existed")) {
					validloginApp();
					getlocaluserLevel();
					paymentActiveValidation();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void vpnmessage() {
		CommonFunctions.message = "Unable to Establish Secure Connection. System will not run with out Security. Please check the Security Instructions and set accordingly.";
		CommonFunctions.invokeAlertBox(WelcomePage.class);
	}

	private void companynamedialog() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				String screenName = "companaynameadding";
				new LoginFXHelper().initAndShowGUI(screenName);
			}
		});
	}

	public static void validloginApp() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// code to launch splash screen
				new LoginFXHelper().initAndShowGUI("loginScreen");
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "null" })
	public static void getlocaluserLevel() {

		ArrayList localUserLevelArrayList = new DAO().getlocalUseLevelDetails(1);

		if (localUserLevelArrayList != null || !localUserLevelArrayList.isEmpty()) {
			for (int i = 0; i < localUserLevelArrayList.size(); i++) {
				LocalUserLevelBeanBinaryTrade LocalUserLevelBeanBinaryTrade1 = (LocalUserLevelBeanBinaryTrade) localUserLevelArrayList
						.get(i);
				localUserLevel = LocalUserLevelBeanBinaryTrade1.getLocalUserLevel();
				activatedDate = LocalUserLevelBeanBinaryTrade1.getCreatedDate();
				// System.out.println(localUserLevel);
			}
		}
	}

	@SuppressWarnings({ "unused", "rawtypes", "null" })
	public static void paymentActiveValidation() {

		DBnameUpdateJAXB dcrdb = new DBnameUpdateJAXB();
		dcrdb.decrypt();

		String status = new DAO().checkDataBaseExist(dcrdb.decrypteddbanme);

		if (status.contains("notExisted") || status.contains("failure")) {
			new WelcomePage().companynamedialog();
		} else if (status.contains("existed")) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date currentDate = new Date();

			ArrayList webUserPaymentDateArrayList = new DAO().getwebUserPaymentDetails(dcrdb.decrypteddbanme);

			if (webUserPaymentDateArrayList != null || !webUserPaymentDateArrayList.isEmpty()) {
				for (int i = 0; i < webUserPaymentDateArrayList.size(); i++) {
					UserTypeBeanBinaryTrade UserTypeBeanBinaryTrade = (UserTypeBeanBinaryTrade) webUserPaymentDateArrayList
							.get(i);
					paymentEndDate = UserTypeBeanBinaryTrade.getUserPaymentValidDate();
				}
			}

			Date date1 = paymentEndDate;
			Date date2 = currentDate;
			long diff = date2.getTime() - date1.getTime();
			long daysCount = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

			userLevel = Integer.parseInt(localUserLevel);

			if (daysCount > 0) {
				if (userLevel == 1 || userLevel == 2 || userLevel == 3 || userLevel == 4 || userLevel == 5
						|| userLevel == 6) {
					new DAO().blockTrailPeriod();
				}
			} else if (daysCount < 0) {
				if (userLevel == 1 || userLevel == 2 || userLevel == 3 || userLevel == 4 || userLevel == 5
						|| userLevel == 6) {
					new DAO().activateUserPeriod();
				}
			}
		}

	}

	public static void setVirtualKeyboardCSS() {
		@SuppressWarnings("deprecation")
		final Iterator<Window> windows = Window.impl_getWindows();

		while (windows.hasNext()) {
			final Window window = windows.next();
			if (window instanceof PopupWindow) {
				if (window.getScene() != null && window.getScene().getRoot() != null) {
					Parent root = window.getScene().getRoot();
					if (root.getChildrenUnmodifiable().size() > 0) {
						Node popup = root.getChildrenUnmodifiable().get(0);
						if (popup.lookup(".fxvk") != null) {
							if (popup instanceof FXVK) {

								FXVK keyboard = (FXVK) popup.lookup(".fxvk");
								ObservableList<String> sheets = keyboard.getStylesheets();
								sheets.add("/com/testmydata/css/VirtualKeyBoard.css");
								System.out.println("Setting keyboard stylesheet");
							}
						}
					}
				}
			}
		}
	}
}
