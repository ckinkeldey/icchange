package net.g2lab.icchange;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;

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

public class UncertaintyFilterApp {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	private JSlider uSlider;
	private JPanel sliderPanel;
	private JButton filterButton;

	public UncertaintyFilterApp() {

		GridCoverage2D[] lc = new GridCoverage2D[0];
		GridCoverage2D[] u = new GridCoverage2D[0];
		GridCoverage2D[] sat = new GridCoverage2D[0];

//		try {
//			String path = System.getProperty("user.home")
//					+ "/Dropbox/hcu/icchange/example/";
//
//			URL sat0URL = new File(path + "2003-32630.tif").toURI().toURL();
//			sat[0] = new RasterIO().readCoverage(sat0URL, new GeoTiffFormat());
//
//			URL sat1URL = new File(path + "2004-32630.tif").toURI().toURL();
//			sat[1] = new RasterIO().readCoverage(sat1URL, new GeoTiffFormat());
//
//			URL sat2URL = new File(path + "2004-2-32630.tif").toURI().toURL();
//			sat[2] = new RasterIO().readCoverage(sat2URL, new GeoTiffFormat());
//
//			URL lc0URL = new File(path + "2003-iso3-32630.tif").toURI().toURL();
//			lc[0] = new RasterIO().readCoverage(lc0URL, new GeoTiffFormat());
//
//			URL lc1URL = new File(path + "2004-iso3-32630.tif").toURI().toURL();
//			lc[1] = new RasterIO().readCoverage(lc1URL, new GeoTiffFormat());
//
//			URL lc2URL = new File(path + "2004-2-iso3-32630.tif").toURI()
//					.toURL();
//			lc[2] = new RasterIO().readCoverage(lc2URL, new GeoTiffFormat());
//
//			URL uOverallURL = new File(path + "u-class-32630.tif").toURI()
//					.toURL();
//			GridCoverage2D uOverall = new RasterIO().readCoverage(uOverallURL,
//					new GeoTiffFormat());

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
//			u[0] = new RasterIO().readCoverage(u0URL, new GeoTiffFormat());
//
//			String[] classNames = { "1", "2", "3" };
//			int[] classCodes = new int[] { 1, 2, 3 };

			this.uSlider = new JSlider(0, 100, 100);
			uSlider.setSnapToTicks(true);
			uSlider.setMajorTickSpacing(20);
			uSlider.setMinorTickSpacing(10);
			uSlider.setPaintTicks(true);
			uSlider.setPaintLabels(true);
			uSlider.setBorder(BorderFactory.createTitledBorder("uncertainty"));
			
			this.sliderPanel = new JPanel();
			sliderPanel.setLayout(new GridLayout(1, 2));
			sliderPanel.add(uSlider);
			sliderPanel.add(new JPanel());
			
			
			MapView mapView = new MapView(lc, sat, null);
			mapView.setSize(720, 770);
			mapView.getContentPane().add("South", sliderPanel);
			mapView.setVisible(true);

//		} catch (IOException e) {
//			LOG.error("Cannot load: " + e, e);
//		} catch (UnsupportedOperationException e) {
//			LOG.error(e, e);
//		} catch (NoSuchAuthorityCodeException e) {
//			LOG.error(e, e);
//		} catch (FactoryException e) {
//			LOG.error(e, e);
//		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new UncertaintyFilterApp();
	}
}
