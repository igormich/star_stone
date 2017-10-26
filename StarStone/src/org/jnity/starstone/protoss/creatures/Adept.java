package org.jnity.starstone.protoss.creatures;

import org.jnity.starstone.cards.CreatureCard;

public class Adept extends CreatureCard {

    private static final long serialVersionUID = 1089893654929980515L;

    public Adept() {
        super("ADEPT", 0, 0, 4, 4);
    }

    @Override
    public void play(CreatureCard target){
        super.play(target);
        int position = getOwner().getCreatures().indexOf(target);

        getOwner().putCreature(new AdeptIllusion(this), position + 1);
    }
}
