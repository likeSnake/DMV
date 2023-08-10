package net.ncie.dmv.util;

import static net.ncie.dmv.ad.AdConst.All_Ad_Clicks;
import static net.ncie.dmv.ad.AdConst.All_Ad_FreeTime;
import static net.ncie.dmv.ad.AdConst.All_Ad_Impressions;
import static net.ncie.dmv.ad.AdConst.All_Ad_Show_Max;
import static net.ncie.dmv.ad.AdConst.All_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.All_Clicks_Max;
import static net.ncie.dmv.ad.AdConst.Interstitial_Ad_Clicks;
import static net.ncie.dmv.ad.AdConst.Interstitial_Ad_FreeTime;
import static net.ncie.dmv.ad.AdConst.Interstitial_Ad_Impressions;
import static net.ncie.dmv.ad.AdConst.Interstitial_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Interstitial_Clicks_Max;
import static net.ncie.dmv.ad.AdConst.Interstitial_Show_Max;
import static net.ncie.dmv.ad.AdConst.Native_Ad_Node_FreeTime;
import static net.ncie.dmv.ad.AdConst.Native_Ad_Result_FreeTime;
import static net.ncie.dmv.ad.AdConst.Native_Main_Ad_Clicks;
import static net.ncie.dmv.ad.AdConst.Native_Ad_Main_FreeTime;
import static net.ncie.dmv.ad.AdConst.Native_Main_Ad_Impressions;
import static net.ncie.dmv.ad.AdConst.Native_Testing_Ad_Clicks;
import static net.ncie.dmv.ad.AdConst.Native_Node_Ad_Impressions;
import static net.ncie.dmv.ad.AdConst.Native_Result_Ad_Clicks;
import static net.ncie.dmv.ad.AdConst.Native_Result_Ad_Impressions;
import static net.ncie.dmv.ad.AdConst.Native_main_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Native_main_Clicks_Max;
import static net.ncie.dmv.ad.AdConst.Native_main_Show_Max;
import static net.ncie.dmv.ad.AdConst.Native_Testing_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Native_Testing_Clicks_Max;
import static net.ncie.dmv.ad.AdConst.Native_Testing_Show_Max;
import static net.ncie.dmv.ad.AdConst.Native_result_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Native_result_Clicks_Max;
import static net.ncie.dmv.ad.AdConst.Native_result_Show_Max;
import static net.ncie.dmv.ad.AdConst.Open_Ad_Clicks;
import static net.ncie.dmv.ad.AdConst.Open_Ad_FreeTime;
import static net.ncie.dmv.ad.AdConst.Open_Ad_Impressions;
import static net.ncie.dmv.ad.AdConst.Open_Ad_Show_Max;
import static net.ncie.dmv.ad.AdConst.Open_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Open_Clicks_Max;
import static net.ncie.dmv.util.MyUtil.MyLog;

import android.content.Context;
import android.widget.FrameLayout;

import com.tencent.mmkv.MMKV;

import net.ncie.dmv.App;
import net.ncie.dmv.ad.InterstitialAds;
import net.ncie.dmv.ad.NativeAds;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class AdUtils {


    //广告展示次数统计
    public static void AdsShowCount(String AdType) {

        int AdShows;
        int AllAdShows;

        if (MMKV.defaultMMKV().decodeString(All_Ad_Impressions) != null) {
            AllAdShows = Integer.parseInt(MMKV.defaultMMKV().decodeString(All_Ad_Impressions));
            MyLog("所有广告展示次数：" + (AllAdShows + 1));
            if (MMKV.defaultMMKV().decodeString(AdType) != null) {
                AdShows = Integer.parseInt(MMKV.defaultMMKV().decodeString(AdType));
                MyLog(AdType + "广告展示次数：" + (AdShows + 1));
            } else {
                AdShows = 0;
            }
        } else {
            AllAdShows = 0;
            AdShows = 0;
        }

        MMKV.defaultMMKV().encode(AdType, String.valueOf(AdShows + 1));
        MMKV.defaultMMKV().encode(All_Ad_Impressions, String.valueOf(AllAdShows + 1));

    }

    //广告点击次数统计
    public static void AdsClickCount(String AdType) {

        int AdClicks;
        int AllAdClicks;

        if (MMKV.defaultMMKV().decodeString(All_Ad_Clicks) != null) {
            AllAdClicks = Integer.parseInt(MMKV.defaultMMKV().decodeString(All_Ad_Clicks));
            MyLog("所有广告点击次数：" + (AllAdClicks + 1));
            if (MMKV.defaultMMKV().decodeString(AdType) != null) {
                AdClicks = Integer.parseInt(MMKV.defaultMMKV().decodeString(AdType));
                MyLog(AdType + "广告点击次数：" + (AdClicks + 1));
            } else {
                AdClicks = 0;
            }
        } else {
            AllAdClicks = 0;
            AdClicks = 0;
        }
        MMKV.defaultMMKV().encode(AdType, String.valueOf(AdClicks + 1));
        MMKV.defaultMMKV().encode(All_Ad_Clicks, String.valueOf(AllAdClicks + 1));

    }

    public static void setAdFreeDay(String type) {
        int nowDay;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate localDate = LocalDate.now();
            nowDay = localDate.getDayOfYear();
        } else {
            long l = System.currentTimeMillis();
            SimpleDateFormat format = new SimpleDateFormat("dd");
            String s = format.format(l);
            nowDay = Integer.parseInt(s);

        }

        MMKV.defaultMMKV().encode(type, String.valueOf(nowDay));
    }
    public static void startInterstitialAds(Context context) {

        if (Interstitial_Ad_Switch&&All_Ad_Switch) {
            InterstitialAds.startAd(context,"null", new App.OnShowAdCompleteListener() {
                @Override
                public void onShowAdComplete() {
                    //广告显示
                    //  isShow = true;
                    MyLog("广告显示成功");
                    CheckAds();
                }

                @Override
                public void TurnoffAds() {
                    //关闭广告后
                }

                @Override
                public void onFailedToLoad() {

                }

                @Override
                public void onAdFailedToShow() {

                }

                @Override
                public void onAdLoaded() {

                }
            });
        }else {
            MyLog("插屏免广告");
        }
    }
    public static void CheckAds() {


        if (MMKV.defaultMMKV().decodeString(All_Ad_Impressions) != null) {
            int AllAdShows = Integer.parseInt(MMKV.defaultMMKV().decodeString(All_Ad_Impressions));
            //判断全局展示广告
            if (AllAdShows >= All_Ad_Show_Max) {
                //全局免广告
                MyLog("展示达全局免广告:" + AllAdShows);
                All_Ad_Switch = false;
                setAdFreeDay(All_Ad_FreeTime);
                MMKV.defaultMMKV().encode(All_Ad_Impressions, "0");

            } else {
                //全局点击广告
                if (MMKV.defaultMMKV().decodeString(All_Ad_Clicks) != null) {
                    int AllAdClicks = Integer.parseInt(MMKV.defaultMMKV().decodeString(All_Ad_Clicks));
                    if (AllAdClicks >= All_Clicks_Max) {
                        //全局免广告
                        MyLog("点击达全局免广告:" + AllAdClicks);
                        All_Ad_Switch = false;
                        setAdFreeDay(All_Ad_FreeTime);
                        MMKV.defaultMMKV().encode(All_Ad_Clicks, "0");

                    }
                }
            }


        } else {
            //第一次进入app

            MMKV.defaultMMKV().encode(Open_Ad_Clicks, "0");
            MMKV.defaultMMKV().encode(Native_Main_Ad_Clicks, "0");
            MMKV.defaultMMKV().encode(Native_Testing_Ad_Clicks, "0");
            MMKV.defaultMMKV().encode(Native_Result_Ad_Clicks, "0");
            MMKV.defaultMMKV().encode(Interstitial_Ad_Clicks, "0");
        }


        int nowDay;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate localDate = LocalDate.now();
            nowDay = localDate.getDayOfYear();
        } else {
            long l = System.currentTimeMillis();
            SimpleDateFormat format = new SimpleDateFormat("dd");
            String s = format.format(l);
            nowDay = Integer.parseInt(s);
        }

        if (MMKV.defaultMMKV().decodeString(All_Ad_FreeTime) != null) {

            int freeDay = Integer.parseInt(MMKV.defaultMMKV().decodeString(All_Ad_FreeTime));

            if (freeDay >= nowDay) {

                MyLog("全局免广告");
                All_Ad_Switch = false;
            } else {
                checkAllAd(nowDay);

            }
        } else {
            checkAllAd(nowDay);
        }

    }

    public static void checkAllAd(int nowDay) {
        All_Ad_Switch = true;

        if (MMKV.defaultMMKV().decodeString(Open_Ad_FreeTime) != null) {
            int openFreeDay = Integer.parseInt(MMKV.defaultMMKV().decodeString(Open_Ad_FreeTime));
            if (openFreeDay < nowDay) {
                Open_Ad_Switch = true;

                checkOpenAd();
            } else {
                MyLog("开屏免广告："+openFreeDay+"-"+nowDay);
                Open_Ad_Switch = false;
            }
        } else {
            checkOpenAd();
        }

        if (MMKV.defaultMMKV().decodeString(Native_Ad_Main_FreeTime) != null) {
            int nativeFreeDay = Integer.parseInt(MMKV.defaultMMKV().decodeString(Native_Ad_Main_FreeTime));
            if (nativeFreeDay < nowDay) {
                Native_main_Ad_Switch = true;

                checkMainNativeAd();

            } else {
                MyLog(nativeFreeDay+"--"+nowDay);
                Native_main_Ad_Switch = false;
            }
        } else {
            checkMainNativeAd();
        }

        if (MMKV.defaultMMKV().decodeString(Native_Ad_Node_FreeTime) != null) {
            int nativeFreeDay = Integer.parseInt(MMKV.defaultMMKV().decodeString(Native_Ad_Node_FreeTime));
            if (nativeFreeDay < nowDay) {
                Native_Testing_Ad_Switch = true;

                checkNodeNativeAd();

            } else {
                MyLog("Native_node免广告时间");
                Native_Testing_Ad_Switch = false;
            }
        } else {
            checkNodeNativeAd();
        }

        if (MMKV.defaultMMKV().decodeString(Native_Ad_Result_FreeTime) != null) {
            int nativeFreeDay = Integer.parseInt(MMKV.defaultMMKV().decodeString(Native_Ad_Result_FreeTime));
            if (nativeFreeDay < nowDay) {
                Native_result_Ad_Switch = true;

                checkResultNativeAd();

            } else {
                MyLog("Native_node免广告时间");
                Native_result_Ad_Switch = false;
            }
        } else {
            checkResultNativeAd();
        }



        if (MMKV.defaultMMKV().decodeString(Interstitial_Ad_FreeTime) != null) {
            int interstitialFreeDay = Integer.parseInt(MMKV.defaultMMKV().decodeString(Interstitial_Ad_FreeTime));
            if (interstitialFreeDay < nowDay) {
                Interstitial_Ad_Switch = true;


                checkInterstitialAd();

            } else {
                Interstitial_Ad_Switch = false;
            }
        } else {
            checkInterstitialAd();
        }

    }


    public static void checkInterstitialAd(){
        //插屏广告
        if (MMKV.defaultMMKV().decodeString(Interstitial_Ad_Impressions) != null) {
            int i = Integer.parseInt(MMKV.defaultMMKV().decodeString(Interstitial_Ad_Impressions));
            if (MMKV.defaultMMKV().decodeString(Interstitial_Ad_Clicks) != null) {
                int j = Integer.parseInt(MMKV.defaultMMKV().decodeString(Interstitial_Ad_Clicks));
                if (i >= Interstitial_Show_Max || j >= Interstitial_Clicks_Max) {
                    //达到当天展示或点击次数，当前广告免广告一天
                    Interstitial_Ad_Switch = false;

                    setAdFreeDay(Interstitial_Ad_FreeTime);
                    MMKV.defaultMMKV().encode(Interstitial_Ad_Impressions, "0");
                    MMKV.defaultMMKV().encode(Interstitial_Ad_Clicks, "0");
                }
            }
        }
    }

    public static void checkMainNativeAd(){
        //自定义的原生广告
        if (MMKV.defaultMMKV().decodeString(Native_Main_Ad_Impressions) != null) {
            int i = Integer.parseInt(MMKV.defaultMMKV().decodeString(Native_Main_Ad_Impressions));
            if (MMKV.defaultMMKV().decodeString(Native_Main_Ad_Clicks) != null) {
                int j = Integer.parseInt(MMKV.defaultMMKV().decodeString(Native_Main_Ad_Clicks));
                if (i >= Native_main_Show_Max || j >= Native_main_Clicks_Max) {
                    //达到展示或点击次数，当前广告免广告一天
                    Native_main_Ad_Switch = false;

                    setAdFreeDay(Native_Ad_Main_FreeTime);
                    MMKV.defaultMMKV().encode(Native_Main_Ad_Impressions, "0");
                    MMKV.defaultMMKV().encode(Native_Main_Ad_Clicks, "0");
                }
            }
        }
    }

    public static void checkNodeNativeAd(){
        //自定义的原生广告
        if (MMKV.defaultMMKV().decodeString(Native_Node_Ad_Impressions) != null) {
            int i = Integer.parseInt(MMKV.defaultMMKV().decodeString(Native_Node_Ad_Impressions));
            if (MMKV.defaultMMKV().decodeString(Native_Testing_Ad_Clicks) != null) {
                int j = Integer.parseInt(MMKV.defaultMMKV().decodeString(Native_Testing_Ad_Clicks));
                if (i >= Native_Testing_Show_Max || j >= Native_Testing_Clicks_Max) {
                    //达到展示或点击次数，当前广告免广告一天
                    Native_Testing_Ad_Switch = false;

                    setAdFreeDay(Native_Ad_Node_FreeTime);
                    MMKV.defaultMMKV().encode(Native_Node_Ad_Impressions, "0");
                    MMKV.defaultMMKV().encode(Native_Testing_Ad_Clicks, "0");
                }
            }
        }
    }

    public static void checkResultNativeAd(){
        //自定义的原生广告
        if (MMKV.defaultMMKV().decodeString(Native_Result_Ad_Impressions) != null) {
            int i = Integer.parseInt(MMKV.defaultMMKV().decodeString(Native_Result_Ad_Impressions));
            if (MMKV.defaultMMKV().decodeString(Native_Result_Ad_Clicks) != null) {
                int j = Integer.parseInt(MMKV.defaultMMKV().decodeString(Native_Result_Ad_Clicks));
                if (i >= Native_result_Show_Max || j >= Native_result_Clicks_Max) {
                    //达到展示或点击次数，当前广告免广告一天
                    Native_result_Ad_Switch = false;

                    setAdFreeDay(Native_Ad_Result_FreeTime);
                    MMKV.defaultMMKV().encode(Native_Result_Ad_Impressions, "0");
                    MMKV.defaultMMKV().encode(Native_Result_Ad_Clicks, "0");
                }
            }
        }
    }

    public static void checkOpenAd(){
        //开屏广告
        if (MMKV.defaultMMKV().decodeString(Open_Ad_Impressions) != null) {
            int i = Integer.parseInt(MMKV.defaultMMKV().decodeString(Open_Ad_Impressions));
            if (MMKV.defaultMMKV().decodeString(Open_Ad_Clicks) != null) {
                int j = Integer.parseInt(MMKV.defaultMMKV().decodeString(Open_Ad_Clicks));
                MyLog("开屏展示点击："+i+"-"+j);
                if (i >= Open_Ad_Show_Max || j >= Open_Clicks_Max) {

                    //达到当天展示或点击次数，所有广告免广告一天
                    Open_Ad_Switch = false;


                    setAdFreeDay(Open_Ad_FreeTime);
                    MMKV.defaultMMKV().encode(Open_Ad_Impressions, "0");
                    MMKV.defaultMMKV().encode(Open_Ad_Clicks, "0");
                }
            }
        }
    }
    public static void refreshMainNativeAd(Context context, FrameLayout ad_frameLayout){
        //加载原生横幅广告
        if (All_Ad_Switch&&Native_main_Ad_Switch) {
            NativeAds.refreshMainNativeAd(context, new NativeAds.OnShowNativeAdCompleteListener() {
                @Override
                public void onShowNativeAdComplete() {
                    //加载成功通知更新
                    EventBus.getDefault().post(new MessageEvent("NativeAds"));
                }

                @Override
                public void onFailedToLoad() {

                }

                @Override
                public void onAdClicked() {
                    MyLog("刷新NativeAds");
                    refreshMainNativeAd(context,ad_frameLayout);
                }

                @Override
                public void onAdShow() {

                }
            });
        }else {
            MyLog("Native_main 免广告");
            ad_frameLayout.removeAllViews();
        }

    }

    public static void loadTestingAd(Context context, FrameLayout ad_frameLayout){
        if (All_Ad_Switch&&Native_Testing_Ad_Switch) {
            NativeAds.refreshTestingNativeAd(context, new NativeAds.OnShowNativeAdCompleteListener() {
                @Override
                public void onShowNativeAdComplete() {
                    //加载成功通知更新
                  //  EventBus.getDefault().post(new MessageEvent("NativeAds"));
                }

                @Override
                public void onFailedToLoad() {

                }

                @Override
                public void onAdClicked() {
                    MyLog("刷新NativeAds");
                    loadTestingAd(context,ad_frameLayout);

                }

                @Override
                public void onAdShow() {
                    loadTestingAd(context, ad_frameLayout);
                }
            });
        }else {
            MyLog("Native_Testing 免广告");
            ad_frameLayout.removeAllViews();
        }

    }
}
