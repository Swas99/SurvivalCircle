package com.archer.survival_circle_dpad;

import android.animation.Animator;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.archer.survival_circle_dpad.game_logic.SinglePlayerGame;

import java.lang.ref.WeakReference;
import java.util.Random;

public class MainViewManager {

    MainActivity mContext;
    ViewGroup Root;

    int CurrentScreen;
    View.OnClickListener clickListener;
    SinglePlayerGame objSinglePlayerGame;


    //region BackGround Colors
    public int All_Background_Res[] =
            {
                    R.drawable.background_blue_1,R.drawable.background_blue_2,
                    R.drawable.background_blue_3,R.drawable.background_green_1,
                    R.drawable.background_green_2,R.drawable.background_orange_1
            };
//    int All_Background_Colors[] =
//            {
//                    Color.argb(0xff, 0xa1, 0x7f, 0xff),Color.argb(0xff,0x75,0x77,0xff),
//                    Color.argb(0xff,0x07,0xa2,0xc9),Color.argb(0xff,0x46,0xa7,0x5e),
//                    Color.argb(0xff,0x46,0xc7,0x2f),Color.argb(0xff,0xcb,0x9a,0x41),
//            };

    int img_music_on_backgrounds[] =
            {
                    R.drawable.img_music_on_1,R.drawable.img_music_on_2,
                    R.drawable.img_music_on_3,R.drawable.img_music_on_5,
                    R.drawable.img_music_on_6,R.drawable.img_music_on_7,
                    R.drawable.img_music_on_4
            };
    int img_music_off_backgrounds[] =
            {
                    R.drawable.img_music_off_1,R.drawable.img_music_off_2,
                    R.drawable.img_music_off_3,R.drawable.img_music_off_5,
                    R.drawable.img_music_off_6,R.drawable.img_music_off_7,
                    R.drawable.img_music_off_4
            };
    int img_sound_on_backgrounds[] =
            {
                    R.drawable.img_sound_on_1,R.drawable.img_sound_on_2,
                    R.drawable.img_sound_on_3,R.drawable.img_sound_on_5,
                    R.drawable.img_sound_on_6,R.drawable.img_sound_on_7,
                    R.drawable.img_sound_on_4
            };
    int img_sound_off_backgrounds[] =
            {
                    R.drawable.img_sound_off_1,R.drawable.img_sound_off_2,
                    R.drawable.img_sound_off_3,R.drawable.img_sound_off_5,
                    R.drawable.img_sound_off_6,R.drawable.img_sound_off_7,
                    R.drawable.img_sound_off_4
            };
    //endregion

    public int Background_Index;

    public MainViewManager(WeakReference<MainActivity> m_context)
    {
        mContext = m_context.get();
        initialize_on_click_listener();


        setUpBackgroundChangeMechanism();
        load_animated_home_screen();
        addGestureListenerToView(Root);
        Point p = Helper.getWindowSize(mContext.getWindowManager().getDefaultDisplay());
        Helper.SCREEN_HEIGHT =p.y;
        Helper.SCREEN_WIDTH = p.x;

    }


    private void addGestureListenerToView(View v) {
        final GestureDetector gdt = new GestureDetector(mContext,new GestureListener());
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });
    }

    private void setUpBackgroundChangeMechanism()
    {
        Random objRandom = new Random();
        Background_Index = objRandom.nextInt(6);
    }

    public void reloadBackground() {
//        int prev_index = Background_Index;

        Background_Index++;
        if(Background_Index==All_Background_Res.length)
            Background_Index=0;


        Root.setBackgroundResource(All_Background_Res[Background_Index]);
        Root.setAlpha(0.7f);
        Root.animate().alpha(1).setDuration(100);
//        int colorFrom = All_Background_Colors[prev_index];
//        int colorTo = All_Background_Colors[Background_Index];
//
//        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
//        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animator) {
//                Root.setBackgroundColor((int) animator.getAnimatedValue());
//
//                if (animator.getAnimatedFraction() == 1)
//                {
//                    Root.setBackgroundResource(All_Background_Res[Background_Index]);
//                    if (CurrentScreen == R.id.home_screen)
//                        addBackgroundResForHomeScreenViews();
//                }
//            }
//        });
//        colorAnimation.start();
    }

    public void changeViewBackground(int index,View v)
    {
        v.setBackgroundResource(All_Background_Res[index]);
        v.setAlpha(0.5f);
        v.animate().alpha(1);
    }

    public void ScaleUP_ThenRevert(final View v)
    {
        final int duration = 200;
        v.animate()
                .scaleX(1.18f)
                .scaleY(1.18f)
                .setDuration(duration);

        new CountDownTimer(202,202) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                v.animate()
                        .scaleX(1)
                        .scaleY(1)
                        .setDuration(200);
            }
        }.start();
    }


    private void initialize_on_click_listener()
    {
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId())
                {

                    //region home screen
                    case R.id.btn_sound:
                        ScaleUP_ThenRevert(mContext.findViewById(R.id.img_sound));
                        mContext.objSoundManager.toggleGameSounds();
                        setSoundsButtonBackground();
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        break;
                    case R.id.btn_music:
                        ScaleUP_ThenRevert(mContext.findViewById(R.id.img_music));
                        mContext.objSoundManager.toggleBackgroundMusic();
                        setMusicButtonBackgrounds();
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        break;
                    case R.id.btn_play:
                        reloadBackground();
                        load_single_player_game();
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        break;
                    case R.id.btn_multi_play:
                        reloadBackground();
                        ScaleUP_ThenRevert(mContext.findViewById(R.id.img_multi_play));
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        break;
                    case R.id.btn_leader_board:
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        ScaleUP_ThenRevert(mContext.findViewById(R.id.img_leader_board));
                        mContext.objGameServices.onShowLeaderBoardRequested(mContext.getString(R.string.leaderboard_top_score));
                        break;
                    case R.id.btn_settings:
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        load_settings_screen();
                        break;
                    case R.id.btn_rate:
                        ScaleUP_ThenRevert(mContext.findViewById(R.id.img_rate));
                        Helper.openPlayStorePage(new WeakReference<>(mContext));
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK_2);
                        break;
                    case R.id.btn_share:
                    {
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        ScaleUP_ThenRevert(mContext.findViewById(R.id.img_share));
                        WeakReference<MainActivity> m_context = new WeakReference<>(mContext);
                        String msg = "Beat my score of " + mContext.BestScore +
                                " in the #SurvivalCircle game on Android " +
                                "https://play.google.com/store/apps/details?id=com.archer.survival_circle_dpad";
                        Helper.takeScreenShotAndShare(m_context, msg);
                        break;
                    }
                    //endregion

                    //region Settings Screen controls
                    case R.id.btn_vibration_settings:
                        ScaleUP_ThenRevert(v);
                        mContext.toggle_vibration();
                        mContext.objVibrator.vibrate(50);
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        break;
                    case R.id.btn_music_settings:
                        ScaleUP_ThenRevert(v);
                        mContext.objSoundManager.toggleBackgroundMusic();
                        setMusicButtonBackgrounds();
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        break;
                    case R.id.btn_sound_settings:
                        ScaleUP_ThenRevert(v);
                        mContext.objSoundManager.toggleGameSounds();
                        setSoundsButtonBackground();
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        break;
                    case R.id.btn_back_settings:
                        gotoHome_SlideRight();
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        break;
                    //endregion

                    case R.id.btn_back_single_player_game:
                        objSinglePlayerGame.stopGame();
                        gotoHome_SlideRight();
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        break;

                    case R.id.img_top_score_single_player_game:
                        mContext.objSoundManager.Play(mContext.objSoundManager.CLICK);
                        ScaleUP_ThenRevert(v);
                        mContext.objGameServices.onShowLeaderBoardRequested(mContext.getString(R.string.leaderboard_top_score));
                        break;
                }
            }
        };
    }

    boolean isLoadingView;
    public void slide_right(View view_to_load,final View view_to_remove)
    {
        isLoadingView=true;
        view_to_remove.animate()
                .translationX(Helper.SCREEN_WIDTH/2)
                .scaleX(0)
                .setDuration(603)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        remove_view(view_to_remove);
                        isLoadingView=false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

        view_to_load.setTranslationX(-Helper.SCREEN_WIDTH);
        view_to_load.setScaleX(0);
        view_to_load.animate()
                .translationX(0)
                .scaleX(1)
                .setDuration(603);

        new CountDownTimer(700, 700) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }
    public void slide_left(View view_to_load, final View view_to_remove)
    {
        isLoadingView=true;
        view_to_remove.animate()
                .translationX(-Helper.SCREEN_WIDTH/2)
                .scaleX(0)
                .setDuration(603)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        remove_view(view_to_remove);
                        isLoadingView=false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

            }
        });

        view_to_load.setTranslationX(Helper.SCREEN_WIDTH);
        view_to_load.setScaleX(0);
        view_to_load.animate()
                .translationX(0)
                .scaleX(1)
                .setDuration(603);
    }


    private void load_single_player_game() {
        if(isLoadingView)
            return;

        View view_to_load,view_to_remove;
        LayoutInflater inflater = mContext.getLayoutInflater();

        view_to_load = inflater.inflate(R.layout.view_single_player_game, Root, false);
        Root.addView(view_to_load.findViewById(R.id.single_player_game));
        view_to_remove = Root.getChildAt(0);
        remove_view(view_to_remove);
        view_to_load.setAlpha(0);
        view_to_load.animate().alpha(1);

        CurrentScreen = R.id.single_player_game;

        view_to_load.findViewById(R.id.btn_back_single_player_game)
                .setOnClickListener(clickListener);
        view_to_load.findViewById(R.id.img_top_score_single_player_game)
                .setOnClickListener(clickListener);

        objSinglePlayerGame = new SinglePlayerGame(new WeakReference<>(mContext));

    }

    public void load_settings_screen()
    {
        if(isLoadingView)
            return;

        View view_to_load;
        LayoutInflater inflater = mContext.getLayoutInflater();

        view_to_load = inflater.inflate(R.layout.view_settings, Root, false);
        Root.addView(view_to_load.findViewById(R.id.settings_screen));
        slide_left(view_to_load, Root.getChildAt(0));


        initialize_settings_screen();
    }

    public void initialize_settings_screen()
    {
        CurrentScreen = R.id.settings_screen;
        int ids[] = {R.id.btn_vibration_settings,R.id.btn_music_settings,R.id.btn_sound_settings,R.id.btn_back_settings};
        for (int id: ids)
            mContext.findViewById(id).setOnClickListener(clickListener);

        ((TextView) mContext.findViewById(R.id.tvBestScore_settings))
                .setText(String.valueOf(mContext.BestScore));
        ((TextView)mContext.findViewById(R.id.tvTotalScore))
                .setText(String.valueOf(mContext.TotalScore));
        ((TextView)mContext.findViewById(R.id.tvAverageScore))
                .setText(String.valueOf(mContext.getAverageScore()));
        ((TextView)mContext.findViewById(R.id.tvTotalGames_settings))
                .setText(String.valueOf(mContext.TotalGames));

        if(mContext.vibration_on)
            mContext.findViewById(R.id.btn_vibration_settings)
                    .setBackgroundResource(R.drawable.double_circle_white);
        else
            mContext.findViewById(R.id.btn_vibration_settings)
                    .setBackgroundResource(R.drawable.hollow_white_circle);

        setMusicButtonBackgrounds();
        setSoundsButtonBackground();

        mContext.objManageAds.DisplayBannerAd(R.id.adView_settings);
    }

    public void load_animated_home_screen()
    {
        View view_to_load;
        LayoutInflater inflater = mContext.getLayoutInflater();
        view_to_load = inflater.inflate(R.layout.activity_main, null, false);
        mContext.setContentView(view_to_load);
        Root = (ViewGroup)mContext.findViewById(R.id.Root);
        Root.setBackgroundResource(All_Background_Res[Background_Index]);

        view_to_load = inflater.inflate(R.layout.view_home, Root, false);
        Root.addView(view_to_load.findViewById(R.id.home_screen));

        View title_1 = mContext.findViewById(R.id.tvTitle1);
        View title_2 = mContext.findViewById(R.id.tvTitle2);
        View divider = mContext.findViewById(R.id.divider);
        View tvBest = mContext.findViewById(R.id.tvBestScore_home);
        View tvGames = mContext.findViewById(R.id.tvTotalGames);

        tvBest.setTranslationY(-90);
        tvBest.animate().translationY(0).setDuration(702);

        tvGames.setTranslationY(-90);
        tvGames.animate().translationY(0).setDuration(702);

        title_1.setScaleY(0);
        title_1.setTranslationY(50);
        title_1.animate().translationY(0).scaleY(1).setDuration(801);

        title_2.setScaleY(0);
        title_2.setTranslationY(-50);
        title_2.animate().translationY(0).scaleY(1.2f).setDuration(801);

        divider.setScaleX(0);
        divider.animate().scaleX(1).setDuration(900);

        load_scoring_data();
        initialize_home_view_controls();
    }

    private void load_scoring_data() {
        WeakReference<MainActivity> m_context = new WeakReference<>(mContext);
        String score_data[] = Helper.readFromFile(m_context, Helper.TOP_SCORE_FILE).split("_");
        mContext.BestScore = Long.parseLong(score_data[0]);
        mContext.TotalGames = Long.parseLong(score_data[1]);
        mContext.TotalScore = Long.parseLong(score_data[2]);
    }

    private void initialize_home_view_controls() {
        CurrentScreen = R.id.home_screen;

        ((TextView) mContext.findViewById(R.id.tvBestScore_home))
                .setText("BEST: " + String.valueOf(mContext.BestScore));
        ((TextView)mContext.findViewById(R.id.tvTotalGames))
                .setText("GAMES: " + String.valueOf(mContext.TotalGames));

        mContext.findViewById(R.id.tvTitle2).animate().scaleY(1.2f).translationY(0);

        int ids[] = {R.id.btn_play,R.id.btn_leader_board,
                R.id.btn_multi_play,
                R.id.btn_music,
                R.id.btn_sound,R.id.btn_settings,R.id.btn_share,R.id.btn_rate};

        for (int id: ids)
            mContext.findViewById(id).setOnClickListener(clickListener);


        addBackgroundResForHomeScreenViews();


        mContext.objManageAds.DisplayBannerAd(R.id.adView_home);
    }

    private void addBackgroundResForHomeScreenViews() {
        int ids[] = {R.id.img_play,R.id.img_leader_board,
                R.id.img_multi_play,
                R.id.img_settings,R.id.img_share,R.id.img_rate};
        //region image resources
        int background_res[][] =
                {
                        {
                                R.drawable.img_btn_play_1,R.drawable.img_btn_play_2,
                                R.drawable.img_btn_play_3,R.drawable.img_btn_play_5,
                                R.drawable.img_btn_play_6,R.drawable.img_btn_play_7,
                                R.drawable.img_btn_play_4
                        },
                        {
                                R.drawable.img_btn_top_scores_1,R.drawable.img_btn_top_scores_2,
                                R.drawable.img_btn_top_scores_3,R.drawable.img_btn_top_scores_5,
                                R.drawable.img_btn_top_scores_6,R.drawable.img_btn_top_scores_7,
                                R.drawable.img_btn_top_scores_4
                        },
                        {
                                R.drawable.img_multiplayer_1,R.drawable.img_multiplayer_2,
                                R.drawable.img_multiplayer_3,R.drawable.img_multiplayer_5,
                                R.drawable.img_multiplayer_6,R.drawable.img_multiplayer_7,
                                R.drawable.img_multiplayer_4
                        },
                        {
                                R.drawable.img_settings_1,R.drawable.img_settings_2,
                                R.drawable.img_settings_3,R.drawable.img_settings_5,
                                R.drawable.img_settings_6,R.drawable.img_settings_7,
                                R.drawable.img_settings_4
                        },
                        {
                                R.drawable.img_share_1,R.drawable.img_share_2,
                                R.drawable.img_share_3,R.drawable.img_share_5,
                                R.drawable.img_share_6,R.drawable.img_share_7,
                                R.drawable.img_share_4
                        },
                        {
                                R.drawable.img_btn_rate_1,R.drawable.img_btn_rate_2,
                                R.drawable.img_btn_rate_3,R.drawable.img_btn_rate_5,
                                R.drawable.img_btn_rate_6,R.drawable.img_btn_rate_7,
                                R.drawable.img_btn_rate_4
                        }
                };
        //endregion

        for (int i=0;i<ids.length;i++)
        {
            mContext.findViewById(ids[i])
                    .setBackgroundResource(background_res[i][Background_Index]);
        }
        setSoundsButtonBackground();
        setMusicButtonBackgrounds();
    }

    public void setSoundsButtonBackground()
    {
        int x[] = new int[2];
        x[0]= img_sound_off_backgrounds[Background_Index];
        x[1]= img_sound_on_backgrounds[Background_Index];
        mContext.objSoundManager.setSoundButtonBackground(x);
    }
    public void setMusicButtonBackgrounds()
    {
        int x[] = new int[2];
        x[0]= img_music_off_backgrounds[Background_Index];
        x[1]= img_music_on_backgrounds[Background_Index];
        mContext.objSoundManager.setMusicButtonBackground(x);
    }


    boolean backPressFlag;
    public void setBackPressFlag()
    {
        try {
            backPressFlag = true;
            Toast.makeText(mContext, "Tap again to exit", Toast.LENGTH_SHORT).show();
            new CountDownTimer(2700, 2700) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    backPressFlag = false;
                }
            }.start();
        }
        catch (Exception ex){
            Toast.makeText(mContext,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public void onBackPress()
    {
        switch (CurrentScreen)
        {
            case R.id.home_screen:
                if(!backPressFlag)
                    setBackPressFlag();
                else
                    mContext.finish();
                break;
            case R.id.settings_screen:
                gotoHome_SlideRight();
                break;
            case R.id.single_player_game:
            {
                objSinglePlayerGame.stopGame();
                gotoHome_SlideRight();
                break;
            }
//            case R.id.multi_player_game:
//            {
//            gotoHome_SlideLeft();
//                break;
//            }


        }
        mContext.CollectGarbage();
    }

    private void gotoHome_SlideRight()
    {
        if(isLoadingView)
            return;

        LayoutInflater inflater = mContext.getLayoutInflater();
        View view_to_load = inflater.inflate(R.layout.view_home, Root, false);
        Root.addView(view_to_load.findViewById(R.id.home_screen));
        View view_to_remove = Root.getChildAt(0);
        slide_right(view_to_load, view_to_remove);
        initialize_home_view_controls();
    }

//    private void gotoHome_SlideLeft()
//    {
//        if(isLoadingView)
//            return;
//        LayoutInflater inflater = mContext.getLayoutInflater();
//        View view_to_load = inflater.inflate(R.layout.view_home, Root, false);
//        Root.addView(view_to_load.findViewById(R.id.home_screen));
//        View view_to_remove = Root.getChildAt(0);
//        slide_left(view_to_load, view_to_remove);
//        initialize_home_view_controls();
//    }

    public void remove_view(View v)
    {
        ViewGroup parent = (ViewGroup)v.getParent();
        if(parent!=null)
            parent.removeView(v);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private final int SWIPE_MIN_DISTANCE = 153;
        private final int SWIPE_THRESHOLD_VELOCITY = 20;
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            {
//                if(CurrentScreen == R.id.settings_screen || CurrentScreen == R.id.single_player_game)
//                    gotoHome_SlideLeft();
                return false; // Right to left
            }
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE ) //&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            {
                if(CurrentScreen == R.id.single_player_game)
                {
                    objSinglePlayerGame.stopGame();
                }

                if(CurrentScreen == R.id.settings_screen || CurrentScreen == R.id.single_player_game)
                    gotoHome_SlideRight();
                return false; // Left to right
            }

            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE)// && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
            {
                return false; // Bottom to top
            }
            else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE)// && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
            {
                return false; // Top to bottom
            }
            return true;
        }

//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//                                float distanceY) {
//            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE*10 ) //&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
//            {
//                GameBackground++;
//                InitializeScreenControls_BoardDetails();
//                return false; // Right to left
//            }
//            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE*10 ) //&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
//            {
//                GameBackground--;
//                InitializeScreenControls_BoardDetails();
//                return false; // Left to right
//            }
//            return true;
//        }
    }


}
