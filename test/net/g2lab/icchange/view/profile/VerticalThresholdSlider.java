package net.g2lab.icchange.view.profile;

import java.awt.Color;

import javax.swing.JSlider;

public class VerticalThresholdSlider extends JSlider {

	public VerticalThresholdSlider() {
		super(JSlider.VERTICAL, 0, 100, 0);
		this.setBackground(Color.WHITE);
		this.setForeground(Color.WHITE);
		this.setPaintTicks(true);
		// this.setPaintTrack(false);
		this.setPaintLabels(true);
	}

}
