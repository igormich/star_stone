package org.jnity.starstone.gui;

import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Player;

import base.Scene;

public class PlayAnimation extends Animation {

	private List<GuiCard>  cards;
	public PlayAnimation(Card card, Scene scene, Player gamer) {
		int mirrow = 1;
		if (!card.getOwner().equals(gamer)) {
			mirrow = -1;
		}
		cards = new ArrayList<>();
		for(CreatureCard creature: card.getOwner().getCreatures()) {
			GuiCard guiCard = GuiCard.get(creature);
			cards.add(guiCard);
		}
		/*List<CreatureCard> creatures = card.getOwner().getCreatures();
		cards = new ArrayList<>();
		int i = 0;
		for(CreatureCard creature: creatures) {
			GuiCard guiCard = GuiCard.get(creature);
			float x= -creatures.size()/2 + i + (1 - creatures.size() % 2) * 0.5f;
			guiCard.startMoving(new Vector3f(2*x*mirrow,0,-2*mirrow));
			cards.add(guiCard);
			i++;
		}
		List<Card> hand = card.getOwner().getHand();
		i = 0;
		for(Card cardInHand: hand) {
			GuiCard guiCard = GuiCard.get(cardInHand);
			float x= -hand.size()/2f + i;
			guiCard.startMoving(new Vector3f(x*mirrow,0,-6.5f*mirrow));
			i++;
		}*/
		Animation.regroupCreatures(card.getOwner(), mirrow);
		Animation.regroupHand(card.getOwner(), mirrow);
	}
	@Override
	public boolean isFinished() {
		return cards.stream().allMatch(GuiCard::isMovingFinished);
	}
}
