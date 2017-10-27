package org.jnity.starstone.zerg.creatures;

import org.jnity.starstone.cards.CreatureCard;

public class Zergling extends CreatureCard {

    private static final long serialVersionUID = 2196076382842048671L;
    private  boolean split = true;

    public Zergling() {
        super("ZERGLING", 1, 0, 1, 1);
    }

    @Override
    public void play(CreatureCard target) {
        super.play(target);
        getOwner().putCreature(new Zergling(),getOwner().getCreatures().size());
    }

}
