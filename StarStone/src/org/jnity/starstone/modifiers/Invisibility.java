package org.jnity.starstone.modifiers;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;

public class Invisibility extends CreatureModifier implements GameListener {

    public Invisibility(CreatureCard target) {
        super(target);
        target.getGame().addListener(this);
    }

	@Override
	public void on(GameEvent gameEvent, Card card, CreatureCard target) {
		if((gameEvent==GameEvent.ATACKS) && card.equals(getTarget())){
			getTarget().getGame().removeListener(this);
			getTarget().removeModifier(this);
		}
	}
	@Override
	public boolean canBeDuplicated() {
		return false;
	}
}
