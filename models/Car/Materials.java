package edu.cg.models.Car;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;


public class Materials {
	private static final float DARK_GRAY[] = { 0.2f, 0.2f, 0.2f };
	private static final float DARK_RED[] = { 0.25f, 0.01f, 0.01f };
	private static final float RED[] = { 0.7f, 0f, 0f };
	private static final float BLACK[] = { 0.05f, 0.05f, 0.05f };

	public static void SetMetalMaterial(GL2 gl, float[] color) {
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, color, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, color, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, color, 0);

		gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 70f);
	}

	public static void SetBlackMetalMaterial(GL2 gl) {
		SetMetalMaterial(gl, BLACK);
	}

	public static void SetRedMetalMaterial(GL2 gl) {
		SetMetalMaterial(gl, RED);
	}

	public static void SetDarkRedMetalMaterial(GL2 gl) {
		SetMetalMaterial(gl, DARK_RED);
	}

	public static void SetDarkGreyMetalMaterial(GL2 gl) {
		SetMetalMaterial(gl, DARK_GRAY);
	}

	public static void setMaterialTire(GL2 gl) {
		float col[] = { .05f, .05f, .05f };
		SetMetalMaterial(gl, col);
	}

	public static void setMaterialRims(GL2 gl) {
		SetMetalMaterial(gl, DARK_GRAY);
	}

	public static void setWoodenBoxMaterial(GL2 gl) {
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.1f, 0.6f, 0.1f, 1.0f}, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.7f, 0.2f, 0.2f, 1.0f}, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.3f, 0.5f, 0.1f, 1.0f}, 0);
	}

	public static void setAsphaltMaterial(GL2 gl) {
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.1f, 0.1f, 0.1f, 1.0f}, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.7f, 0.7f, 0.7f, 1.0f}, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.4f, 0.4f, 0.4f, 1.0f}, 0);
	}

	public static void setGrassMaterial(GL2 gl) {
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.05f, 0.05f, 0.05f, 1.0f}, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.1f, 0.5f, 0.1f, 1.0f}, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.2f, 0.7f, 0.5f, 1.0f}, 0);

		gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 70f);
	}
}
