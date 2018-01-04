package com.testmydata.binarybeans;

public class ControlReportExecutionBinaryTrade extends BinaryTrade {
	private byte[] id, module, name, stoststatus, sttotrstatus, trtolstatus, ltotstatus, rulestatus, stostdiff,
			sttotrdiff, trtoldiff, ltotdiff, sourcecount, stagingcount, transcount, loadingcount, targetcount, message,
			executedby, executedate, sourcecolvalue, stagingcolvalue, transcolvalue, loadingcolvalue, targetcolvalue;

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

	public String getModule() {
		try {
			return new String(module);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setModule(String Module) {
		if (Module == null || Module.length() == 0) {
			return;
		}
		this.module = new byte[Module.length()];
		string2Bytes(Module, this.module);
	}

	public String getName() {
		try {
			return new String(name);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setName(String Name) {
		if (Name == null || Name.length() == 0) {
			return;
		}
		this.name = new byte[Name.length()];
		string2Bytes(Name, this.name);
	}

	public String getStoststatus() {
		try {
			return new String(stoststatus);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStoststatus(String Stoststatus) {
		if (Stoststatus == null || Stoststatus.length() == 0) {
			return;
		}
		this.stoststatus = new byte[Stoststatus.length()];
		string2Bytes(Stoststatus, this.stoststatus);
	}

	public String getSttotrstatus() {
		try {
			return new String(sttotrstatus);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSttotrstatus(String Sttotrstatus) {
		if (Sttotrstatus == null || Sttotrstatus.length() == 0) {
			return;
		}
		this.sttotrstatus = new byte[Sttotrstatus.length()];
		string2Bytes(Sttotrstatus, this.sttotrstatus);
	}

	public String getTrtolstatus() {
		try {
			return new String(trtolstatus);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTrtolstatus(String Trtolstatus) {
		if (Trtolstatus == null || Trtolstatus.length() == 0) {
			return;
		}
		this.trtolstatus = new byte[Trtolstatus.length()];
		string2Bytes(Trtolstatus, this.trtolstatus);
	}

	public String getLtotstatus() {
		try {
			return new String(ltotstatus);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLtotstatus(String Ltotstatus) {
		if (Ltotstatus == null || Ltotstatus.length() == 0) {
			return;
		}
		this.ltotstatus = new byte[Ltotstatus.length()];
		string2Bytes(Ltotstatus, this.ltotstatus);
	}

	public String getRulestatus() {
		try {
			return new String(rulestatus);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setRulestatus(String Rulestatus) {
		if (Rulestatus == null || Rulestatus.length() == 0) {
			return;
		}
		this.rulestatus = new byte[Rulestatus.length()];
		string2Bytes(Rulestatus, this.rulestatus);
	}

	public String getStostdiff() {
		try {
			return new String(stostdiff);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStostdiff(String Stostdiff) {
		if (Stostdiff == null || Stostdiff.length() == 0) {
			return;
		}
		this.stostdiff = new byte[Stostdiff.length()];
		string2Bytes(Stostdiff, this.stostdiff);
	}

	public String getSttotrdiff() {
		try {
			return new String(sttotrdiff);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSttotrdiff(String Sttotrdiff) {
		if (Sttotrdiff == null || Sttotrdiff.length() == 0) {
			return;
		}
		this.sttotrdiff = new byte[Sttotrdiff.length()];
		string2Bytes(Sttotrdiff, this.sttotrdiff);
	}

	public String getTrtoldiff() {
		try {
			return new String(trtoldiff);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTrtoldiff(String Trtoldiff) {
		if (Trtoldiff == null || Trtoldiff.length() == 0) {
			return;
		}
		this.trtoldiff = new byte[Trtoldiff.length()];
		string2Bytes(Trtoldiff, this.trtoldiff);
	}

	public String getLtotdiff() {
		try {
			return new String(ltotdiff);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLtotdiff(String Ltotdiff) {
		if (Ltotdiff == null || Ltotdiff.length() == 0) {
			return;
		}
		this.ltotdiff = new byte[Ltotdiff.length()];
		string2Bytes(Ltotdiff, this.ltotdiff);
	}

	public String getSourcecount() {
		try {
			return new String(sourcecount);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSourcecount(String Sourcecount) {
		if (Sourcecount == null || Sourcecount.length() == 0) {
			return;
		}
		this.sourcecount = new byte[Sourcecount.length()];
		string2Bytes(Sourcecount, this.sourcecount);
	}

	public String getStagingcount() {
		try {
			return new String(stagingcount);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStagingcount(String Stagingcount) {
		if (Stagingcount == null || Stagingcount.length() == 0) {
			return;
		}
		this.stagingcount = new byte[Stagingcount.length()];
		string2Bytes(Stagingcount, this.stagingcount);
	}

	public String getTranscount() {
		try {
			return new String(transcount);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTranscount(String Transcount) {
		if (Transcount == null || Transcount.length() == 0) {
			return;
		}
		this.transcount = new byte[Transcount.length()];
		string2Bytes(Transcount, this.transcount);
	}

	public String getLoadingcount() {
		try {
			return new String(loadingcount);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLoadingcount(String Loadingcount) {
		if (Loadingcount == null || Loadingcount.length() == 0) {
			return;
		}
		this.loadingcount = new byte[Loadingcount.length()];
		string2Bytes(Loadingcount, this.loadingcount);
	}

	public String getTargetcount() {
		try {
			return new String(targetcount);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTargetcount(String Targetcount) {
		if (Targetcount == null || Targetcount.length() == 0) {
			return;
		}
		this.targetcount = new byte[Targetcount.length()];
		string2Bytes(Targetcount, this.targetcount);
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

	public String getExecutedate() {
		try {
			return new String(executedate);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setExecutedate(String Executedate) {
		if (Executedate == null || Executedate.length() == 0) {
			return;
		}
		this.executedate = new byte[Executedate.length()];
		string2Bytes(Executedate, this.executedate);
	}

	public String getSourcecolvalue() {
		try {
			return new String(sourcecolvalue);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSourcecolvalue(String Sourcecolvalue) {
		if (Sourcecolvalue == null || Sourcecolvalue.length() == 0) {
			return;
		}
		this.sourcecolvalue = new byte[Sourcecolvalue.length()];
		string2Bytes(Sourcecolvalue, this.sourcecolvalue);
	}

	public String getStagingcolvalue() {
		try {
			return new String(stagingcolvalue);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStagingcolvalue(String Stagingcolvalue) {
		if (Stagingcolvalue == null || Stagingcolvalue.length() == 0) {
			return;
		}
		this.stagingcolvalue = new byte[Stagingcolvalue.length()];
		string2Bytes(Stagingcolvalue, this.stagingcolvalue);
	}

	public String getTranscolvalue() {
		try {
			return new String(transcolvalue);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTranscolvalue(String Transcolvalue) {
		if (Transcolvalue == null || Transcolvalue.length() == 0) {
			return;
		}
		this.transcolvalue = new byte[Transcolvalue.length()];
		string2Bytes(Transcolvalue, this.transcolvalue);
	}

	public String getLoadingcolvalue() {
		try {
			return new String(loadingcolvalue);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLoadingcolvalue(String Loadingcolvalue) {
		if (Loadingcolvalue == null || Loadingcolvalue.length() == 0) {
			return;
		}
		this.loadingcolvalue = new byte[Loadingcolvalue.length()];
		string2Bytes(Loadingcolvalue, this.loadingcolvalue);
	}

	public String getTargetcolvalue() {
		try {
			return new String(targetcolvalue);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTargetcolvalue(String Targetcolvalue) {
		if (Targetcolvalue == null || Targetcolvalue.length() == 0) {
			return;
		}
		this.targetcolvalue = new byte[Targetcolvalue.length()];
		string2Bytes(Targetcolvalue, this.targetcolvalue);
	}
}
