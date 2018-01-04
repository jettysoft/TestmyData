package com.testmydata.binarybeans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DBConfigBinaryTade extends BinaryTrade {

	private byte[] dbName;
	private byte[] dbUserName;
	private byte[] dbPassword;
	private byte[] dbClassName;
	private byte[] dbURL;

	public String getDbName() {
		try {
			return new String(dbName);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	@XmlElement
	public void setDbName(String DbName) {
		if (DbName == null || DbName.length() == 0) {
			return;
		}
		this.dbName = new byte[DbName.length()];
		string2Bytes(DbName, this.dbName);
	}

	public String getDbUserName() {
		try {
			return new String(dbUserName);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	@XmlElement
	public void setDbUserName(String DbUserName) {
		if (DbUserName == null || DbUserName.length() == 0) {
			return;
		}
		this.dbUserName = new byte[DbUserName.length()];
		string2Bytes(DbUserName, this.dbUserName);
	}

	public String getDbPassword() {
		try {
			return new String(dbPassword);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	@XmlElement
	public void setDbPassword(String DbPassword) {
		if (DbPassword == null || DbPassword.length() == 0) {
			return;
		}
		this.dbPassword = new byte[DbPassword.length()];
		string2Bytes(DbPassword, this.dbPassword);
	}

	public String getDbClassName() {
		try {
			return new String(dbClassName);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	@XmlElement
	public void setDbClassName(String DbClassName) {
		if (DbClassName == null || DbClassName.length() == 0) {
			return;
		}
		this.dbClassName = new byte[DbClassName.length()];
		string2Bytes(DbClassName, this.dbClassName);
	}

	public String getDbURL() {
		try {
			return new String(dbURL);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	@XmlElement
	public void setDbURL(String dbURL) {
		if (dbURL == null || dbURL.length() == 0) {
			return;
		}
		this.dbURL = new byte[dbURL.length()];
		string2Bytes(dbURL, this.dbURL);
	}

}
