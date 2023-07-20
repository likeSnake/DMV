package net.ncie.dmv;


import static net.ncie.dmv.ad.AdConst.isAdShowing;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;
import com.tencent.mmkv.MMKV;

import net.ncie.dmv.act.MainAct;
import net.ncie.dmv.act.RebootAct;
import net.ncie.dmv.act.StartAct;
import net.ncie.dmv.ad.AdConst;
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


       // InterstitialAds.initInterstitialAds(this);

        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(
                            @NonNull InitializationStatus initializationStatus) {}
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

            MyUtil.MyLog("onActivityStarted");
            //仅当正在显示开屏广告时，不加载插页广告
            if (!isAdShowing) {
                activity.startActivity(new Intent(activity, RebootAct.class));
                //始终销毁main以外的界面
                if ((!activity.getClass().getName().equals(MainAct.class.getName()))/*&&(!activity.getClass().getName().equals(StartAct.class.getName()))*/){
                    MyUtil.MyLog("销毁界面："+activity.getClass().getName());
                    activity.finish();
                }
            }
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
            AdConst.AppState =  false;
            if (activity.getClass().getName().equals(MainAct.class.getName())){
                EventBus.getDefault().post(new MessageEvent("isBackground"));
            }

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

        void onAdLoaded();
    }


}