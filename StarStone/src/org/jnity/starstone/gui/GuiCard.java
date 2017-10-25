package org.jnity.starstone.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.gui.shaders.CardShader;
import org.jnity.starstone.gui.shaders.CreatureShader;
import org.jnity.starstone.gui.shaders.SimpleVertexShader;
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
	
	public static void init(MaterialLibrary materialLibrary) throws IOException {
		cardMesh = CardMeshBuilder.createCardMesh();
		creatureMesh = CardMeshBuilder.createCreatureMesh();
		creatureMesh.setMaterialName("creatureShader");
		cardShader = ShaderProcessor.build(SimpleVertexShader.class, CardShader.class);
		creatureShader = ShaderProcessor.build(SimpleVertexShader.class, CreatureShader.class);
		Texture backGround = new Texture2D("s_protoss.png");
		cardShader.addTexture(backGround, "backTex");
		backGround = new Texture2D("protoss.png");
		creatureShader.addTexture(backGround, "backTex");
		Texture numbers = new Texture2D("numbers.png");
		cardShader.addTexture(numbers, "numbersTex");
		creatureShader.addTexture(numbers, "numbersTex");
		cardShader.setBlendMode(SimpleMaterial.ALPHATEST50);
		//cardShader.setzWrite(false);
		creatureShader.setBlendMode(SimpleMaterial.ALPHATEST50);
		//creatureShader.setzWrite(false);
		materialLibrary.addMaterial("cardShader", cardShader);
		materialLibrary.addMaterial("creatureShader", creatureShader);
	}
	
	private float time = 0;
	private Card card;
	private Texture2D faceTex;

	protected Vector3f startTranslation;
	protected Vector3f endTranslation;

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
				if (renderContex.useMaterial()) {
				
				if(card instanceof CreatureCard) {
					CreatureCard cCard = (CreatureCard) card;
					cardMesh.setMaterialName("creatureShader");
					creatureShader.addTexture(faceTex, "faceTex");
					creatureShader.setUniform(new Vector4f(card.getPriceInMineral(), card.getPriceInGas(), cCard.getPower(), cCard.getCurrentHits()), "stats");
					Vector4f modifiers = new Vector4f();
					if (card.getModifiers().stream().anyMatch(m -> m.getClass().equals(PlasmaShield.class)))
						modifiers.x = 1;
					creatureShader.setUniform(modifiers, "modifiers");
				} else {
					cardMesh.setMaterialName("cardShader");
					cardShader.addTexture(faceTex, "faceTex");
					cardShader.setUniform(new Vector4f(card.getPriceInMineral(), card.getPriceInGas(), 0, 0), "stats");
				}
				}
				//if(card.getOwner().getCreatures().contains(card)) {
				//	creatureMesh.render(renderContex, owner);
				//} else {
					cardMesh.render(renderContex, owner);
				//}
				//creatureMesh.render(renderContex, owner);//remove
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
			getPosition().setTranslation(endTranslation);
		}
	}

	public void startMoving(Vector3f start, Vector3f end) {
		time = 0;
		startTranslation = start;
		endTranslation = end;
	}

	public void startMoving(Vector3f vector3f) {
		startMoving(getPosition().getTranslation(), vector3f);
	}

	public Card getCard() {
		return card;
	}

	public static void all(Consumer<GuiCard> cardAction) {
		card2card.values().forEach(cardAction);
	}


}
