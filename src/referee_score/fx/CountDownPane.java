package referee_score.fx;

import java.text.DecimalFormat;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import referee_score.utils.ContestManager;

public class CountDownPane extends Pane{
	
	private double height;
	private double width;
	private Label decroption;
	private Label registAccountLabel;
	private Label clockLabel;
	private Timeline timeline;
	private long countDownRemainTime;
	private long countDownTime;
	private int timeSpace = 10;
	
	private final String registerAccountPrefix = "考试人注册号     "; 
	
	public CountDownPane(double width, double height) {
		this.height = height;
		this.width = width;
		setPrefHeight(height);
		setPrefWidth(width);
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		addCountDownKeyFrame();
		setComponent();
	}
	
	private void setComponent() {
		decroption = new Label("跆拳道级位考试系统");
		decroption.setStyle("-fx-font-weight: bold;");
		decroption.setFont(Font.font ("FZCuYuan", this.height / 10));
		decroption.setTextFill(Color.WHITE);
		registAccountLabel = new Label(registerAccountPrefix);
		registAccountLabel.setStyle("-fx-font-weight: bold;");
		registAccountLabel.setFont(Font.font ("FZCuYuan", this.height / 15));
		registAccountLabel.setTextFill(Color.rgb(103, 184, 41));
		VBox accountVbox = new VBox(40);
		accountVbox.setPadding(new Insets(60, 0, 0, width / 4));
		accountVbox.getChildren().addAll(decroption, registAccountLabel);
		
		VBox countDownVBox = new VBox(40);
		Label countDownLabel = new Label("倒计时");
		countDownLabel.setPrefWidth(width);
		countDownLabel.setAlignment(Pos.CENTER);
		countDownLabel.setTextFill(Color.rgb(218, 36, 26));
		countDownLabel.setFont(Font.font ("FZCuYuan", this.height / 10));
		
		clockLabel = new Label("0:00:000");
		clockLabel.setTranslateX(width / 3);
		clockLabel.setAlignment(Pos.CENTER);
		clockLabel.setTextFill(Color.rgb(103, 184, 41));
		clockLabel.setFont(Font.font ("FZCuYuan", this.height / 10));
		countDownVBox.getChildren().addAll(countDownLabel, clockLabel);
		
		VBox vbox = new VBox(80);
		vbox.getChildren().addAll(accountVbox, countDownVBox);
		getChildren().add(vbox);
	}
	
	public void setRegistAccount(String account) {
		registAccountLabel.setText(registerAccountPrefix+account);
	}
	
	private void setCountDwonLabel(long millisecond) {
		long mintues = millisecond / 1000 / 60;
		long second = millisecond / 1000 % 60;
		long million = millisecond % 1000;
		String concat = Long.toString(mintues) + ":" +
			new DecimalFormat("00").format(second) + ":" +
			new DecimalFormat("000").format(million);
		clockLabel.setText(concat);
	}
	
	public void setCountDownTime(long millisecond) {
		countDownRemainTime = millisecond;
		countDownTime = millisecond;
		Platform.runLater(()->{
			setCountDwonLabel(countDownRemainTime);
		});
	}
	
	public void setTimeSpace(int timeSpace) {
		this.timeSpace = timeSpace;
		addCountDownKeyFrame();
	}
	
	public void play() {
		timeline.play();
	}
	
	public void stop() {
		timeline.stop();
	}
	
	public void reset() {
		countDownRemainTime = countDownTime;
		setCountDwonLabel(countDownRemainTime);
	}
	
	private void addCountDownKeyFrame() {
		timeline.getKeyFrames().clear();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(timeSpace), 
				new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				countDownRemainTime -= timeSpace;
				setCountDwonLabel(Math.max(countDownRemainTime, 0));
				if (countDownRemainTime <= 0) {
					// 比赛结束
					gameOver();
				}
			}
		}));
	}
	
	private void gameOver() {
		countDownRemainTime = 0;
		timeline.stop();
		ContestManager.shareInstance().setTestState(ContestManager.NO_TESTING);
	}
}
