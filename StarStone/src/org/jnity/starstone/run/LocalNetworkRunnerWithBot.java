package org.jnity.starstone.run;

import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.core.TextHolder;
import org.jnity.starstone.gui.GameGui;
import org.jnity.starstone.nerazim.creatures.Centurion;
import org.jnity.starstone.nerazim.creatures.DarkStalker;
import org.jnity.starstone.net.GameClient;
import org.jnity.starstone.net.Server;
import org.jnity.starstone.protoss.creatures.ShieldRecharge;
import org.jnity.starstone.protoss.creatures.ShildBattery;
import org.jnity.starstone.protoss.creatures.Zealot;

public class LocalNetworkRunnerWithBot {
	public static void main(String[] args) throws Exception {
		
		Server server = new Server();
		server.setDaemon(true);
		server.start();
		
		TextHolder.load("./text/ru.inf");
		List<Card> deck1 = new ArrayList<>();
	
		deck1.add(new ShildBattery());
		deck1.add(new ShildBattery());
		deck1.add(new Zealot());
		deck1.add(new DarkStalker());
		deck1.add(new ShieldRecharge());
		deck1.add(new Centurion());
		deck1.add(new Zealot());
	
		Thread botThread = new Thread(() -> {
			try {
				Thread.sleep(2000);
				Player p1 = new Player("Второй игрок", deck1);
				GameClient gameClient = new GameClient("Второй игрок",0, "localhost");
				new PassBot(gameClient, p1);
				gameClient.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		botThread.setDaemon(true);
		botThread.start();
		
		Player p1 = new Player("Первый игрок", deck1);
		GameClient gameClient = new GameClient("Первый игрок",0, "localhost");
		//Player p2 = new Player("Второй второй", deck1);
		
		//Game game = new Game(p1, p2);
		new GameGui(gameClient, p1);
		gameClient.start();
		//game.nextTurn();
	}
}

