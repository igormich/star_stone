package org.jnity.starstone.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.TargetWithHits;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;
import org.jnity.starstone.protoss.PlasmaShield;

public class Player extends CreatureCard implements GameListener{

	private static final long serialVersionUID = 3148587577944545132L;
	private static final int MAX_HAND_SIZE = 10;

	public Player(String name, List<Card> deck) {
		super(name, 0, 0, 30, 0);
		for (Card card:deck) {
			this.deck.add(card.clone());
		}
	}

	private ArrayList<CreatureCard> creatures = new ArrayList<>();
	private ArrayList<Card> hand = new ArrayList<>();
	private ArrayList<Card> deck = new ArrayList<>();

	public List<CreatureCard> getCreatures() {
		return (List<CreatureCard>) creatures.clone();
	}

	public List<Card> getHand() {
		return (List<Card>) hand.clone();
	}

	public List<Card> getDeck() {
		return (List<Card>) deck.clone();
	}

	public void drawCard() {
		if(!deck.isEmpty()) {
			Card card = deck.remove(deck.size() - 1);
			if(hand.size() < MAX_HAND_SIZE) {
				hand.add(card);
				getGame().emit(GameEvent.DRAW, card);
			}
		} else {
			//take damage or lose
		}
	}
	
	@Override
	public void setGame(Game game) {
		if(!creatures.isEmpty() || !hand.isEmpty())
			throw new GameException("Game must be set before otner actions");
		super.setGame(game);
		deck.forEach(c -> c.setGame(game));
		deck.forEach(c -> c.setOwner(this));
		game.addListener(this);
	}

	public void play(Card card, TargetWithHits target, int position) {
		if(!hand.contains(card))
			throw new GameException("Card must be played from hand");
		if(!card.canBePlayed())
			throw new GameException("Card can't be played");
		hand.remove(card);
		if(card instanceof CreatureCard) {
			creatures.add(position, (CreatureCard) card);
		}
		card.play(target);
	}

	public boolean hasResoursesFor(Card card) {
		return true;//add check mineral and gas 
	}

	public List<CreatureCard> getCreaturesNear(CreatureCard creature) {
		if ((creatures.size() == 1) || !creatures.contains(creature))
			return Collections.emptyList();
		List<CreatureCard> result = new ArrayList<>(2);
		int index = creatures.indexOf(creature);
		if (index>0)
			result.add(creatures.get(index-1));
		if (index+1<creatures.size())
			result.add(creatures.get(index+1));
		return result;
	}
	@Override
	public void on(GameEvent gameEvent, Card card) {
		if(GameEvent.DIES == gameEvent && creatures.contains(card)) {
			creatures.remove(card);
		}
	}
}
