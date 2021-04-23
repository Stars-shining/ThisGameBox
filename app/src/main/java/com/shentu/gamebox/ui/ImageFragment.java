package com.shentu.gamebox.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.fastjson.JSONArray;
import com.shentu.gamebox.R;
import com.shentu.gamebox.adapter.mPageAdapter;
import com.shentu.gamebox.bean.DetialGameBean;
import com.shentu.gamebox.utils.DialogUtils;
import com.shentu.gamebox.utils.LogUtils;
import com.shentu.gamebox.view.NestedScrollableHost;
import com.shentu.gamebox.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageFragment extends BaseFragment {
    private static final String tag = "image";
    private String mTag;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<String> imglist;
    private int[] imgs;
    private boolean needIntercept = true;
    private ViewPager2 image_vp;
    private DetialGameBean detailGameBean;
    private mPageAdapter adapter;

    @Override
    protected int setView() {
        return R.layout.fragement_img;
    }

    public static Fragment newInstance(String param) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(tag, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTag = getArguments().getString(tag);
        }
        /*注册eventbus*/
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView(View view) {


        image_vp = view.findViewById(R.id.image_vp);
        NestedScrollableHost nested_host = view.findViewById(R.id.nested_host);
        nested_host.getParent().requestDisallowInterceptTouchEvent(false);
//        nested_host.getLayoutTransition().setAnimateParentHierarchy(false);


//        image_vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                if (position < imgs.length) {
//                    nested_host.getParent().requestDisallowInterceptTouchEvent(false);
//                } else {
//                    nested_host.getParent().requestDisallowInterceptTouchEvent(false);
//                }
//            }
//
//        }


    }


    @Override
    protected void initData() {
        ArrayList<String> coverlist = new ArrayList<>();
        List<String> imgsList = null;
        if (detailGameBean != null) {
//            String cover = detailGameBean.getCover();
//            coverlist.add(cover);
            String imgs = detailGameBean.getImgs();
            imgsList = JSONArray.parseArray(imgs, String.class);
            adapter = new mPageAdapter(mActivity, imgsList);

            image_vp.setAdapter(adapter);

        }else{
            LogUtils.e("detailGameBean" + detailGameBean);
        }


    }

    /*eventbus传递过来的数据*/
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getDetail(DetialGameBean detialGameBean) {
        this.detailGameBean = detialGameBean;
        LogUtils.e("detailGameBean eventBus" + detailGameBean);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        /*解除注册*/
        EventBus.getDefault().unregister(this);
    }

}
