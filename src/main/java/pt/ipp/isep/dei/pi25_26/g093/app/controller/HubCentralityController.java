package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.HubScore;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Algorithms;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Edge;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Graph;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.MapGraph;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.*;
import java.util.function.BinaryOperator;

public class HubCentralityController {

    public List<HubScore> HubCentrality(Graph<CompleteStation, Double> graph)
    {
        List<HubScore> scores = new ArrayList<>();
        Collection<Edge<CompleteStation, Double>> allEdges = graph.edges();
        double totalWeight = 0;
        for(Edge<CompleteStation, Double> edge : allEdges)
            totalWeight += edge.getWeight();

        for(int i = 0; i < graph.numVertices(); i++)
        {
            CompleteStation station = graph.vertex(i);
            double betweenness = Betweenness(graph, station);

            double closeness = Closeness(graph, station);

            int degree = graph.inDegree(station);

            double strength = 0;
            Collection<Edge<CompleteStation, Double>> edges = graph.incomingEdges(station);

            for(Edge<CompleteStation, Double> edge : edges)
                strength += edge.getWeight();

            strength /= totalWeight;

            double hubScore = betweenness * 0.35 + closeness * 0.35 + strength * 0.3;

            HubScore score = new HubScore(i, station.getStation(), degree, strength, betweenness, closeness, hubScore);
            scores.add(score);
        }

        return scores;
    }

    public double Betweenness(Graph<CompleteStation, Double> graph, CompleteStation station)
    {
        Comparator<Double> ce = Double::compareTo;
        BinaryOperator<Double> sum = Double::sum;
        double betweenness = 0;
        ArrayList<CompleteStation> s = graph.vertices();

        for(int i = 0; i < s.size(); i++)
        {
            for(int j = i+1; j < s.size(); j++)
            {
                if(!s.get(i).equals(station) && !s.get(j).equals(station))
                {
                    ArrayList<LinkedList<CompleteStation>> paths = new ArrayList<>();
                    ArrayList<Double> dists = new ArrayList<>();
                    Algorithms.shortestPaths(graph, s.get(i), ce, sum, (double) 0, paths, dists);
                    double distance = 0;
                    double distanceIncludeStation = 0;

                    for(int k = 0; k < paths.size(); k++)
                    {
                        if(paths.get(k).getLast().equals(s.get(j)))
                        {
                            distance = dists.get(k);

                            if(paths.get(k).contains(station))
                            {
                                distanceIncludeStation = dists.get(k);
                            }
                        }
                    }

                    if(distance > 0)
                        betweenness += distanceIncludeStation / distance;
                }
            }
        }

        betweenness /= ((double) ((s.size() - 1) * (s.size() - 2)) / 2);
        return betweenness;
    }

    public double Closeness(Graph<CompleteStation, Double> graph, CompleteStation station)
    {
        double closeness = 0;
        Comparator<Double> ce = Double::compareTo;
        BinaryOperator<Double> sum = Double::sum;
        ArrayList<CompleteStation> stations = graph.vertices();

        for(CompleteStation s : stations)
        {
            if(!s.equals(station))
            {
                double dist = Algorithms.shortestPath(graph, station, s, ce, sum, (double) 0, new LinkedList<>());
                closeness += 1 / dist;
            }
        }

        closeness /= (stations.size() - 1);
        return closeness;
    }
}
