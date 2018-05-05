package com.fhtech.algue4;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class TransportLine {
    private String name;
    private List<Station> stations;
    
}
