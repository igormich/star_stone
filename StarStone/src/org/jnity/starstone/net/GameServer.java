package org.jnity.starstone.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.jnity.starstone.events.GameListener;

public class GameServer extends Thread implements GameListener {

	
	
	public void start() {
		ServerSocket ss;
		try {
			ss = new ServerSocket(666);
			Socket firstClient = ss.accept();//firstPlayer
			Socket secondClient = ss.accept();//secondPlayer
		} catch (IOException e) {

		}

	}

}
