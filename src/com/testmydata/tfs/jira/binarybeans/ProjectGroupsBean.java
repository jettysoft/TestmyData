package com.testmydata.tfs.jira.binarybeans;

import com.testmydata.binarybeans.BinaryTrade;

public class ProjectGroupsBean extends BinaryTrade {
	byte[] groupid, groupname;
	ProjectUsersBean[] projectusersbean;

	public String getGroupid() {
		try {
			return new String(groupid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setGroupid(String Groupid) {
		if (Groupid == null || Groupid.length() == 0) {
			return;
		}
		this.groupid = new byte[Groupid.length()];
		string2Bytes(Groupid, this.groupid);
	}

	public String getGroupname() {
		try {
			return new String(groupname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setGroupname(String Groupname) {
		if (Groupname == null || Groupname.length() == 0) {
			return;
		}
		this.groupname = new byte[Groupname.length()];
		string2Bytes(Groupname, this.groupname);
	}

}
