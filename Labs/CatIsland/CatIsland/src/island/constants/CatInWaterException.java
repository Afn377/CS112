package island.constants;

/**
 * Custom Exception (error) class which gets thrown when a cat moves onto a
 * water tile
 * 
 * @author Colin Sullivan
 */
public class CatInWaterException extends Exception {
    public CatInWaterException() {
        super("Your cat fell in the water!");
    }
}
