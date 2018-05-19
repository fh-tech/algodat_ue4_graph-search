package com.fhtech.graphsearchserver;

import com.fhtech.algue4.Station;
import com.fhtech.algue4.errors.DijkstraException;
import com.fhtech.algue4.graph.LineSegment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping("api/graph/")
public class ApiController {

    private final AppState state;

    @Autowired
    public ApiController(AppState state) {
        this.state = state;
    }

    @RequestMapping(value = "name", params = {"station"})
    List<Station> autoCompleteStationNames(@RequestParam(name = "station") String incompleteStationName){
        return state
                .getStationsMatching((station -> station.getStationName().startsWith(incompleteStationName)))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "path", params = {"from", "to"})
    List<LineSegment> searchPath(@RequestParam Station from, @RequestParam Station to) throws DijkstraException {
        return state.findShortestPath(from, to);
    }



}
