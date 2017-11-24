package org.jnity.starstone.core;

import java.io.Serializable;

public class GamePart implements Serializable{
	
	private Game game;

	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
}
