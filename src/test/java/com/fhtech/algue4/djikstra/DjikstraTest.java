package com.fhtech.algue4.djikstra;

import com.fhtech.algue4.Parser;
import com.fhtech.algue4.Station;
import com.fhtech.algue4.graph.Graph;
import com.fhtech.algue4.graph.LineSegment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DjikstraTest {


    // test simple
    @Test
    void testSimple() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/vik_test.txt"));
//            have to select start and stop from available stations!

        Station from = new Station("Zeiselmauer");
        Station to = new Station("Tulln");

        Djikstra djikstra = new Djikstra(g);
        List<LineSegment> result = djikstra.find_Shortest(from, to);
        djikstra.printPath(result, to);


        String[] station_names_expected = {"Zeiselmauer", "Wipfing", "Tulln"};
        List<String> station_names_real = result
                .stream()
                .map((LineSegment l) -> l.getPrev().getStationName())
                .collect(Collectors.toList());
        station_names_real.add(result.get(result.size() - 1).getNext().getStationName());


        assertEquals(station_names_expected.length, station_names_real.size());
        for (int i = 0; i < station_names_expected.length; i++) {
            assertEquals(station_names_expected[i], station_names_real.get(i));
        }
    }

    //other direction
    @Test
    void test_drive_reversed() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/vik_test.txt"));
        //have to select start and stop from available stations!

        Station from = new Station("Wipfing");
        Station to = new Station("Langenlebarn");

        // execute djikstra
        Djikstra djikstra = new Djikstra(g);
        List<LineSegment> result = djikstra.find_Shortest(from, to);
        djikstra.printPath(result, to);

        String[] station_names_expected = {"Wipfing", "Zeiselmauer", "Muckendorf", "Langenlebarn"};
        List<String> station_names_real = result
                .stream()
                .map((LineSegment l) -> l.getPrev().getStationName())
                .collect(Collectors.toList());
        station_names_real.add(result.get(result.size() - 1).getNext().getStationName());


        assertEquals(station_names_expected.length, station_names_real.size());
        for (int i = 0; i < station_names_expected.length; i++) {
            assertEquals(station_names_expected[i], station_names_real.get(i));
        }
    }

    // station that does not exist
    @Test
    void test_no_path() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/stations.txt"));
        //have to select start and stop from available stations!

        Station from = new Station("Floridsdorf");
        Station to = new Station("a");

        // execute djikstra
        Djikstra djikstra = new Djikstra(g);
        List<LineSegment> result = djikstra.find_Shortest(from, to);
        djikstra.printPath(result, to);

        assertTrue(result.isEmpty());
    }


    // to same station
    @Test
    void test_should_throw() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/stations.txt"));
        //have to select start and stop from available stations!

        Station from = new Station("Floridsdorf");
        Station to = new Station("Floridsdorf");

        // execute djikstra
        Djikstra djikstra = new Djikstra(g);
        assertThrows(IllegalArgumentException.class, () -> djikstra.find_Shortest(from, to));
    }

    //TODO: add test with isolated node
    //TODO: add test with cycle to himself!
    //TODO: add test with no path to the goal and check output
    //TODO: add complicated check for shortest path
    //TODO: add 5 minutes plus for when hop on hop off happens

}
