package dictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<Item> implements Iterable<Item> {
    private int N; // number of elements on queue
    private Node first; // beginning of queue
    private Node last; // end of queue

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
    }

    // maximum allowable number of calls
    private static long maxNumberOfOperations = Long.MAX_VALUE;

    private static long constructorCount = 0;
    private static long enqueueCount = 0;
    private static long dequeueCount = 0;
    private static long peekCount = 0;
    private static long isEmptyCount = 0;
    private static long sizeCount = 0;
    private static long iteratorCount = 0;
    private static long hasNextCount = 0;
    private static long nextCount = 0;
    private static long operationCount = 0;

    public static void resetAllOperationCounts() {
        constructorCount = 0;
        enqueueCount = 0;
        dequeueCount = 0;
        peekCount = 0;
        isEmptyCount = 0;
        sizeCount = 0;
        iteratorCount = 0;
        hasNextCount = 0;
        nextCount = 0;
        operationCount = 0;
    }

    public static long constructorCount() {
        return constructorCount;
    }

    public static long enqueueCount() {
        return enqueueCount;
    }

    public static long dequeueCount() {
        return dequeueCount;
    }

    public static long peekCount() {
        return peekCount;
    }

    public static long isEmptyCount() {
        return isEmptyCount;
    }

    public static long sizeCount() {
        return sizeCount;
    }

    public static long iteratorCount() {
        return iteratorCount;
    }

    public static long hasNextCount() {
        return hasNextCount;
    }

    public static long nextCount() {
        return nextCount;
    }

    public static long operationCount() {
        return operationCount;
    }

    public static void setMaxNumberOfOperations(long limit) {
        maxNumberOfOperations = limit;
    }

    public Queue() {
        constructorCount++;
        operationCount++;

    }

    public boolean isEmpty() {
        isEmptyCount++;
        operationCount++;

        return first == null;
    }

    public int size() {
        sizeCount++;
        operationCount++;

        return N;
    }

    public int length() {
        return size();
    }

    public Item peek() {
        peekCount++;
        operationCount++;

        if (first == null)
            throw new NoSuchElementException("Queue underflow");
        return first.item;
    }

    public void enqueue(Item item) {
        enqueueCount++;
        operationCount++;

        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (first == null)
            first = last;
        else
            oldlast.next = last;
        N++;
    }

    public Item dequeue() {
        dequeueCount++;
        operationCount++;

        if (first == null)
            throw new NoSuchElementException("Queue underflow");
        Item item = first.item;
        first = first.next;
        N--;
        if (first == null)
            last = null; // to avoid loitering
        return item;
    }

    public String toString() {
        throw new UnsupportedOperationException("you should not be calling the toString() method in Queue");
        /*
         * StringBuilder s = new StringBuilder();
         * for (Item item : this)
         * s.append(item + " ");
         * return s.toString();
         */
    }

    public Iterator<Item> iterator() {
        iteratorCount++;
        operationCount++;

        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            hasNextCount++;
            operationCount++;

            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            nextCount++;
            operationCount++;

            if (current == null)
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
