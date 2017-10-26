package org.jnity.starstone.zerg.creatures;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.events.GameListener;

public class Baneling extends CreatureCard implements GameListener{
    private static final long serialVersionUID = -930875945425631349L;

    public Baneling() {
        super("BANELING", 3, 0, 3, 2);
    }

//    @override
//    public void attack(CreatureCard target){
//        super.attack(target);
////        target.addModifier(new Poison(target));
//    }

}
