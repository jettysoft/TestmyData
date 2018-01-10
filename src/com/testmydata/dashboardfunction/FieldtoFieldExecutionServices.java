package com.testmydata.dashboardfunction;

import java.util.ArrayList;

import com.jfoenix.controls.JFXListView;
import com.testmydata.binarybeans.FieldtoFieldBinaryTrade;
import com.testmydata.dao.DAO;
import com.testmydata.fxcontroller.ExecuteFieldtoFieldController;
import com.testmydata.fxcontroller.InvoiceStaticHelper;

import javafx.concurrent.Task;

public class FieldtoFieldExecutionServices {
	static int batchsize = 0, noofthreads = 0;

	ArrayList<FieldtoFieldBinaryTrade> releaselist = new ArrayList<FieldtoFieldBinaryTrade>();
	ArrayList<FieldtoFieldBinaryTrade> cyclelist = new ArrayList<FieldtoFieldBinaryTrade>();
	ArrayList<FieldtoFieldBinaryTrade> suitelist = new ArrayList<FieldtoFieldBinaryTrade>();

	public FieldtoFieldExecutionServices() {
		InvoiceStaticHelper.setffes(this);
	}

	public void releaserun(String release, int batchid, JFXListView<Release> listField, JFXListView<Cycle> listData,
			Release r1) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (releaselist != null && releaselist.size() > 0) {
					releaselist.clear();
				}
				releaselist = new DAO().getScriptsforFieldonDash("testsuites", release, null, null);
				if (releaselist != null && releaselist.size() > 0) {

					batchsize = 20;
					noofthreads = releaselist.size() / batchsize;
					Thread[] threads = new Thread[noofthreads + 1];

					for (int i = 0; i < threads.length; i++) {

						final int startNumber = (i * batchsize) + 1;

						int number = 0;
						if (releaselist.size() >= 20) {
							number = (batchsize + startNumber) - 1;
						} else {
							number = releaselist.size();
						}

						final int endNumber = number;

						threads[i] = new Thread(new Runnable() {
							@Override
							public void run() {
								executeTests(startNumber, endNumber, releaselist, batchid);
							}
						});
						threads[i].start();
					}

					do {
						checkthreads(threads);
					} while (checkthreads(threads));

					if (!checkthreads(threads)) {
						r1.setIsExecute(false);
						for (int i = 0; i < r1.getCycle().length; i++) {
							r1.getCycle()[i].setIsExecute(false);
							for (int j = 0; j < r1.getCycle()[i].getTestsuites().length; j++) {
								r1.getCycle()[i].getTestsuites()[j].setIsExecute(false);
							}
						}
					}

					calcpercentages(batchid, r1);

					listField.refresh();
					listData.refresh();
				}
				return null;
			}
		};

		Thread releasethread = new Thread(task);
		releasethread.setDaemon(true);
		releasethread.start();
	}

	public void cyclerun(String release, String cycle, int batchid, JFXListView<Release> listField,
			JFXListView<Cycle> listData, Release r1, Cycle c1) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (cyclelist != null && cyclelist.size() > 0) {
					cyclelist.clear();
				}
				cyclelist = new DAO().getScriptsforFieldonDash("testsuites", release, cycle, null);
				if (cyclelist != null && cyclelist.size() > 0) {

					batchsize = 20;
					noofthreads = cyclelist.size() / batchsize;
					Thread[] threads = new Thread[noofthreads + 1];

					for (int i = 0; i < threads.length; i++) {

						final int startNumber = (i * batchsize) + 1;

						int number = 0;
						if (cyclelist.size() >= 20) {
							number = (batchsize + startNumber) - 1;
						} else {
							number = cyclelist.size();
						}

						final int endNumber = number;

						threads[i] = new Thread(new Runnable() {
							@Override
							public void run() {
								executeTests(startNumber, endNumber, cyclelist, batchid);
							}
						});
						threads[i].start();
					}

					do {
						checkthreads(threads);
					} while (checkthreads(threads));

					if (!checkthreads(threads)) {
						c1.setIsExecute(false); // means processed
						for (int j = 0; j < c1.getTestsuites().length; j++) {
							c1.getTestsuites()[j].setIsExecute(false);
						}
					}

					cyclescalcpercentages(batchid, r1, c1);

					listField.refresh();
					listData.refresh();
				}
				return null;
			}
		};

		Thread releasethread = new Thread(task);
		releasethread.setDaemon(true);
		releasethread.start();
	}

	public void testsuiterun(String release, String cycle, String suite, int batchid, JFXListView<Release> listField,
			JFXListView<Cycle> listData, Release r1, Cycle c1, TestSuite s1) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (suitelist != null && suitelist.size() > 0) {
					suitelist.clear();
				}
				suitelist = new DAO().getScriptsforFieldonDash("testsuites", release, cycle, suite);
				if (suitelist != null && suitelist.size() > 0) {

					batchsize = 20;
					noofthreads = suitelist.size() / batchsize;
					Thread[] threads = new Thread[noofthreads + 1];

					for (int i = 0; i < threads.length; i++) {

						final int startNumber = (i * batchsize) + 1;

						int number = 0;
						if (suitelist.size() >= 20) {
							number = (batchsize + startNumber) - 1;
						} else {
							number = suitelist.size();
						}

						final int endNumber = number;

						threads[i] = new Thread(new Runnable() {
							@Override
							public void run() {
								executeTests(startNumber, endNumber, suitelist, batchid);
							}
						});
						threads[i].start();
					}

					do {
						checkthreads(threads);
					} while (checkthreads(threads));

					if (!checkthreads(threads)) {
						s1.setIsExecute(false);
					}

					suitecalcpercentages(batchid, r1, c1, s1);

					listField.refresh();
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

	private void executeTests(int start, int end, ArrayList<FieldtoFieldBinaryTrade> list, int batchid1) {
		for (int i = start; i <= end; i++) {
			ExecuteFieldtoFieldController efs = new ExecuteFieldtoFieldController();

			final String result = new DAO().getTestResult(list.get(i - 1).getSqlscript());

			if (result.equals("conerror")) {
				new DAO().updatetabledata("testcases", "message", "Unable to Connect to the QA Server", "id",
						list.get(i - 1).getId());
			} else if (result.equals("noserver")) {
				new DAO().updatetabledata("testcases", "message", "Unable to find default QA Server", "id",
						list.get(i - 1).getId());
			} else if (result.equals("runerror")) {
				new DAO().updatetabledata("testcases", "message", "Runtime Error", "id", list.get(i - 1).getId());
			} else if (result.matches("\\d+")) {
				if (Integer.parseInt(result) == 0) {
					efs.postresultactions(list.get(i - 1).getId(), result, "Pass",
							Integer.parseInt(list.get(i - 1).getTestsuiteid()), list.get(i - 1).getRelease(),
							list.get(i - 1).getCycle(), "Run Successul", batchid1);
				} else {
					efs.postresultactions(list.get(i - 1).getId(), result, "Fail",
							Integer.parseInt(list.get(i - 1).getTestsuiteid()), list.get(i - 1).getRelease(),
							list.get(i - 1).getCycle(), "Run Successul", batchid1);
				}
			} else {
				efs.postresultactions(list.get(i - 1).getId(), result, "Fail",
						Integer.parseInt(list.get(i - 1).getTestsuiteid()), list.get(i - 1).getRelease(),
						list.get(i - 1).getCycle(), "Run Successul", batchid1);
			}

		}
	}

	private void calcpercentages(int batchid, Release r1) {
		ArrayList<TestSuite> suitelist = new DAO().gettestsuitespercentage("fieldresults", batchid);
		if (suitelist != null && suitelist.size() > 0) {
			int cycle_sum_pass = 0;
			int cycle_sum_fail = 0;
			int suites_sum_pass = 0;
			int suites_sum_fail = 0;

			for (int i = 0; i < r1.getCycle().length; i++) {
				Cycle cycle = r1.getCycle()[i];
				for (int m = 0; m < cycle.getTestsuites().length; m++) {
					for (int k = 0; k < suitelist.size(); k++) {
						if (suitelist.get(k).getTsname().equals(cycle.getTestsuites()[m].getTsname())) {
							cycle.getTestsuites()[m].setPass(suitelist.get(k).getPass());
							cycle.getTestsuites()[m].setFail(suitelist.get(k).getFail());

							suites_sum_pass += cycle.getTestsuites()[m].getPass();
							suites_sum_fail += cycle.getTestsuites()[m].getFail();
						}
					}
					cycle.setPass(suites_sum_pass / cycle.getTestsuites().length);
					cycle.setFail((suites_sum_fail / cycle.getTestsuites().length));
				}
				suites_sum_pass = 0;
				suites_sum_fail = 0;
				cycle_sum_pass += cycle.getPass();
				cycle_sum_fail += cycle.getFail();
			}
			r1.setPass((cycle_sum_pass / r1.getCycle().length));
			r1.setFail((cycle_sum_fail / r1.getCycle().length));
		}
	}

	private void cyclescalcpercentages(int batchid, Release r1, Cycle c1) {
		ArrayList<TestSuite> suitelist = new DAO().gettestsuitespercentage("fieldresults", batchid);
		if (suitelist != null && suitelist.size() > 0) {
			int cycle_sum_pass = 0;
			int cycle_sum_fail = 0;
			int suites_sum_pass = 0;
			int suites_sum_fail = 0;

			for (int i = 0; i < 1; i++) {
				for (int m = 0; m < c1.getTestsuites().length; m++) {
					for (int k = 0; k < suitelist.size(); k++) {
						if (suitelist.get(k).getTsname().equals(c1.getTestsuites()[m].getTsname())) {
							c1.getTestsuites()[m].setPass(suitelist.get(k).getPass());
							c1.getTestsuites()[m].setFail(suitelist.get(k).getFail());

							suites_sum_pass += c1.getTestsuites()[m].getPass();
							suites_sum_fail += c1.getTestsuites()[m].getFail();
						}
					}
					c1.setPass(suites_sum_pass / c1.getTestsuites().length);
					c1.setFail((suites_sum_fail / c1.getTestsuites().length));
				}
				suites_sum_pass = 0;
				suites_sum_fail = 0;
			}
			for (int i = 0; i < r1.getCycle().length; i++) {
				cycle_sum_pass += r1.getCycle()[i].getPass();
				cycle_sum_fail += r1.getCycle()[i].getFail();
			}
			r1.setPass((cycle_sum_pass / r1.getCycle().length));
			r1.setFail((cycle_sum_fail / r1.getCycle().length));
		}
	}

	private void suitecalcpercentages(int batchid, Release r1, Cycle c1, TestSuite s1) {
		ArrayList<TestSuite> suitelist = new DAO().gettestsuitespercentage("fieldresults", batchid);
		if (suitelist != null && suitelist.size() > 0) {
			int cycle_sum_pass = 0;
			int cycle_sum_fail = 0;
			int suites_sum_pass = 0;
			int suites_sum_fail = 0;

			for (int m = 0; m < 1; m++) {
				for (int k = 0; k < suitelist.size(); k++) {
					if (suitelist.get(k).getTsname().equals(s1.getTsname())) {
						s1.setPass(suitelist.get(k).getPass());
						s1.setFail(suitelist.get(k).getFail());
					}
				}
			}

			for (int i = 0; i < c1.getTestsuites().length; i++) { // SetAverageOfTestsuite
				suites_sum_pass += c1.getTestsuites()[i].getPass();
				suites_sum_fail += c1.getTestsuites()[i].getFail();
			}

			c1.setPass(suites_sum_pass / c1.getTestsuites().length);
			c1.setFail(suites_sum_fail / c1.getTestsuites().length);

			for (int i = 0; i < r1.getCycle().length; i++) {
				cycle_sum_pass += r1.getCycle()[i].getPass();
				cycle_sum_fail += r1.getCycle()[i].getFail();
			}
			r1.setPass((cycle_sum_pass / r1.getCycle().length));
			r1.setFail((cycle_sum_fail / r1.getCycle().length));
		}
	}
}
