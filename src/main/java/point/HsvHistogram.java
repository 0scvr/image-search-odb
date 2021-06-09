package main.java.point;

import javax.persistence.Embeddable;

import fr.unistra.pelican.Image;

@Embeddable
public class HsvHistogram implements IHistogram {

	private double[] hue;
	private double[] saturation;
	private double[] value;

//	HsvHistogram() {
//
//	}

	HsvHistogram() {
		this.hue = new double[361];
		this.saturation = new double[101];
		this.value = new double[101];
	}

	HsvHistogram(double[] hue, double[] saturation, double[] value) {
		this.hue = hue;
		this.saturation = saturation;
		this.value = value;
	}

	HsvHistogram(Image img) {
		int width = img.getXDim();
		int height = img.getYDim();

		this.hue = new double[361];
		this.saturation = new double[101];
		this.value = new double[101];

		for (int i = 0; i < this.saturation.length; i++) {
			this.saturation[i] = 0d;
			this.value[i] = 0d;
		}
		for (int i = 0; i < this.hue.length; i++) {
			this.hue[i] = 0d;
		}

		// Build the HSV histogram
		for (int x = 0; x < width - 1; x++) {
			for (int y = 0; y < height - 1; y++) {

				int r = img.getPixelXYBByte(x, y, 0);
				int g = img.getPixelXYBByte(x, y, 1);
				int b = img.getPixelXYBByte(x, y, 2);
				HsvColor hsv = RgbColor.toHsv(r, g, b);

				this.hue[(int) hsv.getHue()] += 1;
				this.saturation[(int) hsv.getSaturation() * 100] += 1;
				this.value[(int) hsv.getValue() * 100] += 1;
			}
		}
	}

	public double[] getHue() {
		return hue;
	}

	public void setHue(double[] hue) {
		this.hue = hue;
	}

	public double[] getSaturation() {
		return saturation;
	}

	public void setSaturation(double[] saturation) {
		this.saturation = saturation;
	}

	public double[] getValue() {
		return value;
	}

	public void setValue(double[] value) {
		this.value = value;
	}

	@Override
	public IHistogram getDiscreteHistogram() {
		double sumH = 0;
		double[] hueHist = new double[36];
		double sumS = 0;
		double[] saturationHist = new double[10];
		double sumV = 0;
		double[] valueHist = new double[10];

		final int nbBarres = 10;

		for (int i = 0; i < nbBarres; i++) {
			sumH = 0;

			for (int j = 0; j < i * 36 + 36; j++) {
				sumH += this.hue[i];
			}
			hueHist[i] = sumH / 36;
		}
		for (int i = 0; i < nbBarres; i++) {
			sumS = sumV = 0;
			for (int j = 0; j < i * 10 + 10; j++) {
				sumS += this.saturation[i];
				sumV += this.value[i];
			}
			saturationHist[i] = sumS / 10;
			valueHist[i] = sumV / 10;
		}

		return new HsvHistogram(hue, saturation, value);
	}

	@Override
	public IHistogram getNormalisedHistogram(int pixelCount) {
		double[] nHue = new double[361];
		double[] nSaturation = new double[101];
		double[] nValue = new double[101];

		for (int i = 0; i < this.hue.length; i++) {
			nHue[i] = this.hue[i] / pixelCount;
		}
		for (int i = 0; i < this.saturation.length; i++) {
			nSaturation[i] = this.saturation[i] / pixelCount;
			nValue[i] = this.value[i] / pixelCount;
		}

		return new HsvHistogram(nHue, nSaturation, nValue);
	}

}
