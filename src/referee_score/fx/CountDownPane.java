package referee_score.fx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CountDownPane extends Pane{
	
	private double height;
	private double width;
	private Label decroption;
	private Label registAccountLabel;
	private Label clockLabel;
	
	private final String registerAccountPrefix = "考试人注册号     "; 
	
	public CountDownPane(double width, double height) {
		this.height = height;
		this.width = width;
		setPrefHeight(height);
		setPrefWidth(width);
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
		VBox accountVbox = new VBox(30);
		accountVbox.setPadding(new Insets(40, 0, 0, 30));
		accountVbox.getChildren().addAll(decroption, registAccountLabel);
		
		VBox countDownVBox = new VBox();
		Label countDownLabel = new Label("倒计时");
		countDownLabel.setPrefWidth(width);
		countDownLabel.setAlignment(Pos.CENTER);
		countDownLabel.setTextFill(Color.rgb(218, 36, 26));
		countDownLabel.setFont(Font.font ("FZCuYuan", this.height / 10));
		countDownVBox.getChildren().addAll(countDownLabel);
		
		VBox vbox = new VBox(80);
		vbox.getChildren().addAll(accountVbox, countDownLabel);
		getChildren().add(vbox);
	}
	
	public void setRegistAccount(String account) {
		registAccountLabel.setText(registerAccountPrefix+account);
	}
}
