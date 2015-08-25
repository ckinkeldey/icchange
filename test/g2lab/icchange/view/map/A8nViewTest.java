/**
 * 
 */
package net.g2lab.icchange.view.map;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.g2lab.icchange.view.TestFrame;
import net.g2lab.icchange.view.uncertainty.AnnotationPanel;
import net.g2lab.icchange.view.uncertainty.NoiseAnnotation;
import net.g2lab.io.RasterIO;
import net.g2lab.layer.Layer;
import net.g2lab.layer.RasterLayer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.junit.After;
import org.junit.Before;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class A8nViewTest {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	public void A8nViewTest() throws DataSourceException, IOException {
		URL uncertURL = getClass().getResource("es-grid");
		GridCoverage2D u = new RasterIO().readCoverage(uncertURL);
		Layer uncertaintyLayer = new RasterLayer(u);
		double gridSize = 1;
		NoiseAnnotation na8n = new NoiseAnnotation(uncertaintyLayer, gridSize);
		Envelope envelope = uncertaintyLayer.getEnvelope();
		AnnotationPanel map = new AnnotationPanel(na8n, envelope);
		map.setBackground(Color.DARK_GRAY);
		map.setDrawPolygons(false);
		map.setDrawAnnotations(true);
		map.setDrawPoints(false);
		TestFrame frame = new TestFrame();
		frame.getContentPane().add(map);
		frame.setSize(800, 800);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
			new A8nViewTest();
	}
}
