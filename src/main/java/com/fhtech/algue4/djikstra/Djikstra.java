package com.fhtech.algue4.djikstra;

import com.fhtech.algue4.Station;
import com.fhtech.algue4.graph.Graph;
import com.fhtech.algue4.graph.LineSegment;
import com.fhtech.algue4.graph.StationNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Djikstra {

    private Graph graph;

    public Djikstra(Graph graph) {
        this.graph = graph;
    }

    public @NotNull HashMap<Station, LineSegment> find_Shortest(Station from, Station to) {
        HashMap<Station, LineSegment> previous = new HashMap<Station, LineSegment>();
        HashMap<Station, Integer> durations = new HashMap<Station, Integer>();
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
            // get node with lowest duration - important for djikstra
            StationNode currentNode = getLowestDurationNode(unsettled_nodes, durations);
            // remove this node
            unsettled_nodes.remove(currentNode.getStation());

            for (LineSegment edge : currentNode.getConnectedSegments()) {
                Station adjacentStation = edge.getNext();

                // only execute if not in settled_nodes otherwise we add nodes multiple times + updating duration here is unnecessary - cant be shorter (because in settled already shortest possible (because we are always take shortest duration unsettled node next)
                if (!settled_nodes.contains(adjacentStation)) {
                    update_duration(currentNode.getStation(),adjacentStation, edge, durations, previous);
                    // add all connected nodes to unsettled so we use them here
                    unsettled_nodes.add(adjacentStation);
                }
            }

            settled_nodes.add(currentNode.getStation());
        }
        return previous;
    }

    private void update_duration(Station current, Station adjacent, LineSegment edge, HashMap<Station, Integer> durations, HashMap<Station, LineSegment> previous) {
        // actual duration to the current node
        int dur_current = durations.get(current);
        // actual duration to the adjacent node
        int dur_adjacent = durations.get(adjacent) == null ? Integer.MAX_VALUE : durations.get(adjacent);
        // new duration to the adjacent node
        int new_dur = dur_current + edge.getDuration();

        if (new_dur < dur_adjacent) {
            durations.put(adjacent, new_dur);
            previous.put(adjacent, edge);
        }
    }


    public void printPath(HashMap<Station, LineSegment> previous, Station to) {
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
    private StationNode getLowestDurationNode(@NotNull HashSet<Station> unsettled_nodes, HashMap<Station, Integer> durations) {
        StationNode lowestDurationNode = null;
        int lowestDuration = Integer.MAX_VALUE;

        for (Station station : unsettled_nodes) {
            int dur_actual = durations.get(station);

            if (dur_actual < lowestDuration) {
                lowestDuration = dur_actual;
                lowestDurationNode = this.graph.getStationNode(station);
            }
        }
        return lowestDurationNode;
    }

    private @NotNull Stack<LineSegment> reversePath(@NotNull HashMap<Station, LineSegment> previous, @NotNull Station to) {
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
