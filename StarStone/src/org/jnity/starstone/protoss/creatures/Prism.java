package org.jnity.starstone.protoss.creatures;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.modifiers.Buff;

public class Prism extends CreatureCard{

    private static final int attackBuff = 1;
    private static final long serialVersionUID = 2298275931798946061L;

    public Prism() { super("PRISM", 0, 0, 3, 2);}

    @Override
    public void play(CreatureCard target){
        super.play(target);
        target.heal(target.getMaxHits()-target.getCurrentHits(),this);
        new Buff(target, attackBuff, 0);
    }
}
