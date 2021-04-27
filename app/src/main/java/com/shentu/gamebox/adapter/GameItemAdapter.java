package com.shentu.gamebox.adapter;



import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.shentu.gamebox.R;
import com.shentu.gamebox.bean.HomeItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.List;


public class GameItemAdapter extends BaseQuickAdapter<HomeItem, BaseViewHolder> {

    private final int type;

    public GameItemAdapter(int LayoutResId, List<HomeItem> data, int type) {
        super(R.layout.shouye_item,data);
        this.type = type;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, HomeItem homeItem) {


        holder.setText(R.id.firstpg_title,homeItem.getName());
        holder.setText(R.id.firstpg_intro,homeItem.getIntro());
        holder.setText(R.id.firstpg_open,homeItem.getOpen_time());

        holder.setText(R.id.firstpg_detial_btn,"详情");
        holder.setBackgroundResource(R.id.firstpg_detial_btn,R.drawable.btn_shape);
//        ImageView img = holder.findView(R.id.firstpg_min);
        ImageView img = holder.itemView.findViewById(R.id.firstpg_min);

        ImageView bigImg = holder.itemView.findViewById(R.id.rec_big_cover);

        ImageView reback = holder.itemView.findViewById(R.id.reback);
        ImageView welfare = holder.itemView.findViewById(R.id.welfare);
        String gift = homeItem.getGift_detail();
        String rebate = homeItem.getRebate();

        if (null!= gift && !gift.isEmpty()&& !"0".equals(gift)){
            welfare.setVisibility(View.VISIBLE);
        }
        if (null!= rebate && !rebate.isEmpty() && !"0".equals(rebate)){
            reback.setVisibility(View.VISIBLE);
        }
        RoundedCorners roundedCorners = new RoundedCorners(10);
        RequestOptions override = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        Glide.with(getContext())
                .load(homeItem.getIcon())
                .placeholder(R.mipmap.ic_launcher)
                .apply(override)
                .dontAnimate()
                .into(img);
        String cover = homeItem.getCover();
//        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
//        params.height =0;
//        params.width =0;
//        params.bottomMargin =0;
//        params.topMargin =0;
        int itemPosition = getItemPosition(homeItem);
        if (!cover.isEmpty() && type == 2 && itemPosition ==0){

            bigImg.setVisibility(View.VISIBLE);

            Glide.with(getContext())
                    .load(homeItem.getCover())
                    .apply(override)
                    .dontAnimate()
                    .into(bigImg);
        }else {
            bigImg.setVisibility(View.GONE);
        }
//        holder.setImageResource(R.id.firstpg_min,R.mipmap.ic_launcher);
    }

    @Override
    public int getItemPosition(@Nullable HomeItem item) {
        return super.getItemPosition(item);
    }

}
