package userinterface;

import java.util.List;

import java.util.Arrays;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import algorithm.Measurement;
import algorithm.ToolMeasure;

@SuppressWarnings("unused")
public class Coordinatesystem {

	// @SupressWarnings({"unchecked", "rawtypes"})

	/*
	 * The individual parameters from the class Diagramm (drawAchsen is called
	 * there) are transferred to the method. "Choice" defines which level is to
	 * be selected and shown.The individual x, y and z values are transferred
	 * from the list "List<Measurement>". The chart series and the axes are also
	 * handed over.
	 */

	public static void drawAchsen(String choice, List<Measurement> l, XYChart.Series s, NumberAxis xAxis,
			NumberAxis yAxis) {

		// Create variables from type double x,y and z //
		double x;
		double y;
		double z;

		/*
		 * Selection is transferred
		 * 
		 */
		switch (choice) {

		/*
		 * If "xy" is passed, the axes for the x-y-plane will be drawn and the
		 * values transferred.
		 */
		case "xy":

			xAxis.setLabel("X-Achse");
			yAxis.setLabel("Y-Achse");

			/*
			 * The loop goes through the list and gets all values. With the
			 * getPoint method, the x-value is explicitly picked from these
			 * values
			 */
			for (int i = 0; i < l.size(); i = i + 3) {
				x = l.get(i).getPoint().getX();
				// System.out.println(x);
		
				/*
				 * The loop goes through the list and gets all values. With the
				 * getPoint method, the y-value is explicitly picked from these
				 * values
				 */
				for (int j = 1; j < l.size(); j = j + 3) {

					y = l.get(i).getPoint().getY();
					System.out.println(y);
					s.getData().add(new XYChart.Data(x, y));
					 try {
							s.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}

			/*
			 * Program takes a break here and then it goes on with the next case
			 */
			break;

		case "xz":

			xAxis.setLabel("X-Achse");
			yAxis.setLabel("Z-Achse");
			for (int i = 0; i < l.size(); i = i + 3) {
				x = l.get(i).getPoint().getX();
				;
				System.out.println(x);

				/*
				 * The loop goes through the list and gets all values. With the
				 * getPoint method, the z-value is explicitly picked from these
				 * values
				 */
				for (int j = 2; j < l.size(); j = j + 3) {
					z = l.get(i).getPoint().getZ();
					;
					s.getData().add(new XYChart.Data(x, z));
				}
			}
			break;

		case "zy":

			/*
			 * Set label for the axes
			 */
			xAxis.setLabel("Z-Achse");
			yAxis.setLabel("Y-Achse");
			for (int i = 1; i < l.size(); i = i + 3) {
				y = l.get(i).getPoint().getY();
				;
				System.out.println(y);

				for (int j = 2; j < l.size(); j = j + 3) {
					z = l.get(i).getPoint().getZ();
					;
					/*
					 * The chart is drawn on the series with the axes z and y
					 */
					s.getData().add(new XYChart.Data(z, y));
				}
			}

			break;
		}
	}
}
