package org.jnity.starstone.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;
import org.jnity.starstone.gui.StoredEvent;

public class GameServer extends Thread implements GameListener {

	private ObjectOutputStream oos1;
	private ObjectOutputStream oos2;
	private Game game;

	public void run() {
		ServerSocket ss;
		try {
			ss = new ServerSocket(666);
			Socket firstClient = ss.accept();//firstPlayer
			oos1 = new ObjectOutputStream(new BufferedOutputStream(firstClient.getOutputStream()));
			oos1.flush();
			Socket secondClient = ss.accept();//secondPlayer

			oos2 = new ObjectOutputStream(new BufferedOutputStream(secondClient.getOutputStream()));
			oos2.flush();
			ObjectInputStream ois1 = new ObjectInputStream(new BufferedInputStream(firstClient.getInputStream()));
			Player firstPlayer = (Player) ois1.readObject();
			ObjectInputStream ois2 = new ObjectInputStream(new BufferedInputStream(secondClient.getInputStream()));
			Player secondPlayer = (Player) ois2.readObject();
			game = new Game(firstPlayer, secondPlayer);
			game.addListener(this);
			game.nextTurn();
			while(true){
				Integer cardId = (Integer) ois1.readObject();
				Integer targetId = (Integer) ois1.readObject();
				Integer position = (Integer) ois1.readObject();
				Card card = game.getAll().stream().filter(c -> c.getSerial() == cardId).findAny().get();
				Card target = null;
				if(targetId != null) {
					target = game.getAllCreaturesAndPlayers().stream().filter(c -> c.getSerial() == targetId).findAny().get();
				}
				game.getActivePlayer().play(card,(CreatureCard) target,position);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void on(GameEvent gameEvent, Card card, CreatureCard target) {
		try {
			StoredEvent event = new StoredEvent(gameEvent, card, target);
			game.removeListener(this);
			oos1.writeObject(event);
			oos1.writeObject(game);
			oos1.flush();
			oos1.reset();
			
			oos2.writeObject(event);
			oos2.writeObject(game);
			oos2.flush();
			oos2.reset();
			game.addListener(this);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
