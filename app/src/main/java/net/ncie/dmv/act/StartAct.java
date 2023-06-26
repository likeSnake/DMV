package net.ncie.dmv.act;

import static net.ncie.dmv.ad.AdConst.All_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Open_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.isStartApp;
import static net.ncie.dmv.constant.MyAppApiConfig.AllState;
import static net.ncie.dmv.constant.MyAppApiConfig.My_Firebase_AdConfig;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectCar;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectState;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectStateUrl;
import static net.ncie.dmv.util.AdUtils.CheckAds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import net.ncie.dmv.App;
import net.ncie.dmv.R;
import net.ncie.dmv.ad.NativeAds;
import net.ncie.dmv.ad.OpenAds;
import net.ncie.dmv.util.HttpUtils;
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
        initDate();
        initAds();
    }

    public void initAds(){
        CheckAds();
        appOpenAdManager =  new OpenAds(new App.OnShowAdCompleteListener() {
            @Override
            public void onShowAdComplete() {
                t.cancel();
            }

            @Override
            public void TurnoffAds() {
                MyUtil.MyLog("关闭广告");
                isReadyOpenAd = true;
                startAct();
            }

            @Override
            public void onFailedToLoad() {
                MyUtil.MyLog("开屏广告加载失败");
                isReadyOpenAd = true;
                startAct();
            }

            @Override
            public void onAdFailedToShow() {
                MyUtil.MyLog("开屏广告显示失败");
                isReadyOpenAd = true;
                startAct();
            }
        });

        startOpenAd();

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
                    MyUtil.MyLog("State已获取");
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
        if (Open_Ad_Switch&&All_Ad_Switch){
            //开屏广告初始化并显示
            appOpenAdManager.setActivity(this);
            appOpenAdManager.showAdIfAvailable();
        }else {
            //免广告
            MyUtil.MyLog("开屏免广告");
            t.cancel();
            isReadyOpenAd = true;
            startAct();
        }
    }
}