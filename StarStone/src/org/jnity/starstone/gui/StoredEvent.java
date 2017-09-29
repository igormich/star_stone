package org.jnity.starstone.gui;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.events.GameEvent;

public class StoredEvent {
		private final GameEvent type;
		private final Card card;
		private final CreatureCard target;

		public StoredEvent(GameEvent gameEvent, Card card, CreatureCard target) {
			type = gameEvent;
			this.card = card;
			this.target = target;
		}

		public StoredEvent(GameEvent gameEvent, Card card) {
			this(gameEvent, card, null);
		}

		public GameEvent getType() {
			return type;
		}

		public Card getCard() {
			return card;
		}

		public CreatureCard getTarget() {
			return target;
		}
	}