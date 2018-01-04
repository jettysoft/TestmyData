package com.testmydata.binarybeans;

public class ControlReportHelperBinaryTrade extends BinaryTrade {
	private byte[] dbnames, tablenames, columnnames;

	public String getDbnames() {
		try {
			return new String(dbnames);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setDbnames(String Dbnames) {
		if (Dbnames == null || Dbnames.length() == 0) {
			return;
		}
		this.dbnames = new byte[Dbnames.length()];
		string2Bytes(Dbnames, this.dbnames);
	}

	public String getTablenames() {
		try {
			return new String(tablenames);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTablenames(String Tablenames) {
		if (Tablenames == null || Tablenames.length() == 0) {
			return;
		}
		this.tablenames = new byte[Tablenames.length()];
		string2Bytes(Tablenames, this.tablenames);
	}

	public String getColumnnames() {
		try {
			return new String(columnnames);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setColumnnames(String Columnnames) {
		if (Columnnames == null || Columnnames.length() == 0) {
			return;
		}
		this.columnnames = new byte[Columnnames.length()];
		string2Bytes(Columnnames, this.columnnames);
	}
}
