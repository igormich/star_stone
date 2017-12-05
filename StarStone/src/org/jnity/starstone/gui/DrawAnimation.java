package org.jnity.starstone.gui;

import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.core.Player;
import org.lwjgl.util.vector.Vector3f;

import base.Scene;

public class DrawAnimation extends Animation {

	private GuiCard card3d;

	public DrawAnimation(Card card, Scene scene, Player gamer) {
		card3d = new GuiCard(card);
		scene.add(card3d);
		int mirrow = 1;
		if (!card.getOwner().equals(gamer)) {
			mirrow = -1;
			//card3d.getPosition().turn(180);
		}
		card3d.getPosition().setTranslation(new Vector3f(5*mirrow, 0, -3*mirrow));
		List<Card> hand = card.getOwner().getHand();
		int i = 0;
		for(Card cardInHand: hand) {
			GuiCard guiCard = GuiCard.get(cardInHand);
			float x= -hand.size()/2f + i;
			guiCard.startMoving(new Vector3f(x*mirrow,0,-6.5f*mirrow));
			i++;
		}
		//card3d.startMoving(new Vector3f(5*mirrow, 0, 0), new Vector3f(0,0,-6.5f*mirrow));
	}
	@Override
	public boolean isFinished() {
		return card3d.isMovingFinished();
	}
}
