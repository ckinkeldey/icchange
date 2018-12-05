package net.g2lab.icchange.model;

import java.util.Arrays;
import java.util.Calendar;

/** A sequence of object classes describing a change.
 * @author <a href="mailto:christoph at kinkeldey dot de">Christoph Kinkeldey</a>
 *
 */
public class ChangeSequence {

	private static int id;

	private int number = 0;
	private Calendar[] dates = new Calendar[0];
	private ObjectClass[] classes = new ObjectClass[0];
	private double[] changeUncertainties = new double[0];

	public ChangeSequence() {
		setId(id);
		id++;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return the dates
	 */
	public Calendar[] getDates() {
		return dates;
	}

	/**
	 * @param dates
	 *            the dates to set
	 */
	public void setDates(Calendar[] dates) {
		this.dates = dates;
	}

	/**
	 * @return the classes
	 */
	public ObjectClass[] getObjClasses() {
		return classes;
	}

	/**
	 * @param classes
	 *            the classes to set
	 */
	public void setClasses(ObjectClass[] classes) {
		this.classes = classes;
	}

	public double[] getUncertainties() {
		return changeUncertainties;
	}

	public void setUncertainties(double[] uncertainties) {
		this.changeUncertainties = uncertainties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ChangePattern [classes=" + Arrays.toString(classes) + "]";
	}

	public static boolean isNoChange(int[] pixelValues) {
		boolean result = true;
		for (int i = 0; i < pixelValues.length-1; i++) {
			result &= pixelValues[i] == pixelValues[i+1];
		}
		return result;
	}

	public static Integer getChangeCode(int[] pixelValues) {
		if (isNoChange(pixelValues)) {
			return 0;
		}
		int sum = 0;
		int basis = pixelValues.length;
		for (int i = 0; i < pixelValues.length; i++) {
			sum += Math.pow(basis, i) * pixelValues[i];
		}
		return new Integer(sum);
	}

}
