package net.g2lab.icchange.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import net.g2lab.io.RasterIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;

public class GeoTIFF2Txt {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	public static void convertGeoTIFF2Txt(URL[] inURLs, OutputStream output)
			throws IOException {
		if (output == null) {
			throw new IOException("Output stream must not be null.");
		} else if (isNull(inURLs)) {
			throw new IOException("Input urls must not be null.");
		}
		int numScenes = inURLs.length;
		GridCoverage2D[] images = new RasterIO().readCoverage(inURLs);
		OutputStreamWriter writer = new OutputStreamWriter(output);
		int width = images[0].getRenderedImage().getWidth();
		int height = images[0].getRenderedImage().getHeight();

		float[][] pixelInfo = new float[numScenes][width];
		for (int row = 0; row < height; row++) {
			LOG.debug("row " + row);
			for (int i = 0; i < numScenes; i++) {
				pixelInfo[i] = images[i].getRenderedImage().getData().getPixels(0, row, width, 1,
						pixelInfo[i]);
			}
			for (int col = 0; col < width; col++) {
				for (int i = 0; i < numScenes; i++) {
					String valueString = (pixelInfo[i][col] + "").replace(".",
							",");
					writer.append(valueString + "\t");
				}
				writer.append("\n");
			}
		}
		writer.close();
	}

	private static boolean isNull(Object[] objects) {
		for (Object object : objects) {
			if (object == null) {
				return true;
			}
		}
		return false;
	}
}
