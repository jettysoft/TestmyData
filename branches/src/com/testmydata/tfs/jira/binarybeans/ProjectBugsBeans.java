package com.testmydata.tfs.jira.binarybeans;

import com.testmydata.binarybeans.BinaryTrade;

public class ProjectBugsBeans extends BinaryTrade {
	byte[] bugid, bugname;

	public String getBugid() {
		try {
			return new String(bugid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setBugid(String Bugid) {
		if (Bugid == null || Bugid.length() == 0) {
			return;
		}
		this.bugid = new byte[Bugid.length()];
		string2Bytes(Bugid, this.bugid);
	}

	public String getBugname() {
		try {
			return new String(bugname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setBugname(String Bugname) {
		if (Bugname == null || Bugname.length() == 0) {
			return;
		}
		this.bugname = new byte[Bugname.length()];
		string2Bytes(Bugname, this.bugname);
	}
}
