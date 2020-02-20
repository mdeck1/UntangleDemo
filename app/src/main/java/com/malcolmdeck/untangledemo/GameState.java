package com.malcolmdeck.untangledemo;

import android.app.Activity;
import android.graphics.Canvas;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private static final boolean DEBUG = true;

    private Activity hostingActivity;

    private List<Circle> circleList;
    private List<Edge> edgeList;

    private Circle currentCircle;


    public GameState(int numVertices, Activity activity) {
        this.hostingActivity = activity;
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
        boolean hasCrossings = false;
        for (Edge e : edgeList) {
            if (e.isCrossed()) {
                hasCrossings = true;
                break;
            }
        }
        if (!hasCrossings) {
            Toast.makeText(
                    hostingActivity.getApplicationContext(),
                    "You solved it!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void generatePoints(int n) {
        circleList = new ArrayList<>(n);

        for (int i = 0; i < n; ++i) {
            if (DEBUG) {
                circleList.add(new Circle(
                        (float) (0.3 * Math.cos(2 * i * Math.PI / (double) n) + 0.5),
                        (float) (0.3 * Math.sin(2 * i * Math.PI / (double) n) + 0.5),
                        (float) 0.05,
                        Integer.toString(i)));
            } else {
                circleList.add(new Circle(
                        (float) (0.3 * Math.cos(2 * i * Math.PI / (double) n) + 0.5),
                        (float) (0.3 * Math.sin(2 * i * Math.PI / (double) n) + 0.5),
                        (float) 0.05));
            }
        }
    }

    /**
     * Generate all edges, then delete edges randomly until the graph is planar (but never delete
     * an edge if it disconnects the graph).
     */
    private void generateEdges() {
        edgeList = new ArrayList<>();

        for (int i = 0; i < circleList.size(); ++i) {
            for (int j = i + 1; j < circleList.size(); ++j) {
                    edgeList.add(
                            new Edge(
                                    circleList.get(i),
                                    circleList.get(j)));
            }
        }

        while (!GraphHelper.isPlanar(circleList, edgeList)) {
            int index = (int) (Math.random() * edgeList.size());
            Edge e = edgeList.remove(index);
            if (!GraphHelper.isConnected(circleList, edgeList)) {
                edgeList.add(e);
            }
        }
        return;
    }

    private void recomputeEdgeCrossings() {
        for (Edge e : edgeList) {
            e.setIsCrossed(false);
        }
        for (int i = 0; i < edgeList.size(); ++i) {
            Edge thisEdge = edgeList.get(i);

            // Check if this edge is crossing any other edges
            for (int j = i + 1; j < edgeList.size(); ++j) {
                Edge otherEdge = edgeList.get(j);
                float xCrossing = (otherEdge.getIntercept() - thisEdge.getIntercept()) / (thisEdge.getSlope() - otherEdge.getSlope());
                if (thisEdge.xRangeContains(xCrossing) && otherEdge.xRangeContains(xCrossing)) {
                    thisEdge.setIsCrossed(true);
                    otherEdge.setIsCrossed(true);
                }
            }
            if (!thisEdge.isCrossed()) {
                // Check if this edge intersects any circles (except its own)
                for (Circle c : circleList) {
                    // Ignore this edge's circles
                    if (thisEdge.getC1() == c || thisEdge.getC2() == c) {
                        continue;
                    }
                    // Ignore circles outside of the bounding box of the edge
                    if (!thisEdge.couldIntersect(c)) {
                        continue;
                    }
                    // Compute point on edge closest to circle center
//                    float xBar =
//                            (thisEdge.getIntercept() +
//                                    (c.getY() + (1f/thisEdge.getSlope()) * c.getX())) /
//                                    (thisEdge.getSlope() + (1f / thisEdge.getSlope()));
//                    float yBar = thisEdge.getSlope() * xBar + thisEdge.getIntercept();
//                    float distToCenter =
//                            (float) Math.sqrt(
//                                    Math.pow(c.getX() - xBar, 2) +
//                                    Math.pow(c.getY() - yBar, 2));
                    float distToCenter = (float)
                            (Math.abs(thisEdge.getSlope() * c.getX() - c. getY() + thisEdge.getIntercept()) /
                                    Math.sqrt(Math.pow(thisEdge.getSlope(), 2) + 1));
                    if (distToCenter <= c.getRadius()) {
                        thisEdge.setIsCrossed(true);
                        break;
                    }
                }
            }
        }
    }
}
