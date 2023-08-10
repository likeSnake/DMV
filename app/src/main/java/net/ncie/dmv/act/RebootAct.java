package net.ncie.dmv.act;

import static net.ncie.dmv.ad.AdConst.All_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Open_Ad_Switch;
import static net.ncie.dmv.ad.OpenAds.appOpenAd;
import static net.ncie.dmv.util.AdUtils.CheckAds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import net.ncie.dmv.App;
import net.ncie.dmv.R;
import net.ncie.dmv.ad.OpenAds;
import net.ncie.dmv.util.MyUtil;
import net.ncie.dmv.util.TimerUtil;

public class RebootAct extends AppCompatActivity {

    private TimerUtil t;
    private OpenAds appOpenAdManager;
    private int progressStatus = 0;
    private ProgressBar lodProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_reboot);

        initUI();
        initDate();
        initAds();
    }

    public void initUI(){
        lodProgressBar = findViewById(R.id.lodProgressBar);
    }
    public void initAds(){
        CheckAds();
        appOpenAdManager =  new OpenAds(new App.OnShowAdCompleteListener() {
            @Override
            public void onShowAdComplete() {
                //显示成功
            }

            @Override
            public void TurnoffAds() {
                MyUtil.MyLog("关闭广告");

                startAct();
            }

            @Override
            public void onFailedToLoad() {
                MyUtil.MyLog("开屏广告加载失败");

                //startAct();
            }

            @Override
            public void onAdFailedToShow() {
                MyUtil.MyLog("开屏广告显示失败");

                startAct();
            }

            @Override
            public void onAdLoaded() {
                showOpenAd();
            }
        });

    }
    public void initDate(){
        if (Open_Ad_Switch&&All_Ad_Switch){
            startTimer();

        }else {
            startNoAdTimer();
        }

    }

    public void startAct() {
        t.cancel();
        startActivity(new Intent(RebootAct.this, MainAct.class));
        finish();

    }

    public void loadOpenAd(){
        if (Open_Ad_Switch&&All_Ad_Switch){
            if (appOpenAd!=null){
                showOpenAd();
            }else {
                //开屏广告初始化
                appOpenAdManager.setActivity(this);
                appOpenAdManager.showAdIfAvailable("Loading Open");
            }

        }else {
            //免广告
            MyUtil.MyLog("开屏免广告");
            startAct();
        }
    }

    public void showOpenAd(){
        t.cancel();
        appOpenAdManager.showAdIfAvailable("Loading Open");

    }
    public void startNoAdTimer(){

        t = new TimerUtil(3, 27, new TimerUtil.TimerListener() {

            @Override
            public void onTick(long millisUntilFinished) {
                progressStatus++;
                lodProgressBar.setProgress(progressStatus);
            }

            @Override
            public void onFinish() {
                startAct();
            }
        });

        t.start();
    }


    public void startTimer(){
        //lodProgressBar.setVisibility(View.VISIBLE);
        final boolean[] loadFirst = {true};
        t = new TimerUtil(15, 100, new TimerUtil.TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                progressStatus++;
                lodProgressBar.setProgress(progressStatus);
                if (progressStatus>=30&& loadFirst[0]){
                    loadFirst[0] = false;
                    loadOpenAd();
                }

            }

            @Override
            public void onFinish() {
                MyUtil.MyLog("加载超时");
                startAct();

            }
        });
        t.start();
    }

    @Override
    public void onBackPressed() {
        // 在这里添加你的逻辑代码
        // 如果你希望返回键被忽略，可以直接return
        // 如果你希望执行默认的返回操作，可以调用super.onBackPressed()
        return;
    }
}