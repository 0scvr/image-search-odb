package main.java.point;

public class HsvColor {
	private float hue;
	private float saturation;
	private float value;

	public float getHue() {
		return hue;
	}

	public void setHue(float hue) {
		this.hue = hue;
	}

	public float getSaturation() {
		return saturation;
	}

	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("(%.2f, %.2f, %.2f)", this.hue, this.saturation, this.value);
	}
}
