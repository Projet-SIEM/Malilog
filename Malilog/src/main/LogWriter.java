package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public class LogWriter {

	private static LogWriter logwriter;

	/**
	 * Return the LogWriter instance
	 * 
	 * @return
	 */
	public static LogWriter getLogWriter() {
		if (LogWriter.logwriter == null)
			LogWriter.logwriter = new LogWriter();

		return logwriter;
	}

	private String path = "Logs" + File.separator + "logs.log";
	private PrintWriter writer;

	/**
	 * constructor
	 */
	private LogWriter() {
		createWriter();
	}

	/*
	 * Close the writer
	 */
	public void close() {
		writer.close();
	}

	/**
	 * Initialize the writer and open the file
	 */
	private void createWriter() {
		try {
			System.out.println("Path: " + path);
			File file = new File(path);
			String tmp[] = path.split(path);
			if (tmp.length > 1) {
				file.getParentFile().mkdirs(); // Create parents
			}

			writer = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the file's path
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Write in file
	 * 
	 * @param log The string to write
	 */
	public void write(String log) {
		writer.println(log);
		writer.flush();
	}

}
