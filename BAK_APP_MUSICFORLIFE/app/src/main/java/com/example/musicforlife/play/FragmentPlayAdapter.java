package com.example.musicforlife.play;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentPlayAdapter extends PagerAdapter {
    private Context _context;
    private static final String TAG = "FragmentPlayAdapter";

    public FragmentPlayAdapter(Context context) {
        _context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        FragmentModel fragmentModel=FragmentModel.values()[position];
        LayoutInflater inflater = LayoutInflater.from(_context);
        ViewGroup layout = (ViewGroup) inflater.inflate(fragmentModel.getLayoutId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return FragmentModel.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        FragmentModel customPagerEnum = FragmentModel.values()[position];
        return customPagerEnum.getTitleName();
    }


}
