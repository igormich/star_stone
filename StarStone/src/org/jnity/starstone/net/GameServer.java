package org.jnity.starstone.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.GameException;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;
import org.jnity.starstone.gui.StoredEvent;
import org.jnity.starstone.nerazim.creatures.Centurion;
import org.jnity.starstone.nerazim.creatures.DarkStalker;
import org.jnity.starstone.protoss.creatures.ShieldRecharge;
import org.jnity.starstone.protoss.creatures.ShildBattery;
import org.jnity.starstone.protoss.creatures.Zealot;

public class GameServer extends Thread implements GameListener {

	private Game game;
	private ObjectOutputStream oos1;
	private ObjectInputStream ois1;
	private ObjectOutputStream oos2;
	private ObjectInputStream ois2;

	public GameServer(ObjectOutputStream oos1, ObjectInputStream ois1, ObjectOutputStream oos2,
			ObjectInputStream ois2) {
		this.oos1 = oos1;
		this.ois1 = ois1;
		this.oos2 = oos2;
		this.ois2 = ois2;
	}

	public void run() {
		ServerSocket ss;
		try {
			//Player firstPlayer = (Player) ois1.readObject();
			//Player secondPlayer = (Player) ois2.readObject();
			String name1 = (String) ois1.readObject();
			int deck1 = ois1.readInt();
			String name2 = (String) ois2.readObject();
			int deck2 = ois2.readInt();
			Player firstPlayer = new Player(name1, getDeckFor(name1,deck1));
			Player secondPlayer = new Player(name2, getDeckFor(name2,deck2));
			game = new Game(firstPlayer, secondPlayer);
			game.addListener(this);
			game.nextTurn();
			ObjectInputStream activeClient = ois1;
			while (true) {
				NetActions action = (NetActions) activeClient.readObject();
				try {
				switch (action) {
				case END_OF_TURN:
					game.nextTurn();
					activeClient = activeClient == ois1 ? ois2 : ois1;
					activeClient.skip(100000);
					continue;
				case PLAY:
					Integer cardId = (Integer) activeClient.readObject();
					Integer targetId = (Integer) activeClient.readObject();
					Integer position = (Integer) activeClient.readObject();
					Card card = game.getAll().stream().filter(c -> c.getSerial() == cardId).findAny().get();
					Card target = null;
					if (targetId != null) {
						target = game.getAllCreaturesAndPlayers().stream().filter(c -> c.getSerial() == targetId)
								.findAny().get();
					}
					game.getActivePlayer().play(card, (CreatureCard) target, position);
					break;
				case BATTLE:
					cardId = (Integer) activeClient.readObject();
					targetId = (Integer) activeClient.readObject();
					card = game.getAll().stream().filter(c -> c.getSerial() == cardId).findAny().get();
					target = game.getAllCreaturesAndPlayers().stream().filter(c -> c.getSerial() == targetId).findAny()
							.get();
					game.battle((CreatureCard) card, (CreatureCard) target);
					break;
				default:
					break;
				}
				}
				catch (GameException e) {
					//Player make some wrong
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private List<Card> getDeckFor(String name1, int deckName) {
		List<Card> deck1 = new ArrayList<>();
		
		deck1.add(new ShildBattery());
		deck1.add(new ShildBattery());
		deck1.add(new Zealot());
		deck1.add(new DarkStalker());
		deck1.add(new ShieldRecharge());
		deck1.add(new Centurion());
		deck1.add(new Zealot());
		return deck1;
	}

	public void on(GameEvent gameEvent, Card card, CreatureCard target) {

		StoredEvent event = new StoredEvent(gameEvent, card, target, game);
		game.removeListener(this);
		try {
			oos1.writeObject(event);
			oos1.flush();
			oos1.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			oos2.writeObject(event);
			oos2.flush();
			oos2.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		game.addListener(this);
	}
}
