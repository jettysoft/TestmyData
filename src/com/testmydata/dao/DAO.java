package com.testmydata.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import com.testmydata.auditlog.StoreAuditLogger;
import com.testmydata.binarybeans.ControlReportExecutionBinaryTrade;
import com.testmydata.binarybeans.ControlReportHelperBinaryTrade;
import com.testmydata.binarybeans.ControlReportRulesBinaryTrade;
import com.testmydata.binarybeans.DBConfigBinaryTade;
import com.testmydata.binarybeans.FieldtoFieldBinaryTrade;
import com.testmydata.binarybeans.LocalUserLevelBeanBinaryTrade;
import com.testmydata.binarybeans.ModulesBinaryTrade;
import com.testmydata.binarybeans.ProjectsBeanBinaryTrade;
import com.testmydata.binarybeans.QAServerDetailsBinaryTrade;
import com.testmydata.binarybeans.TestScenariosBinaryTrade;
import com.testmydata.binarybeans.TestSuiteBinaryTrade;
import com.testmydata.binarybeans.UserTypeBeanBinaryTrade;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dashboardfunction.Rule;
import com.testmydata.dashboardfunction.TestSuite;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.DBConfigJAXB;
import com.testmydata.util.EncryptAndDecrypt;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.TrademenException;

@SuppressWarnings("rawtypes")
public class DAO {
	private String dbName;

	private Connection con, fcon;
	private Statement st, st1, st2;
	private PreparedStatement ps;
	private ResultSet rs, rs1, rs2;
	static StringBuffer messageInfo = null;

	String sql = "", fsql = "", crsql = "";

	@SuppressWarnings("unused")
	private static UsersDetailsBeanBinaryTrade currentUsersDetailsBeanBinaryTree;
	private static DAO userHome = null;
	int incomeoverview = 1, expensesoverview = 1, projectsoverview = 1;
	DecimalFormat df = new DecimalFormat("#.00");

	@SuppressWarnings("static-access")
	public DAO() {
		try {
			con = new DBAccess().getConnection();

			if (con == null)
				try {
					con = new DBAccess().getConnection();
				} catch (Exception ignore) {
				}

			dbName = new DBAccess().getDatabaseName();

			// fis.close();

		} catch (Exception e) {

		}
	}

	public DAO(String dbName) {
		// con = new DBAccess(dbName).getConnection();
	}

	public static DAO getInstance(UsersDetailsBeanBinaryTrade loggedInUsersDetailsBeanBinaryTree) {
		if (loggedInUsersDetailsBeanBinaryTree != null) {
			currentUsersDetailsBeanBinaryTree = loggedInUsersDetailsBeanBinaryTree;
		}
		return userHome;
	}

	public void establishRemoteDBConnection() throws TrademenException {
		try {
			// rcon = new DBAccess().getRemoteDBConnection();
			con = new DBAccess().getConnection();

			if (con == null)
				try {
					con = new DBAccess().getConnection();
				} catch (Exception ignore) {
				}

		} catch (Exception e) {
			// e.printStackTrace();

		}
	}

	public String checkDataBaseExist(String dbname) {
		String returnValue = "failure";

		try {
			String checkDbexist = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + dbname
					+ "' ";
			if (con == null) {
				con = new DBAccess().getConnection();
			}
			st = con.createStatement();
			rs = st.executeQuery(checkDbexist);

			if (rs.next()) {
				String db = rs.getString(1);

				if (db.equals(dbname)) {
					returnValue = "existed";
				}
			} else {
				returnValue = "notExisted";
			}
			rs.close();
			st.close();
		} catch (Exception e) {

		}
		return returnValue;
	}

	public String isTableAlreadyExisted(String tableName) {

		String returnValue = "failure";

		try {

			String checkTableSQL = "SELECT COUNT(*) AS TablesCount FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '"
					+ dbName + "' AND TABLE_NAME = '" + tableName + "'";

			st = con.createStatement();
			rs = st.executeQuery(checkTableSQL);

			int noOfTablesExisted = rs.next() ? rs.getInt(1) : rs.getInt(1);

			if (noOfTablesExisted == 1) {
				returnValue = "existed";
			} else {
				returnValue = "notExisted";
			}

			rs.close();
			st.close();

		} catch (Exception e) {

			returnValue = "error";
		}

		return returnValue;
	}

	public ArrayList<LocalUserLevelBeanBinaryTrade> getlocalUseLevelDetails(long userId) {
		ArrayList<LocalUserLevelBeanBinaryTrade> localUserLevelArrayList = new ArrayList<LocalUserLevelBeanBinaryTrade>();

		try {

			String isTableAlreadyExisted = this.isTableAlreadyExisted("users");

			if (isTableAlreadyExisted.equals("existed")) {

				sql = "select userLevel, createdDate, companyName from users where id = '" + userId + "' ";

				st = con.createStatement();
				rs = st.executeQuery(sql);

				while (rs.next()) {
					LocalUserLevelBeanBinaryTrade LocalUserLevelBeanBinaryTrade = new LocalUserLevelBeanBinaryTrade();
					LocalUserLevelBeanBinaryTrade.setLocalUserLevel(rs.getString(1));
					LocalUserLevelBeanBinaryTrade.setCreatedDate(rs.getDate(2));
					LocalUserLevelBeanBinaryTrade.setCompanyName(rs.getString(3));

					localUserLevelArrayList.add(LocalUserLevelBeanBinaryTrade);
				}

				rs.close();
				st.close();
			}

		} catch (Exception e) {
			// Exception Occured
			// e.printStackTrace();
			localUserLevelArrayList = null;
		}
		return localUserLevelArrayList;
	}

	public ArrayList<UserTypeBeanBinaryTrade> getwebUserPaymentDetails(String dbname) {
		ArrayList<UserTypeBeanBinaryTrade> webUserPaymentDateArrayList = new ArrayList<UserTypeBeanBinaryTrade>();

		try {
			String sql1 = "SELECT payment_date_to FROM jettysoft_db.tbl_payments WHERE payment_user_id = (SELECT pay.payment_user_id FROM jettysoft_db.tbl_bi_support bi "
					+ "RIGHT JOIN jettysoft_db.tbl_user usr ON bi.registered_key = usr.user_rkey RIGHT JOIN jettysoft_db.tbl_payments pay ON usr.user_id = pay.payment_user_id "
					+ "WHERE bi.dbname = '" + dbname
					+ "' and usr.user_status = 'active' limit 1) ORDER BY payment_date_to desc LIMIT 1";

			st = con.createStatement();
			rs = st.executeQuery(sql1);

			while (rs.next()) {
				UserTypeBeanBinaryTrade UserTypeBeanBinaryTrade = new UserTypeBeanBinaryTrade();
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date currentDate = rs.getDate("payment_date_to");

				UserTypeBeanBinaryTrade.setUserPaymentValidDate(new java.sql.Date(currentDate.getTime()));

				webUserPaymentDateArrayList.add(UserTypeBeanBinaryTrade);
			}

			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			webUserPaymentDateArrayList = null;
		}

		return webUserPaymentDateArrayList;
	}

	public String blockTrailPeriod() {
		String returnValue = "failure";
		try {
			String sql001 = "UPDATE users SET isActive = 0 where isActive = 1";

			st = con.createStatement();

			int updatedStatus = st.executeUpdate(sql001);

			if (updatedStatus == 1)
				returnValue = "success";
			else
				returnValue = "failure";

			rs.close();
			st.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return returnValue;
	}

	public String activateUserPeriod() {
		String returnValue = "failure";
		try {
			String sql001 = "UPDATE users SET isActive = 1 where isActive = 0";

			st = con.createStatement();

			int updatedStatus = st.executeUpdate(sql001);

			if (updatedStatus == 1)
				returnValue = "success";
			else
				returnValue = "failure";

			rs.close();
			st.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return returnValue;
	}

	public UsersDetailsBeanBinaryTrade authenticateUser(String userId, String password) {
		UsersDetailsBeanBinaryTrade loggedInUsersDetailsBeanBinaryTree = new UsersDetailsBeanBinaryTrade();

		String role = null;
		try {
			sql = "SELECT id FROM Users WHERE isActive = 1 AND userId ='" + userId + "'";

			st1 = con.createStatement();
			rs1 = st1.executeQuery(sql);

			if (rs1.next()) {
				String checkPasswordSql = null;

				checkPasswordSql = "SELECT id,companyName,firstName,lastName,userId,emailId, businessAddress, cityprovince, province, "
						+ "postalcode, mainService, country, updatedBy, userLevel, email, newcr, newff, newts, crexe, tsexe, adduser, addqa, dashboard, reports,"
						+ "testresults, newbug, viewbug, addbugserver, DATE_FORMAT(createdDate, '%Y-%m-%d')as createdDate, projectaccess, defaultproject  "
						+ " FROM users WHERE isActive = 1 and userActive = 1 AND userId ='" + userId
						+ "' AND password = md5('" + password + "')";

				st = con.createStatement();
				rs = st.executeQuery(checkPasswordSql);

				if (rs.next()) {
					loggedInUsersDetailsBeanBinaryTree.setLoginStatus("success");
					Loggedinuserdetails.id = rs.getInt(1);
					Loggedinuserdetails.companyName = rs.getString(2);
					Loggedinuserdetails.firstName = rs.getString(3);
					Loggedinuserdetails.lastName = rs.getString(4);
					Loggedinuserdetails.userId = rs.getString(5);
					Loggedinuserdetails.emailId = rs.getString(6);
					Loggedinuserdetails.businessAddress = rs.getString(7);
					Loggedinuserdetails.cityprovince = rs.getString(8);
					Loggedinuserdetails.province = rs.getString(9);
					Loggedinuserdetails.postalCode = rs.getString(10);
					Loggedinuserdetails.mainService = rs.getString(11);
					Loggedinuserdetails.country = rs.getString(12);
					Loggedinuserdetails.updatedBy = rs.getString(13);
					Loggedinuserdetails.userLevel = rs.getString(14);
					Loggedinuserdetails.email = rs.getInt(15);
					Loggedinuserdetails.newcr = rs.getInt(16);
					Loggedinuserdetails.newff = rs.getInt(17);
					Loggedinuserdetails.newts = rs.getInt(18);
					Loggedinuserdetails.crexe = rs.getInt(19);
					Loggedinuserdetails.tsexe = rs.getInt(20);
					Loggedinuserdetails.adduser = rs.getInt(21);
					Loggedinuserdetails.addqa = rs.getInt(22);
					Loggedinuserdetails.dashboard = rs.getInt(23);
					Loggedinuserdetails.reports = rs.getInt(24);
					Loggedinuserdetails.testresults = rs.getInt(25);
					Loggedinuserdetails.newbug = rs.getInt(26);
					Loggedinuserdetails.viewbug = rs.getInt(27);
					Loggedinuserdetails.addbugserver = rs.getInt(28);
					Loggedinuserdetails.activateddate = rs.getString(29);
					Loggedinuserdetails.projectaccess = rs.getInt(30);
					Loggedinuserdetails.defaultproject = rs.getInt(31);

					role = "Admin";

					StoreAuditLogger.logStoreTransaction(userId, "Login", "", "", true,
							"Successfully logged in to Business Store", role);
				} else {
					loggedInUsersDetailsBeanBinaryTree.setLoginStatus("failure");
					StoreAuditLogger.logStoreTransaction(userId, "Login", "", "", true,
							"Failed to login to Business Store", role);
				}
				rs.close();
				st.close();
			} else {
				loggedInUsersDetailsBeanBinaryTree.setLoginStatus("notExisted");
			}

			rs1.close();
			st1.close();

		} catch (Exception e) {
			loggedInUsersDetailsBeanBinaryTree.setLoginStatus("error");
			e.printStackTrace();
		}

		return loggedInUsersDetailsBeanBinaryTree;
	}

	public String updateUsers(String userId) {
		String returnValue = "failure";
		try {
			java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
			// sql = "UPDATE users SET loggedIn ='"+sqlDate+"' where userId='"+
			// userId +"'";
			sql = "UPDATE users SET loggedIn ='" + sqlDate + "' where userId= ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, userId);

			int updatedStatus = ps.executeUpdate();
			if (updatedStatus == 1)
				returnValue = "success";
			else
				returnValue = "failure";
			ps.close();
		} catch (SQLException e) {
			return returnValue;
		}
		return returnValue;
	}

	public String checkExistingRoleRelations(int roleid) {
		String returnValue = "failure";

		try {
			String isInvoiceAlreadyExisted = this.isTableAlreadyExisted("userfeaturesrelation");

			if (isInvoiceAlreadyExisted.equals("notExisted")) {
				returnValue = "new";
			} else if (isInvoiceAlreadyExisted.equals("existed")) {
				String checkUserRoles = "SELECT roleid FROM userfeaturesrelation WHERE roleid = '" + roleid + "' ";

				st = con.createStatement();
				rs = st.executeQuery(checkUserRoles);
				if (rs.next()) {
					returnValue = "alreadyExisted";
				} else {
					returnValue = "new";
				}
			}
		} catch (Exception e) {
			returnValue = "error";
		}

		return returnValue;
	}

	public String createUserFeaturesRelation(String tableName, int roleid, ArrayList featureid, long userId) {
		String returnValue = "failure";

		try {
			String isInvoiceAlreadyExisted = this.isTableAlreadyExisted(tableName);

			if (isInvoiceAlreadyExisted.equals("notExisted")) {

				String createTabelSQL = "create table " + tableName
						+ "(id bigint(20) NOT NULL AUTO_INCREMENT, roleid int, featureid int, createdUserId bigint(20) default NULL, date date, PRIMARY KEY (id))";
				st = con.createStatement();

				int isTableCreated = st.executeUpdate(createTabelSQL);

				if (isTableCreated == 0) {
					returnValue = insertuserfeaturesrelation(tableName, roleid, featureid, userId);
				} else
					returnValue = "failure";

			} else if (isInvoiceAlreadyExisted.equals("existed")) {
				returnValue = insertuserfeaturesrelation(tableName, roleid, featureid, userId);
			} else {
				returnValue = "failure";
			}

			st.close();
		} catch (SQLException e) {
			returnValue = "failure";
		}
		return returnValue;
	}

	public String insertuserfeaturesrelation(String tableName, int roleid, ArrayList featureid, long userId) {
		String returnValue = "failure";
		ArrayList fea = null;

		try {
			fea = featureid;
			for (int i = 0; i < fea.size(); i++) {
				sql = "insert into " + tableName
						+ "(roleid, featureid, createdUserId, date) values(?,?,?, CURRENT_TIMESTAMP)";
				ps = con.prepareStatement(sql);

				ps.setInt(1, roleid);
				ps.setInt(2, Integer.parseInt(fea.get(i).toString()));
				ps.setLong(3, userId);

				int status11 = ps.executeUpdate();

				if (status11 == 1) {
					returnValue = "success";
				} else {
					returnValue = "failure";
				}
			}

			ps.close();
		} catch (Exception e) {
			returnValue = "error";
		}

		return returnValue;
	}

	public void deleterolerelations(int roleid) {
		try {
			sql = "DELETE from userfeaturesrelation where roleid =" + roleid;
			st = con.createStatement();
			st.executeUpdate(sql);

		} catch (Exception e) {
			// System.out.println(e);
		}
	}

	public String checkValidRoles(String enteredrole) {
		String returnValue = "failure";

		try {
			String isInvoiceAlreadyExisted = this.isTableAlreadyExisted("userroles");

			if (isInvoiceAlreadyExisted.equals("notExisted")) {
				returnValue = "new";
			} else if (isInvoiceAlreadyExisted.equals("existed")) {
				String checkUserRoles = "SELECT userRole FROM userroles WHERE status = 1  AND userRole = '"
						+ enteredrole + "' ";

				st = con.createStatement();
				rs = st.executeQuery(checkUserRoles);
				if (rs.next()) {
					returnValue = "alreadyExisted";
				} else {
					returnValue = "new";
				}
			}
		} catch (Exception e) {
			returnValue = "error";
		}

		return returnValue;
	}

	public String createRoles(String tableName, String rolename, int status, long userId) {
		String returnValue = "failure";

		try {
			String isInvoiceAlreadyExisted = this.isTableAlreadyExisted(tableName);

			if (isInvoiceAlreadyExisted.equals("notExisted")) {

				String createTabelSQL = "create table " + tableName
						+ "(id bigint(20) NOT NULL AUTO_INCREMENT, userRole varchar (30), date date, createdUserId bigint(20) default NULL, status tinyint(1) DEFAULT '1' COMMENT '''1'' means not cancelled and ''0'' means cancelled', PRIMARY KEY (id))";
				st = con.createStatement();

				int isTableCreated = st.executeUpdate(createTabelSQL);

				if (isTableCreated == 0) {
					returnValue = insertusersTable(tableName, rolename, status, userId);
				} else
					returnValue = "failure";

			} else if (isInvoiceAlreadyExisted.equals("existed")) {
				returnValue = insertusersTable(tableName, rolename, status, userId);
			} else {
				returnValue = "failure";
			}

			st.close();
		} catch (SQLException e) {
			returnValue = "failure";
		}
		return returnValue;
	}

	public String insertusersTable(String tableName, String rolename, int status, long userId) {
		String returnValue = "failure";

		try {
			String checkUserRoles = "SELECT userRole FROM " + tableName + " WHERE status = 1  AND userRole = '"
					+ rolename + "' ";

			st = con.createStatement();
			rs = st.executeQuery(checkUserRoles);

			if (rs.next()) {
				returnValue = "alreadyExisted";
			} else {
				sql = "insert into " + tableName
						+ "(userRole, status, createdUserId, date) values(?,?,?, CURRENT_TIMESTAMP)";
				ps = con.prepareStatement(sql);

				ps.setString(1, rolename);
				ps.setInt(2, status);
				ps.setLong(3, userId);

				int status11 = ps.executeUpdate();

				if (status11 == 1) {
					returnValue = "success";
				} else {
					returnValue = "failure";
				}

				rs.close();
				st.close();
				ps.close();
			}
		} catch (Exception e) {
			returnValue = "error";
		}

		return returnValue;
	}

	public String updateTrigerstatus(String featurename, int status1) {
		String returnValue = "failure";
		try {

			String updateproject = "update trigerstatus set status = ?, createddate = CURRENT_TIMESTAMP where featurename = ?";

			ps = con.prepareStatement(updateproject);

			ps.setInt(1, status1);
			ps.setString(2, featurename);

			int status = ps.executeUpdate();
			if (status == 1) {
				returnValue = "success";
			} else {
				returnValue = "failure";
			}

			ps.close();

		} catch (Exception e) {
			// e.printStackTrace();
			returnValue = "failure";
		}

		return returnValue;
	}

	public int gettrigerstatus(String featureanme) {
		int returnvalue = 0;
		try {
			sql = "select status from trigerstatus where featurename = '" + featureanme + "'";

			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				returnvalue = rs.getInt(1);
			}

		} catch (Exception e) {
			// e.printStackTrace();
		}
		return returnvalue;
	}

	public String validateRegistrationKey(String givenKey) {

		String returnValue = "failure";

		try {
			sql = "select status from trademen.registrationkeys where registerKey = '" + givenKey + "'";

			if (con == null) {
				con = new DBAccess().getConnection();
			}
			st = con.createStatement();
			rs = st.executeQuery(sql);

			if (rs.next()) {
				boolean isAlreadyRegistered = rs.getBoolean(1);

				if (isAlreadyRegistered) {
					returnValue = "alreadyRegistered";
				} else {
					returnValue = "success";
				}
			} else {
				returnValue = "invalidKey";
			}

			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			returnValue = "error";
		}

		return returnValue;
	}

	public ArrayList<UsersDetailsBeanBinaryTrade> getWebRegistrationInformation(String registeredkey) {
		ArrayList<UsersDetailsBeanBinaryTrade> registrationlist = new ArrayList<UsersDetailsBeanBinaryTrade>();

		try {
			sql = "SELECT user_username, user_firstname, user_lastname, user_companyname, user_email, user_password, user_contact_doorno, user_contact_street, "
					+ "user_contact_city, user_contact_state, user_contact_country, user_question, user_answer, user_contact_pin FROM jettysoft_db.tbl_user where user_rkey = '"
					+ registeredkey + "'";
			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				UsersDetailsBeanBinaryTrade userregistration = new UsersDetailsBeanBinaryTrade();

				userregistration.setUserId(rs.getString(1));
				userregistration.setFirstName(rs.getString(2));
				userregistration.setLastName(rs.getString(3));
				userregistration.setCompanyName(rs.getString(4));
				userregistration.setEmailId(rs.getString(5));
				userregistration.setPassword(rs.getString(6));
				userregistration.setDoorno(rs.getString(7));
				userregistration.setStreetname(rs.getString(8));
				userregistration.setCityprovince(rs.getString(9));
				userregistration.setProvince(rs.getString(10));
				userregistration.setCountry(rs.getString(11));
				userregistration.setSecurityQuestion(rs.getString(12));
				userregistration.setAnswer(rs.getString(13));
				userregistration.setPostalCode(rs.getString(14));

				registrationlist.add(userregistration);
			}

			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			registrationlist = null;
		}

		return registrationlist;
	}

	public boolean createDB() {
		try {
			Connection con1 = new DBAccess().getConnection();
			DBConfigJAXB db = new DBConfigJAXB();
			DBConfigBinaryTade dbConfig = db.readDBConfig();
			// String upToNCharacters = s.substring(0, Math.min(s.length(), n));
			String companyName = EncryptAndDecrypt.decryptData(dbConfig.getDbName()); // CommonFunctions.getCompanyName();
			String com = companyName.replaceAll(" ", "");
			String upToNCharacters = com.substring(0, Math.min(com.length(), 40));
			if (con1 != null) {
				// System.out.println("I am in");
				// String query = "create database
				// "+EncryptAndDecrypt.decryptData(dbConfig.getDbName());

				String query = "create database " + upToNCharacters.toLowerCase();
				st = con1.createStatement();

				// rs = st.executeQuery(query);
				int state = st.executeUpdate(query);
				st.close();
				if (state == 1) {
					return true;
				} else {
					return false;
				}
			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String registerUser(String tableName, String registrationKey, String companyName, String firstName,
			String lastName, String userId, String password, String emailId, String securityQuestion, String answer,
			String businessAddress, String cityprovince, String province, String postalCode, String mainService,
			String userLevel, String updatedby, String industry, String country, String logoname, int email, int newcr,
			int newff, int newts, int crexe, int tsexe, int adduser, int addqa, int dashboard, int reports,
			int testresults, int newbug, int viewbug, int addbugserver, int projectaccess) {

		String returnValue = "failure";
		String com = companyName.replaceAll(" ", "");
		String companyname = com.substring(0, Math.min(com.length(), 40));

		try {

			String isUsersTableAlreadyExisted = this.isTableAlreadyExisted(tableName);

			if (isUsersTableAlreadyExisted.equals("notExisted")) {

				sql = "use " + companyname;
				st = con.createStatement();
				st.executeUpdate(sql);
				st.close();

				String createTabelSQL = "CREATE TABLE " + tableName
						+ " (id bigint(20) NOT NULL auto_increment, companyName varchar(255) default NULL,firstName varchar(255) default NULL, lastName varchar(255) default NULL, "
						+ "userId varchar(200) default NULL, password varchar(255) default NULL, emailId varchar(300) default NULL, "
						+ "securityQuestion varchar(255) default NULL, answer varchar(255) default NULL, businessAddress varchar(255) default NULL, "
						+ "cityprovince varchar(255) default NULL, province varchar(255) default NULL, postalCode varchar(6) default NULL, mainService varchar(255) default NULL, "
						+ "userLevel varchar(2) default NULL, createdDate datetime default NULL, updatedDate datetime default NULL, updatedBy varchar(255) default NULL, isActive tinyint(1) default '0',"
						+ "loggedIn date, industry varchar(15) default null, country varchar(200) default null, logoname varchar(300) default null, "
						+ "email int(1) default 0, newcr int(1) default 0, newff int(1) default 0, newts int(1) default 0, crexe int(1) default 0, tsexe int(1) default 0,"
						+ "adduser int(1) default 0, addqa int(1) default 0, dashboard int(1) default 0, reports int(1) default 0, testresults int(1) default 0, "
						+ "newbug int(1) default 0, viewbug int(1) default 0, addbugserver int(1) default 0, projectaccess int(1) default 0, defaultproject int(1) default 0, userActive int(1) default 1,"
						+ " PRIMARY KEY  (id))";
				Statement st = con.createStatement();

				int isTableCreated = st.executeUpdate(createTabelSQL);

				st.close();
				if (isTableCreated == 0)
					returnValue = addNewUser(tableName, registrationKey, companyName, firstName, lastName, userId,
							password, emailId, securityQuestion, answer, businessAddress, cityprovince, province,
							postalCode, mainService, userLevel, updatedby, industry, country, logoname, email, newcr,
							newff, newts, crexe, tsexe, adduser, addqa, dashboard, reports, testresults, newbug,
							viewbug, addbugserver, projectaccess);
				else
					returnValue = "failure";

			} else if (isUsersTableAlreadyExisted.equals("existed")) {
				returnValue = addNewUser(tableName, registrationKey, companyName, firstName, lastName, userId, password,
						emailId, securityQuestion, answer, businessAddress, cityprovince, province, postalCode,
						mainService, userLevel, updatedby, industry, country, logoname, email, newcr, newff, newts,
						crexe, tsexe, adduser, addqa, dashboard, reports, testresults, newbug, viewbug, addbugserver,
						projectaccess);
			} else {
				returnValue = "failure";
			}

		} catch (Exception e) {
			// e.printStackTrace();
			returnValue = "error";
		}

		return returnValue;
	}

	public String addNewUser(String tableName, String registrationKey, String companyName, String firstName,
			String lastName, String userId, String password, String emailId, String securityQuestion, String answer,
			String businessAddress, String cityprovince, String province, String postalCode, String mainService,
			String userLevel, String updatedby, String industry, String country, String logoname, int email, int newcr,
			int newff, int newts, int crexe, int tsexe, int adduser, int addqa, int dashboard, int reports,
			int testresults, int newbug, int viewbug, int addbugserver, int projectaccess) {
		String returnValue = "failure";

		try {

			sql = "SELECT id FROM Users WHERE isActive = 1 and userActive = 1 AND userId ='" + userId + "'";

			st = con.createStatement();
			rs = st.executeQuery(sql);

			if (rs.next()) {
				returnValue = "alreadyExisted";
			} else {
				sql = "insert into " + tableName
						+ "(companyName, firstName, lastName, userId, password, emailId, securityQuestion, answer, businessAddress, cityprovince, province, "
						+ "postalCode, mainService, userLevel,createdDate,userActive,loggedIn, updatedBy, industry, country, logoname, email,  newcr, "
						+ "newff,  newts,  crexe,  tsexe,  adduser,  addqa,  dashboard, reports, testresults, newbug, viewbug, addbugserver, projectaccess) "
						+ "values(? , ? , ? , ? , md5(?) , ? , ? , ? , ? , ? , ? , ? , ? , ? , CURRENT_TIMESTAMP , 1 , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				ps = con.prepareStatement(sql);
				ps.setString(1, companyName);
				ps.setString(2, firstName);
				ps.setString(3, lastName);
				ps.setString(4, userId);
				ps.setString(5, password);
				ps.setString(6, emailId);
				ps.setString(7, securityQuestion);
				ps.setString(8, answer);
				ps.setString(9, businessAddress);
				ps.setString(10, cityprovince);
				ps.setString(11, province);
				ps.setString(12, postalCode);
				ps.setString(13, mainService);
				ps.setString(14, userLevel);
				java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
				ps.setDate(15, sqlDate);
				ps.setString(16, updatedby);
				ps.setString(17, industry);
				ps.setString(18, country);
				ps.setString(19, logoname);
				ps.setInt(20, email);
				ps.setInt(21, newcr);
				ps.setInt(22, newff);
				ps.setInt(23, newts);
				ps.setInt(24, crexe);
				ps.setInt(25, tsexe);
				ps.setInt(26, adduser);
				ps.setInt(27, addqa);
				ps.setInt(28, dashboard);
				ps.setInt(29, reports);
				ps.setInt(30, testresults);
				ps.setInt(31, newbug);
				ps.setInt(32, viewbug);
				ps.setInt(33, addbugserver);
				ps.setInt(34, projectaccess);

				int status = ps.executeUpdate();

				if (status == 1) {
					returnValue = "success";
				} else {
					returnValue = "failure";
				}
				ps.close();
			}
			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			returnValue = "error";
		}

		return returnValue;
	}

	public String updateUser(String firstName, String lastName, String userId, String password, String emailId,
			String securityQuestion, String answer, String updatedby, int email, int newcr, int newff, int newts,
			int crexe, int tsexe, int adduser, int addqa, int dashboard, int activestatus, String id, int reports,
			int testresults, int newbug, int viewbug, int addbugserver, int projectaccess) {
		String returnValue = "failure";

		try {
			sql = "update users set firstName = ?, lastName = ?, userId = ?, password = md5(?), emailId = ?, securityQuestion = ?, answer = ?, "
					+ "updatedDate = CURRENT_TIMESTAMP, userActive = ?, updatedBy = ?, email = ?,  newcr = ?, newff = ?,  newts = ?,  "
					+ "crexe = ?,  tsexe =?,  adduser =?,  addqa =?,  dashboard = ?, reports = ?, testresults = ?, newbug = ?, viewbug = ?, "
					+ "addbugserver = ?, projectaccess =?  where id = ?";

			ps = con.prepareStatement(sql);
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, userId);
			ps.setString(4, password);
			ps.setString(5, emailId);
			ps.setString(6, securityQuestion);
			ps.setString(7, answer);
			ps.setInt(8, activestatus);
			ps.setString(9, updatedby);
			ps.setInt(10, email);
			ps.setInt(11, newcr);
			ps.setInt(12, newff);
			ps.setInt(13, newts);
			ps.setInt(14, crexe);
			ps.setInt(15, tsexe);
			ps.setInt(16, adduser);
			ps.setInt(17, addqa);
			ps.setInt(18, dashboard);
			ps.setInt(19, reports);
			ps.setInt(20, testresults);
			ps.setInt(21, newbug);
			ps.setInt(22, viewbug);
			ps.setInt(23, addbugserver);
			ps.setInt(24, projectaccess);
			ps.setString(25, id);

			int status = ps.executeUpdate();

			if (status == 1) {
				returnValue = "success";
			} else {
				returnValue = "failure";
			}
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
			returnValue = "error";
		}

		return returnValue;
	}

	public String updateRegistrationKeys(String registrationKey) {
		String returnValue = "failure";
		try {
			// Connection rcon = new DBAccess().getRemoteDBConnection();
			sql = "UPDATE trademen.registrationkeys SET status = true where registerkey='" + registrationKey + "'";
			String sql1 = "use trademen";
			st = con.createStatement();
			int updatedStus = st.executeUpdate(sql1);

			Statement st1 = con.createStatement();
			int updatedStatus = st.executeUpdate(sql);

			if (updatedStus == 1 && updatedStatus == 1)
				returnValue = "success";
			else
				returnValue = "failure";
			st1.close();
		} catch (SQLException e) {

			// e.printStackTrace();
		}
		return returnValue;
	}

	public String updateUserBISupportDetails(String givenKey) {
		String returnValue = "failure";
		try {
			String sql1 = "insert into jettysoft_db.tbl_bi_support (dbname,registered_key, added_date ) values(?,?,CURRENT_TIMESTAMP)";
			PreparedStatement ps12 = con.prepareStatement(sql1);
			ps12.setString(1, CommonFunctions.getCompanyName().substring(0,
					Math.min(CommonFunctions.getCompanyName().length(), 40)));
			ps12.setString(2, givenKey);

			int status = ps12.executeUpdate();

			if (status == 1) {
				returnValue = "success";
			} else {
				returnValue = "failure";
			}

			ps12.close();

		} catch (Exception e) {
			returnValue = "error";
		}

		return returnValue;
	}

	public String createTrigerstatustable(String companyName) {
		String returnValue = "failure";
		String com = companyName.toLowerCase().replaceAll(" ", "");
		String companyname = com.substring(0, Math.min(com.length(), 40));
		String isTableAlreadyExisted = this.isTableAlreadyExisted("trigerstatus");

		if (isTableAlreadyExisted != "existed") {
			try {
				sql = "use " + companyname;
				st = con.createStatement();
				st.executeUpdate(sql);
				st.close();

				String createTabelSQL = "CREATE TABLE trigerstatus (id bigint(255) NOT NULL AUTO_INCREMENT, featurename varchar(200) DEFAULT null, "
						+ "status bigint(1) DEFAULT 0, createddate datetime, PRIMARY KEY  (id))";
				Statement st1 = con.createStatement();
				int isTableCreated = st1.executeUpdate(createTabelSQL);
				st1.close();

				if (isTableCreated == 0) {
					inserttrigertablefeatures();
				} else {
					returnValue = "failure";
				}
			} catch (Exception e) {
				returnValue = "failure";
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	public String inserttrigertablefeatures() {
		String returnValue = "failure";
		try {
			sql = "insert into trigerstatus (featurename, createddate) " + "values('bugstable', CURRENT_TIMESTAMP), "
					+ "('bugusers', CURRENT_TIMESTAMP), " + "('bugprojects', CURRENT_TIMESTAMP), "
					+ "('bugsserver', CURRENT_TIMESTAMP)";
			ps = con.prepareStatement(sql);

			int status = ps.executeUpdate();

			if (status == 1) {
				returnValue = "success";
			} else {
				returnValue = "failure";
			}
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnValue;
	}

	public ArrayList<UserTypeBeanBinaryTrade> getwebUserLevelDetails(String givenKey) {
		ArrayList<UserTypeBeanBinaryTrade> webUserLevelArrayList = new ArrayList<UserTypeBeanBinaryTrade>();

		try {
			String sql1 = "select user_level from jettysoft_db.tbl_user where user_rkey = '" + givenKey
					+ "' and user_status = 'active'";

			Statement st1 = con.createStatement();
			ResultSet rs1 = st1.executeQuery(sql1);

			while (rs1.next()) {
				UserTypeBeanBinaryTrade UserTypeBeanBinaryTrade = new UserTypeBeanBinaryTrade();
				UserTypeBeanBinaryTrade.setUserLevel(rs1.getString(1));

				webUserLevelArrayList.add(UserTypeBeanBinaryTrade);
			}

			rs1.close();
			st1.close();

		} catch (Exception e) {
			// e.printStackTrace();
			webUserLevelArrayList = null;
		}

		return webUserLevelArrayList;
	}

	public ArrayList<UsersDetailsBeanBinaryTrade> getRegisteredUserDetails() {

		ArrayList<UsersDetailsBeanBinaryTrade> registeredUserDetailsArrayList = new ArrayList<UsersDetailsBeanBinaryTrade>();

		try {

			sql = "SELECT id,companyName,firstName,lastName,userId,emailid,securityQuestion,answer,businessAddress,cityprovince,postalCode,mainService,"
					+ "province FROM users WHERE isActive = 1 and userActive = 1 order by id";

			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {

				UsersDetailsBeanBinaryTrade UsersDetailsBeanBinaryTree = new UsersDetailsBeanBinaryTrade();

				UsersDetailsBeanBinaryTree.setId(rs.getLong(1));
				UsersDetailsBeanBinaryTree.setCompanyName(rs.getString(2));
				UsersDetailsBeanBinaryTree.setFirstName(rs.getString(3));
				UsersDetailsBeanBinaryTree.setLastName(rs.getString(4));
				UsersDetailsBeanBinaryTree.setUserId(rs.getString(5));
				UsersDetailsBeanBinaryTree.setEmailId(rs.getString(6));
				UsersDetailsBeanBinaryTree.setSecurityQuestion(rs.getString(7));
				UsersDetailsBeanBinaryTree.setAnswer(rs.getString(8));
				UsersDetailsBeanBinaryTree.setBusinessAddress(rs.getString(9));
				UsersDetailsBeanBinaryTree.setCityprovince(rs.getString(10));
				UsersDetailsBeanBinaryTree.setPostalCode(rs.getString(11));
				UsersDetailsBeanBinaryTree.setMainService(rs.getString(12));
				UsersDetailsBeanBinaryTree.setProvince(rs.getString(13));

				registeredUserDetailsArrayList.add(UsersDetailsBeanBinaryTree);
			}

			rs.close();
			st.close();

		} catch (Exception e) {
			// Exception Occured
			e.printStackTrace();
			registeredUserDetailsArrayList = null;
		}

		return registeredUserDetailsArrayList;
	}

	public String updatePassword(String userId, String newPassword) {

		String returnValue = "failure";

		try {

			// sql = "UPDATE USERS SET password = md5('"+ newPassword +"') WHERE
			// isActive = 1 AND userId = '"+ userId +"'";
			sql = "UPDATE USERS SET password = md5(?) WHERE isActive = 1 and userActive = 1 AND userId = ?";

			ps = con.prepareStatement(sql);
			ps.setString(1, newPassword);
			ps.setString(2, userId);

			int status = ps.executeUpdate();

			if (status == 1) {
				returnValue = "success";
			} else {
				returnValue = "failure";
			}

			ps.close();

		} catch (Exception e) {
			returnValue = "failure";
		}

		return returnValue;
	}

	public String updateUserPassword(String tableName, long userId, String oldPassword, String newPassword,
			String updatedBy) {
		String returnValue = "failure";

		try {
			sql = "SELECT id, password FROM " + tableName + " WHERE isActive = 1 and userActive = 1 AND id = " + userId
					+ " AND password = md5('" + oldPassword + "')";

			st = con.createStatement();
			rs = st.executeQuery(sql);

			if (rs.next()) {
				sql = "UPDATE " + tableName
						+ " SET password = md5(?), updatedDate = CURRENT_TIMESTAMP, updatedBy = ? WHERE isActive = 1 and userActive = 1 AND id = ?";
				ps = con.prepareStatement(sql);
				ps.setString(1, newPassword);
				ps.setString(2, updatedBy);
				ps.setLong(3, userId);

				int status = ps.executeUpdate();

				if (status == 1) {
					returnValue = "success";
				} else {
					returnValue = "failure";
				}
				ps.close();
			} else {
				returnValue = "passwordNotMatched";
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			returnValue = "error";
		}

		return returnValue;
	}

	public String createqaservertable(String tableName, long userId, String classname, String url, String username,
			String password, String servertype, boolean defaulttype) {

		String returnValue = "failure";

		try {
			String checktable = this.isTableAlreadyExisted(tableName);

			if (checktable.equals("notExisted")) {

				String createTabelSQL = "CREATE TABLE " + tableName
						+ " (id bigint(20) NOT NULL auto_increment, classname varchar(4000) not null, url varchar(4000) not null, "
						+ " username varchar(4000) not null, password varchar(8000) not null, servertype varchar(500) not null,"
						+ "createdUserId bigint(20) default NULL, createdDate datetime default NULL, updateddate datetime default null, defaulttype varchar(5) default null, status tinyint(1) default 1, PRIMARY KEY (id))";

				st = con.createStatement();
				int isTableCreated = st.executeUpdate(createTabelSQL);

				if (isTableCreated == 0)
					returnValue = insertqaserverdetails(tableName, userId, classname, url, username, password,
							servertype, defaulttype);
				else
					returnValue = "failure";
			} else if (checktable.equals("existed")) {
				returnValue = insertqaserverdetails(tableName, userId, classname, url, username, password, servertype,
						defaulttype);
			} else {
				returnValue = "failure";
			}
			st.close();
		} catch (Exception e) {
			returnValue = "error";
			e.printStackTrace();
		}
		return returnValue;
	}

	public String insertqaserverdetails(String tableName, long userId, String classname, String url, String username,
			String password, String servertype, boolean defaulttype) {
		String result = "failure";
		try {
			sql = "insert into " + tableName
					+ " (classname, url, username, password, servertype, createdUserId, createdDate, defaulttype) values(?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, classname);
			ps.setString(2, url);
			ps.setString(3, username);
			ps.setString(4, password);
			ps.setString(5, servertype);
			ps.setLong(6, userId);
			ps.setBoolean(7, defaulttype);
			int status = ps.executeUpdate();
			if (status == 1) {
				result = "success";
			}
		} catch (Exception e) {
			result = "failure";
			e.printStackTrace();
		}
		return result;
	}

	public String updateqaserverdetails(String tableName, long userId, String classname, String url, String username,
			String password, String servertype, int id, boolean defaulttype) {
		String result = "failure";
		try {
			sql = "update " + tableName
					+ " set classname = ?, url = ?, username = ?, password = ?, servertype = ?, createdUserId = ?, updateddate = CURRENT_TIMESTAMP, defaulttype = ? where id = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, classname);
			ps.setString(2, url);
			ps.setString(3, username);
			ps.setString(4, password);
			ps.setString(5, servertype);
			ps.setLong(6, userId);
			ps.setBoolean(7, defaulttype);
			ps.setInt(8, id);
			int status = ps.executeUpdate();
			if (status == 1) {
				result = "success";
			}
		} catch (Exception e) {
			result = "failure";
		}
		return result;
	}

	public ArrayList<QAServerDetailsBinaryTrade> getQAServerDetails(String tableName) {

		ArrayList<QAServerDetailsBinaryTrade> qa = new ArrayList<QAServerDetailsBinaryTrade>();
		String checktable = isTableAlreadyExisted(tableName);
		try {
			if (checktable.equals("existed")) {
				sql = "select id, classname, url, username, password, servertype, createdUserId, createdDate, updateddate, status, defaulttype from "
						+ tableName;

				st = con.createStatement();
				rs = st.executeQuery(sql);

				while (rs.next()) {
					QAServerDetailsBinaryTrade qasd = new QAServerDetailsBinaryTrade();
					qasd.setId(rs.getString(1));
					qasd.setClassname(rs.getString(2));
					qasd.setDburl(rs.getString(3));
					qasd.setUsername(rs.getString(4));
					qasd.setPassword(rs.getString(5));
					qasd.setServertype(rs.getString(6));
					qasd.setUserid(rs.getString(7));
					qasd.setCreateddate(rs.getString(8));
					qasd.setUpdateddate(rs.getString(9));
					if (rs.getInt(10) == 1) {
						qasd.setStatus("Active");
					} else if (rs.getInt(10) == 0) {
						qasd.setStatus("Disabled");
					}
					qasd.setDefaulttype(rs.getString(11));

					qa.add(qasd);
				}

				rs.close();
				st.close();
			} else {
				qa = null;
			}

		} catch (Exception e) {
			qa = null;
		}

		return qa;
	}

	public String createmodulestable(String tableName, long userId, String modulename, int projectid) {

		String returnValue = "failure";

		try {
			String checktable = this.isTableAlreadyExisted(tableName);

			if (checktable.equals("notExisted")) {

				String createTabelSQL = "CREATE TABLE " + tableName
						+ " (id bigint(20) NOT NULL auto_increment, module varchar(200) not null,"
						+ "createdUserId bigint(20) default NULL, createdDate datetime default NULL, updateddate datetime default null, "
						+ "status tinyint(1) default 1, projectid bigint(10) default 0, PRIMARY KEY (id),  UNIQUE KEY(module))";

				st = con.createStatement();
				int isTableCreated = st.executeUpdate(createTabelSQL);

				if (isTableCreated == 0)
					returnValue = insertmoduledetails(tableName, userId, modulename, projectid);
				else
					returnValue = "failure";
			} else if (checktable.equals("existed")) {
				returnValue = insertmoduledetails(tableName, userId, modulename, projectid);
			} else {
				returnValue = "failure";
			}
			st.close();
		} catch (Exception e) {
			returnValue = "error";
			e.printStackTrace();
		}
		return returnValue;
	}

	public String insertmoduledetails(String tableName, long userId, String modulename, int projectid) {
		String result = "failure";
		try {
			String checkduplicates = "select module from modules where module = '" + modulename + "'";
			st = con.createStatement();
			rs = st.executeQuery(checkduplicates);
			if (rs.next()) {
				result = "duplicate";
			} else {
				sql = "insert into " + tableName
						+ " (module, createdUserId, createdDate, projectid) values( ?, ?, CURRENT_TIMESTAMP, ?)";
				ps = con.prepareStatement(sql);
				ps.setString(1, modulename);
				ps.setLong(2, userId);
				ps.setInt(3, projectid);
				int status = ps.executeUpdate();
				if (status == 1) {
					result = "success";
				}
				ps.close();
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			result = "failure";
			e.printStackTrace();
		}
		return result;
	}

	public String updatemoduledetails(String tableName, long userId, String modulename, int id, int projectid) {
		String result = "failure";
		try {
			sql = "update " + tableName
					+ " set module = ?, createdUserId = ?, updateddate = CURRENT_TIMESTAMP, projectid = ? where id = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, modulename);
			ps.setLong(2, userId);
			ps.setInt(3, projectid);
			ps.setInt(4, id);
			int status = ps.executeUpdate();
			if (status == 1) {
				result = "success";
			}
		} catch (Exception e) {
			result = "failure";
		}
		return result;
	}

	public ArrayList<ModulesBinaryTrade> getModuleDetails(String tableName, String required, int projectid) {

		ArrayList<ModulesBinaryTrade> mb = new ArrayList<ModulesBinaryTrade>();
		String checktable = isTableAlreadyExisted(tableName);
		try {
			if (checktable.equals("existed")) {
				if (required.equals("moduleonly")) {
					sql = "select  distinct(module) from " + tableName + " where projectid = '" + projectid + "'";

					st = con.createStatement();
					rs = st.executeQuery(sql);

					while (rs.next()) {
						ModulesBinaryTrade mbt = new ModulesBinaryTrade();
						mbt.setModulename(rs.getString(1));

						mb.add(mbt);
					}
					rs.close();
					st.close();
				} else {
					sql = "select id, module, createdUserId, createdDate, updateddate from " + tableName
							+ " where projectid = '" + projectid + "'";

					st = con.createStatement();
					rs = st.executeQuery(sql);

					while (rs.next()) {
						ModulesBinaryTrade mbt = new ModulesBinaryTrade();
						mbt.setId(rs.getString(1));
						mbt.setModulename(rs.getString(2));
						mbt.setUserid(rs.getString(3));
						mbt.setCreateddate(rs.getString(4));
						mbt.setUpdateddate(rs.getString(5));

						mb.add(mbt);
					}
					rs.close();
					st.close();
				}
			} else {
				mb = null;
			}
		} catch (Exception e) {
			mb = null;
		}
		return mb;
	}

	public int getdefaultserver() {
		int result = 0;
		try {
			sql = "select id from qaservers where defaulttype = 1";
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

	public ArrayList<QAServerDetailsBinaryTrade> getserverdetailsbyid(int id) {
		ArrayList<QAServerDetailsBinaryTrade> qalist = new ArrayList<QAServerDetailsBinaryTrade>();
		try {
			sql = "select url, username, password, classname, servertype from qaservers where id =" + id;
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				QAServerDetailsBinaryTrade qa = new QAServerDetailsBinaryTrade();
				qa.setDburl(rs.getString(1));
				qa.setUsername(rs.getString(2));
				qa.setPassword(rs.getString(3));
				qa.setClassname(rs.getString(4));
				qa.setServertype(rs.getString(5));
				qalist.add(qa);
			}
			rs.close();
			st.close();
		} catch (Exception e) {

		}
		return qalist;
	}

	public String getTestResult(String sqlquery) {
		String result = null;
		ArrayList<QAServerDetailsBinaryTrade> qalist = new ArrayList<QAServerDetailsBinaryTrade>();
		try {
			int serverid = 0;
			serverid = getdefaultserver();
			if (serverid > 0) {
				fsql = sqlquery;
				qalist = getserverdetailsbyid(serverid);
				if (qalist != null && fcon == null) {
					fcon = new OnlineDBAccess().getfConnection(qalist.get(0).getDburl(), qalist.get(0).getUsername(),
							qalist.get(0).getPassword(), qalist.get(0).getClassname());
				}
				if (fcon != null) {
					st1 = fcon.createStatement();
					rs1 = st1.executeQuery(fsql);
					while (rs1.next()) {
						result = rs1.getString(1);
					}
					rs1.close();
					st1.close();
				} else {
					result = "conerror";
				}

			} else {
				result = "noserver";
			}
		} catch (SQLException e) {
			result = e.getMessage();
			e.printStackTrace();
		} catch (Exception e) {
			result = "runerror";
			e.printStackTrace();
		}
		return result;
	}

	public String createtestcasestable(String tableName, long userId, String moduleid, String tsname, String tcname,
			String testcondition, String sqlscript, int projectid) {

		String returnValue = "failure";

		try {
			String checktable = this.isTableAlreadyExisted(tableName);

			if (checktable.equals("notExisted")) {

				String createTabelSQL = "CREATE TABLE " + tableName
						+ " (id bigint(20) NOT NULL auto_increment, moduleid bigint(20) not null,"
						+ " tsname varchar(500) default null, tcname varchar(500) default null, testcondition varchar(8000) default null, sqlscript longtext,"
						+ " executioncount bigint(100) default 0, passcount bigint(100) default 0, failcount bigint(100) default 0, executeduserid bigint(5) default null, "
						+ " updateduserid bigint(5) default null, createdUserId bigint(5) default NULL, createdDate datetime default NULL, updateddate datetime default null, "
						+ " executeddate datetime default null, message varchar(100) default Not Run, queryresult varchar(100) default null, "
						+ " teststatus varchar(10) default null, status tinyint(1) default 1, projectid bigint(10) default 0, PRIMARY KEY (id), UNIQUE KEY(tcname))";

				st = con.createStatement();
				int isTableCreated = st.executeUpdate(createTabelSQL);

				if (isTableCreated == 0)
					returnValue = inserttestcases(tableName, userId, moduleid, tsname, tcname, testcondition, sqlscript,
							projectid);
				else
					returnValue = "failure";
			} else if (checktable.equals("existed")) {
				returnValue = inserttestcases(tableName, userId, moduleid, tsname, tcname, testcondition, sqlscript,
						projectid);
			} else {
				returnValue = "failure";
			}
			st.close();
		} catch (Exception e) {
			returnValue = "error";
			e.printStackTrace();
		}
		return returnValue;
	}

	public String inserttestcases(String tableName, long userId, String modulename, String tsname, String tcname,
			String testcondition, String sqlscript, int projectid) {
		String result = "failure";
		try {

			String id = "select id from modules where module = '" + modulename + "'";
			st = con.createStatement();
			rs = st.executeQuery(id);
			while (rs.next()) {
				sql = "insert into " + tableName
						+ " (moduleid, tsname, tcname, testcondition, sqlscript, createdUserId, createdDate, projectid) values( ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
				ps = con.prepareStatement(sql);
				ps.setString(1, rs.getString(1));
				ps.setString(2, tsname);
				ps.setString(3, tcname);
				ps.setString(4, testcondition);
				ps.setString(5, sqlscript);
				ps.setLong(6, userId);
				ps.setInt(7, projectid);
				int status = ps.executeUpdate();
				if (status == 1) {
					result = "success";
				} else {
					result = "duplicate";
				}
				ps.close();
			}
			rs.close();
			st.close();

		} catch (Exception e) {
			result = "failure";
			e.printStackTrace();
		}
		return result;
	}

	public String updatetabledata(String tableName, String columnname, String columnvalue, String wherecolumn,
			String wherecolumnvalue) {
		String result = "failure";
		try {
			sql = "update " + tableName + " set `" + columnname + "` = ? where " + wherecolumn + "= ?";
			ps = con.prepareStatement(sql);
			if (columnname.equals("moduleid")) {
				String id = "select id from modules where module = '" + columnvalue + "'";
				st = con.createStatement();
				rs = st.executeQuery(id);
				while (rs.next()) {
					ps.setString(1, rs.getString(1));
				}
				rs.close();
				st.close();
			} else if (columnname.equals("updateddate") || columnname.equals("executeddate")) {
				java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
				ps.setDate(1, sqlDate);
			} else if (columnname.equals("executioncount")) {
				String id = "select executioncount from " + tableName + " where `" + wherecolumn + "` = '"
						+ wherecolumnvalue + "'";
				st = con.createStatement();
				rs = st.executeQuery(id);
				while (rs.next()) {
					ps.setInt(1, rs.getInt(1) + 1);
				}
				rs.close();
				st.close();
			} else if (columnname.equals("passcount")) {
				String id = "select passcount from " + tableName + " where `" + wherecolumn + "` = '" + wherecolumnvalue
						+ "'";
				st = con.createStatement();
				rs = st.executeQuery(id);
				while (rs.next()) {
					ps.setInt(1, rs.getInt(1) + 1);
				}
				rs.close();
				st.close();
			} else if (columnname.equals("failcount")) {
				String id = "select failcount from " + tableName + " where `" + wherecolumn + "` = '" + wherecolumnvalue
						+ "'";
				st = con.createStatement();
				rs = st.executeQuery(id);
				while (rs.next()) {
					ps.setInt(1, rs.getInt(1) + 1);
				}
				rs.close();
				st.close();
			} else {
				ps.setString(1, columnvalue);
			}
			ps.setString(2, wherecolumnvalue);

			int status = ps.executeUpdate();
			if (status == 1) {
				result = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "failure";
		}
		return result;
	}

	public ArrayList<FieldtoFieldBinaryTrade> getTestCasesDetails(String tableName, String startdate, String enddate,
			String release, int projectid) {

		ArrayList<FieldtoFieldBinaryTrade> ff = new ArrayList<FieldtoFieldBinaryTrade>();
		String checktable = isTableAlreadyExisted(tableName);
		try {
			if (checktable.equals("existed")) {
				if (release != null) {
					sql = "select id, module, tsname, tcname, testcondition, sqlscript, executioncount, passcount, failcount, executedby, updatedby, createdby, createdDate, updateddate, executeddate, message, "
							+ "queryresult, teststatus, `release`, `cycle`, suitename, testsuiteid from (select tc.id, (select module from modules where  id = tc.moduleid)as module, tc.tsname, tc.tcname, tc.testcondition, tc.sqlscript, tc.executioncount, "
							+ "tc.passcount, tc.failcount, (select userId from users where id = tc.executeduserid)as executedby, (select userId from users "
							+ "where id = tc.updateduserid)as updatedby,  (select userId from users where id = tc.createdUserId)as createdby, "
							+ "DATE_FORMAT(tc.createdDate, '%Y-%m-%d')as createdDate, DATE_FORMAT(tc.updateddate, '%Y-%m-%d') as updateddate, "
							+ "DATE_FORMAT(tc.executeddate, '%Y-%m-%d')as executeddate, tc.message, tc.queryresult, tc.teststatus, ts.`release`, ts.`cycle`, ts.suitename, "
							+ "ts.id as testsuiteid from testsuites ts "
							+ "join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on tc.moduleid = (select id from modules where module = "
							+ "tsd.selecteditems) where ts.`type` = 'modules' and ts.`release` = '" + release
							+ "' and tc.status = 1 and tc.projectid = '" + projectid + "' group by tc.id union all "
							+ "select tc.id, (select module from modules where  id = tc.moduleid)as module, tc.tsname, tc.tcname, tc.testcondition, tc.sqlscript, tc.executioncount, tc.passcount, "
							+ "tc.failcount, (select userId from users where id = tc.executeduserid)as executedby, (select userId from users where id = "
							+ "tc.updateduserid)as updatedby,  (select userId from users where id = tc.createdUserId)as createdby, DATE_FORMAT(tc.createdDate, "
							+ "'%Y-%m-%d')as createdDate, DATE_FORMAT(tc.updateddate, '%Y-%m-%d') as updateddate, DATE_FORMAT(tc.executeddate, '%Y-%m-%d')as "
							+ "executeddate, tc.message, tc.queryresult, tc.teststatus, ts.`release`, ts.`cycle`, ts.suitename, ts.id as testsuiteid "
							+ "from testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id "
							+ "join testcases tc on tc.tsname = tsd.selecteditems where ts.`type` = 'testscenario' and ts.`release` = '"
							+ release + "' and tc.status = 1 and tc.projectid = '" + projectid
							+ "' group by tc.id union all select tc.id, (select module from modules where  id = tc.moduleid)as module, tc.tsname, tc.tcname, tc.testcondition, tc.sqlscript, "
							+ "tc.executioncount, tc.passcount, tc.failcount, (select userId from users where id = tc.executeduserid)as executedby, "
							+ "(select userId from users where id = tc.updateduserid)as updatedby,  (select userId from users where id = tc.createdUserId)as createdby, "
							+ "DATE_FORMAT(tc.createdDate, '%Y-%m-%d')as createdDate, DATE_FORMAT(tc.updateddate, '%Y-%m-%d') as updateddate, "
							+ "DATE_FORMAT(tc.executeddate, '%Y-%m-%d')as executeddate, tc.message, tc.queryresult, tc.teststatus, ts.`release`, ts.`cycle`, "
							+ "ts.suitename, ts.id as testsuiteid  from testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc "
							+ "on tc.tcname = tsd.selecteditems where ts.`type` = 'testcase' and ts.`release` = '"
							+ release + "' and tc.status = 1 and tc.projectid = '" + projectid
							+ "' group by tc.id ) result group by tcname, cycle, tsname";
				} else {
					sql = "select id, (select module from modules where id = moduleid)as modulename, tsname, tcname, testcondition, sqlscript, executioncount, "
							+ "passcount, failcount, (select userId from users where id = executeduserid)as executedby, (select userId from users where "
							+ "id = updateduserid)as updatedby, (select userId from users where id = createdUserId)as createdby, DATE_FORMAT(createdDate, '%Y-%m-%d'), "
							+ "DATE_FORMAT(updateddate, '%Y-%m-%d'), DATE_FORMAT(executeddate, '%Y-%m-%d'), message, queryresult, teststatus from "
							+ tableName + " where DATE_FORMAT(createdDate, '%Y-%m-%d') >= '" + startdate
							+ "' and DATE_FORMAT(createdDate, '%Y-%m-%d') <= '" + enddate
							+ "' and status = 1 and projectid = '" + projectid + "'";
				}
				// } else if (module == null && tsname != null) {
				// sql = "select id, (select module from modules where id =
				// moduleid)as modulename, tsname, tcname, testcondition,
				// sqlscript, executioncount, passcount, failcount, "
				// + "(select userId from users where id = executeduserid)as
				// executedby, (select userId from users where id =
				// updateduserid)as updatedby, "
				// + "(select userId from users where id = createdUserId)as
				// createdby, DATE_FORMAT(createdDate, '%Y-%m-%d'),
				// DATE_FORMAT(updateddate, '%Y-%m-%d'), "
				// + "DATE_FORMAT(executeddate, '%Y-%m-%d'), message,
				// queryresult, teststatus from "
				// + tableName + " where DATE_FORMAT(createdDate, '%Y-%m-%d') >=
				// '" + startdate
				// + "' and DATE_FORMAT(createdDate, '%Y-%m-%d') <= '" + enddate
				// + "' and status = 1 and tsname = '" + tsname + "'";
				// }
				st = con.createStatement();
				rs = st.executeQuery(sql);

				while (rs.next()) {
					FieldtoFieldBinaryTrade ftfb = new FieldtoFieldBinaryTrade();
					ftfb.setId(rs.getString(1)); // testcaseID
					ftfb.setModulename(rs.getString(2));
					ftfb.setTsname(rs.getString(3));
					ftfb.setTcname(rs.getString(4));
					ftfb.setTestcondition(rs.getString(5));
					ftfb.setSqlscript(rs.getString(6));
					ftfb.setExecutioncount(rs.getString(7));
					ftfb.setPasscount(rs.getString(8));
					ftfb.setFailcount(rs.getString(9));
					ftfb.setExecutedby(rs.getString(10));
					ftfb.setUpdatedby(rs.getString(11));
					ftfb.setCreatedby(rs.getString(12));
					ftfb.setCreateddate(rs.getString(13));
					ftfb.setUpdateddate(rs.getString(14));
					ftfb.setExecuteddate(rs.getString(15));
					ftfb.setMessage(rs.getString(16));
					ftfb.setQueryresult(rs.getString(17));
					ftfb.setTeststatus(rs.getString(18));
					if (release != null) {
						ftfb.setRelease(rs.getString(19));
						ftfb.setCycle(rs.getString(20));
						ftfb.setTestsuitename(rs.getString(21));
						ftfb.setTestsuiteid(rs.getString(22));
					}

					ff.add(ftfb);
				}

				rs.close();
				st.close();
			} else {
				ff = null;
			}

		} catch (Exception e) {
			ff = null;
			e.printStackTrace();
		}
		return ff;
	}

	public ArrayList<FieldtoFieldBinaryTrade> getScriptsforFieldonDash(String tableName, String release, String cycle,
			String testsuite) {

		ArrayList<FieldtoFieldBinaryTrade> ff = new ArrayList<FieldtoFieldBinaryTrade>();
		String checktable = isTableAlreadyExisted(tableName);
		try {
			if (checktable.equals("existed")) {
				if (release != null && cycle == null && testsuite == null) {
					sql = "select testsuiteid, id, `release`, `cycle`,  sqlscript from (select ts.id as testsuiteid, tc.id, ts.release, ts.cycle, tc.sqlscript from "
							+ "testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on tc.moduleid = "
							+ "(select id from modules where module = tsd.selecteditems) where ts.`type` = 'modules' and ts.`release` = '"
							+ release + "' and "
							+ "tc.status = 1  group by tc.id union all select ts.id as testsuiteid, tc.id, ts.release, ts.cycle, tc.sqlscript from "
							+ "testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on tc.tsname = tsd.selecteditems "
							+ "where ts.`type` = 'testscenario' and ts.`release` = '" + release
							+ "' and tc.status = 1 group by tc.id union all "
							+ "select ts.id as testsuiteid, tc.id, ts.release, ts.cycle, tc.sqlscript from testsuites ts join testsuitedetails tsd on "
							+ "tsd.suiteid = ts.id join testcases tc on tc.tcname = tsd.selecteditems where ts.`type` = 'testcase' and ts.`release` = '"
							+ release + "' "
							+ "and tc.status = 1 group by tc.id ) result group by id, cycle, testsuiteid";
				} else if (release != null && cycle != null && testsuite == null) {
					sql = "select testsuiteid, id, `release`, `cycle`,  sqlscript from (select ts.id as testsuiteid, tc.id, ts.release, ts.cycle, tc.sqlscript from "
							+ "testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on tc.moduleid = "
							+ "(select id from modules where module = tsd.selecteditems) where ts.`type` = 'modules' and ts.`release` = '"
							+ release + "' and ts.`cycle` = '" + cycle
							+ "' and tc.status = 1  group by tc.id union all select ts.id as testsuiteid, tc.id, ts.release, ts.cycle, tc.sqlscript from "
							+ "testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on tc.tsname = tsd.selecteditems "
							+ "where ts.`type` = 'testscenario' and ts.`release` = '" + release + "' and ts.`cycle` = '"
							+ cycle + "' and tc.status = 1 group by tc.id union all "
							+ "select ts.id as testsuiteid, tc.id, ts.release, ts.cycle, tc.sqlscript from testsuites ts join testsuitedetails tsd on "
							+ "tsd.suiteid = ts.id join testcases tc on tc.tcname = tsd.selecteditems where ts.`type` = 'testcase' and ts.`release` = '"
							+ release + "' " + "and ts.`cycle` = '" + cycle
							+ "' and tc.status = 1 group by tc.id ) result group by id, cycle, testsuiteid";
				} else if (release != null && cycle != null && testsuite != null) {
					sql = "select testsuiteid, id, `release`, `cycle`,  sqlscript from (select ts.id as testsuiteid, tc.id, ts.release, ts.cycle, tc.sqlscript from "
							+ "testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on tc.moduleid = "
							+ "(select id from modules where module = tsd.selecteditems) where ts.`type` = 'modules' and ts.`release` = '"
							+ release + "' and ts.`cycle` = '" + cycle + "' and ts.suitename = '" + testsuite + "' and "
							+ " tc.status = 1  group by tc.id union all select ts.id as testsuiteid, tc.id, ts.release, ts.cycle, tc.sqlscript from "
							+ "testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on tc.tsname = tsd.selecteditems "
							+ "where ts.`type` = 'testscenario' and ts.`release` = '" + release + "' and ts.`cycle` = '"
							+ cycle + "' and ts.suitename = '" + testsuite
							+ "' and tc.status = 1 group by tc.id union all "
							+ "select ts.id as testsuiteid, tc.id, ts.release, ts.cycle, tc.sqlscript from testsuites ts join testsuitedetails tsd on "
							+ "tsd.suiteid = ts.id join testcases tc on tc.tcname = tsd.selecteditems where ts.`type` = 'testcase' and ts.`release` = '"
							+ release + "' " + "and ts.`cycle` = '" + cycle + "' and ts.suitename = '" + testsuite
							+ "' and tc.status = 1 group by tc.id ) result group by id, cycle, testsuiteid";
				}

				st = con.createStatement();
				rs = st.executeQuery(sql);

				while (rs.next()) {
					FieldtoFieldBinaryTrade ftfb = new FieldtoFieldBinaryTrade();
					ftfb.setTestsuiteid(rs.getString(1));
					ftfb.setId(rs.getString(2)); // tcID
					ftfb.setRelease(rs.getString(3));
					ftfb.setCycle(rs.getString(4));
					ftfb.setSqlscript(rs.getString(5));

					ff.add(ftfb);
				}

				rs.close();
				st.close();
			} else {
				ff = null;
			}

		} catch (Exception e) {
			ff = null;
			e.printStackTrace();
		}
		return ff;
	}

	public ArrayList<TestScenariosBinaryTrade> getTSNameDetails(String tableName, String module, int projectid) {

		ArrayList<TestScenariosBinaryTrade> ts = new ArrayList<TestScenariosBinaryTrade>();
		String checktable = isTableAlreadyExisted(tableName);
		try {
			if (checktable.equals("existed")) {
				if (module == null) {
					sql = "select distinct(tsname) from " + tableName + " where projectid = '" + projectid + "'";
				} else {
					sql = "select distinct(tsname) from " + tableName
							+ " where moduleid = (select id from modules where module = '" + module
							+ "') and projectid = '" + projectid + "'";
				}

				st = con.createStatement();
				rs = st.executeQuery(sql);

				while (rs.next()) {
					TestScenariosBinaryTrade tsb = new TestScenariosBinaryTrade();
					tsb.setTsname(rs.getString(1));

					ts.add(tsb);
				}

				rs.close();
				st.close();
			} else {
				ts = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ts = null;
		}
		return ts;
	}

	public ArrayList<TestScenariosBinaryTrade> getTCNameDetails(String tableName, String testscenario) {

		ArrayList<TestScenariosBinaryTrade> ts = new ArrayList<TestScenariosBinaryTrade>();
		String checktable = isTableAlreadyExisted(tableName);
		try {
			if (checktable.equals("existed")) {
				if (testscenario == null) {
					sql = "select tcname from " + tableName;
				} else {
					sql = "select tcname from " + tableName + " where tsname = '" + testscenario + "'";
				}
				st = con.createStatement();
				rs = st.executeQuery(sql);

				while (rs.next()) {
					TestScenariosBinaryTrade tsb = new TestScenariosBinaryTrade();
					tsb.setTcname(rs.getString(1));

					ts.add(tsb);
				}

				rs.close();
				st.close();
			} else {
				ts = null;
			}
		} catch (Exception e) {
			ts = null;
		}
		return ts;
	}

	public String createresultstable(String tableName, long userId, String testcaseid, String message,
			String queryresult, String status, int batchid, int testsuiteid, String releasename, String cyclename,
			int serverid) {

		String returnValue = "failure";

		try {
			String checktable = this.isTableAlreadyExisted(tableName);

			if (checktable.equals("notExisted")) {

				String createTabelSQL = "CREATE TABLE " + tableName
						+ " (id bigint(255) NOT NULL auto_increment, batchid bigint(100) default 0, testsuiteid bigint(10) default 0, testcaseid bigint(20) not null, "
						+ "message varchar(100) default Not Run, release varchar(200) default null, cycle varchar(200) default null,"
						+ "queryresult varchar(100) default null, teststatus varchar(10) default null, executeduserid bigint(5) default null, serverid int(10) default 1"
						+ "createdDate datetime default CURRENT_TIMESTAMP, PRIMARY KEY (id))";

				st = con.createStatement();
				int isTableCreated = st.executeUpdate(createTabelSQL);

				if (isTableCreated == 0)
					returnValue = insertresults(tableName, userId, testcaseid, message, queryresult, status, batchid,
							testsuiteid, releasename, cyclename, serverid);
				else
					returnValue = "failure";
			} else if (checktable.equals("existed")) {
				returnValue = insertresults(tableName, userId, testcaseid, message, queryresult, status, batchid,
						testsuiteid, releasename, cyclename, serverid);
			} else {
				returnValue = "failure";
			}
			st.close();
		} catch (Exception e) {
			returnValue = "error";
			e.printStackTrace();
		}
		return returnValue;
	}

	public String insertresults(String tableName, long userId, String testcaseid, String message, String queryresult,
			String status, int batchid, int testsuiteid, String releasename, String cyclename, int serverid) {
		String result = "failure";
		try {

			sql = "insert into " + tableName
					+ " (batchid, testsuiteid, testcaseid, message, queryresult, teststatus, executeduserid, `release`, `cycle`, serverid) values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
			ps = con.prepareStatement(sql);
			ps.setInt(1, batchid);
			ps.setInt(2, testsuiteid);
			ps.setString(3, testcaseid);
			ps.setString(4, message);
			ps.setString(5, queryresult);
			ps.setString(6, status);
			ps.setLong(7, userId);
			ps.setString(8, releasename);
			ps.setString(9, cyclename);
			ps.setInt(10, serverid);

			int status1 = ps.executeUpdate();
			if (status1 == 1) {
				result = "success";
			}
			ps.close();

		} catch (Exception e) {
			result = "failure";
			e.printStackTrace();
		}
		return result;
	}

	public int getmaxbatchid() {
		int value = 0;
		try {
			String checktable = isTableAlreadyExisted("fieldresults");

			if (checktable.equals("existed")) {
				sql = "select max(batchid) from fieldresults";
				st = con.createStatement();
				rs = st.executeQuery(sql);
				if (rs.next()) {
					value = rs.getInt(1);
				}
				rs.close();
				st.close();
			} else {
				value = 0;
			}
		} catch (Exception e) {
			value = 0;
			e.printStackTrace();
		}
		return value;
	}

	public ArrayList<String> getfieldresults(int batchid, int suiteid, int testcaseid, String columns, int countsize,
			String testsuite, int serverid) {

		ArrayList<String> reportsbean = new ArrayList<String>();
		try {
			if (testsuite != null && batchid == 0 && suiteid == 0 && testcaseid == 0) {
				sql = "select " + columns
						+ " from testcases tc join fieldresults fr on tc.id = fr.testcaseid join testsuites ts on ts.id = fr.testsuiteid "
						+ " where fr.testsuiteid = (select id from testsuites where suitename = '" + testsuite
						+ "') and serverid = " + serverid + " order by fr.batchid ";
			} else if (testsuite != null && batchid != 0 && suiteid == 0 && testcaseid == 0) {
				sql = "select " + columns
						+ " from testcases tc join fieldresults fr on tc.id = fr.testcaseid join testsuites ts on ts.id = fr.testsuiteid "
						+ " where fr.testsuiteid = (select id from testsuites where suitename = '" + testsuite
						+ "') and fr.batchid = " + batchid + " and serverid = " + serverid + " order by fr.batchid ";
			} else if (batchid != 0 && suiteid == 0 && testcaseid == 0 && testsuite == null) {
				sql = "select " + columns
						+ " from testcases tc join fieldresults fr on tc.id = fr.testcaseid join testsuites ts on ts.id = fr.testsuiteid "
						+ " where fr.batchid = " + batchid + " and serverid = " + serverid + " order by fr.batchid";
			} else if (suiteid != 0 && batchid == 0 && testcaseid == 0 && testsuite == null) {
				sql = "select " + columns
						+ " from testcases tc join fieldresults fr on tc.id = fr.testcaseid join testsuites ts on ts.id = fr.testsuiteid "
						+ " where fr.testsuiteid = " + suiteid + " and serverid = " + serverid
						+ " order by fr.testsuiteid";
			} else if (suiteid == 0 && batchid == 0 && testcaseid != 0 && testsuite == null) {
				sql = "select " + columns
						+ " from testcases tc join fieldresults fr on tc.id = fr.testcaseid join testsuites ts on ts.id = fr.testsuiteid "
						+ " where fr.testcaseid = " + testcaseid + " and serverid = " + serverid
						+ " order by fr.testcaseid";
			} else if (batchid != 0 && suiteid != 0 && testcaseid == 0 && testsuite == null) {
				sql = "select " + columns
						+ " from testcases tc join fieldresults fr on tc.id = fr.testcaseid join testsuites ts on ts.id = fr.testsuiteid"
						+ " where fr.batchid = " + batchid + " and fr.testsuiteid = " + suiteid + " and serverid = "
						+ serverid + " order by fr.batchid, fr.testsuiteid";
			} else if (batchid != 0 && suiteid == 0 && testcaseid != 0 && testsuite == null) {
				sql = "select " + columns
						+ " from testcases tc join fieldresults fr on tc.id = fr.testcaseid join testsuites ts on ts.id = fr.testsuiteid"
						+ " where fr.batchid = " + batchid + " and fr.testcaseid = " + testcaseid + " and serverid = "
						+ serverid + " order by fr.batchid, fr.testcaseid";
			} else if (batchid == 0 && suiteid != 0 && testcaseid != 0 && testsuite == null) {
				sql = "select " + columns
						+ " from testcases tc join fieldresults fr on tc.id = fr.testcaseid join testsuites ts on ts.id = fr.testsuiteid"
						+ " where fr.testsuiteid = " + suiteid + " and fr.testcaseid = " + testcaseid
						+ " and serverid = " + serverid + " order by fr.testsuiteid, fr.testcaseid";
			} else if (batchid != 0 && suiteid != 0 && testcaseid != 0 && testsuite == null) {
				sql = "select " + columns
						+ " from testcases tc join fieldresults fr on tc.id = fr.testcaseid join testsuites ts on ts.id = fr.testsuiteid"
						+ " where fr.batchid = " + batchid + " and fr.testsuiteid = " + suiteid
						+ " and fr.testcaseid = " + testcaseid + " and serverid = " + serverid
						+ " order by fr.batchid, fr.testsuiteid, fr.testcaseid";
			}

			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				for (int i = 1; i <= countsize; i++) {
					reportsbean.add(rs.getString(i));
				}
			}
			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			reportsbean = null;
		}
		return reportsbean;
	}

	public String createtestsuite(String tableName, long userId, String suitename, String type, String release,
			String cycle, ArrayList<TestSuiteBinaryTrade> selecteditems, int projectid) {

		String returnValue = "failure";

		try {
			String checktable = this.isTableAlreadyExisted(tableName);

			if (checktable.equals("notExisted")) {

				String createTabelSQL = "CREATE TABLE " + tableName
						+ " (id bigint(20) NOT NULL auto_increment, release varchar(200) default null, cycle varchar(200) default null, suitename varchar(300) default null, type varchar(15) default null, "
						+ " timer datetime default null, status bigint(1) default 1, createdby bigint(5) default null, "
						+ "createdDate datetime default null, updatedby bigint(5) default null, updateddate datetime default null, PRIMARY KEY (id), UNIQUE KEY(suitename))";

				st = con.createStatement();
				int isTableCreated = st.executeUpdate(createTabelSQL);

				if (isTableCreated == 0)
					returnValue = inserttestsuite(tableName, userId, suitename, type, release, cycle, selecteditems,
							projectid);
				else
					returnValue = "failure";
			} else if (checktable.equals("existed")) {
				returnValue = inserttestsuite(tableName, userId, suitename, type, release, cycle, selecteditems,
						projectid);
			} else {
				returnValue = "failure";
			}
			st.close();
		} catch (Exception e) {
			returnValue = "error";
			e.printStackTrace();
		}
		return returnValue;
	}

	public String inserttestsuite(String tableName, long userId, String suitename, String type, String release,
			String cycle, ArrayList<TestSuiteBinaryTrade> selecteditems, int projectid) {
		String result = "failure";
		try {

			sql = "insert into " + tableName
					+ " (suitename, `type`, createdby, `release`, cycle, createdDate, projectid) values( ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, suitename);
			ps.setString(2, type);
			ps.setLong(3, userId);
			ps.setString(4, release);
			ps.setString(5, cycle);
			ps.setInt(6, projectid);

			int status1 = ps.executeUpdate();
			if (status1 == 1) {
				String sql1 = "select id from " + tableName + " where suitename = '" + suitename + "'";
				st = con.createStatement();
				rs = st.executeQuery(sql1);
				if (rs.next()) {
					result = createtestsuitedetails("testsuitedetails", rs.getInt(1), selecteditems);
				} else {
					result = "failure";
				}
				rs.close();
				st.close();
			} else {
				result = "duplicate";
			}
			ps.close();

		} catch (Exception e) {
			result = "failure";
			e.printStackTrace();
		}
		return result;
	}

	public String createtestsuitedetails(String tableName, int suiteid, ArrayList<TestSuiteBinaryTrade> selecteditems) {

		String returnValue = "failure";

		try {
			String checktable = this.isTableAlreadyExisted(tableName);

			if (checktable.equals("notExisted")) {

				String createTabelSQL = "CREATE TABLE " + tableName
						+ " (id bigint(255) NOT NULL auto_increment, suiteid bigint(20) default null, selecteditems varchar(500) default null,  PRIMARY KEY (id))";

				st = con.createStatement();
				int isTableCreated = st.executeUpdate(createTabelSQL);

				if (isTableCreated == 0)
					returnValue = inserttestsuitedetails(tableName, suiteid, selecteditems);
				else
					returnValue = "failure";
			} else if (checktable.equals("existed")) {
				returnValue = inserttestsuitedetails(tableName, suiteid, selecteditems);
			} else {
				returnValue = "failure";
			}
			st.close();
		} catch (Exception e) {
			returnValue = "error";
			e.printStackTrace();
		}
		return returnValue;
	}

	public String inserttestsuitedetails(String tableName, int suiteid, ArrayList<TestSuiteBinaryTrade> selecteditems) {
		String result = "failure";
		try {
			for (int i = 0; i < selecteditems.size(); i++) {
				sql = "insert into " + tableName + " (suiteid, selecteditems) values( ?, ?)";
				ps = con.prepareStatement(sql);
				ps.setInt(1, suiteid);
				ps.setString(2, selecteditems.get(i).getSelecteditems());

				int status1 = ps.executeUpdate();
				if (status1 == 1) {
					result = "success";
				}
				ps.close();
			}
		} catch (Exception e) {
			result = "failure";
			e.printStackTrace();
		}
		return result;
	}

	public String delete(String tablename, String wherecolumn, String columnvalue) {
		String result = "success";
		try {
			sql = "delete from " + tablename + " where `" + wherecolumn + "` = '" + columnvalue + "'";
			ps = con.prepareStatement(sql);
			ps.executeUpdate();

			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = "failure";
		}
		return result;
	}

	public ArrayList<TestSuiteBinaryTrade> gettestsuites(String tablename, String id, int projectid) {
		ArrayList<TestSuiteBinaryTrade> ts = new ArrayList<TestSuiteBinaryTrade>();
		String checktable = isTableAlreadyExisted(tablename);
		try {
			if (checktable.equals("existed")) {

				if (id == null) {
					sql = "select ts.id, suitename, ts.type , timer, ts.release, ts.cycle, case when ts.type = 'modules' then "
							+ "( select count(1) from testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id "
							+ "join modules ms on tsd.selecteditems = ms.module join testcases tc on tc.moduleid = ms.id) "
							+ "when ts.type = 'testcase' then (select count(1) from testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id "
							+ "join testcases tc on tc.tcname = tsd.selecteditems) when ts.type = 'testscenario' then "
							+ "(select count(1) from testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on tc.tsname = tsd.selecteditems) "
							+ "end, case when ts.type = 'modules' then ( select count(1) from testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id "
							+ "join modules ms on tsd.selecteditems = ms.module join testcases tc on tc.moduleid = ms.id where tc.teststatus = 'Pass') "
							+ "when ts.type = 'testcase' then (select count(1) from testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id "
							+ "join testcases tc on tc.tcname = tsd.selecteditems where tc.teststatus = 'Pass') when ts.type = 'testscenario' then "
							+ "(select count(1) from testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on tc.tsname = tsd.selecteditems "
							+ "where tc.teststatus = 'Pass') end, case when ts.type = 'modules' then ( select count(1) from testsuites ts join testsuitedetails tsd "
							+ "on tsd.suiteid = ts.id join modules ms on tsd.selecteditems = ms.module join testcases tc on tc.moduleid = ms.id "
							+ "where tc.teststatus = 'Fail') when ts.type = 'testcase' then (select count(1) from testsuites ts join testsuitedetails tsd "
							+ "on tsd.suiteid = ts.id join testcases tc on tc.tcname = tsd.selecteditems where tc.teststatus = 'Fail') when ts.type = 'testscenario' "
							+ "then (select count(1) from testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on "
							+ "tc.tsname = tsd.selecteditems where tc.teststatus = 'Fail') end, "
							+ "(select GROUP_CONCAT(selecteditems) from testsuitedetails where suiteid = ts.id) as selecteditems from testsuites ts where ts.status =1 and ts.projectid = '"
							+ projectid + "'";
				} else {
					String checktype1 = "select type from testsuites where id = '" + id + "' ";
					st1 = con.createStatement();
					rs1 = st1.executeQuery(checktype1);

					while (rs1.next()) {
						if (rs1.getString(1).equals("modules")) {
							sql = "select ts.id, suitename, type, timer, ts.release, ts.cycle, count(tc.id) as total, "
									+ "(select count(1) from testcases tcs where tcs.teststatus = 'Pass' and tcs.moduleid = ms.id) as Pass, "
									+ "(select count(1) from testcases tcs where tcs.teststatus = 'Fail'  and tcs.moduleid = ms.id) as Fail from testsuites ts "
									+ "join testsuitedetails tsd on tsd.suiteid = ts.id join modules ms on ms.module = tsd.selecteditems "
									+ "join testcases tc on ms.id = tc.moduleid where ms.module = tsd.selecteditems and ts.id = '"
									+ id + "'";
						} else if (rs1.getString(1).equals("testcase")) {
							sql = "select ts.id, suitename, type, timer, ts.release, ts.cycle, count(tc.id) as total, "
									+ "(select count(1) from testcases tcs where tcs.teststatus = 'Pass' and tcs.tcname = tc.tcname) as Pass, "
									+ "(select count(1) from testcases tcs where tcs.teststatus = 'Fail'  and tcs.tcname = tc.tcname) as Fail from testsuites ts "
									+ "join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on tc.tcname = tsd.selecteditems "
									+ "where tc.tcname = tsd.selecteditems and ts.type = 'testcase'";

						} else if (rs1.getString(1).equals("testscenario")) {
							sql = "select ts.id, suitename, type, timer, ts.release, ts.cycle, count(tc.id) as total, "
									+ "(select count(1) from testcases tcs where tcs.teststatus = 'Pass' and tcs.tcname = tc.tcname) as Pass, "
									+ "(select count(1) from testcases tcs where tcs.teststatus = 'Fail'  and tcs.tcname = tc.tcname) as Fail from testsuites ts "
									+ "join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on tc.tcname = tsd.selecteditems "
									+ "where tc.tcname = tsd.selecteditems and ts.type = 'testscenario'";
						}
					}
					rs1.close();
					st1.close();
				}
				st = con.createStatement();
				rs = st.executeQuery(sql);
				while (rs.next() && rs.getString(1) != null) {
					TestSuiteBinaryTrade tsb = new TestSuiteBinaryTrade();
					tsb.setId(rs.getString(1));
					tsb.setTestsuitename(rs.getString(2));
					tsb.setSelectiontype(rs.getString(3));
					tsb.setTimer(rs.getString(4));
					tsb.setRelease(rs.getString(5));
					tsb.setCycle(rs.getString(6));
					tsb.setTestcasescount(rs.getString(7));
					tsb.setPasscount(rs.getString(8));
					tsb.setFailcount(rs.getString(9));
					if (id == null) {
						tsb.setSelecteditems(rs.getString(10));
					}

					if (rs.getInt(7) == 0) {
						tsb.setPassper("0");
						tsb.setFailper("0");
					} else {
						double totalcount = 0, passcount = 0, failcount = 0, passper = 0, failper = 0;

						totalcount = rs.getInt(7);
						passcount = rs.getInt(8);
						failcount = rs.getInt(9);
						passper = ((passcount / totalcount) * 100);
						failper = ((failcount / totalcount) * 100);
						tsb.setPassper(CommonFunctions.formatAmount(passper));
						tsb.setFailper(CommonFunctions.formatAmount(failper));
					}
					ts.add(tsb);
				}
				rs.close();
				st.close();
			} else {
				ts = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ts = null;
		}
		return ts;
	}

	// public ArrayList<TestSuiteBinaryTrade> gettestsuitedetails(String id) {
	// ArrayList<TestSuiteBinaryTrade> ts = new
	// ArrayList<TestSuiteBinaryTrade>();
	// try {
	// sql = "select selecteditems from testsuitedetails where suiteid = '" + id
	// + "'";
	// st = con.createStatement();
	// rs = st.executeQuery(sql);
	// while (rs.next()) {
	// TestSuiteBinaryTrade td = new TestSuiteBinaryTrade();
	// td.setSelecteditems(rs.getString(1));
	//
	// ts.add(td);
	// }
	// rs.close();
	// st.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// ts = null;
	// }
	// return ts;
	// }

	public ArrayList<FieldtoFieldBinaryTrade> getSqlScriptforTestSuites(String id, String type) {

		ArrayList<FieldtoFieldBinaryTrade> ff = new ArrayList<FieldtoFieldBinaryTrade>();

		try {
			if (type.equals("modules")) {
				sql = "select tc.id, tc.sqlscript from testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join modules ms on "
						+ "tsd.selecteditems = ms.module join testcases tc on ms.id = tc.moduleid where ts.id = '" + id
						+ "' and ts.type = 'modules'";
			} else if (type.equals("testcase")) {
				sql = "select tc.id, tc.sqlscript from testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on "
						+ "tsd.selecteditems = tc.tcname where ts.id = '" + id + "' and ts.type = 'testcase'";

			} else if (type.equals("testscenario")) {
				sql = "select tc.id, tc.sqlscript from testsuites ts join testsuitedetails tsd on tsd.suiteid = ts.id join testcases tc on "
						+ "tsd.selecteditems = tc.tsname where ts.id = '" + id + "' and ts.type = 'testscenario'";
			}
			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				FieldtoFieldBinaryTrade ftfb = new FieldtoFieldBinaryTrade();
				ftfb.setId(rs.getString(1));
				ftfb.setSqlscript(rs.getString(2));

				ff.add(ftfb);
			}

			rs.close();
			st.close();

		} catch (Exception e) {
			ff = null;
			e.printStackTrace();
		}
		return ff;
	}

	public ArrayList<ControlReportHelperBinaryTrade> getQAServerDB() {
		ArrayList<ControlReportHelperBinaryTrade> dblist = new ArrayList<ControlReportHelperBinaryTrade>();
		ArrayList<QAServerDetailsBinaryTrade> qalist = new ArrayList<QAServerDetailsBinaryTrade>();
		try {
			int serverid = 0;
			serverid = getdefaultserver();
			if (serverid > 0) {
				qalist = getserverdetailsbyid(serverid);
				if (qalist != null && fcon == null) {
					fcon = new OnlineDBAccess().getfConnection(qalist.get(0).getDburl(), qalist.get(0).getUsername(),
							qalist.get(0).getPassword(), qalist.get(0).getClassname());
				}
				if (fcon != null) {
					if (qalist.get(0).getServertype().equals("MY SQL")) {
						fsql = "SELECT schema_name FROM information_schema.schemata";
					} else if (qalist.get(0).getServertype().equals("MSSQL")) {
						fsql = "SELECT name FROM master.dbo.sysdatabases";
					}

					st1 = fcon.createStatement();
					rs1 = st1.executeQuery(fsql);
					while (rs1.next()) {
						ControlReportHelperBinaryTrade crhbt = new ControlReportHelperBinaryTrade();
						crhbt.setDbnames(rs1.getString(1));
						dblist.add(crhbt);
					}
					rs1.close();
					st1.close();
				} else {
					dblist = null;
				}

			} else {
				dblist = null;
			}
		} catch (SQLException e) {
			dblist = null;
			e.printStackTrace();
		} catch (Exception e) {
			dblist = null;
			e.printStackTrace();
		}
		return dblist;
	}

	public ArrayList<ControlReportHelperBinaryTrade> getQAServerTables(String dbname) {
		ArrayList<ControlReportHelperBinaryTrade> tablelist = new ArrayList<ControlReportHelperBinaryTrade>();
		ArrayList<QAServerDetailsBinaryTrade> qalist = new ArrayList<QAServerDetailsBinaryTrade>();
		try {
			int serverid = 0;
			serverid = getdefaultserver();
			if (serverid > 0) {
				qalist = getserverdetailsbyid(serverid);

				if (qalist != null && fcon == null) {
					fcon = new OnlineDBAccess().getfConnection(qalist.get(0).getDburl(), qalist.get(0).getUsername(),
							qalist.get(0).getPassword(), qalist.get(0).getClassname());
				}
				if (fcon != null) {
					if (qalist.get(0).getServertype().equals("MY SQL")) {
						fsql = "SELECT table_name FROM information_schema.tables where table_schema='" + dbname + "'";
					} else if (qalist.get(0).getServertype().equals("MSSQL")) {
						fsql = "SELECT CONCAT(TABLE_SCHEMA,'.',TABLE_NAME)as name FROM " + dbname
								+ ".INFORMATION_SCHEMA.TABLES";
					}

					st1 = fcon.createStatement();
					rs1 = st1.executeQuery(fsql.toLowerCase());
					while (rs1.next()) {
						ControlReportHelperBinaryTrade crhbt = new ControlReportHelperBinaryTrade();
						crhbt.setTablenames(rs1.getString(1));
						tablelist.add(crhbt);
					}
					rs1.close();
					st1.close();
				} else {
					tablelist = null;
				}

			} else {
				tablelist = null;
			}
		} catch (SQLException e) {
			tablelist = null;
			e.printStackTrace();
		} catch (Exception e) {
			tablelist = null;
			e.printStackTrace();
		}
		return tablelist;
	}

	public ArrayList<ControlReportHelperBinaryTrade> getQAServerColumns(String tablename, String dbname) {
		ArrayList<ControlReportHelperBinaryTrade> columnlist = new ArrayList<ControlReportHelperBinaryTrade>();
		ArrayList<QAServerDetailsBinaryTrade> qalist = new ArrayList<QAServerDetailsBinaryTrade>();
		try {
			int serverid = 0;
			serverid = getdefaultserver();
			if (serverid > 0) {
				qalist = getserverdetailsbyid(serverid);
				if (qalist != null && fcon == null) {
					fcon = new OnlineDBAccess().getfConnection(qalist.get(0).getDburl(), qalist.get(0).getUsername(),
							qalist.get(0).getPassword(), qalist.get(0).getClassname());
				}
				if (fcon != null) {
					if (qalist.get(0).getServertype().equals("MY SQL")) {
						fsql = "select Column_name from information_schema.columns where Table_name = '" + tablename
								+ "'";

						st1 = fcon.createStatement();
						rs1 = st1.executeQuery(fsql.toLowerCase());
						while (rs1.next()) {
							ControlReportHelperBinaryTrade crhbt = new ControlReportHelperBinaryTrade();
							crhbt.setColumnnames(rs1.getString(1));
							columnlist.add(crhbt);
						}
						rs1.close();
						st1.close();
					} else if (qalist.get(0).getServertype().equals("MSSQL")) {
						// fsql = "select column_name from
						// information_schema.columns where
						// concat(TABLE_SCHEMA,'.',TABLE_NAME) = '"
						// + tablename + "'";
						fsql = "select * from " + dbname + "." + tablename + "";

						st1 = fcon.createStatement();
						rs1 = st1.executeQuery(fsql);
						ResultSetMetaData metadata = rs1.getMetaData();

						int columnCount = metadata.getColumnCount();
						for (int i = 0; i < columnCount; i++) {
							ControlReportHelperBinaryTrade crhbt = new ControlReportHelperBinaryTrade();
							crhbt.setColumnnames(metadata.getColumnName(i + 1));
							columnlist.add(crhbt);
						}
						st1.close();
						rs1.close();
					}

				} else {
					columnlist = null;
				}

			} else {
				columnlist = null;
			}
		} catch (SQLException e) {
			columnlist = null;
			e.printStackTrace();
		} catch (Exception e) {
			columnlist = null;
			e.printStackTrace();
		}
		return columnlist;
	}

	public String createControlReportrules(String tableName, String module, String rulename, String sdb, String stable,
			String scolumn, String stdb, String sttable, String stcolumn, String trdb, String trtable, String trcolumn,
			String ldb, String ltable, String lcolumn, String tdb, String ttable, String tcolumn, String sscript,
			String stscript, String transscript, String tscript, long createdby, String scolscript, String stcolscript,
			String transcolscript, String tcolscript, int projectid) {

		String returnValue = "failure";

		try {
			String checktable = this.isTableAlreadyExisted(tableName);

			if (checktable.equals("notExisted")) {

				String createTabelSQL = "CREATE TABLE " + tableName
						+ " (id bigint(255) NOT NULL auto_increment, module varchar(200) default null, rulename varchar(300) default null, sdb varchar(500) default null, "
						+ "stable varchar(500) default null, scolumn varchar(500) default null, sscript longtext, scolscript  longtext, stdb varchar(500) default null, "
						+ "sttable varchar(500) default null, stcolumn varchar(500) default null, stscript longtext, stcolscript  longtext, "
						+ "trdb varchar(500) default null, trtable varchar(500) default null, trcolumn varchar(500) default null, trscript longtext, trcolscript  longtext,"
						+ "ldb varchar(500) default null, ltable varchar(500) default null, lcolumn varchar(500) default null, tdb varchar(500) default null, ttable varchar(500) default null, "
						+ "tcolumn varchar(500) default null, tscript longtext, tcolscript  longtext, createdby bigint (5) default null, updatedby bigint(5) default null, "
						+ "createdDate datetime default NULL, updatedDate datetime default NULL, projectid bigint(10) default 0, PRIMARY KEY (id), UNIQUE KEY(rulename))";

				st = con.createStatement();
				int isTableCreated = st.executeUpdate(createTabelSQL);

				if (isTableCreated == 0)
					returnValue = insertControlReportRules(tableName, module, rulename, sdb, stable, scolumn, stdb,
							sttable, stcolumn, trdb, trtable, trcolumn, ldb, ltable, lcolumn, tdb, ttable, tcolumn,
							sscript, stscript, transscript, tscript, createdby, scolscript, stcolscript, transcolscript,
							tcolscript, projectid);
				else
					returnValue = "failure";
			} else if (checktable.equals("existed")) {
				returnValue = insertControlReportRules(tableName, module, rulename, sdb, stable, scolumn, stdb, sttable,
						stcolumn, trdb, trtable, trcolumn, ldb, ltable, lcolumn, tdb, ttable, tcolumn, sscript,
						stscript, transscript, tscript, createdby, scolscript, stcolscript, transcolscript, tcolscript,
						projectid);
			} else {
				returnValue = "failure";
			}
			st.close();
		} catch (Exception e) {
			returnValue = "error";
			e.printStackTrace();
		}
		return returnValue;
	}

	public String insertControlReportRules(String tableName, String module, String rulename, String sdb, String stable,
			String scolumn, String stdb, String sttable, String stcolumn, String trdb, String trtable, String trcolumn,
			String ldb, String ltable, String lcolumn, String tdb, String ttable, String tcolumn, String sscript,
			String stscript, String transscript, String tscript, long createdby, String scolscript, String stcolscript,
			String transcolscript, String tcolscript, int projectid) {
		String result = "failure";
		try {

			sql = "insert into " + tableName
					+ " (module, rulename, sdb, stable, scolumn, stdb, sttable, stcolumn, trdb, trtable, trcolumn, ldb, ltable, lcolumn, "
					+ "tdb, ttable, tcolumn, createdby, sscript, stscript, trscript, tscript, scolscript, stcolscript, trcolscript, tcolscript, createddate, projectid) "
					+ "values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, module);
			ps.setString(2, rulename);
			ps.setString(3, sdb);
			ps.setString(4, stable);
			ps.setString(5, scolumn);
			ps.setString(6, stdb);
			ps.setString(7, sttable);
			ps.setString(8, stcolumn);
			ps.setString(9, trdb);
			ps.setString(10, trtable);
			ps.setString(11, trcolumn);
			ps.setString(12, ldb);
			ps.setString(13, ltable);
			ps.setString(14, lcolumn);
			ps.setString(15, tdb);
			ps.setString(16, ttable);
			ps.setString(17, tcolumn);
			ps.setLong(18, createdby);
			ps.setString(19, sscript);
			ps.setString(20, stscript);
			ps.setString(21, transscript);
			ps.setString(22, tscript);
			ps.setString(23, scolscript);
			ps.setString(24, stcolscript);
			ps.setString(25, transcolscript);
			ps.setString(26, tcolscript);
			ps.setInt(27, projectid);

			int status1 = ps.executeUpdate();
			if (status1 == 1) {
				result = "success";
			} else {
				result = "duplicate";
			}
			ps.close();

		} catch (Exception e) {
			result = "failure";
			e.printStackTrace();
		}
		return result;
	}

	public String updateControlReportRule(String module, String rulename, String sdb, String stable, String scolumn,
			String stdb, String sttable, String stcolumn, String trdb, String trtable, String trcolumn, String ldb,
			String ltable, String lcolumn, String tdb, String ttable, String tcolumn, String sscript, String stscript,
			String transscript, String tscript, long updatedby, String id, String scolscript, String stcolscript,
			String transcolscript, String tcolscript) {
		String result = "failure";
		try {
			sql = "update controlreportrules set  module = ?, rulename = ?, sdb = ?, stable = ?, scolumn = ?, stdb = ?, sttable = ?, stcolumn = ?, "
					+ " trdb = ?, trtable = ?, trcolumn = ?, ldb = ?, ltable = ?, lcolumn = ?, tdb = ?, ttable = ?, tcolumn = ?, "
					+ " updatedby = ?,  sscript = ?, stscript = ?, trscript = ?, tscript = ?, scolscript = ?, stcolscript = ?, trcolscript = ?, tcolscript = ?, "
					+ " updatedDate = CURRENT_TIMESTAMP where id = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, module);
			ps.setString(2, rulename);
			ps.setString(3, sdb);
			ps.setString(4, stable);
			ps.setString(5, scolumn);
			ps.setString(6, stdb);
			ps.setString(7, sttable);
			ps.setString(8, stcolumn);
			ps.setString(9, trdb);
			ps.setString(10, trtable);
			ps.setString(11, trcolumn);
			ps.setString(12, ldb);
			ps.setString(13, ltable);
			ps.setString(14, lcolumn);
			ps.setString(15, tdb);
			ps.setString(16, ttable);
			ps.setString(17, tcolumn);
			ps.setLong(18, updatedby);
			ps.setString(19, sscript);
			ps.setString(20, stscript);
			ps.setString(21, transscript);
			ps.setString(22, tscript);
			ps.setString(23, scolscript);
			ps.setString(24, stcolscript);
			ps.setString(25, transcolscript);
			ps.setString(26, tcolscript);
			ps.setString(27, id);
			int status11 = ps.executeUpdate();

			if (status11 == 1) {
				result = "success";
			} else {
				result = "failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<ControlReportRulesBinaryTrade> getControlReportRules(String id, int projectid) {
		ArrayList<ControlReportRulesBinaryTrade> rulelist = new ArrayList<ControlReportRulesBinaryTrade>();
		String checktable = isTableAlreadyExisted("controlreportrules");
		if (checktable.equals("existed")) {
			try {
				if (id == null) {
					sql = "select id, module, rulename, sdb, stable, scolumn, stdb, sttable, stcolumn, trdb, trtable, trcolumn, ldb, ltable, "
							+ "lcolumn, tdb, ttable, tcolumn, (select userId from users where id = createdby)as createdby, "
							+ "(select userId from users where id = updatedby)as updatedby, DATE_FORMAT(createdDate, '%Y-%m-%d'), DATE_FORMAT(updatedDate, '%Y-%m-%d'), sscript, "
							+ "stscript, trscript, tscript, scolscript, stcolscript, trcolscript, tcolscript from controlreportrules where projectid = '"
							+ projectid + "'";
				} else {
					sql = "select id, module, rulename, sdb, stable, scolumn, stdb, sttable, stcolumn, trdb, trtable, trcolumn, ldb, ltable, "
							+ "lcolumn, tdb, ttable, tcolumn, (select userId from users where id = createdby)as createdby, "
							+ "(select userId from users where id = updatedby)as updatedby, DATE_FORMAT(createdDate, '%Y-%m-%d'), DATE_FORMAT(updatedDate, '%Y-%m-%d'), sscript,"
							+ " stscript, trscript, tscript, scolscript, stcolscript, trcolscript, tcolscript from controlreportrules where id = '"
							+ id + "' and projectid = '" + projectid + "'";
				}

				st = con.createStatement();
				rs = st.executeQuery(sql);
				while (rs.next()) {
					ControlReportRulesBinaryTrade crbt = new ControlReportRulesBinaryTrade();
					crbt.setId(rs.getString(1));
					crbt.setModule(rs.getString(2));
					crbt.setName(rs.getString(3));
					crbt.setSdb(rs.getString(4));
					crbt.setStable(rs.getString(5));
					crbt.setScolumn(rs.getString(6));
					crbt.setStdb(rs.getString(7));
					crbt.setSttable(rs.getString(8));
					crbt.setStcolumn(rs.getString(9));
					crbt.setTrdb(rs.getString(10));
					crbt.setTrtable(rs.getString(11));
					crbt.setTrcolumn(rs.getString(12));
					crbt.setLdb(rs.getString(13));
					crbt.setLtable(rs.getString(14));
					crbt.setLcolumn(rs.getString(15));
					crbt.setTdb(rs.getString(16));
					crbt.setTtable(rs.getString(17));
					crbt.setTcolumn(rs.getString(18));
					crbt.setCreatedby(rs.getString(19));
					crbt.setUpdatedby(rs.getString(20));
					crbt.setCreatedDate(rs.getString(21));
					crbt.setUpdatedDate(rs.getString(22));
					crbt.setStost(rs.getString(23));
					crbt.setSttotr(rs.getString(24));
					crbt.setTrtol(rs.getString(25));
					crbt.setLtot(rs.getString(26));
					crbt.setStostcol(rs.getString(27));
					crbt.setSttotrcol(rs.getString(28));
					crbt.setTrtolcol(rs.getString(29));
					crbt.setLtotcol(rs.getString(30));

					rulelist.add(crbt);
				}
				rs.close();
				st.close();
			} catch (Exception e) {
				rulelist = null;
				e.printStackTrace();
			}
		} else {
			rulelist = null;
		}

		return rulelist;
	}

	public ArrayList<TestSuiteBinaryTrade> getreleases(String tableName, int projectid) {

		ArrayList<TestSuiteBinaryTrade> ts = new ArrayList<TestSuiteBinaryTrade>();
		String checktable = isTableAlreadyExisted(tableName);
		try {
			if (checktable.equals("existed")) {

				sql = "select distinct(`release`) from " + tableName + " where projectid = '" + projectid + "'";

				st = con.createStatement();
				rs = st.executeQuery(sql);

				while (rs.next()) {
					TestSuiteBinaryTrade tsb = new TestSuiteBinaryTrade();
					tsb.setRelease(rs.getString(1));

					ts.add(tsb);
				}

				rs.close();
				st.close();
			} else {
				ts = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ts = null;
		}
		return ts;
	}

	public ArrayList<TestSuiteBinaryTrade> getcycles(String release, int projectid) {

		ArrayList<TestSuiteBinaryTrade> ts = new ArrayList<TestSuiteBinaryTrade>();

		try {
			sql = "select distinct(`cycle`) from testsuites where `release` = '" + release + "' and projectid = '"
					+ projectid + "'";

			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				TestSuiteBinaryTrade tsb = new TestSuiteBinaryTrade();
				tsb.setCycle(rs.getString(1));

				ts.add(tsb);
			}

			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			ts = null;
		}
		return ts;
	}

	public ArrayList<TestSuiteBinaryTrade> gettestsuitesonly(String cycle, String release, int projectid) {

		ArrayList<TestSuiteBinaryTrade> ts = new ArrayList<TestSuiteBinaryTrade>();

		try {
			sql = "select distinct(`suitename`) from testsuites where `release` = '" + release + "' and cycle = '"
					+ cycle + "' and projectid = '" + projectid + "'";

			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				TestSuiteBinaryTrade tsb = new TestSuiteBinaryTrade();
				tsb.setTestsuitename(rs.getString(1));

				ts.add(tsb);
			}

			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			ts = null;
		}
		return ts;
	}

	public ArrayList<ControlReportExecutionBinaryTrade> getrulenames(String module, int projectid) {

		ArrayList<ControlReportExecutionBinaryTrade> ts = new ArrayList<ControlReportExecutionBinaryTrade>();

		try {
			sql = "select distinct(`rulename`) from controlreportrules where `module` = '" + module
					+ "' and projectid = '" + projectid + "'";

			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				ControlReportExecutionBinaryTrade crbt = new ControlReportExecutionBinaryTrade();
				crbt.setName(rs.getString(1));

				ts.add(crbt);
			}

			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			ts = null;
		}
		return ts;
	}

	public void createcontrolreportresultstable() {
		try {
			String createTableSQL = "CREATE TABLE crresults (id bigint(255) NOT NULL auto_increment, batchid bigint(100) not null, moduleid bigint(255) not null, ruleid bigint(255) not null, "
					+ " message varchar(100) default null, stost varchar(200) default null, sttotr varchar(200) default null, trtol varchar(200) default null, "
					+ " ltot varchar(200) default null, stostdiff varchar(200) default null, sttotrdiff varchar(200) default null, trtoldiff varchar(200) default null, "
					+ " ltotdiff varchar(200) default null, sourcecount varchar(200) default null, stagingcount varchar(200) default null, transcount varchar(200) default null,"
					+ " loadingcount varchar(200) default null, targetcount varchar(200) default null, stostcoldiff  varchar(200) default null, sttotrcoldiff  varchar(200) default null,"
					+ " trtolcoldiff  varchar(200) default null, ltotcoldiff  varchar(200) default null,  sourcecolvalue varchar(200) default null, stagingcolvalue varchar(200) default null,"
					+ " transcolvalue varchar(200) default null, loadingcolvalue varchar(200) default null, targetcolvalue varchar(200) default null, result varchar(10) default null, executeduserid bigint(255) default null, "
					+ "executeddate datetime default CURRENT_TIMESTAMP, PRIMARY KEY (id))";

			st = con.createStatement();
			st.executeUpdate(createTableSQL);
		} catch (Exception e) {

		}
	}

	public ArrayList<ControlReportExecutionBinaryTrade> getrulesBeforeexecution(int projectid) {
		String checktable2 = isTableAlreadyExisted("controlreportrules");
		ArrayList<ControlReportExecutionBinaryTrade> crbt = new ArrayList<ControlReportExecutionBinaryTrade>();
		try {
			if (checktable2.equals("existed")) {
				sql = "select id, module, rulename from controlreportrules where projectid = '" + projectid + "'";

				st = con.createStatement();
				rs = st.executeQuery(sql);

				while (rs.next()) {
					ControlReportExecutionBinaryTrade cr = new ControlReportExecutionBinaryTrade();
					cr.setId(rs.getString(1));
					cr.setModule(rs.getString(2));
					cr.setName(rs.getString(3));
					cr.setStoststatus("Not Executed");
					cr.setSttotrstatus("Not Executed");
					cr.setTrtolstatus("Not Executed");
					cr.setLtotstatus("Not Executed");
					cr.setRulestatus("Not Executed");
					cr.setSourcecount("0");
					cr.setStagingcount("0");
					cr.setTranscount("0");
					cr.setLoadingcount("0");
					cr.setTargetcount("0");
					cr.setSourcecolvalue("0");
					cr.setStagingcolvalue("0");
					cr.setTranscolvalue("0");
					cr.setLoadingcolvalue("0");
					cr.setTargetcolvalue("0");

					crbt.add(cr);
				}
				rs.close();
				st.close();

			} else {
				crbt = null;
			}

		} catch (Exception e) {
			crbt = null;
			e.printStackTrace();
		}
		return crbt;
	}

	public ArrayList<ControlReportExecutionBinaryTrade> getrulesAfterexecution(int batchid, int projectid) {
		String checktable1 = isTableAlreadyExisted("crresults");
		String checktable2 = isTableAlreadyExisted("controlreportrules");
		ArrayList<ControlReportExecutionBinaryTrade> crbt = new ArrayList<ControlReportExecutionBinaryTrade>();
		try {
			if (checktable1.equals("existed")) {
				if (checktable2.equals("existed")) {
					if (batchid == 0) {
						sql = "select crr.id, crr.module, crr.rulename, cr.message, cr.stost, cr.sttotr, cr.trtol, cr.ltot, cr.stostdiff, cr.sttotrdiff, cr.trtoldiff, "
								+ "cr.ltotdiff, cr.sourcecount, cr.stagingcount, cr.transcount, cr.loadingcount, cr.targetcount, cr.result, "
								+ "(select userId from users where id = cr.executeduserid) as executeduserid, cr.executeddate, cr.sourcecolvalue, cr.stagingcolvalue, cr.transcolvalue, cr.loadingcolvalue, cr.targetcolvalue"
								+ " from crresults cr join controlreportrules crr "
								+ "on cr.ruleid =  crr.id where cr.batchid = (select max(crs.batchid) from crresults crs) and crr.projectid = '"
								+ projectid + "' order by cr.batchid";
					} else {
						sql = "select crr.id, crr.module, crr.rulename, cr.message, cr.stost, cr.sttotr, cr.trtol, cr.ltot, cr.stostdiff, cr.sttotrdiff, cr.trtoldiff, "
								+ "cr.ltotdiff, cr.sourcecount, cr.stagingcount, cr.transcount, cr.loadingcount, cr.targetcount, cr.result, "
								+ "(select userId from users where id = cr.executeduserid) as executeduserid, cr.executeddate, cr.sourcecolvalue, cr.stagingcolvalue, cr.transcolvalue, cr.loadingcolvalue, cr.targetcolvalue"
								+ " from crresults cr join controlreportrules crr on cr.ruleid =  crr.id "
								+ " where cr.batchid = " + batchid + " and crr.projectid = '" + projectid
								+ "' order by cr.batchid";
					}
					st = con.createStatement();
					rs = st.executeQuery(sql);

					while (rs.next()) {
						ControlReportExecutionBinaryTrade cr = new ControlReportExecutionBinaryTrade();
						cr.setId(rs.getString(1));
						cr.setModule(rs.getString(2));
						cr.setName(rs.getString(3));
						cr.setMessage(rs.getString(4));
						cr.setStoststatus(rs.getString(5));
						cr.setSttotrstatus(rs.getString(6));
						cr.setTrtolstatus(rs.getString(7));
						cr.setLtotstatus(rs.getString(8));
						cr.setStostdiff(rs.getString(9));
						cr.setSttotrdiff(rs.getString(10));
						cr.setTrtoldiff(rs.getString(11));
						cr.setLtotdiff(rs.getString(12));
						cr.setSourcecount(rs.getString(13));
						cr.setStagingcount(rs.getString(14));
						cr.setTranscount(rs.getString(15));
						cr.setLoadingcount(rs.getString(16));
						cr.setTargetcount(rs.getString(17));
						cr.setRulestatus(rs.getString(18));
						cr.setExecutedby(rs.getString(19));
						cr.setExecutedate(rs.getString(20));
						cr.setSourcecolvalue(rs.getString(21));
						cr.setStagingcolvalue(rs.getString(22));
						cr.setTranscolvalue(rs.getString(23));
						cr.setLoadingcolvalue(rs.getString(24));
						cr.setTargetcolvalue(rs.getString(25));

						crbt.add(cr);
					}
					rs.close();
					st.close();
				} else {
					crbt = null;
				}
			} else {
				createcontrolreportresultstable();
				getrulesAfterexecution(batchid, projectid);
			}

		} catch (Exception e) {
			crbt = null;
			e.printStackTrace();
		}
		return crbt;
	}

	public int getcrmaxbatchid() {
		int value = 0;
		try {
			String checktable = isTableAlreadyExisted("crresults");

			if (checktable.equals("existed")) {
				sql = "select max(batchid) from crresults";
				st = con.createStatement();
				rs = st.executeQuery(sql);
				if (rs.next()) {
					value = rs.getInt(1);
				}
				rs.close();
				st.close();
			} else {
				value = 0;
			}
		} catch (Exception e) {
			value = 0;
			e.printStackTrace();
		}
		return value;
	}

	public String executecrrules(int id, int batchid, Long userid, int serveriddefault) {
		String result = "failure";
		boolean finalresult = true;

		try {
			int serverid = 0;
			serverid = getdefaultserver();
			if (serverid > 0) {
				ArrayList<QAServerDetailsBinaryTrade> qalist = new ArrayList<QAServerDetailsBinaryTrade>();

				qalist = getserverdetailsbyid(serverid);
				if (qalist != null && fcon == null) {
					fcon = new OnlineDBAccess().getfConnection(qalist.get(0).getDburl(), qalist.get(0).getUsername(),
							qalist.get(0).getPassword(), qalist.get(0).getClassname());
				}

				// Source processing
				String sourcesql = "select id, (select id from modules where module = cr.module)as moduleid, sdb, stable, scolumn  from controlreportrules cr where cr.id = "
						+ id;

				st = con.createStatement();
				rs = st.executeQuery(sourcesql);

				while (rs.next()) {

					if (rs.getString(5).equals("SELECT COLUMN")) {
						fsql = "select count(1), 0 from " + rs.getString(3) + "." + rs.getString(4) + " ";
					} else {
						fsql = "select count(1), sum(" + rs.getString(5) + ") from " + rs.getString(3) + "."
								+ rs.getString(4) + "";
					}

					if (fcon != null) {
						st1 = fcon.createStatement();
						rs1 = st1.executeQuery(fsql);
						while (rs1.next()) {
							// Insert the result to crresults
							sql = "insert into crresults (batchid, moduleid, ruleid, message, sourcecount, sourcecolvalue, serverid ) values( ?, ?, ?, ?, ?, ?, ?)";
							ps = con.prepareStatement(sql);
							ps.setInt(1, batchid);
							ps.setString(2, rs.getString(2));
							ps.setString(3, rs.getString(1));
							ps.setString(4, "Run Successful");
							ps.setString(5, rs1.getString(1));
							ps.setString(6, rs1.getString(2));
							ps.setInt(7, serveriddefault);

							ps.executeUpdate();
							ps.close();

						}
						rs1.close();
						st1.close();
					} else {
						result = "conerror";
					}
				}
				rs.close();
				st.close();

				int crids = 0;
				String getcrid = "select id from crresults order by id desc limit 1";
				st = con.createStatement();
				rs = st.executeQuery(getcrid);
				if (rs.next()) {
					crids = rs.getInt(1);
				}
				rs.close();
				st.close();

				// Staging processing
				String stagingsql = "select sscript, stdb, sttable, stcolumn, scolscript from controlreportrules where id = "
						+ id;

				st = con.createStatement();
				rs = st.executeQuery(stagingsql);

				while (rs.next()) {

					if (rs.getString(4).equals("SELECT COLUMN")) {
						fsql = "select count(1), 0 from " + rs.getString(2) + "." + rs.getString(3) + " ";
					} else {
						fsql = "select count(1), sum(" + rs.getString(4) + ") from " + rs.getString(2) + "."
								+ rs.getString(3) + "";
					}

					if (fcon != null) {
						st1 = fcon.createStatement();
						rs1 = st1.executeQuery(fsql);
						while (rs1.next()) {
							String vsql = rs.getString(1);
							st2 = fcon.createStatement();
							rs2 = st2.executeQuery(vsql);

							if (rs2.next()) {

								sql = "update crresults set stost = ?, stostdiff = ?, stagingcount = ?, stagingcolvalue = ? where id = "
										+ crids;
								ps = con.prepareStatement(sql);
								if (rs2.getInt(1) == rs1.getInt(1)) {
									ps.setString(1, "Pass");
								} else {
									ps.setString(1, "Fail");
									finalresult = false;
								}

								ps.setInt(2, rs2.getInt(1) - rs1.getInt(1));
								ps.setInt(3, rs1.getInt(1));
								ps.setDouble(4, rs1.getDouble(2));

								ps.executeUpdate();

								ps.close();
							}
							rs2.close();
							st2.close();

							String vcolsql = rs.getString(5);
							st2 = fcon.createStatement();
							rs2 = st2.executeQuery(vcolsql);

							if (rs2.next()) {

								sql = "update crresults set stost = ?, stostcoldiff = ?  where id = " + crids;
								ps = con.prepareStatement(sql);
								if (rs2.getDouble(1) == rs1.getDouble(2)) {
									ps.setString(1, "Pass");
								} else {
									ps.setString(1, "Fail");
									finalresult = false;
								}

								ps.setDouble(2, rs2.getDouble(1) - rs1.getDouble(2));

								ps.executeUpdate();

								ps.close();
							}
							rs2.close();
							st2.close();

						}
						rs1.close();
						st1.close();
					} else {
						result = "conerror";
					}

				}
				rs.close();
				st.close();

				// Transformation processing
				String transsql = "select stscript, trdb, trtable, trcolumn, stcolscript from controlreportrules  where id = "
						+ id;

				st = con.createStatement();
				rs = st.executeQuery(transsql);

				while (rs.next()) {

					if (rs.getString(4).equals("SELECT COLUMN")) {
						fsql = "select count(1), 0 from " + rs.getString(2) + "." + rs.getString(3) + " ";
					} else {
						fsql = "select count(1), sum(" + rs.getString(4) + ") from " + rs.getString(2) + "."
								+ rs.getString(3) + "";
					}

					if (fcon != null) {
						st1 = fcon.createStatement();
						rs1 = st1.executeQuery(fsql);
						while (rs1.next()) {
							String vsql = rs.getString(1);
							st2 = fcon.createStatement();
							rs2 = st2.executeQuery(vsql);

							if (rs2.next()) {

								sql = "update crresults set sttotr = ?, sttotrdiff = ?, transcount = ?, transcolvalue = ? where id = "
										+ crids;
								ps = con.prepareStatement(sql);
								if (rs2.getInt(1) == rs1.getInt(1)) {
									ps.setString(1, "Pass");
								} else {
									ps.setString(1, "Fail");
									finalresult = false;
								}

								ps.setInt(2, rs2.getInt(1) - rs1.getInt(1));
								ps.setInt(3, rs1.getInt(1));
								ps.setDouble(4, rs1.getDouble(2));

								ps.executeUpdate();

								ps.close();
							}
							rs2.close();
							st2.close();

							String vcolsql = rs.getString(5);
							st2 = fcon.createStatement();
							rs2 = st2.executeQuery(vcolsql);

							if (rs2.next()) {
								sql = "update crresults set sttotr = ?, sttotrcoldiff = ?  where id = " + crids;
								ps = con.prepareStatement(sql);
								if (rs2.getDouble(1) == rs1.getDouble(2)) {
									ps.setString(1, "Pass");
								} else {
									ps.setString(1, "Fail");
									finalresult = false;
								}

								ps.setDouble(2, rs2.getDouble(1) - rs1.getDouble(2));

								ps.executeUpdate();

								ps.close();
							}
							rs2.close();
							st2.close();

						}
						rs1.close();
						st1.close();
					} else {
						result = "conerror";
					}

				}
				rs.close();
				st.close();

				// Loading processing
				String loadsql = "select trscript, ldb, ltable, lcolumn, trcolscript from controlreportrules  where id = "
						+ id;

				st = con.createStatement();
				rs = st.executeQuery(loadsql);

				while (rs.next()) {

					if (rs.getString(4).equals("SELECT COLUMN")) {
						fsql = "select count(1), 0 from " + rs.getString(2) + "." + rs.getString(3) + " ";
					} else {
						fsql = "select count(1), sum(" + rs.getString(4) + ") from " + rs.getString(2) + "."
								+ rs.getString(3) + "";
					}

					if (fcon != null) {
						st1 = fcon.createStatement();
						rs1 = st1.executeQuery(fsql);
						while (rs1.next()) {
							String vsql = rs.getString(1);
							st2 = fcon.createStatement();
							rs2 = st2.executeQuery(vsql);

							if (rs2.next()) {

								sql = "update crresults set trtol = ?, trtoldiff = ?, loadingcount = ?, loadingcolvalue = ? where id = "
										+ crids;
								ps = con.prepareStatement(sql);
								if (rs2.getInt(1) == rs1.getInt(1)) {
									ps.setString(1, "Pass");
								} else {
									ps.setString(1, "Fail");
									finalresult = false;
								}

								ps.setInt(2, rs2.getInt(1) - rs1.getInt(1));
								ps.setInt(3, rs1.getInt(1));
								ps.setDouble(4, rs1.getDouble(2));

								ps.executeUpdate();

								ps.close();
							}
							rs2.close();
							st2.close();

							String vcolsql = rs.getString(5);
							st2 = fcon.createStatement();
							rs2 = st2.executeQuery(vcolsql);

							if (rs2.next()) {
								sql = "update crresults set trtol = ?, trtolcoldiff = ?  where id = " + crids;
								ps = con.prepareStatement(sql);
								if (rs2.getDouble(1) == rs1.getDouble(2)) {
									ps.setString(1, "Pass");
								} else {
									ps.setString(1, "Fail");
									finalresult = false;
								}

								ps.setDouble(2, rs2.getDouble(1) - rs1.getDouble(2));

								ps.executeUpdate();

								ps.close();
							}
							rs2.close();
							st2.close();

						}
						rs1.close();
						st1.close();
					} else {
						result = "conerror";
					}

				}
				rs.close();
				st.close();

				// Target processing
				String targetsql = "select tscript, tdb, ttable, tcolumn, tcolscript from controlreportrules  where id = "
						+ id;

				st = con.createStatement();
				rs = st.executeQuery(targetsql);

				while (rs.next()) {

					if (rs.getString(4).equals("SELECT COLUMN")) {
						fsql = "select count(1), 0 from " + rs.getString(2) + "." + rs.getString(3) + " ";
					} else {
						fsql = "select count(1), sum(" + rs.getString(4) + ") from " + rs.getString(2) + "."
								+ rs.getString(3) + "";
					}

					if (fcon != null) {
						st1 = fcon.createStatement();
						rs1 = st1.executeQuery(fsql);
						while (rs1.next()) {
							String vsql = rs.getString(1);
							st2 = fcon.createStatement();
							rs2 = st2.executeQuery(vsql);

							if (rs2.next()) {

								sql = "update crresults set ltot = ?, ltotdiff = ?, targetcount = ?, targetcolvalue = ?, result = ?, executeduserid = ? where id = "
										+ crids;
								ps = con.prepareStatement(sql);
								if (rs2.getInt(1) == rs1.getInt(1)) {
									ps.setString(1, "Pass");
								} else {
									ps.setString(1, "Fail");
									finalresult = false;
								}

								ps.setInt(2, rs2.getInt(1) - rs1.getInt(1));
								ps.setInt(3, rs1.getInt(1));
								ps.setDouble(4, rs1.getDouble(2));
								if (finalresult) {
									ps.setString(5, "Passed");
								} else {
									ps.setString(6, "Failed");
								}
								ps.setLong(6, userid);

								ps.executeUpdate();

								ps.close();
							}
							rs2.close();
							st2.close();

							String vcolsql = rs.getString(5);
							st2 = fcon.createStatement();
							rs2 = st2.executeQuery(vcolsql);

							if (rs2.next()) {
								sql = "update crresults set ltot = ?, ltotcoldiff = ?  where id = " + crids;
								ps = con.prepareStatement(sql);
								if (rs2.getDouble(1) == rs1.getDouble(2)) {
									ps.setString(1, "Pass");
								} else {
									ps.setString(1, "Fail");
									finalresult = false;
								}

								ps.setDouble(2, rs2.getDouble(1) - rs1.getDouble(2));

								ps.executeUpdate();

								ps.close();
							}
							rs2.close();
							st2.close();
						}
						rs1.close();
						st1.close();
					} else {
						result = "conerror";
					}

				}
				rs.close();
				st.close();

			} else {
				result = "noserver";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<String> getcrresults(int batchid, int moduleid, String columns, int countsize, int serverid) {

		ArrayList<String> reportsbean = new ArrayList<String>();
		try {
			if (batchid != 0 && moduleid == 0) {
				sql = "select " + columns + " from crresults where batchid = " + batchid + " and serverid = " + serverid
						+ " order by moduleid, ruleid";
			} else if (batchid == 0 && moduleid != 0) {
				sql = "select " + columns + " from crresults where moduleid = " + moduleid + " and serverid = "
						+ serverid + " order by batchid desc, ruleid";
			} else if (batchid != 0 && moduleid != 0) {
				sql = "select " + columns + " from crresults where batchid = " + batchid + " and moduleid = " + moduleid
						+ " and serverid = " + serverid + " order by moduleid, ruleid";
			}

			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				for (int i = 1; i <= countsize; i++) {
					reportsbean.add(rs.getString(i));
				}
			}
			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			reportsbean = null;
		}
		return reportsbean;
	}

	public ArrayList<String> getbatchids(String tablename, int serverid, int projectid) {
		ArrayList<String> returnvalue = new ArrayList<>();
		String checktable = isTableAlreadyExisted(tablename);
		try {
			if (checktable.equals("existed")) {
				if (tablename.equals("fieldresults")) {
					sql = "select distinct(batchid) from " + tablename
							+ " fr inner join testcases tc on tc.id = fr.testcaseid where serverid = " + serverid
							+ " and tc.projectid = '" + projectid + "' order by batchid desc";
				} else if (tablename.equals("crresults")) {
					sql = "select distinct(batchid) from " + tablename
							+ " cr inner join controlreportrules crr on crr.id = cr.ruleid where serverid = " + serverid
							+ " and crr.projectid = '" + projectid + "' order by batchid desc";
				}

				st = con.createStatement();
				rs = st.executeQuery(sql);
				while (rs.next()) {
					returnvalue.add(rs.getString(1));
				}
				rs.close();
				st.close();
			} else {
				returnvalue = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnvalue = null;
		}
		return returnvalue;
	}

	public ArrayList<String> gettestormodule(String tablename, String batchid, int projectid) {
		ArrayList<String> returnvalue = new ArrayList<>();
		String checktable = isTableAlreadyExisted(tablename);
		try {
			if (checktable.equals("existed")) {
				if (tablename.equals("fieldresults")) {
					sql = "select concat(fr.testcaseid,'-',tc.tcname) from " + tablename
							+ " fr inner join testcases tc on tc.id = fr.testcaseid where fr.batchid = " + batchid
							+ " and tc.projectid = " + projectid;
				} else if (tablename.equals("crresults")) {
					sql = "select concat(cr.moduleid,'-',m.module) from " + tablename
							+ " cr left join modules m on cr.moduleid = m.id where cr.batchid = " + batchid
							+ " and  m.projectid = " + projectid;
				}
				st = con.createStatement();
				rs = st.executeQuery(sql);
				while (rs.next()) {
					returnvalue.add(rs.getString(1));
				}
				rs.close();
				st.close();
			} else {
				returnvalue = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnvalue = null;
		}
		return returnvalue;
	}

	public ArrayList<String> gettestsuites(int projectid) {
		ArrayList<String> returnvalue = new ArrayList<>();
		String checktable = isTableAlreadyExisted("testsuites");
		try {
			if (checktable.equals("existed")) {
				sql = "select suitename from testsuites where projectid = '" + projectid + "'";
				st = con.createStatement();
				rs = st.executeQuery(sql);
				while (rs.next()) {
					returnvalue.add(rs.getString(1));
				}
				rs.close();
				st.close();
			} else {
				returnvalue = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnvalue = null;
		}
		return returnvalue;
	}

	public ArrayList<String> getbatchids1(String testsuitename, int serverid, int projectid) {
		ArrayList<String> returnvalue = new ArrayList<>();
		String checktable = isTableAlreadyExisted("fieldresults");
		try {
			if (checktable.equals("existed")) {
				sql = "select distinct(batchid) from fieldresults where testsuiteid = (select id from testsuites where suitename = '"
						+ testsuitename + "' and projectid = '" + projectid + "') and serverid = " + serverid
						+ " order by batchid desc";
				st = con.createStatement();
				rs = st.executeQuery(sql);
				while (rs.next()) {
					returnvalue.add(rs.getString(1));
				}
				rs.close();
				st.close();
			} else {
				returnvalue = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnvalue = null;
		}
		return returnvalue;
	}

	public ArrayList<String> getReleasefieldresults(String columns, int countsize, String release, String cycle,
			String testsuite, int projectid) {

		ArrayList<String> reportsbean = new ArrayList<String>();
		try {
			if (release != null && cycle == null && testsuite == null) {
				sql = "select " + columns
						+ " from fieldresults fr join testsuites ts on fr.testsuiteid = ts.id join testcases tc on tc.id = fr.testcaseid where fr.release = '"
						+ release + "' and tc.projectid = '" + projectid + "' order by batchid desc limit 1";
			} else if (release != null && cycle != null && testsuite == null) {
				sql = "select " + columns
						+ " from fieldresults fr join testsuites ts on fr.testsuiteid = ts.id join testcases tc on tc.id = fr.testcaseid where fr.release = '"
						+ release + "' and fr.cycle = '" + cycle + "' and tc.projectid = '" + projectid
						+ "' order by batchid desc limit 1";
			} else if (release != null && cycle != null && testsuite != null) {
				sql = "select " + columns
						+ " from fieldresults fr join testsuites ts on fr.testsuiteid = ts.id join testcases tc on tc.id = fr.testcaseid where fr.release = '"
						+ release + "' and fr.cycle = '" + cycle + "' and ts.suitename = '" + testsuite
						+ "' and tc.projectid = '" + projectid + "' order by batchid desc limit 1";
			}

			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				for (int i = 1; i <= countsize; i++) {
					reportsbean.add(rs.getString(i));
				}
			}
			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			reportsbean = null;
		}
		return reportsbean;
	}

	public ArrayList<UsersDetailsBeanBinaryTrade> getuserlist() {
		ArrayList<UsersDetailsBeanBinaryTrade> userlist = new ArrayList<UsersDetailsBeanBinaryTrade>();
		try {
			sql = "select concat(id,'-',firstName,'-',lastName) from users";
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				UsersDetailsBeanBinaryTrade udbt = new UsersDetailsBeanBinaryTrade();
				udbt.setCombinedname(rs.getString(1));
				userlist.add(udbt);
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			userlist = null;
			e.printStackTrace();
		}

		return userlist;
	}

	public ArrayList<UsersDetailsBeanBinaryTrade> getselecteduserdetails(String id) {
		ArrayList<UsersDetailsBeanBinaryTrade> userlist = new ArrayList<UsersDetailsBeanBinaryTrade>();
		try {
			sql = "select id, firstName, lastName, userId, emailId, securityQuestion, answer, userActive, email, newcr, newff, newts, crexe, "
					+ "tsexe, adduser, addqa, dashboard, reports, testresults, newbug, viewbug, addbugserver from users where id = '"
					+ id + "'";
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				UsersDetailsBeanBinaryTrade udbt = new UsersDetailsBeanBinaryTrade();
				udbt.setId(rs.getLong(1));
				udbt.setFirstName(rs.getString(2));
				udbt.setLastName(rs.getString(3));
				udbt.setUserId(rs.getString(4));
				udbt.setEmailId(rs.getString(5));
				udbt.setSecurityQuestion(rs.getString(6));
				udbt.setAnswer(rs.getString(7));
				udbt.setIsactive(rs.getString(8));
				udbt.setEmail(rs.getString(9));
				udbt.setNewcr(rs.getString(10));
				udbt.setNewff(rs.getString(11));
				udbt.setNewts(rs.getString(12));
				udbt.setCrexe(rs.getString(13));
				udbt.setTsexe(rs.getString(14));
				udbt.setAdduser(rs.getString(15));
				udbt.setAddqa(rs.getString(16));
				udbt.setDashboard(rs.getString(17));
				udbt.setReports(rs.getString(18));
				udbt.setTestresults(rs.getString(19));
				udbt.setNewbug(rs.getString(20));
				udbt.setViewbug(rs.getString(21));
				udbt.setAddbugserver(rs.getString(22));

				userlist.add(udbt);
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			userlist = null;
			e.printStackTrace();
		}
		return userlist;
	}

	public ArrayList<TestSuite> gettestsuitespercentage(String tablename, int batchid) {
		ArrayList<TestSuite> suites = new ArrayList<TestSuite>();
		String checktable = isTableAlreadyExisted(tablename);
		if (checktable.equals("existed")) {
			try {
				sql = "select TRUNCATE((((select count(1) from fieldresults where batchid = " + batchid
						+ " and teststatus = 'fail') / (select count(1) from fieldresults where batchid = " + batchid
						+ ") ) * 100),2)as failper, TRUNCATE((((select count(1) from fieldresults where batchid = "
						+ batchid + " and teststatus = 'pass')/ (select count(1) from fieldresults where batchid = "
						+ batchid + ") ) *100),2) as passper, ts.suitename "
						+ "from fieldresults fr join testsuites ts on fr.testsuiteid = ts.id " + "where batchid = "
						+ batchid + " group by fr.`release`, fr.cycle, fr.testsuiteid";

				st = con.createStatement();
				rs = st.executeQuery(sql);
				while (rs.next()) {
					// here i am getting pass and fail percentage
					TestSuite testSuite = new TestSuite();
					testSuite.setTsname(rs.getString(3));
					testSuite.setPass(rs.getInt(2)); // pass
					testSuite.setFail(rs.getInt(1)); // fail

					suites.add(testSuite);
				}
				rs.close();
				st.close();

			} catch (Exception e) {
			}
		}
		return suites;
	}

	public ArrayList<ControlReportExecutionBinaryTrade> getruleidonly(String name, int type) {
		String checktable2 = isTableAlreadyExisted("controlreportrules");
		ArrayList<ControlReportExecutionBinaryTrade> crbt = new ArrayList<ControlReportExecutionBinaryTrade>();
		try {
			if (checktable2.equals("existed")) {
				if (type == 1) {// GetRuleIDusingModule
					sql = "select id from controlreportrules where module = '" + name + "'";
				} else if (type == 2) {// GetRuleIDusingRulename
					sql = "select id from controlreportrules where rulename = '" + name + "'";
				}

				st = con.createStatement();
				rs = st.executeQuery(sql);

				while (rs.next()) {
					ControlReportExecutionBinaryTrade cr = new ControlReportExecutionBinaryTrade();
					cr.setId(rs.getString(1));

					crbt.add(cr);
				}
				rs.close();
				st.close();

			} else {
				crbt = null;
			}

		} catch (Exception e) {
			crbt = null;
			e.printStackTrace();
		}
		return crbt;
	}

	public ArrayList<Rule> getRulesPercentage(String tablename, int batchid) {
		ArrayList<Rule> rules = new ArrayList<Rule>();
		String checktable = isTableAlreadyExisted(tablename);
		if (checktable.equals("existed")) {
			try {
				sql = "select TRUNCATE((((select count(1) from crresults where batchid = '" + batchid
						+ "' and result = 'Passed') / (select count(1) from crresults where batchid = " + batchid
						+ ") ) * 100),2)as passper, " + "TRUNCATE((((select count(1) from crresults where batchid = '"
						+ batchid + "' and result = 'Failed')/ (select count(1) from crresults where batchid = "
						+ batchid + ") ) *100),2) as failper, crr.rulename "
						+ "from crresults cr join controlreportrules crr on cr.ruleid = crr.id where cr.batchid = '"
						+ batchid + "' group by cr.moduleid, cr.ruleid";

				st = con.createStatement();
				rs = st.executeQuery(sql);
				while (rs.next()) {
					Rule rule = new Rule();
					rule.setPass(rs.getInt(1));
					rule.setFail(rs.getInt(2));
					rule.setRulename(rs.getString(3));

					rules.add(rule);
				}
				rs.close();
				st.close();

			} catch (Exception e) {
			}
		}
		return rules;
	}

	public void createBugstable() {
		try {
			String isInvoiceAlreadyExisted = this.isTableAlreadyExisted("bugs");

			if (isInvoiceAlreadyExisted.equals("notExisted")) {

				String createTabelSQL = "create table bugs (id bigint(40) NOT NULL AUTO_INCREMENT, tfsid bigint(5) default 0, jiraid bigint(5) default 0, tcid bigint(20) default 0, ruleid bigint(20) default 0,"
						+ " title varchar(256) default null, state varchar(256) default null, reason varchar(256) default null, area varchar(256) default null, reprosteps longtext, "
						+ " deleted int(1) default 0, createdby int(10) default 0, updatedby int(10) default 0, createddate datetime, updateddate datetime, projectid bigint(10) default 0, PRIMARY KEY (id))";
				st = con.createStatement();
				st.executeUpdate(createTabelSQL);
				updateTrigerstatus("bugstable", 1);
				st.close();
			}

		} catch (SQLException e) {
			updateTrigerstatus("bugstable", 0);
			e.printStackTrace();
		}
	}

	public void createBugServerTable() {
		try {
			String isInvoiceAlreadyExisted = this.isTableAlreadyExisted("bugserver");

			if (isInvoiceAlreadyExisted.equals("notExisted")) {

				String createTabelSQL = "create table bugserver (id bigint(10) NOT NULL AUTO_INCREMENT, tfs bigint(5) default 0, jira bigint(5) default 0, isdefault int(1) default 0, "
						+ " collectionurl varchar(2000) default null, isactive int(1) default 0, createdby int(10) default 0, createddate datetime, PRIMARY KEY (id))";
				st = con.createStatement();
				st.executeUpdate(createTabelSQL);
				updateTrigerstatus("bugserver", 1);
				st.close();
			}

		} catch (SQLException e) {
			updateTrigerstatus("bugserver", 0);
			e.printStackTrace();
		}
	}

	public void createBugServerUsersTable() {
		try {
			String isInvoiceAlreadyExisted = this.isTableAlreadyExisted("bugserverusers");

			if (isInvoiceAlreadyExisted.equals("notExisted")) {

				String createTabelSQL = "create table bugserverusers (id bigint(10) NOT NULL AUTO_INCREMENT, tfs bigint(5) default 0, jira bigint(5) default 0, username varchar(256) default null, "
						+ " password varchar(256) default null, userid int(10) default 0, PRIMARY KEY (id))";
				st = con.createStatement();
				st.executeUpdate(createTabelSQL);
				updateTrigerstatus("bugusers", 1);
				st.close();
			}

		} catch (SQLException e) {
			updateTrigerstatus("bugusers", 0);
			e.printStackTrace();
		}
	}

	public void createBugServerProjectsTable() {
		try {
			String isInvoiceAlreadyExisted = this.isTableAlreadyExisted("bugserverprojects");

			if (isInvoiceAlreadyExisted.equals("notExisted")) {

				String createTabelSQL = "create table bugserverprojects (id bigint(10) NOT NULL AUTO_INCREMENT, tfs bigint(5) default 0, jira bigint(5) default 0, "
						+ "projectname varchar(1000) default null,  projectid bigint(10) default 0, PRIMARY KEY (id))";
				st = con.createStatement();
				st.executeUpdate(createTabelSQL);
				updateTrigerstatus("bugprojects", 1);
				st.close();
			}

		} catch (SQLException e) {
			updateTrigerstatus("bugprojects", 0);
			e.printStackTrace();
		}
	}

	public String createprojectstable(String tableName, String name, String owner, long createdby) {

		String returnValue = "failure";

		try {
			String checktable = this.isTableAlreadyExisted(tableName);

			if (checktable.equals("notExisted")) {

				String createTabelSQL = "CREATE TABLE " + tableName
						+ " (projectid bigint(10) NOT NULL auto_increment,  name varchar(256) default null, owner varchar(200) default null, testcases bigint(255) default 0, "
						+ " executedcount bigint(255) default 0, nonexecutedcount bigint(255) default 0, ffpass bigint(255) default 0, fffail bigint(255) default 0, dataquality double default 0, "
						+ " crrules bigint(255) default 0, rulesexecuted bigint(255) default 0, rulesnonexecuted bigint(255) default 0, crpass bigint(255) default 0, crfail bigint(255) default 0, "
						+ " controlreportquaility double, createddate datetime, createdby bigint(20) default 0, updateddate datetime, updatedby bigint(20) default 0, isactive TINYINT(1) default 1, "
						+ " PRIMARY KEY (projectid), UNIQUE KEY(name))";

				st = con.createStatement();
				int isTableCreated = st.executeUpdate(createTabelSQL);

				if (isTableCreated == 0)
					returnValue = insertprojects(tableName, name, owner, createdby);
				else
					returnValue = "failure";
			} else if (checktable.equals("existed")) {
				returnValue = insertprojects(tableName, name, owner, createdby);
			} else {
				returnValue = "failure";
			}
			st.close();
		} catch (Exception e) {
			returnValue = "error";
			e.printStackTrace();
		}
		return returnValue;
	}

	public String insertprojects(String tableName, String name, String owner, long createdby) {
		String result = "failure";
		try {

			sql = "insert into " + tableName
					+ " (name, owner, createdby, createddate) values( ?, ?, ?, CURRENT_TIMESTAMP )";
			ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, owner);
			ps.setLong(3, createdby);

			int status1 = ps.executeUpdate();
			if (status1 == 1) {
				result = "success";
			} else {
				result = "duplicate";
			}
			ps.close();

		} catch (Exception e) {
			result = "failure";
			e.printStackTrace();
		}
		return result;
	}

	public String updateprojects(String tableName, int projectid, String name, String owner, long updatedby,
			int isactive) {
		String result = "failure";
		try {

			sql = "update " + tableName
					+ " set name = ?, owner = ?, updatedby = ?, updateddate = CURRENT_TIMESTAMP, isactive = ? where projectid = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, owner);
			ps.setLong(3, updatedby);
			ps.setInt(4, isactive);
			ps.setInt(5, projectid);

			int status1 = ps.executeUpdate();
			if (status1 == 1) {
				result = "success";
			}
			ps.close();

		} catch (Exception e) {
			result = "failure";
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<ProjectsBeanBinaryTrade> getprojectnames() {
		ArrayList<ProjectsBeanBinaryTrade> plist = new ArrayList<ProjectsBeanBinaryTrade>();
		try {

			String checktable = isTableAlreadyExisted("projects");
			if (checktable.equals("existed")) {
				sql = "select concat(projectid,'-',name) from projects";
				st = con.createStatement();
				rs = st.executeQuery(sql);
				while (rs.next()) {
					ProjectsBeanBinaryTrade pbt = new ProjectsBeanBinaryTrade();
					pbt.setName(rs.getString(1));
					plist.add(pbt);
				}
				rs.close();
				st.close();
			} else {
				plist = null;
			}
		} catch (Exception e) {
			plist = null;
		}
		return plist;
	}

	public ArrayList<ProjectsBeanBinaryTrade> getprojectDetails(int status, int projectid) {
		ArrayList<ProjectsBeanBinaryTrade> plist = new ArrayList<ProjectsBeanBinaryTrade>();
		try {

			String checktable = isTableAlreadyExisted("projects");
			if (checktable.equals("existed")) {
				if (status == 1) {
					sql = "select name, owner from projects where projectid = '" + projectid + "'";
				}

				st = con.createStatement();
				rs = st.executeQuery(sql);
				while (rs.next()) {
					ProjectsBeanBinaryTrade pbt = new ProjectsBeanBinaryTrade();
					pbt.setName(rs.getString(1));
					pbt.setOwner(rs.getString(2));
					plist.add(pbt);
				}
				rs.close();
				st.close();
			} else {
				plist = null;
			}
		} catch (Exception e) {
			plist = null;
		}
		return plist;
	}

	public String getModuleorTS(String selectclause, String whereclause, String whereclauseanswer) {
		String result = null;
		try {
			sql = "select " + selectclause + " from testcases ts where " + whereclause + " = '" + whereclauseanswer
					+ "' limit 1";

			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				result = rs.getString(1);
			}
			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
}