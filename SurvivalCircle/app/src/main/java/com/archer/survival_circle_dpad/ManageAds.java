package com.archer.survival_circle_dpad;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.lang.ref.WeakReference;

/**
 * Created by Swastik on 18-01-2016.
 */
public class ManageAds {
    MainActivity mContext;

    public com.google.android.gms.ads.AdRequest AdRequest;
    public InterstitialAd mInterstitialAd;
    public boolean adFreeVersion;

    public ManageAds(WeakReference<MainActivity>m_context)
    {
        mContext = m_context.get();
        initializeAds();
    }

    //region Ads Logic
    private void initializeAds()
    {
//        AdBuddiz.setPublisherKey(getString(R.string.adbuddiz_ad_id)); // See 3.
//        AdBuddiz.cacheAds(this);
//        AdBuddiz.RewardedVideo.fetch(this);

        //region AdMob
        //Initialize AdRequest for Banner Ads
        AdRequest = new AdRequest.Builder().build();

        //Initialize Interstitial Ads
        mInterstitialAd = new InterstitialAd(mContext.getApplication());
        mInterstitialAd.setAdUnitId(mContext.getString(R.string.interstitial_ad_unit_id));
        requestNewInterstitial();
        //My RedMi = 865622026474424
    }

    public void requestNewInterstitial() {
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded())
        {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    public void DisplayInterstitialAd()
    {
        if (mInterstitialAd!=null && mInterstitialAd.isLoaded())
        {
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                    mContext.game_duration = 0;
                }
            });
            mInterstitialAd.show();
        }
        else
        {
            requestNewInterstitial();
        }
    }

    public void DisplayBannerAd(int ad_view_id)
    {
        if(!adFreeVersion)
        {
            final AdView mAdView = (AdView) mContext.findViewById(ad_view_id);
            if(mAdView==null)
                return;

            mAdView.loadAd(AdRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void ShowRemoveAdDialog() {

        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_remove_ads, null, true);

        final Dialog dialog = new AlertDialog.Builder(mContext).show();
        dialog.setCancelable(false);
        dialog.setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        View v2 = mContext.findViewById(R.id.Root);
        lp.width = v2.getMeasuredWidth() - Helper.ConvertToPx(mContext, 40); //WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mContext.objInAppBilling.setRemoveAdsPrice(view.findViewById(R.id.tvRemoveAdsPrice));

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK))
                {
                    DisplayInterstitialAd();
                    dialog.dismiss();
                }
                return false;
            }
        });
        //region Click Listener for remove ads dialog
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId())
                {
                    case R.id.btnShowAd:
                        DisplayInterstitialAd();
                        break;
                    case R.id.btnRemoveAds:
                        String SKU_REMOVE_ADS = "sku_remove_ads";
                        mContext.objInAppBilling.LaunchPurchaseFlow(SKU_REMOVE_ADS);
                        DisplayInterstitialAd();
                        break;
                }
                dialog.dismiss();
            }
        };
        //endregion

        view.findViewById(R.id.btnShowAd).setOnClickListener(listener);
        view.findViewById(R.id.btnRemoveAds).setOnClickListener(listener);
        dialog.show();
    }

    public void hideAdView()
    {
        int ids[] = { R.id.adView_home,R.id.adView_settings};
        for (int id : ids)
        {
            AdView mAdView = (AdView) mContext.findViewById(id);
            if(mAdView!=null)
                mAdView.setVisibility(View.GONE);
        }
    }


    //endregion

}
