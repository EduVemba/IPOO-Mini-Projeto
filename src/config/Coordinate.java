package config;

public class Coordinate {
    private int x, y;

    /**
     *
     * @param x Row
     * @param y Column
     */
    public Coordinate(int x,int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("%c %d", (char)('A' + x), y);
    }
}
