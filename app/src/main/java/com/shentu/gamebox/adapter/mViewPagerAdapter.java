package com.shentu.gamebox.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class mViewPagerAdapter extends FragmentStatePagerAdapter {


    private  ArrayList<Fragment> mfragments;

    public mViewPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<Fragment> fragments) {
        super(fm, behavior);
        mfragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mfragments.get(position);
    }

    @Override
    public int getCount() {
        return mfragments.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

}
