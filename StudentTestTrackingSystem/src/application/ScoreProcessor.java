package application;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Scanner;

public class ScoreProcessor {

	// FIELDS
	// -----------------------------------------------------------------
	ArrayList<String> studentNames;
	private TreeMap<String, ArrayList<Integer>> scoreDetails;
	private ArrayList<Integer> highScores;
	private ArrayList<Integer> lowScores;
	private ArrayList<Integer> avgScores;
	private ArrayList<Integer> studentScoreTrend;
	private ArrayList<Integer> studentScore;

	private int numOfTestScore;
	private int numberOfTest;
	private int numberOfStudents;
	private String[] data;

	int lowestScore;
	int numberAboveAverage;
	int numberBelowAverage;
	float TREND_SLOPE;

	public ScoreProcessor() {
		scoreDetails = new TreeMap<String, ArrayList<Integer>>();
		highScores = new ArrayList<Integer>();
		lowScores = new ArrayList<Integer>();
		avgScores = new ArrayList<Integer>();
		studentScoreTrend = new ArrayList<Integer>();
		studentScore = new ArrayList<Integer>();
		numOfTestScore = 15;
		numberOfStudents = 30;
		TREND_SLOPE = 0f;
	}

	// METHODS
	// ------------------------------------------------------------------
	public TreeMap<String, ArrayList<Integer>> getScoreDetails() {
		return scoreDetails;
	}

	public ArrayList<Integer> gethighScores() {
		return highScores;
	}

	public ArrayList<Integer> getlowScores() {
		return lowScores;
	}

	public int getRoundDownLowestScore() {
		return ((lowestScore - 10) / 10) * 10;
	}

	public ArrayList<Integer> getAveragetScores() {
		return avgScores;
	}

	public ArrayList<Integer> getStudentScoreTrend() {
		return studentScoreTrend;
	}

	public int getNumOfTestScore() {
		return numOfTestScore;
	}

	public int get_numberAboveAverage() {
		return numberAboveAverage;
	}

	public int get_numberBelowAverage() {
		return numberBelowAverage;
	}

	public float getTrendSlope() {
		return TREND_SLOPE;
	}

	public ArrayList<Integer> get_TrendLine() {
		return studentScoreTrend;
	}

	public ArrayList<Integer> getScoresByName(String name) {
		compareToAverage(name);
		return scoreDetails.get(name);
	}

	public void compareToAverage(String name) {
		ArrayList<Integer> scoreByStudent = scoreDetails.get(name);
		numberAboveAverage = 0;
		numberBelowAverage = 0;
		for (int i = 0; i < scoreByStudent.size(); i++) {
			if (scoreByStudent.get(i) <= avgScores.get(i))
				numberBelowAverage++;
			else
				numberAboveAverage++;
		}
		calculateTrend(scoreByStudent);
	}

	//Calculate linear trend line
	public void calculateTrend(ArrayList<Integer> studentScore) {
		float xMean, yMean;
		xMean = yMean = 0;
		// Calculate the mean of the x -values and the mean of the y -values.
		for (int i = 0; i < studentScore.size(); i++) {
			xMean += i;
			yMean += studentScore.get(i);
		}
		xMean = xMean / studentScore.size();
		yMean = yMean / studentScore.size();

		float xMean_multiply_yMean = 0;
		float x_xMean_square = 0;
		for (int i = 0; i < studentScore.size(); i++) {
			xMean_multiply_yMean += (i - xMean) * (studentScore.get(i) - yMean);
			x_xMean_square += (i - xMean) * (i - xMean);
		}
		TREND_SLOPE = xMean_multiply_yMean / x_xMean_square;
		float yIntercept = yMean - TREND_SLOPE * xMean;
		studentScoreTrend.clear();
		for (int i = 0; i < studentScore.size(); i++) {
			studentScoreTrend.add((int) (TREND_SLOPE * i + yIntercept));
		}

	}
	//read scores into tree map
	public void processScores() {
		ArrayList<Integer> tempList;

		for (int i = 0; i < numOfTestScore; i++) {
			int min = 100;
			int max = 0;
			int avg = 0;
			for (Entry<String, ArrayList<Integer>> entry : scoreDetails.entrySet()) {
				tempList = entry.getValue();

				if (tempList.get(i) <= min) {
					min = tempList.get(i);
				}
				if (tempList.get(i) >= max) {
					max = tempList.get(i);
				}
				avg += tempList.get(i);
			}
			lowScores.add(min);
			highScores.add(max);
			avgScores.add(avg / numberOfStudents);

		}
	}
	//read data into program
	public void readScoreFile() {
		Scanner scan = new Scanner(new ScoreProcessor().getClass().getResourceAsStream("Student test scores.txt"));
		String line = "";
		lowestScore = 100; // initial value
		// loop through location file
			while (line != null) {
				try {
					line = scan.nextLine();
				} catch (Exception e) {
					break;
				}
				data = line.split("\t");
				if (data.length > 0) {
					ArrayList<Integer> tmpList = new ArrayList<Integer>();
					String studentName = data[0];
					for (int i = 1; i < data.length; i++) {
						tmpList.add(Integer.parseInt(data[i]));
						if (Integer.parseInt(data[i]) < lowestScore)
							lowestScore = Integer.parseInt(data[i]);
					}
					scoreDetails.put(studentName, tmpList);
				}
			}
			scan.close();
		}
	
}
