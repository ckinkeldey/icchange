/**
 * 
 */
package net.g2lab.icchange.model;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import org.apache.commons.math.stat.StatUtils;

/**
 * A list of change sequences with the same class sequence.
 * 
 * @author <a href="mailto:christoph at kinkeldey dot de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class ChangeSequenceGroup extends ArrayList<ChangeSequence> {

	public int getChangeCode() {
		int code = 0;
		ChangeSequence sequence = this.get(0);
		ObjectClass[] objClasses = sequence.getObjClasses();
		int i = 0;
		for (ObjectClass objClass : objClasses) {
			code += Math.pow(3, i++) * objClass.getClasscode();
		}
		return code;
	}
	
	public double[] getAllMaxUncertainties() {
		double[] maxima = new double[this.size()];
		for (int i = 0; i < this.size(); i++) {
			ChangeSequence sequence = this.get(i);
			maxima[i] = StatUtils.max(sequence.getUncertainties());
		}
		return maxima;
	}
	
	public double getMeanUncertainty() {
		double[] maxima = getAllMaxUncertainties();
		return StatUtils.mean(maxima);
	}
	
	public double getStdDevUncertainty() {
		double[] maxima = getAllMaxUncertainties();
		return Math.sqrt(StatUtils.variance(maxima));
	}

	public boolean isSequenceFitting(ChangeSequence sequence) {
		if (this.size() == 0) {
			return true;
		} else {
			ChangeSequence first = this.get(0);
			return Arrays.equals(first.getObjClasses(),
					sequence.getObjClasses())
					&& Arrays.equals(first.getDates(), sequence.getDates());
		}
	}

	public ObjectClass[] getObjClasses() {
		if (this.size() == 0) {
			return new ObjectClass[0];
		} else {
			return this.get(0).getObjClasses();
		}
	}

	public Calendar[] getDates() {
		if (this.size() == 0) {
			return new Calendar[0];
		} else {
			return this.get(0).getDates();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString() {
		String objClassString = "";
		for (ObjectClass objClass : getObjClasses()) {
			objClassString += objClass.getLabel().trim() + " ";
		}
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		return objClassString + " amount: " + size() + " uncertainty: " + nf.format(getMeanUncertainty()) + " +/- " + nf.format(getStdDevUncertainty());
	}



	

}
