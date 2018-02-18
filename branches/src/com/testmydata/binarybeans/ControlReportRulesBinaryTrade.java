package com.testmydata.binarybeans;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ControlReportRulesBinaryTrade extends BinaryTrade {
	private byte[] id, name, sdb, stable, scolumn, stdb, sttable, stcolumn, trdb, trtable, trcolumn, ldb, ltable,
			lcolumn, tdb, ttable, tcolumn, createdby, updatedby, createdDate, updatedDate, module, stost, sttotr, trtol,
			ltot, stostcol, sttotrcol, trtolcol, ltotcol;

	public ControlReportRulesBinaryTrade() {
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

	public String getSdb() {
		try {
			return new String(sdb);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSdb(String Sdb) {
		if (Sdb == null || Sdb.length() == 0) {
			return;
		}
		this.sdb = new byte[Sdb.length()];
		string2Bytes(Sdb, this.sdb);
	}

	public String getStable() {
		try {
			return new String(stable);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStable(String Stable) {
		if (Stable == null || Stable.length() == 0) {
			return;
		}
		this.stable = new byte[Stable.length()];
		string2Bytes(Stable, this.stable);
	}

	public String getScolumn() {
		try {
			return new String(scolumn);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setScolumn(String Scolumn) {
		if (Scolumn == null || Scolumn.length() == 0) {
			return;
		}
		this.scolumn = new byte[Scolumn.length()];
		string2Bytes(Scolumn, this.scolumn);
	}

	public String getStdb() {
		try {
			return new String(stdb);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStdb(String Stdb) {
		if (Stdb == null || Stdb.length() == 0) {
			return;
		}
		this.stdb = new byte[Stdb.length()];
		string2Bytes(Stdb, this.stdb);
	}

	public String getSttable() {
		try {
			return new String(sttable);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSttable(String Sttable) {
		if (Sttable == null || Sttable.length() == 0) {
			return;
		}
		this.sttable = new byte[Sttable.length()];
		string2Bytes(Sttable, this.sttable);
	}

	public String getStcolumn() {
		try {
			return new String(stcolumn);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStcolumn(String Stcolumn) {
		if (Stcolumn == null || Stcolumn.length() == 0) {
			return;
		}
		this.stcolumn = new byte[Stcolumn.length()];
		string2Bytes(Stcolumn, this.stcolumn);
	}

	public String getTrdb() {
		try {
			return new String(trdb);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTrdb(String Trdb) {
		if (Trdb == null || Trdb.length() == 0) {
			return;
		}
		this.trdb = new byte[Trdb.length()];
		string2Bytes(Trdb, this.trdb);
	}

	public String getTrtable() {
		try {
			return new String(trtable);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTrtable(String Trtable) {
		if (Trtable == null || Trtable.length() == 0) {
			return;
		}
		this.trtable = new byte[Trtable.length()];
		string2Bytes(Trtable, this.trtable);
	}

	public String getTrcolumn() {
		try {
			return new String(trcolumn);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTrcolumn(String Trcolumn) {
		if (Trcolumn == null || Trcolumn.length() == 0) {
			return;
		}
		this.trcolumn = new byte[Trcolumn.length()];
		string2Bytes(Trcolumn, this.trcolumn);
	}

	public String getLdb() {
		try {
			return new String(ldb);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLdb(String Ldb) {
		if (Ldb == null || Ldb.length() == 0) {
			return;
		}
		this.ldb = new byte[Ldb.length()];
		string2Bytes(Ldb, this.ldb);
	}

	public String getLtable() {
		try {
			return new String(ltable);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLtable(String Ltable) {
		if (Ltable == null || Ltable.length() == 0) {
			return;
		}
		this.ltable = new byte[Ltable.length()];
		string2Bytes(Ltable, this.ltable);
	}

	public String getLcolumn() {
		try {
			return new String(lcolumn);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLcolumn(String Lcolumn) {
		if (Lcolumn == null || Lcolumn.length() == 0) {
			return;
		}
		this.lcolumn = new byte[Lcolumn.length()];
		string2Bytes(Lcolumn, this.lcolumn);
	}

	public String getTdb() {
		try {
			return new String(tdb);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTdb(String Tdb) {
		if (Tdb == null || Tdb.length() == 0) {
			return;
		}
		this.tdb = new byte[Tdb.length()];
		string2Bytes(Tdb, this.tdb);
	}

	public String getTtable() {
		try {
			return new String(ttable);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTtable(String Ttable) {
		if (Ttable == null || Ttable.length() == 0) {
			return;
		}
		this.ttable = new byte[Ttable.length()];
		string2Bytes(Ttable, this.ttable);
	}

	public String getTcolumn() {
		try {
			return new String(tcolumn);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTcolumn(String Tcolumn) {
		if (Tcolumn == null || Tcolumn.length() == 0) {
			return;
		}
		this.tcolumn = new byte[Tcolumn.length()];
		string2Bytes(Tcolumn, this.tcolumn);
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

	public String getCreatedDate() {
		try {
			return new String(createdDate);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCreatedDate(String CreatedDate) {
		if (CreatedDate == null || CreatedDate.length() == 0) {
			return;
		}
		this.createdDate = new byte[CreatedDate.length()];
		string2Bytes(CreatedDate, this.createdDate);
	}

	public String getUpdatedDate() {
		try {
			return new String(updatedDate);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUpdatedDate(String UpdatedDate) {
		if (UpdatedDate == null || UpdatedDate.length() == 0) {
			return;
		}
		this.updatedDate = new byte[UpdatedDate.length()];
		string2Bytes(UpdatedDate, this.updatedDate);
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

	public String getStost() {
		try {
			return new String(stost);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStost(String Stost) {
		if (Stost == null || Stost.length() == 0) {
			return;
		}
		this.stost = new byte[Stost.length()];
		string2Bytes(Stost, this.stost);
	}

	public String getSttotr() {
		try {
			return new String(sttotr);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSttotr(String Sttotr) {
		if (Sttotr == null || Sttotr.length() == 0) {
			return;
		}
		this.sttotr = new byte[Sttotr.length()];
		string2Bytes(Sttotr, this.sttotr);
	}

	public String getTrtol() {
		try {
			return new String(trtol);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTrtol(String Trtol) {
		if (Trtol == null || Trtol.length() == 0) {
			return;
		}
		this.trtol = new byte[Trtol.length()];
		string2Bytes(Trtol, this.trtol);
	}

	public String getLtot() {
		try {
			return new String(ltot);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLtot(String Ltot) {
		if (Ltot == null || Ltot.length() == 0) {
			return;
		}
		this.ltot = new byte[Ltot.length()];
		string2Bytes(Ltot, this.ltot);
	}

	public String getStostcol() {
		try {
			return new String(stostcol);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStostcol(String Stostcol) {
		if (Stostcol == null || Stostcol.length() == 0) {
			return;
		}
		this.stostcol = new byte[Stostcol.length()];
		string2Bytes(Stostcol, this.stostcol);
	}

	public String getSttotrcol() {
		try {
			return new String(sttotrcol);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSttotrcol(String Sttotrcol) {
		if (Sttotrcol == null || Sttotrcol.length() == 0) {
			return;
		}
		this.sttotrcol = new byte[Sttotrcol.length()];
		string2Bytes(Sttotrcol, this.sttotrcol);
	}

	public String getTrtolcol() {
		try {
			return new String(trtolcol);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTrtolcol(String Trtolcol) {
		if (Trtolcol == null || Trtolcol.length() == 0) {
			return;
		}
		this.trtolcol = new byte[Trtolcol.length()];
		string2Bytes(Trtolcol, this.trtolcol);
	}

	public String getLtotcol() {
		try {
			return new String(ltotcol);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLtotcol(String Ltotcol) {
		if (Ltotcol == null || Ltotcol.length() == 0) {
			return;
		}
		this.ltotcol = new byte[Ltotcol.length()];
		string2Bytes(Ltotcol, this.ltotcol);
	}
}
