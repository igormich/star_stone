package org.jnity.starstone.protoss;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.modifier.CreatureModifier;

public class PlasmaShield extends CreatureModifier  {

	public PlasmaShield(CreatureCard target) {
		super(target);
	}

	@Override
	public int modifyDamage(int value, CreatureCard creatureCard) {
		getTarget().removeModifier(this);
		return 0;
	}
}
