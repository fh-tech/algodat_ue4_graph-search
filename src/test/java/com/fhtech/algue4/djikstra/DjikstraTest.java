package com.fhtech.algue4.djikstra;

import com.fhtech.algue4.Parser;
import com.fhtech.algue4.Station;
import com.fhtech.algue4.graph.Graph;
import com.fhtech.algue4.graph.LineSegment;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DjikstraTest {


    @Test
    void testSimple() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/vik_test.txt"));
        Station from = null, to = null;
//            have to select start and stop from available stations!
        Set<Station> all_stations = g.get_stations();
        for (Station station : all_stations) {
            if (from == null) {
                if ("Zeiselmauer".equals(station.getStationName())) from = station;
            }
            if (to == null) {
                if ("Tulln".equals(station.getStationName())) to = station;
            }
        }

        HashMap<Station, LineSegment> shortest = Djikstra.find_Shortest(g, from, to);
        Djikstra.printPath(shortest, to);

        LineSegment last = shortest.get(to);
        Station last_station = last.getPrev();
        assertEquals("Wipfing", last_station.getStationName());

        LineSegment lastlast = shortest.get(last_station);
        Station lastlast_station = lastlast.getPrev();
        assertEquals("Zeiselmauer", lastlast_station.getStationName());
    }

    @Test
    void test_Short_1Line() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/short_stations.txt"));
        Station from = null, to = null;
//            have to select start and stop from available stations!
        Set<Station> all_stations = g.get_stations();
        for (Station station : all_stations) {
            if (from == null) {
                if ("Leopoldau".equals(station.getStationName())) from = station;
            }
            if (to == null) {
                if ("Keplerplatz".equals(station.getStationName())) to = station;
            }
        }

        HashMap<Station, LineSegment> shortest = Djikstra.find_Shortest(g, from, to);
        Djikstra.printPath(shortest, to);
    }

    @Test
    void test_Short_U1_U2() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/short_stations.txt"));
        Station from = null, to = null;
//            have to select start and stop from available stations!
        Set<Station> all_stations = g.get_stations();
        for (Station station : all_stations) {
            if (from == null) {
                if ("Leopoldau".equals(station.getStationName())) from = station;
            }
            if (to == null) {
                if ("Volkstheater".equals(station.getStationName())) to = station;
            }
        }

        HashMap<Station, LineSegment> shortest = Djikstra.find_Shortest(g, from, to);
        Djikstra.printPath(shortest, to);
    }

    @Test
    void test_Real() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/stations.txt"));
        Station from = null, to = null;
//            have to select start and stop from available stations!
        Set<Station> all_stations = g.get_stations();
        for (Station station : all_stations) {
            if (from == null) {
                if ("Leopoldau".equals(station.getStationName())) from = station;
            }
            if (to == null) {
                if ("Kagran".equals(station.getStationName())) to = station;
            }
        }

        HashMap<Station, LineSegment> shortest = Djikstra.find_Shortest(g, from, to);
        Djikstra.printPath(shortest, to);
    }



}
