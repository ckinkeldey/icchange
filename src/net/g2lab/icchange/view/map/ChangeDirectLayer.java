/**
 * 
 */
package net.g2lab.icchange.view.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.swing.JMapPane;
import org.opengis.parameter.ParameterValueGroup;


/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class ChangeDirectLayer extends DirectLayer {

	private JMapPane jMapPane;
	private GridCoverage2D[] lc;

	/**
	 * @param lc 
	 * @param jMapPane 
	 * 
	 */
	public ChangeDirectLayer(GridCoverage2D[] lc) {
		this.lc = lc;
		this.jMapPane = jMapPane;
	}

	/* (non-Javadoc)
	 * @see org.geotools.map.DirectLayer#draw(java.awt.Graphics2D, org.geotools.map.MapContent, org.geotools.map.MapViewport)
	 */
	@Override
	public void draw(Graphics2D g2d, MapContent map, MapViewport viewport) {
		ReferencedEnvelope bounds = viewport.getBounds();
		final CoverageProcessor processor = new CoverageProcessor();

		final ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();

		final GeneralEnvelope crop = new GeneralEnvelope(bounds);
		param.parameter("Source").setValue( lc[0] );
		param.parameter("Envelope").setValue( crop );

		GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);
		AffineTransform transform = AffineTransform.getTranslateInstance(0, 0);
		
		g2d.setColor(Color.RED);
		BufferedImage bufferedImage = convertRenderedImage(cropped.getRenderedImage());
		g2d.drawRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		g2d.drawImage(bufferedImage, transform, jMapPane);
	}

	/* (non-Javadoc)
	 * @see org.geotools.map.Layer#getBounds()
	 */
	@Override
	public ReferencedEnvelope getBounds() {
		return new ReferencedEnvelope(lc[0].getEnvelope());
	}
	
	private BufferedImage convertRenderedImage(RenderedImage img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage)img;	
		}	
		ColorModel cm = img.getColorModel();
		int width = img.getWidth();
		int height = img.getHeight();
		WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		Hashtable properties = new Hashtable();
		String[] keys = img.getPropertyNames();
		if (keys!=null) {
			for (int i = 0; i < keys.length; i++) {
				properties.put(keys[i], img.getProperty(keys[i]));
			}
		}
		BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
		img.copyData(raster);
		return result;
	}

}
