/**
 * 
 */
package net.g2lab.icchange.view.histogram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Arrays;

import javax.swing.JComponent;

import org.apache.commons.math.stat.descriptive.rank.Max;

/**
 * @author <a href="mailto:christoph at kinkeldey dot de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class HistogramVerticalPanel extends JComponent {

	private double[][] values;

	public HistogramVerticalPanel(double[][] histogramValues) {
		this.values = histogramValues;

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		double width = this.getSize().getWidth();
		double height = this.getSize().getHeight();

		int x0 = 0;
		int y0 = (int) height;

		int band = 1;
		int colWidth = (int) width;
		int numSteps = 50;
		double lineHeight = height / numSteps;
		double step = values[band].length / numSteps;
		double maxValue = new Max().evaluate(values[band]);
		int arrayIndex = 0;
		for (int i = 0; i < numSteps; i++) {
			double[] range = Arrays.copyOfRange(values[band], arrayIndex,
					(int) (arrayIndex + step));
			int value = (int) new Max().evaluate(range);
			Color color = determineColor(value, maxValue);
			g2d.setColor(color);
			g2d.fillRect(x0, y0, (int) colWidth, (int) lineHeight);
			y0 -= (int) lineHeight;
			arrayIndex += step;
		}
		g2d.setColor(getBackground());
		g2d.drawRect(0, 0, (int) width - 1, (int) height - 1);
	}

	// private double determineMax(double[] values) {
	// double max = Double.NEGATIVE_INFINITY;
	// for (int i = 0; i < values.length; i++) {
	// if (values[i] > max) {
	// max = values[i];
	// }
	// }
	// return max;
	// }

	private Color determineColor(double value, double maxValue) {
		int red = (int) (value / maxValue * 255);
		int green = (int) (100 - (value / maxValue * 100));
		int blue = (int) (0);
		int alpha = 255;
		return new Color(red, green, blue, alpha);
	}

}
