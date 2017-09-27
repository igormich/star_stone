package org.jnity.starstone.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;

public class Game {
	private final List<GameListener> listeners = new ArrayList<>();
	private final List<Player> players = new ArrayList<>();
	private Player activePlayer;
	
	public Game(Player p1,Player p2) {
		players.add(p1);
		players.add(p2);
		players.forEach(p->p.setGame(this));
	}
	public void addListener(GameListener listener) {
		listeners.add(listener);
	}
	public void removeListener(Object listener) {
		listeners.remove(listener);
	}
	public Collection<Player> getPlayers(){
		return players;
	}
	public Player getActivePlayer() {
		return activePlayer;
	}
	public void emit(GameEvent gameEvent, Card card, CreatureCard target) {
		Debug.print(card + " " + gameEvent +  " " + target);
		for (GameListener gameListener : listeners) {
			gameListener.on(gameEvent, card, target);
		}
	}
	public void emit(GameEvent gameEvent, Card card) {
		Debug.print(gameEvent + " " + card);
		for (GameListener gameListener : listeners) {
			gameListener.on(gameEvent, card);
		}
	}
	public void nextTurn() {
		if(activePlayer==null) {
			activePlayer = players.get(0);
		} else {
			emit(GameEvent.END_OF_TURN, activePlayer);
			if(activePlayer.equals(players.get(0))){
				activePlayer = players.get(1);
			} else {
				activePlayer = players.get(0);
			}
		}
		emit(GameEvent.NEW_TURN, activePlayer);
		activePlayer.drawCard();
	}
	
}