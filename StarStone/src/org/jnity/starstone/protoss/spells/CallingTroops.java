package org.jnity.starstone.protoss.spells;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;
import org.jnity.starstone.protoss.creatures.Zealot;

public class CallingTroops extends SpellCard{

    private static final long serialVersionUID = 6686558413657468024L;

    public CallingTroops() {
        super("CALLING_TROOPS", 0, 0);
    }

    @Override
    public void play(CreatureCard target){
        getOwner().putCreature(new Zealot(), getOwner().getCreatures().size());
        getOwner().putCreature(new Zealot(), 0);
    }
}
