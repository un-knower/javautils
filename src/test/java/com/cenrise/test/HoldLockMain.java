package com.cenrise.test;

/**
 * 自动生成锁死的文件，用于查找被锁定的线程定位代码测试
 * @author Admin
 *
 */
public class HoldLockMain {
	public static Object[] lock = new Object[10];
	public static java.util.Random r = new java.util.Random();
	static {
		for (int i = 0; i < lock.length; i++) {
			lock[i] = new Object();
		}
	}

	public static class HoldLockTask implements Runnable {// 一个持有锁的线程

		private int i;

		public HoldLockTask(int i) {
			this.i = i;
		}

		@Override
		public void run() {
			try {
				while (true) {
					synchronized (lock[i]) {// 持有锁
						if (i % 2 == 0)
							lock[i].wait(r.nextInt(10));// 等待
						else
							lock[i].notifyAll();// 通知
					}
				}
			} catch (Exception e) {
			}

		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < lock.length * 2; i++) {// 每2个线程
			new Thread(new HoldLockTask(i / 2)).start();
		}
	}

}
