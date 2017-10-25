package org.jnity.starstone.modifiers;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.modifier.CreatureModifier;

public class PlasmaShield extends CreatureModifier  {

	public PlasmaShield(CreatureCard target) {
		super(target);
	}

	@Override
	public int modifyDamage(int value, CreatureCard creatureCard) {
		if (value>0) {
			getTarget().removeModifier(this);
		}
		return 0;
	}
	@Override
	public boolean canBeDuplicated() {
		return false;
	}
}
