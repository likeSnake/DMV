package net.ncie.dmv.ad;


import static net.ncie.dmv.ad.AdConst.ADMOB_AD_Interstitial_ID;
import static net.ncie.dmv.ad.AdConst.Interstitial_Ad_Clicks;
import static net.ncie.dmv.ad.AdConst.Interstitial_Ad_Impressions;
import static net.ncie.dmv.ad.AdConst.isInitInterstitialShow;
import static net.ncie.dmv.ad.AdCount.loadMainNativeAd;
import static net.ncie.dmv.util.AdUtils.AdsClickCount;
import static net.ncie.dmv.util.AdUtils.AdsShowCount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

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
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.tencent.mmkv.MMKV;

//插页广告
public class InterstitialAds {
    private static AdRequest adRequest;
    private static InterstitialAd mInterstitialAd;
    private static Boolean isInterstitialAdsLoad = false;
    private static App.OnShowAdCompleteListener onShowAdCompleteListeners;
    private static  long startTime;
    private static String Position = "";
    public static void startAd(Context context,String position, App.OnShowAdCompleteListener onShowAdCompleteListener){
        onShowAdCompleteListeners = onShowAdCompleteListener;
        Position = position;
        if (mInterstitialAd!=null){

            mInterstitialAd.show((Activity) context);
        }else {
            initInterstitialAds(context);
        }

    }


    @SuppressLint("VisibleForTests")
    public static void initInterstitialAds(Context context){
        if (isInterstitialAdsLoad){
            return;
        }
        isInterstitialAdsLoad = true;
        startTime = System.currentTimeMillis() / 1000;
        String aid = MMKV.defaultMMKV().decodeString("aid");
        InfoBean infoBean = new InfoBean(aid,Position,"RequestAds",ADMOB_AD_Interstitial_ID,"","","","","","","","","");
        loadMainNativeAd(context,infoBean);

        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context,ADMOB_AD_Interstitial_ID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        //广告已加载完毕
                        MyUtil.MyLog("InterstitialAd广告加载完毕");
                        long l = (System.currentTimeMillis() / 1000) - startTime;
                        String aid = MMKV.defaultMMKV().decodeString("aid");
                        InfoBean infoBean = new InfoBean(aid,Position,"AdsLoad",ADMOB_AD_Interstitial_ID,l +" sec - AdLoad","","",interstitialAd.getResponseInfo().getResponseId(),interstitialAd.getResponseInfo().toString(),"","","","");
                        loadMainNativeAd(context,infoBean);

                        mInterstitialAd = interstitialAd;

                        mInterstitialAd.setOnPaidEventListener(new OnPaidEventListener() {
                            @Override
                            public void onPaidEvent(@NonNull AdValue adValue) {
                                isInterstitialAdsLoad = false;
                                String aid = MMKV.defaultMMKV().decodeString("aid");
                                InfoBean infoBean = new InfoBean(aid,Position,"AdsShowValue",ADMOB_AD_Interstitial_ID,"",adValue.getCurrencyCode(), String.valueOf(adValue.getValueMicros()),"","","","","","");
                                loadMainNativeAd(context,infoBean);
                            }
                        });
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdClicked() {
                                isInterstitialAdsLoad = false;
                                String aid = MMKV.defaultMMKV().decodeString("aid");
                                InfoBean infoBean = new InfoBean(aid,Position,"AdsClicked",ADMOB_AD_Interstitial_ID,"","","","","","","","","");
                                loadMainNativeAd(context,infoBean);

                                // 点击广告
                                AdsClickCount(Interstitial_Ad_Clicks);
                                //预加载下次的广告
                             //   initInterstitialAds(context);

                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // 取消广告
                                MyUtil.MyLog("取消广告");

                            //    isAdShowing = false;
                                isInitInterstitialShow = false;
                                //预加载广告
                           //     initInterstitialAds(context);
                                onShowAdCompleteListeners.TurnoffAds();
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // 广告显示失败
                                MyUtil.MyLog("广告显示失败："+adError);

                                onShowAdCompleteListeners.onAdFailedToShow();
                                //isConnecting = false;
                                //重新加载广告
                             //   initInterstitialAds(context);
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                isInterstitialAdsLoad = false;
                                String aid = MMKV.defaultMMKV().decodeString("aid");
                                InfoBean infoBean = new InfoBean(aid,Position,"AdsShow",ADMOB_AD_Interstitial_ID,"","","","","","","","","");
                                loadMainNativeAd(context,infoBean);

                                //展示广告统计
                                AdsShowCount(Interstitial_Ad_Impressions);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                MyUtil.MyLog("广告显示成功");
                              //  initInterstitialAds(context);
                                onShowAdCompleteListeners.onShowAdComplete();
                                isInitInterstitialShow = true;
                            }
                        });

                        mInterstitialAd.show((Activity) context);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        isInterstitialAdsLoad = false;
                        long l = (System.currentTimeMillis() / 1000) - startTime;
                        String aid = MMKV.defaultMMKV().decodeString("aid");
                        InfoBean infoBean = new InfoBean(aid,Position,"RequestAdsFailure",ADMOB_AD_Interstitial_ID,l +" sec - "+loadAdError.toString(),"","","","","","","","");
                        loadMainNativeAd(context,infoBean);

                        MyUtil.MyLog("广告加载失败:"+loadAdError);
                      //  initInterstitialAds(context);
                        onShowAdCompleteListeners.onFailedToLoad();
                        mInterstitialAd = null;
                    }
                });
    }
}
