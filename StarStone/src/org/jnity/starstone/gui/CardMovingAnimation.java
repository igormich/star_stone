package org.jnity.starstone.gui;

import org.lwjgl.util.vector.Vector3f;

import base.Object3d;
import base.Scene;

public class CardMovingAnimation extends Animation {
	
	protected Object3d card3d;
	protected Vector3f startTranslation;
	protected Vector3f endTranslation;

	public CardMovingAnimation(Object3d card3d) {
		this.card3d = card3d;
	}

	@Override
	public void play(float deltaTime, Scene scene) {
		super.play(deltaTime, scene);
		//card3d.getPosition().move(moveVector.x*deltaTime,moveVector.y*deltaTime,moveVector.z*deltaTime);
		float time =getTime();
		card3d.getPosition().setTranslation(startTranslation.x * (1- time) + endTranslation.x*time, 
											startTranslation.y * (1- time) + endTranslation.y*time,
											startTranslation.z * (1- time) + endTranslation.z*time);
		if(isFinished()) {
			System.out.println(time);
			System.out.println(card3d.getPosition().getTranslation());
		}
	}
}
