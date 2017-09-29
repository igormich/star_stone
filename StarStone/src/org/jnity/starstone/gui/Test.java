package org.jnity.starstone.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.core.TextHolder;
import org.jnity.starstone.protoss.ShildBattery;
import org.jnity.starstone.protoss.Zealot;

public class Test {

	public static void main(String[] args) throws IOException {
		TextHolder.load("./text/ru.inf");
		List<Card> deck1 = new ArrayList<>();
		deck1.add(new Zealot());
		deck1.add(new ShildBattery());
		Player p1 = new Player("Первый игрок", deck1);
		Player p2 = new Player("Второй второй", deck1);
		Game game = new Game(p1, p2);
		game.nextTurn();
		p1.play(p1.getHand().get(0), null, 0);
		p1.getCreatures().forEach(c -> c.takeDamage(1));
		game.nextTurn();
		game.nextTurn();
		p1.play(p1.getHand().get(0), null, 0);
		p1.getCreatures().forEach(c -> c.takeDamage(1));
		game.nextTurn();
		game.nextTurn();
		p1.getCreatures().forEach(c -> c.takeDamage(1));
		game.nextTurn();
		game.nextTurn();
		p1.getCreatures().forEach(c -> c.takeDamage(1));
		game.nextTurn();
		game.nextTurn();
		p1.getCreatures().forEach(c -> c.takeDamage(1));
	}

}
