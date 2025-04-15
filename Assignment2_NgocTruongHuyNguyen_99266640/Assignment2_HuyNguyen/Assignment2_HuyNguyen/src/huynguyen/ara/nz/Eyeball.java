package huynguyen.ara.nz;

class Eyeball implements IEyeballHolder {
    private int row;
    private int column;
    private Direction direction;

    
    public Eyeball(int row, int column, Direction direction) {
        this.row = row;
        this.column = column;
        this.direction = direction;
    }
    
    @Override
    public void addEyeball(int row, int column, Direction direction) {
        this.row = row;
        this.column = column;
        this.direction = direction;
    }

    @Override
    public int getEyeballRow() {
        return row;
    }

    @Override
    public int getEyeballColumn() {
        return column;
    }

    @Override
    public Direction getEyeballDirection() {
        return direction;
    }

    public void moveTo(int newRow, int newColumn) {
        this.row = newRow;
        this.column = newColumn;
    }
    

    public void setRow(int row) { this.row = row; }
    public void setColumn(int column) { this.column = column; }
    public void setDirection(Direction direction) { this.direction = direction; }
}
