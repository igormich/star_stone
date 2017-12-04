package org.jnity.starstone.zerg.creatures;

import org.jnity.starstone.cards.CreatureCard;

public class Hydralisk extends CreatureCard{

    private static final long serialVersionUID = -8044857020463159718L;

    public Hydralisk() {
        super("HYDRALISK", 0, 0, 4, 3);
    }

    @Override
    public void play(CreatureCard target){
        super.play(target);
        if(target != null) target.takeDamage(2, this);
    }

}
