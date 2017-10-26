package org.jnity.starstone.nerazim.creatures;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.modifiers.Buff;
import org.jnity.starstone.modifiers.Invisibility;

public class ShadowScythe extends CreatureCard {

    private static final long serialVersionUID = 1601869087968748609L;

    private static final int powerBuff = 2;
    private static final int hitsBuff = 1;
    private int count;

    public ShadowScythe() {
        super("SHADOWSCYTHE", 0, 0, 4, 3);
        addModifier(new Invisibility(this));
    }

    @Override
    public void play(CreatureCard target){
        super.play(target);
        if(getOwner().getCountPlayedCard() > 0 && count == 0) {
            addModifier(new Buff(this, powerBuff, hitsBuff));
            count++;
        }
    }
}
