package org.jnity.starstone.gui.shaders;

import jglsl.base.JFragmentShader;
import jglsl.base.Sampler2D;
import jglsl.base.Uniform;
import jglsl.base.Varying;
import jglsl.base.Vec2;
import jglsl.base.Vec3;
import jglsl.base.Vec4;

public class CompactCreatureShader extends JFragmentShader {

	@Uniform
	Sampler2D backTex;
	@Uniform
	Sampler2D faceTex;
	@Uniform
	Sampler2D numbersTex;
	@Uniform
	Sampler2D shadowTex;
	@Uniform
	Sampler2D shieldTex;
	@Uniform
	Sampler2D target;
	@Uniform
	Vec4 stats;//no cost x - target
	
	@Uniform
	Vec4 modifiers;//x-shild,y-shadow
	@Uniform
	float time;
	
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
		Vec4 shadow_color = texture2D(shadowTex, add(texCoord.st, -time/100));
		Vec4 shield_color = texture2D(shieldTex, add(texCoord.st, -time/100));
		f_color = add(mul(f_color, 1-modifiers.x), (mul(add(mul(f_color,0.8f),0.2f), mul(shield_color,modifiers.x))));
		f_color = add(mul(f_color, 1-modifiers.y), (mul(add(mul(f_color,0.8f),0.2f), mul(shadow_color,modifiers.y))));
		float faceMask = clamp(b_color.a * 1 - 100 * b_color.r, 0f, 1f);
		Vec4 color = add(mul(f_color, faceMask), b_color);
		
		float numberScale = 6f; 

		Vec2 costText = mul(texCoord.st,numberScale);
		Vec4 number = vec4(0f, 0f, 0f, 0f);
		//power
		costText = mul(texCoord.st, numberScale);
		costText.x-=1.3f;
		costText.y-=numberScale-1-2.5f;
		number = vec4(0f, 0f, 0f, 0f);
		if(max(costText.x,costText.y)<1 && min(costText.x,costText.y)>0) { // better without if
			costText.x-=0.1f;
			costText.y+=stats.z;
			number = texture2D(numbersTex, mul(costText, vec2(1f,0.1f)));
		}
		color = add(mul(color, 1-number.a), number);
		
		//hits
		costText = mul(texCoord.st, numberScale);
		costText.x-=numberScale-1-1.3f;
		costText.y-=numberScale-1-2.6f;
		number = vec4(0f, 0f, 0f, 0f);
		if(max(costText.x,costText.y)<1 && min(costText.x,costText.y)>0) { // better without if
			costText.y+=stats.w-0.1f;
			number = texture2D(numbersTex, mul(costText, vec2(1f,0.1f)));
		}
		color = add(mul(color, 1-number.a), number);
		
		color = add(color, mul(texture2D(target, texCoord.st),stats.x));
		
		gl_FragColor = color;
	}

}
