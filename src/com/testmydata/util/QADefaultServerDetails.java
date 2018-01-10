package com.testmydata.util;

import java.util.ArrayList;

import com.testmydata.binarybeans.QAServerDetailsBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxcontroller.InvoiceStaticHelper;

public class QADefaultServerDetails {

	public static String url = null, username = null, password = null, classname = null, servertype = null;
	public static int id = 0;
	ArrayList<QAServerDetailsBinaryTrade> qaserverlist = new ArrayList<QAServerDetailsBinaryTrade>();

	public QADefaultServerDetails() {
		InvoiceStaticHelper.setqasd(this);
	}

	public void setqadefaultserver() {
		if (qaserverlist != null) {
			restfields();
			qaserverlist.clear();
		}
		id = new DAO().getdefaultserver();

		if (id > 0) {
			qaserverlist = new DAO().getserverdetailsbyid(id);
			if (qaserverlist != null && qaserverlist.size() > 0) {
				url = qaserverlist.get(0).getDburl();
				username = EncryptAndDecrypt.decryptData(qaserverlist.get(0).getUsername());
				password = EncryptAndDecrypt.decryptData(qaserverlist.get(0).getPassword());
				classname = qaserverlist.get(0).getClassname();
				servertype = qaserverlist.get(0).getServertype();
			}
		}
	}

	private void restfields() {
		url = "";
		username = "";
		password = "";
		classname = "";
		servertype = "";
		id = 0;
	}
}
