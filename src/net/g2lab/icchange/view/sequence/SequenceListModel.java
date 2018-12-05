package net.g2lab.icchange.view.sequence;

import javax.swing.DefaultListModel;

import net.g2lab.icchange.model.ChangeSequenceGroup;

public class SequenceListModel extends DefaultListModel<ChangeSequenceGroup> {

	@Override
	public void fireContentsChanged(Object source, int index0, int index1) {
		super.fireContentsChanged(source, index0, index1);
	}

}
