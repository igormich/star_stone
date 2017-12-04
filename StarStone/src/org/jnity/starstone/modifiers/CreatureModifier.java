package org.jnity.starstone.modifiers;

import java.io.Serializable;

import org.jnity.starstone.cards.CreatureCard;

public class CreatureModifier implements Modifier, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6775866767330544308L;
	private CreatureCard target;

	public CreatureModifier(CreatureCard target) {
		this.target = target;
		target.addModifier(this);
	}

	public CreatureCard getTarget() {
		return target;
	}

}
