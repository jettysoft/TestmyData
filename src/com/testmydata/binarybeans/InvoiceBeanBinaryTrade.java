package com.testmydata.binarybeans;

import java.sql.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class InvoiceBeanBinaryTrade extends BinaryTrade {

	private byte[] deliverdby;
	private byte[] emailId;
	private byte[] invoiceText;
	private byte[] pstReg;
	private byte[] gstReg;
	private byte[] remarks;
	private byte[] doorNumber;
	private byte[] street;
	private byte[] city;
	private byte[] customerName;
	private byte[] phoneNumber;
	private byte[] shipTo;
	private byte[] deliverystatus;
	private byte[] createddatetime;
	private byte[] phnumber;
	private double subTotal;
	private double gst;
	private double pst;
	private double sgst;
	private double total;
	private double deposit;
	private double balance;
	private long invoiceId;
	private BooleanProperty checkboxs;

	public InvoiceBeanBinaryTrade() {
		data = new byte[11];
		this.checkboxs = new SimpleBooleanProperty(false);
		// 4 bytes for date and 4 bytes for boolean properties
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

	// public boolean isCheckboxs() {
	// return data[6] == 1 ? true : false;
	// }
	//
	// public void setCheckboxs(boolean checkboxs) {
	// data[6] = (byte) (checkboxs ? 1 : 0);
	// }
	//
	// public BooleanProperty checkboxsProperty() {
	// return new SimpleBooleanProperty(isCheckboxs());
	// }

	public boolean isButtons() {
		return data[6] == 1 ? true : false;
	}

	public void setButtons(boolean buttons) {
		data[6] = (byte) (buttons ? 1 : 0);
	}

	public BooleanProperty buttonsProperty() {
		return new SimpleBooleanProperty(isButtons());
	}

	public boolean isButtons1() {
		return data[7] == 1 ? true : false;
	}

	public void setButtons1(boolean buttons1) {
		data[7] = (byte) (buttons1 ? 1 : 0);
	}

	public BooleanProperty buttons1Property() {
		return new SimpleBooleanProperty(isButtons1());
	}

	public boolean isButtons2() {
		return data[8] == 1 ? true : false;
	}

	public void setButtons2(boolean buttons2) {
		data[8] = (byte) (buttons2 ? 1 : 0);
	}

	public BooleanProperty buttons2Property() {
		return new SimpleBooleanProperty(isButtons2());
	}

	public boolean isButtons3() {
		return data[9] == 1 ? true : false;
	}

	public void setButtons3(boolean buttons3) {
		data[9] = (byte) (buttons3 ? 1 : 0);
	}

	public BooleanProperty buttons3Property() {
		return new SimpleBooleanProperty(isButtons3());
	}

	public boolean isButtons4() {
		return data[10] == 1 ? true : false;
	}

	public void setButtons4(boolean buttons4) {
		data[10] = (byte) (buttons4 ? 1 : 0);
	}

	public BooleanProperty buttons4Property() {
		return new SimpleBooleanProperty(isButtons4());
	}

	public String getDeliverdby() {
		try {
			return new String(deliverdby);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setDeliverdby(String deliverdby) {
		if (deliverdby == null || deliverdby.length() == 0) {
			return;
		}
		this.deliverdby = new byte[deliverdby.length()];
		string2Bytes(deliverdby, this.deliverdby);
	}

	public String getEmailid() {

		try {
			return new String(emailId);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setEmailid(String emailid) {
		if (emailid == null || emailid.length() == 0) {
			return;
		}
		this.emailId = new byte[emailid.length()];
		string2Bytes(emailid, this.emailId);
	}

	public String getInvoiceText() {

		try {
			return new String(invoiceText);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setInvoiceText(String invoiceText) {
		if (invoiceText == null || invoiceText.length() == 0) {
			return;
		}
		this.invoiceText = new byte[invoiceText.length()];
		string2Bytes(invoiceText, this.invoiceText);
	}

	public String getPstReg() {

		try {
			return new String(pstReg);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPstReg(String pstReg) {
		if (pstReg == null || pstReg.length() == 0) {
			return;
		}
		this.pstReg = new byte[pstReg.length()];
		string2Bytes(pstReg, this.pstReg);
	}

	public String getDoorNo() {

		try {
			return new String(doorNumber);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setDoorNo(String doorNo) {
		if (doorNo == null || doorNo.length() == 0) {
			return;
		}
		this.doorNumber = new byte[doorNo.length()];
		string2Bytes(doorNo, this.doorNumber);
	}

	public String getStreet() {

		try {
			return new String(street);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStreet(String street) {
		if (street == null || street.length() == 0) {
			return;
		}
		this.street = new byte[street.length()];
		string2Bytes(street, this.street);
	}

	public String getCity() {

		try {
			return new String(city);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCity(String city) {
		if (city == null || city.length() == 0) {
			return;
		}
		this.city = new byte[city.length()];
		string2Bytes(city, this.city);
	}

	public Date getDueDate() {
		long date1 = wordFromBytesFromOffset(0);
		date1 *= 3600_000L; // milliseconds in a day
		return new Date(date1);
	}

	public void setDueDate(Date date1) {
		long lDate = date1.getTime();
		lDate /= 3600_000L; // milliseconds in a day
		data[0] = (byte) (lDate >>> 16);
		data[1] = (byte) (lDate >>> 8);
		data[2] = (byte) (lDate);
	}

	public Date getDate() {
		long date = wordFromBytesFromOffset(3);
		date *= 3600_000L;// milliseconds in a day
		return new Date(date);
	}

	public void setDate(Date date) {
		long lDate = date.getTime();
		lDate /= 3600_000L; // milliseconds in a day
		data[3] = (byte) (lDate >>> 16);
		data[4] = (byte) (lDate >>> 8);
		data[5] = (byte) (lDate);
	}

	public String getPhnumber() {
		try {
			return new String(phnumber);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPhnumber(String Phnumber) {
		if (Phnumber == null || Phnumber.length() == 0) {
			return;
		}
		this.phnumber = new byte[Phnumber.length()];
		string2Bytes(Phnumber, this.phnumber);
	}

	public String getCustomerName() {
		try {
			return new String(customerName);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCustomerName(String customerName) {
		if (customerName == null || customerName.length() == 0) {
			return;
		}
		this.customerName = new byte[customerName.length()];
		string2Bytes(customerName, this.customerName);
	}

	public String getPhonenumber() {
		try {
			return new String(phoneNumber);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPhonenumber(String PhoneNumber) {
		if (PhoneNumber == null || PhoneNumber.length() == 0) {
			return;
		}
		this.phoneNumber = new byte[PhoneNumber.length()];
		string2Bytes(PhoneNumber, this.phoneNumber);
	}

	public String getShipTo() {
		try {
			return new String(shipTo);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setShipTo(String shipTo) {
		if (shipTo == null || shipTo.length() == 0) {
			return;
		}
		this.shipTo = new byte[shipTo.length()];
		string2Bytes(shipTo, this.shipTo);
	}

	public String getDeliverystatus() {
		try {
			return new String(deliverystatus);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setDeliverystatus(String Deliverystatus) {
		if (Deliverystatus == null || Deliverystatus.length() == 0) {
			return;
		}
		this.deliverystatus = new byte[Deliverystatus.length()];
		string2Bytes(Deliverystatus, this.deliverystatus);
	}

	public String getCreateddatetime() {
		try {
			return new String(createddatetime);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCreateddatetime(String Createddatetime) {
		if (Createddatetime == null || Createddatetime.length() == 0) {
			return;
		}
		this.createddatetime = new byte[Createddatetime.length()];
		string2Bytes(Createddatetime, this.createddatetime);
	}

	public String getGstReg() {

		try {
			return new String(gstReg);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setGstReg(String gstReg) {
		if (gstReg == null || gstReg.length() == 0) {
			return;
		}
		this.gstReg = new byte[gstReg.length()];
		string2Bytes(gstReg, this.gstReg);
	}

	public String getRemarks() {

		try {
			return new String(remarks);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setRemarks(String remarks) {
		if (remarks == null || remarks.length() == 0) {
			return;
		}
		this.remarks = new byte[remarks.length()];
		string2Bytes(remarks, this.remarks);
	}

	public long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public double getSgst() {
		return sgst;
	}

	public void setSgst(double sgst) {
		this.sgst = sgst;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public double getGst() {
		return gst;
	}

	public void setGst(double gst) {
		this.gst = gst;
	}

	public double getPst() {
		return pst;
	}

	public void setPst(double pst) {
		this.pst = pst;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
