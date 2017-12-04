package org.jnity.starstone.nerazim.spells;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;
import org.jnity.starstone.modifiers.Invisibility;

public class ShadowScreen extends SpellCard {

    private static final long serialVersionUID = 1188850524421203942L;

    public ShadowScreen() {
        super("SHADOWSCREEN", 2, 0);
    }

    @Override
    public void play(CreatureCard target){
        super.play(target);
        getOwner().getCreatures().forEach(creatureCard -> new Invisibility(creatureCard));
    }
}
