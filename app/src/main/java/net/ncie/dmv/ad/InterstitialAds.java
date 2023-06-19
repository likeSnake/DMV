package net.ncie.dmv.ad;


import static net.ncie.dmv.ad.AdConst.ADMOB_AD_Interstitial_ID;
import static net.ncie.dmv.ad.AdConst.Interstitial_Ad_Clicks;
import static net.ncie.dmv.ad.AdConst.Interstitial_Ad_Impressions;
import static net.ncie.dmv.ad.AdConst.isInitInterstitialShow;
import static net.ncie.dmv.util.AdUtils.AdsClickCount;
import static net.ncie.dmv.util.AdUtils.AdsShowCount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import net.ncie.dmv.App;
import net.ncie.dmv.util.MyUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

//插页广告
public class InterstitialAds {
    private static AdRequest adRequest;
    private static InterstitialAd mInterstitialAd;

    private static App.OnShowAdCompleteListener onShowAdCompleteListeners;


    public static void startAd(Context context, App.OnShowAdCompleteListener onShowAdCompleteListener){
        onShowAdCompleteListeners = onShowAdCompleteListener;
        if (mInterstitialAd!=null){

            mInterstitialAd.show((Activity) context);
        }else {
            initInterstitialAds(context);
        }

    }


    @SuppressLint("VisibleForTests")
    public static void initInterstitialAds(Context context){
        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context,ADMOB_AD_Interstitial_ID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdClicked() {
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

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // 广告显示失败
                                MyUtil.MyLog("广告显示失败");

                                onShowAdCompleteListeners.onAdFailedToShow();
                                //isConnecting = false;
                                //重新加载广告
                             //   initInterstitialAds(context);
                            }

                            @Override
                            public void onAdImpression() {
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
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        MyUtil.MyLog("广告加载失败:"+loadAdError);
                      //  initInterstitialAds(context);
                        onShowAdCompleteListeners.onFailedToLoad();
                    }
                });
    }
}
