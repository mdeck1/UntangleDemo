package com.malcolmdeck.untangledemo;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Circle {

    private static final Paint circlePaint = new Paint();
    {
        circlePaint.setColor(Color.RED);
    }

    private float x;
    private float y;
    private float radius;
    private Paint paint;

    public Circle(float x, float y, float radius) {
        this(x, y, radius, circlePaint);
    }

    public Circle(float x, float y, float radius, Paint paint) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.paint = paint;
    }

    public void setPosition(float newX, float newY) {
        if (newX > 1.0 - radius) {
            newX = 1.0f - radius;
        }
        if (newX < 0.0 + radius) {
            newX = 0.0f + radius;
        }
        if (newY > 1.0 - radius) {
            newY = 1.0f - radius;
        }
        if (newY < 0.0 + radius) {
            newY = 0.0f + radius;
        }
        Log.e("DEBUG", "( x , y ) = ( " + newX + " , "  + newY + " )");
        x = newX;
        y = newY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public void draw(Canvas canvas) {
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        int size = Math.min(width, height);
        int heightOffset = height > width ? ((height - width) / 2) : 0;
        int widthOffset = width > height ? ((width = height) / 2) : 0;

        canvas.drawCircle(
                x * size + widthOffset,
                y * size + heightOffset,
                radius * size,
                paint);
    }
}
