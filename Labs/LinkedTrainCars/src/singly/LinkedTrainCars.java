package singly;

public class LinkedTrainCars {

    private TrainCar front;

    /**
     * Constructor which starts the train as null
     * 
     * This is for the Driver, you do not need to use/modify this.
     */
    public LinkedTrainCars() {
        front = null;
    }

    /**
     * This method should traverse SLL starting at front and count
     * the number of TrainCar nodes in the list. Then return that number.
     * 
     * @returns the number of train cars (nodes) currently in the list
     */
    public int numCars() {
        // WRITE YOUR CODE HERE

        return -1; // REPLACE THIS LINE. It is only provided so the code compiles.
    }

    /**
     * Inserts the a train node with the given name at the given
     * index in the list, where the front node of the list is index 1.
     * i.e. The front node is index 1, second node is index 2.
     * 
     * @param add   The string to add in a new node
     * 
     * @param index The index to add the node at
     */
    public void insertAt(String add, int index) {
        // WRITE YOUR CODE HERE

    }

    /**
     * Removes and returns the name of the first found instance
     * of the target if it exists, or returns null if it doesn't
     * 
     * @param target String target to remove/return
     * 
     * @return target if it exists, null if it doesn't
     */
    public String remove(String target) {
        // DO NOT EDIT
        TrainCar ptr = front;
        if (ptr == null) {
            return null;
        } else if (ptr.name.equals(target)) {
            front = front.next;
            return target;
        }
        while (ptr.next != null) {
            if (ptr.next.name.equals(target)) {
                ptr.next = ptr.next.next;
                return target;
            }
            ptr = ptr.next;
        }
        return null;
    }

    /**
     * Removes and returns the item at the given index if it
     * exists, or returns null if it doesn't
     * 
     * @param index The index to remove
     * 
     * @return String in the node at that index, or null if none
     */
    public String remove(int index) {
        // DO NOT EDIT
        if (index == 1 && front != null) {
            String temp = front.name;
            front = front.next;
            return temp;
        }
        int i = 2;
        TrainCar ptr = front;
        while (ptr != null && ptr.next != null) {
            if (i == index) {
                String temp = ptr.next.name;
                ptr.next = ptr.next.next;
                return temp;
            }
            ptr = ptr.next;
            i++;
        }
        return null;
    }

    /**
     * @returns the front of the train car SLL
     */
    public TrainCar getFront() {
        return this.front;
    }

}