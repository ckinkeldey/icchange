/**
 * 
 */
package net.g2lab.icchange.view.map;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.g2lab.io.RasterIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.junit.After;
import org.junit.Before;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class MapViewTest {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	public static void main(String[] args) {
		GridCoverage2D[] lc = new GridCoverage2D[3];
		GridCoverage2D[] u = new GridCoverage2D[3];

		try {
			URL lc0URL = new File("c:/temp/icchange/2003-iso3.tif").toURI()
					.toURL();
			lc[0] = new RasterIO().readCoverage(lc0URL,
					new GeoTiffFormat());

			URL lc1URL = new File("c:/temp/icchange/2004-iso3.tif").toURI()
					.toURL();
			lc[1] = new RasterIO().readCoverage(lc1URL,
					new GeoTiffFormat());

			URL lc2URL = new File("c:/temp/icchange/2004-2-iso3-1.tif").toURI()
					.toURL();
			lc[2] = new RasterIO().readCoverage(lc2URL,
					new GeoTiffFormat());

			URL u0URL = new File("c:/temp/icchange/2003-u-n.tif").toURI()
					.toURL();
			u[0] = new RasterIO().readCoverage(u0URL,
					new GeoTiffFormat());

			URL u1URL = new File("c:/temp/icchange/2004-u-n.tif").toURI()
					.toURL();
			u[1] = new RasterIO().readCoverage(u1URL,
					new GeoTiffFormat());

			URL u2URL = new File("c:/temp/icchange/2004-2-u-n.tif").toURI()
					.toURL();
			u[2] = new RasterIO().readCoverage(u2URL,
					new GeoTiffFormat());

//			MapView mapView = new MapView(lc);
//			mapView.setSize(800, 770);
//			mapView.setVisible(true);
		} catch (IOException e) {
			LOG.error("Cannot load: " + e, e);
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
