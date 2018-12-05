package net.g2lab.icchange.view.profile;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TestFrameHorizontal extends JFrame {

	public TestFrameHorizontal() {
		super();
		setSize(1200, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public TestFrameHorizontal(JComponent component) {
		this();
		this.getContentPane().add(component);
		// pack();
	}

	public TestFrameHorizontal(JComponent[][] components) {
		this();
		int numDates = components.length;
		int numObjects = components[0].length;
		GridLayout gridLayout = new GridLayout(numDates + 2, numObjects + 2);
		// gridLayout.setHgap((int)
		// (components[0][0].getPreferredSize().getWidth() / 4.));
		// gridLayout.setVgap((int)
		// (components[0][0].getPreferredSize().getHeight() / 4.));
		this.getContentPane().setLayout(gridLayout);

		// for (int i = 0; i < components[0].length + 2; i++) {
		// this.getContentPane().add(new JLabel());
		// }
		this.getContentPane().add(new JLabel());
		for (int i = 0; i < numObjects; i++) {
			JLabel jLabel = new JLabel("" + i);
			jLabel.setHorizontalAlignment(JLabel.CENTER);
			jLabel.setVerticalAlignment(JLabel.BOTTOM);
			this.getContentPane().add(jLabel);
		}
		this.getContentPane().add(new JLabel());

		for (int i = 0; i < numDates; i++) {
			JLabel jLabel = new JLabel("d " + i);
			jLabel.setHorizontalAlignment(JLabel.CENTER);
			this.getContentPane().add(jLabel);
			for (int j = 0; j < numObjects; j++) {
				this.getContentPane().add(components[i][j]);
			}
			this.getContentPane().add(new JLabel());
		}
		for (int i = 0; i < numObjects + 2; i++) {
			this.getContentPane().add(new JLabel());
		}
		// pack();
	}
}
