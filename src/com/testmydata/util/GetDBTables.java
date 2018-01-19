package com.testmydata.util;

import java.util.ArrayList;

import com.testmydata.binarybeans.ControlReportHelperBinaryTrade;
import com.testmydata.dao.DAO;

public class GetDBTables {

	public static ArrayList<ControlReportHelperBinaryTrade> dblist = new ArrayList<ControlReportHelperBinaryTrade>();
	public static ArrayList<ControlReportHelperBinaryTrade> tablelist = new ArrayList<ControlReportHelperBinaryTrade>();
	public static ArrayList<ControlReportHelperBinaryTrade> columnlist = new ArrayList<ControlReportHelperBinaryTrade>();

	public void refesh() {
		getdblist();
	}

	public static ArrayList<ControlReportHelperBinaryTrade> getdblist() {
		if (dblist != null) {
			dblist.clear();
		}
		dblist = new DAO().getQAServerDB();
		return dblist;
	}

	public static ArrayList<ControlReportHelperBinaryTrade> gettablelist(String selecteddb) {
		if (tablelist != null) {
			tablelist.clear();
		}
		tablelist = new DAO().getQAServerTables(selecteddb);
		return tablelist;
	}

	public static ArrayList<ControlReportHelperBinaryTrade> getcolumnlist(String table, String db) {
		if (columnlist != null) {
			columnlist.clear();
		}
		columnlist = new DAO().getQAServerColumns(table, db);
		return columnlist;
	}
}
