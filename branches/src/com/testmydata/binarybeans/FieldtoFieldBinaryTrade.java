package com.testmydata.binarybeans;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class FieldtoFieldBinaryTrade extends BinaryTrade {
	private byte[] id, modulename, tsname, tcname, testcondition, sqlscript, executioncount, passcount, failcount,
			createdby, executedby, updatedby, status, createddate, updateddate, executeddate, message, queryresult,
			teststatus, release, cycle, testsuitename, testsuiteid;
	private BooleanProperty checkboxs;

	public FieldtoFieldBinaryTrade() {
		data = new byte[2];
		this.checkboxs = new SimpleBooleanProperty(false);
		// This value will change depending what you want to store
	}

	public boolean isCheckboxs() {
		return checkboxs.get();
	}

	public void setCheckboxs(boolean checkboxs) {
		this.checkboxs.set(checkboxs);
	}

	public BooleanProperty checkboxsProperty() {
		return checkboxs;
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

	public boolean isButtons1() {
		return data[1] == 1 ? true : false;
	}

	public void setButtons1(boolean buttons1) {
		data[1] = (byte) (buttons1 ? 1 : 0);
	}

	public BooleanProperty buttons1Property() {
		return new SimpleBooleanProperty(isButtons1());
	}

	public String getTestsuitename() {
		try {
			return new String(testsuitename);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTestsuitename(String Testsuitename) {
		if (Testsuitename == null || Testsuitename.length() == 0) {
			return;
		}
		this.testsuitename = new byte[Testsuitename.length()];
		string2Bytes(Testsuitename, this.testsuitename);
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
		this.tcname = new byte[Tcname.length()];
		string2Bytes(Tcname, this.tcname);
	}

	public String getTestcondition() {
		try {
			return new String(testcondition);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTestcondition(String Testcondition) {
		if (Testcondition == null || Testcondition.length() == 0) {
			return;
		}
		this.testcondition = new byte[Testcondition.length()];
		string2Bytes(Testcondition, this.testcondition);
	}

	public String getSqlscript() {
		try {
			return new String(sqlscript);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSqlscript(String Sqlscript) {
		if (Sqlscript == null || Sqlscript.length() == 0) {
			return;
		}
		this.sqlscript = new byte[Sqlscript.length()];
		string2Bytes(Sqlscript, this.sqlscript);
	}

	public String getExecutioncount() {
		try {
			return new String(executioncount);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setExecutioncount(String Executioncount) {
		if (Executioncount == null || Executioncount.length() == 0) {
			return;
		}
		this.executioncount = new byte[Executioncount.length()];
		string2Bytes(Executioncount, this.executioncount);
	}

	public String getPasscount() {
		try {
			return new String(passcount);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPasscount(String Passcount) {
		if (Passcount == null || Passcount.length() == 0) {
			return;
		}
		this.passcount = new byte[Passcount.length()];
		string2Bytes(Passcount, this.passcount);
	}

	public String getFailcount() {
		try {
			return new String(failcount);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setFailcount(String Failcount) {
		if (Failcount == null || Failcount.length() == 0) {
			return;
		}
		this.failcount = new byte[Failcount.length()];
		string2Bytes(Failcount, this.failcount);
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

	public String getExecutedby() {
		try {
			return new String(executedby);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setExecutedby(String Executedby) {
		if (Executedby == null || Executedby.length() == 0) {
			return;
		}
		this.executedby = new byte[Executedby.length()];
		string2Bytes(Executedby, this.executedby);
	}

	public String getUpdatedby() {
		try {
			return new String(updatedby);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUpdatedby(String Updatedby) {
		if (Updatedby == null || Updatedby.length() == 0) {
			return;
		}
		this.updatedby = new byte[Updatedby.length()];
		string2Bytes(Updatedby, this.updatedby);
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

	public String getExecuteddate() {
		try {
			return new String(executeddate);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setExecuteddate(String Executeddate) {
		if (Executeddate == null || Executeddate.length() == 0) {
			return;
		}
		this.executeddate = new byte[Executeddate.length()];
		string2Bytes(Executeddate, this.executeddate);
	}

	public String getMessage() {
		try {
			return new String(message);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setMessage(String Message) {
		if (Message == null || Message.length() == 0) {
			return;
		}
		this.message = new byte[Message.length()];
		string2Bytes(Message, this.message);
	}

	public String getQueryresult() {
		try {
			return new String(queryresult);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setQueryresult(String Queryresult) {
		if (Queryresult == null || Queryresult.length() == 0) {
			return;
		}
		this.queryresult = new byte[Queryresult.length()];
		string2Bytes(Queryresult, this.queryresult);
	}

	public String getTeststatus() {
		try {
			return new String(teststatus);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTeststatus(String Teststatus) {
		if (Teststatus == null || Teststatus.length() == 0) {
			return;
		}
		this.teststatus = new byte[Teststatus.length()];
		string2Bytes(Teststatus, this.teststatus);
	}

	public String getRelease() {
		try {
			return new String(release);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setRelease(String Release) {
		if (Release == null || Release.length() == 0) {
			return;
		}
		this.release = new byte[Release.length()];
		string2Bytes(Release, this.release);
	}

	public String getCycle() {
		try {
			return new String(cycle);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCycle(String Cycle) {
		if (Cycle == null || Cycle.length() == 0) {
			return;
		}
		this.cycle = new byte[Cycle.length()];
		string2Bytes(Cycle, this.cycle);
	}

	public String getTestsuiteid() {
		try {
			return new String(testsuiteid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTestsuiteid(String Testsuiteid) {
		if (Testsuiteid == null || Testsuiteid.length() == 0) {
			return;
		}
		this.testsuiteid = new byte[Testsuiteid.length()];
		string2Bytes(Testsuiteid, this.testsuiteid);
	}

}
