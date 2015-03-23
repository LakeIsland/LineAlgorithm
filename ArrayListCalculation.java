import java.util.ArrayList;

//this class is collection of several ArrayList calculations.
public class ArrayListCalculation {
	public static ArrayList<ArrayList<Double>> averageOfArrayListOfArrayListArrayListInteger(
			ArrayList<ArrayList<ArrayList<Integer>>> inputData) {

		ArrayList<ArrayList<Double>> average = new ArrayList<ArrayList<Double>>();
		final int depth_0_size = inputData.size();
		final int depth_1_size = inputData.get(0).size();
		final int depth_2_size = inputData.get(0).get(0).size();

		for (int depth_1_sequence = 0; depth_1_sequence < depth_1_size; depth_1_sequence++) {
			ArrayList<Double> temp = new ArrayList<Double>();

			for (int depth_2_sequence = 0; depth_2_sequence < depth_2_size; depth_2_sequence++) {
				double sum = 0;

				for (int depth_0_sequence = 0; depth_0_sequence < depth_0_size; depth_0_sequence++) {
					sum += inputData.get(depth_0_sequence)
							.get(depth_1_sequence).get(depth_2_sequence);
				}
				sum /= (double) depth_0_size;
				temp.add(sum);
			}
			average.add(temp);
		}
		return average;

	}

	public static ArrayList<Double> squareEachElement(
			ArrayList<Double> inputData) {
		ArrayList<Double> result = new ArrayList<Double>();
		for (Double x : inputData) {
			result.add(x * x);
		}
		return result;
	}

	public static ArrayList<Double> removeFirstXElements(
			ArrayList<Double> info, int x) {
		for (int i = 0; i < x; i++) {
			info.remove(0);
		}
		return info;
	}

	public static double averageOfArrayListOfDouble(ArrayList<Double> inputData) {
		double sum = 0;
		for (Double x : inputData)
			sum += x;
		return sum / inputData.size();
	}

	public static double stdevOfArrayListOfDouble(ArrayList<Double> inputData) {
		return Math
				.sqrt(averageOfArrayListOfDouble(squareEachElement(inputData))
						- (averageOfArrayListOfDouble(inputData))
						* (averageOfArrayListOfDouble(inputData)));
	}

	public static ArrayList<ArrayList<Double>> changeRowToColumn(
			ArrayList<ArrayList<Double>> inputData) {
		ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();

		final int depth_0_size = inputData.size();
		final int depth_1_size = inputData.get(0).size();

		for (int depth_1_sequence = 0; depth_1_sequence < depth_1_size; depth_1_sequence++) {
			ArrayList<Double> temp = new ArrayList<Double>();
			for (int depth_0_sequence = 0; depth_0_sequence < depth_0_size; depth_0_sequence++) {
				temp.add(inputData.get(depth_0_sequence).get(depth_1_sequence));
			}
			result.add(temp);
		}
		return result;
	}
}
