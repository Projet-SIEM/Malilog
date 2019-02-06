package main;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

	/**
	 * Shared variables
	 */
	private static boolean stop = false;
	private static int number = -1;
	private static boolean generationEnded = false;
	private static boolean noInteraction = false;

	/**
	 * Loop of auto log generation
	 * 
	 * @param logwriter
	 * @param generator
	 */
	private static void generation(LogWriter logwriter, Generator generator) {
		int cpt = 0;
		boolean check = stop;
		System.out.println("Starting log generation ...");

		if (!noInteraction) {
			System.out.println("Enter \"exit\" to stop the generation.");
		} else {
			System.out.println("No interaction enabled. Press Ctrl+C to stop.");
		}

		while (!(check || stop)) { // Manual or automatic stop
			try {
				Thread.sleep(ThreadLocalRandom.current().nextInt(100, 1000));

				generator.generateLog(logwriter);

				check = (number > 0 ? (cpt >= number - 1) : stop);
				cpt++;

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(cpt + " logs have been generated in \"" + logwriter.getPath() + "\".");
		generationEnded = true;
	}

	public static void main(String[] args) {

		Options options = new Options();

		Option help = new Option("h", "help", false, "Show this help");
		options.addOption(help);

		Option nblog = new Option("nb", "nb-logs", true, "Number of log to generate");
		options.addOption(nblog);

		Option noInteract = new Option("ni", "no-interact", false,
				"Run without interactive mode (Can not stop with \"exit\", must use Ctrl+C)");
		options.addOption(noInteract);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cm;

		try {
			cm = parser.parse(options, args);

			if (cm.hasOption('h')) {
				formatter.printHelp("java -jar malilog.jar [options]", options);
				return;
			}

			if (cm.hasOption("nb")) {
				number = Integer.parseInt(cm.getOptionValue("nb"));
			}

			if (cm.hasOption("ni")) {
				noInteraction = true;
			}

			LogWriter logwriter = LogWriter.getLogWriter();
			Generator generator = Generator.getGenerator();

			Thread t1 = new Thread() {
				public void run() {
					generation(logwriter, generator);
				}
			};
			t1.start();

			if (!cm.hasOption("ni") && !cm.hasOption("nb")) {
				boolean validCmd = false;
				try (Scanner sc = new Scanner(System.in)) {
					String cmd = "";

					do {
						cmd = sc.nextLine();
						cmd = cmd.toLowerCase();

						// If we add more commands
						switch (cmd) {
						case "exit":
							validCmd = true;
							stop = true; // Stop the log generation
							System.out.println("Stopping...");
							break;

						default:
							if (number != -1 && generationEnded) {
								validCmd = true;
							}
							break;
						}

					} while (!validCmd);

					try {
						t1.join(); // Waiting for the thread t1 to stop
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			logwriter.close();
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("java -jar malilog.jar [options]", options);
			System.exit(1);
		}
	}

}
