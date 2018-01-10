/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testmydata.dashboardfunction;

import com.testmydata.binarybeans.BinaryTrade;

public class Cycle extends BinaryTrade {
	byte[] cyname;
	int pass;
	int fail;
	TestSuite[] testsuites;
	boolean isExecute = false;
	Release release;
	int batchId;

	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public Release getRelease() {
		return release;
	}

	public void setRelease(Release release) {
		this.release = release;
	}

	public boolean IsExecute() {
		return isExecute;
	}

	public void setIsExecute(boolean isExecute) {
		this.isExecute = isExecute;
	}

	public String getCyname() {
		try {
			return new String(cyname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCyname(String Cyname) {
		if (Cyname == null || Cyname.length() == 0) {
			return;
		}
		this.cyname = new byte[Cyname.length()];
		string2Bytes(Cyname, this.cyname);
	}

	public int getPass() {
		return pass;
	}

	public void setPass(int pass) {
		this.pass = pass;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public TestSuite[] getTestsuites() {
		return testsuites;
	}

	public void setTestsuites(TestSuite[] testsuites) {
		this.testsuites = testsuites;
	}

}
