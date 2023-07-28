package net.ncie.dmv.act;

import static net.ncie.dmv.ad.AdConst.All_Ad_Show_Max;
import static net.ncie.dmv.ad.AdConst.All_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Native_main_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Open_Ad_Show_Max;
import static net.ncie.dmv.ad.AdConst.Open_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Native_main_Show_Max;
import static net.ncie.dmv.ad.AdConst.Native_node_Show_Max;

import static net.ncie.dmv.ad.AdConst.Native_result_Show_Max;
import static net.ncie.dmv.ad.AdConst.Interstitial_Show_Max;
import static net.ncie.dmv.ad.AdConst.All_Clicks_Max;
import static net.ncie.dmv.ad.AdConst.Open_Clicks_Max;
import static net.ncie.dmv.ad.AdConst.Native_main_Clicks_Max;
import static net.ncie.dmv.ad.AdConst.Native_node_Clicks_Max;
import static net.ncie.dmv.ad.AdConst.Native_result_Clicks_Max;
import static net.ncie.dmv.ad.AdConst.Interstitial_Clicks_Max;
import static net.ncie.dmv.ad.AdConst.Testing_Ad_Interval;
import static net.ncie.dmv.ad.FireBaseConst.Fire_All_Ad_Show_Max;
import static net.ncie.dmv.ad.FireBaseConst.Fire_All_Clicks_Max;
import static net.ncie.dmv.ad.FireBaseConst.Fire_Interstitial_Clicks_Max;
import static net.ncie.dmv.ad.FireBaseConst.Fire_Interstitial_Show_Max;
import static net.ncie.dmv.ad.FireBaseConst.Fire_Native_main_Clicks_Max;
import static net.ncie.dmv.ad.FireBaseConst.Fire_Native_main_Show_Max;
import static net.ncie.dmv.ad.FireBaseConst.Fire_Native_node_Clicks_Max;
import static net.ncie.dmv.ad.FireBaseConst.Fire_Native_node_Show_Max;
import static net.ncie.dmv.ad.FireBaseConst.Fire_Native_result_Clicks_Max;
import static net.ncie.dmv.ad.FireBaseConst.Fire_Native_result_Show_Max;
import static net.ncie.dmv.ad.FireBaseConst.Fire_Open_Ad_Show_Max;
import static net.ncie.dmv.ad.FireBaseConst.Fire_Open_Clicks_Max;
import static net.ncie.dmv.ad.FireBaseConst.Fire_Testing_Ad_Interval;
import static net.ncie.dmv.constant.MyAppApiConfig.AllState;
import static net.ncie.dmv.constant.MyAppApiConfig.My_Firebase_AdConfig;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectCar;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectState;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectStateUrl;
import static net.ncie.dmv.util.AdUtils.CheckAds;
import static net.ncie.dmv.util.MyUtil.MyLog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import net.ncie.dmv.App;
import net.ncie.dmv.R;
import net.ncie.dmv.ad.NativeAds;
import net.ncie.dmv.ad.OpenAds;
import net.ncie.dmv.bean.AdConfigBean;
import net.ncie.dmv.util.HttpUtils;
import net.ncie.dmv.util.MessageEvent;
import net.ncie.dmv.util.MyUtil;
import net.ncie.dmv.util.TimerUtil;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class StartAct extends AppCompatActivity {

    private boolean isMain = true;
    private ArrayList<String> list;
    private TimerUtil t;
    private boolean isReadyState = false;
    private boolean isReadyOpenAd = false;
    private OpenAds appOpenAdManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_start);
        initFireBase();
        initDate();
        initAds();
    }

    public void initAds(){
        CheckAds();
        loadNativeAds();
        appOpenAdManager =  new OpenAds(new App.OnShowAdCompleteListener() {
            @Override
            public void onShowAdComplete() {
                t.cancel();
            }

            @Override
            public void TurnoffAds() {
                MyLog("关闭广告");
                isReadyOpenAd = true;
                startAct();
            }

            @Override
            public void onFailedToLoad() {
                MyLog("开屏广告加载失败");
                isReadyOpenAd = true;
                startAct();
            }

            @Override
            public void onAdFailedToShow() {
                MyLog("开屏广告显示失败");
                isReadyOpenAd = true;
                startAct();
            }

            @Override
            public void onAdLoaded() {
                showOpenAd();
            }
        });

        startOpenAd();

    }
    public void showOpenAd(){
        t.cancel();
        if (Open_Ad_Switch&&All_Ad_Switch){
            appOpenAdManager.showAdIfAvailable("Start Open");
        }
    }
    public void initDate(){
        if(MMKV.defaultMMKV().decodeString(SelectState)!=null&&MMKV.defaultMMKV().decodeString(SelectCar)!=null){
            //   startMain();
            isMain = true;
        }else {
            //   startDistrictAct();
            isMain = false;
        }

        getState();

        t = new TimerUtil(10, 1, new TimerUtil.TimerListener() {
           @Override
           public void onTick(long millisUntilFinished) {

           }

           @Override
           public void onFinish() {
               allStartAct();
           }
       });
        t.start();
    }
    public void getState(){

        HttpUtils.sendGetRequest(SelectStateUrl, new HttpUtils.HttpCallback<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                // 处理返回的结果
                list = result;
                runOnUiThread(()->{
                    MMKV.defaultMMKV().encode(AllState, new Gson().toJson(result));
                    MyLog("State已获取");
                    isReadyState = true;
                    startAct();
                });
            }

            @Override
            public void onFailure(Exception e) {
                // 处理请求失败的情况
                runOnUiThread(()->{
                    Log.e("sendGetRequest","请求失败:"+e);
                    //再次请求
                    Log.i("sendGetRequest","再次请求");
                    getState();
                });
            }
        });
    }

    public void startAct() {
        if (isReadyState&&isReadyOpenAd) {
            if (isMain) {
                startActivity(new Intent(StartAct.this, MainAct.class));
            } else {
                Intent intent = new Intent(StartAct.this, DistrictAct.class);
                intent.putExtra("getState", list);
                startActivity(intent);
            }
            finish();
        }
    }

    public void allStartAct(){
        if (isMain) {
            startActivity(new Intent(StartAct.this, MainAct.class));
        } else {
            Intent intent = new Intent(StartAct.this, DistrictAct.class);
            intent.putExtra("getState", list);
            startActivity(intent);
        }
        finish();
    }

    public void startOpenAd(){
        CheckAds();
        if (Open_Ad_Switch&&All_Ad_Switch){
            //开屏广告初始化
            appOpenAdManager.setActivity(this);
            appOpenAdManager.showAdIfAvailable("Start Open");
        }else {
            //免广告
            MyLog("开屏免广告");
            t.cancel();
            isReadyOpenAd = true;
            startAct();
        }
    }

    public void loadNativeAds(){
        //加载原生横幅广告
        if (All_Ad_Switch&&Native_main_Ad_Switch) {
            NativeAds.refreshMainNativeAd(this, new NativeAds.OnShowNativeAdCompleteListener() {
                @Override
                public void onShowNativeAdComplete() {
                    MyUtil.MyLog("onShowNativeAdComplete完成");
                    EventBus.getDefault().post(new MessageEvent("NativeAds"));

                }

                @Override
                public void onFailedToLoad() {

                }

                @Override
                public void onAdClicked() {

                }
            });
        }else {
            MyUtil.MyLog("Native_main免广告");

        }
    }

    public void initFireBase(){
        FirebaseApp.initializeApp(this);
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        /*FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1800)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);*/


        remoteConfig.fetch()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            remoteConfig.activate();

                            String string12 = remoteConfig.getString(Fire_Testing_Ad_Interval);
                            if (!string12.equals("")){
                                MyLog("FireBase All_Ad_Show_Max展示次数"+string12);
                                Testing_Ad_Interval = Integer.parseInt(string12);
                            }

                            MyLog("Ad远程配置成功");
                        }else {
                            MyLog("Ad远程配置失败");
                        }

                    }});
    }

}