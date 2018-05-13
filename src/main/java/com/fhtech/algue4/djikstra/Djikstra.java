package com.fhtech.algue4.djikstra;

import com.fhtech.algue4.Station;
import com.fhtech.algue4.graph.Graph;
import com.fhtech.algue4.graph.LineSegment;
import com.fhtech.algue4.graph.StationNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Djikstra {

    private static HashMap<Station, LineSegment> previous = new HashMap<Station, LineSegment>();
    private static HashMap<Station, Integer> durations = new HashMap<Station, Integer>();
    private static ArrayList<Station> unsettled_nodes = new ArrayList<Station>();

    public static HashMap<Station, LineSegment> find_Shortest(Graph graph, Station from, Station to) {
        ArrayList<Station> settled_nodes = new ArrayList<Station>();
        //add source to unsettled_nodes
        unsettled_nodes.add(from);
        // init path
        durations.put(from, 0);
        previous.put(from, null);

        //first last is the startNode after that it is the node with the shortest distance from that
        StationNode last = graph.getStationNode(from);
        while (unsettled_nodes.size() != 0) {
            StationNode currentNode = getLowestDurationNode(unsettled_nodes, graph, last);
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
            // set current to be last so currentNode is selected based on that
            last = currentNode;
        }
        return previous;
    }

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


    /**
     *
     * @param unsettled_nodes
     * @param graph
     * @param current
     * @return if no path to the node found returns null otherwise the node with the shortest distance from the current node but wait that should not be possible
     */
    private static StationNode getLowestDurationNode(ArrayList<Station> unsettled_nodes, Graph
            graph, StationNode current) {
        // if the size is 1 we can return the Node
        if (unsettled_nodes.size() == 1) return graph.getStationNode(unsettled_nodes.get(0));

        StationNode lowestDurationNode = null;
        int lowestDuration = Integer.MAX_VALUE;

        for (Station station : unsettled_nodes) {
            StationNode node = graph.getStationNode(station);
            for (LineSegment edge : node.getConnectedSegments()) {
                Station next = edge.getNext();
                // check if there is a way from the unsettled node back to our current! (next because we have a directed graph, previous on the edge would be the station itself)
                if (graph.getStationNode(next) == current) {
                    if (edge.getDuration() < lowestDuration) {
                        lowestDuration = edge.getDuration();
                        lowestDurationNode = node;
                    }
                }
            }
        }
        return lowestDurationNode;
    }

    private static Stack<LineSegment> reversePath(HashMap<Station, LineSegment> previous, Station to) {
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
