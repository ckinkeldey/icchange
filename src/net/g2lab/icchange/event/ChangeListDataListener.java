/**
 * 
 */
package net.g2lab.icchange.event;

import java.awt.Event;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class ChangeListDataListener extends Event implements ListDataListener {

	public ChangeListDataListener(Object target, int id, Object arg) {
		super(target, id, arg);
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
	 */
	@Override
	public void contentsChanged(ListDataEvent e) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
	 */
	@Override
	public void intervalAdded(ListDataEvent e) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
	 */
	@Override
	public void intervalRemoved(ListDataEvent e) {
		// TODO Auto-generated method stub

	}




}
