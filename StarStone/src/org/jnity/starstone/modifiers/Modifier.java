package org.jnity.starstone.modifiers;

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

	default int modifyDamage(int value, CreatureCard creatureCard, Card sourse) {
		return value;
	}
	default int modifyHeal(int value, CreatureCard creatureCard, Card sourse) {
		return value;
	}

	default Player modifyPlayer(Player player, Card card) {
		return player;
	}
	default boolean modifyCanBePlayed(boolean value, Card card) {
		return value;
	}
	
	default int modifyMaxMinerals(int value, Player player) {
		return value;
	}
	
	default int modifyMaxVespenGase(int value, Player player) {
		return value;
	}

	default boolean modifyCanAtack(boolean value, CreatureCard creatureCard) {
		return value;
	}
	default boolean canBeDuplicated() {
		return true;
	}
}
