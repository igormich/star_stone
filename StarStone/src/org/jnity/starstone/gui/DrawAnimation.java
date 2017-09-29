package org.jnity.starstone.gui;

import org.jnity.starstone.cards.Card;

import base.Object3d;
import base.Scene;

public class DrawAnimation extends Animation {

	private Object3d card3d;
	private int mirrow = 1;

	public DrawAnimation(Card card, Scene scene) {
		this.card3d = scene.add(new GuiCard(card));
		if (card.getGame().getTurnNumber()%2==0) {
			mirrow = -1;
			card3d.getPosition().turn(180);
		}
		System.out.println(card.getGame().getTurnNumber());
		System.out.println(mirrow);
		card3d.getPosition().setTranslation(5*mirrow, 0, 0);
		
	}
	@Override
	public void play(float deltaTime, Scene scene) {
		super.play(deltaTime, scene);
		card3d.getPosition().move(-5*deltaTime*mirrow,0,-6*deltaTime*mirrow);
	}
}
