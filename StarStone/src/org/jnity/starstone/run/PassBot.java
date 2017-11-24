package org.jnity.starstone.run;

import java.util.Optional;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;

public class PassBot implements GameListener {

	private Player player;
	private Game game;
	public PassBot(Game game, Player player) {
		this.game = game;
		this.player = player;
		game.addListener(this);
	}
	public void on(GameEvent gameEvent, Card card, CreatureCard unused) {
		if((gameEvent == GameEvent.NEW_TURN) && (card.equals(player))){
			player = game.getPlayerByID(player.getID());
			Optional<Card> play = player.getHand().stream().filter(c -> c.canBePlayed() && !c.needTarget()).findAny();
			if(play.isPresent()) {
				game.play(play.get(), null, 0);
			}
			Optional<CreatureCard> atacker = player.getCreatures().stream().filter(c -> c.canAtack()).findAny();
			if(atacker.isPresent()) {
				CreatureCard a = atacker.get();
				Optional<CreatureCard> target = game.getOpponent(player).getCreatures().stream().filter(c -> a.canAtack(c)).findFirst();
				if(target.isPresent()) {
					game.battle(a, target.get());
				}
			}
			game.nextTurn();
		}
		
	}
}
