package com.archer.survival_circle_dpad.game_logic;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.archer.survival_circle_dpad.Helper;

/**
 * Created by Swastik on 17-01-2016.
 */
public class HollowCircleView extends View
{
    float radius;
    float centerX;
    float centerY;

    float x_pos[];
    float y_pos[];

    Context ct;

    /**
     * Default constructor
     *
     * @param ct {@link Context}
     */
    public HollowCircleView(final Context ct) {
        super(ct);
        this.ct = ct;
    }

    public HollowCircleView(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);
        this.ct = ct;
    }

    public HollowCircleView(final Context ct, final AttributeSet attrs, final int defStyle) {
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

    private void setDimensions()
    {
        int h = (int)Helper.SCREEN_HEIGHT/3;
        int w = (int)Helper.SCREEN_WIDTH/2;

        radius = Math.min(h,w)/2;
        getLayoutParams().height = (int)radius*2;
        getLayoutParams().width = (int)radius*2;
        setLayoutParams(getLayoutParams());

        x_pos = new float[3];
        y_pos = new float[3];
        x_pos[0] = -Helper.ConvertToPx(ct,45);
        x_pos[1] = w - radius;
        x_pos[2] = 2*w -2*radius+Helper.ConvertToPx(ct,45);

        y_pos[0] = -Helper.ConvertToPx(ct,45);
        y_pos[1] = h - radius;
        y_pos[2] = 2*h-2*radius+Helper.ConvertToPx(ct,45);;
    }

    public void recompute_position()
    {
        centerX = getX()+radius;
        centerY = getY()+radius;
    }

    public void init() {
        setDimensions();
    }
}
