package com.testmydata.tfs.jira.binarybeans;

import com.testmydata.binarybeans.BinaryTrade;

public class PhaseSprintsBeans extends BinaryTrade {
	byte[] sprintname;

	public String getSprintname() {
		try {
			return new String(sprintname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSprintname(String Sprintname) {
		if (Sprintname == null || Sprintname.length() == 0) {
			return;
		}
		this.sprintname = new byte[Sprintname.length()];
		string2Bytes(Sprintname, this.sprintname);
	}
}
