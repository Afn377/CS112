package island;

import java.util.ArrayList;
import java.util.List;

import island.constants.*;

/**
 * Cat class for a single cat on a CatIsland
 * 
 * This cat can moveUp(), moveDown(), moveLeft(), and moveRight()
 * It contains attibutes such as name, coords, color
 * 
 * @author Colin Sullivan
 */
public class Cat {

    // This cats name
    private String name;
    // The amount of yarn this cat has collected
    private int yarnCollected;
    // The island this cat inhabits
    private Island homeIsland;
    // The cats current coordinates on its island
    private Coordinate coords;
    // The cats color
    private Color color;
    // The cats species. Static variable, so held by the Cat class itself instead of
    // being an object attribute.
    public static String species = "felis catus";

    /**
     * Cat constructor which initialies all attributes, and places this cat on its
     * island.
     * 
     * @param name  String name of the cat
     * @param home  Island object this cat is on
     * @param row   Row of the island (where 0 is the top row)
     * @param col   Col of the island (where 0 is the leftmost column)
     * @param color Color of this cat, from island.constants package
     */
    public Cat(String name, Island home, int row, int col, Color color) {
        this.name = name;
        this.homeIsland = home;
        this.coords = new Coordinate(row, col);
        this.color = color;
        home.getTiles()[row][col].cat = this;
    }

    /**
     * Moves this cat left on its island (col -= 1)
     * 
     * @throws CatInWaterException if the cat falls in water
     */
    public void moveLeft() throws CatInWaterException {
        if (homeIsland == null) {
            return;
        }
        int row = this.coords.getRow();
        int col = this.coords.getCol();
        Tile[][] home = homeIsland.getTiles();
        if (col > 0 && home[row][col - 1].cat == null) {
            home[row][col - 1].cat = home[row][col].cat;
            home[row][col].cat = null;
            if (home[row][col - 1].hasYarn) {
                home[row][col - 1].hasYarn = false;
                yarnCollected++;
            }
            coords.setCoords(row, col - 1);
            coords.markCoordinate();
            if (home[this.coords.getRow()][this.coords.getCol()].type == Tile.WATER) {
                throw new CatInWaterException();
            }
        }
    }

    /**
     * Moves this cat right on its island (col += 1)
     * 
     * @throws CatInWaterException if the cat falls in water
     */
    public void moveRight() throws CatInWaterException {
        if (homeIsland == null) {
            return;
        }
        int row = this.coords.getRow();
        int col = this.coords.getCol();
        Tile[][] home = homeIsland.getTiles();
        if (col < home[row].length - 1 && home[row][col + 1].cat == null) {
            home[row][col + 1].cat = home[row][col].cat;
            home[row][col].cat = null;
            if (home[row][col + 1].hasYarn) {
                home[row][col + 1].hasYarn = false;
                yarnCollected++;
            }
            coords.setCoords(row, col + 1);
            coords.markCoordinate();
            if (home[this.coords.getRow()][this.coords.getCol()].type == Tile.WATER) {
                throw new CatInWaterException();
            }
        }

    }

    /**
     * Moves this cat up on its island (row -= 1)
     * 
     * @throws CatInWaterException if the cat falls in water
     */
    public void moveUp() throws CatInWaterException {
        if (homeIsland == null) {
            return;
        }
        int row = this.coords.getRow();
        int col = this.coords.getCol();
        Tile[][] home = homeIsland.getTiles();
        if (row > 0 && home[row - 1][col].cat == null) {
            home[row - 1][col].cat = home[row][col].cat;
            home[row][col].cat = null;
            if (home[row - 1][col].hasYarn) {
                home[row - 1][col].hasYarn = false;
                yarnCollected++;
            }
            coords.setCoords(row - 1, col);
            coords.markCoordinate();
            if (home[this.coords.getRow()][this.coords.getCol()].type == Tile.WATER) {
                throw new CatInWaterException();
            }
        }
    }

    /**
     * Moves this cat down on its island (row += 1)
     * 
     * @throws CatInWaterException if the cat falls in water
     */
    public void moveDown() throws CatInWaterException {
        if (homeIsland == null) {
            return;
        }
        int row = this.coords.getRow();
        int col = this.coords.getCol();
        Tile[][] home = homeIsland.getTiles();
        if (row < homeIsland.getTiles()[0].length - 1 && home[row + 1][col].cat == null) {
            home[row + 1][col].cat = home[row][col].cat;
            home[row][col].cat = null;
            if (home[row + 1][col].hasYarn) {
                home[row + 1][col].hasYarn = false;
                yarnCollected++;
            }
            coords.setCoords(row + 1, col);
            coords.markCoordinate();
            if (home[this.coords.getRow()][this.coords.getCol()].type == Tile.WATER) {
                throw new CatInWaterException();
            }
        }
    }

    /**
     * @return This cats name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return This cats color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * @return This cats Island
     */
    public Island getIsland() {
        return this.homeIsland;
    }

    /**
     * @return This cats row on its island
     */
    public int getRow() {
        return this.coords.getRow();
    }

    /**
     * @return This cats column on its island
     */
    public int getCol() {
        return this.coords.getCol();
    }

    /**
     * @return The coordinate object for this cat
     */
    public Coordinate getCoord() {
        return this.coords;
    }

    /**
     * @return The number of yarn this cat has collected
     */
    public int numYarnCollected() {
        return yarnCollected;
    }

    /**
     * @return The number of moves this cat has made
     */
    public int numStepsTaken() {
        return this.coords.getLocationHistory().size();
    }

    //// Used for autolab ////

    /**
     * Moves this cat from its current island to the target island if possible
     * 
     * @param isle The island to move to
     * @param row  the target row
     * @param col  the target column
     */
    public void setIsland(Island isle, int row, int col) {
        if (row < 0 || row >= isle.getTiles().length || col < 0 || col >= isle.getTiles()[0].length) {
            return;
        }
        if (this.homeIsland != null && this.homeIsland.getTiles()[this.getRow()][this.getCol()].cat == this) {
            this.homeIsland.getTiles()[this.getRow()][this.getCol()].cat = null;
            this.homeIsland = null;
        }
        this.homeIsland = isle;
        this.coords.row = row;
        this.coords.col = col;
    }

    /**
     * Checks if two cats are equal
     * Ignores island/location, only compares name and color
     * 
     * @param o Object to compare to
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Cat) {
            Cat c = (Cat) o;
            if (name.equals(c.getName())) {
                if (color.equals(c.getColor())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Overriden toString method, returning the cat object as a string
     * 
     * @return {Name, Color, (row:y, col:x)}
     */
    @Override
    public String toString() {
        return "{" + this.name + ", " + this.color.toString() + ", (row:" + this.getRow() + ", col:" + this.getCol()
                + ")}";
    }

    /**
     * Inner-class which represents a (row, col) coordinate.
     * This class is static, which means it is accesible everywhere. It is
     * simply a completely seperate class written inside of the Cat class.
     */
    public static class Coordinate {
        private int row;
        private int col;
        private List<int[]> history;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
            this.history = new ArrayList<>();
            this.history.add(new int[] { row, col });
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public void setCoords(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void markCoordinate() {
            this.history.add(new int[] { row, col });
        }

        public List<int[]> getLocationHistory() {
            return history;
        }
    }
}
