package net.ncie.dmv.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.ncie.dmv.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mData;
    private int doing = 0;

    public GridAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;


        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_grid, parent, false);
        }
        // 获取item的视图

        TextView textView = view.findViewById(R.id.grid_item);
        textView.setText(mData.get(position));


        if (position<doing){
            view.setBackgroundResource(R.drawable.test_true_grid);
        }

        return view;
    }

    public void setDoing(int item){
        doing = item;
    }
}