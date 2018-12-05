package net.g2lab.icchange.model;

import java.util.Arrays;

public class ItemVisibility {

	private boolean[] visible;

	public ItemVisibility(int numItems) {
		this.visible = new boolean[numItems];
		Arrays.fill(visible, true);
	}

	public boolean isVisible(int index) {
		return visible[index];
	}

	public int getNumVisible() {
		return countVisible();
	}

	private int countVisible() {
		int sum = 0;
		for (boolean vis : visible) {
			if (vis) {
				sum++;
			}
		}
		return sum;
	}

	public void setVisibleExclusively(int from, int to) {
		Arrays.fill(visible, false);
		Arrays.fill(visible, from, to, true);
	}

}
