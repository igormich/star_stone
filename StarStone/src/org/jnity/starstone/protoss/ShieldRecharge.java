package org.jnity.starstone.protoss;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;

public class ShieldRecharge extends SpellCard {

	private static final long serialVersionUID = 2566817888175570746L;
	public ShieldRecharge() {
		super("RECHARGE_SHIELDS", 1, 0);
	}
	public void play(CreatureCard target) {
		target.addModifier(new PlasmaShield(target));
	}
}
