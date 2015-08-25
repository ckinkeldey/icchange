package net.g2lab.icchange.model;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph
 *         Kinkeldey</a>
 * 
 */
public class ChangeSequenceTest {

	private ChangeSequence changePattern;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.changePattern = new ChangeSequence();
		changePattern.setDates(createDates());
		changePattern.setClasses(createClasses());
		changePattern.setUncertainties(createUncertainties());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreation() {
		assertThat(new ChangeSequence(), is(notNullValue()));
	}

	@Test
	public void testGetDates() {
		assertThat(changePattern.getDates()[0],
				equalTo((Calendar) new GregorianCalendar(2000, 5, 1)));
		assertThat(changePattern.getDates()[1],
				equalTo((Calendar) new GregorianCalendar(2000, 7, 1)));
		assertThat(changePattern.getDates()[2],
				equalTo((Calendar) new GregorianCalendar(2001, 3, 1)));
	}

	@Test
	public void testGetChangeUncertainty() {
		assertThat(changePattern.getUncertainties()[0], equalTo(.2));
		assertThat(changePattern.getUncertainties()[1], equalTo(.5));
	}
	
	@Test
	public void testGetChangeCode() {
		int[] pixels = {1,2,3};
		assertThat(ChangeSequence.getChangeCode(pixels), equalTo(34));
	}

	@Test
	public void testGetChangeCodeZero() {
		int[] pixels = {1,1};
		assertThat(ChangeSequence.getChangeCode(pixels), equalTo(0));
	}
	
	private Calendar[] createDates() {
		Calendar[] dates = new Calendar[3];
		dates[0] = new GregorianCalendar(2000, 5, 1);
		dates[1] = new GregorianCalendar(2000, 7, 1);
		dates[2] = new GregorianCalendar(2001, 3, 1);
		return dates;
	}

	private ObjectClass[] createClasses() {
		return new ObjectClass[] { new ObjectClass("classA"),
				new ObjectClass("classB"), new ObjectClass("classC") };
	}

	private double[] createUncertainties() {
		return new double[] { .2, .5 };
	}

}
