package org.jnity.starstone.gui;

import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.core.Game;
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
	private static Object3d selected;
	private static Vector3f basePos;
	public static Object3d endTurnButton;
	public static Game game;
	private Scene scene;
	private Camera camera;
	private List<Object3d> places = new ArrayList<>();

	public MouseProcess(Scene scene, Camera camera) {
		this.scene = scene;
		this.camera = camera;
		scene.getMaterialLibrary().addMaterial("red", new SimpleMaterial(1, 0, 0));
		scene.getMaterialLibrary().addMaterial("green", new SimpleMaterial(0, 1, 0));

		for (int i = 0; i < 14; i++) {
			Mesh placeMesh = PrimitiveFactory.createPlane(0.9f, 2);
			Object3d place = scene.add(placeMesh);
			place.getPosition().setTranslation(i - 7, 0, -2.3f);
			places.add(place);
		}
	}

	public void tick(boolean canTouch) {
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
					selected = underCursor;
					underCursor = null;
					if (selected != null)
						basePos = selected.getPosition().getTranslation();
				} else {
					if (selected != null) {
						((GuiCard) selected).startMoving(basePos);
					}
					selected = null;
				}
			}
		}
		Display.setTitle("" + selected);

	}

}
