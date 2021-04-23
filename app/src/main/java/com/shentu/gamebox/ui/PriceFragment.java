package com.shentu.gamebox.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.shentu.gamebox.R;
import com.shentu.gamebox.adapter.GridItemAdapter;
import com.shentu.gamebox.bean.DetialGameBean;
import com.shentu.gamebox.bean.GridItem;
import com.shentu.gamebox.base.BaseFragment;
import com.shentu.gamebox.bean.VipBean;
import com.shentu.gamebox.utils.ListUtil;
import com.shentu.gamebox.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class PriceFragment extends BaseFragment {
    private static final String tag = "price";
    private String mTag;
    private DetialGameBean detialGameBean;
    private List<VipBean> vipBeans;
    private ListView priceListView;

    @Override
    protected int setView() {
        return R.layout.fragment_price;
    }
    public static Fragment newInstance(String param){
        PriceFragment fragment = new PriceFragment();
        Bundle args = new Bundle();
        args.putString(tag,param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!= null){
            mTag = getArguments().getString(tag);
        }
        /*注册eventbus*/
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView(View view) {

        priceListView = view.findViewById(R.id.price_gridview);



//        priceListView.requestDisallowInterceptTouchEvent(true);

    }

    @Override
    protected void initData() {

        View mView = LayoutInflater.from(mActivity).inflate(R.layout.price_item, null);
        priceListView.addHeaderView(mView,null,false);
        String vip_open = detialGameBean.getVip_open();
        if (!vip_open.isEmpty()){
            vipBeans = JSONObject.parseArray(vip_open, VipBean.class);
            if (vipBeans != null){

                GridItemAdapter gridItemAdapter = new GridItemAdapter(mActivity, R.layout.price_item, vipBeans);
                priceListView.setAdapter(gridItemAdapter);
                ListUtil.setListViewHeightBasedOnChildren(priceListView);
                gridItemAdapter.notifyDataSetChanged();

            }

        }
    }
    /*GameDetailFragment传递过来的数据*/
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getDetail(DetialGameBean detialGameBean){
        if (detialGameBean != null){
            this.detialGameBean = detialGameBean;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*解除注册*/
        EventBus.getDefault().unregister(this);
    }
}
