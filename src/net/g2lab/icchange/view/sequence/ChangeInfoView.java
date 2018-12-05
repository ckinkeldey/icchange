/**
 * 
 */
package net.g2lab.icchange.view.sequence;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ListSelectionListener;

import net.g2lab.icchange.model.ChangeSequenceGroup;
import net.g2lab.icchange.view.map.MapView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class ChangeInfoView extends JFrame implements ActionListener {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private static final String INFOVIEW_TITLE = "ICChange | info view";

	private SequenceListPanel seqListPanel;
	private JScrollPane scrollPane;

	private JButton filterButton;

	private JComboBox<String> sortBox;

	private JPanel filterPanel;

	private JButton resetButton;

	private JSlider uSlider;

	private List<? extends Image> icons;

	public ChangeInfoView(GridCoverage2D[] lc, GridCoverage2D[] u, String[] classLabels, int[] classCodes, Calendar[] dates) {
		try {

			this.seqListPanel = SequenceListPanel.createSequenceViewFromCoverages(
					lc, u, dates, classLabels, classCodes);
			
			this.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;

			String[] elements = { "Sort by amount of change",
					"Sort by mean uncertainty", "Sort by class of date 1",
					"Sort by class of date 2", "Sort by class of date 3" };
			this.sortBox = new JComboBox<String>(elements);
			sortBox.addActionListener(this);
			c.gridx = 0;
			c.gridy = 0;
			c.weightx=1;
			c.weighty=0;
			add(sortBox, c);
			
			this.scrollPane = new JScrollPane(seqListPanel);
			c.gridx = 0;
			c.gridy = 2;
			c.weighty=1;
			c.fill = GridBagConstraints.BOTH;
			this.add(scrollPane, c);

			this.filterPanel = new JPanel();
			filterPanel.setLayout(new BorderLayout());
			
			this.uSlider = new JSlider(0, 100, 100);
			uSlider.setSnapToTicks(true);
			uSlider.setMajorTickSpacing(25);
			uSlider.setPaintTicks(true);
			uSlider.setPaintLabels(true);
			uSlider.setBorder(BorderFactory.createTitledBorder("uncertainty"));
			filterPanel.add("North", uSlider);
			
			this.filterButton = new JButton("filter");
			this.resetButton = new JButton("reset");
			filterButton.addActionListener(this);
			resetButton.addActionListener(this);
			filterPanel.add("Center", filterButton);
			filterPanel.add("East", resetButton);
			c.gridx = 0;
			c.gridy = 3;
			c.weighty=0;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(filterPanel, c);
			
			this.setTitle(INFOVIEW_TITLE);
			this.setIconImages(icons);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.pack();
		} catch (IOException e) {
			LOG.error(e, e);
		} catch (ParseException e) {
			LOG.error(e, e);
		}
	}

	public Collection<ChangeSequenceGroup> getChanges() {
		return seqListPanel.getChangeList();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(filterButton)) {
			LOG.info("filter");
			seqListPanel.filterChangeSequenceGroups();
//			firePropertyChange("changelist", listPanel.getChangeList(), listPanel.getChangeList());
			seqListPanel.repaint();
		} else if (e.getSource().equals(resetButton)) {
			LOG.info("reset");
			uSlider.setValue(100);
			seqListPanel.resetChangeSequenceGroups();
			seqListPanel.setSortBy(SequenceListPanel.SORT_BY[sortBox.getSelectedIndex()]);
			seqListPanel.updateList();
			firePropertyChange("changelist", seqListPanel.getChangeList(), seqListPanel.getChangeList());
			seqListPanel.repaint();
		} else if (e.getSource().equals(sortBox)) {
			LOG.info("sort");
			seqListPanel.setSortBy(SequenceListPanel.SORT_BY[sortBox
					.getSelectedIndex()]);
			seqListPanel.updateList();
			seqListPanel.repaint();
		}
	}

	public void addListDataListener(MapView mapView) {
		this.seqListPanel.addListDataListener(mapView);

	}

	public void addListSelectionListener(ListSelectionListener listener) {
		this.seqListPanel.getList().addListSelectionListener(listener);
		
	}

	public int getUThreshold() {
		return uSlider.getValue();
	}

	public Component getChooser() {
		return sortBox;
	}

}
