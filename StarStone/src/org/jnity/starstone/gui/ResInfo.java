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
	List<Object3d> gas = new ArrayList<Object3d>();
	
	private String playerName; 
	public ResInfo(Game game, Scene scene, String playerID) {
		
		this.game = game;
		this.playerName = playerID;
		this.game.addListener(this);
		
		scene.getMaterialLibrary().addMaterial("minerals_ico1", new TexturedMaterial("minerals.png")).setBlendMode(SimpleMaterial.ALPHATEST50).setUseLigth(false);
		scene.getMaterialLibrary().addMaterial("minerals_ico2", new TexturedMaterial("minerals.png"))
			.setColor(0.5f, 0.5f, 0.5f)
			.setBlendMode(SimpleMaterial.ALPHATEST50)
			.setUseLigth(false);
		scene.getMaterialLibrary().addMaterial("gas_ico1", new TexturedMaterial("gas.png")).setBlendMode(SimpleMaterial.ALPHATEST50).setUseLigth(false);
		scene.getMaterialLibrary().addMaterial("gas_ico2", new TexturedMaterial("gas.png"))
			.setColor(0.5f, 0.5f, 0.5f)
			.setBlendMode(SimpleMaterial.ALPHATEST50)
			.setUseLigth(false);

		Mesh mesh = PrimitiveFactory.createPlane(0.5f, 0.5f);
		mesh.setMaterialName("minerals_ico1");
		for (int i = 0; i < 10; i++) {
			Object3d o = scene.add(mesh.fastClone());
			o.getPosition().setTranslation(-7f,0,-5+i*0.5f);
			o.setVisible(false);
			minerals.add(o);
			o = scene.add(mesh.fastClone());
			o.getPosition().setTranslation(-6.5f,0,-5+i*0.5f);
			o.setVisible(false);
			gas.add(o);
		}
	}
	@Override
	public void on(GameEvent gameEvent, Card card, CreatureCard nothing) {
		Player player = game.getPlayerByID(playerName);
		for (int i = 0; i < 10; i++) {
			minerals.get(i).setVisible(false);
		}
		for (int i = 0; i < player.getCurrentMinerals(); i++) {
			minerals.get(i).setVisible(true);
			minerals.get(i).get(Mesh.class).setMaterialName("minerals_ico1");
		}
		for (int i = player.getCurrentMinerals(); i < player.getMaxMinerals(); i++) {
			minerals.get(i).setVisible(true);
			minerals.get(i).get(Mesh.class).setMaterialName("minerals_ico2");
		}
		for (int i = 0; i < 10; i++) {
			gas.get(i).setVisible(false);
		}
		for (int i = 0; i < player.getCurrentVespenGase(); i++) {
			gas.get(i).setVisible(true);
			gas.get(i).get(Mesh.class).setMaterialName("gas_ico1");
		}
		for (int i = player.getCurrentVespenGase(); i < player.getMaxVespenGase(); i++) {
			gas.get(i).setVisible(true);
			gas.get(i).get(Mesh.class).setMaterialName("gas_ico2");
		}
	}
}
