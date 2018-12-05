package net.g2lab.icchange;

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

public class ICChangeAppPIK {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	public ICChangeAppPIK() {

		GridCoverage2D[] sat = new GridCoverage2D[1];
		GridCoverage2D[] lc = new GridCoverage2D[2];
		GridCoverage2D[] u = new GridCoverage2D[1];

		try {
			String path = System.getProperty("user.home")
					+ "/Dropbox/hcu/expert-study/Lüdeke/";

			URL sat0URL = new File(path + "le7-sub.tif").toURI().toURL();
			sat[0] = new RasterIO().readCoverage(sat0URL, new GeoTiffFormat());
//
//			URL sat1URL = new File(path + "hyd_131.tif").toURI().toURL();
//			sat[1] = new RasterIO().readCoverage(sat1URL);

			URL lc0URL = new File(path + "lc2003-8bit.tif").toURI().toURL();
			lc[0] = new RasterIO().readCoverage(lc0URL);

			URL lc1URL = new File(path + "lc2010-8bit.tif").toURI().toURL();
			lc[1] = new RasterIO().readCoverage(lc1URL);

			URL uOverallURL = new File(path + "lacunarityuncertainty/lac-0310-u-stretch-8bit.tif").toURI().toURL();
			u[0] = new RasterIO().readCoverage(uOverallURL);

			String[] classNames = { "no slum", "slum"};
			int[] classCodes = new int[] { 0, 1};

			Calendar[] dates = new Calendar[] {
					new GregorianCalendar(2003, 1, 1),
					new GregorianCalendar(2010, 1, 1),
			};
			
			LayerStyles.colorCurrent = LayerStyles.COLOR_URBAN;
			
			ChangeInfoView infoView = new ChangeInfoView(lc, u, classNames,
					classCodes, dates);

			infoView.setSize(400, 770);
			infoView.setLocation(720, 0);
			infoView.setVisible(true);

			MapView mapView = new MapView(sat, lc, u[0], infoView);
			mapView.setSize(720, 770);
			
			mapView.getA8nLayer().initA8NWidth = 1000;
			mapView.getA8nLayer().getAnnotation().setGridSizeX(1000);
			mapView.getA8nLayer().getAnnotation().setGridSizeY(1000);
			
			infoView.getChooser().addKeyListener(mapView);
			infoView.addListDataListener(mapView);
			
			mapView.setVisible(true);

			infoView.addListDataListener(mapView);
		} catch (IOException e) {
			LOG.error("Cannot load: " + e, e);
		} catch (UnsupportedOperationException e) {
			LOG.error(e, e);
		} 
		catch (NoSuchAuthorityCodeException e) {
			LOG.error(e, e);
		} catch (FactoryException e) {
			LOG.error(e, e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ICChangeAppPIK();
	}
}
