package island;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import island.constants.*;

/**
 * SmartCat is a sub-class of the Cat class.
 * 
 * This means that it adds onto the existing Cat class, and inherits all of
 * the Cat attributes and methods (including name, homeIsland, row, col, and
 * more).
 * 
 * This allows us to use the same base Cat template, but expand it to include
 * additional features/attributes/methods
 * 
 * In this case, SmartCat adds three methods for walking paths, collecting all
 * yarn, and solving mazes
 */
public class SmartCat extends Cat {

    public SmartCat(String name, Island home, int row, int col, Color color) {
        // We can simply invoke the exisitng Cat constructor, with the "super" keyword
        super(name, home, row, col, color);
    }

    /**
     * Walks a single wide path from start to end,
     * similar to island 5 from lab 1
     */
    public void walkPath() {
        try {
            this.walkPathHelper(this.getIsland().getTiles()[this.getRow()][this.getCol()]);
        } catch (CatInWaterException cwe) {

        }
    }

    /**
     * Recursive helper method to walk a single wide path from
     * start to finish
     * 
     * @param prev The previous tile in the path, so we don't backtrack
     * @throws CatInWaterException If the cat falls in the water
     */
    private void walkPathHelper(Tile prev) throws CatInWaterException {
        Tile[][] tiles = this.getIsland().getTiles();
        int row = this.getRow();
        int col = this.getCol();
        if (row + 1 < tiles.length) {
            if (tiles[row + 1][col].type == Tile.LAND) {
                if (tiles[row + 1][col] != prev) {
                    this.moveDown();
                    walkPathHelper(tiles[row][col]);
                    return;
                }
            }
        }
        if (row - 1 >= 0) {
            if (tiles[row - 1][col].type == Tile.LAND) {
                if (tiles[row - 1][col] != prev) {
                    this.moveUp();
                    walkPathHelper(tiles[row][col]);
                    return;
                }
            }
        }
        if (col + 1 < tiles[0].length) {
            if (tiles[row][col + 1].type == Tile.LAND) {
                if (tiles[row][col + 1] != prev) {
                    this.moveRight();
                    walkPathHelper(tiles[row][col]);
                    return;
                }
            }
        }
        if (col - 1 >= 0) {
            if (tiles[row][col - 1].type == Tile.LAND) {
                if (tiles[row][col - 1] != prev) {
                    this.moveLeft();
                    walkPathHelper(tiles[row][col]);
                    return;
                }
            }
        }
    }

    /**
     * Collects all yarn possible to reach on the cats
     * current island
     */
    public void collectAllYarn() {
        ArrayList<Tile> yarns = new ArrayList<>();
        Tile[][] island = this.getIsland().getTiles();
        for (int i = 0; i < island.length; i++) {
            for (int j = 0; j < island[i].length; j++) {
                if (island[i][j].hasYarn) {
                    yarns.add(island[i][j]);
                }
            }
        }
        if (yarns.isEmpty()) {
            return;
        }
        yarns = this.hasPathTo(yarns); // filter yarns based on which are reachable

        while (!yarns.isEmpty()) {
            Tile min = null;
            int minDistance = Integer.MAX_VALUE; // Start with the largest possible value for comparison

            Iterator<Tile> iter = yarns.iterator();
            while (iter.hasNext()) {
                Tile t = iter.next();

                if (!t.hasYarn) {
                    iter.remove();
                    continue; // In case we already collected it along the way
                }

                // Calculate Manhattan distance from the current position to the tile 't'
                int distance = Math.abs(this.getRow() - t.row) + Math.abs(this.getCol() - t.col);

                // Update min if this tile is closer
                if (distance < minDistance) {
                    min = t;
                    minDistance = distance;
                }
            }

            if (min != null) {
                try {
                    yarns.remove(min);
                    this.navigateTo(min);
                } catch (CatInWaterException cwe) {
                    // Handle exception if the cat cannot navigate to the tile
                }
            }
        }

    }

    /**
     * Solves a maze using recursive DFS helper
     */
    public void solveMaze() {
        Tile[][] island = this.getIsland().getTiles();
        ArrayList<Tile> goal = new ArrayList<>();
        // Add the top right most tile as the exit for mazes
        Tile exit = island[0][island[0].length - 1];
        if (this.getRow() == exit.row && this.getCol() == exit.col) {
            return; // if we are already at the exit, dont move
        }
        goal.add(exit);

        goal = this.hasPathTo(goal);
        if (!goal.isEmpty()) { // maze is solvable
            try {
                this.navigateTo(exit);
            } catch (CatInWaterException cwe) {

            }
        }
    }

    /**
     * Determines which yarn objects in the yarn ArrayList this cat currently
     * has a path to, and marks the corresponding indices in pathTo as true.
     * 
     * @param yarn   The
     * @param pathTo
     */
    private ArrayList<Tile> hasPathTo(ArrayList<Tile> yarns) {
        ArrayList<Tile> reachable = new ArrayList<>();
        Queue<Tile> queue = new LinkedList<>();
        Set<Tile> visited = new HashSet<>();

        queue.add(this.getIsland().getTiles()[this.getRow()][this.getCol()]);
        visited.add(this.getIsland().getTiles()[this.getRow()][this.getCol()]);

        Tile[][] island = this.getIsland().getTiles();

        while (!queue.isEmpty()) {
            Tile current = queue.poll();

            for (int i = 0; i < yarns.size(); i++) {
                if (yarns.get(i) == current) {
                    reachable.add(current);
                    yarns.remove(i);
                }
            }

            int row = current.row;
            int col = current.col;

            if (row + 1 < island.length) {
                if (island[row + 1][col].isLand()) {
                    if (!visited.contains(island[row + 1][col])) {
                        Tile t = island[row + 1][col];
                        queue.add(island[row + 1][col]);
                        visited.add(island[row + 1][col]);
                    }
                }
            }

            if (row - 1 >= 0) {
                if (island[row - 1][col].isLand()) {
                    if (!visited.contains(island[row - 1][col])) {
                        queue.add(island[row - 1][col]);
                        visited.add(island[row - 1][col]);
                    }
                }
            }

            if (col + 1 < island[0].length) {
                if (island[row][col + 1].isLand()) {
                    if (!visited.contains(island[row][col + 1])) {
                        queue.add(island[row][col + 1]);
                        visited.add(island[row][col + 1]);
                    }
                }
            }

            if (col - 1 >= 0) {
                if (island[row][col - 1].isLand()) {
                    if (!visited.contains(island[row][col - 1])) {
                        queue.add(island[row][col - 1]);
                        visited.add(island[row][col - 1]);
                    }
                }
            }

        }
        return reachable;
    }

    private void navigateTo(Tile target) throws CatInWaterException {
        Tile[][] island = this.getIsland().getTiles();
        int startRow = this.getRow();
        int startCol = this.getCol();

        if (startRow == target.row && startCol == target.col) {
            return;
        }

        Queue<Tile> queue = new LinkedList<>();
        Map<Tile, Tile> edgeTo = new HashMap<>();
        Set<Tile> visited = new HashSet<>();

        Tile start = island[startRow][startCol];
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Tile current = queue.poll();

            if (current == target) {
                break;
            }

            for (int[] dir : new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }) { // Right, Down, Left, Up
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];
                if (newRow >= 0 && newRow < island.length && newCol >= 0 && newCol < island[0].length) {
                    Tile neighbor = island[newRow][newCol];
                    if (neighbor.isLand() && !visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                        edgeTo.put(neighbor, current);
                    }
                }
            }
        }

        List<Tile> path = new ArrayList<>();
        Tile step = target;
        while (step != null && edgeTo.containsKey(step)) {
            path.add(step);
            step = edgeTo.get(step);
        }
        Collections.reverse(path);

        for (Tile tile : path) {
            if (tile.row > this.getRow()) {
                this.moveDown();
            } else if (tile.row < this.getRow()) {
                this.moveUp();
            } else if (tile.col > this.getCol()) {
                this.moveRight();
            } else if (tile.col < this.getCol()) {
                this.moveLeft();
            }
        }
    }

}
