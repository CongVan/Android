package com.example.musicforlife.play;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class FragmentPlayAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 2;

    public FragmentPlayAdapter(FragmentManager fm) {
        super(fm);
    }

    private static Fragment mFragmentListPlaying, mFragmentPlaying;

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;

        switch (i) {
            case 0:
                mFragmentListPlaying = FragmentListPlaying.newInstance();
                fragment = mFragmentListPlaying;
                break;
            case 1:
                mFragmentPlaying = new FragmentPlaying();
                fragment = mFragmentPlaying;
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    public FragmentPlaying getFragmentPlaying(){
        return (FragmentPlaying)mFragmentPlaying;
    }
}
