package org.jnity.starstone.gui;

import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import base.Camera;
import base.Object3d;
import base.Scene;
import materials.SimpleMaterial;
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
	private List<Object3d> places = new ArrayList<>();

	public MouseProcess(Scene scene, Camera camera) {
		this.scene = scene;
		this.camera = camera;
		scene.getMaterialLibrary().addMaterial("red", new SimpleMaterial(1, 0, 0));
		scene.getMaterialLibrary().addMaterial("green", new SimpleMaterial(0, 1, 0));

		for (int i = 0; i < 14; i++) {
			Mesh placeMesh = PrimitiveFactory.createPlane(0.9f, 3);
			Object3d place = scene.add(placeMesh);
			place.getPosition().setTranslation(i - 7, 0, -2.8f);
			places.add(place);
		}
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
			selected.setVisible(false);
			if (places.contains(underCursor)) {
				underCursor.get(Mesh.class).setMaterialName("green");
			}
			underCursor = scene.getObject(x, y, camera);
			if (places.contains(underCursor)) {
				underCursor.get(Mesh.class).setMaterialName("red");
			}
			selected.setVisible(true);
		} else {
			underCursor = scene.getObject(x, y, camera);
		}
		boolean canTouch = game.getActivePlayer().equals(player);
		//if(underCursor instanceof GuiCard) {
		//	canTouch&=((GuiCard)underCursor).getCard().getOwner().equals(player);
		//}
		while (Mouse.next()) {
			if (Mouse.getEventButton() > -1) {
				if (Mouse.getEventButtonState()) {
					System.out.println(canTouch);
					if ((underCursor != null) && (underCursor.equals(endTurnButton))) {
						new Thread(() -> game.nextTurn()).start();
						return;
					}
					if (!canTouch) {
						return;
					}
					if(underCursor instanceof GuiCard) {
						GuiCard guiCard = (GuiCard) underCursor;
						if(player.getHand().contains(guiCard.getCard()))
							selected = (GuiCard) guiCard;
					}
					underCursor = null;
					if (selected != null)
						basePos = selected.getPosition().getTranslation();
				} else {
					if (selected != null) {
						if (places.contains(underCursor)) {
							Card card = selected.getCard();
							int i = places.indexOf(underCursor);
							int count = player.getCreatures().size();
							int place = (i-count/2)/2;
							if(place<0)
								place = 0;
							if(place>count)
								place = count;
							int p = place;
							new Thread(() -> player.play(card, null, p)).start();
						} else {
							selected.startMoving(basePos);
						}
					}
					selected = null;
				}
			}
		}
		Display.setTitle("" + selected);

	}

}
