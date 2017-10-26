package org.jnity.starstone.modifiers;

import org.jnity.starstone.cards.CreatureCard;

public class Invisibility extends CreatureModifier{

    public Invisibility(CreatureCard target) {
        super(target);
    }

    @Override
    public int modifyDamage(int value, CreatureCard creatureCard){
        getTarget().removeModifier(this);
        return value;
    }
}
