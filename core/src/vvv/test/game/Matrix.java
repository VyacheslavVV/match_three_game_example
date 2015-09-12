package vvv.test.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Matrix {
	public static final int WIDTH = 8;
	public static final int HEIGHT = 8;
	private int[] tileContainer = new int[WIDTH * HEIGHT];

	private static final int TILE_TYPE_TRIANGLE = 0;
	private static final int TILE_TYPE_CIRCLE	= 1;
	private static final int TILE_TYPE_SQUARE	= 2;
	private static final int TILE_TYPE_DIAMOND	= 3;
	private static final int TILE_TYPE_PENTAGON	= 4;
	private static final int TILE_TYPE_TRAPEZIUM= 5;

	private Texture tileTriangle;
	private Texture tileCircle;
	private Texture tileRectangle;
	private Texture tileDiamond;
	private Texture tilePentagon;
	private Texture tileTrapezium;
	
	public Matrix() {
		tileTriangle	= new Texture(Gdx.files.internal("tile_triangle.png"));
		tileCircle		= new Texture(Gdx.files.internal("tile_circle.png"));
		tileRectangle	= new Texture(Gdx.files.internal("tile_rectangle.png"));
		tileDiamond		= new Texture(Gdx.files.internal("tile_diamond.png"));
		tilePentagon	= new Texture(Gdx.files.internal("tile_pentagon.png"));
		tileTrapezium	= new Texture(Gdx.files.internal("tile_trapezium.png"));
	}

	public void fillRandomly() {
		Random rn = new Random();

		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			tileContainer[i] = rn.nextInt(6);
		}
	}
	
	public void fillTestValues() {

		// no matches
		tileContainer = new int[] {
				0, 1, 2, 3, 4, 5, 0, 1,
				1, 2, 3, 4, 5, 0, 1, 2,
				2, 3, 4, 5, 0, 1, 2, 3,
				3, 4, 5, 0, 1, 2, 3, 4,
				4, 5, 0, 1, 2, 3, 4, 5,
				5, 0, 1, 2, 3, 4, 5, 0,
				0, 1, 2, 3, 4, 5, 0, 1,
				1, 2, 3, 4, 5, 0, 1, 2,
				};
		
		tileContainer = new int[] {
				0, 1, 2, 3, 4, 5, 0, 1,
				1, 2, 3, 4, 5, 0, 1, 2,
				2, 3, 4, 5, 0, 1, 2, 3,
				3, 4, 5, 0, 1, 2, 3, 4,
				4, 5, 0, 1, 2, 3, 4, 5,
				5, 0, 1, 2, 3, 4, 5, 1,
				0, 1, 2, 3, 4, 5, 0, 1,
				1, 2, 3, 4, 5, 0, 1, 1,
				};
	}
	
	public void fancyPrintOut() {
		for (int row = 0; row < WIDTH; row++) {
			for (int column = 0; column < HEIGHT; column++) {
				System.out.print(tileContainer[row * WIDTH + column] + ", ");
			}

			System.out.println();
		}
	}
	
	public Texture getTileTexture(int row, int column) {
		Texture result = null;

		switch (tileContainer[row * WIDTH + column]) {
			case TILE_TYPE_TRIANGLE:
				result = tileTriangle;
				break;
				
			case TILE_TYPE_CIRCLE:
				result = tileCircle;
				break;
				
			case TILE_TYPE_SQUARE:
				result = tileRectangle;
				break;
				
			case TILE_TYPE_DIAMOND:
				result = tileDiamond;
				break;

			case TILE_TYPE_PENTAGON:
				result = tilePentagon;
				break;

			case TILE_TYPE_TRAPEZIUM:
				result = tileTrapezium;
				break;
		}

		return result;
	}
	
	public int getTileType(int row, int column) {
		return tileContainer[row * WIDTH + column];
	}
	
	public int getTileType(int tileId) {
		return tileContainer[tileId];
	}
	
	public int getTileId(int row, int column) {
		return row * WIDTH + column;
	}
	
	public int getColumnByTileId(int tileId) {
		return tileId % WIDTH;
	}
	
	public int getRowByTileId(int tileId) {
		return (int) (tileId / WIDTH);
	}
	
	public boolean swapTiles(int firstTileId, int secondTileId) throws Exception {
		boolean tilesSwapped = false;

		if (tilesCanBeSwapped(firstTileId, secondTileId)) {
			int firstTileType = tileContainer[firstTileId];
			tileContainer[firstTileId] = tileContainer[secondTileId];
			tileContainer[secondTileId] = firstTileType;
			
			tilesSwapped = true;
		}
		
		return tilesSwapped;
	}
	
	public void removeTilesByIdAndShiftDownMatrix(ArrayList<Integer> tileIdList) {
		removeTilesById(tileIdList);
		
		shiftMatrix();
		
		fillEmptyTiles();
	}
	
	private void removeTilesById(ArrayList<Integer> tileIdList) {
		for (int tileId : tileIdList) {
			if (tileId > 0 && tileId < WIDTH * HEIGHT) {
				tileContainer[tileId] = -1;
			}
		}
	}
	
	private void shiftMatrix() {
		boolean tilesFound;
		int upperTileType;
		
		for (int row = HEIGHT - 1; row > 0; row--) {
			for (int column = 0; column < WIDTH; column++) {
				
				tilesFound = true;

				while (tileContainer[row * WIDTH + column] == -1 && tilesFound) {
					tilesFound = false;
					for (int shiftRow = row; shiftRow > 0; shiftRow--) {
						upperTileType = tileContainer[(shiftRow - 1) * WIDTH + column];
						
						if (upperTileType != -1) {
							tilesFound = true;
						}
						
						tileContainer[shiftRow * WIDTH + column] = upperTileType;
						tileContainer[(shiftRow - 1) * WIDTH + column] = -1;
					}
				}
			}
		}
	}
	
	private void fillEmptyTiles() {
		Random rn = new Random();
		
		for (int row = HEIGHT - 1; row >= 0; row--) {
			for (int column = 0; column < WIDTH; column++) {
				if (tileContainer[row * WIDTH + column] == -1) {
					tileContainer[row * WIDTH + column] = rn.nextInt(6);
				}
			}
		}
	}
	
	private boolean tilesCanBeSwapped(int firstTileId, int secondTileId) throws Exception {
		if (firstTileId < 0 || firstTileId >= WIDTH * HEIGHT) {
			throw new Exception("First tile ID is invalid");
		}
		
		if (secondTileId < 0 || secondTileId >= WIDTH * HEIGHT) {
			throw new Exception("Second tile ID is invalid");
		}
		
		if (!isTileANeighbour(firstTileId, secondTileId)) {
			throw new Exception("Tiles are not neighbours");
		}
		
		return true;
	}
	
	private boolean isTileANeighbour(int firstTileId, int secondTileId) {
		boolean isNeighbour = false;

		if (firstTileId != 0 && firstTileId % WIDTH != 0 && (firstTileId - 1) == secondTileId) {
			isNeighbour = true;
		}
		else if (firstTileId == 0 || ((firstTileId + 1) % WIDTH != 0) && (firstTileId + 1) == secondTileId) {
			isNeighbour = true;
		}
		else if (firstTileId > (WIDTH - 1) && (firstTileId - WIDTH) == secondTileId) {
			isNeighbour = true;
		}
		else if (firstTileId < (WIDTH * HEIGHT - WIDTH) && (firstTileId + WIDTH) == secondTileId) {
			isNeighbour = true;
		}

		return isNeighbour;
	}
}
