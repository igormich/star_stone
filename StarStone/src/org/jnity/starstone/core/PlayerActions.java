package org.jnity.starstone.core;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;

public interface PlayerActions {

	void play(Card card, CreatureCard target, int position);

	void endOfTurn();

}