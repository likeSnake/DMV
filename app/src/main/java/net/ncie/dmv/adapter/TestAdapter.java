package net.ncie.dmv.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.ncie.dmv.R;
import net.ncie.dmv.bean.OptionsBean;
import net.ncie.dmv.bean.TopicBean;

import java.util.ArrayList;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private int selectedPosition = -1;
    private Context context;
    private ArrayList<OptionsBean> list;
    private String answer;
    private Boolean isWrongAnswer = false;
    private Boolean isCheck = false;


    public TestAdapter(Context context, ArrayList<OptionsBean> configBeans,String answer) {
        this.context = context;
        this.list = configBeans;
        this.answer = answer;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView test_count;
        private TextView test_options;
        private ImageView choose;
        private RelativeLayout test_relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            test_count = itemView.findViewById(R.id.test_count);
            choose = itemView.findViewById(R.id.choose);
            test_options = itemView.findViewById(R.id.test_options);
            test_relativeLayout = itemView.findViewById(R.id.test_relativeLayout);

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tests,parent,false);
        return new ViewHolder(view);
    }
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
      //  RandomBean configBean = randomBeans.get(position);
        OptionsBean optionsBean = list.get(position);

        // 设置itemView的状态为选中或未选中
        if (selectedPosition == position) {
            holder.choose.setImageResource(R.drawable.is_selected);
           // holder.test_relativeLayout.setBackgroundResource(R.drawable.test_sharp);

        } else {
            holder.choose.setImageResource(R.drawable.ic_choose_no);
            holder.test_relativeLayout.setBackgroundResource(R.color.white);
        }

        if (isCheck) {
            if (isWrongAnswer) {
                //回答错误
                if (selectedPosition == position) {
                    holder.test_count.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.test_relativeLayout.setBackgroundResource(R.drawable.test_sharp);
                    holder.choose.setImageResource(R.drawable.ic_choose_cw);
                }
                if (getZqItem() == position) {
                    holder.choose.setImageResource(R.drawable.ic_choose_yes);
                    holder.test_relativeLayout.setBackgroundResource(R.drawable.test_true_sharp);
                }

            } else {
                //回答正确
                if (selectedPosition == position) {
                    holder.choose.setImageResource(R.drawable.ic_choose_yes);
                    holder.test_relativeLayout.setBackgroundResource(R.drawable.test_true_sharp);
                }
            }
        }


        String s = optionsBean.getContent();
        String option = optionsBean.getOption();
        holder.test_options.setText(option+".");
        holder.test_count.setText(s);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 设置选中位置为当前位置
                int previousSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setSelectedPosition(int position) {
        int previousSelectedPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousSelectedPosition);
        notifyItemChanged(selectedPosition);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setWrongAnswer(Boolean b){
        isWrongAnswer = b;
        isCheck = true;
        if (isWrongAnswer){
            notifyItemChanged(selectedPosition);
            notifyItemChanged(getZqItem());
        }else {
            notifyItemChanged(selectedPosition);
        }
    }

    public int getZqItem(){
        switch (answer){
            case "A":
                return 0;

            case "B":
                return 1;

            case "C":
                return 2;
            default:
                return 0;

        }
    }
}
