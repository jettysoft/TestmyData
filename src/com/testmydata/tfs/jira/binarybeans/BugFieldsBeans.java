package com.testmydata.tfs.jira.binarybeans;

import com.testmydata.binarybeans.BinaryTrade;

public class BugFieldsBeans extends BinaryTrade {
	byte[] bugid, title, assignedto, state, reason, area, history, reprosteps;
	BugAttachmentsBeans[] bugattachmentsbeans;

	public BugAttachmentsBeans[] getBugattachmentsbeans() {
		return bugattachmentsbeans;
	}

	public void setBugattachmentsbeans(BugAttachmentsBeans[] bugattachmentsbeans) {
		this.bugattachmentsbeans = bugattachmentsbeans;
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

}
