package edu.cg.models;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import edu.cg.algebra.Point;
import edu.cg.models.Car.Materials;

public class TrackSegment implements IRenderable {
	// TODO: Some constants you can use
	public final static double ASPHALT_TEXTURE_WIDTH = 20.0;
	public final static double ASPHALT_TEXTURE_DEPTH = 10.0;
	public final static double GRASS_TEXTURE_WIDTH = 10.0;
	public final static double GRASS_TEXTURE_DEPTH = 10.0;
	public final static double TRACK_LENGTH = 500.0;
	public final static double BOX_LENGTH = 1.5;
	private LinkedList<Point> boxesLocations; // Store the boxes centroids (center points) here.
	private SkewedBox skewedBox;
	Texture asphaltTexture;
	Texture grassTexture;


	public void setDifficulty(double difficulty) {
		difficulty = Math.min(difficulty, 0.95);
		difficulty = Math.max(difficulty, 0.05);
		double numberOfLanes = 4.0;
		double deltaZ = 0.0;
		if (difficulty < 0.25) {
			deltaZ = 100.0;
		} else if (difficulty < 0.5) {
			deltaZ = 75.0;
		} else {
			deltaZ = 50.0;
		}
		boxesLocations = new LinkedList<Point>();
		for (double dz = deltaZ; dz < TRACK_LENGTH - BOX_LENGTH / 2.0; dz += deltaZ) {
			int cnt = 0; // Number of boxes sampled at each row.
			boolean flag = false;
			for (int i = 0; i < 12; i++) {
				double dx = -((double) numberOfLanes / 2.0) * ((ASPHALT_TEXTURE_WIDTH - 2.0) / numberOfLanes) + BOX_LENGTH / 2.0
						+ i * BOX_LENGTH;
				if (Math.random() < difficulty) {
					boxesLocations.add(new Point(dx, BOX_LENGTH / 2.0, -dz));
					cnt += 1;
				} else if (!flag) {// The first time we don't sample a box then we also don't sample the box next to. We want enough space for the car to pass through.
					i += 1;
					flag = true;
				}
				if (cnt > difficulty * 10) {
					break;
				}
			}
		}
	}

	public TrackSegment(double difficulty) {
		skewedBox = new SkewedBox(true, BOX_LENGTH);
		setDifficulty(difficulty);
	}

	@Override
	public void render(GL2 gl) {
		renderBoxes(gl);
		renderRoad(gl);
		renderGrass(gl);
	}

	private void renderBoxes(GL2 gl){
		Materials.setWoodenBoxMaterial(gl);
		for(Point boxCenter : boxesLocations){
			gl.glPushMatrix();

			gl.glTranslated(boxCenter.x, boxCenter.y, boxCenter.z);
			skewedBox.render(gl);

			gl.glPopMatrix();
		}
	}

	private void renderRoad(GL2 gl){
		Materials.setAsphaltMaterial(gl);
		gl.glPushMatrix();
		renderTexture(gl,ASPHALT_TEXTURE_WIDTH, ASPHALT_TEXTURE_DEPTH, asphaltTexture);
		gl.glPopMatrix();
	}

	private void renderGrass(GL2 gl){
		Materials.setGrassMaterial(gl);

		// left grass render
		gl.glPushMatrix();
		gl.glTranslated(-ASPHALT_TEXTURE_WIDTH/2.0 - GRASS_TEXTURE_WIDTH/2.0, 0.0, 0.0);
		renderTexture(gl, GRASS_TEXTURE_WIDTH, GRASS_TEXTURE_DEPTH, grassTexture);
		gl.glPopMatrix();

		// right grass render
		gl.glPushMatrix();
		gl.glTranslated(ASPHALT_TEXTURE_WIDTH/2.0 + GRASS_TEXTURE_WIDTH/2.0, 0.0, 0.0);
		renderTexture(gl, GRASS_TEXTURE_WIDTH, GRASS_TEXTURE_DEPTH, grassTexture);
		gl.glPopMatrix();
	}


	private void renderTexture(GL2 gl, double width, double depth, Texture texture){
		// enabling the 2D textures
		gl.glEnable(GL.GL_TEXTURE_2D);
		texture.bind(gl);

		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		//filtering magnification
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		//filtering minification
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);

		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_LOD, GL2.GL_ONE);

		gl.glColor3d(1.0, 0.0, 0.0);
		gl.glNormal3d(0.0, 1.0, 0.0);

		// number of pieces of the road we need to put texture on
		double num_of_pieces = TRACK_LENGTH/depth;

		for(int i = 0; i < num_of_pieces; i++){
			gl.glBegin(GL2.GL_QUAD_STRIP);

			gl.glTexCoord2f(0,1);
			gl.glVertex3d(-width/2.0,0,-(i * depth + depth));

			gl.glTexCoord2f(0,0);
			gl.glVertex3d(-width/2.0,0,-(i * depth) );

			gl.glTexCoord2f(1,1);
			gl.glVertex3d(width/2.0,0,-(i * depth + depth));

			gl.glTexCoord2f(1,0);
			gl.glVertex3d(width/2.0,0,-(i * depth));

			gl.glEnd();
		}
		gl.glDisable(GL.GL_TEXTURE_2D);
	}


	@Override
	public void init(GL2 gl) {
		skewedBox.init(gl);
		try {
//			asphaltTexture = TextureIO.newTexture(new File("./src/edu/cg/Textures/RoadTexture.jpg"), true)
			asphaltTexture = TextureIO.newTexture(new File("Textures/RoadTexture.jpg"), true);
//			grassTexture = TextureIO.newTexture(new File("./src/edu/cg/Textures/GrassTexture.jpg"), true);;
			grassTexture = TextureIO.newTexture(new File("Textures/GrassTexture.jpg"), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void destroy(GL2 gl) {
		asphaltTexture.destroy(gl);
		grassTexture.destroy(gl);
		skewedBox.destroy(gl);
	}

}
