package com.archer.survival_circle_dpad.util;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.archer.survival_circle_dpad.Helper;
import com.archer.survival_circle_dpad.MainActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Middle layer between main activity & util helper classes for In App Billing Logic
 */
public class InAppBilling {

    MainActivity mContext;
    public IabHelper mHelper;
    String SKU_REMOVE_ADS = "sku_remove_ads";
    String RemoveAdsPrice;

    boolean connectedToInAppBillingServices;

    public InAppBilling(WeakReference<MainActivity> m_context) {
        mContext = m_context.get();
        RemoveAdsPrice="";

        initializeInAppBilling();
        //region Check for Ad-Free version
        String AD_FREE_FILE = "jingalala_version";
        String data = "Yay! I am ad Free. ho-ho-ho.";
        if (Helper.readFromFile(new WeakReference<>(mContext), AD_FREE_FILE).equals(data))
        {
            mContext.objManageAds.adFreeVersion = true;
            mContext.objManageAds.hideAdView();
        }
        //endregion
    }

    public void initializeInAppBilling() {
        String base64EncodedPublicKey ;
        // compute your public key and store it in base64EncodedPublicKey
        base64EncodedPublicKey = getPublicKey();

        mHelper = new IabHelper(mContext, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                } else {
                    connectedToInAppBillingServices = true;
                    //check for pending consumptions
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                    getPriceInLocalCurrency();
                }
            }
        });
    }

    //region Check For pending items to consume
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener()
    {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            try
            {
                if (result.isFailure()) {
                    // handle error here
                }
                else {
                    Purchase purchase;
                    //consume pending purchases
                    if(!mContext.objManageAds.adFreeVersion)
                    {
                        purchase = inventory.getPurchase(SKU_REMOVE_ADS);

                        if (purchase != null && verifyDeveloperPayload(purchase))
                        {
                            mContext.objManageAds.adFreeVersion = true;
                            mContext.objManageAds.hideAdView();
                            String AD_FREE_FILE = "jingalala_version";
                            String data = "Yay! I am ad Free. ho-ho-ho.";
                            Helper.writeToFile(new WeakReference<>(mContext), AD_FREE_FILE, data);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                 Toast.makeText(mContext,
                         "Error : Could not consume purchased item." + "\n" +
                         "\n" + "Please : " +
                         "\n" + "1. Restart 2 cards" +
                         "\n" + "2. Check internet connection" +
                         "\n" + "3. Contact us if issue persists",
                         Toast.LENGTH_SHORT).show();
            }
        }
    };
    //endregion


    //region get price in local currency
    public void getPriceInLocalCurrency() {
        if (!connectedToInAppBillingServices)
            return;

        IabHelper.QueryInventoryFinishedListener
                mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                if (result.isFailure()) {
                    // handle error
                    return;
                }

                try {
                    RemoveAdsPrice = inventory.getSkuDetails(SKU_REMOVE_ADS).getPrice();
                } catch (Exception ex) {
                    Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        List<String> additionalSkuList = new ArrayList<>();
        additionalSkuList.add(SKU_REMOVE_ADS);
        mHelper.queryInventoryAsync(true, additionalSkuList,
                mQueryFinishedListener);
    }
    //endregion


    public void LaunchPurchaseFlow(String SKU_ID)
    {
        if(!connectedToInAppBillingServices)
        {
            Toast.makeText(mContext,"Oops. Connection failed.\nPlease try after sometime.",Toast.LENGTH_SHORT).show();
            return;
        }

        IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
                = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result, Purchase purchase)
            {
                try {

                    mHelper.flagEndAsync();
                if (result.isFailure()) {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                    Toast.makeText(mContext,"Purchased Failed\nPlease try after sometime",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (purchase.getSku().equals(SKU_REMOVE_ADS))
                {
                    mContext.objManageAds.adFreeVersion = true;
                    String AD_FREE_FILE = "jingalala_version";
                    String data = "Yay! I am ad Free. ho-ho-ho.";
                    Helper.writeToFile(new WeakReference<>(mContext),AD_FREE_FILE,data);
                    Toast.makeText(mContext,"Application upgraded successfully." +
                    "\nPlease give us a moment to refresh things!",Toast.LENGTH_SHORT).show();
                }
                }
                catch (Exception ex)
                {
                    Toast.makeText(mContext,ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        };

        String code = "survival_circle";
        int transactionID = code.hashCode();
        mHelper.launchPurchaseFlow(mContext, SKU_ID, transactionID,
                mPurchaseFinishedListener, jumble("69"));
    }

    public void setRemoveAdsPrice(View v)
    {
        try {
            if(!RemoveAdsPrice.equals(""))
                ((TextView)v).setText("At " + RemoveAdsPrice);
        }
        catch (Exception ex){ /* Do Nothing */ }
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {

        String payload = p.getDeveloperPayload();
        return payload.trim().equals(jumble("619"));
    }

    public String jumble(String x)
    {
        String a,b,c,d;
        a="SwaS"; b="RiShi_NeVeR"; c="GiVe"; d="Up";
        x= a+"."+b+"."+c+"."+d;
        char y[] = x.toCharArray();
        int []xx = {3,9,5,7,2,11,3,9,5,7,2,11,};
        int l1 = xx.length;
        int l2 = y.length;
        for (int xxx : xx) {
            int index = xxx;
            for (int j = 0; j < l2; j++) {
                char yy = y[j];
                y[j] = y[index % l2];
                y[index % l2] = yy;
                index += xxx;
            }
        }
        x=String.valueOf(y);
        return x.trim();
    }

    public String getPublicKey()
    {
        String key[]=
                {
                        "CBIIMA8QACOAAFEQAB0w9GikhqkgBNAjIBIIM"+
                                "gKCAQEAtVaUxDKdy7leq25ijwuSyZT"+
                                "ipmYQROkr3VjGSjAkWyZBznle62oXOKqb"+
                                "5DkaCjP/rVUyPLWFZB3VcAFY4zzVHs8tab69"+
                                "IlexxQhFxZFsjY/kSCfh+CEz4/xOo0+RLpx"+
                                "GYq/9x9DwTugA3ETi+4M/4G0e0K9cAwbo5"+
                                "oYYovw0rGkUHLpmy0E0oQ0+29RwH7pS"+
                                "2PISAGJtYSesHnnrg9MV8VY3o2"+
                                "mszZGRd1fRhFt/J"+
                                "sw/qaozz7L/KBLxyRbUM/E90ZfOTc2QT96z"+
                                "buyiuKV2t8G9SuBnNGcpGSKiVy/dCIFp5P"+
                                "+9bySmHCTC3F0wtt23BIGrEnxNW7mUEy"+
                                "BAQADIQSw5NpUt"
                };

        String final_result="";
        int length= key.length;
        for(int i=0;i<length;i++)
        {
            if(i%2==0)
                final_result+= new StringBuilder(key[i]).reverse().toString();
            else
                final_result+= key[i];
        }

        return final_result.trim();
    }

}
