package com.testmydata.tfs;

import java.util.ArrayList;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.webservices.IIdentityManagementService2;
import com.microsoft.tfs.core.clients.webservices.IdentityDescriptor;
import com.microsoft.tfs.core.clients.webservices.IdentitySearchFactor;
import com.microsoft.tfs.core.clients.webservices.MembershipQuery;
import com.microsoft.tfs.core.clients.webservices.ReadIdentityOptions;
import com.microsoft.tfs.core.clients.webservices.TeamFoundationIdentity;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.config.client.DefaultClientFactory;
import com.testmydata.dao.TFSAccess;
import com.testmydata.tfs.jira.binarybeans.ProjectUsersBean;

public class UsersTFS {

	public static ArrayList<ProjectUsersBean> gettfsprojectsusers(String projectname) {
		ArrayList<ProjectUsersBean> pulist = new ArrayList<ProjectUsersBean>();
		final TFSTeamProjectCollection tpc = TFSAccess.connectToTFS();
		tpc.authenticate();

		final Project project = tpc.getWorkItemClient().getProjects().get(projectname);
		if (project != null) {

			final DefaultClientFactory factory = new DefaultClientFactory();
			final IIdentityManagementService2 ims = (IIdentityManagementService2) factory
					.newClient(IIdentityManagementService2.class, tpc);
			// final String scopeId = null;
			// final String[] propertyNameFilters = null;
			TeamFoundationIdentity[] appGroups = ims.listApplicationGroups(project.getURI(),
					ReadIdentityOptions.EXTENDED_PROPERTIES);
			// String patternToMatch =
			// "[\\\\!\"#$%&()*+,./:;<=>?@\\[\\]^_{|}~]+";
			for (int i = 0; i < appGroups.length; i++) {
				// System.out.println(
				// appGroups[i].getDisplayName().replaceAll(projectname,
				// "").replaceAll(patternToMatch, ""));
			}
			for (TeamFoundationIdentity group : appGroups) {
				// IdentityDescriptor[] mbs = group.getMembers();
				// System.out.println(group.getUniqueName());

				// IdentityManagementService identityManagementService = new
				// IdentityManagementService(tpc);

				TeamFoundationIdentity[][] groupMembers = ims.readIdentities(IdentitySearchFactor.ACCOUNT_NAME,
						new String[] { group.getDisplayName() }, MembershipQuery.EXPANDED,
						ReadIdentityOptions.EXTENDED_PROPERTIES);
				for (TeamFoundationIdentity[] member : groupMembers) {
					for (TeamFoundationIdentity m : member) {
						for (IdentityDescriptor mi : m.getMembers()) {
							TeamFoundationIdentity memberInfo = ims.readIdentity(IdentitySearchFactor.IDENTIFIER,
									mi.getIdentifier(), MembershipQuery.EXPANDED,
									ReadIdentityOptions.EXTENDED_PROPERTIES);
							ProjectUsersBean pusers = new ProjectUsersBean();
							pusers.setUsername(memberInfo.getProperty("Account").toString());
							// System.out.println(memberInfo.getProperty("Account"));
							int exist = 0;
							if (pulist != null && pulist.size() > 0) {
								for (int i = 0; i < pulist.size(); i++) {
									if (pulist.get(i).getUsername()
											.contains(memberInfo.getProperty("Account").toString())) {
										exist++;
									}
								}
							}
							if (exist == 0) {
								pulist.add(pusers);
								exist = 0;
							}

						}
					}

				}

			}
		}

		tpc.close();
		// pulist = new ArrayList<ProjectUsersBean>(new
		// LinkedHashSet<ProjectUsersBean>(pulist));
		return pulist;
	}
}
