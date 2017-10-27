package org.jnity.starstone.zerg.creatures;

import org.jnity.starstone.cards.CreatureCard;

public class Ultralisk extends CreatureCard {

    private static final long serialVersionUID = -2809682915424808307L;

    public Ultralisk() {
        super("ULTRALISK", 8, 0, 8, 6);
    }

//    @override
//    public void attack(CreatureCard target){
//        super.attack(target);
//        List<CreatureCard> neighbors = target.getOwner().getCreaturesNear(target);
//        neighbors.forEach(c -> c.takeDamage(this.getPower() / 2));
//    }

}
