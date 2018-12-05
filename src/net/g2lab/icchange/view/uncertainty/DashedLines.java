package net.g2lab.icchange.view.uncertainty;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Collection;

import net.g2lab.feature.UncertaintyLineStringFeature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;

/** 
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class DashedLines implements UncertaintyLines {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	
	private static final int STROKE_WIDTH = 8;

	private static final Color DASH_COLOR = new Color(255, 255, 255, 255);
	private static final Color DASH_BG_COLOR = new Color(200, 200, 200, 255);

	protected Collection<UncertaintyLineStringFeature> boundaries;

	private Envelope envelope;

	private int widthPx;

	private int heightPx;

	private double scaleX;

	private double scaleY;
	
	public DashedLines(Collection<UncertaintyLineStringFeature> boundaries, Envelope envelope) {
		this.boundaries = boundaries;
		this.envelope = envelope;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.g2lab.icchange.view.uncertainty.AbstractAnnotation#drawAnnotations
	 * (java.awt.Graphics2D, double, double)
	 */
	public void drawUncertainLines(Graphics2D g2d, int widthPx, int heightPx) {
		this.widthPx = widthPx;
		this.heightPx = heightPx;
		this.scaleX = (double) widthPx / envelope.getWidth();
		this.scaleY = (double) heightPx / envelope.getHeight();
		drawLines(g2d, scaleX, scaleY);
	}

	private void drawLines(Graphics2D g2d, double scaleX, double scaleY) {
		for (UncertaintyLineStringFeature feature : boundaries) {
			drawLineFeature(g2d, feature, scaleX, scaleY);
		}
	}

	private void drawLineFeature(Graphics2D g2d, UncertaintyLineStringFeature feature, double scaleX, double scaleY) {
		
		LineString boundary = (LineString) feature.getGeometry();
		
//		drawHoles(g2d, feature, scaleX, scaleY);

//		LineString outerRing = ((com.vividsolutions.jts.geom.Polygon) feature.getGeometry()).getExteriorRing();
		Polygon polygon = createAWTPolygon(boundary, scaleX, scaleY);
		
//		g2d.setStroke(new BasicStroke(STROKE_WIDTH));
//		g2d.setColor(DASH_BG_COLOR);
//		g2d.draw(polygon);
		
		g2d.setStroke(createDashedStroke(g2d, feature.getUncertainty()));
		g2d.setColor(DASH_COLOR);
		g2d.draw(polygon);
	}

	private BasicStroke createDashedStroke(Graphics2D g2d, double uncertainty) {
//		double uncertainty = Math.random();
		float dashSizePx = (float) (10 * uncertainty);
		float[] dash = new float[] {};
		if (dashSizePx > 0) {
			dash = new float[] {STROKE_WIDTH, STROKE_WIDTH * dashSizePx};
		} 
		return new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 10, dash, 0);
	}
	
	private Polygon createAWTPolygon(LineString outerRing, double scaleX, double scaleY) {
		Polygon polygon = new Polygon();
		Coordinate coord;
		int x;
		int y;
		for (int i = 0; i < outerRing.getCoordinates().length; i++) {
			coord = outerRing.getCoordinates()[i];
			x = (int) (scaleX * (coord.x - envelope.getMinX()));
			y = heightPx - (int) (scaleY * (coord.y - envelope.getMinY()));
			polygon.addPoint(x, y);
		}
		return polygon;
	}

//	private double getDelta(double uncertainty, double size) {
//		return size * 2 * uncertainty * (-1 + 2 * Math.random());
//	}

	public GridCoverage createRaster(int rasterWidth,
			CoordinateReferenceSystem crs) {
		int rasterHeight = (int) (rasterWidth * envelope.getHeight() / envelope
				.getWidth());
		LOG.info("creating a raster of " + rasterWidth + " x " + rasterHeight);

		BufferedImage image = new BufferedImage(rasterWidth, rasterHeight,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		this.drawUncertainLines(g2d, rasterWidth, rasterHeight);

		GridCoverageFactory factory = new GridCoverageFactory();
		double minX = envelope.getMinX();
		double maxX = envelope.getMaxX();
		double minY = envelope.getMinY();
		double maxY = envelope.getMaxY();

		org.opengis.geometry.Envelope bBox;
		bBox = new ReferencedEnvelope(minX, maxX,minY, maxY, crs);
		GridCoverage2D coverage = factory.create("GridCoverage", image, bBox);
		return coverage;
	}

}
