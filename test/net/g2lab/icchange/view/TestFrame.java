package net.g2lab.icchange.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class TestFrame extends JFrame {

	public TestFrame() {
		super();
		setSize(1200, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public TestFrame(JComponent component) {
		this();
		this.getContentPane().add(component);
		// pack();
	}

	public TestFrame(JComponent[][] components) {
		this();
		int rows = components[0].length;
		int columns = components.length;
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				gbc.gridx = i;
				gbc.gridy = j;
				this.getContentPane().add(components[j][i], gbc);
			}
		}
	}

}
