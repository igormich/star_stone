package org.jnity.starstone.protoss.creatures;

import org.jnity.starstone.cards.CreatureCard;

public class Hallucination extends CreatureCard{

    private static final long serialVersionUID = 6086833658178813543L;

    public Hallucination(CreatureCard target) {
        super("HULLUCINATION", 1, 0, target.getCurrentHits(),0);
    }

}
