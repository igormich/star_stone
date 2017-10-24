package org.jnity.starstone.gui;

import java.util.List;
import java.util.Objects;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import base.Camera;
import base.Object3d;
import base.Scene;
import properties.Mesh;
import utils.PrimitiveFactory;

public class MouseProcess {
	private static enum State {
		WAIT,
		PLAY_CREATURE,
		SELECT_TARGET_FOR_BATLECRY,
		PLAY_SPELL_WITHOUT_TARGET,
		PLAY_SPELL_WITH_TARGET,
		SELECT_TARGET_FOR_ATACK
		
	}
	private static Object3d underCursor;
	private static GuiCard selected;
	private static Vector3f basePos;
	public static Object3d endTurnButton;
	public static Game game;
	public static Player player;
	private Scene scene;
	private Camera camera;
	private Object3d place;
	private State state = State.WAIT;

	public MouseProcess(Scene scene, Camera camera) {
		this.scene = scene;
		this.camera = camera;
		Mesh placeMesh = PrimitiveFactory.createPlane(14f, 3);
		place = scene.add(placeMesh);
		place.getPosition().setTranslation(0, 0, -2.8f);
	}

	public void tick() {
		boolean canTouch = game.getActivePlayer().equals(player);
		if(!canTouch)
			return;
		int x = Mouse.getX();
		int y = Mouse.getY();
		switch (state) {
		case WAIT:
			underCursor = scene.getObject(x, y, camera);
			break;
		case PLAY_CREATURE:
			GuiCard.all(c -> c.setVisible(false));
		case PLAY_SPELL_WITH_TARGET:
			selected.setVisible(false);
			underCursor = scene.getObject(x, y, camera);
			GuiCard.all(c -> c.setVisible(true));
		case PLAY_SPELL_WITHOUT_TARGET:
			Vector3f pos = new Vector3f();
			pos.x = ((float) x / Display.getWidth() - 0.5f) * 14;
			pos.y = 0;
			pos.z = ((float) y / Display.getHeight() - 0.5f) * 10;
			((GuiCard) selected).startMoving(pos, pos);
			break;
		default:
			break;
		}
		while (Mouse.next()) {
			if (Mouse.getEventButton() > -1) {
				if (Mouse.getEventButtonState()) {
					if (endTurnButton.equals(underCursor)) {
						nextTurn();
						return;
					}
					switch (state) {
					case WAIT:
						playOrAtack();
						break;
					case SELECT_TARGET_FOR_BATLECRY:
						break;
					default:
						break;
					}
					underCursor = null;
				} else {//Mouse up
					switch (state) {
					case PLAY_CREATURE:
						playCreature();
						break;
					case PLAY_SPELL_WITHOUT_TARGET:
						playSpellWithoutTarget();
						break;
					case PLAY_SPELL_WITH_TARGET:
						playSpellWithTarget();
						break;
					default:
						break;
					}
					selected = null;
				}
			}
		}
		Display.setTitle(Objects.toString(underCursor));
	}

	private void playSpellWithTarget() {
		state = State.WAIT;
		if(underCursor instanceof GuiCard) {
			Card card = selected.getCard();
			Card target = ((GuiCard)underCursor).getCard();
			if(selected.getCard().isValidTarget(target)) {
				new Thread(() -> 
					player.play(card, (CreatureCard) target, -1)
					).start();
				scene.remove(selected);
			} else {
				selected.startMoving(basePos);
			}
		} else {
			selected.startMoving(basePos);
		}
	}

	private void playSpellWithoutTarget() {
		state = State.WAIT;
		if (selected.getPosition().getTranslation().z>-4) {//TODO:fix
			new Thread(() -> player.play(selected.getCard(), null, -1)).start();
		} else {
			selected.startMoving(basePos);
		}
	}

	private void playCreature() {
		state = State.WAIT;
		if (place.equals(underCursor)) {
			List<CreatureCard> creatures = player.getCreatures();
			int place = 0;
			for(CreatureCard creatureCard: creatures) {
				if(GuiCard .get(creatureCard).getPosition().getTranslation().x > selected.getPosition().getTranslation().x)
					break;
				place++;
			}
			int p = place;
			Card card = selected.getCard();
			new Thread(() -> player.play(card, null, p)).start();
		} else {
			selected.startMoving(basePos);
		}
	}

	private void play() {
		
	}

	private void playOrAtack() {
		if(underCursor instanceof GuiCard) {
			GuiCard guiCard = (GuiCard) underCursor;
			Card card = guiCard.getCard();
			if(player.canPlay(card)) {
				selected = guiCard;
				basePos = selected.getPosition().getTranslation();
				if(card instanceof SpellCard) {
					if(card.needTarget())
						state = State.PLAY_SPELL_WITH_TARGET;
					else 
						state = State.PLAY_SPELL_WITHOUT_TARGET;
				} else {
					state = State.PLAY_CREATURE;
				}
			}
			if(player.canAtack(guiCard.getCard())) {
				selected =  guiCard;
				state = State.SELECT_TARGET_FOR_ATACK;
			}
		}
	}

	private void nextTurn() {
		new Thread(() -> {
			game.nextTurn();
			if(game.getActivePlayer().getHand().size()>0 && game.getActivePlayer().canPlay(game.getActivePlayer().getHand().get(0)))
				 game.getActivePlayer().play(game.getActivePlayer().getHand().get(0),null,0);	
			game.nextTurn();
		}).start();
	}

}
