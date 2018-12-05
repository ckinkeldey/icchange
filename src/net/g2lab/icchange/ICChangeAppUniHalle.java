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
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.referencing.CRS;
import org.geotools.styling.Style;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class ICChangeAppUniHalle {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	public ICChangeAppUniHalle() {

		GridCoverage2D[] sat = new GridCoverage2D[3];
		GridCoverage2D[] lc = new GridCoverage2D[3];
		GridCoverage2D[] u = new GridCoverage2D[1];

		try {
			String path = System.getProperty("user.home") + "/Dropbox/hcu/expert-study/Gerstmann/";

			// enforce CRS
	        CoordinateReferenceSystem crs = CRS.decode("EPSG:32633");
	        Hints hints = new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, crs);
	        
			URL sat0URL = new File(path + "MNF-2000-123.tif").toURI().toURL();
			URL sat1URL = new File(path + "MNF-2003-123.tif").toURI().toURL();
			URL sat2URL = new File(path + "MNF-2009-123.tif").toURI().toURL();
			
			sat[0] = new RasterIO().readCoverage(sat0URL, new GeoTiffFormat(), hints);
			sat[1] = new RasterIO().readCoverage(sat1URL, new GeoTiffFormat(), hints);
			sat[2] = new RasterIO().readCoverage(sat2URL, new GeoTiffFormat(), hints);

			URL lc0URL = new File(path + "2000-reclass.tif").toURI().toURL();
			URL lc1URL = new File(path + "2003-rec-mask.tif").toURI().toURL();
			URL lc2URL = new File(path + "2009-rec-mask.tif").toURI().toURL();
			
			lc[0] = new RasterIO().readCoverage(lc0URL, new GeoTiffFormat(), hints);
			lc[1] = new RasterIO().readCoverage(lc1URL, new GeoTiffFormat(), hints);
			lc[2] = new RasterIO().readCoverage(lc2URL, new GeoTiffFormat(), hints);
			
			URL uOverallURL = new File(path + "u-all-class5.tif").toURI().toURL();
			GridCoverage2D uOverall = new RasterIO().readCoverage(uOverallURL, new GeoTiffFormat(), hints);

			// URL u0URL = new File(path +
			// "/spectraluncertainty/2003-2004-2004-2-u.tif").toURI().toURL();
			// u[0] = new RasterIO().readCoverage(u0URL, new GeoTiffFormat());
			u[0] = uOverall;
			
			// log bounding boxes
//			for (int i = 0; i < sat.length; i++) {
//				LOG.debug("sat " + i + ": " + sat[i].getEnvelope());
//			}
//			for (int i = 0; i < lc.length; i++) {
//				LOG.debug("lc " + i + ": " + lc[i].getEnvelope());
//			}
//			for (int i = 0; i < u.length; i++) {
//				LOG.debug("u " + i + ": " + u[i].getEnvelope());
//			}
			
			String[] classNames = { "0", "masked", "offenes Substrat",
					"Wasserflächen", "Nadelbäume", "artenreiche Sandtrockenrasen",
					"artenarme Sandtrockenrasen", "vernässte Gebiete", "Laubwald" };
			int[] classCodes = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8};

			LayerStyles.colorCurrent = //"0,0,0,0 ; 0,0,0,0 ; 255,247,175; 0,91,231; 39,115,0; 208,255,115; 168,168,0; 156,189,215; 77,230,0";
					"0,0,0,0 ; 0,0,0,0 ; 117,113,10; " +
					"0,80,200; 39,115,0; 104,127,57; " +
					"138,168,0; 156,189,215; 112, 199, 0";
			
			Calendar[] dates = new Calendar[] {
					new GregorianCalendar(2000,1,1),
					new GregorianCalendar(2003,1,1),
					new GregorianCalendar(2009,1,1)
			};
					
			ChangeInfoView infoView = new ChangeInfoView(lc, u, classNames,
					classCodes, dates);
			infoView.setSize(400, 770);
			infoView.setLocation(720, 0);
			infoView.setVisible(true);

			MapView mapView = new MapView(sat, lc, uOverall, infoView);
			mapView.setSize(720, 770);
			
			mapView.getA8nLayer().initA8NWidth = 100;
			mapView.getA8nLayer().getAnnotation().setGridSizeX(100);
			mapView.getA8nLayer().getAnnotation().setGridSizeY(100);

			infoView.getChooser().addKeyListener(mapView);
			infoView.addListDataListener(mapView);
			
			mapView.setVisible(true);

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
		new ICChangeAppUniHalle();
	}

}
