package net.g2lab.icchange.model;

import java.util.Comparator;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.feature.Feature;

public class FeatureCoordComparator implements Comparator<Feature> {

	@Override
	public int compare(Feature thisFeature, Feature otherFeature) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;
		if (thisFeature == otherFeature) {
			return EQUAL;
		}
		Envelope thisEnvelope = thisFeature.getGeometry().getEnvelope()
				.getEnvelopeInternal();
		Envelope otherEnvelope = otherFeature.getGeometry().getEnvelope()
				.getEnvelopeInternal();
		int thisCenterX = (int) ((thisEnvelope.getMinX() + thisEnvelope
				.getMaxX()) / 2.);
		int thisCenterY = (int) ((thisEnvelope.getMinY() + thisEnvelope
				.getMaxY()) / 2.);
		int otherCenterX = (int) ((otherEnvelope.getMinX() + otherEnvelope
				.getMaxX()) / 2.);
		int otherCenterY = (int) ((otherEnvelope.getMinY() + otherEnvelope
				.getMaxY()) / 2.);
		if (thisCenterX == otherCenterX && thisCenterY == otherCenterY) {
			return EQUAL;
		} else if (thisCenterX > otherCenterX) {
			return AFTER;
		} else if (thisCenterX < otherCenterX) {
			return BEFORE;
		} else if (thisCenterX == otherCenterX && thisCenterY < otherCenterY) {
			return BEFORE;
		} else if (thisCenterX == otherCenterX && thisCenterY > otherCenterY) {
			return AFTER;
		} else {
			return EQUAL;
		}
	}

}
