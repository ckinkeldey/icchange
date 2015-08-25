package net.g2lab.icchange.view.profile;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JLabel;

public class VerticalThresholdLabelSlider extends VerticalThresholdSlider {

	public VerticalThresholdLabelSlider() {
		super();
		this.setLabelTable(getLableTable());
	}

	private Dictionary getLableTable() {
		Hashtable labels = new Hashtable();
		labels.put(0, new JLabel("0.0"));
		labels.put(10, new JLabel("0.1"));
		labels.put(20, new JLabel("0.2"));
		labels.put(30, new JLabel("0.3"));
		labels.put(40, new JLabel("0.4"));
		labels.put(50, new JLabel("0.5"));
		labels.put(60, new JLabel("0.6"));
		labels.put(70, new JLabel("0.7"));
		labels.put(80, new JLabel("0.8"));
		labels.put(90, new JLabel("0.9"));
		labels.put(100, new JLabel("1.0"));
		return labels;
	}

}
