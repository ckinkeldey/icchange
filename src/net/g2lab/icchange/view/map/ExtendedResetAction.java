package net.g2lab.icchange.view.map;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.geotools.swing.MapPane;
import org.geotools.swing.action.ResetAction;

public class ExtendedResetAction extends ResetAction implements Action {

	private AnnotationLayer a8nLayer;

	public ExtendedResetAction(MapPane mapPane, AnnotationLayer a8nLayer) {
		super(mapPane);
		this.a8nLayer = a8nLayer;
	}

	/* (non-Javadoc)
	 * @see org.geotools.swing.action.ResetAction#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ev) {
		a8nLayer.reset();
		super.actionPerformed(ev);
	}
	
	

}
