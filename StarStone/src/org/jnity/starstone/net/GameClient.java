package org.jnity.starstone.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.gui.StoredEvent;

public class GameClient extends Game implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7020518546725689733L;
	private String host;
	private Player player;
	private ObjectOutputStream oos;

	public GameClient(Player player, String host) throws UnknownHostException, IOException, ClassNotFoundException {
		this.player = player;
		this.host = host;
	}

	private void assigneToThis(Game game) {
		players = (List<Player>) game.getPlayers();
		getPlayers().forEach(p -> p.setGame(this));
		turnNumber.set(game.getTurnNumber());
		activePlayer = game.getActivePlayer();
	}

	public void start() {
		Thread runner = new Thread(this);
		runner.setDaemon(true);
		runner.start();
	}

	@Override
	public void run() {
		try (Socket socket = new Socket(host, 666);) {
			oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			oos.flush();
			oos.writeObject(player);
			oos.flush();
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			while (true) {
				// if(ois.available()>0) {
				try {
					StoredEvent event = (StoredEvent) ois.readObject();
					Game game = event.getGame();
					assigneToThis(game);
					super.emit(event.getType(), event.getCard(), event.getTarget());
					// }
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
					System.out.println(player.getID());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(player.getID());
		}
	}

	@Override
	public void play(Card card, CreatureCard target, int placePosition) {
		try {
			oos.writeObject(NetActions.PLAY);
			oos.writeObject(card.getSerial());
			oos.writeObject(target != null ? target.getSerial() : null);
			oos.writeObject(placePosition);
			oos.flush();
			oos.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void battle(CreatureCard card, CreatureCard target) {
		try {
			oos.writeObject(NetActions.BATTLE);
			oos.writeObject(card.getSerial());
			oos.writeObject(target.getSerial());
			oos.flush();
			oos.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void nextTurn() {
		try {
			oos.writeObject(NetActions.END_OF_TURN);
			oos.flush();
			oos.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void emit(GameEvent gameEvent, Card card, CreatureCard target) {
	}

	@Override
	public void emit(GameEvent gameEvent, Card card) {
	}

}
