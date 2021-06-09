package main.java.point;

public interface IHistogram {

	IHistogram getDiscreteHistogram();
	
	IHistogram getNormalisedHistogram(int pixelCount);

}
