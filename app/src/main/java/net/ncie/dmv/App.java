package net.ncie.dmv;


import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.tencent.mmkv.MMKV;

import net.ncie.dmv.ad.InterstitialAds;
import net.ncie.dmv.ad.NativeAds;
import net.ncie.dmv.util.MessageEvent;
import net.ncie.dmv.util.MyUtil;

import org.greenrobot.eventbus.EventBus;


/** Application class that initializes, loads and show ads when activities change states. */
public class App extends Application
        implements ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private int count=0;
    public  static Boolean isForground = true;

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);
        MMKV.initialize(this);
        initDate();

        initGoogleAds();

    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    public void initDate(){


    }
    //初始化google ads
    public void initGoogleAds(){
      //  initAds();


        InterstitialAds.initInterstitialAds(this);

        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(
                            @NonNull InitializationStatus initializationStatus) {}
                });

    }
    public void initAds(){
        //加载原生横幅广告
        NativeAds.refreshMainNativeAd(this, new NativeAds.OnShowNativeAdCompleteListener() {
            @Override
            public void onShowNativeAdComplete() {
                EventBus.getDefault().post(new MessageEvent("NativeAds"));
            }
        });
    }



    /**
     * DefaultLifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);

    }

    /** ActivityLifecycleCallback methods. */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

        count++;
        if (!isForground){
            //后台回到前台
            isForground = true;


        }

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        MyUtil.MyLog("Resumed--"+activity.getClass().getName());

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        MyUtil.MyLog("Paused");

    }



    @Override
    public void onActivityStopped(@NonNull Activity activity) {

        count--;
        if (count==0){
            isForground = false;
            //切后台

        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        MyUtil.MyLog("onActivitySaveInstanceState");


    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }


    /**
     * 广告加载完成以及关闭回调
     */
    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
        void TurnoffAds();

        void onFailedToLoad();

        void onAdFailedToShow();
    }


}