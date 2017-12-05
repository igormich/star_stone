package org.jnity.starstone.zerg.creatures;

import org.jnity.starstone.cards.CreatureCard;

public class Zergling extends CreatureCard {

    private static final long serialVersionUID = 2196076382842048671L;

    public Zergling() {
        super("ZERLING", 1, 0, 1, 1);
    }

    @Override
    public void play(CreatureCard target) {
        super.play(target);
        Zergling zergling = new Zergling();
        zergling.setGame(getGame());
        zergling.setOwner(getOwner());
        zergling.setOnDesk(true);
        getOwner().putCreature(zergling, getOwner().getCreatures().indexOf(this)+1);
    }

}
