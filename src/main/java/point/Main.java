package main.java.point;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.devsta.jfxtest.Utils;
import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;

public class Main {
	public static void main(String[] args) {
		// Open a database connection
		// (create a new database if it doesn't exist yet):
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/imagesdb.odb");
		EntityManager em = emf.createEntityManager();

		// image pour tester

		emptyDatabase(em);
		// Find the number of Point objects in the database:
//        Query q1 = em.createQuery("SELECT COUNT(p) FROM Point p");
//        System.out.println("Total Points: " + q1.getSingleResult());

		// Find the average X value:
//        Query q2 = em.createQuery("SELECT AVG(p.x) FROM Point p");
//        System.out.println("Average X: " + q2.getSingleResult());

		// Retrieve all the Point objects from the database:
		TypedQuery<ImageData> query = em.createQuery("SELECT p FROM ImageData p", ImageData.class);
		List<ImageData> results = query.getResultList();
		for (ImageData p : results) {
			System.out.println(p.getImagePath());
		}

		// Close the database connection:
		em.close();
		emf.close();
	}

	public static void emptyDatabase(EntityManager em) {
		em.getTransaction().begin();
		em.createQuery("DELETE FROM ImageData").executeUpdate();
		em.getTransaction().commit();

		System.out.println("La base de données d'image a été vidée.");
	}

	public static Image applyMedianFilter(Image in) {
		int width = in.getXDim();
		int height = in.getYDim();
		int bandCount = in.getBDim();
		Image newImg = new ByteImage(width, height, 1, 1, bandCount);

		for (int x = 1; x < width - 2; x++) {
			for (int y = 1; y < height - 2; y++) {
				for (int band = 0; band < bandCount; band++) {
					int v = in.getPixelXYBByte(x, y, band);
					int vX1 = in.getPixelXYBByte(x + 1, y, band);
					int vXY1 = in.getPixelXYBByte(x + 1, y + 1, band);
					int vXY_1 = in.getPixelXYBByte(x - 1, y - 1, band);
					int vX_1 = in.getPixelXYBByte(x - 1, y, band);
					int vY_1 = in.getPixelXYBByte(x, y - 1, band);
					int vX_1Y1 = in.getPixelXYBByte(x - 1, y + 1, band);
					int vX1Y_1 = in.getPixelXYBByte(x + 1, y - 1, band);
					int vY1 = in.getPixelXYBByte(x, y + 1, band);

					// Get the median value of the 8 pixels
					int[] pixels = new int[] { v, vX1, vXY1, vXY_1, vX_1, vY_1, vX_1Y1, vX1Y_1, vY1 };
					Arrays.sort(pixels);
					newImg.setPixelXYBByte(x, y, band, pixels[4]);
				}

			}
		}

		return newImg;
	}

	public static TreeMap<Double, ImageData> findSimilarImages(EntityManager em, Image requestImage, String imagePath, SearchMode mode) {

		TreeMap<Double, ImageData> result = new TreeMap<>();
//		Gson gson = new GsonBuilder().create();
		double[][] optimizedHistogram = null;
		ImageData reqData = new ImageData();

		// Transforme l'image donnée pour améliorer la recherche
		Image newRequestImage = applyMedianFilter(requestImage);
		
		reqData.setImagePath(imagePath);
		
		switch (mode) {
		case RGB:
		reqData.setRgbHistogram(newRequestImage);	
			double[][] discreteHistogram = Utils.getDiscreteHistogram(requestHistogram);
			optimizedHistogram = Utils.getNormalisedRgbHistogram(discreteHistogram,
					(newRequestImage.getXDim() * newRequestImage.getYDim()));
			break;
		case HSV:
			reqData.setHsvHistogram(newRequestImage);
			double[][] requestHistogramHsv = Utils.getHsvHistogram(newRequestImage);
			double[][] discreteHistogramHsv = Utils.getDiscreteHsvHistogram(requestHistogramHsv);
			optimizedHistogram = Utils.getNormalisedRgbHistogram(discreteHistogramHsv,
					(newRequestImage.getXDim() * newRequestImage.getYDim()));
			break;
		}

		// Step 2: Fetch images from the database & calculate their similarity
		TypedQuery<ImageData> query = em.createQuery("SELECT p FROM ImageData p", ImageData.class);
		List<ImageData> results = query.getResultList();

		switch (mode) {
		case RGB:
			for (ImageData p : results) {
				result.put(p.getEuclideanDistance(reqData.getRgbHistogram()), p);
			}
			break;
		case HSV:
			for (ImageData p : results) {
				result.put(p.getEuclideanDistance(reqData.getHsvHistogram()), p);
			}
			break;
		}

		// On ne renvoie que les 10 meilleurs resultats
		int count = 0;
		TreeMap<Double, ImageData> top10 = new TreeMap<Double, ImageData>();
		for (Entry<Double, ImageData> entry : result.entrySet()) {
			if (count >= 10)
				break;

			top10.put(entry.getKey(), entry.getValue());
			count++;
		}
		return top10;
	}

}