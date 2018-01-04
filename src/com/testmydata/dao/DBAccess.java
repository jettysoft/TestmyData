// REMOTE DATABASE CONNECTION

package com.testmydata.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import com.testmydata.binarybeans.DBConfigBinaryTade;
import com.testmydata.util.DBConfigJAXB;
import com.testmydata.util.EncryptAndDecrypt;

@SuppressWarnings("unused")
public class DBAccess {

	public static Connection con;
	private static String dbName = null;
	private final static String dbUserName = "";
	private final static String dbPassword = "";
	private final static String dbClassName = "";
	private static String dbURL = "";

	DBConfigJAXB db = null;
	DBConfigBinaryTade dbConfig = null;
	@SuppressWarnings("rawtypes")
	Vector connectionPool = new Vector();

	public DBAccess() {
		try {

			db = new DBConfigJAXB();
			dbConfig = db.readDBConfig();

			dbName = EncryptAndDecrypt.decryptData(dbConfig.getDbName());
			// System.out.println(dbName);
			if (dbName == null) {
				getConnection();
			} else {
				dbURL = EncryptAndDecrypt.decryptData(dbConfig.getDbURL())
						+ EncryptAndDecrypt.decryptData(dbConfig.getDbName()) + "?zeroDateTimeBehavior=convertToNull";
			}

		} catch (Exception e) {

		}
	}
	//
	// public Connection initializeConnection() {
	// try {
	// if (con == null) {
	// Class.forName(EncryptAndDecrypt.decryptData(dbConfig.getDbClassName()));
	// con =
	// DriverManager.getConnection(EncryptAndDecrypt.decryptData(dbConfig.getDbURL()),
	// EncryptAndDecrypt.decryptData(dbConfig.getDbUserName()),
	// EncryptAndDecrypt.decryptData(dbConfig.getDbPassword()));
	// }
	// } catch (Exception ex) {
	// }
	// return con;
	// }

	// public DBAccess(String dbName) {
	// dbURL = EncryptAndDecrypt.decryptData(dbConfig.getDbURL()) + dbName +
	// "?zeroDateTimeBehavior=convertToNull";
	// }

	public Connection getConnection() {
		try {
			if (con == null) {
				if (connectionPool.size() == 0) {
					initializeConnectionPool();
				}
				// Class.forName(EncryptAndDecrypt.decryptData(dbConfig.getDbClassName()));
				// con = DriverManager.getConnection(
				// EncryptAndDecrypt.decryptData(dbConfig.getDbURL())
				// + EncryptAndDecrypt.decryptData(dbConfig.getDbName()) +
				// "?useConfigs=maxPerformance&"
				// + "useServerPrepStmts=true&" + "cachePrepStmts=true&" +
				// "cacheCallableStmts=true&"
				// + "useLocalSessionState=true&" + "elideSetAutoCommits=true&"
				// + "alwaysSendSetIsolation=false&" +
				// "enableQueryTimeouts=false&"
				// + "cacheServerConfiguration=true&" +
				// "verifyServerCertificate=false&useSSL=true",
				// EncryptAndDecrypt.decryptData(dbConfig.getDbUserName()),
				// EncryptAndDecrypt.decryptData(dbConfig.getDbPassword()));

				// Check if there is a connection available. There are times
				// when
				// all the connections in the pool may be used up
				if (connectionPool.size() > 0) {
					con = (Connection) connectionPool.firstElement();
					connectionPool.removeElementAt(0);
				}
				// Giving away the connection from the connection pool
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return con;
	}

	@SuppressWarnings("unchecked")
	private void initializeConnectionPool() {
		while (!checkIfConnectionPoolIsFull()) {
			// Adding new connection instance until the pool is full
			connectionPool.addElement(createNewConnectionForPool());
		}
		// System.out.println("Connection Pool is full.");
	}

	private synchronized boolean checkIfConnectionPoolIsFull() {
		final int MAX_POOL_SIZE = 2;

		// Check if the pool size
		if (connectionPool.size() < 2) {
			return false;
		}

		return true;
	}

	// Creating a connection
	private Connection createNewConnectionForPool() {
		Connection connection = null;

		try {
			Class.forName(EncryptAndDecrypt.decryptData(dbConfig.getDbClassName()));
			connection = DriverManager.getConnection(
					EncryptAndDecrypt.decryptData(dbConfig.getDbURL())
							+ EncryptAndDecrypt.decryptData(dbConfig.getDbName()) + "?useConfigs=maxPerformance&"
							+ "useServerPrepStmts=true&" + "cachePrepStmts=true&" + "cacheCallableStmts=true&"
							+ "useLocalSessionState=true&" + "elideSetAutoCommits=true&"
							+ "alwaysSendSetIsolation=false&" + "enableQueryTimeouts=false&"
							+ "cacheServerConfiguration=true&" + "verifyServerCertificate=false&useSSL=true",
					EncryptAndDecrypt.decryptData(dbConfig.getDbUserName()),
					EncryptAndDecrypt.decryptData(dbConfig.getDbPassword()));

		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle);
			return null;
		} catch (ClassNotFoundException cnfe) {
			System.err.println("ClassNotFoundException: " + cnfe);
			return null;
		}

		return connection;
	}

	@Override
	public Object clone() {
		return con;
	}

	public static String getDatabaseName() {
		return dbName;
	}
}
