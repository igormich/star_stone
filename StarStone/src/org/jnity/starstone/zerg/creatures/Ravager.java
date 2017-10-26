package org.jnity.starstone.zerg.creatures;

import org.jnity.starstone.cards.CreatureCard;

public class Ravager extends CreatureCard {

    private static final long serialVersionUID = 6702633870724841421L;

    public Ravager() {
        super("RAVAGER", 0, 0, 4, 3);
    }

//    @Override
//    public void attack(CreatureCard target){
//        List modifiers = target.getModifiers();
//        for(int i = 0; i < modifiers.size(); i++) {
//            if (modifiers.get(i) instanceof PlasmaShield){
//                target.removeModifier((Modifier) modifiers.get(i));
//            }
//        }
//        super.attack(target);
//    }
}
