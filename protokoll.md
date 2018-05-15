We chose to use a directed graph although not required for the test data for the following reasons:
1) easier to expand
2) easier to program (logic wise)

As the graph is "licht" we chose to use a adjacency list instead of adjacency matrix because the matrix would take way more space
For dichte or vollst�ndige graphen w�re adjacency matrix besser weil man dann speicherplatz abkoppelt von kantenmenge (nur abh�ngig von knotenzahl)

Disadvantage bei adjacency list ist halt das man auch nicht so schnell findet ob es einen weg von etwas zu etwas gibt
bei einer adjacency matrix findet man das nat�rlich sehr viel schneller

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