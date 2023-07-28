package net.ncie.dmv.ad;

import static net.ncie.dmv.ad.AdConst.UploadUrl;
import static net.ncie.dmv.ad.AdConst.my_city;
import static net.ncie.dmv.ad.AdConst.my_country;
import static net.ncie.dmv.ad.AdConst.my_ip;
import static net.ncie.dmv.util.AesUtil.encrypt;
import static net.ncie.dmv.util.HttpUtils.sendPostRequest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import net.ncie.dmv.bean.InfoBean;
import net.ncie.dmv.util.HttpUtils;
import net.ncie.dmv.util.MyUtil;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

public class AdCount {

    public static void loadMainNativeAd(Context context, InfoBean infoBean){
                    String aid = MMKV.defaultMMKV().decodeString("aid");
                    String mtype = Build.MODEL;
                    String pVersion = Build.VERSION.RELEASE;
                    String versionCode = "";
                    try {
                        versionCode = String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
                    } catch (PackageManager.NameNotFoundException e) {
                        MyUtil.MyLog("NotFoundException-->"+e.toString());
                    }
                    infoBean.setAid(aid);
                    infoBean.setModel(mtype);
                    infoBean.setAndroidVersionCode(pVersion);
                    infoBean.setPkg(context.getPackageName());
                    infoBean.setAppVersionCode(versionCode);


                    String info = new Gson().toJson(infoBean);

                    MyUtil.MyLog("上传详情-->"+info);

                    String encrypt = encrypt(info);

                    Map<String,String> map = new HashMap<>();
                    map.put("log",encrypt);

                    String body = "log="+encrypt;

                    sendPostRequest(UploadUrl, body, new HttpUtils.HttpCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            MyUtil.MyLog("请求结果-"+result);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            MyUtil.MyLog("请求失败：-"+e);
                        }
                    });

    }


}
