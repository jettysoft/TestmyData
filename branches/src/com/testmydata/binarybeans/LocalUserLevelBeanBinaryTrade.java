package com.testmydata.binarybeans;

import java.sql.Date;

public class LocalUserLevelBeanBinaryTrade extends BinaryTrade {
	private byte[] localUserLevel;
	private byte[] companyName;

	public LocalUserLevelBeanBinaryTrade() {
		data = new byte[3];
	}

	public String getLocalUserLevel() {
		try {
			return new String(localUserLevel);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLocalUserLevel(String LocalUserLevel) {
		if (LocalUserLevel == null || LocalUserLevel.length() == 0) {
			return;
		}
		this.localUserLevel = new byte[LocalUserLevel.length()];
		string2Bytes(LocalUserLevel, this.localUserLevel);
	}

	public String getCompanyName() {
		try {
			return new String(companyName);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCompanyName(String CompanyName) {
		if (CompanyName == null || CompanyName.length() == 0) {
			return;
		}
		this.companyName = new byte[CompanyName.length()];
		string2Bytes(CompanyName, this.companyName);
	}

	public Date getCreatedDate() {
		long date1 = wordFromBytesFromOffset(0);
		date1 *= 3600_000L; // milliseconds in a day
		return new Date(date1);
	}

	public void setCreatedDate(Date date1) {
		long lDate = date1.getTime();
		lDate /= 3600_000L; // milliseconds in a day
		data[0] = (byte) (lDate >>> 16);
		data[1] = (byte) (lDate >>> 8);
		data[2] = (byte) (lDate);
	}
}
