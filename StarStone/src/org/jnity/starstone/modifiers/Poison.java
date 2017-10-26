package org.jnity.starstone.modifiers;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.events.GameEvent;

public class Poison extends CreatureModifier{

    public Poison(CreatureCard target) {
        super(target);
    }

    public void on(GameEvent gameEvent, Card card) {

        int count = 0;
        if(GameEvent.END_OF_TURN == gameEvent && count < 2) {
            count++;
            getTarget().takeDamage(1);
        } else getTarget().removeModifier(this);
    }
}
