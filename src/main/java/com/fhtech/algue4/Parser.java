package com.fhtech.algue4;

import com.fhtech.algue4.graph.Graph;
import com.fhtech.algue4.graph.LineSegment;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser {

    public @NotNull Graph readFile(@NotNull InputStream ins) {
        LineNumberReader rdr = new LineNumberReader(new InputStreamReader(ins));
        return rdr.lines()
                .map(this::parseTransportLine)
                .flatMap(Collection::stream)
                .collect(Graph::new, Graph::addLineSegment, (g1, g2) -> {});
    }

    @NotNull
    private List<LineSegment> parseTransportLine(@NotNull String input){

        Line l = new Line(input.substring(0, input.indexOf(":")));
        Matcher matcher = Pattern.compile("\\\"(?<station>.*?)\\\"\\s*(?<duration>\\d+)?")
                .matcher(input);

        List<LineSegment> segments = new ArrayList<>();

        matcher.find();
        while(!matcher.hitEnd()){

            Station from = new Station(matcher.group("station"));
            int duration = Integer.parseInt(matcher.group("duration"));
            matcher.find();
            Station to = new Station(matcher.group("station"));

            segments.add(new LineSegment(l, from, to, duration));
        }

        return segments;

    }

}
