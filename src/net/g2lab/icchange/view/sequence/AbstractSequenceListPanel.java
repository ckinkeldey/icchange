package net.g2lab.icchange.view.sequence;

import java.awt.Color;
import java.util.Calendar;
import java.util.List;

import javax.swing.JPanel;

import net.g2lab.icchange.model.ChangeSequenceGroup;
import net.g2lab.icchange.model.ObjectClass;
import net.g2lab.icchange.view.style.LayerStyles;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractSequenceListPanel extends JPanel {

	private static final Log LOG = LogFactory.getLog(AbstractSequenceListPanel.class);

	protected static final Color COLOR_BACKGROUND = new Color(100, 100, 100);

	protected static final Color[] COLORS = new Color[] { new Color(200, 0, 0),
			new Color(0, 200, 0), new Color(0, 0, 200), new Color(100, 100, 0),
			new Color(100, 0, 100), new Color(0, 100, 100),
			new Color(33, 33, 33) };

	protected List<ChangeSequenceGroup> sequenceGroups;
	protected int overallNumSequences = 0;
	protected int maxNumSequences = 0;
	
	protected ObjectClass[] objClasses;
	protected Color[] colors;

	protected SequenceListModel listModel;

	protected Calendar[] dates;
	
	public AbstractSequenceListPanel(List<ChangeSequenceGroup> sequenceGroups,
			ObjectClass[] objClasses) {
		this.sequenceGroups = sequenceGroups;
		this.objClasses = objClasses;
		this.colors = LayerStyles.createColors(LayerStyles.colorCurrent);
		this.dates = sequenceGroups.get(0).getDates();
		this.overallNumSequences = countSequences(sequenceGroups);
		LOG.debug("overall # sequences: " + overallNumSequences);
		this.maxNumSequences = getMaxSequences(sequenceGroups);
		LOG.debug("max # sequences: " + overallNumSequences);

		initPanel();

		this.setFocusable(true);
	}

	private int countSequences(List<ChangeSequenceGroup> sequenceGroups) {
		int count = 0;
		for (ChangeSequenceGroup group : sequenceGroups) {
			count += group.size();
		}
		return count;
	}
	
	private int getMaxSequences(List<ChangeSequenceGroup> sequenceGroups) {
		int max = 0;
		for (ChangeSequenceGroup group : sequenceGroups) {
			max = group.size() > max ? group.size() : max;
		}
		return max;
	}

	protected abstract void initPanel();

	public static Color[] createRandomColors(int num) {
		Color[] colors = new Color[num];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = new Color(((int) (255 * Math.random())),
					((int) (255 * Math.random())),
					((int) (255 * Math.random())));
		}
		return colors;
	}
	
	protected static double[] computeRelativeDistances(Calendar[] calendars) {
		double[] distances = new double[calendars.length];
		double overallDeltaTime = Math.abs(calendars[0].getTimeInMillis()
				- calendars[calendars.length - 1].getTimeInMillis());
		for (int i = 0; i < calendars.length - 1; i++) {
			double deltaTime = Math.abs(calendars[i + 1].getTimeInMillis()
					- calendars[i].getTimeInMillis());
			distances[i] = deltaTime / overallDeltaTime;
		}
		distances[distances.length - 1] = 0;
		return distances;
	}

}
