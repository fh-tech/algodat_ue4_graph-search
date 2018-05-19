## UE4 Dijkstra Algorithm

### Usage

1) clone this project
2) mvn install
3) jar can be found in "target/dijkstra"
3) java -jar dijkstra <absolute path to input file>
    * if it was a valid input file you will be asked to enter a station you want to start from and a station you want to reach
    * if the station to, from both exist the shortest path will be searched and displayed
    * after that you will be asked to enter to and from again
4) press Ctrl + C to exit the program



We chose to use a directed graph although not required for the test data for the following reasons:
1) easier to expand
2) easier to program (logic wise)



Data Structures:
- Graph
    consists of:
        -Collection of Nodes
        
- Nodes
    consists of:
        -Station
        -Collection von Edges
    
- Edges
    consists of:
        -weight
        -previous
        -next
        -Line
    
- Stations
    consists of:
        -String name
    
- Line
    consists of:
        -String name

For type safety reasons we chose to not save the station name on the edge but rather a station object which contains the name. 




Algorithm:
Map<Station, LineSegment> previous
because multiple Stations can have the same previous Station we have to remember the LineSegment to differentiate between that