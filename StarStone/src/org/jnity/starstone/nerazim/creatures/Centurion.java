package org.jnity.starstone.nerazim.creatures;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.modifiers.Invisibility;

public class Centurion extends CreatureCard {

    private static final long serialVersionUID = 3182545748167923798L;

    public Centurion() {
        super("CENTURION", 1, 0, 1, 2);
        
    }
    @Override
    public void play(CreatureCard target) {
        super.play(target);
        addModifier(new Invisibility(this));
    }
}
