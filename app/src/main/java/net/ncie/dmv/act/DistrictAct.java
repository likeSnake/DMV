package net.ncie.dmv.act;

import static net.ncie.dmv.constant.MyAppApiConfig.SelectLeftItems;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectState;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectStateId;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tencent.mmkv.MMKV;
import net.ncie.dmv.R;
import net.ncie.dmv.adapter.StateAdapter;
import net.ncie.dmv.util.MyUtil;

import java.util.ArrayList;

public class DistrictAct extends AppCompatActivity implements View.OnClickListener{

    private StateAdapter stateAdapter;
    private RecyclerView recyclerView;
    private Button next;
    private ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_district);

        initUI();
        initListener();
        initDate();


    }

    public void initUI(){
        recyclerView = findViewById(R.id.recyclerView);
        next = findViewById(R.id.next);
    }


    public void initListener(){
        next.setOnClickListener(this);
    }
    public void initDate(){


        list =  getIntent().getStringArrayListExtra("getState");
        if (list!=null) {
            start(false, list);

            if (MMKV.defaultMMKV().decodeString(SelectStateId)!=null){
                int i = Integer.parseInt(MMKV.defaultMMKV().decodeString(SelectStateId));
                MyUtil.MyLog(i);
                stateAdapter.setSelectedPosition(i);
            }

        }else {
            MyUtil.MyLog("list为空");
        }


    }

    public void start(Boolean b, ArrayList<String> list){
        LinearLayoutManager manager = new LinearLayoutManager(DistrictAct.this, LinearLayoutManager.VERTICAL, b);
        stateAdapter = new StateAdapter(this,list);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(stateAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next:
                int selectedPosition = stateAdapter.getSelectedPosition();
                if (selectedPosition!=-1) {
                    String s = list.get(selectedPosition);
                    MMKV.defaultMMKV().encode(SelectState,s);
                    MMKV.defaultMMKV().encode(SelectStateId,String.valueOf(selectedPosition));
                    MMKV.defaultMMKV().encode(SelectLeftItems,String.valueOf(selectedPosition));
                    startActivity(new Intent(DistrictAct.this,CarTypeAct.class));
                }else {
                    MyUtil.MyToast(this,"Please select a state");
                }

        }
    }


}