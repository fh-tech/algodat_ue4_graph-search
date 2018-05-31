package com.fhtech.algue4.djikstra;

import com.fhtech.algue4.Line;
import com.fhtech.algue4.Station;
import com.fhtech.algue4.errors.DijkstraException;
import com.fhtech.algue4.graph.Graph;
import com.fhtech.algue4.graph.LineSegment;
import com.fhtech.algue4.graph.StationNode;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;


public class Dijkstra {

    private Graph graph;


    public Dijkstra(@NotNull Graph graph) {
        this.graph = graph;
    }

    /**
     * Finds the shortest path from one edge to another
     * @param from Station you want to start your journey from
     * @param to Station you want to reach
     * @return List of edges that need to be traversed where the first entry is the first edge
     */
    public @NotNull List<LineSegment> find_Shortest(@NotNull Station from, @NotNull Station to) throws DijkstraException {

        if (to.equals(from)) throw new DijkstraException("Station to and from should not be equal.");
        if(graph.getStationNode(from) == null) throw new DijkstraException("The station you want to travel from does not exist.");
        if(graph.getStationNode(to) == null) throw new DijkstraException("The station you want to travel to does not exist.");

        final HashMap<StationFromLine, LineSegmentFromLine> previous = new HashMap<>();
        final HashMap<StationFromLine, Integer> durations = new HashMap<>();
        final Set<StationFromLine> settled = new HashSet<>();
        final PriorityQueue<StationFromLine> unsettled = new PriorityQueue<>(Comparator.comparing(durations::get));

        previous.put(new StationFromLine(from, null), null);
        durations.put(new StationFromLine(from, null), 0);
        unsettled.enqueue(new StationFromLine(from, null));

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
    }

    /**
     * Sets new duration to a Station if adjacent Station duration + edge duration is smaller than the current duration to the Station
     * @param current Station you are currently at
     * @param nextSegment LineSegment you consider going
     * @param durations the temporary durations for each station
     * @param previous previous LineSegments for the stations
     */
    private void updateDuration(StationFromLine current, LineSegment nextSegment, HashMap<StationFromLine, Integer> durations, HashMap<StationFromLine, LineSegmentFromLine> previous) {
        StationFromLine next = new StationFromLine(nextSegment.getNext(), nextSegment.getLine());
        int durationNext = durations.get(current) + nextSegment.getDuration();
        if(current.fromLine != null){
            durationNext += (current.fromLine != next.fromLine) ? 5 : 0;
        }

        if(durations.get(next) == null
        || durationNext < durations.get(next)){

            durations.put(next, durationNext);
            previous.put(next, new LineSegmentFromLine(nextSegment, current.fromLine));
        }
    }

    /**
     * Prints the path on stdout
     * @param path List<LineSegment> which is result of find_Shortest
     */
    public void printPath(@NotNull List<LineSegment> path) {
        int sum = 0;
        if(path.size() != 0) {
            StringBuilder trace = new StringBuilder();
            Line last_line = null;
            for(LineSegment edge : path) {
                boolean switch_line = false;
                Line currentLine = edge.getLine();
                if (!currentLine.equals(last_line)) switch_line = true;

                if(switch_line) {
                    if(last_line != null) {
                        trace.append(edge.getPrev().getStationName()).append("\n\t + 5min");
                    }
                    trace.append("\n").append(currentLine.getName()).append(": ");

                }
                trace.append(edge.getPrev().getStationName()).append(" --").append(edge.getDuration()).append("--> ");

                // calculate total with switching lines respected
                sum = switch_line ? sum + edge.getDuration() + 5 : sum + edge.getDuration();
                //for comparison
                last_line = edge.getLine();
            }
            sum-=5;
            // last edge needs to get previous and next
            trace.append(path.get(path.size() - 1).getNext().getStationName());
            System.out.println();
            System.out.println(trace);
            System.out.println("Gesamtzeit: " + sum + "min");
        }
        // if the path size is 0 there just is no path
        else System.out.println("No path to destination.");
    }


    /**
     * extracts the real path to the Station to
     * @param previous all the shortest previous edges for the Stations
     * @param to Station you want to extract the path to
     * @return List<LineSegment> all edges that need to be traveled to Station to
     */
    private @NotNull List<LineSegment> reversePath(HashMap<StationFromLine, LineSegmentFromLine> previous, HashMap<StationFromLine, Integer> durations, @NotNull Station to) {

        LinkedList<LineSegment> path = new LinkedList<>();
        StationFromLine end = previous
                .keySet()
                .stream()
                .filter((sfl) -> sfl.getStation().equals(to))
                .min(Comparator.comparing(durations::get))
                .orElse(null);

        if(end == null) return Collections.emptyList();

        LineSegmentFromLine lfl = previous.get(end);

        while (lfl != null) {
            path.addFirst(lfl.lineSegment);
            StationFromLine sfl = new StationFromLine(lfl.lineSegment.getPrev(), lfl.fromLine);
            lfl = previous.get(sfl);
        }
        return path;
    }

    @Value
    @AllArgsConstructor
    private class StationFromLine {
        Station station;
        Line fromLine;
    }

    @Value
    @AllArgsConstructor
    private class LineSegmentFromLine{
        LineSegment lineSegment;
        Line fromLine;
    }

}
