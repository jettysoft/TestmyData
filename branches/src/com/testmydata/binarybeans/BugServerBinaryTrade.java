package com.testmydata.binarybeans;

public class BugServerBinaryTrade extends BinaryTrade {

	private byte[] id, tfs, jira, isdefault, collectionurl, isactive, createdby, createdate;
	BugServerProjectsBinaryTrade[] bugprojects;
	BugServerUsersBinaryTrade[] bugusers;

	public String getCreatedate() {
		try {
			return new String(createdate);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCreatedate(String Createdate) {
		if (Createdate == null || Createdate.length() == 0) {
			return;
		}
		this.createdate = new byte[Createdate.length()];
		string2Bytes(Createdate, this.createdate);
	}

	public BugServerProjectsBinaryTrade[] getBugProjects() {
		return bugprojects;
	}

	public void setBugProjects(BugServerProjectsBinaryTrade[] bugsproj) {
		this.bugprojects = bugsproj;
	}

	public BugServerUsersBinaryTrade[] getBugUsers() {
		return bugusers;
	}

	public void setBugUsers(BugServerUsersBinaryTrade[] bugusers) {
		this.bugusers = bugusers;
	}

}
