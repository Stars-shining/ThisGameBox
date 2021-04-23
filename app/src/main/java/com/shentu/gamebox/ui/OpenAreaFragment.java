package com.shentu.gamebox.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.shentu.gamebox.R;
import com.shentu.gamebox.base.BaseActivity;
import com.shentu.gamebox.base.BaseFragment;
import com.shentu.gamebox.bean.DetialGameBean;
import com.shentu.gamebox.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OpenAreaFragment extends BaseFragment {
    private static final String tag = "open";
    private String mTag;
    private DetialGameBean detialGameBean;
    private RecyclerView openIntro;
    private TextView textView;
    private BaseActivity context;

    @Override
    protected int setView() {
        return R.layout.fragment_openarea;
    }
    public static Fragment newInstance(String param){
        OpenAreaFragment fragment = new OpenAreaFragment();
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
        context = mActivity;
        /*注册eventbus*/
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView(View view) {
        openIntro = view.findViewById(R.id.game_open);

    }

    @Override
    protected void initData() {
        String open_close = detialGameBean.getOpen_close();
        LogUtils.e("开合区介绍" + open_close);
        ArrayList<String> lists = new ArrayList<>();
        lists.add(open_close);
        /*设置布局管理器*/
        openIntro.setLayoutManager(new LinearLayoutManager(mActivity));
        openIntro.setHasFixedSize(true);
        openIntro.setNestedScrollingEnabled(false);
        openIntro.setAdapter(new myAdapter(R.layout.open_intro,lists));
    }

    /*eventbus传递过来的数据*/
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getDetail(DetialGameBean detialGameBean){
        this.detialGameBean = detialGameBean;

    }

    private  class myAdapter extends BaseQuickAdapter<String,BaseViewHolder> {


        public myAdapter(int layoutResId, @org.jetbrains.annotations.Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, String s) {
            holder.setText(R.id.open_close,s);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*解除注册*/
        EventBus.getDefault().unregister(this);
    }
}
