package com.testmydata.binarybeans;

public class UsersDetailsBeanBinaryTrade extends BinaryTrade {
	private long id;
	private byte[] loginStatus;
	private byte[] companyName;
	private byte[] firstName;
	private byte[] lastName;
	private byte[] userId;
	private byte[] password;
	private byte[] emailId;
	private byte[] securityQuestion;
	private byte[] answer;
	private byte[] businessAddress;
	private byte[] cityprovince;
	private byte[] postalCode;
	private byte[] mainService;
	private byte[] province;
	private byte[] doorno;
	private byte[] streetname;
	private byte[] country;
	private byte[] createddate;
	private byte[] industry;
	private byte[] logoname;
	private byte[] userrole;
	private byte[] email;
	private byte[] newcr;
	private byte[] newff;
	private byte[] newts;
	private byte[] crexe;
	private byte[] tsexe;
	private byte[] adduser;
	private byte[] addqa;
	private byte[] dashboard;
	private byte[] combinedname;
	private byte[] reports;
	private byte[] testresults;
	private byte[] newbug;
	private byte[] viewbug;
	private byte[] addbugserver;
	private byte[] projectaccess;
	private byte[] selectedproject;
	private byte[] isactive;

	public String getLoginStatus() {
		try {
			return new String(loginStatus);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLoginStatus(String LoginStatus) {
		if (LoginStatus == null || LoginStatus.length() == 0) {
			return;
		}
		this.loginStatus = new byte[LoginStatus.length()];
		string2Bytes(LoginStatus, this.loginStatus);
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

	public String getFirstName() {
		try {
			return new String(firstName);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setFirstName(String FirstName) {
		if (FirstName == null || FirstName.length() == 0) {
			return;
		}
		this.firstName = new byte[FirstName.length()];
		string2Bytes(FirstName, this.firstName);
	}

	public String getLastName() {
		try {
			return new String(lastName);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLastName(String LastName) {
		if (LastName == null || LastName.length() == 0) {
			return;
		}
		this.lastName = new byte[LastName.length()];
		string2Bytes(LastName, this.lastName);
	}

	public String getUserId() {
		try {
			return new String(userId);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUserId(String UserId) {
		if (UserId == null || UserId.length() == 0) {
			return;
		}
		this.userId = new byte[UserId.length()];
		string2Bytes(UserId, this.userId);
	}

	public String getPassword() {
		try {
			return new String(password);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPassword(String Password) {
		if (Password == null || Password.length() == 0) {
			return;
		}
		this.password = new byte[Password.length()];
		string2Bytes(Password, this.password);
	}

	public String getEmailId() {
		try {
			return new String(emailId);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setEmailId(String EmailId) {
		if (EmailId == null || EmailId.length() == 0) {
			return;
		}
		this.emailId = new byte[EmailId.length()];
		string2Bytes(EmailId, this.emailId);
	}

	public String getSecurityQuestion() {
		try {
			return new String(securityQuestion);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSecurityQuestion(String SecurityQuestion) {
		if (SecurityQuestion == null || SecurityQuestion.length() == 0) {
			return;
		}
		this.securityQuestion = new byte[SecurityQuestion.length()];
		string2Bytes(SecurityQuestion, this.securityQuestion);
	}

	public String getAnswer() {
		try {
			return new String(answer);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setAnswer(String Answer) {
		if (Answer == null || Answer.length() == 0) {
			return;
		}
		this.answer = new byte[Answer.length()];
		string2Bytes(Answer, this.answer);
	}

	public String getBusinessAddress() {
		try {
			return new String(businessAddress);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setBusinessAddress(String BusinessAddress) {
		if (BusinessAddress == null || BusinessAddress.length() == 0) {
			return;
		}
		this.businessAddress = new byte[BusinessAddress.length()];
		string2Bytes(BusinessAddress, this.businessAddress);
	}

	public String getCityprovince() {
		try {
			return new String(cityprovince);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCityprovince(String Cityprovince) {
		if (Cityprovince == null || Cityprovince.length() == 0) {
			return;
		}
		this.cityprovince = new byte[Cityprovince.length()];
		string2Bytes(Cityprovince, this.cityprovince);
	}

	public String getPostalCode() {
		try {
			return new String(postalCode);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setPostalCode(String PostalCode) {
		if (PostalCode == null || PostalCode.length() == 0) {
			return;
		}
		this.postalCode = new byte[PostalCode.length()];
		string2Bytes(PostalCode, this.postalCode);
	}

	public String getMainService() {
		try {
			return new String(mainService);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setMainService(String MainService) {
		if (MainService == null || MainService.length() == 0) {
			return;
		}
		this.mainService = new byte[MainService.length()];
		string2Bytes(MainService, this.mainService);
	}

	public String getProvince() {
		try {
			return new String(province);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setProvince(String Province) {
		if (Province == null || Province.length() == 0) {
			return;
		}
		this.province = new byte[Province.length()];
		string2Bytes(Province, this.province);
	}

	public String getDoorno() {
		try {
			return new String(doorno);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setDoorno(String Doorno) {
		if (Doorno == null || Doorno.length() == 0) {
			return;
		}
		this.doorno = new byte[Doorno.length()];
		string2Bytes(Doorno, this.doorno);
	}

	public String getStreetname() {
		try {
			return new String(streetname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setStreetname(String Streetname) {
		if (Streetname == null || Streetname.length() == 0) {
			return;
		}
		this.streetname = new byte[Streetname.length()];
		string2Bytes(Streetname, this.streetname);
	}

	public String getCountry() {
		try {
			return new String(country);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCountry(String Country) {
		if (Country == null || Country.length() == 0) {
			return;
		}
		this.country = new byte[Country.length()];
		string2Bytes(Country, this.country);
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

	public String getIndustry() {
		try {
			return new String(industry);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setIndustry(String Industry) {
		if (Industry == null || Industry.length() == 0) {
			return;
		}
		this.industry = new byte[Industry.length()];
		string2Bytes(Industry, this.industry);
	}

	public String getLogoname() {
		try {
			return new String(logoname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setLogoname(String logoname) {
		if (logoname == null || logoname.length() == 0) {
			return;
		}
		this.logoname = new byte[logoname.length()];
		string2Bytes(logoname, this.logoname);
	}

	public String getUserrole() {
		try {
			return new String(userrole);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setUserrole(String Userrole) {
		if (Userrole == null || Userrole.length() == 0) {
			return;
		}
		this.userrole = new byte[Userrole.length()];
		string2Bytes(Userrole, this.userrole);
	}

	public String getEmail() {
		try {
			return new String(email);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setEmail(String Email) {
		if (Email == null || Email.length() == 0) {
			return;
		}
		this.email = new byte[Email.length()];
		string2Bytes(Email, this.email);
	}

	public String getNewcr() {
		try {
			return new String(newcr);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setNewcr(String Newcr) {
		if (Newcr == null || Newcr.length() == 0) {
			return;
		}
		this.newcr = new byte[Newcr.length()];
		string2Bytes(Newcr, this.newcr);
	}

	public String getNewff() {
		try {
			return new String(newff);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setNewff(String Newff) {
		if (Newff == null || Newff.length() == 0) {
			return;
		}
		this.newff = new byte[Newff.length()];
		string2Bytes(Newff, this.newff);
	}

	public String getNewts() {
		try {
			return new String(newts);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setNewts(String Newts) {
		if (Newts == null || Newts.length() == 0) {
			return;
		}
		this.newts = new byte[Newts.length()];
		string2Bytes(Newts, this.newts);
	}

	public String getCrexe() {
		try {
			return new String(crexe);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCrexe(String Crexe) {
		if (Crexe == null || Crexe.length() == 0) {
			return;
		}
		this.crexe = new byte[Crexe.length()];
		string2Bytes(Crexe, this.crexe);
	}

	public String getTsexe() {
		try {
			return new String(tsexe);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTsexe(String Tsexe) {
		if (Tsexe == null || Tsexe.length() == 0) {
			return;
		}
		this.tsexe = new byte[Tsexe.length()];
		string2Bytes(Tsexe, this.tsexe);
	}

	public String getAdduser() {
		try {
			return new String(adduser);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setAdduser(String Adduser) {
		if (Adduser == null || Adduser.length() == 0) {
			return;
		}
		this.adduser = new byte[Adduser.length()];
		string2Bytes(Adduser, this.adduser);
	}

	public String getAddqa() {
		try {
			return new String(addqa);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setAddqa(String Addqa) {
		if (Addqa == null || Addqa.length() == 0) {
			return;
		}
		this.addqa = new byte[Addqa.length()];
		string2Bytes(Addqa, this.addqa);
	}

	public String getDashboard() {
		try {
			return new String(dashboard);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setDashboard(String Dashboard) {
		if (Dashboard == null || Dashboard.length() == 0) {
			return;
		}
		this.dashboard = new byte[Dashboard.length()];
		string2Bytes(Dashboard, this.dashboard);
	}

	public String getCombinedname() {
		try {
			return new String(combinedname);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setCombinedname(String Combinedname) {
		if (Combinedname == null || Combinedname.length() == 0) {
			return;
		}
		this.combinedname = new byte[Combinedname.length()];
		string2Bytes(Combinedname, this.combinedname);
	}

	public String getIsactive() {
		try {
			return new String(isactive);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setIsactive(String Isactive) {
		if (Isactive == null || Isactive.length() == 0) {
			return;
		}
		this.isactive = new byte[Isactive.length()];
		string2Bytes(Isactive, this.isactive);
	}

	public String getReports() {
		try {
			return new String(reports);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setReports(String Reports) {
		if (Reports == null || Reports.length() == 0) {
			return;
		}
		this.reports = new byte[Reports.length()];
		string2Bytes(Reports, this.reports);
	}

	public String getTestresults() {
		try {
			return new String(testresults);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setTestresults(String Testresults) {
		if (Testresults == null || Testresults.length() == 0) {
			return;
		}
		this.testresults = new byte[Testresults.length()];
		string2Bytes(Testresults, this.testresults);
	}

	public String getNewbug() {
		try {
			return new String(newbug);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setNewbug(String Newbug) {
		if (Newbug == null || Newbug.length() == 0) {
			return;
		}
		this.newbug = new byte[Newbug.length()];
		string2Bytes(Newbug, this.newbug);
	}

	public String getViewbug() {
		try {
			return new String(viewbug);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setViewbug(String Viewbug) {
		if (Viewbug == null || Viewbug.length() == 0) {
			return;
		}
		this.viewbug = new byte[Viewbug.length()];
		string2Bytes(Viewbug, this.viewbug);
	}

	public String getAddbugserver() {
		try {
			return new String(addbugserver);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setAddbugserver(String Addbugserver) {
		if (Addbugserver == null || Addbugserver.length() == 0) {
			return;
		}
		this.addbugserver = new byte[Addbugserver.length()];
		string2Bytes(Addbugserver, this.addbugserver);
	}

	public String getProjectaccess() {
		try {
			return new String(projectaccess);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setProjectaccess(String Projectaccess) {
		if (Projectaccess == null || Projectaccess.length() == 0) {
			return;
		}
		this.projectaccess = new byte[Projectaccess.length()];
		string2Bytes(Projectaccess, this.projectaccess);
	}

	public String getSelectedproject() {
		try {
			return new String(selectedproject);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setSelectedproject(String Selectedproject) {
		if (Selectedproject == null || Selectedproject.length() == 0) {
			return;
		}
		this.selectedproject = new byte[Selectedproject.length()];
		string2Bytes(Selectedproject, this.selectedproject);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
