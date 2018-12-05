package net.g2lab.icchange.view.style;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.g2lab.icchange.model.ChangeSequenceGroup;
import net.g2lab.icchange.model.ObjectClass;
import net.g2lab.icchange.view.map.AnnotationLayer;
import net.g2lab.icchange.view.map.MapView;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.Description;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.opengis.annotation.XmlElement;
import org.opengis.filter.FilterFactory2;
import org.opengis.style.ContrastMethod;
import org.opengis.style.StyleVisitor;

public class LayerStyles {

	private static final Color COLOR_CHANGE = new Color(0, 150, 150, 255);
	// protected static final Color[] LC_COLORS = new Color[] { new Color(200,
	// 0, 0),
	// new Color(0, 200, 0), new Color(0, 0, 200), new Color(100, 100, 0),
	// new Color(100, 0, 100), new Color(0, 100, 100),
	// new Color(33, 33, 33) };

	public static final String COLOR_BREWER_8CLASS_DARK_2 = "27, 158, 119; 217, 95, 2; 117, 112, 179; 231, 41, 138; 102, 166, 30; 230, 171, 2; 166, 118, 29; 102, 102, 102; 0,0,0,0";
	public static final String COLOR_BREWER_8CLASS_DARK_REORDERED = "27, 158, 119; 117, 112, 179; 231, 41, 138; 217, 95, 2; 102, 166, 30; 230, 171, 2; 166, 118, 29; 102, 102, 102";
	public static final String COLOR_BREWER_4CLASS_SET2 = "102,194,165; 252,141,98; 141,160,203; 231,138,195";

	public static final String COLOR_STRING = "141, 211, 199; 255, 255, 179; 190, 186, 218; 251, 128, 114; 128, 177, 211; 253, 180, 98; 179, 222, 105; 252, 205, 229; 217, 217, 217; 188, 128, 189";

	public static final String COLOR_STRING_PASTEL = "251, 180, 174; 179, 205, 227; 204, 235, 197; 222, 203, 228; 254, 217, 166; 255, 255, 204; 229, 216, 189; 253, 218, 236";

	public static final String COLOR_KLIWAS_S = "112, 168, 0; 115, 178, 255; 0, 168, 132; 169, 0, 230; 102, 166, 30; 230, 230, 0; 166, 118, 29; 0, 112, 255";
	public static final String COLOR_KLIWAS_REORDERED = "0, 168, 132; 0, 112, 255; 230, 230, 0; 115, 178, 255;166, 118, 29; 169, 0, 230; 102, 166, 30; 0, 112, 255";

	public static final String COLOR_URBAN = "100, 100, 100, 0; 217, 95, 2; 117, 112, 179; 231, 41, 138; 102, 166, 30; 230, 171, 2; 166, 118, 29; 102, 102, 102;100, 100, 100";

	public static String colorCurrent = COLOR_BREWER_8CLASS_DARK_2;

	private static StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
	private static FilterFactory2 ff = CommonFactoryFinder
			.getFilterFactory2(null);

	public static Style createA8nLinesStyle() {
		int band = 1;
		StyleBuilder sldBuilder = new StyleBuilder();
		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0),
				ContrastMethod.NORMALIZE);
		SelectedChannelType sct = sf.createSelectedChannelType(
				String.valueOf(band), ce);

		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
		int type = ColorMap.TYPE_VALUES;
		String[] labels = { "na8n", "na8n" };
		double[] quantities = new double[] { 0, 255 };
		Color[] colors = new Color[] { new Color(0, 0, 0, 0),
				new Color(255, 255, 255, 100) };
		ColorMap colorMap = sldBuilder.createColorMap(labels, quantities,
				colors, type);
		sym.setColorMap(colorMap);

		ChannelSelection sel = sf.channelSelection(sct);
		sym.setChannelSelection(sel);

		return SLD.wrapSymbolizers(sym);
	}

	public static Style createNDVIStyle() {
		int band = 1;
		StyleBuilder sldBuilder = new StyleBuilder();
		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0),
				ContrastMethod.NONE);
		SelectedChannelType sct = sf.createSelectedChannelType(
				String.valueOf(band), ce);

		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
		int type = ColorMap.TYPE_INTERVALS;
		String[] labels = { "1", "2", "3", "4" };
		double[] quantities = new double[] { -100, 0, 20, 200 };
		Color[] colors = new Color[] { new Color(50, 50, 50, 0),
				new Color(255, 0, 0), new Color(255, 255, 0),
				new Color(255, 255, 255) };
		ColorMap colorMap = sldBuilder.createColorMap(labels, quantities,
				colors, type);
		sym.setColorMap(colorMap);

		ChannelSelection sel = sf.channelSelection(sct);
		sym.setChannelSelection(sel);

		return SLD.wrapSymbolizers(sym);
	}

	public static Style createChangeStyle(Set<Integer> codes,
			Collection<ChangeSequenceGroup> changeList) {
		int band = 1;
		StyleBuilder sldBuilder = new StyleBuilder();
		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0),
				ContrastMethod.NONE);
		SelectedChannelType sct = sf.createSelectedChannelType(
				String.valueOf(band), ce);

		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
		int type = ColorMap.TYPE_VALUES;
		String[] labels = createLabels(codes);
		double[] quantities = createQuantities(codes);
		Color[] colors = createChangeColors(codes, changeList);
		ColorMap colorMap = sldBuilder.createColorMap(labels, quantities,
				colors, type);
		sym.setColorMap(colorMap);

		ChannelSelection sel = sf.channelSelection(sct);
		sym.setChannelSelection(sel);

		return SLD.wrapSymbolizers(sym);
	}

	public static Style createUncertaintyMaskStyle(int threshold) {
		int band = 1;
		StyleBuilder sldBuilder = new StyleBuilder();
		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0),
				ContrastMethod.NONE);
		SelectedChannelType sct = sf.createSelectedChannelType(
				String.valueOf(band), ce);

		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
		int type = ColorMap.TYPE_VALUES;
//		String[] labels = { "0", "10", "20", "30", "40", "50", "60", "70",
//				"80", "90", "100" };
		String[] labels = { "0", "25", "50", "75", "100" };
		double[] quantities = new double[] { 0, 25, 50, 75, 100 };
		Color[] colors = new Color[quantities.length];
		for (int i = 0; i < quantities.length; i++) {
//			if (quantities[i] == 100) {
//				colors[i] = new Color(0, 0, 0, 0);
//			} else {
				colors[i] = quantities[i] <= threshold ? new Color(0, 0, 0, 0) : MapView.bgColor;
//			}
		}
		ColorMap colorMap = sldBuilder.createColorMap(labels, quantities,
				colors, type);
		sym.setColorMap(colorMap);

		ChannelSelection sel = sf.channelSelection(sct);
		sym.setChannelSelection(sel);

		return SLD.wrapSymbolizers(sym);
	}

	private static String[] createLabels(Collection<Integer> codes) {
		String[] labels = new String[codes.size()];
		int i = 0;
		for (int code : codes) {
			labels[i++] = "" + code;
		}
		return labels;
	}

	private static double[] createQuantities(Collection<Integer> codes) {
		double[] quantities = new double[codes.size()];
		int i = 0;
		for (int code : codes) {
			quantities[i++] = code;
		}
		return quantities;
	}

	private static Color[] createChangeColors(Collection<Integer> codes,
			Collection<ChangeSequenceGroup> changeList) {
		Color[] colors = new Color[codes.size()];
		int i = 0;
		for (int code : codes) {
			if (isContainedInChangeGroup(code, changeList)) {
				colors[i++] = COLOR_CHANGE;// new Color(255, 0,
															// 0, 255);
			} else {
				colors[i++] = new Color(255, 255, 255, 0);// MapView.bgColor;
			}
		}
		return colors;
	}

	private static boolean isContainedInChangeGroup(int code,
			Collection<ChangeSequenceGroup> changeList) {
		for (ChangeSequenceGroup changeGroup : changeList) {
			if (code == changeGroup.getChangeCode()) {
				return true;
			}
		}
		return false;
	}

	public static Style createLCStyle() {
		int band = 1;
		StyleBuilder sldBuilder = new StyleBuilder();
		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0),
				ContrastMethod.NONE);
		SelectedChannelType sct = sf.createSelectedChannelType(
				String.valueOf(band), ce);

		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
		int type = ColorMap.TYPE_VALUES;
		String[] labels = { "0", "1", "2", "3", "4", "5", "6", "7", "8"};
		double[] quantities = new double[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
		Color[] colors = LayerStyles.createColors(LayerStyles.colorCurrent);
//		Color[] subColors = new Color[9];
//		subColors[0] = colors[0];
		ColorMap colorMap = sldBuilder.createColorMap(labels, quantities, colors, type);
		sym.setColorMap(colorMap);

		ChannelSelection sel = sf.channelSelection(sct);
		sym.setChannelSelection(sel);

		return SLD.wrapSymbolizers(sym);
	}

	/**
	 * Create a Style to display the specified band of the GeoTIFF image as a
	 * greyscale layer.
	 * <p>
	 * This method is a helper for createGreyScale() and is also called directly
	 * by the displayLayers() method when the application first starts.
	 * 
	 * @param band
	 *            the image band to use for the greyscale display
	 * @param contrastMethod 
	 * 
	 * @return a new Style instance to render the image in greyscale
	 */
	public static Style createGreyscaleStyle(int band, ContrastMethod contrastMethod) {
		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0),
				contrastMethod);
		SelectedChannelType sct = sf.createSelectedChannelType(
				String.valueOf(band), ce);

		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
		ChannelSelection sel = sf.channelSelection(sct);
		sym.setChannelSelection(sel);

		return SLD.wrapSymbolizers(sym);
	}

	/**
	 * This method examines the names of the sample dimensions in the provided
	 * coverage looking for "red...", "green..." and "blue..." (case insensitive
	 * match). If these names are not found it uses bands 1, 2, and 3 for the
	 * red, green and blue channels. It then sets up a raster symbolizer and
	 * returns this wrapped in a Style.
	 * 
	 * @param coverage
	 * 
	 * @return a new Style object containing a raster symbolizer set up for RGB
	 *         image
	 */
	public static Style createRGBStyle(GridCoverage2D coverage) {
		return createRGBStyle(coverage, ContrastMethod.HISTOGRAM);
	}

	public static Style createRGBStyle(GridCoverage2D coverage,
			ContrastMethod contrastMethod) {

		// We need at least three bands to create an RGB style
		int numBands = coverage.getNumSampleDimensions();
		if (numBands < 3) {
			return null;
		}
		// Get the names of the bands
		String[] sampleDimensionNames = new String[numBands];
		for (int i = 0; i < numBands; i++) {
			GridSampleDimension dim = coverage.getSampleDimension(i);
			sampleDimensionNames[i] = dim.getDescription().toString();
		}
		final int RED = 0, GREEN = 1, BLUE = 2;
		int[] channelNum = { -1, -1, -1 };
		// We examine the band names looking for "red...", "green...",
		// "blue...".
		// Note that the channel numbers we record are indexed from 1, not 0.
		for (int i = 0; i < numBands; i++) {
			String name = sampleDimensionNames[i].toLowerCase();
			if (name != null) {
				if (name.matches("red.*")) {
					channelNum[RED] = i + 1;
				} else if (name.matches("green.*")) {
					channelNum[GREEN] = i + 1;
				} else if (name.matches("blue.*")) {
					channelNum[BLUE] = i + 1;
				}
			}
		}
		// If we didn't find named bands "red...", "green...", "blue..."
		// we fall back to using the first three bands in order
		if (channelNum[RED] < 0 || channelNum[GREEN] < 0
				|| channelNum[BLUE] < 0) {
			channelNum[RED] = 1;
			channelNum[GREEN] = 2;
			channelNum[BLUE] = 3;
		}
		// Now we create a RasterSymbolizer using the selected channels
		SelectedChannelType[] sct = new SelectedChannelType[coverage
				.getNumSampleDimensions()];
		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0),
				contrastMethod);
		for (int i = 0; i < 3; i++) {
			sct[i] = sf.createSelectedChannelType(
					String.valueOf(channelNum[i]), ce);
		}
		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
		ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN],
				sct[BLUE]);
		sym.setChannelSelection(sel);

		return SLD.wrapSymbolizers(sym);
	}

	public static Style create213Style(GridCoverage2D coverage) {

		// We need at least three bands to create an RGB style
		int numBands = coverage.getNumSampleDimensions();
		if (numBands < 3) {
			return null;
		}
		// Get the names of the bands
		String[] sampleDimensionNames = new String[numBands];
		for (int i = 0; i < numBands; i++) {
			GridSampleDimension dim = coverage.getSampleDimension(i);
			sampleDimensionNames[i] = dim.getDescription().toString();
		}
		final int RED = 0, GREEN = 1, BLUE = 2;
		int[] channelNum = { -1, -1, -1 };
		// We examine the band names looking for "red...", "green...",
		// "blue...".
		// Note that the channel numbers we record are indexed from 1, not 0.
		// for (int i = 0; i < numBands; i++) {
		// String name = sampleDimensionNames[i].toLowerCase();
		// if (name != null) {
		// if (name.matches("red.*")) {
		// channelNum[RED] = i + 1;
		// } else if (name.matches("green.*")) {
		// channelNum[GREEN] = i + 1;
		// } else if (name.matches("blue.*")) {
		// channelNum[BLUE] = i + 1;
		// }
		// }
		// }
		// If we didn't find named bands "red...", "green...", "blue..."
		// we fall back to using the first three bands in order
		if (channelNum[RED] < 0 || channelNum[GREEN] < 0
				|| channelNum[BLUE] < 0) {
			channelNum[RED] = 1;
			channelNum[GREEN] = 3;
			channelNum[BLUE] = 2;
		}
		// Now we create a RasterSymbolizer using the selected channels
		SelectedChannelType[] sct = new SelectedChannelType[coverage
				.getNumSampleDimensions()];
		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0),
				ContrastMethod.NONE);
		for (int i = 0; i < 3; i++) {
			sct[i] = sf.createSelectedChannelType(
					String.valueOf(channelNum[i]), ce);
		}
		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
		ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN],
				sct[BLUE]);
		sym.setChannelSelection(sel);

		return SLD.wrapSymbolizers(sym);
	}

	public static Color[] createColors(String colorString) {
		String[] colorStrings = colorString.split(";");
		Color[] colors = new Color[colorStrings.length];
		for (int i = 0; i < colors.length; i++) {
			String[] values = colorStrings[i].split(",");
			int r = Integer.parseInt(values[0].trim());
			int g = Integer.parseInt(values[1].trim());
			int b = Integer.parseInt(values[2].trim());
			int a = 255;
			if (values.length == 4) {
				a = Integer.parseInt(values[3].trim());
			}
			colors[i] = new Color(r, g, b, a);
		}
		return colors;
	}

	public static GridCoverageLayer styleLandcoverLayer(GridCoverage2D lc)
			throws IllegalArgumentException, IOException {
		Style rasterStyle = createLCStyle();
		return new GridCoverageLayer(lc, rasterStyle);
	}

	public static GridReaderLayer readAndStyleNDVILayer(File file)
			throws IllegalArgumentException, IOException {
		AbstractGridFormat format = new GeoTiffFormat();
		AbstractGridCoverage2DReader reader = format.getReader(file);
		Style rasterStyle = createNDVIStyle();
		return new GridReaderLayer(reader, rasterStyle);
	}

	public static GridCoverageLayer styleRGBLayer(GridCoverage2D coverage)
			throws IllegalArgumentException, IOException {
		Style rasterStyle = createRGBStyle(coverage);
		return new GridCoverageLayer(coverage, rasterStyle);
	}

	public static GridCoverageLayer styleGreyLayer(GridCoverage2D coverage) {
		return styleGreyLayer(coverage, ContrastMethod.HISTOGRAM);
	}
	
	public static GridCoverageLayer styleGreyLayer(GridCoverage2D coverage, ContrastMethod contrastMethod) {
		Style rasterStyle = createGreyscaleStyle(1, contrastMethod);
		return new GridCoverageLayer(coverage, rasterStyle);
	}

	public static GridReaderLayer readAndStyleRGBLayer(File file)
			throws IllegalArgumentException, IOException {
		AbstractGridFormat format = new GeoTiffFormat();
		AbstractGridCoverage2DReader reader = format.getReader(file);
		GridCoverage2D raster = reader.read(null);
		Style rasterStyle = createRGBStyle(raster);
		return new GridReaderLayer(reader, rasterStyle);
	}

	public static GridCoverage2D readRaster(File file)
			throws IllegalArgumentException, IOException {
		AbstractGridFormat format = new GeoTiffFormat();
		AbstractGridCoverage2DReader reader = format.getReader(file);
		return reader.read(null);
	}

}
