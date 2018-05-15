package com.fhtech.algue4.djikstra;

import com.fhtech.algue4.Station;
import com.fhtech.algue4.graph.Graph;
import com.fhtech.algue4.graph.LineSegment;
import com.fhtech.algue4.graph.StationNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Djikstra {

    private Graph graph;


    public Djikstra(@NotNull Graph graph) {
        this.graph = graph;
    }

    /**
     *
     * @param from
     * @param to
     * @return
     */
    @NotNull List<LineSegment> find_Shortest(@NotNull Station from, @NotNull Station to) {
        if (to.equals(from)) throw new IllegalArgumentException("to and from should not be equal!");
        final HashMap<Station, LineSegment> previous = new HashMap<>();
        final HashMap<Station, Integer> durations = new HashMap<>();
        //sets because they need to be unique
        final HashSet<Station> unsettled_nodes = new HashSet<>();
        final HashSet<Station> settled_nodes = new HashSet<>();

        //add source to unsettled_nodes
        unsettled_nodes.add(from);
        // init path
        durations.put(from, 0);
        previous.put(from, null);

        //first last is the startNode after that it is the node with the shortest distance from that
        while (unsettled_nodes.size() != 0) {
            // get node with lowest duration - important for dijkstra
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
        return reversePath(previous, to);
    }

    /**
     *
     * @param current
     * @param adjacent
     * @param edge
     * @param durations
     * @param previous
     */
    private void update_duration(@NotNull Station current, @NotNull Station adjacent, @NotNull LineSegment edge, @NotNull HashMap<Station, Integer> durations, @NotNull HashMap<Station, LineSegment> previous) {
        // actual duration to the current node
        int dur_current = durations.get(current);
        // actual duration to the adjacent node
        int dur_adjacent = durations.get(adjacent) == null ? Integer.MAX_VALUE : durations.get(adjacent);
        // new duration to the adjacent node
        int new_dur = dur_current + edge.getDuration();

        LineSegment prev = previous.get(edge.getPrev());
        if(prev != null && !edge.getLine().equals(prev.getLine())) new_dur += 5;
        if (new_dur < dur_adjacent) {
            durations.put(adjacent, new_dur);
            previous.put(adjacent, edge);
        }
    }

    /**
     *
     * @param path
     * @param to
     */
    void printPath(@NotNull List<LineSegment> path, @NotNull Station to) {
        int sum = 0;
        if(path.size() != 0) {
            String trace = "";
            for(LineSegment edge : path) {
                trace +=
                        edge.getPrev().getStationName()
                                + " ---" + edge.getLine().getName() + "("
                                + edge.getDuration() + "min)--> ";
                sum += edge.getDuration();
            }
            // last edge needs to get previous and next
            trace += path.get(path.size()-1).getNext().getStationName();
            System.out.println(trace);
            System.out.println("Gesamtzeit: " + sum + "min");
        }
        else System.out.println("No path to destination.");
    }

    /**
     *
     * @param unsettled_nodes
     * @param durations
     * @return
     */
    private StationNode getLowestDurationNode(@NotNull HashSet<Station> unsettled_nodes, @NotNull HashMap<Station, Integer> durations) {
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


    /**
     *
     * @param previous
     * @param to
     * @return
     */
    private @NotNull List<LineSegment> reversePath(@NotNull HashMap<Station, LineSegment> previous, @NotNull Station to) {
        // last segment is null because there is no previous if you reached the start
        LinkedList<LineSegment> reversed = new LinkedList<>();
        LineSegment current = previous.get(to);
        while (current != null) {
            reversed.addFirst(current);
            Station last = current.getPrev();
            current = previous.get(last);
        }
        return reversed;
    }

}
