package referee_score.fx;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import referee_score.tcp.server.BoardServer;

public class Board extends Application {
	
	private final int LEFT_TOP_X;
	private final int LEFT_TOP_Y;
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	
	private int refereeCount = 2;
	private double refreeHeightSplit;
	private RefereePane[] referees;
	private int currentRefereeCount;
	private boolean[] isRefereeCome;
	
	public final String REFEREE_COME_SUCCESS_KEY = "referee_come_success";
	public final String REFEREE_COME_NUMBER_KEY = "referee_come_number";
	public final String REFEREE_COME_FAILD_MSG_KEY = "referee_come_faild_msg";
	
	// 上部组件
	private CountDownPane countDownPane; //倒计时面板
	
	public Board() {
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		this.LEFT_TOP_X = (int) bounds.getMinX();
		this.LEFT_TOP_Y = (int) bounds.getMinY();
		this.SCREEN_WIDTH = (int)bounds.getWidth();
		this.SCREEN_HEIGHT = (int)bounds.getHeight();
		// 下面n个裁判占总高度的 1/3
		this.refreeHeightSplit = 3;
		this.referees = new RefereePane[this.refereeCount];
		for (int i=0;i<this.refereeCount;i++) {
			this.referees[i] = new RefereePane(
				SCREEN_WIDTH / this.refereeCount, SCREEN_HEIGHT / this.refreeHeightSplit, i+1);
		}
		this.currentRefereeCount = 0;
		this.isRefereeCome = new boolean[this.refereeCount];
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		Scene scene = new Scene(buildPane());
		makeStageShowMax(stage);
		startServer();
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.show();
	}
	
	private void startServer() {
		Thread socketListener = new Thread(new BoardServer(this, refereeCount));
		socketListener.setDaemon(true);
		socketListener.start();
	}
	
	private int getNextHopeRefereeNum() {
		for (int i=0;i<this.refereeCount;i++) {
			if (!isRefereeCome[i]) {
				isRefereeCome[i] = true;
				return i;
			}
		}
		return -1;
	}
	
	public Map<String, Integer> refereeIsComeIn() {
		Map<String, Integer> result = new HashMap<>();
		if (currentRefereeCount < refereeCount) {
			currentRefereeCount ++;
			int refereeNum = getNextHopeRefereeNum();
			System.out.println(refereeNum);
			referees[refereeNum].show();
			result.put(REFEREE_COME_SUCCESS_KEY, 1);
			result.put(REFEREE_COME_NUMBER_KEY, refereeNum);
		} else {
			result.put(REFEREE_COME_SUCCESS_KEY, 0);
			result.put(REFEREE_COME_FAILD_MSG_KEY, 0);
		}
		return result;
	}
	
	public void refereeIsOut(int refereeNum) {
		referees[refereeNum].hide();
		isRefereeCome[refereeNum] = false;
		currentRefereeCount --;
		System.out.println(refereeNum+1 + "号裁判走了， 当前裁判人数:" + currentRefereeCount);
	}
	
	public void refereePointsDeduction(int refereeNum, int deduction) {
		referees[refereeNum].pointsDeduction(deduction);
	}
	
	private Pane buildPane() {
		// 总面板
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color:rgb(76, 73, 71);");
		// 底部n个裁判面板
		HBox bottomThreeReferee = new HBox();
		bottomThreeReferee.setStyle("-fx-background-color:rgb(30, 26, 22);");
		bottomThreeReferee.getChildren().addAll(this.referees);
		bottomThreeReferee.setTranslateY(SCREEN_HEIGHT - SCREEN_HEIGHT / this.refreeHeightSplit);
		pane.getChildren().add(bottomThreeReferee);
		// 上部面板
		HBox topAvgScorePane = new HBox();
		topAvgScorePane.setStyle("-fx-background-color:rgb(30, 26, 22);");
		pane.getChildren().add(topAvgScorePane);
		double topPaneHeight = SCREEN_HEIGHT - SCREEN_HEIGHT / this.refreeHeightSplit - 15;
		// 上部面板的左边：倒计时面板
		countDownPane = new CountDownPane(SCREEN_WIDTH / 2, topPaneHeight);
		topAvgScorePane.getChildren().add(countDownPane);
		countDownPane.setRegistAccount("G201968712");
		// 上部面板的右边：平均分面板
		
		return pane;
	}
	
	private void makeStageShowMax(Stage stage) {
		stage.setX(LEFT_TOP_X);
		stage.setY(LEFT_TOP_Y);
		stage.setWidth(SCREEN_WIDTH);
		stage.setHeight(SCREEN_HEIGHT);
	}
	
	public static void main(String []args) {
		Application.launch(args);
	}
}
