package org.jnity.starstone.zerg.creatures;

import org.jnity.starstone.cards.CreatureCard;

public class NydusWorm extends CreatureCard {

    private static final long serialVersionUID = -8246990250816820751L;

    public NydusWorm() {
        super("NYDUSWORM", 0, 0, 5, 3);
    }

    @Override
    public void play(CreatureCard target){
        super.play(target);
        int size = getOwner().getCreatures().size();

        while(size<7){
            int position = getOwner().getCreatures().indexOf(this);
            getOwner().putCreature(new Zergling(),position + 1);
            getOwner().putCreature(new Zergling(), position);
            size += 2;
        }
    }
}
