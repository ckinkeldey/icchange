/**
 * 
 */
package net.g2lab.icchange.view.map;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.g2lab.icchange.model.ChangeSequence;
import net.g2lab.icchange.model.ChangeSequenceGroup;
import net.g2lab.icchange.view.sequence.ChangeInfoView;
import net.g2lab.icchange.view.style.LayerStyles;
import net.g2lab.icchange.view.uncertainty.NoiseAnnotation;

import org.apache.commons.collections.functors.SwitchTransformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.action.ResetAction;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.ZoomInTool;
import org.geotools.swing.tool.ZoomOutTool;
import org.opengis.style.ContrastMethod;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class MapView extends JMapFrame implements MouseListener,
		MouseMotionListener, MouseWheelListener, KeyListener, ListDataListener,
		ListSelectionListener {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	private static final String MAPVIEW_TITLE = "ICChange | map view";

	public static Color BACKGROUND_COLOR = new Color(100, 100, 100);
	public static Color bgColor = new Color(100, 100, 100);// BACKGROUND_COLOR;

	private static int a8nGridSize = 200;

	private AbstractGridCoverage2DReader reader;

	private MapContent map;

	private GridCoverageLayer[] lcLayers;
	private GridCoverageLayer[] satLayers;
	private AnnotationLayer a8nLayer;

	private int satLayerIndex, lcLayerIndex, changeLayerIndex, uMaskIndex,
			na8LayerIndex;

	private List<ChangeSequenceGroup> groups;

	private ChangeGridCoverageLayer changeLayer;

	private ChangeInfoView changeInfoView;

	private GridCoverage2D[] lc;

	private int mouseX, mouseY;

	private GridCoverageLayer uGreyscaleLayer;

	private int layerIndex = 0;

	private EmptyLayer fakeLayer;

	private boolean altPressed;

	private Set<Integer> allCodes;

	private GridCoverageLayer uMask;

	public MapView(GridCoverage2D[] lc, GridCoverage2D[] sat,
			GridCoverage2D uOverall) {
		this(sat, lc, uOverall, null);
	}

	public MapView(GridCoverage2D[] sat, GridCoverage2D[] lc,
			GridCoverage2D uOverall, ChangeInfoView changeInfoView) {
		this.lc = lc;
		this.changeInfoView = changeInfoView;
		this.satLayers = new GridCoverageLayer[sat.length];
		this.lcLayers = new GridCoverageLayer[lc.length];

		try {

			for (int i = 0; i < sat.length; i++) {
//				 satLayers[i] = LayerStyles.styleRGBLayer(sat[i]);
//				 satLayers[i] = LayerStyles.styleGreyLayer(sat[i], ContrastMethod.HISTOGRAM);
				satLayers[i] = new GridCoverageLayer(sat[i], LayerStyles.createRGBStyle(sat[i], ContrastMethod.HISTOGRAM));
			}

			for (int i = 0; i < lc.length; i++) {
				lcLayers[i] = LayerStyles.styleLandcoverLayer(lc[i]);
			}

			if (lc.length > 0) {
				this.allCodes = createAllCodes(lc);
				Style changeLayerStyle = LayerStyles.createChangeStyle(
						allCodes, changeInfoView.getChanges());
				this.changeLayer = new ChangeGridCoverageLayer(lc, uOverall,
						changeLayerStyle);

				Style uncertaintyMaskStyle = LayerStyles
						.createUncertaintyMaskStyle(100);
				this.uMask = new GridCoverageLayer(uOverall,
						uncertaintyMaskStyle);

				// this.uGreyscaleLayer = new GridCoverageLayer(uOverall,
				// LayerStyles.createGreyscaleStyle(1));

				this.a8nLayer = new AnnotationLayer(getMapPane(), uOverall,
						a8nGridSize);
			}
			getMapPane().setBackground(bgColor);
			this.map = new MapContent();
			map.setTitle("ICchange");

			this.satLayerIndex = 0;
			this.lcLayerIndex = satLayers.length;
			this.changeLayerIndex = satLayers.length + lcLayers.length;
			this.uMaskIndex = changeLayerIndex + 1;
			this.na8LayerIndex = uMaskIndex + 1;

			if (satLayers.length > 0) {
				map.addLayers(Arrays.asList(satLayers));
				// turn off all but one sat layer
				for (int i = 1; i < satLayers.length; i++) {
					toggleLayer(satLayerIndex + i);
				}
			}
			if (lcLayers.length > 0) {
				map.addLayers(Arrays.asList(lcLayers));
				// turn off all layers
				for (int i = 0; i < lcLayers.length; i++) {
					toggleLayer(lcLayerIndex + i);
				}
			}
			map.addLayer(changeLayer);
			map.addLayer(uMask);
			map.addLayer(a8nLayer);

			this.setMapContent(map);
			this.setTitle(MAPVIEW_TITLE);
			this.pack();
			this.enableToolBar(true);
			this.enableTool(Tool.PAN, Tool.RESET, Tool.INFO, Tool.ZOOM);
			JButton panButton = (JButton) this.getToolBar()
					.getComponentAtIndex(3);
			panButton.doClick();
			JButton resetButton = (JButton) this.getToolBar()
					.getComponentAtIndex(7);
			resetButton.setAction(new ExtendedResetAction(getMapPane(),
					a8nLayer));

			this.enableStatusBar(false);
			this.enableLayerTable(false);

			RenderingHints hints = new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			this.getMapPane().getRenderer().setJava2DHints(hints);

			this.addKeyListener(this);
			getToolBar().addKeyListener(this);
			getMapPane().addKeyListener(this);
			changeInfoView.addKeyListener(this);
			getMapPane().addMouseListener(this);
			getMapPane().addMouseMotionListener(this);
			getMapPane().addMouseWheelListener(this);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setBackgroundColor(Color color) {
		this.bgColor = color;
		getMapPane().setBackground(bgColor);
	}

	public AnnotationLayer getA8nLayer() {
		return a8nLayer;
	}

	private static Set<Integer> createAllCodes(GridCoverage2D[] lc) {
		Set<Integer> codes = new HashSet<Integer>();
		Raster[] imageData = new Raster[lc.length];
		for (int i = 0; i < lc.length; i++) {
			imageData[i] = lc[i].getRenderedImage().getData();
		}
		for (int i = 0; i < imageData[0].getWidth(); i++) {
			for (int j = 0; j < imageData[0].getHeight(); j++) {
				int[] pixels = new int[lc.length];
				for (int k = 0; k < pixels.length; k++) {
					pixels[k] = imageData[k].getPixel(i, j, new int[1])[0];
				}
				// if (ChangeSequence.isNoChange(pixels)) {
				// codes.add(new Integer(0));
				// } else {
				codes.add(ChangeSequence.getChangeCode(pixels));
				// }

			}
		}
		return codes;
	}

	public void setChangeInfoView(ChangeInfoView infoView) {
		this.changeInfoView = infoView;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ALT) {
			altPressed = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_F1) {
			org.geotools.map.Layer layer = map.layers().get(satLayerIndex);
			if (!layer.isVisible()) {
				// switch on current layer
				toggleLayer(satLayerIndex);
			} else {
				// switch off current layer
				toggleLayer(satLayerIndex);
				this.satLayerIndex += 1;
				if (satLayerIndex == satLayers.length) {
					satLayerIndex = 0;
					// do not switch on new
				} else {
					// switch on new
					toggleLayer(satLayerIndex);
				}
				return;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_F2) {
			org.geotools.map.Layer layer = map.layers().get(lcLayerIndex);
			if (!layer.isVisible()) {
				// switch on current layer
				toggleLayer(lcLayerIndex);
			} else {
				// switch off current layer
				toggleLayer(lcLayerIndex);
				this.lcLayerIndex += 1;
				if (lcLayerIndex == satLayers.length + lcLayers.length) {
					lcLayerIndex = satLayers.length;
					// toggleLayer(lcLayerIndex);
				} else {
					// switch on new
					toggleLayer(lcLayerIndex);
				}
			}
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F3) {
			toggleLayer(changeLayerIndex);
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F4) {
			toggleLayer(na8LayerIndex);
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F5) {
			toggleLayer(4);
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F6) {
			toggleLayer(5);
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F7) {
			toggleLayer(6);
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F8) {
			toggleLayer(7);
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F9) {
			toggleLayer(8);
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_F10) {
			toggleLayer(9);
			return;
		}

		if (e.getKeyChar() == '+') {
			a8nGridSize /= 2;
			a8nLayer.getAnnotation().setGridSizeX(a8nGridSize);
			a8nLayer.getAnnotation().setGridSizeY(a8nGridSize);
			this.a8nLayer.setVisible(false);
			this.a8nLayer.setVisible(true);
			return;
		}
		if (e.getKeyChar() == '-') {
			a8nGridSize *= 2;
			a8nLayer.getAnnotation().setGridSizeX(a8nGridSize);
			a8nLayer.getAnnotation().setGridSizeY(a8nGridSize);
			this.a8nLayer.setVisible(false);
			this.a8nLayer.setVisible(true);
			return;
		}
		if (e.getKeyChar() == ' ') {
			// LOG.debug("update change layer.");
			// updateChangeLayer();
			// changeLayer.setVisible(false);
			// changeLayer.setVisible(true);
			// return;
			repaint();
		}
		if (e.getKeyChar() == '>') {
			NoiseAnnotation a8n = a8nLayer.getAnnotation();
			double offsetX = (a8nLayer.getAnnotation().getOffsetX() + a8n
					.getWidth(1)) % a8n.getGridSizeX();
			LOG.debug("new offset x == " + offsetX);
			this.a8nLayer.getAnnotation().setOffsetX(offsetX);
			this.a8nLayer.setVisible(false);
			this.a8nLayer.setVisible(true);
			return;
		}
		if (e.getKeyChar() == '<') {
			NoiseAnnotation a8n = a8nLayer.getAnnotation();
			double offsetX = (a8nLayer.getAnnotation().getOffsetX() - a8n
					.getWidth(1)) % a8n.getGridSizeX();
			LOG.debug("new offset x == " + offsetX);
			this.a8nLayer.getAnnotation().setOffsetX(offsetX);
			this.a8nLayer.setVisible(false);
			this.a8nLayer.setVisible(true);
			return;
		}
	}

	public void toggleLayer(int index) {
		if (map.layers().size() > index) {
			org.geotools.map.Layer layer = map.layers().get(index);
			layer.setVisible(!layer.isVisible());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ALT) {
			altPressed = false;
		}
	}

	@Override
	public void intervalAdded(ListDataEvent e) {
		// updateChangeLayer();
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
		// updateChangeLayer();
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
		updateChangeLayer();
	}

	private void updateChangeLayer() {
		if (changeInfoView != null) {
			try {
				UpdateChangeThread upChangeThread = new UpdateChangeThread();
				upChangeThread.run();
				upChangeThread.join();
				
				FilterByUThread filterUncertThread = new FilterByUThread(changeInfoView.getUThreshold());
				filterUncertThread.run();
				filterUncertThread.join();
			} catch (InterruptedException e) {
				LOG.error(e, e);
			}
		}

	}

	public class UpdateChangeThread extends Thread {

		public void run() {
			Collection<ChangeSequenceGroup> changeList = changeInfoView
					.getChanges();
			LOG.debug("now " + changeList.size() + " change groups are shown.");

			changeLayer.setStyle(LayerStyles.createChangeStyle(allCodes, changeList));
			getMapPane().setDisplayArea(getMapPane().getDisplayArea());
		}

	}

	public class FilterByUThread extends Thread {

		private int uThreshold;

		public FilterByUThread(int uThreshold) {
			this.uThreshold = uThreshold;
		}

		public void run() {
			uMask.setStyle(LayerStyles.createUncertaintyMaskStyle(uThreshold));
			getMapPane().setDisplayArea(getMapPane().getDisplayArea());
		}

	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON1
				&& event.getClickCount() == 2) {
			MapMouseEvent e = new MapMouseEvent(getMapPane(), event);
			ZoomInTool zoomIn = new ZoomInTool();
			zoomIn.setMapPane(getMapPane());
			zoomIn.onMouseClicked(e);
		} else if (event.getButton() == MouseEvent.BUTTON3
				&& event.getClickCount() == 2) {
			MapMouseEvent e = new MapMouseEvent(getMapPane(), event);
			ZoomOutTool zoomOut = new ZoomOutTool();
			zoomOut.setMapPane(getMapPane());
			zoomOut.onMouseClicked(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		LOG.debug("Mouse pressed: " + e.getX());
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		double a8nGridSize = a8nLayer.getAnnotation().getGridSizeX();
		if (e.getPreciseWheelRotation() > 0) {
			a8nGridSize *= 1.2;
		} else {
			a8nGridSize /= 1.2;
		}
		new UpdateNALinesThread(a8nGridSize).run();
	}

	public class ZoomInThread extends Thread {

		public void run() {
			Collection<ChangeSequenceGroup> changeList = changeInfoView
					.getChanges();
			LOG.debug("now " + changeList.size() + " change groups are shown.");

			changeLayer.setStyle(LayerStyles.createChangeStyle(allCodes,
					changeList));
			getMapPane().repaint();
		}

	}

	public class UpdateNALinesThread extends Thread {

		private double gridSize;

		public UpdateNALinesThread(double gridSize) {
			this.gridSize = gridSize;
		}

		public void run() {
			// a8nLayer.getAnnotation().setGridSizeX(gridSize);
			// a8nLayer.getAnnotation().setGridSizeY(gridSize);
			a8nLayer.setAnnotation(new NoiseAnnotation(a8nLayer.getAnnotation()
					.getUncertaintyLayer(), gridSize));
			// getMapPane().drawLayers(false);
			getMapPane().setDisplayArea(getMapPane().getDisplayArea());
		}

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
	}

}
