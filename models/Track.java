package edu.cg.models;

import com.jogamp.opengl.GL2;

public class Track implements IRenderable {
	private TrackSegment currentTrackSegment = null;
	private TrackSegment nextTrackSegment = null;
	private double currentDifficulty = 0.5; // Current track segment difficulty
	private final double DIFFICULTY_DELTA = 0.05; // Difficulty difference between adjacent track segments
	private final double MAXIMUM_DIFFICULTY = 0.95;
	public static final int SEGMENT_LENGTH = 500;

	public Track() {
		currentTrackSegment = new TrackSegment(currentDifficulty);
		nextTrackSegment = new TrackSegment(currentDifficulty + DIFFICULTY_DELTA);
	}

	@Override
	public void render(GL2 gl) {

		gl.glPushMatrix();

		// render current track segment
		this.currentTrackSegment.render(gl);

		// we need to move to the end of the current track to put the next track
		gl.glTranslated(0,0, -SEGMENT_LENGTH);

		// render next track segment
		this.nextTrackSegment.render(gl);

		gl.glPopMatrix();

	}

	@Override
	public void init(GL2 gl) {
		// The init method for both segments will load the textures of the models.
		currentTrackSegment.init(gl);
		nextTrackSegment.init(gl);
	}

	@Override
	public void destroy(GL2 gl) {
		// This will destroy the textures of the track segments.
		// Note if this method is invoked, then you cannot longer render the track -
		// because the textures are not available.
		currentTrackSegment.destroy(gl);
		nextTrackSegment.destroy(gl);
		currentTrackSegment = nextTrackSegment = null;
	}

	public void changeTrack(GL2 gl) {
		// - We provided an implementation, you can change it if you want.
		TrackSegment tmp = currentTrackSegment;
		currentTrackSegment = nextTrackSegment;
		currentDifficulty += DIFFICULTY_DELTA;
		currentDifficulty = Math.min(currentDifficulty, MAXIMUM_DIFFICULTY);
		tmp.setDifficulty(currentDifficulty + DIFFICULTY_DELTA);
		nextTrackSegment = tmp;
	}

}
