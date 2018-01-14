package com.testmydata.tfs.jira.binarybeans;

import com.testmydata.binarybeans.BinaryTrade;

public class ProjectListBean extends BinaryTrade {
	private byte[] projectid, projectname;
	ProjectBugsBeans[] projectbugsbeans;
	ProjectGroupsBean[] projectgroupsbeans;
	ProjectIterationBean[] projectiterationbean;

	public String getProjectid() {
		try {
			return new String(projectid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setProjectid(String Projectid) {
		if (Projectid == null || Projectid.length() == 0) {
			return;
		}
		this.projectid = new byte[Projectid.length()];
		string2Bytes(Projectid, this.projectid);
	}

	public String getProjectname() {
		try {
			return new String(projectname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setProjectname(String Projectname) {
		if (Projectname == null || Projectname.length() == 0) {
			return;
		}
		this.projectname = new byte[Projectname.length()];
		string2Bytes(Projectname, this.projectname);
	}

}
