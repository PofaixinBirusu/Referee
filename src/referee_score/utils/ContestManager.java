package referee_score.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ContestManager {
	
	private final String TEST_START_MSG = "test start";
	private final String TEST_END_MSG = "test end";
	// 比赛状态
	public static final int TESTING = 1;     // 考试正在进行
	public static final int NO_TESTING = 0;  // 考试没有进行
	
	private int testState = NO_TESTING;
	
	private ArrayList<Socket> socketList = new ArrayList<>();
	
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
		if (testState == TESTING) {
			sendMsgToAllSocket(TEST_START_MSG);
		}else if (testState == NO_TESTING) {
			sendMsgToAllSocket(TEST_END_MSG);
		}
		this.testState = testState;
	}
	
	public void sendMsgToAllSocket(String msg) {
		try {
			for (Socket socket : ContestManager.shareInstance().getSocketList()) {
				DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
					dataOutput.writeUTF(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addSocket(Socket socket) {
		socketList.add(socket);
	}
	
	public void removeSocket(Socket socket) {
		socketList.remove(socket);
	}
	
	public ArrayList<Socket> getSocketList() {
		return socketList;
	}
	
}
