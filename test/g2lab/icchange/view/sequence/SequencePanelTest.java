package net.g2lab.icchange.view.sequence;


import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JFrame;

import net.g2lab.icchange.model.ChangeSequence;
import net.g2lab.icchange.model.ChangeSequenceGroup;
import net.g2lab.icchange.model.ObjectClass;

import org.junit.After;
import org.junit.Before;

public class SequencePanelTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	public static void main(String[] args) {
		Dimension preferredSize = new Dimension(400, 100);
		Color[] colors = new Color[] {Color.WHITE, Color.DARK_GRAY};
		double[] distances = new double[] {1., 2.};
		JFrame frame = new JFrame();
		Container contentPane = frame.getContentPane();
		GridLayout layout = new GridLayout();
		contentPane.setLayout(layout);
//		contentPane.add(createPanel(preferredSize, colors, distances, new int[] {0, 0, 0}));
		contentPane.add(createPanel(preferredSize, colors, distances, new int[] {0, 20, 40, 60, 80, 100}));
//		contentPane.add(createPanel(preferredSize, colors, distances, new int[] {0, 100, 100}));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static SequencePanel createPanel(Dimension preferredSize,
			Color[] colors, double[] distances, int[] uncertainty) {
		SequencePanel panel = new SequencePanel(createGroup(uncertainty), colors, distances);
		panel.setPreferredSize(preferredSize);
		return panel;
	}

	private static ChangeSequenceGroup createGroup(int[] uncertainty) {
		ChangeSequenceGroup group = new ChangeSequenceGroup();
		for (int i = 0; i < uncertainty.length; i++) {
			group.add(createSequence(uncertainty[i]));
		}
		return group;
	}

	private static ChangeSequence createSequence(double uncertainty) {
		ChangeSequence sequence = new ChangeSequence();
		GregorianCalendar date1 = new GregorianCalendar();
		date1.add(Calendar.YEAR, 1);
		Calendar[] dates = new Calendar[] {new GregorianCalendar(), date1};
		sequence.setDates(dates);
		ObjectClass[] classes = new ObjectClass[] {new ObjectClass(0, "0", 0), new ObjectClass(1, "1", 1)};
		sequence.setClasses(classes);
		double[] uncertainties = new double[] {uncertainty};
		sequence.setUncertainties(uncertainties );
		return sequence;
	}
}
