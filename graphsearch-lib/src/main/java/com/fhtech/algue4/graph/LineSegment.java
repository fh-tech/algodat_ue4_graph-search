package com.fhtech.algue4.graph;

import com.fhtech.algue4.Line;
import com.fhtech.algue4.Station;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
public class LineSegment {
    private Line line;
    private Station prev;
    private Station next;
    private int duration;
}
