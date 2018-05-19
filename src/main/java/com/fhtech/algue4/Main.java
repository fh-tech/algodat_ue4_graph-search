package com.fhtech.algue4;

import com.fhtech.algue4.djikstra.Dijkstra;
import com.fhtech.algue4.errors.DijkstraException;
import com.fhtech.algue4.graph.Graph;
import com.fhtech.algue4.graph.LineSegment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static FileInputStream extract_IFS(String path) {
        File inputFile = new File(path);
        if (inputFile.isFile() && inputFile.canRead()) {
            try {
                return new FileInputStream(inputFile);
            } catch (IOException e) {
                System.out.println("Not a valid input file.");
            }
        } else {
            System.out.println("Not a valid input file.");
        }
        return null;
    }

    private static void execute_dijkstra(Graph g) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter from: ");
        String fromS = reader.nextLine();

        System.out.println("Enter to: ");
        String toS = reader.nextLine();

        Station from = new Station(fromS);
        Station to = new Station(toS);

        try {
            // execute dijkstra
            Dijkstra dijkstra = new Dijkstra(g);
            List<LineSegment> result = dijkstra.find_Shortest(from, to);
            dijkstra.printPath(result);
        } catch (DijkstraException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {

        if (args.length > 0) {
            FileInputStream fis = extract_IFS(args[0]);
            if (fis != null) {
                Parser p = new Parser();
                Graph g = p.readFile(fis);

                while(true) {
                    execute_dijkstra(g);
                }

            } else {
                System.out.println("Not a valid input-file");
            }
        } else {
            System.out.println("You have to specify an input-file.");
        }
    }


}
