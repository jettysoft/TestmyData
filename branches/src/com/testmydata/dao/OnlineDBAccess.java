// REMOTE DATABASE CONNECTION

package com.testmydata.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import com.testmydata.util.EncryptAndDecrypt;

public class OnlineDBAccess {

	public static Connection con;

	static String dbName = null;
	static String dbUserName = null;
	static String dbPassword = null;
	static String dbClassName = null;
	static String dbURL1 = null;

	@SuppressWarnings("unused")
	private static String dbURL = "";

	public Connection getfConnection(String url, String dbuser, String dbpassword, String classname) {
		try {
			if (con != null) {
				con.close();
			}
			con = null;
			if (con == null) {

				Class.forName(classname);
				con = DriverManager.getConnection(url, EncryptAndDecrypt.decryptData(dbuser),
						EncryptAndDecrypt.decryptData(dbpassword));
			}

		} catch (Exception ex) {
			con = null;
			ex.printStackTrace();
		}
		return con;
	}
	// Test for connection directly
	// public static void main(String[] args) {
	// dbName = null;
	// dbUserName = EncryptAndDecrypt.encryptData("postgres");
	// dbPassword = EncryptAndDecrypt.encryptData("Test@123");
	// dbClassName = "org.postgresql.Driver";
	// dbURL1 = "jdbc:postgresql://40.121.159.64:5432/";
	//
	// OnlineDBAccess ob = new OnlineDBAccess();
	// System.out.println("State :" + ob.getfConnection(dbURL1, dbUserName,
	// dbPassword, dbClassName));
	// }

}
