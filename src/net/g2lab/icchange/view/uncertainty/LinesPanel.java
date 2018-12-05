package net.g2lab.icchange.view.uncertainty;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.feature.Feature;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class LinesPanel extends JPanel {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	
	private static final Color BG_COLOR = Color.LIGHT_GRAY;

//	protected FeatureCollection polygons;
	private UncertaintyLines lines;
	private Envelope envelope;

	private Map<String, Color> colors;
	
	public LinesPanel(UncertaintyLines lines, Envelope envelope) {
		this.lines = lines;
//		this.polygons = polygons;
		this.envelope = envelope;
		
		this.setBackground(BG_COLOR);

//		String[] objClasses = parseDistinctObjectClasses(polygons.getFeatures());
//		LOG.info(objClasses.length + " object classes.");
//		String classes = "";
//		for (String objClass : objClasses) {
//			classes += objClass + " ";
//		}
//		LOG.info(classes);
		
//		colors = createColors(objClasses);
	}
	
	private Map<String, Color> createColors(String[] objClasses) {
		Map<String, Color> colors = new HashMap<String, Color>();
		for (int i = 0; i < objClasses.length; i++) {
			colors.put(objClasses[i], AnnotationPanelTestBase.CLASS_COLORS[i]);
		}
		return colors;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		double scaleX = (double) getWidth() / envelope.getWidth();
		double scaleY = (double) getHeight() / envelope.getHeight();
		Graphics2D g2d = (Graphics2D) g;
//		drawPolygons(g2d, scaleX, scaleY);
		lines.drawUncertainLines(g2d, getWidth(), getHeight());
	}

//	private void drawPolygons(Graphics2D g2d, double scaleX, double scaleY) {
//		for (Feature feature : ((List<Feature>) polygons.getFeatures())) {
//			drawPolygonFeature(g2d, feature, scaleX, scaleY);
//		}
//	}

	private void drawPolygonFeature(Graphics2D g2d, Feature feature, double scaleX, double scaleY) {
		LineString outerRing = ((com.vividsolutions.jts.geom.Polygon) feature
				.getGeometry()).getExteriorRing();
		Polygon polygon;
		int numHoles = ((com.vividsolutions.jts.geom.Polygon) feature
				.getGeometry()).getNumInteriorRing();
		for (int i = 0; i < numHoles; i++) {
			LineString interiorRing = ((com.vividsolutions.jts.geom.Polygon) feature
					.getGeometry()).getInteriorRingN(i);
			polygon = createPolygon(interiorRing, scaleX, scaleY);
			g2d.setColor(getBackground());
			g2d.fill(polygon);
		}
		
		polygon = createPolygon(outerRing, scaleX, scaleY);
		g2d.setColor(getColor(feature));
		g2d.fill(polygon);
	}

	private Color getColor(Feature feature) {
		String objClass = parseObjectClass(feature);
		return colors.get(objClass);
	}

	private Polygon createPolygon(LineString outerRing, double scaleX, double scaleY) {
		Polygon polygon = new Polygon();
		Coordinate coord;
		int x;
		int y;
		for (int i = 0; i < outerRing.getCoordinates().length; i++) {
			coord = outerRing.getCoordinates()[i];
			x = (int) (scaleX * (coord.x - envelope.getMinX()));
			y = getHeight() - (int) (scaleY * (coord.y - envelope.getMinY()));
			polygon.addPoint(x, y);
		}
		return polygon;
	}

	protected String[] parseDistinctObjectClasses(List<Feature> features) {
		Set<String> distinct = new HashSet<String>();
		for (Feature feature : features) {
			distinct.add(parseObjectClass(feature));
		}
		return distinct.toArray(new String[0]);
	}
	
	protected String parseObjectClass(Feature feature) {
		String attribute = (String) feature.getAttribute(AnnotationPanelTestBase.CLASS_ATTRIBUTE);
		return attribute.trim();
	}
}
