package com.fhtech.algue4.djikstra;

import com.fhtech.algue4.Station;
import com.fhtech.algue4.graph.Graph;
import com.fhtech.algue4.graph.LineSegment;
import com.fhtech.algue4.graph.StationNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Djikstra {

    private static HashMap<Station, LineSegment> previous = new HashMap<Station, LineSegment>();
    private static HashMap<Station, Integer> durations = new HashMap<Station, Integer>();


    public static @NotNull HashMap<Station, LineSegment> find_Shortest(Graph graph, Station from, Station to) {
        //sets because they need to be unique
        HashSet<Station> unsettled_nodes = new HashSet<Station>();
        HashSet<Station> settled_nodes = new HashSet<Station>();
        //add source to unsettled_nodes
        unsettled_nodes.add(from);
        // init path
        durations.put(from, 0);
        previous.put(from, null);

        //first last is the startNode after that it is the node with the shortest distance from that
        while (unsettled_nodes.size() != 0) {
            StationNode currentNode = getLowestDurationNode(unsettled_nodes, graph);
            unsettled_nodes.remove(currentNode.getStation());
            for (LineSegment edge : currentNode.getConnectedSegments()) {
                Station adjacentStation = edge.getNext();
                int duration = edge.getDuration();

                // why do i need settledNodes
                if (!settled_nodes.contains(adjacentStation)) {
                    // actual duration to the current node
                    int dur_current = durations.get(currentNode.getStation());
                    // actual duration to the adjacent node
                    int dur_adjacent = durations.get(adjacentStation) == null ? Integer.MAX_VALUE : durations.get(adjacentStation);
                    // new duration to the adjacent node
                    int new_dur = dur_current + edge.getDuration();

                    if (new_dur < dur_adjacent) {
                        durations.put(adjacentStation, new_dur);
                        previous.put(adjacentStation, edge);
                    }
                    unsettled_nodes.add(adjacentStation);
                }
            }
            settled_nodes.add(currentNode.getStation());
        }
        return previous;
    }

    /**
     * prints the shortest path from station to station
     * @param previous result of find_shortest Map<Station, LineSegment>
     * @param to station to reach
     */
    public static void printPath(HashMap<Station, LineSegment> previous, Station to) {
        String path = "";
        Stack<LineSegment> reversed_path = reversePath(previous, to);

        while (reversed_path.size() > 0) {
            LineSegment edge = reversed_path.pop();
            path +=
                    edge.getPrev().getStationName()
                            + " ---" + edge.getLine().getName() + "("
                            + edge.getDuration() + "min)--> ";

            if (reversed_path.size() == 0) path += edge.getNext().getStationName();
        }
        System.out.println(path);
    }



    // can return null?
    private static StationNode getLowestDurationNode(@NotNull HashSet<Station> unsettled_nodes, @NotNull Graph graph) {
        // if the size is 1 we can return the Node
//        if (unsettled_nodes.size() == 1) return graph.getStationNode(unsettled_nodes.get(0));

        StationNode lowestDurationNode = null;
        int lowestDuration = Integer.MAX_VALUE;

        for (Station station : unsettled_nodes) {
            StationNode node = graph.getStationNode(station);
            for (LineSegment edge : node.getConnectedSegments()) {
                Station next = edge.getNext();
                // check if there is a way from the unsettled node back to our current! (next because we have a directed graph, previous on the edge would be the station itself)
                if (edge.getDuration() < lowestDuration) {
                    lowestDuration = edge.getDuration();
                    lowestDurationNode = node;
                }
            }
        }
        return lowestDurationNode;
    }

    private static @NotNull Stack<LineSegment> reversePath(@NotNull HashMap<Station, LineSegment> previous, @NotNull Station to) {
        // last segment is null because there is no previous if you reached the start
        Stack<LineSegment> reversed = new Stack<LineSegment>();
        LineSegment current = previous.get(to);
        while (current != null) {
            reversed.push(current);
            Station last = current.getPrev();
            current = previous.get(last);
        }
        return reversed;
    }


}
