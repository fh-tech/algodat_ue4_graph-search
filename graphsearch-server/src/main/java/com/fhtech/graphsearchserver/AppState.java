package com.fhtech.graphsearchserver;

import com.fhtech.algue4.Parser;
import com.fhtech.algue4.Station;
import com.fhtech.algue4.djikstra.Dijkstra;
import com.fhtech.algue4.errors.DijkstraException;
import com.fhtech.algue4.graph.Graph;
import com.fhtech.algue4.graph.LineSegment;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;


@Component
public class AppState {

    @Value("${graph.src-file}")
    private Resource resource;

    private Graph graph = new Graph();

    @PostConstruct
    void init() throws IOException {
        graph = new Parser().readFile(resource.getInputStream());
    }

    @NotNull List<LineSegment> findShortestPath(Station from, Station to) throws DijkstraException {
        return new Dijkstra(graph).find_Shortest(from, to);
    }

    Stream<Station> getStationsMatching(Predicate<Station> predicate){
        return graph.get_stations().stream().filter(predicate);
    }

}
