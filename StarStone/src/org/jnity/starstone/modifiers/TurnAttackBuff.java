package org.jnity.starstone.modifiers;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;

public class TurnAttackBuff extends CreatureModifier implements GameListener{

    int powerBuff;

    public TurnAttackBuff(CreatureCard target, int powerBuff) {
        super(target);
        this.powerBuff = powerBuff;
    }

    @Override
    public int modifyPower(int value, CreatureCard creatureCard){
        return value + powerBuff;
    }

	@Override
	public void on(GameEvent gameEvent, Card card, CreatureCard target) {
		if(gameEvent == GameEvent.END_OF_TURN) {
			getTarget().removeModifier(this);
			getTarget().getGame().removeListener(this);
		}
		
	}
}
