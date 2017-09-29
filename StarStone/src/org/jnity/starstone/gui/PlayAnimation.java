package org.jnity.starstone.gui;

import org.jnity.starstone.cards.Card;

import base.Object3d;
import base.Scene;

public class PlayAnimation extends Animation {

	private Object3d card3d;
	private int mirrow = 1;

	public PlayAnimation(Card card, Scene scene) {
		this.card3d = GuiCard.get(card);
		if (card.getGame().getTurnNumber()%2==0) {
			mirrow = -1;
		}
	}
	@Override
	public void play(float deltaTime, Scene scene) {
		super.play(deltaTime, scene);
		card3d.getPosition().move(0,0,5*deltaTime*mirrow);
	}
}
