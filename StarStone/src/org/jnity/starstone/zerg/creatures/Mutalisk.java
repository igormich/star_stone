package org.jnity.starstone.zerg.creatures;

import org.jnity.starstone.cards.CreatureCard;

public class Mutalisk extends CreatureCard {

    private static final long serialVersionUID = -9136509066073735777L;

    public Mutalisk() {
        super("MUTALISK", 3, 0, 2, 4);
    }

    //Charge
    @Override
    public void play(CreatureCard target){
        super.play(target);
//        super.attack(target); //Something like that?

    }
}
