package org.jnity.starstone.gui;

import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Player;
import org.lwjgl.util.vector.Vector3f;

import base.Scene;

public class PlayAnimation extends Animation {

	private List<GuiCard>  cards;
	public PlayAnimation(Card card, Scene scene, Player gamer) {
		int mirrow = 1;
		if (!card.getOwner().equals(gamer)) {
			mirrow = -1;
		}
		List<CreatureCard> creatures = card.getOwner().getCreatures();
		cards = new ArrayList<>();
		int i = 0;
		for(CreatureCard creature: creatures) {
			GuiCard guiCard = GuiCard.get(creature);
			float x= -creatures.size()/2 + i;
			System.out.println(x);
			guiCard.startMoving(new Vector3f(2*x*mirrow,0,-3*mirrow));
			cards.add(guiCard);
			i++;
		}
		List<Card> hand = card.getOwner().getHand();
		i = 0;
		for(Card cardInHand: hand) {
			GuiCard guiCard = GuiCard.get(cardInHand);
			float x= -hand.size()/2f + i;
			System.out.println(new Vector3f(x*mirrow,0,-6.5f*mirrow));
			guiCard.startMoving(new Vector3f(x*mirrow,0,-6.5f*mirrow));
			i++;
		}
	}
	@Override
	public boolean isFinished() {
		return cards.stream().allMatch(GuiCard::isMovingFinished);
	}
}
