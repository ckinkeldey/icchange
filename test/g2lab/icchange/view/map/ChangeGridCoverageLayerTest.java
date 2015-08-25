package net.g2lab.icchange.view.map;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import net.g2lab.icchange.model.ChangeSequence;
import net.g2lab.icchange.model.ChangeSequenceGroup;
import net.g2lab.icchange.model.ObjectClass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangeGridCoverageLayerTest {

	private List<ChangeSequenceGroup> changeList;

	@Before
	public void setUp() throws Exception {
		changeList = new ArrayList<ChangeSequenceGroup>();
		ChangeSequenceGroup group = new ChangeSequenceGroup();
		ChangeSequence sequence = new ChangeSequence();
		ObjectClass[] classes = new ObjectClass[3];
		classes[0] = new ObjectClass("1");
		classes[1] = new ObjectClass("2");
		classes[2] = new ObjectClass("3");
		classes[0].setClasscode(1);
		classes[1].setClasscode(2);
		classes[2].setClasscode(3);
		sequence.setClasses(classes);
		group.add(sequence);
		changeList.add(group );
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testChangeIncludedNull() {
		assertThat(ChangeGridCoverageLayer.changeIncluded(0, 0, 0, null), is(true));
	}
	
	@Test
	public void testChangeIncluded() {
		assertThat(ChangeGridCoverageLayer.changeIncluded(0, 0, 0, changeList), is(false));
		assertThat(ChangeGridCoverageLayer.changeIncluded(3, 2, 1, changeList), is(false));
		assertThat(ChangeGridCoverageLayer.changeIncluded(1, 2, 3, changeList), is(true));
	}

}
