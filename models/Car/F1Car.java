package edu.cg.models.Car;

import com.jogamp.opengl.*;

import edu.cg.models.IRenderable;

/**
 * A F1 Racing Car.
 *
 */
public class F1Car implements IRenderable {
	private Center center;
	private Front front;
	private Back back;

	public F1Car(){
		center = new Center();
		front = new Front();
		back = new Back();
	}

	@Override
	public void render(GL2 gl) {
		center.render(gl);
		gl.glPushMatrix();
		gl.glTranslated(Specification.C_BASE_LENGTH/2 + Specification.F_HOOD_LENGTH_1 ,0,0);
		front.render(gl);
		gl.glPopMatrix();
		gl.glTranslated(-Specification.B_LENGTH/2 ,0,0);
		back.render(gl);
	}

	@Override
	public String toString() {
		return "F1Car";
	}

	@Override
	public void init(GL2 gl) {

	}

	@Override
	public void destroy(GL2 gl) {
		center.destroy(gl);
		front.destroy(gl);
		back.destroy(gl);
	}
}
