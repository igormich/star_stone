package org.jnity.starstone.run;

import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.core.TextHolder;
import org.jnity.starstone.gui.GameGui;
import org.jnity.starstone.net.GameClient;
import org.jnity.starstone.net.Server;

public class LocalNetworkRunnerWithBot {
	public static void main(String[] args) throws Exception {
		
		Server server = new Server();
		server.setDaemon(true);
		server.start();
		
		TextHolder.load("./text/ru.inf");
		List<Card> deck1 = new ArrayList<>();
		
		Thread botThread = new Thread(() -> {
			try {
				Thread.sleep(2000);
				Player p1 = new Player("123", deck1);
				GameClient gameClient = new GameClient("123",0, "localhost");
				new PassBot(gameClient, p1);
				gameClient.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		botThread.setDaemon(true);
		botThread.start();
		
		Player p1 = new Player("321", deck1);
		GameClient gameClient = new GameClient("321",0, "localhost");
		new GameGui(gameClient, p1);
		gameClient.start();
	}
}

