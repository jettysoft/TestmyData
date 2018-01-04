package com.testmydata.fxcontroller;

import com.testmydata.util.QADefaultServerDetails;

public class InvoiceStaticHelper {

	public static DashBoardController dash = null;
	public static NewControlReportRulesController newcrs = null;
	public static QADefaultServerDetails qasd = null;

	public static void setDash(DashBoardController dash) {
		InvoiceStaticHelper.dash = dash;
	}

	public static void setnewcontrol(NewControlReportRulesController newcr) {
		InvoiceStaticHelper.newcrs = newcr;
	}

	public static void setqasd(QADefaultServerDetails qas) {
		InvoiceStaticHelper.qasd = qas;
	}

}
