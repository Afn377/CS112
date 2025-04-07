package inventory;
/**
 * Represents a node in the separate chaining hash table. 
 * Stores a name (used as key), a recipe (its value), and a next reference.
 * @author Kal Pandit
 */
public class Item {
    private String name;
    private Recipe recipe;
    private Item next;

    public Item(String name, Recipe recipe, Item next) {
        this.name = name;
        this.recipe = recipe;
        this.next = next;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Item getNext() {
        return this.next;
    }

    public void setNext(Item next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Item)) {
            return false;
        }
        Item otherItem = (Item) other;
        return this.name.equals(otherItem.getName()) && this.recipe.equals(otherItem.getRecipe());
    }

    

}
