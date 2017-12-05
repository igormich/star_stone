package org.jnity.starstone.events;

public enum GameEvent {
	NEW_GAME,
	GAME_BEGIN, END_OF_TURN, NEW_TURN,//global game events
	DRAW, PLAY, PUT,//card events
	ATACKS, DEFENDED,
	TAKE_DAMAGE, GIVE_DAMAGE, DIES, HEALED,//player and creatures events
}
