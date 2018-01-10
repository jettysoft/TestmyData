/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testmydata.dashboardfunction;

import com.testmydata.binarybeans.BinaryTrade;

public class Rule extends BinaryTrade {
	byte[] rulename;
	int pass;
	int fail;
	boolean isExecute = false;
	Module module;
	int batchId;

	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public boolean IsExecute() {
		return isExecute;
	}

	public void setIsExecute(boolean isExecute) {
		this.isExecute = isExecute;
	}

	public String getRulename() {
		try {
			return new String(rulename);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setRulename(String Rulename) {
		if (Rulename == null || Rulename.length() == 0) {
			return;
		}
		this.rulename = new byte[Rulename.length()];
		string2Bytes(Rulename, this.rulename);
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
