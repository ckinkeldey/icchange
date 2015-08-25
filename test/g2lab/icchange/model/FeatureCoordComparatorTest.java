package net.g2lab.icchange.model;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;

public class FeatureCoordComparatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCompareTo() {
		assertThat(new FeatureCoordComparator().compare(getFeature(0, 0),
				getFeature(0, 0)), is(0));
		assertThat(new FeatureCoordComparator().compare(getFeature(1, 0),
				getFeature(0, 0)), is(1));
		assertThat(new FeatureCoordComparator().compare(getFeature(0, 0),
				getFeature(1, 0)), is(-1));
		assertThat(new FeatureCoordComparator().compare(getFeature(0, 1),
				getFeature(0, 0)), is(1));
		assertThat(new FeatureCoordComparator().compare(getFeature(0, 0),
				getFeature(0, 1)), is(-1));
	}

	private Feature getFeature(int x, int y) {
		GeometryFactory geometryFactory = JTSFactoryFinder
				.getGeometryFactory(null);
		Coordinate coord = new Coordinate(x, y);
		Point point = geometryFactory.createPoint(coord);
		FeatureSchema featureSchema = new FeatureSchema();
		featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
		Feature feature = new BasicFeature(featureSchema);
		feature.setAttribute("GEOMETRY", point);
		return feature;
	}

}
