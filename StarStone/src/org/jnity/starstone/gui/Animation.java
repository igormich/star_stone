package org.jnity.starstone.gui;

import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.events.GameEvent;
import org.lwjgl.util.vector.Vector3f;

import base.Scene;

public class Animation {
	private float time = 0;
	
	public static Animation createFor(StoredEvent event, Scene scene, Player gamer) {
		if(event.getType() == GameEvent.DRAW) {
			return new DrawAnimation(event.getCard(), scene, gamer);
		}
		if(event.getType() == GameEvent.PLAY) {
			return new PlayAnimation(event.getCard(), scene, gamer);
		}
		if(event.getType() == GameEvent.DIES) {
			scene.remove(GuiCard.get(event.getCard()));
			regroupCreatures(event.getCard().getOwner(),gamer.equals(event.getCard().getOwner()) ? 1 : -1);
		}
		return new StubAnimation();
	}

	private static void regroupCreatures(Player player, int mirrow) {
		List<CreatureCard> creatures = player.getCreatures();
		int i = 0;
		for (CreatureCard creature : player.getCreatures()) {
			GuiCard guiCard = GuiCard.get(creature);
			float x = -creatures.size() / 2 + i;
			System.out.println(x);
			guiCard.startMoving(new Vector3f(2 * x * mirrow, 0, -3 * mirrow));
			i++;
		}

	}

	public boolean isFinished() {
		return time > 1;
	}

	public void play(float deltaTime, Scene scene) {
		time+=deltaTime;
	}

	public float getTime() {
		return Math.min(time, 1);
	}

}
