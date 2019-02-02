package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogWriter {

	private String path = "Logs/logs.log";
	private PrintWriter writer;
	private static LogWriter logwriter;

	private LogWriter() {
		createWriter();
	}

	private void createWriter() {
		try {
			File file = new File(path);
			if (!file.exists())
				file.createNewFile();

			writer = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));

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
		writer.close();
	}

	public void write(String log) {
		System.out.println("log: " + log);
		writer.println(log);
		writer.flush();
	}

}
