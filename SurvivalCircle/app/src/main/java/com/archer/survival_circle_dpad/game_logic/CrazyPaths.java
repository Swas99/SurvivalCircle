package com.archer.survival_circle_dpad.game_logic;

import android.graphics.PointF;
import android.os.Build;

import com.archer.survival_circle_dpad.Helper;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CrazyPaths {

    HollowCircleView objSurvivalCircle;
    int path_type_index;
    long start_time;
    float duration;

    public int pos_index;
    PointF all_pos[];

    Random random_generator = new Random();

    int min_x,min_y,max_x,max_y;

    Bezier objBezier = new Bezier();

    public CrazyPaths(WeakReference<HollowCircleView> _objSurvivalCircle)
    {
        objSurvivalCircle = _objSurvivalCircle.get();

        min_x = min_y = 0;
        max_x = (int)(Helper.SCREEN_WIDTH/2);
        max_y = (int)(Helper.SCREEN_HEIGHT/3);

        pos_index=0;
        all_pos = new PointF[9];
        int k=0;
        for (int i=0;i<3;i++)
        {
            for (int j = 0; j < 3; j++) {
                all_pos[k++] = new PointF(objSurvivalCircle.x_pos[i], objSurvivalCircle.y_pos[j]);
            }
        }

        path_type_index=0;
    }

    public void refresh(long _start_time,long _duration)
    {
        start_time = _start_time;
        duration = _duration;

        switch (path_type_index)
        {
            case 0:
                objBezier.generateControlPoints();
                break;
        }
    }

    public PointF getNextPos(long running_time) {

        if(running_time == 0 || running_time>start_time+duration)
            refresh(running_time,(long)duration-2);

        PointF next_pos;
        switch (path_type_index)
        {
            case 0:
                next_pos = new PointF(
                        objBezier.calcBezier_x(running_time),
                        objBezier.calcBezier_y(running_time));
                break;
            default:
                next_pos = new PointF(
                        objBezier.calcBezier_x(running_time),
                        objBezier.calcBezier_y(running_time));
        }


        return next_pos;
    }

    private void shuffleArray(PointF[] ar,int length)
    {
        // If running on Java 6 or older, use `new Random()` on RHS
        Random rnd;
        if(Build.VERSION.SDK_INT >= 21 )
            rnd = ThreadLocalRandom.current();
        else
            rnd = new Random();

        for (int i = length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            PointF a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    class Bezier
    {
        public PointF mStart = new PointF();
        public PointF mControl = new PointF();
        public PointF mEnd = new PointF();



        public void generateControlPoints()
        {
            pos_index++;
            if(pos_index==4)
            {
                pos_index = 0;
                shuffleArray(all_pos,all_pos.length);
            }

            mStart.x = objSurvivalCircle.getX();
            mStart.y = objSurvivalCircle.getY();


            mEnd.x = all_pos[pos_index].x;
            mEnd.y = all_pos[pos_index].y;

//            mEnd.x = random_generator.nextInt(max_x) + min_x;
//            mEnd.y = random_generator.nextInt(max_y) + min_y;


            mControl.x = random_generator.nextInt(max_x-min_x) + min_x;
            mControl.y = random_generator.nextInt(max_y-min_y) + min_y;
        }

        private float calcBezier_x(long current_time) {
            float interpolatedTime = (current_time-start_time)/duration;

            float p0 = mStart.x;
            float p1 = mControl.x;
            float p2 = mEnd.x;

            return calcBezier(interpolatedTime,p0,p1,p2);
        }

        private float calcBezier_y(long current_time) {
            float interpolatedTime = (current_time-start_time)/duration;

            float p0 = mStart.y;
            float p1 = mControl.y;
            float p2 = mEnd.y;

            return calcBezier(interpolatedTime,p0,p1,p2);
        }

        private float calcBezier(float interpolatedTime,float p0,float p1,float p2)
        {
            return Math.round((Math.pow((1 - interpolatedTime), 2) * p0)
                    + (2 * (1 - interpolatedTime) * interpolatedTime * p1)
                    + (Math.pow(interpolatedTime, 2) * p2));
        }
    }
}
