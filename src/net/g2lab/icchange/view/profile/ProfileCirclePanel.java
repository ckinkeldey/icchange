package net.g2lab.icchange.view.profile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class ProfileCirclePanel extends JPanel {

	private double[] minValues;
	private double[] maxValues;
	private double uncertainty;

	public ProfileCirclePanel(double[] minValues, double[] maxValues,
			double uncertainty) {
		this.minValues = minValues;
		this.maxValues = maxValues;
		this.uncertainty = uncertainty;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		double width = this.getSize().getWidth();
		double height = this.getSize().getHeight();
		int elementWidthPx = (int) Math.floor(width / minValues.length);
		int opacity = (int) ((1 - uncertainty) * 255);
		g2d.setColor(new Color(100, 100, 200, opacity));
		g2d.fillRect(0, 0, (int) width - 1, (int) height - 1);
		for (int i = 0; i < minValues.length; i++) {
			int x = i * elementWidthPx;
			int minCircleSize = (int) (minValues[i] / 255. * elementWidthPx);
			int maxCircleSize = (int) (maxValues[i] / 255. * elementWidthPx);
			int xMax = x + (elementWidthPx / 2 - maxCircleSize / 2);
			int y = (int) (height / 2 - maxCircleSize / 2);
			g2d.setColor(getBackground());
			g2d.fillOval(xMax, (int) y, maxCircleSize, maxCircleSize);
			g.setColor(new Color(100, 100, 100, 100));
			g2d.drawOval(xMax, (int) y, maxCircleSize, maxCircleSize);

			g2d.setColor(new Color(100, 100, 200, opacity));
			int xMin = x + (elementWidthPx / 2 - minCircleSize / 2);
			y = (int) (height / 2 - minCircleSize / 2);
			g2d.fillOval(xMin, (int) y, minCircleSize, minCircleSize);
			g.setColor(new Color(100, 100, 100, 100));
			g2d.drawOval(xMin, (int) y, minCircleSize, minCircleSize);
		}

		g.setColor(new Color(100, 100, 100, 100));
		int thickness = (int) (height / 50);
		// g.fillRect(0, (int)(height/2)-1, (int)width, thickness);
		g.drawRect(0, 0, (int) width - 1, (int) height - 1);
	}

}
