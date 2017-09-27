package org.jnity.starstone.modifier;

import org.jnity.starstone.cards.CreatureCard;

public class CreatureModifier implements Modifier {
	private CreatureCard target;

	public CreatureModifier(CreatureCard target) {
		this.target = target;
	}

	public CreatureCard getTarget() {
		return target;
	}

}
