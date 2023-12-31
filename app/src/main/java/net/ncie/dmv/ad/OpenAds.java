package net.ncie.dmv.ad;

import static net.ncie.dmv.ad.AdConst.ADMOB_AD_Open_ID;
import static net.ncie.dmv.ad.AdConst.Open_Ad_Clicks;
import static net.ncie.dmv.ad.AdConst.Open_Ad_Impressions;
import static net.ncie.dmv.ad.AdConst.isAdShowing;
import static net.ncie.dmv.ad.AdConst.isOpenAdLoad;
import static net.ncie.dmv.ad.AdCount.loadMainNativeAd;
import static net.ncie.dmv.util.AdUtils.AdsClickCount;
import static net.ncie.dmv.util.AdUtils.AdsShowCount;
import static net.ncie.dmv.util.AdUtils.CheckAds;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import net.ncie.dmv.App;
import net.ncie.dmv.bean.InfoBean;
import net.ncie.dmv.util.MyUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.tencent.mmkv.MMKV;

import java.util.Date;


/** 开屏广告. */
public class OpenAds {

    private static final String LOG_TAG = "AppOpenAdManager";
    private static  long startTime;
    private String Position = "";
    private App.OnShowAdCompleteListener onShowAdCompleteListener;
    public static AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    public boolean isShowingAd = false;
    private Activity activity;

    /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
    private long loadTime = 0;

    /** Constructor. */
    public OpenAds(App.OnShowAdCompleteListener onShowAdCompleteListener) {
        this.onShowAdCompleteListener = onShowAdCompleteListener;
    }

    /**
     * Load an ad.
     *
     * @param context the context of the activity that loads the ad
     */
    private void loadAd(Context context) {
        // 如果广告正在加载或者广告不需要加载
        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        startTime = System.currentTimeMillis() / 1000;
        String aid = MMKV.defaultMMKV().decodeString("aid");
        InfoBean infoBean = new InfoBean(aid,Position,"RequestAds",ADMOB_AD_Open_ID,"","","","","","","","","");
        loadMainNativeAd(activity,infoBean);


        isLoadingAd = true;
        AdRequest request = new AdRequest.Builder().build();

        AppOpenAd.AppOpenAdLoadCallback appOpenAdLoadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            /**
             * Called when an app open ad has loaded.
             *
             * @param ad the loaded app open ad.
             */
            @Override
            public void onAdLoaded(AppOpenAd ad) {
                long l = (System.currentTimeMillis() / 1000) - startTime;
                String aid = MMKV.defaultMMKV().decodeString("aid");
                InfoBean infoBean = new InfoBean(aid,Position,"AdsLoad",ADMOB_AD_Open_ID,l +" sec - AdLoad","","",ad.getResponseInfo().getResponseId(),ad.getResponseInfo().toString(),"","","","");

                loadMainNativeAd(activity,infoBean);


                //广告加载成功
                isOpenAdLoad = true;
                appOpenAd = ad;
                isLoadingAd = false;
                loadTime = (new Date()).getTime();

                if (activity.isDestroyed() || activity.isFinishing() || activity.isChangingConfigurations()) {
                    return;
                }else {
                    //加载成功
                    //showAdIfAvailable();
                    onShowAdCompleteListener.onAdLoaded();
                }
            }

            /**
             * Called when an app open ad has failed to load.
             *
             * @param loadAdError the error.
             */
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                long l = (System.currentTimeMillis() / 1000) - startTime;
                String aid = MMKV.defaultMMKV().decodeString("aid");
                InfoBean infoBean = new InfoBean(aid,Position,"RequestAdsFailure",ADMOB_AD_Open_ID,l +" sec - "+loadAdError.toString(), "","","","","","","","");

                loadMainNativeAd(activity,infoBean);
                //广告加载失败
                MyUtil.MyLog("开屏广告加载失败："+loadAdError.toString());
                isLoadingAd = false;
                isShowingAd = false;
                isOpenAdLoad = false;
                onShowAdCompleteListener.onFailedToLoad();


            }
        };

        AppOpenAd.load(
                context,
                ADMOB_AD_Open_ID,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                appOpenAdLoadCallback
                );
    }

    /** Check if ad was loaded more than n hours ago. */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /** Check if ad exists and can be shown. */
    private boolean isAdAvailable() {

        MyUtil.MyLog(appOpenAd != null);
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /**
     * Show the ad if one isn't already showing.
     *
     */
    public void showAdIfAvailable(String position) {
        Position = position;
        showAdIfAvailable(activity);
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity the activity that shows the app open ad
     */
    public void showAdIfAvailable(@NonNull final Activity activity) {
        // If the app open ad is already showing, do not show the ad again.
        if (isShowingAd) {
            Log.d(LOG_TAG, "The app open ad is already showing.");
            return;
        }

        // If the app open ad is not available yet, invoke the callback then load the ad.
        if (!isAdAvailable()) {
            Log.d(LOG_TAG, "The app open ad is not ready yet.");
            loadAd(activity);
            return;
        }

        Log.d(LOG_TAG, "显示广告");

        appOpenAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    /** 广告显示监听 */
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        //广告点击关闭
                        appOpenAd = null;
                        isShowingAd = false;
                        isAdShowing = false;
                        onShowAdCompleteListener.TurnoffAds();
                        isOpenAdLoad = false;

                    }

                    /** Called when fullscreen content failed to show. */
                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        //广告显示失败
                        MyUtil.MyLog("开屏广告显示失败："+adError.toString());
                        appOpenAd = null;
                        isShowingAd = false;
                        isAdShowing = false;
                        //loadAd(activity);
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();

                        String aid = MMKV.defaultMMKV().decodeString("aid");
                        InfoBean infoBean = new InfoBean(aid,Position,"AdsClicked",ADMOB_AD_Open_ID,"","","","","","","",activity.getPackageName()
                                ,"");
                        loadMainNativeAd(activity,infoBean);


                        //广告点击次数统计
                        AdsClickCount(Open_Ad_Clicks);
                        //广告点击次数统计
                      //  AdsClickCount(Open_Ad_Clicks);
                        CheckAds();
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        String aid = MMKV.defaultMMKV().decodeString("aid");
                        InfoBean infoBean = new InfoBean(aid,Position,"AdsShow",ADMOB_AD_Open_ID,"","","","","","","",activity.getPackageName()
                                ,"");
                        loadMainNativeAd(activity,infoBean);

                        //广告展示次数统计
                        AdsShowCount(Open_Ad_Impressions);

                        CheckAds();
                    }

                    /** Called when fullscreen content is shown. */
                    @Override
                    public void onAdShowedFullScreenContent() {
                        isShowingAd = true;
                        onShowAdCompleteListener.onShowAdComplete();
                        isAdShowing = true;

                    }
                });

        appOpenAd.setOnPaidEventListener(new OnPaidEventListener() {
            @Override
            public void onPaidEvent(@NonNull AdValue adValue) {
                String ips = "";
                String aid = MMKV.defaultMMKV().decodeString("aid");
                InfoBean infoBean = new InfoBean(aid,Position,"AdsShowValue",ADMOB_AD_Open_ID,"",adValue.getCurrencyCode(), String.valueOf(adValue.getValueMicros()),"","","","",activity.getPackageName()
                        ,"");
                loadMainNativeAd(activity,infoBean);

            }
        });

        appOpenAd.show(activity);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}