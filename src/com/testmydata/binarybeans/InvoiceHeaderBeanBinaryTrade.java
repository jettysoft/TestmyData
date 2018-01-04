package com.testmydata.binarybeans;

import java.util.Date;

public class InvoiceHeaderBeanBinaryTrade extends BinaryTrade {
	private byte[] expensesproject, companyname, companytype, street, city, state, zipcode, officenumber, cellnumber,
			faxnumber, gstregistration, pstregistration, gstselected, pstselected, interesttype, graceperiod, terms,
			createduserid, scanselected, invoicetemplate, posprint, website;
	private double interestrate, deliverycharge;
	private Date interestexecuteddate;

	public String getExpensesproject() {

		try {
			return new String(expensesproject);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setExpensesproject(String Expensesproject) {
		if (Expensesproject == null || Expensesproject.length() == 0) {
			return;
		}
		this.expensesproject = new byte[Expensesproject.length()];
		string2Bytes(Expensesproject, this.expensesproject);
	}

	public String getCompanyname() {

		try {
			return new String(companyname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCompanyname(String Companyname) {
		if (Companyname == null || Companyname.length() == 0) {
			return;
		}
		this.companyname = new byte[Companyname.length()];
		string2Bytes(Companyname, this.companyname);
	}

	public String getCompanytype() {

		try {
			return new String(companytype);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCompanytype(String Companytype) {
		if (Companytype == null || Companytype.length() == 0) {
			return;
		}
		this.companytype = new byte[Companytype.length()];
		string2Bytes(Companytype, this.companytype);
	}

	public String getStreet() {

		try {
			return new String(street);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStreet(String Street) {
		if (Street == null || Street.length() == 0) {
			return;
		}
		this.street = new byte[Street.length()];
		string2Bytes(Street, this.street);
	}

	public String getCity() {

		try {
			return new String(city);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCity(String City) {
		if (City == null || City.length() == 0) {
			return;
		}
		this.city = new byte[City.length()];
		string2Bytes(City, this.city);
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

	public String getZipcode() {

		try {
			return new String(zipcode);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setZipcode(String Zipcode) {
		if (Zipcode == null || Zipcode.length() == 0) {
			return;
		}
		this.zipcode = new byte[Zipcode.length()];
		string2Bytes(Zipcode, this.zipcode);
	}

	public String getOfficenumber() {

		try {
			return new String(officenumber);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setOfficenumber(String Officenumber) {
		if (Officenumber == null || Officenumber.length() == 0) {
			return;
		}
		this.officenumber = new byte[Officenumber.length()];
		string2Bytes(Officenumber, this.officenumber);
	}

	public String getCellnumber() {

		try {
			return new String(cellnumber);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCellnumber(String Cellnumber) {
		if (Cellnumber == null || Cellnumber.length() == 0) {
			return;
		}
		this.cellnumber = new byte[Cellnumber.length()];
		string2Bytes(Cellnumber, this.cellnumber);
	}

	public String getFaxnumber() {

		try {
			return new String(faxnumber);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setFaxnumber(String Faxnumber) {
		if (Faxnumber == null || Faxnumber.length() == 0) {
			return;
		}
		this.faxnumber = new byte[Faxnumber.length()];
		string2Bytes(Faxnumber, this.faxnumber);
	}

	public String getGstregistration() {

		try {
			return new String(gstregistration);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setGstregistration(String Gstregistration) {
		if (Gstregistration == null || Gstregistration.length() == 0) {
			return;
		}
		this.gstregistration = new byte[Gstregistration.length()];
		string2Bytes(Gstregistration, this.gstregistration);
	}

	public String getPstregistration() {
		try {
			return new String(pstregistration);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPstregistration(String Pstregistration) {
		if (Pstregistration == null || Pstregistration.length() == 0) {
			return;
		}
		this.pstregistration = new byte[Pstregistration.length()];
		string2Bytes(Pstregistration, this.pstregistration);
	}

	public String getGstselected() {

		try {
			return new String(gstselected);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setGstselected(String Gstselected) {
		if (Gstselected == null || Gstselected.length() == 0) {
			return;
		}
		this.gstselected = new byte[Gstselected.length()];
		string2Bytes(Gstselected, this.gstselected);
	}

	public String getPstselected() {

		try {
			return new String(pstselected);

		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPstselected(String Pstselected) {
		if (Pstselected == null || Pstselected.length() == 0) {
			return;
		}
		this.pstselected = new byte[Pstselected.length()];
		string2Bytes(Pstselected, this.pstselected);
	}

	public String getInteresttype() {

		try {
			return new String(interesttype);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setInteresttype(String Interesttype) {
		if (Interesttype == null || Interesttype.length() == 0) {
			return;
		}
		this.interesttype = new byte[Interesttype.length()];
		string2Bytes(Interesttype, this.interesttype);
	}

	public String getGraceperiod() {

		try {
			return new String(graceperiod);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setGraceperiod(String Graceperiod) {
		if (Graceperiod == null || Graceperiod.length() == 0) {
			return;
		}
		this.graceperiod = new byte[Graceperiod.length()];
		string2Bytes(Graceperiod, this.graceperiod);
	}

	public String getTerms() {

		try {
			return new String(terms);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTerms(String Terms) {
		if (Terms == null || Terms.length() == 0) {
			return;
		}
		this.terms = new byte[Terms.length()];
		string2Bytes(Terms, this.terms);
	}

	public String getCreateduserid() {

		try {
			return new String(createduserid);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCreateduserid(String Createduserid) {
		if (Createduserid == null || Createduserid.length() == 0) {
			return;
		}
		this.createduserid = new byte[Createduserid.length()];
		string2Bytes(Createduserid, this.createduserid);
	}

	public String getScanselected() {

		try {
			return new String(scanselected);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setScanselected(String Scanselected) {
		if (Scanselected == null || Scanselected.length() == 0) {
			return;
		}
		this.scanselected = new byte[Scanselected.length()];
		string2Bytes(Scanselected, this.scanselected);
	}

	public String getInvoicetemplate() {

		try {
			return new String(invoicetemplate);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setInvoicetemplate(String Invoicetemplate) {
		if (Invoicetemplate == null || Invoicetemplate.length() == 0) {
			return;
		}
		this.invoicetemplate = new byte[Invoicetemplate.length()];
		string2Bytes(Invoicetemplate, this.invoicetemplate);
	}

	public String getPosprint() {

		try {
			return new String(posprint);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPosprint(String Posprint) {
		if (Posprint == null || Posprint.length() == 0) {
			return;
		}
		this.posprint = new byte[Posprint.length()];
		string2Bytes(Posprint, this.posprint);
	}

	public String getWebsite() {

		try {
			return new String(website);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setWebsite(String Website) {
		if (Website == null || Website.length() == 0) {
			return;
		}
		this.website = new byte[Website.length()];
		string2Bytes(Website, this.website);
	}

	public double getInterestrate() {
		return interestrate;
	}

	public void setInterestrate(double interestrate) {
		this.interestrate = interestrate;
	}

	public double getDeliverycharge() {
		return deliverycharge;
	}

	public void setDeliverycharge(double deliverycharge) {
		this.deliverycharge = deliverycharge;
	}

	public Date getInterestexecuteddate() {
		return interestexecuteddate;
	}

	public void setInterestexecuteddate(Date interestexecuteddate) {
		this.interestexecuteddate = interestexecuteddate;
	}

}
