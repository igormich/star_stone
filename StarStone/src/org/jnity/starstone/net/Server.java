package org.jnity.starstone.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	@Override
	public void run() {
		ServerSocket ss;
		try {
			ss = new ServerSocket(666);
			while (true) {
				try {

					Socket firstClient = ss.accept();// firstPlayer
					ObjectOutputStream oos1 = new ObjectOutputStream(
							new BufferedOutputStream(firstClient.getOutputStream()));
					oos1.flush();
					ObjectInputStream ois1 = new ObjectInputStream(
							new BufferedInputStream(firstClient.getInputStream()));
					Socket secondClient = null;
					while (secondClient == null) {
						secondClient = ss.accept();// secondPlayer
					}
					ObjectOutputStream oos2 = new ObjectOutputStream(
							new BufferedOutputStream(secondClient.getOutputStream()));
					oos2.flush();
					ObjectInputStream ois2 = new ObjectInputStream(
							new BufferedInputStream(secondClient.getInputStream()));
					GameServer newGame = new GameServer(oos1, ois1, oos2, ois2);
					newGame.setDaemon(true);
					newGame.start();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {		
		Server server = new Server();
		server.run();
	}
		

}
