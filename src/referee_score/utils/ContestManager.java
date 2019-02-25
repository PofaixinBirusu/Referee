package referee_score.utils;

public class ContestManager {
	// 比赛状态
	public static final int TESTING = 1;     // 考试正在进行
	public static final int NO_TESTING = 0;  // 考试没有进行
	
	private int testState = NO_TESTING;
	// 这个类只有用到时才会加载仅1次，这是jdk为我们保证的
	static class ContestManagerHolder {
		public static final ContestManager managerInstance = new ContestManager();
	}
	
	public static ContestManager shareInstance() {
		return ContestManagerHolder.managerInstance;
	}
	
	public synchronized int getTestState() {
		return testState;
	}
	
	public synchronized void setTestState(int testState) {
		if (testState != TESTING && testState != NO_TESTING) {
			// 这里抛出个异常，简单起见先只输出一下
			System.out.println("比赛状态设置得不对");
		}
		this.testState = testState;
	}
	
}
