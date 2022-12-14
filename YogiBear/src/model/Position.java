package model;

public class Position {

    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param d a direction, either LEFT, RIGHT, UP or DOWN
     * @return returns a the new set of coordinates based on the direction
     */
    public Position translate(Direction d) {
        return new Position(x + d.x, y + d.y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
