package vvv.test.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class GameScreen implements Screen {
	final MatchThree game;
	private OrthographicCamera camera;
	private Texture backgroundImage;

	private Matrix matrix;
	private MatrixCalculations matrixCalculations;
	private Point tileCoordinates;
	private Input inputProcessor;
	
	public GameScreen(final MatchThree gameInstance) {
		this.game = gameInstance;
		
		backgroundImage = new Texture(Gdx.files.internal("background.png"));
	
		camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 480);

        matrix = new Matrix();
        matrix.fillRandomly();
        //matrix.fillTestValues();
        
        matrixCalculations = new MatrixCalculations(matrix);
        
        inputProcessor = new Input(camera, matrix, matrixCalculations);
	}

	@Override
	public void show() {
		Timer.schedule(new Task() {
		    @Override
		    public void run() {
		    	ArrayList<Integer> matches = matrixCalculations.detectMatchesGlobally();
		    	
		    	if ( !matches.isEmpty() ) {
		    		matrix.removeTilesByIdAndShiftDownMatrix(matches);
		    	} else {
		    		Gdx.input.setInputProcessor(inputProcessor);
		    		Timer.instance().clear();
		    	}
		    }
		}, 2, 2);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(backgroundImage, 0, 0);
		drawTileMatrix(game.batch);
		game.batch.end();
	}
	
	private void drawTileMatrix(SpriteBatch batch) {
		Texture tileTexture;

		for (int row = 0; row < Matrix.HEIGHT; row++) {
			for (int column = 0; column < Matrix.WIDTH; column++) {
				tileTexture = matrix.getTileTexture(row, column);
				
				if (tileTexture != null) {
					tileCoordinates = MatrixCalculations.getCoordinates(row, column);
					batch.draw(
							tileTexture,
							tileCoordinates.getX(),
							tileCoordinates.getY()
							);
				}
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		backgroundImage.dispose();
	}

}
