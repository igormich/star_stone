package org.jnity.starstone.gui;

import org.jnity.starstone.events.GameEvent;

import base.Scene;

public class Animation {
	
	private float time = 0;
	
	public static Animation createFor(StoredEvent event, Scene scene) {
		if(event.getType() == GameEvent.DRAW) {
			return new DrawAnimation(event.getCard(), scene);
		}
		if(event.getType() == GameEvent.PLAY) {
			return new PlayAnimation(event.getCard(), scene);
		}
		return new StubAnimation();
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
