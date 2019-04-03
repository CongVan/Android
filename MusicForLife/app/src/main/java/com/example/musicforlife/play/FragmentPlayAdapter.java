package com.example.musicforlife.play;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class FragmentPlayAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 2;
    public FragmentPlayAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment=null;
        switch (i){
            case 0:
                fragment= FragmentListPlaying.newInstance();
                break;
            case 1:
                fragment=new FragmentPlaying();
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
}
