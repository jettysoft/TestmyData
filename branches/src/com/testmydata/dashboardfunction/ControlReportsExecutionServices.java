package com.testmydata.dashboardfunction;

import java.util.ArrayList;

import com.jfoenix.controls.JFXListView;
import com.testmydata.binarybeans.ControlReportExecutionBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.QADefaultServerDetails;

import javafx.concurrent.Task;

public class ControlReportsExecutionServices {
	static int batchsize = 0, noofthreads = 0;

	ArrayList<ControlReportExecutionBinaryTrade> ruleidlist = new ArrayList<ControlReportExecutionBinaryTrade>();

	public void modulerun(String module, int batchid, JFXListView<Module> listModule, JFXListView<Rule> listData,
			Module m1) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (ruleidlist != null && ruleidlist.size() > 0) {
					ruleidlist.clear();
				}
				ruleidlist = new DAO().getruleidonly(module, 1);
				if (ruleidlist != null && ruleidlist.size() > 0) {

					batchsize = 20;
					noofthreads = ruleidlist.size() / batchsize;
					Thread[] threads = new Thread[noofthreads + 1];

					for (int i = 0; i < threads.length; i++) {

						final int startNumber = (i * batchsize) + 1;

						int number = 0;
						if (ruleidlist.size() >= 20) {
							number = (batchsize + startNumber) - 1;
						} else {
							number = ruleidlist.size();
						}

						final int endNumber = number;

						threads[i] = new Thread(new Runnable() {
							@Override
							public void run() {
								moduleexecution(startNumber, endNumber, batchid, ruleidlist);
							}
						});
						threads[i].start();
					}

					do {
						checkthreads(threads);
					} while (checkthreads(threads));

					if (!checkthreads(threads)) {
						m1.setIsExecute(false);
						for (int i = 0; i < m1.getRules().length; i++) {
							m1.getRules()[i].setIsExecute(false);
						}
					}

					calcpercentages(batchid, m1);

					listModule.refresh();
					listData.refresh();
				}
				return null;
			}
		};

		Thread releasethread = new Thread(task);
		releasethread.setDaemon(true);
		releasethread.start();
	}

	public void rulerun(String rulename, int batchid, JFXListView<Module> listModule, JFXListView<Rule> listData,
			Module m1, Rule r1) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (ruleidlist != null && ruleidlist.size() > 0) {
					ruleidlist.clear();
				}
				ruleidlist = new DAO().getruleidonly(rulename, 2);
				if (ruleidlist != null && ruleidlist.size() > 0) {

					batchsize = 20;
					noofthreads = ruleidlist.size() / batchsize;
					Thread[] threads = new Thread[noofthreads + 1];

					for (int i = 0; i < threads.length; i++) {

						final int startNumber = (i * batchsize) + 1;

						int number = 0;
						if (ruleidlist.size() >= 20) {
							number = (batchsize + startNumber) - 1;
						} else {
							number = ruleidlist.size();
						}

						final int endNumber = number;

						threads[i] = new Thread(new Runnable() {
							@Override
							public void run() {
								moduleexecution(startNumber, endNumber, batchid, ruleidlist);
							}
						});
						threads[i].start();
					}

					do {
						checkthreads(threads);
					} while (checkthreads(threads));

					if (!checkthreads(threads)) {
						r1.setIsExecute(false);
					}

					rulecalcpercentages(batchid, m1, r1);

					listModule.refresh();
					listData.refresh();
				}
				return null;
			}
		};

		Thread releasethread = new Thread(task);
		releasethread.setDaemon(true);
		releasethread.start();
	}

	private boolean checkthreads(Thread[] threads) {
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

	private void moduleexecution(int start, int end, int batchid, ArrayList<ControlReportExecutionBinaryTrade> list) {
		for (int i = start; i <= end; i++) {
			new DAO().executecrrules(Integer.parseInt(list.get(i - 1).getId()), batchid,
					Long.parseLong(Integer.toString(Loggedinuserdetails.id)), QADefaultServerDetails.id);
		}
	}

	private void calcpercentages(int batchid, Module m1) {
		ArrayList<Rule> rulelist = new DAO().getRulesPercentage("crresults", batchid);
		if (rulelist != null && rulelist.size() > 0) {
			int rule_sum_pass = 0;
			int rule_sum_fail = 0;

			for (int j = 0; j < m1.getRules().length; j++) {
				for (int k = 0; k < rulelist.size(); k++) {
					if (rulelist.get(k).getRulename().equals(m1.getRules()[j].getRulename())) {
						m1.getRules()[j].setPass(rulelist.get(k).getPass());
						m1.getRules()[j].setFail(rulelist.get(k).getFail());

						rule_sum_pass += m1.getRules()[j].getPass();
						rule_sum_fail += m1.getRules()[j].getFail();
					}
				}
			}
			m1.setPass(rule_sum_pass / m1.getRules().length);
			m1.setFail(rule_sum_fail / m1.getRules().length);
		}
	}

	private void rulecalcpercentages(int batchid, Module m1, Rule r1) {
		ArrayList<Rule> rulelist = new DAO().getRulesPercentage("crresults", batchid);
		if (rulelist != null && rulelist.size() > 0) {
			int rule_sum_pass = 0;
			int rule_sum_fail = 0;

			for (int k = 0; k < rulelist.size(); k++) {
				if (rulelist.get(k).getRulename().equals(r1.getRulename())) {
					r1.setPass(rulelist.get(k).getPass());
					r1.setFail(rulelist.get(k).getFail());
				}
			}

			for (int i = 0; i < m1.getRules().length; i++) {
				rule_sum_pass += m1.getRules()[i].getPass();
				rule_sum_fail += m1.getRules()[i].getFail();
			}

			m1.setPass(rule_sum_pass / m1.getRules().length);
			m1.setFail(rule_sum_fail / m1.getRules().length);
		}
	}
}
