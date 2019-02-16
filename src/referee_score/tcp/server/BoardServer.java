package referee_score.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import referee_score.fx.Board;

public class BoardServer implements Runnable{
	
	private Board board;
	private ExecutorService threadPool;
	private int port;
	
	public BoardServer(Board board, int refereeCount) {
		this.board = board;
		this.threadPool = Executors.newFixedThreadPool(refereeCount);
		this.port = 8866;
	}
	
	@Override
	public void run() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			while (true) {
				Socket socket = server.accept();
				Map<String, Integer> refereeComeInResult = board.refereeIsComeIn();
				if (refereeComeInResult.get(board.REFEREE_COME_SUCCESS_KEY) == 1) {
					// 裁判连接成功
					int refereeNum = refereeComeInResult.get(board.REFEREE_COME_NUMBER_KEY);
					threadPool.execute(new SocketSolve(socket, refereeNum, board));
				} else {
					// 裁判连接失败
					
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (server != null) {
					server.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
