package main.java.point;

import javax.persistence.Embeddable;

import fr.unistra.pelican.Image;

@Embeddable
public class RgbHistogram implements IHistogram {

	private double[] red;
	private double[] green;
	private double[] blue;

	RgbHistogram() {
		this.red = new double[256];
		this.green = new double[256];
		this.blue = new double[256];
	}

	public RgbHistogram(double[] red, double[] green, double[] blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public RgbHistogram(Image img) {
		int width = img.getXDim();
		int height = img.getYDim();

		// Initialize an empty histogram
		this.red = new double[256];
		this.green = new double[256];
		this.blue = new double[256];

		for (int i = 0; i < this.red.length; i++) {
			this.red[i] = 0d;
			this.green[i] = 0d;
			this.blue[i] = 0d;
		}

		// Build the RGB histogram
		for (int x = 0; x < width - 1; x++) {
			for (int y = 0; y < height - 1; y++) {
				int r = img.getPixelXYBByte(x, y, 0);
				int g = img.getPixelXYBByte(x, y, 1);
				int b = img.getPixelXYBByte(x, y, 2);
				this.red[r] += 1;
				this.green[g] += 1;
				this.blue[b] += 1;
			}
		}
	}

	public double[] getRed() {
		return red;
	}

	public void setRed(double[] red) {
		this.red = red;
	}

	public double[] getGreen() {
		return green;
	}

	public void setGreen(double[] green) {
		this.green = green;
	}

	public double[] getBlue() {
		return blue;
	}

	public void setBlue(double[] blue) {
		this.blue = blue;
	}

	@Override
	public IHistogram getDiscreteHistogram() {
		double sumR = 0;
		double[] redHist = new double[8];
		double sumG = 0;
		double[] greenHist = new double[8];
		double sumB = 0;
		double[] blueHist = new double[8];

		for (int i = 0; i < 8; i++) {
			sumR = 0;
			sumG = 0;
			sumB = 0;

			for (int j = 0; j < 32 + i * 32; j++) {
				sumR += redHist[j];
				sumG += greenHist[j];
				sumB += blueHist[j];
			}

			redHist[i] = sumR / 32;
			greenHist[i] = sumG / 32;
			blueHist[i] = sumB / 32;
		}
		return new RgbHistogram(redHist, greenHist, blueHist);
	}

	@Override
	public IHistogram getNormalisedHistogram(int pixelCount) {
		double[] nRed = new double[256];
		double[] nGreen = new double[256];
		double[] nBlue = new double[256];

		for (int i = 0; i < this.red.length; i++) {
			nRed[i] = this.red[i] / pixelCount;
			nGreen[i] = this.green[i] / pixelCount;
			nBlue[i] = this.blue[i] / pixelCount;
		}

		return new RgbHistogram(nRed, nGreen, nBlue);
	}

}
