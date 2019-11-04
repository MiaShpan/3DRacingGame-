package edu.cg.models;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;

public class SkewedBox implements IRenderable {
	private double length, height1, height2, depth1, depth2;
	private boolean isTextured = false;
	private Texture boxTexture;

	public SkewedBox(double length, double h1, double h2, double d1, double d2) {
		this.length = length;
		this.height1 = h1;
		this.height2 = h2;
		this.depth1 = d1;
		this.depth2 = d2;
	}

	public SkewedBox(boolean isTextured, double boxLength){
		this.isTextured = isTextured;
		this.height1 = boxLength;
		this.height2 = boxLength;
		this.depth1 = boxLength;
		this.depth2 = boxLength;
		this.length = boxLength;
	}

	private void renderTextured(GL2 gl){
		gl.glEnable(GL2.GL_TEXTURE_2D);
		boxTexture.bind(gl);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

		//filtering magnification
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		//filtering minification
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);

		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_LOD, GL2.GL_ONE);

		// back rectangle
		gl.glNormal3d(-1.0, 0, 0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glTexCoord2d(0,0);
		gl.glVertex3d(-length / 2.0, 0, -depth1 / 2.0);
		gl.glTexCoord2d(0,1);
		gl.glVertex3d(-length / 2.0, 0, depth1 / 2.0);
		gl.glTexCoord2d(1,1);
		gl.glVertex3d(-length / 2.0, height1, depth1 / 2.0);
		gl.glTexCoord2d(1,0);
		gl.glVertex3d(-length / 2.0, height1, -depth1 / 2.0);
		gl.glEnd();

		// front rectangle
		gl.glNormal3d(1.0, 0, 0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glTexCoord2d(0,0);
		gl.glVertex3d(length / 2.0, 0, depth2 / 2.0);
		gl.glTexCoord2d(0,1);
		gl.glVertex3d(length / 2.0, 0, -depth2 / 2.0);
		gl.glTexCoord2d(1,1);
		gl.glVertex3d(length / 2.0, height2, -depth2 / 2.0);
		gl.glTexCoord2d(1,0);
		gl.glVertex3d(length / 2.0, height2, depth2 / 2.0);
		gl.glEnd();

		// left trapez
		gl.glNormal3d(0.0, 0, -1.0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glTexCoord2d(0,0);
		gl.glVertex3d(-length / 2.0, 0, -depth1 / 2.0);
		gl.glTexCoord2d(0,1);
		gl.glVertex3d(-length / 2.0, height1, -depth1 / 2.0);
		gl.glTexCoord2d(1,1);
		gl.glVertex3d(length / 2.0, height2, -depth2 / 2.0);
		gl.glTexCoord2d(1,0);
		gl.glVertex3d(length / 2.0, 0, -depth2 / 2.0);
		gl.glEnd();

		// bottom trapez
		gl.glNormal3d(0.0, -1.0, 0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glTexCoord2d(0,0);
		gl.glVertex3d(-length / 2.0, 0, depth1 / 2.0);
		gl.glTexCoord2d(0,1);
		gl.glVertex3d(-length / 2.0, 0, -depth1 / 2.0);
		gl.glTexCoord2d(1,1);
		gl.glVertex3d(length / 2.0, 0, -depth2 / 2.0);
		gl.glTexCoord2d(1,0);
		gl.glVertex3d(length / 2.0, 0, depth2 / 2.0);
		gl.glEnd();

		// right trapez
		gl.glNormal3d(0, 0, 1.0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glTexCoord2d(0,0);
		gl.glVertex3d(-length / 2.0, height1, depth1 / 2.0);
		gl.glTexCoord2d(0,1);
		gl.glVertex3d(-length / 2.0, 0, depth1 / 2.0);
		gl.glTexCoord2d(1,1);
		gl.glVertex3d(length / 2.0, 0, depth2 / 2.0);
		gl.glTexCoord2d(1,0);
		gl.glVertex3d(length / 2.0, height2, depth2 / 2.0);
		gl.glEnd();

		// top trapez
		gl.glNormal3d(0, 1.0, 0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glTexCoord2d(0,0);
		gl.glVertex3d(-length / 2.0, height1, depth1 / 2.0);
		gl.glTexCoord2d(0,1);
		gl.glVertex3d(length / 2.0, height2, depth2 / 2.0);
		gl.glTexCoord2d(1,1);
		gl.glVertex3d(length / 2.0, height2, -depth2 / 2.0);
		gl.glTexCoord2d(1,0);
		gl.glVertex3d(-length / 2.0, height1, -depth1 / 2.0);
		gl.glEnd();
	}

	@Override
	public void render(GL2 gl) {
		if(this.isTextured){
			renderTextured(gl);
			return;
		}

		// back rectangle
		gl.glNormal3d(-1.0, 0, 0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex3d(-length / 2.0, 0, -depth1 / 2.0);
		gl.glVertex3d(-length / 2.0, 0, depth1 / 2.0);
		gl.glVertex3d(-length / 2.0, height1, depth1 / 2.0);
		gl.glVertex3d(-length / 2.0, height1, -depth1 / 2.0);
		gl.glEnd();

		// front rectangle
		gl.glNormal3d(1.0, 0, 0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex3d(length / 2.0, 0, depth2 / 2.0);
		gl.glVertex3d(length / 2.0, 0, -depth2 / 2.0);
		gl.glVertex3d(length / 2.0, height2, -depth2 / 2.0);
		gl.glVertex3d(length / 2.0, height2, depth2 / 2.0);
		gl.glEnd();

		// left trapez
		gl.glNormal3d(0.0, 0, -1.0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex3d(-length / 2.0, 0, -depth1 / 2.0);
		gl.glVertex3d(-length / 2.0, height1, -depth1 / 2.0);
		gl.glVertex3d(length / 2.0, height2, -depth2 / 2.0);
		gl.glVertex3d(length / 2.0, 0, -depth2 / 2.0);
		gl.glEnd();

		// bottom trapez
		gl.glNormal3d(0.0, -1.0, 0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex3d(-length / 2.0, 0, depth1 / 2.0);
		gl.glVertex3d(-length / 2.0, 0, -depth1 / 2.0);
		gl.glVertex3d(length / 2.0, 0, -depth2 / 2.0);
		gl.glVertex3d(length / 2.0, 0, depth2 / 2.0);
		gl.glEnd();

		// right trapez
		gl.glNormal3d(0, 0, 1.0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex3d(-length / 2.0, height1, depth1 / 2.0);
		gl.glVertex3d(-length / 2.0, 0, depth1 / 2.0);
		gl.glVertex3d(length / 2.0, 0, depth2 / 2.0);
		gl.glVertex3d(length / 2.0, height2, depth2 / 2.0);
		gl.glEnd();

		// top trapez
		gl.glNormal3d(0, 1.0, 0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex3d(-length / 2.0, height1, depth1 / 2.0);
		gl.glVertex3d(length / 2.0, height2, depth2 / 2.0);
		gl.glVertex3d(length / 2.0, height2, -depth2 / 2.0);
		gl.glVertex3d(-length / 2.0, height1, -depth1 / 2.0);
		gl.glEnd();
	}

	@Override
	public void init(GL2 gl) {
		if(this.isTextured) {
			try {
//				boxTexture = TextureIO.newTexture(new File("./src/edu/cg/Textures/WoodBoxTexture.jpg"), true);
				boxTexture = TextureIO.newTexture(new File("Textures/WoodBoxTexture.jpg"), true);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destroy(GL2 gl) {
		boxTexture.destroy(gl);
	}

	@Override
	public String toString() {
		return "SkewedBox";
	}

}
