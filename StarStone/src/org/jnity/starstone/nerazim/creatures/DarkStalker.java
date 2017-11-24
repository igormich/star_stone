package org.jnity.starstone.nerazim.creatures;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;

public class DarkStalker extends CreatureCard {

	private static final long serialVersionUID = 3331341303221438091L;

	public DarkStalker() {
		super("DARKSTALKER", 3, 0, 3, 3);
	}

	@Override
	public void play(CreatureCard target) {
		super.play(target);
		if (getOwner().getCountPlayedCard() > 1)
			target.takeDamage(2);
	}

	@Override
	public boolean needTarget() {
		return getOwner().getCountPlayedCard() > 0;
	}

	@Override
	public boolean isValidTarget(Card target) {
		return super.isValidTarget(target) && !target.getOwner().equals(this.getOwner());
	}

}
