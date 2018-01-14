package com.testmydata.tfs.jira.binarybeans;

import com.testmydata.binarybeans.BinaryTrade;

public class ProjectUsersBean extends BinaryTrade {
	byte[] userid, username;

	public String getUserid() {
		try {
			return new String(userid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUserid(String Userid) {
		if (Userid == null || Userid.length() == 0) {
			return;
		}
		this.userid = new byte[Userid.length()];
		string2Bytes(Userid, this.userid);
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
}
