package com.testmydata.binarybeans;

public class PercentagesHelperBinaryTrade extends BinaryTrade {
	byte[] pass, fail, batchid;

	public String getPass() {
		try {
			return new String(pass);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPass(String Pass) {
		if (Pass == null || Pass.length() == 0) {
			return;
		}
		this.pass = new byte[Pass.length()];
		string2Bytes(Pass, this.pass);
	}

	public String getFail() {
		try {
			return new String(fail);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setFail(String Fail) {
		if (Fail == null || Fail.length() == 0) {
			return;
		}
		this.fail = new byte[Fail.length()];
		string2Bytes(Fail, this.fail);
	}

	public String getBatchid() {
		try {
			return new String(batchid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setBatchid(String Batchid) {
		if (Batchid == null || Batchid.length() == 0) {
			return;
		}
		this.batchid = new byte[Batchid.length()];
		string2Bytes(Batchid, this.batchid);
	}

}
