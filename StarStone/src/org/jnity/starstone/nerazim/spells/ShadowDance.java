package org.jnity.starstone.nerazim.spells;

import java.util.List;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;
import org.jnity.starstone.modifiers.Invisibility;
import org.jnity.starstone.modifiers.Modifier;
import org.jnity.starstone.modifiers.TurnAttackBuff;

public class ShadowDance extends SpellCard {

    private static final long serialVersionUID = 4320370784822342205L;

    private static int POWER_BUFF = 2;

    public ShadowDance() {
        super("SHADOWDANCE", 0, 0);
    }

    @Override
    public void play(CreatureCard target){
        super.play(target);
        List<CreatureCard> creatures = getOwner().getCreatures();
        for(int i = 0; i < creatures.size(); i++) {
        	List<Modifier> modifiers = creatures.get(i).getModifiers();
            for(int j = 0; j < modifiers.size(); j++){
                if(modifiers.get(j) instanceof Invisibility)
                    creatures.get(i).addModifier(new TurnAttackBuff(creatures.get(i), POWER_BUFF));
            }
        }
    }
}
