package org.jnity.starstone.run;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.core.TextHolder;
import org.jnity.starstone.gui.GameGui;
import org.jnity.starstone.terran.creatures.FireBat;
import org.jnity.starstone.terran.creatures.Marine;
import org.jnity.starstone.zerg.creatures.Zergling;

public class BotRunner {
	public static void main(String[] args) throws IOException {
		TextHolder.load("./text/ru.inf");
		List<Card> deck1 = new ArrayList<>();
		
		deck1.add(new FireBat());
		deck1.add(new Marine());
		deck1.add(new FireBat());
		deck1.add(new Marine());
		
		
		List<Card> deck2 = Arrays.asList(new Zergling(), new Zergling(), new Zergling());
		
		
		Player p1 = new Player("Аванпост Мар-Сара", deck1);
		Player p2 = new Player("Колония зергов", deck2);
		Game game = new Game(p1, p2);
		new PassBot(game, p2);
		new GameGui(game, p1);
		game.nextTurn();
	}
}

