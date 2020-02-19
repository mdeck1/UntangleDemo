package com.malcolmdeck.untangledemo;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private static final double EDGE_PROBABILITY = 0.2;


    private List<Circle> circleList;
    private List<Edge> edgeList;

    private Circle currentCircle;


    public GameState(int numVertices) {
        generatePoints(numVertices);
        generateEdges();
    }

    public void draw(Canvas canvas) {

        for (Circle circle : circleList) {
            circle.draw(canvas);
        }
        recomputeEdgeCrossings();
        for (Edge edge : edgeList) {
            edge.draw(canvas);
        }
    }

    public void circleSelected(float fractionalX, float fractionalY) {
        Circle closestCircle = null;
        float dist = Float.POSITIVE_INFINITY;
        for (Circle circle : circleList) {
            float currDist =
                    (float) (Math.pow((circle.getX() - fractionalX), 2.0f) +
                                        Math.pow((circle.getY() - fractionalY), 2.0f));
            if (currDist < dist) {
                closestCircle = circle;
                dist = currDist;
            }
        }
        //TODO(mdeck): check if we're close enough to the circle via max allowable dist
        currentCircle = closestCircle;
    }

    public void circleMoved(float fractionalX, float fractionalY) {
        currentCircle.setPosition(fractionalX, fractionalY);

    }

    public void circleUnselected() {
        currentCircle = null;
    }

    private void generatePoints(int n) {
        circleList = new ArrayList<>(n);

        for (int i = 0; i < n; ++i) {
            circleList.add(new Circle(
                    (float) (0.3 * Math.cos(2*i*Math.PI/ (double) n) + 0.5),
                    (float) (0.3 * Math.sin(2*i*Math.PI/ (double) n) + 0.5),
                    (float) 0.05));
        }
    }

    private void generateEdges() {
        edgeList = new ArrayList<>();

        for (int i = 0; i < circleList.size(); ++i) {
            for (int j = i + 1; j < circleList.size(); ++j) {
                if (Math.random() < EDGE_PROBABILITY) {
                    edgeList.add(
                            new Edge(
                                    circleList.get(i),
                                    circleList.get(j)));
                }
            }
        }
    }

    private void recomputeEdgeCrossings() {
        for (Edge e : edgeList) {
            e.setIsCrossed(false);
        }
        for (int i = 0; i < edgeList.size(); ++i) {
            for (int j = i + 1; j < edgeList.size(); ++j) {
                Edge e1 = edgeList.get(i);
                Edge e2 = edgeList.get(j);
                float xCrossing = (e2.getIntercept() - e1.getIntercept()) / (e1.getSlope() - e2.getSlope());
                if (e1.xRangeContains(xCrossing) && e2.xRangeContains(xCrossing)) {
                    e1.setIsCrossed(true);
                    e2.setIsCrossed(true);
                }
            }
        }
    }
}
