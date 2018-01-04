package com.testmydata.binarybeans;

import java.sql.Date;

public class UserTypeBeanBinaryTrade extends BinaryTrade {
	private byte[] UserLevel;

	public UserTypeBeanBinaryTrade() {
		data = new byte[3];
	}

	public Date getUserPaymentValidDate() {
		long date1 = wordFromBytesFromOffset(0);
		date1 *= 3600_000L; // milliseconds in a day
		return new Date(date1);
	}

	public void setUserPaymentValidDate(Date date1) {
		long lDate = date1.getTime();
		lDate /= 3600_000L; // milliseconds in a day
		data[0] = (byte) (lDate >>> 16);
		data[1] = (byte) (lDate >>> 8);
		data[2] = (byte) (lDate);
	}

	public String getUserLevel() {
		try {
			return new String(UserLevel);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUserLevel(String UserLevel) {
		if (UserLevel == null || UserLevel.length() == 0) {
			return;
		}
		this.UserLevel = new byte[UserLevel.length()];
		string2Bytes(UserLevel, this.UserLevel);
	}
}
