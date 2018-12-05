package net.g2lab.icchange.view.sequence;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.g2lab.icchange.model.ChangeSequence;
import net.g2lab.icchange.model.ChangeSequenceGroup;
import net.g2lab.icchange.model.ObjectClass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFReader;

public class SequenceDBFParser {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

//	private static final int NUM_OBJCLASSES = 5;

	private Calendar[] dates;
	private ObjectClass[] objClasses;
	private List<ChangeSequenceGroup> sequenceGroups;

	public SequenceDBFParser(InputStream inputStream, Calendar[] dates,
			String[] objClassAttr, String[] classcodeAttr, String[] uncertaintyAttr)
			throws IOException, ParseException {
		DBFReader reader = new DBFReader(inputStream);
		int[] uncertIndices = getIndices(uncertaintyAttr, reader);
		int[] objClassIndices = getIndices(objClassAttr, reader);
		int[] classcodeIndices = getIndices(classcodeAttr, reader);
		
		Integer[][] dataUncert = new Integer[reader.getRecordCount()][uncertIndices.length];
		String[][] dataObjClass = new String[reader.getRecordCount()][objClassIndices.length];
		Map<String, Integer> dataClasscode = new HashMap<String, Integer>();

		Object[] rowObjects;
		int n = 0;
		while ((rowObjects = reader.nextRecord()) != null) {
			for (int i = 0; i < uncertIndices.length; i++) {
				dataUncert[n][i] = ((Double) rowObjects[uncertIndices[i]]).intValue();
//				LOG.debug("uncertainty"+i+": " + dataUncert[n][i]);
			}	
			for (int i = 0; i < objClassIndices.length; i++) {
				dataObjClass[n][i] = (String) rowObjects[objClassIndices[i]];
//				LOG.debug("objClass"+i+": " + dataObjClass[n][i]);
			}
			for (int i = 0; i < classcodeIndices.length; i++) {
				int classcode = ((Double) rowObjects[classcodeIndices[i]]).intValue();
//				LOG.debug("classcode: "+i+": " + classcode);
				dataClasscode.put(dataObjClass[n][i], classcode);
				
			}	
			n++;
		}

		this.objClasses = createObjectClasses(dataObjClass, dataClasscode);
//		LOG.info("object classes: " + objClasses);
		this.sequenceGroups = createChangeSequenceGroups(dataObjClass, dataUncert, dates, objClasses);
		// filter out zero-sized
		deleteZeroSizedGroups(sequenceGroups);
		inputStream.close();
		// }
	}



	private void deleteZeroSizedGroups(
			List<ChangeSequenceGroup> sequenceGroups) {
		List<ChangeSequenceGroup> toBeDeleted = new ArrayList<ChangeSequenceGroup>();
		for (ChangeSequenceGroup group : sequenceGroups) {
			if (group.size() == 0) {
				toBeDeleted.add(group);
			}
		}
		sequenceGroups.removeAll(toBeDeleted);
	}



	private int[] getIndices(String[] attrNames, DBFReader reader)
			throws DBFException {
		int[] indices = new int[attrNames.length];
		for (int i = 0; i < attrNames.length; i++) {
			indices[i] = getFieldIndexByName(attrNames[i], reader);
		}
		
		return indices;
	}

	private int getFieldIndexByName(String name, DBFReader reader)
			throws DBFException {
		for (int i = 0; i < reader.getFieldCount(); i++) {
			if (name.compareTo(reader.getField(i).getName()) == 0) {
				return i;
			}
		}
		return -1;
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

	/**
	 * @param sequenceGroups
	 *            the sequenceGroups to set
	 */
	public void setSequenceGroups(List<ChangeSequenceGroup> sequenceGroups) {
		this.sequenceGroups = sequenceGroups;
	}

	private int parseNumDates(List<String[]> items) {
		return Integer.parseInt(items.get(0)[0]);
	}

	private Calendar[] parseDates(List<String[]> items, int numDates)
			throws ParseException {
		Calendar[] dates = new Calendar[numDates];
		String[] dateStrings = items.get(1);
		for (int i = 0; i < numDates; i++) {
			Date date = new SimpleDateFormat("dd.MM.yyyy")
					.parse(dateStrings[i]);
			dates[i] = new GregorianCalendar();
			dates[i].setTime(date);
		}
		return dates;
	}

	private static ObjectClass[] parseObjectClasses(String[] dataObjClass, ObjectClass[] allObjClasses) {
		String[] objClassLabels = dataObjClass;
		ObjectClass[] objClasses = new ObjectClass[objClassLabels.length];
		for (int i = 0; i < objClasses.length; i++) {
			objClasses[i] = getObjectClassFromLabel(objClassLabels[i], allObjClasses);
		}
		return objClasses;
	}

	private static ObjectClass getObjectClassFromLabel(String label,
			ObjectClass[] objClasses) {
		for (ObjectClass objClass : objClasses) {
			if (objClass.getLabel().compareTo(label) == 0) {
				return objClass;
			}
		}
		return null;
	}

//	private static String[] parseObjectClassNames(String[][] dataObjClass) {
//		return dataObjClass;
//	}

	private static double[] parseUncertainties(Integer[] dataUncert)
			throws ArrayIndexOutOfBoundsException {
		double[] uncert = new double[dataUncert.length];
		for (int i = 0; i < uncert.length; i++) {
			uncert[i] = dataUncert[i];
		}
		return uncert;
	}

	private static ObjectClass[] getClasses(String[] tokens,
			ObjectClass[] objClasses) {
		ObjectClass[] classes = new ObjectClass[tokens.length];
		int i = 0;
		for (String token : tokens) {
			classes[i++] = getObjectClassFromLabel(token, objClasses);
		}
		return classes;
	}

	private static List<ChangeSequenceGroup> createChangeSequenceGroups(
			String[][] dataObjClass, Integer[][] dataUncert, Calendar[] dates, ObjectClass[] allObjClasses) {
		List<ChangeSequenceGroup> sequenceGroups = new ArrayList<ChangeSequenceGroup>();
		int numOccurrences = 0;
		int line = 0;
		try {
			int groupIndex = 0;
			sequenceGroups.add(new ChangeSequenceGroup());
			for (int objIndex = 0; objIndex < dataObjClass.length; objIndex++) {
				ChangeSequence sequence = new ChangeSequence();
				sequence.setDates(dates);
				sequence.setClasses(parseObjectClasses(dataObjClass[objIndex], allObjClasses));
				sequence.setUncertainties(parseUncertainties(dataUncert[objIndex]));
				ChangeSequenceGroup fittingGroup = getFittingGroup(sequence, sequenceGroups);
				if (fittingGroup == null) {
					groupIndex++;
					sequenceGroups.add(new ChangeSequenceGroup());
				} else {
					fittingGroup.add(sequence);
				}
				line++;
			}
		} catch (Exception e) {
			LOG.error("Error parsing line " + line + ": " + e, e);
		}
		return sequenceGroups;
	}

	private static ChangeSequenceGroup getFittingGroup(ChangeSequence sequence, List<ChangeSequenceGroup> sequenceGroups) {
		for (ChangeSequenceGroup group : sequenceGroups) {
			if (group.isSequenceFitting(sequence)) {
				return group;
			}
		}
		return null;
	}



	private static ObjectClass[] createObjectClasses(String[][] dataObjClass, Map<String, Integer> dataClasscode) {
		
		Collection<String> objClassNames = new HashSet<String>();
		int i = 0;
		for (String[] item : dataObjClass) {
			objClassNames.addAll(Arrays.asList(item));
		}
		
		
		
		i = 0;
		ObjectClass[] objClasses = new ObjectClass[dataObjClass.length];
		for (String className : objClassNames) {
			objClasses[i] = new ObjectClass(className);
			objClasses[i].setClasscode(dataClasscode.get(className));
			i++;
		}
		
		return objClasses;
	}

}
