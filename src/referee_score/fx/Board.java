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
import referee_score.utils.ContestManager;

public class Board extends Application {
	
	private final int LEFT_TOP_X;
	private final int LEFT_TOP_Y;
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	
	private int refereeCount = 1;
	private double refreeHeightSplit;
	private RefereePane[] referees;
	private int currentRefereeCount;
	private boolean[] isRefereeCome;
	
	public final String REFEREE_COME_SUCCESS_KEY = "referee_come_success";
	public final String REFEREE_COME_NUMBER_KEY = "referee_come_number";
	public final String REFEREE_COME_FAILD_MSG_KEY = "referee_come_faild_msg";
	
	// 上部组件
	private CountDownPane countDownPane; // 倒计时面板
	private AvgScorePane avgScorePane;   // 平均分面板
	
	private int sumScore;
	private int refereeReadyCount;
	private int countDownMinute = 1;
	
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
		sumScore = refereeCount * 100;
		refereeReadyCount = 0;
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
		if (ContestManager.shareInstance().getTestState() == ContestManager.TESTING) {
			// 如果正在考试的时候裁判离开了，属于意外掉线
			testOver();
		}
	}
	
	public synchronized void refereePointsDeduction(int refereeNum, int deduction) {
		referees[refereeNum].pointsDeduction(deduction);
		sumScore -= deduction;
		avgScorePane.setAvgScore(sumScore / refereeCount);
	}
	
	public synchronized void refereeReady(int refereeNum) {
		if (referees[refereeNum].ready()) {
			refereeReadyCount ++;
			// 所有裁判都准备完毕，考试开始
			if (refereeReadyCount == refereeCount) {
				testBegin();
			}
		} else {
			refereeReadyCount --;
		}
	}
	
	private void testBegin() {
		allComponentInit();
		ContestManager.shareInstance().setTestState(ContestManager.TESTING);
		countDownPane.play();
	}
	
	private void testOver() {
		countDownPane.stop();
		ContestManager.shareInstance().setTestState(ContestManager.NO_TESTING);
		// 告诉每个裁判，比赛意外结束
	}
	
	private void allComponentInit() {
		// 裁判状态初始化
		refereeReadyCount = 0;
		for (RefereePane referee:referees) {
			referee.refereeInit();
		}
		// 倒计时面板初始化
		countDownPane.setCountDownTime(countDownMinute * 60 * 1000);
		// 分数初始化
		sumScore = refereeCount * 100;
		avgScorePane.setAvgScore(sumScore / refereeCount);
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
		double topPaneHeight = SCREEN_HEIGHT - SCREEN_HEIGHT / this.refreeHeightSplit - 10;
		// 上部面板的左边：倒计时面板
		countDownPane = new CountDownPane(SCREEN_WIDTH / 2, topPaneHeight);
		countDownPane.setRegistAccount("G201968712");
		// 测试倒计时动画
		countDownPane.setCountDownTime(countDownMinute * 60 * 1000);
		// 上部面板的右边：平均分面板
		avgScorePane = new AvgScorePane(SCREEN_WIDTH / 2, topPaneHeight);
		topAvgScorePane.getChildren().addAll(countDownPane, avgScorePane);
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
