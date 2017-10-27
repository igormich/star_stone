package org.jnity.starstone.zerg.creatures;

import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Debug;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;

public class Zergling extends CreatureCard implements GameListener{

    private static final long serialVersionUID = 2196076382842048671L;
    private  boolean split = true;

    public Zergling() {
        super("ZERGLING", 0, 0, 2, 1);
    }

    @Override
    public void play(CreatureCard target) {
        super.play(target);
        getGame().addListener(this);
    }

    @Override
    public void on(GameEvent gameEvent, Card card) {
        if(GameEvent.END_OF_TURN == gameEvent && card.equals(getOwner())) {
            List creatures = getOwner().getCreatures();
            if (split && creatures.size() < 7) {
            getOwner().putCreature(new Zergling(), creatures.lastIndexOf(this));
            split = false;  // Один или несколько зергов.
                Debug.print("add new zergling");
            }
        }
    }
}
