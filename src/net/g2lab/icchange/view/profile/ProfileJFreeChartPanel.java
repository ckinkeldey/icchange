package net.g2lab.icchange.view.profile;

import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.DomainOrder;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;

public class ProfileJFreeChartPanel extends JPanel {

	private JFreeChart chart;

	public ProfileJFreeChartPanel() {
		String title = "";
		String categoryAxisLabel = "";
		String valueAxisLabel = "";
		// ProfileData dataset = new ProfileData();
		CategoryDataset dataset = new ProfileCategoryData();
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean legend = false;
		boolean tooltips = false;
		boolean urls = false;

		// this.chart = ChartFactory.createTimeSeriesChart(title,
		// categoryAxisLabel, valueAxisLabel, dataset, legend, tooltips, urls);
		// this.chart = ChartFactory.createAreaChart(title, categoryAxisLabel,
		// valueAxisLabel, dataset, orientation, legend, tooltips, urls);
		// this.chart = ChartFactory.createStackedAreaChart(title,
		// categoryAxisLabel, valueAxisLabel, dataset, orientation, legend,
		// tooltips, urls);
		this.chart = ChartFactory.createStackedBarChart(title, "", "", dataset,
				orientation, legend, tooltips, urls);
		chart.setAntiAlias(true);
		// CategoryItemRenderer renderer = new ProfileRender();
		// chart.getCategoryPlot().setRenderer(renderer );
		ChartPanel chartPanel = new ChartPanel(chart, true);
		this.add(chartPanel);
	}

	// @Override
	// protected void paintComponent(Graphics g) {
	// super.paintComponent(g);
	// Graphics2D g2 = (Graphics2D) g;
	// chart.draw(g2 , this.getBounds());
	// }

	class ProfileRenderer extends StackedBarRenderer {

	}

	class ProfileData implements XYDataset {

		double[] minValues = new double[] { 10, 44, 51, 16, 107, 33, 66 };
		double[] maxValues = new double[] { 210, 144, 119, 106, 255, 40, 166 };

		public int getSeriesCount() {
			return 2;
		}

		public Comparable getSeriesKey(int series) {
			return "" + series;
		}

		public int indexOf(Comparable series) {
			return 0;
		}

		public void addChangeListener(DatasetChangeListener arg0) {
		}

		public DatasetGroup getGroup() {
			return new DatasetGroup();
		}

		public void removeChangeListener(DatasetChangeListener arg0) {
		}

		public void setGroup(DatasetGroup arg0) {
		}

		public DomainOrder getDomainOrder() {
			return DomainOrder.ASCENDING;
		}

		public int getItemCount(int series) {
			if (series == 0) {
				return minValues.length;
			} else if (series == 1) {
				return maxValues.length;
			} else {
				return 0;
			}
		}

		public Number getX(int series, int item) {
			if (series <= 1) {
				return item;
			} else {
				return null;
			}
		}

		public double getXValue(int series, int item) {
			if (series <= 1) {
				return item;
			} else {
				return 0;
			}
		}

		public Number getY(int series, int item) {
			if (series == 0) {
				return minValues[item];
			} else if (series == 1) {
				return maxValues[item];
			} else {
				return null;
			}
		}

		public double getYValue(int series, int item) {
			if (series == 0) {
				return minValues[item];
			} else if (series == 1) {
				return maxValues[item];
			} else {
				return 0;
			}
		}

	}

	class ProfileCategoryData implements CategoryDataset {

		double[] minValues = new double[] { 10, 44, 51, 16, 107, 33, 66 };
		double[] maxValues = new double[] { 210, 144, 119, 106, 255, 40, 166 };

		public int getColumnIndex(Comparable arg0) {
			return 0;
		}

		public Comparable getColumnKey(int arg0) {
			return arg0;
		}

		public List getColumnKeys() {
			return Arrays.asList(new String[] { "0", "1" });
		}

		public int getRowIndex(Comparable arg0) {
			return 0;
		}

		public Comparable getRowKey(int arg0) {
			return null;
		}

		public List getRowKeys() {
			return null;
		}

		public Number getValue(Comparable arg0, Comparable arg1) {
			return null;
		}

		public int getColumnCount() {
			return minValues.length;
		}

		public int getRowCount() {
			return 2;
		}

		public Number getValue(int col, int row) {
			if (col == 0) {
				return minValues[row];
			} else {
				return maxValues[row];
			}
		}

		public void addChangeListener(DatasetChangeListener arg0) {
		}

		public DatasetGroup getGroup() {
			return new DatasetGroup();
		}

		public void removeChangeListener(DatasetChangeListener arg0) {
		}

		public void setGroup(DatasetGroup arg0) {
		}

	}

}
