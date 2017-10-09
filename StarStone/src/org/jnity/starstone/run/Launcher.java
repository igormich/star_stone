package org.jnity.starstone.run;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.cards.*;
import org.jnity.starstone.core.*;
import org.jnity.starstone.gui.GameGui;
import org.jnity.starstone.protoss.*;

public class Launcher {
	public static void main(String[] args) throws IOException {
		TextHolder.load("./text/ru.inf");
		List<Card> deck1 = new ArrayList<>();
	
		deck1.add(new Zealot());
		deck1.add(new ShieldRecharge());
		deck1.add(new Zealot());
		deck1.add(new ShieldRecharge());
		deck1.add(new ShildBattery());
		deck1.add(new ShildBattery());
		deck1.add(new Zealot());
	
		Player p1 = new Player("������ �����", deck1);
		Player p2 = new Player("������ ������", deck1);
		Game game = new Game(p1, p2);
		new GameGui(game);
		game.nextTurn();
		//p1.play(p1.getHand().get(0), null, 0);
		/*
		 * for(int i=0;i<4;i++){ p1.play(p1.getHand().get(0), null, 0);
		 * game.nextTurn(); p2.play(p2.getHand().get(0), null, 0);
		 * game.nextTurn(); } game.nextTurn(); p2.play(p2.getHand().get(0),
		 * null, 0); game.nextTurn(); p1.getCreatures().forEach(c ->
		 * c.takeDamage(1)); game.nextTurn(); game.nextTurn();
		 * p1.getCreatures().forEach(c -> c.takeDamage(1)); game.nextTurn();
		 * game.nextTurn(); p1.getCreatures().forEach(c -> c.takeDamage(1));
		 */
	}
}
