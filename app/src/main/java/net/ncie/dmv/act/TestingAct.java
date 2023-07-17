package net.ncie.dmv.act;

import static net.ncie.dmv.ad.AdConst.All_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Interstitial_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Native_node_Ad_Switch;
import static net.ncie.dmv.ad.AdConst.Testing_Ad_Interval;
import static net.ncie.dmv.ad.NativeAds.adViewTesting;
import static net.ncie.dmv.constant.MyAppApiConfig.AllState;
import static net.ncie.dmv.constant.MyAppApiConfig.ImageUrl;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectCar;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectLeftItems;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectMiddleItems;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectQuestionsUrl;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectState;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectTestUrl;
import static net.ncie.dmv.util.AdUtils.CheckAds;
import static net.ncie.dmv.util.HttpUtils.getBitmapFromURL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;
import com.wang.avi.AVLoadingIndicatorView;

import net.ncie.dmv.App;
import net.ncie.dmv.R;
import net.ncie.dmv.ad.InterstitialAds;
import net.ncie.dmv.ad.NativeAds;
import net.ncie.dmv.adapter.GridAdapter;
import net.ncie.dmv.adapter.TestAdapter;
import net.ncie.dmv.adapter.TopicAdapter;
import net.ncie.dmv.bean.OptionsBean;
import net.ncie.dmv.bean.QuestionsBean;
import net.ncie.dmv.bean.TopicBean;
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

public class TestingAct extends AppCompatActivity implements View.OnClickListener{

    private int counts = 0;
    private String TestId;
    private TopicBean topicBean;
    private ArrayList<String> optionList = new ArrayList<>();
    private TextView test_title;
    private TextView prompts;
    private TextView current_topic;
    private TextView questions_total;
    private Dialog dialog;
    private TextView ToPass_num;
    private TextView Correct_num;
    private TextView Incorrect_num;
    private TextView Progress_test;

    private TestAdapter testAdapter;
    private RecyclerView test_recyclerview;
    private ArrayList<OptionsBean> list = new ArrayList<>();
    private ArrayList<String> grid_list = new ArrayList<>();
    private Button next_test;
    private String answer;
    private Boolean isDisable = false;
    private int currentTest = 0;
    private GridView grid;
    private GridAdapter gridAdapter;
    private LinearLayout show_grid;
    private RelativeLayout topic__relativeLayout;
    private RelativeLayout grid_relative;
    private Boolean isShowGrid = false;
    private ImageView ic_jia_image;
    private ImageView test_image;
    private int correct_count = 0;
    private int incorrect_count = 0;
    private AVLoadingIndicatorView loading;
    private RecyclerView recyclerView1;
    private View bottomSheetView;
    private TopicAdapter topicAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private ArrayList<String> title_list = new ArrayList<>();
    private ArrayList<String> car_list = new ArrayList<>();
    private String myEvent = "";
    private String state;
    private String car;
    private TextView title_test;
    private int selectCar;
    private ImageView image_car;
    private int RightSelectItem = 0;
    private int MiddleSelectItem = 0;
    private int LeftSelectItem = 0;
    private ArrayList<String> state_list;
    private int SelectedPosition;
    private ArrayList<QuestionsBean> questionsBeans;
    private RelativeLayout test_left_top;
    private RelativeLayout test_right_top;
    private TextView cancel;
    private TextView Done;
    private ImageView left;
    private ImageView right;
    private Boolean isFirst = false;
    private Button previous;
    private Button restart;
    private LinearLayout buttons;
    private FrameLayout ads_testing_native;
    private ScrollView topic_scrollView;
    private TimerUtil t;
    private boolean isAds = false;
    private boolean isTestReady = false;
    private boolean isAdReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_testing);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.white));

        initLoading();
        initUI();
        initData();
        getTopic();
        getTests();
        initListener();
        //startNativeAds();
        //initAds();
    }

    public void initUI(){

        test_title = findViewById(R.id.test_title);
        test_recyclerview = findViewById(R.id.test_recyclerview);
        next_test = findViewById(R.id.next_test);
        prompts = findViewById(R.id.prompts);
        current_topic = findViewById(R.id.current_topic);
        questions_total = findViewById(R.id.questions_total);
        grid = findViewById(R.id.grid);
        show_grid = findViewById(R.id.show_grid);
        topic__relativeLayout = findViewById(R.id.topic__relativeLayout);
        grid_relative = findViewById(R.id.grid_relative);
        ToPass_num = findViewById(R.id.ToPass_num);
        Correct_num = findViewById(R.id.Correct_num);
        Incorrect_num = findViewById(R.id.Incorrect_num);
        ic_jia_image = findViewById(R.id.ic_jia_image);
        Progress_test = findViewById(R.id.Progress_test);
        test_image = findViewById(R.id.test_image);
        loading = findViewById(R.id.loadings);
        image_car = findViewById(R.id.image_car);
        title_test = findViewById(R.id.title_test);
        test_left_top = findViewById(R.id.test_left_top);
        test_right_top = findViewById(R.id.test_right_top);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        previous = findViewById(R.id.previous);
        buttons = findViewById(R.id.buttons);
        restart = findViewById(R.id.restart);
        ads_testing_native = findViewById(R.id.ads_main_native);
        topic_scrollView = findViewById(R.id.topic_scrollView);


        bottomSheetView = getLayoutInflater().inflate(R.layout.item_bottom, null);
        recyclerView1 = bottomSheetView.findViewById(R.id.recyclerView1);
        cancel = bottomSheetView.findViewById(R.id.cancel);
        Done = bottomSheetView.findViewById(R.id.Done);

        state = MMKV.defaultMMKV().decodeString(SelectState);
        car = MMKV.defaultMMKV().decodeString(SelectCar);


        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }

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
        dialog.show();
    }
    public void initData(){

        initAds();
      //  loading.hide();
        // 加载底部弹窗的布局

        loading.show();
        loading.setVisibility(View.GONE);
        TestId = getIntent().getStringExtra("SelectTestId");

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
    public void initListener(){
        next_test.setOnClickListener(this);
        show_grid.setOnClickListener(this);
        test_right_top.setOnClickListener(this);
        test_left_top.setOnClickListener(this);
        image_car.setOnClickListener(this);
        Done.setOnClickListener(this);
        cancel.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        previous.setOnClickListener(this);
        buttons.setOnClickListener(this);
        restart.setOnClickListener(this);

        test_recyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return isDisable; // 拦截所有点击事件，使所有项不可点击
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                // 不需要实现任何代码
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                // 不需要实现任何代码
            }
        });
    }
    public void setData(){
        if (topicBean.getResult().size()==currentTest+1){
            System.out.println("最后一道题");
            next_test.setText("Complete");

        }else {
            next_test.setText("NEXT");
        }
        setNumText();
        isDisable = false;
        prompts.setVisibility(View.GONE);
        answer = topicBean.getResult().get(currentTest).getValue();
        String title = topicBean.getResult().get(currentTest).getTitle();
        String image = topicBean.getResult().get(currentTest).getImage();
        test_image.setVisibility(View.GONE);
        if (image!=null&&!image.equals("")){
            MyUtil.MyLog("图片："+ImageUrl + image);
            loading.setVisibility(View.VISIBLE);
            getBitmapFromURL(ImageUrl + image, new HttpUtils.HttpCallback<Bitmap>() {
                @Override
                public void onSuccess(Bitmap result) {
                    runOnUiThread(()->{
                        loading.setVisibility(View.GONE);
                        test_image.setImageBitmap(result);
                        test_image.setVisibility(View.VISIBLE);
                    });

                }

                @Override
                public void onFailure(Exception e) {
                    runOnUiThread(()-> {
                        loading.setVisibility(View.GONE);
                        test_image.setVisibility(View.GONE);
                        MyUtil.MyToast(TestingAct.this,e.toString());
                    });
                }
            });
        }else {
            loading.setVisibility(View.GONE);
        }
        test_title.setText(title);
        list.clear();
        grid_list.clear();
        for (TopicBean.ResultBean.OptionListBean optionListBean : topicBean.getResult().get(currentTest).getOptionList()) {
            optionList.add(optionListBean.getLabel());
            String value = optionListBean.getValue();
            String label = optionListBean.getLabel();
           list.add(new OptionsBean(label,value));
        }

        questions_total.setText(String.valueOf(topicBean.getResult().size()));
        start_grid(false,list, answer);
        for (int i = 0; i < topicBean.getResult().size(); i++) {
            grid_list.add(String.valueOf((i+1)));
        }
        if (currentTest==0){
            previous.setVisibility(View.GONE);
        }else {
            previous.setVisibility(View.VISIBLE);
        }

        current_topic.setText(String.valueOf((currentTest+1)));
        startGrid(grid_list);
        if (isAds) {
            isTestReady = true;
            dialogDismiss(false);
        }else {
            isAds = false;
        }
        switch (myEvent){
            case "middle":
            case "image_right":
            case "image_left":
                dialog.dismiss();
                break;
        }
    }

    public void setNumText(){
        int size = topicBean.getResult().size();
        ToPass_num.setText(String.valueOf(size));
        Correct_num.setText(String.valueOf(correct_count));
        Incorrect_num.setText(String.valueOf(incorrect_count));
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
            case "isBackground":

        }
    }
    public void initAds(){
        //加载原生横幅广告
        if (All_Ad_Switch&&Native_node_Ad_Switch) {
            ads_testing_native.setBackgroundColor(Color.parseColor("#EEEFF2"));
            NativeAds.refreshResultNativeAd(this, new NativeAds.OnShowNativeAdCompleteListener() {
                @Override
                public void onShowNativeAdComplete() {
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
            MyUtil.MyLog("NativeAds免广告");
            ads_testing_native.removeAllViews();
            ads_testing_native.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }


    public void startNativeAds(){

        if (All_Ad_Switch&&Native_node_Ad_Switch) {
            ads_testing_native.setBackgroundColor(Color.parseColor("#EEEFF2"));
            ads_testing_native.removeAllViews();

            if (adViewTesting.getParent()!=null){
                ((FrameLayout) adViewTesting.getParent()).removeView(adViewTesting);
            }

            if (adViewTesting !=null) {
                MyUtil.MyLog("填充横幅广告视图");
                ads_testing_native.addView(adViewTesting);

            }else {
                //加载原生横幅广告
                NativeAds.refreshResultNativeAd(this, new NativeAds.OnShowNativeAdCompleteListener() {
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

                    }
                });
            }

        }else {
            ads_testing_native.removeAllViews();
            ads_testing_native.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }

    public void getTopic(){
        dialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("id",TestId);

        try {
            HttpUtils.getExamQuestions(SelectQuestionsUrl, map, new HttpUtils.HttpCallback<TopicBean>() {
                @Override
                public void onSuccess(TopicBean bean) {
                    topicBean = bean;

                    runOnUiThread(()->{
                        System.out.println("成功："+topicBean.getResult().size());
                        correct_count = 0;
                        incorrect_count = 0;
                        setData();


                    });

                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("GetRequestWithParams","请求失败："+e);
                    runOnUiThread(()-> {
                        dialog.dismiss();
                        //再次请求
                        getTopic();
                    //    dialog.dismiss();
                    });
                }
            });

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void start_grid(Boolean b, ArrayList<OptionsBean> list, String answer){
        LinearLayoutManager manager = new LinearLayoutManager(TestingAct.this, LinearLayoutManager.VERTICAL, b);
        testAdapter = new TestAdapter(this,list,answer);
        test_recyclerview.setLayoutManager(manager);
        test_recyclerview.setAdapter(testAdapter);
    }

    public void start(Boolean b, ArrayList<String> list){
        LinearLayoutManager manager = new LinearLayoutManager(TestingAct.this, LinearLayoutManager.VERTICAL, b);
        topicAdapter = new TopicAdapter(this,list);
        recyclerView1.setLayoutManager(manager);
        recyclerView1.setAdapter(topicAdapter);
    }
    public void getTests(){
        dialog.show();
        car = car_list.get(selectCar);
        Map<String,String> map = new HashMap<>();
        map.put("type",car_list.get(selectCar));
        map.put("state",state);
        try {
            HttpUtils.sendGetRequestWithParams(SelectTestUrl, map, new HttpUtils.HttpCallback<ArrayList<QuestionsBean>>() {
                @Override
                public void onSuccess(ArrayList<QuestionsBean> result) {
                    runOnUiThread(()->{
                        System.out.println("成功："+result.get(1).toString());

                        questionsBeans = result;
                        setTestData();

                    });


                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("GetRequestWithParams","请求失败："+e);
                    runOnUiThread(()-> {
                        //MyUtil.MyToast(TestingAct.this, "请求失败");
                        //再次请求
                        getTests();
                        //dialog.dismiss();
                    });
                }
            });

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public void setTestData(){
        if (questionsBeans!=null){
            title_list.clear();
            for (QuestionsBean questionsBean : questionsBeans) {
                title_list.add(questionsBean.getTitle());
            }
            if (!title_list.isEmpty()){
                initBottomSheet(title_list);
            }

            updateText(true);
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

    public void startGrid(ArrayList<String> list){

        gridAdapter =  new GridAdapter(this,list);
        grid.setAdapter(gridAdapter);
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next_test:
                int selectedPosition = testAdapter.getSelectedPosition();
                if (selectedPosition!=-1) {
                        if (!isDisable) {
                            isDisable = true;

                            String option = list.get(selectedPosition).getOption();
                            if (option.equals(answer)) {
                                //回答正确
                                testAdapter.setWrongAnswer(false);
                                //  prompts.setVisibility(View.VISIBLE);
                                correct_count++;
                            } else {
                                //回答错误
                                prompts.setVisibility(View.GONE);
                                testAdapter.setWrongAnswer(true);
                                incorrect_count++;
                            }
                        } else {
                            if (currentTest+1 != topicBean.getResult().size()) {
                                isDisable = true;
                                currentTest++;
                                MyUtil.MyLog("Testing_Ad_Interval--"+Testing_Ad_Interval);
                                if ((currentTest+1)%Testing_Ad_Interval==0){
                                    isAds = true;
                                    startInterstitialAd(true);
                                  //  startInterstitialAds(this);
                                    initAds();
                                }

                                current_topic.setText(String.valueOf((currentTest + 1)));
                                setData();
                            }else {
                                showEnd();
                            }
                        }

                        gridAdapter.setDoing(currentTest);
                        gridAdapter.notifyDataSetChanged();

                }
                break;

            case R.id.show_grid:
                if(!isShowGrid) {
                    topic_scrollView.setVisibility(View.GONE);
                    grid_relative.setVisibility(View.VISIBLE);
                    ic_jia_image.setImageResource(R.drawable.ic_jian);
                    Progress_test.setText("Hide Progress");
                    buttons.setVisibility(View.GONE);
                    restart.setVisibility(View.VISIBLE);
                    isShowGrid = true;
                }else {
                    topic_scrollView.setVisibility(View.VISIBLE);
                    grid_relative.setVisibility(View.GONE);
                    ic_jia_image.setImageResource(R.drawable.ic_jia);
                    Progress_test.setText("View Progress");
                    buttons.setVisibility(View.VISIBLE);
                    restart.setVisibility(View.GONE);
                    isShowGrid = false;
                }
                break;
            case R.id.right:
                myEvent = "image_right";
                if (selectCar==2){
                    selectCar=0;

                }else {
                    selectCar++;
                }
                MMKV.defaultMMKV().encode(SelectCar,car_list.get(selectCar));


                getTests();
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


                getTests();
         //       updateImageCar();
                break;

            case R.id.Done:
                SelectedPosition = topicAdapter.getSelectedPosition();
                switch (myEvent){
                    case "middle":
                        if (!car_list.get(SelectedPosition).equals(car)){
                            selectCar = SelectedPosition;
                            MMKV.defaultMMKV().encode(SelectCar,car_list.get(SelectedPosition));
                            MMKV.defaultMMKV().encode(SelectMiddleItems,String.valueOf(SelectedPosition));
                            getTests();
                        }
                        break;
                    case "right":
                        updateText(false);
                        break;
                    case "left":
                        isAds = true;
                        startInterstitialAd(false);

                        state = state_list.get(LeftSelectItem);
                        //selectCar = SelectedPosition
                        MMKV.defaultMMKV().encode(SelectState,state);
                        MMKV.defaultMMKV().encode(SelectLeftItems,String.valueOf(SelectedPosition));
                        getTests();
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
            case R.id.test_right_top:
                myEvent = "right";
                topicAdapter.setList(title_list);
                topicAdapter.setSelectedPosition(RightSelectItem);
                bottomSheetDialog.show();
                break;
            case R.id.test_left_top:
                //startInterstitialAd(true);
                changeLeftTop();

                break;
            case R.id.previous:
                isDisable = true;
                currentTest--;
                current_topic.setText(String.valueOf((currentTest+1)));
                setData();
                gridAdapter.setDoing(currentTest);
                gridAdapter.notifyDataSetChanged();
                break;
            case R.id.restart:
                currentTest =0;
                current_topic.setText(String.valueOf((currentTest+1)));
                correct_count = 0;
                incorrect_count = 0;
                setData();
                gridAdapter.setDoing(currentTest);
                gridAdapter.notifyDataSetChanged();
                show_grid.performClick();
                break;
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

    public void startTimer(boolean isSwitch){
        t = new TimerUtil(10, 1, new TimerUtil.TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //allStartAct();
                isAdReady = true;
                dialogDismiss(isSwitch);
            }
        });
        t.start();
    }
    public void startInterstitialAd(boolean isSwitch){
        if (isSwitch){
            dialog.show();
        }
        startTimer(isSwitch);
        if (Interstitial_Ad_Switch&&All_Ad_Switch) {
            InterstitialAds.startAd(this, new App.OnShowAdCompleteListener() {
                @Override
                public void onShowAdComplete() {

                    t.cancel();
                    isAdReady = true;
                    dialogDismiss(isSwitch);
                    //广告显示
                    //  isShow = true;
                    MyUtil.MyLog("广告显示成功");
                    CheckAds();
                }

                @Override
                public void TurnoffAds() {
                    //关闭广告后

                }

                @Override
                public void onFailedToLoad() {
                    //广告加载失败
                    t.cancel();
                    isAdReady = true;
                    dialogDismiss(isSwitch);

                }

                @Override
                public void onAdFailedToShow() {
                    //广告显示失败
                    t.cancel();
                    isAdReady = true;
                    dialogDismiss(isSwitch);
                }

                @Override
                public void onAdLoaded() {

                }
            });
        }else {
            MyUtil.MyLog("插屏免广告");
            t.cancel();
            isAdReady = true;
            dialogDismiss(isSwitch);
        }

    }
    public void updateText(Boolean b){
        updateImageCar();
        int itemCount = topicAdapter.getSelectedPosition();
        String s = title_list.get(itemCount);
        title_test.setText(s);
        switch (myEvent){
            case "middle":
                MiddleSelectItem = SelectedPosition;
                break;
            case "image_right":
              //  dialog.dismiss();
                break;
            case "left":
                LeftSelectItem = SelectedPosition;
                break;
            case "image_left":
                MiddleSelectItem = selectCar;
             //   dialog.dismiss();
                MMKV.defaultMMKV().encode(SelectMiddleItems,String.valueOf(MiddleSelectItem));
                break;
        }

        RightSelectItem =itemCount;
        if (isFirst) {
            currentTest =0;

            TestId = questionsBeans.get(RightSelectItem).getTest_id();
            getTopic();


        }else {
            isFirst = true;
            dialog.dismiss();
        }

    }

    public void dialogDismiss(boolean isSwitch){
        if (dialog!=null){
            if ((isTestReady&&isAdReady)||isSwitch){
                dialog.dismiss();
                isTestReady = false;
                isAdReady = false;
                isAds = false;
            }
        }
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
    public void showEnd(){
        Dialog dialogs = new Dialog(this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.dialog_end);
        dialogs.getWindow().setBackgroundDrawableResource(R.color.my_colors);
        dialogs.setCancelable(false);
        dialogs.setCanceledOnTouchOutside(false);

        TextView dialog_text = dialogs.findViewById(R.id.dialog_text);
        TextView yes_count = dialogs.findViewById(R.id.yes_count);
        TextView cw_count = dialogs.findViewById(R.id.cw_count);
        ImageView ic_close = dialogs.findViewById(R.id.ic_close);
        String s = String.valueOf(topicBean.getResult().size());

        yes_count.setText(String.valueOf(correct_count));
        cw_count.setText(String.valueOf(incorrect_count));
        dialog_text.setText(s);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
            }
        });
        dialogs.findViewById(R.id.dialog_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
            }
        });
        dialogs.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MyUtil.MyLog("is onDestroy");
    }
}