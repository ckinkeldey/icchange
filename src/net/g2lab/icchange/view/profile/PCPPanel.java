package net.g2lab.icchange.view.profile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A panel showing spectral profiles as parallel plot.
 */
public class PCPPanel extends JPanel implements ChangeListener, MouseListener {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private double[][] values;
	private int[] highlighted;
	private double[] maxValues;
	private double[] minValues;
	private double[] uncertainties;
	private double thresholdA;
	private double thresholdB;

	public PCPPanel(double[][] spectralValues, double[] minValues,
			double[] maxValues, double[] uncertaintyValues) {
		super();
		this.values = spectralValues;
		this.uncertainties = uncertaintyValues;
		this.thresholdA = 0.0;
		this.thresholdB = 0.0;
		this.highlighted = filterHighlighted(thresholdA, uncertainties);
		this.minValues = minValues;
		this.maxValues = maxValues;
		this.setBackground(Color.WHITE);
	}

	private int[] filterHighlighted(double threshold,
			double[] valuesToBeFiltered) {
		int[] highlighted = new int[valuesToBeFiltered.length];
		double lowerThreshold;
		double upperThreshold;
		if (thresholdA > thresholdB) {
			lowerThreshold = thresholdB;
			upperThreshold = thresholdA;
		} else {
			lowerThreshold = thresholdA;
			upperThreshold = thresholdB;
		}
		LOG.debug("highlighting w thresholds " + lowerThreshold + " / "
				+ upperThreshold);
		for (int i = 0; i < valuesToBeFiltered.length; i++) {
			highlighted[i] = (valuesToBeFiltered[i] > lowerThreshold && valuesToBeFiltered[i] <= upperThreshold) ? 1
					: 0;
		}
		return highlighted;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int numBands = values.length;
		int elementWidth = (int) ((double) getWidth() / (numBands - 1));
		int n = 0;
		this.highlighted = filterHighlighted(thresholdA, uncertainties);
		for (int i = 0; i < values[0].length; i++) {
			if (!Double.isNaN(values[0][i]) && highlighted[i] <= 0) {
				drawProfile(i, values, uncertainties, highlighted, numBands,
						elementWidth, g2d);
				n++;
			}
		}
		for (int i = 0; i < values[0].length; i++) {
			if (!Double.isNaN(values[0][i]) && highlighted[i] > 0) {
				drawHighlighted(i, values, uncertainties, highlighted,
						numBands, elementWidth, g2d);
				n++;
			}
		}
		LOG.debug(n + " lines drawn.");

		// draw min values
		g2d.setColor(Color.DARK_GRAY);
		for (int i = 0; i < minValues.length; i++) {
			int x = i * elementWidth + 5;
			if (i == minValues.length - 1) {
				x -= 30;
			}
			g2d.drawString("" + minValues[i], x - 3, getHeight() - 10);
		}

		// draw max values
		g2d.setColor(Color.DARK_GRAY);
		for (int i = 0; i < maxValues.length; i++) {
			int x = i * elementWidth + 5;
			if (i == maxValues.length - 1) {
				x -= 30;
			}
			g2d.drawString("" + maxValues[i], x - 3, 30);
		}

		// draw labels
		for (int i = 0; i < values.length; i++) {
			int x = i * elementWidth;
			g2d.setColor(new Color(100, 100, 100, 255));
			g2d.drawLine(x, 0, x, getHeight());
			g2d.setColor(Color.WHITE);
			g2d.fillRect(x - 5, 0, 10, 20);
			g2d.setColor(new Color(100, 100, 100, 255));
			g2d.setFont(new Font("Arial", Font.BOLD, 12));
			if (i == 0) {
				x += 3;
				g2d.drawString("\u0394 band " + (i + 1), x - 3, 10);
			} else if (i == values.length - 1) {
				x -= 65;
				g2d.drawString("uncertainty", x - 3, 10);
			} else {
				g2d.drawString("\u0394 band " + (i + 1), x - 3, 10);
			}

		}
	}

	private void drawProfile(int index, double[][] values,
			double[] uncertainties, int[] highlighted, int numBands,
			int elementWidth, Graphics2D g2d) {
		int nPoints = numBands;
		int[] xPoints = new int[nPoints];
		int[] yPoints = new int[nPoints];
		int height = getHeight() - 20;
		for (int band = 0; band < numBands; band++) {
			int x1 = band * elementWidth;
			double diffY1 = values[band][index] - minValues[band];
			double range = maxValues[band] - minValues[band];
			int y1 = (int) (diffY1 / range * height);
			y1 = height - y1 + 20;
			xPoints[band] = x1;
			yPoints[band] = y1;
		}
		int red = 50;
		int green = 50;
		int blue = 100;// + (int) (1. - uncertainties[index]) * 155;
		g2d.setColor(new Color(red, green, blue, 50));
		g2d.setStroke(new BasicStroke(1));

		g2d.drawPolyline(xPoints, yPoints, nPoints);
		// }
	}

	private void drawHighlighted(int index, double[][] values,
			double[] uncertainties, int[] highlighted, int numBands,
			int elementWidth, Graphics2D g2d) {
		int nPoints = numBands;
		int[] xPoints = new int[nPoints];
		int[] yPoints = new int[nPoints];
		int height = getHeight() - 20;
		for (int band = 0; band < numBands; band++) {
			int x1 = band * elementWidth;
			double diffY1 = values[band][index] - minValues[band];
			int y1 = (int) (diffY1 / (maxValues[band] - minValues[band]) * (height));
			y1 = height - y1 + 20;
			xPoints[band] = x1;
			yPoints[band] = y1;
		}

		int blue = 0;
		int green = 0;
		// int red = 100 + (int) (1. - uncertainties[index]) * 155;
		int red = 255;
		g2d.setColor(new Color(red, green, blue, 150));
		g2d.setStroke(new BasicStroke(2));

		g2d.drawPolyline(xPoints, yPoints, nPoints);
	}

	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() instanceof VerticalThresholdLabelSlider) {
			this.thresholdB = getThreshold(((JSlider) evt.getSource())
					.getValue());
		} else {
			this.thresholdA = getThreshold(((JSlider) evt.getSource())
					.getValue());
		}
		LOG.debug("thresholds: " + thresholdA + " / " + thresholdB);
	}

	private double getThreshold(int value) {
		// if (value <= 50) {
		// return value / 1000.;
		// }
		return value / 100.;
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
		this.repaint();
		// LOG.debug("repaint");
	}

}
