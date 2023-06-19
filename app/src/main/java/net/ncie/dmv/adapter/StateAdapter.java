package net.ncie.dmv.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import net.ncie.dmv.R;

import java.util.ArrayList;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> {

    private int selectedPosition = -1;
    private Context context;
    private ArrayList<String> states;

    public StateAdapter(Context context, ArrayList<String> configBeans) {
        this.context = context;
        this.states = configBeans;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView state;

        public ViewHolder(View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.state);


        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_state,parent,false);
        return new ViewHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
      //  RandomBean configBean = randomBeans.get(position);
        // 设置itemView的状态为选中或未选中
        if (selectedPosition == position) {
            holder.state.setBackgroundResource(R.drawable.at_sharp);
            holder.state.setTextColor(context.getColor(R.color.select_item));
        } else {
            holder.state.setBackgroundResource(R.drawable.et_sharp);
            holder.state.setTextColor(context.getColor(R.color.no_select_item));
        }

        String s = states.get(position);
        holder.state.setText(s);

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
        return states.size();
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


}
