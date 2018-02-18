package com.testmydata.tfs.jira.binarybeans;

import com.testmydata.binarybeans.BinaryTrade;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class BugFieldsBeans extends BinaryTrade {
	byte[] id, serverid, servertype, bugid, title, tcid, ruleid, state, reason, assignedto, area, history, reprosteps,
			createdby, iterationpath, localprojectid;
	BugAttachmentsBeans[] bugattachmentsbeans;

	public BugFieldsBeans() {
		data = new byte[2];
	}

	public boolean isButtons() {
		return data[0] == 1 ? true : false;
	}

	public void setButtons(boolean buttons) {
		data[0] = (byte) (buttons ? 1 : 0);
	}

	public BooleanProperty buttonsProperty() {
		return new SimpleBooleanProperty(isButtons());
	}

	public BugAttachmentsBeans[] getBugattachmentsbeans() {
		return bugattachmentsbeans;
	}

	public void setBugattachmentsbeans(BugAttachmentsBeans[] bugattachmentsbeans) {
		this.bugattachmentsbeans = bugattachmentsbeans;
	}

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

	public String getServerid() {
		try {
			return new String(serverid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setServerid(String Serverid) {
		if (Serverid == null || Serverid.length() == 0) {
			return;
		}
		this.serverid = new byte[Serverid.length()];
		string2Bytes(Serverid, this.serverid);
	}

	public String getBugid() {
		try {
			return new String(bugid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setBugid(String Bugid) {
		if (Bugid == null || Bugid.length() == 0) {
			return;
		}
		this.bugid = new byte[Bugid.length()];
		string2Bytes(Bugid, this.bugid);
	}

	public String getTitle() {
		try {
			return new String(title);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTitle(String Title) {
		if (Title == null || Title.length() == 0) {
			return;
		}
		this.title = new byte[Title.length()];
		string2Bytes(Title, this.title);
	}

	public String getAssignedto() {
		try {
			return new String(assignedto);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setAssignedto(String Assignedto) {
		if (Assignedto == null || Assignedto.length() == 0) {
			return;
		}
		this.assignedto = new byte[Assignedto.length()];
		string2Bytes(Assignedto, this.assignedto);
	}

	public String getState() {
		try {
			return new String(state);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setState(String State) {
		if (State == null || State.length() == 0) {
			return;
		}
		this.state = new byte[State.length()];
		string2Bytes(State, this.state);
	}

	public String getReason() {
		try {
			return new String(reason);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setReason(String Reason) {
		if (Reason == null || Reason.length() == 0) {
			return;
		}
		this.reason = new byte[Reason.length()];
		string2Bytes(Reason, this.reason);
	}

	public String getArea() {
		try {
			return new String(area);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setArea(String Area) {
		if (Area == null || Area.length() == 0) {
			return;
		}
		this.area = new byte[Area.length()];
		string2Bytes(Area, this.area);
	}

	public String getHistory() {
		try {
			return new String(history);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setHistory(String History) {
		if (History == null || History.length() == 0) {
			return;
		}
		this.history = new byte[History.length()];
		string2Bytes(History, this.history);
	}

	public String getReprosteps() {
		try {
			return new String(reprosteps);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setReprosteps(String Reprosteps) {
		if (Reprosteps == null || Reprosteps.length() == 0) {
			return;
		}
		this.reprosteps = new byte[Reprosteps.length()];
		string2Bytes(Reprosteps, this.reprosteps);
	}

	public String getTcid() {
		try {
			return new String(tcid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTcid(String Tcid) {
		if (Tcid == null || Tcid.length() == 0) {
			return;
		}
		this.tcid = new byte[Tcid.length()];
		string2Bytes(Tcid, this.tcid);
	}

	public String getRuleid() {
		try {
			return new String(ruleid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setRuleid(String Ruleid) {
		if (Ruleid == null || Ruleid.length() == 0) {
			return;
		}
		this.ruleid = new byte[Ruleid.length()];
		string2Bytes(Ruleid, this.ruleid);
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

	public String getIterationpath() {
		try {
			return new String(iterationpath);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setIterationpath(String Iterationpath) {
		if (Iterationpath == null || Iterationpath.length() == 0) {
			return;
		}
		this.iterationpath = new byte[Iterationpath.length()];
		string2Bytes(Iterationpath, this.iterationpath);
	}

	public String getLocalprojectid() {
		try {
			return new String(localprojectid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLocalprojectid(String Localprojectid) {
		if (Localprojectid == null || Localprojectid.length() == 0) {
			return;
		}
		this.localprojectid = new byte[Localprojectid.length()];
		string2Bytes(Localprojectid, this.localprojectid);
	}

}
