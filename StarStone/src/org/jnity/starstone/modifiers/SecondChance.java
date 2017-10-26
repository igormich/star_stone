package org.jnity.starstone.modifiers;

import org.jnity.starstone.cards.CreatureCard;

public class SecondChance extends CreatureModifier {

    public SecondChance(CreatureCard target) {
        super(target);
    }

    @Override
    public int modifyDamage(int value, CreatureCard creatureCard){
        if (value >= creatureCard.getCurrentHits()) {
            creatureCard.removeModifier(this);
            return 0;
        }else return value;
    }
}
