package rumaps;

import java.util.*;

/**
 * This class represents the information that can be attained from the Rutgers University Map.
 * 
 * The RUMaps class is responsible for initializing the network, streets, blocks, and intersections in the map.
 * 
 * You will complete methods to initialize blocks and intersections, calculate block lengths, find reachable intersections,
 * minimize intersections between two points, find the fastest path between two points, and calculate a path's information.
 * 
 * Provided is a Network object that contains all the streets and intersections in the map
 * 
 * @author Vian Miranda
 * @author Anna Lu
 */
public class RUMaps {
    
    private Network rutgers;

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Constructor for the RUMaps class. Initializes the streets and intersections in the map.
     * For each block in every street, sets the block's length, traffic factor, and traffic value.
     * 
     * @param mapPanel The map panel to display the map
     * @param filename The name of the file containing the street information
     */
    public RUMaps(MapPanel mapPanel, String filename) {
        StdIn.setFile(filename);
        int numIntersections = StdIn.readInt();
        int numStreets = StdIn.readInt();
        StdIn.readLine();
        rutgers = new Network(numIntersections, mapPanel);
        ArrayList<Block> blocks = initializeBlocks(numStreets);
        initializeIntersections(blocks);

        for (Block block: rutgers.getAdjacencyList()) {
            Block ptr = block;
            while (ptr != null) {
                ptr.setLength(blockLength(ptr));
                ptr.setTrafficFactor(blockTrafficFactor(ptr));
                ptr.setTraffic(blockTraffic(ptr));
                ptr = ptr.getNext();
            }
        }
    }

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Overloaded constructor for testing.
     * 
     * @param filename The name of the file containing the street information
     */
    public RUMaps(String filename) {
        this(null, filename);
    }

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Overloaded constructor for testing.
     */
    public RUMaps() { 
        
    }

    /**
     * Initializes all blocks, given a number of streets.
     * the file was opened by the constructor - use StdIn to continue reading the file
     * @param numStreets the number of streets
     * @return an ArrayList of blocks
     */
    public ArrayList<Block> initializeBlocks(int numStreets) {
        // WRITE YOUR CODE HERE
        ArrayList<Block> blocks = new ArrayList<Block>();
        for(int i  = 0; i < numStreets; i++){
            String name = StdIn.readLine();
            int numBlocks = StdIn.readInt();
            for(int j = 0; j < numBlocks; j++){
                int number = StdIn.readInt();
                int numPoints = StdIn.readInt();
                double roadSize = StdIn.readDouble();
                Block block = new Block(roadSize, name, number);
                for(int k = 0; k < numPoints; k++){
                    int x = StdIn.readInt();
                    int y = StdIn.readInt();
                    Coordinate coord = new Coordinate(x, y);
                    if(k == 0)
                        block.startPoint(coord);
                    else
                        block.nextPoint(coord);
                }
                blocks.add(block);
                StdIn.readLine();
            }

        }
        
        return blocks;
    }

    /**
     * This method traverses through each block and finds
     * the block's start and end points to create intersections. 
     * 
     * It then adds intersections as vertices to the "rutgers" graph if
     * they are not already present, and adds UNDIRECTED edges to the adjacency
     * list.
     * 
     * Note that .addEdge(__) ONLY adds edges in one direction (a -> b). 
     */
    public void initializeIntersections(ArrayList<Block> blocks) {
        // WRITE YOUR CODE HERE
        for (Block originalBlock : blocks) {
            Block block = originalBlock.copy();

            Coordinate startCoord = block.getCoordinatePoints().get(0);
            Coordinate endCoord = block.getCoordinatePoints().get(block.getCoordinatePoints().size() - 1);
    
            int startIndex = rutgers.findIntersection(startCoord);
            Intersection startIntersection;
            if (startIndex == -1) {
                startIntersection = new Intersection(startCoord);
                rutgers.addIntersection(startIntersection);
            } else {
                startIntersection = rutgers.getIntersections()[startIndex];
            }
    
            int endIndex = rutgers.findIntersection(endCoord);
            Intersection endIntersection;
            if (endIndex == -1) {
                endIntersection = new Intersection(endCoord);
                rutgers.addIntersection(endIntersection);
            } else {
                endIntersection = rutgers.getIntersections()[endIndex];
            }
    
            block.setFirstEndpoint(startIntersection);
            block.setLastEndpoint(endIntersection);
    
            Block reversedBlock = originalBlock.copy();
            reversedBlock.setFirstEndpoint(endIntersection);
            reversedBlock.setLastEndpoint(startIntersection);
    
            int updatedStartIndex = rutgers.findIntersection(startCoord);
            int updatedEndIndex = rutgers.findIntersection(endCoord);
            rutgers.addEdge(updatedStartIndex, block);
            rutgers.addEdge(updatedEndIndex, reversedBlock);
        }
     }

    /**
     * Calculates the length of a block by summing the distances between consecutive points for all points in the block.
     * 
     * @param block The block whose length is being calculated
     * @return The total length of the block
     */
    public double blockLength(Block block) {
        // WRITE YOUR CODE HERE
        Coordinate start = block.getCoordinatePoints().get(0);
        double length = 0;
        for (int i = 1; i < block.getCoordinatePoints().size(); i++) {
            Coordinate end = block.getCoordinatePoints().get(i);
            length += coordinateDistance(start, end);
            start = end;
        }
        return length;
   }

    /**
     * Use a DFS to traverse through blocks, and find the order of intersections
     * traversed starting from a given intersection (as source).
     * 
     * Implement this method recursively, using a helper method.
     */
    public ArrayList<Intersection> reachableIntersections(Intersection source) {
        // WRITE YOUR CODE HERE
        ArrayList<Intersection> visited = new ArrayList<>();
        DFS(source, visited);
        return visited;        
    }

    private void DFS(Intersection source, ArrayList<Intersection> visited) {
        int index = rutgers.findIntersection(source.getCoordinate());
        if (index == -1 || visited.contains(source)) {
            return;
        }

        visited.add(source);
        Block block = rutgers.getAdjacencyList()[index];
        while (block != null) {
            Intersection next = block.getLastEndpoint();
            if (!visited.contains(next)) {
                DFS(next, visited);
            }
            block = block.getNext();
        }
        
    }
     

    /**
     * Finds and returns the path with the least number of intersections (nodes) from the start to the end intersection.
     * 
     * - If no path exists, return an empty ArrayList.
     * - This graph is large. Find a way to eliminate searching through intersections that have already been visited.
     * 
     * @param start The starting intersection
     * @param end The destination intersection
     * @return The path with the least number of turns, or an empty ArrayList if no path exists
     */
    public ArrayList<Intersection> minimizeIntersections(Intersection start, Intersection end) {
        // WRITE YOUR CODE HERE
        ArrayList<Intersection> path = new ArrayList<>();
        Intersection[] edgeTo = new Intersection[rutgers.getIntersections().length];
        boolean[] visited = new boolean[rutgers.getIntersections().length];
        Queue<Intersection> queue = new Queue<>();
        queue.enqueue(start);
        visited[rutgers.findIntersection(start.getCoordinate())] = true;
        while (!queue.isEmpty()) {
            Intersection current = queue.dequeue();
            if (current.equals(end)) {
                break;
            }
            int index = rutgers.findIntersection(current.getCoordinate());
            Block block = rutgers.getAdjacencyList()[index];
            while (block != null) {
                Intersection next = block.getLastEndpoint();
                if (!visited[rutgers.findIntersection(next.getCoordinate())]) {
                    visited[rutgers.findIntersection(next.getCoordinate())] = true;
                    edgeTo[rutgers.findIntersection(next.getCoordinate())] = current;
                    queue.enqueue(next);
                }
                block = block.getNext();
            }
        }
        if (!visited[rutgers.findIntersection(end.getCoordinate())]) {
            return new ArrayList<>();
        }
        Intersection current = end;
        while (current != null) {
            path.add(current);
            current = edgeTo[rutgers.findIntersection(current.getCoordinate())];
        }

        Collections.reverse(path);
        return path;
    }

    /**
     * Finds the path with the least traffic from the start to the end intersection using a variant of Dijkstra's algorithm.
     * The traffic is calculated as the sum of traffic of the blocks along the path.
     * 
     * What is this variant of Dijkstra?
     * - We are using traffic as a cost - we extract the lowest cost intersection from the fringe.
     * - Once we add the target to the done set, we're done. 
     * 
     * @param start The starting intersection
     * @param end The destination intersection
     * @return The path with the least traffic, or an empty ArrayList if no path exists
     */
    public ArrayList<Intersection> fastestPath(Intersection start, Intersection end) {
        // WRITE YOUR CODE HERE
        ArrayList<Intersection> path = new ArrayList<>();
        Intersection[] pred = new Intersection[rutgers.getIntersections().length];
        double[] d = new double[rutgers.getIntersections().length];
        PriorityQueue<Intersection> fringe = new PriorityQueue<>(Comparator.comparingDouble(i -> d[rutgers.findIntersection(i.getCoordinate())]));

        for (int i = 0; i < d.length; i++) {
            d[i] = Double.POSITIVE_INFINITY;
            pred[i] = null;
        }
        d[rutgers.findIntersection(start.getCoordinate())] = 0;
        fringe.add(start);
    
        while (!fringe.isEmpty()) {
            Intersection current = fringe.poll();
            int i = rutgers.findIntersection(current.getCoordinate());
    
            if (current.equals(end)) {
                break;
            }
    
            Block block = rutgers.getAdjacencyList()[i];
            while (block != null) {
                Intersection neighbor = block.getLastEndpoint();
                int neighborIndex = rutgers.findIntersection(neighbor.getCoordinate());
    
                double newDist = d[i] + blockTraffic(block);
                if (newDist < d[neighborIndex]) {
                    d[neighborIndex] = newDist;
                    pred[neighborIndex] = current;
    
                    fringe.remove(neighbor); 
                    fringe.add(neighbor);
                }
                block = block.getNext();
            }
        }
    
        if (d[rutgers.findIntersection(end.getCoordinate())] == Double.POSITIVE_INFINITY) {
            return new ArrayList<>();
        }
    
        Intersection current = end;
        while (current != null) {
            path.add(current);
            current = pred[rutgers.findIntersection(current.getCoordinate())];
        }
        Collections.reverse(path);
        return path;
    }
    /**
     * Calculates the total length, average experienced traffic factor, and total traffic for a given path of blocks.
     * 
     * You're given a list of intersections (vertices); you'll need to find the edge in between each pair.
     * 
     * Compute the average experienced traffic factor by dividing total traffic by total length.
     *  
     * @param path The list of intersections representing the path
     * @return A double array containing the total length, average experienced traffic factor, and total traffic of the path (in that order)
     */
    public double[] pathInformation(ArrayList<Intersection> path) {
        double totalLength = 0;
        double totalTraffic = 0;
    
        for (int i = 0; i < path.size() - 1; i++) {
            Intersection current = path.get(i);
            Intersection next = path.get(i + 1);
    
            int currentIndex = rutgers.findIntersection(current.getCoordinate());
            Block block = rutgers.getAdjacencyList()[currentIndex];
    
            while (block != null) {
                if (block.getLastEndpoint().equals(next)) {
                    totalLength += blockLength(block);
                    totalTraffic += blockTraffic(block);
                    break;
                }
                block = block.getNext();
            }
        }
    
        double averageTrafficFactor = totalLength > 0 ? totalTraffic / totalLength : 0;
    
        return new double[]{totalLength, averageTrafficFactor, totalTraffic};
    }

    /**
     * Calculates the Euclidean distance between two coordinates.
     * PROVIDED - do not modify
     * 
     * @param a The first coordinate
     * @param b The second coordinate
     * @return The Euclidean distance between the two coordinates
     */
    private double coordinateDistance(Coordinate a, Coordinate b) {
        // PROVIDED METHOD

        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Calculates and returns a randomized traffic factor for the block based on a Gaussian distribution.
     * 
     * This method generates a random traffic factor to simulate varying traffic conditions for each block:
     * - < 1 for good (faster) conditions
     * - = 1 for normal conditions
     * - > 1 for bad (slower) conditions
     * 
     * The traffic factor is generated with a Gaussian distribution centered at 1, with a standard deviation of 0.2.
     * 
     * Constraints:
     * - The traffic factor is capped between a minimum of 0.5 and a maximum of 1.5 to avoid extreme values.
     * 
     * @param block The block for which the traffic factor is calculated
     * @return A randomized traffic factor for the block
     */
    public double blockTrafficFactor(Block block) {
        double rand = StdRandom.gaussian(1, 0.2);
        rand = Math.max(rand, 0.5);
        rand = Math.min(rand, 1.5);
        return rand;
    }

    /**
     * Calculates the traffic on a block by the product of its length and its traffic factor.
     * 
     * @param block The block for which traffic is being calculated
     * @return The calculated traffic value on the block
     */
    public double blockTraffic(Block block) {
        // PROVIDED METHOD
        
        return block.getTrafficFactor() * block.getLength();
    }

    public Network getRutgers() {
        return rutgers;
    }

}