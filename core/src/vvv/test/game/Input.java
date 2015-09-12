package vvv.test.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Input implements InputProcessor {
	
	private OrthographicCamera camera;
	private Matrix matrix;
	private MatrixCalculations matrixCalculations;
	private int touchedTileId = -1;
	
	public Input(OrthographicCamera camera, Matrix matrix, MatrixCalculations matrixCalculations) {
		this.camera = camera;
		this.matrix = matrix;
		this.matrixCalculations = matrixCalculations;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 touchVector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
	    camera.unproject(touchVector);
		
	    touchedTileId = matrixCalculations.getTileIdByCoordinates(touchVector.x, touchVector.y);

	    if (touchedTileId > 0) {
	    	// TODO: play sound
	    }

	    return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 touchVector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(touchVector);
		
		processMove(
				matrixCalculations.getTileIdByCoordinates(touchVector.x, touchVector.y)
				);

		return true;
	}
	
	private void processMove(int neighbourTileId) {
		if (touchedTileId != -1 && neighbourTileId != -1) {
			try {
				if (matrix.swapTiles(touchedTileId, neighbourTileId)) {

					Gdx.input.setInputProcessor(null);

					final Input _this = this;
					final ArrayList<Integer> matches = matrixCalculations.detectMatches(touchedTileId, neighbourTileId);
					
					if (!matches.isEmpty()) {

						Timer.schedule(new Task() {
							boolean firstRun = true;

							@Override
						    public void run() {
						    	
						    	if (firstRun) {
						    		firstRun = false;
						    		
						    		matrix.removeTilesByIdAndShiftDownMatrix(matches);
						    	} else {
							    	ArrayList<Integer> globalMatches = matrixCalculations.detectMatchesGlobally();
							    	
							    	if (!globalMatches.isEmpty()) {
							    		matrix.removeTilesByIdAndShiftDownMatrix(globalMatches);
							    	} else {
							    		Gdx.input.setInputProcessor(_this);
							    		Timer.instance().clear();
							    	}
						    	}
						    }
						}, 2, 2);						

					} else {
						
						final int _neighbourTileId = neighbourTileId;

						Timer.schedule(new Task() {
						    @Override
						    public void run() {
						    	try {
									matrix.swapTiles(_neighbourTileId, touchedTileId);
									Gdx.input.setInputProcessor(_this);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						    }
						}, 1);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
