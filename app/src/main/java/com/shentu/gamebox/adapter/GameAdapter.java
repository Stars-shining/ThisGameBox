package com.shentu.gamebox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shentu.gamebox.R;
import com.shentu.gamebox.ui.MainActivity;


public class GameAdapter extends RecyclerView.Adapter {

    private  MainActivity mActivity;

    public GameAdapter(MainActivity mainActivity) {
        mActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.shouye_item,parent,false);

        return new mRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class mRecyclerHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public mRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.firstpg_title);
        }

    }
}
