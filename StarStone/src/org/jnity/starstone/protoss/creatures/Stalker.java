package org.jnity.starstone.protoss.creatures;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.modifiers.SecondChance;

public class Stalker extends CreatureCard{

    private static final long serialVersionUID = 640757801498033184L;

    public Stalker() {
        super("STALKER", 0, 0, 3, 4);
    }

    @Override
    public void play(CreatureCard target) {
        super.play(target);
        addModifier(new SecondChance(this));
    }
}
