package org.jnity.starstone.gui.shaders;

import jglsl.base.JFragmentShader;
import jglsl.base.Sampler2D;
import jglsl.base.Uniform;
import jglsl.base.Varying;
import jglsl.base.Vec3;
import jglsl.base.Vec4;

public class _CreatureShader extends JFragmentShader {

	@Uniform
	Sampler2D backTex;
	@Uniform
	Sampler2D faceTex;
	@Uniform
	Sampler2D movedTex;
	@Uniform
	Sampler2D numbers;
	@Uniform
	Vec3 shift;
	@Uniform
	Vec4 values;//x-price in mineral, y-price in gas, z-power,w-hits
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
		Vec4 f_color = texture2D(faceTex, sub(mul(texCoord.st, vec2(1.2f, 1.2f)), vec2(0.1f, 0.05f)));
		Vec4 m_color = texture2D(movedTex, sub(texCoord.st, shift.xy));
		float lightning = max(sin(shift.z), sin(shift.z + 0.7f));
		lightning = clamp(1/(lightning*lightning)-abs(lightning)*100,0f,1f);
		f_color = mul(f_color, 1 + lightning);
		f_color = add(f_color, mul(m_color, m_color.a*0.5f));
		Vec4 b_color = texture2D(backTex, texCoord.st);
		float faceMask = clamp(b_color.a * 1 - 100 * b_color.r, 0f, 1f);
		gl_FragColor = add(mul(f_color, faceMask), b_color);
	}

}
