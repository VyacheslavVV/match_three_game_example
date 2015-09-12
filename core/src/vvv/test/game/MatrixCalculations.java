package vvv.test.game;

import java.util.ArrayList;

public class MatrixCalculations {
	private Matrix matrix;

	//private static final int SCREEN_WIDTH = 320;
	private static final int SCREEN_HEIGHT = 480;
	private static final int MATRIX_PADDING = 12; // left and upper
	private static final int TILE_PADDING = 10; // right and bottom
	private static final int TILE_DIMENSION = 28; // width and height
	
	private static int maxX = MATRIX_PADDING + Matrix.WIDTH * TILE_DIMENSION + (Matrix.WIDTH - 1) * TILE_PADDING;
	private static int minY = SCREEN_HEIGHT -
			(MATRIX_PADDING + Matrix.HEIGHT * TILE_DIMENSION + (Matrix.HEIGHT - 1) * TILE_PADDING);
	
	public MatrixCalculations(Matrix matrix) {
		this.matrix = matrix;
	}

	public int getTileIdByCoordinates(float x, float y) {
		int result = -1;
		
		if (x >= MATRIX_PADDING && x <= maxX && y <= SCREEN_HEIGHT - MATRIX_PADDING && y >= minY) {
			int foundRow = -1;
			int foundColumn = -1;
			float rowY = 0;
			float columnX = 0;

			for (int row = 0; row < Matrix.HEIGHT; row++) {
				rowY = getYForRow(row);

				if (y >= rowY && y <= rowY + TILE_DIMENSION) {
					foundRow = row;
					break;
				} 
			}
			
			for (int column = 0; column < Matrix.WIDTH; column++) {
				columnX = getXForColumn(column);

				if (x >= columnX && x <= columnX + TILE_DIMENSION) {
					foundColumn = column;
					break;
				} 
			}
			
			result = matrix.getTileId(foundRow, foundColumn);
		}

		return result;
	}
	
	public ArrayList<Integer> detectMatchesGlobally() {
		ArrayList<Integer> matchedTileIds = new ArrayList<Integer>();
//		ArrayList<Integer> temporaryMatch = new ArrayList<Integer>();
//		int tileId, matchTileId, tileTypeId;
		
		for (int column = 0; column < Matrix.WIDTH; column++) {
			matchedTileIds = addUniqieValuesToList(
					matchedTileIds,
					getVerticallyMatchedTiles(column)
					);
		}
		
		for (int row = 0; row < Matrix.HEIGHT; row++) {
			matchedTileIds = addUniqieValuesToList(
					matchedTileIds,
					getHorizontalyMatchedTiles(row)
					);
		}
		
//		for (int column = 0; column < Matrix.HEIGHT; column++) {
//			for (int row = 0; row < Matrix.WIDTH; row++) {
//
//				tileId = matrix.getTileId(row, column);
//				tileTypeId = matrix.getTileType(tileId);
//				
//				if (matchedTileIds.contains(tileId) || tileTypeId == -1) {
//					continue;
//				}
//				
//				matchedTileIds = addUniqieValuesToList(
//						matchedTileIds,
//						getHorizontalyMatchedTiles(matrix.getRowByTileId(leftTileId))
//						);
//				
//				matchedTileIds = addUniqieValuesToList(
//						matchedTileIds,
//						getVerticallyMatchedTiles(leftTileColumn)
//						);

//				if (row < Matrix.WIDTH - 2) {
//					temporaryMatch.add(tileId);
//	
//					for (int matchRow = row + 1; matchRow < Matrix.WIDTH; matchRow++) {
//						matchTileId = matrix.getTileId(matchRow, column);
//	
//						if (tileTypeId != matrix.getTileType(matchTileId)) {
//							break;
//						}
//						
//						temporaryMatch.add(matchTileId);
//					}
//					
//					if (temporaryMatch.size() > 2) {
//						matchedTileIds.addAll(temporaryMatch);
//					}
//					
//					temporaryMatch.clear();
//				}
//
//				if (column < Matrix.HEIGHT - 2) {
//					temporaryMatch.add(tileId);
//					
//					for (int matchColumn = column + 1; matchColumn < Matrix.HEIGHT; matchColumn++) {
//						matchTileId = matrix.getTileId(row, matchColumn);
//	
//						if (tileTypeId != matrix.getTileType(matchTileId)) {
//							break;
//						}
//						
//						temporaryMatch.add(matchTileId);
//					}
//					
//					if (temporaryMatch.size() > 2) {
//						matchedTileIds.addAll(temporaryMatch);
//					}
//					
//					temporaryMatch.clear();
//				}
//			}
//		}
		
		return matchedTileIds;
	}
	
	public ArrayList<Integer> detectMatches(int firstTileId, int secondTileId) {
		ArrayList<Integer> matchedTileIds = new ArrayList<Integer>();

		// Horizontal tile exchange
		if (matrix.getColumnByTileId(firstTileId) != matrix.getColumnByTileId(secondTileId)) {
			int leftTileId = (firstTileId < secondTileId) ? firstTileId : secondTileId;
			int leftTileColumn = matrix.getColumnByTileId(leftTileId);
			
			matchedTileIds = addUniqieValuesToList(
					matchedTileIds,
					getHorizontalyMatchedTiles(matrix.getRowByTileId(leftTileId))
					);
			
			matchedTileIds = addUniqieValuesToList(
					matchedTileIds,
					getVerticallyMatchedTiles(leftTileColumn)
					);
			
			matchedTileIds = addUniqieValuesToList(
					matchedTileIds,
					getVerticallyMatchedTiles(leftTileColumn + 1)
					);
		}
		// Vertical tile exchange
		else {
			int lowerTileId = (firstTileId > secondTileId) ? firstTileId : secondTileId;
			int lowerTileRow = matrix.getRowByTileId(lowerTileId);

			matchedTileIds = addUniqieValuesToList(
					matchedTileIds,
					getVerticallyMatchedTiles(matrix.getColumnByTileId(lowerTileId))
					);
			
			matchedTileIds = addUniqieValuesToList(
					matchedTileIds,
					getHorizontalyMatchedTiles(lowerTileRow - 1)
					);

			matchedTileIds = addUniqieValuesToList(
					matchedTileIds,
					getHorizontalyMatchedTiles(lowerTileRow)
					);
		}

		return matchedTileIds;
	}
	
	private ArrayList<Integer> addUniqieValuesToList(
			ArrayList<Integer> destinationList,
			ArrayList<Integer> sourceList
			) {
		for (int item : sourceList) {
			if (!destinationList.contains(item)) {
				destinationList.add(item);
			}
		}
		
		return destinationList;
	}
	
	private ArrayList<Integer> getVerticallyMatchedTiles(
			int column
			) {
		ArrayList<Integer> matchedTileIds = new ArrayList<Integer>();
		ArrayList<Integer> temporaryIdStorage = new ArrayList<Integer>();
		int comparisonTileId;
		int comparisonTileType;
		int matchTileId;

		for (int row = 0; row < Matrix.HEIGHT - 2; row++) {
			comparisonTileId = matrix.getTileId(row, column);
			
			if (matchedTileIds.contains(comparisonTileId)) {
				continue;
			}

			comparisonTileType = matrix.getTileType(comparisonTileId);
			temporaryIdStorage.add(comparisonTileId);
			
			for (int matchRow = row + 1; matchRow < Matrix.HEIGHT; matchRow++) {
				matchTileId = matrix.getTileId(matchRow, column);

				if (comparisonTileType != matrix.getTileType(matchTileId)) {
					break;
				}
				
				temporaryIdStorage.add(matchTileId);
			}

			if (temporaryIdStorage.size() > 2) {
				matchedTileIds.addAll(temporaryIdStorage);
			}
			
			temporaryIdStorage.clear();
		}
		
		return matchedTileIds;
	}
	
	private ArrayList<Integer> getHorizontalyMatchedTiles(
			int row
			) {
		ArrayList<Integer> matchedTileIds = new ArrayList<Integer>();
		ArrayList<Integer> temporaryIdStorage = new ArrayList<Integer>();
		int comparisonTileId;
		int comparisonTileType;
		int matchTileId;
		
		for (int column = 0; column < Matrix.WIDTH - 2; column++) {
			comparisonTileId = matrix.getTileId(row, column);
			
			if (matchedTileIds.contains(comparisonTileId)) {
				continue;
			}

			comparisonTileType = matrix.getTileType(comparisonTileId);
			temporaryIdStorage.add(comparisonTileId);
			
			for (int matchColumn = column + 1; matchColumn < Matrix.WIDTH; matchColumn++) {
				matchTileId = matrix.getTileId(row, matchColumn);

				if (comparisonTileType != matrix.getTileType(matchTileId)) {
					break;
				}
				
				temporaryIdStorage.add(matchTileId);
			}

			if (temporaryIdStorage.size() > 2) {
				matchedTileIds.addAll(temporaryIdStorage);
			}
			
			temporaryIdStorage.clear();
		}
		
		return matchedTileIds;
	}

	public static Point getCoordinates(int row, int column) {
		float x = getXForColumn(column);
		float y = getYForRow(row);

		return new Point(x, y);
	}

	private static float getYForRow(int row) {
		return SCREEN_HEIGHT - (MATRIX_PADDING + (row + 1) * TILE_DIMENSION + row * TILE_PADDING);
	}
	
	private static float getXForColumn(int column) {
		return MATRIX_PADDING + column * TILE_DIMENSION + column * TILE_PADDING;
	}
}
