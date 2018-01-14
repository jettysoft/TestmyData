package com.testmydata.tfs;

import java.util.ArrayList;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.workitem.node.NodeCollection;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.testmydata.dao.TFSAccess;
import com.testmydata.tfs.jira.binarybeans.ProjectIterationBean;

public class IterationTFS {

	public ArrayList<ProjectIterationBean> getiterations(String projectname) {
		ArrayList<ProjectIterationBean> plist = new ArrayList<ProjectIterationBean>();
		final TFSTeamProjectCollection tpc = TFSAccess.connectToTFS();
		tpc.authenticate();
		final Project project = tpc.getWorkItemClient().getProjects().get(projectname);
		NodeCollection nodes = project.getIterationRootNodes();

		for (int i = 0; i < nodes.size(); i++) {
			System.out.println(nodes.getNodes());
		}

		tpc.close();
		return plist;
	}
}
