package com.testmydata.binarybeans;

public class BugServerBinaryTrade extends BinaryTrade {

	private byte[] id, servertype, isdefault, collectionurl, isactive, createdby, createdate;
	BugServerProjectsBinaryTrade[] bugprojects;
	BugServerUsersBinaryTrade[] bugusers;

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

	public String getIsdefault() {
		try {
			return new String(isdefault);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setIsdefault(String Isdefault) {
		if (Isdefault == null || Isdefault.length() == 0) {
			return;
		}
		this.isdefault = new byte[Isdefault.length()];
		string2Bytes(Isdefault, this.isdefault);
	}

	public String getCollectionurl() {
		try {
			return new String(collectionurl);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCollectionurl(String Collectionurl) {
		if (Collectionurl == null || Collectionurl.length() == 0) {
			return;
		}
		this.collectionurl = new byte[Collectionurl.length()];
		string2Bytes(Collectionurl, this.collectionurl);
	}

	public String getIsactive() {
		try {
			return new String(isactive);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setIsactive(String Isactive) {
		if (Isactive == null || Isactive.length() == 0) {
			return;
		}
		this.isactive = new byte[Isactive.length()];
		string2Bytes(Isactive, this.isactive);
	}

	public String getCreatedby() {
		try {
			return new String(createdby);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCreatedby(String Createdby) {
		if (Createdby == null || Createdby.length() == 0) {
			return;
		}
		this.createdby = new byte[Createdby.length()];
		string2Bytes(Createdby, this.createdby);
	}

	public String getCreatedate() {
		try {
			return new String(createdate);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCreatedate(String Createdate) {
		if (Createdate == null || Createdate.length() == 0) {
			return;
		}
		this.createdate = new byte[Createdate.length()];
		string2Bytes(Createdate, this.createdate);
	}

	public BugServerProjectsBinaryTrade[] getBugProjects() {
		return bugprojects;
	}

	public void setBugProjects(BugServerProjectsBinaryTrade[] bugsproj) {
		this.bugprojects = bugsproj;
	}

	public BugServerUsersBinaryTrade[] getBugUsers() {
		return bugusers;
	}

	public void setBugUsers(BugServerUsersBinaryTrade[] bugusers) {
		this.bugusers = bugusers;
	}

}
