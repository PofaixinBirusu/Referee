package referee_score.tcp.client;

import com.sun.prism.paint.Color;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class client extends Application{
	
	private final int LEFT_TOP_X;
	private final int LEFT_TOP_Y;
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	
	public client(){
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		this.LEFT_TOP_X = (int) bounds.getMinX();
		this.LEFT_TOP_Y = (int) bounds.getMinY();
		this.SCREEN_WIDTH = (int)bounds.getWidth();
		this.SCREEN_HEIGHT = (int)bounds.getHeight();
	}

	@Override
	public void start(Stage args) throws Exception {
		// TODO Auto-generated method stub
		makeStageShowMax(args);
		
		VBox box1 = new VBox();
		Label title = new Label("跆拳道级位考试系统");
		title.setPadding(new Insets(30,0,30,40));
		title.setTextFill(javafx.scene.paint.Color.WHITE);
		title.setFont(Font.font(null, FontWeight.BOLD, 30));
		Line line1 = new Line(0,150,500,150);
		line1.setStroke(javafx.scene.paint.Color.GREEN);
		line1.setStrokeWidth(10);
		box1.getChildren().addAll(title,line1);
		
		GridPane pane1 = new GridPane();
		Text text1 = makeText("考试人注册号", 30, 1);
		Text text2 = makeText("G201976821", 30, 1);
		Text text3 = makeText("倒计时：", 30, 1);
		Text text4 = makeText("5:00:00", 40, 1);
		pane1.add(text1, 0, 0);
		pane1.add(text2, 1, 0);
		pane1.add(text3, 0, 1);
		pane1.add(text4, 1, 1);
		pane1.setPadding(new Insets(30));
		pane1.setHgap(20);
		pane1.setVgap(30);
		
		BorderPane pane2 = new BorderPane();
		pane2.setLeft(box1);
		pane2.setRight(pane1);
		
		VBox box2 = new VBox();
		box2.setAlignment(Pos.TOP_CENTER);
		Text text5 = makeText("100", 200, 1);
		Text text6 = makeText("最后得分", 30, 0);
		text6.setTranslateY(-30);
		box2.setTranslateY(-80);
		box2.getChildren().addAll(text5, text6);
		
//		Image image = new Image("referee_score\\tcp\\client\\1.jpg");
//		ImageView view = new ImageView(image);
		DeductionButton button1 = new DeductionButton(-5, 180, 120, 0);
		DeductionButton button2 = new DeductionButton(-3, 180, 120, 0);
		DeductionButton button3 = new DeductionButton(-1, 180, 120, 1);
		DeductionButton button4 = new DeductionButton(-0.5, 180, 120, 1);
		HBox box4 = new HBox(SCREEN_WIDTH/2);
		box4.getChildren().addAll(button1, button3);
		HBox box5 = new HBox(SCREEN_WIDTH/4 - 36);
		
		
		Image image1 = new Image("referee_score\\tcp\\client\\1.png");
		Image image2 = new Image("referee_score\\tcp\\client\\2.png");
        ImageView view = new ImageView(image1);
        view.setFitWidth(72);
        view.setFitHeight(72);
        view.setTranslateY(-45);
		view.setOnMousePressed(e->{
			view.setImage(image2);
		});
		view.setOnMouseReleased(e->{
			view.setImage(image1);
		});
		
		
		box5.getChildren().addAll(button2, view, button4);
		VBox box6 = new VBox(90);
		box6.getChildren().addAll(box4, box5);
		box6.setTranslateX(SCREEN_WIDTH/9);
		box6.setTranslateY(-100);
		
		VBox box3 = new VBox();
		box3.setAlignment(Pos.TOP_CENTER);
		box3.getChildren().addAll(pane2, box2, box6);
		box3.setStyle("-fx-background-color: black");
		Scene scene = new Scene(box3, SCREEN_WIDTH, SCREEN_HEIGHT);
		args.initStyle(StageStyle.UNDECORATED); 
		args.setScene(scene);
		args.show();
	}
	
	public Text makeText(String str, int size, int flag){
		Text text = new Text(str);
		if(flag == 1)
			text.setFill(javafx.scene.paint.Color.GREEN);
		else
			text.setFill(javafx.scene.paint.Color.WHITE);
		text.setFont(Font.font(null, FontWeight.BOLD, size));
		return text;
	}
	
	private void makeStageShowMax(Stage stage) {
		stage.setX(LEFT_TOP_X);
		stage.setY(LEFT_TOP_Y);
		stage.setWidth(SCREEN_WIDTH);
		stage.setHeight(SCREEN_HEIGHT);
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
