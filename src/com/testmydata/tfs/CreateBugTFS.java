package com.testmydata.tfs;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.workitem.CoreFieldReferenceNames;
import com.microsoft.tfs.core.clients.workitem.NonCoreFieldsReferenceNames;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.WorkItemClient;
import com.microsoft.tfs.core.clients.workitem.files.Attachment;
import com.microsoft.tfs.core.clients.workitem.files.AttachmentCollection;
import com.microsoft.tfs.core.clients.workitem.files.AttachmentFactory;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.clients.workitem.query.WorkItemCollection;
import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemType;
//import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemType;
import com.testmydata.dao.TFSAccess;
import com.testmydata.tfs.jira.binarybeans.BugAttachmentsBeans;
import com.testmydata.tfs.jira.binarybeans.BugFieldsBeans;

public class CreateBugTFS {
	public static void main(final String[] args) {
		// CreateBugTFS cfs = new CreateBugTFS();
		// cfs.createTFSBug();
		// cfs.addTFSattachment(new File(
		// new
		// File("C:/Users/jithendra.jetty/workspace/TestMyData/src/com/testmydata/fximages/testsuites.png")
		// .getAbsolutePath()).getAbsoluteFile(),
		// 30);
		// cfs.testgetmethod();
		// cfs.test();

		// ProjectsTFS pfs = new ProjectsTFS();
		//
		// for (int i = 0; i < pfs.gettfsprojects().size(); i++) {
		// //
		// UsersTFS.gettfsprojectsusers(pfs.gettfsprojects().get(i).getProjectname());
		// UsersTFS.gettfsprojectsusers("Parmzpizza_Redesign");
		// }

		// IterationTFS.getiterations("Parmzpizza_Redesign");
		// ProjectsTFS pts = new ProjectsTFS();
		// pts.getCategorylist();
		// pts.gettfsprojects();
	}

	public void test() {
		final TFSTeamProjectCollection tpc = TFSAccess.connectToTFS();

		final Project project = tpc.getWorkItemClient().getProjects().get(TFSAccess.PROJECT_NAME);
		final WorkItemClient workItemClient = project.getWorkItemClient();

		// Define the WIQL query.
		final String wiqlQuery = "Select ID, Title from WorkItems where (System.AreaPath under 'Parmzpizza_Redesign') and (System.WorkItemType = 'Test Case') order by Title"; //$NON-NLS-1$

		// Run the query and get the results.
		final WorkItemCollection workItems = workItemClient.query(wiqlQuery);
		System.out.println("Found " + workItems.size() + " work items."); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println();

		// Write out the heading.
		System.out.println("Query: " + wiqlQuery); //$NON-NLS-1$
		System.out.println();
		System.out.println("ID\tTitle"); //$NON-NLS-1$

		// Output the results of the query.
		final int maxToPrint = 20;
		for (int i = 0; i < workItems.size(); i++) {
			if (i >= maxToPrint) {
				System.out.println("[...]"); //$NON-NLS-1$
				break;
			}

			final WorkItem workItem = workItems.getWorkItem(i);
			System.out.println(workItem.getID() + "\t" + workItem.getTitle()); //$NON-NLS-1$
		}
		tpc.close();
	}

	public void testgetmethod() {
		ArrayList<BugFieldsBeans> buglist = new ArrayList<BugFieldsBeans>();
		buglist = getBugFields(41);
		System.out.println(buglist.get(0).getTitle());
		// System.out.println(buglist.get(0).getReprosteps().replaceAll("<br>",
		// "\n"));
		System.out.println("State : " + buglist.get(0).getState());
	}

	public static int createTFSBug(String title, String assignedto, String state, String reason, String areapath,
			String reprosteps, String createduser, String iterationpath) {
		int newbugid = 0;
		try {
			final TFSTeamProjectCollection tpc = TFSAccess.connectToTFS();
			tpc.authenticate();

			final Project project = tpc.getWorkItemClient().getProjects().get(TFSAccess.PROJECT_NAME);

			// Find the work item type matching the specified name.
			final WorkItemType bugWorkItemType = project.getWorkItemTypes().get("Bug"); //$NON-NLS-1$

			// Create a new work item of the specified type.
			final WorkItem newWorkItem = project.getWorkItemClient().newWorkItem(bugWorkItemType);
			// final WorkItem newWorkItem =
			// project.getWorkItemClient().getWorkItemByID(30);

			// Set the title on the work item.
			newWorkItem.setTitle(title); // $NON-NLS-1$
			newWorkItem.getFields().getField(CoreFieldReferenceNames.ASSIGNED_TO).setValue(assignedto);
			newWorkItem.getFields().getField(CoreFieldReferenceNames.STATE).setValue(state);
			newWorkItem.getFields().getField(CoreFieldReferenceNames.REASON).setValue(reason);
			newWorkItem.getFields().getField(CoreFieldReferenceNames.AREA_PATH).setValue(areapath);
			newWorkItem.getFields().getField(CoreFieldReferenceNames.ITERATION_PATH).setValue(iterationpath);
			newWorkItem.getFields().getField(CoreFieldReferenceNames.HISTORY)
					.setValue("<p>Created from TestMyData by " + createduser + "</p>"); //$NON-NLS-1$
			newWorkItem.getFields().getField(NonCoreFieldsReferenceNames.REPRO_STEPS).setValue(reprosteps);
			// newWorkItem.getFields().getField("Activity").setValue("Testing");

			// Enumerate the work item types for this project.
			// for (final FieldDefinition fieldDefinition :
			// bugWorkItemType.getFieldDefinitions()) {
			// System.out.println(fieldDefinition.getFieldType().getValue() +
			// "[" + fieldDefinition.getName() + "]"); //$NON-NLS-1$
			// // $NON-NLS-2$
			// }

			// final WorkItemClient workItemClient =
			// project.getWorkItemClient();
			// final WorkItemCollection workItems = workItemClient.query(
			// "Select Id from
			// Tfs_JettySoftCollection.dbo.tbl_WorkItemCoreLatest where
			// (WorkItemType = 'Bug')");
			// for (int i = 0; i < workItems.size(); i++) {
			// final WorkItem workItem = workItems.getWorkItem(i);
			// System.out.println(workItem.getID() + "\t" +
			// workItem.getTitle()); //$NON-NLS-1$
			// }

			// Save the new work item to the server.
			newWorkItem.save();

			// System.out.println("Work item " + newWorkItem.getID() + "
			// successfully created"); //$NON-NLS-1$ //$NON-NLS-2$

			newbugid = newWorkItem.getID();
			tpc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newbugid;
	}

	public static int updateTFSBug(int bugid, String title, String assignedto, String state, String reason,
			String areapath, String reprosteps, String updateduser, String iterationpath) {
		try {
			final TFSTeamProjectCollection tpc = TFSAccess.connectToTFS();
			tpc.authenticate();

			final Project project = tpc.getWorkItemClient().getProjects().get(TFSAccess.PROJECT_NAME);

			final WorkItem newWorkItem = project.getWorkItemClient().getWorkItemByID(bugid);

			// Set the title on the work item.
			newWorkItem.setTitle(title); // $NON-NLS-1$
			newWorkItem.getFields().getField(CoreFieldReferenceNames.ASSIGNED_TO).setValue(assignedto);
			newWorkItem.getFields().getField(CoreFieldReferenceNames.STATE).setValue(state);
			newWorkItem.getFields().getField(CoreFieldReferenceNames.REASON).setValue(reason);
			newWorkItem.getFields().getField(CoreFieldReferenceNames.AREA_PATH).setValue(areapath);
			newWorkItem.getFields().getField(CoreFieldReferenceNames.HISTORY)
					.setValue("<p>Updated from TestMyData by " + updateduser + "</p>"); //$NON-NLS-1$
			newWorkItem.getFields().getField(NonCoreFieldsReferenceNames.REPRO_STEPS).setValue(reprosteps);
			newWorkItem.getFields().getField(CoreFieldReferenceNames.ITERATION_PATH).setValue(iterationpath);

			// Save the new work item to the server.
			newWorkItem.save();

			tpc.close();
		} catch (Exception e) {
			bugid = 0;
			e.printStackTrace();
		}

		return bugid;
	}

	public static ArrayList<BugFieldsBeans> getBugFields(int bugid) {
		ArrayList<BugFieldsBeans> bugdetails = new ArrayList<BugFieldsBeans>();
		try {
			final TFSTeamProjectCollection tpc = TFSAccess.connectToTFS();
			tpc.authenticate();

			final Project project = tpc.getWorkItemClient().getProjects().get(TFSAccess.PROJECT_NAME);

			final WorkItem newWorkItem = project.getWorkItemClient().getWorkItemByID(bugid);

			BugFieldsBeans bfb = new BugFieldsBeans();
			bfb.setTitle(newWorkItem.getTitle());
			bfb.setAssignedto(
					newWorkItem.getFields().getField(CoreFieldReferenceNames.ASSIGNED_TO).getValue().toString());
			bfb.setState(newWorkItem.getFields().getField(CoreFieldReferenceNames.STATE).getValue().toString());
			bfb.setReason(newWorkItem.getFields().getField(CoreFieldReferenceNames.REASON).getValue().toString());
			bfb.setArea(newWorkItem.getFields().getField(CoreFieldReferenceNames.AREA_PATH).getValue().toString());
			// bfb.setHistory(newWorkItem.getFields().getField(CoreFieldReferenceNames.HISTORY).getValue().toString());
			bfb.setReprosteps(newWorkItem.getFields().getField(NonCoreFieldsReferenceNames.REPRO_STEPS).getValue()
					.toString().replaceAll("<br>", "\n"));
			bfb.setIterationpath(
					newWorkItem.getFields().getField(CoreFieldReferenceNames.ITERATION_PATH).getValue().toString());
			// bfb.setBugattachmentsbeans(getTFSattachment(bugid));
			bfb.setBugid(newWorkItem.getFields().getField(CoreFieldReferenceNames.ID).getValue().toString());
			bugdetails.add(bfb);

			tpc.close();

			return bugdetails;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void addTFSattachment(File attachment, int bugid) {
		try {
			final TFSTeamProjectCollection tpc = TFSAccess.connectToTFS();
			tpc.authenticate();

			final Project project = tpc.getWorkItemClient().getProjects().get(TFSAccess.PROJECT_NAME);

			final WorkItem newWorkItem = project.getWorkItemClient().getWorkItemByID(bugid);
			Attachment attach = AttachmentFactory.newAttachment(attachment, "");
			newWorkItem.getAttachments().add(attach);

			// Save the new work item to the server.
			newWorkItem.save();

			tpc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BugAttachmentsBeans[] getTFSattachment(int bugid) {
		BugAttachmentsBeans[] bab = new BugAttachmentsBeans[0];
		try {
			final TFSTeamProjectCollection tpc = TFSAccess.connectToTFS();
			tpc.authenticate();

			final Project project = tpc.getWorkItemClient().getProjects().get(TFSAccess.PROJECT_NAME);

			final WorkItem newWorkItem = project.getWorkItemClient().getWorkItemByID(bugid);
			AttachmentCollection attach = newWorkItem.getAttachments();
			Iterator<Attachment> iterator = attach.iterator();

			bab = new BugAttachmentsBeans[attach.size()];
			for (int i = 0; i < attach.size(); i++) {
				BugAttachmentsBeans ba = new BugAttachmentsBeans();
				Attachment attach1 = iterator.next();
				File temp = new File("");
				attach1.downloadTo(temp);
				ba.setAttachment(temp);
				bab[i] = ba;
			}

			tpc.close();
			return bab;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
