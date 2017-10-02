package org.jnity.starstone.gui;

import org.jnity.starstone.core.Game;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import base.Camera;
import base.Object3d;
import base.Scene;

public class MouseProcess {
	private static Object3d underCursor;
	private static Object3d selected;
	private static Vector3f basePos;
	public static Object3d endTurnButton;
	public static Game game;
	public static void tick(Scene scene, Camera camera) {
		int x = Mouse.getX();
		int y = Mouse.getY();
		underCursor = scene.getObject(x, y, camera);
		if(selected!=null) {
			Vector3f pos = new Vector3f();
			pos.x = ((float)x/Display.getWidth() - 0.5f)*14;
			pos.y = 0;
			pos.z = ((float)y/Display.getHeight() - 0.5f)*10;
			((GuiCard)selected).startMoving(pos,pos);
		}
		while(Mouse.next()) {
		    if (Mouse.getEventButton() > -1) {
		        if (Mouse.getEventButtonState()) {
		        	if((underCursor!=null)&&(underCursor.equals(endTurnButton))) {
		        		new Thread(() ->game.nextTurn()).start();
		        		return;
		        	}
		        	selected = underCursor;
		        	if(selected!=null)
		        		basePos = selected.getPosition().getTranslation();
		        }
		        else {
		        	if(selected!=null) {
		        		((GuiCard)selected).startMoving(basePos);
		        	}
		        	selected = null;
		        }
		    }
		}
		Display.setTitle("" + selected);
		
		
	}


}
