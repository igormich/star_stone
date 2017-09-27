package org.jnity.starstone.modifier;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Player;

public interface Modifier {

	default int modifyPriceInMineral(int value, Card card) {
		return value;
	}

	default int modifyPriceInGas(int value, Card card) {
		return value;
	}

	default int modifyPower(int value, CreatureCard creatureCard) {
		return value;
	}

	default int modifyMaxHits(int value, CreatureCard creatureCard) {
		return value;
	}

	default int modifyDamage(int value, CreatureCard creatureCard) {
		return value;
	}
	default int modifyHeal(int value, CreatureCard creatureCard) {
		return value;
	}

	default Player modifyPlayer(Player player, Card card) {
		return player;
	}
	default boolean modifyCanBePlayed(boolean value, Card card) {
		return value;
	}
}
