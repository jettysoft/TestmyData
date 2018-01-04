package com.testmydata.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import com.testmydata.binarybeans.LocalUserLevelBeanBinaryTrade;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;

@SuppressWarnings("serial")
public class UserDashboard extends JFrame implements ActionListener {

	public JDesktopPane desktop;

	public static String currentUserLoginId = null, startDate = null, endDate = null;
	private static UserDashboard userHome = null;

	private static UsersDetailsBeanBinaryTrade currentUsersDetailsBean;

	private String startDate_Default = "", endDate_Default = "";
	private static String startDate_Default1 = "", endDate_Default1 = "";

	public String localUserLevel = null;
	public Date activatedDate = null;
	public String dbname = null;

	long daysCount;
	public int userLevel;

	public static UserDashboard getInstance(UsersDetailsBeanBinaryTrade loggedInUsersDetailsBean) {

		if (loggedInUsersDetailsBean != null) {
			currentUserLoginId = loggedInUsersDetailsBean.getUserId();
			currentUsersDetailsBean = loggedInUsersDetailsBean;
		}

		if (userHome == null) {
			userHome = new UserDashboard();
		} else {
			userHome.setVisible(true);
		}

		return userHome;
	}

	public UserDashboard() {

		// try {
		// BlockingQueue<SmsBean> queue = new ArrayBlockingQueue<SmsBean>(100);
		// SmsProducer producer = new SmsProducer(queue);
		// SmsConsumer con = new SmsConsumer(queue);
		//
		// new Thread(producer).start();
		// new Thread(con).start();
		// new Thread(new ShortMessageService()).start();
		// } catch (RuntimeException e1) {
		// e1.printStackTrace();
		// }
	}

	public void productFeaturesManagement() {

	}

	@SuppressWarnings("null")
	public void getlocaluserLevel() {

		ArrayList<?> localUserLevelArrayList = new DAO().getlocalUseLevelDetails(currentUsersDetailsBean.getId());

		if (localUserLevelArrayList != null || !localUserLevelArrayList.isEmpty()) {
			for (int i = 0; i < localUserLevelArrayList.size(); i++) {
				LocalUserLevelBeanBinaryTrade LocalUserLevelBeanBinaryTrade1 = (LocalUserLevelBeanBinaryTrade) localUserLevelArrayList
						.get(i);
				localUserLevel = LocalUserLevelBeanBinaryTrade1.getLocalUserLevel();
				activatedDate = LocalUserLevelBeanBinaryTrade1.getCreatedDate();
			}
		}
	}

	@SuppressWarnings("unused")
	public void trailValidation() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date trailEndDate = new Date();

		try {
			Date date1 = activatedDate;
			Date date2 = trailEndDate;
			long diff = date2.getTime() - date1.getTime();
			long daysCount = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

			if (daysCount > 30) {
				userLevel = Integer.parseInt(localUserLevel);
				if (userLevel == 11 || userLevel == 12 || userLevel == 13 || userLevel == 14 || userLevel == 15) {
					new DAO().blockTrailPeriod();
				}
			}
		} catch (ParseException e) {

		}

	}

	public void getPaymentEndDate() {

	}

	@SuppressWarnings("unused")
	private void invokeInactivityListener() {
		/*
		 * 
		 * 
		 * FileIOOperations fileOperation = new FileIOOperations(); Properties
		 * props = fileOperation.readPropertyFile("sessionLock.properties");
		 * boolean session =
		 * Boolean.parseBoolean(EncryptAndDecrypt.decryptData(props.getProperty(
		 * "isSessionEnabled"))); if (session){ javax.swing.Action s = new
		 * InactivityEventManager(); InactivityListener ssd = new
		 * InactivityListener(s,
		 * Integer.parseInt(EncryptAndDecrypt.decryptData(props.getProperty(
		 * "timeDuration")))); ssd.start(); }
		 */
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
	}

	public static UserDashboard getDashboardState() {
		return userHome;
	}

	public static void resetUser(String user) {
		currentUserLoginId = user;
	}

	public static UserDashboard setInstance() {
		if (userHome != null) {
			JOptionPane.showMessageDialog(null, "Sucessfully Session Logged Out.", "LogOut",
					JOptionPane.INFORMATION_MESSAGE);
			userHome.dispose();
			userHome = null;
		}
		return null;
	}

	public void populateDatesDefaultValues() {
		try {
			Properties dafaultValuesPF = new Properties();
			FileInputStream fis = new FileInputStream(
					new File(".", "/conf/dafaultValues.properties").getAbsolutePath());
			dafaultValuesPF.load(fis);

			if (dafaultValuesPF != null && !dafaultValuesPF.isEmpty()) {
				startDate_Default = dafaultValuesPF.getProperty("StartDate");
				endDate_Default = dafaultValuesPF.getProperty("EndDate");
				if (startDate_Default == null)
					startDate_Default = "";
				if (endDate_Default == null)
					endDate_Default = "";
			}

			fis.close();

		} catch (Exception e) {

			startDate_Default = "";
			endDate_Default = "";
		}
	}

	public static void populateDatesDefaultValues1() {
		try {
			Properties dafaultValuesPF = new Properties();
			FileInputStream fis = new FileInputStream(
					new File(".", "/conf/dafaultValues.properties").getAbsolutePath());
			dafaultValuesPF.load(fis);

			if (dafaultValuesPF != null && !dafaultValuesPF.isEmpty()) {
				startDate_Default1 = dafaultValuesPF.getProperty("StartDate");
				endDate_Default1 = dafaultValuesPF.getProperty("EndDate");
				if (startDate_Default1 == null)
					startDate_Default1 = "";
				if (endDate_Default1 == null)
					endDate_Default1 = "";
			}

			fis.close();

		} catch (Exception e) {
			startDate_Default1 = "";
			endDate_Default1 = "";
		}
	}

}
