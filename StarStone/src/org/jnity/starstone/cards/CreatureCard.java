package org.jnity.starstone.cards;

import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.modifiers.Modifier;

public class CreatureCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5720889514543323639L;
	private int maxHits;
	private int currentHits;
	private int power;

	public CreatureCard(String ID, int priceInMineral, int priceInGas, int maxHits, int power) {
		super(ID, priceInMineral, priceInGas);
		this.maxHits = maxHits;
		this.power = power;
		this.currentHits = getMaxHits();
	}

	public int getMaxHits() {
		int result = maxHits;
		for (Modifier modifier : getModifiers())
			result = modifier.modifyMaxHits(result, this);
		return result;
	}

	public int getCurrentHits() {
		return currentHits;
	}

	public int getPower() {
		int result = power;
		for (Modifier modifier : getModifiers())
			result = modifier.modifyPower(result, this);
		return result;
	}

	public int takeDamage(int damage, Card sourse) {
		for (Modifier modifier : getModifiers())
			damage = modifier.modifyDamage(damage, this, sourse);
		changeCurrentHits(-damage, sourse);
		return damage;
	}

	public int heal(int healingPower, Card sourse) {
		for (Modifier modifier : getModifiers())
			healingPower = modifier.modifyHeal(healingPower, this, sourse);
		changeCurrentHits(currentHits, sourse);
		return healingPower;
	}

	public void changeCurrentHits(int change, Card sourse) {
		currentHits = Math.min(Math.max(0, currentHits + change), getMaxHits());
		if(change<0)
			getGame().emit(GameEvent.TAKE_DAMAGE, sourse, this);
		if((change>0) && currentHits < getMaxHits())
			getGame().emit(GameEvent.HEALED, sourse, this);
		if (currentHits == 0) 
			die();
	}
	public void die() {
		getGame().emit(GameEvent.DIES, this);
		getGame().removeListener(this);
	}

	@Override
	public String toString() {
		return super.toString() + " A:" +getPower() + " H:" +getCurrentHits();
	}
	@Override
	public void play(CreatureCard target) {
		super.play(target);
	}
	
	public boolean canAtack() {
		if(getPower() <=0)
			return false;
		boolean result = true;
		for (Modifier modifier : getModifiers())
			result = modifier.modifyCanAtack(result, this);
		return result;
	}
}
