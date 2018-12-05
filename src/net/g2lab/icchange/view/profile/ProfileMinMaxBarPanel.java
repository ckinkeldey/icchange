package net.g2lab.icchange.view.profile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class ProfileMinMaxBarPanel extends JPanel {

	private double[] minValues;
	private double[] maxValues;
	private double uncertainty;

	public ProfileMinMaxBarPanel(double[] minValues, double[] maxValues,
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
		Color baseColor = new Color(255, 255, 255);
		int opacity = (int) ((1 - uncertainty) * 255);
		for (int i = 0; i < minValues.length; i++) {
			int x = i * elementWidthPx;
			int min = (int) minValues[i];
			int max = (int) maxValues[i];
			// g.setColor(new Color(100, 100, 200, opacity));
			Color color0 = new Color(100, 100, baseColor.getBlue(), max);
			Color color1 = new Color(100, 100, baseColor.getBlue(), min);
			g2d.setColor(color1);
			g2d.fillRect(x, 0, elementWidthPx, (int) (height / 2 - 1));
			g2d.setColor(color0);
			g2d.fillRect(x, (int) (height / 2 - 1), elementWidthPx,
					(int) (height - 1));
			g2d.setColor(new Color(100, 100, 100, 100));
			g2d.drawRect(x, 0, elementWidthPx, (int) (height - 1));
		}
		g2d.setColor(new Color(100, 100, 100 + (int) (opacity / 255. * 100),
				255));
		// int thickness = (int) (opacity * 2);
		g2d.setStroke(new BasicStroke((float) (opacity / 255. * 5)));
		g2d.drawRect(0, 0, (int) width - 1, (int) height - 1);
	}

}
