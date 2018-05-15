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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DjikstraTest {


    ArrayList<String> extract_stationNames(HashMap<Station, LineSegment> djikstra_result, Station to, Station from) {
        ArrayList<String> station_names = new ArrayList<String>();
        LineSegment current = djikstra_result.get(to);
        int i = 0;
        while (current != null) {
            Station last = current.getPrev();
            station_names.add(current.getNext().getStationName());
            current = djikstra_result.get(last);
        }
        station_names.add(from.getStationName());
        return station_names;
    }


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

        Djikstra djikstra = new Djikstra(g);
        HashMap<Station, LineSegment> result = djikstra.find_Shortest(from, to);
        djikstra.printPath(result, to);

        String[] station_names_expected = {"Tulln", "Wipfing", "Zeiselmauer"};
        ArrayList<String> station_names_real = extract_stationNames(result, to, from);

        assertEquals(station_names_expected.length, station_names_real.size());
        for(int i = 0; i < station_names_expected.length; i++) {
            assertEquals(station_names_expected[i], station_names_real.get(i));
        }
    }

    @Test
    void test_drive_reversed() {
        Parser p = new Parser();
        Graph g = p.readFile(getClass().getResourceAsStream("/vik_test.txt"));
        //have to select start and stop from available stations!
        Station from = null, to = null;

        Set<Station> all_stations = g.get_stations();
        for (Station station : all_stations) {
            if (from == null) {
                if ("Wipfing".equals(station.getStationName())) from = station;
            }
            if (to == null) {
                if ("Langenlebarn".equals(station.getStationName())) to = station;
            }
        }

        // execute djikstra
        Djikstra djikstra = new Djikstra(g);
        HashMap<Station, LineSegment> result = djikstra.find_Shortest(from, to);
        djikstra.printPath(result, to);

        String[] station_names_expected = {"Langenlebarn", "Muckendorf", "Zeiselmauer", "Wipfing"};
        ArrayList<String> station_names_real = extract_stationNames(result, to, from);

        assertEquals(station_names_expected.length, station_names_real.size());
        for(int i = 0; i < station_names_expected.length; i++) {
            assertEquals(station_names_expected[i], station_names_real.get(i));
        }
    }


}
