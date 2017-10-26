package org.jnity.starstone.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;
import org.jnity.starstone.modifiers.Modifier;

public class Player extends CreatureCard implements GameListener{

	private static final long serialVersionUID = 3148587577944545132L;
	private static final int MAX_HAND_SIZE = 10;
	private static final int MAX_CREATURE_COUNT = 7;

	public Player(String name, List<Card> deck) {
		super(name, 0, 0, 30, 0);
		for (Card card:deck) {
			this.deck.add(card.clone());
		}
	}

	private ArrayList<CreatureCard> creatures = new ArrayList<>();
	private ArrayList<Card> hand = new ArrayList<>();
	private ArrayList<Card> deck = new ArrayList<>();
	private int maxMinerals = 0;
	private int currentMinerals;
	private int maxVespenGase = 0;
	private int currentVespenGase;
	private int playedCardCount;
	
	public int getMaxMinerals() {
		int result = maxMinerals;
		for (Modifier modifier : getModifiers())
			result = modifier.modifyMaxMinerals(result, this);
		return result;
	}
	public int getMaxVespenGase() {
		int result = maxVespenGase;
		for (Modifier modifier : getModifiers())
			result = modifier.modifyMaxVespenGase(result, this);
		return result;
	}
	
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
			} else {
				//getGame().emit(GameEvent.CARD_DISPELL, card);
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

	public void play(Card card, CreatureCard target, int position) {
		if(!hand.contains(card))
			throw new GameException("Card must be played from hand");
		if(!card.canBePlayed())
			throw new GameException("Card can't be played");
		hand.remove(card);
		if(card instanceof CreatureCard) {
			putCreature(card, position);
		}
		card.play(target);
		playedCardCount++;
	}

	public void putCreature(Card card, int position) {
		if(creatures.size()<MAX_CREATURE_COUNT) {
			if(position>-1 && position<MAX_CREATURE_COUNT)
				creatures.add(position, (CreatureCard) card);
		}
	}
	public boolean hasResoursesFor(Card card) {
		return getCurrentMinerals()>=card.getPriceInMineral() && getCurrentVespenGase()>=card.getPriceInGas();
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
		if(GameEvent.NEW_TURN == gameEvent && card.equals(this)) {
			maxMinerals++;
			currentMinerals = maxMinerals;
			playedCardCount = 0;
		}
	}
	public int getCurrentMinerals() {
		return currentMinerals;
	}
	public void setCurrentMinerals(int currentMinerals) {
		this.currentMinerals = currentMinerals;
	}
	public int getCurrentVespenGase() {
		return currentVespenGase;
	}
	public void setCurrentVespenGase(int currentVespenGase) {
		this.currentVespenGase = currentVespenGase;
	}
	@Override
	public String toString() {
		return  getName() +" Min:" + getCurrentMinerals() + "Gas:" + getCurrentVespenGase() + "Hits" + getCurrentHits();
	}
	public boolean canPlay(Card card) {
		return getHand().contains(card) && hasResoursesFor(card) && 
				(card instanceof SpellCard || creatures.size() < MAX_CREATURE_COUNT) ;
	}
	public boolean canAtack(Card card) {
		return creatures.contains(card) && ((CreatureCard) card).canAtack();//TODO:
	}
	public int getCountPlayedCard() {
		return playedCardCount;
	}
	public void putInHand(CreatureCard card) {
		if(hand.size() < MAX_HAND_SIZE) {
			hand.add(card);
		}
	}
	
}
