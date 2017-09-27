package org.jnity.starstone.events;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Player;

public interface GameListener {
	default void on(GameEvent gameEvent, Card card, CreatureCard target) {
		
	}
	default void on(GameEvent gameEvent, Card card) {
		
	}
}
