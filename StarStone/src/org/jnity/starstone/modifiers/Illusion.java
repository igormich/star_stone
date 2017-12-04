package org.jnity.starstone.modifiers;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;

public class Illusion extends CreatureModifier {

    public Illusion(CreatureCard target) {
        super(target);
    }

    @Override
    public int modifyDamage(int value, CreatureCard creatureCard, Card sourse) {
        return creatureCard.getMaxHits();
    }
}
