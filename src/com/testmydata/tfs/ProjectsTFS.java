package com.testmydata.tfs;

import java.util.ArrayList;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.workitem.category.Category;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.testmydata.dao.TFSAccess;
import com.testmydata.tfs.jira.binarybeans.ProjectListBean;

public class ProjectsTFS {

	public ArrayList<ProjectListBean> gettfsprojects() {
		ArrayList<ProjectListBean> plist = new ArrayList<ProjectListBean>();
		final TFSTeamProjectCollection tpc = TFSAccess.connectToTFS();
		tpc.authenticate();

		for (final Project project : tpc.getWorkItemClient().getProjects()) {
			ProjectListBean pl = new ProjectListBean();
			pl.setProjectid(Integer.toString(project.getID()));
			pl.setProjectname(project.getName());

			// System.out.println(project.getID() + " " + project.getName());
			plist.add(pl);
		}

		tpc.close();
		return plist;
	}

	public void getCategorylist() {
		final TFSTeamProjectCollection tpc = TFSAccess.connectToTFS();
		tpc.authenticate();
		final Project project = tpc.getWorkItemClient().getProjects().get("BusinessStore_Desktop");

		// Enumerate the work item categories for this project.
		for (final Category category : project.getCategories()) {
			System.out.println(category.getName());
		}
		tpc.close();
	}

}
