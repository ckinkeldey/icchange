/**
 * 
 */
package net.g2lab.icchange.view.profile;

import java.awt.Dimension;

import javax.swing.JComponent;

import net.g2lab.icchange.view.MatrixTestFrame;

import org.junit.After;
import org.junit.Before;

/**
 * @author <a href="mailto:christoph at kinkeldey dot de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class ProfilePanelTest {

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
		JComponent[][] components = new JComponent[10][5];
		for (int i = 0; i < components.length; i++) {
			for (int j = 0; j < components[0].length; j++) {
				double[] minValues = createMinValues(6);
				double[] maxValues = createMaxValues(minValues);
				double uncertainty = .1 + 0.9 * Math.random();

				// JComponent component = new ProfileBarPanel(minValues,
				// maxValues, uncertainty);
				// JComponent component = new ProfileOvalPanel(minValues,
				// maxValues, uncertainty);
				// JComponent component = new ProfileCirclePanel(minValues,
				// maxValues, uncertainty);

				JComponent component = new ProfileAreaPanel(minValues,
						maxValues, uncertainty);
				// JComponent component = new ProfileAreaInvPanel(minValues,
				// maxValues, uncertainty);

				// JComponent component = new ProfileGradientBarPanel(minValues,
				// maxValues, uncertainty);
				// JComponent component = new ProfileMinMaxBarPanel(minValues,
				// maxValues, uncertainty);
				// JComponent component = new
				// ProfileMinAvgMaxBarPanel(minValues, maxValues, uncertainty);

				component.setPreferredSize(new Dimension(100, 100));
				components[i][j] = component;
			}
		}
		new MatrixTestFrame(components);
	}

	private static double[] createHistogram() {
		double[] histogram = new double[10];
		for (int i = 0; i < 10; i++) {
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
