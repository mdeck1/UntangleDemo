package com.malcolmdeck.untangledemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.annotation.Target;

public class GameView extends View {

    private GameState gameState;


    public GameView(Context context) {
        super(context);
        init(null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(21)
    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        //no-op
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFF83A8E8);

        // draw bounding box
        Paint boundingBoxPaint = new Paint();
        boundingBoxPaint.setStrokeWidth(10.0f);
        if (canvas.getWidth() > canvas.getHeight()) {
            int delta = canvas.getWidth() - canvas.getHeight();
            canvas.drawLine(0 + delta/2, 0, 0 + delta/2, canvas.getHeight(),
                    boundingBoxPaint);
            canvas.drawLine(canvas.getWidth() - delta/2, 0, canvas.getWidth() - delta/2, canvas.getHeight(),
                    boundingBoxPaint);
        } else {
            int delta = canvas.getHeight() - canvas.getWidth();
            canvas.drawLine(0, 0 + delta / 2, canvas.getWidth(), 0 + delta/2,
                    boundingBoxPaint);
            canvas.drawLine(0, canvas.getHeight() - delta/2, canvas.getWidth(), canvas.getHeight() - delta/2,
                    boundingBoxPaint);
        }
        if (gameState != null) {
            gameState.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //identify which, if any, circle was touched
                gameState.circleSelected(
                        getFractionalXFromEvent(event),
                        getFractionalYFromEvent(event));
                break;
            case MotionEvent.ACTION_MOVE:
                gameState.circleMoved(
                        getFractionalXFromEvent(event),
                        getFractionalYFromEvent(event));
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                gameState.circleUnselected();
                break;
            default:
                @TargetApi(21)
                Log.e("DEBUG", MotionEvent.actionToString(event.getAction()));
        }
        return true;
    }

    private float getFractionalXFromEvent(MotionEvent event) {
        if (GameView.this.getWidth() > GameView.this.getHeight()){
            float delta = GameView.this.getWidth() - GameView.this.getHeight();
            return (event.getX() - delta / 2) / GameView.this.getHeight();
        } else {
            return event.getX() / GameView.this.getWidth();
        }
    }

    private float getFractionalYFromEvent(MotionEvent event) {
        if (GameView.this.getHeight() > GameView.this.getWidth()){
        float delta = GameView.this.getHeight() - GameView.this.getWidth();
        return (event.getY() - delta / 2) / GameView.this.getWidth();
    } else {
        return event.getX() / GameView.this.getWidth();
    }
    }
}
