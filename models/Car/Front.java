package edu.cg.models.Car;

import com.jogamp.opengl.GL2;

import edu.cg.models.IRenderable;
import edu.cg.models.SkewedBox;

public class Front implements IRenderable {
	// TODO: The front of the car is build from the following elements:
	private SkewedBox hoodBox1 = new SkewedBox(Specification.F_HOOD_LENGTH_1,Specification.F_HOOD_HEIGHT_1,Specification.F_HOOD_HEIGHT_2,Specification.F_HOOD_DEPTH_1,Specification.F_HOOD_DEPTH_2);
	private SkewedBox hoodBox2 = new SkewedBox(Specification.F_HOOD_LENGTH_2,Specification.F_HOOD_HEIGHT_2,Specification.F_BUMPER_HEIGHT_1,Specification.F_HOOD_DEPTH_2,Specification.F_HOOD_DEPTH_3);
	private SkewedBox bumperBox = new SkewedBox(Specification.F_BUMPER_LENGTH,Specification.F_BUMPER_HEIGHT_1,Specification.F_BUMPER_HEIGHT_2,Specification.F_BUMPER_DEPTH,Specification.F_BUMPER_DEPTH);
	private SkewedBox bumperWingsBox = new SkewedBox(Specification.F_BUMPER_LENGTH,Specification.F_BUMPER_WINGS_HEIGHT,Specification.F_BUMPER_HEIGHT_2,Specification.F_BUMPER_WINGS_DEPTH,Specification.F_BUMPER_WINGS_DEPTH);
	private PairOfWheels wheels = new PairOfWheels();


	@Override
	public void render(GL2 gl) {
		Materials.SetRedMetalMaterial(gl);
		gl.glPushMatrix();
		gl.glTranslated(-Specification.F_HOOD_LENGTH_1/2, 0 , 0);
		hoodBox1.render(gl);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(Specification.F_HOOD_LENGTH_2/2, 0 , 0);
		hoodBox2.render(gl);
		gl.glTranslated(0, Specification.F_HOOD_HEIGHT_2/4 + Specification.F_BUMPER_HEIGHT_1/4 , 0);
		wheels.render(gl);
		gl.glTranslated(Specification.F_HOOD_LENGTH_2/2 + Specification.F_BUMPER_LENGTH/2, 0 , 0);
		Materials.SetDarkRedMetalMaterial(gl);
		bumperBox.render(gl);
		gl.glPushMatrix();
		gl.glTranslated(0,0, Specification.F_BUMPER_DEPTH/2 + Specification.F_BUMPER_WINGS_DEPTH/2);
		Materials.SetRedMetalMaterial(gl);
		bumperWingsBox.render(gl);
		gl.glPopMatrix();
		gl.glTranslated(0,0, -Specification.F_BUMPER_DEPTH/2 - Specification.F_BUMPER_WINGS_DEPTH/2);
		bumperWingsBox.render(gl);
		gl.glPopMatrix();
	}

	@Override
	public void init(GL2 gl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(GL2 gl) {
		hoodBox1.destroy(gl);
		hoodBox2.destroy(gl);
		bumperBox.destroy(gl);
		bumperWingsBox.destroy(gl);
		wheels.destroy(gl);
	}

}
