package com.shentu.gamebox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shentu.gamebox.R;
import com.shentu.gamebox.bean.GridItem;
import com.shentu.gamebox.base.BaseActivity;
import com.shentu.gamebox.bean.VipBean;

import java.util.ArrayList;
import java.util.List;

public class GridItemAdapter extends BaseAdapter {

    private Context mContext;
    private int layoutResId;
    private List<VipBean> gridData;

    public GridItemAdapter(BaseActivity mActivity, int price_item, List<VipBean> vipBeans) {
        mContext = mActivity;
        layoutResId = price_item;
        gridData = vipBeans;
    }

    @Override
    public int getCount() {
        return gridData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public VipBean getItem(int position) {
        return gridData.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        mViewHolder holder;
        if (convertView == null) {
            holder = new mViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(layoutResId, parent, false);
            holder.price = convertView.findViewById(R.id.price_txt);
            holder.level = convertView.findViewById(R.id.rank_txt);

            convertView.setTag(holder);

        }else{
            holder = (mViewHolder) convertView.getTag();
        }
        VipBean vipBean = gridData.get(position);

            holder.price.setBackground(mContext.getDrawable(R.drawable.price_table_body));
            holder.level.setBackground(mContext.getDrawable(R.drawable.price_table_body));
            holder.price.setText(vipBean.getPrice());
            holder.level.setText(vipBean.getLavel());


        return convertView;

    }

    static class mViewHolder {
        TextView price;
        TextView level;
    }
}
