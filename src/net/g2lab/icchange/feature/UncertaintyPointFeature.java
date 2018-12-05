package net.g2lab.icchange.feature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class UncertaintyPointFeature {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	
	public static final String SIMPLE_FEATURE_TYPE = "id:String,*geom:Point,u:double";
	
	private String id;
	private Geometry geometry;
	private double uncertainty;
	
	public UncertaintyPointFeature(String id, Point point, double uncertainty) {
		this.geometry = point;
		this.uncertainty = uncertainty;
	}

	public SimpleFeature getAsSimpleFeature() {
		SimpleFeatureType type;
		try {
			type = DataUtilities.createType("feature", SIMPLE_FEATURE_TYPE);
			SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
			builder.add(this.id);
			builder.add(this.geometry);
			builder.add(this.uncertainty);
			return builder.buildFeature(id);
		} catch (SchemaException e) {
			LOG.error("could not create feature type: " + e, e);
			return null;
		}
	}

}
