package org.jnity.starstone.gui;

import static org.lwjgl.opengl.GL11.glColor3f;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.gui.shaders.CardShader;
import org.jnity.starstone.gui.shaders.SimpleVertexShader;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import base.Object3d;
import base.ObjectPosition;
import base.RenderContex;
import jglsl.base.ShaderProcessor;
import materials.MaterialLibrary;
import materials.Shader;
import materials.SimpleMaterial;
import materials.Texture;
import materials.Texture2D;
import properties.Mesh;
import utils.PrimitiveFactory;

public class GuiCard extends Object3d{

	private static Map<Card, GuiCard> card2card= new HashMap<>();
	private static Shader cardShader;
	private static Mesh cardMesh;
	
	
	public static void init(MaterialLibrary materialLibrary) throws IOException {
		cardMesh = PrimitiveFactory.createPlane(2.5f, 3.5f);
		cardMesh.setMaterialName("cardShader");
		cardShader = ShaderProcessor.build(SimpleVertexShader.class, CardShader.class);
		Texture backGround = new Texture2D("protoss.png");
		cardShader.addTexture(backGround, "backTex");
		Texture numbers = new Texture2D("numbers.png");
		cardShader.addTexture(numbers, "numbersTex");
		cardShader.setBlendMode(SimpleMaterial.ALPHATEST50);
		materialLibrary.addMaterial("cardShader", cardShader);
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
			new RuntimeException(e);
		}
		card2card.put(card, this);
	}
	
	@Override
	public void render(RenderContex renderContex) {
		if (!isVisible())
			return;
		((ObjectPosition)getPosition()).apply();
		if (renderContex.selectMode()) {
			int r = getID() & 0xff;
			int g = (getID() >> 8) & 0xff;
			int b = (getID() >> 16) & 0xff;
			glColor3f(r / 255f, g / 255f, b / 255f);
		} else {
			cardShader.addTexture(faceTex, "faceTex");
			if(card instanceof CreatureCard) {
				CreatureCard cCard = (CreatureCard) card;
				cardShader.setUniform(new Vector4f(card.getPriceInMineral(), card.getPriceInGas(), cCard.getPower(), cCard.getCurrentHits()), "stats");
			} else {
				cardShader.setUniform(new Vector4f(card.getPriceInMineral(), card.getPriceInGas(), 0, 0), "stats");
			}
			
		}
		cardMesh.render(renderContex, this);
		((ObjectPosition)getPosition()).unApply();
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


}
