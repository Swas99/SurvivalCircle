package com.archer.survival_circle_dpad.game_logic;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Swastik on 18-01-2016.
 */
public class DirectionPadView extends View {

    private BallView objBall;
    private SinglePlayerGame objGame;

    float radius;
    float centerX;
    float centerY;

    boolean isActivated;


    /**
     * Default constructor
     *
     * @param ct {@link Context}
     */
    public DirectionPadView(final Context ct) {
        super(ct);
    }

    public DirectionPadView(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);
    }

    public DirectionPadView(final Context ct, final AttributeSet attrs, final int defStyle) {
        super(ct, attrs, defStyle);
    }

    @Override
    public void onDraw(final Canvas canv) {
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        radius = Math.min(w/2f,h/2f);
        centerX = getX()+w/2f;
        centerY = getY()+h/2f;
    }

    public void init(WeakReference<BallView> _ball, WeakReference<SinglePlayerGame> singlePlayerGameWeakReference)
    {
        objBall = _ball.get();
        objGame = singlePlayerGameWeakReference.get();
        isActivated = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isActivated)
            return super.onTouchEvent(event);

        if(!objGame.isAlive)
        {
            objGame.start();
        }
        else
        {
            float xTouch = event.getX();
            float yTouch = event.getY();
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    move(getAction(xTouch, yTouch));
                    break;
            }
        }

        return super.onTouchEvent(event);
    }



    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;
    final int LEFT_UP = 4;
    final int RIGHT_UP = 5;
    final int LEFT_DOWN = 6;
    final int RIGHT_DOWN = 7;

    public int getAction(float x,float y) {

        float deltaX = x - centerX;
        float deltaY = y - centerY;

        if (Math.sqrt((deltaX * deltaX + deltaY * deltaY)) > radius
                && Math.abs(Math.abs(deltaX)-Math.abs(deltaY))<radius)
        {
            if(deltaX<0 && deltaY<0)
                return LEFT_UP;
            if(deltaX<0 && deltaY>0)
                return LEFT_DOWN;
            if(deltaX>0 && deltaY<0)
                return RIGHT_UP;
            if(deltaX>0 && deltaY>0)
                return RIGHT_DOWN;
        }

        if (Math.abs(deltaX) > Math.abs(deltaY))
        {
            if (deltaX > 0)
                return RIGHT;
            else
                return LEFT;
        }
        else
        {
            if (deltaY > 0)
                return DOWN;
            else
                return UP;
        }

    }

    public void move(int ACTION)
    {
        switch (ACTION)
        {
            case UP:
                objBall.move_up();
                break;
            case DOWN:
                objBall.move_down();
                break;
            case RIGHT:
                objBall.move_right();
                break;
            case LEFT:
                objBall.move_left();
                break;
            case RIGHT_DOWN:
                objBall.move_right_down();
                break;
            case RIGHT_UP:
                objBall.move_right_up();
                break;
            case LEFT_DOWN:
                objBall.move_left_down();
                break;
            case LEFT_UP:
                objBall.move_left_up();
                break;
        }
    }

}
