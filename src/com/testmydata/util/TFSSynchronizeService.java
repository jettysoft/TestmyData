package com.testmydata.util;

import java.util.ArrayList;

import com.testmydata.dao.DAO;
import com.testmydata.fxcontroller.InvoiceStaticHelper;
import com.testmydata.tfs.CreateBugTFS;
import com.testmydata.tfs.jira.binarybeans.BugFieldsBeans;

import javafx.concurrent.Task;

public class TFSSynchronizeService {
	static int batchsize = 0, noofthreads = 0;
	static ArrayList<BugFieldsBeans> idlist = new ArrayList<BugFieldsBeans>();
	static ArrayList<BugFieldsBeans> bugdatalist = new ArrayList<BugFieldsBeans>();
	public static String source = null;

	public static void startsync() {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {

				if (idlist != null && idlist.size() > 0) {
					idlist.clear();
				}
				idlist = new DAO().getbugidlist("TFS", DefaultBugServerDetails.projectname,
						DefaultBugServerDetails.serverid, Loggedinuserdetails.defaultproject);
				if (idlist != null && idlist.size() > 0) {

					batchsize = 50;
					noofthreads = idlist.size() / batchsize;
					Thread[] threads = new Thread[noofthreads + 1];

					for (int i = 0; i < threads.length; i++) {

						final int startNumber = (i * batchsize) + 1;

						int number = 0;
						if (idlist.size() >= 50) {
							number = (batchsize + startNumber) - 1;
						} else {
							number = idlist.size();
						}

						final int endNumber = number;

						threads[i] = new Thread(new Runnable() {
							@Override
							public void run() {
								getUpdate(startNumber, endNumber, idlist);
							}
						});
						threads[i].start();
					}

					do {
						checkthreads(threads);
					} while (checkthreads(threads));

					if (!checkthreads(threads)) { // UpdatePostActions
						if (source != null && source.equals("buglist")) {
							InvoiceStaticHelper.vblc.refreshpostactions();
						}

					}
				}
				return null;
			}
		};

		Thread releasethread = new Thread(task);
		releasethread.setDaemon(true);
		releasethread.start();
	}

	private static void getUpdate(int start, int end, ArrayList<BugFieldsBeans> list) {
		for (int i = start; i <= end; i++) {

			bugdatalist = CreateBugTFS.getBugFields(Integer.parseInt(list.get(i - 1).getBugid()));
			if (bugdatalist != null && bugdatalist.size() > 0) {
				new DAO().updatebugcopy(DefaultBugServerDetails.serverid, "0", "0", bugdatalist.get(0).getTitle(),
						bugdatalist.get(0).getAssignedto(), bugdatalist.get(0).getState(),
						bugdatalist.get(0).getReason(), bugdatalist.get(0).getArea(),
						bugdatalist.get(0).getReprosteps(), Loggedinuserdetails.id, Loggedinuserdetails.defaultproject,
						bugdatalist.get(0).getBugid(), bugdatalist.get(0).getIterationpath(), "0", "service");
			}
		}
	}

	private static boolean checkthreads(Thread[] threads) {
		int countmoudlethreadsalive = 0;
		for (int j = 0; j < threads.length; j++) {
			if (threads[j].isAlive()) {
				countmoudlethreadsalive++;
			} else {
				countmoudlethreadsalive--;
			}
		}
		if (countmoudlethreadsalive <= 0) {
			return false;
		} else {
			return true;
		}
	}
}
