package org.jnity.starstone.gui;

import org.jnity.starstone.cards.Card;
import org.lwjgl.util.vector.Vector3f;

import base.Scene;

public class DrawAnimation extends Animation {

	private GuiCard card3d;

	public DrawAnimation(Card card, Scene scene) {
		card3d = new GuiCard(card);
		scene.add(card3d);
		int mirrow = 1;
		if (card.getGame().getTurnNumber()%2==0) {
			mirrow = -1;
			card3d.getPosition().turn(180);
		}
		card3d.startMoving(new Vector3f(5*mirrow, 0, 0), new Vector3f(0,0,-6.5f*mirrow));
	}
	@Override
	public boolean isFinished() {
		return card3d.isMovingFinished();
	}
}
