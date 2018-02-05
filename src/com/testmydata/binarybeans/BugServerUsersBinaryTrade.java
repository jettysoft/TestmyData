package com.testmydata.binarybeans;

public class BugServerUsersBinaryTrade extends BinaryTrade {
	private byte[] buguserid, usersbugserverid, username, password, localuser, localuserid;
	BugServerBinaryTrade bugserver;

	public BugServerBinaryTrade getBugeserverU() {
		return bugserver;
	}

	public void setBugserverU(BugServerBinaryTrade bugser) {
		this.bugserver = bugser;
	}

	public String getBuguserid() {
		try {
			return new String(buguserid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setBuguserid(String Buguserid) {
		if (Buguserid == null || Buguserid.length() == 0) {
			return;
		}
		this.buguserid = new byte[Buguserid.length()];
		string2Bytes(Buguserid, this.buguserid);
	}

	public String getUsersbugserverid() {
		try {
			return new String(usersbugserverid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUsersbugserverid(String Usersbugserverid) {
		if (Usersbugserverid == null || Usersbugserverid.length() == 0) {
			return;
		}
		this.usersbugserverid = new byte[Usersbugserverid.length()];
		string2Bytes(Usersbugserverid, this.usersbugserverid);
	}

	public String getUsername() {
		try {
			return new String(username);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUsername(String Username) {
		if (Username == null || Username.length() == 0) {
			return;
		}
		this.username = new byte[Username.length()];
		string2Bytes(Username, this.username);
	}

	public String getPassword() {
		try {
			return new String(password);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPassword(String Password) {
		if (Password == null || Password.length() == 0) {
			return;
		}
		this.password = new byte[Password.length()];
		string2Bytes(Password, this.password);
	}

	public String getLocaluser() {
		try {
			return new String(localuser);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLocaluser(String Localuser) {
		if (Localuser == null || Localuser.length() == 0) {
			return;
		}
		this.localuser = new byte[Localuser.length()];
		string2Bytes(Localuser, this.localuser);
	}

	public String getLocaluserid() {
		try {
			return new String(localuserid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLocaluserid(String Localuserid) {
		if (Localuserid == null || Localuserid.length() == 0) {
			return;
		}
		this.localuserid = new byte[Localuserid.length()];
		string2Bytes(Localuserid, this.localuserid);
	}
}
