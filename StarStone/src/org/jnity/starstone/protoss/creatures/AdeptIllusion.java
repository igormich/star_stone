package org.jnity.starstone.protoss.creatures;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.modifiers.Illusion;

public class AdeptIllusion extends CreatureCard{

    private static final long serialVersionUID = 208046744895689561L;

    public AdeptIllusion(CreatureCard target) {
        super("ADEPTILLUSION", 0, 0, target.getCurrentHits(), target.getPower());

       new Illusion(this);
    }

}
