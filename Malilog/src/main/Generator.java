package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {

	private static Generator generator;

	public static Generator getGenerator() {
		if (Generator.generator == null)
			Generator.generator = new Generator();

		return generator;
	}

	private List<String> protocols;
	private int protoSize = 0;

	private int maxProtoSize = 0;
	private List<String> data;
	private int dataSize = 0;

	private int maxDataSize = 0;

	/**
	 * Constructor
	 */
	private Generator() {
		try {
			protocols = Files.readAllLines(Paths.get("configs/protocols.txt"));
			protoSize = protocols.size();
			for (String s : protocols) {
				int sizeTmp = s.length();
				maxProtoSize = sizeTmp > maxProtoSize ? sizeTmp : maxProtoSize;
			}

		} catch (IOException e) {
			protocols = new ArrayList<>();
			protocols.add("ipv4");
			System.err.println(e.getMessage());
			System.err.println("Default value used: ipv4");
		}

		try {
			data = Files.readAllLines(Paths.get("configs/data.txt"));
			dataSize = data.size();
			for (String s : data) {
				int sizeTmp = s.length();
				maxDataSize = sizeTmp > maxDataSize ? sizeTmp : maxDataSize;
			}

		} catch (IOException e) {
			data = new ArrayList<>();
			data.add("Default message for logs");
			System.err.println(e.getMessage());
			System.err.println("Default value used: Default message for logs");
		}

	}

	/**
	 * NetFlow modified style date - protocol - ip source - ip destination - port
	 * source - port destination - type of service - interface - data
	 * 
	 * @param logwriter
	 * @return The formatted log
	 */
	public void generateLog(LogWriter logwriter) {
		StringBuilder sb = new StringBuilder();
		sb.append(getDate()); // Date
		sb.append(" ");

		sb.append(getRandomTransport()); // TCP or UDP
		sb.append(" ");

		String prot = getRandomProtocol();
		sb.append(prot); // Protocol
		sb.append(padding((maxProtoSize + 1) - prot.length()));

		String ipSource = getRandomIp();
		sb.append(ipSource); // IP source
		sb.append(padding(16 - ipSource.length())); // 15 is max size of ip address (16 to count the " ")

		String ipDest = getRandomIp();
		sb.append(ipDest); // IP destination
		sb.append(padding(16 - ipDest.length()));

		int portSource = getRandomNumber(0, 65536);
		int padS = 0;

		/**
		 * size of the number
		 */
		if (portSource >= 10000) {
			padS = 5;
		} else if (portSource >= 1000) {
			padS = 4;
		} else if (portSource >= 100) {
			padS = 3;
		} else if (portSource >= 10) {
			padS = 2;
		} else {
			padS = 1;
		}

		sb.append(portSource); // port source
		sb.append(padding(6 - padS));

		int portDest = getRandomNumber(0, 65536);
		int padD = 0;

		if (portDest >= 10000) {
			padD = 5;
		} else if (portDest >= 1000) {
			padD = 4;
		} else if (portDest >= 100) {
			padD = 3;
		} else if (portDest >= 10) {
			padD = 2;
		} else {
			padD = 1;
		}

		sb.append(portDest); // port destination
		sb.append(padding(6 - padD));

//		sb.append(getRandomService()); // type of service ?
//		sb.append(" ");

//		sb.append(getRandomInterface()); // interface ?
//		sb.append(" ");

		String dat = getRandomData();
		sb.append(dat); // Data
		sb.append(padding((maxDataSize + 1) - dat.length()));

		logwriter.write(sb.toString());
	}

	/**
	 * Return the current date time
	 * 
	 * @return
	 */
	private String getDate() {
		LocalDateTime date = LocalDateTime.now();
		return "[" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "]";
	}

	/**
	 * Return a random line from data.txt
	 * 
	 * @return
	 */
	private String getRandomData() {
		return data.get(getRandomNumber(0, dataSize));
	}

	/**
	 * Return a random IP address
	 * 
	 * @return
	 */
	public String getRandomIp() {
		int b1 = getRandomNumber(0, 223);
		int b2 = getRandomNumber(0, 255);
		int b3 = getRandomNumber(0, 255);
		int b4 = getRandomNumber(0, 255);

		return b1 + "." + b2 + "." + b3 + "." + b4;
	}

	/**
	 * Return a random number between max and min
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	private int getRandomNumber(int min, int max) {
		Random r = new Random();
		return min >= max ? 0 : r.nextInt(max - min) + min;
	}

	/**
	 * Return a random protocol from protocols.txt
	 * 
	 * @return
	 */
	private String getRandomProtocol() {
		return protocols.get(getRandomNumber(0, protoSize));
	}

	/**
	 * Return either TCP or UDP randomly
	 * 
	 * @return
	 */
	private String getRandomTransport() {
		return getRandomNumber(0, 2) == 0 ? "TCP" : "UDP";
	}

	/**
	 * Pretty printer, get a string with "size" space
	 * 
	 * @param size
	 * @return
	 */
	private String padding(int size) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
}
