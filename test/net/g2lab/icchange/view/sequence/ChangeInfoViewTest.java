/**
 * 
 */
package net.g2lab.icchange.view.sequence;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.g2lab.io.RasterIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class ChangeInfoViewTest {

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

	@Test
	public void testComputeDistances() {
		Calendar[] dates = new Calendar[] { new GregorianCalendar(2000, 1, 1),
				new GregorianCalendar(2001, 1, 1),
				new GregorianCalendar(2002, 1, 1) };
		double[] distances = AbstractSequenceListPanel.computeRelativeDistances(dates);
		assertThat(distances.length, is(3));
		assertEquals(.5, distances[0], .001);
		assertEquals(.5, distances[1], .001);
		assertEquals(.0, distances[2], .001);
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

			URL lc2URL = new File("c:/temp/icchange/2004-2-iso3.tif").toURI()
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

			String[] changeNames = {"1", "2", "3"};
			int[] changeCodes = new int[] {1,2,3};
			Calendar[] dates = new Calendar[] {
				new GregorianCalendar(2003,1,1),
				new GregorianCalendar(2004,1,1),
				new GregorianCalendar(2004,6,1)
			};
			ChangeInfoView view = new ChangeInfoView(lc, u, changeNames, changeCodes, dates );
			view.setSize(600, 770);
			view.setVisible(true);
			
		} catch (IOException e) {
			LOG.error("Cannot load: " + e, e);
		} catch (UnsupportedOperationException e) {
			LOG.error(e, e);
		} catch (NoSuchAuthorityCodeException e) {
			LOG.error(e, e);
		} catch (FactoryException e) {
			LOG.error(e, e);
		}

	}


}
