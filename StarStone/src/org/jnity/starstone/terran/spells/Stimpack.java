package org.jnity.starstone.terran.spells;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;
import org.jnity.starstone.modifiers.Buff;
import org.jnity.starstone.terran.creatures.Human;

public class Stimpack extends SpellCard {

	private static final long serialVersionUID = 1621291416537476469L;

	public Stimpack() {
		super("STIMPACK", 1, 0);
	}

	@Override
	public void play(CreatureCard target) {
		target.takeDamage(1, this);
		new Buff(target, 2, 0);
	}
	public boolean needTarget() {
		return true;
	}
	public boolean isValidTarget(Card target) {
		if (super.isValidTarget(target)){
			if (target instanceof Human) 
				return true;
		}
		return false;
	}
}
