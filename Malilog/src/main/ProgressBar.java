package main;

import java.text.DecimalFormat;

/**
 * ProgressBar class
 * 
 * @author alemasle
 *
 */
public class ProgressBar {

	/**
	 * Maximum value
	 */
	private int max;

	/**
	 * factor of max/100
	 */
	private double factor;

	public ProgressBar() {
		max = -1;
		factor = -1;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max <= 0 ? 0 : max;
		factor = (double) (this.max == 0 ? 1.0 : this.max / 100.0);
	}

	public double getFactor() {
		return factor;
	}

	private String avancement(int value) {
		StringBuilder s = new StringBuilder(max + 2);
		int compteur = 0;
		s.append("[");

		double avance = value;
		double maximum = max / factor;

		avance = (value > max) ? max : (value < 0 ? 0 : value / factor);

		if (avance <= maximum) {

			while (avance - 5 >= 0) {
				s.append("#");
				compteur++;
				avance -= 5;
			}

			int point = 20 - compteur;

			while (point > 0) {
				s.append(".");
				point -= 1;
			}

		}

		s.append("]");
		return s.toString();
	}

	public void printBar(int value) {
		if (max >= 0) {
			DecimalFormat format = new DecimalFormat("0.00");
			String pourcentage = format.format(value / factor);
			System.out.print("Progression: " + avancement(value) + " " + pourcentage + "%\r");
			System.out.flush();
		}
	}

}
