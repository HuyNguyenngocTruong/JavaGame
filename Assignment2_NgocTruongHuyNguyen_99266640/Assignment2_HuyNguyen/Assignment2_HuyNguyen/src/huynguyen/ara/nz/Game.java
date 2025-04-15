package huynguyen.ara.nz;

import java.util.ArrayList;
import java.util.List;

public class Game implements IGoalHolder, ILevelHolder, ISquareHolder, IMoving, IEyeballHolder {
    private List<Level> levels;
    private int currentLevelIndex;
    private int completedGoals;
    private Eyeball eyeball;
    boolean goalAtPrePosi;

    public Game() {
        this.levels = new ArrayList<>();
        this.currentLevelIndex = -1;
        this.completedGoals = 0;
        
    }

    // Level management
    public void addLevel(int height, int width) {
    	levels.add(new Level(height, width));
    	currentLevelIndex = levels.size() - 1; // Set the most recently added level as the current level
	  }
    
    public void addSquare(Square square, int row, int column) {
    	if (currentLevelIndex == -1) {
    		throw new IllegalStateException("No level available to add squares.");
	    }
	
	    	Level currentLevel = levels.get(currentLevelIndex);
	    if (row < 0 || row >= currentLevel.getLevelHeight() || column < 0 || column >= currentLevel.getLevelWidth()) {
	    	throw new IllegalArgumentException("Square position out of bounds.");
	    }
	    currentLevel.setSquare(square, row, column);
    }

    public int getLevelWidth() {
        if (currentLevelIndex == -1) throw new IllegalStateException("No levels added.");
        return levels.get(currentLevelIndex).getLevelWidth();
    }

    public int getLevelHeight() {
        if (currentLevelIndex == -1) throw new IllegalStateException("No levels added.");
        return levels.get(currentLevelIndex).getLevelHeight();
    }

    public int getLevelCount() {
        return levels.size();
    }

    public void setLevel(int levelIndex) {
        if (levelIndex < 0 || levelIndex >= levels.size()) {
            throw new IllegalArgumentException("Invalid level index.");
        }
        currentLevelIndex = levelIndex;
    }
    
    public void addEyeball(int row, int column, Direction direction) {
      if (currentLevelIndex == -1) {
          throw new IllegalStateException("No level available to add an Eyeball.");
      }

      Level currentLevel = levels.get(currentLevelIndex);
      if (row < 0 || row >= currentLevel.getLevelHeight() || column < 0 || column >= currentLevel.getLevelWidth()) {
          throw new IllegalArgumentException("Eyeball position out of bounds.");
      }

      this.eyeball = new Eyeball(row, column, direction);
  }
 
    public void addGoal(int row, int column) {
        if (currentLevelIndex == -1) throw new IllegalStateException("No level to add goals.");
        levels.get(currentLevelIndex).addGoal(new Goal(row, column));
    }

    public int getGoalCount() {
        if (currentLevelIndex == -1) return 0;
        return levels.get(currentLevelIndex).getGoalCount();
    }

    public boolean hasGoalAt(int row, int col) {
        if (currentLevelIndex == -1) return false;
        return levels.get(currentLevelIndex).hasGoalAt(row, col);
    }

	public int getCompletedGoalCount() {
		return completedGoals;
	}
    
    private Square getCurrentSquare() {
        return levels.get(currentLevelIndex).getSquare(eyeball.getEyeballRow(), eyeball.getEyeballColumn());
    }

    private Square getSquareAt(int row, int col) {
        return levels.get(currentLevelIndex).getSquare(row, col);
    }
    
    public boolean canMoveTo(int row, int col) {
        if (eyeball == null) return false;

        int currentRow = eyeball.getEyeballRow();
        int currentCol = eyeball.getEyeballColumn();
        
        Square currentSquare = getCurrentSquare();
        Square targetSquare = getSquareAt(row, col);
        
        boolean isHorizontalMove = (row == currentRow) && (col != currentCol);
        boolean isVerticalMove = (col == currentCol) && (row != currentRow);
        
        if (!(isHorizontalMove || isVerticalMove)) {
            return false; 
        }
        
        if (!isDirectionOK(row, col)) {
            return false;
        }
        return targetSquare instanceof PlayableSquare &&
                (((PlayableSquare) targetSquare).matches((PlayableSquare) currentSquare));
    }
    
    public Message messageIfMovingTo(int row, int col) {
        return canMoveTo(row, col) ? Message.OK : Message.DIFFERENT_SHAPE_OR_COLOR;
    }
    
    public boolean isDirectionOK(int row, int col) {
        int currentRow = eyeball.getEyeballRow();
        int currentCol = eyeball.getEyeballColumn();
        Direction currentDirection = eyeball.getEyeballDirection();

        // Prevent diagonal movement
        if (currentRow != row && currentCol != col) {
            return false;
        }

        // Prevent backward movement
        switch (currentDirection) {
            case UP:
                if (row > currentRow) return false;
                break;
            case DOWN:
                if (row < currentRow) return false;
                break;
            case LEFT:
                if (col > currentCol) return false;
                break;
            case RIGHT:
                if (col < currentCol) return false;
                break;
        }

        return true;
    }
    
    public Message checkDirectionMessage(int row, int col) {
        int currentRow = eyeball.getEyeballRow();
        int currentCol = eyeball.getEyeballColumn();

        // Check for diagonal movement
        if (currentRow != row && currentCol != col) {
            return Message.MOVING_DIAGONALLY;
        }

        // Check for backward movement
        if (!isDirectionOK(row, col)) {
            return Message.BACKWARDS_MOVE;
        }

        return Message.OK; // Default message when movement is valid
    }
    
    public boolean hasBlankFreePathTo(int row, int col) {
        int currentRow = eyeball.getEyeballRow();
        int currentCol = eyeball.getEyeballColumn();
        Level level = levels.get(currentLevelIndex); // Get current level

        if (currentRow == row) { // Moving left or right
            int min = Math.min(currentCol, col);
            int max = Math.max(currentCol, col);
            for (int c = min + 1; c < max; c++) {
                if (c < 0 || c >= level.getLevelWidth()) return false; // Bounds check
                if (level.getSquare(currentRow, c) instanceof BlankSquare) return false;
            }
        } else if (currentCol == col) { // Moving up or down
            int min = Math.min(currentRow, row);
            int max = Math.max(currentRow, row);
            for (int r = min + 1; r < max; r++) {
                if (r < 0 || r >= level.getLevelHeight()) return false; // Bounds check
                if (level.getSquare(r, currentCol) instanceof BlankSquare) return false;
            }
        }
        return true;
    }
   
    public Message checkMessageForBlankOnPathTo(int row, int column) {
        if (hasBlankFreePathTo(row, column)) {
            return Message.OK;
        }
        return Message.MOVING_OVER_BLANK;
    }
    
    private Direction getDirectionForMove(int newRow, int newCol) {
        int oldRow = eyeball.getEyeballRow();
        int oldCol = eyeball.getEyeballColumn();

        if (newRow < oldRow) return Direction.UP;
        if (newRow > oldRow) return Direction.DOWN;
        if (newCol < oldCol) return Direction.LEFT;
        if (newCol > oldCol) return Direction.RIGHT;

        return eyeball.getEyeballDirection(); // Default to current direction (no move)
    }
    
    
    public void moveTo(int row, int col) {
        if (canMoveTo(row, col) && isDirectionOK(row, col) && hasBlankFreePathTo(row, col)) {
            int previousRow = eyeball.getEyeballRow();
            int previousCol = eyeball.getEyeballColumn();

            //DEBUG
//            System.out.println("Previous Row: " + previousRow);
//            System.out.println("Previous Col: " + previousCol);
//            System.out.println("Row: " + row);
//            System.out.println("Col: " + col);

            eyeball.setDirection(getDirectionForMove(row, col)); 
            eyeball.setRow(row);
            eyeball.setColumn(col);

            boolean isGoalAtNewPosition = hasGoalAt(row, col);
            
            // If the previous square is a goal based on goalAtPrePosi, when move to next square turn it to blank
            if (goalAtPrePosi) {
                levels.get(currentLevelIndex).setSquare(new BlankSquare(), previousRow, previousCol);
                goalAtPrePosi = false;
                // System.out.println("Set goal Blank " );
              }
            
            // Remove goal when player reach the goal and set the goalAtPrePosi to be true
            if (isGoalAtNewPosition) {
            	goalAtPrePosi = true;
            	//goals.removeIf(goal -> goal.getRow() == row && goal.getColumn() == col);  
            	levels.get(currentLevelIndex).removeGoal(row, col);
            	completedGoals++;
            }
            
        }
    }
      

    public int getEyeballRow() {
        return eyeball.getEyeballRow();
    }

    public int getEyeballColumn() {
        return eyeball.getEyeballColumn();
    }

    public Direction getEyeballDirection() {
        return eyeball.getEyeballDirection();
    }
    
	public Color getColorAt(int row, int column) {
		if (currentLevelIndex == -1) throw new IllegalStateException("No level available.");
		return levels.get(currentLevelIndex).getColorAt(row, column);
		
	}
	
	public Shape getShapeAt(int row, int column) {
		if (currentLevelIndex == -1) throw new IllegalStateException("No level available.");
			return levels.get(currentLevelIndex).getShapeAt(row, column);
	}
		
}

