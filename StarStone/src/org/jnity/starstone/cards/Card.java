package org.jnity.starstone.cards;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jnity.starstone.core.ModifierContainer;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.core.TextHolder;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.modifier.Modifier;

public class Card extends ModifierContainer implements Cloneable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4904779573123820297L;
	private final String ID;
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
	
	public void play(TargetWithHits target) {
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
}
