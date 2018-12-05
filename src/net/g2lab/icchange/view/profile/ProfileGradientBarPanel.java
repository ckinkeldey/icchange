package net.g2lab.icchange.view.profile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

public class ProfileGradientBarPanel extends JPanel {

	private double[] minValues;
	private double[] maxValues;
	private double uncertainty;

	public ProfileGradientBarPanel(double[] minValues, double[] maxValues,
			double uncertainty) {
		this.minValues = minValues;
		this.maxValues = maxValues;
		this.uncertainty = uncertainty;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		double width = this.getSize().getWidth();
		double height = this.getSize().getHeight();
		int elementWidthPx = (int) Math.floor(width / minValues.length);
		Color baseColor = new Color(255, 255, 255);
		int opacity = (int) ((1 - uncertainty) * 255);
		for (int i = 0; i < minValues.length; i++) {
			int x = i * elementWidthPx;
			int min = (int) minValues[i];
			int max = (int) maxValues[i];
			g.setColor(new Color(100, 100, 200, opacity));
			Point2D point0 = new Point2D.Float(x, 0);
			Color color0 = new Color(baseColor.getRed(), baseColor.getGreen(),
					min);
			Point2D point1 = new Point2D.Float(x, (float) (height - 1));
			Color color1 = new Color(max, baseColor.getGreen(), baseColor
					.getBlue());
			g2d.setPaint(new GradientPaint(point0, color1, point1, color0));
			g2d.fillRect(x, 0, elementWidthPx, (int) (height - 1));
			g2d.setColor(new Color(100, 100, 100, 255));
			g2d.drawRect(x, 0, elementWidthPx, (int) (height - 1));
		}

		g2d.setColor(new Color(opacity, opacity, 255, 255));
		// int thickness = (int) (opacity * 2);
		g2d.setStroke(new BasicStroke(3));
		g2d.drawRect(0, 0, (int) width - 1, (int) height - 1);
	}

}
