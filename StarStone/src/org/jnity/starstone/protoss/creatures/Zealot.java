package org.jnity.starstone.protoss.creatures;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.modifiers.PlasmaShield;

public class Zealot extends CreatureCard{

	private static final long serialVersionUID = 4199614529695022740L;

	public Zealot() {
		super("ZEALOT", 1, 0, 2, 1);
	}
    @Override
    public void play(CreatureCard target) {
        super.play(target);
        new PlasmaShield(this);
    }
}
