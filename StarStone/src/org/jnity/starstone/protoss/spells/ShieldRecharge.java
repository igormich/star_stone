package org.jnity.starstone.protoss.spells;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;
import org.jnity.starstone.modifiers.PlasmaShield;

public class ShieldRecharge extends SpellCard {

	private static final long serialVersionUID = 2566817888175570746L;
	public ShieldRecharge() {
		super("RECHARGE_SHIELDS", 1, 0);
	}
	public void play(CreatureCard target) {
		new PlasmaShield(target);
	}
}
