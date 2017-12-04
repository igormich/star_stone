package org.jnity.starstone.zerg.creatures;

import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Debug;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;
import org.jnity.starstone.modifiers.Buff;

public class Queen extends CreatureCard implements GameListener{

    private static final long serialVersionUID = 3841682324680558710L;
    private static final int attackBuff = 1;

    public Queen() {
        super("QUEEN", 0, 0, 5, 3);
    }

    @Override
    public void play(CreatureCard target){
        super.play(target);
        getGame().addListener(this);
        List<CreatureCard> creatures = getOwner().getCreatures();
        for(int i = 0; i < creatures.size(); i++){
            if(!creatures.get(i).equals(this))
                new Buff(creatures.get(i), attackBuff, 0);
        }
        Debug.print("give attackBuff");
    }

    @Override
    public void on(GameEvent gameEvent, Card card, CreatureCard nothing) {

        if(GameEvent.PLAY == gameEvent
                && card instanceof CreatureCard
                && card.getOwner().equals(this.getOwner())){

            new Buff((CreatureCard) card, attackBuff, 0);
        }

        if(GameEvent.DIES == gameEvent && card.equals(this)){
            List<CreatureCard> creatures = getOwner().getCreatures();

            for(int i = 0; i < creatures.size(); i++) {
                for (int j = 0; j < creatures.get(i).getModifiers().size(); j++) {
                    if (creatures.get(i).getModifiers().get(j) instanceof Buff){
                        creatures.get(i).removeModifier(creatures.get(i).getModifiers().get(j));
                        Debug.print("TurnAttackBuff disable");
                        break;
                    }
                }
            }
        }
    }
}
