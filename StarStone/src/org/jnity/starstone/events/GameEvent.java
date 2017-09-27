package org.jnity.starstone.events;

public enum GameEvent {
	GAME_BEGIN, END_OF_TURN, NEW_TURN,//global game events
	DRAW, PLAY,//card events
	ATACKS, DEFENDED,
	TAKE_DAMAGE, GIVE_DAMAGE, DIES, HEALED,//player and creatures events
}
