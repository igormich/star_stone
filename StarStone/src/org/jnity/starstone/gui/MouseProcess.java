package org.jnity.starstone.gui;

import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import base.Camera;
import base.Object3d;
import base.Scene;
import properties.Mesh;
import utils.PrimitiveFactory;

public class MouseProcess {
	private static Object3d underCursor;
	private static GuiCard selected;
	private static Vector3f basePos;
	public static Object3d endTurnButton;
	public static Game game;
	public static Player player;
	private Scene scene;
	private Camera camera;
	private Object3d place;

	public MouseProcess(Scene scene, Camera camera) {
		this.scene = scene;
		this.camera = camera;
		Mesh placeMesh = PrimitiveFactory.createPlane(14f, 3);
		place = scene.add(placeMesh);
		place.getPosition().setTranslation(0, 0, -2.8f);
	}

	public void tick() {
		int x = Mouse.getX();
		int y = Mouse.getY();
		if (selected != null) {
			Vector3f pos = new Vector3f();
			pos.x = ((float) x / Display.getWidth() - 0.5f) * 14;
			pos.y = 0;
			pos.z = ((float) y / Display.getHeight() - 0.5f) * 10;
			((GuiCard) selected).startMoving(pos, pos);
			GuiCard.all(c -> c.setVisible(false));
			underCursor = scene.getObject(x, y, camera);
			GuiCard.all(c -> c.setVisible(true));
		} else {
			underCursor = scene.getObject(x, y, camera);
		}
		boolean canTouch = game.getActivePlayer().equals(player);
		while (Mouse.next()) {
			if (Mouse.getEventButton() > -1) {
				if (Mouse.getEventButtonState()) {
					if ((underCursor != null) && (underCursor.equals(endTurnButton))) {
						new Thread(() -> game.nextTurn()).start();
						return;
					}
					if (!canTouch) {
						return;
					}
					if(underCursor instanceof GuiCard) {
						GuiCard guiCard = (GuiCard) underCursor;
						if(player.canPlay(guiCard.getCard()))
							selected = (GuiCard) guiCard;
					}
					underCursor = null;
					if (selected != null)
						basePos = selected.getPosition().getTranslation();
				} else {
					if (selected != null) {
						if (place.equals(underCursor)) {
							List<CreatureCard> creatures = player.getCreatures();
							int place = 0;
							for(CreatureCard creatureCard: creatures) {
								if(GuiCard .get(creatureCard).getPosition().getTranslation().x > selected.getPosition().getTranslation().x)
									break;
								place++;
							}
							int p = place;
							Card card = selected.getCard();
							new Thread(() -> player.play(card, null, p)).start();
						} else {
							selected.startMoving(basePos);
						}
					}
					selected = null;
				}
			}
		}
		if(underCursor!=null) {
			//Display.setTitle("" + underCursor.getName());
		} else
			Display.setTitle("-1");

	}

}
