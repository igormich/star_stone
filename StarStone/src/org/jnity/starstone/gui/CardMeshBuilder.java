package org.jnity.starstone.gui;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import properties.Mesh;

public class CardMeshBuilder {
	public static Mesh createCreatureMesh() {
		Mesh result = new Mesh();
		float width = 1.5f;
		float heigth = 2f;
		float hShift = -0.7f;
		float tw = (1-(2.5f - width)/2.5f)/2;
		float th = (1-(3.5f - heigth)/3.5f)/2;
		float thShift = -hShift*heigth/3.5f;
		result.add(new Vector3f(-width / 2, 0, -heigth / 2-hShift), new Vector3f(0, 1, 0), null, new Vector2f(tw, 1-th-thShift));
		result.add(new Vector3f(width / 2, 0, -heigth / 2-hShift), new Vector3f(0, 1, 0), null, new Vector2f(1-tw, 1-th-thShift));
		result.add(new Vector3f(width / 2, 0, heigth / 2-hShift), new Vector3f(0, 1, 0), null, new Vector2f(1-tw, th-thShift));

		result.add(new Vector3f(width / 2, 0, heigth / 2 - hShift), new Vector3f(0, 1, 0), null, new Vector2f(1-tw, th-thShift));
		result.add(new Vector3f(-width / 2, 0, heigth / 2 - hShift), new Vector3f(0, 1, 0), null, new Vector2f(tw, th-thShift));
		result.add(new Vector3f(-width / 2, 0, -heigth / 2 - hShift), new Vector3f(0, 1, 0), null, new Vector2f(tw, 1-th-thShift));

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
