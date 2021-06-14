package application;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ScoreTrackingGUI {
	//declare variables
	ComboBox<String> student;
	TextField graphName;
	TextField selectName;

	Scene scene;
	ScoreProcessor studentScore;
	AnchorPane anchorPane;
	Text analysis;

	// set dimensions
	final int WIDTH = 800;
	final int HEIGHT = 510;
	final int START_AT = 10;
	final int Y_SCALE = 15;
	final int X_SCALE = 8;

	public ScoreTrackingGUI(ScoreProcessor score) {
		studentScore = score;
	}

	// login page
	public void RunGUI(Stage stage) {
		Login login = new Login("Please login to STTS");
		login.show();
		if (!login.isPasswordmatched()) {
			System.exit(0);
		}

		// set anchorPane dimensions
		anchorPane = new AnchorPane();
		anchorPane.setMaxHeight(HEIGHT);
		anchorPane.setMinHeight(HEIGHT);

		analysis = new Text("");

		Label lblAnalysis = new Label("Performance Analysis");
		GridPane gridPane = new GridPane();

		TextField STTS = new TextField();
		TextField title = new TextField();
		ComboBox studentNames = new ComboBox();
		studentNames.setMaxWidth(170);
		studentNames.setMinWidth(170);
		studentNames.getItems().add("Select student");
		for (Entry<String, ArrayList<Integer>> entry : studentScore.getScoreDetails().entrySet()) {
			studentNames.getItems().add(entry.getKey());
		}
		// use lambda to get which student the user selected from combobox
		studentNames.setOnAction(event -> {
			if (studentNames.getSelectionModel().getSelectedIndex() != 0) {
				drawScoreLines((String) studentNames.getValue());
				lblAnalysis.setText(studentNames.getValue() + "'s Performance Analysis");
				analysis.setText(setAnalysisInfo((String) studentNames.getValue()));
				lblAnalysis.setTranslateX(260);
			} else {
				drawScoreLines(null);
				lblAnalysis.setText("Performance Analysis");
				analysis.setText("");
				lblAnalysis.setTranslateX(320);
			}
		});

		// instantiate lines on graph
		Line highline = new Line();
		highline.setFill(Color.GREEN);
		Line studentline = new Line();
		studentline.setFill(Color.BLACK);
		Line lowline = new Line();
		lowline.setFill(Color.RED);
		Line averageline = new Line();
		averageline.setFill(Color.ORANGE);

		// center the analysis
		analysis.setTranslateY(-100);
		analysis.setTranslateX(30);
		analysis.maxWidth(WIDTH - 30);
		analysis.minWidth(WIDTH - 30);
		analysis.setWrappingWidth(WIDTH - 60);
		analysis.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

		// instantiate STTSName
		Label STTSName = new Label("Student Test Tracking System");
		STTSName.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		STTSName.setTranslateX(270);

		// instantiate cbTitle
		Label cbTitle = new Label("Select student: ");
		cbTitle.setTranslateX(280);
		studentNames.setTranslateX(-420);

		// center the lblAnalysis
		lblAnalysis.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		lblAnalysis.setTranslateX(320);
		lblAnalysis.setTranslateY(10);

		// add label to gridPane rows
		gridPane.addRow(0, new Label(""));
		gridPane.addRow(1, STTSName);
		gridPane.addRow(2, new Label(""));
		gridPane.addRow(3, cbTitle, studentNames);
		gridPane.addRow(4, lblAnalysis);
		gridPane.addRow(5, new Label(""));
		gridPane.addRow(6, drawScoreLines(null));
		gridPane.addRow(7, analysis);

		// set stage and scene
		scene = new Scene(gridPane, WIDTH, HEIGHT + 100);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	// write the analysis at bottom
	private String setAnalysisInfo(String studentName) {
		String str;
		str = studentName + " took " + Integer.toString(studentScore.getNumOfTestScore()) + " tests, in which ";
		if (studentScore.get_numberAboveAverage() > 0)
			str = str + Integer.toString(studentScore.get_numberAboveAverage()) + " tests were above class average, ";
		if (studentScore.get_numberBelowAverage() > 0)
			str = str + Integer.toString(studentScore.get_numberBelowAverage()) + " tests were below class average.";
		str = str + " Overall, the student's performance";
		if (studentScore.getTrendSlope() > 0)
			str = str + " is improving.";
		else
			str = str + " needs improvement.";
		return str;
	}

	// calculate points for low, average, high, and student scores
	private AnchorPane drawScoreLines(String studentName) {
	
		// clear anchorPane each time new student is selected
		anchorPane.getChildren().clear();
		ArrayList<Integer> list;

		// Name x and y axis
		NumberAxis xAxis = new NumberAxis(0, studentScore.getNumOfTestScore() + 1, 1);
		xAxis.setLabel("Test Number");
		NumberAxis yAxis = new NumberAxis(studentScore.getRoundDownLowestScore(), 100, 10);
		yAxis.setLabel("Test score");

		// Creating the line chart
		final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		// graph all low scores
		list = studentScore.getlowScores();
		XYChart.Series lowScore = new XYChart.Series();
		lowScore.setName("Lowest score");
		for (int i = 0; i < list.size(); i++)
			addLineValue(lowScore, i + 1, list.get(i));

		// graph all average scores
		XYChart.Series avgScore = new XYChart.Series();
		avgScore.setName("Average score");
		list = studentScore.getAveragetScores();
		for (int i = 0; i < list.size(); i++)
			addLineValue(avgScore, i + 1, list.get(i));

		// graph all high scores
		list = studentScore.gethighScores();
		XYChart.Series highScore = new XYChart.Series();
		highScore.setName("Highest score");
		for (int i = 0; i < list.size(); i++)
			addLineValue(highScore, i + 1, list.get(i));
		if (studentName != null) {
			//load the scores, into an arraylist
			ArrayList<Integer> sscore = studentScore.getScoresByName(studentName);
			
			//instantiate series and graph student scores
			XYChart.Series studScore = new XYChart.Series();
			studScore.setName(studentName + "'s score");
			for (int i = 0; i < sscore.size(); i++)
				addLineValue(studScore, i + 1, sscore.get(i));

			// graph trend line
			list = studentScore.get_TrendLine();
			System.out.println(list);
			XYChart.Series scoreTrend = new XYChart.Series();
			scoreTrend.setName(studentName + "'s progress trend");
			addLineValue(scoreTrend, 1, list.get(0));
			addLineValue(scoreTrend, list.size(), list.get(list.size() - 1));
			
			//load lineChart data into all lines
			lineChart.getData().addAll(lowScore, avgScore, highScore, studScore, scoreTrend);

		} else
			//if no students selected from combobox, just add low, average, and high score lines
			lineChart.getData().addAll(lowScore, avgScore, highScore);

		//set dimensions
		lineChart.setMaxWidth(WIDTH - 10);
		lineChart.setMinWidth(WIDTH - 10);

		anchorPane.getChildren().addAll(lineChart);
		return anchorPane;
	}
	//adding xValue and yValue to graph
	private void addLineValue(XYChart.Series series, int xValue, int yValue) {
		series.getData().add(new XYChart.Data(xValue, yValue));
	}
}