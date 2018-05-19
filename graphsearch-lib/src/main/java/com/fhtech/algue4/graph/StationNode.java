package com.fhtech.algue4.graph;

import com.fhtech.algue4.Station;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class StationNode {
    private Station station;

    private Set<LineSegment> connectedSegments;

    public boolean addSegment(LineSegment l) {
        return connectedSegments.add(l);
    }

    public Stream<Station> connectedStations(){
        return connectedSegments.stream()
                .map(LineSegment::getNext);
    }
}
