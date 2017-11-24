package org.jnity.starstone.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
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
		players.clear();
		players.addAll(game.getPlayers());
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
		try {
			Socket socket = new Socket(host, 666);
			oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			oos.flush();
			oos.writeObject(player);
			oos.flush();
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			while (true) {
				// if(ois.available()>0) {
				try {
					StoredEvent event = (StoredEvent) ois.readObject();
					Game game = (Game) ois.readObject();
					
					assigneToThis(game);
					this.emit(event.getType(), event.getCard(), event.getTarget());
					// }
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
					System.out.println(player.getID());
				}
			}
		} catch (IOException  e) {
			e.printStackTrace();
			System.out.println(player.getID());
		}
	}
	public void play(Card card, CreatureCard target, int placePosition) {
		try {
			oos.writeObject(card.getSerial());
			oos.writeObject(target!=null?target.getSerial():null);
			oos.writeObject(placePosition);
			oos.flush();
			oos.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
