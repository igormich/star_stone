package org.jnity.starstone.terran.creatures;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.modifiers.Defender;

public class Bunker extends Human {

	public Bunker() {
		super("BUNKER", 1, 0, 4, 2);
	}

	@Override
	public void play(CreatureCard target) {
		super.play(target);
		new Defender(this);
	}

	@Override
	public void die() {
		int myPos = getOwner().getCreatures().indexOf(this);
		super.die();
		for(int i=0;i<2;i++) {
			Marine marine = new Marine();
			marine.setGame(getGame());
			marine.setOwner(getOwner());
			marine.setOnDesk(true);
			getOwner().putCreature(marine, myPos);
		}
	}

}
