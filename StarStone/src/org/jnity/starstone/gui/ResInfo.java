package org.jnity.starstone.gui;

import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;

import base.Object3d;
import base.Scene;
import materials.SimpleMaterial;
import materials.TexturedMaterial;
import properties.Mesh;
import utils.PrimitiveFactory;

public class ResInfo extends Object3d implements GameListener {

	private Game game;

	List<Object3d> minerals = new ArrayList<Object3d>();

	private String player; 
	public ResInfo(Game game, Scene scene, String playerID) {
		
		this.game = game;
		this.player = playerID;
		this.game.addListener(this);
		
		scene.getMaterialLibrary().addMaterial("minerals_ico", new TexturedMaterial("minerals.png")).setBlendMode(SimpleMaterial.TRANSPARENCY).setUseLigth(false);
		scene.getMaterialLibrary().addMaterial("gas_ico", new TexturedMaterial("gas.png")).setBlendMode(SimpleMaterial.TRANSPARENCY).setUseLigth(false);

		Mesh mesh = PrimitiveFactory.createPlane(0.5f, 0.5f);
		mesh.setMaterialName("minerals_ico");
		for (int i = 0; i < 10; i++) {
			Object3d o = scene.add(mesh);
			o.getPosition().setTranslation(-7f,0,-5+i*0.5f);
			o.setVisible(false);
			minerals.add(o);
		}
	}
	@Override
	public void on(GameEvent gameEvent, Card card, CreatureCard nothing) {
		for (int i = 0; i < 10; i++) {
			minerals.get(i).setVisible(false);
		}
		for (int i = 0; i < game.getPlayerByID(player).getCurrentMinerals(); i++) {
			minerals.get(i).setVisible(true);
		}
	}
}
