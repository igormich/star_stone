package org.jnity.starstone.nerazim.creatures;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.modifiers.Invisibility;

public class WarpBlade extends CreatureCard {

    private static final long serialVersionUID = -770752591782723340L;

    public WarpBlade() {
        super("WARPBLADE", 0, 0, 3, 4);
    }

    @Override
    public void play(CreatureCard target){
        super.play(target);
        if(getOwner().getCountPlayedCard() > 0)
            new Invisibility(this);
    }
}
