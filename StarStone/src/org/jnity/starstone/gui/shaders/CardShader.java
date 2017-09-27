package org.jnity.starstone.gui.shaders;

import jglsl.base.JFragmentShader;
import jglsl.base.Sampler2D;
import jglsl.base.Uniform;
import jglsl.base.Varying;
import jglsl.base.Vec3;
import jglsl.base.Vec4;

public class CardShader extends JFragmentShader {

	@Uniform
	Sampler2D backTex;
	@Uniform
	Sampler2D faceTex;
	@Uniform
	Sampler2D numbers;

	@Varying
	Vec4 screenPos;
	@Varying
	Vec4 texCoord;
	@Varying
	Vec3 normal;
	@Varying
	Vec3 camDir;
	@Varying
	Vec3 lightPos;

	@Override
	public void main() {
		Vec4 f_color = texture2D(faceTex, sub(mul(texCoord.st, vec2(2f, 2f)), vec2(0.5f, 0.05f)));
		Vec4 b_color = texture2D(backTex, texCoord.st);
		float faceMask = clamp(b_color.a * 1 - 100 * b_color.r, 0f, 1f);
		gl_FragColor = add(mul(f_color, faceMask), b_color);
	}

}
