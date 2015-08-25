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
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.arcgrid.ArcGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.styling.Style;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

public class ICChangeAppRE {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	public ICChangeAppRE() {

		GridCoverage2D[] lc = new GridCoverage2D[2];
		GridCoverage2D[] u = new GridCoverage2D[1];
		GridCoverage2D[] sat = new GridCoverage2D[0];

		try {
			String path = 
					//System.getProperty("user.home")
					"Y:/change-scene-rapideye/scene2/";
//
//			URL sat0URL = new File(path + "2010-07-16t113918_re5_3a-nac_3801862_88700_chip.img").toURI()
//					.toURL();
//			sat[0] = new RasterIO().readCoverage(sat0URL);
//
//			URL sat1URL = new File(path + "2010-08-07t112108_re4_3a-nac_3943246_88611_chip.img").toURI()
//					.toURL();
//			sat[1] = new RasterIO().readCoverage(sat1URL);

			URL lc0URL = new File(path + "07-ml.tif").toURI().toURL();
			lc[0] = new RasterIO().readCoverage(lc0URL, new GeoTiffFormat());

			URL lc1URL = new File(path + "08-ml.tif").toURI().toURL();
			lc[1] = new RasterIO().readCoverage(lc1URL, new GeoTiffFormat());


			URL uOverallURL = new File(path
					+ "0708-u.tif").toURI().toURL();
			GridCoverage2D uOverall = new RasterIO().readCoverage(uOverallURL, new GeoTiffFormat());

			// URL u0URL = new File(path +
			// "/spectraluncertainty/2003-2004-2004-2-u.tif").toURI().toURL();
			// u[0] = new RasterIO().readCoverage(u0URL, new GeoTiffFormat());
			u[0] = uOverall;

			String[] classNames = { "0", "1", "2", "3", "4" };
			int[] classCodes = new int[] { 0, 1, 2, 3, 4 };

			Calendar[] dates = new Calendar[] {
					new GregorianCalendar(2010,6,1),
					new GregorianCalendar(2010,7,1)
			};
			
			ChangeInfoView infoView = new ChangeInfoView(lc, u, classNames,
					classCodes, dates);
			infoView.setSize(400, 770);
			infoView.setLocation(720, 0);
			infoView.setVisible(true);

			MapView mapView = new MapView(sat, lc, uOverall, infoView);
			mapView.setSize(720, 770);
			mapView.setVisible(true);

			infoView.getChooser().addKeyListener(mapView);
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
		new ICChangeAppRE();
	}

}
