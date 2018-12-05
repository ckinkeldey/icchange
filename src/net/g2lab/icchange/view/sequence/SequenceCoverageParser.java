package net.g2lab.icchange.view.sequence;

import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.g2lab.icchange.model.ChangeSequence;
import net.g2lab.icchange.model.ChangeSequenceGroup;
import net.g2lab.icchange.model.ObjectClass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;

public class SequenceCoverageParser {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private Calendar[] dates;
	private ObjectClass[] objClasses;
	private List<ChangeSequenceGroup> sequenceGroups;

	public SequenceCoverageParser(GridCoverage2D[] lc, GridCoverage2D[] u,
			Calendar[] dates, String[] objClassNames, int[] objClassCodes)
			throws IOException, ParseException {
		int numPixels = lc[0].getRenderedImage().getWidth()
				* lc[0].getRenderedImage().getHeight();

		this.objClasses = parseObjectClasses(objClassNames, objClassCodes);

		RenderedImage u0 = u[0].getRenderedImage();
//		RenderedImage u1 = u[1].getRenderedImage();
//		RenderedImage u2 = u[2].getRenderedImage();

		DataBuffer[] imageData = new DataBuffer[lc.length];
		for (int i = 0; i < lc.length; i++) {
			imageData[i] = lc[i].getRenderedImage().getData().getDataBuffer();
		}
		
		DataBuffer uData0 = u0.getData().getDataBuffer();

		ObjectClass[][] dataObjClass = new ObjectClass[dates.length][numPixels];
//		double[][] dataUncert = new double[dates.length][numPixels];
		double[] dataUncert = new double[numPixels];
//		int n = 0;
		for (int i = 0; i < imageData[0].getSize(); i++) {
			for (int k = 0; k < imageData.length; k++) {			
				dataObjClass[k][i] = getObjClassFromClassCode(imageData[k].getElem(i));
			}
			dataUncert[i] = uData0.getElem(i);
//			n++;
		}

		this.sequenceGroups = createChangeSequenceGroups(dataObjClass,
				dataUncert, dates, objClasses);
	}

	private ObjectClass getObjClassFromClassCode(int code) {
		return objClasses[code];
	}

	private List<ChangeSequenceGroup> createChangeSequenceGroups(
			ObjectClass[][] dataObjClass, double[] dataUncert,
			Calendar[] dates, ObjectClass[] objClasses) {
		List<ChangeSequence> changeSequences = new ArrayList<ChangeSequence>();
		for (int i = 0; i < dataObjClass[0].length; i++) {
			if (isNoChange(dataObjClass, i)) {
				continue;
			}
			ChangeSequence sequence = new ChangeSequence();
			sequence.setDates(dates);
			ObjectClass[] changeClasses = new ObjectClass[dates.length];
			for (int k = 0; k < dates.length; k++) {
				changeClasses[k] = dataObjClass[k][i];
			}
			sequence.setClasses(changeClasses);
			double[] uncertainties = new double[dates.length];
			for (int k = 0; k < dates.length; k++) {
				uncertainties[k] = dataUncert[i];
			}
			sequence.setUncertainties(uncertainties);
			changeSequences.add(sequence);
		}

		List<ChangeSequenceGroup> groups = new ArrayList<ChangeSequenceGroup>();
		for (ChangeSequence sequence : changeSequences) {
			ChangeSequenceGroup fittingGroup = getFittingGroup(sequence, groups);
			if (fittingGroup != null) {
				fittingGroup.add(sequence);
			} else {
				ChangeSequenceGroup newGroup = new ChangeSequenceGroup();
				newGroup.add(sequence);
				groups.add(newGroup);
			}
		}
		return groups;
	}

	/**
	 * @param dataObjClass
	 * @param i
	 * @return
	 */
	public boolean isNoChange(ObjectClass[][] dataObjClass, int i) {
		boolean noChange = true;
		for (int n = 0; n < dataObjClass.length-1; n++) {
			noChange &= dataObjClass[n][i].getClasscode() == dataObjClass[n+1][i].getClasscode();
		}
		return noChange;
	}

	private void deleteZeroSizedGroups(List<ChangeSequenceGroup> sequenceGroups) {
		List<ChangeSequenceGroup> toBeDeleted = new ArrayList<ChangeSequenceGroup>();
		for (ChangeSequenceGroup group : sequenceGroups) {
			if (group.size() == 0) {
				toBeDeleted.add(group);
			}
		}
		sequenceGroups.removeAll(toBeDeleted);
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
	 * @return the objClasses
	 */
	public ObjectClass[] getObjClasses() {
		return objClasses;
	}

	/**
	 * @param objClasses
	 *            the objClasses to set
	 */
	public void setObjClasses(ObjectClass[] objClasses) {
		this.objClasses = objClasses;
	}

	/**
	 * @return the sequenceGroups
	 */
	public List<ChangeSequenceGroup> getSequenceGroups() {
		return sequenceGroups;
	}

	private static ObjectClass[] parseObjectClasses(String[] objClassLabels,
			int[] objClassCodes) {
		ObjectClass[] objClasses = new ObjectClass[objClassLabels.length];
		for (int i = 0; i < objClasses.length; i++) {
			objClasses[i] = new ObjectClass(objClassLabels[i]);
			objClasses[i].setClasscode(objClassCodes[i]);
		}
		return objClasses;
	}

	private static ChangeSequenceGroup getFittingGroup(ChangeSequence sequence,
			List<ChangeSequenceGroup> sequenceGroups) {
		for (ChangeSequenceGroup group : sequenceGroups) {
			if (group.isSequenceFitting(sequence)) {
				return group;
			}
		}
		return null;
	}

}
