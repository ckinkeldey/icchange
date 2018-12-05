/**
 * 
 */
package net.g2lab.icchange.view.uncertainty;

import java.awt.Color;
import java.awt.Graphics2D;

import net.g2lab.layer.Layer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class LineWidthAnnotation extends AbstractAnnotation {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private static final Color GRID_COLOR = new Color(255, 255, 255, 100);

	public LineWidthAnnotation(Layer layer, int gridSize) {
		super(layer, gridSize);
	}

	@Override
	public void drawAnnotations(Graphics2D g2d, int widthPx, int heightPx, ReferencedEnvelope envelope) {
		int x0Px, y0Px, x1Px, y1Px;
//		for (int i = 0; i < uncertaintyPoints.size(); i++) {
//			double uncertainty = getUncertainty(i);
//			Geometry point0 = ((Feature) uncertaintyPoints.get(i))
//					.getGeometry();
//
//			int lineWidthPx = getLineWidthPx(uncertainty);
//
//			g2d.setColor(GRID_COLOR);
//			BasicStroke stroke = new BasicStroke(lineWidthPx,
//					BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
//			g2d.setStroke(stroke);
//			x0Px = getXCoordPx(point0.getCoordinate().x - gridSize / 2.);
//			y0Px = getYCoordPx(point0.getCoordinate().y);
//			x1Px = getXCoordPx(point0.getCoordinate().x + gridSize / 2.);
//			g2d.drawLine(x0Px, y0Px, x1Px, y0Px);
//
//			x0Px = getXCoordPx(point0.getCoordinate().x);
//			y0Px = getYCoordPx(point0.getCoordinate().y - gridSize / 2.);
//			y1Px = getYCoordPx(point0.getCoordinate().y + gridSize / 2.);
//			g2d.drawLine(x0Px, y0Px, x0Px, y1Px);
//
//		}
	}

	private int getLineWidthPx(double uncertainty) {
		int width = (int) Math.round(scaleX * uncertainty * gridSizeX / 2.);
		return width;
	}

	private Color getGridColor(double uncertainty) {
		int value = 0 + (int) (.9 * (100 - uncertainty));
		Color color = new Color(255, 255, 255, value);
		return color;
	}

}
