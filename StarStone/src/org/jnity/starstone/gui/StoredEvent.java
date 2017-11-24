package org.jnity.starstone.gui;

import java.io.Serializable;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.events.GameEvent;

public class StoredEvent implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 7740122099419816020L;
		private final GameEvent type;
		private final Card card;
		private final CreatureCard target;
		private final Game game;

		public StoredEvent(GameEvent gameEvent, Card card, CreatureCard target, Game game) {
			type = gameEvent;
			this.card = card;
			this.target = target;
			this.game = game;
		}

		public StoredEvent(GameEvent gameEvent, Card card, Game game) {
			this(gameEvent, card, null, game);
		}

		public GameEvent getType() {
			return type;
		}

		public Card getCard() {
			return game.renewCard(card);
		}

		public CreatureCard getTarget() {
			if(target!=null) {
				return (CreatureCard) game.renewCard(target);
			} else {
				return null;
			}
		}

		public Game getGame() {
			return game;
		}
	}