package net.ncie.dmv.act;

import static net.ncie.dmv.ad.AdConst.All_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Interstitial_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Native_main_Ad_Switch;
import static net.ncie.dmv.ad.NativeAds.adViewMain;
import static net.ncie.dmv.constant.MyAppApiConfig.AllState;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectCar;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectLeftItems;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectMiddleItems;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectState;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectTestUrl;
import static net.ncie.dmv.util.AdUtils.CheckAds;
import static net.ncie.dmv.util.AdUtils.refreshMainNativeAd;
import static net.ncie.dmv.util.MyUtil.MyLog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;
import com.wang.avi.AVLoadingIndicatorView;

import net.ncie.dmv.App;
import net.ncie.dmv.R;
import net.ncie.dmv.ad.InterstitialAds;
import net.ncie.dmv.ad.NativeAds;
import net.ncie.dmv.adapter.TopicAdapter;
import net.ncie.dmv.bean.QuestionsBean;
import net.ncie.dmv.util.HttpUtils;
import net.ncie.dmv.util.MessageEvent;
import net.ncie.dmv.util.MyUtil;
import net.ncie.dmv.util.TimerUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainAct extends AppCompatActivity implements View.OnClickListener{


    private ImageView my_spinner;
    private Dialog dialog;
    private ImageView test_spinner;
    private ArrayList<QuestionsBean> list;
    private TextView title2;
    private TextView textQuestions;
    private TextView textScore;
    private TextView textGrade;
    private TextView testNum;
    private TextView cancel;
    private TextView Done;
    private TextView title_test;
    private String myEvent = "";
    private String state;
    private String car;
    private ImageView image_car;
    private ImageView left;
    private ImageView right;
    private BottomSheetDialog bottomSheetDialog;
    private RelativeLayout bottom_sheet;
    private TopicAdapter topicAdapter;
    private RecyclerView recyclerView1;
    private RelativeLayout right_top;
    private RelativeLayout left_top;
    private View bottomSheetView;
    private ArrayList<String> title_list = new ArrayList<>();
    private ArrayList<String> car_list = new ArrayList<>();
    private int RightSelectItem = 0;
    private int MiddleSelectItem = 0;
    private int LeftSelectItem = 0;
    private int selectCar;
    private ArrayList<String> state_list;
    private int SelectedPosition;
    private Button start_test;
    private TimerUtil t;
    private boolean isAds = false;
    private boolean isTests = false;
    private FrameLayout ads_main_native;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        EventBus.getDefault().register(this);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.white));

        initLoading();
        initUI();
        initAds();
        initData();
        getTests(true);
        initListener();
    }
    public void initUI(){

        my_spinner = findViewById(R.id.my_spinner);
        test_spinner = findViewById(R.id.test_spinner);
        title2 = findViewById(R.id.title2);
        textQuestions = findViewById(R.id.textQuestions);
        textScore = findViewById(R.id.textScore);
        textGrade = findViewById(R.id.textGrade);
        testNum = findViewById(R.id.testNum);
        image_car = findViewById(R.id.image_car);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        title_test = findViewById(R.id.title_test);
        right_top = findViewById(R.id.right_top);
        left_top = findViewById(R.id.left_top);
        start_test = findViewById(R.id.start_test);
        ads_main_native = findViewById(R.id.ads_main_native);
        // 加载底部弹窗的布局
        bottomSheetView = getLayoutInflater().inflate(R.layout.item_bottom, null);


        recyclerView1 = bottomSheetView.findViewById(R.id.recyclerView1);
        cancel = bottomSheetView.findViewById(R.id.cancel);
        Done = bottomSheetView.findViewById(R.id.Done);

        state = MMKV.defaultMMKV().decodeString(SelectState);
        car = MMKV.defaultMMKV().decodeString(SelectCar);


    }

    public void initListener(){

        image_car.setOnClickListener(this);
        right.setOnClickListener(this);
        left.setOnClickListener(this);
        cancel.setOnClickListener(this);
        right_top.setOnClickListener(this);
        left_top.setOnClickListener(this);
        Done.setOnClickListener(this);
        start_test.setOnClickListener(this);

    }

    public void initAds(){
        // refreshMainNativeAd(MainAct.this,ads_main_native);
        startNativeAds();
    }
    public void startNativeAds(){

        if (All_Ad_Switch&&Native_main_Ad_Switch) {
          //  ads_main_native.setBackgroundColor(Color.parseColor("#EEEFF2"));
            if (adViewMain !=null) {
                MyLog("填充横幅广告视图");
                if (adViewMain.getParent()!=null){
                    ((FrameLayout)adViewMain.getParent()).removeView(adViewMain);
                }
                ads_main_native.addView(adViewMain);
            }else {
                refreshMainNativeAd(MainAct.this,ads_main_native);
            }

        }else {
            MyLog("Native_main 免广告");
            ads_main_native.removeAllViews();
         //   ads_main_native.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateNativeAdEvent(MessageEvent event) {
        MyUtil.MyLog("EventBus接收到");
        switch (event.getMsg()){
            case "NativeAds":
                //刷新NativeAd广告
                MyUtil.MyLog("接收到更新");
                startNativeAds();
                break;
            case "NativeAds免广告":
                MyUtil.MyLog("接收到NativeAds免广告");
                ads_main_native.removeAllViews();
                break;
        }

    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.right:
                myEvent = "image_right";
                if (selectCar==2){
                    selectCar=0;

                }else {
                    selectCar++;
                }
                MMKV.defaultMMKV().encode(SelectCar,car_list.get(selectCar));

                MyLog(selectCar);
                getTests(false);
             //   updateImageCar();
                break;

            case R.id.left:

                myEvent = "image_left";
                if (selectCar==0){
                    selectCar=2;

                }else {
                    selectCar--;
                }
                MMKV.defaultMMKV().encode(SelectCar,car_list.get(selectCar));

                MyLog(selectCar);
                getTests(false);
            //    updateImageCar();
                break;

            case R.id.Done:
                SelectedPosition = topicAdapter.getSelectedPosition();
                switch (myEvent){
                    case "middle":
                       if (!car_list.get(SelectedPosition).equals(car)){
                           selectCar = SelectedPosition;
                           MMKV.defaultMMKV().encode(SelectCar,car_list.get(SelectedPosition));
                           MMKV.defaultMMKV().encode(SelectMiddleItems,String.valueOf(SelectedPosition));
                           getTests(false);
                       }
                        break;
                    case "right":
                        updateText(false);
                        break;
                    case "left":

                        startInterstitialAd(false);

                        LeftSelectItem = SelectedPosition;
                        MyLog(state+"*-*-*-*-*"+state_list.get(LeftSelectItem));
                        if (!state.equals(state_list.get(LeftSelectItem))) {
                            state = state_list.get(LeftSelectItem);
                            //selectCar = SelectedPosition
                            MMKV.defaultMMKV().encode(SelectState, state);
                            MMKV.defaultMMKV().encode(SelectLeftItems, String.valueOf(SelectedPosition));
                            getTests(false);
                        }
                }

                bottomSheetDialog.cancel();


                break;
            case R.id.cancel:
                bottomSheetDialog.cancel();
                break;
            case R.id.image_car:

                myEvent = "middle";
                topicAdapter.setList(car_list);
                topicAdapter.setSelectedPosition(MiddleSelectItem);
                bottomSheetDialog.show();
                break;
            case R.id.right_top:
                myEvent = "right";
                topicAdapter.setList(title_list);
                topicAdapter.setSelectedPosition(RightSelectItem);
                bottomSheetDialog.show();
                break;
            case R.id.left_top:
                changeLeftTop();
                break;
            case R.id.start_test:
                adViewMain = null;
                dialog.show();
                startInterstitialAd(true);
                /*Intent intent = new Intent(MainAct.this, TestingAct.class);
                intent.putExtra("SelectTestId",list.get(RightSelectItem).getTest_id());
                startActivity(intent);*/

        }
    }

    public void changeLeftTop(){
        if (!bottomSheetDialog.isShowing()) {
            myEvent = "left";
            if (state_list != null) {
                topicAdapter.setList(state_list);
                topicAdapter.setSelectedPosition(LeftSelectItem);

                bottomSheetDialog.show();

            }
        }
    }
    public void startTimer(Boolean isStartTesting){
        t = new TimerUtil(10, 1, new TimerUtil.TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //allStartAct();
                dialog.dismiss();

                if (isStartTesting){
                    startTesting();
                }
            }
        });
        t.start();
    }
    public void startInterstitialAd(Boolean isStartTesting){
        startTimer(isStartTesting);
        if (Interstitial_Ad_Switch&&All_Ad_Switch) {
            InterstitialAds.startAd(this,"Select", new App.OnShowAdCompleteListener() {
                @Override
                public void onShowAdComplete() {
                    t.cancel();
                    //广告显示
                    //  isShow = true;
                    MyLog("广告显示成功");
                    isAds = true;
                    dialogDismiss();
                    CheckAds();
                }

                @Override
                public void TurnoffAds() {
                    //关闭广告后
                    if (isStartTesting){
                        startTesting();
                    }

                }

                @Override
                public void onFailedToLoad() {
                    MyLog("开屏广告加载失败");
                    t.cancel();
                    isAds = true;
                    dialogDismiss();

                    if (isStartTesting){
                        startTesting();
                    }
                }

                @Override
                public void onAdFailedToShow() {
                    MyLog("开屏广告显示失败");
                    t.cancel();
                    isAds = true;
                    dialogDismiss();

                    if (isStartTesting){
                        startTesting();
                    }
                }

                @Override
                public void onAdLoaded() {

                }
            });
        }else {
            MyLog("插屏免广告");
            t.cancel();
            isAds = true;
            dialogDismiss();

            if (isStartTesting){

                startTesting();
            }
        }

    }

    public void startTesting(){
        if (dialog!=null){
            dialog.dismiss();
        }
        Intent intent = new Intent(MainAct.this, TestingAct.class);
        intent.putExtra("SelectTestId",list.get(RightSelectItem).getTest_id());
        startActivity(intent);

    }
    public void initData(){

        car_list.add("car");
        car_list.add("motorcycle");
        car_list.add("CDL");


        if (MMKV.defaultMMKV().decodeString(SelectMiddleItems)!=null){
            MiddleSelectItem = Integer.parseInt(MMKV.defaultMMKV().decodeString(SelectMiddleItems));
        }
        if (MMKV.defaultMMKV().decodeString(SelectLeftItems)!=null){
            LeftSelectItem = Integer.parseInt(MMKV.defaultMMKV().decodeString(SelectLeftItems));
        }

        if (MMKV.defaultMMKV().decodeString(AllState)!=null){
            String s = MMKV.defaultMMKV().decodeString(AllState);

            state_list =  new Gson().fromJson(s,ArrayList.class);
            System.out.println("-*-*-"+state_list.size());

        }

        if (MMKV.defaultMMKV().decodeString(SelectCar)!=null){
            car = MMKV.defaultMMKV().decodeString(SelectCar);

        }

        updateImageCar();


    }

    public void updateImageCar(){
        switch (car){
            case "car":
                image_car.setImageResource(R.drawable.icon_car);
                selectCar = 0;
                break;
            case "motorcycle":
                image_car.setImageResource(R.drawable.icon_motor);
                selectCar = 1;
                break;
            case "CDL":
                image_car.setImageResource(R.drawable.icon_cdl);
                selectCar = 2;
                break;

        }
    }
    public void updateText(Boolean b){
        updateImageCar();
        int itemCount = topicAdapter.getSelectedPosition();
        String s = title_list.get(itemCount);
        title_test.setText(s);
        title2.setText(s);
        switch (myEvent){
            case "middle":
                MiddleSelectItem = SelectedPosition;
                dialog.dismiss();
                break;
            case "image_right":
                dialog.dismiss();
                break;
            case "left":
                LeftSelectItem = SelectedPosition;
                break;
            case "image_left":
                MiddleSelectItem = selectCar;
                dialog.dismiss();
               // selectCar = SelectedPosition;
             //   MMKV.defaultMMKV().encode(SelectCar,car_list.get(SelectedPosition));
                MMKV.defaultMMKV().encode(SelectMiddleItems,String.valueOf(MiddleSelectItem));
                break;

        }

        RightSelectItem =itemCount;
        if (!b){
            updateTestTitle();
        }

    }

    public void updateTestTitle(){
        if (list!=null) {
            int size = list.size();
            MyLog(RightSelectItem);
            int questionNum = list.get(RightSelectItem).getQuestionNum();
            String passingScore = list.get(RightSelectItem).getPassingScore();
            int acceptableErrNum = list.get(RightSelectItem).getAcceptableErrNum();
            textQuestions.setText(String.valueOf(questionNum));
            textScore.setText(passingScore);
            textGrade.setText(String.valueOf(acceptableErrNum));
            testNum.setText(String.valueOf(size) + " tests");
        }
    }

    public void setData(){
        if (list!=null){
            int size = list.size();
            int questionNum = list.get(RightSelectItem).getQuestionNum();
            String passingScore = list.get(RightSelectItem).getPassingScore();
            int acceptableErrNum = list.get(RightSelectItem).getAcceptableErrNum();
            textQuestions.setText(String.valueOf(questionNum));
            textScore.setText(passingScore);
            textGrade.setText(String.valueOf(acceptableErrNum));
            testNum.setText(String.valueOf(size)+" tests");

            title_list.clear();
            for (QuestionsBean questionsBean : list) {
                title_list.add(questionsBean.getTitle());
            }
            if (!title_list.isEmpty()){
                initBottomSheet(title_list);
            }

            updateText(true);

        }
    }

    public void dialogDismiss(){
        if (dialog!=null){
            if (isTests&&isAds){
                dialog.dismiss();
                isTests = false;
                isAds = false;
            }
        }
    }

    public void getTests(boolean isTrue){
        dialog.show();
        car = car_list.get(selectCar);
        MyLog(car_list.get(selectCar));
        Map<String,String> map = new HashMap<>();
        map.put("type",car_list.get(selectCar));
        map.put("state",state);
        try {
            HttpUtils.sendGetRequestWithParams(SelectTestUrl, map, new HttpUtils.HttpCallback<ArrayList<QuestionsBean>>() {
                @Override
                public void onSuccess(ArrayList<QuestionsBean> result) {
                    runOnUiThread(()->{
                        if (result!=null&&result.size()!=0) {
                            System.out.println("成功：" + result.size());

                            list = result;
                            setData();

                        }else {
                            list = null;
                            MyUtil.MyToast(MainAct.this,"The question bank is empty");
                            initBottomSheet(title_list);
                            setData();
                        }
                        if (isTrue){
                            dialog.dismiss();
                        }else {
                            isTests = true;
                            dialogDismiss();
                        }
                    });


                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("GetRequestWithParams","请求失败："+e);
                    runOnUiThread(()-> {
                        //再次请求
                        getTests(false);
                   //     dialog.dismiss();
                    });
                }
            });

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    private void initBottomSheet(ArrayList<String> title_list) {
        start(false,title_list);

        // 创建底部弹窗
        if (bottomSheetDialog==null) {
            bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogStyle);
        }else {

        }

        bottomSheetDialog.setContentView(bottomSheetView);


    }
    public void start(Boolean b, ArrayList<String> list){
        LinearLayoutManager manager = new LinearLayoutManager(MainAct.this, LinearLayoutManager.VERTICAL, b);
        topicAdapter = new TopicAdapter(this,list);
        recyclerView1.setLayoutManager(manager);
        recyclerView1.setAdapter(topicAdapter);
    }

    public void initLoading(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_simple);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        AVLoadingIndicatorView id = dialog.findViewById(R.id.avi);
        id.show();


    }
}