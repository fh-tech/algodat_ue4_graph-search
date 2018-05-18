package com.fhtech.algue4.djikstra;

import com.fhtech.algue4.Line;
import com.fhtech.algue4.Parser;
import com.fhtech.algue4.Station;
import com.fhtech.algue4.errors.DijkstraException;
import com.fhtech.algue4.graph.Graph;
import com.fhtech.algue4.graph.LineSegment;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

public class DijkstraTest {


    // test simple
    @Test
    void testSimple() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/vik_test.txt"));
//            have to select start and stop from available stations!

        Station from = new Station("Zeiselmauer");
        Station to = new Station("Tulln");

        try {
            Dijkstra dijkstra = new Dijkstra(g);
            List<LineSegment> result = dijkstra.find_Shortest(from, to);
            dijkstra.printPath(result);

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
        } catch (DijkstraException e) {
            fail("should not get here!");
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
        try {
            Dijkstra dijkstra = new Dijkstra(g);
            List<LineSegment> result = dijkstra.find_Shortest(from, to);
            dijkstra.printPath(result);

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
        } catch (DijkstraException e) {
            fail("should not get here!");
        }
    }

    // many short should take them
    @Test
    void test_should_take_many() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/many_short.txt"));
        //have to select start and stop from available stations!

        Station from = new Station("Muckendorf");
        Station to = new Station("Nußdorf");

        // execute djikstra
        try {
            Dijkstra dijkstra = new Dijkstra(g);
            List<LineSegment> result = dijkstra.find_Shortest(from, to);
            dijkstra.printPath(result);

            String[] station_names_expected = {"Muckendorf", "Zeiselmauer", "St.Andrä-Wördern", "Greifenstein-Altenberg", "Höflein/Donau", "Kritzendorf", "Unterkritzendorf", "Kierling", "Weidling", "Nußdorf"};
            List<String> station_names_real = result
                    .stream()
                    .map((LineSegment l) -> l.getPrev().getStationName())
                    .collect(Collectors.toList());
            station_names_real.add(result.get(result.size() - 1).getNext().getStationName());


            assertEquals(station_names_expected.length, station_names_real.size());
            for (int i = 0; i < station_names_expected.length; i++) {
                assertEquals(station_names_expected[i], station_names_real.get(i));
            }
        } catch (DijkstraException e) {
            fail("should not get here!");
        }
    }

    // many short should take them
    @Test
    void test_should_take_short() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/many_short2.txt"));
        //have to select start and stop from available stations!

        Station from = new Station("Muckendorf");
        Station to = new Station("Nußdorf");

        // execute djikstra
        try {
            Dijkstra dijkstra = new Dijkstra(g);
            List<LineSegment> result = dijkstra.find_Shortest(from, to);
            dijkstra.printPath(result);

            String[] station_names_expected = {"Muckendorf", "Nußdorf"};
            List<String> station_names_real = result
                    .stream()
                    .map((LineSegment l) -> l.getPrev().getStationName())
                    .collect(Collectors.toList());
            station_names_real.add(result.get(result.size() - 1).getNext().getStationName());


            assertEquals(station_names_expected.length, station_names_real.size());
            for (int i = 0; i < station_names_expected.length; i++) {
                assertEquals(station_names_expected[i], station_names_real.get(i));
            }
        } catch (DijkstraException e) {
            fail("should not get here!");
        }
    }

    @Test
    void test_should_hop() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/should_hop.txt"));
        //have to select start and stop from available stations!

        Station from = new Station("A");
        Station to = new Station("G");

        // execute djikstra
        try {
            Dijkstra dijkstra = new Dijkstra(g);
            List<LineSegment> result = dijkstra.find_Shortest(from, to);
            dijkstra.printPath(result);

            // check for correct duration with hop on hop off
            Line last = null;
            int duration = 0;
            for(LineSegment edge : result) {
                duration += edge.getDuration();
                if(last != null && !(last == edge.getLine())) duration += 5;
                last = edge.getLine();
            }
            assertEquals(duration, 43);

            // check for correct stations traversed
            String[] station_names_expected = {"A", "B" , "C", "D", "E", "F", "G"};
            List<String> station_names_real = result
                    .stream()
                    .map((LineSegment l) -> l.getPrev().getStationName())
                    .collect(Collectors.toList());
            station_names_real.add(result.get(result.size() - 1).getNext().getStationName());


            assertEquals(station_names_expected.length, station_names_real.size());
            for (int i = 0; i < station_names_expected.length; i++) {
                assertEquals(station_names_expected[i], station_names_real.get(i));
            }
        } catch (DijkstraException e) {
            fail("should not get here!");
        }
    }

    @Test
    void test_should_not_hop() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/should_not_hop.txt"));
        //have to select start and stop from available stations!

        Station from = new Station("A");
        Station to = new Station("G");

        // execute djikstra
        try {
            Dijkstra dijkstra = new Dijkstra(g);
            List<LineSegment> result = dijkstra.find_Shortest(from, to);
            dijkstra.printPath(result);

            // check for correct duration with hop on hop off
            Line last = null;
            int duration = 0;
            for(LineSegment edge : result) {
                duration += edge.getDuration();
                if(last != null && !(last == edge.getLine())) duration += 5;
                last = edge.getLine();
            }
            assertEquals(duration, 30);

            // check for correct stations traversed
            String[] station_names_expected = {"A", "B" , "C", "D", "E", "F", "G"};
            List<String> station_names_real = result
                    .stream()
                    .map((LineSegment l) -> l.getPrev().getStationName())
                    .collect(Collectors.toList());
            station_names_real.add(result.get(result.size() - 1).getNext().getStationName());


            assertEquals(station_names_expected.length, station_names_real.size());
            for (int i = 0; i < station_names_expected.length; i++) {
                assertEquals(station_names_expected[i], station_names_real.get(i));
            }
        } catch (DijkstraException e) {
            fail("should not get here!");
        }
    }

    // station from does not exist
    @Test
    void test_from_does_not_exist() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/stations.txt"));
        //have to select start and stop from available stations!

        Station from = new Station("not_existent");
        Station to = new Station("Schottentor");

        try {
            // execute dijkstra
            Dijkstra dijkstra = new Dijkstra(g);
            List<LineSegment> result = dijkstra.find_Shortest(from, to);
            dijkstra.printPath(result);
            fail("should not get here!");
        } catch (DijkstraException e) {
            assertEquals(e.getMessage(), "The station you want to travel from does not exist.");
            System.out.println(e.getMessage());
        }
    }

    // station to that does not exist
    @Test
    void test_to_does_not_exist() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/stations.txt"));
        //have to select start and stop from available stations!

        Station from = new Station("Floridsdorf");
        Station to = new Station("a");

        try {
            // execute dijkstra
            Dijkstra dijkstra = new Dijkstra(g);
            List<LineSegment> result = dijkstra.find_Shortest(from, to);
            dijkstra.printPath(result);
            fail("should not get here!");
        } catch (DijkstraException e) {
            assertEquals(e.getMessage(), "The station you want to travel to does not exist.");
            System.out.println(e.getMessage());
        }
    }

    // both stations do not exist
    @Test
    void test_both_do_not_exist() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/many_short2.txt"));
        //have to select start and stop from available stations!

        Station from = new Station("non_existent");
        Station to = new Station("non_existent2");

        try {
            // execute dijkstra
            Dijkstra dijkstra = new Dijkstra(g);
            List<LineSegment> result = dijkstra.find_Shortest(from, to);
            dijkstra.printPath(result);
            fail("should not get here!");
        } catch (DijkstraException e) {
            assertEquals(e.getMessage(), "The station you want to travel from does not exist.");
            System.out.println(e.getMessage());
        }
    }

    // to same station
    @Test
    void test_to_from_equal() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/stations.txt"));
        //have to select start and stop from available stations!

        Station from = new Station("Floridsdorf");
        Station to = new Station("Floridsdorf");

        try {
            // execute dijkstra
            Dijkstra dijkstra = new Dijkstra(g);
            List<LineSegment> result = dijkstra.find_Shortest(from, to);
            dijkstra.printPath(result);
            assertTrue(result.isEmpty());
        } catch (DijkstraException e) {
            assertEquals(e.getMessage(), "Station to and from should not be equal.");
        }
    }

    // both stations exist but there is no path
    @Test
    void test_no_path() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/isolated.txt"));
        //have to select start and stop from available stations!

        Station from = new Station("Sieghartskirchen");
        Station to = new Station("Muckendorf");

        try {
            // execute dijkstra
            Dijkstra dijkstra = new Dijkstra(g);
            List<LineSegment> result = dijkstra.find_Shortest(from, to);
            dijkstra.printPath(result);
            assertTrue(result.isEmpty());
        } catch (DijkstraException e) {
            fail("should not get here!");
        }
    }

}
