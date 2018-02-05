package com.testmydata.binarybeans;

public class BugServerProjectsBinaryTrade extends BinaryTrade {
	private byte[] projectid, projbugserverid, projectname, localproject;
	BugServerBinaryTrade bugserver;

	public BugServerBinaryTrade getBugeserverP() {
		return bugserver;
	}

	public void setBugserverP(BugServerBinaryTrade bugser) {
		this.bugserver = bugser;
	}

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

	public String getProjbugserverid() {
		try {
			return new String(projbugserverid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setProjbugserverid(String Projbugserverid) {
		if (Projbugserverid == null || Projbugserverid.length() == 0) {
			return;
		}
		this.projbugserverid = new byte[Projbugserverid.length()];
		string2Bytes(Projbugserverid, this.projbugserverid);
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

	public String getLocalproject() {
		try {
			return new String(localproject);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLocalproject(String Localproject) {
		if (Localproject == null || Localproject.length() == 0) {
			return;
		}
		this.localproject = new byte[Localproject.length()];
		string2Bytes(Localproject, this.localproject);
	}
}
