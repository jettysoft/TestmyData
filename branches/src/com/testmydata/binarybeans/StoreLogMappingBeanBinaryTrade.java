package com.testmydata.binarybeans;

public class StoreLogMappingBeanBinaryTrade extends BinaryTrade {

	private byte[] userId;
	private byte[] role;
	private byte[] description;
	private byte[] moduleName;
	private Boolean statusOfOperation;
	private byte[] oldValOfManagedAttr;
	private byte[] newValOfManagedAttr;

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

	public String getRole() {
		try {
			return new String(role);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setRole(String Role) {
		if (Role == null || Role.length() == 0) {
			return;
		}
		this.role = new byte[Role.length()];
		string2Bytes(Role, this.role);
	}

	public String getDescription() {
		try {
			return new String(description);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setDescription(String Description) {
		if (Description == null || Description.length() == 0) {
			return;
		}
		this.description = new byte[Description.length()];
		string2Bytes(Description, this.description);
	}

	public String getModuleName() {
		try {
			return new String(moduleName);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setModuleName(String ModuleName) {
		if (ModuleName == null || ModuleName.length() == 0) {
			return;
		}
		this.moduleName = new byte[ModuleName.length()];
		string2Bytes(ModuleName, this.moduleName);
	}

	public String getOldValOfManagedAttr() {
		try {
			return new String(oldValOfManagedAttr);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setOldValOfManagedAttr(String OldValOfManagedAttr) {
		if (OldValOfManagedAttr == null || OldValOfManagedAttr.length() == 0) {
			return;
		}
		this.oldValOfManagedAttr = new byte[OldValOfManagedAttr.length()];
		string2Bytes(OldValOfManagedAttr, this.oldValOfManagedAttr);
	}

	public String getNewValOfManagedAttr() {
		try {
			return new String(newValOfManagedAttr);
		} catch (NullPointerException ne) {
			return null;
		}
	}

	public void setNewValOfManagedAttr(String NewValOfManagedAttr) {
		if (NewValOfManagedAttr == null || NewValOfManagedAttr.length() == 0) {
			return;
		}
		this.newValOfManagedAttr = new byte[NewValOfManagedAttr.length()];
		string2Bytes(NewValOfManagedAttr, this.newValOfManagedAttr);
	}

	public void setStatusOfOperation(Boolean statusOfOperation) {
		this.statusOfOperation = statusOfOperation;
	}

	public Boolean getStatusOfOperation() {
		return statusOfOperation;
	}

}
