package org.jnity.starstone.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
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

public class GuiCard extends Object3d {

	private static Map<Card, GuiCard> card2card = new HashMap<>();
	private static Shader cardShader;
	private static Shader creatureShader;
	private static Mesh cardMesh;
	private static Mesh creatureMesh;
	private static Shader compactCreatureShader;

	public static MouseProcess mouseProcess;
	private static Font font;

	public static void init(MaterialLibrary materialLibrary) throws Exception {
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
		// cardShader.setzWrite(false);
		creatureShader.setBlendMode(SimpleMaterial.ALPHATEST50);
		// creatureShader.setzWrite(false);

		Texture shadow = new Texture2D("eff_shadow.jpg");
		Texture shield = new Texture2D("eff_shield.jpg");
		compactCreatureShader.addTexture(shadow, "shadowTex");
		compactCreatureShader.addTexture(shield, "shieldTex");
		Texture target = new Texture2D("target.png");
		compactCreatureShader.addTexture(target, "target");
		materialLibrary.addMaterial("cardShader", cardShader);
		materialLibrary.addMaterial("creatureShader", creatureShader);
		materialLibrary.addMaterial("compactCreatureShader", compactCreatureShader);
		font = Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf"));
	}

	private float time = 2;
	private Card card;
	private Texture2D faceTex;
	private Texture2D aboutTex;

	volatile protected Vector3f startTranslation;
	volatile protected Vector3f endTranslation;

	public GuiCard(Card card) {
		this.card = card;
		try {
			faceTex = new Texture2D(card.getID().toLowerCase() + ".jpg", false);
			BufferedImage about = buildText(card);
			aboutTex = new Texture2D(about, false);
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
				boolean renderSmall = card.onDesk() && (GuiCard.this != mouseProcess.underCursor || mouseProcess.state != MouseProcess.State.WAIT);
				// *
				if (card instanceof CreatureCard) {

					CreatureCard cCard = (CreatureCard) card;
					cardMesh.setMaterialName("creatureShader");

					if (renderSmall) {
						float target = 0;
						if ((mouseProcess.state == State.SELECT_TARGET_FOR_ATACK)
								&& (mouseProcess.selected.getCard().canAtack(cCard))) {
							target = 1;
						}
						if ((mouseProcess.state == State.PLAY_SPELL_WITH_TARGET)
								&& (mouseProcess.selected.getCard().isValidTarget(cCard))) {
							target = 1;
						}
						if ((mouseProcess.state == State.SELECT_TARGET_FOR_BATLECRY)
								&& (mouseProcess.creatureWithTarget.isValidTarget(cCard))) {
							target = 1;
						}
						compactCreatureShader.addTexture(faceTex, "faceTex");
						compactCreatureShader
								.setUniform(new Vector4f(target, 0, cCard.getPower(), cCard.getCurrentHits()), "stats");
						Vector4f modifiers = new Vector4f();
						if (card.getModifiers().stream().anyMatch(m -> m.getClass().equals(PlasmaShield.class)))
							modifiers.x = 0.5f;
						if (card.getModifiers().stream().anyMatch(m -> m.getClass().equals(Invisibility.class)))
							modifiers.y = 0.5f;
						compactCreatureShader.setUniform(modifiers, "modifiers");
						compactCreatureShader.setUniform(renderContex.getTime(), "time");
					} else {
						creatureShader.addTexture(faceTex, "faceTex");
						creatureShader.addTexture(aboutTex, "aboutTex");
						creatureShader.setUniform(new Vector4f(card.getPriceInMineral(), card.getPriceInGas(),
								cCard.getPower(), cCard.getCurrentHits()), "stats");
					}
				} else {
					cardMesh.setMaterialName("cardShader");
					cardShader.addTexture(faceTex, "faceTex");
					cardShader.addTexture(aboutTex, "aboutTex");
					cardShader.setUniform(new Vector4f(card.getPriceInMineral(), card.getPriceInGas(), 0, 0), "stats");
				} // */
				if (renderSmall) {
					creatureMesh.render(renderContex, owner);
				} else {
					cardMesh.render(renderContex, owner);
				}
			}

		});
		setName(card.getName());
	}

	private static BufferedImage buildText(Card card) {
		int WIDTH = 400;
		int HEIGTH = 543;
		BufferedImage result = new BufferedImage(WIDTH, HEIGTH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) result.getGraphics();

		String name = card.getName();
		g.setFont(font);

		float fontSize = 48f;
		g.setFont(g.getFont().deriveFont(fontSize).deriveFont(Font.BOLD | Font.ITALIC));
		while (g.getFontMetrics().stringWidth(name) > WIDTH * 0.75) {
			fontSize -= 2;
			g.setFont(g.getFont().deriveFont(fontSize));
		}
		AffineTransform baseTransform = g.getTransform();
		g.translate(WIDTH / 2 - g.getFontMetrics().stringWidth(name) / 2, 317);
		FontRenderContext frc = g.getFontRenderContext();
		g.setStroke(new BasicStroke(2.0f));
		//TextLayout textTl = new TextLayout(name, g.getFont(), frc);
		//Shape outline = textTl.getOutline(null);
		
		GlyphVector gv = g.getFont().createGlyphVector(frc, name);
		int length = gv.getNumGlyphs();
        for (int i = 0; i < length; i++) {
            Point2D p = gv.getGlyphPosition(i);
            double theta = (p.getX()-WIDTH/2)/(WIDTH*3);
            AffineTransform at = new AffineTransform();
            at.rotate(theta);
            at.translate(0, -theta*20);
            at.translate(0, -p.getX()/10);
            Shape glyph = gv.getGlyphOutline(i);
            Shape outline = at.createTransformedShape(glyph);
            g.setColor(Color.WHITE);
    		g.fill(outline);
    		g.setColor(Color.BLACK);
    		g.draw(outline);
        }
		/*g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(2.0f));
		g.fill(outline);
		g.setColor(Color.BLACK);
		g.draw(outline);*/

		g.setTransform(baseTransform);
		g.setFont(g.getFont().deriveFont(36f).deriveFont(0));
		g.setColor(Color.BLACK);
		String desk = card.getDescription();
		int posY = 370;
		while (desk.length() > 0) {
			if (g.getFontMetrics().stringWidth(desk) < WIDTH * 0.75) {
				g.drawString(desk, WIDTH / 2 - g.getFontMetrics().stringWidth(desk) / 2, posY);
				desk = "";
			} else {
				int split = desk.indexOf(" ");
				while (desk.indexOf(" ", split + 1) > 0 && (g.getFontMetrics()
						.stringWidth(desk.substring(0, desk.indexOf(" ", split + 1))) < WIDTH * 0.8)) {
					split = desk.indexOf(" ", split + 1);
				}
				if (split > 0) {
					String textToDraw = desk.substring(0, split);
					g.drawString(textToDraw, WIDTH / 2 - g.getFontMetrics().stringWidth(textToDraw) / 2, posY);
					posY += g.getFontMetrics().getHeight();
					desk = desk.substring(split + 1);
				} else {
					String textToDraw = desk;
					g.drawString(desk, WIDTH / 2 - g.getFontMetrics().stringWidth(textToDraw) / 2, posY);
					desk = "";
				}

			}
		}
		return result;
	}

	public static GuiCard get(Card card) {
		if (card2card.containsKey(card)) {
			return card2card.get(card);
		} else {
			GuiCard guicard = new GuiCard(card);
			mouseProcess.scene.add(guicard);
			card2card.put(card,guicard);
			return guicard;
		}
		
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
		time += deltaTime;
		if (!isMovingFinished()) {
			getPosition().setTranslation(startTranslation.x * (1 - getTime()) + endTranslation.x * getTime(),
					startTranslation.y * (1 - getTime()) + endTranslation.y * getTime(),
					startTranslation.z * (1 - getTime()) + endTranslation.z * getTime());
		} else {
			if (endTranslation != null) {
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
