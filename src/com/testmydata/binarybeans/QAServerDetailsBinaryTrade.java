package com.testmydata.binarybeans;

public class QAServerDetailsBinaryTrade extends BinaryTrade {
	private byte[] id, defaulttype, classname, dburl, username, password, userid, createddate, updateddate, status,
			servertype;

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

	public String getDefaulttype() {
		try {
			return new String(defaulttype);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setDefaulttype(String Defaulttype) {
		if (Defaulttype == null || Defaulttype.length() == 0) {
			return;
		}
		this.defaulttype = new byte[Defaulttype.length()];
		string2Bytes(Defaulttype, this.defaulttype);
	}

	public String getClassname() {
		try {
			return new String(classname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setClassname(String Classname) {
		if (Classname == null || Classname.length() == 0) {
			return;
		}
		this.classname = new byte[Classname.length()];
		string2Bytes(Classname, this.classname);
	}

	public String getDburl() {
		try {
			return new String(dburl);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setDburl(String Dburl) {
		if (Dburl == null || Dburl.length() == 0) {
			return;
		}
		this.dburl = new byte[Dburl.length()];
		string2Bytes(Dburl, this.dburl);
	}

	public String getUsername() {
		try {
			return new String(username);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUsername(String Username) {
		if (Username == null || Username.length() == 0) {
			return;
		}
		this.username = new byte[Username.length()];
		string2Bytes(Username, this.username);
	}

	public String getPassword() {
		try {
			return new String(password);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPassword(String Password) {
		if (Password == null || Password.length() == 0) {
			return;
		}
		this.password = new byte[Password.length()];
		string2Bytes(Password, this.password);
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

	public String getStatus() {
		try {
			return new String(status);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStatus(String Status) {
		if (Status == null || Status.length() == 0) {
			return;
		}
		this.status = new byte[Status.length()];
		string2Bytes(Status, this.status);
	}

	public String getServertype() {
		try {
			return new String(servertype);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setServertype(String Servertype) {
		if (Servertype == null || Servertype.length() == 0) {
			return;
		}
		this.servertype = new byte[Servertype.length()];
		string2Bytes(Servertype, this.servertype);
	}
}
