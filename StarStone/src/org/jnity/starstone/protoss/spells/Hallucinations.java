package org.jnity.starstone.protoss.spells;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;
import org.jnity.starstone.events.GameListener;
import org.jnity.starstone.modifiers.Invulnerability;
import org.jnity.starstone.protoss.creatures.Hallucination;

public class Hallucinations extends SpellCard {

    private static final long serialVersionUID = 1621291416537476469L;

    public Hallucinations() {
        super("HALLUCINATIONS", 0, 0);
    }

    @Override
    public void play(CreatureCard target){

        int size = getOwner().getCreatures().size();
        int position = getOwner().getCreatures().indexOf(target);

        if(size < 7) {
            getOwner().putCreature(new Hallucination(target), position + 1);
            getOwner().putCreature(new Hallucination(target), position);
            target.addModifier(new Invulnerability(target));
        }
    }
}
