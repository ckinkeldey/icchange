/**
 * 
 */
package net.g2lab.icchange.view.histogram;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import net.g2lab.icchange.view.MatrixTestFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

/**
 * @author <a href="mailto:christoph at kinkeldey dot de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class HistogramPanelTest {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	private Map<Integer, double[][]> histograms_date1 = new HashMap<Integer, double[][]>();
	private Map<Integer, double[][]> histograms_date2 = new HashMap<Integer, double[][]>();

	public HistogramPanelTest() {
		try {
			int numDates = 2;
			int numObjects = 0;
			for (int band = 1; band <= 5; band++) {
				double[][] histogramValues = loadHistogram(band, "07");
				this.histograms_date1.put(band, histogramValues);
				numObjects = histogramValues.length;
			}
			for (int band = 1; band <= 5; band++) {
				double[][] histogramValues = loadHistogram(band, "08");
				this.histograms_date2.put(band, histogramValues);
				numObjects = histogramValues.length;
			}

			JComponent[][] components = new JComponent[numDates][numObjects];
			for (int date = 0; date < components.length; date++) {
				for (int obj = 0; obj < components[0].length; obj++) {
					Map<Integer, double[][]> histograms;
					if (date == 0) {
						histograms = histograms_date1;
					} else {
						histograms = histograms_date2;
					}
					double uncertainty = .1 + 0.9 * Math.random();
					// LOG.debug(numObjects + " " + histogramValues[0].length);
					JComponent component = new HistogramPanel(new double[][] {
							histograms.get(1)[obj], histograms.get(2)[obj],
							histograms.get(3)[obj], histograms.get(4)[obj],
							histograms.get(5)[obj] });

					component.setPreferredSize(new Dimension(300, 100));
					components[date][obj] = component;
				}
			}
			new MatrixTestFrame(components);
		} catch (Exception e) {
			LOG.error(e, e);
		}
	}

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

	public static void main(String[] args) {
		new HistogramPanelTest();
	}

	private static double[][] loadHistogram(int num, String date)
			throws FileNotFoundException, DBFException {
		String file = HistogramPanelTest.class.getResource(
				"change-" + date + "_b" + num + ".dbf").getPath();
		InputStream inputStream = new FileInputStream(file);
		DBFReader reader = new DBFReader(inputStream);
		int numFields = reader.getFieldCount();
		for (int i = 0; i < numFields; i++) {
			DBFField field = reader.getField(i);
			LOG.debug("field " + i + ": " + field.getName());
		}
		int n = 0;
		Object[] rowObjects;
		int numRecords = reader.getRecordCount();
		numFields = 30;
		double[][] values = new double[numFields][numRecords];
		while ((rowObjects = reader.nextRecord()) != null) {
			for (int i = 0; i < numFields; i++) {
				values[i][n] = new Double((Double) rowObjects[i + 40]);
				// LOG.debug(values[n][i]);
			}
			n++;
		}

		return values;
	}

	private static double[] createHistogram(int size) {
		double[] histogram = new double[size];
		for (int i = 0; i < size; i++) {
			histogram[i] = Math.random() * 100;
		}
		return histogram;
	}

	private static double[] createMaxValues(double[] minValues) {
		double[] maxValues = new double[minValues.length];
		for (int i = 0; i < minValues.length; i++) {
			double diff = 255 - minValues[i] - 20;
			maxValues[i] = minValues[i] + 20 + Math.random() * diff;
		}

		return maxValues;
	}

	private static double[] createMinValues(int size) {
		double[] minValues = new double[size];
		for (int i = 0; i < minValues.length; i++) {
			minValues[i] = Math.random() * 150;
		}
		return minValues;
	}

}
