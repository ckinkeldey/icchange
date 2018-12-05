package net.g2lab.icchange.view.sequence;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.g2lab.icchange.model.ChangeSequenceGroup;
import net.g2lab.icchange.model.ObjectClass;
import net.g2lab.icchange.view.map.MapView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;

/**
 * A panel showing the list of change sequences.
 * 
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class SequenceListPanel extends AbstractSequenceListPanel implements
		ListCellRenderer, KeyListener {

	private static final Log LOG = LogFactory.getLog(SequenceListPanel.class);

	private static final int CELL_HEIGHT = 25;
	private static final int VERTICAL_GAP = 3;

	private static final int SORT_NO = -1;
	private static final int SORT_NUM_ITEMS = 0;
	private static final int SORT_MEAN_UNCERTAINTY = 1;
	private static final int SORT_SEQUENCE_0 = 2;
	private static final int SORT_SEQUENCE_1 = 3;
	private static final int SORT_SEQUENCE_2 = 4;

	protected static final int[] SORT_BY = new int[] { SORT_NUM_ITEMS,
			SORT_MEAN_UNCERTAINTY, SORT_SEQUENCE_0, SORT_SEQUENCE_1,
			SORT_SEQUENCE_2 };

	private static final Color COLOR_BACKGROUND = new Color(0, 0, 0);
	private static final Color COLOR_BG_ROW = new Color(100, 100, 100);

	private double[] distances;
	private JList<ChangeSequenceGroup> sequenceJList;
	// private ChangeSequenceGroup[] currentElements;
	private int sortBy = SORT_NO;
	
	private HashSet<ObjectClass[]> filteredObjClasses = new HashSet<ObjectClass[]>();

	boolean filter = false;

	public SequenceListPanel(List<ChangeSequenceGroup> sequenceGroups,
			ObjectClass[] objClasses) {
		super(sequenceGroups, objClasses);
		this.addKeyListener(this);
	}
	
	public int getSortBy() {
		return sortBy;
	}

	public void setSortBy(int sortBy) {
		this.sortBy = sortBy;
	}

	public void filterChangeSequenceGroups() {
		List<ChangeSequenceGroup> selected = sequenceJList
				.getSelectedValuesList();
		if (selected.size() > 0) {
			for (int i = listModel.getSize() - 1; i >= 0; i--) {
				ChangeSequenceGroup element = listModel.get(i);
				if (!(selected.contains(element))) {
					listModel.removeElement(element);
				}
			}
		}
		// filter = true;
		listModel.fireContentsChanged(sequenceJList, 0, 1);
		this.sequenceJList.setSelectedIndices(new int[0]);

	}

	public void resetChangeSequenceGroups() {
		listModel.removeAllElements();
		for (ChangeSequenceGroup element : sequenceGroups) {
			listModel.addElement(element);
		}
		listModel.fireContentsChanged(sequenceJList, 0, 1);
	}

	/**
	 * @return the filteredObjClasses
	 */
	public HashSet<ObjectClass[]> getFilteredObjClasses() {
		this.filteredObjClasses.clear();
		Object[] filteredGroups = sequenceJList.getSelectedValues();
		for (Object object : filteredGroups) {
			ChangeSequenceGroup group = (ChangeSequenceGroup) object;
			this.filteredObjClasses.add(group.getObjClasses());
		}
		LOG.debug("filtered obj classes: " + filteredObjClasses.size());
		return filteredObjClasses;
	}

	public static SequenceListPanel createSequenceViewFromCSV(
			InputStream inputStream) throws IOException, ParseException {
		SequenceCSVParser parser = new SequenceCSVParser(inputStream);
		List<ChangeSequenceGroup> seqGroups = parser.getSequenceGroups();
		ObjectClass[] objClasses = parser.getObjClasses();
		return new SequenceListPanel(seqGroups, objClasses);
	}

	public static SequenceListPanel createSequenceViewFromDBF(
			InputStream inputStream, Calendar[] dates, String[] objClassAttr,
			String[] classcodeAttr, String[] uncertaintyAttr)
			throws IOException, ParseException {
		SequenceDBFParser parser = new SequenceDBFParser(inputStream, dates,
				objClassAttr, classcodeAttr, uncertaintyAttr);
		List<ChangeSequenceGroup> seqGroups = parser.getSequenceGroups();
		ObjectClass[] objClasses = parser.getObjClasses();
		return new SequenceListPanel(seqGroups, objClasses);
	}

	public static SequenceListPanel createSequenceViewFromCoverages(
			GridCoverage2D[] lc, GridCoverage2D[] u, Calendar[] dates,
			String[] classNames, int[] classCodes) throws IOException,
			ParseException {
		SequenceCoverageParser parser = new SequenceCoverageParser(lc, u,
				dates, classNames, classCodes);
		List<ChangeSequenceGroup> seqGroups = parser.getSequenceGroups();
		ObjectClass[] objClasses = parser.getObjClasses();
		return new SequenceListPanel(seqGroups, objClasses);
	}

	protected void initPanel() {
		// this.setLayout(new GridLayout(sequenceGroups.size(),1, 0,
		// VERTICAL_GAP));
		this.setLayout(new BorderLayout());
		this.setBackground(COLOR_BACKGROUND);

		this.add(createLabelPanel(), BorderLayout.NORTH);

		this.distances = computeRelativeDistances(sequenceGroups.get(0)
				.getDates());

		// currentElements = sequenceGroups.toArray(new ChangeSequenceGroup[0]);
		this.listModel = new SequenceListModel();
		for (ChangeSequenceGroup sequenceGroup : sequenceGroups) {
			listModel.addElement(sequenceGroup);
		}
		sequenceJList = new JList(listModel);
		sequenceJList.setBackground(SequencePanel.COLOR_BG);
		sequenceJList.setCellRenderer(this);
		sequenceJList.setFixedCellWidth(300);
		sequenceJList.setFixedCellHeight(CELL_HEIGHT);
		sequenceJList.setBorder(new EmptyBorder(0, 10, 0, 0));
		sequenceJList.setDragEnabled(true);
		sequenceJList.setAutoscrolls(true);

		updateList();

		this.add(sequenceJList, BorderLayout.CENTER);

	}

	private JPanel createLabelPanel() {
		JPanel labelPanel = new JPanel();
		labelPanel.setBackground(SequencePanel.COLOR_BG);
		labelPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(0, 10, 0, 0);
		c.fill = GridBagConstraints.VERTICAL;
		JLabel amountLabel = createLabel("amount");
		amountLabel.setSize(100, 30);
		labelPanel.add(amountLabel, c);
		c.gridx = 1;
		c.insets = new Insets(0, 40, 0, 0);
		
		labelPanel.add(createLabel(getDateString(dates[0])), c);
		if (dates.length <= 2) {
			c.gridx = 2;
			c.insets = new Insets(0, 110, 0, 0);
			labelPanel.add(createLabel(getDateString(dates[1])), c);
		} else {
			c.gridx = 2;
			c.insets = new Insets(0, 40, 0, 0);
			labelPanel.add(createLabel(getDateString(dates[1])), c);
			c.gridx = 3;
			c.insets = new Insets(0, 10, 0, 0);
			labelPanel.add(createLabel(getDateString(dates[2])), c);
		}
		c.gridx = 4;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.NONE;
//		c.anchor = GridBagConstraints.EAST; 
		JLabel uncertaintyLabel = createLabel("uncertainty");
		uncertaintyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		uncertaintyLabel.setSize(100, 10);
		labelPanel.add(uncertaintyLabel, c);
		
		c.gridy = 1;
		JLabel uncertScaleLabel = new JLabel("0%                 100%", SwingConstants.RIGHT);
		uncertScaleLabel.setForeground(Color.WHITE);
		uncertScaleLabel.setFont(uncertScaleLabel.getFont().deriveFont(10.f));
		labelPanel.add(uncertScaleLabel, c);
		
		return labelPanel;
	}

	private String getDateString(Calendar date) {
		return ""+date.get(Calendar.YEAR);
	}

	private JLabel createLabel(String string) {
		JLabel label = new JLabel(string, SwingConstants.LEFT);
		label.setSize(50, 30);
		// label.setBackground(Color.BLUE);
		// label.setOpaque(true);
		label.setForeground(Color.WHITE);
		return label;
	}

	public void updateList() {
		ChangeSequenceGroup[] groupsArray = new ChangeSequenceGroup[listModel
				.getSize()];
		listModel.copyInto(groupsArray);
		ChangeSequenceGroup[] listElements = sortArray(groupsArray);
		this.listModel.clear();
		for (ChangeSequenceGroup element : listElements) {
			this.listModel.addElement(element);
		}
		sequenceJList.setModel(listModel);
	}

	private ChangeSequenceGroup[] sortArray(ChangeSequenceGroup[] array) {
		if (sortBy == SORT_NUM_ITEMS) {
			Arrays.sort(array, new NumItemsComparator(false));
		} else if (sortBy == SORT_MEAN_UNCERTAINTY) {
			Arrays.sort(array, new MeanUncertaintyComparator(true));
		} else if (sortBy == SORT_SEQUENCE_0) {
			Arrays.sort(array, new SequenceComparator(0, false));
		} else if (sortBy == SORT_SEQUENCE_1) {
			Arrays.sort(array, new SequenceComparator(1, false));
		} else if (sortBy == SORT_SEQUENCE_2) {
			Arrays.sort(array, new SequenceComparator(2, false));
		}
		return array;
	}

	class NumItemsComparator implements Comparator<ChangeSequenceGroup> {

		private boolean minToMax = true;

		public NumItemsComparator(boolean minToMax) {
			this.minToMax = minToMax;
		}

		@Override
		public int compare(ChangeSequenceGroup o1, ChangeSequenceGroup o2) {
			if (o1 == null || o2 == null) {
				return 0;
			} else if (o1.size() < o2.size()) {
				return minToMax ? -1 : 1;
			} else if (o1.size() > o2.size()) {
				return minToMax ? 1 : -1;
			} else {
				return 0;
			}
		}

	}

	class MeanUncertaintyComparator implements Comparator<ChangeSequenceGroup> {

		private boolean minToMax = true;

		public MeanUncertaintyComparator(boolean minToMax) {
			this.minToMax = minToMax;
		}

		@Override
		public int compare(ChangeSequenceGroup o1, ChangeSequenceGroup o2) {
			if (o1 == null || o2 == null) {
				return 0;
			} else if (o1.getMeanUncertainty() < o2.getMeanUncertainty()) {
				return minToMax ? -1 : 1;
			} else if (o1.getMeanUncertainty() > o2.getMeanUncertainty()) {
				return minToMax ? 1 : -1;
			} else {
				return 0;
			}
		}

	}

	class SequenceComparator implements Comparator<ChangeSequenceGroup> {

		private boolean minToMax = true;
		private int index;

		public SequenceComparator(int objClassIndex, boolean minToMax) {
			this.index = objClassIndex;
			this.minToMax = minToMax;
		}

		@Override
		public int compare(ChangeSequenceGroup o1, ChangeSequenceGroup o2) {
			if (o1 == null || o2 == null) {
				return 0;
			} else if (o1.getObjClasses()[index].getClasscode() < o2
					.getObjClasses()[index].getClasscode()) {
				return minToMax ? -1 : 1;
			} else if (o1.getObjClasses()[index].getClasscode() > o2
					.getObjClasses()[index].getClasscode()) {
				return minToMax ? 1 : -1;
			} else {
				return 0;
			}
		}

	}

	public static Color[] createRandomColors(int num) {
		Color[] colors = new Color[num];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = new Color(((int) (255 * Math.random())),
					((int) (255 * Math.random())),
					((int) (255 * Math.random())));
		}
		return colors;
	}

	private int sumUp(int[] array) {
		int sum = 0;
		for (int number : array) {
			sum += number;
		}
		return sum;
	}

	private void drawLines(Graphics g, int numLines, int size) {
		int x = 0;
		g.setColor(COLOR_BACKGROUND);
		for (int i = 0; i < numLines + 1; i++) {
			g.fillRect(0, x, getWidth(), 1);
			x += size;
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
			LOG.debug("key right");
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {

	}

	@Override
	public void keyTyped(KeyEvent event) {

	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		JPanel cell = new JPanel();
		cell.setBackground(COLOR_BACKGROUND);
		cell.setLayout(new BorderLayout());

		ChangeSequenceGroup cSGroup = (ChangeSequenceGroup) value;
		SequencePanel sequencePanel = new SequencePanel(cSGroup, colors, distances);
		sequencePanel.setSelected(isSelected);

		double normalizedNumItems = ((double) cSGroup.size()) / maxNumSequences;
		NumLabel numLabel = new NumLabel(normalizedNumItems);
		numLabel.setPreferredSize(new Dimension(100, CELL_HEIGHT));
		numLabel.setSelected(isSelected);

		double[] allMaxUncertainties = cSGroup.getAllMaxUncertainties();
		double meanUncertainty = cSGroup.getMeanUncertainty();
		double stddevUncertainty = cSGroup.getStdDevUncertainty();
		StatsLabel statsLabel = new StatsLabel(allMaxUncertainties,
				meanUncertainty, stddevUncertainty);
		statsLabel.setPreferredSize(new Dimension(100, CELL_HEIGHT));
		statsLabel.setSelected(isSelected);

		cell.setLayout(new BorderLayout());
		cell.setBorder(new EmptyBorder(VERTICAL_GAP, VERTICAL_GAP,
				VERTICAL_GAP, VERTICAL_GAP));
		cell.add(numLabel, BorderLayout.WEST);
		cell.add(sequencePanel, BorderLayout.CENTER);
		cell.add(statsLabel, BorderLayout.EAST);

		cell.setToolTipText(cSGroup.toString());

		return cell;
	}

	class NumLabel extends JLabel {

		private Color bgColor = COLOR_BG_ROW;
		private double normalizedNum;

		public NumLabel(double normalizedNum) {
			this.normalizedNum = normalizedNum;
			this.setOpaque(true);
			this.setToolTipText("100");
		}

		public void setSelected(boolean isSelected) {
			this.bgColor = isSelected ? SequencePanel.COLOR_BG_SELECTED
					: COLOR_BG_ROW;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int borderPx = 10;
			int widthPx = getWidth() - borderPx * 2;
			int columnPx = (int) Math.ceil(normalizedNum * (widthPx));
			g.setColor(bgColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.WHITE);
			g.fillRect(getWidth() - 1 - borderPx - columnPx, 0, columnPx,
					getHeight());
		}

	}

	class StatsLabel extends JLabel {

		private Color bgColor = COLOR_BG_ROW;
		private double mean;
		private double stddev;
		private double[] allValues;

		public StatsLabel(double[] allValues, double mean, double stddev) {
			this.allValues = allValues;
			this.mean = mean;
			this.stddev = stddev;
			this.setOpaque(true);
			NumberFormat nf = NumberFormat.getNumberInstance();
			this.setToolTipText("uncertainty: " + nf.format(mean) + "+/-"
					+ nf.format(stddev));
		}

		public void setSelected(boolean isSelected) {
			this.bgColor = isSelected ? SequencePanel.COLOR_BG_SELECTED
					: COLOR_BG_ROW;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int borderPx = 10;
			int widthPx = getWidth() - 2 * borderPx;
			int meanPx = (int) (mean / 100 * widthPx);
			int stddevMinPx = (int) ((mean - stddev) / 100 * widthPx);
			int stddevMaxPx = (int) ((mean + stddev) / 100 * widthPx);
			g.setColor(bgColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			// orientation line on the left
			g.setColor(new Color(255, 255, 255));
			g.drawLine(borderPx, 0, borderPx, getHeight());
			// orientation line on the right
			g.drawLine(getWidth() - borderPx, 0, getWidth() - borderPx, getHeight());
			
			g.setColor(new Color(255, 255, 255));
			g.fillRect(borderPx + meanPx - 1, 0, 2, getHeight());
//			g.fillRect(borderPx + stddevMinPx, getHeight() / 2 - 1, stddevMaxPx
//					- stddevMinPx, 2);
			
			// stddev
			g.drawLine(borderPx + stddevMinPx, getHeight() / 2 , borderPx + stddevMaxPx, getHeight() / 2 );
			//whiskers
			g.drawLine(borderPx + stddevMinPx, getHeight() / 2 - 2, borderPx + stddevMinPx, getHeight() / 2 + 2);
			g.drawLine(borderPx + stddevMaxPx, getHeight() / 2 - 2, borderPx + stddevMaxPx, getHeight() / 2 + 2);
//			g.fillRect(borderPx + stddevMinPx, getHeight()/2-2, 2, 4);	
//			g.drawLine(borderPx + meanPx - 1, 0, borderPx + meanPx - 1, getHeight());
//			g.drawLine(borderPx + stddevMinPx, getHeight()/2, stddevMaxPx - stddevMinPx, getHeight()/2);
//			g.drawLine(borderPx + stddevMinPx, 0, stddevMaxPx-stddevMinPx, getHeight()-1);
//			g.drawLine(borderPx + stddevMinPx, 0, stddevMaxPx-stddevMinPx, getHeight()-1);
		}

	}

	public void addListDataListener(MapView mapView) {
		this.listModel.addListDataListener(mapView);
	}

	public List<ChangeSequenceGroup> getChangeList() {
		List<ChangeSequenceGroup> groups = new ArrayList<ChangeSequenceGroup>();
		Enumeration<ChangeSequenceGroup> listElements = listModel.elements();
		int index = 0;
		while (listElements.hasMoreElements()) {
			ChangeSequenceGroup group = listElements.nextElement();
			// if (sequenceJList.isSelectedIndex(index)) {
			groups.add(group);
			// }
			index++;
		}
		return groups;
	}

	public JList<ChangeSequenceGroup> getList() {
		return this.sequenceJList;
	}

}
