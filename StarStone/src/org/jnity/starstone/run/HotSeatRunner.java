package org.jnity.starstone.run;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.core.TextHolder;
import org.jnity.starstone.gui.GameGui;
import org.jnity.starstone.nerazim.creatures.Centurion;
import org.jnity.starstone.nerazim.creatures.DarkStalker;
import org.jnity.starstone.protoss.creatures.ShieldRecharge;
import org.jnity.starstone.protoss.creatures.ShildBattery;
import org.jnity.starstone.protoss.creatures.Zealot;

public class HotSeatRunner {
	public static void main(String[] args) throws IOException {
		TextHolder.load("./text/ru.inf");
		List<Card> deck1 = new ArrayList<>();
	
		deck1.add(new ShildBattery());
		deck1.add(new ShildBattery());
		deck1.add(new Zealot());
		deck1.add(new DarkStalker());
		deck1.add(new ShieldRecharge());
		deck1.add(new Centurion());
		deck1.add(new Zealot());
	
		Player p1 = new Player("Первый игрок", deck1);
		Player p2 = new Player("Второй второй", deck1);
		Game game = new Game(p1, p2);
		new GameGui(game);
		game.nextTurn();
	}
}
