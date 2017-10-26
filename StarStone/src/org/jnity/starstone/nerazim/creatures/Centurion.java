package org.jnity.starstone.nerazim.creatures;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.modifiers.Invisibility;

public class Centurion extends CreatureCard {

    private static final long serialVersionUID = 3182545748167923798L;

    public Centurion() {
        super("CENTURION", 0, 0, 1, 2);
        addModifier(new Invisibility(this));
    }
}
