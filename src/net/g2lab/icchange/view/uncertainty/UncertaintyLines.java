package net.g2lab.icchange.view.uncertainty;

import java.awt.Graphics2D;

import org.opengis.coverage.grid.GridCoverage;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public interface UncertaintyLines {

	void drawUncertainLines(Graphics2D g2d, int widthPx, int heightPx);
	
	GridCoverage createRaster(int rasterWidth, CoordinateReferenceSystem crs);
	
}
