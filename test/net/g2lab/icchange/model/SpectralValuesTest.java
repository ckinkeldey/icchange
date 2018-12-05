/**
 * 
 */
package net.g2lab.icchange.model;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:christoph at kinkeldey dot de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class SpectralValuesTest {

	private SpectralValues spectralValues;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.spectralValues = new SpectralValues(1);
		spectralValues.add(createTestData());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetValues() {
		assertThat(spectralValues.getValues(0), is(createTestData()));
	}

	@Test
	public void testGetMinValues() {
		assertThat(spectralValues.getMinValues(), is(new double[] { 1 }));
	}

	@Test
	public void testGetMaxValues() {
		assertThat(spectralValues.getMaxValues(), is(new double[] { 6 }));
	}

	private double[][] createTestData() {
		double[][] testData = new double[3][2];
		testData[0][0] = 3;
		testData[0][1] = 2;
		testData[1][0] = 1;
		testData[1][1] = 6;
		testData[2][0] = 5;
		testData[2][1] = 4;
		return testData;
	}

}
