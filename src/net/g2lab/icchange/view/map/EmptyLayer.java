package net.g2lab.icchange.view.map;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;

public class EmptyLayer extends Layer {

	private ReferencedEnvelope bounds;

	public EmptyLayer(ReferencedEnvelope bounds) {
		this.bounds = bounds;
	}

	@Override
	public ReferencedEnvelope getBounds() {
		return bounds;
	}

}
