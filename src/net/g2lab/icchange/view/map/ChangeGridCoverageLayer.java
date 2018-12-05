/**
 * 
 */
package net.g2lab.icchange.view.map;

import java.awt.image.Raster;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.g2lab.icchange.model.ChangeSequence;
import net.g2lab.icchange.model.ChangeSequenceGroup;
import net.g2lab.icchange.model.ObjectClass;
import net.g2lab.icchange.view.style.LayerStyles;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.map.GridCoverageLayer;
import org.geotools.styling.Style;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class ChangeGridCoverageLayer extends GridCoverageLayer {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private GridCoverage2D[] lc;

	/**
	 * @param uOverall
	 * @param style
	 * @param coverage
	 * @param style
	 * @param title
	 */
	public ChangeGridCoverageLayer(GridCoverage2D[] lc,
			GridCoverage2D uncertainty, Style style) {
		super(createChangeCoverage2D(lc, uncertainty), style);
		this.lc = lc;
	}

	private static GridCoverage2D createChangeCoverage2D(GridCoverage2D[] lc,
			GridCoverage2D uncertainty) {
		return createChangeCoverage2D(lc, uncertainty, null);
	}

	// private static GridCoverage2D createChangeCoverage2D(GridCoverage2D[] lc,
	// GridCoverage2D uncertainty, List<ChangeSequenceGroup> changeList) {
	// float[][] changeArray = new
	// float[lc[0].getRenderedImage().getHeight()][lc[0]
	// .getRenderedImage().getWidth()];
	// Raster data0 = lc[0].getRenderedImage().getData();
	// Raster data1 = lc[1].getRenderedImage().getData();
	// Raster data2 = lc[2].getRenderedImage().getData();
	// for (int i = 0; i < data0.getWidth(); i++) {
	// for (int j = 0; j < data0.getHeight(); j++) {
	// int pixel0 = data0.getPixel(i, j, new int[1])[0];
	// int pixel1 = data1.getPixel(i, j, new int[1])[0];
	// int pixel2 = data2.getPixel(i, j, new int[1])[0];
	// if (pixel0 == pixel1 && pixel1 == pixel2) {
	// changeArray[j][i] = 0;
	// } else { //if (changeIncluded(pixel0, pixel1, pixel2, changeList)) {
	// changeArray[j][i] = pixel0 + 3 * pixel1 + 9 * pixel2;
	// }
	//
	// }
	// }
	// return new GridCoverageFactory().create("change", changeArray,
	// lc[0].getEnvelope());
	// }

	private static GridCoverage2D createChangeCoverage2D(GridCoverage2D[] lc,
			GridCoverage2D uncertainty, List<ChangeSequenceGroup> changeList) {
		float[][] changeArray = new float[lc[0].getRenderedImage().getHeight()][lc[0]
				.getRenderedImage().getWidth()];
		Raster[] imageData = new Raster[lc.length];
		for (int k = 0; k < lc.length; k++) {
			imageData[k] = lc[k].getRenderedImage().getData();
		}
		for (int i = 0; i < imageData[0].getWidth(); i++) {
			for (int j = 0; j < imageData[0].getHeight(); j++) {
				int[] pixels = new int[lc.length];
				for (int k = 0; k < lc.length; k++) {
					pixels[k] = imageData[k].getPixel(i, j, new int[1])[0];
				}
				changeArray[j][i] = ChangeSequence.getChangeCode(pixels);
			}
		}
		return new GridCoverageFactory().create("change", changeArray,
				lc[0].getEnvelope());
	}

	protected static boolean changeIncluded(int pixel0, int pixel1, int pixel2,
			List<ChangeSequenceGroup> changeList) {
		if (changeList == null || changeList.size() == 0) {
			return true;
		} else {
			for (ChangeSequenceGroup group : changeList) {
				ObjectClass[] objClasses = group.getObjClasses();
				if (objClasses[0].getClasscode() == pixel0
						&& objClasses[1].getClasscode() == pixel1
						&& objClasses[2].getClasscode() == pixel2) {
					return true;
				}
			}
			return false;
		}
	}

}
