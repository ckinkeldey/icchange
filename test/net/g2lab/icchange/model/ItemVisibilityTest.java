/**
 * 
 */
package net.g2lab.icchange.model;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kinkeldey
 * 
 */
public class ItemVisibilityTest {

	private ItemVisibility visibility;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		visibility = new ItemVisibility(10);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreation() {
		assertThat(visibility, is(notNullValue()));
	}

	@Test
	public void testIsVisible() {
		assertThat(visibility.isVisible(0), is(true));
	}

	@Test
	public void testGetNumVisible() {
		assertThat(visibility.getNumVisible(), is(10));
	}

	public static void main(String[] args) {

	}

}
