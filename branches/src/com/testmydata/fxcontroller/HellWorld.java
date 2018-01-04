package com.testmydata.fxcontroller;

public class HellWorld {

	public static void main(String[] args) {

		int mainNumber = 2;
		int divideNumber = 200;
		int numberOfThreads = mainNumber / divideNumber;

		Thread[] threads = new Thread[numberOfThreads + 1];
		for (int i = 0; i < threads.length; i++) {

			final int startNumber = (i * divideNumber) + 1;
			// final int endNumber = divideNumber * (i + 1);
			final int endNumber = (mainNumber - startNumber) + 1;

			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					exampleMethod(startNumber, endNumber);
				}
			});
			threads[i].start();
		}
	}

	private static void exampleMethod(int startNumber, int endNumber) {
		for (int i = startNumber; i <= endNumber; i++) {
			System.out.println("this is a thread" + i);
		}
	}
}