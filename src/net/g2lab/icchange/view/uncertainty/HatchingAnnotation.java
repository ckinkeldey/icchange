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
public class HatchingAnnotation extends AbstractAnnotation {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private static final Color HATCHING_COLOR = new Color(255, 255, 255, 100);

	public HatchingAnnotation(Layer layer, int gridSize) {
		super(layer, gridSize);
	}

	@Override
	public void drawAnnotations(Graphics2D g2d, int widthPx, int heightPx, ReferencedEnvelope envelope) {
		double x, y;
		int xPx, yPx, gridSizeHalfPxX, gridSizeHalfPxY;
//		for (int i = 0; i < uncertaintyPoints.size(); i++) {
//			double uncertainty = getUncertainty(i);
//			Geometry point = ((Feature) uncertaintyPoints.get(i)).getGeometry();
//			x = point.getCentroid().getX();
//			y = point.getCentroid().getY();
//			g2d.setColor(HATCHING_COLOR);
//
//			// x -= gridSize / 2.;
//
//			xPx = getXCoordPx(x);
//			yPx = getYCoordPx(y);
//
//			gridSizeHalfPxX = (int) (scaleX * gridSize / 2.);
//			gridSizeHalfPxY = (int) (scaleY * gridSize / 2.);
//
//			int numSteps = (int) (10 * uncertainty);
//			double step = 0;
//			if (numSteps > 0) {
//				step = gridSize / numSteps;
//			}
//
//			drawRows(g2d, x, yPx, gridSizeHalfPxY, numSteps, step);
//			drawColumns(g2d, y, xPx, gridSizeHalfPxX, numSteps, step);
//		}
	}

	private void drawRows(Graphics2D g2d, double x, int yPx,
			int gridSizeHalfPx, int numSteps, double step) {
		int xPx;
		for (int j = 0; j < numSteps; j++) {
			x += step;
			xPx = getXCoordPx(x);
			g2d.drawLine(xPx, yPx - gridSizeHalfPx, xPx, yPx + gridSizeHalfPx);
		}
	}

	private void drawColumns(Graphics2D g2d, double y, int xPx,
			int gridSizeHalfPx, int numSteps, double step) {
		int yPx;
		for (int j = 0; j < numSteps; j++) {
			y += step;
			yPx = getYCoordPx(y);
			g2d.drawLine(xPx - gridSizeHalfPx, yPx, xPx + gridSizeHalfPx, yPx);
		}
	}

}
