package net.ncie.dmv.ad;

public class  AdConst {
    //正式广告
    public static final String ADMOB_AD_Native_ID = "ca-app-pub-6544599885867352/4973746921";
    public static final String ADMOB_AD_Open_ID = "ca-app-pub-6544599885867352/1266420291";
    public static final String ADMOB_AD_Interstitial_ID = "ca-app-pub-6544599885867352/6742656992";
    //测试广告
/*    public static final String ADMOB_AD_Native_ID = "ca-app-pub-3940256099942544/2247696110";
    public static final String ADMOB_AD_Interstitial_ID = "ca-app-pub-3940256099942544/1033173712";
    public static final String ADMOB_AD_Open_ID = "ca-app-pub-3940256099942544/3419835294";*/


    public static Boolean isStartApp = false;
    public static Boolean isOpenAdLoad = false;

    //isAdShowing为true时，切后台不进入加载界面
    public static Boolean isAdShowing = false;

    public static Boolean isInitInterstitialShow = false;

    //广告展示最大次数
    public static int All_Ad_Show_Max = Integer.MAX_VALUE;
    public static int Open_Ad_Show_Max = Integer.MAX_VALUE;
    public static int Native_main_Show_Max = Integer.MAX_VALUE;
    public static int Native_Testing_Show_Max = Integer.MAX_VALUE;
    public static int Native_result_Show_Max = Integer.MAX_VALUE;
    public static int Interstitial_Show_Max = Integer.MAX_VALUE;
    public static int Testing_Ad_Interval = Integer.MAX_VALUE;
    public static int Testing_Ad_Native = Integer.MAX_VALUE;

    //广告点击最大次数
    public static int All_Clicks_Max = 10;
    public static int Open_Clicks_Max = Integer.MAX_VALUE;
    public static int Native_main_Clicks_Max = Integer.MAX_VALUE;
    public static int Native_Testing_Clicks_Max = Integer.MAX_VALUE;
    public static int Native_result_Clicks_Max = Integer.MAX_VALUE;
    public static int Interstitial_Clicks_Max = Integer.MAX_VALUE;


    //广告开关
    public static Boolean All_Ad_Switch = true;
    public static Boolean Native_main_Ad_Switch = true;
    public static Boolean Native_Testing_Ad_Switch = true;
    public static Boolean Native_result_Ad_Switch = true;
    public static Boolean Interstitial_Ad_Switch = true;
    public static Boolean Open_Ad_Switch = true;

    //免广告时间
    public static String All_Ad_FreeTime = "All_Ad_FreeTime";
    public static String Open_Ad_FreeTime = "Open_Ad_FreeTime";
    public static String Native_Ad_Main_FreeTime = "Native_Ad_Main_FreeTime";
    public static String Native_Ad_Node_FreeTime = "Native_Ad_Node_FreeTime";
    public static String Native_Ad_Result_FreeTime = "Native_Ad_Result_FreeTime";
    public static String Interstitial_Ad_FreeTime = "Interstitial_Ad_FreeTime";

    //广告展示次数以及点击次数
    public static String All_Ad_Impressions = "All_Ad_Impressions";
    public static String All_Ad_Clicks = "All_Ad_Clicks";


    public static String Open_Ad_Impressions = "Open_Ad_Impressions";
    public static String Open_Ad_Clicks = "Open_Ad_Clicks";


    public static String Native_Main_Ad_Impressions = "Native_Main_Ad_Impressions";
    public static String Native_Node_Ad_Impressions = "Native_Node_Ad_Impressions";
    public static String Native_Result_Ad_Impressions = "Native_Result_Ad_Impressions";
    public static String Native_Main_Ad_Clicks = "Native_Main_Ad_Clicks";
    public static String Native_Testing_Ad_Clicks = "Native_Main_Ad_Clicks";
    public static String Native_Result_Ad_Clicks = "Native_Main_Ad_Clicks";

    public static String Interstitial_Ad_Impressions = "Interstitial_Ad_Impressions";
    public static String Interstitial_Ad_Clicks = "Interstitial_Ad_Clicks";


    public static boolean AppState = true;

    public static String my_ip = "";
    public static String my_city = "";
    public static String my_country = "";

    public static String UploadUrl = "https://yoyofast.net/dmv/event";
    public static String loadUrl = "https://api.myip.com";
}
