package edu.cg;

import java.awt.Component;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import edu.cg.algebra.Vec;
import edu.cg.models.Car.F1Car;
import edu.cg.models.Track;
import edu.cg.models.TrackSegment;

/**
 * An OpenGL 3D Game.
 *
 */
public class NeedForSpeed implements GLEventListener {
	private GameState gameState = null; // Tracks the car movement and orientation
	private F1Car car = null; // The F1 car we want to render
	private Vec carCameraTranslation = null; // The accumulated translation that should be applied on the car, camera
	// and light sources
	private Track gameTrack = null; // The game track we want to render
	private FPSAnimator ani; // This object is responsible to redraw the model with a constant FPS
	private Component glPanel; // The canvas we draw on.
	private boolean isModelInitialized = false; // Whether model.init() was called.
	private boolean isDayMode = true; // Indicates whether the lighting mode is day/night.

	private Vec initialCameraPosition = new Vec(0.0,2.0,2.0);
	private Vec initialCarPosition = new Vec(0.0,0.5,-5.0);
	private static final float IMAGINARY_WALL_X = (float)TrackSegment.ASPHALT_TEXTURE_WIDTH/ 2 - 1;


	public NeedForSpeed(Component glPanel) {
		this.glPanel = glPanel;
		gameState = new GameState();
		gameTrack = new Track();
		carCameraTranslation = new Vec(0.0);
		car = new F1Car();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		if (!isModelInitialized) {
			initModel(gl);
		}
		if (isDayMode) {
			gl.glClearColor(0.52f, 0.824f, 1.0f, 1.0f);
		} else {
			gl.glClearColor(0.0f, 0.0f, 0.32f, 1.0f);
		}
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		// Step (1) You should update the accumulated translation that needs to be
		// applied on the car, camera and light sources.
		updateCarCameraTranslation(gl);
		// Step (2) Position the camera and setup its orientation
		setupCamera(gl);
		// Step (3) setup the lighting.
		setupLights(gl);
		// Step (4) render the car.
		renderCar(gl);
		// Step (5) render the track.
		renderTrack(gl);
	}

	private void updateCarCameraTranslation(GL2 gl) {
		// Update the car and camera translation value
		this.carCameraTranslation = this.carCameraTranslation.add(this.gameState.getNextTranslation());

		// bound the car by the imaginary wall
		carCameraTranslation.x = carCameraTranslation.x > IMAGINARY_WALL_X ? IMAGINARY_WALL_X : carCameraTranslation.x;
		carCameraTranslation.x = carCameraTranslation.x < -IMAGINARY_WALL_X ? -IMAGINARY_WALL_X : carCameraTranslation.x;

		// if we pass the segment length we need to move to the next segment
		if(this.carCameraTranslation.z * (-1) >= Track.SEGMENT_LENGTH){
			// go to next segment
			this.gameTrack.changeTrack(gl);
			// new translation value
			this.carCameraTranslation.z = this.carCameraTranslation.z %  Track.SEGMENT_LENGTH;
		}
	}

	private void setupCamera(GL2 gl) {
		GLU glu = new GLU();
		glu.gluLookAt(initialCameraPosition.x + carCameraTranslation.x,
				initialCameraPosition.y + carCameraTranslation.y,
				initialCameraPosition.z + carCameraTranslation.z ,
				carCameraTranslation.x,
				1.5 + carCameraTranslation.y,
				-5.0 +carCameraTranslation.z, 0,1,0);
	}


	private void setupLights(GL2 gl) {
		if (isDayMode) {
			// switch-off any light sources that were used in night mode
			gl.glDisable(GL2.GL_LIGHT0);
			gl.glDisable(GL2.GL_LIGHT1);

			//switch off moonlight
			gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);

			// turn on day light
			turnOnDayLight(gl);

		} else {
			// switch-off any light sources that are used in day mode
			gl.glDisable(GL2.GL_LIGHT0);

			// Setup night lighting
			turnOnNightLights(gl);
		}
	}

	private void turnOnDayLight(GL2 gl){
		Vec lightDirection = new Vec(0,1,1).normalize();

		// set light position
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[]{lightDirection.x,
				lightDirection.y, lightDirection.z, 0}, 0);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[]{0.2f, 0.2f, 0.2f, 1f}, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[]{1f, 1f, 1f, 1f}, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[]{1f, 1f, 1f, 1f}, 0);

		gl.glEnable(GL2.GL_LIGHT0);
	}

	private void turnOnNightLights(GL2 gl){
		float[] lightDirection = new float[]{0.0f, -1.0f, 0.0f};

		// light 0
		turnOnSpotlight(gl,GL2.GL_LIGHT0, new float[]{carCameraTranslation.x + 2,
				carCameraTranslation.y + 5, carCameraTranslation.z,1.0f }, lightDirection);
		// light 1
		turnOnSpotlight(gl,GL2.GL_LIGHT1, new float[]{carCameraTranslation.x - 1,
				carCameraTranslation.y + 5, carCameraTranslation.z,1.0f }, lightDirection);
		turnOnMoonlight(gl);
	}


	private void turnOnSpotlight(GL2 gl, int light, float[] lightPosition, float[] lightDirection){

		// set light position
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);

		// set light direction
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION, lightDirection, 0);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[]{1f, 1f, 1f, 1f}, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[]{1f, 1f, 1f, 1f}, 0);

		// maximum spread angle
		gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, 90);

		gl.glEnable(light);
	}

	private void turnOnMoonlight(GL2 gl) {
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{0.2f, 0.2f, 0.2f, 1.0f}, 0);
	}

	private void renderTrack(GL2 gl) {
		gl.glPushMatrix();
		gameTrack.render(gl);
		gl.glPopMatrix();
	}

	private void renderCar(GL2 gl) {
		gl.glPushMatrix();

		// car position = initial position + the accumulated translation
		gl.glTranslated(initialCarPosition.x + carCameraTranslation.x,
				initialCarPosition.y + carCameraTranslation.y,
				initialCarPosition.z + carCameraTranslation.z);

		// rotate around y axis in -carRotation degrees
		gl.glRotated(-this.gameState.getCarRotation(),0.0,1.0,0.0);

		// rotate the car so it will align the z axis
		gl.glRotated(90,0,1,0);

		// scale the car
		gl.glScaled(4,4,4);

		car.render(gl);

		gl.glPopMatrix();
	}


	public GameState getGameState() {
		return gameState;
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gameTrack.destroy(gl);
		car.destroy(gl);
	}


	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// Initialize display callback timer
		ani = new FPSAnimator(30, true);
		ani.add(drawable);
		glPanel.repaint();

		initModel(gl);
		ani.start();
	}

	public void initModel(GL2 gl) {
		gl.glCullFace(GL2.GL_BACK);
		gl.glEnable(GL2.GL_CULL_FACE);

		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_SMOOTH);

		car.init(gl);
		gameTrack.init(gl);
		isModelInitialized = true;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

		GLU glu = new GLU();
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(gl.GL_PROJECTION);
		gl.glLoadIdentity();

		// near = 2 (the distance from the camera to the scene)
		// far = 502 (the volume depth should be 500)
		glu.gluPerspective(57.0, width/height, 2.0, 502.0);
	}

	/**
	 * Start redrawing the scene with 30 FPS
	 */
	public void startAnimation() {
		if (!ani.isAnimating())
			ani.start();
	}

	/**
	 * Stop redrawing the scene with 30 FPS
	 */
	public void stopAnimation() {
		if (ani.isAnimating())
			ani.stop();
	}

	public void toggleNightMode() {
		isDayMode = !isDayMode;
	}

}