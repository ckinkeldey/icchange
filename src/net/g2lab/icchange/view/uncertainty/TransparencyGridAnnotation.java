/**
 * 
 */
package net.g2lab.icchange.view.uncertainty;

import java.awt.BasicStroke;
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
public class TransparencyGridAnnotation extends AbstractAnnotation {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private static final Color COLOR = new Color(255, 255, 255, 255);
	
	public TransparencyGridAnnotation(Layer layer, int gridSize) {
		super(layer, gridSize);
	}
	
	@Override
	public void drawAnnotations(Graphics2D g2d, int widthPx, int heightPx, ReferencedEnvelope envelope) {
		double x, y;
		int xPx, yPx, gridSizePxX, gridSizePxY;
//		for (int i = 0; i < uncertaintyPoints.size(); i++) {
//			double uncertainty = getUncertainty(i);
//			Geometry point = ((Feature) uncertaintyPoints.get(i)).getGeometry();
//			x = point.getCentroid().getX();
//			y = point.getCentroid().getY();
//
//			int alpha = (int) (200 * uncertainty);
//			g2d.setColor(new Color(COLOR.getRed(), COLOR.getGreen(), COLOR
//					.getBlue(), alpha));
//
//			x -= gridSize / 2.;
//			y -= gridSize / 2.;
//			
//			xPx = getXCoordPx(x);
//			yPx = getYCoordPx(y);
//
//			gridSizePxX = (int) (scaleX * gridSize);
//			gridSizePxY = (int) (scaleY * gridSize);
//
////			int numSteps = (int) (10 * uncertainty);
////			double step = 0;
////			if (numSteps > 0) {
////				step = gridSize / numSteps;
////			}
//
//			drawRows(g2d, x, yPx, gridSizePxX);
//			drawColumns(g2d, y, xPx, gridSizePxY);
//		}
	}

	private void drawRows(Graphics2D g2d, double x, int yPx, int gridSizePx) {
		int xPx = getXCoordPx(x);
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(xPx, yPx, xPx + gridSizePx, yPx);
	}

	private void drawColumns(Graphics2D g2d, double y, int xPx, int gridSizePx) {
		int yPx = getYCoordPx(y);
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(xPx, yPx, xPx, yPx + gridSizePx);
	}

}
