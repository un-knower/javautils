

public class HoldCPUMain {
	public static class HoldCPUTask implements Runnable {
		@Override
		public void run() {
			while (true) {
				double a = Math.random() * Math.random();
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
