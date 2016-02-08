package com.archer.survival_circle_dpad;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Swastik on 18-01-2016.
 */
public class Helper {

    public static final int GAME_SOUND = 100;
    public static final int BACKGROUND_MUSIC = 102;
    public static final int TOP_SCORE_UPDATE_REQUIRED = 201;
    public static final int VIBRATION_VALUE = 301;


    public final static String TOP_SCORE_FILE = "config.txt";
    public final static String UPDATE_FILE = "update";

    public static float SCREEN_WIDTH;
    public static float SCREEN_HEIGHT;

    public static void takeScreenShotAndShare(WeakReference<MainActivity> m_context,String msg)
    {
        MainActivity mContext = m_context.get();

        //region create screenshot
        View mainView = mContext.getWindow().getDecorView().getRootView();

        mainView.setDrawingCacheEnabled(true);
        Bitmap bitmap = mainView.getDrawingCache();//screenshot for background view

        File imageFile = new File(mContext.getFilesDir(),"img.jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            bitmap.recycle();
        }
        catch (Exception e) {
            e.printStackTrace();
            bitmap.recycle();
            mainView.setDrawingCacheEnabled(false);
            return;
        }
        finally {
            mainView.setDrawingCacheEnabled(false);
        }
        //endregion
        //region Share with apps
        Uri screenshotUri = FileProvider.getUriForFile(
                mContext,
                "com.archer.survival_circle_dpad",
                imageFile);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("*/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share using.."));
        //endregion
    }

    public static void openPlayStorePage(WeakReference<MainActivity> m_context)
    {
        MainActivity mContext = m_context.get();
        try
        {
            mContext.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.archer.survival_circle"));
            mContext.startActivity(intent);
        }
        catch (Exception e)
        {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.archer.survival_circle_dpad"));
                mContext.startActivity(intent);
            }
            catch (Exception ex)
            {
//                Toast.makeText( mContext, "I am unknown error x", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static void writeToFile(WeakReference<MainActivity> m_context,String file_name,String data) {
        MainActivity mContext = m_context.get();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput(file_name, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }

        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());

        }
    }

    public static String readFromFile(WeakReference<MainActivity> m_context,String file_name) {
        MainActivity mContext = m_context.get();
        String ret = "";
        try {
            InputStream inputStream = mContext.openFileInput(file_name);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            ret = "0";
            if(file_name == TOP_SCORE_FILE)
                ret = "0_0_0";
//            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            ret = "0";
            if(file_name == TOP_SCORE_FILE)
                ret = "0_0_0";
//            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }

    public static Point getWindowSize(Display defaultDisplay)
    {
        Point windowSize = new Point();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            defaultDisplay.getSize(windowSize);
        }
        else
        {
            windowSize.x = defaultDisplay.getWidth();
            windowSize.y = defaultDisplay.getHeight();
        }
        return windowSize;
    }
    public static int ConvertToPx(Context c,int dip)
    {
        Resources r = c.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }


    public static void shuffleArray(PointF[] ar,int length)
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

}
