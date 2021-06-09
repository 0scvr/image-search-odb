import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.java.point.HsvColor;
import main.java.point.RgbColor;

class RgbColorTest {

	@Test
	void testRgbToHsvConversion() {
		HsvColor hsvColor = RgbColor.toHsv(77, 166, 255);

		assertEquals(hsvColor.getHue(), 210);
		assertEquals(hsvColor.getSaturation() * 100, 70);
		assertEquals(hsvColor.getValue() * 100, 100);

		HsvColor hsvColor2 = RgbColor.toHsv(92, 219, 13);

		assertEquals(hsvColor2.getHue(), 97);
		assertEquals(hsvColor2.getSaturation() * 100, 94);
		assertEquals(hsvColor2.getValue() * 100, 86);

	}

}
