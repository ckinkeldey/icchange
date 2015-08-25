package net.g2lab.icchange;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.g2lab.icchange.view.map.ChangeGridCoverageLayer;
import net.g2lab.icchange.view.map.MapView;
import net.g2lab.icchange.view.sequence.ChangeInfoView;
import net.g2lab.io.RasterIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

public class ICChangeApp {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	public ICChangeApp() {

		GridCoverage2D[] sat = new GridCoverage2D[3];
		GridCoverage2D[] lc = new GridCoverage2D[3];
		GridCoverage2D[] u = new GridCoverage2D[1];

		try {
//			String path = System.getProperty("user.home")
//					+ "/Dropbox/hcu/icchange/example/";

			URL sat0URL = this.getClass().getClassLoader().getResource("2003-32630.tif").toURI().toURL();
			URL sat1URL = this.getClass().getClassLoader().getResource("2004-32630.tif").toURI().toURL();
			URL sat2URL = this.getClass().getClassLoader().getResource("2004-2-32630.tif").toURI().toURL();
			URL lc0URL = this.getClass().getClassLoader().getResource("2003-iso3-32630.tif").toURI().toURL();
			URL lc1URL = this.getClass().getClassLoader().getResource("2004-iso3-32630.tif").toURI().toURL();
			URL lc2URL = this.getClass().getClassLoader().getResource("2004-2-iso3-32630.tif").toURI().toURL();
			URL uOverallURL = this.getClass().getClassLoader().getResource("u-class-32630.tif").toURI().toURL();
			
//			URL sat0URL = new File("2003-32630.tif").toURI().toURL();
//			URL sat1URL = new File("2004-32630.tif").toURI().toURL();
//			URL sat2URL = new File("2004-2-32630.tif").toURI().toURL();
//			URL lc0URL = new File("2003-iso3-32630.tif").toURI().toURL();
//			URL lc1URL = new File("2004-iso3-32630.tif").toURI().toURL();			
//			URL lc2URL = new File("2004-2-iso3-32630.tif").toURI().toURL();
//			URL uOverallURL = new File("u-class-32630.tif").toURI().toURL();

			sat[0] = new RasterIO().readCoverage(sat0URL, new GeoTiffFormat());
			sat[1] = new RasterIO().readCoverage(sat1URL, new GeoTiffFormat());
			sat[2] = new RasterIO().readCoverage(sat2URL, new GeoTiffFormat());

			lc[0] = new RasterIO().readCoverage(lc0URL, new GeoTiffFormat());
			lc[1] = new RasterIO().readCoverage(lc1URL, new GeoTiffFormat());
			lc[2] = new RasterIO().readCoverage(lc2URL, new GeoTiffFormat());
			
			GridCoverage2D uOverall = new RasterIO().readCoverage(uOverallURL,
					new GeoTiffFormat());

			// URL u0URL = new File(path + "2003-u-n.tif").toURI().toURL();
			// u[0] = new RasterIO().readCoverage(u0URL, new GeoTiffFormat());
			//
			// URL u1URL = new File(path + "2004-u-n.tif").toURI().toURL();
			// u[1] = new RasterIO().readCoverage(u1URL, new GeoTiffFormat());
			//
			// URL u2URL = new File(path + "2004-2-u-n.tif").toURI().toURL();
			// u[2] = new RasterIO().readCoverage(u2URL, new GeoTiffFormat());

//			URL u0URL = new File(path
//					+ "/spectraluncertainty/2003-2004-2004-2-u.tif").toURI()
//					.toURL();
			u[0] = uOverall; //new RasterIO().readCoverage(u0URL, new GeoTiffFormat());

			String[] classNames = { "0", "1", "2", "3", "4", "5", "6", "7", "8"};
			int[] classCodes = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };

			Calendar[] dates = new Calendar[] {
					new GregorianCalendar(2003, 11, 3),
					new GregorianCalendar(2004, 4, 24),
					new GregorianCalendar(2004, 5, 15)
			};
			ChangeInfoView infoView = new ChangeInfoView(lc, u, classNames,
					classCodes, dates);
//			infoView.setDateLabels(new String[] {"11/03", "04/04", "07/04"});
			infoView.setSize(400, 770);
			infoView.setLocation(720, 0);
			infoView.setVisible(true);

			MapView mapView = new MapView(sat, lc, uOverall, infoView);
			mapView.setSize(720, 770);
			
			mapView.getA8nLayer().getAnnotation().setGridSizeX(100);
			mapView.getA8nLayer().getAnnotation().setGridSizeY(100);
			
			infoView.getChooser().addKeyListener(mapView);
			infoView.addListDataListener(mapView);
			
			mapView.setVisible(true);

			infoView.addListDataListener(mapView);
		} catch (IOException e) {
			LOG.error("Cannot load: " + e, e);
		} catch (UnsupportedOperationException e) {
			LOG.error(e, e);
		} catch (NoSuchAuthorityCodeException e) {
			LOG.error(e, e);
		} catch (FactoryException e) {
			LOG.error(e, e);
		} 
		catch (URISyntaxException e) {
			LOG.error(e, e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ICChangeApp();
	}
}
