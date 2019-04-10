package com.example.musicforlife.play;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.musicforlife.listsong.SongModel;


public class FragmentPlayAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 2;
    private SongModel mSongPlaying;

    public FragmentPlayAdapter(FragmentManager fm, SongModel songPlaying) {
        super(fm);
        mSongPlaying = songPlaying;
    }

    private static Fragment mFragmentListPlaying, mFragmentPlaying;

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i) {
            case 0:
                mFragmentListPlaying = FragmentListPlaying.newInstance(mSongPlaying);
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

    public FragmentPlaying getFragmentPlaying() {
        return (FragmentPlaying) mFragmentPlaying;
    }
}
