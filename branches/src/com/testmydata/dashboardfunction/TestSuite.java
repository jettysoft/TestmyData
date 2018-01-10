/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testmydata.dashboardfunction;

import com.testmydata.binarybeans.BinaryTrade;

public class TestSuite extends BinaryTrade {
	byte[] tsname;
	int pass;
	int fail;
	boolean isExecute = false;
	Cycle cycle;
	int batchId;

	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public Cycle getCycle() {
		return cycle;
	}

	public void setCycle(Cycle cycle) {
		this.cycle = cycle;
	}

	public boolean IsExecute() {
		return isExecute;
	}

	public void setIsExecute(boolean isExecute) {
		this.isExecute = isExecute;
	}

	public String getTsname() {
		try {
			return new String(tsname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTsname(String Tsname) {
		if (Tsname == null || Tsname.length() == 0) {
			return;
		}
		this.tsname = new byte[Tsname.length()];
		string2Bytes(Tsname, this.tsname);
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

}
