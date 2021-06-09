package main.java.point;

import javax.persistence.Entity;
import fr.unistra.pelican.Image;

@Entity
public class ImageData {

	private String imagePath;
	private HsvHistogram hsvHistogram;
	private RgbHistogram rgbHistogram;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public HsvHistogram getHsvHistogram() {
		return hsvHistogram;
	}

	public void setHsvHistogram(HsvHistogram hsvHistogram) {
		this.hsvHistogram = hsvHistogram;
	}

	public RgbHistogram getRgbHistogram() {
		return rgbHistogram;
	}

	public void setRgbHistogram(RgbHistogram rgbHistogram) {
		this.rgbHistogram = rgbHistogram;
	}

	public double getEuclideanDistance(IHistogram histo) {

		if (histo instanceof RgbHistogram) {
			RgbHistogram rgbHisto = (RgbHistogram) histo;
			double r = 0;
			double g = 0;
			double b = 0;
			for (int i = 0; i < rgbHisto.getRed().length; i++) {
				r += Math.pow((this.rgbHistogram.getRed()[i] - rgbHisto.getRed()[i]), 2);
				g += Math.pow((this.rgbHistogram.getBlue()[i] - rgbHisto.getBlue()[i]), 2);
				b += Math.pow((this.rgbHistogram.getGreen()[i] - rgbHisto.getGreen()[i]), 2);
			}

			return Math.sqrt(r) + Math.sqrt(g) + Math.sqrt(b);
		} else {
			HsvHistogram hsvHisto = (HsvHistogram) histo;
			double h = 0;
			double s = 0;
			double v = 0;
			for (int i = 0; i < hsvHisto.getHue().length; i++) {
				h += Math.pow((this.hsvHistogram.getHue()[i] - hsvHisto.getHue()[i]), 2);
			}
			for (int i = 0; i < hsvHisto.getSaturation().length; i++) {
				s += Math.pow((this.hsvHistogram.getSaturation()[i] - hsvHisto.getSaturation()[i]), 2);
				v += Math.pow((this.hsvHistogram.getValue()[i] - hsvHisto.getValue()[i]), 2);
			}

			return Math.sqrt(h) + Math.sqrt(s) + Math.sqrt(v);
		}
	}

	public ImageData(Image img, String filePath) {
		this.imagePath = filePath;
		this.hsvHistogram = new HsvHistogram(img);
		this.rgbHistogram = new RgbHistogram(img);
	}

	ImageData() {
	}

}
