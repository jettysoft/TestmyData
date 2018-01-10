/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testmydata.dashboardfunction;

import com.testmydata.binarybeans.BinaryTrade;

public class Module extends BinaryTrade {
	Rule[] rules;
	byte[] modulename;
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

	public Rule[] getRules() {
		return rules;
	}

	public void setRules(Rule[] rules) {
		this.rules = rules;
	}

	public String getModulename() {
		try {
			return new String(modulename);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setModulename(String Modulename) {
		if (Modulename == null || Modulename.length() == 0) {
			return;
		}
		this.modulename = new byte[Modulename.length()];
		string2Bytes(Modulename, this.modulename);
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
