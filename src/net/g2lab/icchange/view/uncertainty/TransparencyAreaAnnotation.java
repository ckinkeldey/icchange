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
public class TransparencyAreaAnnotation extends AbstractAnnotation {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private static final Color COLOR = new Color(255, 255, 255, 255);

	public TransparencyAreaAnnotation(Layer layer, int gridSize) {
		super(layer, gridSize);
	}

	@Override
	public void drawAnnotations(Graphics2D g2d, int widthPx, int heightPx, ReferencedEnvelope envelope) {
		double x, y;
		int xPx, yPx, gridSizeXPx, gridSizeYPx;
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
//
//			xPx = getXCoordPx(x);
//			yPx = getYCoordPx(y);
//
//			gridSizeXPx = (int) Math.round(scaleX * gridSize);
//			gridSizeYPx = (int) Math.round(scaleY * gridSize);
//
//			g2d.fillRect(xPx, yPx, gridSizeXPx, gridSizeYPx);
//		}
	}

}
