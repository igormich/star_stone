package org.jnity.starstone.modifiers;

import org.jnity.starstone.cards.CreatureCard;

public class Buff extends CreatureModifier {

    private int powerBuff;
    private int hitsBuff;

    public Buff(CreatureCard target, int powerBuff, int hitsBuff) {
        super(target);
        this.powerBuff = powerBuff;
        this.hitsBuff = hitsBuff;
    }

    @Override
    public int modifyPower(int value, CreatureCard creatureCard){
        return value + powerBuff;
    }

    @Override
    public int modifyMaxHits(int value, CreatureCard creatureCard){
        return value + hitsBuff;
    }
}
