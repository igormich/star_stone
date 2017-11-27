package org.jnity.starstone.run;

import java.util.Collections;
import java.util.Random;

import org.jnity.starstone.core.Player;
import org.jnity.starstone.core.TextHolder;
import org.jnity.starstone.gui.GameGui;
import org.jnity.starstone.net.GameClient;

public class NetworkClient {
	public static void main(String[] args) throws Exception {
		
		TextHolder.load("./text/ru.inf");

		String name = "Игрок " + new Random().nextInt();
		GameClient gameClient = new GameClient(name, 0, "localhost");
		//Player p2 = new Player("Второй второй", deck1);
		
		//Game game = new Game(p1, p2);
		System.out.println(name);
		new GameGui(gameClient, new Player(name, Collections.EMPTY_LIST));
		gameClient.start();
		//game.nextTurn();
	}
}

