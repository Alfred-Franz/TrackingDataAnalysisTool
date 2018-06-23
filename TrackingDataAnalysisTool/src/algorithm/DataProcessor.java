package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Point3D;

//import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import com.jme3.math.Quaternion;

/** This class provides the methods for processing the measurements. */
public class DataProcessor {

	/** This method computes the distance of two average points. */
	public double getAccuracy(double expectedDistance, AverageMeasurement firstAverangeMeasurement,
			AverageMeasurement secondAverangeMeasurement)
	
	/** Method getDistance from class Point 3D. */
	{
		return getDistance(firstAverangeMeasurement.getPoint(), secondAverangeMeasurement.getPoint())
				- expectedDistance;
	}

	/** */
	public double getAccuracyRotation(double expectedAngle, Measurement firstMeasurement,
			Measurement secondMeasurement) {
		return (firstMeasurement.getRotation().getAngle() - secondMeasurement.getRotation().getAngle()) - expectedAngle;
	}

	
	/** This method calculates the values for a boxplot. */
	public BoxPlot getBoxPlot(List<Double> values) {

		BoxPlot boxPlot = new BoxPlot();
		
		/** The values are sorted by using the method sort of the class Collections. */
		Collections.sort(values);
		
		/** Create an array. */
		Percentile percentile = new Percentile();
		percentile.setData(toDoubleArray(values));

		/** Calculation oft the values. */
		boxPlot.setMax(Collections.max(values));
		boxPlot.setMin(Collections.min(values));
		boxPlot.setQ1(percentile.evaluate(25));
		boxPlot.setMedian(percentile.evaluate(50));
		boxPlot.setQ3(percentile.evaluate(75));

		return boxPlot;
	}

	/** wandelt Liste in Array um */
	double[] toDoubleArray(List<Double> list) {
		double[] ret = new double[list.size()];
		int i = 0;
		for (Double e : list)
			ret[i++] = e.intValue();
		return ret;
	}

	// public Measurement getCalibration(Point referencePoint, Measurement
	// mainToolMeasurement) {
	//
	// for (int f = 0; f < mainToolMeasurement.getPoints().size(); f++) {
	// Point mainPoint = mainToolMeasurement.getPoints().get(f);
	// double distance = getDistance(referencePoint, mainPoint);
	// mainPoint.setDistanceToPike(distance);
	// }
	// return mainToolMeasurement;
	// }

	
	
	/** This method computes the mean of the passed values and the average rotation. */
	public AverageMeasurement getAverageMeasurement(List<Measurement> measurements) {

		int measureSize = measurements.size();
		Point3D addPoint = new Point3D(0, 0, 0);
		Point3D addRotationPoint = new Point3D(0, 0, 0);
		double scalar = 0;

		for (int i = 0; i < measureSize; i++) {
			Measurement measurement = measurements.get(i);
			Point3D point = measurement.getPoint();
			addPoint = addPoint.add(point);
			
			Rotation rotation = measurement.getRotation();
			
//			Quaternion quat = new Quaternion();
//			quat.add(q)
//			quat.slerp(quat, quat, 0.5f);
			
			Point3D rotationPoint = new Point3D(rotation.getQ1(), rotation.getQ2(), rotation.getQ3());
			addRotationPoint = addRotationPoint.add(rotationPoint);
			scalar += rotation.getQ0();
		}

		double averageX = addPoint.getX() / measureSize;
		double averageY = addPoint.getY() / measureSize;
		double averageZ = addPoint.getZ() / measureSize;

		double avgRotationS = scalar / measureSize;
		double avgRotationX = addRotationPoint.getX() / measureSize;
		double avgRotationY = addRotationPoint.getY() / measureSize;
		double avgRotationZ = addRotationPoint.getZ() / measureSize;

		AverageMeasurement averageMeasurement = new AverageMeasurement();
		Point3D averagePoint = new Point3D(averageX, averageY, averageZ);
		Rotation averageRotation = new Rotation(avgRotationS, avgRotationX, avgRotationY, avgRotationZ, true);

		averageMeasurement.setPoint(averagePoint);
		averageMeasurement.setRotation(averageRotation);
		return averageMeasurement;
	}

	/** Fehler werden ausgerechnet und in Liste gespeichert */
	public List<Double> getErrors(List<Measurement> measurements, Point3D avgPoint) {
		List<Double> errors = new ArrayList<>();

		for (int i = 0; i < measurements.size(); i++) {
			Point3D point = measurements.get(i).getPoint();
			double distance = getDistance(point, avgPoint);
			errors.add(distance);
		}
		return errors;
	}

	/** berechnet aus Fehlern RMSE */
	public double getJitter(List<Double> errors) {
		return getRMSE(errors);
	}

	/** berechnet Jitter von Rotation */
	public RotationError getRotationJitter(List<Measurement> measurements, Rotation avgRotation) {
		
		/** Create two array lists */
		List<Double> rotationPositionErrors = new ArrayList<>();
		List<Double> rotationAngleErrors = new ArrayList<>();

		RotationError rotationError = new RotationError();

		for (int i = 0; i < measurements.size(); i++) {
			Rotation rotationPosition = measurements.get(i).getRotation();
			double angle = measurements.get(i).getRotation().getAngle();

			/** Calculation of the difference between the angle of the rotation and the angle of the average rotation. */
			double angleRotation = angle - avgRotation.getAngle();
			
			/** Calculation of the distance between the rotation and the average rotation. */
			double distanceRotation = Rotation.distance(rotationPosition, avgRotation);
			
			rotationPositionErrors.add(distanceRotation);
			rotationAngleErrors.add(angleRotation);
		}
		
		/** Calculation of the jitter. */
		rotationError.setRotationPositionError(getRMSE(rotationPositionErrors));
		rotationError.setRotationAngleError(getRMSE(rotationAngleErrors));

		return rotationError;
	}

	 /** This method computes the root mean square error */
	private double getRMSE(List<Double> errors) {
		double additionalPowError = 0;

		for (double error : errors) {
			additionalPowError += Math.pow(error, 2);
		}
		return Math.sqrt(additionalPowError / errors.size());
	}

	/** This method calculates the distance of two points. */
	private double getDistance(Point3D firstPoint, Point3D secondPoint) {
		return firstPoint.distance(secondPoint);
	}
}