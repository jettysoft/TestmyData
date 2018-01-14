package com.testmydata.tfs.jira.binarybeans;

import com.testmydata.binarybeans.BinaryTrade;

public class IterationPhasesBean extends BinaryTrade {
	byte[] phasename;
	PhaseSprintsBeans[] phasesprintsbeans;

	public String getPhasename() {
		try {
			return new String(phasename);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPhasename(String Phasename) {
		if (Phasename == null || Phasename.length() == 0) {
			return;
		}
		this.phasename = new byte[Phasename.length()];
		string2Bytes(Phasename, this.phasename);
	}

}
