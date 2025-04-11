package minecraft;

/**
 * This class implements a linear-probing inventory system
 * based on Minecraft.
 * 
 * @author Kal Pandit
 */
public class MinecraftLP {

    private Item[] st; // the linear probing hash table
    private int slotsFilled; // UNIQUE items, not the sum of counts

    /**
     * Default constructor - initializes a hash table with
     * initial capacity 9.
     */
    public MinecraftLP() {
        st = new Item[9];
    }

    /**
     * Parameterized constructor - initializes a hash table with
     * the capacity specified in parameters.
     * @param initialCapacity the new length of this table
     */
    public MinecraftLP(int initialCapacity) {
        st = new Item[initialCapacity];
    }

    /**
     * Uses linear probing to:
     * - insert an item if it doesn't exist.
     * - update an item count if it does exist.
     * - increment slotsFilled when an item is added.
     * - call resize to double the table when the load factor is met.
     * @param name the item name to try adding
     * @param count how much of this item to add
     */
    public void put(String name, int count) {
        // WRITE YOUR CODE HERE
        int i = Math.abs(name.hashCode()) % st.length;
        while(true){
            if(st[i] == null){
                st[i] = new Item(name, count);
                slotsFilled++;
                break;
            }else if(st[i].getName().equals(name)){
                st[i].setCount(st[i].getCount() + count);
                break;
            }else{
                i = (i + 1) % st.length;
            }
        }
    }

    /**
     * This deletes "count" items of "name" from the inventory IF that item
     * and count exist in the table, and fixes the table accordingly.
     * 
     * @param name the name of the item to delete
     * @param count how much of this item to delete
     */
    public void delete(String name, int count) {
        // WRITE YOUR CODE HERE
    }

    /**
     * Given an item name, this method searches the table for the
     * item and returns either its index or -1 if not found. 
     * @param name the item name to search for
     * @return the item index, or -1 if not found
     */
    public int search(String name) {
        // PROVIDED - do not edit
        int i;
        for (i = Math.abs(name.hashCode()) % st.length; st[i] != null; i = (i + 1) % st.length) {
            if (name.equals(st[i].getName())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Calculates load factor.
     * @return the load factor of this table
     */
    private double loadFactor() {
        // PROVIDED - do not edit
        return (double) slotsFilled / st.length;
    }

    /**
     * Resizes the entire table by:
     * - instantiating a new MinecraftLP object with new capacity
     * - calling .put() on all items in the table on the temp object
     * - reassigning this class's st and slotsFilled to those in temp
     * 
     * @param capacity the new capacity
     */
    private void resize(int capacity) {
        // PROVIDED - do not edit
        MinecraftLP temp = new MinecraftLP(capacity);

        for (int i = 0; i < st.length; i++) {
            if (st[i] != null) {
                temp.put(st[i].getName(), st[i].getCount());
            }
        }
        st = temp.st;
        slotsFilled = temp.slotsFilled;
    }

    /**
     * Rehashes items starting at index i and moving to
     * the first empty space. Used in this lab to fix the array when
     * an item is removed. 
     * 
     * @param i the index to start from: in linear probing, rehashing 
     * starts from a single index. 
     */
    private void rehash(int i) {
        while (st[i] != null) {
            Item value = st[i];
            st[i] = null;
            slotsFilled--;
            put(value.getName(), value.getCount());
            i = (i + 1) % st.length;
        }
    }

    /**
     * Getter for the symbol table. Do not edit.
     * @return the linear probing table of items
     */
    public Item[] getST() {
        return st;
    }

}
