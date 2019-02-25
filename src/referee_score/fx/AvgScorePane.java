package referee_score.fx;

import java.text.DecimalFormat;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AvgScorePane extends Pane{
	
	private double height;
	private double width;
	private final String PASS_STR = "合格";
	private final String NO_PASS_STR = "不合格";
	
	private Label passLabel;
	private Label scoreLabel;
	
	public AvgScorePane(double width, double height) {
		this.height = height;
		this.width = width;
		setPrefHeight(height);
		setPrefWidth(width);
		setComponent();
	}
	
	private void setComponent() {
		VBox vbox = new VBox(40);
		double labelWidth = width - width / 4;
		vbox.setPadding(new Insets(60, width / 4, 0, 0));
		passLabel = new Label(PASS_STR);
		passLabel.setPrefWidth(labelWidth);
		passLabel.setAlignment(Pos.BOTTOM_RIGHT);
		passLabel.setTextFill(Color.rgb(218, 36, 26));
		passLabel.setFont(Font.font ("FZCuYuan", this.height / 5));
		Label lastScoreLabel = new Label("最后得分");
		VBox scoreVbox = new VBox();
		lastScoreLabel.setPrefWidth(labelWidth);
		lastScoreLabel.setAlignment(Pos.CENTER);
		lastScoreLabel.setTextFill(Color.rgb(218, 36, 26));
		lastScoreLabel.setFont(Font.font ("FZCuYuan", this.height / 15));
		scoreLabel = new Label("100");
		scoreLabel.setPrefWidth(labelWidth);
		scoreLabel.setAlignment(Pos.TOP_CENTER);
		scoreLabel.setTextFill(Color.rgb(186, 217, 0));
		scoreLabel.setFont(Font.font ("FZCuYuan", this.height / 3));
		scoreVbox.getChildren().addAll(lastScoreLabel, scoreLabel);
		vbox.getChildren().addAll(passLabel, scoreVbox);
		getChildren().add(vbox);
	}
	
	private void setScore(String scoreStr) {
		Platform.runLater(()->{
			scoreLabel.setText(scoreStr);
		});
	}
	
	public void setAvgScore(double score) {
		String scoreStr = null;
		if (Math.abs((int)score - score) <= 1e-8) {
			scoreStr = Integer.toString((int)score);
		} else {
			scoreStr = new DecimalFormat("0.00").format(score);
		}
		setScore(scoreStr);
	}
}
