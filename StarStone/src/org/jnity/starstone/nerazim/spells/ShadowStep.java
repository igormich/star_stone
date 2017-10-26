package org.jnity.starstone.nerazim.spells;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;

public class ShadowStep extends SpellCard {

    private static final long serialVersionUID = -4794699504700940681L;

    public ShadowStep() {
        super("SHADOWSTEP", 0, 0);
    }

    @Override
    public void play(CreatureCard target){
        super.play(target);
        getOwner().putInHand(target);
    }
}
