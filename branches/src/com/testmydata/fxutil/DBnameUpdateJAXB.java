package com.testmydata.fxutil;

import com.testmydata.binarybeans.DBConfigBinaryTade;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.DBConfigJAXB;
import com.testmydata.util.EncryptAndDecrypt;

public class DBnameUpdateJAXB {
	public String decrypteddbanme;

	public void encrypt(String dbname) {
		try {
			DBConfigJAXB dbC = new DBConfigJAXB();
			DBConfigBinaryTade dbConfig = dbC.readDBConfig();
			String dbn = dbname.replaceAll(" ", "");
			String upToNCharacters = dbn.substring(0, Math.min(dbn.length(), 40));
			dbConfig.setDbName(upToNCharacters);

			dbC.generateDBConfig(dbConfig);
		} catch (Exception e1) {
			CommonFunctions.message = "DBConfiguration File is Missing, Please contact 'Test My Data' support...!";
			CommonFunctions.invokeAlertBox(getClass());
		}
	}

	public void decrypt() {
		try {
			DBConfigJAXB dbC = new DBConfigJAXB();
			DBConfigBinaryTade dbConfig = dbC.readDBConfig();
			decrypteddbanme = EncryptAndDecrypt.decryptData(dbConfig.getDbName());
		} catch (Exception e1) {
			CommonFunctions.message = "DBConfiguration File is Missing, Please contact 'Test My Data' support...!";
			CommonFunctions.invokeAlertBox(getClass());
		}
	}

}
