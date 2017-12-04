package org.jnity.starstone.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;
import org.jnity.starstone.modifiers.CombatFatigue;

public class Game implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8168645814801696635L;
	protected final ArrayList<GameListener> listeners = new ArrayList<>();
	protected List<Player> players = new ArrayList<>();
	protected final AtomicInteger turnNumber = new AtomicInteger(0);
	protected volatile Player activePlayer;

	public Game() {
		
	}
	public Game(Player p1, Player p2) {
		players.add(p1);
		players.add(p2);
		players.forEach(p -> p.setGame(this));
	}

	public void addListener(GameListener listener) {
		listeners.add(listener);
	}

	public void removeListener(Object listener) {
		listeners.remove(listener);
	}

	public Collection<Player> getPlayers() {
		return players;
	}

	public Player getActivePlayer() {
		return activePlayer;
	}

	
	public void emit(GameEvent gameEvent, Card card, CreatureCard target) {
		Debug.print(card + " " + gameEvent + " " + target);
		@SuppressWarnings("unchecked")
		ArrayList<GameListener> listeners = (ArrayList<GameListener>)this.listeners.clone();
		for (GameListener gameListener : listeners) {
			gameListener.on(gameEvent, card, target);
		}
	}

	public void emit(GameEvent gameEvent, Card card) {
		Debug.print(gameEvent + " " + card);
		@SuppressWarnings("unchecked")
		ArrayList<GameListener> listeners = (ArrayList<GameListener>)this.listeners.clone();
		for (GameListener gameListener :  listeners) {
			gameListener.on(gameEvent, card, null);
		}
	}

	public void nextTurn() {
		if (activePlayer == null) {
			activePlayer = players.get(0);
			emit(GameEvent.GAME_BEGIN, activePlayer);
			players.get(0).drawCard();
			players.get(0).drawCard();
			players.get(0).drawCard();
			
			players.get(1).drawCard();
			players.get(1).drawCard();
			players.get(1).drawCard();
			players.get(1).drawCard();
			
		} else {
			emit(GameEvent.END_OF_TURN, activePlayer);
			if (activePlayer.equals(players.get(0))) {
				activePlayer = players.get(1);
			} else {
				activePlayer = players.get(0);
			}
		}
		activePlayer.drawCard();
		turnNumber.incrementAndGet();
		emit(GameEvent.NEW_TURN, activePlayer);
	}

	public int getTurnNumber() {
		return turnNumber.get();
	}

	public void battle(CreatureCard card, CreatureCard target) {
		int atackerPower = card.getPower();
		int defenderPower = target.getPower();
		card.takeDamage(defenderPower,target);
		target.takeDamage(atackerPower, card);
		new CombatFatigue(card);
		emit(GameEvent.ATACKS, card, target);
		emit(GameEvent.DEFENDED, target, card);
	}

	public List<CreatureCard> getAllCreaturesAndPlayers() {
		List<CreatureCard> result = new ArrayList<>();
		result.addAll(players);
		players.forEach(p -> result.addAll(p.getCreatures()));
		return result;
	}
	public List<Card> getAll() {
		List<Card> result = new ArrayList<>();
		result.addAll(getAllCreaturesAndPlayers());
		players.forEach(p -> result.addAll(p.getHand()));
		return result;
	}
	public boolean isReady() {
		return activePlayer != null;
	}
	public Player getPlayerByID(String player) {
		return players.stream().filter(p -> p.getID().equals(player)).findFirst().get();
	}
	public void play(Card card, CreatureCard target, int placePosition) {
		activePlayer.play(card,target,placePosition);
	}
	public Card getCardBySerial(int serial) {
		return getAll().stream().filter(c -> c.getSerial() == serial).findAny().orElseGet(() -> null);
	}
	public Card renewCard(Card card) {
		Card found = getCardBySerial(card.getSerial());
		if (found != null) {
			return found;
		}
		return card;
	}
	public Player getOpponent(Player player) {
		return players.stream().filter(p->!player.equals(p)).findAny().get();
	}

}
