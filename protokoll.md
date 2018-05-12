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