package com.archer.survival_circle_dpad.game_logic;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.archer.survival_circle_dpad.Helper;


public class BallView extends View
{
    Context ct;
    boolean isAnimating;
    float radius;
    float centerX;
    float centerY;

    float x_pos[];
    float y_pos[];
    int current_x;
    int current_y;

    float prevScaleX;
    float prevScaleY;

    /**
     * Default constructor
     *
     * @param ct {@link Context}
     */
    public BallView(final Context ct) {
        super(ct);
        this.ct = ct;
    }

    public BallView(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);
        this.ct = ct;
    }

    public BallView(final Context ct, final AttributeSet attrs, final int defStyle) {
        super(ct, attrs, defStyle);
        this.ct = ct;
    }

    @Override
    public void onDraw(final Canvas canv) {

    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void move_right()
    {
        current_x++;
        if(current_x==x_pos.length)
            current_x--;
        move(1.2f, .6f);
    }
    public void move_left()
    {
        current_x--;
        if(current_x==-1)
            current_x=0;
        move(1.2f,.6f);
    }
    public void move_up()
    {
        current_y--;
        if(current_y==-1)
            current_y=0;
        move(.6f,1.2f);
    }
    public void move_down()
    {
        current_y++;
        if(current_y==y_pos.length)
            current_y--;
        move(.6f, 1.2f);
    }
    public void move_right_down()
    {
        setRotation(135);
        move_right();
        move_down();
    }
    public void move_left_down()
    {
        setRotation(225);
        move_left();
        move_down();
    }
    public void move_right_up()
    {
        setRotation(225);
        move_right();
        move_up();
    }
    public void move_left_up()
    {
        setRotation(135);
        move_left();
        move_up();
    }
    public void move(float x_scale_effect,float y_scale_effect)
    {
        if(!isAnimating)
        {
            prevScaleX = getScaleX();
            prevScaleY = getScaleY();
        }

        animate()
                .x(x_pos[current_x])
                .y(y_pos[current_y])
                .scaleX(prevScaleX * x_scale_effect)
                .scaleY(prevScaleY * y_scale_effect);
    }


    public void setAnimationListener()
    {

        animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setScaleX(prevScaleX);
                setScaleY(prevScaleY);
                setRotation(0);
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void init()
    {
        setAnimationListener();
        setDimensions();
    }
    private void setDimensions()
    {
        recompute_position();
        float cell_width = Helper.SCREEN_WIDTH/4;
        float cell_height = Helper.SCREEN_HEIGHT/6;

        radius = Helper.ConvertToPx(ct,22);

        x_pos = new float[4];
        y_pos = new float[4];
        x_pos[0] = cell_width/2 - radius;
        y_pos[0] = cell_height/2 - radius;

        for (int i=1;i<4;i++)
        {
            x_pos[i]=x_pos[i-1]+cell_width;
            y_pos[i]=y_pos[i-1]+cell_height;
        }
        current_x=1;
        current_y=2;
    }

    public void recompute_position()
    {
        radius = getWidth()/2f;
        centerX = getX()+radius;
        centerY = getY()+radius;
    }

}