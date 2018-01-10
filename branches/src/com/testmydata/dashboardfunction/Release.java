/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testmydata.dashboardfunction;

import com.testmydata.binarybeans.BinaryTrade;

public class Release extends BinaryTrade {
	Cycle[] cycle;
	byte[] name;
	int pass;
	int fail;
	boolean isExecute = false;
	int batchId;

	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public boolean IsExecute() {
		return isExecute;
	}

	public void setIsExecute(boolean isExecute) {
		this.isExecute = isExecute;
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

	public Cycle[] getCycle() {
		return cycle;
	}

	public void setCycle(Cycle[] cycle) {
		this.cycle = cycle;
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
