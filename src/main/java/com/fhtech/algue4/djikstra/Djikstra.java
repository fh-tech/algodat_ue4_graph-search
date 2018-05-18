package com.fhtech.algue4.djikstra;

import com.fhtech.algue4.Line;
import com.fhtech.algue4.Station;
import com.fhtech.algue4.errors.DijkstraException;
import com.fhtech.algue4.graph.Graph;
import com.fhtech.algue4.graph.LineSegment;
import com.fhtech.algue4.graph.StationNode;
import org.jetbrains.annotations.NotNull;
import java.util.*;


public class Djikstra {

    private Graph graph;


    public Djikstra(@NotNull Graph graph) {
        this.graph = graph;
    }

    /**
     * Finds the shortest path from one edge to another
     * @param from Station you want to start your journey from
     * @param to Station you want to reach
     * @return List of edges that need to be traversed where the first entry is the first edge
     */
    @NotNull List<LineSegment> find_Shortest(@NotNull Station from, @NotNull Station to) throws DijkstraException {
        if (to.equals(from)) throw new DijkstraException("Station to and from should not be equal.");
        if(graph.getStationNode(from) == null) throw new DijkstraException("The station you want to travel from does not exist.");
        if(graph.getStationNode(to) == null) throw new DijkstraException("The station you want to travel to does not exist.");

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

            // we can stop we found the node checking neighbouring edges unnecessary
            if(currentNode.getStation().equals(to)) break;

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
     * Sets new duration to a Station if adjacent Station duration + edge duration is smaller than the current duration to the Station
     * @param current Station you are currently at
     * @param adjacent a adjacent station
     * @param edge the edge
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

        // edge we took to get to current (can be null in case of start node)
        LineSegment prev = previous.get(current);

        // previous line we traveled on
        Line prevLine = prev == null ? null : prev.getLine();
        // the new line we will travel
        Line newLine = edge.getLine();

        // if the line we travel changed (hop on hop off) add 5 minutes
        if(prev != null && !newLine.equals(prevLine)) new_dur += 5;
        if (new_dur < dur_adjacent) {
            durations.put(adjacent, new_dur);
            previous.put(adjacent, edge);
        }
    }

    /**
     * Prints the path on stdout
     * @param path List<LineSegment> which is result of find_Shortest
     */
    void printPath(@NotNull List<LineSegment> path) {
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
     * get the node with the shortest path
     * @param unsettled_nodes nodes that can be used next
     * @param durations current durations for all the nodes
     * @return StationNode or null if empty
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
     * extracts the real path to the Station to
     * @param previous all the shortest previous edges for the Stations
     * @param to Station you want to extract the path to
     * @return List<LineSegment> all edges that need to be traveled to Station to
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
