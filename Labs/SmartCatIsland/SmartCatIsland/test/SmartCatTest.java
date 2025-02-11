package test;

import static org.junit.Assert.*;
import org.junit.*;

import island.*;
import island.constants.Color;

/**
 * This is a JUnit test class, used to test code
 * 
 * You should test the SmartCat class by designing Island test cases
 * and filling in the JUnit tests according to the assignment
 * description.
 * 
 * @author Colin Sullivan
 */
public class SmartCatTest {

    public static Island pathIsland = new Island(new String[][] {
            {"L", "W", "L", "L", "L", "W", "L"},
            {"L", "W", "L", "W", "L", "W", "L"},
            {"L", "W", "L", "W", "L", "W", "L"},
            {"L", "W", "L", "W", "L", "W", "L"},
            {"L", "W", "L", "W", "L", "W", "L"},
            {"L", "W", "L", "W", "L", "W", "L"},
            {"L", "L", "L", "W", "L", "L", "L"},
    });

    public static Island yarnIsland = new Island(new String[][] {
            {"L", "L", "L", "Y", "L", "W", "L", "Y", "L"},
            {"L", "W", "Y", "W", "L", "W", "Y", "W", "W"},
            {"L", "W", "L", "W", "L", "W", "L", "W", "L"},
            {"L", "W", "Y", "W", "L", "W", "L", "W", "L"},
            {"Y", "W", "L", "W", "L", "W", "L", "W", "L"},
            {"L", "W", "Y", "W", "L", "W", "L", "W", "L"},
            {"Y", "W", "L", "W", "L", "W", "L", "W", "L"},
            {"Y", "W", "Y", "W", "L", "W", "L", "W", "L"},
            {"L", "L", "L", "L", "L", "L", "L", "L", "L"},
    });

    public static Island mazeIsland = new Island(new String[][] {
           {"L", "W", "L", "L", "L", "W", "L", "L", "L", "L"},
           {"L", "L", "L", "W", "L", "W", "L", "W", "W", "W"},
           {"L", "W", "L", "W", "L", "W", "L", "L", "L", "L"},
           {"L", "L", "L", "L", "L", "W", "L", "W", "W", "L"},
           {"L", "W", "L", "W", "L", "W", "L", "W", "W", "L"},
           {"L", "L", "L", "W", "L", "W", "W", "W", "W", "L"},
           {"L", "W", "L", "L", "L", "W", "L", "L", "L", "L"},
           {"L", "L", "L", "W", "L", "W", "L", "W", "W", "W"},
           {"L", "W", "L", "L", "L", "W", "L", "W", "W", "W"},
           {"L", "L", "L", "W", "L", "L", "L", "L", "L", "W"},
    });

    @Test
    public void testWalkPath() {
        // WRITE YOUR CODE HERE
        SmartCat cat = new SmartCat("Cat", SmartCatTest.pathIsland, 0, 0, Color.BLACK);
        cat.walkPath();
        assertEquals(cat.getCol(), 6);
        assertEquals(cat.getRow(), 0);

    }

    @Test
    public void testCollectAllYarn() {
        // WRITE YOUR CODE HERE
        SmartCat cat = new SmartCat("Cat", SmartCatTest.yarnIsland, 0, 0, Color.BLACK);
        cat.collectAllYarn();
        Tile[][] tiles = yarnIsland.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                assertFalse(tiles[i][j].hasYarn);
            }
        }

    }

    @Test
    public void testSolveMaze() {
        // WRITE YOUR CODE HERE
        SmartCat cat = new SmartCat("Cat", SmartCatTest.mazeIsland, 0, 0, Color.BLACK);
        cat.solveMaze();
        assertEquals(cat.getCol(), 9);
        assertEquals(cat.getRow(), 0);
        assertTrue(cat.numStepsTaken() >= 30);
    }

}
