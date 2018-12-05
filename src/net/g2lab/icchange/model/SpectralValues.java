package net.g2lab.icchange.model;

import java.util.ArrayList;
import java.util.List;

public class SpectralValues {

	List<double[][]> spectralValues = new ArrayList<double[][]>();
	double[] minima;
	double[] maxima;
	double[][] uncertainties;

	public SpectralValues(int numBands) {
		this.minima = new double[numBands];
		this.maxima = new double[numBands];
	}

	public void add(double[][] bandValues) {
		this.spectralValues.add(bandValues);
		this.minima[spectralValues.indexOf(bandValues)] = getMinValue(bandValues);
		this.maxima[spectralValues.indexOf(bandValues)] = getMaxValue(bandValues);
	}

	public double[][] getValues(int band) {
		return spectralValues.get(band);
	}

	public double getMin(int band) {
		return minima[band];
	}

	public double[] getMinValues() {
		return minima;
	}

	public double getMax(int band) {
		return maxima[band];
	}

	public double[] getMaxValues() {
		return maxima;
	}

	public double[][] getUncertainties() {
		return uncertainties;
	}

	private double getMinValue(double[][] values) {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				if (values[i][j] != Double.NaN && values[i][j] < min) {
					min = values[i][j];
				}
			}
		}
		return min;
	}

	private double getMaxValue(double[][] values) {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				if (values[i][j] != Double.NaN && values[i][j] > max) {
					max = values[i][j];
				}
			}
		}
		return max;
	}

	public int getNumBands() {
		return spectralValues.size();
	}

	public void setUncertainties(double[][] uncertainties) {
		double min = getMinValue(uncertainties);
		double max = getMaxValue(uncertainties);
		this.uncertainties = scaleUncertainties(uncertainties, min, max);

	}

	private double[][] scaleUncertainties(double[][] values, double min,
			double max) {
		double[][] scaled = new double[values.length][values[0].length];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				if (!Double.isNaN(values[i][j])) {
					scaled[i][j] = (values[i][j] - min) / (max - min);
					// if (scaled[i][j] * 10000 < 1.0) {
					// scaled[i][j] *= 10000;
					// }
				} else {
					scaled[i][j] = Double.NaN;
				}
			}
		}
		return scaled;
	}

}
