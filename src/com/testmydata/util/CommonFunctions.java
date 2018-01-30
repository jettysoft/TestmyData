package com.testmydata.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.JDesktopPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.testmydata.binarybeans.DBConfigBinaryTade;
import com.testmydata.dao.DAO;
import com.toedter.calendar.JDateChooser;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CommonFunctions {
	public static int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static int runcount = 0, passcount = 0, failcount = 0;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@SuppressWarnings("unused")
	private static final String dateFormat_MMDDYYYY = "^(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d$";
	@SuppressWarnings("unused")
	private static final String dateFormat_DDMMYYYY = "^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$";

	private static final String[] monthNmaes = { "January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December" };

	public static String dbName = null;
	public static String message, queryresult, teststatus, selectedcashchequeaccount, selectedcashchequeaccountId,
			selectedcashchequeaccount1, selectedcashchequeaccount1Id, selecteddepositaccount, selecteddepositaccountId,
			selectedcardaccount, selectedcardaccountId, paymentMode, notes, chequenumber, selectionstatus = null,
			companyname, selectedemployeename, selectprojectorbranchid = null, selectedshiftid = null,
			selectedshiftstartime, selectedshiftendtime, selectedtotaltime, selectedspecialdate,
			selectedprojectstatus = null, selectedTime = null, selecteddate = null, selectendtime = null,
			selectstarttime = null, calculatedreturnamount = null, dialogokstatus = null;

	static Stage pleasewaitstage;

	public static ArrayList<String> returnsidlist = new ArrayList<>();
	public static ArrayList<String> imageslist = new ArrayList<String>();

	public Dimension getCurrentScreenDimensions() {

		Dimension currentScreenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) currentScreenDimensions.getWidth();
		screenHeight = (int) currentScreenDimensions.getHeight();

		return currentScreenDimensions;
	}

	@SuppressWarnings({ "unused", "deprecation" })
	public static JSpinner getTimeChooser() {

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getTime().getHours()); // 24
																			// ==
																			// 12
																			// PM
																			// ==
																			// 00:00:00
		calendar.set(Calendar.MINUTE, calendar.getTime().getMinutes());
		calendar.set(Calendar.SECOND, calendar.getTime().getSeconds());

		SpinnerDateModel model = new SpinnerDateModel();
		model.setValue(calendar.getTime());

		JSpinner spinner = new JSpinner(model);

		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm:ss");
		DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();
		formatter.setAllowsInvalid(false); // this makes what you want
		formatter.setOverwriteMode(true);

		spinner.setEditor(editor);
		return spinner;
	}

	public static Double roundOf(double a) {
		double roundOff = Math.round(a * 100.0) / 100.0;
		return roundOff;
	}

	public Dimension getInternalFrameDimension(JDesktopPane desktopPane) {

		Dimension currentFrameDimension = desktopPane.getSize();

		return currentFrameDimension;

	}

	public boolean validateEmail(String email) {
		boolean returnValue = false;

		try {

			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(email);

			returnValue = matcher.matches();
		} catch (Exception e) {

			returnValue = false;
		}

		return returnValue;
	}

	public MaskFormatter createNumberFormatter(String s) {
		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter(s);
		} catch (java.text.ParseException exc) {
			// System.err.println("formatter is bad: " + exc.getMessage());
			System.exit(-1);
		}
		return formatter;
	}

	@SuppressWarnings("unused")
	public boolean validateDateFormat(String givenDate, String dateFormat) {

		boolean returnValue = false;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(dateFormat);
			Date date = sdf.parse(givenDate);

			returnValue = true;

		} catch (Exception e) {

			returnValue = false;
		}

		return returnValue;
	}

	public int returnMonthUtility(int value) {

		int val = 0;

		if (value == 0) {
			val = 01;

		} else if (value == 1) {
			val = 02;

		} else if (value == 2) {
			val = 03;

		} else if (value == 3) {
			val = 04;

		} else if (value == 4) {
			val = 05;

		} else if (value == 5) {
			val = 06;

		} else if (value == 6) {
			val = 07;

		} else if (value == 7) {
			val = 8;

		} else if (value == 8) {
			val = 9;

		} else if (value == 9) {
			val = 10;

		} else if (value == 10) {
			val = 11;

		} else if (value == 11) {
			val = 12;

		}
		return val;

	}

	public boolean isANumber(String number) {
		boolean returnValue = false;

		try {
			String NUMBER_PATTERN = "^[0-9]+ *$";
			Pattern pattern = Pattern.compile(NUMBER_PATTERN);
			Matcher matcher = pattern.matcher(number);

			returnValue = matcher.matches();
		} catch (Exception e) {

			returnValue = false;
		}

		return returnValue;
	}

	public static String getFormattedDate(Date givenDate) {

		String convertedDate = null;
		try {

			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			convertedDate = formatter.format(givenDate);

		} catch (Exception e) {
			convertedDate = null;
		}

		return convertedDate;
	}

	public static String getFormattedDate1(LocalDate givenDate) {

		String convertedDate = null;
		try {

			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			convertedDate = formatter.format(givenDate);

		} catch (Exception e) {
			convertedDate = null;
		}

		return convertedDate;
	}

	public static String compareDates(Date startDate, Date endDate) {

		String returnValue = "failure";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDate_String = sdf.format(startDate);
			String endDate_String = sdf.format(endDate);
			Date startDate_Con = sdf.parse(startDate_String);
			Date endDate_Con = sdf.parse(endDate_String);

			if (startDate_Con.compareTo(endDate_Con) > 0) {
				returnValue = "after";
			} else if (startDate_Con.compareTo(endDate_Con) < 0) {
				returnValue = "before";
			} else if (startDate_Con.compareTo(endDate_Con) == 0) {
				returnValue = "equal";
			}

		} catch (Exception e) {
			returnValue = "failure";
		}

		return returnValue;
	}

	public static String validateWeekDates(Date startDate, Date endDate) {

		String returnValue = "failure";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDate_String = sdf.format(startDate);
			String endDate_String = sdf.format(endDate);
			Date startDate_Con = sdf.parse(startDate_String);
			Date endDate_Con = sdf.parse(endDate_String);

			DateTime date1 = new DateTime(startDate_Con);
			DateTime date2 = new DateTime(endDate_Con);
			DateTime dtPlusOne = date2.plusDays(1);

			int days = Days.daysBetween(date1, dtPlusOne).getDays();

			if (startDate_Con.compareTo(endDate_Con) >= 7) {
				returnValue = "after";
			} else if (days % 7 == 0) {
				returnValue = "valid";
			} else {
				returnValue = "sevendayfailure";
			}

		} catch (Exception e) {
			returnValue = "failure";
		}

		return returnValue;
	}

	public String compareDates1(LocalDate startDate, LocalDate endDate) {

		String returnValue = "failure";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDate_String = sdf.format(startDate);
			String endDate_String = sdf.format(endDate);
			Date startDate_Con = sdf.parse(startDate_String);
			Date endDate_Con = sdf.parse(endDate_String);

			if (startDate_Con.compareTo(endDate_Con) > 0) {
				returnValue = "after";
			} else if (startDate_Con.compareTo(endDate_Con) < 0) {
				returnValue = "before";
			} else if (startDate_Con.compareTo(endDate_Con) == 0) {
				returnValue = "equal";
			}

		} catch (Exception e) {
			returnValue = "failure";
		}

		return returnValue;
	}

	public String genRandomString(int length) {

		final String AB = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();

		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	public String genRandomBarcodeString(int length) {

		final String AB = "123456789";
		Random rnd = new Random();

		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	public boolean isStringContainsAlphabet(String projectName) {
		boolean returnValue = false;

		try {
			Pattern pattern = Pattern.compile("^.*[a-zA-Z].*$");
			Matcher matcher = pattern.matcher(projectName);

			returnValue = matcher.matches();
		} catch (Exception e) {

			returnValue = false;
		}

		return returnValue;
	}

	public static Date getDateFromString(String givenDate) {

		Date convertedDate = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			convertedDate = formatter.parse(givenDate);

		} catch (Exception e) {
			convertedDate = null;
		}
		return convertedDate;
	}

	public String getYearMonthFromStringDate(String givenDate) {

		String month = null;
		try {
			String[] dateParts = givenDate.split("-");
			month = dateParts[0] + "-" + dateParts[1];

		} catch (Exception e) {
			month = null;
		}

		return month;
	}

	public double calculateTaxByPercentage(double amount, double percentage) {

		return (amount / 100) * percentage;
	}

	public boolean isValidNoOfHours(String givenNoOfHours) {

		boolean returnValue = false;

		if (givenNoOfHours.contains(".")) {
			String[] hoursTokens = givenNoOfHours.split("\\.");

			if (hoursTokens.length == 1)
				returnValue = false;
			else {
				if (hoursTokens[1].length() > 1)
					returnValue = false;
				else
					returnValue = true;
			}
		} else {
			returnValue = true;
		}

		return returnValue;
	}

	public static long getDateInMillis(JDateChooser mydatechooser) {
		try {
			Calendar cal = mydatechooser.getCalendar();
			return cal.getTimeInMillis();
		} catch (NullPointerException e) {

		}
		return 0;
	}

	public static long toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}

	public boolean isAMonth(String givenMonth) {
		boolean returnValue = false;
		for (String month : monthNmaes) {
			if (month.equals(givenMonth))
				returnValue = true;
		}

		return returnValue;
	}

	public static String formatAmount(double amount) {
		return BigDecimal.valueOf(Math.round(amount * 100.0) / 100.0).toPlainString();
	}

	public boolean isValidIPAddress(String ipAddress) {
		boolean returnValue = false;
		try {
			StringTokenizer ipAddressTokenizers = new StringTokenizer(ipAddress, ".");

			if (ipAddressTokenizers.countTokens() == 4)
				returnValue = true;
			else
				returnValue = false;
		} catch (Exception e) {

			returnValue = false;
		}
		return returnValue;
	}

	@SuppressWarnings({ "rawtypes" })
	public static void updateCompanyName(String Ip) {
		String IP = Ip.toLowerCase().replaceAll(" ", "");
		dbName = IP;

		boolean flag = false;
		HashMap<String, String> newProps = new HashMap<String, String>();
		FileIOOperations fileop = new FileIOOperations();
		Properties props = fileop.readPropertyFile("dafaultValues.properties");
		if (!(props.containsKey("CompanyName"))) {
			flag = true;
		}
		Enumeration enumProps = props.propertyNames();
		String key = "";
		while (enumProps.hasMoreElements()) {
			key = (String) enumProps.nextElement();
			if (key.equals("CompanyName")) {
				newProps.put(key, IP);
			} else {
				newProps.put(key, props.getProperty(key));
			}
		}
		if (flag) {
			newProps.put("CompanyName", IP);
		}
		fileop.createPropertyFile(newProps, "dafaultValues.properties");

		// Updating companyname in dbconfig.xml
		String companyName = IP;
		String upToNCharacters = companyName.substring(0, Math.min(companyName.length(), 40));
		try {
			DBConfigJAXB dbC = new DBConfigJAXB();
			DBConfigBinaryTade dbConfig = dbC.readDBConfig();

			dbConfig.setDbName(EncryptAndDecrypt.encryptData(upToNCharacters.toLowerCase()));

			dbC.generateDBConfig(dbConfig);
		} catch (Exception e1) {

		}
	}

	public static void invokeAlertBox(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/Dialog.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setAlwaysOnTop(true);
		stage.showAndWait();

	}

	public static void invokeTestResultsDialog(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/testresultsdialog.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setAlwaysOnTop(true);
		stage.showAndWait();
	}

	public static void invokeChangepassword(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/ChangePassword.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setAlwaysOnTop(true);
		stage.showAndWait();
	}

	public static void invokeCashInput(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/SelectBankChequeAccount.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokeCreditInput(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/SelectBankCreditAccount.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokeNotesInput(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/NotesWithDrawl.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokeChequeNumberInput(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/ChequeNumberInput.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokePaymentSelection(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/PaymentModeSelection.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokeConfirmDialog(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/ConfirmDialog.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokeAddMissingInfoDialog(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader
					.load(className.getResource("/com/testmydata/fxml/AddMissingInformationVerifyScheduler.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokeAddTimeforModificationDialog(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {

			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/AddTimeforModification.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokeBiTaxModificationDialog(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {

			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/BITaxModifications.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void salesReturnInvoiceDetailsTracker(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/RetailsSalesReturnInvoiceTracker.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokeSQLScriptController(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/sqlscriptview.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	// @SuppressWarnings("static-access")
	// public static void invokeAddTowTimeforModificationDialog(Class<?>
	// className, String projbasedornot, Time srtTime,
	// Time endTime, Stage stage1) {
	// Stage stage = new Stage();
	// Parent root = null;
	// try {
	// FXMLLoader fxmlLoader = new FXMLLoader(
	// className.getResource("/com/testmydata/fxml/AddTwoTimeForModificationController.fxml"));
	//
	// AddTwoTimeForModificationController ct =
	// fxmlLoader.<AddTwoTimeForModificationController>getController();
	// ct.setProjectbasedornot(projbasedornot);
	// if (srtTime != null) {
	// ct.setSrtTime(srtTime);
	// }
	// if (endTime != null) {
	// ct.setEndtime(endTime);
	// }
	// root = (Parent) fxmlLoader.load();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// stage.setScene(new Scene(root));
	// stage.initStyle(StageStyle.UNDECORATED);
	// stage.initModality(Modality.APPLICATION_MODAL);
	// stage.setAlwaysOnTop(true);
	// stage.initOwner(stage1);
	// stage.showAndWait();
	// }

	public static void invokeDatePickerforModificationDialog(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/DatePickerforModification.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokeCompanyNameAddingDialog(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/CompanyNameAdding.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokeEmployeeSelectionforInvoice(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/EmployeeSelectionforDelivery.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokeTrackOrder(Class<?> className) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/TrackingOrder.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static void invokePleaseWait(Class<?> className, Boolean result) {
		pleasewaitstage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(className.getResource("/com/testmydata/fxml/PleaseWait.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pleasewaitstage.setScene(new Scene(root));
		pleasewaitstage.initStyle(StageStyle.UNDECORATED);
		pleasewaitstage.initModality(Modality.APPLICATION_MODAL);
		try {
			Thread.sleep(25);

			if (result == true) {
				pleasewaitstage.hide();
			} else {
				Thread.sleep(25);
				pleasewaitstage.hide();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void pleasewaithide() {
		// System.out.println(pleasewaitstage);
		pleasewaitstage.hide();
	}

	public static String getCompanyName() {
		return dbName;
	}

	public static List<String> getDatesBeteenDates(String srtDate, String edDate) {
		List<Date> dates = new ArrayList<Date>();
		List<String> returnDates = new ArrayList<String>();

		DateFormat formatter;

		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = formatter.parse(srtDate);
			Date endDate = formatter.parse(edDate);
			long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
			long endTime = endDate.getTime(); // create your endtime here,
												// possibly using Calendar or
												// Date
			long curTime = startDate.getTime();
			while (curTime <= endTime) {
				dates.add(new Date(curTime));
				curTime += interval;
			}
			for (int i = 0; i < dates.size(); i++) {
				Date lDate = dates.get(i);
				// String ds = formatter.format(lDate);
				returnDates.add(formatter.format(lDate));
			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return returnDates;
	}

	public static List<Date> getDatesBetweenDates(String srtDate, String edDate) {
		List<Date> dates = new ArrayList<Date>();

		DateFormat formatter;

		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = formatter.parse(srtDate);
			Date endDate = formatter.parse(edDate);
			long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
			long endTime = endDate.getTime(); // create your endtime here,
												// possibly using Calendar or
												// Date
			long curTime = startDate.getTime();
			while (curTime <= endTime) {
				dates.add(new Date(curTime));
				curTime += interval;
			}

		} catch (ParseException e) {

			e.printStackTrace();
		}
		return dates;
	}

	public static String formatDateToString(Date date) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	@SuppressWarnings("deprecation")
	public static Time getTime(String str) {
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Date date = null;
		try {
			date = formatter.parse(str);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		// System.out.println(date.getHours()+":"+date.getMinutes()+":"+date.getSeconds());
		return new Time(date.getHours(), date.getMinutes(), date.getSeconds());
	}

	public static Date getDateFromDateString(String dateStr) {

		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Date date = null;
		try {
			date = formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	@SuppressWarnings("deprecation")
	public static Time compareTwoTimeStamps(java.sql.Time currentTime, java.sql.Time oldTime) {
		long milliseconds1 = currentTime.getTime();
		long milliseconds2 = oldTime.getTime();

		long diff = milliseconds2 - milliseconds1;
		// long diffSeconds = diff / 1000;
		long diffHours = diff / (60 * 60 * 1000);
		long diffMinutes = (diff - diffHours * 3600 * 1000) / (60 * 1000);
		// long diffDays = diff / (24 * 60 * 60 * 1000);

		Time t = new Time((int) diffHours, (int) diffMinutes, 0);
		// return diffHours;
		return t;
	}

	@SuppressWarnings("deprecation")
	public static Time getTimeDifference(String d1d, String d2d) {
		Date d1 = getDateFromDateString(d1d);
		Date d2 = getDateFromDateString(d2d);
		// System.out.println("D1:"+d1+ " D2:"+d2);
		long d = d2.getTime() - d1.getTime();
		long hh = d / (3600 * 1000);
		long mm = (d - hh * 3600 * 1000) / (60 * 1000);
		// System.out.println(hh+":"+mm);
		Time t = new Time((int) hh, (int) mm, 0);
		return t;
	}

	@SuppressWarnings("deprecation")
	public static Date getStartDateOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, cal.getTime().getHours()); // ! clear
																	// would not
																	// reset the
																	// hour of
																	// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.setFirstDayOfWeek(2);
		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

		return cal.getTime();
	}

	@SuppressWarnings("deprecation")
	public static Date getEndDateOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, cal.getTime().getHours()); // ! clear
																	// would not
																	// reset the
																	// hour of
																	// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

		cal.add(Calendar.DATE, 7);

		return cal.getTime();
	}

	@SuppressWarnings("deprecation")
	public static Date getStartDateOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.getTime().getHours()); // ! clear
																	// would not
																	// reset the
																	// hour of
																	// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.setFirstDayOfWeek(2);
		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

		return cal.getTime();
	}

	@SuppressWarnings("deprecation")
	public static Date getEndDateOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.getTime().getHours()); // ! clear
																	// would not
																	// reset the
																	// hour of
																	// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

		cal.add(Calendar.DATE, 7);

		return cal.getTime();
	}

	public static Date addWeeks(Date date, int range) {

		DateTime dt = new DateTime(date);
		dt = dt.plusWeeks(range).minusDays(1);
		Date returnDate = dt.toDate();
		return returnDate;

	}

	public static Date addMonths(Date date, int range) {

		DateTime dt = new DateTime(date);
		dt = dt.plusMonths(range).minusDays(1);
		Date returnDate = dt.toDate();
		return returnDate;
	}

	public static Date addDays(Date date, int range) {

		DateTime dt = new DateTime(date);
		dt = dt.plusDays(range).minusDays(1);
		Date returnDate = dt.toDate();
		return returnDate;
	}

	public static Date minusDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}

	public static String sumDates(String[] dateStrings) {
		DateFormat df = new SimpleDateFormat("hh:mm:ss");
		int secs = 0, mins = 0, hrs = 0;
		Calendar calendar = Calendar.getInstance();
		for (int i = 0; i < dateStrings.length; i++) {
			String dateString = dateStrings[i];
			Date date = null;
			try {
				date = df.parse(dateString);
			} catch (ParseException e) {

				e.printStackTrace();
			}
			calendar.setTime(date);
			secs += calendar.get(Calendar.SECOND);
			mins += calendar.get(Calendar.MINUTE);
			hrs += calendar.get(Calendar.HOUR);
			// System.out.println(hrs+":"+mins+":"+secs);
		}
		return hrs + ":" + mins + ":" + secs;
		/*
		 * calendar.set(0, 0, 0, hrs, mins, secs); return
		 * df.format(calendar.getTime());
		 */
	}

	public static int getTotalWeeksInYear() {
		Calendar mCalendar = new GregorianCalendar(TimeZone.getDefault());
		mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
		// Workaround
		mCalendar.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.DECEMBER, 31);
		int totalDaysInYear = mCalendar.get(Calendar.DAY_OF_YEAR);
		int totalWeeks = totalDaysInYear / 7;
		return totalWeeks;
	}

	public static int getTotalDaysInYear() {
		Calendar mCalendar = new GregorianCalendar(TimeZone.getDefault());
		mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
		// Workaround
		mCalendar.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.DECEMBER, 31);
		int totalDaysInYear = mCalendar.get(Calendar.DAY_OF_YEAR);
		return totalDaysInYear;
	}

	public static ArrayList<String> getProvinceList(String countryName) {
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = null;
		ArrayList<String> pp = new ArrayList<String>();
		try {

			jsonArray = (JSONArray) parser.parse(new FileReader(new File(".", "exports" + File.separator + "json"
					+ File.separator + "countries" + File.separator + countryName + ".json").getAbsolutePath()));

			for (Object o : jsonArray) {
				JSONObject person = (JSONObject) o;

				String strName = (String) person.get("name");
				// System.out.println("Name::::" + strName);
				pp.add(strName);
				// System.out.println();
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		return pp;
	}

	public static ArrayList<String> getCountryList() {
		ArrayList<String> country = new ArrayList<String>();
		File[] files = new File("exports" + File.separator + "json" + File.separator + "countries").listFiles();
		// If this pathname does not denote a directory, then listFiles()
		// returns null.
		for (File file : files) {
			if (file.isFile()) {
				String fileName = file.getName();
				int pos = fileName.lastIndexOf(".");
				if (pos > 0) {
					fileName = fileName.substring(0, pos);
					country.add(fileName);
				}

			}
		}
		return country;
	}

	public static Date getPayPeriodEndDate(String subperiodType, Date date, double length) {
		Date dd = null;
		Date dt = date;
		Double len = length;
		if (subperiodType.equalsIgnoreCase("Weeks")) {
			dd = CommonFunctions.addWeeks(dt, len.intValue());

		} else if (subperiodType.equalsIgnoreCase("Months")) {
			dd = CommonFunctions.addMonths(dt, len.intValue());

		} else if (subperiodType.equalsIgnoreCase("Days")) {
			dd = CommonFunctions.addDays(dt, len.intValue());

		} else if (subperiodType.equalsIgnoreCase("1-15,16-End")) {

		}
		return dd;
	}

	public static String calculateTotalTimeFromTimeArray(Time[] time) {
		int hours = 0, min = 0, sec = 0;
		for (Time t : time) {
			Date date = new Date(t.getTime());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			sec += calendar.get(Calendar.SECOND);
			min += calendar.get(Calendar.MINUTE);
			hours += calendar.get(Calendar.HOUR);
		}
		return hours + ":" + min + ":" + sec;
	}

	@SuppressWarnings("serial")
	public static void changeRowColor(final Color bckcolorOne, final Color bckColorTwo, JTable table,
			final Color foreColorOne, final Color foreColorTwo) {
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				c.setBackground(row % 2 == 0 ? bckcolorOne : bckColorTwo);
				c.setForeground(row % 2 == 0 ? foreColorOne : foreColorTwo);
				return c;
			}
		});
	}

	public static void changeHeader(JTable table, final Color headerBackg, final Color headerText, int j) {

		DefaultTableCellRenderer header = new DefaultTableCellRenderer();
		header.setFont(header.getFont().deriveFont(Font.BOLD));
		// header.setFont(CustomFonts.MenuItemsFont);
		header.setBackground(headerBackg);
		header.setForeground(headerText);
		TableColumnModel model = table.getColumnModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			model.getColumn(i).setHeaderRenderer(header);
			if (i == j) {

			}

		}
	}

	public void updateKeyProperties() {
		Properties keysPropertiesFile = new Properties();
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(".", "/conf/keys.properties").getAbsolutePath());
			keysPropertiesFile.setProperty("isUserAlreadyRegisted", EncryptAndDecrypt.encryptData("true"));
			keysPropertiesFile.store(fos, new Date().toString());
			fos.close();
		} catch (IOException e) {

		}

	}

	// Pattern Email Validation
	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	// convert InputStream to String
	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();
	}

	public static void listImagesinFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listImagesinFolder(fileEntry);
			} else {
				imageslist.add(fileEntry.getName());
			}
		}
	}

	public static LocalDate getdateforpicker(int days) {
		LocalDate date = null;
		try {
			DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			SimpleDateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = new Date();
			Date todaydate = new DateTime(date1).minusDays(days).toDate();

			final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			date = LocalDate.parse(writeFormat.format(formatter.parse(todaydate.toString())), dtf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static boolean updatetables(String tableName, String columnname, String columnvalue, String wherecolumn,
			String wherecolumnvalue, String successmessage, String failmessage, int reset, int runcount1) {
		boolean result = true;

		if (reset == 1) {
			runcount = 0;
			passcount = 0;
			failcount = 0;
		}
		runcount++;
		String result1 = new DAO().updatetabledata(tableName, columnname, columnvalue, wherecolumn, wherecolumnvalue);
		if (result1.equals("success")) {
			passcount++;
		} else {
			failcount++;
		}

		if (runcount == runcount1 && failcount > 0) {
			result = false;
			CommonFunctions cf = new CommonFunctions();
			cf.runmessage(failmessage);
		} else if (runcount == runcount1 && passcount > 0) {
			CommonFunctions cf = new CommonFunctions();
			cf.runmessage(successmessage);
		}

		return result;
	}

	// Method to display the Messages
	public void runmessage(String message1) {
		message = message1;
		invokeAlertBox(getClass());
	}
}
