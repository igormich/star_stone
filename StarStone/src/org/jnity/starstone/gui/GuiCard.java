package org.jnity.starstone.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.gui.shaders.CardShader;
import org.jnity.starstone.gui.shaders.SimpleVertexShader;

import base.Object3d;
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
	
	
	private Card card;

	public GuiCard(Card card) {
		this.card = card;
		add(cardMesh);
		card2card.put(card, this);
	}

	public static void init(MaterialLibrary materialLibrary) throws IOException {
		cardMesh = PrimitiveFactory.createPlane(2.5f, 3.5f);
		cardMesh.setMaterialName("cardShader");
		cardShader = ShaderProcessor.build(SimpleVertexShader.class, CardShader.class);
		Texture backGround = new Texture2D("protoss.png");
		cardShader.addTexture(backGround, "backTex");
		Texture zealotTex = new Texture2D("zealot.jpg", false);
		cardShader.addTexture(zealotTex, "faceTex");
		cardShader.setBlendMode(SimpleMaterial.ALPHATEST50);
		materialLibrary.addMaterial("cardShader", cardShader);

		/*creatureShader = ShaderProcessor.build(SimpleVertexShader.class, CreatureShader.class);
		Texture backGround1 = new Texture2D("u_protoss.png");
		creatureShader.addTexture(backGround1, "backTex");
		creatureShader.addTexture(zealotTex, "faceTex");
		Texture rainTex = new Texture2D("rain.png");
		creatureShader.addTexture(rainTex, "movedTex");
		creatureShader.setBlendMode(SimpleMaterial.ALPHATEST50);
		materialLibrary.addMaterial("creatureShader", creatureShader);*/
	}

	public static Object3d get(Card card) {
		return card2card.get(card);
	}


}
