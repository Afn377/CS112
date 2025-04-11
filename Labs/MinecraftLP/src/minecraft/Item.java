package minecraft;
/**
 * Represents an item in the linear probing hash table. 
 * Stores a name (used as key), and a count (how much of this item is in this slot).
 * @author Kal Pandit
 */
public class Item {
    private String name;
    private int count;

    public Item(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Item && this.name.equals(((Item) other).getName())
                && this.count == ((Item) other).getCount();
    }

}
