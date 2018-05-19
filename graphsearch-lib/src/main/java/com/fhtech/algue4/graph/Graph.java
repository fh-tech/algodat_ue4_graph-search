package com.fhtech.algue4.graph;

import com.fhtech.algue4.Station;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {

    public Graph(){}

    private HashMap<Station, StationNode> stations = new HashMap<>();

    private void addLineSegmentUniDirectional(LineSegment lineSegment){
        stations.computeIfAbsent(lineSegment.getPrev(),
                (s) -> new StationNode(lineSegment.getPrev(),
                        Stream.of(lineSegment).collect(Collectors.toSet())))
                .addSegment(lineSegment);
    }

    public Set<Station> get_stations() {
        return stations.keySet();
    }

    public StationNode getStationNode(Station station) {
        return stations.get(station);
    }

    public StationNode addStation(Station station){
        return stations.computeIfAbsent(station,
                (s) -> new StationNode(station, new HashSet<>()));
    }

    public int size(){
        return stations.size();
    }

    public void addLineSegment(@NotNull LineSegment lineSegment) {
        addLineSegmentUniDirectional(lineSegment);

        LineSegment reverse = new LineSegment(
                lineSegment.getLine(),
                lineSegment.getNext(),
                lineSegment.getPrev(),
                lineSegment.getDuration());

        addLineSegmentUniDirectional(reverse);
    }



}
