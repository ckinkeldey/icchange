package net.g2lab.icchange.view.profile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class ProfileAreaInvPanel extends JPanel {

	private double[] minValues;
	private double[] maxValues;
	private double uncertainty;

	public ProfileAreaInvPanel(double[] minValues, double[] maxValues,
			double uncertainty) {
		this.minValues = minValues;
		this.maxValues = maxValues;
		this.uncertainty = uncertainty;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		double width = this.getSize().getWidth();
		double height = this.getSize().getHeight();
		int elementWidthPx = (int) Math.floor(width / minValues.length);
		int opacity = (int) ((1 - uncertainty) * 255);
		int i;
		Polygon minPolygon = new Polygon();
		minPolygon.addPoint(elementWidthPx / 2, (int) (height - 1));
		for (i = 0; i < minValues.length; i++) {
			int x = (int) Math.round(elementWidthPx / 2 + (i * elementWidthPx));
			int y = (int) Math.round((int) height - 1
					- (minValues[i] / 255. * height));
			minPolygon.addPoint(x, y);
		}
		minPolygon
				.addPoint((int) elementWidthPx / 2
						+ ((minValues.length - 1) * elementWidthPx),
						(int) (height - 1));

		Polygon maxPolygon = new Polygon();
		maxPolygon.addPoint(elementWidthPx / 2, (int) (height - 1));
		for (i = 0; i < maxValues.length; i++) {
			// int x = elementWidthPx / 2 + (maxValues.length-1-i) *
			// elementWidthPx;
			int x = Math.round(elementWidthPx / 2 + (i * elementWidthPx));
			int y = (int) ((int) height - 1 - (maxValues[i] / 255. * height));
			maxPolygon.addPoint(x, y);
		}
		maxPolygon
				.addPoint((int) elementWidthPx / 2
						+ ((maxValues.length - 1) * elementWidthPx),
						(int) (height - 1));

		g2.setColor(new Color(50, 50, 200, opacity));
		g2.fillRect(0, 0, (int) width, (int) height);

		g2.setColor(getBackground());
		g2.fillPolygon(maxPolygon);
		g2.setColor(new Color(100, 100, 100, 100));
		g2.drawPolygon(maxPolygon);

		g2.setColor(new Color(100, 100, 100, 100));
		g2.fillPolygon(minPolygon);

		g2.setColor(getBackground());
		g2.fillPolygon(minPolygon);
		g2.setColor(new Color(50, 50, 200, opacity));
		// minPolygon.translate(0, 1);
		g2.fillPolygon(minPolygon);

		g2.setColor(new Color(100, 100, 100, 100));
		// int thickness = (int) (height/50);
		// g.fillRect(0, (int)(height/2)-1, (int)width, thickness);
		g2.drawRect(0, 0, (int) width - 1, (int) height - 1);
	}

}
