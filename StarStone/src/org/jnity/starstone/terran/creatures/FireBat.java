package org.jnity.starstone.terran.creatures;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;

public class FireBat extends Human implements GameListener {

	private boolean splashDamage = false;
	public FireBat() {
		super("FIREBAT", 2, 1, 2, 3);
	}
    @Override
    public void play(CreatureCard target){
        super.play(target);
        getGame().addListener(this);
    }
	@Override
	public void on(GameEvent gameEvent, Card card, CreatureCard target) {
		if(gameEvent == GameEvent.TAKE_DAMAGE && this.equals(card) && this.getGame().getActivePlayer().equals(getOwner()) && !splashDamage ) {
			splashDamage = true;
			target.getOwner().getCreaturesNear(target).forEach(c->c.takeDamage(getPower(), this));
		}
		splashDamage = false;
	}
}
