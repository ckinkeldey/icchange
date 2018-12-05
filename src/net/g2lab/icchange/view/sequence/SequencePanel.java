/**
 * 
 */
package net.g2lab.icchange.view.sequence;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.g2lab.icchange.model.ChangeSequenceGroup;
import net.g2lab.icchange.model.ObjectClass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A panel showing a single change sequence.
 * 
 * @author <a href="mailto:christoph at kinkeldey dot de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class SequencePanel extends JLabel {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	public static final Color COLOR_BG = new Color(100, 100, 100);
	public static final Color COLOR_BG_SELECTED = new Color(150, 150, 0);

	private static final Color COLOR_BORDER = new Color(255, 255, 255, 255);
	private static final Color COLOR_BARCODE = new Color(255, 255, 255);
	private static final Color COLOR_LINK = new Color(255, 255, 255, 100);

	private ChangeSequenceGroup group;
	private Color[] colors;
	private double[] distances;
	private boolean selected = false;

	public SequencePanel(ChangeSequenceGroup group, Color[] colors,
			double[] distances) {
		this.group = group;
		this.colors = colors;
		this.distances = distances;
	}

	public void setSelected(boolean isSelected) {
		this.selected = isSelected;
	}

	@Override
	protected void paintComponent(Graphics g) {
		int numObjClasses = group.getObjClasses().length;

		if (numObjClasses == 0) {
			return;
		}

		if (selected) {
			g.setColor(COLOR_BG_SELECTED);
		} else {
			g.setColor(COLOR_BG);
		}
		g.fillRect(0, 0, getWidth(), getHeight());

		int symbolSize = getHeight();
		if (symbolSize > getWidth() / numObjClasses) {
			symbolSize = (int) (0.8 * getWidth() / numObjClasses);
		}

		drawChangeSequence(g, numObjClasses, symbolSize);
		this.setPreferredSize(new Dimension(getWidth(), symbolSize));
	}

	private void drawChangeSequence(Graphics g, int numObjClasses, int symbolSize) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int space = getWidth() - numObjClasses * symbolSize;
		int x = 0;
		int n = 0;
		for (ObjectClass objClass : group.getObjClasses()) {
			if (n < numObjClasses - 1) {
				int linkWidth = (int) (distances[n] * space);

				for (int i = 0; i < group.size(); i++) {
					double uncertainty = group.get(i).getUncertainties()[n];
//					LOG.debug("uncertainty: " + uncertainty);
//					drawBarcodeLink(g2d, symbolSize, linkWidth, x, uncertainty,
//							group.size());
					drawSimpleLink(g2d, symbolSize, linkWidth, x);
				}
//				drawBarcodeCenterLine(g2d, symbolSize, linkWidth, x);
//				drawBarcodeOutline(g2d, symbolSize, linkWidth, x);
			}

			drawObjClassSymbol(symbolSize, g2d, x, objClass);

			x += symbolSize + distances[n] * space;
			n++;
		}

	}

	private void drawObjClassSymbol(int symbolSize, Graphics2D g2d, int x,
			ObjectClass objClass) {
		int index = (objClass.getClasscode())%(colors.length);
		Color color = colors[index];
		int alpha = 255;// (int) (pattern.getUncertainties()[i] * 255);
		g2d.setColor(new Color(color.getRed(), color.getGreen(), color
				.getBlue(), alpha));
		Shape shape = new Ellipse2D.Float(x, 0, symbolSize - 1, symbolSize - 1);
		// Shape shape = new Rectangle(x, 0, symbolSize-1, symbolSize-1);
		g2d.fill(shape);
		g2d.setColor(COLOR_BORDER);
		g2d.setStroke(new BasicStroke(2));
		g2d.draw(shape);

		// g2d.drawString(objClass.getLabel(), x + symbolSize/2, symbolSize/2);
	}

	private void drawSimpleLink(Graphics2D g2d, int symbolSize, int linkWidth,
			int x0) {
		g2d.setColor(COLOR_LINK);
		int linkStroke = symbolSize / 10;
		int x = x0 + symbolSize;
		int y = symbolSize / 2 - linkStroke / 2;
		g2d.fillRect(x, y, linkWidth, linkStroke);
	}

	private void drawOutlineLink(Graphics2D g2d, int symbolSize, int linkWidth,
			int x0) {
		g2d.setColor(COLOR_BORDER);
		int linkStroke = symbolSize / 10;
		g2d.setStroke(new BasicStroke(linkStroke));

		int x = x0 + symbolSize / 2;
		g2d.drawLine(x, 0, x + linkWidth + symbolSize, 0);
		g2d.drawLine(x, getHeight() - 1, x + linkWidth + symbolSize,
				getHeight() - 1);
	}

	private void drawColoredLink(Graphics2D g2d, int symbolSize, int linkWidth,
			int x0, Color color0, Color color1, double uncertainty) {
		int alpha = (int) (255 - (255 * uncertainty));
		color0 = new Color(color0.getRed(), color0.getGreen(),
				color0.getBlue(), alpha);
		color1 = new Color(color1.getRed(), color1.getGreen(),
				color1.getBlue(), alpha);
		int linkStroke = symbolSize / 1;
		int x = x0 + symbolSize / 2;
		int y = symbolSize / 2 - linkStroke / 2;
		GradientPaint gradient = new GradientPaint(x, y, color0, x + linkWidth,
				y + linkStroke, color1, true);
		g2d.setPaint(gradient);
		g2d.fillRect(x, y, linkWidth + symbolSize, linkStroke);
	}

	private void drawBarcodeLink(Graphics2D g2d, int symbolSize, int linkWidth,
			int x0, double uncertainty, int numEntries) {
//		 int alpha = (int) ((255 - (255 * uncertainty))/ numEntries);
		int alpha = (int) Math.ceil(255. / numEntries);
		int a = alpha < 5 ? 5 : alpha;
		Color color = new Color(COLOR_BARCODE.getRed(),
				COLOR_BARCODE.getGreen(), COLOR_BARCODE.getBlue(), a);
		g2d.setColor(color);
//		 int linkStroke = symbolSize/1;
		int linkStroke = symbolSize / 10;
		int symbolSizeY = (int) (0.9 * symbolSize);
		int x, x1;
//		float cp0x, cp1x;
		int y, y0, y1;
//		float cp0y, cp1y;

		g2d.setStroke(new BasicStroke(linkStroke));
		GeneralPath path0 = new GeneralPath();
		GeneralPath path1 = new GeneralPath();

		// steep lines left
		x = x0 + symbolSize;
		x1 = (int) (x0 + symbolSize + linkWidth / 2. - symbolSize / 2.);
		y = symbolSize / 2 - linkStroke / 2;
		double height = uncertainty / 100. * symbolSizeY / 2.;
//		y0 = (int) (symbolSize / 2. - height - linkStroke / 2.);
//		y1 = (int) (symbolSize / 2. + height + linkStroke / 2.);
		y0 = (int) (symbolSize / 2. - height);
		y1 = (int) (symbolSize / 2. + height);
		
//		cp0x = (x + x1) / 2.f;
//		cp0y = y;
//		cp1x = x1;
//		cp1y = (y + y0) / 2.f;

		path0.moveTo(x1, y0);
		// path0.lineTo(x1, y0);
		// path0.curveTo(cp0x, cp0y, cp1x, cp1y, x1, y0);

		path1.moveTo(x1, y1);
		// path1.lineTo(x1, y1);
//		cp1y = (y + y1) / 2.f;
		// path1.curveTo(cp0x, cp0y, cp1x, cp1y, x1, y1);

		// even lines
		x = x1 + linkStroke / 2;
		x1 = x + symbolSize - 1;
		path0.lineTo(x1, y0);
		path1.lineTo(x1, y1);

		// steep lines right
//		x = x1 + linkStroke / 2;
//		x1 = x0 + symbolSize + linkWidth;
//		y1 = symbolSize / 2 - linkStroke / 2;
//		cp1x = (x + x1) / 2.f;
//		cp1y = y1;
//		cp0x = x;
//		cp0y = (y + y0) / 2.f;
		// path0.lineTo(x1, y1);
		// path0.curveTo(cp0x, cp0y, cp1x, cp1y, x1, y1);

//		cp0y = (y + y1) / 2.f;
		// path1.curveTo(cp0x, cp0y, cp1x, cp1y, x1, y1);

		g2d.draw(path0);
		g2d.draw(path1);
	}

	private void drawBarcodeLinkSpiderWeb(Graphics2D g2d, int symbolSize,
			int linkWidth, int x0, double uncertainty, int numEntries) {
		// int alpha = (int) ((255 - (255 * uncertainty))/ numEntries);
		int alpha = (int) Math.round(255. / numEntries);
		Color color = new Color(COLOR_BARCODE.getRed(),
				COLOR_BARCODE.getGreen(), COLOR_BARCODE.getBlue(), alpha);
		g2d.setColor(color);
		// int linkStroke = symbolSize/1;
		int linkStroke = symbolSize / 10;
		int symbolSizeY = (int) (.6 * symbolSize);
		int x, x1;
		float cp0x, cp1x;
		int y, y0, y1;
		float cp0y, cp1y;

		GeneralPath path0 = new GeneralPath();
		GeneralPath path1 = new GeneralPath();

		// steep lines left
		x = x0 + symbolSize;
		x1 = (int) (x0 + symbolSize + linkWidth / 2. - symbolSize / 2.);
		y = symbolSize / 2 - linkStroke / 2;
		y0 = (int) (symbolSize / 2. - uncertainty * symbolSizeY / 2. - linkStroke / 2.);
		y1 = (int) (symbolSize / 2. + uncertainty * symbolSizeY / 2. + linkStroke / 2.);
		cp0x = (x + x1) / 2.f;
		cp0y = y;
		cp1x = x1;
		cp1y = (y + y0) / 2.f;

		path0.moveTo(x, y);
		// path0.lineTo(x1, y0);
		path0.curveTo(cp0x, cp0y, cp1x, cp1y, x1, y0);

		path1.moveTo(x, y);
		// path1.lineTo(x1, y1);
		cp1y = (y + y1) / 2.f;
		path1.curveTo(cp0x, cp0y, cp1x, cp1y, x1, y1);

		// even lines
		x = x1 + linkStroke / 2;
		x1 = x + symbolSize;
		path0.lineTo(x1, y0);
		path1.lineTo(x1, y1);

		// steep lines right
		x = x1 + linkStroke / 2;
		x1 = x0 + symbolSize + linkWidth;
		y1 = symbolSize / 2 - linkStroke / 2;
		cp1x = (x + x1) / 2.f;
		cp1y = y1;
		cp0x = x;
		cp0y = (y + y0) / 2.f;
		// path0.lineTo(x1, y1);
		path0.curveTo(cp0x, cp0y, cp1x, cp1y, x1, y1);

		cp0y = (y + y1) / 2.f;
		path1.curveTo(cp0x, cp0y, cp1x, cp1y, x1, y1);

		g2d.draw(path0);
		g2d.draw(path1);
	}

	private void drawBarcodeCenterLine(Graphics2D g2d, int symbolSize,
			int linkWidth, int x0) {
		g2d.setColor(COLOR_BORDER);

		int linkStroke = symbolSize / 10;
		g2d.setStroke(new BasicStroke(linkStroke));
		int x = x0 + symbolSize;
		int y = symbolSize / 2 - linkStroke / 2;
		g2d.fillRect(x, y, (int) (linkWidth / 2. - symbolSize / 2.), linkStroke);

		x = x0 + symbolSize + (int) (linkWidth / 2. + symbolSize / 2.);
		y = symbolSize / 2 - linkStroke / 2;
		g2d.fillRect(x, y, (int) (linkWidth / 2. - symbolSize / 2.), linkStroke);

	}

	private void drawBarcodeOutline(Graphics2D g2d, int symbolSize,
			int linkWidth, int x0) {
		g2d.setColor(COLOR_BORDER);

		int linkStroke = symbolSize / 10;
		int x = (int) (x0 + symbolSize + linkWidth / 2. - symbolSize / 2.);
		int y = symbolSize / 2 - linkStroke / 2;
		g2d.setColor(COLOR_BORDER);
		g2d.setStroke(new BasicStroke(1));
//		g2d.drawLine(x, 0, x, symbolSize - 1);
//		g2d.drawLine(x+symbolSize, 0, x+symbolSize, symbolSize - 1);
		g2d.drawRect(x, 0, symbolSize, symbolSize - 1);
	}

	private void drawOpacityLink(Graphics2D g2d, int symbolSize, int linkWidth,
			int x0, double uncertainty) {
		int alpha = (int) (255 - (255 * uncertainty));
		g2d.setColor(new Color(200, 200, 200, alpha));
		int linkStroke = symbolSize / 1;
		int x = x0 + symbolSize / 2;
		g2d.fillRect(x, symbolSize / 2 - linkStroke / 2,
				linkWidth + symbolSize, linkStroke);
	}

	private void drawSineLink(Graphics2D g2d, int symbolSize, int linkWidth,
			int x0, double uncertainty) {
		g2d.setColor(COLOR_LINK);

		if (uncertainty < 0.05) {
			drawSimpleLink(g2d, symbolSize, linkWidth, x0);
			return;
		} else if (uncertainty > 0.95) {
			uncertainty = .95;
		}
		int linkStroke = symbolSize / 10;
		int maxX = x0 + linkWidth;

		int halfY = (int) (getHeight() / 4);
		int frequency = (int) (uncertainty * linkWidth);
		for (int i = 0; i < linkWidth; i++) {
			int x = x0 + symbolSize + i;
			int sine = getNormalizedSine(i, halfY, frequency);
			int y = halfY + sine;
			g2d.fillOval(x, y, 1, linkStroke);
		}
	}

	private void drawSineAmplitudeLink(Graphics2D g2d, int symbolSize,
			int linkWidth, int x0, double uncertainty) {
		g2d.setColor(COLOR_LINK);

		// if (uncertainty < 0.05) {
		// drawSimpleLink(g2d, symbolSize, linkWidth, x0);
		// return;
		// } else if (uncertainty > 0.95) {
		// uncertainty = .95;
		// }
		int linkStroke = symbolSize / 10;

		int halfY = (int) (uncertainty * getHeight() / 3);
		int maxX = (int) (linkWidth);

		g2d.setColor(COLOR_LINK);
		g2d.setStroke(new BasicStroke(2));

		int xPre = 0;
		int yPre = 0;
		for (int i = 0; i < linkWidth; i++) {
			int x = x0 + symbolSize + i;
			int sine = getNormalizedSine(i, halfY, 20);
			int y = (int) symbolSize / 2 - linkStroke / 2 - halfY + sine;
			if (i > 0) {
				g2d.drawLine(xPre, yPre, x, y);
			}
			xPre = x;
			yPre = y;
		}

	}

	private void drawNoiseLink(Graphics2D g2d, int symbolSize, int linkWidth,
			int x0, double uncertainty) {
		g2d.setColor(COLOR_LINK);
		int linkStroke = symbolSize / 10;
		int maxX = x0 + linkWidth;

		int halfY = (int) (uncertainty * getHeight() / 2);

		for (int i = 0; i < linkWidth; i++) {
			int x = x0 + symbolSize + i;
			int y = getHeight() / 2
					+ (int) (((-1) + 2 * Math.random()) * halfY);
			g2d.fillOval(x, y, 1, linkStroke);
		}
	}

	/**
	 * Calculates the sine value
	 * 
	 * @param x
	 *            the value along the x-axis
	 * @param halfY
	 *            the value of the y-axis
	 * @param maxX
	 *            the width of the x-axis
	 * @return int
	 */
	private int getNormalizedSine(int x, int halfY, int maxX) {
		double piDouble = 2 * Math.PI;
		double factor = piDouble / maxX;
		return (int) (Math.sin(x * factor) * halfY + halfY);
	}

}
