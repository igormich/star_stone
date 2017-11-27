package org.jnity.starstone.modifiers;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.events.GameEvent;

public class Poison extends CreatureModifier{

    private int time;

	public Poison(CreatureCard target, int time) {
        super(target);
		this.time = time;
    }

    public void on(GameEvent gameEvent, Card card) {
        if(GameEvent.END_OF_TURN == gameEvent && card.equals(getTarget().getOwner()) && time >0 ) {
            getTarget().takeDamage(1);
        } else getTarget().removeModifier(this);
    }
}
