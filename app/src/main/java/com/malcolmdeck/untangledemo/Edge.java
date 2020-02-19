package com.malcolmdeck.untangledemo;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;

public class Edge {

    private static final Paint edgeNotCrossedPaint = new Paint();
    {
        edgeNotCrossedPaint.setColor(Color.YELLOW);
        edgeNotCrossedPaint.setStrokeWidth(8.0f);
    }
    private static final Paint edgeIsCrossedPaint = new Paint();
    {
        edgeIsCrossedPaint.setColor(Color.BLACK);
        edgeIsCrossedPaint.setStrokeWidth(8.0f);
    }

    private Circle c1;
    private Circle c2;
    private boolean isCrossed;

    private float startX;
    private float startY;
    private float stopX;
    private float stopY;

    public Edge(Circle c1, Circle c2) {
        this.c1 = c1;
        this.c2 = c2;
        this.isCrossed = false;
    }

    public boolean isCrossed() {
        return isCrossed;
    }

    public void setIsCrossed(boolean isCrossed) {
        this.isCrossed = isCrossed;
    }

    public float getSlope() {
        return (c2.getY() - c1.getY()) / (c2.getX() - c1.getX());
    }

    public float getIntercept() {
        return (c2.getY() - getSlope() * c2.getX());
    }

    public boolean xRangeContains(float x) {
        computeEnds();
        return (x < startX && x > stopX) || (x > startX && x < stopX);

    }

    public void draw(Canvas canvas) {
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        int size = Math.min(width, height);
        int heightOffset = height > width ? ((height - width) / 2) : 0;
        int widthOffset = width > height ? ((width = height) / 2) : 0;

        computeEnds();

        canvas.drawLine(
                startX * size + widthOffset,
                startY * size + heightOffset,
                stopX * size + widthOffset,
                stopY * size + heightOffset,
                isCrossed ? edgeIsCrossedPaint : edgeNotCrossedPaint);
    }

    private void computeEnds() {
        float mX = Math.abs((c1.getX() + c2.getX()) / 2);
        float mY = Math.abs((c1.getY() + c2.getY()) / 2);
        Pair<Float, Float> startPoint = getCircleEdge(c1, mX, mY);
        Pair<Float, Float> stopPoint = getCircleEdge(c2, mX, mY);
        startX = startPoint.first;
        startY = startPoint.second;
        stopX = stopPoint.first;
        stopY = stopPoint.second;
    }

    private Pair<Float, Float> getCircleEdge(Circle circle, float mX, float mY) {
        float a = 1 + (float) Math.pow(getSlope(), 2.0f);
        float b = 2 * (getSlope() * getIntercept() - getSlope() * circle.getY() - circle.getX());
        float c = circle.getX() * circle.getX() + getIntercept() * getIntercept() -
                2 * getIntercept() * circle.getY() + circle.getY() * circle.getY() - circle.getRadius() * circle.getRadius();

        float candidateX = (-1.0f * b + (float) Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        float finalX = 0.0f;
        if (mX > circle.getX() && candidateX > circle.getX() ||
                mX < circle.getX() && candidateX < circle.getX()) {
            finalX = candidateX;
        } else {
            float delta = Math.abs(circle.getX() - candidateX);
            if (circle.getX() > candidateX) {
                finalX = circle.getX() + delta;
            } else {
                finalX = circle.getX() - delta;
            }
        }
        float finalY = getSlope() * finalX + getIntercept();
        return new Pair<>(finalX, finalY);
    }
}
