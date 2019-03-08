package referee_score.tcp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import referee_score.fx.Board;
import referee_score.utils.ContestManager;

public class SocketSolve implements Runnable{
	
	private Socket socket;
	private DataInputStream dataInput = null;
	private DataOutputStream dataOutput = null;
	private boolean bConnected = false;
	private int refereeNum;
	private Board board;
	
	private final String DEDUCTION_ORDER = "deduction";
	private final String REFEREE_READY_ORDER = "ready";
	private final String DEDUCTION_SPLIT = ":";
	
	public SocketSolve(Socket socket, int refereeNum, Board board) {
		this.socket = socket;
		this.refereeNum = refereeNum;
		this.board = board;
		try {
			dataInput = new DataInputStream(socket.getInputStream());
			dataOutput = new DataOutputStream(socket.getOutputStream());
			bConnected = true;
			
			send(Integer.toString(refereeNum));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void send(String str) {
		try {
			dataOutput.writeUTF(str);
		} catch (IOException e) {
			e.printStackTrace();
			board.refereeIsOut(refereeNum);
		}
	}
	
	private void close() {
		try {
			if (dataInput != null) {
				dataInput.close();
			}
			if (dataOutput != null) {
				dataOutput.close();
			}
			if (socket != null) {
				socket.close();
			}
			board.refereeIsOut(refereeNum);
			ContestManager.shareInstance().removeSocket(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void dealInstruction(String instruction) {
		if (instruction.startsWith(DEDUCTION_ORDER)) {
			if (ContestManager.shareInstance().getTestState() == ContestManager.TESTING) {
				// 扣分指令
				int deduction = Integer.parseInt(instruction.split(DEDUCTION_SPLIT)[1]);
				board.refereePointsDeduction(refereeNum, deduction);
			}
		} else if (instruction.startsWith(REFEREE_READY_ORDER)) {
			// 裁判“准备”指令
			board.refereeReady(refereeNum);
		}
	}
	
	@Override
	public void run() {
		try {
			while (bConnected) {
				String instruction = dataInput.readUTF();
				dealInstruction(instruction);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
}
