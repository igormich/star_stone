package org.jnity.starstone.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.gui.MouseProcess.State;
import org.jnity.starstone.gui.shaders.CardShader;
import org.jnity.starstone.gui.shaders.CompactCreatureShader;
import org.jnity.starstone.gui.shaders.CreatureShader;
import org.jnity.starstone.gui.shaders.SimpleVertexShader;
import org.jnity.starstone.modifiers.Invisibility;
import org.jnity.starstone.modifiers.PlasmaShield;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import base.Object3d;
import base.RenderContex;
import jglsl.base.ShaderProcessor;
import materials.MaterialLibrary;
import materials.Shader;
import materials.SimpleMaterial;
import materials.Texture;
import materials.Texture2D;
import properties.Mesh;
import properties.Property3d;

public class GuiCard extends Object3d{

	private static Map<Card, GuiCard> card2card= new HashMap<>();
	private static Shader cardShader;
	private static Shader creatureShader;
	private static Mesh cardMesh;
	private static Mesh creatureMesh;
	private static Shader compactCreatureShader;
	
	public static MouseProcess mouseProcess;
	
	public static void init(MaterialLibrary materialLibrary) throws IOException {
		cardMesh = CardMeshBuilder.createCardMesh();
		creatureMesh = CardMeshBuilder.createCreatureMesh();
		creatureMesh.setMaterialName("compactCreatureShader");
		cardShader = ShaderProcessor.build(SimpleVertexShader.class, CardShader.class);
		creatureShader = ShaderProcessor.build(SimpleVertexShader.class, CreatureShader.class);
		compactCreatureShader = ShaderProcessor.build(SimpleVertexShader.class, CompactCreatureShader.class);
		Texture backGround = new Texture2D("s_protoss.png");
		cardShader.addTexture(backGround, "backTex");
		backGround = new Texture2D("protoss.png");
		creatureShader.addTexture(backGround, "backTex");
		backGround = new Texture2D("u_protoss.png");
		compactCreatureShader.addTexture(backGround, "backTex");
		Texture numbers = new Texture2D("numbers.png");
		cardShader.addTexture(numbers, "numbersTex");
		creatureShader.addTexture(numbers, "numbersTex");
		compactCreatureShader.addTexture(numbers, "numbersTex");
		
		cardShader.setBlendMode(SimpleMaterial.ALPHATEST50);
		compactCreatureShader.setBlendMode(SimpleMaterial.ALPHATEST50);
		//cardShader.setzWrite(false);
		creatureShader.setBlendMode(SimpleMaterial.ALPHATEST50);
		//creatureShader.setzWrite(false);
		
		Texture shadow = new Texture2D("eff_shadow.jpg");
		Texture shield = new Texture2D("eff_shield.jpg");
		compactCreatureShader.addTexture(shadow, "shadowTex");
		compactCreatureShader.addTexture(shield, "shieldTex");
		Texture target = new Texture2D("target.png");
		compactCreatureShader.addTexture(target, "target");
		materialLibrary.addMaterial("cardShader", cardShader);
		materialLibrary.addMaterial("creatureShader", creatureShader);
		materialLibrary.addMaterial("compactCreatureShader", compactCreatureShader);
	}
	
	private float time = 2;
	private Card card;
	private Texture2D faceTex;

	volatile protected Vector3f startTranslation;
	volatile protected Vector3f endTranslation;

	public GuiCard(Card card) {
		this.card = card;
		try {
			faceTex = new Texture2D(card.getID().toLowerCase() +".jpg", false);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		card2card.put(card, this);
		
		add(new Property3d() {
			
			@Override
			public Property3d fastClone() {
				return this;
			}

			@Override
			public void render(RenderContex renderContex, Object3d owner) {
				Card card = GuiCard.this.getCard();
				if(card instanceof CreatureCard) {
					CreatureCard cCard = (CreatureCard) card;
					cardMesh.setMaterialName("creatureShader");
					if(card.onDesk()) {
						float target = 0;
						if ((mouseProcess.state == State.SELECT_TARGET_FOR_ATACK) && (mouseProcess.selected.getCard().canAtack(cCard))) {
							target = 1;
						}
						if ((mouseProcess.state == State.PLAY_SPELL_WITH_TARGET) && (mouseProcess.selected.getCard().isValidTarget(cCard))) {
							target = 1;
						}
						if ((mouseProcess.state == State.SELECT_TARGET_FOR_BATLECRY) && (mouseProcess.creatureWithTarget.isValidTarget(cCard))) {
							target = 1;
						}
						compactCreatureShader.addTexture(faceTex, "faceTex");
						compactCreatureShader.setUniform(new Vector4f(target, 0, cCard.getPower(), cCard.getCurrentHits()), "stats");
						Vector4f modifiers = new Vector4f();
						if (card.getModifiers().stream().anyMatch(m -> m.getClass().equals(PlasmaShield.class)))
							modifiers.x = 0.5f;
						if (card.getModifiers().stream().anyMatch(m -> m.getClass().equals(Invisibility.class)))
							modifiers.y = 0.5f;
						compactCreatureShader.setUniform(modifiers, "modifiers");
						compactCreatureShader.setUniform(renderContex.getTime(), "time");
					} else {
						creatureShader.addTexture(faceTex, "faceTex");
						creatureShader.setUniform(new Vector4f(card.getPriceInMineral(), card.getPriceInGas(), cCard.getPower(), cCard.getCurrentHits()), "stats");
					}
				} else {
					cardMesh.setMaterialName("cardShader");
					cardShader.addTexture(faceTex, "faceTex");
					cardShader.setUniform(new Vector4f(card.getPriceInMineral(), card.getPriceInGas(), 0, 0), "stats");
				}
				if(card.onDesk()) {
					creatureMesh.render(renderContex, owner);
				} else {
					cardMesh.render(renderContex, owner);
				}
			}
			
		});
		setName(card.getName());
	}

	public static GuiCard get(Card card) {
		return card2card.get(card);
	}

	public boolean isMovingFinished() {
		return time > 1;
	}
	public float getTime() {
		return Math.min(time, 1);
	}
	@Override
	public void tick(float deltaTime, float globalTime) {
		super.tick(deltaTime, globalTime);
		time+=deltaTime;
		if(!isMovingFinished()) {
			getPosition().setTranslation(startTranslation.x * (1- getTime()) + endTranslation.x*getTime(), 
									 	 startTranslation.y * (1- getTime()) + endTranslation.y*getTime(),
									 	 startTranslation.z * (1- getTime()) + endTranslation.z*getTime());
		} else {
			if(endTranslation!=null) {
				getPosition().setTranslation(endTranslation);
			}
		}
	}

	public void startMoving(Vector3f start, Vector3f end) {
		startTranslation = start;
		endTranslation = end;
		time = 0;
	}

	public void startMoving(Vector3f vector3f) {
		startMoving(getPosition().getTranslation(), vector3f);
	}

	public Card getCard() {
		Card found = card.getGame().renewCard(card);
		if (found != null) {
			return found;
		}
		return card;
	}

	public static void all(Consumer<GuiCard> cardAction) {
		card2card.values().forEach(cardAction);
	}


}
