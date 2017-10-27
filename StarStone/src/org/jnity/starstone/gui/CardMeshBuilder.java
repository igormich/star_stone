package org.jnity.starstone.gui;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import properties.Mesh;

public class CardMeshBuilder {
	public static Mesh createCreatureMesh() {
		Mesh result = new Mesh();
		float width = 1.7f;
		float heigth = 2.5f;
		float hShift = -0.5f;
		float tw=0.8f/(2*2.5f);
		float th=1/(2*3.5f);
		result.add(new Vector3f(-width / 2, 0, -heigth / 2-hShift), new Vector3f(0, 1, 0), null, new Vector2f(tw, 1 - th*2));
		result.add(new Vector3f(width / 2, 0, -heigth / 2-hShift), new Vector3f(0, 1, 0), null, new Vector2f(1-tw, 1- th*2));
		result.add(new Vector3f(width / 2, 0, heigth / 2-hShift), new Vector3f(0, 1, 0), null, new Vector2f(1-tw, 0));

		result.add(new Vector3f(width / 2, 0, heigth / 2 - hShift), new Vector3f(0, 1, 0), null, new Vector2f(1-tw, 0));
		result.add(new Vector3f(-width / 2, 0, heigth / 2 - hShift), new Vector3f(0, 1, 0), null, new Vector2f(tw, 0));
		result.add(new Vector3f(-width / 2, 0, -heigth / 2 - hShift), new Vector3f(0, 1, 0), null, new Vector2f(tw, 1- th*2));

		result.setRenderParts(Mesh.TEXTURE + Mesh.NORMAL);
		return result;
	}

	public static Mesh createCardMesh() {
		Mesh result = new Mesh();
		float width = 2.5f;
		float heigth = 3.5f;
		result.add(new Vector3f(-width / 2, 0, -heigth / 2), new Vector3f(0, 1, 0), null, new Vector2f(0, 1));
		result.add(new Vector3f(width / 2, 0, -heigth / 2), new Vector3f(0, 1, 0), null, new Vector2f(1, 1));
		result.add(new Vector3f(width / 2, 0, heigth / 2), new Vector3f(0, 1, 0), null, new Vector2f(1, 0));

		result.add(new Vector3f(width / 2, 0, heigth / 2), new Vector3f(0, 1, 0), null, new Vector2f(1, 0));
		result.add(new Vector3f(-width / 2, 0, heigth / 2), new Vector3f(0, 1, 0), null, new Vector2f(0, 0));
		result.add(new Vector3f(-width / 2, 0, -heigth / 2), new Vector3f(0, 1, 0), null, new Vector2f(0, 1));

		result.setRenderParts(Mesh.TEXTURE + Mesh.NORMAL);
		return result;
	}
}
