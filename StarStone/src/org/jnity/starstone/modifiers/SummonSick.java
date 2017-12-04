package org.jnity.starstone.modifiers;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;

public class SummonSick extends CreatureModifier implements GameListener {

	public SummonSick(CreatureCard target) {
		super(target);
		target.getGame().addListener(this);
	}
	public void on(GameEvent gameEvent, Card card, CreatureCard unused) {
		if (gameEvent == GameEvent.END_OF_TURN) {
			getTarget().removeModifier(this);
			getTarget().getGame().removeListener(this);
		}
	}
	@Override
	public boolean modifyCanAtack(boolean value, CreatureCard creatureCard) {
		return false;
	}
	@Override
	public boolean canBeDuplicated() {
		return false;
	}
}
