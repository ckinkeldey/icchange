/**
 * 
 */
package net.g2lab.icchange.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class GeoTIFF2TxtTest {

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
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL[] inURLs = new URL[] {
					ClassLoader.getSystemResource("SUB_2009-04_NDVI.tif"),
					loader.getResource("SUB_2009-05_NDVI.tif"),
					loader.getResource("SUB_2009-07_NDVI.tif") };
			File outFile = new File(System.getProperty("java.io.tmpdir")
					+ "sub_2009-04-05-07_ndvi.txt");
			OutputStream output = new FileOutputStream(outFile);

			GeoTIFF2Txt.convertGeoTIFF2Txt(inURLs, output);
			LOG.debug("output written to " + outFile.getAbsolutePath());
		} catch (FileNotFoundException e) {
			LOG.error(e, e);
		} catch (IOException e) {
			LOG.error(e, e);
		}
	}

}
