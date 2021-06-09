package main.java.point;

public class RgbColor {
	private int red;
	private int green;
	private int blue;

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public RgbColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public static HsvColor toHsv(int red, int green, int blue) {
		float red1 = red / 255f;
		float green1 = green / 255f;
		float blue1 = blue / 255f;
		float maxColor = Math.max(Math.max(red1, green1), blue1);
		float minColor = Math.min(Math.min(red1, green1), blue1);
		float diff = maxColor - minColor;

		HsvColor result = new HsvColor();

		// Hue
		if (diff == 0) {
			result.setHue(0f);
		} else if (red1 == maxColor) {
			float hue = ((green1 - blue1) / diff) % 6;
			result.setHue(Math.round(hue * 60));
		} else if (green1 == maxColor) {
			float hue = ((blue1 - red1) / diff) + 2f;
			result.setHue(Math.round(hue * 60));
		} else if (blue1 == maxColor) {
			float hue = ((red1 - green1) / diff) + 4f;
			result.setHue(Math.round(hue * 60));
		} else {
			result.setHue(0f);
		}

		// Saturation
		result.setSaturation(maxColor != 0 ? Math.round((diff / maxColor) * 100f) / 100f : 0f);

		// Value
		result.setValue(Math.round(maxColor * 100f) / 100f);

		return result;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d, %d)", this.red, this.green, this.blue);
	}
}
