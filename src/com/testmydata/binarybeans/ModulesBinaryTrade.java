package com.testmydata.binarybeans;

public class ModulesBinaryTrade extends BinaryTrade {
	private byte[] id, createddate, userid, modulename, updateddate;

	public String getId() {
		try {
			return new String(id);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setId(String Id) {
		if (Id == null || Id.length() == 0) {
			return;
		}
		this.id = new byte[Id.length()];
		string2Bytes(Id, this.id);
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

	public String getUserid() {
		try {
			return new String(userid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUserid(String Userid) {
		if (Userid == null || Userid.length() == 0) {
			return;
		}
		this.userid = new byte[Userid.length()];
		string2Bytes(Userid, this.userid);
	}

	public String getCreateddate() {
		try {
			return new String(createddate);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCreateddate(String Createddate) {
		if (Createddate == null || Createddate.length() == 0) {
			return;
		}
		this.createddate = new byte[Createddate.length()];
		string2Bytes(Createddate, this.createddate);
	}

	public String getUpdateddate() {
		try {
			return new String(updateddate);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUpdateddate(String Updateddate) {
		if (Updateddate == null || Updateddate.length() == 0) {
			return;
		}
		this.updateddate = new byte[Updateddate.length()];
		string2Bytes(Updateddate, this.updateddate);
	}
}
