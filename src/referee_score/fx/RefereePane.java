package referee_score.fx;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RefereePane extends Pane{
	
	private double width;
	private double height;

	private Label[] scores;
	private Label refereeTag;
	
	private int score;
	
	public RefereePane(double width, double height, int refereeNum) {
		setStyle("-fx-background-colod:rgb(30, 26, 22);");
		this.height = height;
		this.width = width;
		this.refereeTag = new Label(refereeNum + "#考官");
		this.scores = new Label[3];
		for (int i=0;i<3;i++) {
			scores[i] = new Label("100".charAt(i)+"");
		}
		setPrefHeight(height);
		setPrefWidth(width);
		setComponent();
		hide();
	}
	
	private void setComponent() {
		this.getChildren().add(refereeTag);
		refereeTag.setFont(Font.font ("FZCuYuan", this.height / 8));
		refereeTag.setStyle("-fx-font-weight: bold;");
		refereeTag.setTranslateX(this.width / 5 * 2);
		refereeTag.setTranslateY(this.height / 8 * 1.5);
		refereeTag.setTextFill(Color.rgb(224, 85, 26));
		score = 100;
		for (int i=0;i<3;i++) {
			scores[i].setText("100".charAt(i)+"");
			scores[i].setTextFill(Color.rgb(186, 217, 0));
			scores[i].setTranslateX(this.width / 7 * (i + 2));
			scores[i].setTranslateY(this.height - this.height / 1.5);
			scores[i].setFont(Font.font ("Verdana", this.height / 2.5));
			this.getChildren().add(scores[i]);
		}
	}
	
	public void show() {
		this.setOpacity(1);
	}
	
	public void hide() {
		this.setOpacity(0);
	}
	
	private void setScore(int score) {
		this.score = score;
		int score_1 = score / 100;
		int score_2 = score / 10 % 10;
		int score_3  =score % 10;
		scores[0].setText(score_1 + "");
		scores[1].setText(score_2 + "");
		scores[2].setText(score_3 + "");
	}
	
	public void pointsDeduction(int deduction) {
		Platform.runLater(() -> {
			setScore(score - deduction);
	    });
	}
}
