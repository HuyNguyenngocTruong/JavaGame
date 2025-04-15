package huynguyen.ara.nz;

public class PlayableSquare extends Square {
    private final Color color;
    private final Shape shape;

    public PlayableSquare(Color color, Shape shape) {
        this.color = color;
        this.shape = shape;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Shape getShape() {
        return shape;
    }
    
    public boolean matches(PlayableSquare other) {
        return this.color == other.color || this.shape == other.shape;
    }
}
