## UE4 Dijkstra Algorithm

### 1) Usage for CLI program

1) clone this project  
2) mvn install  
3) jar for the CLI program can be found in "graphsearch-cli/target/*.jar"
   - jar for Spring Frontend can be found in "graphsearch-server/target/*.jar" -- server runs on localhost:8080
3) cli can be used like: `java -jar dijkstra <absolute path to input file>`  
    * if it was a valid input file you will be asked to enter a station you want to start from and a station you want to reach
    * if the station to, from both exist the shortest path will be searched and displayed
    * after that you will be asked to enter to and from again
4) press Ctrl + C to exit the program


### 2) Implementation Details
We chose to use an adjacency list because public transport networks are usually sparse graphs.
We chose to use a directed graph although not required for the test data, because it is easier to expand later. For example 
driving into the other direction can take more or less time and would not be possible with an undirected graph. Or if for example a 
line can not stop in one direction due to construction work.

#### Data Structures:
We used the Lombok library to easily generate equals, hashCode, getter and setter methods.
Which keeps the classes short and readable (no Java-bloat).

##### Stations
For type safety reasons we chose to not save the station name as a String but rather as a station object which contains the name. This
allows us to easily distinguish between Stations and Lines.
* consists of:
    - String name  

##### Line
Again for type safety reasons a wrapping class.
* consists of:
    - String name

##### StationNode
The "vertices" for our implementation.
* consists of:
    - Station
    - Set<LineSegment> connectedSegments
The LineSegment Set is our adjacency list. We chose a set because there can not be two identical paths from one StationNode to another.
Also there is no ordering or priority.
    
##### LineSegment
The "edges" for our implementation.
* consists of  
    - int duration
    - Station previous
    - Station next
    - Line
We save the line on the edge so we can decide which edge is the most "cost efficient". Switching lines costs **5 minutes** by default.
It also makes differentiating between multiple edges going from one Station to the same destination easier.

##### Graph
Where the whole public transport network is illustrated. The HashMap<Station, StationNode> is used for easier indexing of the StationNodes.
The file format allows us to do this because 2 **different** Stations can never have the same name.
* consists of:
    - HashMap<Station, StationNode> 
        
#### Dijkstra
```Java
    if(to.equals(from)) throw new DijkstraException("Station to and from should not be equal.");
    if(graph.getStationNode(from) == null) throw new DijkstraException("The station you want to travel from does not exist.");
    if(graph.getStationNode(to) == null) throw new DijkstraException("The station you want to travel to does not exist.");
``` 
First of the Station to and from will be checked:
    * if they are equal the algorithm does not start
    * if no Station equals to **or** from the algorithm does not start
    

Dijkstra`s Algorithm needs the following data structures:
```Java
     final HashMap<StationFromLine, LineSegmentFromLine> previous = new HashMap<>();
     final HashMap<StationFromLine, Integer> durations = new HashMap<>();
     final Set<StationFromLine> settled = new HashSet<>();
     final PriorityQueue<StationFromLine> unsettled = new PriorityQueue<>(Comparator.comparing(durations::get));
```
 - unsettled nodes (nodes we found connections to)
    * we used PriorityQueue<Station>
 - settled nodes (nodes we already considered)
    * we used HashSet<StationFromLine> because we only want to check if a StationNode is already contained or not
    * we have to do many checks if something is contained so searching should be fast -- a Hashtable is a good choice with O(1) random access
 - previous LineSegments
    * we used HashMap<StationFromLine, LineSegment> previous
    * as our "indexing" works easily via StationFromLine we use a HashMap
 - durations to the StationNodes that we visited
    * we used HashMap<StationFromLine, Integer> durations
    
```Java
    previous.put(new StationFromLine(from, null), null);
    durations.put(new StationFromLine(from, null), 0);
    unsettled.enqueue(new StationFromLine(from, null));
```
To start of we need to:
* add the start-Station to the unsettled nodes
* set the duration to 0
* the previous StationFromLine to null as there is none for the starting point

```Java
outer: while (unsettled.size() > 0){
    StationFromLine current;
    do{
        if(unsettled.size() == 0) break outer;
        // get node with lowest duration from heap
        current = unsettled.dequeue();
    } while (settled.contains(current));

    if(current.station.equals(to)) break;

    for (LineSegment nextSegment: graph.getStationNode(current.getStation()).getConnectedSegments()){
        StationFromLine next = new StationFromLine(nextSegment.getNext(), nextSegment.getLine());
        if(!settled.contains(next)){
            updateDuration(current, nextSegment, durations, previous);
            unsettled.enqueue(next);
        }
    }
    settled.add(current);
}

return reversePath(previous, durations, to);

```
We have to keep going until there are no unsettled nodes left anymore (note at start we have only the start node).
The node with the highest priority is fetched from the heap. If the fetched Station is already in the
settled nodes, we fetch the next.
During the algorithm the duration to/(priority of) the Stations can change.  
_Updating the priority queue would have **O(n)** time complexity so we just add the Station with the new priority
which is just **O(log(n))**_.   
A drawback of this is that we can have duplicates and the unsettled nodes can contain
settled nodes.

If the selected station equals the Station we want to travel to we are done.  
With the highest priority Station we get the corresponding node in the graph.
Now we have to calculate the duration to the connected nodes by visiting all edges associated with the current StationFromLine.  
If the duration is shorter we have to update it otherwise leave it be. We also have to add all connected nodes to the 
unsettled nodes and our current StationFromLine to the settled nodes.

The algorithm breaks if:
* the currentStation is the one we want to find
* unsettledNodes is empty (no path to the node) 

At the end we return the reversed path to the Station.

#### Switching Penalty (hop on hop off)
In order to find the shortest path while considering extra time for changing the line one travels on the easiest solution would be to 
just duplicate each station for every line connected to it and add new connections in between them with the penalty duration.
As this would greatly increase the graph size and hence the algorithm run time we wanted to avoid that. So we did the following:

We remember not only what the previous LineSegment was but also with which line we got there. By doing that we can differentiate between 
two paths leading to a station that just differ Line-wise. Otherwise a path that is shorter till this point will take priority over another
which is shorter in the long run, as the Station would already be in the settled nodes and would not be considered anymore.


#### Priority Queue
The priority queue is implemented using a flat heap structure. To calculate the priority we use a Java comparator which 
can be constructed with a lambda-expression. A heap is just a tree which gets filled level-wise. Highest priority element 
is the root. A parent node has to have a higher priority than its children. 
If this is violated while inserting the nodes have to be swapped until it is in order again.
Removing the highest priority element requires us to set the element with the lowest priority as root and then swapping
it down to restore order.

A heap can easily be represented by a list (flat heap). By inserting the elements layerwise into the list the following
structure is created:

![](doc/flat_heap.png)

Due to the nature of the list one can easily calculate the parent and child indexes as follows:
```Java
   private int rightChildIdx(int parent) {
        return (parent * 2) + 2;
    }

    private int leftChildIdx(int parent) {
        return (parent * 2) + 1;
    }

    private int parentIdx(int child) {
        return ((child + 1) / 2) - 1;
    }
```
 
### 3) Tests
We wrote unit tests for:
   * the parsing of the input-file with the specified format
   * the priority-queue implementation
   * the dijkstra implementation