package net.g2lab.icchange.view.sequence;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

import javax.swing.JPanel;

import net.g2lab.icchange.model.ChangeSequence;
import net.g2lab.icchange.model.ObjectClass;

public class LineSequenceView extends JPanel {

	private Map<Integer, ChangeSequence> patterns;
	private ObjectClass[] objClasses;

	// private Object xPositions;

	public LineSequenceView(Map<Integer, ChangeSequence> patterns,
			ObjectClass[] objClasses) {
		this.patterns = patterns;
		this.objClasses = objClasses;
		this.setBackground(new Color(10, 10, 10));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// int height = getHeight() / pattern.getClasses().length;
		int[] heights = computeHeights(patterns.get(0).getDates());
		for (ChangeSequence pattern : patterns.values()) {
			drawChangePattern(pattern, g, heights);
		}
	}

	private int[] computeHeights(Calendar[] dates) {
		int[] heights = new int[dates.length - 1];
		double overallDelta = dates[dates.length - 1].getTimeInMillis()
				- dates[0].getTimeInMillis();
		for (int i = 0; i < dates.length - 1; i++) {
			double delta = dates[i + 1].getTimeInMillis()
					- dates[i].getTimeInMillis();
			heights[i] = (int) ((delta / overallDelta) * getHeight());
		}
		return heights;
	}

	private void drawChangePattern(ChangeSequence pattern, Graphics g,
			int[] heights) {
		int y = 0;
		for (int i = 0; i < pattern.getObjClasses().length - 1; i++) {
			ObjectClass objectClass0 = pattern.getObjClasses()[i];
			ObjectClass objectClass1 = pattern.getObjClasses()[i + 1];
			int x0 = getX(objectClass0, pattern.getObjClasses());
			int x1 = getX(objectClass1, pattern.getObjClasses());
			g.setColor(new Color(255, 255, 255, (int) (pattern
					.getUncertainties()[i] * 255)));
			g.drawLine(x0, y, x1, y + heights[i]);
			y += heights[i];
		}
	}

	protected int getX(ObjectClass objectClass, ObjectClass[] classes) {
		int index = Arrays.asList(classes).indexOf(objectClass);
		return (int) (index * (this.getWidth() / (double) classes.length));
	}

}
