package org.jnity.starstone.gui;

import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.modifiers.SummonSick;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import base.Camera;
import base.Object3d;
import base.Scene;
import io.ResourceController;
import properties.Mesh;
import properties.MultiMesh;
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
	private Object3d underCursor;
	private GuiCard selected;
	private Vector3f basePos;
	public Object3d endTurnButton;
	public Game game;
	public Player player;
	private Scene scene;
	private Camera camera;
	private Object3d place;
	private State state = State.WAIT;
	private GameGui gameGui;

	public MouseProcess(Scene scene, Camera camera, Game game, GameGui gameGui) {
		this.scene = scene;
		this.camera = camera;
		this.game = game;
		this.gameGui = gameGui;
		Mesh placeMesh = PrimitiveFactory.createPlane(14f, 3);
		place = scene.add(placeMesh);
		place.setVisible(false);
		place.setName("place for creatures");
		place.getPosition().setTranslation(0, 0, -2.8f);
		endTurnButton = scene.add(ResourceController.getOrCreate().getOrLoadMesh(new MultiMesh(), "cube.smd"));
		endTurnButton.getPosition().setTranslation(7, 0, 0).setScale(0.5f, 0.1f, 0.2f);
		endTurnButton.setName("endTurnButton");
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
			int i = 0;
			boolean skip = false;
			List<CreatureCard> creatures = player.getCreatures();
			for(CreatureCard creature: creatures) {
				GuiCard guiCard = GuiCard.get(creature);
				float fx = -creatures.size()/2 + i;
				if(GuiCard .get(creature).getPosition().getTranslation().x > selected.getPosition().getTranslation().x) {
					guiCard.startMoving(new Vector3f(2*fx+0.5f,0,-3));	
				} else
					guiCard.startMoving(new Vector3f(2*fx-0.5f,0,-3));
				i++;
			}
			GuiCard.all(c -> c.setVisible(false));
			place.setVisible(true);
		case PLAY_SPELL_WITH_TARGET:
		case SELECT_TARGET_FOR_ATACK:
			selected.setVisible(false);
			underCursor = scene.getObject(x, y, camera);
			GuiCard.all(c -> c.setVisible(true));
			place.setVisible(false);
		case PLAY_SPELL_WITHOUT_TARGET:
			Vector3f pos = new Vector3f();
			pos.x = ((float) x / Display.getWidth() - 0.5f) * 14;
			pos.y = -0.1f;
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
					case SELECT_TARGET_FOR_ATACK:
						atackTarget();
						break;
					default:
						state = State.WAIT;
						break;
					}
					selected = null;
				}
			}
		}
		//Display.setTitle(Objects.toString(underCursor));
	}

	private void atackTarget() {
		state = State.WAIT;
		selected.startMoving(basePos);
		if(underCursor instanceof GuiCard) {
			CreatureCard card = (CreatureCard) selected.getCard();
			Card target = ((GuiCard)underCursor).getCard();
			if(card.canAtack(target)) {
				new Thread(() -> 
					card.getGame().battle(card, (CreatureCard)target)
					).start();
			}
		}
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
			int i = 0;
			List<CreatureCard> creatures = player.getCreatures();
			for(CreatureCard creature: creatures) {
				GuiCard guiCard = GuiCard.get(creature);
				float x= -creatures.size()/2 + i;
				guiCard.startMoving(new Vector3f(2*x,0,-3));
				i++;
			}
		}
	}

	private void playOrAtack() {
		if(underCursor instanceof GuiCard) {
			GuiCard guiCard = (GuiCard) underCursor;
			Card card = guiCard.getCard();
			if (player.getHand().contains(card)) {
				playCard(guiCard, card);
			}
			if (player.getCreatures().contains(card)) {
				atack(guiCard, card);
			}
		}
	}

	private void atack(GuiCard guiCard, Card card) {
		if(player.canAtack(guiCard.getCard())) {
			selected =  guiCard;
			basePos = selected.getPosition().getTranslation();
			state = State.SELECT_TARGET_FOR_ATACK;
		} else {
			if(guiCard.getCard().getModifiers().stream().anyMatch(m -> m instanceof SummonSick)) {
				gameGui.setMessage(GameGui.SUMMON_SICK);
			} else {
				gameGui.setMessage(GameGui.CANT_ATACK);
			}
		}	
	}

	private void playCard(GuiCard guiCard, Card card) {
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
		} else {
			gameGui.setMessage(GameGui.NEED_MORE_RESOURSES);
		}
		
	}

	private void nextTurn() {
		new Thread(() -> {
			game.nextTurn();
			if (game.getActivePlayer().getHand().size() > 0) {
				Card c = game.getActivePlayer().getHand().get(0);
				if (game.getActivePlayer().canPlay(c) && !c.needTarget()) {
					game.getActivePlayer().play(game.getActivePlayer().getHand().get(0), null, 0);
				}
			}
			game.nextTurn();
		}).start();
	}

}
