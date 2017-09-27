package org.jnity.starstone.gui.shaders;

import jglsl.base.JVertexShader;
import jglsl.base.Uniform;
import jglsl.base.Varying;
import jglsl.base.Vec3;
import jglsl.base.Vec4;

public class SimpleVertexShader extends JVertexShader {

	@Uniform
	protected Vec3 lightPosU;
	
	@Varying
	protected Vec4 texCoord;
	@Varying
	protected Vec4 vPos;
	@Varying
	protected Vec3 normal;
	@Varying
	protected Vec3 lightPos;
	@Varying
	protected Vec3 camDir;
	@Varying
	protected Vec4 screenPos;
	@Override
	public void main() {
		vPos=gl_Vertex;
		Vec3 position = vec3(mul(gl_ModelViewMatrix, gl_Vertex));
		camDir = mul(normalize(position),-1f);
		normal = normalize(mul(gl_NormalMatrix, gl_Normal));
		lightPos = normalize(sub(lightPosU, position));
		gl_Position = mul(mul(gl_ProjectionMatrix,gl_ModelViewMatrix),gl_Vertex);
		screenPos = gl_Position;
		texCoord = gl_MultiTexCoord0;
	}

}
