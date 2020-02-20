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

    private static final Paint identifierPaint = new Paint();
    {
        identifierPaint.setColor(Color.WHITE);
        identifierPaint.setTextSize(80.0f);
    }

    private float x;
    private float y;
    private float radius;
    private Paint paint;
    private String identifier;

    public Circle(float x, float y, float radius) {
        this(x, y, radius, circlePaint);
    }

    public Circle(float x, float y, float radius, String identifier) {
        this(x, y, radius, circlePaint, identifier);
    }

    public Circle(float x, float y, float radius, Paint paint) {
        this(x, y, radius, paint, /*identifier=*/ null);
    }

    public Circle(float x, float y, float radius, Paint paint, String identifier) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.paint = paint;
        this.identifier = identifier;
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
        if (identifier != null) {
            canvas.drawText(
                    identifier,
                    x * size + widthOffset,
                    y * size + heightOffset,
                    identifierPaint);
        }
    }
}
