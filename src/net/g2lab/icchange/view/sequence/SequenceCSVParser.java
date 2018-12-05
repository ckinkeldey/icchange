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
import java.util.HashSet;
import java.util.List;

import net.g2lab.icchange.model.ChangeSequence;
import net.g2lab.icchange.model.ChangeSequenceGroup;
import net.g2lab.icchange.model.ObjectClass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.skife.csv.CSVReader;
import org.skife.csv.SimpleReader;

public class SequenceCSVParser {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private static final int NUM_OBJCLASSES = 3;

	private Calendar[] dates;
	private ObjectClass[] objClasses;
	private List<ChangeSequenceGroup> sequenceGroups;

	public SequenceCSVParser(InputStream in) throws IOException, ParseException {
		CSVReader reader = new SimpleReader();
		reader.setSeperator(';');
		List<String[]> items = reader.parse(in);
		int numDates = parseNumDates(items);
		this.dates = parseDates(items, numDates);
		items = items.subList(2, items.size());
		this.objClasses = createObjectClasses(items);
		this.sequenceGroups = createChangeSequenceGroups(items, dates, objClasses);
		in.close();
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
			Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dateStrings[i]);
			dates[i] = new GregorianCalendar();
			dates[i].setTime(date);
		}
		return dates;
	}

	private static ObjectClass[] parseObjectClasses(String[] item,
			ObjectClass[] allObjClasses) {
		String[] objClassLabels = parseObjectClassNames(item);
		ObjectClass[] objClasses = new ObjectClass[objClassLabels.length];
		for (int i = 0; i < objClasses.length; i++) {
			objClasses[i] = getObjectClassFromLabel(objClassLabels[i],
					allObjClasses);
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

	private static String[] parseObjectClassNames(String[] item) {
		return Arrays.copyOfRange(item, 0, NUM_OBJCLASSES);
	}

	private static double[] parseUncertainties(String[] item)
			throws ArrayIndexOutOfBoundsException {
		double[] uncert = new double[NUM_OBJCLASSES];
		for (int i = 0; i < NUM_OBJCLASSES; i++) {
			uncert[i] = 100 * Double.parseDouble(item[NUM_OBJCLASSES + i].replace(
					',', '.'));
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
			List<String[]> items, Calendar[] dates, ObjectClass[] allObjClasses) {
		List<ChangeSequenceGroup> sequenceGroups = new ArrayList<ChangeSequenceGroup>();
		int numOccurrences = 0;
		int line = 0;
		try {
			int groupIndex = 0;
			sequenceGroups.add(new ChangeSequenceGroup());
			for (String[] item : items) {
				ChangeSequence sequence = new ChangeSequence();
				sequence.setDates(dates);
				sequence.setClasses(parseObjectClasses(item, allObjClasses));
				sequence.setUncertainties(parseUncertainties(item));
				if (!sequenceGroups.get(groupIndex).isSequenceFitting(sequence)) {
					groupIndex++;
					sequenceGroups.add(new ChangeSequenceGroup());
				} else {
					sequenceGroups.get(groupIndex).add(sequence);
				}
				line++;
			}
		} catch (Exception e) {
			LOG.error("Error parsing line " + line + ": " + e, e);
		}
		return sequenceGroups;
	}

	private static ObjectClass[] createObjectClasses(List<String[]> items) {
		Collection<String> objClassNames = new HashSet<String>();
		for (String[] item : items) {
			objClassNames.addAll(Arrays.asList(parseObjectClassNames(item)));
		}
		ObjectClass[] objClasses = new ObjectClass[objClassNames.size()];
		int i = 0;
		for (String className : objClassNames) {
			objClasses[i++] = new ObjectClass(className);
		}
		return objClasses;
	}

}
