package referee_score.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TestServer {
	public static void main(String[] args) {
		Socket socket = null;
		DataOutputStream dataOutput = null;
		DataInputStream dataInput = null;
		boolean bConnected = false;
		Scanner in = new Scanner(System.in);
		try {
			socket = new Socket("127.0.0.1", 8866);
			dataOutput = new DataOutputStream(socket.getOutputStream());
			dataInput = new DataInputStream(socket.getInputStream());
			bConnected = true;
			
			String str = dataInput.readUTF();
			System.out.println("server sad:" + str);
			
			while (bConnected) {
				String msg = in.next();
				send(dataOutput, msg);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void send(DataOutputStream dataOutput, String str) {
		try {
			dataOutput.writeUTF(str);
			dataOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
