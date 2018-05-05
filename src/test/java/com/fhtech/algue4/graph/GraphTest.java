package com.fhtech.algue4.graph;

import com.fhtech.algue4.Line;
import com.fhtech.algue4.Station;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void addStation() {
        Graph g = new Graph();

        g.addStation(new Station("Hello"));
        assertEquals(new Station("Hello"), g.getStationNode(new Station("Hello")).getStation());
        assertEquals(1, g.size());
        g.addStation(new Station("Hello"));
        assertEquals(1, g.size());
        g.addStation(new Station("World"));
        assertEquals(2, g.size());

    }

    @Test
    void addLineSegment() {

        Graph g = new Graph();

        g.addStation(new Station("Hello"));
        g.addStation(new Station("World"));

        g.addLineSegment(
            new LineSegment(
                new Line("U1"),
                new Station("Hello"),
                new Station("World"),
        10)
        );

        assertEquals(
                Collections.singletonList(new Station("World")),
                g.getStationNode(new Station("Hello"))
                        .connectedStations()
                        .collect(Collectors.toList())
        );

        assertEquals(
                Collections.singletonList(new Station("Hello")),
                g.getStationNode(new Station("World"))
                        .connectedStations()
                        .collect(Collectors.toList())
        );

    }

    @Test
    void addingLineSegmentsOnly(){
        Graph g = new Graph();
        g.addLineSegment(new LineSegment(
                new Line("L1"),
                new Station("A"),
                new Station("B"),
                1
        ));
        g.addLineSegment(new LineSegment(
                new Line("L1"),
                new Station("B"),
                new Station("C"),
                1
        ));
        g.addLineSegment(new LineSegment(
                new Line("L1"),
                new Station("C"),
                new Station("A"),
                1
        ));
        assertThat( g.getStationNode(new Station("A")).connectedStations().collect(Collectors.toList())
                , hasItems(new Station("B"), new Station("C")));
        assertThat( g.getStationNode(new Station("B")).connectedStations().collect(Collectors.toList())
                , hasItems(new Station("A"), new Station("C")));
        assertThat( g.getStationNode(new Station("C")).connectedStations().collect(Collectors.toList())
                  , hasItems(new Station("A"), new Station("B")));

    }
}