package referee_score.tcp.client;

import java.text.DecimalFormat;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class DeductionButton extends Pane{
	
	private double width;
	private double height;
	private double score;
	private client client;
	
	public DeductionButton(double score, double width, double height ,int flag){
		this.score = score;
		this.width = width;
		this.height = height;
		setPrefWidth(width);
		setPrefHeight(height);
//		setStyle("-fx-background-color:gray");
		setComponent(flag);
	}
	
	public void setClient(client client) {
		this.client = client;
	}
	
	private void setComponent(int flag){
		double space = 10;
		Image image1 = new Image("referee_score\\tcp\\client\\1.png");
		Image image2 = new Image("referee_score\\tcp\\client\\2.png");
        ImageView view = new ImageView(image1);
        view.setFitWidth(height*3/5);
        view.setFitHeight(height*3/5);
		view.setOnMousePressed(e->{
			view.setImage(image2);
		});
		view.setOnMouseReleased(e->{
			view.setImage(image1);
			client.deductionScore(-score);
		});
		Pane pane = new Pane();
		pane.setStyle("-fx-border-color: #E16821;-fx-border-width:0px 0px 5px 5px");
		pane.setTranslateX(view.getFitWidth()/2);
		pane.setPrefWidth(width - view.getFitWidth()/2);
		pane.setPrefHeight(height - view.getFitHeight() - space);
		String scoreStr = new DecimalFormat("0.0").format(score);
		if (Math.abs(score - (int)score) < 1e-8) {
			scoreStr = Integer.toString((int)score);
		}
		
		Label label = new Label(scoreStr + "·Ö");
		label.setTextFill(Color.WHITE);
		label.setFont(Font.font (null, FontWeight.BOLD, this.height / 5));
		label.setPrefWidth(pane.getPrefWidth());
		label.setPrefHeight(pane.getPrefHeight());
		label.setAlignment(Pos.CENTER_RIGHT);
		pane.getChildren().add(label);
		
		if(flag == 1){
			view.setTranslateX(width - view.getFitWidth()/2);
			pane.setStyle("-fx-border-color: #E16821;-fx-border-width:0px 5px 5px 0px");
			pane.setTranslateX(view.getFitWidth()/2);
			label.setAlignment(Pos.CENTER_LEFT);
		}
		
		VBox box = new VBox(space);
		box.getChildren().addAll(view, pane);
		this.getChildren().add(box);
	}
}
