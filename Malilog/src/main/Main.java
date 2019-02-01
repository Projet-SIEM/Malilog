package main;

import java.util.Scanner;

public class Main {

	private static void autoMode() {

	}

	public static void main(String[] args) {

		LogWriter logwriter = LogWriter.getLogWriter();
		Scanner sc = new Scanner(System.in);
		String cmd = "";
		do {
			System.out.println("Choose \"Auto\" or \"Manual\" mode:");
			cmd = sc.nextLine();
			cmd = cmd.toLowerCase();
		} while (!cmd.equals("auto") && !cmd.equals("manual"));

		switch (cmd) {
		case "auto":
			autoMode();
			break;
		case "manual":
			System.out.println("Enter new log:");
			String in = sc.nextLine();
			sc.close();
			System.out.println("in: " + in);
			logwriter.write(in);
			System.out.println("Writing done!");
			break;
		default:
			System.out.println("Error with input:" + cmd);
			break;
		}

	}

}
