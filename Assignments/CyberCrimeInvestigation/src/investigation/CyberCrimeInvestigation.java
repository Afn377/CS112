package investigation;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList; 

/*  
 * This class represents a cyber crime investigation.  It contains a directory of hackers, which is a resizing
 * hash table. The hash table is an array of HNode objects, which are linked lists of Hacker objects.  
 * 
 * The class contains methods to add a hacker to the directory, remove a hacker from the directory.
 * You will implement these methods, to create and use the HashTable, as well as analyze the data in the directory.
 * 
 * @author Colin Sullivan
 */
public class CyberCrimeInvestigation {
       
    private HNode[] hackerDirectory;
    private int numHackers = 0; 

    public CyberCrimeInvestigation() {
        hackerDirectory = new HNode[10];
    }

    /**
     * Initializes the hacker directory from a file input.
     * @param inputFile
     */
    public void initializeTable(String inputFile) { 
        // DO NOT EDIT
        StdIn.setFile(inputFile);  
        while(!StdIn.isEmpty()){
            addHacker(readSingleHacker());
        }
    }

    /**
     * Reads a single hackers data from the already set file,
     * Then returns a Hacker object with the data, including 
     * the incident data.
     * 
     * StdIn.setFile() has already been called for you.
     * 
     * @param inputFile The name of the file to read hacker data from.
     */
     public Hacker readSingleHacker(){ 
        // WRITE YOUR CODE HERE
        String name = StdIn.readLine();
        String ip = StdIn.readLine();
        String location = StdIn.readLine();
        String os = StdIn.readLine();
        String webServer = StdIn.readLine();
        String date = StdIn.readLine();
        String urlHash = StdIn.readLine();
        Hacker hacker = new Hacker(name);
        Incident incident = new Incident(os, webServer, date, location, ip, urlHash);
        hacker.addIncident(incident);
        
        return hacker; 
    }

    /**
     * Adds a hacker to the directory.  If the hacker already exists in the directory,
     * instead adds the given Hacker's incidents to the existing Hacker's incidents.
     * 
     * After a new insertion (NOT if a hacker already exists), checks if the number of 
     * hackers in the table is >= table length divided by 2. If so, calls resize()
     * 
     * @param toAdd
     */
    public void addHacker(Hacker toAdd) {
        // WRITE YOUR CODE HERE
        int i = toAdd.hashCode() % hackerDirectory.length;
        if(hackerDirectory[i] == null){
            hackerDirectory[i] = new HNode(toAdd);
            numHackers++;
            if(numHackers >= hackerDirectory.length / 2){
                resize();
            }
        } else{
            HNode ptr = hackerDirectory[i];
            HNode prev = null;
            while(ptr != null){
                if(ptr.getHacker().getName().equals(toAdd.getName())){
                    ptr.getHacker().getIncidents().addAll(toAdd.getIncidents());
                    return;
                }
                prev = ptr;
                ptr = ptr.getNext();
            }
            prev.setNext(new HNode(toAdd));
            numHackers++;
            if(numHackers >= hackerDirectory.length / 2){
                resize();
            }
        }

    }

    /**
     * Resizes the hacker directory to double its current size.  Rehashes all hackers
     * into the new doubled directory.
     */
    private void resize() {
        // WRITE YOUR CODE HERE
        HNode[] temp = hackerDirectory;
        numHackers = 0;
        hackerDirectory = new HNode[temp.length * 2];
        for(int i = 0; i < temp.length; i++){
            if(temp[i] != null){
                HNode ptr = temp[i];
                while(ptr != null){
                    addHacker(ptr.getHacker());
                    ptr = ptr.getNext();
                    numHackers++;
                }
            }
        }
        
    }

    /**
     * Searches the hacker directory for a hacker with the given name.
     * Returns null if the Hacker is not found
     * 
     * @param toSearch
     * @return The hacker object if found, null otherwise.
     */
    public Hacker search(String toSearch) {
        // WRITE YOUR CODE HERE
        int i = Math.abs(toSearch.hashCode()) % hackerDirectory.length;
        
        HNode ptr = hackerDirectory[i];
        while(ptr != null){

            if(ptr.getHacker().getName().equals(toSearch))
                return ptr.getHacker();
            ptr = ptr.getNext();

        }

        return null;
    }

    /**
     * Removes a hacker from the directory.  Returns the removed hacker object.
     * If the hacker is not found, returns null.
     * 
     * @param toRemove
     * @return The removed hacker object, or null if not found.
     */
    public Hacker remove(String toRemove) {
        // WRITE YOUR CODE HERE
        int i = Math.abs(toRemove.hashCode()) % hackerDirectory.length;
        HNode prev = null;
        HNode ptr = hackerDirectory[i];
        if(ptr.getHacker().getName().equals(toRemove)){
            hackerDirectory[i] = ptr.getNext();
            numHackers--;
            return ptr.getHacker();
        }
        prev = ptr;
        ptr = ptr.getNext();

        while(ptr != null){
            
            if(ptr.getHacker().getName().equals(toRemove)){
                prev.setNext(ptr.getNext());
                numHackers--;
                return ptr.getHacker();
            }

            prev = ptr;
            ptr = ptr.getNext();
        }
        return null;
    } 

    /**
     * Merges two hackers into one based on number of incidents.
     * 
     * @param hacker1 One hacker
     * @param hacker2 Another hacker to attempt merging with
     * @return True if the merge was successful, false otherwise.
     */
    public boolean mergeHackers(String hacker1, String hacker2) {  
        // WRITE YOUR CODE HERE 
        Hacker h1 = search(hacker1);
        Hacker h2 = search(hacker2);
        if(h1 == null || h2 == null)
            return false;
        
        if(h2.numIncidents() > h1.numIncidents()){
            h2.getIncidents().addAll(h1.getIncidents());
            h2.addAlias(h1.getName());
            remove(hacker1);
            return true;
        } else{
            h1.getIncidents().addAll(h2.getIncidents());
            h1.addAlias(h2.getName());
            remove(hacker2);
            return true;
        }

    }

    /**
     * Gets the top n most wanted Hackers from the directory, and
     * returns them in an arraylist. 
     * 
     * You should use the provided MaxPQ class to do this. You can
     * add all hackers, then delMax() n times, to get the top n hackers.
     * 
     * @param n
     * @return Arraylist containing top n hackers
     */
    public ArrayList<Hacker> getNMostWanted(int n) {
        // WRITE YOUR CODE HERE 
        MaxPQ<Hacker> pq = new MaxPQ<Hacker>();
        for(int i = 0; i < hackerDirectory.length; i++){
            HNode ptr = hackerDirectory[i];
            while(ptr != null){
                pq.insert(ptr.getHacker());
                ptr = ptr.getNext();
            }
        }
        
        ArrayList<Hacker> result = new ArrayList<Hacker>();
        for(int i = 0; i < n; i ++){
            result.add(pq.delMax());
        }
        return result;
    }

    /**
     * Gets all hackers that have been involved in incidents at the given location.
     * 
     * You should check all hackers, and ALL of each hackers incidents.
     * You should not add a single hacker more than once.
     * 
     * @param location
     * @return Arraylist containing all hackers who have been involved in incidents at the given location.
     */
    public ArrayList<Hacker> getHackersByLocation(String location) {
        // WRITE YOUR CODE HERE 
        ArrayList<Hacker> result = new ArrayList<Hacker>();
        for(int i = 0; i < hackerDirectory.length; i++){
            if(hackerDirectory[i] != null){
                HNode ptr = hackerDirectory[i];
                while(ptr != null){
                    Hacker hacker = ptr.getHacker();
                    ArrayList<Incident> incidents = hacker.getIncidents();
                    for(int j = 0; j < hacker.numIncidents(); j++){
                        if(incidents.get(j).getLocation().equals(location)){
                            result.add(hacker);
                            break;
                        }
                    }
                    ptr = ptr.getNext();
                }
                
            }
        }
        return result;
    }
  

    /**
     * PROVIDED--DO NOT MODIFY!
     * Outputs the entire hacker directory to the terminal. 
     */
     public void printHackerDirectory() { 
        System.out.println(toString());
    } 

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.hackerDirectory.length; i++) {
            HNode headHackerNode = hackerDirectory[i];
            while (headHackerNode != null) {
                if (headHackerNode.getHacker() != null) {
                    sb.append(headHackerNode.getHacker().toString()).append("\n");
                    ArrayList<Incident> incidents = headHackerNode.getHacker().getIncidents();
                    for (Incident incident : incidents) {
                        sb.append("\t" +incident.toString()).append("\n");
                    }
                }
                headHackerNode = headHackerNode.getNext();
            } 
        }
        return sb.toString();
    }

    public HNode[] getHackerDirectory() {
        return hackerDirectory;
    }
}
