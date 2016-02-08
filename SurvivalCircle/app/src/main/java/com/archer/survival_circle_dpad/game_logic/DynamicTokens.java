package com.archer.survival_circle_dpad.game_logic;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archer.survival_circle_dpad.Helper;
import com.archer.survival_circle_dpad.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Swastik on 13-01-2016.
 */
public class DynamicTokens {

    Random objRnd;
    ViewGroup mainContainer;
    List<View> activeTokens = new ArrayList<>();

    SinglePlayerGame objSinglePlayerGame;
    PlusOneToken objPlusOneToken = new PlusOneToken();
    PlusTwoToken objPlusTwoToken = new PlusTwoToken();


    PlusThreeToken objPlusThreeToken = new PlusThreeToken();
    MinusOneToken objMinusOneToken = new MinusOneToken();
    MinusSixToken objMinusSixToken = new MinusSixToken();

    PointF negative_tokens_positions[];
    PointF negative_6_tokens_positions[];
    PointF positive_tokens_positions[];
    int positive_token_index;
    int negative_token_index;
    int negative_6_token_index;

    public DynamicTokens(WeakReference<SinglePlayerGame> _objSinglePlayerGame)
    {
        objRnd = new Random();
        objSinglePlayerGame = _objSinglePlayerGame.get();
        mainContainer = (RelativeLayout)objSinglePlayerGame.objBall.getParent();
        createPositionMap();
    }

    public void createPositionMap() {
        positive_tokens_positions = new PointF[15];
        negative_tokens_positions = new PointF[16];
        negative_6_tokens_positions = new PointF[4];
        positive_token_index = 0;
        negative_token_index = 0;
        negative_6_token_index = 0;

        float x_pos_positive_tokens[] = new float[5];
        float y_pos_positive_tokens[] = new float[3];

        int k = 0,l=0;
        for(int i = 0;i<4;i++)
        {
            for (int j = 0; j < 4; j++) {
                negative_tokens_positions[k++] =
                        new PointF(objSinglePlayerGame.objBall.x_pos[i],objSinglePlayerGame.objBall.y_pos[j]);

                if(i!=0 && i!=3 && j!=0 && j!=3)
                    negative_6_tokens_positions[l++] =
                            new PointF(objSinglePlayerGame.objBall.x_pos[i],objSinglePlayerGame.objBall.y_pos[j]);
            }
        }

        float cell_width = Helper.SCREEN_WIDTH/4;
        float cell_height = Helper.SCREEN_HEIGHT/6;

        x_pos_positive_tokens[0] = objSinglePlayerGame.objBall.x_pos[0] + cell_width/2;
        for(int i = 1; i<5; i++)
            x_pos_positive_tokens[i] = x_pos_positive_tokens[i-1]+cell_width/2;

        y_pos_positive_tokens[0] = objSinglePlayerGame.objBall.y_pos[0] + cell_height/2;
        for(int i = 1; i<3; i++)
            y_pos_positive_tokens[i] = objSinglePlayerGame.objBall.y_pos[i] + cell_height/2;

        k=0;
        for(int i = 0;i<3;i++)
        {
            for (int j = 0; j < 5; j++) {
                positive_tokens_positions[k++]
                        = new PointF(x_pos_positive_tokens[j],y_pos_positive_tokens[i]);
            }
        }


    }


    public void addPlusOneToken()
    {
        objPlusOneToken.CreatePlusOneToken(8200);
    }
    public void addPlusTwoToken()
    {
        objPlusTwoToken.CreatePlusTwoToken(8200);
    }
    public void addPlusThreeToken()
    {
        objPlusThreeToken.CreatePlusThreeToken(8200);
    }
    public void addMinusOneToken()
    {
        objMinusOneToken.CreateMinusOneToken(8200);
    }

    public void addMinusSixToken()
    {
        objMinusSixToken.CreateMinusSixToken();
    }

    private View getTokenView(int background_res,String text,int tag)
    {
        RelativeLayout cell = new RelativeLayout(objSinglePlayerGame.mContext);
        cell.setTag(tag);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cell.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams layoutParams_r = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams_r.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);


        TextView textView = new TextView(objSinglePlayerGame.mContext);
        textView.setLayoutParams(layoutParams_r);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setBackgroundResource(background_res);

        cell.addView(textView);

        switch (tag )
        {
            case 1:
            case 2:
            case 3:
                positive_token_index++;
                if(positive_token_index>=positive_tokens_positions.length)
                {
                    positive_token_index=0;
                    Helper.shuffleArray(positive_tokens_positions,positive_tokens_positions.length);
                }
                cell.setX(positive_tokens_positions[positive_token_index].x);
                cell.setY(positive_tokens_positions[positive_token_index].y);
                break;
            case -1:
                negative_token_index++;
                if(negative_token_index>=negative_tokens_positions.length)
                {
                    negative_token_index=0;
                    Helper.shuffleArray(negative_tokens_positions,negative_tokens_positions.length);
                }
                cell.setX(negative_tokens_positions[negative_token_index].x);
                cell.setY(negative_tokens_positions[negative_token_index].y);
                break;
            case -2:
                negative_6_token_index++;
                if(negative_6_token_index>=negative_6_tokens_positions.length)
                {
                    negative_6_token_index=0;
                    Helper.shuffleArray(negative_6_tokens_positions,negative_6_tokens_positions.length);
                }
                cell.setX(negative_6_tokens_positions[negative_6_token_index].x);
                cell.setY(negative_6_tokens_positions[negative_6_token_index].y);
                break;
        }
        return cell;
    }


    class PlusOneToken
    {
        public void CreatePlusOneToken(long milliSecondsTillTimeOut)
        {
            final View tokenView = getTokenView(R.drawable.background_blue_circle,"+1",1);
            mainContainer.addView(tokenView);
            activeTokens.add(tokenView);

            final CountDownTimer objCountDownTimer = new CountDownTimer(milliSecondsTillTimeOut,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    tokenView.setTag("0");
                    removeToken(tokenView);
                }
            };
            objCountDownTimer.start();
        }
    }

    class PlusTwoToken
    {
        public void CreatePlusTwoToken(long milliSecondsTillTimeOut)
        {
            final View tokenView = getTokenView(R.drawable.background_red_circle,"+2",2);
            mainContainer.addView(tokenView);
            activeTokens.add(tokenView);

            final CountDownTimer objCountDownTimer = new CountDownTimer(milliSecondsTillTimeOut,milliSecondsTillTimeOut) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    tokenView.setTag("0");
                    removeToken(tokenView);
                }
            };
            objCountDownTimer.start();
        }
    }

    class PlusThreeToken
    {
        public void CreatePlusThreeToken(long milliSecondsTillTimeOut)
        {
            final View tokenView = getTokenView(R.drawable.background_black_circle,"+3",3);
            mainContainer.addView(tokenView);
            activeTokens.add(tokenView);

            final CountDownTimer objCountDownTimer = new CountDownTimer(milliSecondsTillTimeOut,milliSecondsTillTimeOut) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    tokenView.setTag("0");
                    removeToken(tokenView);
                }
            };
            objCountDownTimer.start();
        }
    }

    class MinusOneToken
    {
        public void CreateMinusOneToken(long milliSecondsTillTimeOut)
        {
            final View tokenView = getTokenView(R.drawable.background_blue_square,"-1",-1);
            mainContainer.addView(tokenView);
            activeTokens.add(tokenView);

            final CountDownTimer objCountDownTimer = new CountDownTimer(milliSecondsTillTimeOut,milliSecondsTillTimeOut) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    tokenView.setTag("0");
                    removeToken(tokenView);
                }
            };

            objCountDownTimer.start();
        }
    }

    class MinusSixToken
    {
        public void CreateMinusSixToken()
        {
            final View tokenView = getTokenView(R.drawable.background_black_square,"-2",-2);
            mainContainer.addView(tokenView);
            activeTokens.add(tokenView);
        }
    }

    public void removeToken(View v)
    {
        if(mainContainer.indexOfChild(v)>=0)
            mainContainer.removeView(v);
    }

    public void flushAllTokensFromScreen()
    {
        for (View v : activeTokens)
            removeToken(v);
        activeTokens.clear();
    }

}
