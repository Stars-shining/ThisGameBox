package com.shentu.gamebox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.bumptech.glide.Glide;
import com.shentu.gamebox.R;
import com.shentu.gamebox.base.BaseActivity;
import com.shentu.gamebox.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;


public class mPageAdapter extends RecyclerView.Adapter {

    private List<String> covers;
    private Context context;
    private View parentView;

    public mPageAdapter(BaseActivity mActivity, List<String> coverlist) {
        context = mActivity;
        covers = coverlist;

    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentView = LayoutInflater.from(context).inflate(R.layout.image, parent, false);

        return new mViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        mViewHolder mViewHolder = (mPageAdapter.mViewHolder) holder;
        String imgUrl = covers.get(position);

        Glide.with(context).load(imgUrl).into(((mViewHolder) holder).img);
       mViewHolder.img.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               View view = LayoutInflater.from(context).inflate(R.layout.image_dialog,null);
               DialogUtils.bigImageDialog(context,view,imgUrl);
           }
       });
    }

    @Override
    public int getItemCount() {
        return covers.size();
    }


    class mViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.game_botomImg);
        }
    }
}
