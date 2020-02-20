package com.malcolmdeck.untangledemo;

import com.malcolmdeck.untangledemo.graphutils.BoyerMyrvoldPlanarityInspector;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.List;

public class GraphHelper {

    public static boolean isConnected(List<Circle> vertices, List<Edge> edges) {
        Graph graph = getGraphFromLists(vertices, edges);
        return new ConnectivityInspector<>(graph).isConnected();
    }

    public static boolean isPlanar(List<Circle> vertices, List<Edge> edges) {
        Graph graph = getGraphFromLists(vertices, edges);
        return new BoyerMyrvoldPlanarityInspector(graph).isPlanar();
    }

    private static Graph getGraphFromLists(List<Circle> vertices, List<Edge> edges) {
        Graph graph = new DefaultUndirectedGraph(DefaultEdge.class);
        for (Circle c : vertices) {
            graph.addVertex(c);
        }
        for (Edge e : edges) {
            graph.addEdge(e.getC1(), e.getC2());
        }
        return graph;
    }

}
