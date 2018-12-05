/**
 * 
 */
package net.g2lab.icchange.view.uncertainty;

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
public class AmplitudeAnnotation extends AbstractAnnotation {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	
	public AmplitudeAnnotation(Layer layer, int gridSize) {
		super(layer, gridSize);
	}

	@Override
	public void drawAnnotations(Graphics2D g2d, int widthPx, int heightPx, ReferencedEnvelope envelope) {
		drawRows(g2d, scaleX, scaleY);
//		 drawColumns(g2d, scaleX, scaleY);
	}

	private void drawRows(Graphics2D g2d, double scaleX, double scaleY) {
//		int x, y, xPx, yPx;
//		for (int i = 0; i < uncertaintyPoints.size(); i++) {
//			double uncertainty = getUncertainty(i);
//			Geometry point0 = ((Feature) uncertaintyPoints.get(i))
//					.getGeometry();
//			x = getXCoordPx(point0.getCoordinate().x);
//			y = getYCoordPx(point0.getCoordinate().y);
//			int size = (int) (scaleX * (gridSize / 2));
//			Color color = new Color(255, 255, 255, 100);
//			g2d.setColor(color);
//			g2d.drawLine(x - size, y, x + size, y);
//
//			int amplitudePx = (int) (scaleY * size * uncertainty);
//
//			for (int j = 0; j < 2 * size; j++) {
//				xPx = x - size + j;
//				color = new Color(255, 255, 255, 255);
//				yPx = (int) (y + amplitudePx * Math.sin(j / 2 * size * Math.PI));
//				g2d.setColor(color);
//				g2d.drawLine(xPx, yPx, xPx, yPx);
//			}
//		}
	}

}
