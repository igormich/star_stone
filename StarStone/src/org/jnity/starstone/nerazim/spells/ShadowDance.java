package org.jnity.starstone.nerazim.spells;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;
import org.jnity.starstone.modifiers.TurnAttackBuff;
import org.jnity.starstone.modifiers.Invisibility;

import java.util.List;

public class ShadowDance extends SpellCard {

    private static final long serialVersionUID = 4320370784822342205L;

    private static int powerBuff = 2;

    public ShadowDance() {
        super("SHADOWDANCE", 0, 0);
    }

    @Override
    public void play(CreatureCard target){
        super.play(target);
        List<CreatureCard> creatures = getOwner().getCreatures();
        List modifiers;
        for(int i = 0; i < creatures.size(); i++) {
            modifiers = creatures.get(i).getModifiers();
            for(int j = 0; j < modifiers.size(); j++){
                if(modifiers.get(j) instanceof Invisibility)
                    creatures.get(i).addModifier(new TurnAttackBuff(creatures.get(i), powerBuff));
            }
        }
    }
}
