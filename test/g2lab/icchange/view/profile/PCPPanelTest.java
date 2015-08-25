/**
 * 
 */
package net.g2lab.icchange.view.profile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import net.g2lab.icchange.model.SpectralValues;
import net.g2lab.icchange.view.TestFrame;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import darrylbu.plaf.vertical.VerticalLabelUI;

/**
 * @author <a href="mailto:christoph at kinkeldey dot de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class PCPPanelTest {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		// URL band1URL = PCPPanelTest.class.getResource("change-07_b1.txt");
		// URL band2URL = PCPPanelTest.class.getResource("change-07_b2.txt");
		// URL band3URL = PCPPanelTest.class.getResource("change-07_b3.txt");
		// URL band4URL = PCPPanelTest.class.getResource("change-07_b4.txt");
		// URL band5URL = PCPPanelTest.class.getResource("change-07_b5.txt");
		// URL uncertURL =
		// PCPPanelTest.class.getResource("uncertainties-07.txt");

		URL band1URL = PCPPanelTest.class
				.getResource("rastert_delta056-b1.txt");
		URL band2URL = PCPPanelTest.class
				.getResource("rastert_delta056-b2.txt");
		URL band3URL = PCPPanelTest.class
				.getResource("rastert_delta056-b1.txt");
		URL band4URL = PCPPanelTest.class
				.getResource("rastert_delta056-b4.txt");
		URL band5URL = PCPPanelTest.class
				.getResource("rastert_delta056-b5.txt");
		URL ndviURL = PCPPanelTest.class.getResource("rastert_0503-dndvi1.txt");
		// URL uncerURL =
		// PCPPanelTest.class.getResource("uncertainties-2010-03.txt");
		URL uncertURL = PCPPanelTest.class
				.getResource("rastert_0503-uncert.txt");

		double[][] band1Values = loadSpectralValues(
				new File(band1URL.getFile()), 50, 50);
		double[][] band2Values = loadSpectralValues(
				new File(band2URL.getFile()), 50, 50);
		double[][] band3Values = loadSpectralValues(
				new File(band3URL.getFile()), 50, 50);
		double[][] band4Values = loadSpectralValues(
				new File(band4URL.getFile()), 50, 50);
		double[][] band5Values = loadSpectralValues(
				new File(band5URL.getFile()), 50, 50);
		double[][] ndviValues = loadSpectralValuesFloat(new File(ndviURL
				.getFile()), 100, 100);
		double[][] uncertainties = loadSpectralValuesFloat(new File(uncertURL
				.getFile()), 100, 100);

		SpectralValues[] bandValues = new SpectralValues[1];

		LOG.debug(countValidValues(band1Values) + " valid values");
		LOG.debug(countValidValues(band2Values) + " valid values");
		LOG.debug(countValidValues(band3Values) + " valid values");
		LOG.debug(countValidValues(band4Values) + " valid values");
		LOG.debug(countValidValues(band5Values) + " valid values");
		LOG.debug(countValidValues(uncertainties) + " valid values");
		LOG.debug(countValidValues(ndviValues) + " valid values");

		bandValues[0] = new SpectralValues(7);
		bandValues[0].add(band1Values);
		bandValues[0].add(band2Values);
		bandValues[0].add(band3Values);
		bandValues[0].add(band4Values);
		bandValues[0].add(band5Values);
		bandValues[0].add(uncertainties);
		// bandValues[0].add(ndviValues);
		bandValues[0].setUncertainties(ndviValues);

		// band1URL = PCPPanelTest.class.getResource("change-08_b1.txt");
		// band2URL = PCPPanelTest.class.getResource("change-08_b2.txt");
		// band3URL = PCPPanelTest.class.getResource("change-08_b3.txt");
		// band4URL = PCPPanelTest.class.getResource("change-08_b4.txt");
		// band5URL = PCPPanelTest.class.getResource("change-08_b5.txt");
		// uncertURL = PCPPanelTest.class.getResource("uncertainties-08.txt");
		//
		// band1Values = loadSpectralValues(new File(band1URL.getFile()));
		// band2Values = loadSpectralValues(new File(band2URL.getFile()));
		// band3Values = loadSpectralValues(new File(band3URL.getFile()));
		// band4Values = loadSpectralValues(new File(band4URL.getFile()));
		// band5Values = loadSpectralValues(new File(band5URL.getFile()));
		// uncertainties = loadSpectralValuesFloat(new
		// File(uncertURL.getFile()));
		//
		// bandValues[1] = new SpectralValues(5);
		// bandValues[1].add(band1Values);
		// bandValues[1].add(band2Values);
		// bandValues[1].add(band3Values);
		// bandValues[1].add(band4Values);
		// bandValues[1].add(band5Values);
		// bandValues[1].setUncertainties(uncertainties);

		String[] classStrings = new String[] { "gain in vegetation",
				"class 'arable land'" };
		String[] dateStrings = new String[] { "03-2010 / 05-2010", "08-13-2010" };

		JComponent[][] components = new JComponent[1][1];
		int n = 0;
		for (int i = 0; i < components.length; i++) {
			for (int j = 0; j < components[0].length; j++) {
				double[][] spectralValues = convertSpectralValues(bandValues[i % 2]);
				double[] uncertaintyValues = convertUncertaintyValues(bandValues[i % 2]);
				n++;
				JComponent component = new JPanel();
				component.setLayout(new BorderLayout());

				JPanel labelPanel = new JPanel();
				labelPanel.setLayout(new BorderLayout());
				JLabel dateLabel = new JLabel(dateStrings[i % 2],
						SwingConstants.CENTER);
				dateLabel.setFont(new Font("Arial", Font.ITALIC, 20));
				dateLabel.setUI(new VerticalLabelUI());
				Dimension preferredSize = new Dimension(50, 100);
				dateLabel.setPreferredSize(preferredSize);
				JLabel classLabel = new JLabel(classStrings[i % 2],
						SwingConstants.CENTER);
				classLabel.setFont(new Font("Arial", Font.BOLD, 20));
				classLabel.setUI(new VerticalLabelUI());
				labelPanel.add(dateLabel, BorderLayout.EAST);
				labelPanel.add(classLabel, BorderLayout.WEST);

				// JXMultiSlider slider = new
				// JXMultiSlider(JXMultiSlider.VERTICAL, 0, 100, 0, 100);
				JSlider slider = new VerticalThresholdSlider();
				JSlider slider2 = new VerticalThresholdLabelSlider();
				JPanel sliderPanel = new JPanel();
				// TitledBorder titledBorder =
				// BorderFactory.createTitledBorder("Uncertainty");

				RangePanel thermometer = new RangePanel();

				sliderPanel.setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				JLabel uncertLabel = new JLabel("delta ndvi",
						SwingConstants.CENTER);
				uncertLabel.setFont(new Font("Arial", Font.ITALIC, 20));
				// uncertLabel.setPreferredSize(new Dimension(200, 50));
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.gridwidth = 3;
				gbc.gridheight = 1;
				gbc.weightx = 1;
				gbc.weighty = .1;
				gbc.anchor = GridBagConstraints.CENTER;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				sliderPanel.add(uncertLabel, gbc);
				gbc.gridx = 0;
				gbc.gridy = 1;
				gbc.weightx = 1;
				gbc.weighty = 1;
				gbc.fill = GridBagConstraints.VERTICAL;
				gbc.gridwidth = 1;
				gbc.anchor = GridBagConstraints.EAST;
				sliderPanel.add(slider, gbc);
				gbc.gridx = 1;
				gbc.anchor = GridBagConstraints.CENTER;
				sliderPanel.add(thermometer, gbc);
				gbc.gridx = 2;
				gbc.anchor = GridBagConstraints.WEST;
				sliderPanel.add(slider2, gbc);

				Dimension sliderPanelDim = new Dimension(150, 20);
				sliderPanel.setPreferredSize(sliderPanelDim);

				double[] minValues = bandValues[i % 2].getMinValues();
				double[] maxValues = bandValues[i % 2].getMaxValues();
				PCPPanel pcp = new PCPPanel(spectralValues, minValues,
						maxValues, uncertaintyValues);
				slider.addChangeListener(pcp);
				slider2.addChangeListener(pcp);
				slider.addMouseListener(pcp);
				slider2.addMouseListener(pcp);

				slider.addChangeListener(thermometer);
				slider2.addChangeListener(thermometer);

				component.add(labelPanel, BorderLayout.WEST);
				component.add(pcp, BorderLayout.CENTER);
				component.add(sliderPanel, BorderLayout.EAST);
				component.setPreferredSize(new Dimension(500, 100));
				components[i][j] = component;
			}
		}
		TestFrame frame = new TestFrame(components);
		frame.setBackground(Color.WHITE);
	}

	private static int countValidValues(double[][] values) {
		int n = 0;
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				if (!Double.isNaN(values[i][j])) {
					n++;
				}
			}
		}
		return n;
	}

	private static double[][] convertSpectralValues(
			SpectralValues spectralValues) {
		int numBands = spectralValues.getNumBands();
		double[] bandValues1 = extractValues(spectralValues.getValues(0));
		int numValues = bandValues1.length;
		double[][] values = new double[numBands][numValues];
		for (int band = 0; band < numBands; band++) {
			double[] bandValues = extractValues(spectralValues.getValues(band));
			for (int i = 0; i < numValues; i++) {
				values[band][i] = bandValues[i];
			}
		}
		return values;
	}

	private static double[] convertUncertaintyValues(
			SpectralValues spectralValues) {
		return extractValues(spectralValues.getUncertainties());
	}

	private static double[] extractValues(double[][] values) {
		List<Double> validValues = new ArrayList<Double>();
		for (int row = 0; row < values.length; row++) {
			for (int col = 0; col < values[0].length; col++) {
				if (!Double.isNaN(values[row][col])) {
					validValues.add(values[row][col]);
				}
			}
		}
		return toArray(validValues);
	}

	// private static double[] extractUncertaintyValues(double[][] values) {
	// List<Double> validValues = new ArrayList<Double>();
	// for (int row = 0; row < values.length; row++) {
	// for (int col = 0; col < values[0].length; col++) {
	// if (values[row][col] >= 0) {
	// double scale = 1;
	// double uncertValue = values[row][col];
	// if (uncertValue < 0.5/scale) {
	// uncertValue *= scale;
	// }
	// validValues.add(uncertValue);
	// }
	// }
	// }
	// return toArray(validValues);
	// }

	private static double[] toArray(List<Double> list) {
		double[] array = new double[list.size()];
		int i = 0;
		for (double value : list) {
			array[i++] = value;
		}
		return array;
	}

	// private static double[][] createSpectralValues(int numValues, int
	// numBands) {
	// double[][] values = new double[numValues][numBands];
	// for (int band = 0; band < numBands; band++) {
	// int min = (int) (Math.random() * 100);
	// int max = (int) (Math.random() * 100)+155;
	// for (int i=0; i < numValues; i++) {
	// values[i][band] = min + Math.random() * (max - min);
	// }
	// }
	// return values;
	// }

	private static double[][] loadSpectralValues(File file)
			throws FileNotFoundException, IOException {
		return loadSpectralValues(file, -1, -1);
	}

	private static double[][] loadSpectralValues(File file, int i, int j)
			throws FileNotFoundException, IOException {
		List<String> lines = IOUtils.readLines(new FileReader(file));
		int numColumns, numRows;
		if (i == -1) {
			numColumns = Integer.parseInt(lines.get(0).split("\\s+")[1]);
			numRows = Integer.parseInt(lines.get(1).split("\\s+")[1]);
		} else {
			numColumns = i;
			numRows = j;
		}
		double[][] values = new double[numColumns][numRows];
		for (int row = 0; row < numRows; row++) {
			String line = lines.get(row + 6);
			String[] tokens = line.split(" ");
			for (int n = 0; n < numColumns; n++) {
				if ("-9999".equals(tokens[n])) {
					values[n][row] = Double.NaN;
				} else {
					values[n][row] = Integer.parseInt(tokens[n]);
				}
			}
		}
		return values;
	}

	private static double[][] loadSpectralValuesFloat(File file)
			throws FileNotFoundException, IOException {
		return loadSpectralValuesFloat(file, -1, -1);
	}

	private static double[][] loadSpectralValuesFloat(File file, int i, int j)
			throws FileNotFoundException, IOException {
		List<String> lines = IOUtils.readLines(new FileReader(file));
		int numColumns, numRows;
		if (i == -1) {
			numColumns = Integer.parseInt(lines.get(0).split("\\s+")[1]);
			numRows = Integer.parseInt(lines.get(1).split("\\s+")[1]);
		} else {
			numColumns = i;
			numRows = j;
		}
		double[][] values = new double[numColumns][numRows];
		for (int row = 0; row < numRows; row++) {
			String line = lines.get(row + 6);
			String[] tokens = line.split(" ");
			for (int n = 0; n < numColumns; n++) {
				if ("-9999".equals(tokens[n])) {
					values[n][row] = Double.NaN;
				} else {
					values[n][row] = Double.parseDouble(tokens[n].replace(",",
							"."));
				}
			}
		}
		return values;
	}

	private static double[][] loadSpectralValues1MinusFloat(File file)
			throws FileNotFoundException, IOException {
		List<String> lines = IOUtils.readLines(new FileReader(file));
		int numColumns = Integer.parseInt(lines.get(0).split("\\s+")[1]);
		int numRows = Integer.parseInt(lines.get(1).split("\\s+")[1]);
		double[][] values = new double[numColumns][numRows];
		for (int row = 0; row < numRows; row++) {
			String line = lines.get(row + 6);
			String[] tokens = line.split(" ");
			for (int n = 0; n < tokens.length; n++) {
				if ("-9999".equals(tokens[n])) {
					values[n][row] = Double.NaN;
				} else {
					values[n][row] = 1 - Double.parseDouble(tokens[n].replace(
							",", "."));
				}
			}
		}
		return values;
	}
}
