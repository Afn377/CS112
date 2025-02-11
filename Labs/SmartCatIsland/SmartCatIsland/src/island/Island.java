package island;

/**
 * Island class which holds tiles, cat, and yarn
 */
public class Island {

    // Array of tiles which makes up this island
    private Tile[][] island;

    /**
     * Constructor which makes an island
     * 
     * @param island
     */
    public Island(String[][] island) {
        this.island = Island.createIsland(island);
    }

    /**
     * Alternate private island constructor which takes in a finished Tile array
     * Used in the Island.copyIsland() method, which deep copies each tile into a
     * new island
     * 
     * @param island
     */
    private Island(Tile[][] island) {
        this.island = island;
    }

    public Tile[][] getTiles() {
        return island;
    }

    public static Tile[][] createIsland(String[][] toParse) {
        Tile[][] parsed = new Tile[toParse.length][toParse[0].length];
        for (int i = 0; i < toParse.length; i++) {
            for (int j = 0; j < toParse[i].length; j++) {
                if (toParse[i][j].toUpperCase().equals(Tile.LAND)) {
                    parsed[i][j] = new Tile(Tile.LAND, false, i, j);
                } else if (toParse[i][j].toUpperCase().equals(Tile.WATER)) {
                    parsed[i][j] = new Tile(Tile.WATER, false, i, j);
                } else if (toParse[i][j].toUpperCase().equals("Y")) {
                    parsed[i][j] = new Tile(Tile.LAND, true, i, j);
                }
            }
        }
        return parsed;
    }

    public static Island copyIsland(Island s) {
        if (s == null) {
            return null;
        }
        Tile[][] copyArr = new Tile[s.getTiles().length][s.getTiles()[0].length];
        Island copy = new Island(copyArr);
        for (int i = 0; i < copyArr.length; i++) {
            for (int j = 0; j < copyArr[i].length; j++) {
                copyArr[i][j] = new Tile(s.getTiles()[i][j].type, s.getTiles()[i][j].hasYarn, i, j);
                if (s.getTiles()[i][j].cat != null) {
                    SmartCat c = (SmartCat) s.getTiles()[i][j].cat;
                    copyArr[i][j].cat = new SmartCat(c.getName(), copy, c.getRow(), c.getCol(), c.getColor());
                }
            }
        }
        return copy;
    }

}
