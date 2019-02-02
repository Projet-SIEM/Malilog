package main;

public class Main {

	private static void generation(LogWriter logwriter) {
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logwriter.write("test-" + i);
		}
	}

	public static void main(String[] args) {

		LogWriter logwriter = LogWriter.getLogWriter();
		generation(logwriter);
	}

}
