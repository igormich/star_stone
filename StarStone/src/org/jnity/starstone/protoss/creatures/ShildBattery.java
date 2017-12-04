package org.jnity.starstone.protoss.creatures;

import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Debug;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;
import org.jnity.starstone.modifiers.PlasmaShield;

public class ShildBattery extends CreatureCard implements GameListener {

	private static final long serialVersionUID = 4681027585238431880L;

	public ShildBattery() {
		super("SHILD_BATTERY", 2, 0, 4, 0);
	}

	@Override
	public void play(CreatureCard target) {
		super.play(target);
		getGame().addListener(this);
	}

	@Override
	public void on(GameEvent gameEvent, Card card, CreatureCard nothing) {
		if(GameEvent.END_OF_TURN == gameEvent && card.equals(getOwner())) {
			List<CreatureCard> neighbors = getOwner().getCreaturesNear(this);
			neighbors.forEach(c -> new PlasmaShield(c));
			Debug.print("give shild to " + neighbors);
		}
	}
	
	
}
