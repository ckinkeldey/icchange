package net.g2lab.icchange.view;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MatrixTestFrame extends JFrame {

	public MatrixTestFrame() {
		super();
		setSize(1200, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public MatrixTestFrame(JComponent component) {
		this();
		this.getContentPane().add(component);
		// pack();
	}

	public MatrixTestFrame(JComponent[][] components) {
		this();
		int numDates = components.length;
		int numObjects = components[0].length;
		GridLayout gridLayout = new GridLayout(numObjects + 2, numDates + 2);
		// gridLayout.setHgap((int)
		// (components[0][0].getPreferredSize().getWidth() / 4.));
		// gridLayout.setVgap((int)
		// (components[0][0].getPreferredSize().getHeight() / 4.));
		this.getContentPane().setLayout(gridLayout);

		// for (int i = 0; i < components[0].length + 2; i++) {
		// this.getContentPane().add(new JLabel());
		// }
		this.getContentPane().add(new JLabel());
		for (int i = 0; i < numDates; i++) {
			JLabel jLabel = new JLabel("date " + i);
			jLabel.setHorizontalAlignment(JLabel.CENTER);
			jLabel.setVerticalAlignment(JLabel.BOTTOM);
			this.getContentPane().add(jLabel);
		}
		this.getContentPane().add(new JLabel());

		for (int i = 0; i < numObjects; i++) {
			JLabel jLabel = new JLabel("obj " + i);
			jLabel.setHorizontalAlignment(JLabel.CENTER);
			this.getContentPane().add(jLabel);
			for (int j = 0; j < numDates; j++) {
				// components[i][j].setPreferredSize(new Dimension(300, 100));
				this.getContentPane().add(components[j][i]);

			}
			this.getContentPane().add(new JLabel());
		}

		for (int i = 0; i < numDates + 2; i++) {
			this.getContentPane().add(new JLabel());
		}
		// pack();
	}
}
