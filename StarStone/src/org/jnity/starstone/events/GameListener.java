package org.jnity.starstone.events;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;

public interface GameListener {
	default void on(GameEvent gameEvent, Card card, CreatureCard target) {
		
	}
	default void on(GameEvent gameEvent, Card card) {
		
	}
}
