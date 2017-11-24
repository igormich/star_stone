package org.jnity.starstone.cards;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jnity.starstone.core.ModifierContainer;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.core.TextHolder;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.modifiers.Defender;
import org.jnity.starstone.modifiers.Invisibility;
import org.jnity.starstone.modifiers.Invulnerability;
import org.jnity.starstone.modifiers.Modifier;

public class Card extends ModifierContainer implements Cloneable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4904779573123820297L;
	
	static final AtomicInteger idCounter = new AtomicInteger(1);
	private int id = idCounter.getAndIncrement();
	
	private final String ID;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getSerial();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (getSerial() != other.getSerial())
			return false;
		return true;
	}

	protected final int priceInMineral;
	protected final int priceInGas;
	private Player player;

	public int getPriceInGas() {
		int result = priceInGas;
		for (Modifier modifier : getModifiers())
			result = modifier.modifyPriceInGas(result, this);
		return result;
	}

	public int getPriceInMineral() {
		int result = priceInMineral;
		for (Modifier modifier : getModifiers())
			result = modifier.modifyPriceInMineral(result, this);
		return result;
	}

	public Card(String ID, int priceInMineral, int priceInGas) {
		super();
		this.ID = ID;
		this.priceInMineral = priceInMineral;
		this.priceInGas = priceInGas;
	}
	
	public String getID() {
		return ID;
	}
	
	public final String getName() {
		return TextHolder.getName(getID());	
	}
	
	public final String getDescription() {
		return TextHolder.getDescription(getID());	
	}
	
	public final String getAbout() {
		return TextHolder.getAbout(getID());	
	}
	
	public Card clone() {		
		try {
			ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(tempBuffer);
			out.writeObject(this);
			out.flush();
			out.close();
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(tempBuffer.toByteArray()));
			Card result = (Card) in.readObject();
			result.id = idCounter.getAndIncrement();
			in.close();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public boolean canBePlayed() {
		boolean result = player.hasResoursesFor(this);
		for (Modifier modifier : getModifiers())
			result = modifier.modifyCanBePlayed(result, this);
		return result;
	}
	
	public void play(CreatureCard target) {
		getGame().emit(GameEvent.PLAY, this);
	}

	@Override
	public String toString() {
		return getName() + " M:" +  getPriceInMineral();
	}

	public void setOwner(Player player) {
		this.player = player;
	}
	public Player getOwner() {
		Player result = player;
		for (Modifier modifier : getModifiers())
			result = modifier.modifyPlayer(result, this);
		return result;
	}

	public boolean needTarget() {
		return false;
	}

	public boolean isValidTarget(Card target) {
		List<Modifier> modifiers = target.getModifiers();
		for (Object modifier : modifiers) {
			if (modifier instanceof Invisibility || modifier instanceof Invulnerability)
				return false;
		}
		return true;
	}

	public boolean canAtack(CreatureCard target) {
		if (!target.hasModifier(Defender.class)){
			if (target.getOwner().getCreatures().stream().anyMatch(c ->c.hasModifier(Defender.class) && !c.hasModifier(Defender.class))) {
				return false;
			}
		}
		return !target.getOwner().equals(this.getOwner())
				&& target instanceof CreatureCard
				&& target.getOwner().getCreatures().contains(target)
				&& this.getOwner().getCreatures().contains(this)
				&& isValidTarget(target);
	}

	public int getSerial() {
		return id;
	}



}
