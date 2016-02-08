package com.archer.survival_circle_dpad.game_logic;

import com.archer.survival_circle_dpad.MainActivity;

import java.lang.ref.WeakReference;

/**
 * Created by Swastik on 20-01-2016.
 */
public class StartGame {

    MainActivity mContext;

    public StartGame(WeakReference<MainActivity> m_context)
    {
        mContext = m_context.get();
        init();
    }

    private void init() {
//        ball = (BallView)findViewById(R.id.ball);
//        dPad_1 = (DirectionPadView)findViewById(R.id.d_pad_one);
//        objCrazyCircle = (HollowCircleView)findViewById(R.id.circle);
//
//        WeakReference<MainActivity> m_context = new WeakReference<>(this);
//        dPad_1.init(m_context);
//        ball.init(m_context);
//        objCrazyCircle.init(m_context);
    }
}
