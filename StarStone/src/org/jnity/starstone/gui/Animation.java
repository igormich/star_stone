package org.jnity.starstone.gui;

import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.events.GameEvent;
import org.lwjgl.util.vector.Vector3f;

import base.Scene;

public class Animation {
	private float time = 0;

	public static Animation createFor(StoredEvent event, Scene scene, Player gamer) {
		if (event.getType() == GameEvent.DRAW) {
			return new DrawAnimation(event.getCard(), scene, gamer);
		}
		if (event.getType() == GameEvent.PLAY) {
			return new PlayAnimation(event.getCard(), scene, gamer);
		}
		if (event.getType() == GameEvent.PUT) {
			regroupCreaturesWithOutDelay(event.getCard().getOwner(),
					gamer.equals(event.getCard().getOwner()) ? 1 : -1);
		}
		if (event.getType() == GameEvent.DIES) {
			scene.remove(GuiCard.get(event.getCard()));
			regroupCreatures(event.getCard().getOwner(), gamer.equals(event.getCard().getOwner()) ? 1 : -1);
		}
		return new StubAnimation();
	}

	public static void regroupCreaturesWithOutDelay(Player player, int mirrow) {
		List<CreatureCard> creatures = player.getCreatures();
		int i = 0;
		for (CreatureCard creature : player.getCreatures()) {
			GuiCard guiCard = GuiCard.get(creature);
			float x = -creatures.size() / 2 + i + (1 - creatures.size() % 2) * 0.5f;
			Vector3f pos = new Vector3f(2 * x * mirrow, 0, -1.5f * mirrow - 0.5f);
			guiCard.getPosition().setTranslation(pos);
			guiCard.startMoving(pos);
			i++;
		}
	}

	public static void regroupCreatures(Player player, int mirrow) {
		List<CreatureCard> creatures = player.getCreatures();
		int i = 0;
		for (CreatureCard creature : player.getCreatures()) {
			GuiCard guiCard = GuiCard.get(creature);
			float x = -creatures.size() / 2 + i + (1 - creatures.size() % 2) * 0.5f;
			guiCard.startMoving(new Vector3f(2 * x * mirrow, 0, -1.5f * mirrow - 0.5f));
			i++;
		}
	}

	public static void regroupHand(Player player, int mirrow) {
		List<Card> hand = player.getHand();
		int i = 0;
		for (Card cardInHand : hand) {
			GuiCard guiCard = GuiCard.get(cardInHand);
			float x = -hand.size() / 2f + i;
			guiCard.startMoving(new Vector3f(x * mirrow, 0, -6.5f * mirrow));
			i++;
		}
	}

	public boolean isFinished() {
		return time > 1;
	}

	public void play(float deltaTime, Scene scene) {
		time += deltaTime;
	}

	public float getTime() {
		return Math.min(time, 1);
	}

}
