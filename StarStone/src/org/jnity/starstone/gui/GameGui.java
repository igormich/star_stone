package org.jnity.starstone.gui;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jnity.starstone.cards.Card;
import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.Game;
import org.jnity.starstone.core.Player;
import org.jnity.starstone.core.TextHolder;
import org.jnity.starstone.events.GameEvent;
import org.jnity.starstone.events.GameListener;
import org.jnity.starstone.gui.shaders.CardShader;
import org.jnity.starstone.gui.shaders.CreatureShader;
import org.jnity.starstone.gui.shaders.SimpleVertexShader;
import org.jnity.starstone.protoss.ShieldRecharge;
import org.jnity.starstone.protoss.ShildBattery;
import org.jnity.starstone.protoss.Zealot;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import base.Camera;
import base.Object3d;
import base.Scene;
import io.ResourceController;
import jglsl.base.ShaderProcessor;
import materials.Shader;
import materials.SimpleMaterial;
import materials.Texture;
import materials.Texture2D;
import properties.Mesh;
import properties.MultiMesh;
import utils.PrimitiveFactory;

public class GameGui extends Thread implements GameListener {

	

	private final ConcurrentLinkedQueue<StoredEvent> events = new ConcurrentLinkedQueue<>();
	private Game game;

	public GameGui(Game game) throws IOException {
		this.game = game;
		game.addListener(this);
		start();
	}

	@Override
	public void run() {
		try{
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		Scene scene = new Scene();
		width = 1024;
		height = 768;
		Utils.initDisplay(width, height, false);
		Display.setVSyncEnabled(true);
		Camera camera = new Camera();
		camera.width = width;
		camera.height = height;
		camera.farPlane = 100;
		Object3d cameraBox = new Object3d();
		cameraBox.addChild(camera);
		scene.add(cameraBox);
		
		GuiCard.init(scene.getMaterialLibrary());
		MouseProcess.endTurnButton = scene.add(ResourceController.getOrCreate().getOrLoadMesh(new MultiMesh(), "cube.smd"));
		MouseProcess.endTurnButton.getPosition().setTranslation(6,0,0).setScale(0.5f, 0.1f, 0.2f);
		MouseProcess.game = game;
		camera.getPosition().move(0, -10, 0).roll(90).turn(90);
		scene.setBackColor(new Vector3f(0.5f, 1, 0.5f));
		long sysTime = 0;
		float time = 0;
		Animation animation = new StubAnimation();

		while (!Display.isCloseRequested()) {
			
			float deltaTime = 0;
			if (System.currentTimeMillis() - sysTime < 1000) {
				deltaTime = (System.currentTimeMillis() - sysTime) / 1000f;
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
					deltaTime *= 10;
			}
			time += deltaTime;
			sysTime = System.currentTimeMillis();
			MouseProcess.tick(scene, camera);
			
			if(!animation.isFinished()) {
				animation.play(deltaTime, scene);
			} else if(!events.isEmpty()) {
				StoredEvent event = events.poll();
				animation = Animation.createFor(event, scene);
			}
			
			scene.tick(deltaTime);
			scene.render(camera);
			Display.update();
		}
		Display.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	@Override
	public void on(GameEvent gameEvent, Card card, CreatureCard target) {
		events.add(new StoredEvent(gameEvent, card, target));
		while(!events.isEmpty());
	}

	@Override
	public void on(GameEvent gameEvent, Card card) {
		events.add(new StoredEvent(gameEvent, card));
		while(!events.isEmpty());
	}

	public static void main(String[] args) throws IOException {
		TextHolder.load("./text/ru.inf");
		List<Card> deck1 = new ArrayList<>();
		
		deck1.add(new Zealot());
		deck1.add(new ShieldRecharge());
		deck1.add(new Zealot());
		deck1.add(new ShieldRecharge());
		deck1.add(new ShildBattery());
		deck1.add(new ShildBattery());
		deck1.add(new Zealot());
		
		Player p1 = new Player("Первый игрок", deck1);
		Player p2 = new Player("Второй второй", deck1);
		Game game = new Game(p1, p2);
		new GameGui(game);
		game.nextTurn();
		/*for(int i=0;i<4;i++){
			p1.play(p1.getHand().get(0), null, 0);
			game.nextTurn();
			p2.play(p2.getHand().get(0), null, 0);
			game.nextTurn();
		}
		game.nextTurn();
		p2.play(p2.getHand().get(0), null, 0);
		game.nextTurn();
		p1.getCreatures().forEach(c -> c.takeDamage(1));
		game.nextTurn();
		game.nextTurn();
		p1.getCreatures().forEach(c -> c.takeDamage(1));
		game.nextTurn();
		game.nextTurn();
		p1.getCreatures().forEach(c -> c.takeDamage(1));*/
	}

}
