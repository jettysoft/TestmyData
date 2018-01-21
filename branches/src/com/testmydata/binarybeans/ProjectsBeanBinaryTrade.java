package com.testmydata.binarybeans;

public class ProjectsBeanBinaryTrade extends BinaryTrade {
	private byte[] projectid, name, owner, testcases, executedcount, nonexecutedcount, ffpass, fffail, dataquality,
			crrules, rulesexecuted, rulesnonexecuted, crpass, crfail, controlreportquaility, createddate, createdby,
			updateddate, updatedby, isactive;

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

	public String getName() {
		try {
			return new String(name);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setName(String Name) {
		if (Name == null || Name.length() == 0) {
			return;
		}
		this.name = new byte[Name.length()];
		string2Bytes(Name, this.name);
	}

	public String getOwner() {
		try {
			return new String(owner);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setOwner(String Owner) {
		if (Owner == null || Owner.length() == 0) {
			return;
		}
		this.owner = new byte[Owner.length()];
		string2Bytes(Owner, this.owner);
	}

	public String getTestcases() {
		try {
			return new String(testcases);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTestcases(String Testcases) {
		if (Testcases == null || Testcases.length() == 0) {
			return;
		}
		this.testcases = new byte[Testcases.length()];
		string2Bytes(Testcases, this.testcases);
	}

	public String getExecutedcount() {
		try {
			return new String(executedcount);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setExecutedcount(String Executedcount) {
		if (Executedcount == null || Executedcount.length() == 0) {
			return;
		}
		this.executedcount = new byte[Executedcount.length()];
		string2Bytes(Executedcount, this.executedcount);
	}

	public String getNonexecutedcount() {
		try {
			return new String(nonexecutedcount);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setNonexecutedcount(String Nonexecutedcount) {
		if (Nonexecutedcount == null || Nonexecutedcount.length() == 0) {
			return;
		}
		this.nonexecutedcount = new byte[Nonexecutedcount.length()];
		string2Bytes(Nonexecutedcount, this.nonexecutedcount);
	}

	public String getFfpass() {
		try {
			return new String(ffpass);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setFfpass(String Ffpass) {
		if (Ffpass == null || Ffpass.length() == 0) {
			return;
		}
		this.ffpass = new byte[Ffpass.length()];
		string2Bytes(Ffpass, this.ffpass);
	}

	public String getFffail() {
		try {
			return new String(fffail);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setFffail(String Fffail) {
		if (Fffail == null || Fffail.length() == 0) {
			return;
		}
		this.fffail = new byte[Fffail.length()];
		string2Bytes(Fffail, this.fffail);
	}

	public String getDataquality() {
		try {
			return new String(dataquality);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setDataquality(String Dataquality) {
		if (Dataquality == null || Dataquality.length() == 0) {
			return;
		}
		this.dataquality = new byte[Dataquality.length()];
		string2Bytes(Dataquality, this.dataquality);
	}

	public String getCrrules() {
		try {
			return new String(crrules);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCrrules(String Crrules) {
		if (Crrules == null || Crrules.length() == 0) {
			return;
		}
		this.crrules = new byte[Crrules.length()];
		string2Bytes(Crrules, this.crrules);
	}

	public String getRulesexecuted() {
		try {
			return new String(rulesexecuted);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setRulesexecuted(String Rulesexecuted) {
		if (Rulesexecuted == null || Rulesexecuted.length() == 0) {
			return;
		}
		this.rulesexecuted = new byte[Rulesexecuted.length()];
		string2Bytes(Rulesexecuted, this.rulesexecuted);
	}

	public String getRulesnonexecuted() {
		try {
			return new String(rulesnonexecuted);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setRulesnonexecuted(String Rulesnonexecuted) {
		if (Rulesnonexecuted == null || Rulesnonexecuted.length() == 0) {
			return;
		}
		this.rulesnonexecuted = new byte[Rulesnonexecuted.length()];
		string2Bytes(Rulesnonexecuted, this.rulesnonexecuted);
	}

	public String getCrpass() {
		try {
			return new String(crpass);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCrpass(String Crpass) {
		if (Crpass == null || Crpass.length() == 0) {
			return;
		}
		this.crpass = new byte[Crpass.length()];
		string2Bytes(Crpass, this.crpass);
	}

	public String getCrfail() {
		try {
			return new String(crfail);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCrfail(String Crfail) {
		if (Crfail == null || Crfail.length() == 0) {
			return;
		}
		this.crfail = new byte[Crfail.length()];
		string2Bytes(Crfail, this.crfail);
	}

	public String getControlreportquaility() {
		try {
			return new String(controlreportquaility);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setControlreportquaility(String Controlreportquaility) {
		if (Controlreportquaility == null || Controlreportquaility.length() == 0) {
			return;
		}
		this.controlreportquaility = new byte[Controlreportquaility.length()];
		string2Bytes(Controlreportquaility, this.controlreportquaility);
	}

	public String getCreateddate() {
		try {
			return new String(createddate);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCreateddate(String Createddate) {
		if (Createddate == null || Createddate.length() == 0) {
			return;
		}
		this.createddate = new byte[Createddate.length()];
		string2Bytes(Createddate, this.createddate);
	}

	public String getCreatedby() {
		try {
			return new String(createdby);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCreatedby(String Createdby) {
		if (Createdby == null || Createdby.length() == 0) {
			return;
		}
		this.createdby = new byte[Createdby.length()];
		string2Bytes(Createdby, this.createdby);
	}

	public String getUpdateddate() {
		try {
			return new String(updateddate);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUpdateddate(String Updateddate) {
		if (Updateddate == null || Updateddate.length() == 0) {
			return;
		}
		this.updateddate = new byte[Updateddate.length()];
		string2Bytes(Updateddate, this.updateddate);
	}

	public String getUpdatedby() {
		try {
			return new String(updatedby);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUpdatedby(String Updatedby) {
		if (Updatedby == null || Updatedby.length() == 0) {
			return;
		}
		this.updatedby = new byte[Updatedby.length()];
		string2Bytes(Updatedby, this.updatedby);
	}

	public String getIsactive() {
		try {
			return new String(isactive);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setIsactive(String Isactive) {
		if (Isactive == null || Isactive.length() == 0) {
			return;
		}
		this.isactive = new byte[Isactive.length()];
		string2Bytes(Isactive, this.isactive);
	}

}
