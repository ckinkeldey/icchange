/**
 * 
 */
package net.g2lab.icchange.model;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:christoph at kinkeldey dot de">Christoph Kinkeldey</a>
 *
 */
public class ChangeSequenceGroupTest {

	private ChangeSequenceGroup group;
	private ChangeSequence sequence;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.group = new ChangeSequenceGroup();
		this.sequence = createTestSequence();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testIsSequenceFitting() {
		assertThat(group.isSequenceFitting(sequence), is(true));
		group.add(sequence);
		assertThat(group.isSequenceFitting(sequence), is(true));
		ChangeSequence newSequence = createTestSequence();
		newSequence.setUncertainties(new double[] {0,0});
		assertThat(group.isSequenceFitting(newSequence), is(true));
	}
	
	@Test
	public void testGetChangeCode() {
		assertThat(group.getChangeCode(), is(0));
	}
	
	private ChangeSequence createTestSequence() {
		ChangeSequence sequence = new ChangeSequence();
		ObjectClass objClass = new ObjectClass(0, "A");
		sequence.setClasses(new ObjectClass[] {objClass});
		sequence.setDates(new Calendar[] {new GregorianCalendar(2000,1,1)});
		sequence.setUncertainties(new double[] {0});
		return sequence;
	}
	
	

}
