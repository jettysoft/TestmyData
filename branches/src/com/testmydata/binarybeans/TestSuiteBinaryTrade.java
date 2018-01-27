package com.testmydata.binarybeans;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TestSuiteBinaryTrade extends BinaryTrade {
	private byte[] id, testsuitename, selectiontype, selectedname, timer, testcasescount, passcount, failcount, passper,
			failper, selecteditems, release, cycle;
	private BooleanProperty checkboxs;

	public TestSuiteBinaryTrade() {
		data = new byte[2];
		this.checkboxs = new SimpleBooleanProperty(true);
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

	public String getSelectiontype() {
		try {
			return new String(selectiontype);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSelectiontype(String Selectiontype) {
		if (Selectiontype == null || Selectiontype.length() == 0) {
			return;
		}
		this.selectiontype = new byte[Selectiontype.length()];
		string2Bytes(Selectiontype, this.selectiontype);
	}

	public String getSelectedname() {
		try {
			return new String(selectedname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSelectedname(String Selectedname) {
		if (Selectedname == null || Selectedname.length() == 0) {
			return;
		}
		this.selectedname = new byte[Selectedname.length()];
		string2Bytes(Selectedname, this.selectedname);
	}

	public String getTimer() {
		try {
			return new String(timer);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTimer(String Timer) {
		if (Timer == null || Timer.length() == 0) {
			return;
		}
		this.timer = new byte[Timer.length()];
		string2Bytes(Timer, this.timer);
	}

	public String getTestcasescount() {
		try {
			return new String(testcasescount);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTestcasescount(String Testcasescount) {
		if (Testcasescount == null || Testcasescount.length() == 0) {
			return;
		}
		this.testcasescount = new byte[Testcasescount.length()];
		string2Bytes(Testcasescount, this.testcasescount);
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

	public String getPassper() {
		try {
			return new String(passper);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPassper(String Passper) {
		if (Passper == null || Passper.length() == 0) {
			return;
		}
		this.passper = new byte[Passper.length()];
		string2Bytes(Passper, this.passper);
	}

	public String getFailper() {
		try {
			return new String(failper);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setFailper(String Failper) {
		if (Failper == null || Failper.length() == 0) {
			return;
		}
		this.failper = new byte[Failper.length()];
		string2Bytes(Failper, this.failper);
	}

	public String getSelecteditems() {
		try {
			return new String(selecteditems);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSelecteditems(String Selecteditems) {
		if (Selecteditems == null || Selecteditems.length() == 0) {
			return;
		}
		this.selecteditems = new byte[Selecteditems.length()];
		string2Bytes(Selecteditems, this.selecteditems);
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
}
