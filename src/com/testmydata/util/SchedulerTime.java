package com.testmydata.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import com.testmydata.dao.DAO;
import com.testmydata.main.WelcomePage;

@SuppressWarnings("rawtypes")
public class SchedulerTime extends TimerTask {

	private List<byte[]> usedMemory = new LinkedList<byte[]>();
	private List<byte[][]> usedMemory1 = new LinkedList<byte[][]>();
	private List<char[]> usedMemory2 = new LinkedList<char[]>();
	private List<int[]> usedMemory3 = new LinkedList<int[]>();
	private List<String[]> usedMemory4 = new LinkedList<String[]>();
	private List<HashMap[]> usedMemory5 = new LinkedList<HashMap[]>();
	private List<ArrayList[]> usedMemory6 = new LinkedList<ArrayList[]>();
	private List<Object[]> usedMemory7 = new LinkedList<Object[]>();

	@SuppressWarnings("unused")
	@Override
	public void run() {
		DAO dao = new DAO();
		try {
			dao.establishRemoteDBConnection();

			WelcomePage.paymentActiveValidation();

			for (int i = 0; i < 10; i++) {
				super.finalize();
				free();
			}

			/*
			 * Runtime r = Runtime.getRuntime();
			 * 
			 * Integer someints[] = new Integer[5000]; r.gc(); for (int i = 0; i
			 * < 5000; i++) someints[i] = new Integer(i); // allocate integers
			 * // discard Integers - 275 - for (int i = 0; i < 5000; i++)
			 * someints[i] = null; r.gc(); // request garbage collection
			 */
			// Get current size of heap in bytes
			long heapSize = Runtime.getRuntime().totalMemory();

			// Get maximum size of heap in bytes. The heap cannot grow beyond
			// this
			// size.// Any attempt will result in an OutOfMemoryException.
			long heapMaxSize = Runtime.getRuntime().maxMemory();

			// Get amount of free memory within the heap in bytes. This size
			// will
			// increase // after garbage collection and decrease as new objects
			// are
			// created.
			long heapFreeSize = Runtime.getRuntime().freeMemory();

			// System.out.println("Current Size :" + (heapSize / 1024) / 1024);
			// System.out.println("Heap Max Size:" + (heapMaxSize / 1024) /
			// 1024);
			// System.out.println("Heap Free Size:" + (heapFreeSize / 1024) /
			// 1024);

		} // catch (TrademenException e) {
		catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void allocate(int howManyMB) {
		usedMemory.add(new byte[howManyMB * 1024 * 1024]);
		usedMemory1.add(new byte[howManyMB * 1024 * 1024][howManyMB * 1024 * 1024]);
		usedMemory2.add(new char[howManyMB * 1024 * 1024]);
		usedMemory3.add(new int[howManyMB * 1024 * 1024]);
		usedMemory4.add(new String[howManyMB * 1024 * 1024]);
		usedMemory5.add(new HashMap[howManyMB * 1024 * 1024]);
		usedMemory6.add(new ArrayList[howManyMB * 1024 * 1024]);
		usedMemory7.add(new Object[howManyMB * 1024 * 1024]);
	}

	public void free() {
		usedMemory.clear();
		usedMemory1.clear();
		usedMemory2.clear();
		usedMemory3.clear();
		usedMemory4.clear();
		usedMemory5.clear();
		usedMemory6.clear();
		usedMemory7.clear();

		System.runFinalization();
		// System.gc();
	}
}