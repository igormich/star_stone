package org.jnity.starstone.gui.shaders;

import jglsl.base.JFragmentShader;
import jglsl.base.Sampler2D;
import jglsl.base.Uniform;
import jglsl.base.Varying;
import jglsl.base.Vec2;
import jglsl.base.Vec3;
import jglsl.base.Vec4;

public class CardShader extends JFragmentShader {

	@Uniform
	Sampler2D backTex;
	@Uniform
	Sampler2D faceTex;
	@Uniform
	Sampler2D numbersTex;

	@Uniform
	Vec4 stats;
	
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
		Vec4 color = add(mul(f_color, faceMask), b_color);
		
		float numberScale = 5f; 
		//cost
		Vec2 costText = mul(texCoord.st,numberScale);
		Vec4 number = vec4(0f, 0f, 0f, 0f);
		costText.x-=0.3f;
		if(max(costText.x,costText.y)<1 && min(costText.x,costText.y)>0) { // better without if
			costText.y+=stats.x;
			number = texture2D(numbersTex, mul(costText, vec2(1f,0.1f)));
		}
		color = add(mul(color, 1-number.a), number);
		
		gl_FragColor = color;
	}

}
