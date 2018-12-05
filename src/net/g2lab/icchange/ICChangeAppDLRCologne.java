package net.g2lab.icchange;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.g2lab.icchange.view.map.ChangeGridCoverageLayer;
import net.g2lab.icchange.view.map.MapView;
import net.g2lab.icchange.view.sequence.ChangeInfoView;
import net.g2lab.icchange.view.style.LayerStyles;
import net.g2lab.io.RasterIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

public class ICChangeAppDLRCologne {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	public ICChangeAppDLRCologne() {

		GridCoverage2D[] sat = new GridCoverage2D[0];
		GridCoverage2D[] lc = new GridCoverage2D[3];
		GridCoverage2D[] u = new GridCoverage2D[1];

		try {
			String path = System.getProperty("user.home")
					+ "/Dropbox/hcu/expert-study/Wurm/Köln/";

//			URL sat0URL = new File(path + "1987-sat.tif").toURI().toURL();
//			sat[0] = new RasterIO().readCoverage(sat0URL, new GeoTiffFormat());
//
//			URL sat1URL = new File(path + "1995-sat.tif").toURI().toURL();
//			sat[1] = new RasterIO().readCoverage(sat1URL, new GeoTiffFormat());
//
//			URL sat2URL = new File(path + "2004-sat.tif").toURI().toURL();
//			sat[2] = new RasterIO().readCoverage(sat2URL, new GeoTiffFormat());

			URL lc0URL = new File(path + "1984.tif").toURI().toURL();
			lc[0] = new RasterIO().readCoverage(lc0URL, new GeoTiffFormat());

			URL lc1URL = new File(path + "1991.tif").toURI().toURL();
			lc[1] = new RasterIO().readCoverage(lc1URL, new GeoTiffFormat());

			URL lc2URL = new File(path + "2003.tif").toURI().toURL();
			lc[2] = new RasterIO().readCoverage(lc2URL, new GeoTiffFormat());

			URL uOverallURL = new File(path + "u-reclass5.tif").toURI().toURL();
			GridCoverage2D uOverall = new RasterIO().readCoverage(uOverallURL, new GeoTiffFormat());

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
			u[0] = uOverall;//new RasterIO().readCoverage(u0URL, new GeoTiffFormat());

			String[] classNames = { "non-urban", "urban", "2", "3", "4", "5", "6" };
			int[] classCodes = new int[] { 0, 1, 2, 3, 4, 5, 6 };

			Calendar[] dates = new Calendar[] {
					new GregorianCalendar(1984,1,1),
					new GregorianCalendar(1991,1,1),
					new GregorianCalendar(2003,1,1)
			};
			
			LayerStyles.colorCurrent = LayerStyles.COLOR_URBAN;
			
			ChangeInfoView infoView = new ChangeInfoView(lc, u, classNames,
					classCodes, dates);
//			infoView.setDateLabelInsets();
			infoView.setSize(400, 770);
			infoView.setLocation(720, 0);
			infoView.setVisible(true);

			MapView mapView = new MapView(sat, lc, uOverall, infoView);
//			mapView.setBackgroundColor(Color.WHITE);
			mapView.setSize(720, 770);
			
			mapView.getA8nLayer().initA8NWidth = 2000;
			mapView.getA8nLayer().getAnnotation().setGridSizeX(2000);
			mapView.getA8nLayer().getAnnotation().setGridSizeY(2000);
			
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
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ICChangeAppDLRCologne();
	}
}
