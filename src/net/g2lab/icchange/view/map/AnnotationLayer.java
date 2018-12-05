/**
 * 
 */
package net.g2lab.icchange.view.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import net.g2lab.icchange.view.uncertainty.NoiseAnnotation;
import net.g2lab.layer.RasterLayer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.swing.JMapPane;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class AnnotationLayer extends DirectLayer implements MouseListener, MouseWheelListener {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	
	public static double initA8NWidth = 1000;
	
	private GridCoverage2D uncertainty;
	private NoiseAnnotation annotation;
	private RasterLayer uncertaintyLayer;

	private JMapPane jMapPane;

	public AnnotationLayer(JMapPane jMapPane, GridCoverage2D uncertainty, double a8nGridWidth) {
		this.jMapPane = jMapPane;
		this.uncertainty = uncertainty;
		this.initA8NWidth = a8nGridWidth;
		this.uncertaintyLayer = new RasterLayer(uncertainty);
		this.annotation = new NoiseAnnotation(uncertaintyLayer, a8nGridWidth);
	}

	/**
	 * @return the annotation
	 */
	public NoiseAnnotation getAnnotation() {
		return annotation;
	}

	/**
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(NoiseAnnotation annotation) {
		this.annotation = annotation;
	}

	/* (non-Javadoc)
	 * @see org.geotools.map.DirectLayer#draw(java.awt.Graphics2D, org.geotools.map.MapContent, org.geotools.map.MapViewport)
	 */
	@Override
	public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {
		new AnnotationDrawThread(graphics, map, viewport).run();
	}
	
	public class AnnotationDrawThread extends Thread {

	    private Graphics2D graphics;
		private MapContent map;
		private MapViewport viewport;

		public AnnotationDrawThread(Graphics2D graphics, MapContent map,
				MapViewport viewport) {
			this.graphics = graphics;
			this.map = map;
			this.viewport = viewport;
		}

		public void run() {
	    	int widthPx = jMapPane.getWidth();
			int heightPx = jMapPane.getHeight();
//			graphics.setColor(new Color(255, 255, 255, ANNOTATION_OPACITY));
			annotation.draw(graphics, widthPx, heightPx, viewport.getBounds()); 
//			getMapPane().setDisplayArea(getMapPane().getDisplayArea());
	    }

	}
	
	/* (non-Javadoc)
	 * @see org.geotools.map.Layer#getBounds()
	 */
	@Override
	public ReferencedEnvelope getBounds() {
		return new ReferencedEnvelope(uncertainty.getEnvelope());
	}



	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		NoiseAnnotation a8n = getAnnotation();
		double offsetX = (getAnnotation().getOffsetX() + a8n.getWidth(1)) % a8n.getGridSizeX();
		LOG.debug("new offset x == " + offsetX);
		this.getAnnotation().setOffsetX(offsetX);
		this.setVisible(false);
		this.setVisible(true);
		return;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double a8nGridSize = annotation.getGridSizeX();
		if (e.getPreciseWheelRotation() > 0) {
			a8nGridSize *= 1.2;
		} else {
			a8nGridSize /= 1.2;
		}
		getAnnotation().setGridSizeX(a8nGridSize);
		getAnnotation().setGridSizeY(a8nGridSize);
		this.setVisible(false);
		this.setVisible(true);
		return;
		
	}

	public void reset() {
		getAnnotation().setGridSizeX(initA8NWidth);
		getAnnotation().setGridSizeY(initA8NWidth);
		this.setVisible(false);
		this.setVisible(true);
	}

}
