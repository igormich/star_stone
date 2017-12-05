package org.jnity.starstone.terran.creatures;

import org.jnity.starstone.cards.CreatureCard;

public abstract class Human extends CreatureCard {

	public Human(String ID, int priceInMineral, int priceInGas, int maxHits, int power) {
		super(ID, priceInMineral, priceInGas, maxHits, power);
	}

}
