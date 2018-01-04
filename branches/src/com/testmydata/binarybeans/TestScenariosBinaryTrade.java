package com.testmydata.binarybeans;

public class TestScenariosBinaryTrade extends BinaryTrade {
	private byte[] tsname, tcname;

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

	public String getTcname() {
		try {
			return new String(tcname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTcname(String Tcname) {
		if (Tcname == null || Tcname.length() == 0) {
			return;
		}
		this.tsname = new byte[Tcname.length()];
		string2Bytes(Tcname, this.tsname);
	}
}
