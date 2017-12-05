package org.jnity.starstone.gui;

import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.cards.SpellCard;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.modifiers.CombatFatigue;
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
	public static enum State {
		WAIT, PLAY_CREATURE, SELECT_TARGET_FOR_BATLECRY, PLAY_SPELL_WITHOUT_TARGET, PLAY_SPELL_WITH_TARGET, SELECT_TARGET_FOR_ATACK
	}

	public Object3d underCursor;
	public GuiCard selected;
	private Vector3f basePos;
	public Object3d endTurnButton;
	public Game game;
	public String player;
	public Scene scene;
	private Camera camera;
	private Object3d place;
	public State state = State.WAIT;
	private GameGui gameGui;
	private int placePosition;
	public CreatureCard creatureWithTarget;

	public MouseProcess(Scene scene, Camera camera, Game game, GameGui gameGui) {
		this.scene = scene;
		this.camera = camera;
		this.game = game;
		this.gameGui = gameGui;
		Mesh placeMesh = PrimitiveFactory.createPlane(14f, 3);
		place = scene.add(placeMesh);
		place.setVisible(false);
		place.setName("place for creatures");
		place.getPosition().setTranslation(0, 0, -1.4f);
		endTurnButton = scene.add(ResourceController.getOrCreate().getOrLoadMesh(new MultiMesh(), "cube.smd"));
		endTurnButton.getPosition().setTranslation(7, 0, 0).setScale(0.5f, 0.1f, 0.2f);
		endTurnButton.setName("endTurnButton");
	}

	public void tick() {
		boolean canTouch = game.getActivePlayer().getID().equals(player);
		if (!canTouch)
			return;
		int x = Mouse.getX();
		int y = Mouse.getY();
		switch (state) {
		case WAIT:
			Object3d oldUnderCursor = underCursor;
			underCursor = scene.getObject(x, y, camera);
			if (oldUnderCursor != underCursor) {
				if (oldUnderCursor instanceof GuiCard) {
					oldUnderCursor.getPosition().setScale(1);
					GuiCard guiCard = (GuiCard) oldUnderCursor;
					Card card = guiCard.getCard();
					if (!card.onDesk() && card.getOwner().getID().equals(player)) {
						List<Card> hand = card.getOwner().getHand();
						int i = hand.indexOf(card);
						float xx = -hand.size() / 2f + i;
						guiCard.startMoving(new Vector3f(xx, 0, -6.5f));
					}
				}
				if (underCursor instanceof GuiCard) {
					GuiCard guiCard = (GuiCard) underCursor;
					Card card = guiCard.getCard();
					if (card.onDesk() || card.getOwner().getID().equals(player)) {
						underCursor.getPosition().setScale(1.2f);
					}
					if (!card.onDesk() && card.getOwner().getID().equals(player)) {
						List<Card> hand = card.getOwner().getHand();
						int i = hand.indexOf(card);
						float xx = -hand.size() / 2f + i;
						guiCard.startMoving(new Vector3f(xx, -1, -3.3f));
					}
				}
			}

			break;
		case PLAY_CREATURE:
			int i = 0;
			List<CreatureCard> creatures = game.getPlayerByID(player).getCreatures();
			for (CreatureCard creature : creatures) {
				GuiCard guiCard = GuiCard.get(creature);
				float fx = -creatures.size() / 2 + i;
				if (GuiCard.get(creature).getPosition().getTranslation().x > selected.getPosition()
						.getTranslation().x) {
					guiCard.startMoving(new Vector3f(2 * fx + 0.5f, 0, -2));
				} else
					guiCard.startMoving(new Vector3f(2 * fx - 0.5f, 0, -2));
				i++;
			}
			GuiCard.all(c -> c.setVisible(false));
			place.setVisible(true);
		case PLAY_SPELL_WITH_TARGET:
		case SELECT_TARGET_FOR_ATACK:
			selected.setVisible(false);
		case SELECT_TARGET_FOR_BATLECRY:
			underCursor = scene.getObject(x, y, camera);
			GuiCard.all(c -> c.setVisible(true));
			place.setVisible(false);
		case PLAY_SPELL_WITHOUT_TARGET:
			if (selected == null)
				break;
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
						playCreatureWithTarget();
						break;
					default:
						break;
					}
					underCursor = null;
				} else {// Mouse up
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
		// Display.setTitle(Objects.toString(underCursor));
	}

	private void playCreatureWithTarget() {
		state = State.WAIT;
		if (underCursor instanceof GuiCard) {
			Card card = ((GuiCard) underCursor).getCard();
			if (card instanceof CreatureCard) {
				CreatureCard target = (CreatureCard) card;
				if (creatureWithTarget.isValidTarget(target)) {
					CreatureCard creatureCard = creatureWithTarget;
					new Thread(() -> game.play(creatureCard, target, placePosition)).start();
					return;
				}
			}
		}
		GuiCard.get(creatureWithTarget).startMoving(basePos);
		Animation.regroupCreatures(game.getPlayerByID(player), 1);
		/*int i = 0;
		List<CreatureCard> creatures = game.getPlayerByID(player).getCreatures();
		for (CreatureCard creature : creatures) {
			GuiCard guiCard = GuiCard.get(creature);
			float x = -creatures.size() / 2 + i;
			guiCard.startMoving(new Vector3f(2 * x, 0, -2));
			i++;
		}*/

	}

	private void atackTarget() {
		state = State.WAIT;
		selected.startMoving(basePos);
		if (underCursor instanceof GuiCard) {
			CreatureCard card = (CreatureCard) selected.getCard();
			Card target = ((GuiCard) underCursor).getCard();
			if (target instanceof CreatureCard) {
				CreatureCard creatureTarget = (CreatureCard) target;
				if (card.canAtack(creatureTarget)) {
					new Thread(() -> card.getGame().battle(card, creatureTarget)).start();
				}
			}
		}
	}

	private void playSpellWithTarget() {
		state = State.WAIT;
		if (underCursor instanceof GuiCard) {
			Card card = selected.getCard();
			Card target = ((GuiCard) underCursor).getCard();
			if (selected.getCard().isValidTarget(target)) {
				new Thread(() -> game.play(card, (CreatureCard) target, -1)).start();
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
		if (selected.getPosition().getTranslation().z > -4) {// TODO:fix
			Card c = selected.getCard();
			new Thread(() -> game.play(c, null, -1)).start();
			scene.remove(selected);
		} else {
			selected.startMoving(basePos);
		}
	}

	private void playCreature() {
		state = State.WAIT;
		if (place.equals(underCursor)) {
			List<CreatureCard> creatures = game.getPlayerByID(player).getCreatures();
			int p = 0;
			for (CreatureCard creatureCard : creatures) {
				if (GuiCard.get(creatureCard).getPosition().getTranslation().x > selected.getPosition()
						.getTranslation().x)
					break;
				p++;
			}
			placePosition = p;
			CreatureCard card = (CreatureCard) selected.getCard();
			creatureWithTarget = card;
			if (card.needTarget() && game.getAllCreaturesAndPlayers().stream().anyMatch(t -> card.isValidTarget(t))) {
				state = State.SELECT_TARGET_FOR_BATLECRY;
			} else {
				new Thread(() -> game.play(card, null, placePosition)).start();
			}
		} else {
			selected.startMoving(basePos);
			Animation.regroupCreatures(game.getPlayerByID(player), 1);
			/*(int i = 0;
			List<CreatureCard> creatures = game.getPlayerByID(player).getCreatures();
			for (CreatureCard creature : creatures) {
				GuiCard guiCard = GuiCard.get(creature);
				float x= -creatures.size()/2 + i + (1 - creatures.size() % 2) * 0.5f;
				guiCard.startMoving(new Vector3f(2 * x, 0, -2));
				i++;
			}*/
		}
	}

	private void playOrAtack() {
		if (underCursor instanceof GuiCard) {
			GuiCard guiCard = (GuiCard) underCursor;
			Card card = guiCard.getCard();
			if (game.getPlayerByID(player).getHand().contains(card)) {
				playCard(guiCard, card);
			}
			if (game.getPlayerByID(player).getCreatures().contains(card)) {
				atack(guiCard, card);
			}
			underCursor.getPosition().setScale(1);
		}
	}

	private void atack(GuiCard guiCard, Card card) {
		if (game.getPlayerByID(player).canAtack(guiCard.getCard())) {
			selected = guiCard;
			basePos = selected.getPosition().getTranslation();
			state = State.SELECT_TARGET_FOR_ATACK;
		} else {
			if (guiCard.getCard().getModifiers().stream().anyMatch(m -> m instanceof SummonSick)) {
				gameGui.setMessage(GameGui.SUMMON_SICK);
			} else if (guiCard.getCard().getModifiers().stream().anyMatch(m -> m instanceof CombatFatigue)) {
				gameGui.setMessage(GameGui.AFTER_ATACK);
			} else {
				gameGui.setMessage(GameGui.CANT_ATACK);
			}
		}
	}

	private void playCard(GuiCard guiCard, Card card) {
		if (game.getPlayerByID(player).canPlay(card)) {
			selected = guiCard;
			basePos = selected.getPosition().getTranslation();
			if (card instanceof SpellCard) {
				if (card.needTarget())
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
			/*
			 * if (game.getActivePlayer().getHand().size() > 0) { Card c =
			 * game.getActivePlayer().getHand().get(0); if
			 * (game.getActivePlayer().canPlay(c) && !c.needTarget()) {
			 * game.getActivePlayer().play(game.getActivePlayer().getHand().get(
			 * 0), null, 0); } }
			 */
			// game.nextTurn();
		}).start();
	}

}
