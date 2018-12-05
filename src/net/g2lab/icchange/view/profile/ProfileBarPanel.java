package net.g2lab.icchange.view.profile;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ProfileBarPanel extends JPanel {

	private double[] minValues;
	private double[] maxValues;
	private double uncertainty;

	public ProfileBarPanel(double[] minValues, double[] maxValues,
			double uncertainty) {
		this.minValues = minValues;
		this.maxValues = maxValues;
		this.uncertainty = uncertainty;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		double width = this.getSize().getWidth();
		double height = this.getSize().getHeight();
		int elementWidthPx = (int) Math.floor(width / minValues.length);
		int opacity = (int) ((1 - uncertainty) * 255);
		for (int i = 0; i < minValues.length; i++) {
			int x = i * elementWidthPx;
			int elementY0 = (int) (minValues[i] / 255. * height);
			int elementY1 = (int) (maxValues[i] / 255. * height);
			g.setColor(new Color(100, 100, 200, opacity));
			g.fillRect(x, elementY0, elementWidthPx, elementY1 - elementY0);
			g.setColor(new Color(100, 100, 100, 255));
			g.drawRect(x, elementY0, elementWidthPx, elementY1 - elementY0);
		}

		g.setColor(new Color(100, 100, 100, 100));
		int thickness = (int) (height / 50);
		// g.fillRect(0, (int)(height/2)-1, (int)width, thickness);
		g.drawRect(0, 0, (int) width - 1, (int) height - 1);
	}

}
