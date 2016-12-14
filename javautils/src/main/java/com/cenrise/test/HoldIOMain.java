package com.cenrise.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * 开启几个线程，其中一个占用大量的IO，用于测试。
 * @author Admin
 *
 */
public class HoldIOMain {

	public static class HoldIOTask implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					FileOutputStream fos = new FileOutputStream(
							new File("temp"));
					for (int i = 0; i < 10000; i++) {
						fos.write(i);// 大量的写操作
						fos.close();
						FileInputStream fis = new FileInputStream(new File(
								"temp"));
						while (fis.read() != -1)
							;// 大量的读操作
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class LazyTask implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					Thread.sleep(1000);// 一个空闲线程
				}
			} catch (Exception e) {
			}
		}
	}

	public static void main(String[] args) {
		new Thread(new HoldIOTask()).start();// 开启占用I/O的线程
		new Thread(new LazyTask()).start();// 开启空闲线程
		new Thread(new LazyTask()).start();
		new Thread(new LazyTask()).start();
	}
}
