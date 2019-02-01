package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogWriter {

	private String path = "../Logs/logs.log";
	private BufferedWriter bw;
	private static LogWriter logwriter;

	private LogWriter() {
		createWriter();
	}

	private void createWriter() {
		try {
			File file = new File(path);
			if (!file.exists())
				file.createNewFile();

			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static LogWriter getLogWriter() {
		if (LogWriter.logwriter == null)
			LogWriter.logwriter = new LogWriter();

		return logwriter;
	}

	public void close() {
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String log) {
		try {
			System.out.println("log: " + log);
			bw.write(log);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
