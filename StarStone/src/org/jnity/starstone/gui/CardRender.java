package org.jnity.starstone.gui;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import org.jnity.starstone.gui.shaders.CardShader;
import org.jnity.starstone.gui.shaders.CreatureShader;
import org.jnity.starstone.gui.shaders.SimpleVertexShader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import base.Camera;
import base.Object3d;
import base.Scene;
import jglsl.base.ShaderProcessor;
import materials.Shader;
import materials.SimpleMaterial;
import materials.Texture;
import materials.Texture2D;
import properties.Mesh;
import utils.PrimitiveFactory;

public class CardRender {

	public static void main(String[] args) throws IOException {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();

		Scene scene = new Scene();
		width = 512;
		height = 512;
		Utils.initDisplay(width, height, false);
		Display.setVSyncEnabled(true);
		Camera camera = new Camera();
		camera.width = width;
		camera.height = height;
		camera.farPlane = 100;
		Object3d cameraBox = new Object3d();
		cameraBox.addChild(camera);
		scene.add(cameraBox);
		Mesh cardMesh = PrimitiveFactory.createPlane(2.5f, 3.5f);
		cardMesh.setMaterialName("cardShader");
		
		Shader cardShader= ShaderProcessor.build(SimpleVertexShader.class, CardShader.class);
		Texture backGround = new Texture2D("protoss.png");
		cardShader.addTexture(backGround, "backTex");
		Texture zealotTex = new Texture2D("zealot.jpg", false);
		cardShader.addTexture(zealotTex, "faceTex");
		cardShader.setBlendMode(SimpleMaterial.ALPHATEST50);
		scene.getMaterialLibrary().addMaterial("cardShader", cardShader);
		
		Shader creatureShader= ShaderProcessor.build(SimpleVertexShader.class, CreatureShader.class);
		Texture backGround1 = new Texture2D("u_protoss.png");
		creatureShader.addTexture(backGround1, "backTex");
		creatureShader.addTexture(zealotTex, "faceTex");
		Texture rainTex = new Texture2D("rain.png");
		creatureShader.addTexture(rainTex, "movedTex");
		creatureShader.setBlendMode(SimpleMaterial.ALPHATEST50);
		scene.getMaterialLibrary().addMaterial("creatureShader", creatureShader);
		
		scene.add(cardMesh);
		camera.getPosition().move(0,-5,0).roll(90).turn(90);
		scene.setBackColor(new Vector3f(0.5f, 1, 0.5f));
		long sysTime = 0;
		float time=0;
		while (!Display.isCloseRequested()) {
			scene.render(camera);
			float deltaTime = 0;
			if (System.currentTimeMillis() - sysTime < 1000) {
				deltaTime = (System.currentTimeMillis() - sysTime) / 1000f;
				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
					deltaTime*=10;
			}
			time+=deltaTime;
			creatureShader.setUniform(new Vector3f(-time * 0.1f, time * 0.5f, time), "shift");
			sysTime = System.currentTimeMillis();
			if(Keyboard.isKeyDown(Keyboard.KEY_F1))
				cardMesh.setMaterialName("cardShader");
			if(Keyboard.isKeyDown(Keyboard.KEY_F2))
				cardMesh.setMaterialName("creatureShader");
			Display.update();
		}
		Display.destroy();
	}

}
