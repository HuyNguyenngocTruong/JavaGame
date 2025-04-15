package huynguyen.ara.nz;

import java.util.ArrayList;
import java.util.List;

class Level{
    private final int height;
    private final int width;
    private final List<Goal> goals;
    private Square[][] squares;
    
    public Level(int height, int width) {
        this.height = height;
        this.width = width;
        this.goals = new ArrayList<>();
        this.squares = new Square[height][width];
        
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                squares[row][col] = new BlankSquare();
            }
        }
    }

    public int getLevelHeight() {
        return height;
    }

    public int getLevelWidth() {
        return width;
    }

    public void addGoal(Goal goal) {
        int row = goal.getRow();
        int col = goal.getColumn();

        if (row < 0 || row >= height || col < 0 || col >= width) {
            throw new IllegalArgumentException("Goal position out of bounds.");
        }
        
        goals.add(goal);
    }


    public int getGoalCount() {
        return goals.size();
    }

    public boolean hasGoalAt(int row, int column) {
        return goals.stream().anyMatch(goal -> goal.getRow() == row && goal.getColumn() == column);
    }

    public int getCompletedGoalCount() {
        return (int) goals.stream().filter(Goal::isCompleted).count();
    }
    
    public void removeGoal(int row, int col) {
        goals.removeIf(goal -> goal.getRow() == row && goal.getColumn() == col);
    }
    
    public void setSquare(Square square, int row, int column) {
        squares[row][column] = square;
    }

    public Color getColorAt(int row, int column) {
        return squares[row][column].getColor();
    }

    public Shape getShapeAt(int row, int column) {
        return squares[row][column].getShape();
    }

	public Square getSquare(int row, int column) {
		return squares [row][column];
	}
	
	public boolean hasBlankBetween(int startRow, int startCol, int endRow, int endCol) {
      int min = Math.min(startRow, endRow);
      int max = Math.max(startRow, endRow);
      for (int i = min + 1; i < max; i++) {
          if (squares[i][startCol] instanceof BlankSquare) return true;
      }
      return false;
  }
}
