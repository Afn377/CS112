package island;

/**
 * Tile class which represents one single tile
 * 
 * Can be land or water. Land tiles can have yarn, cat, or nothing on them
 * Must exist with a (row, col) value
 * 
 * @author Colin Sullivan
 */
public class Tile {

    public static final String WATER = "W";
    public static final String LAND = "L";

    public Cat cat; // The cat at this tile
    public boolean hasYarn; // If this tile has yarn on it
    public String type; // Water or Land

    public int row;
    public int col;

    public Tile(String type, boolean yarn, int row, int col) {
        this.type = type;
        this.hasYarn = yarn;
        this.row = row;
        this.col = col;
        if (this.type.equals(WATER)) {
            hasYarn = false;
        }
    }

    public boolean isLand() {
        return this.type == Tile.LAND;
    }

}
