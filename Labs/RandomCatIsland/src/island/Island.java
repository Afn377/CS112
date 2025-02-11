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

    public static Island createIsland(Tile[][] toParse) {
        return new Island(toParse);
    }

    public static Tile[][] createIsland(String[][] toParse) {
        Tile[][] parsed = new Tile[toParse.length][toParse[0].length];
        for (int i = 0; i < toParse.length; i++) {
            for (int j = 0; j < toParse[i].length; j++) {
                if (toParse[i][j].equals(Tile.LAND)) {
                    parsed[i][j] = new Tile(Tile.LAND, false, i, j);
                } else if (toParse[i][j].equals(Tile.WATER)) {
                    parsed[i][j] = new Tile(Tile.WATER, false, i, j);
                } else if (toParse[i][j].equals("Y")) {
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
                    Cat c = s.getTiles()[i][j].cat;
                    copyArr[i][j].cat = new Cat(c.getName(), copy, c.getRow(), c.getCol(), c.getColor());
                }
            }
        }
        return copy;
    }

    public int numLandTiles() {
        return getCountOf(Tile.LAND);
    }

    public int numWaterTiles() {
        return getCountOf(Tile.WATER);
    }

    public int numYarnTiles() {
        int res = 0;
        for (int i = 0; i < island.length; i++) {
            for (int j = 0; j < island[0].length; j++) {
                if (island[i][j].hasYarn) {
                    res++;
                }
            }
        }
        return res;
    }

    private int getCountOf(String type) {
        int res = 0;
        for (int i = 0; i < island.length; i++) {
            for (int j = 0; j < island[0].length; j++) {
                if (island[i][j].type.equals(type)) {
                    res++;
                }
            }
        }
        return res;
    }

}
