package com.fhtech.algue4;

import com.fhtech.algue4.graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void readFile() {

        Parser p = new Parser();

        Graph g =  p.readFile(getClass().getResourceAsStream("/circle.txt"));

        System.out.println();

        Set<Station> stations = g.getStationNode(new Station("a"))
                .connectedStations()
                .flatMap(s -> g.getStationNode(s).connectedStations())
                .flatMap(s -> g.getStationNode(s).connectedStations())
                .collect(Collectors.toSet());

        assertThat(stations,
                hasItems( new Station("a")
                        , new Station("b")
                        , new Station("c")
                        , new Station("d"))
        );
    }

    @Test
    void weirdInputShouldWork(){
        Graph g = new Parser().readFile(this.getClass().getResourceAsStream("/weird_names.txt"));

        assertNotNull(g.getStationNode(new Station("ßÖ♂aè")));
        assertNotNull(g.getStationNode(new Station(" ")));
    }

    @Test
    void weirdFormatFile() {
        Graph g = new Parser().readFile(this.getClass().getResourceAsStream("/stations_weird.txt"));
        Graph g2 = new Parser().readFile(this.getClass().getResourceAsStream("/stations.txt"));

        assertEquals(g.size(), g2.size());
        assertTrue(g.get_stations().containsAll(g2.get_stations()));
        assertTrue(g2.get_stations().containsAll(g.get_stations()));
    }
}