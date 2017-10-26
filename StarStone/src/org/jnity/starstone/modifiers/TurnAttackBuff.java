package org.jnity.starstone.modifiers;

import org.jnity.starstone.cards.CreatureCard;

public class TurnAttackBuff extends CreatureModifier{

    int powerBuff;

    public TurnAttackBuff(CreatureCard target, int powerBuff) {
        super(target);
        this.powerBuff = powerBuff;
    }

    @Override
    public int modifyPower(int value, CreatureCard creatureCard){
        getTarget().removeModifier(this);
        return value + powerBuff;
    }
}
