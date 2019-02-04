package main;

import java.util.Random;

public class RandomIPGen {

	public RandomIPGen() {
		// TODO Auto-generated constructor stub
	}

	public String getRandomIp() {
		int b1 = getRandomNumber(0, 223);
		int b2 = getRandomNumber(0, 255);
		int b3 = getRandomNumber(0, 255);
		int b4 = getRandomNumber(0, 255);

		return b1 + "." + b2 + "." + b3 + "." + b4;
	}

	private int getRandomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}
}
