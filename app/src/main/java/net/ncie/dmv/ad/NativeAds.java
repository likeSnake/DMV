package net.ncie.dmv.ad;

import static net.ncie.dmv.ad.AdConst.ADMOB_AD_Native_ID;
import static net.ncie.dmv.ad.AdConst.Native_Main_Ad_Clicks;
import static net.ncie.dmv.ad.AdConst.Native_Main_Ad_Impressions;
import static net.ncie.dmv.ad.AdConst.Native_Testing_Ad_Clicks;
import static net.ncie.dmv.ad.AdConst.Native_Node_Ad_Impressions;
import static net.ncie.dmv.ad.AdConst.isAdShowing;
import static net.ncie.dmv.ad.AdCount.loadMainNativeAd;
import static net.ncie.dmv.util.AdUtils.AdsClickCount;
import static net.ncie.dmv.util.AdUtils.AdsShowCount;
import static net.ncie.dmv.util.AdUtils.CheckAds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.ncie.dmv.R;
import net.ncie.dmv.bean.InfoBean;
import net.ncie.dmv.util.MyUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.tencent.mmkv.MMKV;

import java.util.Locale;
import java.util.Objects;

//原生高级广告
public class NativeAds {
    private static NativeAd nativeMainAd;
    private static NativeAd nativeNodeAd;
    private static NativeAd nativeResultAd;
    private static Boolean isMainLoad = false;
    private static Boolean isTestingLoad = false;
    public static NativeAdView adViewMain;
    public static NativeAdView adViewTesting;
    public static NativeAdView adViewNode;
    private static  long startMainTime;
    private static  long startTestingTime;
    private static Boolean isMute  = false;//广告是否静音



    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */



    //加载/刷新广告
    @SuppressLint("VisibleForTests")
    public static void refreshMainNativeAd(Context context, OnShowNativeAdCompleteListener listener) {
        if (isMainLoad) {
            return;
        }
        isMainLoad = true;

        startMainTime = System.currentTimeMillis() / 1000;
        String aid = MMKV.defaultMMKV().decodeString("aid");
        InfoBean infoBean = new InfoBean(aid,"Home Native","RequestAds",ADMOB_AD_Native_ID,"","","","","","","",context.getPackageName()
                ,"");

        loadMainNativeAd(context,infoBean);

        Activity activity  = (Activity)context;
        AdLoader.Builder builder = new AdLoader.Builder(context, ADMOB_AD_Native_ID);

        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd unifiedNativeAd) {

                        long l = (System.currentTimeMillis() / 1000) - startMainTime;

                        String aid = MMKV.defaultMMKV().decodeString("aid");
                        InfoBean infoBean = new InfoBean(aid,"Home Native","AdsLoad",ADMOB_AD_Native_ID,l +" sec - AdLoad","","", Objects.requireNonNull(unifiedNativeAd.getResponseInfo()).getResponseId(), unifiedNativeAd.getResponseInfo().toString(),"","",context.getPackageName()
                           ,""     );
                        loadMainNativeAd(context,infoBean);

                        if (nativeMainAd != null) {
                            nativeMainAd.destroy();
                        }
                        nativeMainAd = unifiedNativeAd;

                        nativeMainAd.setOnPaidEventListener(new OnPaidEventListener() {
                            @Override
                            public void onPaidEvent(@NonNull AdValue adValue) {
                                String aid = MMKV.defaultMMKV().decodeString("aid");
                                InfoBean infoBean = new InfoBean(aid,"Home Native","AdsShowValue",ADMOB_AD_Native_ID,"",adValue.getCurrencyCode(), String.valueOf(adValue.getValueMicros()),"","","","",activity.getPackageName(),""
                                        );
                                loadMainNativeAd(activity,infoBean);
                            }
                        });

                        adViewMain =
                                (NativeAdView) activity.getLayoutInflater()
                                        .inflate(R.layout.ad_test_fied, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adViewMain,true);

                        MyUtil.MyLog("nativeMainAd视图已更新");



                        if (listener!=null) {
                            listener.onShowNativeAdComplete();
                        }
                    }


                });

        //设置静音 默认静音
        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(isMute).build();

        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader =
                builder.withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                        MyUtil.MyLog("MainNativeAd 广告加载失败："+loadAdError.toString());
                                        isAdShowing = false;
                                        long l = (System.currentTimeMillis() / 1000) - startMainTime;
                                        String aid = MMKV.defaultMMKV().decodeString("aid");
                                        InfoBean infoBean = new InfoBean(aid,"Home Native","RequestAdsFailure",ADMOB_AD_Native_ID,l +" sec - "+loadAdError.toString(),"","","","","","",context.getPackageName(),"");

                                        loadMainNativeAd(context,infoBean);

                                        isMainLoad = false;
                                    }


                                    @Override
                                    public void onAdClicked() {
                                        super.onAdClicked();
                                        String aid = MMKV.defaultMMKV().decodeString("aid");
                                        InfoBean infoBean = new InfoBean(aid,"Home Native","AdsClicked",ADMOB_AD_Native_ID,"","","","","","","","","");
                                        loadMainNativeAd(context,infoBean);

                                        //点击广告统计
                                        AdsClickCount(Native_Main_Ad_Clicks);
                                        CheckAds();

                                        if (listener!=null) {
                                            listener.onAdClicked();
                                        }

                                       /* refreshAd(context, new OnShowNativeAdCompleteListener() {
                                            @Override
                                            public void onShowNativeAdComplete() {

                                            }
                                        });*/
                                       /* //点击后刷新广告
                                        refreshAd(context);*/
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        super.onAdImpression();
                                        String aid = MMKV.defaultMMKV().decodeString("aid");
                                        InfoBean infoBean = new InfoBean(aid,"Home Native","AdsShow",ADMOB_AD_Native_ID,"","","","","","","","","");

                                        loadMainNativeAd(context,infoBean);

                                        //展示广告统计
                                        AdsShowCount(Native_Main_Ad_Impressions);
                                        CheckAds();
                                    }

                                    @Override
                                    public void onAdLoaded() {
                                        super.onAdLoaded();
                                        isMainLoad = false;
                                        //加载成功
                                        MyUtil.MyLog("Native加载成功");

                                    }
                                })
                        .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }


    public static void AdTestingTest(Context context, OnShowNativeAdCompleteListener listener){
        if (isTestingLoad){
            return;
        }
        isTestingLoad = true;
        startTestingTime = System.currentTimeMillis() / 1000;

        String aid = MMKV.defaultMMKV().decodeString("aid");
        InfoBean infoBean = new InfoBean(aid,"Practice Native","RequestAds",ADMOB_AD_Native_ID,"","","","","","","","","");

        loadMainNativeAd(context,infoBean);

        Activity activity  = (Activity)context;
        AdLoader.Builder builder = new AdLoader.Builder(context, ADMOB_AD_Native_ID);

        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {

                    @Override
                    public void onNativeAdLoaded(NativeAd unifiedNativeAd) {
                        long l = (System.currentTimeMillis() / 1000) - startTestingTime;
                        InfoBean infoBean = new InfoBean(aid,"Practice Native","AdsLoad",ADMOB_AD_Native_ID,l +" sec - AdLoad","","","","","","","","");
                        loadMainNativeAd(context,infoBean);

                        if (nativeResultAd != null) {
                            nativeResultAd.destroy();
                        }
                        nativeResultAd = unifiedNativeAd;

                        nativeResultAd.setOnPaidEventListener(new OnPaidEventListener() {
                            @Override
                            public void onPaidEvent(@NonNull AdValue adValue) {
                                String aid = MMKV.defaultMMKV().decodeString("aid");
                                InfoBean infoBean = new InfoBean(aid,"Practice Native","AdsShowValue",ADMOB_AD_Native_ID,"",adValue.getCurrencyCode(), String.valueOf(adValue.getValueMicros()),"","","","",activity.getPackageName(),""
                                );
                                loadMainNativeAd(activity,infoBean);
                            }
                        });

                        MyUtil.MyLog("Testing视图已更新");

                        adViewTesting = (NativeAdView) activity.getLayoutInflater()
                                .inflate(R.layout.ad_test_fied, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adViewTesting,true);

                        if (listener!=null) {
                            listener.onShowNativeAdComplete();
                        }
                    }


                });
    }

    @SuppressLint("VisibleForTests")
    public static void refreshTestingNativeAd(Context context, OnShowNativeAdCompleteListener listener) {
        if (isTestingLoad){
            return;
        }
        isTestingLoad = true;
        startTestingTime = System.currentTimeMillis() / 1000;

        String aid = MMKV.defaultMMKV().decodeString("aid");
        InfoBean infoBean = new InfoBean(aid,"Practice Native","RequestAds",ADMOB_AD_Native_ID,"","","","","","","","","");

        loadMainNativeAd(context,infoBean);

        Activity activity  = (Activity)context;
        AdLoader.Builder builder = new AdLoader.Builder(context, ADMOB_AD_Native_ID);

        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {

                    @Override
                    public void onNativeAdLoaded(NativeAd unifiedNativeAd) {
                        long l = (System.currentTimeMillis() / 1000) - startTestingTime;
                        InfoBean infoBean = new InfoBean(aid,"Practice Native","AdsLoad",ADMOB_AD_Native_ID,l +" sec - AdLoad","","","","","","","","");
                        loadMainNativeAd(context,infoBean);

                        if (nativeResultAd != null) {
                       //     nativeResultAd.destroy();
                        }
                        nativeResultAd = unifiedNativeAd;

                        nativeResultAd.setOnPaidEventListener(new OnPaidEventListener() {
                            @Override
                            public void onPaidEvent(@NonNull AdValue adValue) {
                                String aid = MMKV.defaultMMKV().decodeString("aid");
                                InfoBean infoBean = new InfoBean(aid,"Practice Native","AdsShowValue",ADMOB_AD_Native_ID,"",adValue.getCurrencyCode(), String.valueOf(adValue.getValueMicros()),"","","","",activity.getPackageName(),""
                                );
                                loadMainNativeAd(activity,infoBean);
                            }
                        });

                        MyUtil.MyLog("Testing视图已更新");

                        adViewTesting = (NativeAdView) activity.getLayoutInflater()
                                .inflate(R.layout.ad_test_fied, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adViewTesting,true);

                        if (listener!=null) {
                            listener.onShowNativeAdComplete();
                        }
                    }


                });

        //设置静音 默认静音
        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(isMute).build();

        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader =
                builder.withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                        if (listener!=null) {
                                            listener.onFailedToLoad();
                                        }
                                        MyUtil.MyLog("ResultNativeAd广告加载失败："+loadAdError.toString());
                                        isAdShowing = false;
                                        long l = (System.currentTimeMillis() / 1000) - startTestingTime;
                                        String aid = MMKV.defaultMMKV().decodeString("aid");
                                        InfoBean infoBean = new InfoBean(aid,"Practice Native","RequestAdsFailure",ADMOB_AD_Native_ID,l +" sec - "+loadAdError.toString(),"","","","","","","","");

                                        loadMainNativeAd(context,infoBean);
                                        isTestingLoad = false;


                                    }


                                    @Override
                                    public void onAdClicked() {
                                        super.onAdClicked();
                                        //点击广告统计
                                        AdsClickCount(Native_Testing_Ad_Clicks);
                                        CheckAds();

                                        if (listener!=null) {
                                            listener.onAdClicked();
                                        }

                                        String aid = MMKV.defaultMMKV().decodeString("aid");
                                        InfoBean infoBean = new InfoBean(aid,"Practice Native","AdsClicked",ADMOB_AD_Native_ID,"","","","","","","","","");

                                        loadMainNativeAd(context,infoBean);


                                    }

                                    @Override
                                    public void onAdImpression() {
                                        super.onAdImpression();
                                        if (listener!=null) {
                                            listener.onAdShow();
                                        }

                                        String aid = MMKV.defaultMMKV().decodeString("aid");
                                        InfoBean infoBean = new InfoBean(aid,"Practice Native","AdsShow",ADMOB_AD_Native_ID,"","","","","","","","","");
                                        loadMainNativeAd(context,infoBean);
                                        //展示广告统计
                                        AdsShowCount(Native_Node_Ad_Impressions);
                                        CheckAds();


                                    }

                                    @Override
                                    public void onAdLoaded() {
                                        super.onAdLoaded();

                                        //加载成功
                                        MyUtil.MyLog("Native加载成功");
                                        isTestingLoad = false;

                                    }
                                })
                        .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }


    //加载/刷新广告
    @SuppressLint("VisibleForTests")
    public static void refreshNodeNativeAd(Context context, OnShowNativeAdCompleteListener listener) {

        Activity activity  = (Activity)context;
        AdLoader.Builder builder = new AdLoader.Builder(context, ADMOB_AD_Native_ID);

        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {

                    @Override
                    public void onNativeAdLoaded(NativeAd unifiedNativeAd) {

                        if (nativeNodeAd != null) {
                            nativeNodeAd.destroy();
                        }
                        nativeNodeAd = unifiedNativeAd;

                        adViewNode =
                                (NativeAdView) activity.getLayoutInflater()
                                        .inflate(R.layout.ad_node_fied, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adViewNode,false);
                        if (listener!=null) {
                            listener.onShowNativeAdComplete();
                        }
                    }


                });

        //设置静音 默认静音
        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(isMute).build();

        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader =
                builder.withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                        MyUtil.MyLog("ResultNativeAd广告加载失败："+loadAdError.toString());
                                        isAdShowing = false;
                                    }


                                    @Override
                                    public void onAdClicked() {
                                        super.onAdClicked();
                                        //点击广告统计
                                        AdsClickCount(Native_Main_Ad_Clicks);

                                       /* //点击后刷新广告
                                        refreshAd(context);*/
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        super.onAdImpression();
                                        //展示广告统计
                                        AdsShowCount(Native_Main_Ad_Impressions);
                                        CheckAds();
                                    }

                                    @Override
                                    public void onAdLoaded() {
                                        super.onAdLoaded();
                                        //加载成功

                                    }
                                })
                        .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }



    /**
     * Populates a {@link NativeAdView} object with data from a given
     * {@link NativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private static void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView,Boolean isIcon) {
        // Set the media view.广告图
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.广告标题
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        //广告内容/描述
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        //广告行动按钮
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        //广告图标
        if (isIcon) {
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        }

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (isIcon) {
            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        }

        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();


        //判断是否有视频广告
        if (vc.hasVideoContent()) {
            //
            System.out.println((String.format(Locale.getDefault(),
                    "Video status: Ad contains a %.2f:1 video asset.",
                    nativeAd.getMediaContent().getAspectRatio())));


            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    //视频广告播放结束后

                    //videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        } else {
           //没有视频广告
        }
    }
    public interface OnShowNativeAdCompleteListener{
        void onShowNativeAdComplete();

        void onFailedToLoad();
        void onAdClicked();

        void onAdShow();
    }
}
