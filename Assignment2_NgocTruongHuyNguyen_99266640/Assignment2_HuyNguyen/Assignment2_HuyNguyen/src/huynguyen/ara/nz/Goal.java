package huynguyen.ara.nz;

public class Goal {
    private int row;
    private int column;
    private boolean completed;

    public Goal(int row, int column) {
        this.row = row;
        this.column = column;
        this.completed = false;  
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

