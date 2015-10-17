package com.cenrise.commons.utils.test;


/**
 * 模拟创建三个线程，其中一个占用大量CPU，用于监控测试的类
 * @author Admin
 *
 */
public class HoldCPUMain {
	public static class HoldCPUTask implements Runnable {
		@Override
		public void run() {
			while (true) {
				double a = Math.random() * Math.random();// ռ��CPU
			}

		}
	}

	public static class LazyTask implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new Thread(new HoldCPUTask()).start();
		new Thread(new LazyTask()).start();
		new Thread(new LazyTask()).start();
		new Thread(new LazyTask()).start();

	}
}
