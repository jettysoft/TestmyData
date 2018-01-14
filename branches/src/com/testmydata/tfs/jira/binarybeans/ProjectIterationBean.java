package com.testmydata.tfs.jira.binarybeans;

import com.testmydata.binarybeans.BinaryTrade;

public class ProjectIterationBean extends BinaryTrade {
	byte[] iterationid, iterationpath;
	IterationPhasesBean[] iterationphasesbean;

	public String getIterationid() {
		try {
			return new String(iterationid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setIterationid(String Iterationid) {
		if (Iterationid == null || Iterationid.length() == 0) {
			return;
		}
		this.iterationid = new byte[Iterationid.length()];
		string2Bytes(Iterationid, this.iterationid);
	}

	public String getIterationpath() {
		try {
			return new String(iterationpath);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setIterationpath(String Iterationpath) {
		if (Iterationpath == null || Iterationpath.length() == 0) {
			return;
		}
		this.iterationpath = new byte[Iterationpath.length()];
		string2Bytes(Iterationpath, this.iterationpath);
	}
}
