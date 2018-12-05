package net.g2lab.icchange.view.profile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RangePanel extends JPanel implements ChangeListener {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	double lowerValue = 0;
	double upperValue = 0;

	private double thresholdA, thresholdB;

	public RangePanel() {
		super();
		setPreferredSize(new Dimension(10, 200));
	}

	public void setLowerValue(double value) {
		lowerValue = value;
	}

	public void setUpperValue(double value) {
		upperValue = value;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (thresholdA > thresholdB) {
			lowerValue = thresholdB;
			upperValue = thresholdA;
		} else {
			lowerValue = thresholdA;
			upperValue = thresholdB;
		}
		int height = (int) (getHeight() * 1);
		int delta = (int) (.5 * (getHeight() - height));
		int width = 8;
		int x = (int) (getWidth() / 2. - width / 2.);
		int yLow = (height + delta) - (int) ((lowerValue) * height);
		int yHi = (height + delta) - (int) ((upperValue) * height);
		g.setColor(Color.RED);
		g.fillRect(x, yHi, width, yLow - yHi);
	}

	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() instanceof VerticalThresholdLabelSlider) {
			this.thresholdB = ((JSlider) evt.getSource()).getValue() / 100.;
		} else {
			this.thresholdA = ((JSlider) evt.getSource()).getValue() / 100.;
		}
		repaint();
		LOG.debug("thresholds: " + thresholdA + " / " + thresholdB);
	}

}
