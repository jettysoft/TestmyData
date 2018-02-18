package com.testmydata.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.dao.TFSAccess;
import com.testmydata.tfs.IterationTFS;
import com.testmydata.tfs.UsersTFS;
import com.testmydata.tfs.jira.binarybeans.ProjectIterationBean;
import com.testmydata.tfs.jira.binarybeans.ProjectUsersBean;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;

public class LoadTFSorJiraUsers implements Initializable {

	public static ArrayList<ProjectUsersBean> tfsuserslist = new ArrayList<ProjectUsersBean>();
	public static ArrayList<ProjectIterationBean> tfsiterationlist = new ArrayList<ProjectIterationBean>();

	public static ArrayList<UsersDetailsBeanBinaryTrade> tmduserslist = new ArrayList<UsersDetailsBeanBinaryTrade>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadingservice.reset();
		loadingservice.start();
	}

	Service<Void> loadingservice = new Service<Void>() {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					// Background work
					if (DefaultBugServerDetails.servertype != null
							&& DefaultBugServerDetails.servertype.contains("TFS")) { // TFSUsers
						tfsuserslist = UsersTFS.gettfsprojectsusers(TFSAccess.PROJECT_NAME);
						tfsiterationlist = IterationTFS.getiterations(TFSAccess.PROJECT_NAME);
					} else if (DefaultBugServerDetails.servertype != null
							&& DefaultBugServerDetails.servertype.contains("JIRA")) {

					} else if (DefaultBugServerDetails.servertype != null
							&& DefaultBugServerDetails.servertype.contains("TMD")) {
						tmduserslist = new DAO().getuserlist();
					}
					// Keep with the background work
					return null;
				}
			};
		}
	};

}
