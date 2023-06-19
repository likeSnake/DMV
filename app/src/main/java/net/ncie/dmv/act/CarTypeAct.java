package net.ncie.dmv.act;

import static net.ncie.dmv.constant.MyAppApiConfig.SelectCar;
import static net.ncie.dmv.constant.MyAppApiConfig.SelectMiddleItems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tencent.mmkv.MMKV;

import net.ncie.dmv.R;

public class CarTypeAct extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout car;
    private RelativeLayout motorcycle;
    private RelativeLayout CDL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_car_type);
        initUI();
        initListener();
    }

    public void initUI(){
        car = findViewById(R.id.car);
        motorcycle = findViewById(R.id.motorcycle);
        CDL = findViewById(R.id.CDL);
    }

    public void initListener(){
        car.setOnClickListener(this);
        motorcycle.setOnClickListener(this);
        CDL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car:
                MMKV.defaultMMKV().encode(SelectCar,"car");
                MMKV.defaultMMKV().encode(SelectMiddleItems,String.valueOf(0));
                startMain();
                break;
            case R.id.motorcycle:
                MMKV.defaultMMKV().encode(SelectCar,"motorcycle");
                MMKV.defaultMMKV().encode(SelectMiddleItems,String.valueOf(1));
                startMain();
                break;
            case R.id.CDL:
                MMKV.defaultMMKV().encode(SelectCar,"CDL");
                MMKV.defaultMMKV().encode(SelectMiddleItems,String.valueOf(2));
                startMain();
                break;

        }
    }
    public void startMain(){
        startActivity(new Intent(this,MainAct.class));
        finish();
    }
}